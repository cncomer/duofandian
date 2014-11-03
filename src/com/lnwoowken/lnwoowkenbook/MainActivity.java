package com.lnwoowken.lnwoowkenbook;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.actionbarsherlock.view.MenuItem;
import com.cncom.app.base.service.TimeService;
import com.cncom.app.base.ui.BaseSlidingFragmentActivity;
import com.cncom.app.base.util.YouMengMessageHelper;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.shwy.bestjoy.utils.DebugUtils;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;
/**
 * @author chenkai
 *
 */
public class MainActivity extends BaseSlidingFragmentActivity implements 
	SlidingMenu.OnOpenedListener, SlidingMenu.OnClosedListener{
	private static final String TAG = "MainActivity";
	private MainActivityContentFragment mContent;
	private PersonalInfoCenterFragment mMenu;
	private Bundle mBundles;
	private boolean mIsNeedShowContent = false;
	/**表示是否是第一次进入*/
	private static final String  KEY_FIRST_SHOW = "MainActivity2.first";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DebugUtils.logD(TAG, "onCreate()");
		if (isFinishing()) {
			return ;
		}
		
		//友盟更新功能
		UmengUpdateAgent.update(this);
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		
		
		if (savedInstanceState != null) {
			mContent = (MainActivityContentFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
			mMenu = (PersonalInfoCenterFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mMenu");
			DebugUtils.logD(TAG, "savedInstanceState != null, we try to get Fragment from FragmentManager, mContent=" + mContent + ", mMenu=" + mMenu);
		}
		if (mContent == null) {
			mContent = new MainActivityContentFragment();
			mContent.setArguments(mBundles);
			// set the Above View
			setContentView(R.layout.content_frame);
			getSupportFragmentManager()
			.beginTransaction()
			.add(R.id.content_frame, mContent)
			.commit();
		} else {
			// set the Above View
			setContentView(R.layout.content_frame);
			getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.content_frame, mContent)
			.commit();
		}
		
		if (mMenu == null) {
			mMenu = new PersonalInfoCenterFragment();
			// set the Behind View
			setBehindContentView(R.layout.menu_frame);
			getSupportFragmentManager()
			.beginTransaction()
			.add(R.id.menu_frame, mMenu)
			.commit();

		} else {
			// set the Behind View
			setBehindContentView(R.layout.menu_frame);
			getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.menu_frame, mMenu)
			.commit();

		}
		
		
		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
//		sm.setBehindOffsetRes(R.dimen.choose_device_slidingmenu_offset);
//        sm.setAboveOffsetRes(R.dimen.choose_device_slidingmenu_offset);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindScrollScale(0.25f);
		sm.setFadeDegree(0.25f);
		sm.setMode(SlidingMenu.LEFT);
		sm.setTouchModeAbove(SlidingMenu.LEFT);
		sm.setBehindOffsetRes(R.dimen.choose_device_choose_slidingmenu_offset);
		sm.setOnOpenedListener(this);
		sm.setOnClosedListener(this);
		
		setSlidingActionBarEnabled(true);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setLogo(R.drawable.logo_appname);
		getSupportActionBar().setDisplayUseLogoEnabled(true);
		
		//启动推送功能
		YouMengMessageHelper.getInstance().startPushAgent();
		
		TimeService.startService(this);
		
	}
	
	protected boolean showActinBar() {
		return true;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (mIsNeedShowContent) {
			mIsNeedShowContent = false;
			if (getSlidingMenu().isMenuShowing()) {
				getSlidingMenu().showContent();
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	     switch(item.getItemId()) {
		     case android.R.id.home:
			     if (getSlidingMenu().isMenuShowing()) {
			    	 getSlidingMenu().showContent();
			     } else {
			    	 getSlidingMenu().showMenu();
			     }
				 return true;
		}
		return super.onOptionsItemSelected(item);
		
	}


	@Override
	public void onOpened() {
		//当SlidingMenu打开后，我们需要隐藏掉手动打开SlidinMenu按钮
	}


	@Override
	public void onClosed() {
		//当SlidingMenu关闭后，我们需要重新显示手动打开SlidinMenu按钮
		
	}
	
	public void onNewIntent(Intent intent){
		super.onNewIntent(intent);
		mIsNeedShowContent = true;
		DebugUtils.logD(TAG, "onNewIntent " + intent);
	}
	
	public static void startIntent(Context context, Bundle bundle) {
		Intent intent = new Intent(context, MainActivity.class);
		if (bundle != null) intent.putExtras(bundle);
		context.startActivity(intent);
	}
	
	public static void startIntentClearTop(Context context, Bundle bundle) {
		Intent intent = new Intent(context, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		if (bundle != null) intent.putExtras(bundle);
		context.startActivity(intent);
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (getSlidingMenu().isMenuShowing()) {
				getSlidingMenu().showContent();
			} else {
				getSlidingMenu().showMenu();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected boolean checkIntent(Intent intent) {
		mBundles = intent.getExtras();
		if (mBundles == null) {
			DebugUtils.logD(TAG, "checkIntent mBundles is null");
		}
		return true;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		DebugUtils.logD(TAG, "onSaveInstanceState(), we try to save Fragment to FragmentManager, mContent=" + mContent + ", mMenu=" + mMenu);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
		getSupportFragmentManager().putFragment(outState, "mMenu", mMenu);
	}
	
}
