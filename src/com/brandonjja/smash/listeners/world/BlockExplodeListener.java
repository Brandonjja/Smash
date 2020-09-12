package com.brandonjja.smash.listeners.world;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class BlockExplodeListener implements Listener {
	
	@EventHandler
	public void onBlockExplode(BlockExplodeEvent e) {
		e.setCancelled(true);
		//Bukkit.getPlayer("Brandonjja").sendMessage("ev");
	}
	
	@EventHandler
	public void onExplosionPrime(ExplosionPrimeEvent e) {
		e.setCancelled(true);
		Location loc = e.getEntity().getLocation();
		e.getEntity().getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 2, false, false);
	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent e) {
		//Bukkit.getPlayer("Brandonjja").sendMessage("ev2");
		e.blockList().clear();
	}
	
	@EventHandler
	public void onIgnite(BlockIgniteEvent e) {
		if (e.getCause() == IgniteCause.LIGHTNING) {
			e.setCancelled(true);
		}
	}
}
