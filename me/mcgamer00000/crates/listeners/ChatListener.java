package me.mcgamer00000.crates.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.mcgamer00000.crates.inventories.BaseInvHolder;
import me.mcgamer00000.crates.utils.ChatListenerInfo;

public class ChatListener implements Listener {

	private static Map<UUID, ChatListenerInfo> chatListeners = new HashMap<>();
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onChat(AsyncPlayerChatEvent e) {
		if(!chatListeners.containsKey(e.getPlayer().getUniqueId())) return;
		e.setCancelled(true);
		ChatListenerInfo info = chatListeners.get(e.getPlayer().getUniqueId());
		info.cancelRunnable();
		BaseInvHolder holder = info.getHolder();
		if(!e.getMessage().equals("cancel")) {
			holder.onChat(e);
		}
		e.getPlayer().openInventory(holder.getInventory());
		chatListeners.remove(e.getPlayer().getUniqueId());
	}
	
	public static void addChatListener(UUID uuid, BaseInvHolder holder, String message) {
		UUID u = uuid;
		BukkitRunnable runnable = new BukkitRunnable() {
			@Override
			public void run() {
				Bukkit.getPlayer(u).sendMessage(cc("&c[&eCrates&c] &6" + message + "\n&c[&eCrates&c] &6Type \"cancel\" to cancel."));
			}
		};
		chatListeners.put(uuid, new ChatListenerInfo(holder, runnable));
	}
	
	private static String cc(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	
	
}
