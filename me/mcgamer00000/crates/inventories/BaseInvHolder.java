package me.mcgamer00000.crates.inventories;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import me.mcgamer00000.crates.utils.Icon;

public class BaseInvHolder implements InventoryHolder {

	private final Map<Integer, Icon> icons = new  HashMap<>();

    private final int size;
    private String title;

    public BaseInvHolder(int size, String title) {
        this.size = size;
        this.title = cc(title);
    }

    public void setIcon(int position, Icon icon) {
        this.icons.put(position, icon);
    }
 
    public Icon getIcon(int position) {
        return this.icons.get(position);
    }
    
	public void removeIcon(int pos) {
		if(this.icons.containsKey(pos)) this.icons.remove(pos);
	}

    @Override
    public Inventory getInventory() {
        
    	updateCrate();
    	
    	Inventory inventory = Bukkit.createInventory(this, this.size, this.title);
        
        //You should check for inventory size so you don't get errors
        for (Entry<Integer, Icon> entry : this.icons.entrySet()) {
            inventory.setItem(entry.getKey(), entry.getValue().itemStack);
        }
   
        return inventory;
    }
    
	protected String cc(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	
	protected void setTitle(String title) {
		this.title = cc(title);
	}
	
	public void onChat(AsyncPlayerChatEvent e) {}

	public void onBottomClick(InventoryClickEvent event) {}

	public void updateCrate() {}

}
