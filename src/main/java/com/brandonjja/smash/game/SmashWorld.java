package com.brandonjja.smash.game;

import com.brandonjja.smash.Smash;
import com.brandonjja.smash.SmashCore;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

public class SmashWorld {

    public static final List<Location> SLAB_LOCATIONS = new ArrayList<>();

    private static final List<Location> ITEM_LOCATIONS = new ArrayList<>();

    public static void updateBlocks() {
        SLAB_LOCATIONS.clear();
        ITEM_LOCATIONS.clear();

        World world = Bukkit.getWorld(SmashCore.currentMap);

        world.setStorm(false);
        world.setThundering(false);

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
                        if (block.getType() != Material.WOOL) {
                            continue;
                        }

                        Location blockLoc = block.getLocation();
                        Location location = new Location(world, blockLoc.getX(), blockLoc.getBlockY() - 1, blockLoc.getZ());

                        if (location.getBlock().getType() != Material.NETHER_FENCE) {
                            continue;
                        }

                        blockLoc.setY(location.getBlockY() + 1);
                        ITEM_LOCATIONS.add(blockLoc);
                        block.setType(Material.AIR);
                        block.getState().update();

                        SLAB_LOCATIONS.add(location);
                        location.getBlock().setType(Material.AIR);
                    }
                }
            }
        }
    }

    public static void spawnItem() {
        Random random = ThreadLocalRandom.current();
        int first;
        int second;
        try {
            first = random.nextInt(ITEM_LOCATIONS.size());
            second = random.nextInt(ITEM_LOCATIONS.size());
        } catch (IllegalArgumentException e) {
            Smash.getInstance().getLogger().log(Level.WARNING, "Item Spawn Error. Please use /end on this world, or stop the server and delete it");
            return;
        }

        while (first == second) {
            second = random.nextInt(ITEM_LOCATIONS.size());
        }

        if (SLAB_LOCATIONS.get(first).getBlock().getType() != Material.STEP) {
            SLAB_LOCATIONS.get(first).getBlock().setType(Material.STEP);
            ITEM_LOCATIONS.get(first).getWorld().dropItem(ITEM_LOCATIONS.get(first).getBlock().getLocation().add(0.5, 1, 0.5), randomItem()).setVelocity(new Vector(0, 0, 0));
        }

        if (SLAB_LOCATIONS.get(second).getBlock().getType() != Material.STEP) {
            SLAB_LOCATIONS.get(second).getBlock().setType(Material.STEP);
            ITEM_LOCATIONS.get(second).getWorld().dropItem(ITEM_LOCATIONS.get(second).getBlock().getLocation().add(0.5, 1, 0.5), randomItem()).setVelocity(new Vector(0, 0, 0));
        }
    }

    // Update ItemPickUpListener
    // Update PlayerInteractListener

    private static ItemStack randomItem() {
        List<ItemStack> items = new ArrayList<>();
        ItemMeta meta;

        ItemStack hammer = new ItemStack(Material.IRON_AXE);
        meta = hammer.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Hammer");

        ItemStack feather = new ItemStack(Material.FEATHER);
        meta = feather.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Swift Feather");

        ItemStack apple = new ItemStack(Material.APPLE);
        ItemStack gApple = new ItemStack(Material.GOLDEN_APPLE);
        ItemStack bat = new ItemStack(Material.STICK);
        ItemStack landMine = new ItemStack(Material.STONE_PLATE);
        ItemStack tnt = new ItemStack(Material.TNT);

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

        return items.get(ThreadLocalRandom.current().nextInt(items.size()));
    }
}
