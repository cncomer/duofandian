package com.lnwoowken.lnwoowkenbook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


@SuppressLint("HandlerLeak")
public class WelcomeActivity extends Activity {
	
	private static final int WHAT_FINISH_ACTIVITY_DELAY = 1;
	private Handler handler = new Handler(){
 
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			MainActivity.startIntent(WelcomeActivity.this, null);
			WelcomeActivity.this.finish();
			
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		handler.sendEmptyMessageDelayed(WHAT_FINISH_ACTIVITY_DELAY, 500);

	}
}
