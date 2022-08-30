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
	public void onBlockExplode(BlockExplodeEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onExplosionPrime(ExplosionPrimeEvent event) {
		event.setCancelled(true);
		Location loc = event.getEntity().getLocation();
		event.getEntity().getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 2, false, false);
	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		event.blockList().clear();
	}
	
	@EventHandler
	public void onIgnite(BlockIgniteEvent event) {
		if (event.getCause() == IgniteCause.LIGHTNING) {
			event.setCancelled(true);
		}
	}
}
