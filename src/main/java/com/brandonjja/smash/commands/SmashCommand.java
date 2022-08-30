package com.brandonjja.smash.commands;

import org.bukkit.entity.Player;

public abstract class SmashCommand {
	public abstract boolean execute(Player player, String[] args);
}
