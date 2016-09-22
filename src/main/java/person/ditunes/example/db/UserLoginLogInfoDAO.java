package person.ditunes.example.db;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;


public class UserLoginLogInfoDAO {

	private static String TABLE_NAME = "cas_user_login_log";
	
	private static String INSERT_LOG_INFO = "insert into "+TABLE_NAME+"(userId,ip,location,loginTime) values(?,?,?,?)";
	
	private static String QUERY_TAGET_NUMS_INFO_BY_USER_AND_LOCATION = "select 1 from "+TABLE_NAME+" where userId = ? and location=? limit ?";
	
	private static String EXIST_LOGIN_INFO = "select 1 from "+TABLE_NAME+" where userId = ? limit 1 ";
	
	private JdbcTemplate jdbcTemplate;

	public UserLoginLogInfoDAO(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public UserLoginLogInfoDAO() {
		super();
	}



	public boolean existLoginInfo(Long userId){
		try {
			Integer flag = jdbcTemplate.queryForObject(EXIST_LOGIN_INFO, new Object[]{userId}, Integer.class);
			return flag == 1;
		} catch (EmptyResultDataAccessException e) {
			return false;
		}
	}
	
	public boolean existTargetNumUserLoginInfoByLocation(UserLoginLogInfo info, int num){
		List<Integer> list = jdbcTemplate.queryForList(QUERY_TAGET_NUMS_INFO_BY_USER_AND_LOCATION, new Object[]{info.getUserId(), info.getLocation(),num}, Integer.class);
		return list.size() >= num;
	}
	
	@Transactional
	public void insertUserLoginLogInfo(UserLoginLogInfo loginInfo){
		jdbcTemplate.update(INSERT_LOG_INFO, new Object[]{loginInfo.getUserId(),loginInfo.getIp(), 
				loginInfo.getLocation(), loginInfo.getLoginTime()});
	}

}
