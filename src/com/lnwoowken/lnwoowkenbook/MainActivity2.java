package com.lnwoowken.lnwoowkenbook;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.ui.BaseSlidingFragmentActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.shwy.bestjoy.utils.DebugUtils;
/**
 * @author chenkai
 *
 */
public class MainActivity2 extends BaseSlidingFragmentActivity implements 
	SlidingMenu.OnOpenedListener, SlidingMenu.OnClosedListener{
	private static final String TAG = "MainActivity2";
	private MainActivityContentFragment mContent;
	private PersonalInfoCenterFragment mMenu;
	private Bundle mBundles;
	/**琛ㄧず鏄惁鏄涓�杩涘叆*/
	private static final String  KEY_FIRST_SHOW = "MainActivity2.first";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DebugUtils.logD(TAG, "onCreate()");
		if (isFinishing()) {
			return ;
		}
		
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
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		getSupportMenuInflater().inflate(R.menu.new_card_activity_menu, menu);
		menu.add(1000, R.string.menu_login, 0,  R.string.menu_login).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);//login
		menu.add(1000, R.string.exit_login, 1,  R.string.exit_login).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean hasLogined = MyAccountManager.getInstance().hasLoginned();
		menu.findItem(R.string.menu_login).setVisible(!hasLogined);
		menu.findItem(R.string.exit_login).setVisible(hasLogined);
		return true;
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
        case android.R.id.home:
     	   Intent upIntent = NavUtils.getParentActivityIntent(this);
     	   if (upIntent == null) {
     		   // If we has configurated parent Activity in AndroidManifest.xml, we just finish current Activity.
     		   finish();
     		   return true;
     	   }
            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                // This activity is NOT part of this app's task, so create a new task
                // when navigating up, with a synthesized back stack.
                TaskStackBuilder.create(this)
                        // Add all of this activity's parents to the back stack
                        .addNextIntentWithParentStack(upIntent)
                        // Navigate up to the closest parent
                        .startActivities();
            } else {
                // This activity is part of this app's task, so simply
                // navigate up to the logical parent activity.
                NavUtils.navigateUpTo(this, upIntent);
            }
            return true;
        case R.string.menu_login:
        	Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
        	return true;
        case R.string.exit_login:
        	showExitLoginDialog();
        	return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onOpened() {
		//褰揝lidingMenu鎵撳紑鍚庯紝鎴戜滑闇�闅愯棌鎺夋墜鍔ㄦ墦寮�lidinMenu鎸夐挳
	}


	@Override
	public void onClosed() {
		//褰揝lidingMenu鍏抽棴鍚庯紝鎴戜滑闇�閲嶆柊鏄剧ず鎵嬪姩鎵撳紑SlidinMenu鎸夐挳
		
	}
	
	public void onNewIntent(Intent intent){
		super.onNewIntent(intent);
		DebugUtils.logD(TAG, "onNewIntent " + intent);
	}
	
	public static void startIntent(Context context, Bundle bundle) {
		Intent intent = new Intent(context, MainActivity2.class);
		if (bundle != null) intent.putExtras(bundle);
		context.startActivity(intent);
	}
	
	public static void startIntentClearTop(Context context, Bundle bundle) {
		Intent intent = new Intent(context, MainActivity2.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		if (bundle != null) intent.putExtras(bundle);
		context.startActivity(intent);
	}
	
	private void showExitLoginDialog() {
	    new AlertDialog.Builder(this)
			.setTitle("鎻愮ず")
			.setMessage("鎮ㄥ凡缁忕櫥褰�鏄惁瑕侀�鍑洪噸鏂扮櫥褰�")
			.
			// setIcon(R.drawable.welcome_logo).
			setPositiveButton("纭畾", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					MyAccountManager.getInstance().deleteDefaultAccount();
					Intent in = new Intent();
					in.setAction("login");
					sendBroadcast(in);
					Toast.makeText(mContext, "鎴愬姛閫�嚭鐧诲綍", Toast.LENGTH_SHORT).show();
				}
			})
			.setNegativeButton("鍙栨秷", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			}).show();
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
			DebugUtils.logD(TAG, "checkIntent failed, due to mBundles is null");
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
