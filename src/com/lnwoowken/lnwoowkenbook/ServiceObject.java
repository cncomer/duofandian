package com.lnwoowken.lnwoowkenbook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.shwy.bestjoy.utils.DebugUtils;
import com.shwy.bestjoy.utils.UrlEncodeStringBuilder;


public class ServiceObject {
	private static final String TAG = "ServiceObject";
	public static final String SERVICE_URL = "http://manage.lnwoowken.com/";
	/**
	 * 返回登陆调用URL
	 * @param tel
	 * @param pwd
	 * @return
	 */
	public static String getLoginOrUpdateUrl(String tel, String pwd) {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		//sb.append("mobile/common/login.ashx?cell=").append(tel)
		//.append("&pwd=").appendUrlEncodedString(pwd);
		sb.append("mobile/common/login.ashx?para={\"cell\":\"").append(tel)
		.append("\",\"pwd\":\"").appendUrlEncodedString(pwd)
		.append("\"}");
		return sb.toString();
	}
	
	//add by chenkai, 20140726, 将发送短信抽离出来，以便修改 begin
	/**
	 * 返回登陆调用URL
	 * @param para
	 * @param jsonString
	 * @return
	 */
	public static String getFindPasswordUrl(String para, String jsonString) {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("Mobile/common/getPwd.ashx?")
		.append(para).append("=").appendUrlEncodedString(jsonString);
		return sb.toString();
	}
	
	//add by chenkai, 20140726, 将发送短信抽离出来，以便修改 end
	
	public static String getAllShopInfoUrl(String para, String jsonString) {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("Mobile/common/getAllShopInfo.ashx?")
		.append(para).append("=").appendUrlEncodedString(jsonString);
		return sb.toString();
	}
	
	public static String getAllAvailableTableUrl(String para, String jsonString) {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("Mobile/common/showdesk.ashx?")
		.append(para).append("=").appendUrlEncodedString(jsonString);
		return sb.toString();
	}
	/**
	 * 获取所有店铺id, 如返回：[{"ID":"1"},{"ID":"2"},{"ID":"3"},{"ID":"4"},{"ID":"5"}]
	 * @return
	 */
	public static String getAllShopIdsUrl() {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("mobile/GetShopID.ashx?");
		return sb.toString();
	}
	
	/**
	 * 得到店铺的详细信息
	 * @param id 指定的店铺ID
	 * @return
	 */
	public static String getShopDetailsUrl(long id) {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("mobile/GetShopDetailByID.ashx?shopid=").append(id);
		return sb.toString();
	}
	
	/**
	 * 得到店铺当天可以预订的桌数, 如{"number":"0"}，（注：桌子的数据需要维护）
	 * @param id 指定的店铺ID
	 * @return
	 */
	public static String getShopOrderCounts(long id) {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("mobile/GetAvailableDeskCount.ashx?shopid=").append(id);
		return sb.toString();
	}
	
	public static class ServiceResultObject {
		public int mStatusCode = 0;
		public String mStatusMessage;
		public JSONObject mJsonData;
		public String mStrData;
		public JSONArray mShops;
		
		public static ServiceResultObject parse(String content) {
			ServiceResultObject resultObject = new ServiceResultObject();
			if (TextUtils.isEmpty(content)) {
				return resultObject;
			}
			try {
				JSONObject jsonObject = new JSONObject(content);
				resultObject.mStatusCode = Integer.parseInt(jsonObject.getString("StatusCode"));
				resultObject.mStatusMessage = jsonObject.getString("StatusMessage");
				DebugUtils.logD("HaierResultObject", "StatusCode = " + resultObject.mStatusCode);
				DebugUtils.logD("HaierResultObject", "StatusMessage = " +resultObject.mStatusMessage);
				try {
					resultObject.mJsonData = jsonObject.getJSONObject("Data");
				} catch (JSONException e) {
					resultObject.mStrData = jsonObject.getString("Data");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				resultObject.mStatusMessage = e.getMessage();
			}
			return resultObject;
		}

		public static ServiceResultObject parseShops(String content) {
			ServiceResultObject resultObject = new ServiceResultObject();
			if (TextUtils.isEmpty(content)) {
				return resultObject;
			}
			try {
				JSONObject jsonObject = new JSONObject(content);
				resultObject.mStatusCode = Integer.parseInt(jsonObject.getString("StatusCode"));

				JSONObject dataObject = jsonObject.getJSONObject("Data");
				resultObject.mShops = dataObject.getJSONArray("data");
				resultObject.mStatusMessage = jsonObject.getString("StatusMessage");
				DebugUtils.logD("ServiceResultObject", "mAddresses = " + resultObject.mShops);
				DebugUtils.logD("ServiceResultObject", "StatusCode = " + resultObject.mStatusCode);
				DebugUtils.logD("ServiceResultObject", "StatusMessage = " +resultObject.mStatusMessage);
			} catch (JSONException e) {
				e.printStackTrace();
				resultObject.mStatusMessage = e.getMessage();
			}
			return resultObject;
		}

		public static ServiceResultObject parseAvailableTables(String content) {
			ServiceResultObject resultObject = new ServiceResultObject();
			if (TextUtils.isEmpty(content)) {
				return resultObject;
			}
			try {
				JSONObject jsonObject = new JSONObject(content);
				resultObject.mStatusCode = Integer.parseInt(jsonObject.getString("StatusCode"));

				resultObject.mShops = jsonObject.getJSONArray("Data");
				resultObject.mStatusMessage = jsonObject.getString("StatusMessage");
				DebugUtils.logD("ServiceResultObject", "mShops = " + resultObject.mShops);
				DebugUtils.logD("ServiceResultObject", "StatusCode = " + resultObject.mStatusCode);
				DebugUtils.logD("ServiceResultObject", "StatusMessage = " +resultObject.mStatusMessage);
			} catch (JSONException e) {
				e.printStackTrace();
				resultObject.mStatusMessage = e.getMessage();
			}
			return resultObject;
		}
		
		public boolean isOpSuccessfully() {
			return mStatusCode == 1;
		}
	}
}
