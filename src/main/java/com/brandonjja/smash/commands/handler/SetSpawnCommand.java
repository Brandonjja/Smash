package com.brandonjja.smash.commands.handler;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.brandonjja.smash.commands.SmashCommand;

public class SetSpawnCommand extends SmashCommand {

    @Override
    public boolean execute(Player player, String[] args) {
        if (!player.isOp()) {
            return false;
        }

        World world = player.getWorld();
        Location playerLocation = player.getLocation();
        world.setSpawnLocation(playerLocation.getBlockX(), playerLocation.getBlockY(), playerLocation.getBlockZ());
        world.setKeepSpawnInMemory(true);
        player.sendMessage(ChatColor.LIGHT_PURPLE + "[Smash] " + ChatColor.GREEN + "Spawn Set!");
        return true;
    }
}
