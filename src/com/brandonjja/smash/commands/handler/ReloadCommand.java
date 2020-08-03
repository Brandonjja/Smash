package com.brandonjja.smash.commands.handler;

import org.bukkit.entity.Player;

import com.brandonjja.smash.commands.SmashCommand;
import com.brandonjja.smash.game.Game;

public class ReloadCommand extends SmashCommand {

	@Override
	public boolean execute(Player player, String[] args) {
		if (player.isOp()) {
			Game.forceEnd();
			return true;
		}
		return false;
	}
}
