package com.brandonjja.smash.listeners.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import com.brandonjja.smash.Smash;
import com.brandonjja.smash.SmashCore;
import com.brandonjja.smash.game.Game;
import com.brandonjja.smash.game.ScoreboardManager;
import com.brandonjja.smash.game.SmashPlayer;
import com.brandonjja.smash.kits.classes.Jigglyo;
import com.brandonjja.smash.kits.classes.Pika;
import com.brandonjja.smash.kits.classes.Shadow;
import com.brandonjja.smash.kits.classes.Toshi;

public class PlayerMoveListener implements Listener {

    private static final double JUMP_VELOCITY = 0.42F;

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        SmashPlayer smashPlayer = SmashCore.PLAYERS.get(player);
        if (event.isSneaking() && smashPlayer.getKit() instanceof Jigglyo && player.isOnGround()) {
            Jigglyo kit = (Jigglyo) smashPlayer.getKit();
            int time = Bukkit.getScheduler().scheduleSyncRepeatingTask(Smash.getInstance(), () -> {
                if (player.getLevel() >= 2) {
                    if (!player.isOnGround()) {
                        ((Jigglyo) smashPlayer.getKit()).cancelSneakTimer();
                    } else {
                        player.setLevel(player.getLevel() - 1);
                        ScoreboardManager.updateKB(player);
                    }
                } else {
                    player.setLevel(1);
                }
            }, 10L, 10L);
            kit.setSneakTimer(time);
        } else if (!event.isSneaking() && smashPlayer.getKit() instanceof Jigglyo) {
            ((Jigglyo) smashPlayer.getKit()).cancelSneakTimer();
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        SmashPlayer smashPlayer = SmashCore.PLAYERS.get(player);

        player.setHealth(20);
        player.setFoodLevel(20);

        if (player.getLocation().getY() < 2) {
            player.teleport(player.getWorld().getSpawnLocation());
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }

            smashPlayer.giveKitItems();
            smashPlayer.resetJumps();
            smashPlayer.setKnockback(1);

            ScoreboardManager.updateKB(player);
            ScoreboardManager.updateScore(smashPlayer.getLastHitFrom());

            smashPlayer.sendKillMessage(player, smashPlayer.getLastHitFrom(), smashPlayer.getLastHitWeapon());
            smashPlayer.setLastHitFrom(null);
            smashPlayer.setLastHitWeapon(null);

            smashPlayer.getKit().setCanGiveItem(null, false);

            if (Game.isGameOver()) {
                return;
            }

            smashPlayer.cancelHammerCooldown();

            Game.checkEndGame();
        }

        if (smashPlayer.getKit() instanceof Toshi) {
            Toshi kit = (Toshi) smashPlayer.getKit();
            if (kit.usedPound()) {
                if (player.isOnGround()) {
                    kit.setUsedPound(false);
                    kit.usePound(player);
                }
            }
        }

        Vector velocity = player.getVelocity();
        if (player.getLocation().getBlock().getType() != Material.LADDER && Double.compare(velocity.getY(), JUMP_VELOCITY) == 0) {
            smashPlayer.resetJumps();
            if (smashPlayer.getKit() instanceof Pika) {
                Pika pika = (Pika) smashPlayer.getKit();
                pika.setLunge(true);
            }

            if (smashPlayer.getKit() instanceof Shadow) {
                Shadow shadow = (Shadow) smashPlayer.getKit();
                shadow.setLunge(true);
            }
            return;
        }

        Location fromLocation = event.getFrom();
        Location toLocation = event.getTo();
        if (fromLocation.getY() == toLocation.getY()) {
            Location loc = new Location(toLocation.getWorld(), toLocation.getBlockX(), toLocation.getBlockY(), toLocation.getBlockZ());
            if (player.getWorld().getBlockAt(loc.subtract(0, 1, 0)).getType() != Material.AIR) {
                if (smashPlayer.getKit() instanceof Pika) {
                    Pika pika = (Pika) smashPlayer.getKit();
                    pika.setLunge(true);
                }

                if (smashPlayer.getKit() instanceof Shadow) {
                    ((Shadow) smashPlayer.getKit()).setLunge(true);
                }

                if (smashPlayer.getFirstJump()) {
                    smashPlayer.setFirstJump(false);
                    return;
                }

                smashPlayer.resetJumps();
            }
        }
    }
}
