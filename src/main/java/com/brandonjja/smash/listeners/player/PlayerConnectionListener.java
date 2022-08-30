package com.brandonjja.smash.listeners.player;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.brandonjja.smash.Smash;
import com.brandonjja.smash.SmashCore;
import com.brandonjja.smash.game.SmashPlayer;
import com.brandonjja.smash.worldLoader.SmashFileManager;

public class PlayerConnectionListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		SmashPlayer smashPlayer = new SmashPlayer(player);
		
		if (SmashCore.currentMap.equalsIgnoreCase("lobby2")) {
			if (!player.getWorld().getName().equalsIgnoreCase("lobby2")) {
				try {
					World world = Bukkit.getWorld("lobby2");
					Location spawnLocation = world.getSpawnLocation();
					player.teleport(new Location(world, spawnLocation.getX() + 0.5, spawnLocation.getY(), spawnLocation.getZ() + 0.5, 90, 0));
					SmashCore.currentMap = "lobby2";
				} catch (NullPointerException ex) {

					// An error here means lobby2 doesn't exist
					// Ignore this error because lobby2 is going to be created
					// If failed, another error will occur
					Smash.getInstance().getLogger().log(Level.INFO, "BELOW ==- Ignore this stack trace -== BELOW");
					ex.printStackTrace();
					Smash.getInstance().getLogger().log(Level.INFO, "^^^^==- Ignore this stack trace -==^^^^");

					World world = SmashFileManager.createLobby();
					Location spawnLocation = world.getSpawnLocation();
					player.teleport(new Location(world, spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ()));
				}
				player.setGameMode(GameMode.SURVIVAL);
			} else {
				World world = Bukkit.getWorld("lobby2");
				Location spawnLocation = world.getSpawnLocation();
				player.teleport(new Location(world, spawnLocation.getX() + 0.5, spawnLocation.getY(), spawnLocation.getZ() + 0.5, 90, 0));
			}
		} else {
			World world = Bukkit.getWorld(SmashCore.currentMap);
			Location spawnLocation = world.getSpawnLocation();
			player.teleport(new Location(world, spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ()));
			player.setGameMode(GameMode.SPECTATOR);
		}

		event.setJoinMessage(ChatColor.GREEN + player.getName() + " joined!");
		
		SmashCore.players.put(player, smashPlayer);
		smashPlayer.giveKitItems();
		smashPlayer.setKnockback(1);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		event.setQuitMessage(ChatColor.RED + player.getName() + " quit!");
		
		SmashCore.players.remove(player);
	}
	
	@EventHandler
	public void onPlayerLobby(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		World world = player.getWorld();
		if (world.getName().equalsIgnoreCase("lobby2") && !event.getFrom().getName().equalsIgnoreCase("lobby2")) {
			Location loc = world.getSpawnLocation();
			player.teleport(new Location(world, loc.getBlockX() + 0.5, loc.getBlockY(), loc.getBlockZ() + 0.5, 90, 0));
		}
	}
}
