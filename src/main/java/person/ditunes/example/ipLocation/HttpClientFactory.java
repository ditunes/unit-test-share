package person.ditunes.example.ipLocation;


import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.FactoryBean;

/**
 * http client实体创建工厂，即创建一个带有线程池且无线程安全的全局client对象。
 * @author linhan
 */
public class HttpClientFactory implements FactoryBean<CloseableHttpClient>{
	
	/**
	 * 连接超时的重试次数
	 */
	private int RETRY_COUNT = 1;
	
	/**
	 * 连接超时时间，httpclient设置值为毫秒
	 */
	private int TIME_OUT_SECONDS = 2*1000;
	
	public CloseableHttpClient createHtpClient(){ 
		LayeredConnectionSocketFactory ssl =  new SSLConnectionSocketFactory(createSSLContentext(),
				SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		Registry<ConnectionSocketFactory> sfr = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("http",
						PlainConnectionSocketFactory.getSocketFactory())
				.register(//支持https协议
						"https",
						ssl != null ? ssl : SSLConnectionSocketFactory
								.getSocketFactory()).build();

		CloseableHttpClient client = HttpClientBuilder
				.create()
				.setDefaultRequestConfig(RequestConfig.custom().setConnectionRequestTimeout(TIME_OUT_SECONDS).setSocketTimeout(TIME_OUT_SECONDS).setConnectTimeout(TIME_OUT_SECONDS).build())
				.setConnectionManager(
						new PoolingHttpClientConnectionManager(sfr)).setRetryHandler(new HttpRequestRetryHandler() {
							
							@Override
							public boolean retryRequest(IOException exception, int executionCount,
									HttpContext context) {
								if(executionCount > RETRY_COUNT){
									return false;
								}else{
									return true;
								}
							}
						})
				.setMaxConnTotal(200)
				.setMaxConnPerRoute(100)
				.build();
		return client;
		
	}
	
	private SSLContext createSSLContentext(){
		try {
			SSLContext sslcontext;
			sslcontext = SSLContext.getInstance(SSLConnectionSocketFactory.TLS); 
			sslcontext.init(null, getTrustsCerts(), new SecureRandom());
			return sslcontext;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private TrustManager[] getTrustsCerts() {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs,
					String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs,
					String authType) {
			}
		} };
		return trustAllCerts;
	}

	@Override
	public CloseableHttpClient getObject() throws Exception {
		return this.createHtpClient();
	}

	@Override
	public Class<?> getObjectType() {
		return CloseableHttpClient.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
