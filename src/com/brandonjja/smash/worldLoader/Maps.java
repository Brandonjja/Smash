package com.brandonjja.smash.worldLoader;

import java.util.ArrayList;
import java.util.List;

public class Maps {
	private static List<String> maps = new ArrayList<>();
	
	protected static List<String> getMaps() {
		maps = new ArrayList<>();
		maps.add("Angry Birds");
		maps.add("Aquartez");
		maps.add("Imagination");
		maps.add("Block Fort");
		maps.add("Supernova");
		maps.add("Sand Track");
		maps.add("Pokemon Star");
		maps.add("Clock");
		return maps;
	}
}
