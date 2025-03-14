package com.brandonjja.smash.kits.classes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.brandonjja.smash.kits.Kit;

public class Jigglyo extends Kit {

    private static final List<ItemStack> ITEMS = new ArrayList<>();

    // Kit items
    static {
        ItemStack ironSword = new ItemStack(Material.IRON_SWORD);
        ItemStack rocket = new ItemStack(Material.FIREWORK);

        ItemMeta meta = ironSword.getItemMeta();
        meta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "Sword");
        ironSword.setItemMeta(meta);
        ironSword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

        meta = rocket.getItemMeta();
        meta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "Double-Jump Rocket");
        rocket.setItemMeta(meta);

        ITEMS.add(ironSword);
        ITEMS.add(rocket);
    }

    private int miniJumps;
    private int sneakTimer;

    public Jigglyo() {
        super("Jigglyo", 1);
        miniJumps = 5;
        sneakTimer = -1;
    }

    @Override
    public String getName() {
        return super.name;
    }

    @Override
    public ItemStack[] getItems() {
        return ITEMS.toArray(new ItemStack[0]);
    }

    @Override
    public ItemStack getHelmet() {
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta meta = (LeatherArmorMeta) helmet.getItemMeta();
        meta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + super.name + " Helmet");
        meta.setColor(Color.fromRGB(255, 182, 193)); // PINK
        helmet.setItemMeta(meta);

        helmet.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

        return helmet;
    }

    public void decrementMiniJumps() {
        miniJumps--;
    }

    public int getMiniJumps() {
        return miniJumps;
    }

    public void resetMiniJumps() {
        miniJumps = 5;
    }

    public void setSneakTimer(int sneakTimer) {
        this.sneakTimer = sneakTimer;
    }

    public void cancelSneakTimer() {
        if (sneakTimer != -1) {
            Bukkit.getScheduler().cancelTask(sneakTimer);
        }
        sneakTimer = -1;
    }

    @Override
    public boolean canGiveItem(Material item) {
        return false;
    }

    @Override
    public void setCanGiveItem(Material item, boolean cooldown) {
    }
}
