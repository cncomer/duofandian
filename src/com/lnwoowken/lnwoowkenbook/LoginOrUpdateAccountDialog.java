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
import android.widget.TextView;

import com.cncom.app.base.account.AccountObject;
import com.cncom.app.base.account.AccountParser;
import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.util.DebugUtils;
import com.lnwoowken.lnwoowkenbook.ServiceObject.ServiceResultObject;
import com.shwy.bestjoy.utils.AsyncTaskUtils;
import com.shwy.bestjoy.utils.Intents;
import com.shwy.bestjoy.utils.NetworkUtils;
/**
 * 这个类用来更新和登录账户使用。
 * @author chenkai
 *
 */
public class LoginOrUpdateAccountDialog extends Activity{

	private static final String TAG = "LoginOrUpdateAccountDialog";
	private String mTel, mPwd;
	private boolean mIsLogin = false;
	private TextView mStatusView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_or_update_layout);
		mStatusView = (TextView) findViewById(R.id.title);
		Intent intent = getIntent();
		mIsLogin = intent.getBooleanExtra(Intents.EXTRA_TYPE, true);
		mTel = intent.getStringExtra(Intents.EXTRA_TEL);
		mPwd = intent.getStringExtra(Intents.EXTRA_PASSWORD);
		loginAsync();
	}
	
	private void showStatusMessage(final int resId) {
		MyApplication.getInstance().postAsync(new Runnable() {
			@Override
			public void run() {
				mStatusView.setText(resId);
			}
		});
	}

	private LoginAsyncTask mLoginAsyncTask;
	private void loginAsync() {
		mStatusView.setText(mIsLogin?R.string.msg_login_dialog_title_wait:R.string.msg_update_dialog_title_wait);
		AsyncTaskUtils.cancelTask(mLoginAsyncTask);
		mLoginAsyncTask = new LoginAsyncTask();
		mLoginAsyncTask.execute();
	}
	private class LoginAsyncTask extends AsyncTask<Void, Void, ServiceResultObject> {

		@Override
		protected ServiceResultObject doInBackground(Void... params) {
			InputStream _is = null;
			ServiceResultObject result = new ServiceResultObject();
			try {
				JSONObject queryJsonObject = new JSONObject();
				queryJsonObject.put("cell", mTel);
				queryJsonObject.put("pwd", mPwd);
				DebugUtils.logD(TAG, "FidnPasswordTask doInBackground() queryJsonObject = " + queryJsonObject.toString());
				_is = NetworkUtils.openContectionLocked(ServiceObject.getLoginOrUpdateUrl("para", queryJsonObject.toString()), MyApplication.getInstance().getSecurityKeyValuesObject());
				if (_is != null) {
					result = ServiceResultObject.parse(NetworkUtils.getContentFromInput(_is));
					if (result.isOpSuccessfully()) {
						//登录成功，我们解析账户信息
						showStatusMessage(R.string.msg_login_download_accountinfo_wait);
						if (result.mJsonData != null) {
							AccountObject _accountObject = AccountParser.parseAccountData(result.mJsonData);
							//登录成功后返回的数据中没有包含密码，所以这里我们认为添加登录时候用的密码
							_accountObject.mAccountPwd = mPwd;
							boolean saveAccountOk = MyAccountManager.getInstance().saveAccountObject(getContentResolver(), _accountObject);
							if (!saveAccountOk) {
								//登录成功了，但本地数据保存失败，通常不会走到这里
								result.mStatusMessage = getString(R.string.msg_login_save_success);
							}
						}
				    	
					}
					
				 } else {
					 result.mStatusMessage = MyApplication.getInstance().getGernalNetworkError();
				 }
			} catch (JSONException e) {
				e.printStackTrace();
				result.mStatusMessage = e.getMessage();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				result.mStatusMessage = MyApplication.getInstance().getGernalNetworkError();
			} catch (IOException e) {
				e.printStackTrace();
				result.mStatusMessage = MyApplication.getInstance().getGernalNetworkError();
			} finally {
				NetworkUtils.closeInputStream(_is);
			}
			
			return result;
		}
		

		@Override
		protected void onPostExecute(ServiceResultObject result) {
			super.onPostExecute(result);
			if (isCancelled()) {
				//通常不走到这里
				onCancelled();
				return;
			}
			MyApplication.getInstance().showMessage(result.mStatusMessage);
			if (result.isOpSuccessfully()) {
				//如果登陆成功
				setResult(Activity.RESULT_OK);
			} else {
				setResult(Activity.RESULT_CANCELED);
			}
			finish();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			MyApplication.getInstance().showMessage(R.string.msg_op_canceled);
			setResult(Activity.RESULT_CANCELED);
			finish();
			
		}
		
		public void cancelTask(boolean cancel) {
			super.cancel(cancel);
			
		}
		
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (mLoginAsyncTask != null) {
			mLoginAsyncTask.cancelTask(true);
			DebugUtils.logD(TAG, "login or update is canceled by user");
		}
	}
	
	public static Intent createLoginOrUpdate(Context context, boolean login, String tel, String pwd) {
		Intent intent = new Intent(context, LoginOrUpdateAccountDialog.class);
		intent.putExtra(Intents.EXTRA_TYPE, login);
		intent.putExtra(Intents.EXTRA_TEL, tel);
		intent.putExtra(Intents.EXTRA_PASSWORD, pwd);
		return intent;
	}
	
}
