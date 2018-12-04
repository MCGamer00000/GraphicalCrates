package me.mcgamer00000.crates.inventories;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.mcgamer00000.crates.Crates;
import me.mcgamer00000.crates.utils.ClickAction;
import me.mcgamer00000.crates.utils.Icon;
import me.mcgamer00000.crates.utils.ItemStackUtil;

public class CrateMaterialInv extends BaseInvHolder {

	public CrateMaterialInv() {
		super(27, "&4Choose Crate Material");
		
		/**
		 * 
		 * Chest, EnderChest, 
		 * Coal, Iron, Redstone, Lapis, Gold, Diamond, Emerald [Block]
		 * Coal, Iron, Redstone, Lapis, Gold, Diamond, Emerald [Ore]
		 * 
		 **/
		Map<Integer, Material> slots = new HashMap<>();
		slots.put(0, Material.CHEST);
		slots.put(1, Material.ENDER_CHEST);
		
		slots.put(9, Material.COAL_BLOCK);
		slots.put(10, Material.IRON_BLOCK);
		slots.put(11, Material.REDSTONE_BLOCK);
		slots.put(12, Material.LAPIS_BLOCK);
		slots.put(13, Material.GOLD_BLOCK);
		slots.put(14, Material.DIAMOND_BLOCK);
		slots.put(15, Material.EMERALD_BLOCK);
		
		slots.put(18, Material.COAL_ORE);
		slots.put(19, Material.IRON_ORE);
		slots.put(20, Material.REDSTONE_ORE);
		slots.put(21, Material.LAPIS_ORE);
		slots.put(22, Material.GOLD_ORE);
		slots.put(23, Material.DIAMOND_ORE);
		slots.put(24, Material.EMERALD_ORE);
		setSpecificSlots(slots);
		this.setIcon(26, new Icon(ItemStackUtil.createItem(Material.IRON_TRAPDOOR, "&cCancel")).addClickAction(new ClickAction() {

			@Override
			public void execute(InventoryClickEvent e) {
				e.getWhoClicked().openInventory(Crates.getInstance().getPlayerInMenu(e.getWhoClicked().getUniqueId()).popInventory().getInventory());
				//e.getWhoClicked().openInventory(new CreateCrateHolder(Crates.getInstance().getCrateId(e)).getInventory());
			}
			
		}));
	}
	
	private void setSpecificSlots(Map<Integer, Material> slots) {
		for(int i: slots.keySet()) {
			this.setIcon(i, new Icon(new ItemStack(slots.get(i))).addClickAction(new ClickAction() {

				@Override
				public void execute(InventoryClickEvent e) {
					Crates.getInstance().getData().set("crates." + Crates.getInstance().getCrateId(e) + ".material", e.getCurrentItem().getType().toString());
					Crates.getInstance().saveData();
					e.getWhoClicked().openInventory(Crates.getInstance().getPlayerInMenu(e.getWhoClicked().getUniqueId()).popInventory().getInventory());
				}
				
			}));
		}
	}

	
	
}
