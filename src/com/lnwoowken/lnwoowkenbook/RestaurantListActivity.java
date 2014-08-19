package com.lnwoowken.lnwoowkenbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullAndLoadListView.OnLoadMoreListener;
import com.costum.android.widget.PullToRefreshListView.OnRefreshListener;
import com.lnwoowken.lnwoowkenbook.ServiceObject.ServiceResultObject;
import com.lnwoowken.lnwoowkenbook.model.Contant;
import com.lnwoowken.lnwoowkenbook.model.StoreInfo;
import com.lnwoowken.lnwoowkenbook.view.ProgressDialog;
import com.shwy.bestjoy.utils.AsyncTaskUtils;
import com.shwy.bestjoy.utils.NetworkUtils;

/**
 * 店家信息的界面
 * 
 * @author chs
 * 
 */
public class RestaurantListActivity extends Activity implements OnClickListener {
	private static final String TAG = "RestaurantListActivity";
	private AnimationDrawable draw;
    private	LinearLayout progress;
	private Button btn_search;
	private EditText search;
	private Button btn_home;
	private PopupWindow popupWindow;
	private Button btn_sort;
	private Context context = RestaurantListActivity.this;
	private ImageButton btn_back;
	private Button btn_more;
	private Dialog dialog;
	private PullAndLoadListView mShopListView;
	private ShopListAdapter mShopListAdapter;
	private static final int STATE_IDLE = 0;
	private static final int STATE_FREASHING = STATE_IDLE + 1;
	private static final int STATE_FREASH_COMPLETE = STATE_FREASHING + 1;
	private static final int STATE_FREASH_CANCEL = STATE_FREASH_COMPLETE + 1;
	private int mLoadState = STATE_IDLE;
	private int mLoadPageIndex = 0;
	private List <ShopInfoObject> mShopsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_list);

		mShopListView = (PullAndLoadListView) findViewById(R.id.listView_all_store);
		mShopsList = new ArrayList<ShopInfoObject>();
		mShopListAdapter = new ShopListAdapter(context, mShopsList);
		initialize();

		loadAllShopInfoAsyncTask();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void initialize() {
		btn_back = (ImageButton) findViewById(R.id.imageButton_back);
		btn_back.setOnClickListener(RestaurantListActivity.this);
		btn_search = (Button) findViewById(R.id.button_search);
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
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(TextUtils.isEmpty(search.getText().toString().trim())) {
					loadAllShopInfoAsyncTask();
				}
				//mShopListAdapter.initShopList(searchShopByName(search.getText().toString().trim()));
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}

		});
		mShopListView.setAdapter(mShopListAdapter);
		mShopListView.setOnItemClickListener(mShopListAdapter);
		mShopListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                GetDataTask();
            }
        });
		mShopListView.setOnLoadMoreListener(new OnLoadMoreListener() {
			
			public void onLoadMore() {
				// Do the work to load more items at the end of list here.
				LoadMoreDataTask();
			}
		});
	}

	@Override
	public void onClick(View v) {
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
				View view = LayoutInflater.from(context).inflate(R.layout.popmenu, null);
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
			if (popupWindow!=null) {
				popupWindow.dismiss();
				popupWindow = null;
			}
			MainActivity.startIntentClearTop(context, null);
			RestaurantListActivity.this.finish();
		}
		if (v.equals(btn_search)) {
			loadShopInfoByNameAsyncTask(search.getText().toString().trim());
			//mShopListAdapter.initShopList(searchShopByName(search.getText().toString().trim()));
		}
	}

	private List<ShopInfoObject> searchShopByName(String searchStr) {
		List <ShopInfoObject> searchList = new ArrayList<ShopInfoObject>(); 
		if (mShopsList != null && !TextUtils.isEmpty(searchStr)) {
			for (int i = 0; i < mShopsList.size(); i++) {
				if (searchStr.equals(mShopsList.get(i).getShopName())||
						mShopsList.get(i).getShopName().contains(searchStr)) {
					searchList.add(mShopsList.get(i));
					Log.d(TAG, mShopsList.get(i).getShopName());
				}
			}
			if (popupWindow!=null) {
				popupWindow.dismiss();
				popupWindow = null;
			}
		}
		if(TextUtils.isEmpty(searchStr)) {
			return mShopsList;
		}
		return searchList;
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
						Toast.makeText(context, "成功退出登录", Toast.LENGTH_SHORT).show();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).create();
		alertDialog.show();
	}

	private void dismissProgressDialog(){
		if(dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
	}
	
	private void showProgressDialog(){
		if(dialog != null && dialog.isShowing()) return; 
		dialog = new ProgressDialog(RestaurantListActivity.this,
				R.style.ProgressDialog);
		dialog.show();
		progress = (LinearLayout) dialog.findViewById(R.id.imageView_progress);
	
		progress.setBackgroundResource(R.anim.animition_progress); 
        draw = (AnimationDrawable)progress.getBackground(); 
        draw.start();
        dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface arg0) {
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
	


	public class ShopListAdapter extends BaseAdapter implements ListView.OnItemClickListener{
		private Context _context;
		private List<ShopInfoObject> _shoplist;
		public ShopListAdapter (Context context, List<ShopInfoObject> shoplist) {
			_context = context;
			_shoplist = shoplist;
		}
		public void initShopList(List<ShopInfoObject> shoplist) {
			_shoplist = shoplist;
			notifyDataSetChanged();
		}
		@Override
		public int getCount() {
			return _shoplist != null ? _shoplist.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(_context).inflate(R.layout.listview_shop_item, parent, false);
				holder = new ViewHolder();
				holder._name = (TextView) convertView.findViewById(R.id.textView_storename);
				holder._tablenum = (TextView) convertView.findViewById(R.id.textView_tablenumber);
				holder._favorate = (TextView) convertView.findViewById(R.id.textView_favorate);
				holder._price = (TextView) convertView.findViewById(R.id.textView_price);
				holder._detail = (TextView) convertView.findViewById(R.id.textView_position);
				holder._distance = (TextView) convertView.findViewById(R.id.textView_distance);
				holder._youhui = (ImageView) convertView.findViewById(R.id.imageView_hui);
				holder._tuangou = (ImageView) convertView.findViewById(R.id.imageView_tuan);
				holder._diancan = (ImageView) convertView.findViewById(R.id.imageView_dian);
				holder._maidian = (ImageView) convertView.findViewById(R.id.imageView_mai);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			/*Float f = 0f;
			if(!TextUtils.isEmpty((_shoplist.get(position).getMaintenancePointDistance()))) {
				f = Float.valueOf(_shoplist.get(position).getMaintenancePointDistance()) / 1000;
			}*/
			holder._name.setText(_shoplist.get(position).getShopName());
			holder._tablenum.setText(_shoplist.get(position).getShopDeskCount());
			holder._price.setText(_shoplist.get(position).getShopRenJun());
			holder._detail.setText(_shoplist.get(position).getShopBrief());
			String n = _shoplist.get(position).getShopName();
			String a = _shoplist.get(position).getShopTuanGou();
			ShopInfoObject b = _shoplist.get(position);
			if(!TextUtils.isEmpty(_shoplist.get(position).getShopYouHui())) holder._youhui.setVisibility(View.VISIBLE); else holder._youhui.setVisibility(View.GONE);
			if(!TextUtils.isEmpty(_shoplist.get(position).getShopTuanGou())) holder._tuangou.setVisibility(View.VISIBLE); else holder._tuangou.setVisibility(View.GONE);
			if(!TextUtils.isEmpty(_shoplist.get(position).getShopDianCan())) holder._diancan.setVisibility(View.VISIBLE); else holder._diancan.setVisibility(View.GONE);
			if(!TextUtils.isEmpty(_shoplist.get(position).getShopMaiDian())) holder._maidian.setVisibility(View.VISIBLE); else holder._maidian.setVisibility(View.GONE);
			/*final int pos = position;
			holder._phone.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					String tel = _shoplist.get(pos).getMaintenancePointTel();
					if(!TextUtils.isEmpty(tel)) {
						Intents.callPhone(getActivity(), tel);						
					} else {
						MyApplication.getInstance().showMessage(R.string.no_tel_tips);
					}
				}
			});*/
			return convertView;
		}

		private class ViewHolder {
			private TextView _name, _tablenum, _favorate, _price, _detail, _distance;
			private ImageView _youhui, _tuangou, _diancan, _maidian;
		}

		@Override
		public void onItemClick(AdapterView<?> listView, View view, int pos, long arg3) {
			if(mLoadState == STATE_FREASHING) return;
			Bundle bundle = new Bundle();
			bundle.putString("shop_id", _shoplist.get(pos-1).getShopID());
			RestuarantInfoActivity.startIntent(_context, bundle);
		}
	}

	//refresh data begin
	private RefreshShopInfoAsyncTask mRefreshShopInfoAsyncTask;
	private void GetDataTask(String... param) {
		mLoadState = STATE_FREASHING;
		AsyncTaskUtils.cancelTask(mRefreshShopInfoAsyncTask);
		mRefreshShopInfoAsyncTask = new RefreshShopInfoAsyncTask();
		mRefreshShopInfoAsyncTask.execute(param);
	}

	private class RefreshShopInfoAsyncTask extends AsyncTask<String, Void, ServiceResultObject> {
		@Override
		protected ServiceResultObject doInBackground(String... params) {
			ServiceResultObject serviceResultObject = new ServiceResultObject();
			InputStream is = null;

			try {
				JSONObject queryJsonObject = new JSONObject();
				queryJsonObject.put("pageindex", 1);
				is = NetworkUtils.openContectionLocked(ServiceObject.getAllShopInfoUrl("para", queryJsonObject.toString()), null);
				serviceResultObject = ServiceResultObject.parseShops(NetworkUtils.getContentFromInput(is));
				mShopsList = PatternInfoUtils.getShopInfoClean(serviceResultObject.mShops, getContentResolver());

				DebugUtils.logD(TAG, "mShopsList = " + mShopsList);
				DebugUtils.logD(TAG, "StatusCode = " + serviceResultObject.mStatusCode);
				DebugUtils.logD(TAG, "StatusMessage = " + serviceResultObject.mStatusMessage);
				if (serviceResultObject.isOpSuccessfully()) {
					String data = serviceResultObject.mStrData;
					DebugUtils.logD(TAG, "Data = " + data);
				}
			} catch (JSONException e) {
				DebugUtils.logD(TAG, "JSONException = " + e);
				e.printStackTrace();
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
			} else {
				mLoadPageIndex = 1;
			}
			mShopListView.onRefreshComplete();
			mShopListAdapter.initShopList(mShopsList);
			mLoadState = STATE_FREASH_COMPLETE;
			DebugUtils.logD(TAG, "huasong onPostExecute onLoadMoreComplete");
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			mShopListView.onRefreshComplete();
			mShopListAdapter.initShopList(mShopsList);
			mLoadState = STATE_FREASH_CANCEL;
		}
	}
	//refresh data end
	
	//load more data begin
	private LoadMoreShopInfoAsyncTask mLoadMoreShopInfoAsyncTask;
	private void LoadMoreDataTask(String... param) {
		mLoadState = STATE_FREASHING;
		AsyncTaskUtils.cancelTask(mLoadMoreShopInfoAsyncTask);
		mLoadMoreShopInfoAsyncTask = new LoadMoreShopInfoAsyncTask();
		mLoadMoreShopInfoAsyncTask.execute(param);
	}

	private class LoadMoreShopInfoAsyncTask extends AsyncTask<String, Void, ServiceResultObject> {
		@Override
		protected ServiceResultObject doInBackground(String... params) {
			//更新保修卡信息
			ServiceResultObject serviceResultObject = new ServiceResultObject();
			InputStream is = null;
			
			try {
				JSONObject queryJsonObject = new JSONObject();
				queryJsonObject.put("pageindex", ++mLoadPageIndex);
				is = NetworkUtils.openContectionLocked(ServiceObject.getAllShopInfoUrl("para", queryJsonObject.toString()), null);
				serviceResultObject = ServiceResultObject.parseShops(NetworkUtils.getContentFromInput(is));
				mShopsList.addAll(PatternInfoUtils.getShopInfo(serviceResultObject.mShops, getContentResolver()));

				DebugUtils.logD(TAG, "mShopsList = " + mShopsList);
				DebugUtils.logD(TAG, "StatusCode = " + serviceResultObject.mStatusCode);
				DebugUtils.logD(TAG, "StatusMessage = " + serviceResultObject.mStatusMessage);
			} catch (JSONException e) {
				DebugUtils.logD(TAG, "JSONException = " + e);
				e.printStackTrace();
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
			mShopListView.onLoadMoreComplete();
			mShopListAdapter.initShopList(mShopsList);
			mLoadState = STATE_FREASH_COMPLETE;
			DebugUtils.logD(TAG, "huasong onPostExecute onLoadMoreComplete");
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			mShopListView.onLoadMoreComplete();
			mShopListAdapter.initShopList(mShopsList);
			mLoadState = STATE_FREASH_CANCEL;
		}
	}
	//load more data end
	
	
	private LoadAllShopInfoAsyncTask mLoadAllShopInfoAsyncTask;
	private void loadAllShopInfoAsyncTask(String... param) {
		int locatCount = PatternInfoUtils.getShopsDataCount(getContentResolver());
		if(locatCount > 0 && false){//本地已有缓存
			mShopsList = PatternInfoUtils.getShopInfoLocal(getContentResolver());
			mShopListAdapter.initShopList(mShopsList);
			mLoadPageIndex = locatCount / 10 + 1;
		} else {
			showProgressDialog();
			mLoadState = STATE_FREASHING;
			AsyncTaskUtils.cancelTask(mLoadAllShopInfoAsyncTask);
			mLoadAllShopInfoAsyncTask = new LoadAllShopInfoAsyncTask();
			mLoadAllShopInfoAsyncTask.execute(param);
		}
	}

	private class LoadAllShopInfoAsyncTask extends AsyncTask<String, Void, ServiceResultObject> {
		@Override
		protected ServiceResultObject doInBackground(String... params) {
			//更新保修卡信息
			ServiceResultObject serviceResultObject = new ServiceResultObject();
			InputStream is = null;
			try {
				JSONObject queryJsonObject = new JSONObject();
				queryJsonObject.put("pageindex", 1);
				is = NetworkUtils.openContectionLocked(ServiceObject.getAllShopInfoUrl("para", queryJsonObject.toString()), null);
				serviceResultObject = ServiceResultObject.parseShops(NetworkUtils.getContentFromInput(is));
				mShopsList = PatternInfoUtils.getShopInfo(serviceResultObject.mShops, getContentResolver());
				DebugUtils.logD(TAG, "mShopsList = " + mShopsList);
				DebugUtils.logD(TAG, "StatusCode = " + serviceResultObject.mStatusCode);
				DebugUtils.logD(TAG, "StatusMessage = " + serviceResultObject.mStatusMessage);
				if (serviceResultObject.isOpSuccessfully()) {
					String data = serviceResultObject.mStrData;
					DebugUtils.logD(TAG, "Data = " + data);
				}
			} catch (JSONException e) {
				DebugUtils.logD(TAG, "JSONException = " + e);
				e.printStackTrace();
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
			} else {
				mLoadPageIndex = 1;
			}
			mLoadState = STATE_FREASH_COMPLETE;
			mShopListAdapter.initShopList(mShopsList);
			dismissProgressDialog();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			mLoadState = STATE_FREASH_CANCEL;
			mShopListAdapter.initShopList(mShopsList);
			dismissProgressDialog();
		}
	}
	
	private LoadShopInfoByNameAsyncTask mLoadShopInfoByNameAsyncTask;
	private void loadShopInfoByNameAsyncTask(String... param) {
		if(TextUtils.isEmpty(param[0])) return;
		showProgressDialog();
		mLoadState = STATE_FREASHING;
		AsyncTaskUtils.cancelTask(mLoadShopInfoByNameAsyncTask);
		mLoadShopInfoByNameAsyncTask = new LoadShopInfoByNameAsyncTask();
		mLoadShopInfoByNameAsyncTask.execute(param);
	}

	private class LoadShopInfoByNameAsyncTask extends AsyncTask<String, Void, ServiceResultObject> {
		@Override
		protected ServiceResultObject doInBackground(String... params) {
			//更新保修卡信息
			ServiceResultObject serviceResultObject = new ServiceResultObject();
			InputStream is = null;
			try {
				JSONObject queryJsonObject = new JSONObject();
				queryJsonObject.put("condition", params[0]);
				is = NetworkUtils.openContectionLocked(ServiceObject.getAllShopInfoUrl("para", queryJsonObject.toString()), null);
				serviceResultObject = ServiceResultObject.parseShops(NetworkUtils.getContentFromInput(is));
				DebugUtils.logD(TAG, "mShopsList = " + mShopsList);
				DebugUtils.logD(TAG, "StatusCode = " + serviceResultObject.mStatusCode);
				DebugUtils.logD(TAG, "StatusMessage = " + serviceResultObject.mStatusMessage);
				if (serviceResultObject.isOpSuccessfully()) {
					String data = serviceResultObject.mStrData;
					DebugUtils.logD(TAG, "Data = " + data);
				}
			} catch (JSONException e) {
				DebugUtils.logD(TAG, "JSONException = " + e);
				e.printStackTrace();
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
			} else {
				mLoadPageIndex = 1;
			}
			mLoadState = STATE_FREASH_COMPLETE;
			try {
				mShopListAdapter.initShopList(PatternInfoUtils.getShopInfo(result.mShops));
			} catch (JSONException e) {
			}
			dismissProgressDialog();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			mLoadState = STATE_FREASH_CANCEL;
			mShopListAdapter.initShopList(mShopsList);
			dismissProgressDialog();
		}
	}
}
