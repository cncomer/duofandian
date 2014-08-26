package com.lnwoowken.lnwoowkenbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.ui.BaseActionbarActivity;
import com.cncom.app.base.util.DebugUtils;
import com.cncom.app.base.util.PatternInfoUtils;
import com.cncom.app.base.util.ShopInfoObject;
import com.cncom.app.base.util.TableInfoObject;
import com.lnwoowken.lnwoowkenbook.ServiceObject.ServiceResultObject;
import com.lnwoowken.lnwoowkenbook.model.BillObject;
import com.lnwoowken.lnwoowkenbook.model.TableInfo;
import com.lnwoowken.lnwoowkenbook.tools.MyCount;
import com.lnwoowken.lnwoowkenbook.view.ProgressDialog;
import com.shwy.bestjoy.utils.AsyncTaskUtils;
import com.shwy.bestjoy.utils.DateUtils;
import com.shwy.bestjoy.utils.NetworkUtils;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

public class UnPayInfoActivity extends BaseActionbarActivity {
	private static final String TAG = "PayInfoActivity";
	private boolean isAgree;
	private MyCount mCountDownTime;
	private Context context = UnPayInfoActivity.this;
	private int price;
	private TextView textView_price, textView_needpay, textView_billnumber;
	private String mShopId;
	private String time;
	private ShopInfoObject mShopInfoObject;
	private static final int requestCode = 888;
	private Button btn_commit;
	private Button btn_back;
	private String tNumber;
	private BillObject mBillObject;
	private TableInfoObject mTableInfo;
	private String mBillNumber;
	private RadioButton radioUpmp;
	private Dialog dialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unpayinfo);
		initialize();
	}

	private void initialize() {
		Bundle bundle = getIntent().getExtras();
		mBillNumber = bundle.getString("bill_number");
		mBillObject = BillListManager.getBillObjectByBillNumber(getContentResolver(), mBillNumber);
		//mTableInfo = PatternInfoUtils.getTableInfoByName(getContentResolver(), mBillObject.getTableName());
		price = (int) ((Integer.parseInt(TextUtils.isEmpty(mBillObject.getDabiaoPrice()) ? "0" : mBillObject.getDabiaoPrice()) * 0.2) + Integer.parseInt(TextUtils.isEmpty(mBillObject.getServicePrice()) ? "0" : mBillObject.getServicePrice()));
		if (tNumber!=null&&!tNumber.equals("")) {
			if (mCountDownTime == null) {
				mCountDownTime = new MyCount(30 * 1000, 1000, findViewById(R.id.bottom));
				mCountDownTime.start();
			}
		}
		textView_price = (TextView) findViewById(R.id.textView_price);
		textView_needpay = (TextView) findViewById(R.id.textView_needpay);
		textView_billnumber = (TextView) findViewById(R.id.textView_billnumber);
		textView_price.setText(price + getString(R.string.yuan));
		textView_needpay.setText(price + getString(R.string.yuan));
		textView_billnumber.setText(mBillNumber);
		btn_commit = (Button) findViewById(R.id.button_commit);
		btn_commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (isAgree) {
					if (!TextUtils.isEmpty(mBillNumber)) {
						requestUnPayingAsyncTask();
					} else {
						Toast.makeText(context, "未获得订单号", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(context, "您还没有选择支付方式", Toast.LENGTH_SHORT).show();
				}
			}
		});
		radioUpmp = (RadioButton) findViewById(R.id.radioButton_upmp);
		radioUpmp.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				isAgree = arg1;
				if (arg1) {
//					final Dialog dialog = new MyDialog(BookTableActivity.this, R.style.MyDialog);
//					dialog.show();
				}
				Log.d(TAG, isAgree+"");
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			UnPayInfoActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	public static boolean checkApkExist(Context context, String packageName) {
		if (packageName == null || packageName.equals("")) {
			return false;
		}
		try {
			context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}
		String str = data.getExtras().getString("pay_result");
		Log.d(TAG, str);
		if (str.equalsIgnoreCase("success")) {
			if (mCountDownTime!=null) {
				mCountDownTime.cancel();
				mCountDownTime = null;
				Toast.makeText(context, context.getString(R.string.pay_success_tips), Toast.LENGTH_LONG).show();
				saveBillDatabase(BillObject.STATE_SUCCESS);
				UnPayInfoActivity.this.finish();
			}
		} else if (str.equalsIgnoreCase("cancel")) {
			mCountDownTime.onFinish();
			mCountDownTime.cancel();
			mCountDownTime = null;
			Toast.makeText(context, context.getString(R.string.pay_cancel_tips), Toast.LENGTH_LONG).show();
			saveBillDatabase(BillObject.STATE_UNPAY);
		}
	}

	private void saveBillDatabase(int state) {
		BillObject billObj = new BillObject();
		billObj.setBillNumber(mBillNumber);
		billObj.setState(state);
		
		BillListManager.saveBill(billObj, getContentResolver());
	}
	@Override
	protected boolean checkIntent(Intent intent) {
		return true;
	}

	private RequestUnPayingAsyncTask mRequestUnPayingAsyncTask;
	private void requestUnPayingAsyncTask(String... param) {
		showProgressDialog();
		AsyncTaskUtils.cancelTask(mRequestUnPayingAsyncTask);
		mRequestUnPayingAsyncTask = new RequestUnPayingAsyncTask();
		mRequestUnPayingAsyncTask.execute(param);
	}

	private class RequestUnPayingAsyncTask extends AsyncTask<String, Void, ServiceResultObject> {
		@Override
		protected ServiceResultObject doInBackground(String... params) {
			//更新保修卡信息
			ServiceResultObject serviceResultObject = new ServiceResultObject();
			InputStream is = null;
			try {
				JSONObject queryJsonObject = new JSONObject();
				queryJsonObject.put("orderNumber", mBillNumber);
				queryJsonObject.put("orderAmount", MyAccountManager.getInstance().getCurrentAccountUid());

				is = NetworkUtils.openContectionLocked(ServiceObject.getVoidOrderUrl("para", queryJsonObject.toString()), null);
				serviceResultObject= ServiceResultObject.parse(NetworkUtils.getContentFromInput(is));
				DebugUtils.logD(TAG, "result = " + serviceResultObject.mStrData);
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
			if(result != null) {
				saveBillDatabase(BillObject.STATE_TUIDING_SUCCESS);
				MyApplication.getInstance().showMessage(result.mStatusMessage);
			}
			dismissProgressDialog();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			dismissProgressDialog();
		}
	}

	private void dismissProgressDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
	}

	private void showProgressDialog() {
		if (dialog != null && dialog.isShowing())
			return;
		dialog = new ProgressDialog(UnPayInfoActivity.this, R.style.ProgressDialog);
		dialog.show();
		LinearLayout progress = (LinearLayout) dialog.findViewById(R.id.imageView_progress);

		progress.setBackgroundResource(R.anim.animition_progress);
		final AnimationDrawable draw = (AnimationDrawable) progress.getBackground();
		draw.start();
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface arg0) {
				draw.stop();
			}
		});
		dialog.setCanceledOnTouchOutside(false);
	}

}
