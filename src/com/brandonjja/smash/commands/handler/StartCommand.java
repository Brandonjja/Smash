package com.brandonjja.smash.commands.handler;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.brandonjja.smash.commands.SmashCommand;
import com.brandonjja.smash.worldLoader.Maps;
import com.brandonjja.smash.worldLoader.SmashFileManager;

import net.md_5.bungee.api.ChatColor;

public class StartCommand extends SmashCommand {

	@Override
	public boolean execute(Player player, String[] args) {
		if (!player.isOp()) {
			return false;
		}
		if (args.length == 0) {
			SmashFileManager.generateMap();
			return true;
		} else {
			String mapName = "";
			for (int i = 0; i < args.length; i++) {
				mapName += args[i];
				if (i != args.length - 1) {
					mapName += " ";
				}
			}
			List<String> mapsList = new ArrayList<>(Maps.getMaps());
			for (String map : mapsList) {
				if (map.equalsIgnoreCase(mapName)) {
					SmashFileManager.generateMap(mapName);
					return true;
				}
			}
			player.sendMessage(ChatColor.LIGHT_PURPLE + "[Smash] " + ChatColor.GRAY + "Map " + mapName + " not found!");
		}
		return true;
	}

}
