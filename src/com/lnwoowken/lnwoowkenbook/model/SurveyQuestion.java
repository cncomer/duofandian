package com.lnwoowken.lnwoowkenbook.model;

import java.util.List;

public class SurveyQuestion {
	public SurveyQuestion(int qid,String text,int sid){
		this.qid = qid;
		this.text = text;
		this.sid = sid;
	//	this.answers = answers;
		
	}
	
//	public SurveyQuestion(){
//	//	super();
//	//	this.answers = answers;
//		
//	}
	private int qid;
	private String text;
	private int sid;
	private List<SurveyAnswer> answers;
	private int selectedAnswerId;
	public int getSelectedAnswerId() {
		return selectedAnswerId;
	}
	public void setSelectedAnswerId(int selectedAnswerId) {
		this.selectedAnswerId = selectedAnswerId;
	}
	public List<SurveyAnswer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<SurveyAnswer> answers) {
		this.answers = answers;
	}
	public int getQid() {
		return qid;
	}
	public void setQid(int qid) {
		this.qid = qid;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	

}
