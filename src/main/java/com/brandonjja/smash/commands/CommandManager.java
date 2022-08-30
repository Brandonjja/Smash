package com.brandonjja.smash.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.brandonjja.smash.commands.handler.EndCommand;
import com.brandonjja.smash.commands.handler.KitCommand;
import com.brandonjja.smash.commands.handler.MapsCommand;
import com.brandonjja.smash.commands.handler.SetSpawnCommand;
import com.brandonjja.smash.commands.handler.SpeedCommand;
import com.brandonjja.smash.commands.handler.StartCommand;
import com.brandonjja.smash.commands.handler.TpwCommand;
import com.brandonjja.smash.commands.handler.WorldCommand;

public class CommandManager implements CommandExecutor {
	
	private static Map<String, SmashCommand> map = new HashMap<>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		return map.get(commandLabel).execute((Player) sender, args);
	}
	
	public static void registerCommands() {
		
		map.put("kit", new KitCommand());
		map.put("kits", new KitCommand());
		map.put("speed", new SpeedCommand());
		map.put("world", new WorldCommand());
		map.put("start", new StartCommand());
		map.put("tpw", new TpwCommand());
		map.put("end", new EndCommand());
		map.put("maps", new MapsCommand());
		map.put("setspawn", new SetSpawnCommand());
		
		for (String cmd : map.keySet()) {
			register(cmd, new CommandManager());
		}
	}

	private static void register(String label, CommandExecutor command) {
		Bukkit.getPluginCommand(label).setExecutor(command);
	}
	
}