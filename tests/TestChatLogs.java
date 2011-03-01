

//Testing for consistent chatlogs among individual users.

import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.berkeley.cs.cs162.ChatServer;
import edu.berkeley.cs.cs162.ChatServerInterface;
import edu.berkeley.cs.cs162.MessageDeliveryTask;
import edu.berkeley.cs.cs162.User;
public class TestChatLogs {
	
	public static void main(String [] args) throws Exception {
		ChatServerInterface s = new ChatServer();
		ExecutorService exe = Executors.newFixedThreadPool(10);
		int i;
		
		s.login("A");
		s.login("B");
		User a = (User)s.getUser("A");
		User b = (User)s.getUser("B");
		
		for(i = 0; i < 10; i++) {
			MessageDeliveryTask t = new MessageDeliveryTask(s, "A", "B", "ab "+ i);
			MessageDeliveryTask c = new MessageDeliveryTask(s, "B", "A", "ba "+ i);
			exe.execute(t);
			exe.execute(c);
		}
		
		exe.shutdown();
		
		Thread.sleep(5000);	
		File afile = new File("aFile.txt");
		File bfile = new File("bFile.txt");
		
		FileOutputStream fop=new FileOutputStream(afile);
		fop.write(a.getLog("B").toString().getBytes());
		fop.flush();
        fop.close();
        
        fop=new FileOutputStream(bfile);
		fop.write(b.getLog("A").toString().getBytes());
		fop.flush();
        fop.close();
		
		s.shutdown();
		System.out.println("done \n");
	}
	
}