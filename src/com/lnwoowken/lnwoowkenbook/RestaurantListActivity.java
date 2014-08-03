package com.lnwoowken.lnwoowkenbook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.lnwoowken.lnwoowkenbook.adapter.AllStoreListAdapter;
import com.lnwoowken.lnwoowkenbook.adapter.OtherShopAdapter;
import com.lnwoowken.lnwoowkenbook.model.Contant;
import com.lnwoowken.lnwoowkenbook.model.StoreInfo;
import com.lnwoowken.lnwoowkenbook.thread.RequestServerThread;
import com.lnwoowken.lnwoowkenbook.view.OtherShopDialog;
import com.lnwoowken.lnwoowkenbook.view.ProgressDialog;
import com.lnwoowken.lnwoowkenbook.view.VercialTabHost.TabHostTest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

/**
 * 店家信息的界面
 * 
 * @author sean
 * 
 */
@SuppressLint("HandlerLeak")
@SuppressWarnings("unused")
public class RestaurantListActivity extends Activity implements OnClickListener {
	private AnimationDrawable draw;
    private	LinearLayout progress;
	private Intent intent;
	private String flagFrom;
	private List<StoreInfo> listStore;
	private ImageButton btn_search;
	private EditText search;
	private Button btn_home;// --返回主界面
	private PopupWindow popupWindow;
	private Button btn_sort;
	private ListView allStore_listView;
	private AllStoreListAdapter adapter;
	private Context context = RestaurantListActivity.this;
	private ImageButton btn_back;
	private RequestServerThread myThread;
	// private RequestServerThread tableNumThread;
	private Button btn_more;
	private Dialog dialog;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// textView.setText("server端返回的数据是：\n" + s);
			myThread.start();
			showProgressDialog();
			draw.start();
		}

	};

	private Handler tableHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// textView.setText("server端返回的数据是：\n" + s);
			// tableNumThread.start();
		}

	};

	private Handler initila_handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// textView.setText("server端返回的数据是：\n" + s);
			// myThread.start();
			if (msg.arg1 == 1 || myThread.getResult().equals(Contant.NO_NET)) {
				Toast.makeText(context, R.string.no_net, Toast.LENGTH_LONG)
						.show();
			} else {
				if (Contant.SHOP_LIST.size() == 0) {
					Toast.makeText(context, R.string.no_data,
							Toast.LENGTH_LONG).show();
				} else {
					listStore = Contant.SHOP_LIST;
					initialListView(listStore);
					if (dialog!=null) {
						dialog.dismiss();
					}
					
				}

			}

		}

	};
	
	
	private Handler listDataChanged_handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// textView.setText("server端返回的数据是：\n" + s);
			// myThread.start();
			//allStore_listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_list);

		int flag = Contant.FLAG_GETSHOPBYID;
//		if (listStore != null && listStore.size() > 0) {
//
//			initialListView(listStore);
//		} else {
			allStore_listView = (ListView) findViewById(R.id.listView_all_store);
			adapter = new AllStoreListAdapter(context, Contant.SHOP_LIST);
			initialListView(Contant.SHOP_LIST);
			if (Contant.SHOP_LIST.size()>0) {
				
			}
			else {
				myThread = new RequestServerThread("", initila_handler, context,
						flag,adapter,listDataChanged_handler);
				// tableNumThread = new RequestServerThread("", initila_handler,
				// context, 6);
				Message msg = new Message();
				handler.sendMessage(msg);
			}
			
			
			
			// Message msg1 = new Message();
			// tableHandler.sendMessage(msg1);
	//	}

		initialize();

	}
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		Log.d("onResume______________RestaurantListAcitity","");
//		int flag = Contant.FLAG_GETSHOPBYID;
////		if (listStore != null && listStore.size() > 0) {
////
////			initialListView(listStore);
////		} else {
//			allStore_listView = (ListView) findViewById(R.id.listView_all_store);
//			adapter = new AllStoreListAdapter(context, Contant.SHOP_LIST);
//			initialListView(Contant.SHOP_LIST);
//			if (Contant.SHOP_LIST.size()>0) {
//				
//			}
//			else {
//				myThread = new RequestServerThread("", initila_handler, context,
//						flag,adapter,listDataChanged_handler);
//				// tableNumThread = new RequestServerThread("", initila_handler,
//				// context, 6);
//				Message msg = new Message();
//				handler.sendMessage(msg);
//			}
//			
//			
//			
//			// Message msg1 = new Message();
//			// tableHandler.sendMessage(msg1);
//	//	}
//
//		initialize();
	}



	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.d("onRestart______________RestaurantListAcitity","");
//		int flag = Contant.FLAG_GETSHOPBYID;
////		if (listStore != null && listStore.size() > 0) {
////
////			initialListView(listStore);
////		} else {
//			allStore_listView = (ListView) findViewById(R.id.listView_all_store);
//			adapter = new AllStoreListAdapter(context, Contant.SHOP_LIST);
//			initialListView(Contant.SHOP_LIST);
//			if (Contant.SHOP_LIST.size()>0) {
//				
//			}
//			else {
//				myThread = new RequestServerThread("", initila_handler, context,
//						flag,adapter,listDataChanged_handler);
//				// tableNumThread = new RequestServerThread("", initila_handler,
//				// context, 6);
//				Message msg = new Message();
//				handler.sendMessage(msg);
//			}
//			
//			
//			
//			// Message msg1 = new Message();
//			// tableHandler.sendMessage(msg1);
//	//	}
//
//		initialize();
	}



	private void listViewDataChanged(){
		
	}

	private void initialListView(final List<StoreInfo> shopList) {
		
		
		allStore_listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		// allStore_listView.setDivider(null);
		allStore_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				// Log.d("00000000000", position+"");
				if (shopList != null && shopList.size() > 0) {

					Intent intent = new Intent(context,
							RestuarantInfoActivity.class);

					// Bundle bundle = new Bundle();
					// bundle.putInt("shopId",list_store.get(position-1).getId());
					intent.putExtra("shopId", shopList.get(position)
							.getId());
					Log.d("shopid______________RestaurantListAcitity",
							shopList.get(position).getId() + "");
					context.startActivity(intent);
				}
			}
		});
	}

	private void initialize() {
		intent = getIntent();
		flagFrom = intent.getExtras().getString("flag_from");
		btn_back = (ImageButton) findViewById(R.id.imageButton_back);
		btn_back.setOnClickListener(RestaurantListActivity.this);
		btn_search = (ImageButton) findViewById(R.id.imageButton_search);
		btn_search.setOnClickListener(RestaurantListActivity.this);
		btn_sort = (Button) findViewById(R.id.button_sort);
		btn_sort.setOnClickListener(RestaurantListActivity.this);
		btn_more = (Button) findViewById(R.id.button_more);
		btn_more.setOnClickListener(RestaurantListActivity.this);
		btn_home = (Button) findViewById(R.id.button_home);
		btn_home.setOnClickListener(RestaurantListActivity.this);
		search = (EditText) findViewById(R.id.editText_search);
		search.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// textview.setText(edittext.getText());
				// Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}

		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(btn_back)) {
			RestaurantListActivity.this.finish();
			if (popupWindow!=null) {
				popupWindow.dismiss();
				popupWindow = null;
			}
		}
		if (v.equals(btn_sort)) {
			Intent intent = new Intent(context, TabHostActivity.class);
			startActivity(intent);
			if (popupWindow!=null) {
				popupWindow.dismiss();
				popupWindow = null;
			}
		}
		if (v.equals(btn_more)) {
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
		if (v.equals(btn_home)) {
			Intent intent = new Intent(RestaurantListActivity.this,
					TestMain.class);
			startActivity(intent);
			RestaurantListActivity.this.finish();
			if (popupWindow!=null) {
				popupWindow.dismiss();
				popupWindow = null;
			}
		}
		if (v.equals(btn_search)) {
			if (listStore!=null&&!search.getText().toString().equals("")) {
				String temp = search.getText().toString();
				List<StoreInfo> tempList = new ArrayList<StoreInfo>();
				for (int i = 0; i < Contant.SHOP_LIST.size(); i++) {
					if (temp.equals(Contant.SHOP_LIST.get(i).getName())||
						Contant.SHOP_LIST.get(i).getName().contains(temp)) {
						tempList.add(Contant.SHOP_LIST.get(i));
						Log.d("search===========", Contant.SHOP_LIST.get(i).getName());
					}
					else {
						//listStore.remove(i);
					}
				}
				listStore = tempList;
				initialListView(listStore);
				allStore_listView.invalidate();
				if (popupWindow!=null) {
					popupWindow.dismiss();
					popupWindow = null;
				}
			}
			else {
				
			}
			
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 是否触发按键为back键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 弹出退出确认框
			Log.d("back==================", "");
			RestaurantListActivity.this.finish();
			// System.exit(0);

			return true;
		} else// 如果不是back键正常响应
		{
			return super.onKeyDown(keyCode, event);
		}

	}
	
	@Override 
	public void onBackPressed() { 
	super.onBackPressed(); 
		System.out.println("按下了back键 onBackPressed()"); 
		RestaurantListActivity.this.finish();
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

	private void showProgressDialog(){
		dialog = new ProgressDialog(RestaurantListActivity.this,
				R.style.ProgressDialog);
		dialog.show();
		progress = (LinearLayout) dialog.findViewById(R.id.imageView_progress);
	
		progress.setBackgroundResource(R.anim.animition_progress); 
        draw = (AnimationDrawable)progress.getBackground(); 
        dialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
				draw.stop();
			}
		});
//		progress.setIndeterminate(true);
		//setProgressBarIndeterminateVisibility(true); 
		
		
		dialog.setCanceledOnTouchOutside(false);
		
		
		// TableListDialog dialog = new
		// TableListDialog(BookTableActivity.this);
		// dialog.show();
	
	}
	
}
