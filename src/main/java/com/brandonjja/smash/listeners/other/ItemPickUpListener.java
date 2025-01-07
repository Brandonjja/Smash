package com.brandonjja.smash.listeners.other;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.meta.ItemMeta;

import com.brandonjja.smash.Smash;
import com.brandonjja.smash.SmashCore;
import com.brandonjja.smash.game.SmashPlayer;

public class ItemPickUpListener implements Listener {

    @EventHandler
    public void onPickUp(PlayerPickupItemEvent event) {
        Item item = event.getItem();
        Player player = event.getPlayer();
        SmashPlayer smashPlayer = SmashCore.PLAYERS.get(player);
        Location itemLocation = item.getLocation();

        if (player.getInventory().contains(Material.IRON_AXE)) {
            event.setCancelled(true);
            return;
        }

        if (itemLocation.getWorld().getBlockAt(itemLocation).getType() == Material.STEP) {
            itemLocation.getWorld().getBlockAt(itemLocation).setType(Material.AIR);
        }

        ItemMeta meta;
        meta = item.getItemStack().getItemMeta();
        // HAMMER
        if (item.getItemStack().getType() == Material.IRON_AXE) {

            if (player.getInventory().contains(Material.IRON_AXE)) {
                event.setCancelled(true);
                return;
            }

            meta.setDisplayName(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Hammer");
            item.getItemStack().setItemMeta(meta);
            smashPlayer.saveInventory();
            player.getInventory().clear();
            for (int i = 0; i < 8; i++) {
                player.getInventory().addItem(item.getItemStack());
            }

            int cooldown = Bukkit.getScheduler().scheduleSyncDelayedTask(Smash.getInstance(), () -> {
                player.getInventory().clear();
                player.getInventory().setContents(smashPlayer.getInventory());
                smashPlayer.addMissingItemsAfterHammer();
                player.updateInventory();
            }, 20 * 5);
            smashPlayer.setHammerCooldown(cooldown);
        }

        // SWIFT FEATHER
        if (item.getItemStack().getType() == Material.FEATHER) {
            meta.setDisplayName(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Swift Feather");
        }

        if (item.getItemStack().getType() == Material.APPLE) {
            meta.setDisplayName(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Maxim Apple");
        }

        if (item.getItemStack().getType() == Material.GOLDEN_APPLE) {
            meta.setDisplayName(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Golden Maxim Apple");
        }

        if (item.getItemStack().getType() == Material.STICK) {
            meta.setDisplayName(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Home-Run Bat");
            item.getItemStack().setItemMeta(meta);
            event.setCancelled(true);
            event.getItem().remove();
            if (player.getInventory().containsAtLeast(item.getItemStack(), 1)) {
                player.getInventory().addItem(item.getItemStack());
            } else {
                player.getInventory().setItem(smashPlayer.getNextSlot(), item.getItemStack());
            }
            // Add 2 extra
            player.getInventory().addItem(item.getItemStack());
            return;
        }

        if (item.getItemStack().getType() == Material.TNT) {
            meta.setDisplayName(ChatColor.YELLOW + ChatColor.BOLD.toString() + "TNT");
        }

        if (item.getItemStack().getType() == Material.STONE_PLATE) {
            meta.setDisplayName(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Land Mine");
        }

        item.getItemStack().setItemMeta(meta);
        event.setCancelled(true);
        event.getItem().remove();
        if (player.getInventory().containsAtLeast(item.getItemStack(), 1)) {
            player.getInventory().addItem(item.getItemStack());
        } else {
            player.getInventory().setItem(smashPlayer.getNextSlot(), item.getItemStack());
        }
    }
}
