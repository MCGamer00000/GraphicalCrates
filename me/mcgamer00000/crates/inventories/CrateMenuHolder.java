package me.mcgamer00000.crates.inventories;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.mcgamer00000.crates.Crates;
import me.mcgamer00000.crates.utils.ClickAction;
import me.mcgamer00000.crates.utils.Icon;
import me.mcgamer00000.crates.utils.ItemStackUtil;
import me.mcgamer00000.crates.utils.PlayerInMenu;

public class CrateMenuHolder extends BaseInvHolder {
	
	int page = 0;
	
	public CrateMenuHolder(Player p) {
		super(54, "&4Crate Menu: Page 0");
		PlayerInMenu player = new PlayerInMenu(p.getUniqueId());
		
		Crates.getInstance().addPlayerInMenu(player);
		Crates.getInstance().addMenuToPlayer(p.getUniqueId(), this);
		
		updateCrate();
		
		for(int i = 45; i < 54; i++) {
			this.setIcon(i, new Icon(ItemStackUtil.createItem(Material.STAINED_GLASS_PANE, "&0")));
		}
		
		ItemStack closeItem = ItemStackUtil.createItem(Material.IRON_TRAPDOOR, "&cClose Menu");
		this.setIcon(53, new Icon(closeItem).addClickAction(new ClickAction() {
			@Override
			public void execute(InventoryClickEvent e) {
				e.getWhoClicked().closeInventory();
				Crates.getInstance().removePlayerInMenu(e.getWhoClicked().getUniqueId());
			}
		}));
		
		ItemStack newItem = ItemStackUtil.createItem(Material.CHEST, "&6Create New Item");
		this.setIcon(49, new Icon(newItem).addClickAction(new ClickAction() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void execute(InventoryClickEvent e) {
				
				Crates pl = Crates.getInstance();
				FileConfiguration plData = pl.getData();
				String id = plData.getInt("cratesAmount") + "";
				UUID u = e.getWhoClicked().getUniqueId();
				
				pl.getPlayerInMenu(u).setCrateId(id);
				CreateCrateHolder holder = new CreateCrateHolder(id);
				pl.addMenuToPlayer(u, holder);
				
				List<String> crates = (List<String>) plData.getList("crateList");
				crates.add(id);
				plData.set("crateList", crates);
				plData.set("cratesAmount", pl.getData().getInt("cratesAmount") + 1);
				
				e.getWhoClicked().openInventory(holder.getInventory());
			}
			
		}));
		
		ItemStack nextPage = ItemStackUtil.createItem(Material.PAPER, "&aNext Page");
		this.setIcon(50, new Icon(nextPage).addClickAction(new ClickAction() {
			
			@Override
			public void execute(InventoryClickEvent e) {
				if((page+1) * 45 < Crates.getInstance().getData().getInt("cratesAmount"))
					page += 1;
				
				updateCrate();
				Inventory inv = getInventory();
				e.getWhoClicked().openInventory(inv);
			}
			
		}));
		
		ItemStack lastPage = ItemStackUtil.createItem(Material.PAPER, "&aLast Page");
		this.setIcon(48, new Icon(lastPage).addClickAction(new ClickAction() {
			
			@Override
			public void execute(InventoryClickEvent e) {
				if(page > 0) {
					page -= 1;
					updateCrate();
					Inventory inv = getInventory();
					e.getWhoClicked().openInventory(inv);
				}
			}
			
		}));
		
		
	}

	@Override
	public void updateCrate() {
		this.setTitle("&4Crate Menu: Page " + page);
		Crates pl = Crates.getInstance();
		for(int i = 0; i < 45; i++) {
			removeIcon(i);
		}
		int i = 0;
		if(!pl.getData().contains("crateList")) pl.getData().set("crateList", new ArrayList<String>());
		List<?> crateList = pl.getData().getList("crateList");
		for(Object obj: crateList.subList(45*page, Math.min(crateList.size(), 45*page+45))) {
			String id = (String) obj;
			ItemStack item = ItemStackUtil.createItem(Material.getMaterial(pl.getData().getString("crates." + id + ".material")), cc("&c" + pl.getData().getString("crates." + id + ".name")), "&6&lId: &c" + id);
			this.setIcon(i, new Icon(item).addClickAction(new ClickAction() {
				final String crateId = id;
				@Override
				public void execute(InventoryClickEvent e) {
					Crates.getInstance().getPlayerInMenu(e.getWhoClicked().getUniqueId()).setCrateId(crateId);
					CreateCrateHolder inv = new CreateCrateHolder(crateId);
					Crates.getInstance().addMenuToPlayer(e.getWhoClicked().getUniqueId(), inv);
					e.getWhoClicked().openInventory(inv.getInventory());
				}
				
			}));
			i += 1;
			if(i >= 45) break;
		}
	}

	
	
}
