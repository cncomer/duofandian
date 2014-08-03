package com.lnwoowken.lnwoowkenbook.view.VercialTabHost;

import com.lnwoowken.lnwoowkenbook.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class MyVercialTabHost extends LinearLayout {

	private Context context;
	public MyVercialTabHost(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		

	}
	
	@SuppressWarnings("unused")
	private void initialize(){
		LayoutInflater inflater = LayoutInflater.from(this.context);
		View view = inflater.inflate(R.layout.my_vercial_tabhost, null);
	}

}
