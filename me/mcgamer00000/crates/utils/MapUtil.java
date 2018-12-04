package me.mcgamer00000.crates.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MapUtil {

	public static Map<String, Object> createNewMap(ItemStack item, float f) {
		Map<String, Object> map = new HashMap<>();
		map.put("itemstack", getMapFromItem(item));
		map.put("chance", f);
		map.put("giveItem", true);
		map.put("cmds", new ArrayList<String>());
		return map;
	}

	@SuppressWarnings("unchecked")
	public static ItemStack getItemStack(Object object) {
		Map<String, Object> map = (Map<String, Object>) object;
		ItemStack item = new ItemStack(Material.valueOf((String) map.get("type")));
		ItemMeta meta = item.getItemMeta();
		if(map.containsKey("name")) meta.setDisplayName((String) map.get("name"));
		if(map.containsKey("lore")) meta.setLore((List<String>) map.get("lore"));
		item.setItemMeta(meta);
		if(map.containsKey("ench")) item.addUnsafeEnchantments(getEnchants((List<Map<String, Object>>) map.get("ench")));
		if(map.containsKey("damage")) item.setDurability(Short.valueOf(map.get("damage") + ""));
		if(map.containsKey("count")) item.setAmount((int) map.get("count"));
		return item;
	}
	
	public static Map<Enchantment, Integer> getEnchants(List<Map<String, Object>> enchs) {
		Map<Enchantment, Integer> enchants = new HashMap<>();
		for(Map<String, Object> enchant: enchs) {
			enchants.put(Enchantment.getByName((String) enchant.get("name")), (Integer) enchant.get("level"));
		}
		return enchants;
	}
	
	public static List<Map<String,Object>> serializeEnchants(Map<Enchantment, Integer> enchants) {
		List<Map<String, Object>> enchs = new ArrayList<>();
		for(Enchantment enchant: enchants.keySet()) {
			Map<String, Object> ench = new HashMap<>();
			ench.put("name", enchant.getName());
			ench.put("level", enchants.get(enchant));
			enchs.add(ench);
		}
		return enchs;
	}
	
	public static Map<String, Object> getMapFromItem(ItemStack item) {
		Map<String, Object> map = new HashMap<String, Object>();
		ItemMeta meta = item.getItemMeta();
		map.put("damage", item.getDurability());
		map.put("name", meta.getDisplayName());
		map.put("ench", serializeEnchants(item.getEnchantments()));
		map.put("lore", meta.getLore());
		map.put("type", item.getType().toString());
		map.put("count", item.getAmount());
		return map;
	}
	
}
