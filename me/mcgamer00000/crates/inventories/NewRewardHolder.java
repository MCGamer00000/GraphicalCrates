package me.mcgamer00000.crates.inventories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mcgamer00000.crates.Crates;
import me.mcgamer00000.crates.listeners.ChatListener;
import me.mcgamer00000.crates.utils.ChatData;
import me.mcgamer00000.crates.utils.ChatReason;
import me.mcgamer00000.crates.utils.ClickAction;
import me.mcgamer00000.crates.utils.Icon;
import me.mcgamer00000.crates.utils.ItemStackUtil;
import me.mcgamer00000.crates.utils.MapUtil;
import me.mcgamer00000.crates.utils.ToggleableIcon;

public class NewRewardHolder extends BaseInvHolder {

	public Map<UUID, ChatData> chatReasons = new HashMap<>();
	String crateId;
	int id;
	private int selectedCmd = 0;
	
	@SuppressWarnings("unchecked")
	public NewRewardHolder(String crateId, int id) {
		super(54, "&4Crate Reward");
		this.crateId = crateId;
		this.id = id;
		
		Crates pl = Crates.getInstance();
		List<Map<String, Object>> rewards = (List<Map<String, Object>>) 
				pl.getData().getList("crates." + crateId + ".rewards");
		ItemStack item = MapUtil.getItemStack(rewards.get(id).get("itemstack"));
		
		//00,01,02,03,04,05,06,07,08
		//09,10,11,12,13,14,15,16,17 # Representative
		//18,19,20,21,22,23,24,25,26 # Material, Name, Line 1 Lore, Durability
		//27,28,29,30,31,32,33,34,35 #
		//36,37,38,39,40,41,42,43,44 # Probability 
		//45,46,47,48,49,50,51,52,53 # Delete, Save
		
		for(int i = 0; i < 54; i++) {
			this.setIcon(i, new Icon(ItemStackUtil.createItem(Material.STAINED_GLASS_PANE, "&0")));
		}
		
		// Material, Representative, Set Name, Proability +10,+1,+0.1,+0.01,Show, & negate
		// Delete, Exit
		
		this.setIcon(13, new Icon(item.clone()).addClickAction(new ClickAction() {

			@Override
			public void execute(InventoryClickEvent e) {
				e.setCurrentItem(item.clone());
			}
			
		}));
		
		NewRewardHolder holder = this;
		ItemStack changeName = ItemStackUtil.createItem(Material.PAPER, "&6Rename Item");
		this.setIcon(21, new Icon(changeName).addClickAction(new ClickAction() {

			@Override
			public void execute(InventoryClickEvent e) {
				chatReasons.put(e.getWhoClicked().getUniqueId(), new ChatData(crateId, ChatReason.RENAME_REWARD));
				ChatListener.addChatListener(e.getWhoClicked().getUniqueId(), holder, "Type in chat to rename the reward item. ");
				e.getWhoClicked().closeInventory();
			}
			
		}));
		
		addCmdsIcon(0);
		
		ItemStack changeLore = ItemStackUtil.createItem(Material.GOLDEN_APPLE, "&6Change Lore");
		this.setIcon(19, new Icon(changeLore).addClickAction(new ClickAction() {
			
			@Override
			public void execute(InventoryClickEvent e) {
				chatReasons.put(e.getWhoClicked().getUniqueId(), new ChatData(crateId, ChatReason.CHANGE_LORE_REWARD));
				ChatListener.addChatListener(e.getWhoClicked().getUniqueId(), holder, "Type in chat to change the lore of the reward item. ");
				e.getWhoClicked().closeInventory();
			}
			
		}));
		
		ItemStack onItem = ItemStackUtil.createItem(Material.BLAZE_ROD, "&6Give item on win? &e(&aYes&e)");
		ItemStack offItem = ItemStackUtil.createItem(Material.BLAZE_ROD, "&6Give item on win? &e(&cNo&e)");
		
		this.setIcon(23, new ToggleableIcon("crates." + crateId + ".rewards", id, "giveItem", this, onItem, offItem));
				
				/*.addClickAction(new ClickAction() {

			@Override
			public void execute(InventoryClickEvent e) {
				Crates pl = Crates.getInstance();
				List<Map<String, Object>> rewards = (List<Map<String, Object>>) pl.getData().getList("crates." + crateId + ".rewards");
				rewards.get(id).put("giveItem", !(boolean) rewards.get(id).get("giveItem"));
				ItemStack giveItem = ItemStackUtil.createItem(Material.BLAZE_ROD, "&6Give item on win? &e(" + ((boolean) rewards.get(id).get("giveItem") ? "&aYes" : "&cNo") + "&e)");
				pl.getData().set("crates." + crateId + ".rewards", rewards);
				pl.saveData();
				setIcon(23, new Icon(giveItem).addClickAction(this));
				e.getWhoClicked().openInventory(getInventory());
			}
			
		}));*/
		
		ItemStack changeCount = ItemStackUtil.createItem(Material.ANVIL, "&6Change Count", "&cLeft Click -1", "&cRight Click +1", "&eShift for 10x");
		this.setIcon(15, new Icon(changeCount).addClickAction(new ClickAction() {

			@Override
			public void execute(InventoryClickEvent e) {
				Crates pl = Crates.getInstance();
				List<Map<String, Object>> rewards = (List<Map<String, Object>>) pl.getData().getList("crates." + crateId + ".rewards");
				ItemStack item = MapUtil.getItemStack(rewards.get(id).get("itemstack"));
				if(e.isLeftClick()) {
					if(e.isShiftClick() && item.getAmount() > 10) { 
						item.setAmount(item.getAmount() - 10);
					} else if(e.isShiftClick()) {
						item.setAmount(1);
					} else if(item.getAmount() > 1) {
						item.setAmount(item.getAmount() - 1);
					}
				} else if(e.isRightClick()) {
					if(e.isShiftClick() && item.getAmount() < 55) { 
						item.setAmount(item.getAmount() + 10);
					} else if(e.isShiftClick()) {
						item.setAmount(64);
					} else if(item.getAmount() < 64) {
						item.setAmount(item.getAmount() + 1);
					}
				}
				rewards.get(id).put("itemstack", MapUtil.getMapFromItem(item));
				pl.getData().set("crates." + crateId + ".rewards", rewards);
				pl.saveData();
				resetItem(crateId);
				e.getWhoClicked().openInventory(getInventory());
			}
			
		}));
		
		ItemStack chance = ItemStackUtil.createItem(Material.BEACON, "&6Chance: &c" + (Math.round((toDouble(rewards.get(id).get("chance"))*10000.0f))/100.0f) + "%");
		this.setIcon(40, new Icon(chance));

		ItemStack minus1000 = ItemStackUtil.createItem(Material.STAINED_GLASS, "&c-10.0%", 14);
		ItemStack minus0100 = ItemStackUtil.createItem(Material.STAINED_GLASS, "&c-1.0%", 14);
		ItemStack minus0010 = ItemStackUtil.createItem(Material.STAINED_GLASS, "&c-0.1%", 14);
		ItemStack minus0001 = ItemStackUtil.createItem(Material.STAINED_GLASS, "&c-0.01%", 14);
		
		this.setIcon(36, new Icon(minus1000).addClickAction(new ClickAction() {public void execute(InventoryClickEvent e) {changeChance(-0.1f   );e.getWhoClicked().openInventory(getInventory());}}));
		this.setIcon(37, new Icon(minus0100).addClickAction(new ClickAction() {public void execute(InventoryClickEvent e) {changeChance(-0.01f  );e.getWhoClicked().openInventory(getInventory());}}));
		this.setIcon(38, new Icon(minus0010).addClickAction(new ClickAction() {public void execute(InventoryClickEvent e) {changeChance(-0.001f );e.getWhoClicked().openInventory(getInventory());}}));
		this.setIcon(39, new Icon(minus0001).addClickAction(new ClickAction() {public void execute(InventoryClickEvent e) {changeChance(-0.0001f);e.getWhoClicked().openInventory(getInventory());}}));
		
		ItemStack plus1000 = ItemStackUtil.createItem(Material.STAINED_GLASS, "&a+10.0%", 5);
		ItemStack plus0100 = ItemStackUtil.createItem(Material.STAINED_GLASS, "&a+1.0%", 5);
		ItemStack plus0010 = ItemStackUtil.createItem(Material.STAINED_GLASS, "&a+0.1%", 5);
		ItemStack plus0001 = ItemStackUtil.createItem(Material.STAINED_GLASS, "&a+0.01%", 5);
		
		this.setIcon(44, new Icon(plus1000 ).addClickAction(new ClickAction() {public void execute(InventoryClickEvent e) {changeChance(0.1f   );e.getWhoClicked().openInventory(getInventory());}}));
		this.setIcon(43, new Icon(plus0100 ).addClickAction(new ClickAction() {public void execute(InventoryClickEvent e) {changeChance(0.01f  );e.getWhoClicked().openInventory(getInventory());}}));
		this.setIcon(42, new Icon(plus0010 ).addClickAction(new ClickAction() {public void execute(InventoryClickEvent e) {changeChance(0.001f );e.getWhoClicked().openInventory(getInventory());}}));
		this.setIcon(41, new Icon(plus0001 ).addClickAction(new ClickAction() {public void execute(InventoryClickEvent e) {changeChance(0.0001f);e.getWhoClicked().openInventory(getInventory());}}));
		
		ItemStack delete = ItemStackUtil.createItem(Material.REDSTONE_BLOCK, "&cDelete");
		this.setIcon(47, new Icon(delete).addClickAction(new ClickAction() {

			@Override
			public void execute(InventoryClickEvent e) {
				ItemStack delete = ItemStackUtil.createItem(Material.GOLD_BLOCK, "&6Are you sure?", "&6This will be removed forever.");
				setIcon(47, new Icon(delete).addClickAction(new ClickAction() {

					@Override
					public void execute(InventoryClickEvent e) {
						String crateId = Crates.getInstance().getPlayerInMenu(e.getWhoClicked().getUniqueId()).getCrateId();
						List<Map<String, Object>> rewards = (List<Map<String, Object>>) pl.getData().getList("crates." + crateId + ".rewards");
						if(rewards.size() > id) rewards.remove(id);
						pl.getData().set("crates." + crateId + ".rewards", rewards);
						pl.saveData();
						RewardHolder gui = new RewardHolder(crateId);
						Crates.getInstance().getPlayerInMenu(e.getWhoClicked().getUniqueId()).addInventory(gui);
						e.getWhoClicked().openInventory(gui.getInventory());
					}
					
				}));
				e.getWhoClicked().openInventory(getInventory());
			}
			
		}));
		
		ItemStack exit = ItemStackUtil.createItem(Material.IRON_TRAPDOOR, "&cSave & Exit");
		this.setIcon(51, new Icon(exit).addClickAction(new ClickAction() {

			@Override
			public void execute(InventoryClickEvent e) {
				e.getWhoClicked().openInventory(Crates.getInstance().getPlayerInMenu(e.getWhoClicked().getUniqueId()).popInventory().getInventory());
			}
			
		}));
		
	}
	
	@SuppressWarnings("unchecked")
	public void addCmdsIcon(int delta) {
		List<Map<String, Object>> rewards = (List<Map<String, Object>>) Crates.getInstance().getData().get("crates." + crateId + ".rewards");
		List<String> cmds = (List<String>) rewards.get(id).get("cmds");
		cmds.add(0, "Add New Command");
		List<String> colCmds = new ArrayList<>();
		colCmds.add(cc("&eShift Click to Edit/Create"));
		colCmds.add(cc("&eRight Click &c= &6Down"));
		colCmds.add(cc("&eLeft Click &c= &6Up"));
		cmds.forEach(s -> colCmds.add(cc("&6\u226B &e" + s)));
		if(this.selectedCmd + delta >= 0 && this.selectedCmd + delta < cmds.size()) {
			this.selectedCmd += delta;
		}
		colCmds.set(this.selectedCmd+3 , cc("&c\u226B &c" + cmds.get(this.selectedCmd) + " &c\u226A"));
		ItemStack commands = ItemStackUtil.createItem(Material.COMMAND, "&6On Win Execute Commands:", colCmds);
		NewRewardHolder holder = this;
		this.setIcon(11, new Icon(commands).addClickAction(new ClickAction() {
			@Override
			public void execute(InventoryClickEvent e) {
				if(e.isShiftClick()) {
					if(selectedCmd == 0) {
						ChatListener.addChatListener(e.getWhoClicked().getUniqueId(), holder, "Adding new Command. Type the command without the \"/\". If required, put a \".\" in front (Which will be ignored.)");
						chatReasons.put(e.getWhoClicked().getUniqueId(), new ChatData(crateId, ChatReason.ADD_NEW_COMMAND));
					} else {
						ChatListener.addChatListener(e.getWhoClicked().getUniqueId(), holder, "Editting Command. Type the new command without the \"/\". If required, put a \".\" in front (Which will be ignored.) Type \"remove\" to remove the command.");
						chatReasons.put(e.getWhoClicked().getUniqueId(), new ChatData(crateId, ChatReason.EDIT_COMMAND));
					}
					e.getWhoClicked().closeInventory();
				} else if(e.isLeftClick()) {
					addCmdsIcon(-1);
					e.getWhoClicked().openInventory(getInventory());
				} else if(e.isRightClick()) {
					addCmdsIcon(1);
					e.getWhoClicked().openInventory(getInventory());
				}
			}
		}));
		cmds.remove(0);
	}

	@SuppressWarnings("unchecked")
	public void resetItem(String crateId) {
		Crates pl = Crates.getInstance();
		List<Map<String, Object>> rewards = (List<Map<String, Object>>) pl.getData().getList("crates." + crateId + ".rewards");
		ItemStack item = MapUtil.getItemStack(rewards.get(id).get("itemstack"));
		
		this.setIcon(13, new Icon(item.clone()).addClickAction(new ClickAction() {

			@Override
			public void execute(InventoryClickEvent e) {
				e.setCurrentItem(item.clone());
			}
			
		}));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onChat(AsyncPlayerChatEvent e) {
		ChatData data = chatReasons.get(e.getPlayer().getUniqueId());
		if(data.getReason() == ChatReason.RENAME_REWARD) {
			Crates pl = Crates.getInstance();
			List<Map<String, Object>> rewards = (List<Map<String, Object>>) pl.getData().getList("crates." + data.getId() + ".rewards");
			ItemStack item = MapUtil.getItemStack(rewards.get(id).get("itemstack"));
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(cc(e.getMessage()));
			item.setItemMeta(meta);
			rewards.get(id).put("itemstack", MapUtil.getMapFromItem(item));
			pl.getData().set("crates." + data.getId() + ".rewards", rewards);
			resetItem(data.getId());
		} else if(data.getReason() == ChatReason.CHANGE_LORE_REWARD) {
			Crates pl = Crates.getInstance();
			List<Map<String, Object>> rewards = (List<Map<String, Object>>) pl.getData().getList("crates." + data.getId() + ".rewards");
			ItemStack item = MapUtil.getItemStack(rewards.get(id).get("itemstack"));
			ItemMeta meta = item.getItemMeta();
			meta.setLore(Arrays.asList(cc(e.getMessage())));
			item.setItemMeta(meta);
			rewards.get(id).put("itemstack", MapUtil.getMapFromItem(item));
			pl.getData().set("crates." + data.getId() + ".rewards", rewards);
			resetItem(data.getId());
		} else if(data.getReason() == ChatReason.ADD_NEW_COMMAND) {
			Crates pl = Crates.getInstance();
			List<Map<String, Object>> rewards = (List<Map<String, Object>>) pl.getData().getList("crates." + data.getId() + ".rewards");
			List<String> cmds = (List<String>) rewards.get(id).get("cmds");
			if(e.getMessage().startsWith(".")) {
				cmds.add(e.getMessage().substring(1));
			} else {
				cmds.add(e.getMessage());
			}
			pl.getData().set("crates." + data.getId() + ".rewards", rewards);
			pl.saveData();
			addCmdsIcon(0);
		} else if(data.getReason() == ChatReason.EDIT_COMMAND) {
			Crates pl = Crates.getInstance();
			List<Map<String, Object>> rewards = (List<Map<String, Object>>) pl.getData().getList("crates." + data.getId() + ".rewards");
			List<String> cmds = (List<String>) rewards.get(id).get("cmds");
			if(this.selectedCmd >= cmds.size()) this.selectedCmd = cmds.size() - 1;
			if(this.selectedCmd < 0) this.selectedCmd = 0;
			if(e.getMessage().equals("remove")) {
				if(cmds.size() <= this.selectedCmd) {
					cmds.remove(cmds.size()-1);
					this.selectedCmd = cmds.size() - 1;
				} else {
					cmds.remove(this.selectedCmd);
					this.selectedCmd -= 1;
				}
			} else {
				if(e.getMessage().startsWith(".")) {
					cmds.set(this.selectedCmd, e.getMessage().substring(1));
				} else {
					cmds.set(this.selectedCmd, e.getMessage());
				}
			}
			if(this.selectedCmd >= cmds.size()) this.selectedCmd = cmds.size() - 1;
			if(this.selectedCmd < 0) this.selectedCmd = 0;
			pl.getData().set("crates." + data.getId() + ".rewards", rewards);
			pl.saveData();
			addCmdsIcon(0);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void changeChance(float amt) {
		List<Map<String, Object>> rewards = (List<Map<String, Object>>) Crates.getInstance().getData().getList("crates." + crateId + ".rewards");
		Map<String, Object> map = rewards.get(id);
		float newValue = (Math.round((toDouble(map.get("chance")) + amt)*10000.0f))/10000.0f;
		if(newValue > 1.0)
			map.put("chance", 1.0f);
		else if (newValue < 0.0)
			map.put("chance", 0.0f);
		else 
			map.put("chance", newValue);
		Crates.getInstance().getData().set("crates." + crateId + ".rewards", rewards);
		this.setIcon(40, new Icon(ItemStackUtil.createItem(Material.BEACON, "&6Chance: &c" + (toDouble(map.get("chance")) * 100.0) + "%")));
		
		Crates.getInstance().saveData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onBottomClick(InventoryClickEvent event) {
		Crates pl = Crates.getInstance();
		List<Map<String, Object>> rewards = (List<Map<String, Object>>) pl.getData().getList("crates." + crateId + ".rewards" );
		rewards.get(id).put("itemstack", MapUtil.createNewMap(event.getCurrentItem(), 0.01f).get("itemstack"));
		event.setCancelled(true);
		pl.getData().set("crates." + id + ".rewards", rewards);
		pl.saveData();
		ItemStack item = event.getCurrentItem();
		this.setIcon(13, new Icon(item.clone()).addClickAction(new ClickAction() {

			@Override
			public void execute(InventoryClickEvent e) {
				e.setCurrentItem(item.clone());
			}
			
		}));
		event.getWhoClicked().openInventory(getInventory());
	}
	
	public double toDouble(Object d) {
		return Double.valueOf(d + "");
	}
	
}
