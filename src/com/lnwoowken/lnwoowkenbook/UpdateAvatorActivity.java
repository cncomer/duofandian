
/**
 * 修改手机号的ACTIVITY
 */
package com.lnwoowken.lnwoowkenbook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.ui.BaseActionbarActivity;

public class UpdateAvatorActivity extends BaseActionbarActivity {
	private static final String TAG = "UpdateAvatorActivity";
	private TextView mCellView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_avator);
	}
	
	
	
	public static void startActivity(Context context) {
		Intent intent = new Intent(context, UpdateAvatorActivity.class);
		context.startActivity(intent);
	}
	
	@Override
	protected boolean checkIntent(Intent intent) {
		return true;
	}

}
