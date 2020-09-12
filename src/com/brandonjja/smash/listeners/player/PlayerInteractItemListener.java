package com.brandonjja.smash.listeners.player;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Egg;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.brandonjja.smash.Smash;
import com.brandonjja.smash.SmashCore;
import com.brandonjja.smash.game.Game;
import com.brandonjja.smash.game.ScoreboardManager;
import com.brandonjja.smash.game.SmashPlayer;
import com.brandonjja.smash.kits.classes.Blink;
import com.brandonjja.smash.kits.classes.Jigglyo;
import com.brandonjja.smash.kits.classes.Metoo;
import com.brandonjja.smash.kits.classes.Pika;
import com.brandonjja.smash.kits.classes.Shadow;
import com.brandonjja.smash.kits.classes.Toshi;

import net.md_5.bungee.api.ChatColor;

public class PlayerInteractItemListener implements Listener {
	
	@EventHandler
	public void onItemUse(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		SmashPlayer smashPlayer = SmashCore.players.get(player);
		
		Material item = player.getItemInHand().getType();
		
		if (e.getAction().equals(Action.PHYSICAL) && e.getClickedBlock().getType() == Material.STONE_PLATE) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Smash.getInstance(), new Runnable() {
				@Override
				public void run() {
					e.getClickedBlock().setType(Material.AIR);
					e.getClickedBlock().getState().update();
				}

			}, 2); // 20 Ticks * x seconds = Starts in x seconds

			TNTPrimed tnt = (TNTPrimed) player.getWorld().spawn(player.getLocation(), TNTPrimed.class);
			tnt.setFuseTicks(1);
			return;
			/*player.sendMessage(loc.getY() + "");
			player.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 5, false, false);*/
		}
		
		if (item == Material.STONE_PLATE) {
			@SuppressWarnings("deprecation") // TODO: Deprecated
			FallingBlock landMine = player.getLocation().getWorld().spawnFallingBlock(player.getLocation(), Material.STONE_PLATE, (byte) 0);
			Vector vec = player.getLocation().getDirection();
			landMine.setVelocity(vec.multiply(2));
			
			ItemStack landMineItem = player.getInventory().getItemInHand();
			landMineItem.setAmount(landMineItem.getAmount() - 1);
			player.getInventory().setItemInHand(landMineItem);
			return;
		}
		
		// Double-Jump
		if (item == Material.FIREWORK) {
			//player.getLocation().setY(player.getLocation().getY());
			e.setCancelled(true);
			
			if (inNormalParkourArea(player)) {
				return;
			}
			
			if (!smashPlayer.hasJump()) {
				smashPlayer.setFirstJump(false);
				
				if (smashPlayer.getKit() instanceof Jigglyo) {
					if (((Jigglyo) smashPlayer.getKit()).getMiniJumps() >= 1) {
						((Jigglyo) smashPlayer.getKit()).decrementMiniJumps();
						smashPlayer.decrementJumps();
						Vector vec = player.getEyeLocation().getDirection();
						vec.multiply(0.45);
						//vec.setY(0.6);
						vec.setY(0.7);
						player.setVelocity(vec);
					}
				}
				
				return;
			}
			smashPlayer.decrementJumps();
			smashPlayer.setFirstJump(true);
			Vector vec = player.getEyeLocation().getDirection();
			vec.multiply(0.8);
			vec.setY(1);
			player.setVelocity(vec);
		}
		
		// Pika Lunge
		if (item == Material.GOLDEN_CARROT) {
			if (inNormalParkourArea(player)) {
				return;
			}
			if (smashPlayer.getKit() instanceof Pika) {
				Pika pika = (Pika) smashPlayer.getKit();
				if (pika.canLunge()) {
					pika.setLunge(false);
					Vector vector = player.getEyeLocation().getDirection();
					vector.multiply(1.4F);
					vector.setY(0.4);
					player.setVelocity(vector);
				}
			} else if (smashPlayer.getKit() instanceof Shadow) {
				Shadow shadow = (Shadow) smashPlayer.getKit();
				if (shadow.canLunge()) {
					shadow.setLunge(false);
					Vector vector = player.getEyeLocation().getDirection();
					vector.multiply(1.4F);
					vector.setY(0.4);
					player.setVelocity(vector);
				}
			}
		}
		
		if (!Game.inGame()) {
			//e.setCancelled(true);
			//player.getInventory().setItemInHand(player.getItemInHand());
			return;
		}
		
		// Pika Thunder Jolt
		if (item == Material.WOOD_AXE || item == Material.STONE_AXE) {
			if (smashPlayer.getKit() instanceof Pika) {
				if (!((Pika) smashPlayer.getKit()).canStrike()) {
					return;
				}
				
				LightningStrike lightning = player.getWorld().strikeLightning(player.getTargetBlock((Set<Material>)null, 4).getLocation());
				((Pika) smashPlayer.getKit()).setLightning(lightning);
				((Pika) smashPlayer.getKit()).setStrike(false);
				Bukkit.getScheduler().scheduleSyncDelayedTask(Smash.getInstance(), new Runnable() {
					@Override
					public void run() {
						((Pika) smashPlayer.getKit()).setStrike(true);
						player.sendMessage(ChatColor.GREEN + "You can now use your " + ChatColor.RED + "" + ChatColor.BOLD + "Thunder Jolt" + ChatColor.GREEN + " again!");
					}

				}, 20 * 15); // 20 Ticks * x seconds = Starts in x seconds
			} else if (smashPlayer.getKit() instanceof Shadow) {
				if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
					return;
				}
				if (!((Shadow) smashPlayer.getKit()).canStrike()) {
					return;
				}
				
				LightningStrike lightning = player.getWorld().strikeLightning(player.getTargetBlock((Set<Material>)null, 4).getLocation());
				((Shadow) smashPlayer.getKit()).setLightning(lightning);
				((Shadow) smashPlayer.getKit()).setStrike(false);
				Bukkit.getScheduler().scheduleSyncDelayedTask(Smash.getInstance(), new Runnable() {
					@Override
					public void run() {
						((Shadow) smashPlayer.getKit()).setStrike(true);
						player.sendMessage(ChatColor.GREEN + "You can now use your " + ChatColor.RED + "" + ChatColor.BOLD + "Thunder Jolt" + ChatColor.GREEN + " again!");
					}

				}, 20 * 25); // 20 Ticks * x seconds = Starts in x seconds
			}
		}
		
		// Metoo
		if (item == Material.ENDER_PEARL) {
			smashPlayer.runItemTimer(item, "Teleporter", 8, player.getInventory().getHeldItemSlot());
			/*Bukkit.getScheduler().scheduleSyncDelayedTask(Smash.getInstance(), new Runnable() {
				@Override
				public void run() {
					if (p.getKit().canGiveItem(item)) {
						ItemStack pearl = new ItemStack(Material.ENDER_PEARL);
						ItemMeta meta = pearl.getItemMeta();
						meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Teleporter");
						pearl.setItemMeta(meta);
						player.getInventory().addItem(pearl);
					}
					p.getKit().setCanGiveItem(item, true);
				}

			}, 20 * 4); // 20 Ticks * x seconds = Starts in x seconds*/
		}
		
		if (item == Material.SNOW_BALL) {
			if (smashPlayer.getKit() instanceof Blink) {
				smashPlayer.runItemTimer(item, "Switcher", 8, player.getInventory().getHeldItemSlot());
				/*Bukkit.getScheduler().scheduleSyncDelayedTask(Smash.getInstance(), new Runnable() {
					@Override
					public void run() {
						ItemStack snowball = new ItemStack(Material.SNOW_BALL);
						ItemMeta meta = snowball.getItemMeta();
						meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Switcher");
						snowball.setItemMeta(meta);
						if (p.getKit().canGiveItem(item) && !player.getInventory().contains(snowball)) {
							player.getInventory().addItem(snowball);
						} else {
							if (!player.getInventory().contains(snowball)) {
								player.getInventory().addItem(snowball);
							}
						}
						p.getKit().setCanGiveItem(item, true);
					}

				}, 20 * 4); // 20 Ticks * x seconds = Starts in x seconds*/
			} else if (smashPlayer.getKit() instanceof Metoo) {
				smashPlayer.runItemTimer(item, "Psychic Orb", 14, player.getInventory().getHeldItemSlot());
				/*Bukkit.getScheduler().scheduleSyncDelayedTask(Smash.getInstance(), new Runnable() {
					@Override
					public void run() {
						ItemStack snowball = new ItemStack(Material.SNOW_BALL);
						ItemMeta meta = snowball.getItemMeta();
						meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Psychic Orb");
						snowball.setItemMeta(meta);
						if (p.getKit().canGiveItem(item) && !player.getInventory().contains(snowball)) {
							player.getInventory().addItem(snowball);
						} else {
							if (!player.getInventory().contains(snowball)) {
								player.getInventory().addItem(snowball);
							}
						}
						p.getKit().setCanGiveItem(item, true);
					}

				}, 20 * 4); // 20 Ticks * x seconds = Starts in x seconds*/
			} else if (smashPlayer.getKit() instanceof Shadow) {
				smashPlayer.runItemTimer(item, "Switcher", 16, player.getInventory().getHeldItemSlot());
				/*Bukkit.getScheduler().scheduleSyncDelayedTask(Smash.getInstance(), new Runnable() {
					@Override
					public void run() {
						ItemStack snowball = new ItemStack(Material.SNOW_BALL);
						ItemMeta meta = snowball.getItemMeta();
						meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Switcher");
						snowball.setItemMeta(meta);
						if (p.getKit().canGiveItem(item) && !player.getInventory().contains(snowball)) {
							player.getInventory().addItem(snowball);
						} else {
							if (!player.getInventory().contains(snowball)) {
								player.getInventory().addItem(snowball);
							}
						}
						p.getKit().setCanGiveItem(item, true);
					}

				}, 20 * 12); // 20 Ticks * x seconds = Starts in x seconds*/
			}
		}
		
		if (item == Material.CLAY_BRICK && smashPlayer.getKit() instanceof Toshi) {
			
			ItemStack brick = player.getInventory().getItemInHand();
			brick.setAmount(brick.getAmount() - 1);
			player.getInventory().setItemInHand(brick);
			
			Vector vec = new Vector(0,  -2,  0);
			vec.multiply(0.8);
			player.setVelocity(vec);
			((Toshi)smashPlayer.getKit()).setUsedPound(true); //TODO: Just try
			//TODO: move to MoveListener?
			
			smashPlayer.runItemTimer(item, "Ground Pound", 15, player.getInventory().getHeldItemSlot());
			
		}
		
		if (item == Material.MONSTER_EGG && smashPlayer.getKit() instanceof Toshi) {
			player.launchProjectile(Egg.class);
			player.getInventory().remove(Material.MONSTER_EGG);
			
			int slot = player.getInventory().getHeldItemSlot();
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(Smash.getInstance(), new Runnable() {
				@Override
				public void run() {
					ItemStack egg = new ItemStack(Material.MONSTER_EGG, 1, (short) 50);
					ItemMeta meta = egg.getItemMeta();
					meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Toshi Egg");
					egg.setItemMeta(meta);
					if (smashPlayer.getKit().canGiveItem(item)) {
						//player.getInventory().addItem(egg);
						player.getInventory().setItem(slot, egg);
					} else {
						if (!player.getInventory().contains(egg)) {
							//player.getInventory().addItem(egg);
							player.getInventory().setItem(slot, egg);
						}
					}
					smashPlayer.getKit().setCanGiveItem(item, true);
				}

			}, 20 * 8); // 20 Ticks * x seconds = Starts in x seconds
		}
		
		
		
		
		// ______________________________________________________________________________________________________________________________________________________
		// ______________________________________________________________________________________________________________________________________________________
		
		//										BELOW IS CODE FOR ITEMS YOU PICK UP AROUND THE MAP
		
		// ______________________________________________________________________________________________________________________________________________________
		// ______________________________________________________________________________________________________________________________________________________
		
		
		
		
		if (item == Material.TNT) {
			//FallingBlock tnt = player.getLocation().getWorld().spawnFallingBlock(player.getLocation(), Material.TNT, (byte) 0);
			TNTPrimed tnt = (TNTPrimed) player.getWorld().spawn(player.getLocation(), TNTPrimed.class);
			Vector vec = player.getLocation().getDirection();
			tnt.setVelocity(vec.multiply(2));
			tnt.setFuseTicks(20 * 2);
			ItemStack handTnt = player.getInventory().getItemInHand();
			handTnt.setAmount(handTnt.getAmount() - 1);
			player.getInventory().setItemInHand(handTnt);
		}
		
		if (item == Material.FEATHER) {
			ItemStack feather = player.getInventory().getItemInHand();
			feather.setAmount(feather.getAmount() - 1);
			player.getInventory().setItemInHand(feather);
			if (player.hasPotionEffect(PotionEffectType.JUMP) && player.hasPotionEffect(PotionEffectType.SPEED)) {
				player.removePotionEffect(PotionEffectType.JUMP);
				player.removePotionEffect(PotionEffectType.SPEED);
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 10, 2));
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 10, 2));
		}
		
		if (item == Material.APPLE) {
			e.setCancelled(true);
			ItemStack apple = player.getInventory().getItemInHand();
			if (apple.getAmount() <= 1) {
				player.getInventory().remove(apple);
				player.updateInventory();
				if (player.getLevel() - 50 <= 1) {
					player.setLevel(1);
				} else {
					player.setLevel(player.getLevel() - 50); // Should be 80
				}
				ScoreboardManager.updateKB(player);
				return;
			}
			apple.setAmount(apple.getAmount() - 1);
			player.getInventory().setItemInHand(apple);
			player.updateInventory();
			if (player.getLevel() - 50 <= 1) {
				player.setLevel(1);
			} else {
				player.setLevel(player.getLevel() - 50); // Should be 80
			}
			ScoreboardManager.updateKB(player);
			return;
		}
		
		if (item == Material.GOLDEN_APPLE) {
			e.setCancelled(true);
			ItemStack apple = player.getInventory().getItemInHand();
			apple.setAmount(apple.getAmount() - 1);
			player.getInventory().setItemInHand(apple);
			player.setLevel(1);
			ScoreboardManager.updateKB(player);
			return;
		}
		
		
	}
	
	
	private boolean inNormalParkourArea(Player player) {
		if (!player.getWorld().getName().equalsIgnoreCase("lobby2")) {
			return false;
		}
		
		Location loc = player.getLocation();
		
		if ((loc.getY() >= 19 && loc.getY() <= 26) && (loc.getX() < 1156 && loc.getX() > 1152) && (loc.getZ() <= -583.5 && loc.getZ() >= -588) ) {
			return false;
		}
		
		if (loc.getZ() > -588 && loc.getZ() < -544) {
			player.sendMessage(ChatColor.LIGHT_PURPLE + "[Smash] " + ChatColor.RED + "Double-Jumps are disabled in this area");
			return true;
		}
		return false;
	}
	
	
}
