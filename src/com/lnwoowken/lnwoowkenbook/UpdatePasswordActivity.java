package com.lnwoowken.lnwoowkenbook;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.cncom.app.base.account.AccountObject;
import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.database.DBHelper;
import com.cncom.app.base.ui.BaseActivity;
import com.cncom.app.base.util.DebugUtils;
import com.lnwoowken.lnwoowkenbook.ServiceObject.ServiceResultObject;
import com.shwy.bestjoy.utils.AsyncTaskUtils;
import com.shwy.bestjoy.utils.NetworkUtils;

public class UpdatePasswordActivity extends BaseActivity{

	private static final String TAG = "UpdatePasswordActivity";
	private EditText mOldPwdInput, mNewPwdInput, mNewPwdReInput;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_update_password);
		
		mOldPwdInput = (EditText) findViewById(R.id.password_input);
		mNewPwdInput = (EditText) findViewById(R.id.newpassword_input);
		mNewPwdReInput = (EditText) findViewById(R.id.renewpassword_input);
		
		findViewById(R.id.button_modify).setOnClickListener(this);
		findViewById(R.id.button_cancel).setOnClickListener(this);
	}
	
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.button_cancel:
			finish();
			break;
		case R.id.button_modify:
			//检查输入有效性
			String oldPassword = mOldPwdInput.getText().toString().trim();
			String newPassword = mNewPwdInput.getText().toString().trim();
			String newPasswordAgain = mNewPwdReInput.getText().toString().trim();
			if (TextUtils.isEmpty(oldPassword)) {
				MyApplication.getInstance().showMessage(R.string.msg_password_input_empty);
				return;
			}
			if (TextUtils.isEmpty(newPassword)) {
				MyApplication.getInstance().showMessage(R.string.msg_password_input_new_empty);
				return;
			}
			
			if (TextUtils.isEmpty(newPasswordAgain)) {
				MyApplication.getInstance().showMessage(R.string.msg_password_input_renew_empty);
				return;
			}
			
			if (!newPasswordAgain.equals(newPassword)) {
				MyApplication.getInstance().showMessage(R.string.msg_password_input_not_equal);
				return;
			}
			updateAccounNameAsync(oldPassword, newPassword);
			break;
		}
	}
	
	
	private UpdateAccountPasswordAsyncTask mUpdateAccountPasswordAsyncTask;
	private void updateAccounNameAsync(String oldPwd, String newPwd) {
		showDialog(DIALOG_PROGRESS);
		AsyncTaskUtils.cancelTask(mUpdateAccountPasswordAsyncTask);
		mUpdateAccountPasswordAsyncTask = new UpdateAccountPasswordAsyncTask();
		mUpdateAccountPasswordAsyncTask.execute(oldPwd, newPwd);
	}
	
	private class UpdateAccountPasswordAsyncTask extends AsyncTask<String, Void, ServiceResultObject> {

		@Override
		protected ServiceResultObject doInBackground(String... params) {
			InputStream is = null;
			ServiceResultObject serviceObject = new ServiceResultObject();
			try {
				AccountObject accountObject = MyAccountManager.getInstance().getAccountObject();
				JSONObject queryJsonObject = new JSONObject();
				queryJsonObject.put("UID", accountObject.mAccountUid);
				queryJsonObject.put("pwd", params[1]);
				DebugUtils.logD(TAG, "UpdateAccountPasswordAsyncTask doInBackground() queryJsonObject=" + queryJsonObject.toString());
				is = NetworkUtils.openContectionLocked(ServiceObject.getUpdateUserLoginPasswordUrl("para", queryJsonObject.toString()), MyApplication.getInstance().getSecurityKeyValuesObject());
				
				serviceObject = ServiceResultObject.parse(NetworkUtils.getContentFromInput(is));
				if (serviceObject.isOpSuccessfully()) {
					DebugUtils.logD(TAG, "UpdateAccountPasswordAsyncTask--updatePwssword successfully. start save new password.");
					ContentValues values = new ContentValues();
					accountObject.mAccountPwd = params[1];
					values.put(DBHelper.ACCOUNT_PWD, params[1]);
					accountObject.updateAccount(mContext.getContentResolver(), values);
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
				NetworkUtils.closeInputStream(is);
			}
			return serviceObject;
		}

		@Override
		protected void onPostExecute(ServiceResultObject result) {
			super.onPostExecute(result);
			dismissDialog(DIALOG_PROGRESS);
			MyApplication.getInstance().showMessage(result.mStatusMessage);
			if (result.isOpSuccessfully()) {
				finish();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			dismissDialog(DIALOG_PROGRESS);
			MyApplication.getInstance().showMessage(R.string.msg_op_canceled);
		}
		
	}
	
	@Override
	protected boolean checkIntent(Intent intent) {
		return true;
	}
	
	
	public static void startActivity(Context context, Bundle bundle) {
		Intent intent = new Intent(context, UpdatePasswordActivity.class);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		context.startActivity(intent);
		
	}

}
