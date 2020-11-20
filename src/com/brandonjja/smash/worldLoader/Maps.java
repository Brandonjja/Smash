package com.brandonjja.smash.worldLoader;

import java.util.List;

import com.brandonjja.smash.Smash;

public class Maps {
	private static List<String> maps = Smash.getInstance().getConfig().getStringList("maps");
	
	public static List<String> getMaps() {
		return maps;
	}
}
