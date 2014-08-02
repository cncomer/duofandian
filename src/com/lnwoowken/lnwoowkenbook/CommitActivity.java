package com.lnwoowken.lnwoowkenbook;



import java.util.List;


import com.lnwoowken.lnwoowkenbook.data.PayInfoData;
import com.lnwoowken.lnwoowkenbook.model.BookTime;
import com.lnwoowken.lnwoowkenbook.model.Contant;
import com.lnwoowken.lnwoowkenbook.model.StoreInfo;
import com.lnwoowken.lnwoowkenbook.model.UserInfo;
import com.lnwoowken.lnwoowkenbook.network.Client;
import com.lnwoowken.lnwoowkenbook.network.JsonParser;
import com.lnwoowken.lnwoowkenbook.tools.MyCount;
import com.lnwoowken.lnwoowkenbook.tools.Tools;
import com.lnwoowken.lnwoowkenbook.view.MyDialog;
import com.lnwoowken.lnwoowkenbook.view.UserDialog;

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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

@SuppressWarnings("unused")
public class CommitActivity extends Activity {
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
	private int shopId;
	private String time;
	private String servicePrice;
	private TextView textView_money_describ;
	private String tableId;
	private String tableName;
	private float tablePrice;
	//private int tableId;
	private StoreInfo shop;
	private TextView textView_shopName;
	private TextView textView_timeinfo;
	private TextView textView_seat;
	private TextView textView_money;
	private TextView textView_pay_inall;
	private List<BookTime> time_list;
	private Button btn_commit;
	private float price_service;
	private PayInfoData parcelableData;
	private RadioButton radioButton_agree;
	//private TextView textView_shopName;
	//private TextView textView_shopName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bill);
		initialize();
		

	}
	
	private void createBill() {
		if (Contant.USER != null) {
			UserInfo user = Contant.USER;

			String jsonStr = "{\"uid\":\"" + user.getId() + "\",\"sid\":\""
					+ shopId + "\",\"tid\":\"" + tableId + "\",\"rprice\":\""
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
			// Log.d("url___________====", url);
			String result = Client.executeHttpGetAndCheckNet(str,
					CommitActivity.this);

			// Log.d("url___________====", result);
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
					
					//Log.d("createBill============", payId);
					getTnumber(payId);
				}
				
				// Log.d("createBill============", payId);

			}
		} else {
			showDialog();
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
		// // TODO Auto-generated catch block
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
			// TODO Auto-generated method stub
			super.run();
			// Log.d("___________====", "I'm in");
			// Log.d("___________====", url);
			if (Contant.ISLOGIN && Contant.USER != null) {
				createBill();
			} else {
				showDialog();
			}

		}

	}
	private Handler payHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			RequestPayInfoThread payThread = new RequestPayInfoThread();
			payThread.run();
		}

	};
	
	private void initialize(){
		
		Bundle bundle = getIntent().getExtras();  
		parcelableData = bundle.getParcelable("PayInfo");  
		String testBundleString = bundle.getString("MyString");
		shopId = parcelableData.getShopId();
		time = parcelableData.getTime();	
		shop =Tools. findShopById(shopId);
		tableId = parcelableData.getTableId();
		tableName= parcelableData.getTableName();
		tablePrice = parcelableData.getTablePrice();
		price = Float.parseFloat(shop.getServicePrice()) + tablePrice;
	//	Log.d("shopPirce===============", shop.getServicePrice());
	//	Log.d("tablePrice==============", tablePrice+"");
		
		textView_money_describ = (TextView) findViewById(R.id.textView_money_describ);
		textView_money_describ.setText("(定金"+tablePrice+"元+服务费"+shop.getServicePrice()+"元)");
		textView_shopName = (TextView) findViewById(R.id.textView_shopname);
		textView_timeinfo = (TextView) findViewById(R.id.textView_timeinfo);
		textView_seat = (TextView) findViewById(R.id.textView_seat);
		textView_money = (TextView) findViewById(R.id.textView_money);
		textView_pay_inall = (TextView) findViewById(R.id.textView_money_inall);
		Log.d("CommitActivity___shop.getName()", shop.getName());
		textView_shopName.setText(shop.getName());
		textView_timeinfo.setText(time);
		textView_seat.setText(tableName);
		servicePrice = (Float.parseFloat(shop.getServicePrice())+tablePrice)+"";
		textView_money.setText(servicePrice+"");
		textView_pay_inall.setText(servicePrice+"");
//		textView_shopName = (TextView) findViewById(R.id.textView_shopname);
//		textView_shopName = (TextView) findViewById(R.id.textView_shopname);
//		textView_shopName = (TextView) findViewById(R.id.textView_shopname);
		
		radioButton_agree = (RadioButton) findViewById(R.id.radioButton_agree);
		radioButton_agree.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				
				if (arg1) {
//					final Dialog dialog = new MyDialog(BookTableActivity.this,
//							R.style.MyDialog);
//
//					dialog.show();
					final Dialog dialog = new UserDialog(CommitActivity.this,
							R.style.MyDialog);

					dialog.show();
					dialog.setOnDismissListener(new OnDismissListener() {
						
						@Override
						public void onDismiss(DialogInterface arg0) {
							// TODO Auto-generated method stub
							if (isAgree == false) {
								radioButton_agree.setChecked(false);
							}
						}
					});
					Button btn_agree = (Button) dialog.findViewById(R.id.button_accept);
					btn_agree.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							radioButton_agree.setChecked(true);
							isAgree = true;
						}
						
						
					});
					Button btn_nagree = (Button) dialog.findViewById(R.id.button_naccept);
					btn_nagree.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							radioButton_agree.setChecked(false);
							isAgree = false;
						}
						
						
					});
					//isAgree = arg1;
				}
				//Log.d("isAgree================", isAgree+"");
			}
		});
		
		
		
		editText_name = (EditText) findViewById(R.id.editText_name);
		editText_phoneNum = (EditText) findViewById(R.id.editText_phone_number);
		checkBox_default = (CheckBox) findViewById(R.id.checkBox_default);
		checkBox_default.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
				if (isAgree) {
					
					Message msg = new Message();
					payHandler.sendMessage(msg);
					
					
					
					
				}
				else {
					Toast.makeText(context, "您还没有同意用户协议", Toast.LENGTH_SHORT).show();
				}
				
				
			}
		});
		btn_back = (Button) findViewById(R.id.button_back);
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				CommitActivity.this.finish();
			}
		});
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		//
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			CommitActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void showDialog() {
		Dialog alertDialog = new AlertDialog.Builder(this)
				.setTitle("提示")
				.setMessage("您还没有登录,请先登录")
				.
				// setIcon(R.drawable.welcome_logo).
				setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(context, LoginActivity.class);
						startActivity(intent);

						CommitActivity.this.finish();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				}).

				create();
		alertDialog.show();
	}
}
