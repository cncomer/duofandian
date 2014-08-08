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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
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

import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.util.DebugUtils;
import com.cncom.app.base.util.PatternShopInfoUtils;
import com.cncom.app.base.util.ShopInfoObject;
import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullAndLoadListView.OnLoadMoreListener;
import com.costum.android.widget.PullToRefreshListView.OnRefreshListener;
import com.lnwoowken.lnwoowkenbook.ServiceObject.ServiceResultObject;
import com.lnwoowken.lnwoowkenbook.model.Contant;
import com.lnwoowken.lnwoowkenbook.model.StoreInfo;
import com.lnwoowken.lnwoowkenbook.thread.RequestServerThread;
import com.lnwoowken.lnwoowkenbook.view.ProgressDialog;
import com.shwy.bestjoy.utils.AsyncTaskUtils;
import com.shwy.bestjoy.utils.NetworkUtils;

/**
 * 店家信息的界面
 * 
 * @author sean
 * 
 */
@SuppressLint("HandlerLeak")
@SuppressWarnings("unused")
public class RestaurantListActivity extends Activity implements OnClickListener {
	private static final String TAG = "RestaurantListActivity";
	private AnimationDrawable draw;
    private	LinearLayout progress;
	private Intent intent;
	private List<StoreInfo> listStore;
	private ImageButton btn_search;
	private EditText search;
	private Button btn_home;
	private PopupWindow popupWindow;
	private Button btn_sort;
	private ListView mShopListView;
	private Context context = RestaurantListActivity.this;
	private ImageButton btn_back;
	private RequestServerThread myThread;
	private Button btn_more;
	private Dialog dialog;
	private PullAndLoadListView mShopInfoListView;
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

		int flag = Contant.FLAG_GETSHOPBYID;
		mShopInfoListView = (PullAndLoadListView) findViewById(R.id.listView_all_store);
		mShopsList = new ArrayList<ShopInfoObject>();
		mShopListAdapter = new ShopListAdapter(context);
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



	private void listViewDataChanged(){
		
	}

	private void initialize() {
		intent = getIntent();
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
		mShopInfoListView.setAdapter(mShopListAdapter);
		mShopInfoListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                GetDataTask();
            }
        });
		mShopInfoListView.setOnLoadMoreListener(new OnLoadMoreListener() {
			
			public void onLoadMore() {
				// Do the work to load more items at the end of list
				// here
				LoadMoreDataTask();
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
				mShopListView.invalidate();
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

	private void dismissProgressDialog(){
		if(dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
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
	


	public class ShopListAdapter extends BaseAdapter implements ListView.OnItemClickListener{

		private Context _context;
		private ShopListAdapter (Context context) {
			_context = context;
		}
		@Override
		public int getCount() {
			return mShopsList != null ? mShopsList.size() : 0;
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
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			/*Float f = 0f;
			if(!TextUtils.isEmpty((mShopsList.get(position).getMaintenancePointDistance()))) {
				f = Float.valueOf(mShopsList.get(position).getMaintenancePointDistance()) / 1000;
			}*/
			holder._name.setText(mShopsList.get(position).getShopName());
			//holder._tablenum.setText(mShopsList.get(position).getMaintenancePointDetail());
			//holder._favorate.setText(String.format("%.1f", f) + _context.getResources().getString(R.string.maintence_point_distance_unit));
			holder._price.setText(mShopsList.get(position).getShopServerprice());
			holder._detail.setText(mShopsList.get(position).getShopBrief());
			//holder._distance.setText(mShopsList.get(position).getMaintenancePointName());
			holder._youhui.setImageResource(R.drawable.icon_hui);
			/*final int pos = position;
			holder._phone.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					String tel = mShopsList.get(pos).getMaintenancePointTel();
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
			private ImageView _youhui;
		}

		@Override
		public void onItemClick(AdapterView<?> listView, View view, int pos, long arg3) {
			if(mLoadState == STATE_FREASHING) return;
			/*String url = mShopsList.get(pos-1).getMaintenancePointUrl();
			if(!TextUtils.isEmpty(url)) {				
				BrowserActivity.startActivity(_context, url, _context.getString(R.string.repair_point_detail));
			} else {
				MyApplication.getInstance().showMessage(R.string.repair_point_detail_no_uri_tips);
			}*/
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
				queryJsonObject.put("pagesize", 11);
				queryJsonObject.put("pageindex", 1);
				is = NetworkUtils.openContectionLocked(ServiceObject.getAllShopInfoUrl("para", queryJsonObject.toString()), null);
				serviceResultObject = ServiceResultObject.parseShops(NetworkUtils.getContentFromInput(is));
				mShopsList = PatternShopInfoUtils.getShopInfoClean(serviceResultObject.mShops, getContentResolver());
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

			mShopListAdapter.notifyDataSetChanged();
			if(result.mShops == null || result.mShops.length() == 0) {
				MyApplication.getInstance().showMessage(R.string.shop_info_query_fail);
			} else {
				mLoadPageIndex = 1;
			}
			mShopInfoListView.onRefreshComplete();
			mLoadState = STATE_FREASH_COMPLETE;
			DebugUtils.logD(TAG, "huasong onPostExecute onLoadMoreComplete");
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			mShopInfoListView.onRefreshComplete();
			mLoadState = STATE_FREASH_CANCEL;
		}
	}
	//refresh data end
	
	//load more data begin
	private LoadMoreNearestPointAsyncTask mLoadMoreNearestPointAsyncTask;
	private void LoadMoreDataTask(String... param) {
		mLoadState = STATE_FREASHING;
		AsyncTaskUtils.cancelTask(mLoadMoreNearestPointAsyncTask);
		mLoadMoreNearestPointAsyncTask = new LoadMoreNearestPointAsyncTask();
		mLoadMoreNearestPointAsyncTask.execute(param);
	}

	private class LoadMoreNearestPointAsyncTask extends AsyncTask<String, Void, ServiceResultObject> {
		@Override
		protected ServiceResultObject doInBackground(String... params) {
			if(mLoadPageIndex == 0) return null;
			//更新保修卡信息
			ServiceResultObject serviceResultObject = new ServiceResultObject();
			InputStream is = null;
			
			/*String cell = MyAccountManager.getInstance().getAccountObject().mAccountTel;
			String pwd = MyAccountManager.getInstance().getAccountObject().mAccountPwd;
			
			StringBuilder sb = new StringBuilder(ServiceObject.SERVICE_URL);
			sb.append("GetNearby.ashx?")
			.append("AID=").append(mBaoxiuCardObject.mAID)
			.append("&BID=").append(mBaoxiuCardObject.mBID)
			.append("&token=").append(SecurityUtils.MD5.md5(cell+pwd))
			.append("&page_num=").append(++mLoadPageIndex);//0 page
			DebugUtils.logD(TAG, "param " + sb.toString());
			try {
				is = NetworkUtils.openContectionLocked(sb.toString(), MyApplication.getInstance().getSecurityKeyValuesObject());
				serviceResultObject = ServiceResultObject.parseAddress(NetworkUtils.getContentFromInput(is));
				mShopsList.addAll(PatternMaintenanceUtils.getMaintenancePoint(serviceResultObject.mAddresses, getActivity().getContentResolver(), mBaoxiuCardObject.mAID, mBaoxiuCardObject.mBID));
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
			} finally {
				NetworkUtils.closeInputStream(is);
			}*/
			return serviceResultObject;
		}

		@Override
		protected void onPostExecute(ServiceResultObject result) {
			super.onPostExecute(result);
			mShopListAdapter.notifyDataSetChanged();

			mShopInfoListView.onLoadMoreComplete();
			mLoadState = STATE_FREASH_COMPLETE;
			DebugUtils.logD(TAG, "huasong onPostExecute onLoadMoreComplete");
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			mShopInfoListView.onLoadMoreComplete();
			mLoadState = STATE_FREASH_CANCEL;
		}
	}
	//load more data end
	
	
	private LoadAllShopInfoAsyncTask mLoadAllShopInfoAsyncTask;
	private void loadAllShopInfoAsyncTask(String... param) {
		showProgressDialog();
//		if(PatternShopInfoUtils.isExsited(getContentResolver(), String.valueOf(mBaoxiuCardObject.mAID), String.valueOf(mBaoxiuCardObject.mBID)) > 0){
//			mShopsList = PatternMaintenanceUtils.getMaintenancePointLocal(getContentResolver(), mBaoxiuCardObject.mAID, mBaoxiuCardObject.mBID);
//			mShopListAdapter.notifyDataSetChanged();
//			mLoadPageIndex = mShopsList.size() / 10;
//		} else {
			mLoadState = STATE_FREASHING;
			AsyncTaskUtils.cancelTask(mLoadAllShopInfoAsyncTask);
			mLoadAllShopInfoAsyncTask = new LoadAllShopInfoAsyncTask();
			mLoadAllShopInfoAsyncTask.execute(param);
//		}
	}

	private class LoadAllShopInfoAsyncTask extends AsyncTask<String, Void, ServiceResultObject> {
		@Override
		protected ServiceResultObject doInBackground(String... params) {
			//更新保修卡信息
			ServiceResultObject serviceResultObject = new ServiceResultObject();
			InputStream is = null;
			try {
				JSONObject queryJsonObject = new JSONObject();
				queryJsonObject.put("pagesize", 11);
				queryJsonObject.put("pageindex", 1);
				is = NetworkUtils.openContectionLocked(ServiceObject.getAllShopInfoUrl("para", queryJsonObject.toString()), null);
				serviceResultObject = ServiceResultObject.parseShops(NetworkUtils.getContentFromInput(is));
				mShopsList = PatternShopInfoUtils.getShopInfo(serviceResultObject.mShops, getContentResolver());
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
			mShopListAdapter.notifyDataSetChanged();
			if(result.mShops == null || result.mShops.length() == 0) {
				MyApplication.getInstance().showMessage(R.string.shop_info_query_fail);
			} else {
				mLoadPageIndex = 1;
			}
			mLoadState = STATE_FREASH_COMPLETE;
			dismissProgressDialog();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			mLoadState = STATE_FREASH_CANCEL;
			dismissProgressDialog();
		}
	}
	
}
