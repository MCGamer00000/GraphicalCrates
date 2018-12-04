package me.mcgamer00000.crates.utils;

import java.util.List;
import java.util.Map;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.mcgamer00000.crates.Crates;
import me.mcgamer00000.crates.inventories.BaseInvHolder;

public class ToggleableIcon extends Icon {
	
	public ToggleableIcon(String location, BaseInvHolder inv, ItemStack on, ItemStack off) {
		super(Crates.getInstance().getData().getBoolean(location) ? on : off);
		super.addClickAction(new ClickAction() {
			@Override
			public void execute(InventoryClickEvent e) {
				String crateId = Crates.getInstance().getCrateId(e);
				boolean bool = !((boolean) Crates.getInstance().getData().get("crates." + crateId + ".openInv"));
				Crates.getInstance().getData().set("crates." + crateId + ".openInv", bool);
				itemStack = bool ? on : off;
				e.getWhoClicked().openInventory(Crates.getInstance().getPlayerInMenu(e.getWhoClicked().getUniqueId()).getLastInventory().getInventory());
			}
		});
	}

	@SuppressWarnings("unchecked")
	public ToggleableIcon(String listLocation, int index, String name, BaseInvHolder inv, ItemStack on, ItemStack off) {
		super((boolean) ((List<Map<String, Object>>) Crates.getInstance().getData().getList(listLocation)).get(index).get("name") ? on : off);
		super.addClickAction(new ClickAction() {
			@Override
			public void execute(InventoryClickEvent e) {
				Crates pl = Crates.getInstance();
				List<Map<String, Object>> rewards = (List<Map<String, Object>>) 
						pl.getData().getList(listLocation);
				
				boolean bool = !((boolean) rewards.get(index).get(name));
				
				rewards.get(index).put(name, bool);
				pl.getData().set(listLocation, rewards);
				
				itemStack = bool ? on : off;
				e.getWhoClicked().openInventory(Crates.getInstance().getPlayerInMenu(e.getWhoClicked().getUniqueId()).getLastInventory().getInventory());
			}
		});
	}
	
}
