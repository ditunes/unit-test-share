package person.ditunes.example.ipLocation;

/**
 * Created by linhan on 16/9/20.
 */
public class IpDataServer {

    private final String serverName;

    private final String serverAddress;

    private final String accessKey;


    public IpDataServer(String serverName, String serverAddress, String accessKey) {
        this.serverName = serverName;
        this.serverAddress = serverAddress;
        this.accessKey = accessKey;
    }

    public String getServerName() {
        return serverName;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public String getAccessKey() {
        return accessKey;
    }
}
