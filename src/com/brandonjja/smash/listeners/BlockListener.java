package com.brandonjja.smash.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.util.Vector;

import com.brandonjja.smash.Smash;
import com.brandonjja.smash.game.Game;

public class BlockListener implements Listener {
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
			return;
		}
		if (Game.inGame()) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDespawn(ItemDespawnEvent e) {
		e.setCancelled(true);
	}
	
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
					EnderPearl ep = (EnderPearl) e.getEntity();
					Location loc = ep.getLocation();
					loc.setDirection(vector);
					ep.remove();
					
					player.teleport(loc);
					
				}

			}, 8); // 20 Ticks * x seconds = Starts in x seconds
		}
	}
}
