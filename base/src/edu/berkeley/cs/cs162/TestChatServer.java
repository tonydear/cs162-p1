package edu.berkeley.cs.cs162;

/**
 * This is an example of how you should test your chat server.  Create
 * users and groups, and have them login, logout, and send messages in
 * different orders.  By using a thread pool, we are able to increase
 * the level of concurrency to better stress your synchronization.
 *
 */

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class TestChatServer {
	
	public static void main(String [] args) throws InterruptedException {
		ChatServerInterface s = new ChatServer();
		ExecutorService exe = Executors.newFixedThreadPool(10);
		int i;
		
		
		s.login("steve");
		s.login("mike");
		s.login("jay");
		BaseUser bu = s.getUser("mike");
		BaseUser jay = s.getUser("jay");
		s.joinGroup(bu, "group1");
		s.joinGroup(jay, "group1");
		for (i = 0; i < 50; i++) {
			MessageDeliveryTask t = new MessageDeliveryTask(s, "steve", "mike", "hi "+ i);
			MessageDeliveryTask c = new MessageDeliveryTask(s, "steve", "group1", "hig "+ i);
			exe.execute(t);
			exe.execute(c);
		}
		
		exe.shutdown();
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s.shutdown();
		System.out.println("done \n");
	}
}
