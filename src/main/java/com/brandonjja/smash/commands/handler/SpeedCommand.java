package com.brandonjja.smash.commands.handler;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.brandonjja.smash.commands.SmashCommand;

public class SpeedCommand extends SmashCommand {

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.YELLOW + "Current fly speed: " + ChatColor.GREEN + (player.getFlySpeed() * 10));
            return true;
        }

        if (args.length == 1) {
            try {
                double newSpeed = Double.parseDouble(args[0]);
                if (newSpeed <= 0) {
                    return false;
                }

                newSpeed /= 10;
                player.setFlySpeed((float) newSpeed);
                player.sendMessage(ChatColor.YELLOW + "Fly speed set to: " + ChatColor.GREEN + (newSpeed * 10));
                return true;
            } catch (IllegalArgumentException ex) {
                return false;
            }
        }
        return false;
    }
}
