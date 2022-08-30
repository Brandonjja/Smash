package com.brandonjja.smash.listeners.other;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.util.Vector;

import com.brandonjja.smash.Smash;
import com.brandonjja.smash.SmashCore;
import com.brandonjja.smash.game.Game;
import com.brandonjja.smash.game.SmashPlayer;
import com.brandonjja.smash.kits.classes.Metoo;

public class PearlListener implements Listener {
	
	@EventHandler
	public void onPearl(ProjectileLaunchEvent e) {
		if (e.getEntity().getType() == EntityType.ENDER_PEARL) {
			if (!(e.getEntity().getShooter() instanceof Player)) {
				return;
			}
			Player player = (Player) e.getEntity().getShooter();
			Vector vector = player.getLocation().getDirection();
			Bukkit.getScheduler().scheduleSyncDelayedTask(Smash.getInstance(), new Runnable() {
				@Override
				public void run() {
					SmashPlayer smashPlayer = SmashCore.players.get(player);
					if (!(smashPlayer.getKit() instanceof Metoo)) {
						return;
					}
					if (!((Metoo) smashPlayer.getKit()).canTeleport()) {
						((Metoo)smashPlayer.getKit()).setTeleport(true);
						return;
					}
					EnderPearl ep = (EnderPearl) e.getEntity();
					Location loc = ep.getLocation();
					loc.setDirection(vector);
					ep.remove();
					//loc.setY(loc.getY() + 1);
					if (!loc.getWorld().getBlockAt(loc).isEmpty()) {
						loc.setY(loc.getY() + 1);
					}
					//if (Bukkit.getWorld(loc.getWorld().getName()).getbl)
					player.teleport(loc);
				}

			}, 8); // 20 Ticks * x seconds = Starts in x seconds
		}
	}
	
	@EventHandler
	public void onPearlHit(PlayerTeleportEvent e) {
		if (e.getCause() == TeleportCause.ENDER_PEARL) {
			if (Game.inGame()) {
				SmashPlayer smashPlayer = SmashCore.players.get(e.getPlayer());
				if (smashPlayer.getKit() instanceof Metoo) {
					((Metoo) smashPlayer.getKit()).setTeleport(false);
				}
			}
		}
	}
}
