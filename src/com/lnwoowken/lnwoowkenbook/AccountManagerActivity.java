package com.lnwoowken.lnwoowkenbook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.cncom.app.base.account.AccountObject;
import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.database.BjnoteContent;
import com.cncom.app.base.ui.BaseActionbarActivity;
import com.lnwoowken.lnwoowkenbook.view.MemberInfoItemLayout;
import com.shwy.bestjoy.utils.AsyncTaskUtils;

public class AccountManagerActivity extends BaseActionbarActivity{
	// private Button btn_regist;
	// private Context context = UserInfoActivity.this;
	private TextView userName;
	private TextView phoneNumber;
	private TextView mMemberJifen;
	private AccountObject mAccountObject;
	private ContentObserver mContentObserver;
	private ImageView mAvator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_manager);
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
		showHome(false);
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

	private void initialize() {
		mAvator = (ImageView) findViewById(R.id.imageView_photo);
		mAvator.setOnClickListener(this);
		
		userName = (TextView) findViewById(R.id.textView_username);
		phoneNumber = (TextView) findViewById(R.id.textView_phone_data);
		
		mMemberJifen = (TextView) findViewById(R.id.available);
		
		updateView();
	}
	
	public void updateView() {
		mAvator.setImageResource(MyAccountManager.getInstance().getAccountSystemAvatorResId());
		mAvator.setBackgroundResource(MyAccountManager.getInstance().getSystemAvatorBackgroudResId());
		
		userName.setText(mAccountObject.mAccountName);
		phoneNumber.setText(mAccountObject.mAccountTel);
		
		mMemberJifen.setText(mAccountObject.mAccountJifen);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.imageView_photo:
			//登陆后，可以修改头像
			if (MyAccountManager.getInstance().hasSystemAvator()) {
				UpdateAvatorActivity.startActivity(mContext, MyAccountManager.getInstance().getSystemAvatorIndex());
			}
			break;
			default:
				super.onClick(v);
		}
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
		Intent intent = new Intent(context, AccountManagerActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected boolean checkIntent(Intent intent) {
		return MyAccountManager.getInstance().hasLoginned();
	}
}
