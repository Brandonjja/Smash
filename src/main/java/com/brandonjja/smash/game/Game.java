package com.brandonjja.smash.game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.brandonjja.smash.Smash;
import com.brandonjja.smash.SmashCore;
import com.brandonjja.smash.worldLoader.Maps;
import com.brandonjja.smash.worldLoader.SmashFileManager;

public class Game {
	private final static int pointsNeeded = 5;
	private static boolean win = false;
	private static boolean playing = false;
	
	private static int itemSpawnDelay = 40; // Time between new items spawning around the map (seconds)
	
	private static int loopID = -1;
	
	public static boolean getWin() {
		return win;
	}
	
	public static void startGame() {
		SmashWorld.updateBlocks();
		
		String mapName = SmashCore.currentMap.split("2")[0]; // Map names in the form of Name2, world2, etc.
		
		List<String> mapsList = new ArrayList<>(Maps.getMaps());
		for (String map : mapsList) {
			if (map.equalsIgnoreCase(mapName)) {
				mapName = map; // Used to get proper capitalization of a map
				break;
			}
		}
		
		final String gameStartMsg = ChatColor.GRAY + "You are playing on " + ChatColor.AQUA + mapName + ChatColor.GRAY + "!" + "\n" + ChatColor.LIGHT_PURPLE + "Game starting in 5 seconds!";
		
		for (Player pl : Bukkit.getOnlinePlayers()) {
			pl.setGameMode(GameMode.ADVENTURE);
			pl.sendMessage(gameStartMsg);
			
			SmashCore.players.get(pl).giveKitItems();
			pl.setLevel(1);
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Smash.getInstance(), new Runnable() {
			@Override
			public void run() {
				final String gameStartMsg = ChatColor.GOLD + "Game starting now!";
				for (Player pl : Bukkit.getOnlinePlayers()) {
					pl.sendMessage(gameStartMsg);
					SmashCore.players.get(pl).giveKitItems();
				}
				playing = true;
				runGameLoop();
			}

		}, 20 * 5);
		
	}
	
	private static void runGameLoop() {
		loopID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Smash.getInstance(), new Runnable() {
			@Override
			public void run() {
				SmashWorld.spawnItem();
				fixSlabs();
			}
		}, 20L * 10, 20L * itemSpawnDelay);
	}
	
	private static void fixSlabs() {
		List<Location> safe = new ArrayList<>();
		Player player = (Player) Bukkit.getOnlinePlayers().toArray()[0];
		
		for (Entity entity : player.getWorld().getEntities()) {
			Location loc = entity.getLocation();
			if (entity instanceof Player) {
				continue;
			}
			safe.add(loc);
		}
		
		/*for (Entity entity : player.getLocation().getChunk().getEntities()) {
			Location loc = entity.getLocation();
			if (loc.getWorld().getBlockAt(loc).getType() == Material.STEP) {
				safe.add(loc);
			}
		}*/
		
		boolean needToUpdate[] = new boolean[SmashWorld.slabLoc.size()];
		int ctr = 0;
		
		for (Location l : SmashWorld.slabLoc) {
			for (Location safeL : safe) {
				if (safeL.getBlock().getType() != Material.STEP) {
					needToUpdate[ctr] = false;
					return;
				}
				needToUpdate[ctr] = true;
				if (l.getBlockX() == safeL.getBlockX() && l.getBlockY() == safeL.getBlockY() && l.getBlockZ() == safeL.getBlockZ()) {
					needToUpdate[ctr] = false;
					break;
				}
			}
			ctr++;
		}
		
		for (int i = 0; i < SmashWorld.slabLoc.size(); i++) {
			if (needToUpdate[i]) {
				SmashWorld.slabLoc.get(i).getBlock().setType(Material.AIR);
				//Bukkit.getPlayer("Brandonjja").sendMessage("Fixed at: " + SmashWorld.slabLoc.get(i).getBlockX() + " " + SmashWorld.slabLoc.get(i).getBlockZ());
			}
		}
	}
	
	/*private static boolean isSafe() {
		List<Location> safe = new ArrayList<>();
		
		for (Location l : SmashWorld.slabLoc) {
			for (Location safeL : safe) {
				if (l.getX() == safeL.getX() && l.getY() == safeL.getY() && l.getZ() == safeL.getZ()) {
					return true;
				}
			}
		}
		return false;
	}*/
	
	/** Checks if a player has won the game */
	public static void checkEndGame() {
		List<Player> winners = new ArrayList<>();
		for (Player pl : Bukkit.getOnlinePlayers()) {
			if (ScoreboardManager.getScore(pl) == pointsNeeded) {
				winners.add(pl);
				win = true;
			}
		}
		if (win) {
			triggerEndGame(winners);
		}
	}
	
	/**
	 * Ends the game
	 * @param winners A list of winners in the game
	 */
	private static void triggerEndGame(List<Player> winners) {
		playing = false;
		Bukkit.getScheduler().cancelTask(loopID);
		loopID = -1;
		
		String winMsg;
		
		if (winners.size() == 1) {
			Player winnerPlayer = winners.get(0);
			winMsg = ChatColor.GOLD + winnerPlayer.getName() + " wins with a score of " + ScoreboardManager.getScore(winnerPlayer) + "!";
		} else {
			//String names = "";
			StringBuilder names = new StringBuilder("");
			names.append(ChatColor.GOLD);
			for (int i = 0; i < winners.size() - 1; i++) {
				names.append(winners.get(i));
				names.append(" and ");
			}
			names.append(winners.get(winners.size() - 1));
			winMsg = names.toString() + " draw with a score of " + ScoreboardManager.getScore(winners.get(0)) + "!";
		}
		
		final String gameFinishedMsg = ChatColor.GOLD + "Game Finished!";
		for (Player player : Bukkit.getOnlinePlayers()) {
			SmashPlayer smashPlayer = SmashCore.players.get(player);
			smashPlayer.giveKitItems();
			smashPlayer.setLastHitFrom(null);
			player.setGameMode(GameMode.SURVIVAL);
			player.sendMessage(gameFinishedMsg);
			player.sendMessage(winMsg);
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Smash.getInstance(), new Runnable() {
			@Override
			public void run() {
				SmashFileManager.deleteLobby();
				SmashFileManager.createLobbyAndTeleport();
				deleteMap();
				SmashCore.currentMap = "lobby2";
				win = false;
				for (Player player : Bukkit.getOnlinePlayers()) {
					player.setLevel(1);
					ScoreboardManager.updateKB(player);
				}
			}

		}, 20 * 8); // 20 Ticks * x seconds = Starts in x seconds
	}
	
	/** Forcibly end a game before a player wins */
	public static void forceEnd() {
		playing = false;
		Bukkit.getScheduler().cancelTask(loopID);
		loopID = -1;
		final String forceEndMsg = ChatColor.LIGHT_PURPLE + "[Smash] " + ChatColor.GOLD + "Game force ended";
		for (Player player : Bukkit.getOnlinePlayers()) {
			SmashPlayer smashPlayer = SmashCore.players.get(player);
			smashPlayer.giveKitItems();
			smashPlayer.setLastHitFrom(null);
			player.setGameMode(GameMode.SURVIVAL);
			player.sendMessage(forceEndMsg);
			player.setLevel(1);
			ScoreboardManager.updateKB(player);
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Smash.getInstance(), new Runnable() {
			@Override
			public void run() {
				String map = ((Player)Bukkit.getOnlinePlayers().toArray()[0]).getWorld().getName();
				SmashFileManager.deleteLobby();
				SmashFileManager.createLobbyAndTeleport();
				deleteMap(map);
				SmashCore.currentMap = "lobby2";
				win = false;
				
			}

		}, 20);
	}
	
	/** Deletes the current map being used in game */
	private static void deleteMap() {
		if (SmashCore.currentMap.equals("lobby") || SmashCore.currentMap.equals("lobby2")) {
			return;
		}
		deleteMap(SmashCore.currentMap);
	}
	
	/**
	 * Deletes the map
	 * @param map The map name
	 */
	private static void deleteMap(String map) {
		World delete = Bukkit.getWorld(map);
		if (map.equals("lobby") || map.equals("lobby2")) {
			return;
		}
		SmashFileManager.unloadWorld(delete);
		File deleteFolder;
		try {
			deleteFolder = delete.getWorldFolder();
			SmashFileManager.deleteWorld(deleteFolder);
		} catch (NullPointerException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public static void setInGame(boolean inGame) {
		playing = inGame;
	}
	
	public static boolean inGame() {
		return playing;
	}
}
