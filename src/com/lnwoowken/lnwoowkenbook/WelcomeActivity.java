package com.lnwoowken.lnwoowkenbook;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.cncom.app.base.ui.PreferencesActivity;
import com.cncom.app.base.update.ServiceAppInfo;
import com.shwy.bestjoy.utils.DebugUtils;
import com.shwy.bestjoy.utils.FilesUtils;
import com.shwy.bestjoy.utils.Intents;


public class WelcomeActivity extends Activity {
	private static final String TAG = "WelcomeActivity";
	private Context mContext;
	private static final int DIALOG_MUST_INSTALL = 100001;
	private static final int DIALOG_CONFIRM_INSTALL = 100002;
	
	private ServiceAppInfo mServiceAppInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_welcome);
		mServiceAppInfo = new ServiceAppInfo();
		showHelpOnFirstLaunch();

	}
	
	/**
	 * We want the help screen to be shown automatically the first time a new
	 * version of the app is run. The easiest way to do this is to check
	 * android:versionCode from the manifest, and compare it to a value stored
	 * as a preference.
	 */
	private void showHelpOnFirstLaunch() {
		try {
			PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
			int currentVersion = info.versionCode;
			String currentVersionCodeName = info.versionName;

			int lastVersion = MyApplication.getInstance().mPreferManager.getInt(PreferencesActivity.KEY_LATEST_VERSION, 0);
			if (currentVersion != lastVersion) {// 安装好后第一次启动
				// 设置版本号
				DebugUtils.logD(TAG, "showHelpOnFirstLaunch");
				SharedPreferences.Editor edit = MyApplication.getInstance().mPreferManager.edit();
				edit.putInt(PreferencesActivity.KEY_LATEST_VERSION, currentVersion);
				edit.putString(PreferencesActivity.KEY_LATEST_VERSION_CODE_NAME, currentVersionCodeName);
				
				edit.putBoolean(PreferencesActivity.KEY_LATEST_VERSION_INSTALL, true);
				edit.putLong(PreferencesActivity.KEY_LATEST_VERSION_LEVEL, 0);
				edit.putLong(ServiceAppInfo.KEY_SERVICE_APP_INFO_CHECK_TIME, -1);
				edit.commit();
				//删除下载更新的临时目录，确保没有其他的安装包了
				File downloadFile = MyApplication.getInstance().getExternalStorageRoot(".download");
				if(downloadFile != null) FilesUtils.deleteFile(TAG, downloadFile);
			} else {// 不是第一次启动
					// 是否完成上次下载的更新的安装
				DebugUtils.logD(TAG, "not FirstLaunch");
				if (!mServiceAppInfo.hasChecked()) {
					DebugUtils.logD(TAG, "mServiceAppInfo is null, maybe we do not start to updating check");
				} else {
					File localApkFile = mServiceAppInfo.buildExternalDownloadAppFile();
					//如果更新包存在，并且更新包的版本高于当前版本，我们认为是下载了更新包当是没有安装
					if (localApkFile != null && localApkFile.exists() && mServiceAppInfo.mVersionCode > currentVersion) {
						if (!MyApplication.getInstance().mPreferManager.getBoolean(PreferencesActivity.KEY_LATEST_VERSION_INSTALL, true)) {
							// 是否放弃安装，如果放弃，且重要程度为1则不在进行提示，否则必须安装
							if (MyApplication.getInstance().mPreferManager.getLong(PreferencesActivity.KEY_LATEST_VERSION_LEVEL, ServiceAppInfo.IMPORTANCE_OPTIONAL) == ServiceAppInfo.IMPORTANCE_OPTIONAL) {
								showDialog(DIALOG_CONFIRM_INSTALL);
							} else {
								showDialog(DIALOG_MUST_INSTALL);
							}
							return;
						}
					}
				}
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		launchMainActivityNoDelay();
	}
	
	private void launchMainActivityNoDelay() {
		MyApplication.getInstance().postDelay(new Runnable() {

			@Override
			public void run() {
				MainActivity.startIntent(WelcomeActivity.this, null);
				WelcomeActivity.this.finish();
			}
			
		}, 1000);
		
	}
	
	@Override
	public Dialog onCreateDialog(int id) {
		switch(id) {
		case DIALOG_CONFIRM_INSTALL:
		case DIALOG_MUST_INSTALL:
			
			AlertDialog.Builder builder =  new AlertDialog.Builder(mContext)
			.setTitle(R.string.app_update_title)
			.setCancelable(false)
			.setMessage(R.string.app_update_not_install)
			.setPositiveButton(R.string.button_update_ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							File localApk = MyApplication.getInstance().buildLocalDownloadAppFile(mServiceAppInfo.mVersionCode);
							Intents.install(mContext, localApk);
						}
					});
			if (id == DIALOG_CONFIRM_INSTALL) {
				builder.setNegativeButton(R.string.button_update_no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								MyApplication.getInstance().mPreferManager.edit().putBoolean(PreferencesActivity.KEY_LATEST_VERSION_INSTALL, true).commit();
								launchMainActivityNoDelay();
							}
						});
			} else {
				builder.setNegativeButton(R.string.button_update_no,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialog,
									int which) {
								finish();
							}
						});
			}
			return builder.create();
			default:
				return super.onCreateDialog(id);
		}
		
	}
}
