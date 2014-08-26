package com.lnwoowken.lnwoowkenbook.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import com.cncom.app.base.database.BjnoteContent;
import com.cncom.app.base.database.DBHelper;
import com.shwy.bestjoy.utils.DebugUtils;

public class BillObject {
	private static final String TAG = "BillObject";
	
	private String id;
	private String uid;
	private String sid;
	private String tid;
	private String peopleNum;
	private String rcode;
	private String mac;
	private String Ip;
	private String phone;
	private String version;
	private String shopName;// --店名
	private String date;// --预定日期
	private String time;// --预定时间
	private int state;// --订单状态
	private String tableName;// --桌名
	private String tableStyle;// --桌型
	private String createTime;// --订单生成时间
	private String billNumber;// --订单号
	private String dabiaoPrice;// --达标金额
	private String servicePrice;// --服务金额
	
	/**正常状态*/
	public static final int STATE_IDLE = 1;
	/**未支付*/
	public static final int STATE_UNPAY = STATE_IDLE + 1;
	/**支付成功*/
	public static final int STATE_SUCCESS = STATE_UNPAY + 1;
	/**退定成功*/
	public static final int STATE_TUIDING_SUCCESS = STATE_SUCCESS + 1;

	public static final String BILL_ID = "_id";
	public static final String BILL_UID = "uid";
	public static final String BILL_SID = "sid";
	public static final String BILL_TID = "tid";
	public static final String BILL_PEOPLENUM = "peoplenum";
	public static final String BILL_RCODE = "rcode";
	public static final String BILL_MAC = "mac";
	public static final String BILL_IP = "ip";
	public static final String BILL_PHONE = "phone";
	public static final String BILL_VERSION = "version";
	public static final String BILL_SHOPNAME = "shopname";
	public static final String BILL_DATE = "date";
	public static final String BILL_TIME = "time";
	public static final String BILL_STATE = "state";
	public static final String BILL_TABLENAME = "tablename";
	public static final String BILL_TABLESTYLE = "tableStyle";
	public static final String BILL_CREATETIME = "createTime";
	public static final String BILL_NUMBER = "billnumber";
	public static final String BILL_DABIAO_PRICE = "dabiaoprice";
	public static final String BILL_SERVICE_PRICE = "serviceprice";

	public static final String[] BILL_PROJECTION = new String[] {
		DBHelper.BILL_ID,
		DBHelper.BILL_UID,
		DBHelper.BILL_SID,
		DBHelper.BILL_TID,
		DBHelper.BILL_PEOPLENUM,
		DBHelper.BILL_RCODE,
		DBHelper.BILL_MAC,
		DBHelper.BILL_IP,
		DBHelper.BILL_PHONE,
		DBHelper.BILL_VERSION,
		DBHelper.BILL_SHOPNAME,
		DBHelper.BILL_DATE,
		DBHelper.BILL_TIME,
		DBHelper.BILL_STATE,
		DBHelper.BILL_TABLENAME,
		DBHelper.BILL_TABLESTYLE,
		DBHelper.BILL_CREATETIME,
		DBHelper.BILL_NUMBER,
		DBHelper.BILL_DABIAO_PRICE,
		DBHelper.BILL_SERVICE_PRICE,
	};

	public static final String BILL_SELECTION = DBHelper.BILL_NUMBER + "=?";

	public static final String BILL_UNPAY_SELECTION = DBHelper.BILL_STATE + "!=?";

	public static final String BILL_NUMBER_SELECTION = DBHelper.BILL_NUMBER + "=?";
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getPeopleNum() {
		return peopleNum;
	}

	public void setPeopleNum(String peopleNum) {
		this.peopleNum = peopleNum;
	}

	public String getRcode() {
		return rcode;
	}

	public void setRcode(String rcode) {
		this.rcode = rcode;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getIp() {
		return Ip;
	}

	public void setIp(String ip) {
		Ip = ip;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableStyle() {
		return tableStyle;
	}

	public void setTableStyle(String tableStyle) {
		this.tableStyle = tableStyle;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getDabiaoPrice() {
		return dabiaoPrice;
	}

	public void setDabiaoPrice(String dabiaoPrice) {
		this.dabiaoPrice = dabiaoPrice;
	}

	public String getServicePrice() {
		return servicePrice;
	}

	public void setServicePrice(String servicePrice) {
		this.servicePrice = servicePrice;
	}
	
	public boolean saveDatabase(ContentResolver cr, ContentValues addtion) {
		ContentValues values = new ContentValues();
		if (addtion != null) {
			values.putAll(addtion);
		}
		values.put(DBHelper.BILL_UID, uid);
		values.put(DBHelper.BILL_SID, sid);
		values.put(DBHelper.BILL_TID, tid);
		values.put(DBHelper.BILL_PEOPLENUM, peopleNum);
		values.put(DBHelper.BILL_RCODE, rcode);
		values.put(DBHelper.BILL_MAC, mac);
		values.put(DBHelper.BILL_IP, Ip);
		values.put(DBHelper.BILL_PHONE, phone);
		values.put(DBHelper.BILL_VERSION, version);
		values.put(DBHelper.BILL_SHOPNAME, shopName);
		values.put(DBHelper.BILL_DATE, date);
		values.put(DBHelper.BILL_TIME, time);
		values.put(DBHelper.BILL_STATE, state);
		values.put(DBHelper.BILL_TABLENAME, tableName);
		values.put(DBHelper.BILL_TABLESTYLE, tableStyle);
		values.put(DBHelper.BILL_CREATETIME, createTime);
		values.put(DBHelper.BILL_NUMBER, billNumber);
		values.put(DBHelper.BILL_DABIAO_PRICE, dabiaoPrice);
		values.put(DBHelper.BILL_SERVICE_PRICE, servicePrice);
		
		Uri uri = cr.insert(BjnoteContent.Bills.CONTENT_URI, values);
		if (uri != null) {
			DebugUtils.logD(TAG, "saveInDatebase insert");
			return true;
		} else {
			DebugUtils.logD(TAG, "saveInDatebase failly insert");
		}
		return false;
	}
	public boolean updateDatabase(ContentResolver cr) {
		ContentValues values = new ContentValues();

		values.put(DBHelper.BILL_UID, uid);
		values.put(DBHelper.BILL_SID, sid);
		values.put(DBHelper.BILL_TID, tid);
		values.put(DBHelper.BILL_PEOPLENUM, peopleNum);
		values.put(DBHelper.BILL_RCODE, rcode);
		values.put(DBHelper.BILL_MAC, mac);
		values.put(DBHelper.BILL_IP, Ip);
		values.put(DBHelper.BILL_PHONE, phone);
		values.put(DBHelper.BILL_VERSION, version);
		values.put(DBHelper.BILL_SHOPNAME, shopName);
		values.put(DBHelper.BILL_DATE, date);
		values.put(DBHelper.BILL_TIME, time);
		values.put(DBHelper.BILL_STATE, state);
		values.put(DBHelper.BILL_TABLENAME, tableName);
		values.put(DBHelper.BILL_TABLESTYLE, tableStyle);
		values.put(DBHelper.BILL_CREATETIME, createTime);
		values.put(DBHelper.BILL_NUMBER, billNumber);
		values.put(DBHelper.BILL_DABIAO_PRICE, dabiaoPrice);
		values.put(DBHelper.BILL_SERVICE_PRICE, servicePrice);
		
		int uri = cr.update(BjnoteContent.Bills.CONTENT_URI, values, BILL_SELECTION, new String[]{billNumber});
		if (uri != -1) {
			DebugUtils.logD(TAG, "saveInDatebase update");
			return true;
		} else {
			DebugUtils.logD(TAG, "saveInDatebase failly update");
		}
		return false;
	}
}
