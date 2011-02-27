package edu.berkeley.cs.cs162;

import java.util.Random;

public class JoinAndLeaveGroupTask implements Runnable {
	ChatServer server;
	
	JoinAndLeaveGroupTask(ChatServer s) {
		server = s;
	}
	
	@Override
	public void run() {
		//Can probably have some better output format here, like dumping grouplist and group members to file to compare state
		Random rand = new Random();
		while (true) {
			String username = "random_user " + rand.nextInt(100);
			server.login(username);
			User user = (User) server.getUser(username);
			
			String groupname = "random_group" + String.valueOf(rand.nextInt(50));
			if (server.joinGroup(user, groupname)) {
				int numUsers = server.getGroup(groupname).getNumUsers();
				if (numUsers == 1)
					System.out.println(user + " created group " + groupname + "!");
				else
					System.out.println(user + " joined group " + groupname + "! " + "Group size is now " + numUsers); 
			}
			else
				System.out.println(user + "rejected from group " + groupname + "!");

			//not done, still need to add leave group tests
		}
	}
}
