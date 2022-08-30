package com.brandonjja.smash;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.brandonjja.smash.commands.CommandManager;
import com.brandonjja.smash.game.SmashPlayer;
import com.brandonjja.smash.listeners.EventManager;

public class SmashCore {
	
	public static Map<Player, SmashPlayer> players = new HashMap<>();
	public static String currentMap;
	
	protected static void init() {
		
		CommandManager.registerCommands();
		EventManager.registerListeners();
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			SmashPlayer smashPlayer = new SmashPlayer(player);
			players.put(player, smashPlayer);
		}
		
		currentMap = "lobby2";
		
		loadConfig();
	}
	
	protected static void loadConfig() {
		Smash INSTANCE = Smash.getInstance();
		INSTANCE.reloadConfig();
		INSTANCE.getConfig().options().copyDefaults(true);
		INSTANCE.saveConfig();
	}
	
	protected static void shutdown() {
		for (Player pl : Bukkit.getOnlinePlayers()) {
			SmashCore.players.remove(pl);
		}
	}
}
