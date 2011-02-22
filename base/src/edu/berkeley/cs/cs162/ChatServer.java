package edu.berkeley.cs.cs162;

import java.util.HashMap;
import java.util.HashSet;
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
	private final static int MAX_USERS = 100;
	
	public ChatServer() {
		users = new HashMap<String, User>();
		groups = new HashMap<String, ChatGroup>();
		allNames = new HashSet<String>();
		lock = new ReentrantReadWriteLock();
	}
	
	@Override
	public LoginError login(String username) {
		lock.writeLock().lock();
		if (users.size() >= MAX_USERS) {
			lock.writeLock().unlock();
			return LoginError.USER_DROPPED;
		}
		if (allNames.contains(username)) {
			lock.writeLock().unlock();
			return LoginError.USER_REJECTED;
		}
		return null;
	}

	@Override
	public boolean logoff(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean joinGroup(BaseUser user, String groupname) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean leaveGroup(BaseUser user, String groupname) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BaseUser getUser(String username) {
		// TODO Auto-generated method stub
		return null;
	}
}
