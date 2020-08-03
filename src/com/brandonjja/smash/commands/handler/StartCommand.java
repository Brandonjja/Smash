package com.brandonjja.smash.commands.handler;

import org.bukkit.entity.Player;

import com.brandonjja.smash.commands.SmashCommand;
import com.brandonjja.smash.worldLoader.SmashFileManager;

public class StartCommand extends SmashCommand {

	@Override
	public boolean execute(Player player, String[] args) {
		SmashFileManager.generateMap();
		return true;
	}

}
