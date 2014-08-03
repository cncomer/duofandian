package com.lnwoowken.lnwoowkenbook;



import java.util.ArrayList;
import java.util.List;

import com.lnwoowken.lnwoowkenbook.adapter.ShopAdapter;
import com.lnwoowken.lnwoowkenbook.adapter.TabAdapter;
import com.lnwoowken.lnwoowkenbook.model.Contant;
import com.lnwoowken.lnwoowkenbook.model.ShopTree;
import com.lnwoowken.lnwoowkenbook.model.StoreInfo;
import com.lnwoowken.lnwoowkenbook.model.TabObj;
import com.lnwoowken.lnwoowkenbook.network.Client;
import com.lnwoowken.lnwoowkenbook.network.JsonParser;
import com.lnwoowken.lnwoowkenbook.thread.RequestServerThread;
import com.lnwoowken.lnwoowkenbook.tools.Tools;
import com.lnwoowken.lnwoowkenbook.view.VercialTabHost.HelloWorld;
import com.lnwoowken.lnwoowkenbook.view.VercialTabHost.TabHostTest.RequestShopTreeThread;

import android.annotation.SuppressLint;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
@SuppressWarnings("unused")
public class TabHostActivity extends Activity implements OnClickListener {
	//private RequestShopListThread myThread;
	private PopupWindow popupWindow;
	private Button btn_more;//--“更多”按钮
	private Button btn_home;//--返回主界面
	private Button btn_back;//--返回上一页
	private TabAdapter tabAdapter = null;
	private ShopAdapter shopAdapter;
	private String resultTree;
	private TextView content_title;
	private List<StoreInfo> shopList;
	private int position;
	private Button btn_return;
	private Context context = TabHostActivity.this;
	private ListView listView_tab;
	private int treeId = 0;
	private ListView listView_content;
	private List<ShopTree> list_tabObj;
	//private String[] tabText = new String[]{"全部分类","默认","口味","热门商圈"};
	private int[] iconId = new int[]{R.drawable.img_clock,R.drawable.img_clock,R.drawable.img_clock,R.drawable.img_clock};
	
	private Handler initila_handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// textView.setText("server端返回的数据是：\n" + s);
			// myThread.start();
			

			

		}

	};
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// textView.setText("server端返回的数据是：\n" + s);
			RequestShopListThread myThread = new RequestShopListThread();
			myThread.start();
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
			if (Contant.SHOPTREE_LIST != null) {
				list_tabObj = Contant.SHOPTREE_LIST;
			}
			if (list_tabObj!=null) {
				tabAdapter = new TabAdapter(context, list_tabObj);
				listView_tab.setAdapter(tabAdapter);
				listView_tab.setDivider(null);
				
			}
			else {
				Toast.makeText(context, "初始化分页分类失败", Toast.LENGTH_SHORT).show();
			}
			listView_tab.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// TODO Auto-generated method stub
					//Toast.makeText(context, ""+arg2, Toast.LENGTH_SHORT).show();
					position = arg2;
					Message msg = new Message();
					startThread_handler.sendMessage(msg);
					tabAdapter.setSelectedPosition(position);  
					tabAdapter.notifyDataSetInvalidated(); 
					Log.d("length"+list_tabObj.size(), "position-1:"+(arg2-1));
					treeId = list_tabObj.get(position).getId();
					
				}
			});

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
	
	private void setTab(ShopTree tree) {
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_classification);
		initialize();
	}
	
	private void initialize(){
		listView_tab = (ListView) findViewById(R.id.listView_tab);
		listView_content = (ListView) findViewById(R.id.listView_content);
		//list_tabObj = initialTabList();
		Message msg = new Message();
		startThread_handler.sendMessage(msg);
		
	
		content_title = (TextView) findViewById(R.id.textView_content_title);
		//content_title.setText(Contant.DEFAULTSORT);
		btn_return = (Button) findViewById(R.id.button_return);
		btn_return.setOnClickListener(TabHostActivity.this);
		btn_back = (Button) findViewById(R.id.button_back);
		btn_back.setOnClickListener(TabHostActivity.this);
		btn_home = (Button) findViewById(R.id.button_home);
		btn_home.setOnClickListener(TabHostActivity.this);
		btn_more = (Button) findViewById(R.id.button_more);
		btn_more.setOnClickListener(TabHostActivity.this);
		
	}
	
	
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if (v.equals(btn_return)) {
			treeId = 0;
			content_title.setText("");
			shopList = new ArrayList<StoreInfo>();
			shopAdapter = new ShopAdapter(context, shopList);
			listView_content.setAdapter(shopAdapter);
			//listView_content.setDivider(null);
			Message msg = new Message();
			startThread_handler.sendMessage(msg);
		}
		if (v.equals(btn_back)) {
			TabHostActivity.this.finish();
		}
		if (v.equals(btn_home)) {
			Intent intent = new Intent(TabHostActivity.this, TestMain.class);
			startActivity(intent);
			TabHostActivity.this.finish();
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
	}
	
	public class RequestShopTreeThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			getShopTree();
			getShopList();
			
		}
	}
	
	public class RequestShopListThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			if (shopList!=null&&shopList.size()>0) {
				for (int i = 0; i < shopList.size(); i++) {
					String resultJson;
					String opJson = "{\"id\":\"" + shopList.get(i).getId()
							+ "\"}";
					opJson = Client.encodeBase64(opJson);
					// String url =
					// "http://"+Contant.SERVER_IP+":"+Contant.SERVER_PORT+"/javadill/shop?id=s7&op="+opJson;
					// Log.d("url______________", url);
					String str = Tools.getRequestStr(Contant.SERVER_IP,
							Contant.SERVER_PORT + "", "shop?id=", "s7", "&op="
									+ opJson);

					resultJson = Client
							.executeHttpGetAndCheckNet(str, TabHostActivity.this);
					if (resultJson!=null) {
						resultJson = Client.decodeBase64(resultJson);
						JsonParser.parseShopInfoJson(resultJson,
								shopList.get(i));
						checkAllList(shopList.get(i));
					}
				}
				Message msg = new Message();
				refrashShopList_handler.sendMessage(msg);
				
			}
			
			//getShopList();
//			Message msg = new Message();
//			refrash_handler.sendMessage(msg);
		}
	}
	
	private Handler refrashShopList_handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			if (shopList!=null) {
				shopAdapter = new ShopAdapter(context, shopList);
				listView_content.setAdapter(shopAdapter);
				//listView_content.setDivider(null);
				ShopTree shopTree = getShopTreeById(treeId, Contant.SHOPTREE_LIST);
				String nameStr;
				if (shopTree==null) {
					nameStr = "";
				}
				else {
					nameStr = shopTree.getName();
				}
				content_title.setText(nameStr);
				listView_content.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						// TODO Auto-generated method stub
						// Log.d("00000000000", position+"");
						if (shopList != null && shopList.size() > 0) {

							Intent intent = new Intent(context,
									RestuarantInfoActivity.class);

							intent.putExtra("flag_from", "tabhost");
							// Bundle bundle = new Bundle();
							// bundle.putInt("shopId",list_store.get(position-1).getId());
							intent.putExtra("shopId", shopList.get(position)
									.getId());
							Log.d("shopid______________TabHostActivity",
									shopList.get(position).getId() + "");
							context.startActivity(intent);
						}
					}
				});
				
			}
		}

	};
	
	private ShopTree getShopTreeById(int id,List<ShopTree> list){
		ShopTree shopTree = null;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId()==id) {
				shopTree = list.get(i);
				break;
			}
		}
		return shopTree;
	}
	
	private void checkAllList(StoreInfo shop){
		boolean isContains = false;
		for (int i = 0; i < Contant.SHOP_LIST.size(); i++) {
			if (shop.getId()== Contant.SHOP_LIST.get(i).getId()) {
				isContains = true;
				break;
			}
		}
		if (isContains) {
			
		}
		else {
			Contant.SHOP_LIST.add(shop);
		}
	}
	
	private void getShopTree() {
		String resultJson;
		String opJson = "{\"Tid\":\"" + treeId + "\"}";
		opJson = Client.encodeBase64(opJson);
		String str = Tools.getRequestStr(Contant.SERVER_IP, Contant.SERVER_PORT
				+ "", "shop?id=", "s11", "&op=" + opJson);
		resultJson = Client.executeHttpGetAndCheckNet(str, this.context);
		resultJson = Client.decodeBase64(resultJson);
		resultTree = resultJson;
		if (resultJson != null) {
			if (JsonParser.checkError(resultJson)) {
				
			}
			else {
				Log.d("getShopTree=============", resultJson);
				Contant.SHOPTREE_LIST = JsonParser.parseShopTreeJson(resultJson);
				Message msg = new Message();
				refrash_handler.sendMessage(msg);			
			}
		}
	}
	
	
	private void getShopList() {
		String resultJson;
		String opJson = "{\"Tid\":\"" + treeId + "\"}";
		opJson = Client.encodeBase64(opJson);
		String str = Tools.getRequestStr(Contant.SERVER_IP, Contant.SERVER_PORT
				+ "", "shop?id=", "s12", "&op=" + opJson);
		resultJson = Client.executeHttpGetAndCheckNet(str, this.context);
		resultJson = Client.decodeBase64(resultJson);
		resultTree = resultJson;
		if (resultJson != null) {

			Log.d("getShopList=============", resultJson);
			if (JsonParser.checkError(resultJson)) {
				//Toast.makeText(context, resultJson, Toast.LENGTH_SHORT).show();
			} else {
				if (resultJson != null && !resultJson.equals("")) {
					shopList = JsonParser.parseShopIdJson(resultJson);
					//int flag = Contant.FLAG_GETSHOPBYID;
					Message msg = new Message();
					handler.sendMessage(msg);
				}
			}			 
			//Contant.SHOPTREE_LIST = JsonParser.parseShopTreeJson(resultJson);
		}
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