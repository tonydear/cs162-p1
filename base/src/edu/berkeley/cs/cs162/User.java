package edu.berkeley.cs.cs162;


import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class User extends BaseUser {
	
	private ChatServer server;
	private String username;
	private List<String> groupsJoined;
	private Map<String, ChatLog> chatlogs;
	private Queue<MessageJob> toSend;
	private ReentrantReadWriteLock sendLock;
	private int sqn;
	private volatile boolean loggedOff;
	private final static int MAX_SEND = 10000;
	
	public User(ChatServer server, String username) {
		this.server = server;
		this.username = username;
		groupsJoined = new LinkedList<String>();
		chatlogs = new HashMap<String, ChatLog>();
		toSend = new LinkedList<MessageJob>();
		sendLock = new ReentrantReadWriteLock(true);
		sqn = 0;
	}
	
	public String getUsername() {
		return username;
	}
	
	public List<String> getUserGroups() {
		return groupsJoined;
	}
	
	public Set<String> getAllUsers() {
		return server.getUsers();
	}
	
	public Set<String> getAllGroups() { 
		return server.getGroups();
	}
	
	public int getNumUsers() {
		return server.getNumUsers();
	}
	
	public int getNumGroups() {
		return server.getNumGroups();
	}
	
	public void addToGroups(String group) {
		groupsJoined.add(group);
	}
	
	public void removeFromGroups(String group) {
		groupsJoined.remove(group);
	}
	
	public ChatLog getLog(String name){
		if(chatlogs.containsKey(name)){
			return chatlogs.get(name);
		}
		return null;
	}
	
	public Map<String, ChatLog> getLogs() {
		return chatlogs;
	}
	
	public void send(String dest, String msg) {
		sendLock.writeLock().lock();
		if(loggedOff || toSend.size() >= MAX_SEND){
			String timestamp = Long.toString(System.currentTimeMillis()/1000);
			String formattedMsg = username + " " + dest + " " + timestamp+ " " + sqn; 
			sqn++;
			TestChatServer.logUserSendMsg(username, formattedMsg);
			TestChatServer.logChatServerDropMsg(formattedMsg, new Date());
			sendLock.writeLock().unlock();
			return;
		}
		
		String timestamp = Long.toString(System.currentTimeMillis()/1000);
		MessageJob msgJob = new MessageJob(dest,msg,sqn,timestamp);
		String formattedMsg = username + " " + dest + " " + timestamp+ " " + sqn; 

		TestChatServer.logUserSendMsg(username, formattedMsg);
		sqn++;
		toSend.add(msgJob);
		sendLock.writeLock().unlock();
	}
	
	public void acceptMsg(Message msg) {
		logRecvMsg(msg);
		TestChatServer.logUserMsgRecvd(username, msg.toString(), new Date());
		msgReceived(msg.getSource()+"\t"+msg.getDest()+"\t"+msg.getSQN()+"\t"+msg.getContent());
	}
	
	@Override
	public void msgReceived(String msg) {
		System.out.println(msg);
	}

	private void logRecvMsg(Message msg) {
		// Add to chatlog
		ChatLog log;
		String reference;
		
		if (msg.isFromGroup())
			reference = msg.getDest();
		else
			reference = msg.getSource();

		if (chatlogs.containsKey(reference))
			log = chatlogs.get(reference);
		else {
			if (msg.isFromGroup())
				log = new ChatLog(msg.getSource(), this, msg.getDest());
			else
				log = new ChatLog(msg.getSource(), this);
			
			chatlogs.put(reference, log);
		}
		
		log.add(msg);
	}
	
	public void logoff(){
		loggedOff = true;
	}
	
	public void run() {
		while(!loggedOff){
			sendLock.writeLock().lock();
			if(!toSend.isEmpty()) {
				MessageJob msgJob = toSend.poll();
				MsgSendError msgStatus = server.processMessage(username, msgJob.dest, msgJob.msg, msgJob.sqn, msgJob.timestamp);
				// Do something with message send error
			}
			sendLock.writeLock().unlock();
		}
		sendLock.writeLock().lock();
		while(!toSend.isEmpty()) {
			MessageJob msgJob = toSend.poll();
			String formattedMsg = username + " " + msgJob.dest + " " + System.currentTimeMillis()/1000 + "\t" + msgJob.sqn;
			TestChatServer.logChatServerDropMsg(formattedMsg, new Date());
		}
		sendLock.writeLock().unlock();
		for(String group : groupsJoined) {
			TestChatServer.logUserLeaveGroup(group, username, new Date());
		}
		groupsJoined.clear();
		TestChatServer.logUserLogout(username, new Date());
	}
}
