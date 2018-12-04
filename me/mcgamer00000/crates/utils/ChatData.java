package me.mcgamer00000.crates.utils;

public class ChatData {
	
	String id;
	ChatReason reason;
	
	public ChatData(String id, ChatReason reason) {
		this.id = id;
		this.reason = reason;
	}
	
	public ChatReason getReason() {
		return reason;
	}
	
	public String getId() {
		return id;
	}
	
}