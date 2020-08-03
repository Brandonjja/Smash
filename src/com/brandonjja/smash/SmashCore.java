package com.brandonjja.smash;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import com.brandonjja.smash.commands.CommandManager;
import com.brandonjja.smash.game.SmashPlayer;
import com.brandonjja.smash.kits.KitLoader;
import com.brandonjja.smash.listeners.EventManager;

public class SmashCore {
	
	// use this to register commands and listeners
	// but maybe still use CommandManager to list all commands? Then call static method from there, in the init, to register
	
	public static Map<Player, SmashPlayer> players = new HashMap<>();
	public static String currentMap;
	
	private static String commands[] = {"kit", "kits", "speed", "world", "start", "tpw", "end"
			};
	
	protected static void init() {
		KitLoader.init();
		registerCommands();
		EventManager.registerListeners();
		
		for (Player pl : Bukkit.getOnlinePlayers()) {
			SmashPlayer p = new SmashPlayer(pl);
			players.put(pl, p);
		}
		
		currentMap = "lobby2";
		
	}
	
	private static void registerCommands() {
		
		for (String s : commands) {
			register(s, new CommandManager());
		}
		
		/*register("kit", new CommandManager());
		register("speed", new CommandManager());
		register("world", new CommandManager());
		register("start", new CommandManager());
		register("tpw", new CommandManager());*/
		for (Player pl : Bukkit.getOnlinePlayers()) {
			players.remove(pl);
		}
	}
	
	private static void register(String label, CommandExecutor command) {
		Bukkit.getPluginCommand(label).setExecutor(command);
	}
}
