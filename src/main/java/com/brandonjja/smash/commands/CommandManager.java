package com.brandonjja.smash.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.brandonjja.smash.commands.handler.EndCommand;
import com.brandonjja.smash.commands.handler.KitCommand;
import com.brandonjja.smash.commands.handler.MapsCommand;
import com.brandonjja.smash.commands.handler.SetSpawnCommand;
import com.brandonjja.smash.commands.handler.SpeedCommand;
import com.brandonjja.smash.commands.handler.StartCommand;
import com.brandonjja.smash.commands.handler.TpwCommand;
import com.brandonjja.smash.commands.handler.WorldCommand;

public class CommandManager implements CommandExecutor {

    private static final Map<String, SmashCommand> COMMANDS = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        return COMMANDS.get(commandLabel).execute((Player) sender, args);
    }

    public static void registerCommands() {
        COMMANDS.put("kit", new KitCommand());
        COMMANDS.put("kits", new KitCommand());
        COMMANDS.put("speed", new SpeedCommand());
        COMMANDS.put("world", new WorldCommand());
        COMMANDS.put("start", new StartCommand());
        COMMANDS.put("tpw", new TpwCommand());
        COMMANDS.put("end", new EndCommand());
        COMMANDS.put("maps", new MapsCommand());
        COMMANDS.put("setspawn", new SetSpawnCommand());

        for (String cmd : COMMANDS.keySet()) {
            register(cmd, new CommandManager());
        }
    }

    private static void register(String label, CommandExecutor command) {
        Bukkit.getPluginCommand(label).setExecutor(command);
    }

}