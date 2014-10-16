package com.lnwoowken.lnwoowkenbook.model;

import java.text.ParseException;
import java.util.Date;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.cncom.app.base.database.BjnoteContent;
import com.cncom.app.base.database.DBHelper;
import com.shwy.bestjoy.utils.DateUtils;
import com.shwy.bestjoy.utils.DebugUtils;
import com.shwy.bestjoy.utils.InfoInterface;

public class BillObject implements InfoInterface{
	private static final String TAG = "BillObject";
	
	private String id;
	private String uid;   //账户id
	private String sid;  //ShopId
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
	private String tableType;// --桌型
	private long createTime;// --订单生成时间
	private String billNumber;// --订单号
	private String dabiaoPrice;// --达标金额
	private String servicePrice;// --服务金额
	private String dingjinPrice;//定金金额
	
	private int mVisited = 0;  //满意度调查是否已经评价过了
	
	/**未支付 0*/
	public static final int STATE_UNPAY = 0;
	/**支付成功 1*/
	public static final int STATE_SUCCESS = STATE_UNPAY + 1;
	/**退定成功 2*/
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
	public static final String BILL_DINGJIN_PRICE = "zifu_price";
	
	public static final String BILL_VISITED = "visited";

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
		DBHelper.BILL_DINGJIN_PRICE,
		DBHelper.BILL_VISITED,
	};

	public static final String BILL_SELECTION = DBHelper.BILL_NUMBER + "=?";
	public static final String BILL_UNPAY_SELECTION = DBHelper.BILL_STATE + "!=?";
	public static final String BILL_NUMBER_SELECTION = DBHelper.BILL_NUMBER + "=?";
	public static final String BILL_SORT = DBHelper.BILL_CREATETIME + " DESC";
	
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

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableStyle) {
		this.tableType = tableStyle;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
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
	public String getDingJinPrice() {
		return dingjinPrice;
	}

	public void setDingJinPrice(String dingjinPrice) {
		this.dingjinPrice = dingjinPrice;
	}
	
	public boolean hasVisited() {
		return mVisited == 1;
	}
	
	public void setVisited(int visited) {
		mVisited = visited;
	}
	
	/**
	 * 返回预定时间
	 * @return
	 */
	public Date getOrderDate() {
		//"date":"2014/9/26 0:00:00","time":"19:15"
		int find = date.indexOf(" ");
		StringBuilder sb = new StringBuilder();
		if (find > 0) {
			sb.append(date.substring(0, find));
			sb.append(" ").append(time);
		}
		Date date = new Date();
		try {
			date = DateUtils.DATE_TIME_FORMAT.parse(sb.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
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
		values.put(DBHelper.BILL_TABLESTYLE, tableType);
		values.put(DBHelper.BILL_CREATETIME, createTime);
		values.put(DBHelper.BILL_NUMBER, billNumber);
		values.put(DBHelper.BILL_DABIAO_PRICE, dabiaoPrice);
		values.put(DBHelper.BILL_SERVICE_PRICE, servicePrice);
		values.put(DBHelper.BILL_DINGJIN_PRICE, dingjinPrice);
		
		values.put(DBHelper.BILL_VISITED, mVisited);
		
		
		
		Uri uri = cr.insert(BjnoteContent.Bills.CONTENT_URI, values);
		if (uri != null) {
			DebugUtils.logD(TAG, "saveInDatebase insert billnumber " + billNumber);
			return true;
		} else {
			DebugUtils.logD(TAG, "saveInDatebase failly insert " + billNumber);
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
		values.put(DBHelper.BILL_TABLESTYLE, tableType);
		values.put(DBHelper.BILL_CREATETIME, createTime);
		values.put(DBHelper.BILL_NUMBER, billNumber);
		values.put(DBHelper.BILL_DABIAO_PRICE, dabiaoPrice);
		values.put(DBHelper.BILL_SERVICE_PRICE, servicePrice);
		values.put(DBHelper.BILL_DINGJIN_PRICE, dingjinPrice);
		
		values.put(DBHelper.BILL_VISITED, mVisited);
		
		int updated = cr.update(BjnoteContent.Bills.CONTENT_URI, values, BILL_SELECTION, new String[]{billNumber});
		if (updated != -1) {
			DebugUtils.logD(TAG, "saveInDatebase update billNumber " + billNumber + ", #updated "+ updated);
			return true;
		} else {
			DebugUtils.logD(TAG, "saveInDatebase failly update billNumber " + billNumber);
		}
		return false;
	}

	@Override
	public boolean saveInDatebase(ContentResolver cr, ContentValues addtion) {
		if (isExsited(cr, billNumber) > 0) {
			return updateDatabase(cr);
		} else {
			return saveDatabase(cr, addtion);
		}
	}
	
	public static long isExsited(ContentResolver cr, String billNumber) {
		long id = -1;
		Cursor c = cr.query(BjnoteContent.Bills.CONTENT_URI, BillObject.BILL_PROJECTION, BillObject.BILL_SELECTION, new String[] {billNumber}, null);
		if (c != null) {
			if (c.moveToNext()) {
				id = c.getLong(0);
			}
			c.close();
		}
		return id;
	}
}
