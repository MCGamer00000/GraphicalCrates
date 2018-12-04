package me.mcgamer00000.crates.inventories;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.mcgamer00000.crates.Crates;
import me.mcgamer00000.crates.utils.ClickAction;
import me.mcgamer00000.crates.utils.Icon;
import me.mcgamer00000.crates.utils.ItemStackUtil;
import me.mcgamer00000.crates.utils.MapUtil;

public class GuiEditHolder extends BaseInvHolder {

	public GuiEditHolder(UUID u) {
		super(36, "&4Edit GUI Display");
		
		String id = Crates.getInstance().getPlayerInMenu(u).getCrateId();
		
		for(int i = 0; i < 36; i++) {
			int i2 = i;
			this.setIcon(i, new Icon(ItemStackUtil.createItem(Material.STAINED_GLASS_PANE, "&0")).addClickAction(new ClickAction() {
				final int id = i2;
				@Override
				public void execute(InventoryClickEvent e) {
					e.getWhoClicked().openInventory(new GuiItemEditHolder(e.getWhoClicked().getUniqueId(), id).getInventory());
				}
				
			}));
		}
		
		ItemStack exit = ItemStackUtil.createItem(Material.IRON_TRAPDOOR, "&cExit");
		this.setIcon(35, new Icon(exit).addClickAction(new ClickAction() {
			@Override
			public void execute(InventoryClickEvent e) {
				e.getWhoClicked().openInventory(Crates.getInstance().getPlayerInMenu(e.getWhoClicked().getUniqueId()).popInventory().getInventory());
			}
		}));
		int i = 0;
		for(Map<?, ?> guiItem: Crates.getInstance().getData().getMapList("crates." + id + ".gui")) {
			int i2 = i;
			this.setIcon(i, new Icon(MapUtil.getItemStack(guiItem.get("itemstack"))).addClickAction(new ClickAction() {
				final int id = i2;
				@Override
				public void execute(InventoryClickEvent e) {
					e.getWhoClicked().openInventory(new GuiItemEditHolder(e.getWhoClicked().getUniqueId(), id).getInventory());
				}
				
			}));
			i++;
		}
		
	}

	
	
}
