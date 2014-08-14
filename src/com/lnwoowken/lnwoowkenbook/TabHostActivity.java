package com.lnwoowken.lnwoowkenbook;



import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cncom.app.base.util.DebugUtils;
import com.cncom.app.base.util.PatternInfoUtils;
import com.cncom.app.base.util.ShopInfoObject;
import com.lnwoowken.lnwoowkenbook.ServiceObject.ServiceResultObject;
import com.lnwoowken.lnwoowkenbook.adapter.ShopAdapter;
import com.lnwoowken.lnwoowkenbook.adapter.TabAdapter;
import com.lnwoowken.lnwoowkenbook.model.Contant;
import com.lnwoowken.lnwoowkenbook.model.ShopTree;
import com.lnwoowken.lnwoowkenbook.model.StoreInfo;
import com.lnwoowken.lnwoowkenbook.view.ProgressDialog;
import com.shwy.bestjoy.utils.AsyncTaskUtils;
import com.shwy.bestjoy.utils.NetworkUtils;

@SuppressLint("HandlerLeak")
@SuppressWarnings("unused")
public class TabHostActivity extends Activity implements OnClickListener, OnItemClickListener {
	private static final String TAG = "TabHostActivity";
	//private RequestShopListThread myThread;
	private PopupWindow popupWindow;
	private Button btn_more;//--“更多”按钮
	private Button btn_home;//--返回主界面
	private Button btn_back;//--返回上一页
	private TabAdapter mTabAdapter = null;
	private ShopAdapter mShopListAdapter;
	private String resultTree;
	private TextView content_title;
	private List<ShopInfoObject> mShopList;
	private int position;
	private Button btn_return;
	private Context context = TabHostActivity.this;
	private ListView listView_tab;
	private String mSelectTree;
	private ListView listView_content;
	private List<ShopTree> list_tabObj;
	private Dialog dialog;
	private int[] iconId = new int[]{R.drawable.img_clock,R.drawable.img_clock,R.drawable.img_clock,R.drawable.img_clock};
	
	private static final int LEVEL_FIRST = 1;
	private static final int LEVEL_SECOND = LEVEL_FIRST + 1;
	private static final int LEVEL_THIRD = LEVEL_SECOND + 1;
	//当前为第一级
	private int mCurrentLevel = LEVEL_FIRST;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_classification);
		initialize();
	}
	
	private void initialize(){
		listView_tab = (ListView) findViewById(R.id.listView_tab);
		listView_content = (ListView) findViewById(R.id.listView_content);
	
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
		
		mTabAdapter = new TabAdapter(context);
		listView_tab.setAdapter(mTabAdapter);
		listView_tab.setOnItemClickListener(this);

		mShopList = new ArrayList<ShopInfoObject>();
		mShopListAdapter = new ShopAdapter(context, mShopList);
		listView_content.setAdapter(mShopListAdapter);
		listView_content.setOnItemClickListener(this);
		mTabAdapter.initTabList();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (parent.getId()) {
			case R.id.listView_tab://左边的导航栏
				switch (mCurrentLevel) {
					case LEVEL_FIRST:
						//品牌
						if(position == 0) {
							loadInfoAsyncTask();	
							mCurrentLevel = LEVEL_SECOND;
							btn_return.setText(R.string.back_level);
						}
						break;
					case LEVEL_SECOND:
						String title = mTabAdapter.getItem(position);
						loadShopInfoAsyncTask(title);	
						content_title.setText(title);				
						break;
				}
				break;
			}
	}
	
	@Override
	public void onClick(View v) {
		if (v.equals(btn_return)) {
			mSelectTree = null;
			//content_title.setText("");
			//mShopList.clear();
			//mShopListAdapter.notifyDataSetChanged();
			mTabAdapter.initTabList();
			mCurrentLevel = LEVEL_FIRST;
			btn_return.setText(R.string.first_level);
		}
		if (v.equals(btn_back)) {
			TabHostActivity.this.finish();
		}
		if (v.equals(btn_home)) {
			MainActivity.startIntentClearTop(context, null);
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
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
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
				// setIcon(R.drawable.welcome_logo).
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
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
					}
				}).

				create();
		alertDialog.show();
	}

	private LoadInfoAsyncTask mLoadInfoAsyncTask;
	private void loadInfoAsyncTask(String... param) {
		showProgressDialog();
		AsyncTaskUtils.cancelTask(mLoadInfoAsyncTask);
		mLoadInfoAsyncTask = new LoadInfoAsyncTask();
		mLoadInfoAsyncTask.execute(param);
	}

	private class LoadInfoAsyncTask extends AsyncTask<String, Void, ServiceResultObject> {
		@Override
		protected ServiceResultObject doInBackground(String... params) {
			//更新保修卡信息
			ServiceResultObject serviceResultObject = new ServiceResultObject();
			InputStream is = null;
			try {
				is = NetworkUtils.openContectionLocked(ServiceObject.getPinpaiUrl(), null);
				serviceResultObject = ServiceResultObject.parsePinpai(NetworkUtils.getContentFromInput(is));
				DebugUtils.logD(TAG, "mShopsList = " + serviceResultObject.mShops);
				DebugUtils.logD(TAG, "StatusCode = " + serviceResultObject.mStatusCode);
				DebugUtils.logD(TAG, "StatusMessage = " + serviceResultObject.mStatusMessage);
				if (serviceResultObject.isOpSuccessfully()) {
					String data = serviceResultObject.mStrData;
					DebugUtils.logD(TAG, "Data = " + data);
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
			} catch (IOException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
			}finally {
				NetworkUtils.closeInputStream(is);
			}
			return serviceResultObject;
		}

		@Override
		protected void onPostExecute(ServiceResultObject result) {
			super.onPostExecute(result);
			if(result.mShops == null || result.mShops.length() == 0) {
				MyApplication.getInstance().showMessage(R.string.shop_info_query_fail);
			}
			try {
				mTabAdapter.updateTabList(PatternInfoUtils.getPinpaiList(result.mShops));
			} catch (JSONException e) {
			}
			dismissProgressDialog();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			dismissProgressDialog();
		}
	}
	
	

	private LoadShopInfoAsyncTask mLoadShopInfoAsyncTask;
	private void loadShopInfoAsyncTask(String... param) {
		showProgressDialog();
		AsyncTaskUtils.cancelTask(mLoadShopInfoAsyncTask);
		mLoadShopInfoAsyncTask = new LoadShopInfoAsyncTask();
		mLoadShopInfoAsyncTask.execute(param);
	}

	private class LoadShopInfoAsyncTask extends AsyncTask<String, Void, ServiceResultObject> {
		@Override
		protected ServiceResultObject doInBackground(String... params) {
			ServiceResultObject serviceResultObject = new ServiceResultObject();
			InputStream is = null;
			try {
				JSONObject queryJsonObject = new JSONObject();
				queryJsonObject.put("brandname", params[0]);
				is = NetworkUtils.openContectionLocked(ServiceObject.getShopByPinpaiUrl("para", queryJsonObject.toString()), null);
				serviceResultObject = ServiceResultObject.parsePinpaiShops(NetworkUtils.getContentFromInput(is));
				DebugUtils.logD(TAG, "mShopsList = " + serviceResultObject.mShops);
				DebugUtils.logD(TAG, "StatusCode = " + serviceResultObject.mStatusCode);
				DebugUtils.logD(TAG, "StatusMessage = " + serviceResultObject.mStatusMessage);
			} catch (JSONException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
			} catch (IOException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
			}finally {
				NetworkUtils.closeInputStream(is);
			}
			return serviceResultObject;
		}

		@Override
		protected void onPostExecute(ServiceResultObject result) {
			super.onPostExecute(result);
			if(result.mShops == null || result.mShops.length() == 0) {
				MyApplication.getInstance().showMessage(R.string.shop_info_query_fail);
			}
			try {
				mShopListAdapter.updateShopList(PatternInfoUtils.getShopInfo(result.mShops));
			} catch (JSONException e) {
			}
			dismissProgressDialog();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			dismissProgressDialog();
		}
	}
	

	private void dismissProgressDialog(){
		if(dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
	}
	
	private void showProgressDialog(){
		if(dialog != null && dialog.isShowing()) return; 
		dialog = new ProgressDialog(TabHostActivity.this, R.style.ProgressDialog);
		dialog.show();
		LinearLayout progress = (LinearLayout) dialog.findViewById(R.id.imageView_progress);
		progress.setBackgroundResource(R.anim.animition_progress); 
		final AnimationDrawable draw = (AnimationDrawable)progress.getBackground(); 
        draw.start();
        dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface arg0) {
				draw.stop();
			}
		});
		dialog.setCanceledOnTouchOutside(false);
	}
}