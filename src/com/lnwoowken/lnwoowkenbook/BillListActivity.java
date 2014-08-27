package com.lnwoowken.lnwoowkenbook;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.ui.BaseActionbarActivity;
import com.lnwoowken.lnwoowkenbook.adapter.BillListAdapter;
import com.lnwoowken.lnwoowkenbook.model.BillObject;
import com.lnwoowken.lnwoowkenbook.model.Contant;

public class BillListActivity extends BaseActionbarActivity {
	private PopupWindow popupWindow;
	private Context context = BillListActivity.this;
	private ListView listBill;
	private BillListAdapter mBillListAdapter;
	private List<BillObject> mBillList;
	
	private Button btn_all;
	private Button btn_unpay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_bill);	
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
				if (MyAccountManager.getInstance().hasLoginned()) {
					if(mBillListAdapter != null) mBillListAdapter.updateList(BillListManager.getBillListLocal(getContentResolver()));
				} else {
					Toast.makeText(context, "您还没有登录,请先登录", Toast.LENGTH_SHORT).show();
				}
			}
		});
		btn_unpay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_all.setBackgroundResource(R.drawable.button_tab);
				btn_unpay.setBackgroundResource(R.drawable.button_tab_maincolor);
				if (MyAccountManager.getInstance().hasLoginned()) {
					if(mBillListAdapter != null) mBillListAdapter.updateList(BillListManager.getUnpayBillListLocal(getContentResolver()));
				} else {
					Toast.makeText(context, "您还没有登录,请先登录", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		if (MyAccountManager.getInstance().hasLoginned()) {
			mBillList = BillListManager.getBillListLocal(getContentResolver());
			mBillListAdapter = new BillListAdapter(context, mBillList);
			listBill.setAdapter(mBillListAdapter);
			listBill.setDivider(null);
			listBill.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				}
			});
		} else {
			Toast.makeText(context, "您还没有登录,请先登录", Toast.LENGTH_SHORT).show();
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
					}
				}).
				create();
		alertDialog.show();
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
