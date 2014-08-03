package com.lnwoowken.lnwoowkenbook.view.VercialTabHost;



import com.lnwoowken.lnwoowkenbook.BookTableActivity;
import com.lnwoowken.lnwoowkenbook.R;
import com.lnwoowken.lnwoowkenbook.model.Contant;
import com.lnwoowken.lnwoowkenbook.network.Client;
import com.lnwoowken.lnwoowkenbook.network.JsonParser;
import com.lnwoowken.lnwoowkenbook.tools.Tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class HelloWorld extends Activity implements OnClickListener {
	private TextView textView;
	private int id = 0;
	private Intent intent;
	private Context context = HelloWorld.this;
	private RequestShopTreeThread mThread;
	private String resultTree;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setTheme(android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hello_world);
		//textView = (TextView) findViewById(R.id.text);
		intent = getIntent();
		id = intent.getExtras().getInt("id");
		Toast.makeText(context, ""+id, Toast.LENGTH_SHORT).show();
		//textView.setText(intent.getExtras().getInt("id")+"");
		Message msg = new Message();
		startThread_handler.sendMessage(msg);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
	}
	
	
	/**
	 * 启动线程
	 */
	private Handler startThread_handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			mThread = new RequestShopTreeThread();
			mThread.start();
		}

	};
	
	/**
	 * 更新UI
	 */
	private Handler refrash_handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Log.d("refrash_handler,shopTree================", resultTree);
			//Contant.SHOPTREE_LIST
		}

	};
	
	private void getShopTree(){
		String resultJson;
		String opJson = "{\"Tid\":\""
				+id+ "\"}";
		opJson = Client.encodeBase64(opJson); 
		String str = Tools.getRequestStr(Contant.SERVER_IP,
				Contant.SERVER_PORT + "", "shop?id=", "s12",
				"&op=" + opJson);
		resultJson = Client.executeHttpGetAndCheckNet(str, this.context);
		resultJson = Client.decodeBase64(resultJson);
		resultTree = resultJson;
		if (resultJson != null) {
			
			Log.d("tableNumber=============", resultJson);
			// JsonParser.parseShopInfoJson(resultJson,Contant.SHOP_LIST.get(i));

		}
	}
	
	public class RequestShopTreeThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			if (id!=0) {
				getShopTree();
				Message msg = new Message();
				refrash_handler.sendMessage(msg);
			}
			
		}
	}
	
	
}
 