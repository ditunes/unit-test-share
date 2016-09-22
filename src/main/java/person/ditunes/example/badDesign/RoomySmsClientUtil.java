/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
/*    */package person.ditunes.example.badDesign;

/*    */
/*    */

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
/*    */
import java.util.Map;

/*    */
/*    */public abstract class RoomySmsClientUtil
/*    */{
	/*    */
	/*    */public static Map<String, String> sendSms(String phone,
			String content, String prov, String deadline, String sendDate,
			String account, String addSerial)
	/*    */{
		/* 34 */Map tokenMap = RoomySmsHttpClientUtils.getToken();
		/* 35 */String token = (String) tokenMap.get("token");
		/* 36 */Map param = Maps.newHashMap();
		/* 37 */param.put("token", token);
		/* 38 */param.put("tels", phone);
		/* 39 */param.put("content", content);
		/* 40 */if (null != prov) {
			/* 41 */param.put("prov", prov);
			/*    */}
		/* 43 */if (null != deadline) {
			/* 44 */param.put("dl", deadline);
			/*    */}
		/* 46 */if (null != sendDate) {
			/* 47 */param.put("st", sendDate);
			/*    */}
		/* 49 */param.put("account", account);
		/* 50 */if (null != addSerial) {
			/* 51 */param.put("addSerial", addSerial);
			/*    */}
		/* 53 */String resutl = RoomySmsHttpClientUtils.post(
				"/service/call/smsSender", param);
		/* 54 */return ((Map) JSON.parseObject(resutl,Map.class));
		/*    */}


}