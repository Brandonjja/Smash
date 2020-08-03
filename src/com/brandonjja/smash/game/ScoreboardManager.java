package com.brandonjja.smash.game;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.brandonjja.smash.Smash;

import net.md_5.bungee.api.ChatColor;

public class ScoreboardManager {
	private static Scoreboard gameBoard;
	private static Objective obj, objKB;
	
	public static void newBoard() {
		gameBoard = Bukkit.getScoreboardManager().getNewScoreboard();
		obj = gameBoard.registerNewObjective("smash", "dummy");
		obj.setDisplayName(ChatColor.GOLD + "Score");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		for (Player pl : Bukkit.getOnlinePlayers()) {
			obj.getScore(pl.getName()).setScore(0);
		}
		
		objKB = gameBoard.registerNewObjective("%", "dummy");
		objKB.setDisplaySlot(DisplaySlot.BELOW_NAME);
		
		for (Player pl : Bukkit.getOnlinePlayers()) {
			objKB.getScore(pl.getName()).setScore(pl.getLevel());
		}
	}
	
	public static void giveScoreboard(Player player) {
		player.setScoreboard(gameBoard);
	}
	
	public static void updateKB(Player player) {
		try {
			objKB.getScore(player.getName()).setScore(player.getLevel());
		} catch (NullPointerException e) {
			Smash.getInstance().getLogger().log(Level.WARNING, "Scoreboard Error - ScoreboardManager:43. Are you in game?");
		}
	}
	
	public static void updateScore(Player player) {
		try {
			obj.getScore(player.getName()).setScore(obj.getScore(player.getName()).getScore() + 1);
		} catch (NullPointerException ex) {
			//ex.printStackTrace();
			Smash.getInstance().getLogger().log(Level.WARNING, "==- ScoreboardError: ScoreboardManager:47 -==");
			// This prob happened if a player fell off before the game
		}
	}
	
	public static void updateScore(Player player, int score) {
		obj.getScore(player.getName()).setScore(obj.getScore(player.getName()).getScore() + score);
	}
	
	public static int getScore(Player player) {
		try {
			return obj.getScore(player.getName()).getScore();
		} catch (NullPointerException e) {
			Smash.getInstance().getLogger().log(Level.WARNING, "Scoreboard Error - ScoreboardManager:65. Are you in game?");
			return 0;
		}
	}
}
