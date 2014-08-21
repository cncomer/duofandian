package com.lnwoowken.lnwoowkenbook;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.cncom.app.base.ui.BaseActionbarActivity;
import com.cncom.app.base.util.DebugUtils;
import com.cncom.app.base.util.PatternInfoUtils;
import com.cncom.app.base.util.ShopInfoObject;
import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullAndLoadListView.OnLoadMoreListener;
import com.costum.android.widget.PullToRefreshListView.OnRefreshListener;
import com.lnwoowken.lnwoowkenbook.ServiceObject.ServiceResultObject;
import com.lnwoowken.lnwoowkenbook.view.ProgressDialog;
import com.shwy.bestjoy.utils.AsyncTaskUtils;
import com.shwy.bestjoy.utils.NetworkUtils;

/**
 * 店家信息的界面
 * 
 * @author chs
 * 
 */
public class RestaurantListActivity extends BaseActionbarActivity {
	private static final String TAG = "RestaurantListActivity";
	private AnimationDrawable draw;
    private	LinearLayout progress;
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
	
	private boolean mFirstStart = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_list);
		initialize();
//		loadAllShopInfoAsyncTask();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (mFirstStart) {
			mFirstStart = false;
			mShopListView.prepareForRefresh();
			mShopListView.onRefresh();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.search_menu, menu);
		SearchView searchView = (SearchView) menu.findItem(R.string.menu_search).getActionView();
		searchView.setQueryHint(getString(R.string.menu_search));
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
            public boolean onQueryTextSubmit(String query) {
				loadShopInfoByNameAsyncTask(query);
	            return true;
            }

			@Override
            public boolean onQueryTextChange(String newText) {
	            return true;
            }
			
		});
		
		try {
            Class<?> argClass = searchView.getClass();
            //指定某个私有属性
            Field mSearchHintIconField = argClass.getDeclaredField("mSearchHintIcon");
            mSearchHintIconField.setAccessible(true);
            View mSearchHintIcon = (View)mSearchHintIconField.get(searchView);
            mSearchHintIcon.setVisibility(View.GONE);
             /*
            //注意mSearchPlate的背景是stateListDrawable(不同状态不同的图片)  所以不能用BitmapDrawable
            Field ownField = argClass.getDeclaredField("mSearchPlate");
            //setAccessible 它是用来设置是否有权限访问反射类中的私有属性的，只有设置为true时才可以访问，默认为false
            ownField.setAccessible(true);
            View mView = (View) ownField.get(searchView);
            mView.setBackground(getResources().getDrawable(R.drawable.contacts_search_writebg));
 			*/
 
            //指定某个私有属性  
            Field mQueryTextView = argClass.getDeclaredField("mQueryTextView");
            mQueryTextView.setAccessible(true);
            Class<?> mTextViewClass = mQueryTextView.get(searchView).getClass().getSuperclass().getSuperclass().getSuperclass();
 
 
           //mCursorDrawableRes光标图片Id的属性 这个属性是TextView的属性，所以要用mQueryTextView（SearchAutoComplete）
            //的父类（AutoCompleteTextView）的父  类( EditText）的父类(TextView)  
            Field mCursorDrawableRes = mTextViewClass.getDeclaredField("mCursorDrawableRes");
            //setAccessible 它是用来设置是否有权限访问反射类中的私有属性的，只有设置为true时才可以访问，默认为false
            mCursorDrawableRes.setAccessible(true);
          //注意第一个参数持有这个属性(mQueryTextView)的对象(mSearchView) 光标必须是一张图片不能是颜色，因为光标有两张图片，
            //一张是第一次获得焦点的时候的闪烁的图片，一张是后边有内容时候的图片，如果用颜色填充的话，就会失去闪烁的那张图片，
            //颜色填充的会缩短文字和光标的距离（某些字母会背光标覆盖一部分）。
//            mCursorDrawableRes.set(mQueryTextView.get(searchView), R.drawable.icon_small_add_delete);  
        } catch (Exception e) {
            e.printStackTrace();
        }
		return true;
	}

	private void initialize() {
		findViewById(R.id.button_sort).setOnClickListener(this);
		mShopListView = (PullAndLoadListView) findViewById(R.id.listView_all_store);
		mShopsList = new ArrayList<ShopInfoObject>();
		mShopListAdapter = new ShopListAdapter(mContext, mShopsList);
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
		switch(v.getId()){
		case R.id.button_sort:
			TabHostActivity.startActivity(mContext);
			break;
		default:
			super.onClick(v);
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
		}
		if(TextUtils.isEmpty(searchStr)) {
			return mShopsList;
		}
		return searchList;
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
			mShopListView.setSelection(0);
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

	@Override
	protected boolean checkIntent(Intent intent) {
		return true;
	}
}
