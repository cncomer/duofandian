package com.lnwoowken.lnwoowkenbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
//import android.content.Context;
import android.os.Bundle;

import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lnwoowken.lnwoowkenbook.model.Contant;

public class UserInfoActivity extends Activity implements OnClickListener {
	// private Button btn_regist;
	// private Context context = UserInfoActivity.this;
	private TextView userName;
	private TextView phoneNumber;
	private Button btn_back;
	private TextView exitLogin;
	private RelativeLayout r1, r2, r3, r4, r5, r6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 获取屏幕大小
		Display mDisplay = getWindowManager().getDefaultDisplay();
		int width = mDisplay.getWidth();
		int height = mDisplay.getHeight();
		setContentView(R.layout.activity_user_center);
		// if(width==480 && height==800) {
		// setContentView(R.layout.activity_user_center);
		// }else if (width==1080 && height==1920){
		// setContentView(R.layout.activity_user_center2);
		// }
		initialize();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String text = "敬请期待！";
		if (v.equals(btn_back)) {
			UserInfoActivity.this.finish();
		}
		if (v.equals(exitLogin)) {
			if (Contant.ISLOGIN) {
				showExitLoginDialog();
			}
		}
		if (v.equals(r1)) {
			Toast.makeText(UserInfoActivity.this, text, 1).show();
			// Toast.makeText(UserInfoActivity.this, text,Toast.LENGTH_SHORT).show();
		}
		if (v.equals(r2)) {
			Toast.makeText(UserInfoActivity.this, text, 1).show();
		}
		if (v.equals(r3)) {
			Toast.makeText(UserInfoActivity.this, text, 1).show();
		}
		if (v.equals(r4)) {
			Toast.makeText(UserInfoActivity.this, text, 1).show();
		}
		if (v.equals(r5)) {
			Toast.makeText(UserInfoActivity.this, text, 1).show();
		}
		if (v.equals(r6)) {
			Toast.makeText(UserInfoActivity.this, text, 1).show();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		//
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			UserInfoActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initialize() {
		userName = (TextView) findViewById(R.id.textView_username);
		phoneNumber = (TextView) findViewById(R.id.textView_phone_data);
		btn_back = (Button) findViewById(R.id.button_back);
		btn_back.setOnClickListener(UserInfoActivity.this);
		exitLogin = (TextView) findViewById(R.id.textView_exit);
		exitLogin.setOnClickListener(UserInfoActivity.this);
		r1 = (RelativeLayout) findViewById(R.id.r1);
		r2 = (RelativeLayout) findViewById(R.id.r2);
		r3 = (RelativeLayout) findViewById(R.id.r3);
		r4 = (RelativeLayout) findViewById(R.id.r4);
		r5 = (RelativeLayout) findViewById(R.id.r5);
		r6 = (RelativeLayout) findViewById(R.id.r6);
		r1.setOnClickListener(UserInfoActivity.this);
		r2.setOnClickListener(UserInfoActivity.this);
		r3.setOnClickListener(UserInfoActivity.this);
		r4.setOnClickListener(UserInfoActivity.this);
		r5.setOnClickListener(UserInfoActivity.this);
		r6.setOnClickListener(UserInfoActivity.this);
		if (Contant.USER != null) {
			userName.setText(Contant.USER.getName());
			phoneNumber.setText(Contant.USER.getPhoneNum());
		}
	}

	private void showExitLoginDialog() {
		Dialog alertDialog = new AlertDialog.Builder(this).setTitle("提示")
				.setMessage("您确定要退出登录吗?")
				.
				// setIcon(R.drawable.welcome_logo).
				setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Contant.ISLOGIN = false;
						Contant.USER = null;

						UserInfoActivity.this.finish();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				}).

				create();
		alertDialog.show();
	}
}
