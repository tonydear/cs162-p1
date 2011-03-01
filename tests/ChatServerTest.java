import edu.berkeley.cs.cs162.*;
import org.junit.*;
import static org.junit.Assert.* ;



public class ChatServerTest {
	
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
	public void processMessageInvalidSourceTest() {
		System.out.println("Running process message invalid source test");
		server.login("B");
		assertTrue(server.processMessage("A", "B", "this message should be invalid",0, null) == MsgSendError.INVALID_SOURCE);
		
	}
	
	@Test
	public void processMessageInvalidDestTest() {
		System.out.println("Running process message invalid destination test");
		server.login("A");
		assertTrue(server.processMessage("A", "B", "this message should be invalid",0,null) == MsgSendError.INVALID_DEST);
		
	}
	
	@Test
	public void processMessageValidTest() {
		System.out.println("Running valid processMessage test");
		server.login("A");
		server.login("B");
		assertTrue(server.processMessage("A", "B", "Hello B",0,null) == MsgSendError.MESSAGE_SENT);
	}
}
