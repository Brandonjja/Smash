package com.brandonjja.smash.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardManager {
	private static Scoreboard gameBoard;
	private static Objective objective, kbLevelObjective;
	private final static int scoreOne = 1;
	
	/** Creates a new game Scoreboard, setting everybody's score to zero */
	public static void newBoard() {
		gameBoard = Bukkit.getScoreboardManager().getNewScoreboard();
		objective = gameBoard.registerNewObjective("smash", "dummy");
		objective.setDisplayName(ChatColor.GOLD + "Score");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			objective.getScore(player.getName()).setScore(0);
		}
		
		kbLevelObjective = gameBoard.registerNewObjective("%", "dummy");
		kbLevelObjective.setDisplaySlot(DisplaySlot.BELOW_NAME);
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			kbLevelObjective.getScore(player.getName()).setScore(player.getLevel());
		}
	}
	
	/**
	 * Gives the Scoreboard to a player. The Scoreboard may already contain
	 * score values, if given after a game has started
	 * 
	 * @param player the player to give the Scoreboard to
	 */
	public static void giveScoreboard(Player player) {
		player.setScoreboard(gameBoard);
	}
	
	/** Updates the given player's knockback level displayed above their head */
	public static void updateKB(Player player) {
		try {
			kbLevelObjective.getScore(player.getName()).setScore(player.getLevel());
		} catch (NullPointerException ex) {
			// No scoreboard to update. Most likely not in game
		}
	}
	
	/**
	 * Updates the player's score on the Scoreboard by 1
	 * 
	 * @param player the player who is getting their score changed
	 */
	public static void updateScore(Player player) {
		try {
			objective.getScore(player.getName()).setScore(objective.getScore(player.getName()).getScore() + scoreOne);
		} catch (NullPointerException ex) {
			// This prob happened if a player fell off before the game started
		}
	}
	
	/**
	 * Updates the player's score on the Scoreboard by the amount given
	 * 
	 * @param player the player who is getting their score changed
	 * @param score the amount to modify the player's score by
	 */
	public static void updateScore(Player player, int score) {
		objective.getScore(player.getName()).setScore(objective.getScore(player.getName()).getScore() + score);
	}
	
	/**
	 * Retrieves the score of a given player
	 * 
	 * @param player the player to get the score of
	 * @return the score of the player, or zero if there is no current game
	 */
	public static int getScore(Player player) {
		try {
			return objective.getScore(player.getName()).getScore();
		} catch (NullPointerException ex) {
			return 0; // Not in a game, so just return zero
		}
	}
}
