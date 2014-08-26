package com.lnwoowken.lnwoowkenbook;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cncom.app.base.account.MyAccountManager;
import com.lnwoowken.lnwoowkenbook.adapter.BillListAdapter;
import com.lnwoowken.lnwoowkenbook.model.BillObject;
import com.lnwoowken.lnwoowkenbook.model.Contant;

public class BillListActivity extends Activity {
	private PopupWindow popupWindow;
	private Context context = BillListActivity.this;
	private ListView listBill;
	private BillListAdapter mBillListAdapter;
	private List<BillObject> mBillList;
	private Button btn_back;
	private Button btn_home;
	private Button btn_more;//--“更多”按钮
	
	private Button btn_all;
	private Button btn_unpay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_bill);	
		initialize();
	}
	
	private void initialize(){
		listBill = (ListView) findViewById(R.id.listView_bill);
		btn_back = (Button) findViewById(R.id.button_back);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BillListActivity.this.finish();
			}
		});
		btn_home = (Button) findViewById(R.id.button_home);
		btn_more = (Button) findViewById(R.id.button_more);
		btn_home.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.startIntentClearTop(context, null);
				BillListActivity.this.finish();
			}
		});
		btn_more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popupWindow == null || !popupWindow.isShowing()) {
					View view = LayoutInflater.from(context).inflate(R.layout.popmenu, null);
					RelativeLayout myBill = (RelativeLayout) view.findViewById(R.id.mybill);
					RelativeLayout exitLogin = (RelativeLayout) view.findViewById(R.id.exit_login);
					exitLogin.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if (MyAccountManager.getInstance().hasLoginned()) {
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
					popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
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
		});
		btn_all = (Button) findViewById(R.id.button_bill_all);
		btn_unpay = (Button) findViewById(R.id.button_bill_unpay);
		
		btn_all.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_all.setBackgroundResource(R.drawable.button_tab_maincolor);
				btn_unpay.setBackgroundResource(R.drawable.button_tab);
				if (MyAccountManager.getInstance().hasLoginned()) {
					mBillList = BillListManager.getBillListLocal(getContentResolver());
					if(mBillListAdapter != null) mBillListAdapter.notifyDataSetChanged();
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
					mBillList = BillListManager.getUnpayBillListLocal(getContentResolver());
					if(mBillListAdapter != null) mBillListAdapter.notifyDataSetChanged();
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

}
