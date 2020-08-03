package com.brandonjja.smash.commands.handler;

import org.bukkit.entity.Player;

import com.brandonjja.smash.SmashCore;
import com.brandonjja.smash.commands.SmashCommand;
import com.brandonjja.smash.game.SmashPlayer;
import com.brandonjja.smash.kits.classes.Blink;
import com.brandonjja.smash.kits.classes.Jiggly;
import com.brandonjja.smash.kits.classes.Metoo;
import com.brandonjja.smash.kits.classes.Pika;
import com.brandonjja.smash.kits.classes.Shadow;
import com.brandonjja.smash.kits.classes.Toshi;

import net.md_5.bungee.api.ChatColor;

public class KitCommand extends SmashCommand {

	@Override
	public boolean execute(Player player, String[] args) {
		
		SmashPlayer p = SmashCore.players.get(player);
		
		// TODO: Remember to load new classes in KitLoader!!
		
		if (!SmashCore.currentMap.equalsIgnoreCase("lobby2")) {
			player.sendMessage(ChatColor.LIGHT_PURPLE + "[Smash] " + ChatColor.RED + "You can only choose kits from the lobby!");
			player.sendMessage(ChatColor.GOLD + "You are the " + ChatColor.GREEN + p.getKit().getName() + ChatColor.GOLD + " class!");
			return true;
		}
		
		if (args.length == 0) {
			player.sendMessage(ChatColor.GOLD + "Kits: " + ChatColor.GREEN + "Blink, Metoo, Pika, Toshi, Jiggly");
			player.sendMessage(ChatColor.GOLD + "You are the " + ChatColor.GREEN + p.getKit().getName() + ChatColor.GOLD + " class!");
			return true;
		} else if (args.length == 1) {
			String kit = args[0];
			//p.updateInventoryMap();
			if (kit.equalsIgnoreCase("blink")) {
				p.setKit(new Blink());
			} else if (kit.equalsIgnoreCase("metoo")) {
				p.setKit(new Metoo());
			} else if (kit.equalsIgnoreCase("pika")) {
				p.setKit(new Pika());
			} else if (kit.equalsIgnoreCase("toshi")) {
				p.setKit(new Toshi());
			} else if (kit.equalsIgnoreCase("shadow")) {
				p.setKit(new Shadow());
			} else if (kit.equalsIgnoreCase("jiggly")) {
				p.setKit(new Jiggly());
			}
			
			else {
				p.updateInventoryMap();
				return false;
			}
			player.sendMessage(p.getKit().getKitMessage());
			player.getInventory().setContents(p.getKit().getItems());
			player.getInventory().setHelmet(p.getKit().getHelmet());
			player.updateInventory();
			p.updateInventoryMap();
			p.setKnockback(1);
			return true;
		} else {
			return false;
		}
	}
	
}
