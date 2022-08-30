package com.brandonjja.smash.listeners.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class PlayerConsumeListener implements Listener {
	
	@EventHandler
	public void onEat(PlayerItemConsumeEvent e) {
		/*ItemStack apple = e.getItem();
		if (apple.getType() == Material.APPLE) {
			e.setCancelled(true);
			apple.setAmount(apple.getAmount() - 1);
		}*/
		e.setCancelled(true);
		
	}
}
