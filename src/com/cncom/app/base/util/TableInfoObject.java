package com.cncom.app.base.util;

public class TableInfoObject {
	//{"shiduan_id":"1","desk_type":"2人桌(1-2人)","desk_name":"A2","date":"2014/7/18 0:00:00","desk_status":"3","shiduan_time":"12:15","shiduan_name":"午市","DeskID":"1","dabiao_price":"190","service_price":"20"}
	private String mShiduanId;
	private String mDeskType;
	private String mDeskName;
	private String mDate;
	private String mDeskStates;
	private String mShiduanTime;
	private String mShiduanName;
	private String mDeskId;
	private String mDabiaoPrice;
	private String mServicePrice;
	
	public static final String SHIDUAN_ID = "shiduan_id";
	public static final String DESK_TYPE = "desk_type";
	public static final String DESK_NAME = "desk_name";
	public static final String DATE = "date";
	public static final String DATE_STATES = "desk_status";
	public static final String SHIDUAN_TIME = "shiduan_time";
	public static final String SHIDUAN_NAME = "shiduan_name";
	public static final String DESK_ID = "DeskID";
	public static final String DESK_DABIAO_PRICE = "dabiao_price";
	public static final String DESK_SERVICE_PRICE = "service_price";
	
	public String getShiduanId() {
		return mShiduanId;
	}
	
	public void setShiduanId(String mShiduanId) {
		this.mShiduanId = mShiduanId;
	}

	public String getDeskType() {
		return mDeskType;
	}
	
	public void setDeskType(String mDeskType) {
		this.mDeskType = mDeskType;
	}

	public String getDeskName() {
		return mDeskName;
	}
	
	public void setDeskName(String mDeskName) {
		this.mDeskName = mDeskName;
	}

	public String getDate() {
		return mDate;
	}
	
	public void setDate(String mDate) {
		this.mDate = mDate;
	}

	public String getDeskStates() {
		return mDeskStates;
	}
	
	public void setDeskStates(String mDeskStates) {
		this.mDeskStates = mDeskStates;
	}

	public String getmShiduanTime() {
		return mShiduanTime;
	}
	
	public void setShiduanTime(String mShiduanTime) {
		this.mShiduanTime = mShiduanTime;
	}

	public String getShiduanName() {
		return mShiduanName;
	}
	
	public void setShiduanName(String mShiduanName) {
		this.mShiduanName = mShiduanName;
	}

	public String getDeskId() {
		return mDeskId;
	}
	
	public void setDeskId(String mDeskId) {
		this.mDeskId = mDeskId;
	}

	public String getDabiaoPrice() {
		return mDabiaoPrice;
	}
	
	public void setDabiaoPrice(String mDabiaoPrice) {
		this.mDabiaoPrice = mDabiaoPrice;
	}

	public String getServicePrice() {
		return mServicePrice;
	}
	
	public void setServicePrice(String mServicePrice) {
		this.mServicePrice = mServicePrice;
	}
}
