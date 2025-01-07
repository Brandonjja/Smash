package com.brandonjja.smash.listeners.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.brandonjja.smash.Smash;
import com.brandonjja.smash.SmashCore;
import com.brandonjja.smash.game.Game;
import com.brandonjja.smash.game.ScoreboardManager;
import com.brandonjja.smash.game.SmashPlayer;
import com.brandonjja.smash.kits.classes.Blink;
import com.brandonjja.smash.kits.classes.Metoo;
import com.brandonjja.smash.kits.classes.Pika;
import com.brandonjja.smash.kits.classes.Shadow;
import com.brandonjja.smash.kits.classes.Toshi;

public class PlayerDamageListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        player.setHealth(20);

        SmashPlayer smashPlayer = SmashCore.PLAYERS.get(player);

        if (event.getCause() == DamageCause.FALL) {
            smashPlayer.resetJumps();
            event.setCancelled(true);
            if (Game.inGame() && smashPlayer.getKit() instanceof Toshi) {
                Toshi kit = (Toshi) smashPlayer.getKit();
                if (kit.usedPound()) {
                    kit.setUsedPound(false);
                    kit.usePound(player);
                }
            }
        }

        if (event.getCause() == DamageCause.BLOCK_EXPLOSION) {
            event.setDamage(0);
            player.setVelocity(player.getLocation().getDirection().setY(0.05).multiply(player.getLevel()));
            player.setLevel(player.getLevel() + 5);
            ScoreboardManager.updateKB(player);
            return;
        }

        if (!Game.inGame()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!Game.inGame()) {
            event.setCancelled(true);
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player victim = (Player) event.getEntity();

        if (event.getDamager() instanceof LightningStrike) {
            SmashPlayer smashVictim = SmashCore.PLAYERS.get(victim);
            LightningStrike lightning = (LightningStrike) event.getDamager();
            if (smashVictim.getKit() instanceof Pika) {
                Pika pika = (Pika) smashVictim.getKit();
                if (pika.getLightning(lightning)) {
                    event.setCancelled(true);
                    return;
                }
            }

            if (smashVictim.getKit() instanceof Shadow) {
                Shadow shadow = (Shadow) smashVictim.getKit();
                if (shadow.getLightning(lightning)) {
                    event.setCancelled(true);
                    return;
                }
            }

            victim.setVelocity(victim.getLocation().getDirection().setY(0.01).normalize().multiply(-((victim.getLevel() / 10) + 2)));
            victim.setLevel(victim.getLevel() + 2);
            event.setDamage(0);
            ScoreboardManager.updateKB(victim);

            SmashPlayer smashPlayer;
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                smashPlayer = SmashCore.PLAYERS.get(onlinePlayer);
                if (smashPlayer.getKit() instanceof Pika) {
                    if (((Pika) smashPlayer.getKit()).getLightning(lightning)) {
                        smashVictim.setLastHitFrom(onlinePlayer);
                        smashVictim.setLastHitWeapon("Thunder Jolt");
                    }
                }
            }
        }

        if (event.getDamager() instanceof Egg) {
            Egg egg = (Egg) event.getDamager();
            if (egg.getShooter() instanceof Player) {
                SmashPlayer smashVictim = SmashCore.PLAYERS.get(victim);
                Player player = (Player) egg.getShooter();

                if (victim.equals(player)) {
                    event.setCancelled(true);
                    return;
                }

                victim.setVelocity(player.getLocation().getDirection().setY(0).normalize().multiply(victim.getLevel() / 10));
                victim.setLevel(victim.getLevel() + 5);
                ScoreboardManager.updateKB(victim);
                smashVictim.setLastHitFrom(player);
                smashVictim.setLastHitWeapon("Toshi Egg");

                Location loc = new Location(victim.getWorld(), victim.getLocation().getX(), victim.getLocation().getY() + 1, victim.getLocation().getZ());

                Firework firework = victim.getWorld().spawn(loc, Firework.class);
                FireworkMeta meta = firework.getFireworkMeta();
                meta.addEffect(FireworkEffect.builder().flicker(false).trail(false).with(Type.BALL).withColor(Color.GREEN).build());
                meta.setPower(0);
                firework.setFireworkMeta(meta);

                new BukkitRunnable() {
                    public void run() {
                        firework.detonate();
                    }
                }.runTaskLater(Smash.getInstance(), 1);

                event.setDamage(0);
            }
        }

        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player) {
                Player player = (Player) arrow.getShooter();
                if (victim.equals(player)) {
                    event.setCancelled(true);
                    return;
                }

                victim.setVelocity(player.getLocation().getDirection().setY(0).normalize().multiply(victim.getLevel() / 10));
                victim.setLevel(victim.getLevel() + (int) event.getDamage());
                ScoreboardManager.updateKB(victim);
                event.setDamage(0);

                SmashPlayer smashVictim = SmashCore.PLAYERS.get(victim);
                smashVictim.setLastHitFrom(player);
                smashVictim.setLastHitWeapon("Bow");

                return;
            }

            victim.setVelocity(arrow.getLocation().getDirection().setY(0).normalize().multiply(victim.getLevel() / 10));
            victim.setLevel(victim.getLevel() + (int) event.getDamage());
            ScoreboardManager.updateKB(victim);
            event.setDamage(0);

            try {
                Player player = (Player) arrow.getShooter();

                SmashPlayer smashVictim = SmashCore.PLAYERS.get(victim);

                smashVictim.setLastHitFrom(player);
                smashVictim.setLastHitWeapon(arrow.getName());
            } catch (ClassCastException ignored) {
            }

            return;
        }

        if (event.getDamager() instanceof Snowball) {
            Snowball ball = (Snowball) event.getDamager();
            Player player = (Player) ball.getShooter();
            if (victim.equals(player)) {
                event.setCancelled(true);
                return;
            }

            SmashPlayer smashPlayerShooter = SmashCore.PLAYERS.get(player);
            SmashPlayer smashVictim = SmashCore.PLAYERS.get(victim);

            smashVictim.setLastHitFrom(player);

            if (smashPlayerShooter.getKit() instanceof Blink) {
                Location loc = player.getLocation();
                player.teleport(victim.getLocation());
                victim.teleport(loc);
                player.sendMessage(ChatColor.GREEN + "You switched with " + victim.getName() + "!");
                victim.sendMessage(ChatColor.RED + "You got switched with " + player.getName() + "!");

                smashVictim.setLastHitWeapon("Switcher");
            }

            if (smashPlayerShooter.getKit() instanceof Shadow) {
                Location loc = player.getLocation();
                player.teleport(victim.getLocation());
                victim.teleport(loc);
                player.sendMessage(ChatColor.GREEN + "You switched with " + victim.getName() + "!");
                victim.sendMessage(ChatColor.RED + "You got switched with " + player.getName() + "!");

                smashVictim.setLastHitWeapon("Switcher");
            }

            if (smashPlayerShooter.getKit() instanceof Metoo) {
                victim.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 8, 1));
                victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 2, 1));
                victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 8, 1));
                victim.sendMessage(ChatColor.RED + "You were hit by " + player.getName() + "'s" + ChatColor.DARK_AQUA + " Psychic Orb" + ChatColor.RED + "!");
                player.sendMessage(ChatColor.GREEN + "You hit " + victim.getName() + " with your " + ChatColor.DARK_AQUA + "Psychic Orb" + ChatColor.GREEN + "!");

                smashVictim.setLastHitWeapon("Psychic Orb");


                Location loc = new Location(victim.getWorld(), victim.getLocation().getX(), victim.getLocation().getY() + 1, victim.getLocation().getZ());

                Firework firework = victim.getWorld().spawn(loc, Firework.class);
                FireworkMeta meta = firework.getFireworkMeta();
                meta.addEffect(FireworkEffect.builder().flicker(false).trail(false).with(Type.BALL).withColor(Color.PURPLE).build());
                meta.setPower(0);
                firework.setFireworkMeta(meta);

                new BukkitRunnable() {
                    public void run() {
                        firework.detonate();
                    }
                }.runTaskLater(Smash.getInstance(), 1);

            }
        }

        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player attacker = (Player) event.getDamager();

        victim.setLevel(victim.getLevel() + (int) event.getDamage());
        ScoreboardManager.updateKB(victim);

        SmashPlayer smashVictim = SmashCore.PLAYERS.get(victim);
        smashVictim.setLastHitFrom(attacker);
        try {
            String[] arr = attacker.getItemInHand().getItemMeta().getDisplayName().split("");
            StringBuilder name = new StringBuilder();
            for (int i = 4; i < arr.length; i++) {
                name.append(arr[i]);
            }
            smashVictim.setLastHitWeapon(name.toString());
        } catch (NullPointerException ex) {
            smashVictim.setLastHitWeapon("Fist");
        }

        if (attacker.getItemInHand().getType() == Material.IRON_AXE) {
            victim.setVelocity(attacker.getLocation().getDirection().setY(0).normalize().multiply(((victim.getLevel() / 10) + 5) * 2));
            event.setDamage(0);
            return;
        }

        if (attacker.getItemInHand().getType() == Material.STICK) {
            victim.setVelocity(attacker.getLocation().getDirection().setY(0).normalize().multiply(((victim.getLevel() / 10.0) + 2) * 1.25));
            event.setDamage(0);
            ItemStack bat = attacker.getInventory().getItemInHand();
            bat.setAmount(bat.getAmount() - 1);
            attacker.getInventory().setItemInHand(bat);
            return;
        }

        victim.setVelocity(attacker.getLocation().getDirection().setY(0).normalize().multiply(victim.getLevel() / 10));
        event.setDamage(0);
    }
}
