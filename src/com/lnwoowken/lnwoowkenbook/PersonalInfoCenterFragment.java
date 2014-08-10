package com.lnwoowken.lnwoowkenbook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.ui.BaseFragment;

public class PersonalInfoCenterFragment extends BaseFragment implements View.OnClickListener{

	private static final String TAG = "PersonalInfoCenterFragment";
	private ImageView mAvator;
	/**登陆后的顶部会员信息，和游客顶部信息*/
	private View mMemberTopLayout, mGuestTopLayout;
	
	private TextView mMemberName, mMemberLevel, mMemberTel;
	
	@Override
	public void onResume() {
		super.onResume();
		updateViews();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.left_panel, container, false);
		view.findViewById(R.id.top_layout).setOnClickListener(this);
		mMemberTopLayout = view.findViewById(R.id.member_top_layout);
		mAvator = (ImageView) view.findViewById(R.id.avator);
		mAvator.setOnClickListener(this);
		
		mMemberName = (TextView) view.findViewById(R.id.memberName);
		mMemberLevel = (TextView) view.findViewById(R.id.memberLevel);
		mMemberTel = (TextView) view.findViewById(R.id.memberTel);
		
		mGuestTopLayout = view.findViewById(R.id.guest_top_layout);
		
		
		return view;
	}
	
	public void updateViews() {
		if (MyAccountManager.getInstance().hasLoginned()) {
			mMemberTopLayout.setVisibility(View.VISIBLE);
			mGuestTopLayout.setVisibility(View.GONE);
			
			mMemberName.setText(MyAccountManager.getInstance().getAccountObject().mAccountName);
			mMemberLevel.setText(MyAccountManager.getInstance().getAccountObject().mAccountLevel);
			mMemberTel.setText(MyAccountManager.getInstance().getAccountObject().mAccountTel);
		} else {
			mMemberTopLayout.setVisibility(View.GONE);
			mGuestTopLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.top_layout:
			if (MyAccountManager.getInstance().hasLoginned()) {
				UserInfoActivity.startActivity(getActivity());
			} else {
				LoginActivity.startActivity(getActivity());
			}
			break;
		case R.id.avator:
			//登陆后，可以修改头像
			if (MyAccountManager.getInstance().hasLoginned()) {
				
			}
			break;
		case R.id.guest_top_layout:
			LoginActivity.startActivity(getActivity());
			break;
		case R.id.member_top_layout:
			break;
		}
		
	}
	

}
