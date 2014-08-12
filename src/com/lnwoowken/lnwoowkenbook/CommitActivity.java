package com.lnwoowken.lnwoowkenbook;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.util.PatternInfoUtils;
import com.cncom.app.base.util.ShopInfoObject;
import com.lnwoowken.lnwoowkenbook.data.PayInfoData;
import com.lnwoowken.lnwoowkenbook.model.BookTime;
import com.lnwoowken.lnwoowkenbook.model.Contant;
import com.lnwoowken.lnwoowkenbook.network.Client;
import com.lnwoowken.lnwoowkenbook.network.JsonParser;
import com.lnwoowken.lnwoowkenbook.tools.Tools;
import com.lnwoowken.lnwoowkenbook.view.UserDialog;

public class CommitActivity extends Activity {
	private static final String TAG = "CommitActivity";
	private float price;
	private String tNumber;
	private String payId;
	private Context context = CommitActivity.this;
	private boolean isAgree;
	private Button btn_back;
	private CheckBox checkBox_default;
	private CheckBox checkBox_others;
	private EditText editText_phoneNum;
	private EditText editText_name;
	private String mShopId;
	private String time;
	private String mServicePrice;
	private String mTablePrice;
	private TextView textView_money_describ;
	private String tableId;
	private String tableName;
	private ShopInfoObject mShopInfoObject;
	private TextView textView_shopId;
	private TextView textView_shopName;
	private TextView textView_timeinfo;
	private TextView textView_seat;
	private TextView textView_money;
	private TextView textView_agree;
	private List<BookTime> time_list;
	private Button btn_commit;
	private float price_service;
	private PayInfoData parcelableData;
	private RadioButton radioButton_agree;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bill);
		initialize();
	}
	
	private void createBill() {
		if (MyAccountManager.getInstance().hasLoginned()) {
			String jsonStr = "{\"uid\":\"" + MyAccountManager.getInstance().getCurrentAccountUid() + "\",\"sid\":\""
					+ mShopId + "\",\"tid\":\"" + tableId + "\",\"rprice\":\""
					+ parcelableData.getRprice() + "\",\"sprice\":\""
					+ parcelableData.getSprice() + "\",\"dtimeid\":\""
					+ parcelableData.getDtimeid() + "\",\"sttid\":\""
					+ parcelableData.getSttid() + "\",\"content\":\""
					+ parcelableData.getContent() + "\",\"secid\":\""
					+ parcelableData.getSecid() + "\"" + "}";

			jsonStr = Client.encodeBase64(jsonStr);
			String str = Tools.getRequestStr(Contant.SERVER_IP,
					Contant.SERVER_PORT + "", "Reserve?id=", "Rl3", "&op="
							+ jsonStr);
			Log.d(TAG, " str = " + str);
			String result = Client.executeHttpGetAndCheckNet(str,
					CommitActivity.this);

			Log.d(TAG, "result = " + result);
			if (result != null) {
				// Toast.makeText(BookTableActivity.this,
				// result,Toast.LENGTH_LONG).show();
			}

			result = Client.decodeBase64(result);

			if (result != null) {
				// if (JsonParser.checkError(result)) {
				// Toast.makeText(PayInfoActivity.this, "支付时遇到问题\n错误代码"+result,
				// Toast.LENGTH_LONG).show();
				// }
				// else {
				// Toast.makeText(PayInfoActivity.this, result,
				// Toast.LENGTH_LONG).show();
				// Log.d("pay", str);
				// Log.d("pay", result);
				// List<PayInfo> payList = JsonParser.parsePayInfoJson(result);
				// PayInfo pay = payList.get(0);
				// startSdkToPay(pay.gettNumber(), 9);
				// }
				Log.d("pay()=============", result);
				if (JsonParser.checkError(result)) {
					//Log.d("checkError()=============", result);
				}
				else {
					payId = JsonParser.parsePayNumberJson(result);
					getTnumber(payId);
				}
			}
		} else {
			showLoginDialog();
		}

	}
	
	private String getTnumber(String pid) {
		// String resultJson = null;
		// String strId =
		// "{\"id\":\""+Contant.UPMPPAY+"\",\"vd\":\"0\",\"vc\":\"0\"}";
		// String opJson = "{\"pid\":\""
		// +pid + "\",\"pp\":\""
		// + price + "\"}";
		// opJson = Client.encodeBase64(opJson);
		// String url = "http://" + Contant.SERVER_IP + ":"+ Contant.SERVER_PORT
		// + "/javadill/pay";
		//
		// List <NameValuePair> params=new ArrayList<NameValuePair>();
		// params.add(new BasicNameValuePair("id",strId));
		// params.add(new BasicNameValuePair("op",opJson));
		// try {
		// resultJson = Client.doPost(params, url);
		// if (resultJson != null) {
		//
		// Log.d("getTnumber=============", resultJson);
		// // JsonParser.parseShopInfoJson(resultJson,Contant.SHOP_LIST.get(i));
		//
		// }
		// //Log.d("version=============", resultJson);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		String resultJson;
		String opJson = "{\"pid\":\"" + pid + "\",\"pp\":\"" + 0.01 + "\"}";
		opJson = Client.encodeBase64(opJson);
		String str = Tools.getRequestStr(Contant.SERVER_IP, Contant.SERVER_PORT
				+ "", "pay?id=", Contant.UPMPPAY, "&op=" + opJson);
		resultJson = Client
				.executeHttpGetAndCheckNet(str, CommitActivity.this);
		resultJson = Client.decodeBase64(resultJson);

		if (resultJson != null) {

		//	Log.d("getTnumber=============", resultJson);
			tNumber = JsonParser.parseTradeNumberJson(resultJson);
			// if (tNumber!=null&&!tNumber.equals("")) {
			// if (checkApkExist(context, "com.unionpay.uppay")) {
			//
			// }
			// else {
			// //retrieveApkFromAssets(context, srcfileName, desFileName)
			// }
			// }
			
			Intent intent = new Intent(CommitActivity.this,PayInfoActivity.class);
			Bundle bundle = new Bundle();  
			bundle.putString("MyString", "test bundle");  
			bundle.putParcelable("PayData", parcelableData); 
			bundle.putString("tNumber", tNumber);
			intent.putExtras(bundle);  
			startActivity(intent);
			CommitActivity.this.finish();
			
			
			//pay(tNumber);
			
			// JsonParser.parseShopInfoJson(resultJson,Contant.SHOP_LIST.get(i));
		}
		return resultJson;
	}
	
	public class RequestPayInfoThread extends Thread {

		@Override
		public void run() {
			super.run();
			if (MyAccountManager.getInstance().hasLoginned()) {
				createBill();
			} else {
				showLoginDialog();
			}
		}
	}
	private Handler payHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			RequestPayInfoThread payThread = new RequestPayInfoThread();
			payThread.run();
		}
	};
	
	private void initialize(){
		Bundle bundle = getIntent().getExtras();  
		parcelableData = bundle.getParcelable("PayInfo");  
		mShopId = parcelableData.getShopId();
		time = parcelableData.getTime();
		mShopInfoObject = PatternInfoUtils.getShopInfoLocalById(getContentResolver(), mShopId);
		tableId = parcelableData.getTableId();
		tableName= parcelableData.getTableName();
		mTablePrice = parcelableData.getTablePrice();
		mServicePrice = parcelableData.getSprice();
		
		int dingjin = (int) (Integer.parseInt(TextUtils.isEmpty(mTablePrice) ? "0" : mTablePrice) * 0.2);
		int service = Integer.parseInt(TextUtils.isEmpty(mServicePrice) ? "0" : mServicePrice);
		
		textView_money_describ = (TextView) findViewById(R.id.textView_money_describ);
		textView_money_describ.setText("(定金" + (int) (Integer.parseInt(TextUtils.isEmpty(mTablePrice) ? "0" : mTablePrice) * 0.2)+"元+服务费" + mServicePrice + "元)");
		textView_shopId = (TextView) findViewById(R.id.textView_shopid);
		textView_shopName = (TextView) findViewById(R.id.textView_shopname);
		textView_timeinfo = (TextView) findViewById(R.id.textView_timeinfo);
		textView_seat = (TextView) findViewById(R.id.textView_seat);
		textView_money = (TextView) findViewById(R.id.textView_money);
		textView_agree = (TextView) findViewById(R.id.textView_agree);
		Log.d("CommitActivity___shop.getName()", mShopInfoObject.getShopName());
		textView_shopId.setText(mShopInfoObject.getShopID());
		textView_shopName.setText(mShopInfoObject.getShopName());
		textView_timeinfo.setText(parcelableData.getTime());
		textView_seat.setText(parcelableData.getTableName());
		//应付金额： 服务费+订金（额定消费X20%）
		textView_money.setText((int) ((Integer.parseInt(TextUtils.isEmpty(mTablePrice) ? "0" : mTablePrice) * 0.2) + Integer.parseInt(TextUtils.isEmpty(mServicePrice) ? "0" : mServicePrice)) + "");
		
		radioButton_agree = (RadioButton) findViewById(R.id.radioButton_agree);
		/*radioButton_agree.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					showProtocolDialog();
				}
			}
		});*/
		textView_agree.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showProtocolDialog();
			}
		});
		radioButton_agree.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(radioButton_agree.isChecked()) {
					
				}
			}
		});

		editText_name = (EditText) findViewById(R.id.editText_name);
		editText_phoneNum = (EditText) findViewById(R.id.editText_phone_number);
		checkBox_default = (CheckBox) findViewById(R.id.checkBox_default);
		checkBox_default.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (checkBox_default.isChecked()) {
					checkBox_others.setChecked(false);
					//checkBox_default.refreshDrawableState();
					editText_name.setEnabled(false);
					editText_phoneNum.setEnabled(false);
				}
			}
		});
		
		
		checkBox_others = (CheckBox) findViewById(R.id.checkBox_for_other);
		checkBox_others.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (
					checkBox_others.isChecked()) {
					checkBox_default.setChecked(false);
					editText_name.setEnabled(true);
					editText_phoneNum.setEnabled(true);
					//checkBox_default.refreshDrawableState();
				}
				else {
					editText_name.setEnabled(false);
					editText_phoneNum.setEnabled(false);
				}
			}
			
		});
		checkBox_default.setChecked(true);
		btn_commit = (Button) findViewById(R.id.button_commit);
		btn_commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(radioButton_agree.isChecked()) {
					Message msg = new Message();
					payHandler.sendMessage(msg);
				} else {
					MyApplication.getInstance().showMessage(R.string.agree_protocal_tips);
					showProtocolDialog();
				}
			}
		});
		btn_back = (Button) findViewById(R.id.button_back);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				CommitActivity.this.finish();
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
	
	private void showLoginDialog() {
		Dialog alertDialog = new AlertDialog.Builder(this)
				.setTitle("提示")
				.setMessage("您还没有登录,请先登录")
				.
				// setIcon(R.drawable.welcome_logo).
				setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(context, LoginActivity.class);
						startActivity(intent);

						CommitActivity.this.finish();
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
}
