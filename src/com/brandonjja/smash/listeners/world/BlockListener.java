package com.brandonjja.smash.listeners.world;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemDespawnEvent;

import com.brandonjja.smash.game.Game;

public class BlockListener implements Listener {
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE && e.getPlayer().isOp()) {
			return;
		} else {
			e.setCancelled(true);
		}
		if (Game.inGame()) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE && e.getPlayer().isOp()) {
			return;
		} else {
			e.setCancelled(true);
		}
		if (Game.inGame()) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDespawn(ItemDespawnEvent e) {
		e.setCancelled(true);
	}
	
	
	
	/*@EventHandler
	public void onHit(ProjectileHitEvent e) {
		if (e.getEntity() instanceof EnderPearl) {
			if (e.getEntity().getShooter() instanceof Player) {
				Player player = (Player) e.getEntity().getShooter();
				Location playerLoc = player.getLocation();
				Location pearlLoc = e.getEntity().getLocation();
				
				Location newLoc = pearlLoc.subtract(playerLoc);
				player.sendMessage(newLoc.toString());
				
				
			}
		}
	}*/
}
