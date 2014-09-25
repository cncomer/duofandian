package com.lnwoowken.lnwoowkenbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;

import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.ui.PullToRefreshListPageActivity;
import com.lnwoowken.lnwoowkenbook.ServiceObject.ServiceResultObject;
import com.lnwoowken.lnwoowkenbook.adapter.BillListCursorAdapter;
import com.lnwoowken.lnwoowkenbook.model.BillObject;
import com.shwy.bestjoy.utils.AdapterWrapper;
import com.shwy.bestjoy.utils.InfoInterface;
import com.shwy.bestjoy.utils.NetworkUtils;
import com.shwy.bestjoy.utils.PageInfo;
import com.shwy.bestjoy.utils.Query;

public class BillListActivity extends PullToRefreshListPageActivity {
	private static final String TAG = "BillListActivity";
	
	private Button btn_all;
	private Button btn_unpay;
	
	private BillListCursorAdapter mBillListCursorAdapter;
	
	private int mSelectedTextColor, mUnSelectedTextColor;
	/**订单类型，默认是查看全部订单*/
    private int mOrderType = BillListCursorAdapter.DATA_TYPE_ALL;

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
		setOrderTypeTab(BillListCursorAdapter.DATA_TYPE_ALL);
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	protected boolean isNeedForceRefreshOnResume() {
		if (BillListManager.shouldRefreshBillFromServer()) {
			BillListManager.setShouldRefreshBillFromServer(false);
			return true;
		}
		return false;
	}
	
	/**
	 * 设置订单类型对应的Tab
	 */
	private void setOrderTypeTab(int type) {
		mOrderType = type;
		if (mOrderType == BillListCursorAdapter.DATA_TYPE_ALL) {
			btn_all.setBackgroundResource(R.drawable.button_tab_maincolor);
			btn_all.setTextColor(mSelectedTextColor);
			btn_unpay.setBackgroundResource(R.drawable.button_tab);
			btn_unpay.setTextColor(mUnSelectedTextColor);
		} else if (mOrderType == BillListCursorAdapter.DATA_TYPE_UNPAY) {
			btn_all.setBackgroundResource(R.drawable.button_tab);
			btn_all.setTextColor(mUnSelectedTextColor);
			btn_unpay.setBackgroundResource(R.drawable.button_tab_maincolor);
			btn_unpay.setTextColor(mSelectedTextColor);
		}
		if(mBillListCursorAdapter != null) {
			mBillListCursorAdapter.setOrderType(mOrderType);
		}
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
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.button_bill_all:
			setOrderTypeTab(BillListCursorAdapter.DATA_TYPE_ALL);
			break;
		case R.id.button_bill_unpay:
			setOrderTypeTab(BillListCursorAdapter.DATA_TYPE_UNPAY);
			break;
		}
	}

	@Override
	protected AdapterWrapper<? extends BaseAdapter> getAdapterWrapper() {
		mBillListCursorAdapter = new BillListCursorAdapter(mContext, null, true);
		return new AdapterWrapper<CursorAdapter>(mBillListCursorAdapter);
	}

	@Override
	protected Cursor loadLocal(ContentResolver contentResolver) {
		return mBillListCursorAdapter.requeryLocalCursor();
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
		ServiceResultObject serviceResultObject = ServiceResultObject.parseJsonArray(NetworkUtils.getContentFromInput(is));
		List<BillObject> list = new ArrayList<BillObject>();
		if (serviceResultObject.isOpSuccessfully()) {
			for(int i = 0; i < serviceResultObject.mJsonArrayData.length(); i++) {
				try {
					BillObject billObject = BillListManager.getBillFromJsonObject(serviceResultObject.mJsonArrayData.getJSONObject(i));
					list.add(billObject);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		}
		return list;
	}

	@Override
	protected Query getQuery() {
		JSONObject queryJsonObject = new JSONObject();
		try {
			queryJsonObject.put("uid", MyAccountManager.getInstance().getCurrentAccountUid());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Query query =  new Query();
		query.qServiceUrl = ServiceObject.getAllBillInfoUrl("para", queryJsonObject.toString());
		return query;
	}

	@Override
	protected void onRefreshStart() {
		
	}

	@Override
	protected void onRefreshEnd() {
		BillListManager.setShowNew(false);
	}

	@Override
	protected int getContentLayout() {
		return R.layout.pull_to_refresh_page_activity;
	}
}
