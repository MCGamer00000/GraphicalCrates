package me.mcgamer00000.crates.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.mcgamer00000.crates.inventories.BaseInvHolder;
import me.mcgamer00000.crates.utils.ClickAction;
import me.mcgamer00000.crates.utils.Icon;

public class InventoryClick implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent event) {
	    if (event.getView().getTopInventory().getHolder() instanceof BaseInvHolder) {
	        event.setCancelled(true);
	        if (event.getWhoClicked() instanceof Player) {
	            ItemStack itemStack = event.getCurrentItem();
	            if (itemStack == null || itemStack.getType() == Material.AIR) return;
	            BaseInvHolder createCrateInvHolder = (BaseInvHolder) event.getView().getTopInventory().getHolder();
	            if(!event.getClickedInventory().equals(event.getView().getTopInventory())) {
	            	createCrateInvHolder.onBottomClick(event);
	            	return;
	            }
	            Icon icon = createCrateInvHolder.getIcon(event.getRawSlot());
	            if (icon == null) return;
	            if (icon.clickActions.size() == 0) {
	            	event.setCancelled(true);
	            	return;
	            }
	            for (ClickAction clickAction : icon.getClickActions()) {
	                clickAction.execute(event);
	            }
	        }
	    }
	}
	
}
