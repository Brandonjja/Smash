package com.brandonjja.smash.listeners.other;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.List;

public class SignClickListener implements Listener {

    private static final List<String> KITS = Arrays.asList("blink", "metoo", "pika", "toshi", "jigglyo", "shadow");

    @EventHandler
    public void onSignClick(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }

        BlockState blockState = block.getState();
        if (!(blockState instanceof Sign)) {
            return;
        }

        Sign sign = (Sign) blockState;
        if (!sign.getLine(0).equalsIgnoreCase("kit")) {
            return;
        }

        if (KITS.contains(sign.getLine(2).toLowerCase())) {
            Player player = event.getPlayer();
            assignKit(player, sign.getLine(2));
        }
    }

    private void assignKit(Player player, String kitName) {
        player.performCommand("kit " + kitName);
    }
}
