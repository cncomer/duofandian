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
	 * @param desString DES加密后的字串
	 * @return
	 */
	public static String getFindPasswordUrl(String para, String DESString) {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("20140726/SendMessage.ashx?")
		.append(para).append("=").appendUrlEncodedString(DESString);
		return sb.toString();
	}
	//add by chenkai, 20140726, 将发送短信抽离出来，以便修改 begin
	
	public static class ServiceResultObject {
		public int mStatusCode = 0;
		public String mStatusMessage;
		public JSONObject mJsonData;
		public String mStrData;
		public JSONArray mAddresses;
		
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

		public static ServiceResultObject parseAddress(String content) {
			ServiceResultObject resultObject = new ServiceResultObject();
			if (TextUtils.isEmpty(content)) {
				return resultObject;
			}
			try {
				JSONObject jsonObject = new JSONObject(content);
				resultObject.mAddresses = jsonObject.getJSONArray("results");
				resultObject.mStatusCode = Integer.parseInt(jsonObject.getString("status"));
				resultObject.mStatusMessage = jsonObject.getString("message");
				DebugUtils.logD("HaierResultObject", "mAddresses = " + resultObject.mAddresses);
				DebugUtils.logD("HaierResultObject", "StatusCode = " + resultObject.mStatusCode);
				DebugUtils.logD("HaierResultObject", "StatusMessage = " +resultObject.mStatusMessage);
				try {
					resultObject.mJsonData = jsonObject.getJSONObject("results");
				} catch (JSONException e) {
					resultObject.mStrData = jsonObject.getString("results");
				}
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
