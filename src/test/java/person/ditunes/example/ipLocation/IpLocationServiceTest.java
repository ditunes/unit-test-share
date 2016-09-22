package person.ditunes.example.ipLocation;

import com.google.common.collect.Lists;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import person.ditunes.example.ipLocation.IpLocationService;
import person.ditunes.example.ipLocation.IpLocationService.*;

/**
 * Created by linhan on 16/9/20.
 */
public class IpLocationServiceTest {

    public static final String UNKNOWN = "UNKNOWN";
    private IpLocationService ipLocationService;

    private IpLocationService.BaiduIpLocationServer baiduServer;

    private IpLocationService.SinaIpLocationServer sinaServer;

    @Before
    public void setup() {
        baiduServer = Mockito.mock(IpLocationService.BaiduIpLocationServer.class);
        sinaServer = Mockito.mock(IpLocationService.SinaIpLocationServer.class);
    }

    @Test
    public void baidu_has_exception_and_sina_works_well_then_get_sina_result() throws Exception {
        //given
        String ipThatBaiduCannotProcess = "120.24.61.222";
        IpLocationService.LocationInfo expectedSinaResult = new IpLocationService.LocationInfo("中国", "福建省", "厦门市");
        new ServerBuilder()
                .theServer(baiduServer).inUnAvaliableStatus()
                .and()
                .theServer(sinaServer).existIpAndLocationInfo(ipThatBaiduCannotProcess, expectedSinaResult);
        ipLocationService = new IpLocationService(Lists.newArrayList(baiduServer, sinaServer));
        //when
        LocationInfo actual = ipLocationService.getLocationInfoByIp(ipThatBaiduCannotProcess);
        //then
        Assertions.assertThat(actual).as("should get expected location from only one avaliable server").isEqualTo(expectedSinaResult);
    }

    @Test
    public void baidu_and_sina_all_have_exception_then_get_unknown_location() throws Exception {
        //given
        String anyIp = "120.24.65.87";
        LocationInfo expectedUnKnownLocation = new LocationInfo(UNKNOWN, UNKNOWN, UNKNOWN);
        new ServerBuilder()
                .theServer(baiduServer).inUnAvaliableStatus()
                .and()
                .theServer(sinaServer).inUnAvaliableStatus();
        ipLocationService = new IpLocationService(Lists.newArrayList(baiduServer, sinaServer));
        //when
        LocationInfo actual = ipLocationService.getLocationInfoByIp(anyIp);
        //then
        Assertions.assertThat(actual)
                .as("none server can work should return unknown location info")
                .isEqualTo(expectedUnKnownLocation);
    }

//    @Test
    public void all_servers_work_well_then_get_the_first_server_result() throws Exception {
        //Todo
    }

    @Test(expected = RuntimeException.class)
    public void no_server_exist_should_throw_exception() {
        ipLocationService = new IpLocationService(Lists.<OpenIpLocationServer>newArrayList());
    }

    public class ServerBuilder {
        public OpenIpLocationServer server;

        public ServerBuilder theServer(OpenIpLocationServer server) {
            this.server = server;
            return this;
        }

        public ServerBuilder inUnAvaliableStatus() throws Exception {
            OpenIpLocationServer server = this.server;
            Mockito.doThrow(new RuntimeException("error")).when(server).getLocationInfoByIp(Mockito.anyString());
            return this;
        }

        public ServerBuilder existIpAndLocationInfo(String ip, LocationInfo location) throws Exception {
            Mockito.doReturn(location).when(this.server).getLocationInfoByIp(Mockito.eq(ip));
            return this;
        }

        public ServerBuilder and() {
            return new ServerBuilder();
        }
    }
}
