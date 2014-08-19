package com.lnwoowken.lnwoowkenbook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.cncom.app.base.account.AccountObject;
import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.ui.BaseActionbarActivity;
import com.shwy.bestjoy.utils.AsyncTaskUtils;

public class UserInfoActivity extends BaseActionbarActivity implements OnClickListener {
	// private Button btn_regist;
	// private Context context = UserInfoActivity.this;
	private TextView userName;
	private TextView phoneNumber;
	private TextView mMemberJifen, mMemberLevel;
	private RelativeLayout r1, r2, r3, r4, r5, r6;
	private AccountObject mAccountObject;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_center);
		mAccountObject = MyAccountManager.getInstance().getAccountObject();
		initialize();

	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(1000, R.string.exit_login, 1,  R.string.exit_login).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
        case R.string.exit_login:
        	new AlertDialog.Builder(this)
        	.setTitle(R.string.msg_existing_system_confirm_title)
			.setMessage(R.string.msg_existing_system_confirm)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					deleteAccountAsync();
				}
			})
			.setNegativeButton(android.R.string.cancel, null)
			.show();
        	return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String text = "敬请期待！";
		if (v.equals(r1)) {
			Toast.makeText(UserInfoActivity.this, text, 1).show();
			// Toast.makeText(UserInfoActivity.this, text,Toast.LENGTH_SHORT).show();
		}
		if (v.equals(r2)) {
			Toast.makeText(UserInfoActivity.this, text, 1).show();
		}
		if (v.equals(r3)) {
			Toast.makeText(UserInfoActivity.this, text, 1).show();
		}
		if (v.equals(r4)) {
			Toast.makeText(UserInfoActivity.this, text, 1).show();
		}
		if (v.equals(r5)) {
			Toast.makeText(UserInfoActivity.this, text, 1).show();
		}
		if (v.equals(r6)) {
			Toast.makeText(UserInfoActivity.this, text, 1).show();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		//
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			UserInfoActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initialize() {
		userName = (TextView) findViewById(R.id.textView_username);
		phoneNumber = (TextView) findViewById(R.id.textView_phone_data);
		
		mMemberLevel = (TextView) findViewById(R.id.textView5);
		mMemberJifen = (TextView) findViewById(R.id.textView7);
		
		r1 = (RelativeLayout) findViewById(R.id.r1);
		r2 = (RelativeLayout) findViewById(R.id.r2);
		r3 = (RelativeLayout) findViewById(R.id.r3);
		r4 = (RelativeLayout) findViewById(R.id.r4);
		r5 = (RelativeLayout) findViewById(R.id.r5);
		r6 = (RelativeLayout) findViewById(R.id.r6);
		r1.setOnClickListener(UserInfoActivity.this);
		r2.setOnClickListener(UserInfoActivity.this);
		r3.setOnClickListener(UserInfoActivity.this);
		r4.setOnClickListener(UserInfoActivity.this);
		r5.setOnClickListener(UserInfoActivity.this);
		r6.setOnClickListener(UserInfoActivity.this);
		
		
		userName.setText(mAccountObject.mAccountName);
		phoneNumber.setText(mAccountObject.mAccountTel);
		
		mMemberLevel.setText(mAccountObject.mAccountLevel);
		mMemberJifen.setText(mAccountObject.mAccountJifen);
	}
	
	 private DeleteAccountTask mDeleteAccountTask;
	 private void deleteAccountAsync() {
		 AsyncTaskUtils.cancelTask(mDeleteAccountTask);
		 showDialog(DIALOG_PROGRESS);
		 mDeleteAccountTask = new DeleteAccountTask();
		 mDeleteAccountTask.execute();
	 }
	 private class DeleteAccountTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			MyAccountManager.getInstance().deleteDefaultAccount();
			MyAccountManager.getInstance().saveLastUsrTel("");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			dismissDialog(DIALOG_PROGRESS);
			MyApplication.getInstance().showMessage(R.string.msg_op_successed);
			finish();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			dismissDialog(DIALOG_PROGRESS);
		}
		
		
		 
	 }

	
	public static void startActivity(Context context) {
		Intent intent = new Intent(context, UserInfoActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected boolean checkIntent(Intent intent) {
		return true;
	}
}
