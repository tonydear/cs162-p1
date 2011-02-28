package edu.berkeley.cs.cs162;

public class Message {
	private String timestamp;
	private String source;
	private String chatgroup;
	private String dest;
	private String content;
	private int sqn;
	
	public Message(String timestamp, String source, String dest, String content) {
		this.timestamp = timestamp;
		this.source = source;
		this.dest = dest;
		this.content = content;
		sqn = 0;
	}
	
	public Message(String timestamp, String source, String dest, String chatgroup, String content) {
		this.timestamp = timestamp;
		this.source = source;
		this.chatgroup = chatgroup;
		this.dest = dest;
		this.content = content;
		sqn = 0;
	}
	
	public void setSQN(int num){
		sqn = num;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	
	public String getSource() {
		return source;
	}
	
	public String getDest() {
		return dest;
	}
	
	public String getGroup() {
		return chatgroup;
	}
	
	public String getContent() {
		return content;
	}
	
	/**
	 * format: SRC DST TIMESTAMP_UNIXTIME SQN
	 */
	
	public String toString(){
		return source + " " + dest + " " + chatgroup + " " + timestamp + " " + sqn;
	}
	
}
