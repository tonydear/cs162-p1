package edu.berkeley.cs.cs162;

import java.io.IOException;
import java.util.Set;

public class TestMessageOrder {
	static ChatServer server;
	public static void main(String[] args){
		server = new ChatServer();
		server.start();
		basicChatGroupTest();
		System.out.println("u called shutdown");
		server.shutdown();
		System.out.println("shutted down");
	}
	
	/**USE: press enter after receiving message printing stop
	 * checks whether size of chatlogs are right (everyone received all messages?)
	 * if yes should print size matches: int
	 * checks whether chatlogs match with other members in group, printing true if yes
	 * FOR: only testing 20 people, each with one group, 5 in a group, all in group and no one leaves
	**/
	public static void basicChatGroupTest(){
		int[] groupMessageCounts = new int[4]; //counting messages sent in group
		String[] groupLogs = new String[4];
		for(int i = 0; i < 20; i++){ //logs users in and have each user i join group i/5
			String newUser = "user" + i;
			server.login(newUser);
			BaseUser u = server.getUser(newUser);
			server.joinGroup(u, "group" + i/5);
		}
		for(int n = 0; n < 10; n++){ //each user may send to group up to 1k times
			for(int i = 0; i < 20; i++){
				if(Math.random() > 0.5){
					server.getUser("user"+i).send("group" + i/5, "lala");
					groupMessageCounts[i/5]++;
				}
			}
		}
		
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//checks if all messages received for all users
		//then checks if users in same group have same chatlog
		for(int n = 0; n < 20; n+=5){
			for(int i = 0; i < 5; i++){
				int group = (i+n)/5;
				User u = (User)server.getUser("user" + (i+n));
				ChatLog log = u.getLog("group" + group);
				System.out.println(u.getUsername() + ": "); 
				testCount(log,groupMessageCounts[group]);
				if(groupLogs[group] == null){
					groupLogs[group] = log.toString();
					System.out.println(true);
				}else{
					System.out.println(groupLogs[group].equals(log.toString()));
				}
					
			}
		}
	}
	
	public static void testCount(ChatLog log, int count){
		int size = log.getMessages().size();
		if(size == count)
			System.out.println("size matches: " + count);
		else
			System.out.println("size doesn't match: " + count + size);
	}

}
