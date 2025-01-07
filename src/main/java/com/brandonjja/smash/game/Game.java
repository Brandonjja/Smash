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

    private static final int POINTS_NEEDED_FOR_WIN = 5;
    private static final int ITEM_SPAWN_DELAY = 40; // Time between new items spawning around the map (seconds)

    private static boolean gameOver = false;
    private static boolean playing = false;

    private static int loopID = -1;

    public static boolean isGameOver() {
        return gameOver;
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

        String gameStartMsg = ChatColor.GRAY + "You are playing on " + ChatColor.AQUA + mapName + ChatColor.GRAY + "!" + "\n" + ChatColor.LIGHT_PURPLE + "Game starting in 5 seconds!";

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setGameMode(GameMode.ADVENTURE);
            player.sendMessage(gameStartMsg);

            SmashCore.PLAYERS.get(player).giveKitItems();
            player.setLevel(1);
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(Smash.getInstance(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(ChatColor.GOLD + "Game starting now!");
                SmashCore.PLAYERS.get(player).giveKitItems();
            }
            playing = true;
            runGameLoop();
        }, 20 * 5);

    }

    private static void runGameLoop() {
        loopID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Smash.getInstance(), () -> {
            SmashWorld.spawnItem();
            fixSlabs();
        }, 20L * 10, 20L * ITEM_SPAWN_DELAY);
    }

    private static void fixSlabs() {
        List<Location> safe = new ArrayList<>();
        Player player = (Player) Bukkit.getOnlinePlayers().toArray()[0];

        for (Entity entity : player.getWorld().getEntities()) {
            Location entityLocation = entity.getLocation();
            if (entity instanceof Player) {
                continue;
            }

            safe.add(entityLocation);
        }

        boolean[] needToUpdate = new boolean[SmashWorld.SLAB_LOCATIONS.size()];
        int ctr = 0;

        for (Location slabLocation : SmashWorld.SLAB_LOCATIONS) {
            for (Location safeLocation : safe) {
                if (safeLocation.getBlock().getType() != Material.STEP) {
                    needToUpdate[ctr] = false;
                    return;
                }

                needToUpdate[ctr] = true;
                if (slabLocation.getBlockX() == safeLocation.getBlockX() && slabLocation.getBlockY() == safeLocation.getBlockY() && slabLocation.getBlockZ() == safeLocation.getBlockZ()) {
                    needToUpdate[ctr] = false;
                    break;
                }
            }
            ctr++;
        }

        for (int i = 0; i < SmashWorld.SLAB_LOCATIONS.size(); i++) {
            if (needToUpdate[i]) {
                SmashWorld.SLAB_LOCATIONS.get(i).getBlock().setType(Material.AIR);
            }
        }
    }

    /**
     * Checks if a player has won the game
     */
    public static void checkEndGame() {
        List<Player> winners = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (ScoreboardManager.getScore(player) == POINTS_NEEDED_FOR_WIN) {
                winners.add(player);
                gameOver = true;
            }
        }

        if (gameOver) {
            triggerEndGame(winners);
        }
    }

    /**
     * Ends the game
     *
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
            StringBuilder names = new StringBuilder();
            names.append(ChatColor.GOLD);
            for (int i = 0; i < winners.size() - 1; i++) {
                names.append(winners.get(i));
                names.append(" and ");
            }
            names.append(winners.get(winners.size() - 1));
            winMsg = names + " draw with a score of " + ScoreboardManager.getScore(winners.get(0)) + "!";
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            SmashPlayer smashPlayer = SmashCore.PLAYERS.get(player);
            smashPlayer.giveKitItems();
            smashPlayer.setLastHitFrom(null);
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(ChatColor.GOLD + "Game Finished!");
            player.sendMessage(winMsg);
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(Smash.getInstance(), () -> {
            SmashFileManager.deleteLobby();
            SmashFileManager.createLobbyAndTeleport();
            deleteMap();
            SmashCore.currentMap = "lobby2";
            gameOver = false;
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.setLevel(1);
                ScoreboardManager.updateKB(player);
            }
        }, 20 * 8);
    }

    /**
     * Forcibly end a game before a player wins
     */
    public static void forceEnd() {
        playing = false;
        Bukkit.getScheduler().cancelTask(loopID);
        loopID = -1;
        String forceEndMsg = ChatColor.LIGHT_PURPLE + "[Smash] " + ChatColor.GOLD + "Game force ended";
        for (Player player : Bukkit.getOnlinePlayers()) {
            SmashPlayer smashPlayer = SmashCore.PLAYERS.get(player);
            smashPlayer.giveKitItems();
            smashPlayer.setLastHitFrom(null);
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(forceEndMsg);
            player.setLevel(1);
            ScoreboardManager.updateKB(player);
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(Smash.getInstance(), () -> {
            String map = ((Player) Bukkit.getOnlinePlayers().toArray()[0]).getWorld().getName();
            SmashFileManager.deleteLobby();
            SmashFileManager.createLobbyAndTeleport();
            deleteMap(map);
            SmashCore.currentMap = "lobby2";
            gameOver = false;

        }, 20);
    }

    /**
     * Deletes the current map being used in game
     */
    private static void deleteMap() {
        if (SmashCore.currentMap.equals("lobby") || SmashCore.currentMap.equals("lobby2")) {
            return;
        }
        deleteMap(SmashCore.currentMap);
    }

    /**
     * Deletes the map
     *
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
        }
    }

    public static boolean inGame() {
        return playing;
    }
}
