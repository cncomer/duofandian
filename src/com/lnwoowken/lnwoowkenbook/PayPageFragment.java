package com.lnwoowken.lnwoowkenbook;

import java.io.InputStream;
import java.util.ArrayList;
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

import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.database.DBHelper;
import com.cncom.app.base.ui.PullToRefreshListPageForFragment;
import com.lnwoowken.lnwoowkenbook.ServiceObject.ServiceResultObject;
import com.lnwoowken.lnwoowkenbook.model.BillObject;
import com.shwy.bestjoy.utils.AdapterWrapper;
import com.shwy.bestjoy.utils.InfoInterface;
import com.shwy.bestjoy.utils.NetworkUtils;
import com.shwy.bestjoy.utils.PageInfo;
import com.shwy.bestjoy.utils.Query;

public class PayPageFragment extends PullToRefreshListPageForFragment{

	private int mBillStatus = -1;
	private static final String BILL_STATE_SELECTION = DBHelper.BILL_STATE + "=?";
	private BillListAdapter mBillListCursorAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		mBillStatus = this.getArguments().getInt("bill_status", -1);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected AdapterWrapper<? extends BaseAdapter> getAdapterWrapper() {
		mBillListCursorAdapter = new BillListAdapter(MyApplication.getInstance(), null, false);
		return new AdapterWrapper<CursorAdapter>(mBillListCursorAdapter);
	}

	@Override
	protected Cursor loadLocal(ContentResolver contentResolver) {
		return BillListManager.getLocalBillsCursor(contentResolver, BILL_STATE_SELECTION, new String[]{String.valueOf(mBillStatus)});
	}

	@Override
	protected int savedIntoDatabase(ContentResolver contentResolver, List<? extends InfoInterface> infoObjects) {
		int insertOrUpdateCount = 0;
		if (infoObjects != null) {
			for(InfoInterface object:infoObjects) {
				if (object.saveInDatebase(contentResolver, null)) {
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

	private String getFilterServiceUrl() {
		try {
			JSONObject queryJsonObject = new JSONObject();
			queryJsonObject.put("uid", MyAccountManager.getInstance().getCurrentAccountUid());
			queryJsonObject.put("filter", mBillStatus);
			return queryJsonObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected Query getQuery() {
		Query query =  new Query();
		query.qServiceUrl = ServiceObject.getAllBillInfoUrl("para", getFilterServiceUrl());
		return query;
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

		public BillListAdapter(Context context, Cursor c, boolean autoRequery) {
			super(context, c, autoRequery);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return LayoutInflater.from(context).inflate(R.layout.pay_page_fragment_list_item, parent, false);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			
		}
		
	}

}
