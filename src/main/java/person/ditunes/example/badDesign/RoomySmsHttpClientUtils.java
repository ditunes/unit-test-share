/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
/*     */package person.ditunes.example.badDesign;

/*     */
/*     *///import cn.roomy.framework.utils.SpringUtils;
/*     */
import com.google.common.collect.Lists;
/*     */
import java.io.FileInputStream;
import java.io.InputStream;
/*     */
import java.util.List;
/*     */
import java.util.Map;
/*     */
import java.util.Properties;
/*     */
import org.apache.http.HttpEntity;
/*     */
import org.apache.http.HttpResponse;
/*     */
import org.apache.http.client.entity.UrlEncodedFormEntity;
/*     */
import org.apache.http.client.methods.HttpGet;
/*     */
import org.apache.http.client.methods.HttpPost;
/*     */
import org.apache.http.impl.client.CloseableHttpClient;
/*     */
import org.apache.http.impl.client.HttpClients;
/*     */
import org.apache.http.message.BasicNameValuePair;
/*     */
import org.apache.http.util.EntityUtils;
/*     */
import org.slf4j.Logger;
/*     */
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

/*     */
/*     */public abstract class RoomySmsHttpClientUtils
/*     */{
	/*  38 */private static Logger logger = LoggerFactory
			.getLogger(RoomySmsHttpClientUtils.class);
	/*     */private static String appId;
	/*     */private static String sercet;
	/*     */private static String serverUrl;
	/*     */
	/*     */public static String post(String url, Map<String, String> params)
	/*     */{
				return null;
		/*     */}

	/*     */
	/*     */public static Map<String, String> getToken()
	/*     */{
				return null;
		    }

	/*     */
	/*     */static
	/*     */{
		/*     */try
		/*     */{
			/*  52 */SmsClient client = null;
			/*  53 */appId = client.getAppId();
			/*  54 */sercet = client.getAppSercet();
			/*  55 */serverUrl = client.getUapServer();
			/*     */} catch (Exception e) {
			/*     */try {
				/*  59 */InputStream is = new FileInputStream( ResourceUtils.getFile("classpath:roomy-sms-client.properties"));
				/*  60 */Properties properties = new Properties();
				/*  61 */properties.load(is);
				/*  62 */appId = properties
						.getProperty("roomy.uap.client.appId");
				/*  63 */sercet = properties
						.getProperty("roomy.uap.client.sercet");
				/*  64 */serverUrl = properties
						.getProperty("roomy.uap.server.url");
				/*     */} catch (Exception e1) {
				/*  66 */logger
						.info("�޷���roomy-sms-client.properties��ȡ���ã�����application.properties�ļ���ȡ��");
				/*     */try {
					/*  68 */InputStream is = new FileInputStream( ResourceUtils.getFile("classpath:application.properties"));
					/*  69 */Properties properties = new Properties();
					/*  70 */properties.load(is);
					/*  71 */appId = properties
							.getProperty("roomy.uap.client.appId");
					/*  72 */sercet = properties
							.getProperty("roomy.uap.client.sercet");
					/*  73 */serverUrl = properties
							.getProperty("roomy.uap.server.url");
					/*     */} catch (Exception e2) {
					/*  75 */logger.info("�޷���ȡ�ͻ��������ļ�������ϵͳ�����ж�ȡ��");
					/*  76 */appId = System
							.getProperty("roomy.uap.client.appId");
					/*  77 */sercet = System
							.getProperty("roomy.uap.client.sercet");
					/*  78 */serverUrl = System
							.getProperty("roomy.uap.server.url");
					/*     */}
				/*     */}
			/*     */}
		/*     */}
	/*     */
}