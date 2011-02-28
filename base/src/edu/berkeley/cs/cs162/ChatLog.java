package edu.berkeley.cs.cs162;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ChatLog {
	private List<Message> log;
	private User user;
	private String source;
	private String chatgroup;
	
	public ChatLog(String source, User user) {
		this.source = source;
		this.user = user;
		log = new LinkedList<Message>();
		chatgroup = null;
	}
	
	public ChatLog(String source, User user, String chatgroup) {
		this.source = source;
		this.user = user;
		log = new LinkedList<Message>();
		this.chatgroup = chatgroup;
	}
	
	public List<Message> getLog() {
		return log;
	}
	
	public User getUser() {
		return user;
	}
	
	public void add(Message message) { 
		log.add(message);
	}
	
	public String getSource() { 
		return source;
	}
	
	public String getGroup() {
		return chatgroup;
	}
	
	public String toString() {
		String ret = new String();
		
		if (chatgroup != null) {
			ret.concat("ChatGroup: ");
			ret.concat(chatgroup);
			ret.concat("\n");
		}
		
		ret.concat("User: ");
		ret.concat(user.toString());
		ret.concat("\n");
		ret.concat("Source: ");
		ret.concat(source);
		ret.concat("\n");
		
		Iterator<Message> iter = log.iterator();
		while (iter.hasNext()) {
			Message msg = iter.next();
			ret.concat(msg.getTimestamp());
			ret.concat(": ");
			ret.concat(msg.getContent());
			ret.concat("\n");
		}
		
		return ret;
	}
	
	public List<Message> getMessages(){
		return log;
	}
}
