package com.brandonjja.smash.worldLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import com.brandonjja.smash.Smash;
import com.brandonjja.smash.SmashCore;
import com.brandonjja.smash.game.Game;
import com.brandonjja.smash.game.ScoreboardManager;

public class SmashFileManager {
	
	/** Unloads the world so that it can be deleted **/
	public static void unloadWorld(World world) {
		if (world != null) {
			Bukkit.getServer().unloadWorld(world, true);
		}
	}
	
	/** Deletes the world folder (entire world) **/
	public static void deleteWorld(File path) {
		if (path.exists()) {
			File files[] = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteWorld(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		path.delete();
	}
	
	/** Copy a world to a new folder **/
	public static void copyWorld(File source, File target) {
		try {
			ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.lock")); // session.dat ??
			if (!ignore.contains(source.getName())) {
				if (source.isDirectory()) {
					if (!target.exists())
						target.mkdirs();
					String files[] = source.list();
					for (String file : files) {
						File srcFile = new File(source, file);
						File destFile = new File(target, file);
						copyWorld(srcFile, destFile);
					}
				} else {
					InputStream inputStream = new FileInputStream(source);
					OutputStream outputStream = new FileOutputStream(target);
					byte[] buffer = new byte[1024];
					int inStreamLength;
					while ((inStreamLength = inputStream.read(buffer)) > 0)
						outputStream.write(buffer, 0, inStreamLength);
					inputStream.close();
					outputStream.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Create a lobby2 world, which is a copy of the lobby **/
	public static World createLobby() {
		Bukkit.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "[Smash] " + ChatColor.WHITE + "Loading lobby...");
		
		String mapName = "lobby";
		String path = formatAbsolutePath(Bukkit.getWorldContainer().getAbsolutePath());
		
		String oldWorld = path + mapName;
		
		path += mapName + "2";
		
		String newWorld = path;
		File src = new File(oldWorld);
		File des = new File(newWorld);

		copyWorld(src, des);
		
		return Bukkit.createWorld(new WorldCreator("lobby2"));
	}
	
	/** Create a lobby2 world, then teleports everyone **/
	public static void createLobbyAndTeleport() {
		World world = createLobby();
		Location lobbySpawn = new Location(world, world.getSpawnLocation().getX(), world.getSpawnLocation().getY(), world.getSpawnLocation().getZ());
		Scoreboard blankScoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.teleport(lobbySpawn);
			player.setScoreboard(blankScoreboard);
		}
	}

	/** Deletes lobby2 world **/
	public static void deleteLobby() {
		World worldToDelete = Bukkit.getWorld("lobby2");
		unloadWorld(worldToDelete);
		File deleteFolder;
		try {
			deleteFolder = worldToDelete.getWorldFolder();
			deleteWorld(deleteFolder);
		} catch (NullPointerException e) {
			Smash.getInstance().getLogger().log(Level.INFO, "Lobby world already deleted");
			return;
		}
	}
	
	/** Generates a new random map from the map list and teleports all players to it */
	public static void generateMap() {
		Random random = new Random();
		int index = random.nextInt(Maps.getMaps().size());
		
		String mapName = Maps.getMaps().get(index);
		
		generateMap(mapName);
	}
	
	/**
	 * Generates a new random map from the map name and teleports all players to it
	 * @param map The map name
	 */
	public static void generateMap(String map) {
		Bukkit.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "[Smash] " + ChatColor.WHITE + "Loading Game Map...");
		
		ScoreboardManager.newBoard();
		
		String path = formatAbsolutePath(Bukkit.getWorldContainer().getAbsolutePath());
		
		String newWorld = path + map + "2";
		File des = new File(newWorld);
		
		String oldWorld = path + "maps/" + map;
		File src = new File(oldWorld);
		
		SmashCore.currentMap = map + "2";
		
		copyWorld(src, des);
		
		World world = Bukkit.createWorld(new WorldCreator(SmashCore.currentMap));
		Location spawnLocation = new Location(world, world.getSpawnLocation().getX(), world.getSpawnLocation().getY(), world.getSpawnLocation().getZ());
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.teleport(spawnLocation);
			ScoreboardManager.giveScoreboard(player);
		}
		Game.startGame();
	}
	
	/**
	 * 
	 * @param absolutePath The path to format (ex. Bukkit.getWorldContainer().getAbsolutePath() )
	 * @return Formatted path (C:\\Users formatted to C:/Users)
	 */
	private static String formatAbsolutePath(String absolutePath) {
		char charArr[] = absolutePath.toCharArray();
		StringBuilder path = new StringBuilder("");
		for (char cc : charArr) {
			if (cc == '.') {
				continue;
			} else if (cc == '\\') {
				cc = '/';
			}
			path.append(cc);
		}
		return path.toString();
	}
}
