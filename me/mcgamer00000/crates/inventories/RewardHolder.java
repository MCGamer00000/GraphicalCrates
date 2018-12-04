package me.mcgamer00000.crates.inventories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mcgamer00000.crates.Crates;
import me.mcgamer00000.crates.utils.ClickAction;
import me.mcgamer00000.crates.utils.Icon;
import me.mcgamer00000.crates.utils.ItemStackUtil;
import me.mcgamer00000.crates.utils.MapUtil;

public class RewardHolder extends BaseInvHolder {

	int page = 0;
	String id;
	
	public RewardHolder(String id) {
		super(54, "&4Crate Rewards: Page Null");
		Crates pl = Crates.getInstance();
		this.id = id;
		updateCrate();
		for(int i = 45; i < 54; i++) {
			this.setIcon(i, new Icon(ItemStackUtil.createItem(Material.STAINED_GLASS_PANE, "&0")));
		}
		
		ItemStack closeItem = ItemStackUtil.createItem(Material.IRON_TRAPDOOR, "&cClose Menu");
		this.setIcon(53, new Icon(closeItem).addClickAction(new ClickAction() {
			@Override
			public void execute(InventoryClickEvent e) {
				e.getWhoClicked().openInventory(Crates.getInstance().getPlayerInMenu(e.getWhoClicked().getUniqueId()).popInventory().getInventory());
			}
		}));
		
		ItemStack newReward = ItemStackUtil.createItem(Material.DIAMOND, "&6&lCreate New Reward", "&6You can also click on an item in your inventory to add it.");
		this.setIcon(49, new Icon(newReward).addClickAction(new ClickAction() {

			@SuppressWarnings("unchecked")
			@Override
			public void execute(InventoryClickEvent e) {
				
				String id = Crates.getInstance().getPlayerInMenu(e.getWhoClicked().getUniqueId()).getCrateId();
				
				List<Map<String, Object>> rewards = (List<Map<String, Object>>) pl.getData().getList("crates." + id + ".rewards" );
				rewards.add(MapUtil.createNewMap(new ItemStack(Material.DIAMOND), 0.01f));
				pl.getData().set("crates." + id + ".rewards", rewards);
				pl.saveData();
				
				NewRewardHolder gui = new NewRewardHolder(id, rewards.size() - 1);
				Crates.getInstance().addMenuToPlayer(e.getWhoClicked().getUniqueId(), gui);
				e.getWhoClicked().openInventory(gui.getInventory());
			}
			
		}));
		
		ItemStack previousPage = ItemStackUtil.createItem(Material.PAPER, "&6&lPrevious Page");
		this.setIcon(47, new Icon(previousPage).addClickAction(new ClickAction() {

			@Override
			public void execute(InventoryClickEvent e) {
				if(page > 0) {
					page -= 1;
					e.getWhoClicked().openInventory(getInventory());
				}
			}
			
		}));
		
		ItemStack nextPage = ItemStackUtil.createItem(Material.PAPER, "&6&lNext Page");
		this.setIcon(51, new Icon(nextPage).addClickAction(new ClickAction() {

			@Override
			public void execute(InventoryClickEvent e) {
				String cId = Crates.getInstance().getCrateId(e);
				if(page*45 < pl.getData().getList("crates." + cId + ".rewards").size()) {
					page += 1;
					e.getWhoClicked().openInventory(getInventory());
				}
			}
			
		}));
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void updateCrate() {
		setTitle("&4Crate Rewards: Page " + page);
		
		Crates pl = Crates.getInstance();
		for(int i = 0; i < 45; i++) {
			removeIcon(i);
		}
		
		List<Map<String, Object>> crateRewards = (List<Map<String, Object>>) 
				pl.getData().getList("crates." + id + ".rewards");
		
		if(crateRewards == null) return;
		crateRewards = crateRewards.subList(45*page, Math.min(45*page+45, crateRewards.size()));
		
		for(int i = 0; i < crateRewards.size(); i++) {
			Map<String, Object> map = crateRewards.get(i);
			
			ItemStack item = MapUtil.getItemStack(map.get("itemstack"));
			ItemMeta meta = item.getItemMeta();
			List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<String>();
			lore.add(cc("&6&lChance: &c" + (toDouble(map.get("chance")) * 100) + "%"));
			meta.setLore(lore);
			item.setItemMeta(meta);
			
			final int ind = i+45*page;
			this.setIcon(i, new Icon(item).addClickAction(new ClickAction() {
				@Override
				public void execute(InventoryClickEvent e) {
					String id = Crates.getInstance().getCrateId(e);
					BaseInvHolder gui = new NewRewardHolder(id, ind);
					Crates.getInstance().getPlayerInMenu(e.getWhoClicked().getUniqueId()).addInventory(gui);
					e.getWhoClicked().openInventory(gui.getInventory());
				}
				
			}));
			
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onBottomClick(InventoryClickEvent event) {
		Crates pl = Crates.getInstance();
		String id = pl.getPlayerInMenu(event.getWhoClicked().getUniqueId()).getCrateId();
		
		List<Map<String, Object>> rewards = (List<Map<String, Object>>) pl.getData().getList("crates." + id + ".rewards" );
		rewards.add(MapUtil.createNewMap(event.getClickedInventory().getItem(event.getSlot()), 0.01f));
		event.setCancelled(true);
		pl.getData().set("crates." + id + ".rewards", rewards);
		event.getWhoClicked().openInventory(new NewRewardHolder(id, rewards.size() - 1).getInventory());
	}
	
	public double toDouble(Object d) {
		return Double.valueOf(d + "");
	}

	
	
}
