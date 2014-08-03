package com.lnwoowken.lnwoowkenbook.adapter;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lnwoowken.lnwoowkenbook.R;
import com.lnwoowken.lnwoowkenbook.adapter.TabAdapter.ViewHolder;
import com.lnwoowken.lnwoowkenbook.model.ShopTree;
import com.lnwoowken.lnwoowkenbook.model.Survey;
import com.lnwoowken.lnwoowkenbook.model.SurveyQuestion;

public class SurveyListAdapter extends BaseAdapter {

	private Context context;
	private Survey survey;
	private List<SurveyQuestion> questions;
	private int selectedPosition = -1;
	HashMap<Integer, View> lmap = new HashMap<Integer, View>();

	// private String[][] arr;
	// private Typeface typeface12=null;//微软雅黑
	// private Typeface typeface3=null;//times
	public SurveyListAdapter(Context context, Survey survey) {
		this.context = context;
		this.survey = survey;
		this.questions = survey.getQuestions();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub

		return questions.size();
	}

	@Override
	public SurveyQuestion getItem(int arg0) {
		// TODO Auto-generated method stub
		SurveyQuestion item = null;

		if (null != questions) {
			item = questions.get(arg0);
		}

		return item;

	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View convertView1, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view;
		ViewHolder groupHolder = null;
		if (lmap.get(position) == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.survey_list_item, null);
			groupHolder = new ViewHolder();
			// groupHolder.row=(LinearLayout)
			// convertView.findViewById(R.id.table_list_row);
			groupHolder.question = (TextView) view
					.findViewById(R.id.textView_question);
			groupHolder.radioAnswers = (RadioGroup) view
					.findViewById(R.id.radioGroup1);
			groupHolder.radio0 = (RadioButton) view.findViewById(R.id.radio0);
			groupHolder.radio1 = (RadioButton) view.findViewById(R.id.radio1);
			groupHolder.radio2 = (RadioButton) view.findViewById(R.id.radio2);

			// groupHolder.img_distance=(ImageButton)
			// convertView.findViewById(R.id.imageButton_distance);
			lmap.put(position, view);
			view.setTag(groupHolder);

		} else {
			view = lmap.get(position);
			groupHolder = (ViewHolder) view.getTag();
			// groupHolder=(ViewHolder)convertView.getTag();
		}

		groupHolder.question.setText((position + 1) + "."
				+ questions.get(position).getText());
		groupHolder.radio0.setText(questions.get(position).getAnswers().get(0)
				.getText());
		groupHolder.radio1.setText(questions.get(position).getAnswers().get(1)
				.getText());
		groupHolder.radio2.setText(questions.get(position).getAnswers().get(2)
				.getText());
		
		int id = groupHolder.radioAnswers.getCheckedRadioButtonId();
		switch (id) {
		case R.id.radio0:
			questions.get(position).setSelectedAnswerId(
					questions.get(position).getAnswers().get(0)
							.getAnswerId());
			break;
		case R.id.radio1:
			questions.get(position).setSelectedAnswerId(
					questions.get(position).getAnswers().get(1)
							.getAnswerId());
			break;
		case R.id.radio2:
			questions.get(position).setSelectedAnswerId(
					questions.get(position).getAnswers().get(2)
							.getAnswerId());
			break;

		default:
			questions.get(position).setSelectedAnswerId(
					questions.get(position).getAnswers().get(0)
							.getAnswerId());
			break;
		}
		
		Log.d("question~~~~~~~~~~~~~~~~~~~~~~~"
				,
				questions.get(position).getText());
		Log.d("selectedid~~~~~~~~~~~~~~~~~~~~"
				,
				questions.get(position).getSelectedAnswerId()+"");
		
		groupHolder.radio0
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						// TODO Auto-generated method stub
						if (arg1) {
							questions.get(position).setSelectedAnswerId(
									questions.get(position).getAnswers().get(0)
											.getAnswerId());
							Log.d("question============="
									,
									questions.get(position).getText());
							Log.d("answerid============="
									+ questions.get(position).getAnswers()
											.get(0).getAnswerId(),
									"selectid=========="
											+ questions.get(position)
													.getSelectedAnswerId());
						}
					}
				});
		groupHolder.radio1
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						if (arg1) {
							// TODO Auto-generated method stub
							questions.get(position).setSelectedAnswerId(
									questions.get(position).getAnswers().get(1)
											.getAnswerId());
							Log.d("question============="
									,
									questions.get(position).getText());
							Log.d("answerid============="
									+ questions.get(position).getAnswers()
											.get(1).getAnswerId(),
									"selectid=========="
											+ questions.get(position)
													.getSelectedAnswerId());
						}
					}
				});
		groupHolder.radio2
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						// TODO Auto-generated method stub
						if (arg1) {
							questions.get(position).setSelectedAnswerId(
									questions.get(position).getAnswers().get(2)
											.getAnswerId());
							Log.d("question============="
									,
									questions.get(position).getText());
							Log.d("answerid============="
									+ questions.get(position).getAnswers()
											.get(2).getAnswerId(),
									"selectid=========="
											+ questions.get(position)
													.getSelectedAnswerId());
						}

					}
				});
		// convertView.setClickable(true);
		return view;
	}

	static class ViewHolder {

		TextView question;
		RadioGroup radioAnswers;
		RadioButton radio0;
		RadioButton radio1;
		RadioButton radio2;
		// ImageView icon;
		// LinearLayout linearLayout;
		// TextView environmentLevel;

		// ImageButton img_distance;
	}

}
