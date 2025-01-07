package com.brandonjja.smash.game;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.brandonjja.smash.Smash;
import com.brandonjja.smash.SmashCore;
import com.brandonjja.smash.kits.Kit;
import com.brandonjja.smash.kits.classes.Blink;
import com.brandonjja.smash.kits.classes.Jigglyo;
import com.brandonjja.smash.kits.classes.Shadow;

public class SmashPlayer {

    private final Player player;

    private Kit kit;
    private String weapon = null;
    private boolean firstJump = true;
    public Map<Integer, ItemStack> inventorySlot;
    private int hammerCooldown;

    private Player lastPlayerToHit = null; // the player that hit you last

    private int jumpsLeft = 0;

    private ItemStack[] inventory;

    public SmashPlayer(Player player) {
        this.player = player;
        this.kit = new Blink();
        this.giveKitItems();
    }

    /**
     * Get if the player used their first double-jump
     */
    public boolean getFirstJump() {
        return firstJump;
    }

    public void setFirstJump(boolean firstJump) {
        this.firstJump = firstJump;
    }

    public Kit getKit() {
        return kit;
    }

    public void setKit(Kit kit) {
        this.kit = kit;
        this.jumpsLeft = kit.getJumps();
    }

    public Player getPlayer() {
        return player;
    }

    public void setKnockback(int knockback) {
        this.player.setLevel(knockback);
        this.player.setExp(0.99F);
    }

    public boolean hasJump() {
        return jumpsLeft > 0;
    }

    /**
     * Reduce the amount of remaining double-jumps to use
     */
    public void decrementJumps() {
        --jumpsLeft;
        if (jumpsLeft == kit.getJumps() - 1) {
            firstJump = true;
        }

        if (kit instanceof Jigglyo) {
            player.setExp(player.getExp() - (1 / (float) ((kit).getJumps() + 5)));
            firstJump = true;
            if (((Jigglyo) kit).getMiniJumps() == 0) {
                player.setExp(0F);
            }
            return;
        }

        if (jumpsLeft == 0) {
            player.setExp(0F);
        }

        player.setExp(player.getExp() - (1 / (float) kit.getJumps()));
        firstJump = true;
        if (jumpsLeft == 0) {
            player.setExp(0F);
        }
    }

    /**
     * Reset the amount of double-jumps the player is able to use, back to the kit default
     */
    public void resetJumps() {
        jumpsLeft = kit.getJumps();
        player.setExp(0.99F);
        if (kit instanceof Jigglyo) {
            ((Jigglyo) kit).resetMiniJumps();
        }
    }

    /**
     * Returns the player's remaining double-jumps they are able to use
     */
    public int getJumpsLeft() {
        return jumpsLeft;
    }

    /**
     * Returns the player who hit this player last. null if this player was not hit yet
     */
    public Player getLastHitFrom() {
        return lastPlayerToHit;
    }

    public void setLastHitFrom(Player player) {
        this.lastPlayerToHit = player;
    }

    public String getLastHitWeapon() {
        return weapon;
    }

    public void setLastHitWeapon(String weapon) {
        this.weapon = weapon;
    }

    public void saveInventory() {
        inventory = player.getInventory().getContents();
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    public void addMissingItemsAfterHammer() {
        for (ItemStack item : kit.getItems()) {
            if (!player.getInventory().contains(item.getType())) {
                item.setAmount(1);
                player.getInventory().addItem(item);
            }
        }
        player.updateInventory();
    }

    /**
     * Gives the player the items from their selected kit
     */
    public void giveKitItems() {
        if (inventorySlot == null) {
            player.getInventory().clear();
            player.getInventory().setContents(kit.getItems());
            player.getInventory().setHelmet(kit.getHelmet());
            player.updateInventory();
            updateInventoryMap();
        } else {
            getInventoryMapItems();
        }
    }

    public void updateInventoryMap() {
        inventorySlot = new HashMap<>();
        for (int i = 0; i < 36; i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item == null) {
                continue;
            }

            if (item.getType() != Material.MONSTER_EGG) {
                item.setDurability((short) 0);
            }

            if (Arrays.asList(kit.getItems()).contains(item)) {
                if (item.getType() == Material.IRON_AXE && !(kit instanceof Shadow)) {
                    return;
                }

                inventorySlot.put(i, item);
            }
        }

        for (ItemStack kitItem : kit.getItems()) {
            if (!player.getInventory().contains(kitItem)) {
                inventorySlot.put(getNextSlot(), kitItem);
            }
        }
    }

    private void getInventoryMapItems() {
        player.getInventory().clear();
        for (int i : inventorySlot.keySet()) {
            if (inventorySlot.get(i).getAmount() == 0) {
                inventorySlot.get(i).setAmount(1); // Fixes dupe bug where you could get negative pearls, that multiply
            }
            player.getInventory().setItem(i, inventorySlot.get(i));
        }

        player.getInventory().setHelmet(kit.getHelmet());
        player.updateInventory();
    }

    /**
     * Returns the next empty slot available, omitting slots where a kit item on cool-down is supposed to go
     */
    public int getNextSlot() {
        for (int i = 0; i < 36; i++) {
            if (!inventorySlot.containsKey(i) && player.getInventory().getItem(i) == null) {
                return i;
            }
        }
        return 35;
    }

    public void cancelHammerCooldown() {
        Bukkit.getScheduler().cancelTask(hammerCooldown);
        hammerCooldown = -1;
    }

    public void setHammerCooldown(int hammerCooldown) {
        this.hammerCooldown = hammerCooldown;
    }

    /**
     * Adds a cool-down to the usage of an item
     *
     * @param material the type of the item to put on cooldown
     * @param itemName the custom name of the time
     * @param time     the amount of time to set the cool-down for
     * @param slot     the slot of the item being used
     */
    public void runItemTimer(Material material, String itemName, int time, int slot) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Smash.getInstance(), () -> {
            if (player.getInventory().contains(Material.IRON_AXE)) {
                return;
            }

            ItemStack item = new ItemStack(material, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + itemName);
            item.setItemMeta(meta);

            if (getKit().canGiveItem(material) && !player.getInventory().contains(item)) {
                player.getInventory().setItem(slot, item);
            } else {
                if (!player.getInventory().contains(item)) {
                    player.getInventory().setItem(slot, item);
                }
            }

            getKit().setCanGiveItem(material, true);
        }, 20L * time);
    }

    /**
     * Sends a message to all players when a player falls into the void
     *
     * @param victim the player who fell into the void
     * @param killer the player who killed the victim. null if the victim fell on their own
     * @param weapon the weapon used by the killer, if the killer is not null
     */
    public void sendKillMessage(Player victim, Player killer, String weapon) {
        SmashPlayer smashPlayer = SmashCore.PLAYERS.get(victim);
        StringBuilder message = new StringBuilder();
        message.append(ChatColor.GOLD)
                .append(victim.getName())
                .append("(")
                .append(smashPlayer.getKit().getName())
                .append(")")
                .append(ChatColor.WHITE);

        if (killer == null) {
            message.append(" fell out");
            final String finalMessage = message.toString();
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(finalMessage);
            }
            return;
        }

        String finalMessage = message.append(" was KO'd by ")
                .append(ChatColor.GREEN)
                .append(killer.getName())
                .append("(")
                .append(SmashCore.PLAYERS.get(killer).getKit().getName())
                .append(")'s ")
                .append(ChatColor.DARK_AQUA)
                .append(this.weapon).toString();

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(finalMessage);
        }
    }
}

