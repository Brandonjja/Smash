package com.brandonjja.smash.worldLoader;

import java.util.ArrayList;
import java.util.List;

public class Maps {
	private static List<String> maps = new ArrayList<>();
	
	public static List<String> getMaps() {
		maps = new ArrayList<>();
		maps.add("Angry Birds");
		maps.add("Aquartez");
		maps.add("Imagination");
		
		maps.add("Block Fort");
		maps.add("Supernova");
		
		maps.add("EndStone");
		maps.add("Catch 'em All");
		maps.add("Time's Up"); // Clock
		maps.add("Sandy Dayz");
		maps.add("Star");
		maps.add("Snowflake");
		return maps;
	}
}
