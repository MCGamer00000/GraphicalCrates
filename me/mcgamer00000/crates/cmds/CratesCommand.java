package me.mcgamer00000.crates.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.mcgamer00000.crates.Crates;
import me.mcgamer00000.crates.inventories.CrateMenuHolder;

public class CratesCommand implements CommandExecutor {

	Crates pl;
	
	public CratesCommand(Crates pl) {
		this.pl = pl;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length < 1) {
			sender.sendMessage("Invalid Args");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("menu")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage("You must be a player to create a crate");
				return true;
			}
			Inventory i = new CrateMenuHolder((Player) sender).getInventory();
			((Player) sender).openInventory(i);
		}
		return true;
	}

	public String cc(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
}