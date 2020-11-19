package com.brandonjja.smash.commands.handler;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import com.brandonjja.smash.commands.SmashCommand;

public class TpwCommand extends SmashCommand {

	@Override
	public boolean execute(Player player, String[] args) {
		if (!player.isOp()) {
			return false;
		}
		if (args.length >= 1) {
			String name = "";
			for (int i = 0; i < args.length; i++) {
				name += args[i];
				if (i != args.length - 1) {
					name += " ";
				}
			}
			name = name.toLowerCase();
			String worldPath = getFilePath(name);
			if (worldPath == null) {
				player.sendMessage("Cannot find a world with the name: " + name);
				return true;
			}
			try {
				World world = Bukkit.getWorld(worldPath);
				player.teleport(new Location(world, world.getSpawnLocation().getX(), world.getSpawnLocation().getY(), world.getSpawnLocation().getZ()));
			} catch (NullPointerException ex) {
				World world = Bukkit.createWorld(new WorldCreator(worldPath));
				player.teleport(new Location(world, world.getSpawnLocation().getX(), world.getSpawnLocation().getY(), world.getSpawnLocation().getZ()));
			}
			return true;
		}
		return false;
	}
	
	private String getFilePath(String name) {
		File path = Bukkit.getWorldContainer();
		for (File f : path.listFiles()) {
			if (f.getName().equalsIgnoreCase(name)) {
				return f.getName();
			}
		}
		try {
			String mapsPath = path.getCanonicalPath() + "/maps/";
			File dir = new File(mapsPath);
			for (File f : dir.listFiles()) {
				if (f.getName().equalsIgnoreCase(name)) {
					return f.getParentFile().getName() + "\\" + f.getName();
				}
			}
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
