package com.brandonjja.smash.kits.classes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.Vector;

import com.brandonjja.smash.SmashCore;
import com.brandonjja.smash.game.SmashPlayer;
import com.brandonjja.smash.kits.Kit;

public class Toshi extends Kit {
	private static List<ItemStack> items;
	private boolean usedPound = false;
	private boolean poundCooldown;
	private boolean eggCooldown;

	public Toshi() {
		super("Toshi", 2);
		poundCooldown = true;
		eggCooldown = true;
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
		ItemStack brick = new ItemStack(Material.CLAY_BRICK);
		ItemStack egg = new ItemStack(Material.MONSTER_EGG, 1, (byte) 50);

		ItemMeta meta = ironSword.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Sword");
		ironSword.setItemMeta(meta);
		ironSword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

		meta = rocket.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Double-Jump Rocket");
		rocket.setItemMeta(meta);

		meta = brick.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Ground Pound");
		brick.setItemMeta(meta);

		meta = egg.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Toshi Egg");
		egg.setItemMeta(meta);

		items.add(ironSword);
		items.add(rocket);
		items.add(brick);
		items.add(egg);
	}

	@Override
	public ItemStack getHelmet() {
		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta meta = (LeatherArmorMeta) helmet.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + super.name + " Helmet");
		meta.setColor(Color.GREEN);
		helmet.setItemMeta(meta);
		
		helmet.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		
		return helmet;
	}
	
	public void setUsedPound(boolean usedPound) {
		this.usedPound = usedPound;
	}
	
	public boolean usedPound() {
		return usedPound;
	}
	
	public void usePound(Player player) {
		player.setNoDamageTicks(20);
		Location loc = player.getLocation();
		player.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 2, false, false);
		player.setVelocity(new Vector(0, 0, 0));
		for (Entity e : player.getNearbyEntities(2.5, 2, 2.5)) {
			if (e instanceof Player) {
				SmashPlayer smashVictim = SmashCore.players.get((Player) e);
				smashVictim.setLastHitFrom(player);
				smashVictim.setLastHitWeapon("Ground Pound");
			}
		}
	}

	@Override
	public boolean canGiveItem(Material item) {
		if (item == Material.CLAY_BRICK) {
			return poundCooldown;
		}
		if (item == Material.MONSTER_EGG) {
			return eggCooldown;
		}
		return false;
	}

	@Override
	public void setCanGiveItem(Material item, boolean cooldown) {
		if (item == null) {
			this.poundCooldown = false;
			this.eggCooldown = false;
			return;
		}
		if (item == Material.CLAY_BRICK) {
			this.poundCooldown = cooldown;
		}
		if (item == Material.MONSTER_EGG) {
			this.eggCooldown = cooldown;
		}
	}
}
