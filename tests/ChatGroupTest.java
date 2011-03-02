import java.util.HashMap;
import java.util.Set;

import org.junit.After;
import org.junit.Before;

import edu.berkeley.cs.cs162.*;
import org.junit.*;
import static org.junit.Assert.* ;



public class ChatGroupTest {

	ChatServer server;
	
	@Before
	public void beforeEachTest() {
		System.out.println();
		server = new ChatServer();
		server.start();
		server.login("A");
		server.login("B");
		server.login("C");
	}

	@After
	public void afterEachTest() {
		server.shutdown();
	}
	
	@Test
	public void TestCreateGroup() {
		System.out.println("Running create group test");
		server.joinGroup(server.getUser("A"), "newGroup");
		Set<String> groups = server.getGroups();
		ChatGroup newGroup = server.getGroup("newGroup");
		HashMap<String, User> userlist = newGroup.getUserList();
		assertTrue(groups.contains("newGroup") && userlist.size() == 1);
	}
	
	@Test
	public void TestJoinGroup() {
		System.out.println("Running join group test");
		server.joinGroup(server.getUser("A"), "newGroup");
		server.joinGroup(server.getUser("B"), "newGroup");
		Set<String> groups = server.getGroups();
		ChatGroup newGroup = server.getGroup("newGroup");
		HashMap<String, User> userlist = newGroup.getUserList();
		assertTrue(userlist.keySet().contains("A") && userlist.keySet().contains("B")
				&& userlist.keySet().size() == 2);
	}
	
	@Test
	public void TestFullGroup() {
		System.out.println("Running full group test");
		for (int i=0; i<10; i++) {
			server.login(Integer.toString(i));
			server.joinGroup(server.getUser(Integer.toString(i)), "newGroup");
		}
		Set<String> groups = server.getGroups();
		ChatGroup newGroup = server.getGroup("newGroup");
		HashMap<String, User> userlist = newGroup.getUserList();
		assertTrue(server.joinGroup(server.getUser("A"), "newGroup") == false &&
				userlist.size() == 10);	
	}
	
	@Test
	public void TestLeaveGroupNotLastUser() {
		System.out.println("Running leave group not last user test");
		server.joinGroup(server.getUser("A"), "newGroup");
		server.joinGroup(server.getUser("B"), "newGroup");
		server.leaveGroup(server.getUser("A"), "newGroup");
		Set<String> groups = server.getGroups();
		ChatGroup newGroup = server.getGroup("newGroup");
		HashMap<String, User> userlist = newGroup.getUserList();
		assertTrue(userlist.size()==1);
	}
	
	@Test
	public void TestLeaveGroupLastUser() {
		System.out.println("Running leave group last user test");
		server.joinGroup(server.getUser("A"), "newGroup");
		server.leaveGroup(server.getUser("A"), "newGroup");
		Set<String> groups = server.getGroups();
		assertFalse(groups.contains("newGroup"));
	}
	
	@Test
	public void TestLeaveNonexistantGroup() {
		assertFalse(server.leaveGroup(server.getUser("A"), "doesn't Exist"));
	}
	
	@Test
	public void TestSendMessageNotInGroup() {
		server.joinGroup(server.getUser("A"), "newGroup");
		server.leaveGroup(server.getUser("A"), "newGroup");
		assert(server.processMessage("A", "newGroup", "test", 0, "0") == MsgSendError.NOT_IN_GROUP);
	}
	
	@Test
	public void TestSendMessageToGroup() throws InterruptedException {
		server.joinGroup(server.getUser("A"), "newGroup");
		server.joinGroup(server.getUser("B"), "newGroup");
		server.joinGroup(server.getUser("C"), "newGroup");
		User a = (User) server.getUser("A");
		a.send("newGroup", "Hello B and C");
		Thread.sleep(4000);
		User b = (User) server.getUser("B");
		User c = (User) server.getUser("C");
		Message messageB = b.getLog("newGroup").getLog().get(0);
		Message messageC = c.getLog("newGroup").getLog().get(0);
		assertTrue(messageB.getContent() == "Hello B and C" 
			&& messageC.getContent() == "Hello B and C");
			
	}
	
	@Test
	public void TestJoinGroupAfterLogoff() {
		System.out.println("Running test join group after logoff");
		User a = (User) server.getUser("A");
		server.logoff("A");
		System.out.println(a);
		assertFalse(server.joinGroup(a, "newGroup"));
		
	}
}
