package com.brandonjja.smash.listeners.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import com.brandonjja.smash.Smash;
import com.brandonjja.smash.SmashCore;
import com.brandonjja.smash.game.Game;
import com.brandonjja.smash.game.ScoreboardManager;
import com.brandonjja.smash.game.SmashPlayer;
import com.brandonjja.smash.kits.classes.Jigglyo;
import com.brandonjja.smash.kits.classes.Pika;
import com.brandonjja.smash.kits.classes.Shadow;
import com.brandonjja.smash.kits.classes.Toshi;

public class PlayerMoveListener implements Listener {

	static int ctr = 0;
	
	private final static double JUMPVEL = (double) 0.42F;
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onSneak(PlayerToggleSneakEvent e) {
		Player player = e.getPlayer();
		SmashPlayer smashPlayer = SmashCore.players.get(player);
		if (e.isSneaking() && smashPlayer.getKit() instanceof Jigglyo && player.isOnGround()) {
			Jigglyo kit = (Jigglyo) smashPlayer.getKit();
			int time = Bukkit.getScheduler().scheduleSyncRepeatingTask(Smash.getInstance(), new Runnable() {
				@Override
				public void run() {
					if (player.getLevel() >= 2) {
						if (!player.isOnGround()) {
							((Jigglyo)smashPlayer.getKit()).cancelSneakTimer();
						} else {
							player.setLevel(player.getLevel() - 1);
							ScoreboardManager.updateKB(player);
						}
					} else {
						player.setLevel(1);
					}
				}
			}, 10L, 10L); // 0 Tick initial delay, 20 * x seconds between repeats
			kit.setSneakTimer(time);
		} else if (!e.isSneaking() && smashPlayer.getKit() instanceof Jigglyo) {
			((Jigglyo)smashPlayer.getKit()).cancelSneakTimer();
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		SmashPlayer p = SmashCore.players.get(player);
		
		player.setHealth(20);
		player.setFoodLevel(20);
		
		if (player.getLocation().getY() < 2) {
			player.teleport(player.getWorld().getSpawnLocation());
			for (PotionEffect effect : player.getActivePotionEffects()) {
				player.removePotionEffect(effect.getType());
			}
			p.giveKitItems();
			p.resetJumps();
			p.setKnockback(1);
			
			ScoreboardManager.updateKB(player);
			ScoreboardManager.updateScore(p.getLastHitFrom());
			//ScoreboardManager.updateScore(player, -1);
			
			p.sendKillMessage(player, p.getLastHitFrom(), p.getLastHitWeapon());
			p.setLastHitFrom(null);
			p.setLastHitWeapon(null);
			
			p.getKit().setCanGiveItem(null, false);
			
			if (Game.getWin()) {
				return;
			}
			
			p.cancelHammerCooldown();
			
			Game.checkEndGame();
		}
		
		if (p.getKit() instanceof Toshi) {
			Toshi kit = (Toshi) p.getKit();
			if (kit.usedPound()) {
				if (player.isOnGround()) {
					kit.setUsedPound(false);
					kit.usePound(player);
				}
			}
		}
		
		/*Block b = player.getLocation().getBlock();
		if (b.getType() != Material.AIR
                || b.getRelative(BlockFace.DOWN).getType() != Material.AIR) {
            p.resetJumps();
            //player.sendMessage("r");
            return;
        }*/
		
		/*if (p.getJumpsLeft() + 1 == p.getKit().getJumps()) {
			return;
		}*/
		
		// https://www.spigotmc.org/threads/how-to-check-if-player-is-jumping.367036/
		Vector velocity = player.getVelocity();
		if (player.getLocation().getBlock().getType() != Material.LADDER && Double.compare(velocity.getY(), JUMPVEL) == 0) {
			p.resetJumps();
			if (p.getKit() instanceof Pika) {
				Pika pika = (Pika) p.getKit();
				pika.setLunge(true);
			}
			if (p.getKit() instanceof Shadow) {
				Shadow shadow = (Shadow) p.getKit();
				shadow.setLunge(true);
			}
			return;
		}
		
		if (e.getFrom().getY() == e.getTo().getY()) {
			Location loc = new Location(e.getTo().getWorld(), e.getTo().getBlockX(), e.getTo().getBlockY(), e.getTo().getBlockZ());
			if (player.getWorld().getBlockAt(loc.subtract(0, 1, 0)).getType() != Material.AIR) {
				if (p.getKit() instanceof Pika) {
					Pika pika = (Pika) p.getKit();
					pika.setLunge(true);
				}
				if (p.getKit() instanceof Shadow) {
					((Shadow) p.getKit()).setLunge(true);
				}
				if (p.getFirstJump()) {
					p.setFirstJump(false);
					return;
				}
				p.resetJumps();
				return;
			}
		}
		
		ctr++;
		
		/*if (p.getFirstJump()) {
			//player.sendMessage("fj");
			p.setFirstJump(false);
			return;
		}
		
		p.setFirstJump(true);*/
		
		/*if (player.isOnGround()) {
			p.resetJumps();
		}*/
		
		/*if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
			if (p.getFirstJump()) {
				p.setFirstJump(false);
				return;
			}
			p.resetJumps();
		}*/
	}
}
