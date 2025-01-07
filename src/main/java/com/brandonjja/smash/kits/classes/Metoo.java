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

public class Metoo extends Kit {

    private static final List<ItemStack> ITEMS = new ArrayList<>();

    // Kit items
    static {
        ItemStack ironSword = new ItemStack(Material.IRON_SWORD);
        ItemStack rocket = new ItemStack(Material.FIREWORK);
        ItemStack pearl = new ItemStack(Material.ENDER_PEARL);
        ItemStack snowball = new ItemStack(Material.SNOW_BALL);

        ItemMeta meta = ironSword.getItemMeta();
        meta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "Sword");
        ironSword.setItemMeta(meta);
        ironSword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

        meta = rocket.getItemMeta();
        meta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "Double-Jump Rocket");
        rocket.setItemMeta(meta);

        meta = pearl.getItemMeta();
        meta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "Teleporter");
        pearl.setItemMeta(meta);

        meta = snowball.getItemMeta();
        meta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "Psychic Orb");
        snowball.setItemMeta(meta);

        ITEMS.add(ironSword);
        ITEMS.add(rocket);
        ITEMS.add(pearl);
        ITEMS.add(snowball);
    }

    private boolean pearlCooldown;
    private boolean orbCooldown;
    private boolean doTeleport;

    public Metoo() {
        super("Metoo", 2);
        pearlCooldown = true;
        orbCooldown = true;
        doTeleport = true;
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
        meta.setColor(Color.BLUE);
        helmet.setItemMeta(meta);

        helmet.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

        return helmet;
    }

    @Override
    public boolean canGiveItem(Material item) {
        if (item == Material.ENDER_PEARL) {
            return pearlCooldown;
        }

        if (item == Material.SNOW_BALL) {
            return orbCooldown;
        }

        return false;
    }

    @Override
    public void setCanGiveItem(Material item, boolean cooldown) {
        if (item == null) {
            this.pearlCooldown = false;
            this.orbCooldown = false;
            return;
        }

        if (item == Material.ENDER_PEARL) {
            this.pearlCooldown = cooldown;
        }

        if (item == Material.SNOW_BALL) {
            this.orbCooldown = cooldown;
        }
    }

    public void setTeleport(boolean doTeleport) {
        this.doTeleport = doTeleport;
    }

    public boolean canTeleport() {
        return doTeleport;
    }
}
