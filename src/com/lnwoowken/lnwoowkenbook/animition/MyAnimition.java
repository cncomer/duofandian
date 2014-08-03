package com.lnwoowken.lnwoowkenbook.animition;

import com.lnwoowken.lnwoowkenbook.LoginActivity;
import com.lnwoowken.lnwoowkenbook.RestaurantListActivity;
import com.lnwoowken.lnwoowkenbook.UserInfoActivity;
import com.lnwoowken.lnwoowkenbook.model.Contant;


import android.content.Context;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class MyAnimition implements AnimationListener {
	private int flag;
	private Context context;
	public MyAnimition(int flag,Context context){
		this.flag = flag;
		this.context = context;
	}

	@Override
	public void onAnimationEnd(Animation arg0) {
		// TODO Auto-generated method stub
		switch (flag) {
		case 1:
			Intent intent = new Intent(context, RestaurantListActivity.class);
			intent.putExtra("flag_from", "main");
			context.startActivity(intent);
			break;
		case 2:
//			Intent intent1 = new Intent(context, RegistActivity.class);
//			context.startActivity(intent1);
			if (Contant.ISLOGIN) {
				Intent intent1 = new Intent(context, UserInfoActivity.class);
				context.startActivity(intent1);
			}
			else {
				Intent intent1 = new Intent(context, LoginActivity.class);
				context.startActivity(intent1);
			}
			
			break;
		
		default:
			break;
		}
		
	}

	@Override
	public void onAnimationRepeat(Animation arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation arg0) {
		// TODO Auto-generated method stub
		
	}

}
