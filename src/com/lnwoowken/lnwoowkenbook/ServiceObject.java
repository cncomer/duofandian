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
	public static String getLoginOrUpdateUrl(String para, String jsonString) {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("mobile/common/login.ashx?")
		.append(para).append("=").appendUrlEncodedString(jsonString);
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
	
	public static String getYanzhengCodeUrl(String para, String jsonString) {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("Mobile/common/GetRandCode.ashx?")
		.append(para).append("=").appendUrlEncodedString(jsonString);
		return sb.toString();
	}
	
	//add by chenkai, 20140726, 将发送短信抽离出来，以便修改 end
	
	public static String getAllShopInfoUrl() {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("Mobile/common/getAllShopInfo.ashx?");
		return sb.toString();
	}
	
	public static String getAllShopInfoUrl(String para, String jsonString) {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("Mobile/common/getAllShopInfo.ashx?")
		.append(para).append("=").appendUrlEncodedString(jsonString);
		return sb.toString();
	}
	
	public static String getAllBillInfoUrl(String para, String jsonString) {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("Mobile/common/GetOrderDetailByUID.ashx?")
		.append(para).append("=").appendUrlEncodedString(jsonString);
		return sb.toString();
	}
	
	public static String getAllAvailableTableUrl(String para, String jsonString) {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("Mobile/common/showdesk.ashx?")
		.append(para).append("=").appendUrlEncodedString(jsonString);
		return sb.toString();
	}
	
	public static String getInOrderingUrl(String para, String jsonString) {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("Mobile/common/InOrdering.ashx?")
		.append(para).append("=").appendUrlEncodedString(jsonString);
		return sb.toString();
	}
	
	public static String getVoidOrderUrl(String para, String jsonString) {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("mobile/common/VoidOrder.ashx?")
		.append(para).append("=").appendUrlEncodedString(jsonString);
		return sb.toString();
	}
	
	public static String getPinpaiUrl() {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("Mobile/common/getBrand.ashx");
		return sb.toString();
	}
	
	public static String getShangquanUrl() {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("Mobile/common/GetShangQuanCaiXi.ashx");
		return sb.toString();
	}
	
	public static String getCaixiUrl() {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("Mobile/common/GetShangQuanCaiXi.ashx");
		return sb.toString();
	}
	
	public static String getXingzhengquUrl() {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("Mobile/common/GetRegion.ashx");
		return sb.toString();
	}
	
	public static String getShopByPinpaiUrl(String para, String jsonString) {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("Mobile/common/GetShopByBrand.ashx?")
		.append(para).append("=").appendUrlEncodedString(jsonString);
		return sb.toString();
	}
	
	public static String getShangquanUrl(String para, String jsonString) {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("Mobile/common/GetPagedShopDetailByShangquan.ashx?")
		.append(para).append("=").appendUrlEncodedString(jsonString);
		return sb.toString();
	}
	
	public static String getLiushuiNumberUrl(String para, String jsonString) {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("Mobile/common/GenOrder.ashx?")
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
	
	/**
	 * 得到餐厅照片,例如http://manage.lnwoowken.com/shopimg/{showid}_01.jpg
	 * @param photoID 指定的店铺ID_type
	 * @return
	 */
	public static String getShopImage(String photoID) {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("shopimg/").append(photoID).append(".jpg");
		return sb.toString();
	}
	/**
	 * 如http://manage.lnwoowken.com/comphoto/2014-09-23/e8b9a06af2513f748d191f9feda7bf8b.jpg
	 * @param photoID comphoto/2014-09-23/e8b9a06af2513f748d191f9feda7bf8b.jpg
	 * @return
	 */
	public static String getIndexPageImage(String photoID) {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append(photoID);
		return sb.toString();
	}
	/**
	 * 返回指定店铺的提示信息，如抢位告知、订单确认、支付提示等信息，形如http://manage.lnwoowken.com/mobile/common/gettip.ashx?para={shopid:12}
	 * @param shopShowId
	 * @return
	 */
	public static String getShopTipsUrl(String shopShowId) {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("mobile/common/gettip.ashx?para=").appendUrlEncodedString("{shopid:").append(shopShowId).appendUrlEncodedString("}");
		return sb.toString();
	}
	
	public static String getUpdateUserNameUrl(String para, String jsonString) {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("mobile/common/UpdateUserName.ashx?")
		.append(para).append("=").appendUrlEncodedString(jsonString);
		return sb.toString();
	}
	
	public static String getUpdateUserLoginPasswordUrl(String para, String jsonString) {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("mobile/common/UpdateUserPwd.ashx?")
		.append(para).append("=").appendUrlEncodedString(jsonString);
		return sb.toString();
	}
	/**更新头像，JSONObject对象需要有uid和img*/
	public static String getUpdateUserAvatorUrl(String para, String jsonString) {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("mobile/common/changeimgmethod.ashx?")
		.append(para).append("=").appendUrlEncodedString(jsonString);
		return sb.toString();
	}
	
	public static String getRegisterUrl(String para, String jsonString) {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("mobile/common/register.ashx?")
		.append(para).append("=").appendUrlEncodedString(jsonString);
		return sb.toString();
	}
	
	public static String getIndexPageAdsUrl() {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("Mobile/common/GetCompanyImg.ashx?");
		return sb.toString();
	}
	/**
	 * 获取满意度调查问卷
	 * http://manage.lnwoowken.com/Mobile/common/GetQuestion.ashx?shopid=36
	 * @return
	 */
	public static String getSurveyQuestionsUrl(String shopId) {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("Mobile/common/GetQuestion.ashx?shopid=").append(shopId);
		return sb.toString();
	}
	
	public static String getCommitSurveyQuestionsUrl(String para, String jsonString) {
		UrlEncodeStringBuilder sb = new UrlEncodeStringBuilder(ServiceObject.SERVICE_URL);
		sb.append("Mobile/common/submitQuestion.ashx?")
		.append(para).append("=").appendUrlEncodedString(jsonString);
		return sb.toString();
	}
	
	public static String buildPageQuery(String url, int pageIndex, int pageSize) {
		  StringBuilder sb = new StringBuilder(url).append('&');
		  sb.append("pageindex=").append(pageIndex).append('&');
		  sb.append("pagesize=").append(pageSize);
  	  return sb.toString();
	  }
	
	public static class ServiceResultObject {
		public int mStatusCode = 0;
		public String mStatusMessage;
		public JSONObject mJsonData;
		public String mStrData;
		public JSONArray mJsonArrayData;
		
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

		public static ServiceResultObject parseJsonArray(String content) {
			ServiceResultObject resultObject = new ServiceResultObject();
			if (TextUtils.isEmpty(content)) {
				return resultObject;
			}
			try {
				JSONObject jsonObject = new JSONObject(content);
				resultObject.mStatusCode = Integer.parseInt(jsonObject.getString("StatusCode"));
				resultObject.mJsonArrayData = jsonObject.getJSONArray("Data");
				resultObject.mStatusMessage = jsonObject.getString("StatusMessage");
				DebugUtils.logD("ServiceResultObject", "pinpai = " + resultObject.mJsonArrayData);
				DebugUtils.logD("ServiceResultObject", "StatusCode = " + resultObject.mStatusCode);
				DebugUtils.logD("ServiceResultObject", "StatusMessage = " +resultObject.mStatusMessage);
			} catch (JSONException e) {
				e.printStackTrace();
				resultObject.mStatusMessage = e.getMessage();
			}
			return resultObject;
		}

		public static ServiceResultObject parseShangquanInfo(String content) {
			ServiceResultObject resultObject = new ServiceResultObject();
			if (TextUtils.isEmpty(content)) {
				return resultObject;
			}
			try {
				JSONObject jsonObject = new JSONObject(content);
				resultObject.mStatusCode = Integer.parseInt(jsonObject.getString("StatusCode"));
				JSONObject dataObject = jsonObject.getJSONObject("Data");
				resultObject.mJsonArrayData = dataObject.getJSONArray("shangquan");
				resultObject.mStatusMessage = jsonObject.getString("StatusMessage");
				DebugUtils.logD("ServiceResultObject", "pinpai = " + resultObject.mJsonArrayData);
				DebugUtils.logD("ServiceResultObject", "StatusCode = " + resultObject.mStatusCode);
				DebugUtils.logD("ServiceResultObject", "StatusMessage = " +resultObject.mStatusMessage);
			} catch (JSONException e) {
				e.printStackTrace();
				resultObject.mStatusMessage = e.getMessage();
			}
			return resultObject;
		}

		public static ServiceResultObject parseCaixiInfo(String content) {
			ServiceResultObject resultObject = new ServiceResultObject();
			if (TextUtils.isEmpty(content)) {
				return resultObject;
			}
			try {
				JSONObject jsonObject = new JSONObject(content);
				resultObject.mStatusCode = Integer.parseInt(jsonObject.getString("StatusCode"));
				JSONObject dataObject = jsonObject.getJSONObject("Data");
				resultObject.mJsonArrayData = dataObject.getJSONArray("caixi");
				resultObject.mStatusMessage = jsonObject.getString("StatusMessage");
				DebugUtils.logD("ServiceResultObject", "pinpai = " + resultObject.mJsonArrayData);
				DebugUtils.logD("ServiceResultObject", "StatusCode = " + resultObject.mStatusCode);
				DebugUtils.logD("ServiceResultObject", "StatusMessage = " +resultObject.mStatusMessage);
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
				resultObject.mJsonArrayData = dataObject.getJSONArray("data");
				resultObject.mStatusMessage = jsonObject.getString("StatusMessage");
				DebugUtils.logD("ServiceResultObject", "mAddresses = " + resultObject.mJsonArrayData);
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
