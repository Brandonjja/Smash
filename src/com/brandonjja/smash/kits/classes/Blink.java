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

public class Blink extends Kit {
	private static List<ItemStack> items;
	private boolean switcherCooldown;
	
	public Blink() {
		super("Blink", 3);
		switcherCooldown = true;
	}
	
	public String getName() {
		return super.name;
	}
	
	public ItemStack[] getItems() {
		//return Arrays.fill(items);;
		/*int size = items.size();
		ItemStack arr[] = new ItemStack[size];
		for (int i = 0; i < size; i++) {
			arr[i] = items.get(i);
		}*/
		
		return items.toArray(new ItemStack[0]);
		//return arr;
	}
	
	public static void load() {
		items = new ArrayList<>();
		ItemStack ironSword = new ItemStack(Material.IRON_SWORD);
		ItemStack rocket = new ItemStack(Material.FIREWORK);
		ItemStack bow = new ItemStack(Material.BOW);
		ItemStack snowball = new ItemStack(Material.SNOW_BALL);
		ItemStack tnt = new ItemStack(Material.TNT);
		ItemStack arrow = new ItemStack(Material.ARROW);

		ItemMeta meta = ironSword.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Sword");
		ironSword.setItemMeta(meta);
		ironSword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

		meta = rocket.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Double-Jump Rocket");
		rocket.setItemMeta(meta);

		meta = bow.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Bow");
		bow.setItemMeta(meta);
		bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		bow.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

		meta = snowball.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Switcher");
		snowball.setItemMeta(meta);

		meta = tnt.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "TNT");
		tnt.setItemMeta(meta);

		items.add(ironSword);
		items.add(rocket);
		items.add(bow);
		items.add(snowball);
		//items.add(tnt);
		items.add(arrow);
	}
	
	public int getJumps() {
		return super.jumps;
	}

	@Override
	public ItemStack getHelmet() {
		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta meta = (LeatherArmorMeta) helmet.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + super.name + " Helmet");
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
	
	
	
	/*public void init() {
		System.out.println("test");
	}*/
}
