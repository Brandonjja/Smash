package com.brandonjja.smash.listeners.other;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class GiveCommandListener implements Listener{
	
	@EventHandler
	public void onGiveCommand(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().startsWith("/give ")) {
			String[] args = event.getMessage().split(" ");
			if (args.length == 3) {
				event.setCancelled(true);
				try {
				event.getPlayer().getInventory().addItem(new ItemStack(Material.matchMaterial(args[2].replace("minecraft:", ""))));
				} catch (NullPointerException ex) {
					String[] itemName = args[2].replace("minecraft:", "").split(":");
					if (itemName.length == 2) {
						try {
							short dur = Short.parseShort(itemName[1]);
							ItemStack item = new ItemStack(Material.matchMaterial(itemName[0]));
							item.setDurability(dur);
							event.getPlayer().getInventory().addItem(item);
						} catch (NullPointerException | NumberFormatException ex2) {
							ex2.printStackTrace();
							event.getPlayer().sendMessage(ChatColor.RED + "An error has occurred.");
							return;
						}
					}
				}
				event.getPlayer().sendMessage("Given [" + args[2].replace("_", " ").replace("minecraft:", "") + "] * 1 to " + event.getPlayer().getName());
			} else if (args.length == 4) {
				int items;
				try {
					items = Integer.parseInt(args[3]);
				} catch (NumberFormatException ex) {
					return;
				}
				event.setCancelled(true);
				try {
					event.getPlayer().getInventory().addItem(new ItemStack(Material.matchMaterial(args[2].replace("minecraft:", "")), items));
					} catch (NullPointerException ex) {
						String[] itemName = args[2].replace("minecraft:", "").split(":");
						if (itemName.length == 2) {
							try {
								short dur = Short.parseShort(itemName[1]);
								ItemStack item = new ItemStack(Material.matchMaterial(itemName[0]), items);
								item.setDurability(dur);
								event.getPlayer().getInventory().addItem(item);
							} catch (NullPointerException | NumberFormatException ex2) {
								ex2.printStackTrace();
								event.getPlayer().sendMessage(ChatColor.RED + "An error has occurred.");
								return;
							}
						}
					}
				event.getPlayer().sendMessage(
						"Given [" + args[2].replace("_", " ").replace("minecraft:", "") + "] * " + items + " to " + event.getPlayer().getName());

			}
		}
	}
}
