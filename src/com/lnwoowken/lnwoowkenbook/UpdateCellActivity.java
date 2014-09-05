
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

public class UpdateCellActivity extends BaseActionbarActivity {
	private static final String TAG = "UpdateCellActivity";
	private TextView mCellView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_cell);
		
		mCellView = (TextView) findViewById(R.id.tel);
		String cell = MyAccountManager.getInstance().getDefaultPhoneNumber();
		if (!TextUtils.isEmpty(cell)) {
			StringBuilder sb = new StringBuilder(cell);
			cell = sb.replace(3, 7, "xxxx").toString();
		}
		mCellView.setText(cell);
	}
	
	
	
	public static void startActivity(Context context) {
		Intent intent = new Intent(context, UpdateCellActivity.class);
		context.startActivity(intent);
	}
	
	@Override
	protected boolean checkIntent(Intent intent) {
		return true;
	}

}
