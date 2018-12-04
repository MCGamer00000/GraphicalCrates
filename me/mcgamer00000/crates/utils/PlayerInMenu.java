package me.mcgamer00000.crates.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.mcgamer00000.crates.inventories.BaseInvHolder;

public class PlayerInMenu {

	private List<BaseInvHolder> inventories = new ArrayList<>();
	private final UUID u;
	private String id;
	
	public PlayerInMenu(UUID u) {
		this.u = u;
	}
	
	public void setCrateId(String id) {
		this.id = id;
	}
	
	public UUID getUUID() {
		return u;
	}
	
	public void addInventory(BaseInvHolder inv) {
		inventories.add(inv);
	}

	public String getCrateId() {
		return id;
	}
	
	public BaseInvHolder getLastInventory() {
		return inventories.get(inventories.size()-1);
	}

	public BaseInvHolder popInventory() {
		inventories.remove(getLastInventory());
		return getLastInventory();
	}
	
}
