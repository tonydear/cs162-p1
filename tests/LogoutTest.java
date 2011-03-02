import edu.berkeley.cs.cs162.*;
import junit.framework.TestCase;
import org.junit.*;

import static org.junit.Assert.* ;

public class LogoutTest {
	
	ChatServer server;
	
	@Before
	public void beforeEachTest() {
		System.out.println();
		server = new ChatServer();
		server.start();
	}
	
	@After
	public void afterEachTest() {
		server.shutdown();
	}
	
	@Test
	public void testAcceptedUserLogoff() {
		server.login("A");
		System.out.println("Running accepted user logoff test");
		assertTrue(server.logoff("A") == true);
	}
	
	@Test
	public void testRejectedUserLogoff() {
		System.out.println("Running rejected user logoff test");
		assertTrue(server.logoff("A") == false);
	}
	
	@Test
	public void testLogoffWhileInGroup() {
		System.out.println("Running logoff while in group test");
		server.login("A");
		server.login("B");
		BaseUser a = server.getUser("A");
		BaseUser b = server.getUser("B");
		
		//create 4 chat groups; 2 will only have a, 2 will have both
		server.joinGroup(a, "1");
		server.joinGroup(b, "3");
		server.joinGroup(b, "4");
		server.joinGroup(a, "3");
		server.joinGroup(a, "4");
		
		//make sure correct number of users in group
		assertTrue(server.getGroup("1").getNumUsers() == 1);
		assertTrue(server.getGroup("3").getNumUsers() == 2);
		assertTrue(server.getGroup("4").getNumUsers() == 2);
		
		server.logoff("A");
		
		assertTrue(server.getGroup("3").getNumUsers() == 1);
		assertTrue(server.getGroup("4").getNumUsers() == 1);
		assertTrue(server.getGroup("1") == null);
		
	}
	
}