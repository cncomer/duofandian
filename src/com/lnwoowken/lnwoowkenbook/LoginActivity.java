package com.lnwoowken.lnwoowkenbook;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.cncom.app.base.account.AccountObject;
import com.cncom.app.base.account.AccountParser;
import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.util.DebugUtils;
import com.cncom.app.base.util.DialogUtils;
import com.lnwoowken.lnwoowkenbook.ServiceObject.ServiceResultObject;
import com.lnwoowken.lnwoowkenbook.model.Contant;
import com.lnwoowken.lnwoowkenbook.thread.RequestServerThread;
import com.shwy.bestjoy.utils.AsyncTaskUtils;
import com.shwy.bestjoy.utils.ComConnectivityManager;
import com.shwy.bestjoy.utils.NetworkUtils;
import com.shwy.bestjoy.utils.SecurityUtils;

@SuppressLint("HandlerLeak")
public class LoginActivity extends Activity implements OnClickListener {
	private static final String TAG = "LoginActivity";
	private PopupWindow popupWindow;
	private Button btn_back;
	private Button btn_home;
	private Button btn_more;
	private Context context = LoginActivity.this;
	private Button btn_login;
	private EditText editText_uid;
	private EditText editText_pwd;
	private RequestServerThread myThread;
	private Button btn_regist;
	private Button btn_getPwd;
	private AccountObject mAccountObject;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// textView.setText("server端返回的数据是：\n" + s);
			myThread.start();
			//Toast.makeText(context, myThread.getResult(), Toast.LENGTH_SHORT).show();
		}

	};
	private Handler login_result_handler = new Handler() {
		private String _error;
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// textView.setText("server端返回的数据是：\n" + s);
		//	myThread.start();
			String result = myThread.getResult();
			Log.d("result==========================info", result);
			
			try {
				mAccountObject = AccountParser.parseJson(result);
				if (mAccountObject != null && mAccountObject.isLogined()) {
					boolean saveAccountOk = MyAccountManager.getInstance().saveAccountObject(LoginActivity.this.getContentResolver(), mAccountObject);
					if (!saveAccountOk) {
						//登录成功了，但本地数据保存失败，通常不会走到这里
						_error = LoginActivity.this.getString(R.string.msg_login_save_success);
					} else {
						Toast.makeText(context,context.getResources().getString(R.string.login_success) , Toast.LENGTH_SHORT).show();
						Intent in = new Intent();
						in.setAction("login");  
			            sendBroadcast(in);  
						LoginActivity.this.finish();
					}
				} else {
					Toast.makeText(context, context.getResources().getString(R.string.login_error)+",请检查用户名密码是否正确", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
				_error = e.getMessage();
			}
			if (_error != null) {
				Toast.makeText(context, _error, Toast.LENGTH_SHORT).show();
			}
			
			/*if (JsonParser.checkError(result)) {
				//Toast.makeText(context, context.getResources().getString(R.string.login_error)+result, Toast.LENGTH_SHORT).show();
				Toast.makeText(context, context.getResources().getString(R.string.login_error)+",请检查用户名密码是否正确", Toast.LENGTH_SHORT).show();
			}
			else {
				Toast.makeText(context,context.getResources().getString(R.string.login_success) , Toast.LENGTH_SHORT).show();
				Log.d("login_success==========================info", result);
			//	result = Client.decodeBase64(result);
				//Log.d("result+++++++++++++++++", result);
				if (result != null && !result.equals("")) {
					Contant.USER  = JsonParser.parseUserInfoJson(result);
					if (Contant.USER!=null) {
						Log.d("login===================", "userinfo解析成功");
						Contant.ISLOGIN = true;
						//Intent intent = new Intent(LoginActivity.this, UserInfoActivity.class);
						Intent intent1 = new Intent();
						intent1.setAction("login");  
			            sendBroadcast(intent1);  
						//startActivity(intent);
						LoginActivity.this.finish();
						
					}
				}
			}*/
			
			
		}

	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initialize();
	}
	private void initialize(){
		btn_login = (Button) findViewById(R.id.button_login);
		btn_regist = (Button) findViewById(R.id.button_regist);
		btn_getPwd = (Button) findViewById(R.id.button_getpwd);
		editText_uid = (EditText) findViewById(R.id.EditText_uid);
		editText_uid.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					if (editText_uid.getText().toString().contains("用户名")) {
						editText_uid.setText("");
					}
				}else {
					
				}
			}
		});
		editText_pwd = (EditText) findViewById(R.id.EditText_pwd);
		editText_pwd.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					if (editText_pwd.getText().toString().contains("密码")) {
						editText_pwd.setText("");
						editText_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					}
				}else {
					
				}
			}
		});
		btn_login.setOnClickListener(LoginActivity.this);
		btn_regist.setOnClickListener(LoginActivity.this);
		btn_getPwd.setOnClickListener(LoginActivity.this);
		btn_home = (Button) findViewById(R.id.button_home);
		btn_home.setOnClickListener(LoginActivity.this);
		btn_more = (Button) findViewById(R.id.button_more);
		btn_more.setOnClickListener(LoginActivity.this);
		btn_back = (Button) findViewById(R.id.button_back);
		btn_back.setOnClickListener(LoginActivity.this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(btn_login)) {
			if (Contant.ISLOGIN) {
				
			}
			else {
				//test account user "15001881049" pwd "123456";
				String user = editText_uid.getText().toString();
				String pwd = editText_pwd.getText().toString();
				if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(pwd)) {

					myThread = new RequestServerThread(ServiceObject.getLoginOrUpdateUrl(user, pwd), login_result_handler,
							LoginActivity.this, Contant.FLAG_LOGIN);
					Message msg = new Message();
					handler.sendMessage(msg);
				}
				else {
					Toast.makeText(context, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
				}
			}
			
		}
		else if (v.equals(btn_regist)) {
			Intent intent = new Intent(context, RegistActivity.class);
			startActivity(intent);
			
		}
		else if (v.equals(btn_getPwd)) {
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
		}
		else if (v.equals(btn_more)) {
			if (popupWindow == null || !popupWindow.isShowing()) {
				View view = LayoutInflater.from(context).inflate(
						R.layout.popmenu, null);
				popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				popupWindow.showAsDropDown(v, 10, 10);
				// 使其聚集
				// popupWindow.setFocusable(true);
				// 设置允许在外点击消失
				// popupWindow.setOutsideTouchable(true);
				// 刷新状态（必须刷新否则无效）
				popupWindow.update();
			} else {
				popupWindow.dismiss();
				popupWindow = null;
			}

		} 
		else if (v.equals(btn_back)) {
			LoginActivity.this.finish();
			
		}
		else if (v.equals(btn_home)) {
			Intent intent = new Intent(LoginActivity.this, TestMain.class);
			startActivity(intent);
			LoginActivity.this.finish();
		}
		
	}
	private FidnPasswordTask mFidnPasswordTask;
	private void findPasswordAsync() {
		AsyncTaskUtils.cancelTask(mFidnPasswordTask);
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
			if (result.isOpSuccessfully()) {
				MyApplication.getInstance().showMessage(result.mStatusMessage);
			} else {
				DialogUtils.createSimpleConfirmAlertDialog(context, context.getString(R.string.tel_not_register), null);
			}
			
		}
		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
		
		
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			LoginActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (popupWindow != null && popupWindow.isShowing()) {

			popupWindow.dismiss();

			popupWindow = null;

		}

		return super.onTouchEvent(event);

	}
}
