package com.lnwoowken.lnwoowkenbook.animition;

import android.content.Context;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.cncom.app.base.account.MyAccountManager;
import com.lnwoowken.lnwoowkenbook.LoginActivity;
import com.lnwoowken.lnwoowkenbook.RestaurantListActivity;
import com.lnwoowken.lnwoowkenbook.UserInfoActivity;

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
			if (!MyAccountManager.getInstance().hasLoginned()) {
				LoginActivity.startActivity(context);
			} else {
				UserInfoActivity.startActivity(context);
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
