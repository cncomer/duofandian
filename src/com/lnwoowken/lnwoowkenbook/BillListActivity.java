package com.lnwoowken.lnwoowkenbook;

import java.util.ArrayList;
import java.util.List;

import com.lnwoowken.lnwoowkenbook.adapter.BillListAdapter;
import com.lnwoowken.lnwoowkenbook.model.Bill;
import com.lnwoowken.lnwoowkenbook.model.Contant;
import com.lnwoowken.lnwoowkenbook.network.Client;
import com.lnwoowken.lnwoowkenbook.network.JsonParser;
import com.lnwoowken.lnwoowkenbook.tools.Tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class BillListActivity extends Activity {
	private PopupWindow popupWindow;
	private Context context = BillListActivity.this;
	private ListView listBill;
	private BillListAdapter adapter;
	private List<Bill> list;
	private Button btn_back;
	private Button btn_home;
	private Button btn_more;//--“更多”按钮
	
	
	private Handler toastHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Toast.makeText(context, "您还没有登录,请先登录", Toast.LENGTH_SHORT).show();
		}

	};
	
	private Handler billListHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			RequestBillListThread payThread = new RequestBillListThread();
			payThread.start();
		}

	};
	private Handler UIHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			adapter = new BillListAdapter(context, list);
			listBill.setAdapter(adapter);
			listBill.setDivider(null);
			listBill.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					
					
				}
				
			});
		}

	};
	
	public class RequestBillListThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			// Log.d("___________====", "I'm in");
			// Log.d("___________====", url);
			if (Contant.ISLOGIN&&Contant.USER!=null) {
				//createBill();
				getBillList(Contant.USER.getId()+"");
			}
			else {
				//showDialog();
				Message msg = new Message();
				toastHandler.sendMessage(msg);
			}
			

		}	
	}
	
	
	private String getBillList(String uid){
		String resultJson;
		String opJson = "{\"id\":\""
				+ uid + "\"}";
		opJson = Client.encodeBase64(opJson);
		String str = Tools.getRequestStr(Contant.SERVER_IP,
				Contant.SERVER_PORT + "", "Reserve?id=", Contant.LISTBILL,
				"&op=" + opJson);
		resultJson = Client.executeHttpGetAndCheckNet(str, BillListActivity.this);
		resultJson = Client.decodeBase64(resultJson);

		if (resultJson != null) {
			Log.d("getBillList=============", resultJson);
			list=JsonParser.parseBillListJson(resultJson);
			Log.i("INFO", list.size()+"");
			Message msg = new Message();
			UIHandler.sendMessage(msg);
		}
		return resultJson;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//获取屏幕大小
		Display mDisplay = getWindowManager().getDefaultDisplay(); 
		 int width = mDisplay.getWidth();
		 int height = mDisplay.getHeight();
		 setContentView(R.layout.activity_list_bill);	
//		if(width==480 && height==800) {
//			setContentView(R.layout.activity_list_bill);	
//		}else if (width==1080 && height==1920){
//			setContentView(R.layout.activity_list_bill2);
//		}
		initialize();
		
	}
	
	private void initialize(){
		listBill = (ListView) findViewById(R.id.listView_bill);
		btn_back = (Button) findViewById(R.id.button_back);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BillListActivity.this.finish();
			}
		});
		//list = new ArrayList<Bill>();
		Log.i("INFO==", list+"");
		
		btn_home = (Button) findViewById(R.id.button_home);
		btn_more = (Button) findViewById(R.id.button_more);
		btn_home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(BillListActivity.this, TestMain.class);
				startActivity(intent);
				BillListActivity.this.finish();
			}
		});
		btn_more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (popupWindow == null || !popupWindow.isShowing()) {
					View view = LayoutInflater.from(context).inflate(
							R.layout.popmenu, null);
					RelativeLayout myBill = (RelativeLayout) view.findViewById(R.id.mybill);
					RelativeLayout exitLogin = (RelativeLayout) view.findViewById(R.id.exit_login);
					exitLogin.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (Contant.ISLOGIN) {
								showExitLoginDialog();
							} else {
								Intent intent = new Intent(context, LoginActivity.class);
								startActivity(intent);
								
							}
							popupWindow.dismiss();
							popupWindow = null;
						}
					});
					myBill.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Log.d("popwindow=============", "in");
							Intent intent = new Intent(context, BillListActivity.class);
							startActivity(intent); 
							popupWindow.dismiss();
							popupWindow = null;
						}
					});
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
		});
		Message msg = new Message();
		billListHandler.sendMessage(msg);
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
	
	private void showExitLoginDialog() {
		Dialog alertDialog = new AlertDialog.Builder(this)
				.setTitle("提示")
				.setMessage("您已经登录,是否要退出重新登录?")
				.
				// setIcon(R.drawable.welcome_logo).
				setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Contant.ISLOGIN = false;
						Contant.USER = null;
						Intent intent1 = new Intent();
						intent1.setAction("login");
						sendBroadcast(intent1);
						Toast.makeText(context, "成功退出登录", Toast.LENGTH_SHORT)
								.show();
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
