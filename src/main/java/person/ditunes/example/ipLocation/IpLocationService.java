package person.ditunes.example.ipLocation;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class IpLocationService {

    private static Logger logger = LoggerFactory.getLogger(IpLocationService.class);

    private List<? extends OpenIpLocationServer> openIpLocationServers;

    public IpLocationService(List<? extends OpenIpLocationServer> openIpLocationServers) {
        if (openIpLocationServers == null || openIpLocationServers.isEmpty()) {
            throw new RuntimeException("无有效的IP查询服务器，则无法提供ip查询服务");
        }
        this.openIpLocationServers = openIpLocationServers;
    }

    public LocationInfo getLocationInfoByIp(String ip) {
        if (openIpLocationServers.size() == 1) {
            return doIpLocationQueryFromOnlyOneServer(ip);
        } else {
            return doIpLocationQueryFromMultServer(ip);
        }
    }

    private LocationInfo doIpLocationQueryFromMultServer(String ip) {
        for (int serverIndexOrderByRank = 0; serverIndexOrderByRank < openIpLocationServers.size(); serverIndexOrderByRank++) {
            LocationInfo info;
            OpenIpLocationServer server = openIpLocationServers.get(serverIndexOrderByRank);
            try {
                info = openIpLocationServers.get(serverIndexOrderByRank).getLocationInfoByIp(ip);
                return info;
            } catch (Exception e) {
                logger.error("IP:[{}]-server:[{}]调用发生异常，切换至备用Ip库处理", ip, server.getServerName(), e);
                continue;
            }
        }
        return LocationInfo.UNKNOWN_LOCATION;
    }

    private LocationInfo doIpLocationQueryFromOnlyOneServer(String ip) {
        OpenIpLocationServer server = openIpLocationServers.get(0);
        try {
            return openIpLocationServers.get(0).getLocationInfoByIp(ip);
        } catch (Exception e) {
            logger.error("IP:[{}]-server:[{}]-message:[{}]", ip, server.getServerName(), "该服务无法获取当前IP位置", e);
            return LocationInfo.UNKNOWN_LOCATION;
        }
    }


    public static abstract class OpenIpLocationServer {

        private CloseableHttpClient httpClient;

        private IpDataServer ipDataServer;

        public OpenIpLocationServer(CloseableHttpClient httpClient,
                                    IpDataServer ipDataServer) {
            super();
            this.httpClient = httpClient;
            this.ipDataServer = ipDataServer;
        }

        public LocationInfo getLocationInfoByIp(String ip) throws Exception {
            HttpUriRequest request = constructRequestToServer(ip);
            HttpEntity entity = null;
            try {
                CloseableHttpResponse response = httpClient
                        .execute(request);
                entity = response.getEntity();

                return retrieveIpInfoFromHttpEntity(entity);
            } finally {
                EntityUtils.consumeQuietly(entity);
            }
        }

        protected String getServerName() {
            return ipDataServer.getServerName();
        }

        protected IpDataServer getServer() {
            return this.ipDataServer;
        }

        protected abstract LocationInfo retrieveIpInfoFromHttpEntity(HttpEntity entity) throws Exception;

        protected abstract HttpUriRequest constructRequestToServer(String reqIp);
    }


    public static class SinaIpLocationServer extends OpenIpLocationServer {

        public SinaIpLocationServer(CloseableHttpClient httpClient,
                                    IpDataServer ipDataServer) {
            super(httpClient, ipDataServer);
        }

        @Override
        protected LocationInfo retrieveIpInfoFromHttpEntity(HttpEntity entity)
                throws Exception {
            String result = EntityUtils.toString(entity);
            JSONObject content;
            try {
                content = JSONObject.parseObject(result);
            } catch (Exception e) {
                throw new RuntimeException("新浪ip库api接口无法处理该请求:一般为ip格式不准确或为特殊ip");
            }
            if ("中国".equals(content.getString("country"))) {
                return retrieveChinaIpLocationInfo(content);
            }
            return new LocationInfo(content.getString("country"),
                    content.getString("province"), content.getString("city"));
        }

        private LocationInfo retrieveChinaIpLocationInfo(JSONObject content) {
            return new LocationInfo(content.getString("country"),
                    content.getString("province") + "省", content.getString("city") + "市");
        }

        @Override
        protected HttpUriRequest constructRequestToServer(String reqIp) {
            return RequestBuilder.get()
                    .addParameter("format", "json")
                    .addParameter("ip", reqIp)
                    .setUri(getServer().getServerAddress())
                    .build();
        }

    }

    public static class BaiduIpLocationServer extends OpenIpLocationServer {

        public BaiduIpLocationServer(CloseableHttpClient httpClient,
                                     IpDataServer ipDataServer) {
            super(httpClient, ipDataServer);
        }

        @Override
        protected LocationInfo retrieveIpInfoFromHttpEntity(HttpEntity entity) throws Exception {
            String result = EntityUtils.toString(entity);
            JSONObject json = JSONObject.parseObject(result);
            String status = json.getString("status");
            if (!status.equals("0")) {
                throw new RuntimeException("百度ip库api接口无法处理该请求:错误码："
                        + "[" + status + "] 原因:[" + json.getString("message") + "]");
            }
            JSONObject content = json.getJSONObject("content").getJSONObject("address_detail");
            String city = content.getString("city");
            String province = content.getString("province");
            return new LocationInfo("中国", province, city);
        }

        @Override
        protected HttpUriRequest constructRequestToServer(String reqIp) {
            return RequestBuilder.get()
                    .addParameter("ip", reqIp)
                    .addParameter("ak", getServer().getAccessKey())
                    .setUri(getServer().getServerAddress()).build();
        }

    }


    public static class IpLocationInfo implements Serializable {

        private static final long serialVersionUID = 1449257058128254596L;

        private final String ip;

        private final LocationInfo locationInfo;

        public IpLocationInfo(String ip, LocationInfo locationInfo) {
            super();
            this.ip = ip;
            this.locationInfo = locationInfo;
        }

        public String getIp() {
            return ip;
        }

        public LocationInfo getLocationInfo() {
            return locationInfo;
        }

    }

    public static class LocationInfo implements Serializable {

        private static final long serialVersionUID = 4812005180943280998L;

        private static final String UNKNOWN_VAL = "UNKNOWN";

        private final String province;

        private final String city;

        private final String country;

        public static final LocationInfo UNKNOWN_LOCATION = new LocationInfo("UNKNOWN", "UNKNOWN", "UNKNOWN");

        public static final LocationInfo LOCAL_NET_WORK_LOCATION = new LocationInfo("LOCAL", "LOCAL", "LOCAL");

        public LocationInfo(String country, String province, String city) {
            super();
            this.province = StringUtils.isBlank(province) ? UNKNOWN_VAL : province;
            this.city = StringUtils.isBlank(city) ? UNKNOWN_VAL : city;
            this.country = StringUtils.isBlank(country) ? UNKNOWN_VAL : country;
        }

        public String getProvince() {
            return province;
        }

        public String getCity() {
            return city;
        }

        public String getCountry() {
            return country;
        }

        public boolean isUnknownLocation() {
            return (this == UNKNOWN_LOCATION) || this.equals(UNKNOWN_LOCATION);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((city == null) ? 0 : city.hashCode());
            result = prime * result
                    + ((country == null) ? 0 : country.hashCode());
            result = prime * result
                    + ((province == null) ? 0 : province.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            LocationInfo other = (LocationInfo) obj;
            if (city == null) {
                if (other.city != null)
                    return false;
            } else if (!city.equals(other.city))
                return false;
            if (country == null) {
                if (other.country != null)
                    return false;
            } else if (!country.equals(other.country))
                return false;
            if (province == null) {
                if (other.province != null)
                    return false;
            } else if (!province.equals(other.province))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "LocationInfo [province=" + province + ", city=" + city
                    + ", country=" + country + "]";
        }

    }

}
