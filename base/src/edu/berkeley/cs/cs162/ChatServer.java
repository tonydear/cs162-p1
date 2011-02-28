package edu.berkeley.cs.cs162;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This is the core of the chat server.  Put the management of groups
 * and users in here.  You will need to control all of the threads,
 * and respond to requests from the test harness.
 *
 * It must implement the ChatServerInterface Interface, and you should
 * not modify that interface; it is necessary for testing.
 */

public class ChatServer extends Thread implements ChatServerInterface {

	private Map<String, User> users;
	private Map<String, ChatGroup> groups;
	private Set<String> allNames;
	private ReentrantReadWriteLock lock;
	private boolean isDown;
	private final static int MAX_USERS = 100;
	
	public ChatServer() {
		users = new HashMap<String, User>();
		groups = new HashMap<String, ChatGroup>();
		allNames = new HashSet<String>();
		lock = new ReentrantReadWriteLock(true);
		isDown = false;
	}
	
	@Override
	public LoginError login(String username) {
		lock.writeLock().lock();
		if(isDown){
			TestChatServer.logUserLoginFailed(username, new Date(), LoginError.USER_REJECTED);
			return LoginError.USER_REJECTED;
		}
		if (allNames.contains(username)) {
			lock.writeLock().unlock();
			TestChatServer.logUserLoginFailed(username, new Date(), LoginError.USER_REJECTED);
			return LoginError.USER_REJECTED;
		}
		if (users.size() >= MAX_USERS) {
			lock.writeLock().unlock();
			TestChatServer.logUserLoginFailed(username, new Date(), LoginError.USER_DROPPED);
			return LoginError.USER_DROPPED;
		}
	
		User newUser = new User(this, username);
		users.put(username, newUser);
		allNames.add(username);
		newUser.connected();
		lock.writeLock().unlock();
		TestChatServer.logUserLogin(username, new Date());
		return LoginError.USER_ACCEPTED;
	}

	@Override
	public boolean logoff(String username) {
		// TODO Auto-generated method stub
		lock.writeLock().lock();
		if(!users.containsKey(username)){
			lock.writeLock().unlock();
			return false;
		}
		List <String> userGroups = users.get(username).getUserGroups();
		Iterator<String> it = userGroups.iterator();
		while(it.hasNext()){
			groups.get(it.next()).leaveGroup(username);
		}
		users.get(username).logoff();
		allNames.remove(username);
		users.remove(username);
		lock.writeLock().unlock();	
		TestChatServer.logUserLogout(username, new Date());
		return true;
	}

	@Override
	public boolean joinGroup(BaseUser baseUser, String groupname) {
		// TODO Auto-generated method stub
		lock.writeLock().lock();
		ChatGroup group;
		User user = (User) baseUser;
		boolean success = false;
		if(groups.containsKey(groupname)) {
			group = groups.get(groupname);
			success = group.joinGroup(user.getUsername(), user);
			lock.writeLock().unlock();
			TestChatServer.logUserJoinGroup(groupname, user.getUsername(), new Date());
			return success;
		}
		else {
			if(allNames.contains(groupname))
				return success;
			group = new ChatGroup(groupname);
			groups.put(groupname, group);
			success = group.joinGroup(user.getUsername(), user);
			lock.writeLock().unlock();
			return success;
		}
	}

	@Override
	public boolean leaveGroup(BaseUser baseUser, String groupname) {
		// TODO Auto-generated method stub
		User user = (User) baseUser;
		ChatGroup group = groups.get(groupname);
		if (group == null)
			return false;
		lock.writeLock().lock();
		if(group.leaveGroup(user.getUsername())) {
			if(group.getNumUsers() <= 0) { groups.remove(groupname); }
			lock.writeLock().unlock();
			TestChatServer.logUserLeaveGroup(groupname, user.getUsername(), new Date());
			return true;
		}
		lock.writeLock().unlock();
		return false;
	}

	@Override
	public void shutdown() {
		lock.writeLock().lock();
		Set<String> userNames = users.keySet();
		for(String name: userNames){
			users.get(name).logoff();
		}
		users.clear();
		groups.clear();
		isDown = true;
		lock.writeLock().unlock();
	}

	@Override
	public BaseUser getUser(String username) {
		BaseUser u;
		lock.readLock().lock();
		u = users.get(username);
		lock.readLock().unlock();
		return u;
	}
	
	public ChatGroup getGroup(String groupname) {
		ChatGroup group;
		lock.readLock().lock();
		group = groups.get(groupname);
		lock.readLock().unlock();
		return group;
	}
	
	public Set<String> getGroups() {
		Set<String> groupNames;
		lock.readLock().lock();
		groupNames = this.groups.keySet();
		lock.readLock().unlock();
		return groupNames;
	}
	
	public Set<String> getUsers() {
		Set<String> userNames;
		lock.readLock().lock();
		userNames = users.keySet();
		lock.readLock().unlock();
		return userNames;
	}
	
	public int getNumUsers(){
		int num;
		lock.readLock().lock();
		num = users.size();
		lock.readLock().unlock();
		return num;
	}
	
	public int getNumGroups(){
		int num;
		lock.readLock().lock();
		num = groups.size();
		lock.readLock().unlock();
		return num;
	}
	
	public MsgSendError processMessage(String source, String dest, String msg, int sqn) {	
		Message message = new Message(Long.toString(System.currentTimeMillis()), source, dest, msg);
		message.setSQN(sqn);
		lock.readLock().lock();
		TestChatServer.logUserSendMsg(source, message.toString());
		
		if (users.containsKey(source)) {
			if (users.containsKey(dest)) {
				User destUser = users.get(dest);
				destUser.enqueueMsg(message);
			} else if (groups.containsKey(dest)) {
				message.setIsFromGroup();
				ChatGroup group = groups.get(dest);
				if (!group.forwardMessage(message)) {
					lock.readLock().unlock();
					TestChatServer.logChatServerDropMsg(message.toString(), new Date());
					return MsgSendError.NOT_IN_GROUP;
				}
				
			} else {
				lock.readLock().unlock();
				TestChatServer.logChatServerDropMsg(message.toString(), new Date());
				return MsgSendError.INVALID_DEST;
			}
			
		} else {
			lock.readLock().unlock();
			TestChatServer.logChatServerDropMsg(message.toString(), new Date());
			return MsgSendError.INVALID_SOURCE;
		}
		
		lock.readLock().unlock();
		return MsgSendError.MESSAGE_SENT;
	}
	
	@Override
	public void run(){
		while(!isDown){
		}
	}
}
