package com.lnwoowken.lnwoowkenbook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cncom.app.base.ui.BaseActivity;

public class AboutActivity extends BaseActivity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_about);
	    mHomeBtn.setVisibility(View.INVISIBLE);
	    
	}

	@Override
	protected boolean checkIntent(Intent intent) {
		return true;
	}
	
	public static void startActivity(Context context) {
		Intent intent = new Intent(context, AboutActivity.class);
		context.startActivity(intent);
	}

}
