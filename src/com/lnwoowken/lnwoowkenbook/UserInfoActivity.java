package com.lnwoowken.lnwoowkenbook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.cncom.app.base.account.AccountObject;
import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.database.BjnoteContent;
import com.cncom.app.base.ui.BaseActionbarActivity;
import com.lnwoowken.lnwoowkenbook.view.MemberInfoItemLayout;
import com.shwy.bestjoy.utils.AsyncTaskUtils;

public class UserInfoActivity extends BaseActionbarActivity{
	// private Button btn_regist;
	// private Context context = UserInfoActivity.this;
	private TextView userName;
	private TextView phoneNumber;
	private TextView mMemberJifen, mMemberLevel;
	private AccountObject mAccountObject;
	private MemberInfoItemLayout mMemberTelInfoItemLayout;
	private ContentObserver mContentObserver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_center);
		mAccountObject = MyAccountManager.getInstance().getAccountObject();
		initialize();

		mContentObserver = new ContentObserver(new Handler()) {
			@Override
			public void onChange(boolean selfChange) {
				super.onChange(selfChange);
				updateView();
			}
		};
		getContentResolver().registerContentObserver(BjnoteContent.Accounts.CONTENT_URI, true, mContentObserver);
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		getContentResolver().unregisterContentObserver(mContentObserver);
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
		mMemberTelInfoItemLayout = (MemberInfoItemLayout) findViewById(R.id.member_info_tel);
		
		updateView();
	}
	
	public void updateView() {
		userName.setText(mAccountObject.mAccountName);
		phoneNumber.setText(mAccountObject.mAccountTel);
		
		mMemberLevel.setText(MyAccountManager.getInstance().getMemberLevelResId());
		mMemberJifen.setText(mAccountObject.mAccountJifen);
		mMemberTelInfoItemLayout.setSummery(mAccountObject.mAccountTel);
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
