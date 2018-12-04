package me.mcgamer00000.crates.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackUtil {

	public static ItemStack createItem(Material material, String name) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack createItem(Material material, String name, String lore) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&',lore)));
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack createItem(Material material, String name, int i) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		item.setItemMeta(meta);
		item.setDurability((short) i);
		return item;
	}
	
	public static ItemStack createItem(Material material, String name, String... lore) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		List<String> coloredLore = new ArrayList<String>();
		for(String str: lore) coloredLore.add(ChatColor.translateAlternateColorCodes('&',str));
		meta.setLore(coloredLore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack createItem(Material material, String name, List<String> lore) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		List<String> coloredLore = new ArrayList<String>();
		for(String str: lore) coloredLore.add(ChatColor.translateAlternateColorCodes('&',str));
		meta.setLore(coloredLore);
		item.setItemMeta(meta);
		return item;
	}
	
}
