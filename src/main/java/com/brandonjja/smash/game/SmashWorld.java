package com.brandonjja.smash.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.brandonjja.smash.Smash;
import com.brandonjja.smash.SmashCore;

public class SmashWorld {
	
	static List<Block> type = new ArrayList<>();
	static List<Location> itemLoc = new ArrayList<>();
	static List<Location> slabLoc = new ArrayList<>();
	
	public static void updateBlocks() {
		type = new ArrayList<>();
		itemLoc = new ArrayList<>();
		slabLoc = new ArrayList<>();
		
		World world = Bukkit.getWorld(SmashCore.currentMap);
		
		world.setStorm(false);
		world.setThundering(false);
		
		long time = System.currentTimeMillis();
		for (Chunk chunk : world.getLoadedChunks()) {
			
			final int minX = chunk.getX() << 4;
	        final int minZ = chunk.getZ() << 4;
	        final int maxX = minX | 15;
			final int maxY = chunk.getWorld().getMaxHeight();
			final int maxZ = minZ | 15;

			for (int x = minX; x <= maxX; ++x) {
				for (int y = 0; y <= maxY; ++y) {
					for (int z = minZ; z <= maxZ; ++z) {
						Block block = chunk.getBlock(x, y, z);
						if (block.getType() == Material.WOOL) {
							Location blockLoc = block.getLocation();
							Location loc = new Location(world, blockLoc.getX(), blockLoc.getBlockY() - 1, blockLoc.getZ());

							if (loc.getBlock().getType() == Material.NETHER_FENCE) {
								blockLoc.setY(loc.getBlockY() + 1);
								type.add(block);
								itemLoc.add(blockLoc);
								block.setType(Material.AIR);
								block.getState().update();

								slabLoc.add(loc);
								loc.getBlock().setType(Material.AIR);
							}

						}
					}
				}
			}
		}
		long end = System.currentTimeMillis();
	}
	
	public static void spawnItem() {
		Random r = new Random();
		int first = 0;
		int second = 0;
		try {
			first = r.nextInt(itemLoc.size());
			second = r.nextInt(itemLoc.size());
		} catch (IllegalArgumentException e) {
			Smash.getInstance().getLogger().log(Level.WARNING, "Item Spawn Error - SmashWorld:108. Please use /end on this world, or stop the server and delete it");
			return;
		}
		
		while (first == second) {
			second = r.nextInt(itemLoc.size());
		}
		
		if (slabLoc.get(first).getBlock().getType() != Material.STEP) {
			slabLoc.get(first).getBlock().setType(Material.STEP);
			itemLoc.get(first).getWorld().dropItem(itemLoc.get(first).getBlock().getLocation().add(+0.5, +1, +0.5), randomItem()).setVelocity(new Vector(0, 0, 0));
		}
		
		if (slabLoc.get(second).getBlock().getType() != Material.STEP) {
			slabLoc.get(second).getBlock().setType(Material.STEP);
			itemLoc.get(second).getWorld().dropItem(itemLoc.get(second).getBlock().getLocation().add(+0.5, +1, +0.5), randomItem()).setVelocity(new Vector(0, 0, 0));
		}
	}
	
	// Update ItemPickUpListener
	// Update PlayerInteractListener
	
	private static ItemStack randomItem() {
		List<ItemStack> items = new ArrayList<>();
		TreeMap<Integer, ItemStack> map = new TreeMap<>();
		ItemMeta meta;
		
		Random r = new Random();
		
		ItemStack hammer = new ItemStack(Material.IRON_AXE);
		meta = hammer.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Hammer");
		
		ItemStack feather = new ItemStack(Material.FEATHER);
		meta = feather.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Swift Feather");
		
		ItemStack apple = new ItemStack(Material.APPLE);
		ItemStack gApple = new ItemStack(Material.GOLDEN_APPLE);
		ItemStack bat = new ItemStack(Material.STICK);
		ItemStack landMine = new ItemStack(Material.STONE_PLATE);
		ItemStack tnt = new ItemStack(Material.TNT);
		
		map.put(1, hammer);
		map.put(5, feather);
		map.put(3, apple);
		map.put(2, gApple);
		map.put(4, bat);
		map.put(6, landMine);
		map.put(7, tnt);
		
		
		
		items.add(hammer);
		
		items.add(feather);
		items.add(feather);
		items.add(feather);
		items.add(feather);
		
		items.add(apple);
		items.add(apple);
		
		items.add(gApple);
		
		items.add(bat);
		items.add(bat);
		
		items.add(landMine);
		items.add(landMine);
		items.add(landMine);
		items.add(landMine);
		
		items.add(tnt);
		items.add(tnt);
		items.add(tnt);
		items.add(tnt);
		items.add(tnt);
		items.add(tnt);
		
		return items.get(r.nextInt(items.size()));
	}
}
