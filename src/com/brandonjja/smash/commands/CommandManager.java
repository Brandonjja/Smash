package com.brandonjja.smash.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.brandonjja.smash.commands.handler.AdminCommand;
import com.brandonjja.smash.commands.handler.KitCommand;
import com.brandonjja.smash.commands.handler.ReloadCommand;
import com.brandonjja.smash.commands.handler.StartCommand;
import com.brandonjja.smash.commands.handler.TpwCommand;
import com.brandonjja.smash.commands.handler.WorldCommand;

public class CommandManager implements CommandExecutor {

	private SmashCommand instance;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}

		Player player = (Player) sender;

		switch (commandLabel.toLowerCase()) {
		case "kit":
			instance = new KitCommand();
			return instance.execute(player, args);
		case "kits":
			instance = new KitCommand();
			return instance.execute(player, args);
		case "speed":
			instance = new AdminCommand();
			return instance.execute(player, args);
		case "world":
			instance = new WorldCommand();
			return instance.execute(player, args);
		case "start":
			return new StartCommand().execute(player, args);
		case "tpw":
			return new TpwCommand().execute(player, args);
		case "end":
			return new ReloadCommand().execute(player, args);
		default:
			return false;
		}
	}
}