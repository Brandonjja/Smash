package com.brandonjja.smash.commands.handler;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.brandonjja.smash.commands.SmashCommand;
import com.brandonjja.smash.worldLoader.Maps;

public class MapsCommand extends SmashCommand {

	@Override
	public boolean execute(Player player, String[] args) {
		StringBuilder sb = new StringBuilder(ChatColor.LIGHT_PURPLE + "[Smash Maps] " + ChatColor.GRAY);
		List<String> mapsList = new ArrayList<>(Maps.getMaps());
		for (String map : mapsList) {
			sb.append(map).append(", ");
		}
		sb.setLength(sb.length() - 2); // Removes the last comma and space
		player.sendMessage(sb.toString());
		return true;
	}
}
