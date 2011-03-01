package edu.berkeley.cs.cs162;

public class MessageJob {
	String dest,msg,timestamp;
	int sqn;
	
	public MessageJob(String dest, String msg, int sqn, String timestamp){
		this.dest = dest;
		this.msg = msg;
		this.sqn = sqn;
		this.timestamp = timestamp;
	}
}
