package edu.berkeley.cs.cs162;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.sun.tools.javac.util.Pair;

public class User extends BaseUser {
	
	private ChatServer server;
	private String username;
	private List<String> groupsJoined;
	private Map<String, ChatLog> chatlogs;
	private Queue<Message> toRecv;
	private Queue<Pair<String,String>> toSend;
	
	public User(ChatServer server, String username) {
		this.server = server;
		this.username = username;
		groupsJoined = new LinkedList<String>();
		chatlogs = new HashMap<String, ChatLog>();
		toRecv = new LinkedList<Message>();
		toSend = new LinkedList<Pair<String,String>>();
	}
	
	public void getGroups() { 
		Set<String> groups = server.getGroups();
		// Do something with group list
	}
	
	public void getUsers() {
		Set<String> users = server.getUsers();
		// Do something with users list
	}
	
	public List<String> getUserGroups() {
		return groupsJoined;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void send(String dest, String msg) {
		Pair<String,String> pair = new Pair<String,String>(dest, msg);
		toSend.add(pair);
	}
	
	public void msgReceived(Message msg) {
		toRecv.add(msg);		
	}
	
	public void msgReceived(String msg) {
		System.out.println(username + " received the message: " + msg);
	}
	
	public void run() {
		while(!toSend.isEmpty()) {
			Pair<String,String> pair = toSend.poll();
			MsgSendError msgStatus = server.processMessage(username, pair.fst, pair.snd);
			// Do something with message send error
		}
		while(!toRecv.isEmpty()) {
			Message msg = toRecv.poll();
			logRecvMsg(msg);
			msgReceived(msg.getContent());
		}
	}
	
	private void logRecvMsg(Message msg) {
		// Add to chatlog
		String source = msg.getSource();
		ChatLog log;
		if (chatlogs.containsKey(source)) {
			log = chatlogs.get(source);
		} else {
			log = new ChatLog(source, this);
			chatlogs.put(msg.getSource(), log);
		}
		log.add(msg);
	}
	
}
