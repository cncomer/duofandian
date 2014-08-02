package com.lnwoowken.lnwoowkenbook.model;

public class SurveyAnswer {
	public SurveyAnswer(int answerid,String text,int qid){
		this.answerId = answerid;
		this.text = text;
		this.qid = qid;
		
	}
	private int answerId;
	private String text;
	private int qid;
	public int getAnswerId() {
		return answerId;
	}
	public void setAnswerId(int answerId) {
		this.answerId = answerId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getQid() {
		return qid;
	}
	public void setQid(int qid) {
		this.qid = qid;
	}
	

}
