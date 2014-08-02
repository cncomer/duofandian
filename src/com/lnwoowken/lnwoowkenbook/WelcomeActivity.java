package com.lnwoowken.lnwoowkenbook;

import com.lnwoowken.lnwoowkenbook.thread.RequestServerThread;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;


@SuppressLint("HandlerLeak")
public class WelcomeActivity extends Activity {
	private TextView textView_title;
	private TextView textView_copyright;
	//private RequestServerThread versionThread;
	private SharedPreferences preferences;
	
	private Handler handler = new Handler(){
 
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Intent intent = new Intent(WelcomeActivity.this, TestMain.class);
			startActivity(intent);
			WelcomeActivity.this.finish();
			
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		initialize();
		

	}
	
	private void initialize(){
		//textView_title = (TextView) findViewById(R.id.textView_title);
		textView_copyright = (TextView) findViewById(R.id.textView_copyright);
		AssetManager mgr=getAssets();//得到AssetManager
		//Typeface tf1=Typeface.createFromAsset(mgr, "font/fzybxsj.TTF");//根据路径得到Typeface
		//Typeface tf2=Typeface.createFromAsset(mgr, "font/fzzyjt.ttf");//根据路径得到Typeface
		//textView_title.setTypeface(tf1);
		//textView_copyright.setTypeface(tf2);
		TimeThread mThread = new TimeThread(); 
		mThread.start();
		//checkFirst();
		
	}
	
	class TimeThread extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			int index = 1;
			while (index>=0) {
				index--;
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			Message msg = new Message();
			handler.sendMessage(msg);
		}
		
	}
	
	
}
