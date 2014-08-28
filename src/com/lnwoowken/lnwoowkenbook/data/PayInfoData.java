package com.lnwoowken.lnwoowkenbook.data;

import com.lnwoowken.lnwoowkenbook.model.PayInfo;

import android.os.Parcel;
import android.os.Parcelable;

public class PayInfoData  implements Parcelable{  
    private String shopId;  
    private String tableId;  
    private String time; 
    private String tableName;
    private String tablePrice;
    
    private String uid;
	private String rprice;
	private String sprice;
	private String dtimeid;
	private String sttid;
	private String secid;
	private String content;
	
	private String tableType;
	private String dingJinPrice;
      
    public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTablePrice() {
		return tablePrice;
	}

	public void setTablePrice(String tablePrice) {
		this.tablePrice = tablePrice;
	}

	public PayInfoData(PayInfo pay){  
        shopId = pay.getShopId();  
        tableId = pay.getTableId();  
        time = pay.getTime();
        tableName = pay.getTableName();
        tablePrice = pay.getTablePrice();
        uid = pay.getUid();
        rprice = pay.getRprice();
        sprice = pay.getSprice();
        dtimeid = pay.getDtimeid();
        sttid = pay.getSttid();
        secid = pay.getSecid();
        content = pay.getContent();
        tableType = pay.getTableType();
        dingJinPrice = pay.getDingJinPrice();
    }  
      
    public PayInfoData(Parcel in){  
        //顺序要和writeToParcel写的顺序一样  
        shopId = in.readString();  
        tableId = in.readString();  
        time = in.readString();
        tableName = in.readString();
        tablePrice = in.readString();
        uid = in.readString();
        rprice = in.readString();
        sprice = in.readString();
        dtimeid = in.readString();
        sttid = in.readString();
        secid = in.readString();
        content = in.readString();
        tableType = in.readString();
        dingJinPrice = in.readString();
    } 

	
  
    @Override  
    public int describeContents() {  
        return 0;  
    }  
  
    @Override  
    public void writeToParcel(Parcel dest, int flags) {  
        dest.writeString(shopId);  
        dest.writeString(tableId);  
        dest.writeString(time);
        dest.writeString(tableName);
        dest.writeString(tablePrice);
        dest.writeString(uid);
        dest.writeString(rprice);
        dest.writeString(sprice);
        dest.writeString(dtimeid);
        dest.writeString(sttid);
        dest.writeString(secid);
        dest.writeString(content);
        dest.writeString(tableType);
        dest.writeString(dingJinPrice);
    }  
      
    public static final Parcelable.Creator<PayInfoData> CREATOR = new Parcelable.Creator<PayInfoData>() {
        public PayInfoData createFromParcel(Parcel in) {  
            return new PayInfoData(in);  
        }  
          
        public PayInfoData[] newArray(int size) {  
            return new PayInfoData[size];  
        }  
    };  
  
    public String getTableType() {
		return tableType;
	}
	public void setTableType(String tableType) {
		this.tableType = tableType;
	}
	public String getDingJInPrice() {
		return dingJinPrice;
	}
	public void setDingJInPrice(String dingJinPrice) {
		this.dingJinPrice = dingJinPrice;
	}
    
    public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	 
      
    public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
    public String getShopId(){  
        return shopId;  
    }  
      
    public void setShopId(String shopId){  
        this.shopId = shopId;  
    }  
      
    public String getTableId(){  
        return tableId;  
    }  
      
    public void setTableId(String tid) {  
        this.tableId = tid;  
    }  
    
    public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getRprice() {
		return rprice;
	}
	public void setRprice(String rprice) {
		this.rprice = rprice;
	}
	public String getSprice() {
		return sprice;
	}
	public void setSprice(String sprice) {
		this.sprice = sprice;
	}
	public String getDtimeid() {
		return dtimeid;
	}
	public void setDtimeid(String dtimeid) {
		this.dtimeid = dtimeid;
	}
	public String getSttid() {
		return sttid;
	}
	public void setSttid(String sttid) {
		this.sttid = sttid;
	}
	public String getSecid() {
		return secid;
	}
	public void setSecid(String secid) {
		this.secid = secid;
	}
}  
