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
	
	public void send(String dest, String msg) {
		MsgSendError msgStatus = server.processMessage(username, dest, msg);
		// Do something with message send error
	}
	
}