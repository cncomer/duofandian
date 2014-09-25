package com.cncom.app.base.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.lnwoowken.lnwoowkenbook.MyApplication;
import com.lnwoowken.lnwoowkenbook.R;
import com.shwy.bestjoy.utils.DateUtils;


public class TableInfoObject implements Parcelable{
	//{"shiduan_id":"1","desk_type":"2人桌(1-2人)","desk_name":"A2","date":"2014/7/18 0:00:00","desk_status":"3","shiduan_time":"12:15","shiduan_name":"午市","DeskID":"1","dabiao_price":"190","service_price":"20"}
	/**解析桌子数据使用 begin*/
	private String mShiduanId;
	private String mDeskType;
	private String mDeskName;
	private String mDate;
	private String mDeskStates;
	private String mShiduanTime;
	private String mShiduanName;
	private String mDeskId;
	private String mDabiaoPrice;
	private String mServicePrice = "0";
	/**定金*/
	private String mDingJinPrice = "0";
	
	public static final String SHIDUAN_ID = "shiduan_id";
	public static final String DESK_TYPE = "desk_type";
	public static final String DESK_NAME = "desk_name";
	public static final String DATE = "date";
	public static final String DATE_STATES = "desk_status";
	public static final String SHIDUAN_TIME = "shiduan_time";
	public static final String SHIDUAN_NAME = "shiduan_name";
	public static final String DESK_ID = "DeskID";
	public static final String DESK_DABIAO_PRICE = "dabiao_price";
	/**定金金额*/
	public static final String DESK_DINGJIN_PRICE = "zifu_price";
	public static final String DESK_SERVICE_PRICE = "service_price";
	/**解析桌子数据使用 end*/
	
	public static final String TABLE_NAME_PROJECTION = DESK_NAME + "=?";
	/**MM月dd日 E HH时mm分*/
	public static DateFormat BILL_ORDER_DATE_FORMAT = new SimpleDateFormat("MM月dd日 E HH时mm分", Locale.getDefault());
	
	//支付订单信息使用 begin
	private String mShopId;
	private String mUid;
//	 private String mTime; 
	 private String mNote; //备注信息
	 /**交易流水号*/
	 private String mTn;
	 /**订单号*/
	 private String mOrderNo;
	//支付订单信息使用 end
	 /**交易流水号*/
	 public String getTn() {
			return mTn;
		}
		public void setTn(String tn) {
			mTn = tn;
		}
		/**订单号*/
		public String getOrderNo() {
			return mOrderNo;
		}
		public void setOrderNo(String orderNo) {
			mOrderNo = orderNo;
		}
	 
//	public String getTime() {
//		return mTime;
//	}
//	public void setTime(String time) {
//		this.mTime = time;
//	}
	/**
	 * 返回预定时间
	 * @return
	 */
	public Date getOrderDate() {
		//"date":"2014/9/26 0:00:00","time":"19:15"
		int find = mDate.indexOf(" ");
		StringBuilder sb = new StringBuilder();
		if (find > 0) {
			sb.append(mDate.substring(0, find));
			sb.append(" ").append(mShiduanTime);
		}
		Date date = new Date();
		try {
			date = DateUtils.DATE_TIME_FORMAT.parse(sb.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	public String getNote() {
		return mNote;
	}
	public void setNote(String note) {
		this.mNote = note;
	}
	public String getShopId() {
		return mShopId;
	}
	public void setShopId(String shopId) {
		this.mShopId = shopId;
	}
	public String getUid() {
		return mUid;
	}
	public void setUid(String uid) {
		this.mUid = uid;
	}
	
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
		return TextUtils.isEmpty(mServicePrice) ? "0" : mServicePrice;
	}
	
	public void setServicePrice(String mServicePrice) {
		this.mServicePrice = mServicePrice;
	}
	
	public String getDingJinPrice() {
		return TextUtils.isEmpty(mDingJinPrice) ? "0" : mDingJinPrice;
	}
	
	public void setDingJinPrice(String dingJinPrice) {
		this.mDingJinPrice = dingJinPrice;
	}
	/**
	 * 得到定金和服务费的总和
	 * @return
	 */
	public int getTotalPrice() {
		int dingJinPrice = Integer.valueOf(getDingJinPrice()); 
		int servicePrice = Integer.valueOf(getServicePrice()); 
		return dingJinPrice + servicePrice;
	}
	/**
	 * 得到需要支付的金额，如%1$s元(定金%2$s元+服务费%3$s元)
	 * @return
	 */
	public String getBillPay() {
		int dingJinPrice = Integer.valueOf(getDingJinPrice()); 
		int servicePrice = Integer.valueOf(getServicePrice()); 
		int totalPrice = dingJinPrice + servicePrice;
		return MyApplication.getInstance().getString(R.string.bill_pay_format, totalPrice, dingJinPrice, servicePrice);
		
	}

	@Override
	public int describeContents() {
		return 0;
	}
	public TableInfoObject(){};
	public TableInfoObject(Parcel in){  
        //顺序要和writeToParcel写的顺序一样  
		mShiduanId = in.readString();  
		mDeskType = in.readString();  
		mDeskName = in.readString();
		mDate = in.readString();
		mDeskStates = in.readString();
		mShiduanTime = in.readString();
		mShiduanName = in.readString();
		mDeskId = in.readString();
		mDabiaoPrice = in.readString();
		mServicePrice = in.readString();
		mDingJinPrice = in.readString();
		mUid = in.readString();
//		mTime = in.readString();
		mNote = in.readString();
		mShopId = in.readString();
		
		mTn = in.readString();
		mOrderNo = in.readString();
    } 
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mShiduanId);  
	    dest.writeString(mDeskType);
	    dest.writeString(mDeskName);
	    dest.writeString(mDate);
	    dest.writeString(mDeskStates);
	    dest.writeString(mShiduanTime);
	    dest.writeString(mShiduanName);
	    dest.writeString(mDeskId);
	    dest.writeString(mDabiaoPrice);
	    dest.writeString(mServicePrice);
	    dest.writeString(mDingJinPrice);
	    dest.writeString(mUid);
//	    dest.writeString(mTime);
	    dest.writeString(mNote);
	    dest.writeString(mShopId);
	    dest.writeString(mTn);
	    dest.writeString(mOrderNo);
	    
	}
	
	public static final Parcelable.Creator<TableInfoObject> CREATOR = new Parcelable.Creator<TableInfoObject>() {
        public TableInfoObject createFromParcel(Parcel in) {  
            return new TableInfoObject(in);  
        }  
          
        public TableInfoObject[] newArray(int size) {  
            return new TableInfoObject[size];  
        }  
    };  
}
