package com.brandonjja.smash.listeners;

import org.bukkit.GameMode;
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
		smashPlayer.updateInventoryMap();
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);
		}
	}
}
