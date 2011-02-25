package edu.berkeley.cs.cs162;

import java.util.Random;

public class LogInAndOutTask implements Runnable {
	ChatServer server;
	
	LogInAndOutTask(ChatServer s) {
		server = s;
	}
	
	@Override
	public void run() {
		//Can probably have some better output format here, like dumping userlist to file to compare state
		Random rand = new Random();
		while (true) {
			String userin = "random" + String.valueOf(rand.nextInt(500));
			if (server.login(userin) == LoginError.USER_ACCEPTED)
				System.out.println(userin + "logged in! Total users = " + server.getUsers().size());
			else
				System.out.println(userin + "rejected! Total users = " + server.getUsers().size());
			String userout = "random" + String.valueOf(rand.nextInt(500));
			if (server.logoff(userout))
				System.out.println(userout + "logged off!");
		}
	}

}
