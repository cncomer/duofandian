package com.lnwoowken.lnwoowkenbook.view;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cncom.app.base.account.AccountObject;
import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.database.DBHelper;
import com.cncom.app.base.util.DebugUtils;
import com.lnwoowken.lnwoowkenbook.MyApplication;
import com.lnwoowken.lnwoowkenbook.R;
import com.lnwoowken.lnwoowkenbook.ServiceObject;
import com.lnwoowken.lnwoowkenbook.UpdateAvatorActivity;
import com.lnwoowken.lnwoowkenbook.UpdateCellActivity;
import com.lnwoowken.lnwoowkenbook.ServiceObject.ServiceResultObject;
import com.lnwoowken.lnwoowkenbook.UpdatePasswordActivity;
import com.shwy.bestjoy.utils.AsyncTaskUtils;
import com.shwy.bestjoy.utils.NetworkUtils;

public class MemberInfoItemLayout extends LinearLayout{

	private static final String TAG = "MemberInfoItemLayout";
	private TextView mTitleView, mSummeryView;
	private ImageView mRightArrowImage;
	private TextPaint mTextPaint;
	private Context mContext;
	public MemberInfoItemLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.member_info_item_layout, this, true);
		mTitleView = (TextView) findViewById(R.id.title);
		mSummeryView = (TextView) findViewById(R.id.summery);
		mRightArrowImage = (ImageView) findViewById(R.id.icon);
		
		 TypedArray a = context.obtainStyledAttributes(
                 attrs, R.styleable.MemberInfoItem, 0, 0);
		 
		 mTextPaint = mTitleView.getPaint();
		 mTextPaint.setFakeBoldText(true);
		 mTitleView.setText(a.getString(R.styleable.MemberInfoItem___title));
		
		 if (this.isInEditMode()) {
			 return;
		 }
		 mSummeryView.setText(a.getString(R.styleable.MemberInfoItem___summery));
//		 mRightArrowImage.setImageDrawable(a.getDrawable(R.styleable.MemberInfoItem___icon));
		 
		 a.recycle();
		 
		 this.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch(v.getId()) {
				case R.id.member_info_avator:   //会员头像
					UpdateAvatorActivity.startActivity(mContext);
					break;
				case R.id.member_info_name:   //会员昵称
					showRenameDialog();
					break;
				case R.id.member_info_login_password:   //会员登录密码
					UpdatePasswordActivity.startActivity(mContext, null);
					break;
				case R.id.member_info_pay_password:   //会员支付密码
					break;
				case R.id.member_info_tel:   //会员电话
					UpdateCellActivity.startActivity(mContext);
					break;
				}
			}
		});
		
	}
	
	public void setSummery(int resId) {
		mSummeryView.setText(resId);
	}
	public void setSummery(CharSequence text) {
		mSummeryView.setText(text);
	}
	
	public void showRenameDialog() {
		View view = LayoutInflater.from(mContext).inflate(R.layout.rename_dialog, null);
		final EditText nameInput =  (EditText) view.findViewById(R.id.name_input);
		nameInput.setText(MyAccountManager.getInstance().getAccountName());
		
		final Dialog dialog = new AlertDialog.Builder(mContext)
		.setView(view)
		.show();
		
		Button cancelBtn = (Button) view.findViewById(R.id.button_cancel);
		Button modifyBtn = (Button) view.findViewById(R.id.button_modify);
		
		View.OnClickListener clickListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch(v.getId()) {
				case R.id.button_cancel:
					dialog.dismiss();
					break;
				case R.id.button_modify:
					String newName = nameInput.getText().toString().trim();
					if (TextUtils.isEmpty(newName)) {
						MyApplication.getInstance().showMessage(R.string.attention_empty_name);
						return;
					}
					DebugUtils.logD(TAG, "newName=" + newName + ", and oldName=" + MyAccountManager.getInstance().getAccountName());
					if (!newName.equals(MyAccountManager.getInstance().getAccountName())) {
						updateAccounNameAsync(newName);
					}
					dialog.dismiss();
					break;
				}
			}
		};
		
		cancelBtn.setOnClickListener(clickListener);
		modifyBtn.setOnClickListener(clickListener);
	}
	
	
	private UpdateAccountNameAsyncTask mUpdateAccountNameAsyncTask;
	private void updateAccounNameAsync(String newName) {
		AsyncTaskUtils.cancelTask(mUpdateAccountNameAsyncTask);
		mUpdateAccountNameAsyncTask = new UpdateAccountNameAsyncTask();
		mUpdateAccountNameAsyncTask.execute(newName);
	}
	
	private class UpdateAccountNameAsyncTask extends AsyncTask<String, Void, ServiceResultObject> {

		@Override
		protected ServiceResultObject doInBackground(String... params) {
			InputStream is = null;
			ServiceResultObject serviceObject = new ServiceResultObject();
			try {
				AccountObject accountObject = MyAccountManager.getInstance().getAccountObject();
				JSONObject queryJsonObject = new JSONObject();
				queryJsonObject.put("UID", accountObject.mAccountUid);
				queryJsonObject.put("UserName", params[0]);
				
				DebugUtils.logD(TAG, "UpdateAccountNameAsyncTask doInBackground() queryJsonObject=" + queryJsonObject.toString());
				is = NetworkUtils.openContectionLocked(ServiceObject.getUpdateUserNameUrl("para", queryJsonObject.toString()), MyApplication.getInstance().getSecurityKeyValuesObject());
				
				serviceObject = ServiceResultObject.parse(NetworkUtils.getContentFromInput(is));
				if (serviceObject.isOpSuccessfully()) {
					DebugUtils.logD(TAG, "UpdateAccountNameAsyncTask--updateName successfully. start save new name.");
					accountObject.mAccountName = params[0];
					ContentValues values = new ContentValues();
					values.put(DBHelper.ACCOUNT_NAME, params[0]);
					accountObject.updateAccount(mContext.getContentResolver(), values);
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
				NetworkUtils.closeInputStream(is);
			}
			return serviceObject;
		}

		@Override
		protected void onPostExecute(ServiceResultObject result) {
			super.onPostExecute(result);
			MyApplication.getInstance().showMessage(result.mStatusMessage);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			MyApplication.getInstance().showMessage(R.string.msg_op_canceled);
		}
		
	}

}
