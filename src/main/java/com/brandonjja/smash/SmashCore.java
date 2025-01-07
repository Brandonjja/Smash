package com.brandonjja.smash;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.brandonjja.smash.commands.CommandManager;
import com.brandonjja.smash.game.SmashPlayer;
import com.brandonjja.smash.listeners.EventManager;

public class SmashCore {

    public static final Map<Player, SmashPlayer> PLAYERS = new HashMap<>();

    public static String currentMap;

    protected static void init() {

        CommandManager.registerCommands();
        EventManager.registerListeners();

        for (Player player : Bukkit.getOnlinePlayers()) {
            SmashPlayer smashPlayer = new SmashPlayer(player);
            PLAYERS.put(player, smashPlayer);
        }

        currentMap = "lobby2";

        loadConfig();
    }

    protected static void loadConfig() {
        Smash.getInstance().reloadConfig();
        Smash.getInstance().getConfig().options().copyDefaults(true);
        Smash.getInstance().saveConfig();
    }

    protected static void shutdown() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            SmashCore.PLAYERS.remove(player);
        }
    }
}
