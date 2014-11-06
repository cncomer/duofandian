package com.lnwoowken.lnwoowkenbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.database.DBHelper;
import com.cncom.app.base.ui.PullToRefreshListPageActivity;
import com.cncom.app.base.util.DebugUtils;
import com.cncom.app.base.util.TableInfoObject;
import com.lnwoowken.lnwoowkenbook.ServiceObject.ServiceResultObject;
import com.lnwoowken.lnwoowkenbook.adapter.BillListCursorAdapter;
import com.lnwoowken.lnwoowkenbook.adapter.BillListCursorAdapter.ViewHolder;
import com.lnwoowken.lnwoowkenbook.model.BillObject;
import com.shwy.bestjoy.utils.AdapterWrapper;
import com.shwy.bestjoy.utils.AsyncTaskUtils;
import com.shwy.bestjoy.utils.ComPreferencesManager;
import com.shwy.bestjoy.utils.InfoInterface;
import com.shwy.bestjoy.utils.NetworkUtils;
import com.shwy.bestjoy.utils.PageInfo;
import com.shwy.bestjoy.utils.Query;

public class BillListActivity extends PullToRefreshListPageActivity implements View.OnClickListener{
	private static final String TAG = "BillListActivity";
	
	private Button btn_all;
	private Button btn_unpay;
	
	private BillListCursorAdapter mBillListCursorAdapter;
	
	private int mSelectedTextColor, mUnSelectedTextColor;
	/**订单类型*/
    private int mOrderType = -1;
    
    private Handler mHandler;
    /**刷新界面，以便按钮能够适时灰化*/
    private static final int WHAT_UPDATE_TIME = 10;
    private static final int UPDATE_TIME_DELAY = 60 * 1000;
    
    /**刷新服务器数据*/
    private static final int WHAT_REFRESH = 11;
    /**10分钟刷新一次*/
    private static final int WHAT_REFRESH_TIME_DELAY = 10 * 60 * 1000;
    private Query mQuery;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BillListManager.setShowNew(false);
		mSelectedTextColor = this.getResources().getColor(R.color.main_color);
		mUnSelectedTextColor = this.getResources().getColor(R.color.textColor);
		btn_all = (Button) findViewById(R.id.button_bill_all);
		btn_all.setOnClickListener(this);
		
		btn_unpay = (Button) findViewById(R.id.button_bill_unpay);
		btn_unpay.setOnClickListener(this);
		//默认显示的是全部订单TAB
		setOrderTypeTab(BillObject.STATE_ALL);
		
		
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch(msg.what) {
				case WHAT_UPDATE_TIME:
					if(mBillListCursorAdapter != null && mBillListCursorAdapter.getCount() > 0) {
						DebugUtils.logD(TAG, "mBillListCursorAdapter.notifyDataSetChanged() " + new Date().getTime());
						mBillListCursorAdapter.notifyDataSetChanged();
					}
					mHandler.sendEmptyMessageDelayed(WHAT_UPDATE_TIME, UPDATE_TIME_DELAY);	
					break;
				case WHAT_REFRESH:
					forceRefresh();
					mHandler.sendEmptyMessageDelayed(WHAT_REFRESH, WHAT_REFRESH_TIME_DELAY);
					break;
				}
			}
			
		};
		mHandler.sendEmptyMessageDelayed(WHAT_UPDATE_TIME, UPDATE_TIME_DELAY);		
		mHandler.sendEmptyMessageDelayed(WHAT_REFRESH, WHAT_REFRESH_TIME_DELAY);
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onStop() {
		super.onStop();
	}
	
	@Override
	protected boolean isNeedForceRefreshOnResume() {
		//第一次我们需要刷新
		if (ComPreferencesManager.getInstance().isFirstLaunch(TAG, true)) {
			ComPreferencesManager.getInstance().setFirstLaunch(TAG, false);
			return true;
		}
		//以后只有订单表发生变化了，我们才会需要刷新
		if (BillListManager.isShowNew()) {
			return true;
		}
		//上次刷新后，如果超过了10分钟，我们需要重新获取服务器数据一次以便同步状态
		long nowTime = new Date().getTime();
		long lastRefreshTime = ComPreferencesManager.getInstance().mPreferManager.getLong(TAG+".refreshTime", nowTime);
		if (Math.abs(nowTime - lastRefreshTime) >= WHAT_REFRESH_TIME_DELAY) {
			DebugUtils.logD(TAG, "isNeedForceRefreshOnResume() > WHAT_REFRESH_TIME_DELAY");
			return true;
		}
		
		return false;
	}
	
	/**
	 * 设置订单类型对应的Tab
	 */
	private void setOrderTypeTab(int type) {
		mOrderType = type;
		if (mOrderType == BillObject.STATE_ALL) {
			btn_all.setBackgroundResource(R.drawable.button_tab_maincolor);
			btn_all.setTextColor(mSelectedTextColor);
			btn_unpay.setBackgroundResource(R.drawable.button_tab);
			btn_unpay.setTextColor(mUnSelectedTextColor);
		} else if (mOrderType == BillObject.STATE_UNPAY) {
			btn_all.setBackgroundResource(R.drawable.button_tab);
			btn_all.setTextColor(mUnSelectedTextColor);
			btn_unpay.setBackgroundResource(R.drawable.button_tab_maincolor);
			btn_unpay.setTextColor(mSelectedTextColor);
		}
		if (mBillListCursorAdapter != null) {
			mBillListCursorAdapter.setOrderType(mOrderType);
		}
		mQuery.qServiceUrl = ServiceObject.getAllBillInfoUrl("para", getFilterServiceUrl());
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mBillListCursorAdapter != null) {
			mBillListCursorAdapter.changeCursor(null);
			mBillListCursorAdapter = null;
		}
		mHandler.removeMessages(WHAT_REFRESH);
		mHandler.removeMessages(WHAT_UPDATE_TIME);	
	}
	
	
	public static void startActivity(Context context) {
		Intent intent = new Intent(context, BillListActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected boolean checkIntent(Intent intent) {
		return true;
	}
	
	private void showDialog(String str, final BillObject billObject) {
		final Dialog alertDialog = new Dialog(mContext, R.style.MyDialog);
		alertDialog.setTitle(R.string.dialog_title);
		alertDialog.setContentView(R.layout.dialog_layout);
		TextView title = (TextView) alertDialog.findViewById(R.id.title);
		TextView context = (TextView) alertDialog.findViewById(R.id.message);
		Button buttonOk = (Button) alertDialog.findViewById(R.id.button_ok);
		Button buttonCancel = (Button) alertDialog.findViewById(R.id.button_back);
		title.setText(R.string.dialog_title);
		context.setText(str);
		alertDialog.setCanceledOnTouchOutside(false);
		buttonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
				deleteBillAsync(billObject);
			}
		});
		buttonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.button_bill_all:
			setOrderTypeTab(BillObject.STATE_ALL);
			forceRefresh();
			break;
		case R.id.button_bill_unpay:
			setOrderTypeTab(BillObject.STATE_UNPAY);
			forceRefresh();
			break;
		case R.id.imageButton_delete://删除订单
			ViewHolder groupHolder = (ViewHolder) v.getTag();
			showDialog(mContext.getString(R.string.delete_tips), groupHolder.billObject);
			break;
		case R.id.button_confirm_pay:
			//确认支付
			ViewHolder viewHolder = (ViewHolder) v.getTag();
			TableInfoObject tableInfoObject = new TableInfoObject();
			//设置ShopID
			tableInfoObject.setShopId(viewHolder.billObject.getSid());
			//设置DeskID
			tableInfoObject.setDeskId(viewHolder.billObject.getTid());
			//设置交易流水号和订单号
			tableInfoObject.setTn(viewHolder.billObject.mTN);
			tableInfoObject.setOrderNo(viewHolder.billObject.getBillNumber());
			Bundle bundle = new Bundle();  
			bundle.putParcelable("shopobject", tableInfoObject);
			PayInfoActivity.startActivity(mContext, bundle);
			break;
		}
	}

	private static final String BILL_STATE_SELECTION = DBHelper.BILL_STATE + "=?";
	@Override
	protected AdapterWrapper<? extends BaseAdapter> getAdapterWrapper() {
		mBillListCursorAdapter = new BillListCursorAdapter(mContext, null, true);
		mBillListCursorAdapter.setOnClickListener(this);
		return new AdapterWrapper<CursorAdapter>(mBillListCursorAdapter);
	}

	@Override
	protected Cursor loadLocal(ContentResolver contentResolver) {
		String select = BILL_STATE_SELECTION;
		String[] selectArgs = new String[]{String.valueOf(mOrderType)};
		if (mOrderType == BillObject.STATE_ALL) {
			select = null;
			selectArgs = null;
		}
		return BillListManager.getLocalBillsCursor(contentResolver, select, selectArgs);
	}

	@Override
	protected int savedIntoDatabase(ContentResolver cr, List<? extends InfoInterface> infoObjects) {
		int insertOrUpdateCount = 0;
		if (infoObjects != null) {
			for(InfoInterface object:infoObjects) {
				if (object.saveInDatebase(cr, null)) {
					insertOrUpdateCount++;
				}
			}
		}
		return insertOrUpdateCount;
	}

	@Override
	protected List<? extends InfoInterface> getServiceInfoList(InputStream is, PageInfo pageInfo) {
		ServiceResultObject serviceResultObject = ServiceResultObject.parse(NetworkUtils.getContentFromInput(is));
		List<BillObject> list = new ArrayList<BillObject>();
		if (serviceResultObject.isOpSuccessfully()) {
			try {
				serviceResultObject.mJsonArrayData = serviceResultObject.mJsonData.getJSONArray("rows");
				int len = serviceResultObject.mJsonArrayData.length();
				pageInfo.mTotalCount = serviceResultObject.mJsonData.optInt("total", 0);
				for(int i = 0; i < len; i++) {
					try {
						BillObject billObject = BillListManager.getBillFromJsonObject(serviceResultObject.mJsonArrayData.getJSONObject(i));
						list.add(billObject);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return list;
	}

	@Override
	protected Query getQuery() {
		mQuery =  new Query();
		mQuery.mPageInfo = new PageInfo();
		mQuery.qServiceUrl = ServiceObject.getAllBillInfoUrl("para", getFilterServiceUrl());
		return mQuery;
	}
	
	private String getFilterServiceUrl() {
		try {
			JSONObject queryJsonObject = new JSONObject();
			queryJsonObject.put("uid", MyAccountManager.getInstance().getCurrentAccountUid());
			queryJsonObject.put("filter", mOrderType);
			queryJsonObject.put("pageindex", mQuery.mPageInfo.mPageIndex);
			queryJsonObject.put("pagesize", mQuery.mPageInfo.mPageSize);
			return queryJsonObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onRefreshLoadEnd() {
		BillListManager.deleteCachedData(getContentResolver());
	}
	
	@Override
	protected void onRefreshStart() {
		ComPreferencesManager.getInstance().mPreferManager.edit().putLong(TAG+".refreshTime", new Date().getTime()).commit();
	}

	@Override
	protected void onRefreshEnd() {
		BillListManager.setShowNew(false);
	}

	@Override
	protected int getContentLayout() {
		return R.layout.pull_to_refresh_page_activity;
	}
	
	private DeleteBillTask mDeleteBillTask;
	private void deleteBillAsync(BillObject billObject) {
		AsyncTaskUtils.cancelTask(mDeleteBillTask);
		mDeleteBillTask = new DeleteBillTask(billObject);
		mDeleteBillTask.execute();
		showDialog(DIALOG_PROGRESS);
	}
	private class DeleteBillTask extends AsyncTask<Void, Void, ServiceResultObject> {

		private BillObject _billObject;
		public DeleteBillTask(BillObject billObject) {
			_billObject = billObject;
		}
		
		@Override
		protected ServiceResultObject doInBackground(Void... params) {
			InputStream is = null;
			ServiceResultObject serviceResultObject = new ServiceResultObject();
			try {
				JSONObject jsonQuerry = new JSONObject();
				jsonQuerry.put("OrderID", _billObject.getBillNumber());
				jsonQuerry.put("Uid", MyAccountManager.getInstance().getCurrentAccountUid());
				is = NetworkUtils.openContectionLocked(ServiceObject.getDeleteBillUrl("para", jsonQuerry.toString()), MyApplication.getInstance().getSecurityKeyValuesObject());
				serviceResultObject = ServiceResultObject.parse(NetworkUtils.getContentFromInput(is));
				if (serviceResultObject.isOpSuccessfully()) {
					//删除成功，我们还要删除本地的数据
					boolean deleted =BillListManager.deleteBillByNumber(getContentResolver(), _billObject.getBillNumber());
					DebugUtils.logD(TAG, "DeleteBillTask delete local bill " + _billObject.getBillNumber() + ", deleted " + deleted);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
			} catch (IOException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
			} finally {
				NetworkUtils.closeInputStream(is);
			}
			return serviceResultObject;
		}

		@Override
		protected void onPostExecute(ServiceResultObject result) {
			super.onPostExecute(result);
			dismissDialog(DIALOG_PROGRESS);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			dismissDialog(DIALOG_PROGRESS);
		}
		
	}
}
