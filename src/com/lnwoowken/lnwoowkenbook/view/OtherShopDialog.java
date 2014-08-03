package com.lnwoowken.lnwoowkenbook.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.lnwoowken.lnwoowkenbook.R;

public class OtherShopDialog extends Dialog{

	public OtherShopDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public OtherShopDialog(Context context,int id) {
		super(context,id);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_otershop);
		//getWindow().setFeatureInt(Window.FEATURE_NO_TITLE, R.layout.title);//镊畾涔夊竷锟?锟斤拷锟?
		
	}
}
