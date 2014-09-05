package com.lnwoowken.lnwoowkenbook;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cncom.app.base.ui.BaseActivity;
import com.lnwoowken.lnwoowkenbook.adapter.SurveyListAdapter;
import com.lnwoowken.lnwoowkenbook.model.Contant;
import com.lnwoowken.lnwoowkenbook.model.Location;
import com.lnwoowken.lnwoowkenbook.model.Margins;
import com.lnwoowken.lnwoowkenbook.model.Paddings;
import com.lnwoowken.lnwoowkenbook.model.Screen;
import com.lnwoowken.lnwoowkenbook.model.Size;
import com.lnwoowken.lnwoowkenbook.model.Survey;
import com.lnwoowken.lnwoowkenbook.model.SurveyQuestion;
import com.lnwoowken.lnwoowkenbook.network.Client;
import com.lnwoowken.lnwoowkenbook.network.JsonParser;
import com.lnwoowken.lnwoowkenbook.tools.Tools;
import com.lnwoowken.lnwoowkenbook.tools.ViewSettor;

@SuppressLint("HandlerLeak")
public class SurveyActivity extends BaseActivity {

	private ListView listViewSurvey;
	private SurveyListAdapter adapter;
	private List<SurveyQuestion> questions;
	private Survey survey = new Survey();
	private Button btn_commit;
	private Intent intent;
	private String rid;
	private boolean isCommit = false;
	private RelativeLayout top;
	private LinearLayout bottom;
	private TextView title;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// textView.setText("server端返回的数据是：\n" + s);

			RequestSurveyThread mThread = new RequestSurveyThread();
			mThread.start();

		}

	};
	private Handler commitHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// textView.setText("server端返回的数据是：\n" + s);

			CommitSurveyThread mThread = new CommitSurveyThread();
			mThread.start();

		}

	};

	private Handler UIhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// textView.setText("server端返回的数据是：\n" + s);

			adapter = new SurveyListAdapter(mContext, survey);
			listViewSurvey.setAdapter(adapter);

		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_survey);
		// --屏幕参数
		Screen screen = ViewSettor.getScreen(SurveyActivity.this);
		// --外边距
		Margins margin = new Margins(Tools.dip2px(mContext, 10), Tools.dip2px(
				mContext, 10), Tools.dip2px(mContext, 10), Tools.dip2px(mContext,
				10));
		title = (TextView) findViewById(R.id.tv);
		bottom = (LinearLayout) findViewById(R.id.bottom);

		ViewSettor settor = new ViewSettor();
		// --内边距
		Paddings bottom_padding = new Paddings(Tools.dip2px(mContext, 10),
				Tools.dip2px(mContext, 10), Tools.dip2px(mContext, 10),
				Tools.dip2px(mContext, 10));
		Paddings noPadding = new Paddings(0, 0, 0, 0);
		// --title的文字内边距
		Paddings textPadding = new Paddings(Tools.dip2px(mContext, 10),

		Tools.dip2px(mContext, 10), Tools.dip2px(mContext, 10), Tools.dip2px(
				mContext, 10));
		// --设置底部工具（导航）栏 一般无特殊情况可直接按照这里的方法调用，参数与此处相同即可
		ViewSettor.setBottom(bottom, bottom_padding, new Size(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));

		// --设置在RelativeLayout中控件的参数（在LinearLayout中的控件则调用setViewInLinearLayout方法，使用方法与setViewInRelativeLayout类似）
		ViewSettor.setViewInRelativeLayout(
				textPadding,
				new Size(screen.getWidth() - margin.getLeft()
						- margin.getRight(),
						ViewGroup.LayoutParams.WRAP_CONTENT), margin,
				new Location(0, 0, 0, R.id.top), title);

		intent = getIntent();
		rid = intent.getExtras().getString("rid");
		btn_commit = (Button) findViewById(R.id.button_commit);
		btn_commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				commitHandler.sendMessage(msg);
			}
		});

		listViewSurvey = (ListView) findViewById(R.id.listView_survey);
		questions = new ArrayList<SurveyQuestion>();
		Message msg = new Message();
		handler.sendMessage(msg);

		// for (int i = 0; i < 10; i++) {
		// SurveyQuestion question = new SurveyQuestion(i+1, "问题"+(i+1)+"的文字",
		// 1);
		// questions.add(question);
		// }
		//
		// for (int i = 0; i < questions.size(); i++) {
		// List<SurveyAnswer> answers = new ArrayList<SurveyAnswer>();
		// for (int j = 0; j < 3; j++) {
		// SurveyAnswer answer = new SurveyAnswer(1, "问题"+(i+1)+"的答案"+(j+1),
		// questions.get(i).getQid());
		// answers.add(answer);
		//
		// }
		// questions.get(i).setAnswers(answers);
		// }

		//
		// for (int i = 0; i < survey.getQuestions().size(); i++) {
		// Log.d(survey.getQuestions().get(i).getText()+"================================",
		// "");
		// for (int j = 0; j < survey.getQuestions().get(i).getAnswers().size();
		// j++) {
		// Log.d(survey.getQuestions().get(i).getAnswers().get(j).getText()+"+++++++++++++++++++++++++++++++",
		// "");
		// }
		//
		// }
	}

	public class CommitSurveyThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();

			// String jsonStr = "{\"id\":\"" + tableStyle +
			// "\",\"se\":\""+se+"\",\"dt\":\"" + selectDate

			// selectDate = selectDate.replace("月", "-");
			if (questions != null && questions.size() > 0) {
				for (int i = 0; i < questions.size(); i++) {
					
					
					Log.d("rid===============", rid);
					String jsonStr = "{\"Aid \":\""
							+ questions.get(i).getSelectedAnswerId()
							+ "\",\"Answerstate \":\"" + 1 + "\",\"Uid\":\""
							+ Contant.USER.getId() + "\",\"Rid\":\"" + rid
							+ "\",\"Qid\":\"" + questions.get(i).getQid()
							+ "\"}";// "{\"Sid\":\""+shopId+"\",\"Tid\":\"1\",\"RSTime\":\""+time+"\"}";
					jsonStr = Client.encodeBase64(jsonStr);
					String str = Tools.getRequestStr(Contant.SERVER_IP,
							Contant.SERVER_PORT + "", "Reserve?id=", "Rl22",
							"&op=" + jsonStr);
					String result = Client.executeHttpGetAndCheckNet(str,
							SurveyActivity.this);
					result = Client.decodeBase64(result);

					Log.d("rl22===============", result);
					if (result != null) {
						if (JsonParser.checkError(result)) {
							Message msg = new Message();
							errorHandler.sendMessage(msg);
						} else {
							Message msg = new Message();
							successHandler.sendMessage(msg);
							// questions =
							// JsonParser.parseSurveyQuestionJson(result);
							// getAnswersFromService(questions);
							// survey.setQuestions(questions);
							// Message msg = new Message();
							// UIhandler.sendMessage(msg);
						}

					}
				}
			}

		}
	}

	public class RequestSurveyThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();

			// String jsonStr = "{\"id\":\"" + tableStyle +
			// "\",\"se\":\""+se+"\",\"dt\":\"" + selectDate

			// selectDate = selectDate.replace("月", "-");
			String jsonStr = "{\"sid\":\"" + 1 + "\"}";// "{\"Sid\":\""+shopId+"\",\"Tid\":\"1\",\"RSTime\":\""+time+"\"}";
			jsonStr = Client.encodeBase64(jsonStr);
			String str = Tools.getRequestStr(Contant.SERVER_IP,
					Contant.SERVER_PORT + "", "Reserve?id=", "Rl20", "&op="
							+ jsonStr);
			String result = Client.executeHttpGetAndCheckNet(str,
					SurveyActivity.this);
			result = Client.decodeBase64(result);

			Log.d("rl20===============", result);
			if (result != null) {
				if (JsonParser.checkError(result)) {
					Message msg = new Message();
					errorHandler.sendMessage(msg);
				} else {
					questions = JsonParser.parseSurveyQuestionJson(result);
					getAnswersFromService(questions);
					survey.setQuestions(questions);
					Message msg = new Message();
					UIhandler.sendMessage(msg);
				}

			}

		}
	}

	private void getAnswersFromService(List<SurveyQuestion> questions) {
		for (int i = 0; i < questions.size(); i++) {
			int qid = questions.get(i).getQid();
			String jsonStr = "{\"qid\":\"" + qid + "\"}";// "{\"Sid\":\""+shopId+"\",\"Tid\":\"1\",\"RSTime\":\""+time+"\"}";
			jsonStr = Client.encodeBase64(jsonStr);
			String str = Tools.getRequestStr(Contant.SERVER_IP,
					Contant.SERVER_PORT + "", "Reserve?id=", "Rl21", "&op="
							+ jsonStr);
			String result = Client.executeHttpGetAndCheckNet(str,
					SurveyActivity.this);
			result = Client.decodeBase64(result);

			Log.d("rl21===============", result);
			if (result != null) {
				if (JsonParser.checkError(result)) {
					Message msg = new Message();
					errorHandler.sendMessage(msg);
				} else {
					questions.get(i).setAnswers(
							JsonParser.parseSurveyAnswerJson(result));
					// questions = JsonParser.parseSurveyQuestionJson(result);
				}

			}
		}

	}

	private Handler errorHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// textView.setText("server端返回的数据是：\n" + s);

			Toast.makeText(mContext, "服务器数据错误,提交失败", Toast.LENGTH_SHORT).show();

		}

	};

	private Handler successHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// textView.setText("server端返回的数据是：\n" + s);

			Toast.makeText(mContext, "提交成功!", Toast.LENGTH_SHORT).show();
			SurveyActivity.this.finish();

		}

	};

	@Override
	protected boolean checkIntent(Intent intent) {
		return true;
	}
}
