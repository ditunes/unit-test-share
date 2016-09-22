package person.ditunes.example.db;

import java.util.Date;

/**
 * Created by linhan on 16/9/21.
 */
public class UserLoginLogInfo {
    private Long userId;
    private String location;
    private String ip;
    private Date loginTime;

    public UserLoginLogInfo(Long userId, String location, String ip, Date loginTime) {
        this.userId = userId;
        this.location = location;
        this.ip = ip;
        this.loginTime = loginTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
