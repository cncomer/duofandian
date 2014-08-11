package com.lnwoowken.lnwoowkenbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cncom.app.base.util.PatternShopInfoUtils;
import com.cncom.app.base.util.ShopInfoObject;
import com.lnwoowken.lnwoowkenbook.data.PayInfoData;
import com.lnwoowken.lnwoowkenbook.tools.MyCount;
import com.umpay.creditcard.android.UmpayActivity;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

@SuppressLint("HandlerLeak")
@SuppressWarnings("unused")
public class PayInfoActivity extends Activity {
	private boolean isAgree;
	private MyCount mc;
	private TextView tv;
	private Context context = PayInfoActivity.this;
	private Intent intent;
	private float price;
	private TextView textView_price;
	private TextView textView_needpay;
	private String mShopId;
	private String time;
	private String servicePrice;
	private String tableId;
	private String tableName;
	private float tablePrice;
	private PayInfoData parcelableData;
	// private int tableId;
	private ShopInfoObject mShopInfoObject;
	private static final int requestCode = 888;
	private Button btn_commit;
	private Button btn_back;
	private String tNumber;
	private RadioButton radioUpmp;
	
	

//	private Handler payHandler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			RequestPayInfoThread payThread = new RequestPayInfoThread();
//			payThread.run();
//		}
//
//	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payinfo);
		initialize();

	}

	private void initialize() {
		Bundle bundle = getIntent().getExtras();
		parcelableData = bundle.getParcelable("PayData");
		String testBundleString = bundle.getString("MyString");
		mShopId = parcelableData.getShopId();
		time = parcelableData.getTime();
		mShopInfoObject =  PatternShopInfoUtils.getShopInfoLocalById(getContentResolver(), mShopId);
		tableId = parcelableData.getTableId();
		tableName = parcelableData.getTableName();
		tablePrice = parcelableData.getTablePrice();
		//price = Float.parseFloat(mShopInfoObject.getShopServerprice()) + tablePrice;
		tNumber = getIntent().getExtras().getString("tNumber");
		tv = (TextView) findViewById(R.id.textView_count);
		if (tNumber!=null&&!tNumber.equals("")) {
			if (mc == null) {
				mc = new MyCount(60 * 10 * 1000, 1000, tv);
				mc.start();
			}
		}
		// intent = getIntent();
		// price = intent.getExtras().getInt("price");
		// shopId = intent.getExtras().getInt("shopId");
		// time = intent.getExtras().getString("time");
		// tableId = intent.getExtras().getString("tableId");
		textView_price = (TextView) findViewById(R.id.textView_price);
		textView_needpay = (TextView) findViewById(R.id.textView_needpay);
		textView_price.setText(price + "");
		textView_needpay.setText(price + "");
		btn_commit = (Button) findViewById(R.id.button_commit);
		btn_commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				Message msg = new Message();
//				payHandler.sendMessage(msg);
				if (isAgree) {
					if (!TextUtils.isEmpty(tNumber)) {
						pay(tNumber);
					}
					else {
						Toast.makeText(context, "未获得支付流水号", Toast.LENGTH_SHORT).show();
					}
					
				}
				else {
					Toast.makeText(context, "您还没有选择支付方式", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		btn_back = (Button) findViewById(R.id.button_back);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PayInfoActivity.this.finish();
				
			}
		});
		radioUpmp = (RadioButton) findViewById(R.id.radioButton_upmp);
		radioUpmp.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				isAgree = arg1;
				if (arg1) {
//					final Dialog dialog = new MyDialog(BookTableActivity.this,
//							R.style.MyDialog);
//
//					dialog.show();
					
				}
				Log.d("isAgree================", isAgree+"");
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			PayInfoActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	

	

	private void startSdkToPay(String tradNo, int payType) {
		// 跳转到SDK页面
		// 将输入的参数传入Activity
		Intent intent = new Intent();
		intent.putExtra("tradeNo", tradNo);
		intent.putExtra("payType", payType);
		intent.setClass(PayInfoActivity.this, UmpayActivity.class);
		startActivityForResult(intent, requestCode);
	}

	

	

	public static boolean checkApkExist(Context context, String packageName) {
		if (packageName == null || packageName.equals("")) {
			return false;
		}
		try {
			context.getPackageManager().getApplicationInfo(packageName,
					PackageManager.GET_UNINSTALLED_PACKAGES);
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
		Log.d("onActivityResult==============", str);
		if (str.equalsIgnoreCase("success")) {
			if (mc!=null) {
				mc.cancel();
				mc = null;
				tv.setText("");
				//Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
				PayInfoActivity.this.finish();
			}
		}
//		if (str.equalsIgnoreCase(R_SUCCESS)) {
//			showResultDialog(" 支付成功！ ");
//		} else if (str.equalsIgnoreCase(R_FAIL)) {
//			showResultDialog(" 支付失败！ ");
//		} else if (str.equalsIgnoreCase(R_CANCEL)) {
//			showResultDialog(" 你已取消了本次订单的支付！ ");
//		}
	}

	public static boolean retrieveApkFromAssets(Context context,
			String srcfileName, String desFileName) {
		boolean bRet = false;
		try {
			InputStream is = context.getAssets().open(srcfileName);

			FileOutputStream fos = context.openFileOutput(desFileName,
					Context.MODE_WORLD_READABLE);
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
		
		
		Activity a = PayInfoActivity.this;
		//UPPayAssistEx.startPay(a, null, null, tn, "01");
		UPPayAssistEx.startPayByJAR(a, PayActivity.class, null, null, tn, "00");
		
		
		//UPPayAssistEx.startPayByJAR(a, PayActivity.class, null, null, tn, "01");
	}
	

}
