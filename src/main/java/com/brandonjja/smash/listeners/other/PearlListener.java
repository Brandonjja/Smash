package com.brandonjja.smash.listeners.other;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import com.brandonjja.smash.Smash;
import com.brandonjja.smash.SmashCore;
import com.brandonjja.smash.game.Game;
import com.brandonjja.smash.game.SmashPlayer;
import com.brandonjja.smash.kits.classes.Metoo;

public class PearlListener implements Listener {

    @EventHandler
    public void onPearl(ProjectileLaunchEvent event) {
        Projectile projectileEntity = event.getEntity();
        if (projectileEntity.getType() != EntityType.ENDER_PEARL) {
            return;
        }

        ProjectileSource shooter = projectileEntity.getShooter();
        if (!(shooter instanceof Player)) {
            return;
        }

        Player player = (Player) shooter;
        Vector vector = player.getLocation().getDirection();
        Bukkit.getScheduler().scheduleSyncDelayedTask(Smash.getInstance(), () -> {
            SmashPlayer smashPlayer = SmashCore.PLAYERS.get(player);
            if (!(smashPlayer.getKit() instanceof Metoo)) {
                return;
            }

            if (!((Metoo) smashPlayer.getKit()).canTeleport()) {
                ((Metoo) smashPlayer.getKit()).setTeleport(true);
                return;
            }

            EnderPearl enderPearl = (EnderPearl) projectileEntity;
            Location pearlLocation = enderPearl.getLocation();
            pearlLocation.setDirection(vector);
            enderPearl.remove();
            if (!pearlLocation.getWorld().getBlockAt(pearlLocation).isEmpty()) {
                pearlLocation.setY(pearlLocation.getY() + 1);
            }

            player.teleport(pearlLocation);
        }, 8);
    }

    @EventHandler
    public void onPearlHit(PlayerTeleportEvent event) {
        if (event.getCause() != TeleportCause.ENDER_PEARL) {
            return;
        }

        if (!Game.inGame()) {
            return;
        }

        SmashPlayer smashPlayer = SmashCore.PLAYERS.get(event.getPlayer());
        if (smashPlayer.getKit() instanceof Metoo) {
            ((Metoo) smashPlayer.getKit()).setTeleport(false);
        }
    }
}
