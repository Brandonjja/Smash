package com.brandonjja.smash.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import com.brandonjja.smash.Smash;
import com.brandonjja.smash.listeners.other.GiveCommandListener;
import com.brandonjja.smash.listeners.other.ItemPickUpListener;
import com.brandonjja.smash.listeners.other.PearlListener;
import com.brandonjja.smash.listeners.other.SignClickListener;
import com.brandonjja.smash.listeners.player.PlayerConnectionListener;
import com.brandonjja.smash.listeners.player.PlayerConsumeListener;
import com.brandonjja.smash.listeners.player.PlayerDamageListener;
import com.brandonjja.smash.listeners.player.PlayerInteractItemListener;
import com.brandonjja.smash.listeners.player.PlayerInventoryListener;
import com.brandonjja.smash.listeners.player.PlayerMoveListener;
import com.brandonjja.smash.listeners.world.BlockExplodeListener;
import com.brandonjja.smash.listeners.world.BlockListener;
import com.brandonjja.smash.listeners.world.WeatherListener;

public class EventManager {
	public static void registerListeners() {
		register(new BlockExplodeListener());
		register(new BlockListener());
		
		register(new ItemPickUpListener());
		register(new PearlListener());
		
		register(new PlayerConnectionListener());
		register(new PlayerConsumeListener());
		
		register(new PlayerDamageListener());
		
		register(new PlayerInteractItemListener());
		register(new PlayerInventoryListener());
		
		register(new PlayerMoveListener());
		register(new SignClickListener());
		
		register(new WeatherListener());
		
		register(new GiveCommandListener());
	}
	
	private static void register(Listener listener) {
		Bukkit.getPluginManager().registerEvents(listener, Smash.getInstance());
	}
}
