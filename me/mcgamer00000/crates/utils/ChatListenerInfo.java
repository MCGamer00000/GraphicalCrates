package me.mcgamer00000.crates.utils;

import org.bukkit.scheduler.BukkitRunnable;

import me.mcgamer00000.crates.Crates;
import me.mcgamer00000.crates.inventories.BaseInvHolder;

public class ChatListenerInfo {
	
	private BaseInvHolder holder;
	private BukkitRunnable reminder;

	public ChatListenerInfo(BaseInvHolder holder, BukkitRunnable reminder) {
		this.holder = holder;
		this.reminder = reminder;
		reminder.runTaskTimer(Crates.getInstance(), 0, 20L * 5L);
	}

	public BaseInvHolder getHolder() {
		return holder;
	}

	public void cancelRunnable() {
		reminder.cancel();
	}
	
}