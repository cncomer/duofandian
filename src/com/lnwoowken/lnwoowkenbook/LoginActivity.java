package com.lnwoowken.lnwoowkenbook;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.ui.BaseActionbarActivity;
import com.cncom.app.base.util.DebugUtils;
import com.cncom.app.base.util.DialogUtils;
import com.lnwoowken.lnwoowkenbook.ServiceObject.ServiceResultObject;
import com.shwy.bestjoy.utils.AsyncTaskUtils;
import com.shwy.bestjoy.utils.ComConnectivityManager;
import com.shwy.bestjoy.utils.NetworkUtils;

public class LoginActivity extends BaseActionbarActivity implements OnClickListener {
	private static final String TAG = "LoginActivity";
	private EditText editText_uid;
	private EditText editText_pwd;
	private static final int REQUEST_LOGIN = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initialize();
	}
	private void initialize(){
		findViewById(R.id.button_login).setOnClickListener(this);
		findViewById(R.id.button_regist).setOnClickListener(this);
		findViewById(R.id.button_getpwd).setOnClickListener(this);
		editText_uid = (EditText) findViewById(R.id.EditText_uid);
		//显示上一次输入的用户号码
		editText_uid.setText(MyAccountManager.getInstance().getLastUsrTel());
		editText_pwd = (EditText) findViewById(R.id.EditText_pwd);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.button_regist:
			RegistActivity.startActivity(mContext);
			break;
		case R.id.button_login:
			if (!ComConnectivityManager.getInstance().isConnected()) {
				//没有联网，这里提示用户
				ComConnectivityManager.getInstance().onCreateNoNetworkDialog(mContext).show();
				return;
			}
			String user = editText_uid.getText().toString();
			String pwd = editText_pwd.getText().toString();
			if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(pwd)) {
				MyAccountManager.getInstance().saveLastUsrTel(user);
				startActivityForResult(LoginOrUpdateAccountDialog.createLoginOrUpdate(this, true, user, pwd), REQUEST_LOGIN);
			} else {
				MyApplication.getInstance().showShortMessage(R.string.attention_empty_name_or_password);
			}
			break;
		case R.id.button_getpwd:
			//如果电话号码为空，提示用户先输入号码，在找回密码
			if (TextUtils.isEmpty(editText_uid.getText().toString())) {
				MyApplication.getInstance().showMessage(R.string.msg_input_tel_when_find_password);
			} else {
				if (!ComConnectivityManager.getInstance().isConnected()) {
					ComConnectivityManager.getInstance().onCreateNoNetworkDialog(this).show();
				} else {
					findPasswordAsync();
				}
			}
			break;
		}
	}
	private FidnPasswordTask mFidnPasswordTask;
	private void findPasswordAsync() {
		AsyncTaskUtils.cancelTask(mFidnPasswordTask);
		showDialog(DIALOG_PROGRESS);
		mFidnPasswordTask = new FidnPasswordTask();
		mFidnPasswordTask.execute();
	}
	private class FidnPasswordTask extends AsyncTask<Void, Void, ServiceResultObject> {

		@Override
		protected ServiceResultObject doInBackground(Void... params) {
			ServiceResultObject result = new ServiceResultObject();
			
			InputStream is = null;
			try {
				JSONObject queryJsonObject = new JSONObject();
				queryJsonObject.put("cell", editText_uid.getText().toString().trim());
				DebugUtils.logD(TAG, "FidnPasswordTask doInBackground() queryJsonObject = " + queryJsonObject.toString());
				is = NetworkUtils.openContectionLocked(ServiceObject.getFindPasswordUrl("para", queryJsonObject.toString()), null);
			    if (is != null) {
			    	result = ServiceResultObject.parse(NetworkUtils.getContentFromInput(is));
			    }
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				result.mStatusMessage = e.getMessage();
			} catch (IOException e) {
				e.printStackTrace();
				result.mStatusMessage = MyApplication.getInstance().getGernalNetworkError();
			} catch (JSONException e) {
				e.printStackTrace();
				result.mStatusMessage = e.getMessage();
			} finally {
				NetworkUtils.closeInputStream(is);
			}
			return result;
		}
		@Override
		protected void onPostExecute(ServiceResultObject result) {
			super.onPostExecute(result);
			dismissDialog(DIALOG_PROGRESS);
			if (result.isOpSuccessfully()) {
				MyApplication.getInstance().showMessage(result.mStatusMessage);
			} else {
				DialogUtils.createSimpleConfirmAlertDialog(mContext, result.mStatusMessage, null);
			}
			
		}
		@Override
		protected void onCancelled() {
			super.onCancelled();
			dismissDialog(DIALOG_PROGRESS);
		}
		
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_LOGIN) {
			if (resultCode == Activity.RESULT_OK) {
				// login successfully
				MainActivity.startIntentClearTop(mContext, null);
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	
//	private LoginTask mLoginTask;
//	private void loginAsync(String name, String pwd) {
//		showDialog(DIALOG_PROGRESS);
//		AsyncTaskUtils.cancelTask(mLoginTask);
//		mLoginTask = new LoginTask();
//		mLoginTask.execute(new String[]{name, pwd});
//	} 
//	
//	private class LoginTask extends AsyncTask<String, Void, ServiceResultObject> {
//
//		@Override
//		protected ServiceResultObject doInBackground(String... params) {
//			InputStream is = null;
//			ServiceResultObject result = new ServiceResultObject();
//			try {
//				JSONObject queryJsonObject = new JSONObject();
//				queryJsonObject.put("cell", params[0]);
//				queryJsonObject.put("pwd", params[1]);
//				DebugUtils.logD(TAG, "FidnPasswordTask doInBackground() queryJsonObject = " + queryJsonObject.toString());
//				is = NetworkUtils.openContectionLocked(ServiceObject.getLoginOrUpdateUrl("para", queryJsonObject.toString()), MyApplication.getInstance().getSecurityKeyValuesObject());
//				if (is != null) {
//					result = ServiceResultObject.parse(NetworkUtils.getContentFromInput(is));
//					if (result.mJsonData != null) {
//						
//					}
//			    	AccountObject _accountObject = AccountParser.parseJson(stringResult);
//					if (_accountObject != null && _accountObject.isLogined()) {
//						result.mStatusCode = 1;
//						result.mStatusMessage = mContext.getString(R.string.login_success);
//						boolean saveAccountOk = MyAccountManager.getInstance().saveAccountObject(LoginActivity.this.getContentResolver(), _accountObject);
//						if (!saveAccountOk) {
//							//登录成功了，但本地数据保存失败，通常不会走到这里
//							result.mStatusMessage = LoginActivity.this.getString(R.string.msg_login_save_success);
//						}
//					} else {
//						result.mStatusMessage = mContext.getString(R.string.login_error);
//					}
//				 }
//				
//			} catch (JSONException e) {
//				e.printStackTrace();
//				result.mStatusMessage = e.getMessage();
//			} catch (ClientProtocolException e) {
//				e.printStackTrace();
//				result.mStatusMessage = MyApplication.getInstance().getGernalNetworkError();
//			} catch (IOException e) {
//				e.printStackTrace();
//				result.mStatusMessage = MyApplication.getInstance().getGernalNetworkError();
//			}
//			
//			return result;
//		}
//
//		@Override
//		protected void onPostExecute(ServiceResultObject result) {
//			super.onPostExecute(result);
//			dismissDialog(DIALOG_PROGRESS);
//			if (result.isOpSuccessfully()) {
//				Intent in = new Intent();
//				in.setAction("login");  
//	            sendBroadcast(in);  
//				LoginActivity.this.finish();
//			}
//			MyApplication.getInstance().showMessage(result.mStatusMessage);
//			
//		}
//		@Override
//		protected void onCancelled() {
//			super.onCancelled();
//			dismissDialog(DIALOG_PROGRESS);
//		}
//		
//	}
	
	public static void startActivity(Context context) {
		Intent intent = new Intent(context, LoginActivity.class);
		context.startActivity(intent);
	}
	@Override
	protected boolean checkIntent(Intent intent) {
		return true;
	}
}
