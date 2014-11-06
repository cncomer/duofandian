package com.lnwoowken.lnwoowkenbook;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.cncom.app.base.account.AccountObject;
import com.cncom.app.base.ui.BaseActivity;
import com.shwy.bestjoy.utils.FeedbackHelper;
import com.shwy.bestjoy.utils.Intents;
import com.shwy.bestjoy.utils.NetworkUtils;
import com.shwy.bestjoy.utils.ServiceResultObject;
public class FeedbackActivity extends BaseActivity implements View.OnClickListener{

	private static final String TAG = "LoginOrUpdateAccountDialog";
	private AccountObject mAccountObject;
	private EditText mFeedbackInput, mEmailInput;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		mHomeBtn.setVisibility(View.INVISIBLE);
		mFeedbackInput = (EditText) findViewById(R.id.feedback);
		mEmailInput = (EditText) findViewById(R.id.email);
		
		findViewById(R.id.button_commit).setOnClickListener(this);
	}

	public static void startActivity(Context context, long accountUid) {
		Intent intent = new Intent(context, FeedbackActivity.class);
		intent.putExtra(Intents.EXTRA_ID, accountUid);
		context.startActivity(intent);
	}

	@Override
	protected boolean checkIntent(Intent intent) {
		long uid = intent.getLongExtra(Intents.EXTRA_ID, -1);
		
		if (uid == -1) {
			mAccountObject = new AccountObject();
		} else{
			mAccountObject = AccountObject.getAccountFromDatabase(mContext, uid);
		}
		return mAccountObject != null;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.button_commit:
			final String message = mFeedbackInput.getText().toString().trim();
			 if (!TextUtils.isEmpty(message)) {
				 FeedbackHelper.AbstractFeedbackObject feedbackObject = new FeedbackHelper.AbstractFeedbackObject() {

					@Override
					public void onFeedbackEnd(ServiceResultObject serviceResultObject) {
						dismissDialog(DIALOG_PROGRESS);
						MyApplication.getInstance().showMessage(serviceResultObject.mStatusMessage);
						if (serviceResultObject.isOpSuccessfully()) {
							finish();
						}
					}

					@Override
					public void onFeedbackStart() {
						showDialog(DIALOG_PROGRESS);
					}

					@Override
					public void onFeedbackonCancelled() {
						dismissDialog(DIALOG_PROGRESS);
					}

					@Override
					public InputStream openConnection()
							throws ClientProtocolException, JSONException, IOException {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("uid", String.valueOf(mAccountObject.mAccountUid))
						.put("content", message)
						.put("email", mEmailInput.getText().toString().trim());
						return NetworkUtils.openContectionLocked(ServiceObject.getFeedbackUrl("para", jsonObject.toString()), MyApplication.getInstance().getSecurityKeyValuesObject());
					}
					 
				 };
				 FeedbackHelper.getInstance().feedbackAsync(feedbackObject);
				 break;
			 }
		}
		
	}
	
}
