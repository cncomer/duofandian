
/**
 * 注册的ACTIVITY
 */
package com.lnwoowken.lnwoowkenbook;



import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cncom.app.base.account.AccountObject;
import com.cncom.app.base.service.TimeService;
import com.cncom.app.base.service.TimeService.CountdownObject;
import com.cncom.app.base.ui.BaseActionbarActivity;
import com.lnwoowken.lnwoowkenbook.ServiceObject.ServiceResultObject;
import com.shwy.bestjoy.utils.AsyncTaskUtils;
import com.shwy.bestjoy.utils.DebugUtils;
import com.shwy.bestjoy.utils.NetworkUtils;
import com.shwy.bestjoy.utils.SecurityUtils;

public class RegistActivity extends BaseActionbarActivity implements TimeService.CountdownCallback{
	private static final String TAG = "RegistActivity";
	private Button btn_regist;
	private Button btn_getSMS;
	
	private EditText editText_checkSMS;
	private EditText cell;
	private EditText email;
	private EditText niname;
	private EditText password;
	private EditText editText_pwd_confirm;
	private AccountObject mAccountObject;
	private static final int TIME_COUNDOWN = 120; //s
	private String mYanZhengCodeFromServer;
	
	
	private void initialize(){
		btn_getSMS = (Button) findViewById(R.id.button_getSMS);
		btn_getSMS.setOnClickListener(RegistActivity.this);
		btn_regist = (Button) findViewById(R.id.button_regist);
		btn_regist.setOnClickListener(RegistActivity.this);
		cell = (EditText) findViewById(R.id.editText_name);
		email = (EditText) findViewById(R.id.editText_email);
		niname = (EditText) findViewById(R.id.editText_niname);
		password = (EditText) findViewById(R.id.editText_pwd);
		editText_pwd_confirm = (EditText) findViewById(R.id.editText_pwd_confirm);
		editText_checkSMS = (EditText) findViewById(R.id.editText_checkSMS);
		
		CountdownObject newCountdownObject = new CountdownObject(TAG, TIME_COUNDOWN, this);
		CountdownObject exsitedCountdownObject = TimeService.getService().getCountdownObject(TAG);
		if (exsitedCountdownObject != null &&  exsitedCountdownObject.getCurrent() > 1) {
			TimeService.getService().commit(newCountdownObject, false);
		}
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitvity_regist);
		mAccountObject = new AccountObject();
		initialize();
	}

	private boolean checkConfirm(){
		if (editText_pwd_confirm.getText().toString().equals(password.getText().toString())) {
			return true;
		}
		return false;
	}
	
	private boolean checkInput() {
		mAccountObject.mAccountTel = cell.getText().toString().trim();
		if(TextUtils.isEmpty(mAccountObject.mAccountTel)) {
			MyApplication.getInstance().showMessage(R.string.msg_empty_cell);
			return false;
		}
		mAccountObject.mAccountEmail = email.getText().toString().trim();
		if(TextUtils.isEmpty(mAccountObject.mAccountEmail) || !mAccountObject.mAccountEmail.contains("@")) {
			MyApplication.getInstance().showMessage(R.string.msg_empty_email);
			return false;
		}
		if(!checkConfirm()) {
			MyApplication.getInstance().showMessage(R.string.msg_password_not_equal);
			return false;
		}
		mAccountObject.mAccountPwd = editText_pwd_confirm.getText().toString();
		mAccountObject.mAccountName = niname.getText().toString();
		if(TextUtils.isEmpty(mAccountObject.mAccountName)) {
			MyApplication.getInstance().showMessage(R.string.msg_empty_niname);
			return false;
		}
		String yanzhengma = editText_checkSMS.getText().toString().trim();
		if (TextUtils.isEmpty(yanzhengma)) {
			MyApplication.getInstance().showMessage(R.string.msg_empty_yanzhengma);
			return false;
		} else if (!SecurityUtils.MD5.md5(yanzhengma).equals(mYanZhengCodeFromServer)) {
			DebugUtils.logD(TAG, "yanzhengma=" + SecurityUtils.MD5.md5(yanzhengma));
			MyApplication.getInstance().showMessage(R.string.msg_yanzhengma_not_correct);
			return false;
		}
		return true;
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.button_regist:
			if(checkInput()) {
				registerAsync();
			}	
			break;
		case R.id.button_getSMS:
			String phone = cell.getText().toString().trim();
			if (!TextUtils.isEmpty(phone)) {
				getYanzhengmaTask(phone);
				CountdownObject newCountdownObject = new CountdownObject(TAG, TIME_COUNDOWN, this);
				TimeService.getService().remove(newCountdownObject);
				TimeService.getService().commit(newCountdownObject, true);
			} else {
				MyApplication.getInstance().showMessage(R.string.msg_empty_cell);
			}
			break;
			default:
				super.onClick(v);
		}
		
		
	}
	
	private GetYanzhengmaTask mGetYanzhengmaTask;
	private void getYanzhengmaTask(String cell) {
		AsyncTaskUtils.cancelTask(mGetYanzhengmaTask);
		mGetYanzhengmaTask = new GetYanzhengmaTask(cell);
		mGetYanzhengmaTask.execute();
		showDialog(DIALOG_PROGRESS);
	}
	private class GetYanzhengmaTask extends AsyncTask<Void, Void, ServiceResultObject> {

		private String _cell;
		public GetYanzhengmaTask(String cell) {
			_cell = cell;
		}
		@Override
		protected ServiceResultObject doInBackground(Void... params) {
			InputStream is = null;
			ServiceResultObject serviceResultObject = new ServiceResultObject();
			try {
				JSONObject queryJSONObject = new JSONObject();
				queryJSONObject.put("cell", _cell);
				is = NetworkUtils.openContectionLocked(ServiceObject.getYanzhengCodeUrl("para", queryJSONObject.toString()), MyApplication.getInstance().getSecurityKeyValuesObject());
				if (is != null) {
					serviceResultObject = ServiceResultObject.parse(NetworkUtils.getContentFromInput(is));
					if (serviceResultObject.isOpSuccessfully()) {
						mYanZhengCodeFromServer = serviceResultObject.mStrData;
						DebugUtils.logD(TAG, "GetYanzhengmaTask mYanZhengCodeFromServer=" + mYanZhengCodeFromServer);
					}
				}
				
			} catch (NumberFormatException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
			} catch (JSONException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
			} catch (IOException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
			}
			return serviceResultObject;
		}

		@Override
		protected void onPostExecute(ServiceResultObject result) {
			super.onPostExecute(result);
			dismissDialog(DIALOG_PROGRESS);
			MyApplication.getInstance().showMessage(result.mStatusMessage);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			dismissDialog(DIALOG_PROGRESS);
		}
		
	}
	
	
	private void showDialog() {
		Dialog alertDialog = new AlertDialog.Builder(this)
				.setTitle("提示")
				.setMessage("是否现在登录?")
				.
				// setIcon(R.drawable.welcome_logo).
				setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(mContext, LoginActivity.class);
						startActivity(intent);
						RegistActivity.this.finish();
						//BookTableActivity.this.finish();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						RegistActivity.this.finish();
					}
				}).

				create();
		alertDialog.show();
	}
	
	private RegisterTask mRegisterTask;
	private void registerAsync() {
		AsyncTaskUtils.cancelTask(mRegisterTask);
		mRegisterTask = new RegisterTask();
		mRegisterTask.execute();
		showDialog(DIALOG_PROGRESS);
	}
	private class RegisterTask extends AsyncTask<Void, Void, ServiceResultObject> {

		@Override
		protected ServiceResultObject doInBackground(Void... params) {
			InputStream is = null;
			ServiceResultObject serviceResultObject = new ServiceResultObject();
			try {
				//para={"cell":"18621951097","pwd":"wangkun","nickname":"kun","email":"369319633@qq.com"}
				//String str = "http://manage.lnwoowken.com/Mobile/common/register.ashx?para={\"cell\":\"" + name.getText().toString().trim() + "\",\"pwd\":\"" + password.getText().toString() + "\",\"nickname\":\"" + niname.getText().toString().trim() + "\",\"email\":\"" + email.getText().toString().trim() +"\"}";
				JSONObject queryJSONObject = new JSONObject();
				queryJSONObject.put("cell", mAccountObject.mAccountTel);
				queryJSONObject.put("pwd", mAccountObject.mAccountPwd);
				queryJSONObject.put("nickname", mAccountObject.mAccountName);
				queryJSONObject.put("email", mAccountObject.mAccountEmail);
				is = NetworkUtils.openContectionLocked(ServiceObject.getRegisterUrl("para", queryJSONObject.toString()), MyApplication.getInstance().getSecurityKeyValuesObject());
				if (is != null) {
					serviceResultObject = ServiceResultObject.parse(NetworkUtils.getContentFromInput(is));
				}
				
			} catch (NumberFormatException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
			} catch (JSONException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
			} catch (IOException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
			}
			return serviceResultObject;
		}

		@Override
		protected void onPostExecute(ServiceResultObject result) {
			super.onPostExecute(result);
			dismissDialog(DIALOG_PROGRESS);
			MyApplication.getInstance().showMessage(result.mStatusMessage);
			if (result.isOpSuccessfully()) {
				showDialog();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			dismissDialog(DIALOG_PROGRESS);
		}
		
	}
	
	
	
	
	
	public static void startActivity(Context context) {
		Intent intent = new Intent(context, RegistActivity.class);
		context.startActivity(intent);
	}
	@Override
	protected boolean checkIntent(Intent intent) {
		return true;
	}
	@Override
	public void countdown(int current) {
		if (current > 1) {
			btn_getSMS.setText(mContext.getResources().getString(R.string.time_countdown, current));
		} else {
			btn_getSMS.setText(mContext.getResources().getString(R.string.get_yanzheng_code));
			btn_getSMS.setEnabled(true);
			btn_getSMS.setTextColor(mContext.getResources().getColor(R.color.text_selector));
			
			mYanZhengCodeFromServer = "";
		}
	}
	
	@Override
	public void start(int current) {
		//开始倒数
		btn_getSMS.setEnabled(false);
		btn_getSMS.setText(mContext.getResources().getString(R.string.time_countdown, current));
	}

}
