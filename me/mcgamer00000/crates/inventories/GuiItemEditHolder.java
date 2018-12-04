package me.mcgamer00000.crates.inventories;

import java.util.HashMap;
import java.util.List;
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

public class GuiItemEditHolder extends BaseInvHolder {

	@SuppressWarnings("unchecked")
	public GuiItemEditHolder(UUID u, int id) {
		super(54, "&4Gui Item Edit");
		
		Crates pl = Crates.getInstance();
		String crateId = pl.getPlayerInMenu(u).getCrateId();
		List<Map<String, Object>> items = (List<Map<String, Object>>) pl.getData().getList("crates." + crateId + ".gui");
		if(items.size() >= id || items.get(id).isEmpty()) {
			for(int i = items.size(); i <= id; i++) {
				items.add(new HashMap<String, Object>());
			}
			HashMap<String, Object> item = (HashMap<String, Object>) items.get(id);
			item.put("itemstack", MapUtil.getMapFromItem(new ItemStack(Material.STAINED_GLASS_PANE)));
			item.put("type", "decorative");
		}
		
		generateIcons(crateId, id);
		
	}
	
	@SuppressWarnings("unchecked")
	public void generateIcons(String crateId, int id) {
		Crates pl = Crates.getInstance();
		Map<String, Object> item = (Map<String, Object>) pl.getData().getList("crates." + crateId + ".gui").get(id);
		
		ItemStack decoration = ItemStackUtil.createItem(Material.DOUBLE_PLANT, "&6Decorative Item &e(" + (item.get("type").equals("decorative") ? "&aYes" : "&cNo") + "&e)");
		this.setIcon(3, new Icon(decoration).addClickAction(new ClickAction() {
			
			@Override
			public void execute(InventoryClickEvent e) {
				List<Map<String, Object>> list = (List<Map<String, Object>>) pl.getData().getList("crates." + crateId + ".gui");
				Map<String, Object> item = list.get(id);
				item.put("type", "decorative");
				pl.getData().set("crates." + crateId + ".gui", list);
				pl.saveData();
				generateIcons(crateId, id);
			}
			
		}));
	}

}
