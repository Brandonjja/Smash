package com.brandonjja.smash.listeners.player;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import com.brandonjja.smash.SmashCore;
import com.brandonjja.smash.game.SmashPlayer;

public class PlayerInventoryListener implements Listener {
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		SmashPlayer smashPlayer = SmashCore.players.get(event.getPlayer());
		if (event.getPlayer().getInventory().contains(Material.IRON_AXE)) {
			return;
		}
		
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
			return;
		}
		
		smashPlayer.updateInventoryMap();
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
			event.setCancelled(true);
		}
	}
}
