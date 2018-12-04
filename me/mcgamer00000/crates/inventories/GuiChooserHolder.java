package me.mcgamer00000.crates.inventories;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.mcgamer00000.crates.Crates;
import me.mcgamer00000.crates.utils.ClickAction;
import me.mcgamer00000.crates.utils.Icon;
import me.mcgamer00000.crates.utils.ItemStackUtil;

public class GuiChooserHolder extends BaseInvHolder {
	
	public GuiChooserHolder() {
		super(27, "&4Edit GUI");
			
		for(int i = 0; i < 27; i++) {
			this.setIcon(i, new Icon(ItemStackUtil.createItem(Material.STAINED_GLASS_PANE, "&0")));
		}
		
		ItemStack guiEditor = ItemStackUtil.createItem(Material.DIAMOND, "&6GUI Editor");
		this.setIcon(11, new Icon(guiEditor).addClickAction(new ClickAction() {
			@Override
			public void execute(InventoryClickEvent e) {
				GuiEditHolder gui = new GuiEditHolder(e.getWhoClicked().getUniqueId());
				Crates.getInstance().getPlayerInMenu(e.getWhoClicked().getUniqueId()).addInventory(gui);
				e.getWhoClicked().openInventory(gui.getInventory());
			}
		}));

		ItemStack exit = ItemStackUtil.createItem(Material.IRON_TRAPDOOR, "&cExit");
		this.setIcon(26, new Icon(exit).addClickAction(new ClickAction() {

			@Override
			public void execute(InventoryClickEvent e) {
				e.getWhoClicked().openInventory(Crates.getInstance().getPlayerInMenu(e.getWhoClicked().getUniqueId()).popInventory().getInventory());
			}
			
		}));
		
	}

}
