package edu.berkeley.cs.cs162;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class User extends BaseUser {
	
	private ChatServer server;
	private String username;
	private List<ChatGroup> groupsJoined;
	private Map<String, ChatLog> chatlogs;
	
	public User(ChatServer server, String username) {
		this.server = server;
		this.username = username;
		groupsJoined = new LinkedList<ChatGroup>();
		chatlogs = new HashMap<String, ChatLog>();
	}
	
	public void getGroups() { 
		Set<String> groups = server.getGroups();
		// Do something with group list
	}
	
	public void getUsers() {
		Set<String> users = server.getUsers();
		// Do something with users list
	}
	
	public void send(String dest, String msg) {
		MsgSendError msgStatus = server.processMessage(username, dest, msg);
		// Do something with message send error
	}
	
	public void msgReceived(Message msg) {
		// Add to chatlog
		String source = msg.getSource();
		ChatLog log;
		if (chatlogs.containsKey(source)) {
			log = chatlogs.get(source);
		} else {
			log = new ChatLog(source, username);
		}
		log.add(msg);
		msgReceived(msg.getContent());
	}
	
	public void msgReceived(String msg) {
		System.out.println(username + " received the message: " + msg);
	}
	
}
