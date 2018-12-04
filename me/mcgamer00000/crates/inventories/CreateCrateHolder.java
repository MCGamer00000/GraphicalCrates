package me.mcgamer00000.crates.inventories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import me.mcgamer00000.crates.Crates;
import me.mcgamer00000.crates.listeners.ChatListener;
import me.mcgamer00000.crates.utils.ChatData;
import me.mcgamer00000.crates.utils.ChatReason;
import me.mcgamer00000.crates.utils.ClickAction;
import me.mcgamer00000.crates.utils.Icon;
import me.mcgamer00000.crates.utils.ItemStackUtil;
import me.mcgamer00000.crates.utils.ToggleableIcon;

public class CreateCrateHolder extends BaseInvHolder {

	public Map<UUID, ChatData> chatReasons = new HashMap<>();
	private String id;
	
	
	public CreateCrateHolder(String id) {
		super(54, "&4Create Crate");
		this.id = id;
		
		Crates pl = Crates.getInstance();
		
		if(!pl.getData().contains("crates." + id)) {
			pl.getData().set("crates." + id + ".name", "Unnamed Crate " + id);
			pl.getData().set("crates." + id + ".material", Material.STONE.toString());
			pl.getData().set("crates." + id + ".rewards", new ArrayList<Map<String, Object>>());
			pl.getData().set("crates." + id + ".openInv", false);
			pl.getData().set("crates." + id + ".gui", new ArrayList<>());
			pl.saveData();
		}

		for(int i = 0; i < 54; i++) {
			this.setIcon(i, new Icon(ItemStackUtil.createItem(Material.STAINED_GLASS_PANE, "&0")));
		}
		
		// Creating the "Rename Crate" item
		
		ItemStack crateName = ItemStackUtil.createItem(Material.GOLDEN_APPLE, "&6Rename Crate");
		BaseInvHolder holder = this;
		this.setIcon(11, new Icon(crateName).addClickAction(new ClickAction() {

			// On click "Rename Crate" item, close the inventory, and wait for chat input. See ChatListener & onChat method (at bottom)
			@Override
			public void execute(InventoryClickEvent e) {
				String id = Crates.getInstance().getCrateId(e);
				chatReasons.put(e.getWhoClicked().getUniqueId(), new ChatData(id, ChatReason.RENAME_CRATE));
				ChatListener.addChatListener(e.getWhoClicked().getUniqueId(), holder, "Type in chat to rename the crate. ");
				e.getWhoClicked().closeInventory();
			}
			
		}));
		
		ItemStack material = ItemStackUtil.createItem(Material.GRASS, "&6Change Material");
		this.setIcon(15, new Icon(material).addClickAction(new ClickAction() {

			@Override
			public void execute(InventoryClickEvent e) {
				CrateMaterialInv inv = new CrateMaterialInv();
				Crates.getInstance().addMenuToPlayer(e.getWhoClicked().getUniqueId(), inv);
				e.getWhoClicked().openInventory(inv.getInventory());
			}
			
		}));
		
		ItemStack delete = ItemStackUtil.createItem(Material.REDSTONE_BLOCK, "&cDelete");
		this.setIcon(45, new Icon(delete).addClickAction(new ClickAction() {

			@Override
			public void execute(InventoryClickEvent e) {
				ItemStack delete = ItemStackUtil.createItem(Material.GOLD_BLOCK, "&6Are you sure?", "&6This will be removed forever.");
				setIcon(45, new Icon(delete).addClickAction(new ClickAction() {

					@SuppressWarnings("unchecked")
					@Override
					public void execute(InventoryClickEvent e) {
						String id = Crates.getInstance().getCrateId(e);
						pl.getData().set("crates." + id, null);
						List<String> cratesList = (List<String>) pl.getData().getList("crateList");
						if(cratesList.contains(id)) cratesList.remove(id);
						pl.getData().set("crateList", cratesList);
						pl.saveData();
						Crates.getInstance().getPlayerInMenu(e.getWhoClicked().getUniqueId()).popInventory();
						e.getWhoClicked().openInventory(new CrateMenuHolder((Player) e.getWhoClicked()).getInventory());
					}
					
				}));
				e.getWhoClicked().openInventory(getInventory());
			}
			
		}));
		ItemStack exit = ItemStackUtil.createItem(Material.IRON_TRAPDOOR, "&cClose");
		this.setIcon(53, new Icon(exit).addClickAction(new ClickAction() {

			@Override
			public void execute(InventoryClickEvent e) {
				//Crates.getInstance().getPlayerInMenu(e.getWhoClicked().getUniqueId()).popInventory();
				e.getWhoClicked().openInventory(new CrateMenuHolder((Player) e.getWhoClicked()).getInventory());
			}
			
		}));
		
		ItemStack reward = ItemStackUtil.createItem(Material.DIAMOND, "&6Rewards");
		this.setIcon(38, new Icon(reward).addClickAction(new ClickAction() {

			@Override
			public void execute(InventoryClickEvent e) {
				RewardHolder gui = new RewardHolder(Crates.getInstance().getCrateId(e));
				Crates.getInstance().getPlayerInMenu(e.getWhoClicked().getUniqueId()).addInventory(gui);
				e.getWhoClicked().openInventory(gui.getInventory());
			}
			
		}));
		
		ItemStack gui = ItemStackUtil.createItem(Material.CHEST, "&6Edit GUI");
		this.setIcon(40, new Icon(gui).addClickAction(new ClickAction() {

			@Override
			public void execute(InventoryClickEvent e) {
				GuiChooserHolder gui = new GuiChooserHolder();
				Crates.getInstance().getPlayerInMenu(e.getWhoClicked().getUniqueId()).addInventory(gui);
				e.getWhoClicked().openInventory(gui.getInventory());
			}
			
		}));
		
		this.setIcon(42, new ToggleableIcon("crates." + id + ".openInv", this, ItemStackUtil.createItem(Material.WOOL, "&aWill Open Inv? (On)", 5), ItemStackUtil.createItem(Material.WOOL, "&cWill Open Inv? (Off)", 14)));
		
		//updateCrate();
	}
	
	@Override
	public void updateCrate() {
		
		Crates pl = Crates.getInstance();
		
		// Add "Open Inv" boolean item to inventory, its a method because its called outside of construction
		//openInvIconUpdate(id);

		// Add an item representative of the crate, to see what it looks like.
		ItemStack representative = ItemStackUtil.createItem(Material.valueOf((String) pl.getData().get("crates." + id + ".material")), cc("&c" + (String) pl.getData().get("crates." + id + ".name")));
		this.setIcon(13, new Icon(representative));
		
	}
	/*
	private void openInvIconUpdate(String id) {
		Crates pl = Crates.getInstance();
		ItemStack openInv = pl.getData().getBoolean("crates." + id + ".openInv") ? ItemStackUtil.createItem(Material.WOOL, "&aWill Open Inv? (On)", 5) : ItemStackUtil.createItem(Material.WOOL, "&cWill Open Inv? (Off)", 14);

		this.setIcon(42, new Icon(openInv).addClickAction(new ClickAction() {
			@Override
			public void execute(InventoryClickEvent e) {
				String crateId = Crates.getInstance().getCrateId(e);
				pl.getData().set("crates." + crateId + ".openInv", !((boolean) pl.getData().get("crates." + crateId + ".openInv")));
				openInvIconUpdate(crateId);
				e.getWhoClicked().openInventory(getInventory());
			}
		}));
		
	}*/

	@Override
	public void onChat(AsyncPlayerChatEvent e) {
		ChatData info = chatReasons.get(e.getPlayer().getUniqueId());
		ChatReason reason = info.getReason();
		Crates pl = Crates.getInstance();
		if(reason == ChatReason.RENAME_CRATE) {
			pl.getData().set("crates." + info.getId() + ".name", e.getMessage());
			pl.saveData();
			ItemStack representative = ItemStackUtil.createItem(Material.valueOf((String) pl.getData().get("crates." + info.getId() + ".material")), cc("&c" + (String) pl.getData().get("crates." + info.getId() + ".name")));
			this.setIcon(13, new Icon(representative));
		}
	}
	
	
}
