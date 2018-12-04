package me.mcgamer00000.crates;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.mcgamer00000.crates.cmds.CratesCommand;
import me.mcgamer00000.crates.inventories.BaseInvHolder;
import me.mcgamer00000.crates.listeners.ChatListener;
import me.mcgamer00000.crates.listeners.InventoryClick;
import me.mcgamer00000.crates.utils.PlayerInMenu;

public class Crates extends JavaPlugin{

	private static Crates instance;
	private Map<UUID, PlayerInMenu> playersInMenu = new HashMap<>();
	
	File crates = new File("./cratetypes");
	private File dataFile = new File(getDataFolder(), "/data.yml");
	private FileConfiguration dataConfiguration;
	
	@Override
	public void onEnable() {
		instance = this;
		Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
		Bukkit.getPluginManager().registerEvents(new InventoryClick(), this);
		Bukkit.getPluginCommand("crates").setExecutor(new CratesCommand(this));
		if(!crates.exists()) {
			crates.mkdir();
		}
		if(!(new File(this.getDataFolder(), "config.yml").exists()))
			this.getConfig().options().copyDefaults(true);
		loadDataFile();
	}
	
	public void loadDataFile() {
		if(!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		if(!dataFile.exists()) {
			try {
				dataFile.createNewFile();
				dataConfiguration = YamlConfiguration.loadConfiguration(dataFile);
				getData().set("cratesAmount", 0);
				getData().set("crateList", new ArrayList<String>());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			dataConfiguration = YamlConfiguration.loadConfiguration(dataFile);
		}
		saveData();
	}

	public static Crates getInstance() {
		return instance;
	}

	public FileConfiguration getData() {
		return dataConfiguration;
	}
	
	public void saveData() {
		try {
			dataConfiguration.save(dataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addPlayerInMenu(PlayerInMenu p) {
		playersInMenu.put(p.getUUID(), p);
	}
	
	public void removePlayerInMenu(UUID u) {
		if(playersInMenu.containsKey(u)) playersInMenu.remove(u);
	}
	
	public void addMenuToPlayer(UUID u, BaseInvHolder inv) {
		if(playersInMenu.containsKey(u)) playersInMenu.get(u).addInventory(inv);
	}
	
	public BaseInvHolder popMenuFromPlayer(UUID u) {
		if(playersInMenu.containsKey(u)) return playersInMenu.get(u).getLastInventory();
		return null;
	}
	
	public PlayerInMenu getPlayerInMenu(UUID u) {
		if(playersInMenu.containsKey(u)) return playersInMenu.get(u);
		return null;
	}

	public void addPlayerInMenu(Player p, String crateId, BaseInvHolder inv) {
		this.addPlayerInMenu(new PlayerInMenu(p.getUniqueId()));
		this.addMenuToPlayer(p.getUniqueId(), inv);
	}

	public String getCrateId(InventoryClickEvent e) {
		return this.getPlayerInMenu(e.getWhoClicked().getUniqueId()).getCrateId();
	}
	
}
