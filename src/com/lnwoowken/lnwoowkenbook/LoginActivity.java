﻿package com.lnwoowken.lnwoowkenbook;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.lnwoowken.lnwoowkenbook.model.Contant;
import com.lnwoowken.lnwoowkenbook.thread.RequestServerThread;

@SuppressLint("HandlerLeak")
public class LoginActivity extends Activity implements OnClickListener {
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		//
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			LoginActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// TODO Auto-generated method stub

		if (popupWindow != null && popupWindow.isShowing()) {

			popupWindow.dismiss();

			popupWindow = null;

		}

		return super.onTouchEvent(event);

	}
}
