package com.brandonjja.smash.game;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.brandonjja.smash.Smash;
import com.brandonjja.smash.SmashCore;
import com.brandonjja.smash.kits.Kit;
import com.brandonjja.smash.kits.classes.Blink;
import com.brandonjja.smash.kits.classes.Jigglyo;
import com.brandonjja.smash.kits.classes.Shadow;

public class SmashPlayer {
	private Kit kit;
	private Player player;
	private String weapon = null;
	private boolean firstJump = true;
	public Map<Integer, ItemStack> inventorySlot;
	//private List<Material> kitItems;
	private int hammerCooldown;
	
	private Player lastPlayerToHit = null; // the player that hit you last
	// set this player from entity attack (the attacker)
	// on KO, use this player for the chat
	
	private int knockback = 0;
	private int jumpsLeft = 0;
	
	private ItemStack[] inventory;
	
	public SmashPlayer(Player player) {
		this.player = player;
		this.kit = new Blink();
		//this.addKitItems();
		this.giveKitItems();
	}
	
	/*private void addKitItems() {
		kitItems = new ArrayList<>();
		kitItems.add(Material.STONE_AXE);
		kitItems.add(Material.IRON_SWORD);
		kitItems.add(Material.FIREWORK);
		kitItems.add(Material.BOW);
		kitItems.add(Material.ARROW);
		kitItems.add(Material.SNOW_BALL);
		kitItems.add(Material.ENDER_PEARL);
		kitItems.add(Material.GOLDEN_CARROT);
		kitItems.add(Material.WOOD_AXE);
		kitItems.add(Material.CLAY_BRICK);
		kitItems.add(Material.MONSTER_EGG);
	}*/
	
	public boolean getFirstJump() {
		return firstJump;
	}
	
	public void setFirstJump(boolean firstJump) {
		this.firstJump = firstJump;
	}
	
	public Kit getKit() {
		return kit;
	}
	
	public void setKit(Kit kit) {
		this.kit = kit;
		jumpsLeft = kit.getJumps();
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public int getKnockback() {
		return knockback;
	}
	
	public void setKnockback(int knockback) {
		this.knockback = knockback;
		this.player.setLevel(knockback);
		this.player.setExp(0.99f);
		//this.player.setExp(((float)knockback % 100) / 100);
	}
	
	public boolean hasJump() {
		if (jumpsLeft > 0) {
			return true;
		}
		return false;
	}
	
	public void decrementJumps() {
		--jumpsLeft;
		if (jumpsLeft == kit.getJumps() - 1) {
			firstJump = true;
		}
		if (kit instanceof Jigglyo) {
			player.setExp(player.getExp() - (1 / (float) ( ((Jigglyo)kit).getJumps() + 5)) );
			firstJump = true;
			if (((Jigglyo)kit).getMiniJumps() == 0) {
				player.setExp(0f);
			}
			return;
		}
		if (jumpsLeft == 0) {
			player.setExp(0f);
		}
		player.setExp(player.getExp() - (1 / (float) kit.getJumps()));
		firstJump = true;
		if (jumpsLeft == 0) {
			player.setExp(0f);
		}
	}
	
	public void resetJumps() {
		jumpsLeft = kit.getJumps();
		player.setExp(0.99f);
		if (kit instanceof Jigglyo) {
			((Jigglyo) kit).resetMiniJumps();
		}
	}
	
	public int getJumpsLeft() {
		return jumpsLeft;
	}
	
	public Player getLastHitFrom() {
		return lastPlayerToHit;
	}
	
	public void setLastHitFrom(Player player) {
		this.lastPlayerToHit = player;
	}
	
	public String getLastHitWeapon() {
		return weapon;
	}
	
	public void setLastHitWeapon(String weapon) {
		this.weapon = weapon;
	}
	
	public void saveInventory() {
		inventory = player.getInventory().getContents();
		/*boolean contains = false;
		for (int key : inventorySlot.keySet()) {
			for (int i = 0; i < inventory.length; i++) {
				if (inventorySlot.get(key).getType() == inventory[i].getType()) {
					contains = true;
					break;
				}
			}
			if (!contains) {
				inventory = new ItemStack[inventory.length + 1];
				
			}
		}*/
	}
	
	public ItemStack[] getInventory() {
		return inventory;
	}
	
	public void addMissingItemsAfterHammer() {
		//for (ItemStack item : inventorySlot.values()) {
		for (ItemStack item : kit.getItems()) {
			if (!player.getInventory().contains(item.getType())) {
				item.setAmount(1);
				player.getInventory().addItem(item);
				player.sendMessage("added " + item.getType());
			}
		}
		player.updateInventory();
	}
	
	public void setInventoryMap() {
		
	}
	
	public void giveKitItems() {
		if (inventorySlot == null) {
			player.getInventory().clear();
			player.getInventory().setContents(kit.getItems());
			player.getInventory().setHelmet(kit.getHelmet());
			player.updateInventory();
			updateInventoryMap();
		} else {
			getInventoryMapItems();
		}
	}
	
	public void updateInventoryMap() {
		inventorySlot = null;
		inventorySlot = new HashMap<>();
		for (int i = 0; i < 36; i++) {
			ItemStack item = player.getInventory().getItem(i);
			if (item == null) {
				continue;
			}
			//if (kitItems.contains(item.getType())) {
			if (item.getType() != Material.MONSTER_EGG) {
				item.setDurability((short) 0);
			}
			if (Arrays.asList(kit.getItems()).contains(item)) {
				//player.sendMessage("contains " + item.getType().toString() + item.getDurability());
				
			//if ()
				if (item.getType() == Material.IRON_AXE && !(kit instanceof Shadow)) {
					return;
				}
				inventorySlot.put(i, item);
			}
		}
		
		for (ItemStack kitItem : kit.getItems()) {
			if (!player.getInventory().contains(kitItem)) {
				inventorySlot.put(getNextSlot(), kitItem);
			}
		}
		
	}
	
	private void getInventoryMapItems() {
		player.getInventory().clear();
		for (int i : inventorySlot.keySet()) {
			if (inventorySlot.get(i).getAmount() == 0) {
				/*ItemStack dupeFix = new ItemStack(inventorySlot.get(i).getType(), 1);
				inventorySlot.put(i, dupeFix);*/
				inventorySlot.get(i).setAmount(1); // Fixes dupe bug where you could get negative pearls, that multiply
			}
			player.getInventory().setItem(i, inventorySlot.get(i));
		}
		player.getInventory().setHelmet(kit.getHelmet());
		player.updateInventory();
	}
	
	public int getNextSlot() {
		for (Integer i = 0; i < 36; i++) {
			if (!inventorySlot.containsKey(i) && player.getInventory().getItem(i) == null) {
				return i;
			}
		}
		return 35;
	}
	
	public void cancelHammerCooldown() {
		Bukkit.getScheduler().cancelTask(hammerCooldown);
		hammerCooldown = -1;
	}
	
	public void setHammerCooldown(int hammerCooldown) {
		this.hammerCooldown = hammerCooldown;
	}
	
	public void runItemTimer(Material material, String itemName, int time, int slot) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Smash.getInstance(), new Runnable() {
			@Override
			public void run() {
				if (player.getInventory().contains(Material.IRON_AXE)) {
					return;
				}
				ItemStack item = new ItemStack(material, 1);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + itemName);
				item.setItemMeta(meta);
				if (getKit().canGiveItem(material) && !player.getInventory().contains(item)) {
					player.getInventory().setItem(slot, item);
				} else {
					if (!player.getInventory().contains(item)) {
						//player.getInventory().addItem(item);
						player.getInventory().setItem(slot, item);
					}
				}
				getKit().setCanGiveItem(material, true);
			}

		}, 20 * time); // 20 Ticks * x seconds = Starts in x seconds
	}
	
	public void sendKillMessage(Player victim, Player killer, String weapon) {
		if (killer == null) {
			for (Player pl : Bukkit.getOnlinePlayers()) {
				pl.sendMessage(ChatColor.GOLD + victim.getName() + "(" + SmashCore.players.get(victim).getKit().getName()
					+ ")" + ChatColor.WHITE + " fell out");
			}
			return;
		}
		for (Player pl : Bukkit.getOnlinePlayers()) {
			pl.sendMessage(ChatColor.GOLD + victim.getName() + "(" + SmashCore.players.get(victim).getKit().getName()
					+ ")" + ChatColor.WHITE + " was KO'd by " + ChatColor.GREEN + killer.getName() + "("
					+ SmashCore.players.get(killer).getKit().getName() + ")\'s " + ChatColor.DARK_AQUA + this.weapon);
		}
	}
}

