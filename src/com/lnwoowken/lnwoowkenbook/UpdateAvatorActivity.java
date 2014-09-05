
/**
 * 修改头像的ACTIVITY
 */
package com.lnwoowken.lnwoowkenbook;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.cncom.app.base.account.AccountObject;
import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.database.DBHelper;
import com.cncom.app.base.ui.BaseActionbarActivity;
import com.cncom.app.base.util.DebugUtils;
import com.lnwoowken.lnwoowkenbook.ServiceObject.ServiceResultObject;
import com.shwy.bestjoy.utils.AsyncTaskUtils;
import com.shwy.bestjoy.utils.NetworkUtils;

public class UpdateAvatorActivity extends BaseActionbarActivity {
	private static final String TAG = "UpdateAvatorActivity";
	private GridView mGridView;
	private int mSelectedPosition = -1;
	private AvatorAdapter mAvatorAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_avator);
		mGridView = (GridView) findViewById(R.id.gridview);
		mAvatorAdapter = new AvatorAdapter();
		mGridView.setAdapter(mAvatorAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (mSelectedPosition != position) {
					mSelectedPosition = position;
					mAvatorAdapter.notifyDataSetChanged();
				}
			}
			
		});
		
		findViewById(R.id.button_modify).setOnClickListener(this);
		findViewById(R.id.button_cancel).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.button_modify:
			updateAccounAvatorAsync();
			break;
		case R.id.button_cancel:
			finish();
			break;
			default:
				super.onClick(v);
		}
	}
	
	private UpdateAccountAvatorAsyncTask mUpdateAccountAvatorAsyncTask;
	private void updateAccounAvatorAsync() {
		AsyncTaskUtils.cancelTask(mUpdateAccountAvatorAsyncTask);
		mUpdateAccountAvatorAsyncTask = new UpdateAccountAvatorAsyncTask();
		mUpdateAccountAvatorAsyncTask.execute();
		showDialog(DIALOG_PROGRESS);
	}
	
	private class UpdateAccountAvatorAsyncTask extends AsyncTask<String, Void, ServiceResultObject> {

		@Override
		protected ServiceResultObject doInBackground(String... params) {
			InputStream is = null;
			ServiceResultObject serviceObject = new ServiceResultObject();
			try {
				AccountObject accountObject = MyAccountManager.getInstance().getAccountObject();
				JSONObject queryJsonObject = new JSONObject();
				queryJsonObject.put("uid", accountObject.mAccountUid);
				//由于选中的图像索引对应位置+1了，所以，我么需要在当前位置上+1等于头像索引
				queryJsonObject.put("img", mSelectedPosition+1);
				
				DebugUtils.logD(TAG, "UpdateAccountAvatorAsyncTask doInBackground() queryJsonObject=" + queryJsonObject.toString());
				is = NetworkUtils.openContectionLocked(ServiceObject.getUpdateUserAvatorUrl("para", queryJsonObject.toString()), MyApplication.getInstance().getSecurityKeyValuesObject());
				
				serviceObject = ServiceResultObject.parse(NetworkUtils.getContentFromInput(is));
				if (serviceObject.isOpSuccessfully()) {
					DebugUtils.logD(TAG, "UpdateAccountAvatorAsyncTask--updateAvator successfully. start save new Avator.");
					accountObject.mAccountAvator = String.valueOf(mSelectedPosition+1);
					ContentValues values = new ContentValues();
					values.put(DBHelper.ACCOUNT_AVATOR, accountObject.mAccountAvator);
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
			dismissDialog(DIALOG_PROGRESS);
			MyApplication.getInstance().showMessage(result.mStatusMessage);
			if (result.isOpSuccessfully()) {
				finish();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			dismissDialog(DIALOG_PROGRESS);
			MyApplication.getInstance().showMessage(R.string.msg_op_canceled);
		}
		
	}
	
	private class AvatorAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return MyAccountManager.SYSTEM_AVATOR_ARRAY.length - 1;
		}
		
		private int getAvatorImgResId(int position) {
			return MyAccountManager.SYSTEM_AVATOR_ARRAY[position+1];
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.avator_imageview, parent, false);
			}
			((ImageView)convertView).setImageResource(getAvatorImgResId(position));
			if (mSelectedPosition == position) {
				((ImageView)convertView).setBackgroundResource(MyAccountManager.getInstance().getSystemAvatorBackgroudResId(position+1));
			} else {
				((ImageView)convertView).setBackgroundResource(0);
			}
			return convertView;
		}
		
	}
	
	public static void startActivity(Context context, int systemAvatorIndex) {
		Intent intent = new Intent(context, UpdateAvatorActivity.class);
		intent.putExtra("index", systemAvatorIndex);
		context.startActivity(intent);
	}
	
	@Override
	protected boolean checkIntent(Intent intent) {
		mSelectedPosition = intent.getIntExtra("index", -1);
		if (mSelectedPosition > 0) {
			//由于我们这里并不显示默认的头像，所以，如果设置了头像，我们要-1
			mSelectedPosition -=1;
		}
		DebugUtils.logD(TAG, "checkIntent mSelectedPosition=" + mSelectedPosition);
		return true;
	}

}
