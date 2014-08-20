package com.lnwoowken.lnwoowkenbook;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cncom.app.base.ui.BaseActionbarActivity;
import com.cncom.app.base.util.PatternInfoUtils;
import com.cncom.app.base.util.ShopInfoObject;
import com.lnwoowken.lnwoowkenbook.model.Contant;
import com.lnwoowken.lnwoowkenbook.model.StoreInfo;
import com.lnwoowken.lnwoowkenbook.network.Client;
import com.lnwoowken.lnwoowkenbook.network.JsonParser;
import com.lnwoowken.lnwoowkenbook.tools.Tools;

/**
 * 餐厅详情
 *
 */
public class RestuarantInfoActivity extends BaseActionbarActivity implements View.OnClickListener{
	private Button btn_chooseFood;
	private List<ShopInfoObject> mShopList;
	private List<StoreInfo> shopid;
	private String params = "http://pic.lnwoowken.com/望湘园.png";
	private Button btnFirst, btnSecond;
	private ProgressBar progress;
	private FrameLayout frameLayout;
	private Bitmap bitmap = null;
	ProgressDialog dialog = null;
	private RequestOtherShopThread mThread;
	ShopInfoObject mShopInfoObject;
	private Button btn_chooseTable;// --进入选桌界面的按钮
	private Intent intent;
	private String mShopId;
	private ImageView shopImg;
	private TextView textView_info;//--餐厅详情介绍
	private TextView textView_shopName;//--店名
	private TextView textView_price;//--人均消费
	private TextView textView_address;//--餐厅地址
	private TextView textView_phone;//--联系电话
	private Context context = RestuarantInfoActivity.this;

	// CalendarView calendar;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restuarant_info);
		initialize();

	}


	@SuppressWarnings({ "deprecation", "unused" })
	private void initialize() {
		shopImg = (ImageView) findViewById(R.id.imageView_shop_img);
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
	//	int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
		int margin_int = com.lnwoowken.lnwoowkenbook.tools.Tools.dip2px(
				context, 20);
		LinearLayout.LayoutParams l2 = new LinearLayout.LayoutParams(
				screenWidth,
				screenWidth*260/480);
		l2.setMargins(0, 0, 0, 0);
		shopImg.setLayoutParams(l2);


		textView_price = (TextView) findViewById(R.id.textView_price);

		textView_info = (TextView) findViewById(R.id.textView_info);
		textView_shopName = (TextView) findViewById(R.id.textView_storename);
		intent = RestuarantInfoActivity.this.getIntent();
		mShopId = intent.getExtras().getString("shop_id");
		mShopInfoObject = PatternInfoUtils.getShopInfoLocalById(getContentResolver(), mShopId);
		if (mShopInfoObject != null) {
			textView_shopName.setText(mShopInfoObject.getShopName());
			Log.d("mShopInfoObject.getShopName()-------------------", mShopInfoObject.getShopName() + "");
			String price = textView_price.getText().toString() + mShopInfoObject.getShopServerprice();
			textView_price.setText(price);
			updateShotcutImage();
		}
		btn_chooseFood = (Button) findViewById(R.id.imageButton_pickfood);
		btn_chooseFood.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(context, "敬请期待", Toast.LENGTH_SHORT).show();
			}
		});
		btn_chooseTable = (Button) findViewById(R.id.button_choose_table);
		btn_chooseTable.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(RestuarantInfoActivity.this, BookTableActivity.class);
				intent.putExtra("shop_id", mShopInfoObject.getShopID());
				startActivity(intent);

			}
		});
		textView_address = (TextView) findViewById(R.id.textView_location);
		textView_phone = (TextView) findViewById(R.id.textView_phone);
		if (mShopInfoObject != null) {
			textView_address.setText(mShopInfoObject.getShopAddress());
			textView_phone.setText(mShopInfoObject.getShopContactsPhone());
			textView_info.setText(mShopInfoObject.getShopBrief());
		}
	}

	private void updateShotcutImage() {
		if(!TextUtils.isEmpty(mShopInfoObject.getShopYouHui())) findViewById(R.id.imageView_hui).setVisibility(View.VISIBLE);
		if(!TextUtils.isEmpty(mShopInfoObject.getShopTuanGou())) findViewById(R.id.imageView_tuan).setVisibility(View.VISIBLE);
		if(!TextUtils.isEmpty(mShopInfoObject.getShopDianCan())) findViewById(R.id.imageView_dian).setVisibility(View.VISIBLE);
		if(!TextUtils.isEmpty(mShopInfoObject.getShopMaiDian())) findViewById(R.id.imageView_mai).setVisibility(View.VISIBLE);
		
	}
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (mThread != null) {
				mThread.start();
				
			}
			File f = new File(Contant.WOOWKEN_DIR+"/"+mShopInfoObject.getShopImg());
			if (f.exists()) {
				bitmap = BitmapFactory.decodeFile(Contant.WOOWKEN_DIR+"/"+mShopInfoObject.getShopImg());
				if (bitmap!=null) {
					shopImg.setImageBitmap(bitmap);
				}
			}
			else {
				if (thread!=null) {
					thread.start();
				}
			}
			
			

		}
	};

	public class RequestOtherShopThread extends Thread {

		@Override
		public void run() {
			super.run();

			String op = "{\"Sid\":\"" + mShopId + "\""
					+ ",\"Pid\":\"56\"}";
			op = Client.encodeBase64(op);
			String str = Tools.getRequestStr(Contant.SERVER_IP,
					Contant.SERVER_PORT + "", "shop?id=", "s13", "&op=" + op);
			String result = Client.executeHttpGetAndCheckNet(str,
					RestuarantInfoActivity.this);
			result = Client.decodeBase64(result);
			if (JsonParser.checkError(result)) {
				//Log.d("getShopList=============error", result);
			}
			else {
				String tid = JsonParser.parseTreeShopJson(result);
				Log.d("tid=============", tid);
				if (tid!=null&&!tid.equals("")) {
					String resultJson;
					String opJson = "{\"Tid\":\"" + tid + "\"}";
					opJson = Client.encodeBase64(opJson);
					String rStr = Tools.getRequestStr(Contant.SERVER_IP, Contant.SERVER_PORT
							+ "", "shop?id=", "s12", "&op=" + opJson);
					resultJson = Client.executeHttpGetAndCheckNet(rStr, context);
					resultJson = Client.decodeBase64(resultJson);
					
					//resultTree = resultJson;
					if (resultJson != null) {

						Log.d("getShopList=============", resultJson);
						if (JsonParser.checkError(resultJson)) {
							Log.d("checkError=============s12", "");
							//Toast.makeText(context, resultJson, Toast.LENGTH_SHORT).show();
						} else {
							if (resultJson != null && !resultJson.equals("")) {
								shopid = JsonParser.parseShopIdJson(resultJson);
								Log.d("shopList=============s12", ""+shopid.size());
								//int flag = Contant.FLAG_GETSHOPBYID;
								Message msg = new Message();
								initialhandler.sendMessage(msg);
							}
						}			 
						//Contant.SHOPTREE_LIST = JsonParser.parseShopTreeJson(resultJson);
					}
				}
			}
			Log.d("s13============", "result:" + result);


		}
	}
	
	
	private Handler initialhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			/*if (shopid!=null) {
			
				//String[] mItems = new String[]{"其他分店"};
				btnTittle = (Button) findViewById(R.id.textView1);
				btnTittle.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						showShopDialog();
					}
				});
//				ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.simple_spinner_item);
//				//String level[] = getResources().getStringArray(R.array.affair_level);//资源文件
				mShopList = new ArrayList<ShopInfoObject>();
				for (int i = 0; i < shopid.size(); i++) {
					
					StoreInfo temp = Tools.findShopById(shopid.get(i).getId());
					shopList.add(temp);
					Log.d("shopList============initialhandler", "shopList name:" + shopList.get(i).getName());
					//adapter.add(temp.getName());
				}
				
				
			}*/
			
		}
	};
	
	
	Thread thread = new Thread(new Runnable() {
		
		@Override
		public void run() {

			
			params = "http://pic.lnwoowken.com/"+mShopInfoObject.getShopImg();
			Log.d("download=======================", params);
			HttpGet httpRequest = new HttpGet(params);
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			try {
				response = (HttpResponse) httpclient.execute(httpRequest);
				HttpEntity entity = response.getEntity();
				BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(entity);
				InputStream is = bufferedHttpEntity.getContent();
				bitmap = BitmapFactory.decodeStream(is);
				if (bitmap!=null) {
					Tools.saveBitmapToFile(bitmap, Contant.WOOWKEN_DIR+"/"+mShopInfoObject.getShopImg());
					Message msg = new Message();
					msg.what = 1;
					imghandler.sendMessage(msg);
				}
				else {
					Log.d("btimap===============", "null");
					
					//Toast.makeText(MainActivity.this, "null", Toast.LENGTH_SHORT).show();
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
	});
	
	private Handler imghandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (thread!=null) {
				switch (msg.what) {
				case 1:
					// 关闭
					shopImg.setImageBitmap(bitmap);
					//dialog.dismiss();
					break;
				}
			}
			
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (bitmap!=null) {
			bitmap.recycle();
			bitmap = null;
		}
	}
	
	
	private void showShopDialog(){
		/*final Dialog dialog = new OtherShopDialog(RestuarantInfoActivity.this,
				R.style.MyDialog);

		dialog.show();
		ListView list = (ListView) dialog
				.findViewById(R.id.listView_othershop);
		list.setAdapter(new OtherShopAdapter(RestuarantInfoActivity.this,
				shopList));
		list.setDivider(null);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// Toast.makeText(BookTableActivity.this,
				// ""+arg2,Toast.LENGTH_LONG).show();
				mShopInfoObject = shopList.get(arg2);
				if (shop != null) {
					Log.d("===============", shop.getAddress());
					textView_address.setText(shop.getAddress());
					textView_phone.setText(shop.getPhoneNumber());
					textView_info.setText(shop.getInfo());
					textView_shopName.setText(shop.getName());
					
					String price = textView_price.getText().toString()
							+ shop.getAveragePrice();
					textView_price.setText(price);
					File f = new File(Contant.WOOWKEN_DIR+"/"+shop.getImagePath());
					if (f.exists()) {
						bitmap = BitmapFactory.decodeFile(Contant.WOOWKEN_DIR+"/"+shop.getImagePath());
						Log.d("shop.getImagePath()-------------------", shop.getImagePath() + "");
						if (bitmap!=null) {
							shopImg.setImageBitmap(bitmap);
						}
					}
				}
				dialog.dismiss();
			}
		});
		
		// TableListDialog dialog = new
		// TableListDialog(BookTableActivity.this);
		// dialog.show();*/
	
	}
	
	public static void startIntent(Context context, Bundle bundle) {
		Intent intent = new Intent(context, RestuarantInfoActivity.class);
		if (bundle == null) {
			return;
		}
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean checkIntent(Intent intent) {
		return true;
	}
	
}
