package com.brandonjja.smash.kits.classes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LightningStrike;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.brandonjja.smash.kits.Kit;

public class Shadow extends Kit {

    private static final List<ItemStack> ITEMS = new ArrayList<>();

    // Kit items
    static {
        ItemStack rocket = new ItemStack(Material.FIREWORK);
        ItemStack bow = new ItemStack(Material.BOW);
        ItemStack snowball = new ItemStack(Material.SNOW_BALL);
        ItemStack carrot = new ItemStack(Material.GOLDEN_CARROT);
        ItemStack axe = new ItemStack(Material.STONE_AXE);
        ItemStack arrow = new ItemStack(Material.ARROW);

        ItemMeta meta = axe.getItemMeta();
        meta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "Thunder Jolt");
        axe.setItemMeta(meta);
        axe.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

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

        meta = carrot.getItemMeta();
        meta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "Lunge");
        carrot.setItemMeta(meta);

        ITEMS.add(axe);
        ITEMS.add(rocket);
        ITEMS.add(bow);
        ITEMS.add(snowball);
        ITEMS.add(carrot);
        ITEMS.add(arrow);
    }

    private final List<LightningStrike> lightning = new ArrayList<>();

    private boolean switcherCooldown;
    private boolean lunge;
    private boolean strike = true;

    public Shadow() {
        super("Shadow", 2);
        switcherCooldown = true;
        lunge = true;
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
        meta.setColor(Color.BLACK);
        helmet.setItemMeta(meta);

        helmet.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

        return helmet;
    }

    public boolean canLunge() {
        return lunge;
    }

    public void setLunge(boolean lunge) {
        this.lunge = lunge;
    }

    public boolean canStrike() {
        return strike;
    }

    public void setStrike(boolean strike) {
        this.strike = strike;
    }

    public boolean getLightning(LightningStrike lightning) {
        return this.lightning.contains(lightning);
    }

    public void setLightning(LightningStrike lightning) {
        this.lightning.add(lightning);
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
