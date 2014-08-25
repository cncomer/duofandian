﻿package com.lnwoowken.lnwoowkenbook;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cncom.app.base.service.PhotoManagerUtilsV2;
import com.cncom.app.base.ui.BaseActionbarActivity;
import com.cncom.app.base.util.PatternInfoUtils;
import com.cncom.app.base.util.ShopInfoObject;

/**
 * 餐厅详情
 *
 */
public class RestuarantInfoActivity extends BaseActionbarActivity {
	private static final String TAG = "RestuarantInfoActivity";
	private Button btn_chooseFood;
	private ShopInfoObject mShopInfoObject;
	private Button btn_chooseTable;// --进入选桌界面的按钮
	private Intent intent;
	private String mShopId;
	private ImageView shopImg;
	private TextView textView_info, textView_shopName, textView_price,
			textView_address, textView_phone;
	private Context context = RestuarantInfoActivity.this;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restuarant_info);
		PhotoManagerUtilsV2.getInstance().requestToken(TAG);
		initialize();
	}

	@SuppressWarnings({ "deprecation", "unused" })
	private void initialize() {
		shopImg = (ImageView) findViewById(R.id.imageView_shop_img);
//		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
//	//	int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
//		int margin_int = com.lnwoowken.lnwoowkenbook.tools.Tools.dip2px(context, 20);
//		LinearLayout.LayoutParams l2 = new LinearLayout.LayoutParams(screenWidth, screenWidth*260/480);
//		l2.setMargins(0, 0, 0, 0);
//		shopImg.setLayoutParams(l2);
		mShopInfoObject = PatternInfoUtils.getShopInfoLocalById(getContentResolver(), mShopId);
		PhotoManagerUtilsV2.getInstance().loadPhotoAsync(TAG, shopImg, mShopInfoObject.getShopPhotoId("01"), null, PhotoManagerUtilsV2.TaskType.SHOP_IMAGE);

		textView_price = (TextView) findViewById(R.id.textView_price);
		textView_info = (TextView) findViewById(R.id.textView_info);
		textView_shopName = (TextView) findViewById(R.id.textView_storename);
		intent = RestuarantInfoActivity.this.getIntent();
		mShopId = intent.getExtras().getString("shop_id");
		
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		PhotoManagerUtilsV2.getInstance().releaseToken(TAG);
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
		switch(v.getId()) {
		default:
			super.onClick(v);
		}
	}

	@Override
	protected boolean checkIntent(Intent intent) {
		return true;
	}
}
