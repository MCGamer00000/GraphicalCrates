package me.mcgamer00000.crates.utils;

import org.bukkit.Location;

public class CrateData {
	Location loc;
	String name;
	public CrateData(Location l, String name) {
		this.loc = l;
		this.name = name;
	}
	public Location getLoc() {
		return loc;
	}
	public void setLoc(Location l) {
		this.loc = l;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}