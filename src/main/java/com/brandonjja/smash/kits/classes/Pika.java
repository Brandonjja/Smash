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

public class Pika extends Kit {
	private static List<ItemStack> items;
	private boolean lunge = true;
	private boolean strike = true;
	//private LightningStrike lightning = null;
	private List<LightningStrike> lightning = new ArrayList<>();

	public Pika() {
		super("Pika", 2);
	}

	@Override
	public String getName() {
		return super.name;
	}

	@Override
	public ItemStack[] getItems() {
		return items.toArray(new ItemStack[0]);
	}
	
	// Kit items
	static {
		items = new ArrayList<>();
		ItemStack ironSword = new ItemStack(Material.IRON_SWORD);
		ItemStack rocket = new ItemStack(Material.FIREWORK);
		ItemStack carrot = new ItemStack(Material.GOLDEN_CARROT);
		ItemStack axe = new ItemStack(Material.WOOD_AXE);

		ItemMeta meta = ironSword.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Sword");
		ironSword.setItemMeta(meta);
		ironSword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

		meta = rocket.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Double-Jump Rocket");
		rocket.setItemMeta(meta);

		meta = carrot.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Lunge");
		carrot.setItemMeta(meta);

		meta = axe.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Thunder Jolt");
		axe.setItemMeta(meta);
		axe.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

		items.add(ironSword);
		items.add(rocket);
		items.add(carrot);
		items.add(axe);
	}
	
	@Override
	public ItemStack getHelmet() {
		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta meta = (LeatherArmorMeta) helmet.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + super.name + " Helmet");
		meta.setColor(Color.YELLOW);
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
		return false;
	}

	@Override
	public void setCanGiveItem(Material item, boolean cooldown) {
		
	}
}
