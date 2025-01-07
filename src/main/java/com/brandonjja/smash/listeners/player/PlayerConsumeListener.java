package com.brandonjja.smash.listeners.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class PlayerConsumeListener implements Listener {

    @EventHandler
    public void onEat(PlayerItemConsumeEvent event) {
        event.setCancelled(true);
    }
}
