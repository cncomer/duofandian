package com.lnwoowken.lnwoowkenbook;



import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.cncom.app.base.ui.BaseActivity;
import com.cncom.app.base.util.DebugUtils;
import com.cncom.app.base.util.PatternInfoUtils;
import com.cncom.app.base.util.ShopInfoObject;
import com.lnwoowken.lnwoowkenbook.ServiceObject.ServiceResultObject;
import com.lnwoowken.lnwoowkenbook.adapter.ShopAdapter;
import com.lnwoowken.lnwoowkenbook.adapter.TabAdapter;
import com.lnwoowken.lnwoowkenbook.model.Contant;
import com.lnwoowken.lnwoowkenbook.model.ShopTree;
import com.lnwoowken.lnwoowkenbook.view.ProgressDialog;
import com.shwy.bestjoy.utils.AsyncTaskUtils;
import com.shwy.bestjoy.utils.NetworkUtils;

@SuppressLint("HandlerLeak")
@SuppressWarnings("unused")
public class TabHostActivity extends BaseActivity implements OnItemClickListener {
	private static final String TAG = "TabHostActivity";
	//private RequestShopListThread myThread;
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

	private int mCurrentMode;
	
	private static final int PINPAI = 0;
	private static final int SHANGQUAN = 1;
	private static final int CAIXI = 2;
	private static final int XINGZHENGQU = 3;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
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
						if(position == PINPAI) {
							//品牌
							loadInfoAsyncTask(PINPAI);
						} else if (position == SHANGQUAN) {
							//商圈
							loadInfoAsyncTask(SHANGQUAN);
						} else if (position == CAIXI) {
							//菜系
							loadInfoAsyncTask(CAIXI);
						} else if (position == XINGZHENGQU) {
							//行政区
							//loadInfoAsyncTask(XINGZHENGQU);
						}
						mCurrentLevel = LEVEL_SECOND;
						btn_return.setText(R.string.back_level);
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
		super.onClick(v);
	}
	
	private LoadInfoAsyncTask mLoadInfoAsyncTask;
	private void loadInfoAsyncTask(Integer... param) {
		showProgressDialog();
		AsyncTaskUtils.cancelTask(mLoadInfoAsyncTask);
		mLoadInfoAsyncTask = new LoadInfoAsyncTask();
		mLoadInfoAsyncTask.execute(param);
	}

	private class LoadInfoAsyncTask extends AsyncTask<Integer, Void, ServiceResultObject> {
		@Override
		protected ServiceResultObject doInBackground(Integer... params) {
			//更新保修卡信息
			ServiceResultObject serviceResultObject = new ServiceResultObject();
			InputStream is = null;
			try {
				switch (params[0]) {
					case PINPAI:
						mCurrentMode = PINPAI;
						is = NetworkUtils.openContectionLocked(ServiceObject.getPinpaiUrl(), null);
						serviceResultObject = ServiceResultObject.parsePinpaiInfo(NetworkUtils.getContentFromInput(is));
						break;
					case SHANGQUAN:
						mCurrentMode = SHANGQUAN;
						is = NetworkUtils.openContectionLocked(ServiceObject.getShangquanUrl(), null);
						serviceResultObject = ServiceResultObject.parseShangquanInfo(NetworkUtils.getContentFromInput(is));
						break;
					case CAIXI:
						mCurrentMode = CAIXI;
						is = NetworkUtils.openContectionLocked(ServiceObject.getCaixiUrl(), null);
						serviceResultObject = ServiceResultObject.parseCaixiInfo(NetworkUtils.getContentFromInput(is));
						break;
					case XINGZHENGQU:
						mCurrentMode = XINGZHENGQU;
						is = NetworkUtils.openContectionLocked(ServiceObject.getXingzhengquUrl(), null);
						serviceResultObject = ServiceResultObject.parsePinpaiInfo(NetworkUtils.getContentFromInput(is));
						break;
				}
				DebugUtils.logD(TAG, "mShopsList = " + serviceResultObject.mShops);
				DebugUtils.logD(TAG, "StatusCode = " + serviceResultObject.mStatusCode);
				DebugUtils.logD(TAG, "StatusMessage = " + serviceResultObject.mStatusMessage);
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

				switch (mCurrentMode) {
					case PINPAI:
						mTabAdapter.updateTabList(PatternInfoUtils.getPinpaiList(result.mShops));
						break;
					case SHANGQUAN:
						mTabAdapter.updateTabList(PatternInfoUtils.getShangquanList(result.mShops));
						break;
					case CAIXI:
						mTabAdapter.updateTabList(PatternInfoUtils.getCaixiList(result.mShops));
						break;
					case XINGZHENGQU:
						mTabAdapter.updateTabList(PatternInfoUtils.getPinpaiList(result.mShops));
						break;
				}
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
				is = NetworkUtils.openContectionLocked(getTabUrl(params[0]), null);
				serviceResultObject = ServiceResultObject.parsePinpaiShops(NetworkUtils.getContentFromInput(is));
				DebugUtils.logD(TAG, "mShopsList = " + serviceResultObject.mShops);
				DebugUtils.logD(TAG, "StatusCode = " + serviceResultObject.mStatusCode);
				DebugUtils.logD(TAG, "StatusMessage = " + serviceResultObject.mStatusMessage);
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

		private String getTabUrl(String json) {
			String url = null;
			switch (mCurrentMode) {
				case PINPAI:
					url = ServiceObject.getShopByPinpaiUrl("para", getJsonObject(json).toString());
					break;
				case SHANGQUAN:
					url = ServiceObject.getShangquanUrl("para", getJsonObject(json).toString());
					break;
				case CAIXI:
					url = ServiceObject.getShangquanUrl("para", getJsonObject(json).toString());
					break;
				case XINGZHENGQU:
					url = ServiceObject.getShopByPinpaiUrl("para", getJsonObject(json).toString());
					break;
			}
			return url;
		}

		private Object getJsonObject(String value) {
			JSONObject queryJsonObject = new JSONObject();
			try {
				switch (mCurrentMode) {
					case PINPAI:
						queryJsonObject.put("brandname", value);
						break;
					case SHANGQUAN:
						queryJsonObject.put("shangquan", value);
						break;
					case CAIXI:
						queryJsonObject.put("caixi", value);
						break;
					case XINGZHENGQU:
						queryJsonObject.put("brandname", value);
						break;
					}
				} catch (JSONException e) {
				}
			return queryJsonObject;
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
	
	
	public static void startActivity(Context context) {
		Intent intent = new Intent(context, TabHostActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected boolean checkIntent(Intent intent) {
		return true;
	}
}