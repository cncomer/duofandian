package com.lnwoowken.lnwoowkenbook;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.ui.BaseActionbarActivity;
import com.cncom.app.base.util.DebugUtils;
import com.cncom.app.base.util.ShopInfoObject;
import com.cncom.app.base.util.TableInfoObject;
import com.lnwoowken.lnwoowkenbook.ServiceObject.ServiceResultObject;
import com.lnwoowken.lnwoowkenbook.model.BillObject;
import com.lnwoowken.lnwoowkenbook.view.ProgressDialog;
import com.lnwoowken.lnwoowkenbook.view.UserDialog;
import com.shwy.bestjoy.utils.AsyncTaskUtils;
import com.shwy.bestjoy.utils.DateUtils;
import com.shwy.bestjoy.utils.NetworkUtils;

public class CommitActivity extends BaseActionbarActivity {
	private static final String TAG = "CommitActivity";
	private boolean isAgree;
	private CheckBox checkBox_default;
	private CheckBox checkBox_others;
	private EditText editText_phoneNum;
	private EditText editText_name;
	private TextView textView_money_describ;
	private ShopInfoObject mShopInfoObject;
//	private TableInfo mTableInfo;
	private TextView textView_shopId;
	private TextView textView_shopName;
	private TextView textView_timeinfo;
	private TextView textView_seat;
	private TextView textView_money;
	private Button btn_commit;
	private TableInfoObject parcelableData;
	private RadioButton radioButton_agree;
	private Dialog dialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bill);
		initialize();
	}
	
	private void initialize(){
		Bundle bundle = getIntent().getExtras();  
		parcelableData = bundle.getParcelable("shopobject");
		mShopInfoObject = ShopInfoObject.getShopInfoObjectByShopId(getContentResolver(), parcelableData.getShopId());
		
		((TextView) findViewById(R.id.textView_attention)).setText(mShopInfoObject.mOrderConfirmTip);
		
//		mTableInfo = new TableInfo();
//		mTableInfo.setTableId(parcelableData.getTableId());
//		mTableInfo.setTableName(parcelableData.getTableName());
//		mTableInfo.setTableStyle(parcelableData.getTableType());
//		mTableInfo.setPrice(parcelableData.getTablePrice());
//		mTableInfo.setSprice(parcelableData.getTablePrice());
//		mTableInfo.setNote(parcelableData.getContent());
		//mPriceTotal = (int) ((Integer.parseInt(TextUtils.isEmpty(mTableInfo.getPrice()) ? "0" : mTableInfo.getPrice()) * 0.2) + Integer.parseInt(TextUtils.isEmpty(mTableInfo.getSprice()) ? "0" : mTableInfo.getSprice()));
		textView_money_describ = (TextView) findViewById(R.id.textView_money_describ);
		textView_money_describ.setText(parcelableData.getBillPay());
		//textView_money_describ.setText(" (定金0" +"元+服务费" + mTableInfo.getSprice() + "元)");
		textView_shopId = (TextView) findViewById(R.id.textView_shopid);
		textView_shopName = (TextView) findViewById(R.id.textView_shopname);
		textView_timeinfo = (TextView) findViewById(R.id.textView_timeinfo);
		textView_seat = (TextView) findViewById(R.id.textView_seat);
		textView_money = (TextView) findViewById(R.id.textView_money);
		Log.d("CommitActivity___shop.getName()", mShopInfoObject.getShopName());
		textView_shopId.setText(mShopInfoObject.getShopShowId());
		textView_shopName.setText(mShopInfoObject.getShopName());
		textView_timeinfo.setText(TableInfoObject.BILL_ORDER_DATE_FORMAT.format(parcelableData.getOrderDate()));
		textView_seat.setText(parcelableData.getDeskName());
		//应付金额： 服务费+订金（额定消费X20%）
		textView_money.setText(parcelableData.getTotalPrice());
		
		radioButton_agree = (RadioButton) findViewById(R.id.radioButton_agree);
		
		radioButton_agree.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showProtocolDialog();
			}
		});
//		editText_name = (EditText) findViewById(R.id.editText_name);
//		editText_phoneNum = (EditText) findViewById(R.id.editText_phone_number);
//		checkBox_default = (CheckBox) findViewById(R.id.checkBox_default);
//		checkBox_default.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				if (checkBox_default.isChecked()) {
//					checkBox_others.setChecked(false);
//					editText_name.setEnabled(false);
//					editText_phoneNum.setEnabled(false);
//				}
//			}
//		});
//		
//		checkBox_others = (CheckBox) findViewById(R.id.checkBox_for_other);
//		checkBox_others.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				if (checkBox_others.isChecked()) {
//					checkBox_default.setChecked(false);
//					editText_name.setEnabled(true);
//					editText_phoneNum.setEnabled(true);
//				} else {
//					editText_name.setEnabled(false);
//					editText_phoneNum.setEnabled(false);
//				}
//			}
//		});
//		checkBox_default.setChecked(true);
		btn_commit = (Button) findViewById(R.id.button_commit);
		btn_commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(radioButton_agree.isChecked()) {
					createBillAsyncTask();
				} else {
					MyApplication.getInstance().showMessage(R.string.agree_protocal_tips);
					showProtocolDialog();
				}
			}
		});
	}
	
	private void showProtocolDialog() {
		final Dialog dialog = new UserDialog(CommitActivity.this, R.style.MyDialog);
		dialog.show();
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface arg0) {
				if (isAgree == false) {
					radioButton_agree.setChecked(false);
				} else {
					radioButton_agree.setChecked(true);
				}
			}
		});
		Button btn_agree = (Button) dialog.findViewById(R.id.button_accept);
		btn_agree.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				isAgree = true;
			}
		});
		Button btn_nagree = (Button) dialog.findViewById(R.id.button_naccept);
		btn_nagree.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				isAgree = false;
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			CommitActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private CreateBillAsyncTask mCreateBillAsyncTask;
	private void createBillAsyncTask(String... param) {
		showProgressDialog();
		AsyncTaskUtils.cancelTask(mCreateBillAsyncTask);
		mCreateBillAsyncTask = new CreateBillAsyncTask();
		mCreateBillAsyncTask.execute(param);
	}

	private class CreateBillAsyncTask extends AsyncTask<String, Void, ServiceResultObject> {
		@Override
		protected ServiceResultObject doInBackground(String... params) {
			ServiceResultObject serviceResultObject = new ServiceResultObject();
			InputStream is = null;
			try {
				JSONObject queryJsonObject = new JSONObject();
				queryJsonObject.put("deskID", parcelableData.getDeskId());
				queryJsonObject.put("note", parcelableData.getNote());
				queryJsonObject.put("service_price", Float.valueOf(parcelableData.getServicePrice()) * 100);
				queryJsonObject.put("zifu_price", Float.valueOf(parcelableData.getDingJinPrice()) * 100);
				queryJsonObject.put("uid", MyAccountManager.getInstance().getCurrentAccountUid());
				DebugUtils.logD(TAG, "CreateBillAsyncTask doInBackground() queryJsonObject=" + queryJsonObject.toString());
				is = NetworkUtils.openContectionLocked(ServiceObject.getLiushuiNumberUrl("para", queryJsonObject.toString()), null);
				serviceResultObject = ServiceResultObject.parse(NetworkUtils.getContentFromInput(is));
				
				if (serviceResultObject.isOpSuccessfully()) {
					//订单生成成功
					BillObject billObj = new BillObject();
					String orderNo = serviceResultObject.mJsonData.getString("orderno"); //订单号
					String tNumber = serviceResultObject.mJsonData.getString("tn"); //流水号暂时不使用
					String createTime = serviceResultObject.mJsonData.optString("createTime", "");//订单号生成时间
					//设置交易流水号和订单号
					parcelableData.setTn(tNumber);
					parcelableData.setOrderNo(orderNo);
					
					billObj.setBillNumber(orderNo);
					billObj.mTN = tNumber;
					billObj.setUid(MyAccountManager.getInstance().getCurrentAccountUid());
					billObj.setSid(mShopInfoObject.getShopID());
					billObj.setTid(parcelableData.getDeskId());
					billObj.setShopName(mShopInfoObject.getShopName());
					billObj.setTableName(parcelableData.getDeskName());
					try {
						billObj.setCreateTime(TextUtils.isEmpty(createTime)?new Date().getTime():DateUtils.DATE_FULL_TIME_FORMAT.parse(createTime).getTime());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					billObj.setDate(parcelableData.getDate());
					billObj.setTime(parcelableData.getmShiduanTime());
					billObj.setState(BillObject.STATE_UNPAY);
					billObj.setTableType(parcelableData.getDeskType());
					billObj.setDabiaoPrice(parcelableData.getDabiaoPrice());
					billObj.setServicePrice(parcelableData.getServicePrice());
					billObj.setDingJinPrice(parcelableData.getDingJinPrice());
					BillListManager.saveBill(billObj, getContentResolver());
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
			}finally {
				NetworkUtils.closeInputStream(is);
			}
			return serviceResultObject;
		}

		@Override
		protected void onPostExecute(ServiceResultObject result) {
			super.onPostExecute(result);
			dismissProgressDialog();
			if(result.isOpSuccessfully()) {
				commitPay();
			} else {
				MyApplication.getInstance().showMessage(result.mStatusMessage);
			}
				
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			dismissProgressDialog();
		}
	}

	private void dismissProgressDialog(){
		if(dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
	}
	
	public void commitPay() {
		Bundle bundle = new Bundle();  
		bundle.putParcelable("shopobject", parcelableData);
		PayInfoActivity.startActivity(mContext, bundle);
		finish();
	}

	private void showProgressDialog(){
		if(dialog != null && dialog.isShowing()) return; 
		dialog = new ProgressDialog(CommitActivity.this, R.style.ProgressDialog);
		dialog.show();
		LinearLayout progress = (LinearLayout) dialog.findViewById(R.id.imageView_progress);
		progress.setBackgroundResource(R.anim.animition_progress); 
		final AnimationDrawable draw = (AnimationDrawable)progress.getBackground(); 
        draw.start();
        dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface arg0) {
				draw.stop();
			}
		});
		dialog.setCanceledOnTouchOutside(false);
	}

	@Override
	protected boolean checkIntent(Intent intent) {
		return true;
	}
}
