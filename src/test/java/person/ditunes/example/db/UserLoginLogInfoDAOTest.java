package person.ditunes.example.db;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

import org.assertj.db.api.Assertions;
import org.assertj.db.type.Changes;
import org.assertj.db.type.Table;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;


public class UserLoginLogInfoDAOTest {

	private UserLoginLogInfoDAO userLoginLogInfoDAO;
	
	private EmbeddedDatabase db;

	private Long EXIST_LOGIN_LOG_USER_ID = 1L;
	
	private Long NOT_EXIST_LOGIN_LOG_USER_ID = 2L;
	
	private String EXIST_LOGIN_LOCATION = "深圳市广东省中国";
	
	private String UN_USED_IP = "127.0.0.1";
	
	@Before
	public void setup() throws IOException {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		db = builder.setType(EmbeddedDatabaseType.H2)
				.addScript(getScriptPath("cas-user-login-log-schema.sql"))
				.addScript(getScriptPath("cas-user-login-log-data.sql")).build();
		userLoginLogInfoDAO = new UserLoginLogInfoDAO(new JdbcTemplate(db));
	}

	@Test
	public void when_query_exist_data_user_info_can_get_right_val() throws IOException {
		Assert.assertTrue(userLoginLogInfoDAO.existLoginInfo(EXIST_LOGIN_LOG_USER_ID));
		Assert.assertFalse(userLoginLogInfoDAO.existLoginInfo(NOT_EXIST_LOGIN_LOG_USER_ID));
	}

	@Test
	public void testInsertUserLoginInfo(){

		String userInfoTableName = "cas_user_login_log";
		Table table = new Table(db, userInfoTableName);

		Changes changes = new Changes(db).setStartPointNow();
		userLoginLogInfoDAO.insertUserLoginLogInfo(new UserLoginLogInfo(2L,"厦门","192.168.1.1",new Date()));
		changes.setEndPointNow();

		Assertions.assertThat(changes)
				.hasNumberOfChanges(1)
				.change()
				.hasPksNames("id").hasPksValues(7)
				.rowAtEndPoint()
				.value("id").isEqualTo(7)
				.value("ip").isEqualTo("192.168.1.1")
				.value("location").isEqualTo("厦门");
	}


	
	@After
	public void tearDown(){
		db.shutdown();
	}

	public String getScriptPath(String scriptName) {
		try {
			return new ClassPathResource(scriptName).getURI()
					.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
