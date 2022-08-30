package com.brandonjja.smash.commands.handler;

import org.bukkit.entity.Player;

import com.brandonjja.smash.commands.SmashCommand;

import net.md_5.bungee.api.ChatColor;

public class SpeedCommand extends SmashCommand {

	@Override
	public boolean execute(Player player, String[] args) {
		
		if (args.length == 0) {
			player.sendMessage(ChatColor.YELLOW + "Current fly speed: " + ChatColor.GREEN + (player.getFlySpeed() * 10));
			return true;
		}
		
		if (args.length == 1) {
			try {
				double num = Double.parseDouble(args[0]);
				if (num <= 0) {
					return false;
				}
				num /= 10;
				player.setFlySpeed((float) num);
				player.sendMessage(ChatColor.YELLOW + "Fly speed set to: " + ChatColor.GREEN + (num * 10));
				return true;
			} catch (IllegalArgumentException ex) {
				return false;
			}
		}
		return false;
	}
}
