
/**
 * 找回密码的ACTIVITY
 */
package com.lnwoowken.lnwoowkenbook;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cncom.app.base.account.AccountObject;
import com.cncom.app.base.ui.BaseActionbarActivity;
import com.cncom.app.base.util.DebugUtils;
import com.cncom.app.base.util.DialogUtils;
import com.lnwoowken.lnwoowkenbook.ServiceObject.ServiceResultObject;
import com.shwy.bestjoy.utils.AsyncTaskUtils;
import com.shwy.bestjoy.utils.NetworkUtils;
import com.shwy.bestjoy.utils.SecurityUtils.MD5;

public class FindPasswordActivity extends BaseActionbarActivity {
	private static final String TAG = "FindPasswordActivity";
	private Button btn_getSMS;
	private EditText editText_checkSMS;
	private EditText editText_phone;
	private EditText email;
	private AccountObject mAccountObject;
	private static final int TIME_COUNDOWN = 120000;
	private String mYanZhengCodeFromServer;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findpassword);
		mAccountObject = new AccountObject();
		initialize();
	}
	
	private void initialize(){
		btn_getSMS = (Button) findViewById(R.id.button_getSMS);
		btn_getSMS.setOnClickListener(FindPasswordActivity.this);
		editText_phone = (EditText) findViewById(R.id.editText_name);
		email = (EditText) findViewById(R.id.editText_email);
		editText_checkSMS = (EditText) findViewById(R.id.editText_checkSMS);
		findViewById(R.id.button_commit).setOnClickListener(FindPasswordActivity.this);
		editText_phone.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					if (editText_phone.getText().toString().contains("用户名")) {
						editText_phone.setText("");
					}
				}else {
					
				}
			}
		});
	}
	
	
	private boolean checkInput() {
		if(TextUtils.isEmpty(editText_phone.getText().toString())) {
			MyApplication.getInstance().showMessage(R.string.input_regist_tel);
			return false;
		}
		if(TextUtils.isEmpty(email.getText().toString())) {
			MyApplication.getInstance().showMessage(R.string.input_regist_email);
			return false;
		}
		if(!email.getText().toString().contains("@") || !email.getText().toString().trim().endsWith(".com")) {
			MyApplication.getInstance().showMessage(R.string.input_email_wrong_tips);
			return false;
		}
		if(TextUtils.isEmpty(editText_checkSMS.getText().toString())) {
			MyApplication.getInstance().showMessage(R.string.input_yanzhengcode);
			return false;
		}
		if(TextUtils.isEmpty(mYanZhengCodeFromServer) || !mYanZhengCodeFromServer.equals(MD5.md5(editText_checkSMS.getText().toString().trim()))) {
			MyApplication.getInstance().showMessage(R.string.input_yanzhengcode_error);
			return false;
		}
		return true;
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.button_commit:
			if(checkInput()) {
				findPasswordAsync();
			}
			break;
		case R.id.button_getSMS:
			String phone = editText_phone.getText().toString().trim();
			if (!TextUtils.isEmpty(phone)) {
				btn_getSMS.setTextColor(Color.GRAY);
				doTimeCountDown();
				getRandCodeAsync();
			} else {
				MyApplication.getInstance().showMessage(R.string.input_regist_tel);
			}
			break;
			default:
				super.onClick(v);
		}
		
		
	}
	private void doTimeCountDown() {
		new TimeCount(TIME_COUNDOWN, 1000).start();
	}
	class TimeCount extends CountDownTimer {

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			btn_getSMS.setEnabled(false);
			btn_getSMS.setTextColor(Color.GRAY);
		}

		@Override
		public void onFinish() {
			btn_getSMS.setText(mContext.getResources().getString(R.string.get_yanzheng_code));
			btn_getSMS.setEnabled(true);
			btn_getSMS.setTextColor(mContext.getResources().getColor(R.color.text_selector));
		}

		@Override
		public void onTick(long millisUntilFinished) {
			btn_getSMS.setText(mContext.getResources()
					.getString(R.string.time_countdown, millisUntilFinished / 1000));
		}
	}
	
	public static void startActivity(Context context) {
		Intent intent = new Intent(context, FindPasswordActivity.class);
		context.startActivity(intent);
	}
	
	@Override
	protected boolean checkIntent(Intent intent) {
		return true;
	}
	

	private FindPasswordTask mFindPasswordTask;
	private void findPasswordAsync() {
		AsyncTaskUtils.cancelTask(mFindPasswordTask);
		showDialog(DIALOG_PROGRESS);
		mFindPasswordTask = new FindPasswordTask();
		mFindPasswordTask.execute();
	}
	private class FindPasswordTask extends AsyncTask<Void, Void, ServiceResultObject> {

		@Override
		protected ServiceResultObject doInBackground(Void... params) {
			ServiceResultObject result = new ServiceResultObject();
			
			InputStream is = null;
			try {
				JSONObject queryJsonObject = new JSONObject();
				queryJsonObject.put("cell", editText_phone.getText().toString().trim());
				DebugUtils.logD(TAG, "FindPasswordTask doInBackground() queryJsonObject = " + queryJsonObject.toString());
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
				FindPasswordActivity.this.finish();
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
	

	private GetRandCodeTask mGetRandCodeTask;
	private void getRandCodeAsync() {
		AsyncTaskUtils.cancelTask(mGetRandCodeTask);
		showDialog(DIALOG_PROGRESS);
		mGetRandCodeTask = new GetRandCodeTask();
		mGetRandCodeTask.execute();
	}
	private class GetRandCodeTask extends AsyncTask<Void, Void, ServiceResultObject> {

		@Override
		protected ServiceResultObject doInBackground(Void... params) {
			ServiceResultObject result = new ServiceResultObject();
			
			InputStream is = null;
			try {
				JSONObject queryJsonObject = new JSONObject();
				queryJsonObject.put("cell", editText_phone.getText().toString().trim());
				DebugUtils.logD(TAG, "FindPasswordTask doInBackground() queryJsonObject = " + queryJsonObject.toString());
				is = NetworkUtils.openContectionLocked(ServiceObject.getYanzhengCodeUrl("para", queryJsonObject.toString()), null);
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
				mYanZhengCodeFromServer = result.mStrData;
				MyApplication.getInstance().showMessage(R.string.yanzhengcode_sent_tips);
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

}
