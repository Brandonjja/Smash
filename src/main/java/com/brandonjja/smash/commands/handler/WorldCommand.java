package com.brandonjja.smash.commands.handler;

import org.bukkit.entity.Player;

import com.brandonjja.smash.commands.SmashCommand;

public class WorldCommand extends SmashCommand {

    @Override
    public boolean execute(Player player, String[] args) {
        player.sendMessage(player.getWorld().getName());
        return true;
    }

}
