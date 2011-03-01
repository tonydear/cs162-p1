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
	
	public BaseUser getUser() {
		return user;
	}
	
	public String getSource() { 
		return source;
	}
	
	public String getGroup() {
		return chatgroup;
	}
	
	public void add(Message message) { 
		log.add(message);
	}
	
	public String toString() {
		String ret = new String();
		
		if (chatgroup != null) {
			ret = ret.concat("ChatGroup: ");
			ret = ret.concat(chatgroup);
			ret = ret.concat("\n");
		}
		
		/*ret = ret.concat("User: ");
		ret = ret.concat(user.toString());
		ret = ret.concat("\n");
		ret = ret.concat("Source: ");
		ret = ret.concat(source);
		ret = ret.concat("\n");*/
		
		Iterator<Message> iter = log.iterator();
		while (iter.hasNext()) {
			Message msg = iter.next();
			ret = ret.concat("Source: " + msg.getSource() + "\n");
			//System.out.println(msg.getTimestamp());
			ret = ret.concat(msg.getTimestamp());
			ret = ret.concat(": ");
			ret = ret.concat(msg.getContent());
			ret = ret.concat("\n");
		}
		
		return ret;
	}
	
	public boolean isSuperLogOf(ChatLog chatLog) {
		boolean isSub = false;
		String match = chatLog.getLog().get(0).toString();
		Iterator<Message> iter = log.iterator();
		while(iter.hasNext()) {
			Message test = iter.next();
			if(test.toString().equals(match)) {
				isSub = true;
				for(Message head : chatLog.getLog()) {
					//System.out.println(head.toString());
					//System.out.println(test.toString());
					if(!head.toString().equals(test.toString())) {
						isSub = false;
						break;
					}
					if(iter.hasNext())
						test = iter.next();
				}
				break;
			}
		}
		return isSub;
	}
}
