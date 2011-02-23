package edu.berkeley.cs.cs162;

import java.util.LinkedList;
import java.util.List;

public class ChatLog {
	private List<Message> log;
	private User user;
	private String source;
	
	public ChatLog(String source, User user){
		this.source = source;
		this.user = user;
		log = new LinkedList<Message>();
	}
	
	public void add(Message message){ log.add(message);}
	
	public String getSource(){ return source;}
	
	public String toString(){
		return null;
	}
	
	
}
