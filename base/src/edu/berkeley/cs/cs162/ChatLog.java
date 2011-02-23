package edu.berkeley.cs.cs162;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ChatLog {
	private List<Message> log;
	private User user;
	private String source;
	
	public ChatLog(String source, User user) {
		this.source = source;
		this.user = user;
		log = new LinkedList<Message>();
	}
	
	public void add(Message message) { 
		log.add(message);
	}
	
	public String getSource() { 
		return source;
	}
	
	public String toString() {
		String ret = new String();
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
	
	
}
