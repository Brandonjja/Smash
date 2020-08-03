package com.brandonjja.smash.kits.classes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.brandonjja.smash.kits.Kit;

import net.md_5.bungee.api.ChatColor;

public class Metoo extends Kit {
	private static List<ItemStack> items;
	private boolean pearlCooldown;
	private boolean orbCooldown;

	public Metoo() {
		super("Metoo", 2);
		pearlCooldown = true;
		orbCooldown = true;
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
		ItemStack pearl = new ItemStack(Material.ENDER_PEARL);
		ItemStack snowball = new ItemStack(Material.SNOW_BALL);

		ItemMeta meta = ironSword.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Sword");
		ironSword.setItemMeta(meta);
		ironSword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

		meta = rocket.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Double-Jump Rocket");
		rocket.setItemMeta(meta);

		meta = pearl.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Teleporter");
		pearl.setItemMeta(meta);

		meta = snowball.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Psychic Orb");
		snowball.setItemMeta(meta);

		items.add(ironSword);
		items.add(rocket);
		items.add(pearl);
		items.add(snowball);
	}
	
	@Override
	public ItemStack getHelmet() {
		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta meta = (LeatherArmorMeta) helmet.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Metoo Helment");
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
}
