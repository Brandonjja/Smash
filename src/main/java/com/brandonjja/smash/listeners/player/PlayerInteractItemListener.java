package com.brandonjja.smash.listeners.player;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Egg;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.brandonjja.smash.Smash;
import com.brandonjja.smash.SmashCore;
import com.brandonjja.smash.game.Game;
import com.brandonjja.smash.game.ScoreboardManager;
import com.brandonjja.smash.game.SmashPlayer;
import com.brandonjja.smash.kits.classes.Blink;
import com.brandonjja.smash.kits.classes.Jigglyo;
import com.brandonjja.smash.kits.classes.Metoo;
import com.brandonjja.smash.kits.classes.Pika;
import com.brandonjja.smash.kits.classes.Shadow;
import com.brandonjja.smash.kits.classes.Toshi;

public class PlayerInteractItemListener implements Listener {

    @EventHandler
    public void onItemUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        SmashPlayer smashPlayer = SmashCore.PLAYERS.get(player);

        Material item = player.getItemInHand().getType();

        if (event.getAction().equals(Action.PHYSICAL) && event.getClickedBlock().getType() == Material.STONE_PLATE) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Smash.getInstance(), () -> {
                event.getClickedBlock().setType(Material.AIR);
                event.getClickedBlock().getState().update();
            }, 2);

            TNTPrimed tnt = player.getWorld().spawn(player.getLocation(), TNTPrimed.class);
            tnt.setFuseTicks(1);
            return;
        }

        if (item == Material.STONE_PLATE) {
            FallingBlock landMine = player.getLocation().getWorld().spawnFallingBlock(player.getLocation(), Material.STONE_PLATE, (byte) 0);
            Vector vec = player.getLocation().getDirection();
            landMine.setVelocity(vec.multiply(2));

            ItemStack landMineItem = player.getInventory().getItemInHand();
            landMineItem.setAmount(landMineItem.getAmount() - 1);
            player.getInventory().setItemInHand(landMineItem);
            return;
        }

        // Double-Jump
        if (item == Material.FIREWORK) {
            event.setCancelled(true);

            if (inNormalParkourArea(player)) {
                return;
            }

            if (!smashPlayer.hasJump()) {
                smashPlayer.setFirstJump(false);

                if (smashPlayer.getKit() instanceof Jigglyo) {
                    if (((Jigglyo) smashPlayer.getKit()).getMiniJumps() >= 1) {
                        ((Jigglyo) smashPlayer.getKit()).decrementMiniJumps();
                        smashPlayer.decrementJumps();
                        Vector vec = player.getEyeLocation().getDirection();
                        vec.multiply(0.45);
                        vec.setY(0.7);
                        player.setVelocity(vec);
                    }
                }
                return;
            }

            smashPlayer.decrementJumps();
            smashPlayer.setFirstJump(true);
            Vector vec = player.getEyeLocation().getDirection();
            vec.multiply(0.8);
            vec.setY(1);
            player.setVelocity(vec);
        }

        // Pika Lunge
        if (item == Material.GOLDEN_CARROT) {
            if (inNormalParkourArea(player)) {
                return;
            }

            if (smashPlayer.getKit() instanceof Pika) {
                Pika pika = (Pika) smashPlayer.getKit();
                if (pika.canLunge()) {
                    pika.setLunge(false);
                    Vector vector = player.getEyeLocation().getDirection();
                    vector.multiply(1.4F);
                    vector.setY(0.4);
                    player.setVelocity(vector);
                }
            } else if (smashPlayer.getKit() instanceof Shadow) {
                Shadow shadow = (Shadow) smashPlayer.getKit();
                if (shadow.canLunge()) {
                    shadow.setLunge(false);
                    Vector vector = player.getEyeLocation().getDirection();
                    vector.multiply(1.4F);
                    vector.setY(0.4);
                    player.setVelocity(vector);
                }
            }
        }

        if (!Game.inGame()) {
            return;
        }

        // Pika Thunder Jolt
        if (item == Material.WOOD_AXE || item == Material.STONE_AXE) {
            if (smashPlayer.getKit() instanceof Pika) {
                if (!((Pika) smashPlayer.getKit()).canStrike()) {
                    return;
                }

                LightningStrike lightning = player.getWorld().strikeLightning(player.getTargetBlock((Set<Material>) null, 4).getLocation());
                ((Pika) smashPlayer.getKit()).setLightning(lightning);
                ((Pika) smashPlayer.getKit()).setStrike(false);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Smash.getInstance(), () -> {
                    ((Pika) smashPlayer.getKit()).setStrike(true);
                    player.sendMessage(ChatColor.GREEN + "You can now use your " + ChatColor.RED + ChatColor.BOLD + "Thunder Jolt" + ChatColor.GREEN + " again!");
                }, 20 * 15);
            } else if (smashPlayer.getKit() instanceof Shadow) {
                if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    return;
                }

                if (!((Shadow) smashPlayer.getKit()).canStrike()) {
                    return;
                }

                LightningStrike lightning = player.getWorld().strikeLightning(player.getTargetBlock((Set<Material>) null, 4).getLocation());
                ((Shadow) smashPlayer.getKit()).setLightning(lightning);
                ((Shadow) smashPlayer.getKit()).setStrike(false);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Smash.getInstance(), () -> {
                    ((Shadow) smashPlayer.getKit()).setStrike(true);
                    player.sendMessage(ChatColor.GREEN + "You can now use your " + ChatColor.RED + ChatColor.BOLD + "Thunder Jolt" + ChatColor.GREEN + " again!");
                }, 20 * 25);
            }
        }

        // Metoo
        if (item == Material.ENDER_PEARL) {
            smashPlayer.runItemTimer(item, "Teleporter", 8, player.getInventory().getHeldItemSlot());
        }

        if (item == Material.SNOW_BALL) {
            if (smashPlayer.getKit() instanceof Blink) {
                smashPlayer.runItemTimer(item, "Switcher", 8, player.getInventory().getHeldItemSlot());
            } else if (smashPlayer.getKit() instanceof Metoo) {
                smashPlayer.runItemTimer(item, "Psychic Orb", 14, player.getInventory().getHeldItemSlot());
            } else if (smashPlayer.getKit() instanceof Shadow) {
                smashPlayer.runItemTimer(item, "Switcher", 16, player.getInventory().getHeldItemSlot());
            }
        }

        if (item == Material.CLAY_BRICK && smashPlayer.getKit() instanceof Toshi) {
            ItemStack brick = player.getInventory().getItemInHand();
            brick.setAmount(brick.getAmount() - 1);
            player.getInventory().setItemInHand(brick);

            Vector vec = new Vector(0, -2, 0);
            vec.multiply(0.8);
            player.setVelocity(vec);
            ((Toshi) smashPlayer.getKit()).setUsedPound(true);

            smashPlayer.runItemTimer(item, "Ground Pound", 15, player.getInventory().getHeldItemSlot());
        }

        if (item == Material.MONSTER_EGG && smashPlayer.getKit() instanceof Toshi) {
            player.launchProjectile(Egg.class);
            player.getInventory().remove(Material.MONSTER_EGG);

            int slot = player.getInventory().getHeldItemSlot();

            Bukkit.getScheduler().scheduleSyncDelayedTask(Smash.getInstance(), () -> {
                ItemStack egg = new ItemStack(Material.MONSTER_EGG, 1, (short) 50);
                ItemMeta meta = egg.getItemMeta();
                meta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "Toshi Egg");
                egg.setItemMeta(meta);
                if (smashPlayer.getKit().canGiveItem(item)) {
                    player.getInventory().setItem(slot, egg);
                } else {
                    if (!player.getInventory().contains(egg)) {
                        player.getInventory().setItem(slot, egg);
                    }
                }
                smashPlayer.getKit().setCanGiveItem(item, true);
            }, 20 * 8);
        }

        // BELOW IS THE CODE FOR ITEMS YOU PICK UP AROUND THE MAP

        if (item == Material.TNT) {
            TNTPrimed tnt = player.getWorld().spawn(player.getLocation(), TNTPrimed.class);
            Vector vec = player.getLocation().getDirection();
            tnt.setVelocity(vec.multiply(2));
            tnt.setFuseTicks(20 * 2);
            ItemStack handTnt = player.getInventory().getItemInHand();
            handTnt.setAmount(handTnt.getAmount() - 1);
            player.getInventory().setItemInHand(handTnt);
        }

        if (item == Material.FEATHER) {
            ItemStack feather = player.getInventory().getItemInHand();
            feather.setAmount(feather.getAmount() - 1);
            player.getInventory().setItemInHand(feather);

            if (player.hasPotionEffect(PotionEffectType.JUMP) && player.hasPotionEffect(PotionEffectType.SPEED)) {
                player.removePotionEffect(PotionEffectType.JUMP);
                player.removePotionEffect(PotionEffectType.SPEED);
            }

            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 10, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 10, 2));
        }

        if (item == Material.APPLE) {
            event.setCancelled(true);
            ItemStack apple = player.getInventory().getItemInHand();
            if (apple.getAmount() <= 1) {
                player.getInventory().remove(apple);
                player.updateInventory();
                // Should be 80
                player.setLevel(Math.max(player.getLevel() - 50, 1));
                ScoreboardManager.updateKB(player);
                return;
            }

            apple.setAmount(apple.getAmount() - 1);
            player.getInventory().setItemInHand(apple);
            player.updateInventory();
            // Should be 80
            player.setLevel(Math.max(player.getLevel() - 50, 1));
            ScoreboardManager.updateKB(player);
            return;
        }

        if (item == Material.GOLDEN_APPLE) {
            event.setCancelled(true);
            ItemStack apple = player.getInventory().getItemInHand();
            apple.setAmount(apple.getAmount() - 1);
            player.getInventory().setItemInHand(apple);
            player.setLevel(1);
            ScoreboardManager.updateKB(player);
        }
    }

    private boolean inNormalParkourArea(Player player) {
        if (!player.getWorld().getName().equalsIgnoreCase("lobby2")) {
            return false;
        }

        Location playerLocation = player.getLocation();

        if ((playerLocation.getY() >= 19 && playerLocation.getY() <= 26) && (playerLocation.getX() < 1156 && playerLocation.getX() > 1152) && (playerLocation.getZ() <= -583.5 && playerLocation.getZ() >= -588)) {
            return false;
        }

        if (playerLocation.getZ() > -588 && playerLocation.getZ() < -544) {
            player.sendMessage(ChatColor.LIGHT_PURPLE + "[Smash] " + ChatColor.RED + "Double-Jumps are disabled in this area");
            return true;
        }

        return false;
    }
}
