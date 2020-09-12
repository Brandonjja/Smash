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
	public void onInventoryClose(InventoryCloseEvent e) {
		SmashPlayer smashPlayer = SmashCore.players.get(e.getPlayer());
		if (e.getPlayer().getInventory().contains(Material.IRON_AXE)) {
			return;
		}
		
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
			return;
		}
		
		smashPlayer.updateInventoryMap();
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);
		}
	}
}
