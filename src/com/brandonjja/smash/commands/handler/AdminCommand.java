package com.brandonjja.smash.commands.handler;

import org.bukkit.entity.Player;

import com.brandonjja.smash.commands.SmashCommand;

import net.md_5.bungee.api.ChatColor;

public class AdminCommand extends SmashCommand {

	@Override
	public boolean execute(Player player, String[] args) {
		double num = Double.parseDouble(args[0]);
		if (num >= 1) {
			num /= 10;
		}
		player.setFlySpeed((float) num);
		player.sendMessage(ChatColor.YELLOW + "Fly speed set to: " + ChatColor.GREEN + num);
		return true;
	}
}
