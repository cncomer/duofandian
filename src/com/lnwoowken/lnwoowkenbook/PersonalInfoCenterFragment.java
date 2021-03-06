package com.lnwoowken.lnwoowkenbook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.module.ModuleSettings;
import com.cncom.app.base.ui.BaseFragment;
import com.shwy.bestjoy.utils.AsyncTaskUtils;

public class PersonalInfoCenterFragment extends BaseFragment implements View.OnClickListener{

	private static final String TAG = "PersonalInfoCenterFragment";
	private ImageView mAvator;
	/**登陆后的顶部会员信息，和游客顶部信息*/
	private View mMemberTopLayout, mGuestTopLayout;
	
	private TextView mMemberName, mMemberLevel, mMemberTel;
	private ContentObserver mContentObserver;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
		
//		mContentObserver = new ContentObserver(new Handler()) {
//			@Override
//			public void onChange(boolean selfChange) {
//				super.onChange(selfChange);
//				DebugUtils.logD(TAG, "onChange()--Menu[menu_my_order] has changed--showNewFlag=" + BillListManager.isShowNew());
//				//订单发生变化了，我们要标记new状态
//				View view = getActivity().findViewById(R.id.menu_my_order);
//				if (view != null) {
//					View newFlagView = view.findViewById(R.id.imageView_myBill_tips);
//					if(BillListManager.isShowNew()) {
//						newFlagView.setVisibility(View.VISIBLE);
//					} else {
//						newFlagView.setVisibility(View.GONE);
//					}
//				}
//			}
//		};
//		
//		getActivity().getContentResolver().registerContentObserver(BjnoteContent.Bills.CONTENT_URI, true, mContentObserver);
		
	}

	@Override
	public void onResume() {
		super.onResume();
		updateViews();
		updateViewNewInfo();
		getActivity().invalidateOptionsMenu();
		
	}
	
	private void updateViewNewInfo() {
		//订单发生变化了，我们要标记new状态
		View view = getActivity().findViewById(R.id.menu_my_order);
		if (view != null) {
			View newFlagView = view.findViewById(R.id.imageView_myBill_tips);
			if(BillListManager.isShowNew()) {
				newFlagView.setVisibility(View.VISIBLE);
			} else {
				newFlagView.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
//		getActivity().getContentResolver().unregisterContentObserver(mContentObserver);
	}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	menu.add(1000, R.string.menu_login, 0,  R.string.menu_login).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);//login
		menu.add(1000, R.string.exit_login, 1,  R.string.exit_login).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        //Nothing to see here.
    }
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		boolean hasLogined = MyAccountManager.getInstance().hasLoginned();
		menu.findItem(R.string.menu_login).setVisible(!hasLogined);
		menu.findItem(R.string.exit_login).setVisible(hasLogined);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
        case R.string.menu_login:
        	Intent intent = new Intent(getActivity(), LoginActivity.class);
			startActivity(intent);
        	return true;
        case R.string.exit_login:
        	new AlertDialog.Builder(getActivity())
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.left_panel, container, false);
		mMemberTopLayout = view.findViewById(R.id.member_top_layout);
		mMemberTopLayout.setOnClickListener(this);
		mAvator = (ImageView) view.findViewById(R.id.avator);
		mAvator.setOnClickListener(this);
		
		mMemberName = (TextView) view.findViewById(R.id.memberName);
		mMemberLevel = (TextView) view.findViewById(R.id.memberLevel);
		mMemberTel = (TextView) view.findViewById(R.id.memberTel);
		
		mGuestTopLayout = view.findViewById(R.id.guest_top_layout);

		mMemberTopLayout.setOnClickListener(this);
		mGuestTopLayout.setOnClickListener(this);
		
		ModuleSettings.getInstance().installModule((ViewGroup) view.findViewById(R.id.menu_content), this);
		return view;
	}
	
	public void updateViews() {
		if (MyAccountManager.getInstance().hasLoginned()) {
			mMemberTopLayout.setVisibility(View.VISIBLE);
			mGuestTopLayout.setVisibility(View.GONE);
			
			mMemberName.setText(MyAccountManager.getInstance().getAccountName());
			mMemberLevel.setText(MyAccountManager.getInstance().getMemberLevelResId());
			mMemberTel.setText(MyAccountManager.getInstance().getAccountObject().mAccountTel);
			getActivity().setTitle(MyAccountManager.getInstance().getAccountObject().mAccountTel);
		} else {
			mMemberTopLayout.setVisibility(View.GONE);
			mGuestTopLayout.setVisibility(View.VISIBLE);
			getActivity().setTitle("");
		}
		
		mAvator.setImageResource(MyAccountManager.getInstance().getAccountSystemAvatorResId());
		mAvator.setBackgroundResource(MyAccountManager.getInstance().getSystemAvatorBackgroudResId());
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.avator:
			//登陆后，可以修改头像
			if (MyAccountManager.getInstance().hasSystemAvator()) {
				UpdateAvatorActivity.startActivity(getActivity(), MyAccountManager.getInstance().getSystemAvatorIndex());
			}
			break;
		case R.id.guest_top_layout:
			LoginActivity.startActivity(getActivity());
			break;
		case R.id.member_top_layout:
			UserInfoActivity.startActivity(getActivity());
			break;
		case R.id.menu_duo_seat:
			Intent intent = new Intent(getActivity(), RestaurantListActivity.class);
			intent.putExtra("flag_from", "main");
			getActivity().startActivity(intent);
			break;
		case R.id.menu_my_order:
			if (!MyAccountManager.getInstance().hasLoginned()) {
				LoginActivity.startActivity(getActivity());
				MyApplication.getInstance().showNeedLoginMessage();
			} else {
				BillListActivity.startActivity(getActivity());
			}
			break;
		case R.id.menu_settings:
			SettingsActivity.startActivity(getActivity());
			break;
		case R.id.menu_my_account:
			if (MyAccountManager.getInstance().hasLoginned()) {
				AccountManagerActivity.startActivity(getActivity());
			} else {
				MyApplication.getInstance().showNeedLoginMessage();
			}
			
			break;
		case R.id.menu_order_dishes:
			default:
				MyApplication.getInstance().showUnsupportMessage();
				break;
			
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
			getActivity().invalidateOptionsMenu();
			updateViews();
			MyApplication.getInstance().showMessage(R.string.msg_op_successed);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			getActivity().invalidateOptionsMenu();
			updateViews();
			dismissDialog(DIALOG_PROGRESS);
		}
		
		
		 
	 }

}
