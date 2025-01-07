package com.brandonjja.smash.kits.classes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.brandonjja.smash.kits.Kit;

public class Blink extends Kit {

    private static final List<ItemStack> ITEMS = new ArrayList<>();

    // Kit items
    static {
        ItemStack ironSword = new ItemStack(Material.IRON_SWORD);
        ItemStack rocket = new ItemStack(Material.FIREWORK);
        ItemStack bow = new ItemStack(Material.BOW);
        ItemStack snowball = new ItemStack(Material.SNOW_BALL);
        ItemStack tnt = new ItemStack(Material.TNT);
        ItemStack arrow = new ItemStack(Material.ARROW);

        ItemMeta meta = ironSword.getItemMeta();
        meta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "Sword");
        ironSword.setItemMeta(meta);
        ironSword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

        meta = rocket.getItemMeta();
        meta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "Double-Jump Rocket");
        rocket.setItemMeta(meta);

        meta = bow.getItemMeta();
        meta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "Bow");
        bow.setItemMeta(meta);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        bow.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

        meta = snowball.getItemMeta();
        meta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "Switcher");
        snowball.setItemMeta(meta);

        meta = tnt.getItemMeta();
        meta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "TNT");
        tnt.setItemMeta(meta);

        ITEMS.add(ironSword);
        ITEMS.add(rocket);
        ITEMS.add(bow);
        ITEMS.add(snowball);
        ITEMS.add(tnt);
        ITEMS.add(arrow);
    }

    private boolean switcherCooldown;

    public Blink() {
        super("Blink", 3);
        switcherCooldown = true;
    }

    public String getName() {
        return super.name;
    }

    public ItemStack[] getItems() {
        return ITEMS.toArray(new ItemStack[0]);
    }

    @Override
    public ItemStack getHelmet() {
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta meta = (LeatherArmorMeta) helmet.getItemMeta();
        meta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + super.name + " Helmet");
        meta.setColor(Color.LIME);
        helmet.setItemMeta(meta);

        helmet.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

        return helmet;
    }

    @Override
    public boolean canGiveItem(Material item) {
        if (item == Material.SNOW_BALL) {
            return switcherCooldown;
        }
        return false;
    }

    @Override
    public void setCanGiveItem(Material item, boolean cooldown) {
        if (item == null) {
            this.switcherCooldown = false;
            return;
        }

        if (item == Material.SNOW_BALL) {
            this.switcherCooldown = cooldown;
        }
    }
}
