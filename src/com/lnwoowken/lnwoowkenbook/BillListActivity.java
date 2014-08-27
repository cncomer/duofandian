package com.lnwoowken.lnwoowkenbook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import com.cncom.app.base.ui.BaseActivity;
import com.lnwoowken.lnwoowkenbook.adapter.BillListCursorAdapter;

public class BillListActivity extends BaseActivity {
	private ListView listBill;
	
	private Button btn_all;
	private Button btn_unpay;
	
	private BillListCursorAdapter mBillListCursorAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_bill);	
		BillListManager.setShowNew(false);
		initialize();
	}
	
	private void initialize(){
		listBill = (ListView) findViewById(R.id.listView_bill);
		btn_all = (Button) findViewById(R.id.button_bill_all);
		btn_unpay = (Button) findViewById(R.id.button_bill_unpay);
		
		btn_all.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_all.setBackgroundResource(R.drawable.button_tab_maincolor);
				btn_unpay.setBackgroundResource(R.drawable.button_tab);
				if(mBillListCursorAdapter != null) mBillListCursorAdapter.changeCursor(BillListManager.getLocalAllBillCursor(getContentResolver()));
			}
		});
		btn_unpay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_all.setBackgroundResource(R.drawable.button_tab);
				btn_unpay.setBackgroundResource(R.drawable.button_tab_maincolor);
				if(mBillListCursorAdapter != null) mBillListCursorAdapter.changeCursor(BillListManager.getLocalUnpayBillCursor(getContentResolver()));
			}
		});
		
		mBillListCursorAdapter = new BillListCursorAdapter(mContext, null, true);
		listBill.setAdapter(mBillListCursorAdapter);
		listBill.setDivider(null);
		listBill.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
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

}
