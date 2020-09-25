package com.brandonjja.smash.listeners.other;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class GiveCommandListener implements Listener{
	
	@EventHandler
	public void onGiveCommand(PlayerCommandPreprocessEvent e) {
		if (e.getMessage().startsWith("/give ")) {
			String args[] = e.getMessage().split(" ");
			if (args.length == 3) {
				e.setCancelled(true);
				try {
				e.getPlayer().getInventory().addItem(new ItemStack(Material.matchMaterial(args[2].replace("minecraft:", ""))));
				} catch (NullPointerException ex) {
					String itemName[] = args[2].replace("minecraft:", "").split(":");
					if (itemName.length == 2) {
						try {
							short dur = Short.parseShort(itemName[1]);
							ItemStack item = new ItemStack(Material.matchMaterial(itemName[0]));
							item.setDurability(dur);
							e.getPlayer().getInventory().addItem(item);
						} catch (NullPointerException | NumberFormatException ex2) {
							ex2.printStackTrace();
							e.getPlayer().sendMessage(ChatColor.RED + "An error has occured.");
							return;
						}
					}
				}
				e.getPlayer().sendMessage("Given [" + args[2].replace("_", " ").replace("minecraft:", "") + "] * 1 to " + e.getPlayer().getName());
			} else if (args.length == 4) {
				int items;
				try {
					items = Integer.parseInt(args[3]);
				} catch (NumberFormatException ex) {
					return;
				}
				e.setCancelled(true);
				try {
					e.getPlayer().getInventory().addItem(new ItemStack(Material.matchMaterial(args[2].replace("minecraft:", "")), items));
					} catch (NullPointerException ex) {
						String itemName[] = args[2].replace("minecraft:", "").split(":");
						if (itemName.length == 2) {
							try {
								short dur = Short.parseShort(itemName[1]);
								ItemStack item = new ItemStack(Material.matchMaterial(itemName[0]), items);
								item.setDurability(dur);
								e.getPlayer().getInventory().addItem(item);
							} catch (NullPointerException | NumberFormatException ex2) {
								ex2.printStackTrace();
								e.getPlayer().sendMessage(ChatColor.RED + "An error has occured.");
								return;
							}
						}
					}
				//e.getPlayer().getInventory().addItem(new ItemStack(Material.matchMaterial(args[2].replace("minecraft:", "")), items));
				e.getPlayer().sendMessage(
						"Given [" + args[2].replace("_", " ").replace("minecraft:", "") + "] * " + items + " to " + e.getPlayer().getName());

			}
		}
	}
}
