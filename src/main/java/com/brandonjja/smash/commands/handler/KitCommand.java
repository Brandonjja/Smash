package com.brandonjja.smash.commands.handler;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.brandonjja.smash.SmashCore;
import com.brandonjja.smash.commands.SmashCommand;
import com.brandonjja.smash.game.SmashPlayer;
import com.brandonjja.smash.kits.Kit;
import com.brandonjja.smash.kits.classes.Blink;
import com.brandonjja.smash.kits.classes.Jigglyo;
import com.brandonjja.smash.kits.classes.Metoo;
import com.brandonjja.smash.kits.classes.Pika;
import com.brandonjja.smash.kits.classes.Shadow;
import com.brandonjja.smash.kits.classes.Toshi;

import net.md_5.bungee.api.ChatColor;

public class KitCommand extends SmashCommand {

	private Map<String, Kit> kitMap;
	private final String[] iHaveOcdList = {"Blink", "Metoo", "Pika", "Toshi", "Jigglyo"}; // For listing kits using /kits
	
	private void getKits() {
		kitMap.put("Blink", new Blink());
		kitMap.put("Metoo", new Metoo());
		kitMap.put("Pika", new Pika());
		kitMap.put("Toshi", new Toshi());
		kitMap.put("Jigglyo", new Jigglyo());
		
		kitMap.put("Shadow", new Shadow());
	}
	
	@Override
	public boolean execute(Player player, String[] args) {
		kitMap = new HashMap<>();

		getKits();

		SmashPlayer smashPlayer = SmashCore.players.get(player);

		// TODO: Remember to load new classes in KitLoader!!

		if (!SmashCore.currentMap.equalsIgnoreCase("lobby2")) {
			player.sendMessage(ChatColor.LIGHT_PURPLE + "[Smash] " + ChatColor.RED + "You can only choose kits from the lobby!");
			player.sendMessage(ChatColor.GOLD + "You are the " + ChatColor.GREEN + smashPlayer.getKit().getName() + ChatColor.GOLD + " class!");
			return true;
		}

		if (args.length == 0) {
			StringBuilder sb = new StringBuilder().append(ChatColor.GOLD).append("Kits: ").append(ChatColor.GREEN);
			for (String kitName : iHaveOcdList) {
				if (kitName.equalsIgnoreCase("shadow")) {
					continue;
				}
				sb.append(kitName).append(", ");
			}
			sb.setLength(sb.length() - 2);
			player.sendMessage(sb.toString());
			player.sendMessage(ChatColor.GOLD + "You are the " + ChatColor.GREEN + smashPlayer.getKit().getName() + ChatColor.GOLD + " class!");
			return true;
		} else if (args.length == 1) {
			String kit = args[0].toLowerCase();
			kit = kit.substring(0, 1).toUpperCase() + kit.substring(1);

			if (!kitMap.containsKey(kit)) {
				player.sendMessage(ChatColor.LIGHT_PURPLE + "[Smash] " + ChatColor.RED + "No kit \"" + kit + "\" found!");
				return false;
			}

			smashPlayer.setKit(kitMap.get(kit));

			player.sendMessage(smashPlayer.getKit().getKitMessage());
			player.getInventory().setContents(smashPlayer.getKit().getItems());
			player.getInventory().setHelmet(smashPlayer.getKit().getHelmet());
			player.updateInventory();
			smashPlayer.updateInventoryMap();
			smashPlayer.setKnockback(1);
			return true;
		} else {
			return false;
		}
	}
}
