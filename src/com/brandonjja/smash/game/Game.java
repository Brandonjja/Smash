package com.brandonjja.smash.game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.brandonjja.smash.Smash;
import com.brandonjja.smash.SmashCore;
import com.brandonjja.smash.worldLoader.SmashFileManager;

import net.md_5.bungee.api.ChatColor;

public class Game {
	private final static int pointsNeeded = 5;
	private static boolean win = false;
	private static boolean playing = false;
	
	private static int loopID = -1;
	
	public static boolean getWin() {
		return win;
	}
	
	public static void startGame() {
		
		SmashWorld.updateBlocks();
		
		for (Player pl : Bukkit.getOnlinePlayers()) {
			pl.setGameMode(GameMode.ADVENTURE);
			//pl.sendMessage(ChatColor.GRAY + "You're playing on " + ChatColor.AQUA + "mapName");
			pl.sendMessage(ChatColor.LIGHT_PURPLE + "Game starting in 5 seconds!");
			
			/*pl.getInventory().clear();
			pl.getInventory().setContents(SmashCore.players.get(pl).getKit().getItems());
			pl.getInventory().setHelmet(SmashCore.players.get(pl).getKit().getHelmet());
			pl.updateInventory();*/
			SmashCore.players.get(pl).giveKitItems();
			pl.setLevel(1);
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(Smash.getInstance(), new Runnable() {
			@Override
			public void run() {
				for (Player pl : Bukkit.getOnlinePlayers()) {
					pl.sendMessage(ChatColor.GOLD + "Game starting now!");
					/*pl.getInventory().clear();
					pl.getInventory().setContents(SmashCore.players.get(pl).getKit().getItems());
					pl.getInventory().setHelmet(SmashCore.players.get(pl).getKit().getHelmet());
					pl.updateInventory();*/
					SmashCore.players.get(pl).giveKitItems();
					
					/*for (int i : SmashCore.players.get(pl).inventorySlot.keySet()) {
						Bukkit.getPlayer("Brandonjja").sendMessage(SmashCore.players.get(pl).inventorySlot.get(i).getType().toString() + i);
					}*/
					
				}
				playing = true;
				runGameLoop();
			}

		}, 20 * 5); // 20 Ticks * x seconds = Starts in x seconds
		
	}
	
	private static void runGameLoop() {
		loopID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Smash.getInstance(), new Runnable() {
			@Override
			public void run() {
				SmashWorld.spawnItem();
				fixSlabs();
			}
		}, 0L, 20L * 4); // 0 Tick initial delay, 20 * x seconds between repeats
	}
	
	private static void fixSlabs() {
		//long time = System.currentTimeMillis();
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
		
		//String mes = "";
		
		for (Location l : SmashWorld.slabLoc) {
			for (Location safeL : safe) {
				if (safeL.getBlock().getType() != Material.STEP) {
					needToUpdate[ctr] = false;
					//mes += "not step ";
					return;
				}
				needToUpdate[ctr] = true;
				if (l.getBlockX() == safeL.getBlockX() && l.getBlockY() == safeL.getBlockY() && l.getBlockZ() == safeL.getBlockZ()) {
					needToUpdate[ctr] = false;
					//Bukkit.getPlayer("Brandonjja").sendMessage("added");
					//mes += "added ";
					break;
				}
			}
			ctr++;
			/*if (safe.contains(l)) {
				continue;
			}*/
		}
		
		for (int i = 0; i < SmashWorld.slabLoc.size(); i++) {
			if (needToUpdate[i]) {
				SmashWorld.slabLoc.get(i).getBlock().setType(Material.AIR);
				//Bukkit.getPlayer("Brandonjja").sendMessage("Fixed at: " + SmashWorld.slabLoc.get(i).getBlockX() + " " + SmashWorld.slabLoc.get(i).getBlockZ());
			}
		}
		
		//long end = System.currentTimeMillis();
		//Bukkit.getPlayer("Brandonjja").sendMessage(mes);
        //Bukkit.getPlayer("Brandonjja").sendMessage((end - time) + "ms to fix");
		
		
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
	
	private static void triggerEndGame(List<Player> winners) {
		playing = false;
		Bukkit.getScheduler().cancelTask(loopID);
		loopID = -1;
		for (Player pl : Bukkit.getOnlinePlayers()) {
			//pl.getInventory().setContents(SmashCore.players.get(pl).getKit().getItems());
			SmashCore.players.get(pl).giveKitItems();
			pl.setGameMode(GameMode.ADVENTURE);
			pl.sendMessage(ChatColor.GOLD + "Game finished!");
			if (winners.size() == 1) {
				pl.sendMessage(ChatColor.GOLD + winners.get(0).getName() + " wins with a score of " + ScoreboardManager.getScore(winners.get(0)) + "!");
			} else {
				String names = "";
				for (int i = 0; i < winners.size() - 1; i++) {
					names += winners.get(i) + " and ";
				}
				names += winners.get(winners.size() - 1);
				pl.sendMessage(ChatColor.GOLD + names + " draw with a score of " + ScoreboardManager.getScore(winners.get(0)) + "!");
			}
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Smash.getInstance(), new Runnable() {
			@Override
			public void run() {
				SmashFileManager.deleteLobby();
				SmashFileManager.createLobbyAndTeleport();
				deleteMap();
				SmashCore.currentMap = "lobby2";
				win = false;
			}

		}, 20 * 8); // 20 Ticks * x seconds = Starts in x seconds
	}
	
	public static void forceEnd() {
		playing = false;
		Bukkit.getScheduler().cancelTask(loopID);
		loopID = -1;
		for (Player pl : Bukkit.getOnlinePlayers()) {
			/*pl.getInventory().clear();
			pl.getInventory().setContents(SmashCore.players.get(pl).getKit().getItems());
			pl.updateInventory();*/
			SmashCore.players.get(pl).giveKitItems();
			pl.setGameMode(GameMode.ADVENTURE);
			pl.sendMessage(ChatColor.LIGHT_PURPLE + "[Smash] " + ChatColor.GOLD + "Game force ended");
			pl.setLevel(1);
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

		}, 20); // 20 Ticks * x seconds = Starts in x seconds
	}
	
	private static void deleteMap() {
		World delete = Bukkit.getWorld(SmashCore.currentMap);
		if (SmashCore.currentMap == "lobby" || SmashCore.currentMap == "lobby2") {
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
		SmashFileManager.deleteWorld(deleteFolder);
		return;
	}
	
	private static void deleteMap(String map) {
		World delete = Bukkit.getWorld(map);
		if (map == "lobby" || map == "lobby2") {
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
		SmashFileManager.deleteWorld(deleteFolder);
		return;
	}
	
	public static void setInGame(boolean inGame) {
		playing = inGame;
	}
	
	public static boolean inGame() {
		return playing;
	}
}
