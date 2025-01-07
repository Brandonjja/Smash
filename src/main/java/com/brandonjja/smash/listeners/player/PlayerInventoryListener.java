package com.brandonjja.smash.listeners.player;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import com.brandonjja.smash.SmashCore;
import com.brandonjja.smash.game.SmashPlayer;

public class PlayerInventoryListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        SmashPlayer smashPlayer = SmashCore.PLAYERS.get(player);
        if (player.getInventory().contains(Material.IRON_AXE)) {
            return;
        }

        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        smashPlayer.updateInventoryMap();
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }
}
