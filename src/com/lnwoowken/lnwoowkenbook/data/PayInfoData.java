package com.lnwoowken.lnwoowkenbook.data;

import com.lnwoowken.lnwoowkenbook.model.PayInfo;

import android.os.Parcel;
import android.os.Parcelable;

public class PayInfoData  implements Parcelable{  
    private int shopId;  
    private String tableId;  
    private String time; 
    private String tableName;
    private float tablePrice;
    
    private String uid;
	private float rprice;
	private float sprice;
	private String dtimeid;
	private String sttid;
	private String secid;
	private String content;
      
    public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public float getTablePrice() {
		return tablePrice;
	}

	public void setTablePrice(float tablePrice) {
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
    }  
      
    public PayInfoData(Parcel in){  
        //顺序要和writeToParcel写的顺序一样  
        shopId = in.readInt();  
        tableId = in.readString();  
        time = in.readString();
        tableName = in.readString();
        tablePrice = in.readFloat();
        uid = in.readString();
        rprice = in.readFloat();
        sprice = in.readFloat();
        dtimeid = in.readString();
        sttid = in.readString();
        secid = in.readString();
        content = in.readString();
    } 

	
  
    @Override  
    public int describeContents() {  
        // TODO Auto-generated method stub  
        return 0;  
    }  
  
    @Override  
    public void writeToParcel(Parcel dest, int flags) {  
        // TODO Auto-generated method stub  
        dest.writeInt(shopId);  
        dest.writeString(tableId);  
        dest.writeString(time);
        dest.writeString(tableName);
        dest.writeFloat(tablePrice);
        dest.writeString(uid);
        dest.writeFloat(rprice);
        dest.writeFloat(sprice);
        dest.writeString(dtimeid);
        dest.writeString(sttid);
        dest.writeString(secid);
        dest.writeString(content);
    }  
      
    public static final Parcelable.Creator<PayInfoData> CREATOR = new Parcelable.Creator<PayInfoData>() {  
        public PayInfoData createFromParcel(Parcel in) {  
            return new PayInfoData(in);  
        }  
          
        public PayInfoData[] newArray(int size) {  
            return new PayInfoData[size];  
        }  
    };  
  
    
    
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
    public int getShopId(){  
        return shopId;  
    }  
      
    public void setShopId(int name){  
        this.shopId = name;  
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
	public float getRprice() {
		return rprice;
	}
	public void setRprice(float rprice) {
		this.rprice = rprice;
	}
	public float getSprice() {
		return sprice;
	}
	public void setSprice(float sprice) {
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
