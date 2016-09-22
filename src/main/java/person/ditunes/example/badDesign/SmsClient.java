package person.ditunes.example.badDesign;

/**
 * Created by linhan on 16/9/20.
 */
public class SmsClient {
    private String uapServer;
    private String appSercet;
    private String appId;


    public String getAppId() {
        return appId;
    }

    public String getAppSercet() {
        return appSercet;
    }

    public String getUapServer() {
        return uapServer;
    }
}
