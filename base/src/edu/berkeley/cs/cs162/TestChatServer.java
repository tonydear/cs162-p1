package edu.berkeley.cs.cs162;

/**
 * This is an example of how you should test your chat server.  Create
 * users and groups, and have them login, logout, and send messages in
 * different orders.  By using a thread pool, we are able to increase
 * the level of concurrency to better stress your synchronization.
 *
 */

import java.io.IOException;
import java.util.Date;
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
	
	/**
	 * Logs the events of a user logging into the ChatServer.  This should only be called AFTER
	 * the user has been accepted by the ChatServer.
	 *
	 * @param username user logging the event.
	 * @param time time of the event.
	 */
	public static void logUserLogin(String username, Date time){
		
	}

	/**
	 * Logs the events of a user logging into the ChatServer.  This should only be called AFTER
	 * the user has been accepted by the ChatServer.
	 *
	 * @param username user logging the event.
	 * @param time time of the event.
	 */
	public static void logUserLoginFailed(String username, Date time, LoginError e){
		
	}
	
	/**
	 * Logs the logout event.  This should only be called AFTER the user has been released by the
	 * ChatServer successfully.
	 * 
	 * @param username user logging the event
	 * @param time time of the event
	 */
	public static void logUserLogout(String username, Date time){
		
	}
	
	/**
	 * This should be called when the user attempts to send a message to the chat server 
	 * (after the call is made).
	 *
	 * @param msg the string representation of the message. 
	 * 			SRC DST TIMESTAMP_UNIXTIME SQN
	 * 		example: alice bob 1298489721 23
	 */
	public static void logUserSendMsg(String msg){
		
	}
	
	/**
	 * If, for any reason, the ChatServer determines that the message cannot be delivered.  This
	 * message should be called to log that event.
	 * 
	 * @param msg string representation of the message
	 * 			SRC DST TIMESTAMP_UNIXTIME SQN
	 * 		example: alice bob 1298489721 23
	 * @param time time when the event occurred.
	 */
	public static void logChatServerDropMsg(String msg, Date time){
		
	}
	
	/**
	 * When the user receives a message, this method should be called.
	 * 
	 * @param msg string representation of the message.
	 * 			SRC DST TIMESTAMP_UNIXTIME SQN
	 * 		example: alice bob 1298489721 23
	 * @param time time when the event occurred.
	 */
	public static void logUserMsgRecvd(String msg, Date time){
		
	}
}
