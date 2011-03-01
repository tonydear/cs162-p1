import org.junit.runner.RunWith;
import org.junit.runners.Suite;
@RunWith(Suite.class)
@Suite.SuiteClasses({ChatGroupTest.class, ChatServerTest.class, 
	LoginTest.class, LogoutTest.class, OneToOneMsg.class})
public class AllTests {

}
