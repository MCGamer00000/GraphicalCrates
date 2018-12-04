package me.mcgamer00000.crates.listeners;


import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.mcgamer00000.crates.Crates;
import me.mcgamer00000.crates.utils.CrateData;

public class ClickCrate implements Listener {

	Crates pl;
	
	public ClickCrate(Crates pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(!(e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		for(Object l: pl.getConfig().getList("crates.locations")) {
			if(!(l instanceof CrateData)) return;
			Location loc = ((CrateData) l).getLoc();
			if(e.getClickedBlock().getLocation().equals(loc)) {
				e.getPlayer().sendMessage("Openning a crate!");
				
			} else {
				continue;
			}
			
		}
	}
	
}
