package com.brandonjja.smash.listeners.other;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

public class GiveCommandListener implements Listener {

    @EventHandler
    public void onGiveCommand(PlayerCommandPreprocessEvent event) {
        if (!event.getMessage().startsWith("/give ")) {
            return;
        }

        Player player = event.getPlayer();
        String[] args = event.getMessage().split(" ");
        if (args.length == 3) {
            event.setCancelled(true);

            try {
                player.getInventory().addItem(new ItemStack(Material.matchMaterial(args[2].replace("minecraft:", ""))));
            } catch (NullPointerException ignored) {
                String[] itemName = args[2].replace("minecraft:", "").split(":");
                if (itemName.length == 2) {
                    try {
                        short durability = Short.parseShort(itemName[1]);
                        ItemStack item = new ItemStack(Material.matchMaterial(itemName[0]));
                        item.setDurability(durability);
                        player.getInventory().addItem(item);
                    } catch (NullPointerException | NumberFormatException ex) {
                        ex.printStackTrace();
                        player.sendMessage(ChatColor.RED + "An error has occurred.");
                        return;
                    }
                }
            }

            player.sendMessage("Given [" + args[2].replace("_", " ").replace("minecraft:", "") + "] * 1 to " + player.getName());
            return;
        }

        if (args.length == 4) {
            int items;
            try {
                items = Integer.parseInt(args[3]);
            } catch (NumberFormatException ignored) {
                return;
            }

            event.setCancelled(true);
            try {
                player.getInventory().addItem(new ItemStack(Material.matchMaterial(args[2].replace("minecraft:", "")), items));
            } catch (NullPointerException ex) {
                String[] itemName = args[2].replace("minecraft:", "").split(":");
                if (itemName.length == 2) {
                    try {
                        short durability = Short.parseShort(itemName[1]);
                        ItemStack item = new ItemStack(Material.matchMaterial(itemName[0]), items);
                        item.setDurability(durability);
                        player.getInventory().addItem(item);
                    } catch (NullPointerException | NumberFormatException ex2) {
                        ex2.printStackTrace();
                        player.sendMessage(ChatColor.RED + "An error has occurred.");
                        return;
                    }
                }
            }

            player.sendMessage("Given [" + args[2].replace("_", " ").replace("minecraft:", "") + "] * " + items + " to " + player.getName());
        }
    }
}
