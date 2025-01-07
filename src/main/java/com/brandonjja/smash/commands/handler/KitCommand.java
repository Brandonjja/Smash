package com.brandonjja.smash.commands.handler;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.brandonjja.smash.SmashCore;
import com.brandonjja.smash.commands.SmashCommand;
import com.brandonjja.smash.game.SmashPlayer;
import com.brandonjja.smash.kits.Kit;
import com.brandonjja.smash.kits.classes.Blink;
import com.brandonjja.smash.kits.classes.Jigglyo;
import com.brandonjja.smash.kits.classes.Metoo;
import com.brandonjja.smash.kits.classes.Pika;
import com.brandonjja.smash.kits.classes.Shadow;
import com.brandonjja.smash.kits.classes.Toshi;

public class KitCommand extends SmashCommand {

    private final Map<String, Kit> kitMap = new HashMap<>();
    private final String[] iHaveOcdList = {"Blink", "Metoo", "Pika", "Toshi", "Jigglyo"}; // For listing kits using /kits

    @Override
    public boolean execute(Player player, String[] args) {
        Map<String, Kit> kitMap = getKits();

        SmashPlayer smashPlayer = SmashCore.PLAYERS.get(player);

        // TODO: Remember to load new classes in KitLoader!!

        if (!SmashCore.currentMap.equalsIgnoreCase("lobby2")) {
            player.sendMessage(ChatColor.LIGHT_PURPLE + "[Smash] " + ChatColor.RED + "You can only choose kits from the lobby!");
            player.sendMessage(ChatColor.GOLD + "You are the " + ChatColor.GREEN + smashPlayer.getKit().getName() + ChatColor.GOLD + " class!");
            return true;
        }

        if (args.length == 0) {
            StringBuilder kitListMessage = new StringBuilder().append(ChatColor.GOLD).append("Kits: ").append(ChatColor.GREEN);
            for (String kitName : iHaveOcdList) {
                if (kitName.equalsIgnoreCase("shadow")) {
                    continue;
                }
                kitListMessage.append(kitName).append(", ");
            }

            kitListMessage.setLength(kitListMessage.length() - 2);
            player.sendMessage(kitListMessage.toString());
            player.sendMessage(ChatColor.GOLD + "You are the " + ChatColor.GREEN + smashPlayer.getKit().getName() + ChatColor.GOLD + " class!");
            return true;
        }

        if (args.length == 1) {
            String kit = args[0].toLowerCase();
            kit = kit.substring(0, 1).toUpperCase() + kit.substring(1);

            if (!kitMap.containsKey(kit)) {
                player.sendMessage(ChatColor.LIGHT_PURPLE + "[Smash] " + ChatColor.RED + "No kit \"" + kit + "\" found!");
                return false;
            }

            smashPlayer.setKit(kitMap.get(kit));

            player.sendMessage(smashPlayer.getKit().getKitMessage());
            player.getInventory().setContents(smashPlayer.getKit().getItems());
            player.getInventory().setHelmet(smashPlayer.getKit().getHelmet());
            player.updateInventory();
            smashPlayer.updateInventoryMap();
            smashPlayer.setKnockback(1);
            return true;
        }

        return false;
    }

    private Map<String, Kit> getKits() {
        if (kitMap.isEmpty()) {
            kitMap.put("Blink", new Blink());
            kitMap.put("Metoo", new Metoo());
            kitMap.put("Pika", new Pika());
            kitMap.put("Toshi", new Toshi());
            kitMap.put("Jigglyo", new Jigglyo());

            kitMap.put("Shadow", new Shadow());
        }

        return kitMap;
    }
}
