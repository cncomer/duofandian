package com.lnwoowken.lnwoowkenbook.view.VercialTabHost;

import java.util.List;

import com.lnwoowken.lnwoowkenbook.R;
import com.lnwoowken.lnwoowkenbook.model.Contant;
import com.lnwoowken.lnwoowkenbook.model.ShopTree;
import com.lnwoowken.lnwoowkenbook.network.Client;
import com.lnwoowken.lnwoowkenbook.network.JsonParser;
import com.lnwoowken.lnwoowkenbook.tools.Tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
//import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

//import android.widget.TabHost.OnTabChangeListener;

@SuppressLint("HandlerLeak")
@SuppressWarnings("deprecation")
public class TabHostTest extends ActivityGroup implements
		MyTabHostLayout.OnTabChangeListener {
	// private List<ShopTree> shopTree;
	private final String TAG = "TabHostTest";
	//private RequestShopTreeThread mThread;
	private String resultTree;
	private MyTabHostLayout mTabHost;
	private Context context = TabHostTest.this;
	private static final int TAB_INDEX_ALL_CALL = 0;
	private static final int TAB_INDEX_OUT_CALL = 1;
	private static final int TAB_INDEX_RECEIVE_CALL = 2;
	private static final int TAB_INDEX_LOST_CALL = 3;
	private static int CURRENT_TAB_INDEX = TAB_INDEX_ALL_CALL;
	private static final String PREFERENCES_TABHOSTTEST = "preferences.tabhosttest";
	private static final String PREFERENCES_TAB_INDEX = "preferences.tab.index";
	private Button btn_back;

	/**
	 * 更新UI
	 */
	private Handler refrash_handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Log.d("refrash_handler,shopTree================", resultTree);
			if (Contant.SHOPTREE_LIST != null) {
				for (int i = 0; i < Contant.SHOPTREE_LIST.size(); i++) {
					setTab(Contant.SHOPTREE_LIST.get(i));
				}
			}

		}

	};

	/**
	 * 启动线程
	 */
	private Handler startThread_handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			RequestShopTreeThread mThread = new RequestShopTreeThread();
			mThread.start();
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_host_test);

		// mTabHost = super.getTabHost();
		mTabHost = (MyTabHostLayout) findViewById(R.id.edit_item_tab_host);
		mTabHost.setOnTabChangedListener(this);
		mTabHost.setup(this.getLocalActivityManager());
		Message msg = new Message();
		startThread_handler.sendMessage(msg);
		btn_back = (Button) findViewById(R.id.button_getback);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				
				startThread_handler.sendMessage(msg);
			}
		});
		// mTabHost.getTabWidget().setOrientation(1);

		// setupAllCall();
		// setupOutCall();
		// setupReceiveCall();
		// setupLostCall();

		/*
		 * setupTabWidget("first", R.drawable.alert_dialog_icon, 0);
		 * setupTabWidget("second", R.drawable.animated_gif, 1);
		 */
	}

	private void setTab(ShopTree tree) {
		Intent intent = new Intent("com.android.phone.action.RECENT_CALLS");
		intent.setClass(this, HelloWorld.class);
		intent.putExtra("id", tree.getId());

		mTabHost.addTab(mTabHost
				.newTabSpec("out_call_log" + tree.getId())
				.setIndicator(
						tree.getName(),
						getResources().getDrawable(
								R.drawable.ic_call_log_header_incoming_call))
				.setContent(intent));
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		SharedPreferences sp = getSharedPreferences("PREFERENCES_TABHOSTTEST",
				MODE_PRIVATE);
		CURRENT_TAB_INDEX = sp.getInt(PREFERENCES_TAB_INDEX, CURRENT_TAB_INDEX);

		// Intent intent = getIntent();
		Log.i("TabHostTest", "CURRENT_TAB_INDEX:" + CURRENT_TAB_INDEX);

		setCurrentTab(CURRENT_TAB_INDEX);

		LinearLayout t = mTabHost.getTabWidget();
		Log.i("TabWidget", t.getClass().toString());
		Log.i("TabWidget", t.getOrientation() + "");
		// t.setOrientation(1);
		for (int i = 0; i < t.getChildCount(); i++) {
			Log.i("TabWidget", "TabWidget child " + i + " "
					+ t.getChildAt(i).getClass());
		}
		/*
		 * Activity activity = getLocalActivityManager().
		 * getActivity(mTabHost.getCurrentTabTag());
		 */
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		SharedPreferences.Editor editer = getSharedPreferences(
				"PREFERENCES_TABHOSTTEST", MODE_PRIVATE).edit();
		editer.putInt(PREFERENCES_TAB_INDEX, CURRENT_TAB_INDEX);
		editer.commit();
	}

	private void setCurrentTab(int index) {
		// TODO Auto-generated method stub
		Log.i(TAG, "set current tab index:" + index);
		mTabHost.setCurrentTab(index);
	}

	private void setupLostCall() {
		// TODO Auto-generated method stub
		Intent intent = new Intent("com.android.phone.action.RECENT_CALLS");
		intent.setClass(this, HelloWorld.class);
		intent.putExtra("id", 1);
		mTabHost.addTab(mTabHost
				.newTabSpec("out_call_log" + 1)
				.setIndicator(
						"eee",
						getResources().getDrawable(
								R.drawable.ic_call_log_header_missed_call))
				.setContent(intent));
	}

	private void setupReceiveCall() {
		// TODO Auto-generated method stub
		Intent intent = new Intent("com.android.phone.action.RECENT_CALLS");
		intent.setClass(this, HelloWorld.class);
		intent.putExtra("id", 2);

		mTabHost.addTab(mTabHost
				.newTabSpec("out_call_log" + 2)
				.setIndicator(
						"aaa",
						getResources().getDrawable(
								R.drawable.ic_call_log_header_incoming_call))
				.setContent(intent));
	}

	private void setupOutCall() {
		// TODO Auto-generated method stub
		Intent intent = new Intent("com.android.phone.action.RECENT_CALLS");
		intent.setClass(this, HelloWorld.class);
		intent.putExtra("id", 3);
		mTabHost.addTab(mTabHost
				.newTabSpec("out_call_log" + 3)
				.setIndicator(
						"bbb",
						getResources().getDrawable(
								R.drawable.ic_call_log_header_outgoing_call))
				.setContent(intent));
	}

	private void setupAllCall() {
		// TODO Auto-generated method stub
		Intent intent = new Intent("com.android.phone.action.RECENT_CALLS");
		intent.setClass(this, HelloWorld.class);
		intent.putExtra("id", 4);
		mTabHost.addTab(mTabHost
				.newTabSpec("all_call_log" + 4)
				.setIndicator("ccc",
						getResources().getDrawable(R.drawable.icon))
				.setContent(intent));

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onTabChanged tabId:" + tabId);
		Activity activity = getLocalActivityManager().getActivity(tabId);
		Log.i(TAG, "onTabChanged" + activity.getClass());
		if (activity != null) {
			activity.onWindowFocusChanged(true);
		}

		// Remember this tab index. This function is also called, if the tab is
		// set automatically
		// in which case the setter (setCurrentTab) has to set this to its old
		// value afterwards
		CURRENT_TAB_INDEX = mTabHost.getCurrentTab();
	}

	public static int[] flag = new int[] { 1, 2, 3, 4 };

	private void getShopTree() {
		String resultJson;
		String opJson = "{\"Tid\":\"" + 0 + "\"}";
		opJson = Client.encodeBase64(opJson);
		String str = Tools.getRequestStr(Contant.SERVER_IP, Contant.SERVER_PORT
				+ "", "shop?id=", "s11", "&op=" + opJson);
		resultJson = Client.executeHttpGetAndCheckNet(str, this.context);
		resultJson = Client.decodeBase64(resultJson);
		resultTree = resultJson;
		if (resultJson != null) {

			Log.d("tableNumber=============", resultJson);
			Contant.SHOPTREE_LIST = JsonParser.parseShopTreeJson(resultJson);

		}
	}

	public class RequestShopTreeThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			getShopTree();
			Message msg = new Message();
			refrash_handler.sendMessage(msg);
		}
	}
}
