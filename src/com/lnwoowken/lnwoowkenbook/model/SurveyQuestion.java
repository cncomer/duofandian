package com.lnwoowken.lnwoowkenbook.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.ContentValues;

import com.shwy.bestjoy.utils.InfoInterface;

/**
 * http://manage.lnwoowken.com/Mobile/common/GetQuestion.ashx?shopid=36
 * {"Id":"661 ","ShopID":"36 ","Question":"菜肴的味道是否符合口味？ "}
 * @author chenkai
 *
 */
public class SurveyQuestion implements InfoInterface{
	/**问题id*/
	public String mID;
	/**问题描述*/
	public String mQuestion;
	/**店铺*/
	public String mShopID;
	/**问题的答案，1表示满意，以此类推*/
	public int mAnswerIndex=1;
	/**问题题目条目，如问题1*/
	public int mQuestionIndex = 0;
	
	public SurveyQuestion(String id, String sid, String question) {
		mID = id;
		mShopID = sid;
		mQuestion = question;
		
	}
	
	public SurveyQuestion() {
		
	}
	/**
	 * 返回形如1. xxxx的问题描述
	 * @return
	 */
	public String getQuestionDesc() {
		return "" + mQuestionIndex + ". " + mQuestion;
	}
	
	public int getSelectedAnswerIndex() {
		return mAnswerIndex;
	}
	public void setSelectedAnswerIndex(int selectedAnswerIndex) {
		mAnswerIndex = selectedAnswerIndex;
	}

	@Override
	public boolean saveInDatebase(ContentResolver cr, ContentValues addtion) {
		return false;
	}
	
	/**
	 * http://manage.lnwoowken.com/Mobile/common/GetQuestion.ashx?shopid=36
	 * 
	 * {"StatusCode":"1","StatusMessage":"返回数据", "Data":[
	 *     {"Id":"661 ","ShopID":"36 ","Question":"菜肴的味道是否符合口味？ "},
	 *     {"Id":"662 ","ShopID":"36 ","Question":"菜肴的品种是否丰富？ "},
	 *     {"Id":"663 ","ShopID":"36 ","Question":"菜肴口味保持的一致性如何？ "},
	 *     {"Id":"664 ","ShopID":"36 ","Question":"上菜的速度如何？ "}]}
	 * @return
	 * @throws JSONException 
	 */
	public static SurveyQuestion parse(JSONObject jsonObject) throws JSONException {
		SurveyQuestion object = new SurveyQuestion();
		object.mID = jsonObject.getString("Id");
		object.mShopID = jsonObject.getString("ShopID");
		object.mQuestion = jsonObject.getString("Question");
		return object;
	}
}
