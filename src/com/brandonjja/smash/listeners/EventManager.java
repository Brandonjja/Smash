package com.brandonjja.smash.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import com.brandonjja.smash.Smash;

public class EventManager {
	public static void registerListeners() {
		register(new BlockExplodeListener());
		register(new BlockListener());
		
		register(new ItemPickUpListener());
		
		register(new PlayerConnectionListener());
		register(new PlayerConsumeListener());
		
		register(new PlayerDamageListener());
		
		register(new PlayerInteractListener());
		register(new PlayerInventoryListener());
		
		register(new PlayerMoveListener());
	}
	
	private static void register(Listener listener) {
		Bukkit.getPluginManager().registerEvents(listener, Smash.getInstance());
	}
}
