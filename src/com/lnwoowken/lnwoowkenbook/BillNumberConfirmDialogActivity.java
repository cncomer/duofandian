package com.lnwoowken.lnwoowkenbook;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cncom.app.base.database.BjnoteContent;
import com.cncom.app.base.util.DebugUtils;
import com.lnwoowken.lnwoowkenbook.ServiceObject.ServiceResultObject;
import com.lnwoowken.lnwoowkenbook.model.BillObject;
import com.shwy.bestjoy.utils.AsyncTaskUtils;
import com.shwy.bestjoy.utils.Intents;
import com.shwy.bestjoy.utils.NetworkUtils;
import com.umeng.message.entity.UMessage;
/**
 * 
 * 订单确认流程
 * 
 * 服务端下发推送信息，包含如下内容
 *  orderno 
	msg_type =1 表示订单金额确认
	realfee实际消费金额
	dingjin 定金
	random随机数
	
	
	用户确定或则no,此时需要将结果反馈給后台，需要如下数据
	orderno
	random
	msg_type
	usr_op 用户选项 1-同意  0-不同意
 * 
 * @author chenkai
 *
 */
public class BillNumberConfirmDialogActivity extends Activity implements View.OnClickListener{

	private static final String TAG = "BillNumberConfirmDialogActivity";
    Context mContext;
    private TextView mTitleView;
    private FrameLayout mContentView;
    private Button mYesBtn, mNoBtn;
    private View.OnClickListener mButtonOnClickListener;
    private UMessage mUMessage;
    
    private View mProgressLayout;
    private TextView mProgressStatusTextView;
    
    private String mRandom;
    private String mBillNumber;
    private String mConfirmOp;
    private String mBillNumberConfirmType;
    
    private WakeLock mWakeLock;
    private KeyguardLock mKeyguardLock;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!checkIntent(getIntent())) {
        	finish();
        	return;
        }
        mContext = this;
        this.setContentView(R.layout.bill_number_confirm_dialog);
        mTitleView = (TextView) findViewById(R.id.title);
        
        if (TextUtils.isEmpty(mUMessage.title)) {
        	mTitleView.setVisibility(View.GONE);
        } else {
        	mTitleView.setText(mUMessage.title);
        }
        mContentView = (FrameLayout) findViewById(R.id.content);
        mProgressLayout = findViewById(R.id.progress_layout);
        mProgressStatusTextView = (TextView) mProgressLayout.findViewById(R.id.progress_status);
        mProgressStatusTextView.setText(R.string.progress_status_bill_number_confirm);
        
        mProgressLayout.setVisibility(View.GONE);
        
        mYesBtn = (Button) findViewById(R.id.button_accept);
        mNoBtn = (Button) findViewById(R.id.button_naccept);
        
        mYesBtn.setOnClickListener(this);
        mNoBtn.setOnClickListener(this);
        mBillNumberConfirmType = mUMessage.extra.get("msg_type");
        int type = Integer.valueOf(mBillNumberConfirmType);
        View view = null;
        switch(type){
        case 1:
        	view = LayoutInflater.from(mContext).inflate(R.layout.bill_number_confirm_dialog_type1, mContentView, false);
        	mContentView.addView(view);
        	mBillNumber = mUMessage.extra.get("orderno");
        	mRandom = mUMessage.extra.get("random");
    		String realFee = mUMessage.extra.get("realfee");
    	    String backFee = mUMessage.extra.get("dingjin");
    		TextView textView = (TextView) view.findViewById(R.id.bill_number_input);
    		textView.setText(mBillNumber);
    		textView = (TextView) view.findViewById(R.id.bill_fee_input);
    		textView.setText(mContext.getString(R.string.format_title_real_fee, realFee));
    		
    		textView = (TextView) view.findViewById(R.id.bill_back_input);
    		textView.setText(mContext.getString(R.string.format_title_real_fee, backFee));
        	break;
        }
        
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, TAG);
		if (!mWakeLock.isHeld()) {
			mWakeLock.acquire(10000);
		}
		
		KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);  
        //得到键盘锁管理器对象  
		mKeyguardLock = km.newKeyguardLock("unLock");
		
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	mKeyguardLock.disableKeyguard(); 
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    }
    @Override
    public void onStop() {
    	super.onStop();
    	mKeyguardLock.reenableKeyguard();
    	if (mWakeLock.isHeld()) {
    		mWakeLock.release();
    	}
    }
    
    
    
    @Override
    public void onBackPressed() {
    	
    }
    
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.button_accept:
			mConfirmOp="1";
			break;
		case R.id.button_naccept:
			mConfirmOp="0";
			break;
		}
		updateBillConfirmAsync();
		
	}
	protected boolean checkIntent(Intent intent) {
		String rawMessage = intent.getStringExtra(Intents.EXTRA_RESULT);
		if (!TextUtils.isEmpty(rawMessage)) {
			try {
				mUMessage = new UMessage(new JSONObject(rawMessage));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return mUMessage != null;
	}
	
	public static void startActivity(Context context, UMessage uessage) {
		Intent intent = new Intent(context, BillNumberConfirmDialogActivity.class);
		intent.putExtra(Intents.EXTRA_RESULT, uessage.getRaw().toString());
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}
	private UpdateBillConfirmTask mUpdateBillConfirmTask;
	private void updateBillConfirmAsync() {
		AsyncTaskUtils.cancelTask(mUpdateBillConfirmTask);
		mUpdateBillConfirmTask = new UpdateBillConfirmTask();
		mUpdateBillConfirmTask.execute();
		mProgressLayout.setVisibility(View.VISIBLE);
	}
	private class UpdateBillConfirmTask extends AsyncTask<Void, Void, ServiceResultObject> {
		
		@Override
		protected ServiceResultObject doInBackground(Void... params) {
			InputStream is = null;
			ServiceResultObject serviceObject = new ServiceResultObject();
			try {
				JSONObject queryJsonObject = new JSONObject();
				queryJsonObject.put("orderno", mBillNumber);
				queryJsonObject.put("random", mRandom);
				queryJsonObject.put("msg_type", mBillNumberConfirmType);
				queryJsonObject.put("usr_op", mConfirmOp);
				DebugUtils.logD(TAG, "UpdateBillConfirmTask doInBackground() queryJsonObject=" + queryJsonObject.toString());
				is = NetworkUtils.openContectionLocked(ServiceObject.getUpdateUserLoginPasswordUrl("para", queryJsonObject.toString()), MyApplication.getInstance().getSecurityKeyValuesObject());
				
				serviceObject = ServiceResultObject.parse(NetworkUtils.getContentFromInput(is));
				if (serviceObject.isOpSuccessfully()) {
					long id = BjnoteContent.existed(getContentResolver(), BjnoteContent.Bills.CONTENT_URI, BillObject.BILL_NUMBER_SELECTION, new String[]{mBillNumber});
					DebugUtils.logD(TAG, "UpdateBillConfirmTask find existed billNumber " + mBillNumber + ", id =" + id);
					if (id > 0) {
						ContentValues values = new ContentValues();
						values.put(BillObject.BILL_STATE, BillObject.STATE_XIAOFEI);
						int updated =BjnoteContent.update(getContentResolver(), BjnoteContent.Bills.CONTENT_URI, values, BillObject.BILL_NUMBER_SELECTION, new String[]{mBillNumber});
						
						DebugUtils.logD(TAG, "update existed billNumber updated# " + updated);
					}
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
			mProgressLayout.setVisibility(View.GONE);
			if (!result.isOpSuccessfully()) {
				MyApplication.getInstance().showMessage(result.mStatusMessage);
			} else {
				finish();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			mProgressLayout.setVisibility(View.GONE);
		}
		
	}

}
