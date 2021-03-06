package com.cncom.app.base.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.database.BjnoteContent;
import com.cncom.app.base.database.DBHelper;
import com.lnwoowken.lnwoowkenbook.BillNumberConfirmDialogActivity;
import com.lnwoowken.lnwoowkenbook.MyApplication;
import com.lnwoowken.lnwoowkenbook.R;
import com.lnwoowken.lnwoowkenbook.ServiceObject;
import com.shwy.bestjoy.utils.DebugUtils;
import com.shwy.bestjoy.utils.NetworkUtils;
import com.shwy.bestjoy.utils.ServiceResultObject;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.IUmengUnregisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

public class YouMengMessageHelper {
	private static final String TAG = "YouMengMessageHelper";
	public static YouMengMessageHelper INSTANCE = new YouMengMessageHelper();
	private Context mContext;
	private SharedPreferences mPreferManager;
	private PushAgent mPushAgent;
	private YouMengMessageHelper() {};
	public void setContext(Context context) {
		mContext = context;
		mPreferManager = PreferenceManager.getDefaultSharedPreferences(mContext);
		registerYmengMessageHandler();
		mPushAgent = PushAgent.getInstance(mContext);
	}
	
	public static YouMengMessageHelper getInstance() {
		return INSTANCE;
	}
	/**
	 * 启动推送功能,一旦设备token注册成功，会回调mRegisterCallback
	 */
	public void startPushAgent() {
		mPushAgent.enable(mRegisterCallback);
	}
	
	 public String getDeviceTotke() {
	    	return mPreferManager.getString("device_token", "");
	    }
	    public boolean saveDeviceToken(String deviceToken) {
	    	boolean ok =  mPreferManager.edit().putString("device_token", deviceToken).commit();
	    	DebugUtils.logD(TAG, "saveDeviceToken " + deviceToken + ", saved " + ok);
	    	return ok;
	    }
	    
	    public boolean getDeviceTotkeStatus() {
	    	return mPreferManager.getBoolean("device_token_status", false);
	    }
	    public boolean saveDeviceTokenStatus(boolean deviceTokenHasRegistered) {
	    	boolean ok =  mPreferManager.edit().putBoolean("device_token_status", deviceTokenHasRegistered).commit();
	    	DebugUtils.logD(TAG, "saveDeviceTokenStatus " + deviceTokenHasRegistered + ", saved " + ok);
	    	return ok;
	    }
	
	public void registerYmengMessageHandler() {
		PushAgent.getInstance(mContext).setMessageHandler(new MyYmengMessageHandler());
	}
	
	public UMessage getUMessageFromCursor(Cursor c) {
		try {
			UMessage mesasge = new UMessage(new JSONObject(c.getString(BjnoteContent.YMESSAGE.INDEX_MESSAGE_RAW)));
			return mesasge;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public class MyYmengMessageHandler extends UmengMessageHandler {

		@Override
		public void dealWithCustomMessage(Context arg0, UMessage mesasge) {
			super.dealWithCustomMessage(arg0, mesasge);
//			saveYmengMessageAsync(mesasge);
		}

		@Override
		public void dealWithNotificationMessage(Context arg0, UMessage mesasge) {
			super.dealWithNotificationMessage(arg0, mesasge);
			UMessage umessage = mesasge;
			String msgType = umessage.extra.get("msg_type");
			if (!TextUtils.isEmpty(msgType)) {
				int type = Integer.valueOf(msgType);
				DebugUtils.logD(TAG, "SaveYmengMessageAsyncTask find MsgType = " + type);
				switch(type) {
				case 1:
					//订单金额确认
					String billNumber = umessage.extra.get("orderno");
					if (TextUtils.isEmpty(billNumber)) {
						DebugUtils.logD(TAG, "SaveYmengMessageAsyncTask no find orderno, just ignore the message.");
					}
					BillNumberConfirmDialogActivity.startActivity(mContext, umessage);
					break;
				}
				return;
			}
//			saveYmengMessageAsync(mesasge);
		}
		
	}
	
	private void saveYmengMessageAsync(UMessage message) {
		new SaveYmengMessageAsyncTask().execute(message);
	}
	private class SaveYmengMessageAsyncTask extends AsyncTask<UMessage, Void, Void> {

		@Override
		protected Void doInBackground(UMessage... params) {
			DebugUtils.logD(TAG, "SaveYmengMessageAsyncTask save UMessage = " + params[0]);
			
			if (true) {
				UMessage umessage = params[0];
				String msgType = umessage.extra.get("msg_type");
				if (!TextUtils.isEmpty(msgType)) {
					int type = Integer.valueOf(msgType);
					DebugUtils.logD(TAG, "SaveYmengMessageAsyncTask find MsgType = " + type);
					switch(type) {
					case 1:
						//订单金额确认
						String billNumber = umessage.extra.get("orderno");
						if (TextUtils.isEmpty(billNumber)) {
							DebugUtils.logD(TAG, "SaveYmengMessageAsyncTask no find orderno, just ignore the message.");
							return null;
						}
						BillNumberConfirmDialogActivity.startActivity(mContext, umessage);
						break;
					}
				}
				return null;
			}
			ContentResolver cr = mContext.getContentResolver();
			Cursor c = cr.query(BjnoteContent.YMESSAGE.CONTENT_URI, BjnoteContent.YMESSAGE.PROJECTION, BjnoteContent.YMESSAGE.WHERE_YMESSAGE_ID, new String[]{params[0].msg_id}, null);
			boolean msgExsited = false;
			if (c != null) {
				if (c.moveToNext()) {
					msgExsited = true;
					DebugUtils.logD(TAG, "SaveYmengMessageAsyncTask UMessage has existed with msg_id = " + params[0].msg_id);
				}
				c.close();
			}
			if (!msgExsited) {
				ContentValues values = new ContentValues();
				values.put(DBHelper.YOUMENG_MESSAGE_ID, params[0].msg_id);
				values.put(DBHelper.YOUMENG_TITLE, params[0].title);
				values.put(DBHelper.YOUMENG_TEXT, params[0].text);
				values.put(DBHelper.YOUMENG_MESSAGE_ACTIVITY, params[0].activity);
				values.put(DBHelper.YOUMENG_MESSAGE_URL, params[0].url);
				values.put(DBHelper.YOUMENG_MESSAGE_CUSTOM, params[0].custom);
				values.put(DBHelper.YOUMENG_MESSAGE_RAW, params[0].getRaw().toString());
				values.put(DBHelper.DATE, new Date().getTime());
				Uri uri = cr.insert(BjnoteContent.YMESSAGE.CONTENT_URI, values);
				DebugUtils.logD(TAG, "SaveYmengMessageAsyncTask UMessage need to save with msg_id = " + params[0].msg_id + ", uri = " + uri);
			}
			return null;
		}
		
	}
	
//	private CheckDeviceTokenTask mCheckDeviceTokenTask;
//	public void startCheckDeviceTokenAsync() {
//		if (TextUtils.isEmpty(getDeviceTotke())) {
//			AsyncTaskUtils.cancelTask(mCheckDeviceTokenTask);
//			mCheckDeviceTokenTask = new CheckDeviceTokenTask();
//			mCheckDeviceTokenTask.execute();
//		}
//	}
//	public void cancelCheckDeviceTokenTask() {
//		AsyncTaskUtils.cancelTask(mCheckDeviceTokenTask);
//	}
//	private class CheckDeviceTokenTask extends AsyncTask<Void, Void, String> {
//
//		@Override
//		protected String doInBackground(Void... params) {
//			while (!isCancelled()) {
//				//如果没有取消
//				if (ComConnectivityManager.getInstance().isConnected()) {
//					String device_token = UmengRegistrar.getRegistrationId(mContext);
//					if (!TextUtils.isEmpty(device_token)) {
//						DebugUtils.logD(TAG, "CheckDeviceTokenTask doInBackground() get " + device_token);
//						return device_token;
//					}
//					SystemClock.sleep(5000); //sleep 5 s
//				} else {
//					SystemClock.sleep(60000); //sleep 60 s
//				}
//				
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			super.onPostExecute(result);
//			if (!TextUtils.isEmpty(result)) {
//				//我们保存设备token
//				saveDeviceToken(result);
//			}
//		}
//		
//	}
	
	/**
	 * 将设备Token提交服务器
	 */
	public void postDeviceTokenToServiceLocked() {
		DebugUtils.logD(TAG, "postDeviceTokenToServiceLocked()");
		try {
			if (MyAccountManager.getInstance().hasLoginned() && !TextUtils.isEmpty(getDeviceTotke()) && !getDeviceTotkeStatus()) {
				InputStream is = NetworkUtils.openContectionLocked(ServiceObject.getUpdateDeviceTokenUrl(String.valueOf(MyAccountManager.getInstance().getAccountObject().mAccountUid), getDeviceTotke(), "android"), 
						MyApplication.getInstance().getSecurityKeyValuesObject());
				if (is != null) {
					ServiceResultObject result = ServiceResultObject.parse(NetworkUtils.getContentFromInput(is));
					NetworkUtils.closeInputStream(is);
					
					saveDeviceTokenStatus(result.isOpSuccessfully());
					DebugUtils.logD(TAG, result.toString());
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
public IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback() {
		
		@Override
		public void onRegistered(String registrationId) {
			//设备token已经注册
			//我们保存设备token
			saveDeviceToken(registrationId);
			DebugUtils.logD(TAG, "IUmengRegisterCallback onRegistered() get " + registrationId);
		}
	};
	
	public IUmengUnregisterCallback mUnregisterCallback = new IUmengUnregisterCallback() {
		
		@Override
		public void onUnregistered(String registrationId) {
		}
	};
}
