package com.lnwoowken.lnwoowkenbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.cncom.app.base.database.BjnoteContent;
import com.cncom.app.base.database.DBHelper;
import com.cncom.app.base.ui.BaseActivity;
import com.lnwoowken.lnwoowkenbook.ServiceObject.ServiceResultObject;
import com.lnwoowken.lnwoowkenbook.model.BillObject;
import com.lnwoowken.lnwoowkenbook.model.SurveyQuestion;
import com.shwy.bestjoy.utils.AsyncTaskUtils;
import com.shwy.bestjoy.utils.DebugUtils;
import com.shwy.bestjoy.utils.Intents;
import com.shwy.bestjoy.utils.NetworkUtils;
/**
 * 满意度调查界面
 * @author chenkai
 *
 */
public class SurveyActivity extends BaseActivity {

	private static final String TAG = "TAG";
	private ListView mListView;
	private List<SurveyQuestion> mSurveyQuestionList = new ArrayList<SurveyQuestion>();
	private BillObject mBillObject;
	
	private View mProgressBarLayout;
	
	private Button mCommitBtn;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (isFinishing()) {
			return;
		}
		setContentView(R.layout.activity_survey);
		mListView = (ListView) findViewById(R.id.listview);
		mProgressBarLayout = findViewById(R.id.progressbar_layout);
		mCommitBtn = (Button) findViewById(R.id.button_commit);
		mCommitBtn.setOnClickListener(this);
		loadSurveyAsync();
	}
	
	public static void startActivity(Context context, String billNumber) {
		Intent intent = new Intent(context, SurveyActivity.class);
		intent.putExtra(Intents.EXTRA_ID, billNumber);
		context.startActivity(intent);
		
	}
	@Override
	protected boolean checkIntent(Intent intent) {
		String billNumber = intent.getStringExtra(Intents.EXTRA_ID);
		if (TextUtils.isEmpty(billNumber)) {
			DebugUtils.logD(TAG, "checkIntent() failed empty billNumber");
			return false;
		}
		mBillObject = BillListManager.getBillObjectByBillNumber(getContentResolver(), billNumber);
		if (mBillObject == null) {
			DebugUtils.logD(TAG, "checkIntent() failed can't get BillObject by BillListManager.getBillObjectByBillNumber()");
			return false;
		}
		return true;
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.button_commit:
			if (mSurveyQuestionList.size() > 0) {
				commitSurveyAsync();
			}
			break;
		}
	}
	
	private LoadSurveyTask mLoadSurveyTask;
	private void loadSurveyAsync() {
		mProgressBarLayout.setVisibility(View.VISIBLE);
		AsyncTaskUtils.cancelTask(mLoadSurveyTask);
		mLoadSurveyTask = new LoadSurveyTask();
		mLoadSurveyTask.execute();
	}
	private class LoadSurveyTask extends AsyncTask<Void, Void, ServiceResultObject> {

		@Override
		protected ServiceResultObject doInBackground(Void... params) {
			InputStream is = null;
			ServiceResultObject serviceResultObject = new ServiceResultObject();
			try {
				is = NetworkUtils.openContectionLocked(ServiceObject.getSurveyQuestionsUrl(mBillObject.getSid()), MyApplication.getInstance().getSecurityKeyValuesObject());
				
				if (is != null) {
					serviceResultObject = ServiceResultObject.parseJsonArray(NetworkUtils.getContentFromInput(is));
					if (serviceResultObject.isOpSuccessfully()) {
						//解析问卷数据
						int len = serviceResultObject.mJsonArrayData.length();
						mSurveyQuestionList.clear();
						SurveyQuestion surveyQuestion = null;
						for(int index=0; index <len; index++) {
							surveyQuestion = SurveyQuestion.parse(serviceResultObject.mJsonArrayData.getJSONObject(index));
							surveyQuestion.mQuestionIndex = index+1;
							mSurveyQuestionList.add(surveyQuestion);
						}
					}
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
			} catch (IOException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
			} catch (JSONException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
			} finally {
				NetworkUtils.closeInputStream(is);
			}
			
			return serviceResultObject;
		}

		@Override
		protected void onPostExecute(ServiceResultObject result) {
			super.onPostExecute(result);
			mProgressBarLayout.setVisibility(View.GONE);
			if (!result.isOpSuccessfully()) {
				MyApplication.getInstance().showMessage(result.mStatusMessage);
				mCommitBtn.setEnabled(false);
			} else {
				mListView.setAdapter(new SurveyQuestionAdapter());
				mCommitBtn.setEnabled(true);
			}
			
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			mProgressBarLayout.setVisibility(View.GONE);
		}
		
	}
	
	
	
	private class SurveyQuestionAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mSurveyQuestionList.size();
		}

		@Override
		public SurveyQuestion getItem(int position) {
			return mSurveyQuestionList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.survey_list_item, parent, false);
				viewHolder._question = (TextView) convertView.findViewById(R.id.question);
				viewHolder._radioGroup = (RadioGroup) convertView.findViewById(R.id.radioGroup1);
				viewHolder._button1 = (RadioButton) convertView.findViewById(R.id.radio1);
				viewHolder._button2 = (RadioButton) convertView.findViewById(R.id.radio2);
				viewHolder._button3 = (RadioButton) convertView.findViewById(R.id.radio3);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			SurveyQuestion question = getItem(position);
			viewHolder._radioGroup.setTag(question);
			viewHolder._question.setText(question.getQuestionDesc());
			
			viewHolder._radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	            
	            @Override
	            public void onCheckedChanged(RadioGroup radioGroup, int arg1) {
	            	int id = radioGroup.getCheckedRadioButtonId();
	            	SurveyQuestion question = (SurveyQuestion) radioGroup.getTag();
	            	if (id == R.id.radio1) {
	            		question.mAnswerIndex = 1;
	            	} else if (id == R.id.radio2) {
	            		question.mAnswerIndex = 2;
	            	} else if (id == R.id.radio3) {
	            		question.mAnswerIndex = 3;
	            	}
	            	DebugUtils.logD(TAG, "onCheckedChanged " + question.mAnswerIndex);
	            }
	        });
			return convertView;
		}
		
	}
	
	private class ViewHolder {
		private RadioButton _button1, _button2, _button3;
		private RadioGroup _radioGroup;
		private TextView _question;
	}
	
	
	private CommitSurveyTask mCommitSurveyTask;
	private void commitSurveyAsync() {
		AsyncTaskUtils.cancelTask(mCommitSurveyTask);
		mCommitSurveyTask = new CommitSurveyTask();
		mCommitSurveyTask.execute();
		showDialog(DIALOG_PROGRESS);
	}
	
	private class CommitSurveyTask extends AsyncTask<Void, Void, ServiceResultObject> {

		@Override
		protected ServiceResultObject doInBackground(Void... params) {
			InputStream is = null;
			ServiceResultObject serviceResultObject = new ServiceResultObject();
			try {
				//para=[{shopid :"",orderno :"",qid :"",aid :""},{},{}]
				JSONArray queryObject = new JSONArray();
				for(SurveyQuestion question : mSurveyQuestionList) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("qid", question.mID);
					jsonObject.put("aid", String.valueOf(question.mAnswerIndex));
					jsonObject.put("shopid", question.mShopID);
					jsonObject.put("orderno", mBillObject.getBillNumber());
					queryObject.put(jsonObject);
				}
				DebugUtils.logD(TAG, "CommitSurveyTask doInBackground() queryObject " + queryObject.toString());
				is = NetworkUtils.openContectionLocked(ServiceObject.getCommitSurveyQuestionsUrl("para", queryObject.toString()), MyApplication.getInstance().getSecurityKeyValuesObject());
				
				if (is != null) {
					serviceResultObject = ServiceResultObject.parse(NetworkUtils.getContentFromInput(is));
					if (serviceResultObject.isOpSuccessfully()) {
						//解析问卷数据
						mBillObject.setVisited(1);
						ContentValues values = new ContentValues();
						values.put(DBHelper.BILL_VISITED, 1);
						int updated = BjnoteContent.update(getContentResolver(), BjnoteContent.Bills.CONTENT_URI, values, BjnoteContent.ID_SELECTION, new String[]{mBillObject.getId()});
						
						DebugUtils.logD(TAG, "CommitSurveyTask update local BillObject #updated " + updated);
					}
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
			} catch (IOException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
			} catch (JSONException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
			} finally {
				NetworkUtils.closeInputStream(is);
			}
			
			return serviceResultObject;
		}

		@Override
		protected void onPostExecute(ServiceResultObject result) {
			super.onPostExecute(result);
			dismissDialog(DIALOG_PROGRESS);
			if (result.isOpSuccessfully()) {
				finish();
			}
			MyApplication.getInstance().showMessage(result.mStatusMessage);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			dismissDialog(DIALOG_PROGRESS);
		}
		
	}
}
