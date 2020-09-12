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
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import com.brandonjja.smash.Smash;
import com.brandonjja.smash.SmashCore;
import com.brandonjja.smash.game.Game;
import com.brandonjja.smash.game.ScoreboardManager;

import net.md_5.bungee.api.ChatColor;

public class SmashFileManager {
	/** Unloads the world so that it can be deleted **/
	public static void unloadWorld(World world) {
		if (world != null) {
			Bukkit.getServer().unloadWorld(world, true);
		}
	}
	
	/** Deletes the world folder (entire world) **/
	public static boolean deleteWorld(File path) {
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
		return (path.delete());
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
					InputStream in = new FileInputStream(source);
					OutputStream out = new FileOutputStream(target);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = in.read(buffer)) > 0)
						out.write(buffer, 0, length);
					in.close();
					out.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Create a lobby2 world, which is a copy of the lobby **/
	public static World createLobby() {
		for (Player pl : Bukkit.getOnlinePlayers()) {
			pl.sendMessage(ChatColor.LIGHT_PURPLE + "[Smash] " + ChatColor.WHITE + "Loading lobby...");
		}
		
		// New code for lobby
		String map = "lobby";
		String pa = Bukkit.getWorldContainer().getAbsolutePath();
		char charArr[] = pa.toCharArray();
		String path = "";
		for (char cc : charArr) {
			if (cc == '.') {
				continue;
			} else if (cc == '\\') {
				cc = '/';
			}
			path += cc;
		}
		String oldWorld = path + map;

		String world2 = path + map + "2";
		File src2 = new File(oldWorld);
		File des2 = new File(world2);

		copyWorld(src2, des2);

		Bukkit.createWorld(new WorldCreator("lobby2"));
		World w = Bukkit.getWorld("lobby2");
		// New code for lobby END
		
		return w;
	}
	
	/** Create a lobby2 world, then teleports everyone **/
	public static void createLobbyAndTeleport() {
		for (Player pl : Bukkit.getOnlinePlayers()) {
			pl.sendMessage(ChatColor.LIGHT_PURPLE + "[Smash] " + ChatColor.WHITE + "Loading lobby...");
		}
		
		// New code for lobby
		String map = "lobby";
		String pa = Bukkit.getWorldContainer().getAbsolutePath();
		char charArr[] = pa.toCharArray();
		String path = "";
		for (char cc : charArr) {
			if (cc == '.') {
				continue;
			} else if (cc == '\\') {
				cc = '/';
			}
			path += cc;
		}
		String oldWorld = path + map;

		String world2 = path + map + "2";
		File src2 = new File(oldWorld);
		File des2 = new File(world2);

		copyWorld(src2, des2);

		Bukkit.createWorld(new WorldCreator("lobby2"));
		World world = Bukkit.getWorld("lobby2");
		// New code for lobby END
		
		//SmashCore.currentMap = "lobby2";
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.teleport(new Location(world, world.getSpawnLocation().getX(), world.getSpawnLocation().getY(), world.getSpawnLocation().getZ()));
			// = p;
			//ScoreboardManager.giveScoreboard(p);
			p.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
		}
	}

	/** Deletes lobby2 world **/
	public static void deleteLobby() {
		String name = "lobby2";
		World delete = Bukkit.getWorld(name);
		unloadWorld(delete);
		File deleteFolder;
		try {
			deleteFolder = delete.getWorldFolder();
			deleteWorld(deleteFolder);
		} catch (NullPointerException e) {
			// Ignore because lobby doesn't exist, so no need to delete
			Smash.getInstance().getLogger().log(Level.INFO, "==- Ignore this stack trace -==");
			e.printStackTrace();
			Smash.getInstance().getLogger().log(Level.INFO, "==- Ignore this stack trace -==");
			return;
		}
		deleteWorld(deleteFolder);
	}
	
	public static void generateMap() {
		
		for (Player pl : Bukkit.getOnlinePlayers()) {
			pl.sendMessage(ChatColor.LIGHT_PURPLE + "[Smash] " + ChatColor.WHITE + "Loading Game Map...");
		}
		
		ScoreboardManager.newBoard();
		
		Random r = new Random();
		int index = r.nextInt(Maps.getMaps().size());
		
		String map = Maps.getMaps().get(index);
		
		//
		String w = Bukkit.getWorldContainer().getAbsolutePath();
		char charArr[] = w.toCharArray();
		String path = "";
		for (char cc : charArr) {
			if (cc == '.') {
				continue;
			} else if (cc == '\\') {
				cc = '/';
			}
			path += cc;
		}
		String oldWorld = path + map;
		
		//String world2 = "C:/Users/ASUS/Desktop/Server/" + name + "2";
		String world2 = path + map + "2";
		File src2 = new File(oldWorld);
		File des2 = new File(world2);
		
		SmashCore.currentMap = map + "2";
		
		copyWorld(src2, des2);
		//
		
		//Player first = null;
		
		Bukkit.createWorld(new WorldCreator(SmashCore.currentMap));
		World world = Bukkit.getWorld(SmashCore.currentMap);
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.teleport(new Location(world, world.getSpawnLocation().getX(), world.getSpawnLocation().getY(), world.getSpawnLocation().getZ()));
			// = p;
			ScoreboardManager.giveScoreboard(p);
			//p.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
		}
		Game.startGame();
	}
	
public static void generateMap(String map) {
		
		for (Player pl : Bukkit.getOnlinePlayers()) {
			pl.sendMessage(ChatColor.LIGHT_PURPLE + "[Smash] " + ChatColor.WHITE + "Loading Game Map...");
		}
		
		ScoreboardManager.newBoard();
		
		//
		String w = Bukkit.getWorldContainer().getAbsolutePath();
		char charArr[] = w.toCharArray();
		String path = "";
		for (char cc : charArr) {
			if (cc == '.') {
				continue;
			} else if (cc == '\\') {
				cc = '/';
			}
			path += cc;
		}
		String oldWorld = path + map;
		
		//String world2 = "C:/Users/ASUS/Desktop/Server/" + name + "2";
		String world2 = path + map + "2";
		File src2 = new File(oldWorld);
		File des2 = new File(world2);
		
		SmashCore.currentMap = map + "2";
		
		copyWorld(src2, des2);
		//
		
		//Player first = null;
		
		Bukkit.createWorld(new WorldCreator(SmashCore.currentMap));
		World world = Bukkit.getWorld(SmashCore.currentMap);
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.teleport(new Location(world, world.getSpawnLocation().getX(), world.getSpawnLocation().getY(), world.getSpawnLocation().getZ()));
			// = p;
			ScoreboardManager.giveScoreboard(p);
			//p.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
		}
		Game.startGame();
	}
}
