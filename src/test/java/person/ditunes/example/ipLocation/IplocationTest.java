package person.ditunes.example.ipLocation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class IplocationTest {

	@Autowired
	private IpLocationService service;

	@Autowired
	private IpLocationService.BaiduIpLocationServer baiduServer;

	@Autowired
	private IpLocationService.SinaIpLocationServer sinaServer;

	private Logger logger = LoggerFactory.getLogger(IplocationTest.class);
	
	@Test
	public void testGetLocationInfoByIp(){
		String ip = "8.8.8.8";
		String ip2 = "120.24.61.210";
		IpLocationService.LocationInfo info = service.getLocationInfoByIp(ip);
		IpLocationService.LocationInfo info2 = service.getLocationInfoByIp(ip2);
		System.out.println(info);
		System.out.println(info2);
	}



	@Test
	public void testGetLocationInfoByIp2(){
		String ip = "8.8.8.8";
		IpLocationService.LocationInfo info = service.getLocationInfoByIp(ip);
		Assert.assertEquals("加利福尼亚", info.getCity());
		Assert.assertEquals("美国",info.getCountry());
		Assert.assertEquals("加利福尼亚州", info.getProvince());
	}
	
	@Test
	public void testGetLocationInfoByIp3(){
		try {
			String ip = "8.8.8.8";
			IpLocationService.LocationInfo info = service.getLocationInfoByIp(ip);
			if(info.getProvince().equals("unknown")){
				Assert.assertEquals("unknown", info.getCity());
				Assert.assertEquals("unknonw", info.getCountry());
				Assert.assertEquals("unknow", info.getProvince());
			}else if(info.getCountry().equals("unknown")){
				Assert.assertEquals("美国",info.getCountry());
				Assert.assertEquals("加利福尼亚", info.getCity());
				Assert.assertEquals("加利福尼亚州", info.getProvince());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	@Test
	public void testOpenLocationServer() throws Exception {
		String ip = "8.8.8.8";
		IpLocationService.LocationInfo info= baiduServer.getLocationInfoByIp(ip);
		IpLocationService.LocationInfo sinaInfo= sinaServer.getLocationInfoByIp(ip);
		Assert.assertEquals("unknow",info.getCountry());
		Assert.assertEquals("unknow", info.getCity());
		Assert.assertEquals("unknow", info.getProvince());
		Assert.assertEquals("美国",info.getCountry());
		Assert.assertEquals("加利福尼亚", info.getCity());
		Assert.assertEquals("加利福尼亚州", info.getProvince());
	}
	
}
