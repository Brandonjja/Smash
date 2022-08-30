package com.brandonjja.smash.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class Kit {
	protected String name;
	protected int jumps = 1;
	
	//Kit(String name, List<ItemStack> items) {
	protected Kit(String name, int jumps) {
		this.name = name;
		this.jumps = jumps;
		//this.items = new ArrayList<>();
	}
	
	public abstract String getName();
	public abstract ItemStack[] getItems();
	public abstract ItemStack getHelmet();
	public abstract boolean canGiveItem(Material item);
	public abstract void setCanGiveItem(Material item, boolean cooldown);
	//public abstract int getJumps();
	
	public int getJumps() {
		return jumps;
	}
	
	public String getKitMessage() {
		String message = ChatColor.GOLD + "Selected the " + ChatColor.GREEN + this.name + ChatColor.GOLD + " class!";
		return message;
	}
	
	//protected abstract void load();
	//protected abstract List<ItemStack> init();
}
