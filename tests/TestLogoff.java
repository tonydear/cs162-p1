

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.berkeley.cs.cs162.BaseUser;
import edu.berkeley.cs.cs162.ChatServer;
import edu.berkeley.cs.cs162.ChatServerInterface;
import edu.berkeley.cs.cs162.MessageDeliveryTask;
import edu.berkeley.cs.cs162.User;

public class TestLogoff {
	public static void main(String [] args) throws Exception {
		ChatServerInterface s = new ChatServer();
		ExecutorService exe = Executors.newFixedThreadPool(10);
		int i;
		
		s.login("A");
		s.login("B");
		User a = (User)s.getUser("A");
		User b = (User)s.getUser("B");
		s.joinGroup((BaseUser)a, "Group1");
		s.joinGroup((BaseUser)b, "Group1");
		
		for(i = 0; i < 10; i++) {
			MessageDeliveryTask t = new MessageDeliveryTask(s, "A", "Group1", "ab "+ i);
			MessageDeliveryTask c = new MessageDeliveryTask(s, "B", "Group1", "ba "+ i);
			exe.execute(t);
			exe.execute(c);
		}
		Thread.sleep(22);
		s.logoff("A");
		for(i = 10; i < 20; i++) {
			MessageDeliveryTask t = new MessageDeliveryTask(s, "B", "Group1", "ba "+ i);
			exe.execute(t);
		}
		exe.shutdown();
		
		Thread.sleep(10000);
		File afile = new File("aFile.txt");
		File bfile = new File("bFile.txt");
		
		FileOutputStream fop=new FileOutputStream(afile);
		fop.write(a.getLog("Group1").toString().getBytes());
		fop.flush();
        fop.close();
        
        fop=new FileOutputStream(bfile);
		fop.write(b.getLog("Group1").toString().getBytes());
		fop.flush();
        fop.close();
		
		s.shutdown();
		System.out.println("done \n");
	}

}
