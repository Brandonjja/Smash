package com.brandonjja.smash.listeners.other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignClickListener implements Listener {
	
	private final List<String> kits = new ArrayList<>(Arrays.asList("blink", "metoo", "pika", "toshi", "jigglyo", "shadow"
			));
	
	@EventHandler
	public void onSignClick(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		Block block = (Block) e.getClickedBlock();
		if (block != null && block.getState() instanceof Sign) {
			Sign sign = (Sign) block.getState();

			if (!sign.getLine(0).equalsIgnoreCase("kit")) {
				return;
			}

			if (kits.contains(sign.getLine(2).toLowerCase())) {
				assignKit(player, sign.getLine(2));
			}
		}
	}

	private void assignKit(Player player, String kitName) {
		player.performCommand("kit " + kitName);
	}
}
