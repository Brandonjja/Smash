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
	public void onPlace(BlockPlaceEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE && event.getPlayer().isOp()) {
			return;
		} else {
			event.setCancelled(true);
		}

		if (Game.inGame()) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE && event.getPlayer().isOp()) {
			return;
		} else {
			event.setCancelled(true);
		}

		if (Game.inGame()) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDespawn(ItemDespawnEvent event) {
		event.setCancelled(true);
	}

}
