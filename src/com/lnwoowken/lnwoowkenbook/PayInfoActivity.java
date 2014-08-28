package com.lnwoowken.lnwoowkenbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
import com.shwy.bestjoy.utils.NetworkUtils;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

public class PayInfoActivity extends BaseActionbarActivity {
	private static final String TAG = "PayInfoActivity";
	private boolean isAgree;
	private MyCount mCountDownTime;
	private Context context = PayInfoActivity.this;
	private int price;
	private TextView textView_price, textView_needpay, textView_billnumber;
	private String mShopId;
	private TableInfoObject parcelableData;
	private ShopInfoObject mShopInfoObject;
	private TableInfo mTableInfo;
	private Button btn_commit;
	private String tNumber;
	private String mOrderNumber;
	private RadioButton radioUpmp;
	private Dialog dialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payinfo);
		initialize();
	}

	private void initialize() {
		Bundle bundle = getIntent().getExtras();
		parcelableData = bundle.getParcelable("shopobject");
		mShopId = parcelableData.getShopId();
		mShopInfoObject =  PatternInfoUtils.getShopInfoLocalById(getContentResolver(), mShopId);
		
		tNumber = getIntent().getExtras().getString("tNumber");
		mOrderNumber = getIntent().getExtras().getString("orderNo");
		//price = (int) ((Integer.parseInt(TextUtils.isEmpty(mTableInfo.getPrice()) ? "0" : mTableInfo.getPrice()) * 0.2) + Integer.parseInt(TextUtils.isEmpty(mTableInfo.getSprice()) ? "0" : mTableInfo.getSprice()));
		price = (int) Integer.parseInt(TextUtils.isEmpty(mTableInfo.getSprice()) ? "0" : mTableInfo.getSprice());
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
		textView_billnumber.setText(mOrderNumber);
		btn_commit = (Button) findViewById(R.id.button_commit);
		btn_commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (isAgree) {
					if (!TextUtils.isEmpty(tNumber)) {
						requestTablePayingState();
					} else {
						Toast.makeText(context, "未获得支付流水号", Toast.LENGTH_SHORT).show();
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
		
		((TextView) findViewById(R.id.textView_attention)).setText(mShopInfoObject.mOrderPayTip);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			PayInfoActivity.this.finish();
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
				BillListManager.updateBillStateByBillNumber(getContentResolver(), mOrderNumber, BillObject.STATE_SUCCESS);
				PayInfoActivity.this.finish();
			}
		} else if (str.equalsIgnoreCase("cancel")) {
			mCountDownTime.onFinish();
			mCountDownTime.cancel();
			mCountDownTime = null;
			Toast.makeText(context, context.getString(R.string.pay_cancel_tips), Toast.LENGTH_LONG).show();
			BillListManager.updateBillStateByBillNumber(getContentResolver(), mOrderNumber, BillObject.STATE_UNPAY);
		}
	}

	public static boolean retrieveApkFromAssets(Context context, String srcfileName, String desFileName) {
		boolean bRet = false;
		try {
			InputStream is = context.getAssets().open(srcfileName);

			FileOutputStream fos = context.openFileOutput(desFileName, Context.MODE_WORLD_READABLE);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close();
			bRet = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bRet;
	}

	private void pay(String tn) {
		UPPayAssistEx.startPayByJAR((Activity)PayInfoActivity.this, PayActivity.class, null, null, tn, "00");
	}

	@Override
	protected boolean checkIntent(Intent intent) {
		return true;
	}

	private RequestTablePayingState mRequestTablePayingState;
	private void requestTablePayingState(String... param) {
		showProgressDialog();
		AsyncTaskUtils.cancelTask(mRequestTablePayingState);
		mRequestTablePayingState = new RequestTablePayingState();
		mRequestTablePayingState.execute(param);
	}

	private class RequestTablePayingState extends AsyncTask<String, Void, ServiceResultObject> {
		@Override
		protected ServiceResultObject doInBackground(String... params) {
			ServiceResultObject serviceResultObject = new ServiceResultObject();
			InputStream is = null;
			try {
				JSONObject queryJsonObject = new JSONObject();
				queryJsonObject.put("orderno", mOrderNumber);
				queryJsonObject.put("deskid", mTableInfo.getTableId());

				is = NetworkUtils.openContectionLocked(ServiceObject.getInOrderingUrl("para", queryJsonObject.toString()), null);
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
				if(result.isOpSuccessfully()) {					
					pay(tNumber);
				} else {					
					MyApplication.getInstance().showMessage(result.mStatusMessage);
				}
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
		dialog = new ProgressDialog(PayInfoActivity.this, R.style.ProgressDialog);
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
