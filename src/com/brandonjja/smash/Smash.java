package com.brandonjja.smash;

import org.bukkit.plugin.java.JavaPlugin;

public class Smash extends JavaPlugin {
	private static Smash INSTANCE;
	
	@Override
	public void onEnable() {
		INSTANCE = this;
		SmashCore.init();
	}
	
	@Override
	public void onDisable() {
		INSTANCE = null;
	}
	
	public static Smash getInstance() {
		return INSTANCE;
	}
}
