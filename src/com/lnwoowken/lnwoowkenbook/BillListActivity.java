package com.lnwoowken.lnwoowkenbook;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.ui.BaseActivity;
import com.cncom.app.base.util.DebugUtils;
import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullToRefreshListView.OnRefreshListener;
import com.lnwoowken.lnwoowkenbook.ServiceObject.ServiceResultObject;
import com.lnwoowken.lnwoowkenbook.adapter.BillListCursorAdapter;
import com.shwy.bestjoy.utils.AsyncTaskUtils;
import com.shwy.bestjoy.utils.NetworkUtils;

public class BillListActivity extends BaseActivity {
	private static final String TAG = "BillListActivity";
	
	private PullAndLoadListView mListBill;
	private Button btn_all;
	private Button btn_unpay;
	
	private BillListCursorAdapter mBillListCursorAdapter;
	
	private int mSelectedTextColor, mUnSelectedTextColor;
	private static final int STATE_IDLE = 0;
	private static final int STATE_FREASHING = STATE_IDLE + 1;
	private static final int STATE_FREASH_COMPLETE = STATE_FREASHING + 1;
	private static final int STATE_FREASH_CANCEL = STATE_FREASH_COMPLETE + 1;
	private int mLoadState = STATE_IDLE;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_bill);	
		BillListManager.setShowNew(false);
		mSelectedTextColor = this.getResources().getColor(R.color.main_color);
		mUnSelectedTextColor = this.getResources().getColor(R.color.textColor);
		initialize();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (BillListManager.shouldRefreshBillFromServer()) {
			BillListManager.setShouldRefreshBillFromServer(false);
			mListBill.prepareForRefresh();
			mListBill.onRefresh();
		}
	}
	
	private void initialize(){
		mListBill = (PullAndLoadListView) findViewById(R.id.listView_bill);
		btn_all = (Button) findViewById(R.id.button_bill_all);
		btn_unpay = (Button) findViewById(R.id.button_bill_unpay);
		
		btn_all.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_all.setBackgroundResource(R.drawable.button_tab_maincolor);
				btn_all.setTextColor(mSelectedTextColor);
				btn_unpay.setBackgroundResource(R.drawable.button_tab);
				btn_unpay.setTextColor(mUnSelectedTextColor);
				if(mBillListCursorAdapter != null) mBillListCursorAdapter.changeCursor(BillListManager.getLocalAllBillCursor(getContentResolver()), BillListCursorAdapter.DATA_TYPE_ALL);
			}
		});
		btn_unpay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_all.setBackgroundResource(R.drawable.button_tab);
				btn_all.setTextColor(mUnSelectedTextColor);
				btn_unpay.setBackgroundResource(R.drawable.button_tab_maincolor);
				btn_unpay.setTextColor(mSelectedTextColor);
				if(mBillListCursorAdapter != null) mBillListCursorAdapter.changeCursor(BillListManager.getLocalUnpayBillCursor(getContentResolver()), BillListCursorAdapter.DATA_TYPE_UNPAY);
			}
		});
		
		mBillListCursorAdapter = new BillListCursorAdapter(mContext, BillListManager.getLocalAllBillCursor(getContentResolver()), true);
		mListBill.setAdapter(mBillListCursorAdapter);
		mBillListCursorAdapter.changeCursor(BillListManager.getLocalAllBillCursor(getContentResolver()));
		mListBill.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
                // Do work to refresh the list here.
                GetDataTask();
			}
		});
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mBillListCursorAdapter != null) {
			mBillListCursorAdapter.changeCursor(null);
			mBillListCursorAdapter = null;
		}
	}
	
	
	public static void startActivity(Context context) {
		Intent intent = new Intent(context, BillListActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected boolean checkIntent(Intent intent) {
		return true;
	}
	

	//refresh data begin
	private RefreshBillInfoAsyncTask mRefreshBillInfoAsyncTask;
	private void GetDataTask(String... param) {
		mLoadState = STATE_FREASHING;
		AsyncTaskUtils.cancelTask(mRefreshBillInfoAsyncTask);
		mRefreshBillInfoAsyncTask = new RefreshBillInfoAsyncTask();
		mRefreshBillInfoAsyncTask.execute(param);
	}

	private class RefreshBillInfoAsyncTask extends AsyncTask<String, Void, ServiceResultObject> {
		@Override
		protected ServiceResultObject doInBackground(String... params) {
			ServiceResultObject serviceResultObject = new ServiceResultObject();
			InputStream is = null;

			try {
				JSONObject queryJsonObject = new JSONObject();
				queryJsonObject.put("uid", MyAccountManager.getInstance().getCurrentAccountUid());
				is = NetworkUtils.openContectionLocked(ServiceObject.getAllBillInfoUrl("para", queryJsonObject.toString()), null);
				serviceResultObject = ServiceResultObject.parseJsonArray(NetworkUtils.getContentFromInput(is));
				BillListManager.getBillList(getContentResolver(), serviceResultObject.mJsonArrayData);

				DebugUtils.logD(TAG, "mListBill = " + mListBill);
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
				serviceResultObject.mStatusMessage = MyApplication.getInstance().getGernalNetworkError();
			} catch(SocketException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = MyApplication.getInstance().getGernalNetworkError();
			} catch (IOException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = MyApplication.getInstance().getGernalNetworkError();
			}finally {
				NetworkUtils.closeInputStream(is);
			}
			return serviceResultObject;
		}

		@Override
		protected void onPostExecute(ServiceResultObject result) {
			super.onPostExecute(result);

			if(result.mJsonArrayData == null || result.mJsonArrayData.length() == 0) {
				if (result.isOpSuccessfully()) {
					MyApplication.getInstance().showMessage(R.string.shop_info_query_fail);
				}
			}
			if(mBillListCursorAdapter != null) mBillListCursorAdapter.changeCursor(BillListManager.getLocalAllBillCursor(getContentResolver()), BillListCursorAdapter.DATA_TYPE_ALL);
			mListBill.onRefreshComplete();
			mLoadState = STATE_FREASH_COMPLETE;
			DebugUtils.logD(TAG, "onPostExecute onRefreshComplete");
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			mListBill.onRefreshComplete();
			mLoadState = STATE_FREASH_CANCEL;
		}
	}
	//refresh data end
}
