package com.brandonjja.smash.kits.classes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.brandonjja.smash.kits.Kit;

import net.md_5.bungee.api.ChatColor;

public class Jiggly extends Kit {
	private static List<ItemStack> items;
	private int miniJumps;
	private int sneakTimer;
	
	public Jiggly() {
		super("Jiggly", 1);
		miniJumps = 5;
		sneakTimer = -1;
	}

	@Override
	public String getName() {
		return super.name;
	}

	@Override
	public ItemStack[] getItems() {
		return items.toArray(new ItemStack[0]);
	}
	
	public static void load() {
		items = new ArrayList<>();
		ItemStack ironSword = new ItemStack(Material.IRON_SWORD);
		ItemStack rocket = new ItemStack(Material.FIREWORK);

		ItemMeta meta = ironSword.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Sword");
		ironSword.setItemMeta(meta);
		ironSword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

		meta = rocket.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Double-Jump Rocket");
		rocket.setItemMeta(meta);

		items.add(ironSword);
		items.add(rocket);
	}

	@Override
	public ItemStack getHelmet() {
		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta meta = (LeatherArmorMeta) helmet.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Jiggly Helment");
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
	
	public int getSneakTimer() {
		return sneakTimer;
	}
	
	public void cancelSneakTimer() {
		Bukkit.getScheduler().cancelTask(sneakTimer);
	}

	@Override
	public boolean canGiveItem(Material item) {
		return false;
	}

	@Override
	public void setCanGiveItem(Material item, boolean cooldown) {}
}
