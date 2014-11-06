package com.lnwoowken.lnwoowkenbook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.ui.BaseActivity;
import com.cncom.app.base.ui.PreferencesActivity;
import com.cncom.app.base.update.ServiceAppInfo;
import com.cncom.app.base.update.UpdateActivity;
import com.cncom.app.base.update.UpdateService;
import com.shwy.bestjoy.utils.DebugUtils;

public class SettingsActivity extends BaseActivity implements View.OnClickListener{
	private static final String TAG = "SettingsActivity";
	private ServiceAppInfo mServiceAppInfo;
	private TextView mVersionName, mUpdateStatus;
	private int mCurrentVersion;
	private String mCurrentVersionCodeName; 
	private LinearLayout mButtonUpdate;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_settings);
	    mHomeBtn.setVisibility(View.INVISIBLE);
	    
	    SharedPreferences prefs = MyApplication.getInstance().mPreferManager;
		mCurrentVersion = prefs.getInt(PreferencesActivity.KEY_LATEST_VERSION, 0);
		mCurrentVersionCodeName = prefs.getString(PreferencesActivity.KEY_LATEST_VERSION_CODE_NAME, "");
		
		mServiceAppInfo = new ServiceAppInfo();
		
		mVersionName = (TextView) findViewById(R.id.app_version_name);
		mVersionName.setText(getString(R.string.format_current_sw_version, mCurrentVersionCodeName));
		mUpdateStatus = (TextView) findViewById(R.id.desc_update);
		mButtonUpdate = (LinearLayout) findViewById(R.id.button_update);
		mButtonUpdate.setOnClickListener(this);
		if (mServiceAppInfo != null && mServiceAppInfo.mVersionCode > mCurrentVersion) {
			//发现新版本
			mButtonUpdate.setEnabled(true);
			mUpdateStatus.setText(getString(R.string.format_latest_version, mServiceAppInfo.mVersionName));
		} else {
			//已经是最新的版本了
			mButtonUpdate.setEnabled(false);
			mUpdateStatus.setText(R.string.msg_app_has_latest);
		}
		
	    findViewById(R.id.button_about_me).setOnClickListener(this);
	    findViewById(R.id.button_clear_cache).setOnClickListener(this);
	    
	    findViewById(R.id.button_feedback).setOnClickListener(this);
	    UpdateService.startUpdateServiceForce(mContext);
	}
	

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.button_clear_cache:
			break;
		case R.id.button_about_me:
			AboutActivity.startActivity(mContext);
			break;
		case R.id.button_update:
			if (mServiceAppInfo != null) {
				startActivity(UpdateActivity.createIntent(mContext, mServiceAppInfo));
			} else {
				DebugUtils.logE(TAG, "mServiceAppInfo == null, so we ignore update click");
			}
			break;
		case R.id.button_feedback:
			FeedbackActivity.startActivity(mContext, MyAccountManager.getInstance().getCurrentAccountId());
			break;
		}
	}

	@Override
	protected boolean checkIntent(Intent intent) {
		return true;
	}
	
	public static void startActivity(Context context) {
		Intent intent = new Intent(context, SettingsActivity.class);
		context.startActivity(intent);
	}

}
