package com.brandonjja.smash.listeners.other;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.meta.ItemMeta;

import com.brandonjja.smash.Smash;
import com.brandonjja.smash.SmashCore;
import com.brandonjja.smash.game.SmashPlayer;

public class ItemPickUpListener implements Listener {
	
	@EventHandler
	public void onPickUp(PlayerPickupItemEvent e) {
		Item item = e.getItem();
		Player player = e.getPlayer();
		SmashPlayer smashPlayer = SmashCore.players.get(player);
		Location loc = item.getLocation();
		
		//List<Block> safe = new ArrayList<>();
		
		//if (loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockY()));
		
		if (player.getInventory().contains(Material.IRON_AXE)) {
			e.setCancelled(true);
			return;
		}
		
		if (loc.getWorld().getBlockAt(loc).getType() == Material.STEP) {
			
			/*if (item.getItemStack().getType() == Material.IRON_AXE) {
				if (player.getInventory().contains(Material.IRON_AXE)) {
					e.setCancelled(true);
					return;
				}
			}*/
			
			loc.getWorld().getBlockAt(loc).setType(Material.AIR);
		} else {
			/*for (Entity entity : player.getLocation().getChunk().getEntities()) {
				if (loc.getWorld().getBlockAt(loc).getType() == Material.STEP) {
					safe.add(loc.getWorld().getBlockAt(loc));
				}
			}*/
		}
		
		//ItemStack itemStack = new ItemStack(item.getItemStack());
		ItemMeta meta;
		meta = item.getItemStack().getItemMeta();
		// HAMMER
		if (item.getItemStack().getType() == Material.IRON_AXE) {
			
			if (player.getInventory().contains(Material.IRON_AXE)) {
				e.setCancelled(true);
				return;
			}
			
			meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Hammer");
			//itemStack.setItemMeta(meta);
			//item.setItemStack(itemStack);
			item.getItemStack().setItemMeta(meta);
			smashPlayer.saveInventory();
			player.getInventory().clear();
			for (int i = 0; i < 8; i++) {
				player.getInventory().addItem(item.getItemStack());
			}
			
			int cooldown = Bukkit.getScheduler().scheduleSyncDelayedTask(Smash.getInstance(), new Runnable() {
				@Override
				public void run() {
					//SmashPlayer p = SmashCore.players.get(player);
					player.getInventory().clear();
					//player.getInventory().setContents(p.getKit().getItems());
					player.getInventory().setContents(smashPlayer.getInventory());
					smashPlayer.addMissingItemsAfterHammer();
					//player.getInventory().setHelmet(p.getKit().getHelmet());
					player.updateInventory();
				}

			}, 20 * 5); // 20 Ticks * x seconds = Starts in x seconds
			smashPlayer.setHammerCooldown(cooldown);
		}
		
		// SWIFT FEATHER
		if (item.getItemStack().getType() == Material.FEATHER) {
			meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Swift Feather");
		}
		
		if (item.getItemStack().getType() == Material.APPLE) {
			meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Maxim Apple");
		}
		
		if (item.getItemStack().getType() == Material.GOLDEN_APPLE) {
			meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Golden Maxim Apple");
		}
		
		if (item.getItemStack().getType() == Material.STICK) {
			meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Home-Run Bat");
			item.getItemStack().setItemMeta(meta);
			e.setCancelled(true);
			e.getItem().remove();
			if (player.getInventory().containsAtLeast(item.getItemStack(), 1)) {
				player.getInventory().addItem(item.getItemStack());
			} else {
				player.getInventory().setItem(smashPlayer.getNextSlot(), item.getItemStack());
			}
			// Add 2 extra
			player.getInventory().addItem(item.getItemStack());
			//player.getInventory().addItem(item.getItemStack());
			return;
		}
		
		if (item.getItemStack().getType() == Material.TNT) {
			meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "TNT");
		}
		
		if (item.getItemStack().getType() == Material.STONE_PLATE) {
			meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Land Mine");
		}
		
		item.getItemStack().setItemMeta(meta);
		e.setCancelled(true);
		e.getItem().remove();
		if (player.getInventory().containsAtLeast(item.getItemStack(), 1)) {
			player.getInventory().addItem(item.getItemStack());
		} else {
			player.getInventory().setItem(smashPlayer.getNextSlot(), item.getItemStack());
		}
	}
}
