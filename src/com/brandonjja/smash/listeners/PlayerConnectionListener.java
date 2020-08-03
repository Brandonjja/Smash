package com.brandonjja.smash.listeners;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.brandonjja.smash.Smash;
import com.brandonjja.smash.SmashCore;
import com.brandonjja.smash.game.SmashPlayer;
import com.brandonjja.smash.worldLoader.SmashFileManager;

public class PlayerConnectionListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		SmashPlayer p = new SmashPlayer(player);
		
		if (SmashCore.currentMap.equalsIgnoreCase("lobby2")) {
			if (!player.getWorld().getName().equalsIgnoreCase("lobby2")) {
				try {
					World w = Bukkit.getWorld("lobby2");
					player.teleport(new Location(w, w.getSpawnLocation().getX(), w.getSpawnLocation().getY(), w.getSpawnLocation().getZ()));
					SmashCore.currentMap = "lobby2";
				} catch (NullPointerException ex) {

					// An error here means lobby2 doesn't exist
					// Ignore this error because lobby2 is going to be created
					// If failed, another error will occur
					Smash.getInstance().getLogger().log(Level.INFO, "BELOW ==- Ignore this stack trace -== BELOW");
					ex.printStackTrace();
					Smash.getInstance().getLogger().log(Level.INFO, "^^^^==- Ignore this stack trace -==^^^^");

					World w = SmashFileManager.createLobby();
					player.teleport(new Location(w, w.getSpawnLocation().getX(), w.getSpawnLocation().getY(), w.getSpawnLocation().getZ()));
				}
			}
		} else {
			World w = Bukkit.getWorld(SmashCore.currentMap);
			player.teleport(new Location(w, w.getSpawnLocation().getX(), w.getSpawnLocation().getY(), w.getSpawnLocation().getZ()));
			player.setGameMode(GameMode.SPECTATOR);
		}

		e.setJoinMessage(ChatColor.GREEN + player.getName() + " joined!");
		
		SmashCore.players.put(player, p);
		/*player.getInventory().clear();
		player.getInventory().setContents(p.getKit().getItems());
		player.getInventory().setHelmet(p.getKit().getHelmet());
		player.updateInventory();*/
		p.giveKitItems();
		p.setKnockback(1);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		e.setQuitMessage(ChatColor.RED + player.getName() + " quit!");
		
		SmashCore.players.remove(player);
	}
}
