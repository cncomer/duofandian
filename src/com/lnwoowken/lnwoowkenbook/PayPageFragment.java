package com.lnwoowken.lnwoowkenbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.database.BjnoteContent;
import com.cncom.app.base.database.DBHelper;
import com.cncom.app.base.ui.PullToRefreshListPageForFragment;
import com.lnwoowken.lnwoowkenbook.ServiceObject.ServiceResultObject;
import com.lnwoowken.lnwoowkenbook.model.BillObject;
import com.shwy.bestjoy.utils.AdapterWrapper;
import com.shwy.bestjoy.utils.DateUtils;
import com.shwy.bestjoy.utils.DebugUtils;
import com.shwy.bestjoy.utils.InfoInterface;
import com.shwy.bestjoy.utils.NetworkUtils;
import com.shwy.bestjoy.utils.PageInfo;
import com.shwy.bestjoy.utils.Query;

public class PayPageFragment extends PullToRefreshListPageForFragment{

	private static final String TAG = "PayPageFragment";
	private int mBillStatus = -1;
	private static final String BILL_STATE_SELECTION = DBHelper.BILL_STATE + "=?";
	private BillListAdapter mBillListCursorAdapter;
	private Query mQuery;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public void setBillState(int status) {
		mBillStatus = status;
	}
	
	@Override
	protected AdapterWrapper<? extends BaseAdapter> getAdapterWrapper() {
		mBillListCursorAdapter = new BillListAdapter(getActivity(), null, false);
		return new AdapterWrapper<CursorAdapter>(mBillListCursorAdapter);
	}

	@Override
	protected Cursor loadLocal(ContentResolver contentResolver) {
		DebugUtils.logD(TAG, "loadLocal billStatus " + mBillStatus);
		String select = BILL_STATE_SELECTION;
		String[] selectArgs = new String[]{String.valueOf(mBillStatus)};
		if (mBillStatus == BillObject.STATE_ALL) {
			select = null;
			selectArgs = null;
		}
		return BillListManager.getLocalAccountBillsCursor(contentResolver, select, selectArgs);
	}

	@Override
	protected int savedIntoDatabase(ContentResolver contentResolver, List<? extends InfoInterface> infoObjects) {
		int insertOrUpdateCount = 0;
		if (infoObjects != null) {
			for(InfoInterface object:infoObjects) {
				if (object instanceof BillObject) {
					BillObject billObject = (BillObject) object;
					billObject.mBillTableContentUri = BjnoteContent.Bills.ACCOUNT_CONTENT_URI;
				} 
				if (object.saveInDatebase(contentResolver, null)) {
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

	private String getFilterServiceUrl() {
		try {
			JSONObject queryJsonObject = new JSONObject();
			queryJsonObject.put("uid", MyAccountManager.getInstance().getCurrentAccountUid());
			queryJsonObject.put("filter", mBillStatus);
			queryJsonObject.put("pageindex", mQuery.mPageInfo.mPageIndex);
			queryJsonObject.put("pagesize", mQuery.mPageInfo.mPageSize);
			return queryJsonObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected Query getQuery() {
		mQuery =  new Query();
		mQuery.mPageInfo = new PageInfo();
		mQuery.qServiceUrl = ServiceObject.getAllBillInfoUrl("para", getFilterServiceUrl());
		return mQuery;
	}

	@Override
	protected void onRefreshStart() {
		
	}

	@Override
	protected void onRefreshEnd() {
		
	}

	@Override
	protected int getContentLayout() {
		return R.layout.pay_page_fragment;
	}
	
	private class BillListAdapter extends CursorAdapter {

		private String _format_pay_time, _format_tuiding_time, _format_xiaopei_time, _format_bill_number, _format_price;
		public BillListAdapter(Context context, Cursor c, boolean autoRequery) {
			super(context, c, autoRequery);
			_format_pay_time = context.getString(R.string.format_bill_pay_time);
			_format_tuiding_time = context.getString(R.string.format_bill_tuiding_time);
			_format_xiaopei_time = context.getString(R.string.format_bill_xiaofei_time);
			_format_bill_number = context.getString(R.string.format_bill_number);
			_format_price = context.getString(R.string.format_price);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			ViewHolder viewHolder = new ViewHolder();
			View view = LayoutInflater.from(context).inflate(R.layout.pay_page_fragment_list_item, parent, false);
			viewHolder._name = (TextView) view.findViewById(R.id.name);
			viewHolder._price = (TextView) view.findViewById(R.id.bill_price);
			viewHolder._billnumber = (TextView) view.findViewById(R.id.bill_number);
			viewHolder._time = (TextView) view.findViewById(R.id.bill_time);
			view.setTag(viewHolder);
			return view;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			ViewHolder viewHolder = (ViewHolder) view.getTag();
			BillObject billObject = BillListManager.getBillObjectFromCursor(cursor);
			viewHolder._name.setText(billObject.getShopName());
			
			viewHolder._billnumber.setText(String.format(_format_bill_number, billObject.getBillNumber()));
			if (mBillStatus == BillObject.STATE_SUCCESS || mBillStatus == BillObject.STATE_ALL) {
				viewHolder._price.setText(String.format(_format_price, billObject.getTotalPrice()));
				viewHolder._time.setText(String.format(_format_pay_time, DateUtils.DATE_TIME_FORMAT.format(billObject.getCreateTime())));
			} else if (mBillStatus == BillObject.STATE_TUIDING_SUCCESS) {
				viewHolder._price.setText(String.format(_format_price, billObject.getTotalPrice()));
				viewHolder._time.setText(String.format(_format_tuiding_time, DateUtils.DATE_TIME_FORMAT.format(new Date(billObject.getCreateTime()))));
			} else if (mBillStatus == BillObject.STATE_XIAOFEI) {
				//实际消费
				viewHolder._price.setText(String.format(_format_price, billObject.mRealFee));
				viewHolder._time.setText(String.format(_format_xiaopei_time, DateUtils.DATE_TIME_FORMAT.format(new Date(billObject.getCreateTime()))));
			}
		}
		
	}
	
	private class ViewHolder {
		private TextView _name, _price, _billnumber, _time;
	}

}
