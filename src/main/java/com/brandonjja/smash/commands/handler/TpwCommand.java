package com.brandonjja.smash.commands.handler;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import com.brandonjja.smash.commands.SmashCommand;

public class TpwCommand extends SmashCommand {

    @Override
    public boolean execute(Player player, String[] args) {
        if (!player.isOp()) {
            return false;
        }

        if (args.length >= 1) {
            StringBuilder name = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                name.append(args[i]);
                if (i != args.length - 1) {
                    name.append(" ");
                }
            }

            String lowerCaseName = name.toString().toLowerCase();
            String worldPath = getFilePath(lowerCaseName);
            if (worldPath == null) {
                player.sendMessage("Cannot find a world with the name: " + lowerCaseName);
                return true;
            }

            try {
                World world = Bukkit.getWorld(worldPath);
                Location spawnLocation = world.getSpawnLocation();
                player.teleport(new Location(world, spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ()));
            } catch (NullPointerException ex) {
                World world = Bukkit.createWorld(new WorldCreator(worldPath));
                Location spawnLocation = world.getSpawnLocation();
                player.teleport(new Location(world, spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ()));
            }
            return true;
        }
        return false;
    }

    private String getFilePath(String name) {
        File path = Bukkit.getWorldContainer();
        for (File file : path.listFiles()) {
            if (file.getName().equalsIgnoreCase(name)) {
                return file.getName();
            }
        }

        try {
            String mapsPath = path.getCanonicalPath() + "/maps/";
            File dir = new File(mapsPath);
            for (File file : dir.listFiles()) {
                if (file.getName().equalsIgnoreCase(name)) {
                    return file.getParentFile().getName() + "\\" + file.getName();
                }
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
