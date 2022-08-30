package com.brandonjja.smash.listeners.player;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.brandonjja.smash.Smash;
import com.brandonjja.smash.SmashCore;
import com.brandonjja.smash.game.Game;
import com.brandonjja.smash.game.ScoreboardManager;
import com.brandonjja.smash.game.SmashPlayer;
import com.brandonjja.smash.kits.classes.Blink;
import com.brandonjja.smash.kits.classes.Metoo;
import com.brandonjja.smash.kits.classes.Pika;
import com.brandonjja.smash.kits.classes.Shadow;
import com.brandonjja.smash.kits.classes.Toshi;

import net.md_5.bungee.api.ChatColor;

public class PlayerDamageListener implements Listener {
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player)) {
				return;
		}
		
		Player player = (Player) e.getEntity();
		player.setHealth(20);
		
		SmashPlayer p = SmashCore.players.get(player);
		
		if (e.getCause() == DamageCause.FALL) {
			p.resetJumps();
			e.setCancelled(true);
			if (Game.inGame() && p.getKit() instanceof Toshi) {
				Toshi kit = (Toshi) p.getKit();
				if (kit.usedPound()) {
					kit.setUsedPound(false);
					kit.usePound(player);
				}
			}
		}
		
		if (e.getCause() == DamageCause.BLOCK_EXPLOSION) {
			e.setDamage(0);
			player.setVelocity(player.getLocation().getDirection().setY(0.05).multiply(player.getLevel()));
			player.setLevel(player.getLevel() + 5);
			ScoreboardManager.updateKB(player);
			return;
		}
		
		/*if (e.getCause() == DamageCause.LIGHTNING) {
			SmashPlayer p = SmashCore.players.get(player);
			if (p.getKit() instanceof Pika) {
				if (!((Pika) p.getKit()).canStrike()) {
				}
			}
		}*/
		
		
		
		/*e.setDamage(0);
		
		Player victim = (Player) e.getEntity();
		
		victim.setVelocity(victim.getVelocity().multiply((victim.getLevel())));
		*/
		
		if (!Game.inGame()) {
			e.setCancelled(true);
		}
		
	}
	
	@EventHandler
	public void onAttack(EntityDamageByEntityEvent e) {
		
		if (!Game.inGame()) {
			e.setCancelled(true);
			return;
		}
		
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		
		Player victim = (Player) e.getEntity();
		
		if (e.getDamager() instanceof LightningStrike) {
			SmashPlayer smashVictim = SmashCore.players.get(victim);
			LightningStrike lightning = (LightningStrike) e.getDamager();
			if (smashVictim.getKit() instanceof Pika) {
				Pika pika = (Pika) smashVictim.getKit();
				//if (lightning.equals(pika.getLightning(lightning))) {
				if (pika.getLightning(lightning)) {
					e.setCancelled(true);
					return;
				}
			}
			if (smashVictim.getKit() instanceof Shadow) {
				Shadow shadow = (Shadow) smashVictim.getKit();
				//if (lightning.equals(pika.getLightning(lightning))) {
				if (shadow.getLightning(lightning)) {
					e.setCancelled(true);
					return;
				}
			}
			victim.setVelocity(victim.getLocation().getDirection().setY(0.01).normalize().multiply(-((victim.getLevel() / 10) + 2)));
			victim.setLevel(victim.getLevel() + 2);
			e.setDamage(0);
			ScoreboardManager.updateKB(victim);
			
			SmashPlayer smashPlayer;
			for (Player pl : Bukkit.getOnlinePlayers()) {
				smashPlayer = SmashCore.players.get(pl);
				if (smashPlayer.getKit() instanceof Pika) {
					if (((Pika)smashPlayer.getKit()).getLightning(lightning)) {
						smashVictim.setLastHitFrom(pl);
						smashVictim.setLastHitWeapon("Thunder Jolt");
					}
				}
			}
			
		}
		
		if (e.getDamager() instanceof Egg) {
			Egg egg = (Egg) e.getDamager();
			if (egg.getShooter() instanceof Player) {
				SmashPlayer smashVictim = SmashCore.players.get(victim);
				Player player = (Player) egg.getShooter();
				
				if (victim.equals(player)) {
					e.setCancelled(true);
					return;
				}
				
				victim.setVelocity(player.getLocation().getDirection().setY(0).normalize().multiply(victim.getLevel() / 10));
				victim.setLevel(victim.getLevel() + 5);
				ScoreboardManager.updateKB(victim);
				smashVictim.setLastHitFrom(player);
				smashVictim.setLastHitWeapon("Toshi Egg");
				
				Location loc = new Location(victim.getWorld(), victim.getLocation().getX(), victim.getLocation().getY() + 1, victim.getLocation().getZ());
				
				Firework firework = (Firework) victim.getWorld().spawn(loc, Firework.class);
				FireworkMeta meta = firework.getFireworkMeta();
				meta.addEffect(FireworkEffect.builder().flicker(false).trail(false).with(Type.BALL).withColor(Color.GREEN).build());
				meta.setPower(0);
				firework.setFireworkMeta(meta);
				
				new BukkitRunnable() {
		            public void run() {
		                firework.detonate();
		            }
		        }.runTaskLater(Smash.getInstance(), 1);
				
				e.setDamage(0);
			}
		}
		
		if (e.getDamager() instanceof Arrow) {
			Arrow a = (Arrow) e.getDamager();
			if (a.getShooter() instanceof Player) {
				Player player = (Player) a.getShooter();
				if (victim.equals(player)) {
					e.setCancelled(true);
					return;
				}
				victim.setVelocity(player.getLocation().getDirection().setY(0).normalize().multiply(victim.getLevel() / 10));
				victim.setLevel(victim.getLevel() + (int) e.getDamage());
				ScoreboardManager.updateKB(victim);
				e.setDamage(0);
				
				SmashPlayer smashVictim = SmashCore.players.get(victim);
				smashVictim.setLastHitFrom(player);
				smashVictim.setLastHitWeapon("Bow");
				
				return;
			}
			
			//victim.setVelocity(a.getLocation().getDirection().setY(0).normalize().multiply(victim.getLevel() / 10));
			victim.setVelocity(a.getLocation().getDirection().setY(0).normalize().multiply(victim.getLevel() / 10));
			victim.setLevel(victim.getLevel() + (int) e.getDamage());
			ScoreboardManager.updateKB(victim);
			e.setDamage(0);
			
			try {
				Player player = (Player) a.getShooter();

				SmashPlayer smashVictim = SmashCore.players.get(victim);

				smashVictim.setLastHitFrom(player);
				smashVictim.setLastHitWeapon(a.getName());
			} catch (ClassCastException ex) {

			}
			
			return;
		}
		
		if (e.getDamager() instanceof Snowball) {
			Snowball ball = (Snowball) e.getDamager();
			Player player = (Player) ball.getShooter();
			if (victim.equals(player)) {
				e.setCancelled(true);
				return;
			}
			SmashPlayer p = SmashCore.players.get(player);
			SmashPlayer smashVictim = SmashCore.players.get(victim);
			
			smashVictim.setLastHitFrom(player);
			//smashVictim.setLastHitWeapon(ball.getName());
			
			if (p.getKit() instanceof Blink) {
				Location loc = player.getLocation();
				player.teleport(victim.getLocation());
				victim.teleport(loc);
				player.sendMessage(ChatColor.GREEN + "You switched with " + victim.getName() + "!");
				victim.sendMessage(ChatColor.RED + "You got switched with " + player.getName() + "!");
				
				smashVictim.setLastHitWeapon("Switcher");
			}
			
			if (p.getKit() instanceof Shadow) {
				Location loc = player.getLocation();
				player.teleport(victim.getLocation());
				victim.teleport(loc);
				player.sendMessage(ChatColor.GREEN + "You switched with " + victim.getName() + "!");
				victim.sendMessage(ChatColor.RED + "You got switched with " + player.getName() + "!");
				
				smashVictim.setLastHitWeapon("Switcher");
			}
			
			if (p.getKit() instanceof Metoo) {
				victim.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 8, 1));
				victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 2, 1));
				victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 8, 1));
				victim.sendMessage(ChatColor.RED + "You were hit by " + player.getName() + "\'s" + ChatColor.DARK_AQUA + " Psychic Orb" + ChatColor.RED + "!");
				player.sendMessage(ChatColor.GREEN + "You hit " + victim.getName() + " with your " + ChatColor.DARK_AQUA + "Psychic Orb" + ChatColor.GREEN + "!");
				
				smashVictim.setLastHitWeapon("Psychic Orb");
				
				
				Location loc = new Location(victim.getWorld(), victim.getLocation().getX(), victim.getLocation().getY() + 1, victim.getLocation().getZ());
				
				Firework firework = (Firework) victim.getWorld().spawn(loc, Firework.class);
				FireworkMeta meta = firework.getFireworkMeta();
				meta.addEffect(FireworkEffect.builder().flicker(false).trail(false).with(Type.BALL).withColor(Color.PURPLE).build());
				meta.setPower(0);
				firework.setFireworkMeta(meta);
				
				new BukkitRunnable() {
		            public void run() {
		                firework.detonate();
		            }
		        }.runTaskLater(Smash.getInstance(), 1);
				
				
			}
		}
		
		if (!(e.getDamager() instanceof Player)) {
			return;
		}
		
		Player attacker = (Player) e.getDamager();
		
		victim.setLevel(victim.getLevel() + (int) e.getDamage());
		ScoreboardManager.updateKB(victim);
		
		//victim.setVelocity(victim.getVelocity().multiply((victim.getLevel())));
		//victim.setVelocity(attacker.getLocation().getDirection().multiply(victim.getLevel() / 50));
		
		SmashPlayer smashVictim = SmashCore.players.get(victim);
		smashVictim.setLastHitFrom(attacker);
		//ItemMeta meta = attacker.getItemInHand().getItemMeta();
		//meta.setsetDisplayName(ChatColor.WHITE + attacker.getItemInHand().getType().toString());
		try {
			String arr[] = attacker.getItemInHand().getItemMeta().getDisplayName().split("");
			String name = "";
			for (int i = 4; i < arr.length; i++) {
				name += arr[i];
			}
			smashVictim.setLastHitWeapon(name);
		} catch (NullPointerException ex) {
			smashVictim.setLastHitWeapon("Fist");
		}
		
		if (attacker.getItemInHand().getType() == Material.IRON_AXE) {
			victim.setVelocity(attacker.getLocation().getDirection().setY(0).normalize().multiply(((victim.getLevel() / 10) + 5) * 2));
			e.setDamage(0);
			return;
		}
		
		if (attacker.getItemInHand().getType() == Material.STICK) {
			victim.setVelocity(attacker.getLocation().getDirection().setY(0).normalize().multiply(((victim.getLevel() / 10) + 2) * 1.25));
			e.setDamage(0);
			ItemStack bat = attacker.getInventory().getItemInHand();
			bat.setAmount(bat.getAmount() - 1);
			attacker.getInventory().setItemInHand(bat);
			return;
		}
		
		victim.setVelocity(attacker.getLocation().getDirection().setY(0).normalize().multiply(victim.getLevel() / 10));
		e.setDamage(0);
		
		//victim.setVelocity(victim.getLocation().getDirection().multiply(-1 * (victim.getLevel() / 20)));
		
	}
}
