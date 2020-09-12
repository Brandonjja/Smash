package com.brandonjja.smash.commands.handler;

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
			try {
			World w = Bukkit.getWorld(name);
			player.teleport(new Location(w, w.getSpawnLocation().getX(), w.getSpawnLocation().getY(), w.getSpawnLocation().getZ()));
			} catch (NullPointerException ex) {
				Bukkit.createWorld(new WorldCreator(name));
				World w = Bukkit.getWorld(name);
				player.teleport(new Location(w, w.getSpawnLocation().getX(), w.getSpawnLocation().getY(), w.getSpawnLocation().getZ()));
			}
			return true;
		}
		return false;
	}
}
