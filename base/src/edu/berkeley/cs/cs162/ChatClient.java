package edu.berkeley.cs.cs162;

import java.io.InputStream;
import java.net.Socket;
import java.util.Map;


public class ChatClient {
	private Socket mySocket;
	private Map<String,ChatLog> logs;
	private InputStream commands;
	private InputStream received;
	private Thread receiver;
	private boolean connected;
	private Message reply; //what should reply from server look like
	private volatile boolean isWaiting; //waiting for reply from server?
	
	
	public ChatClient(){
		receiver = new Thread(){
            @Override
            public void run(){
            	while(true){
            		receive();
            	}
            }
        };
        receiver.start();
	}
	
	private boolean connect(String hostname, int port){
		return false;
	}
	
	private void disconnect(){
		
	}
	
	private boolean login(String username){
		return false;
	}
	
	private void logout(){
		
	}
	
	private boolean join(String gname){
		return false;
	}
	
	private boolean leave(String gname){
		return false;
	}
	
	private void send(String dest, int sqn, String msg){
		
	}
	
	private void receive(){
		
	}
	
	private void sleep(int time){
		
	}
	
	public Map<String,ChatLog> getLogs(){
		return null;
	}
	
	public synchronized void processCommands(){
		
	}
	
	private synchronized void signalReceive(){
		
	}
	
	public void main(String[] args){
		ChatClient client = new ChatClient();
		while(true){
			client.processCommands();
		}
	}
	
}
