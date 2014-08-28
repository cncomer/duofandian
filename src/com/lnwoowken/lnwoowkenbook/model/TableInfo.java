

package com.lnwoowken.lnwoowkenbook.model;
/**
 * 桌子信息,请使用TableInfoObject代替
 * @author sean
 * @hide
 *
 */
public class TableInfo {

	private String aid;//--时段id
	private String name;//--桌名
	//private String tntid;//--桌型id
	private String rt;//--时点
	private String style;//--桌型
	private String stid;
	//private String total;//--当前桌子状态
	private String price;//--价格
	private String sPrice;//--价格
	private String state;//--状态
	//private String drid;//--时段id	
	private String rCode;//--订单号
	private String phone;//--联系方式
	private String peopleNum;//--人数
	private String tableid;//--桌子id
	
	private String note;//抢位备注
	
	public String getTableStyle() {
		return style;
	}

	public void setTableStyle(String dtypeid) {
		this.style = dtypeid;
	}

	public String getStid() {
		return stid;
	}

	public void setStid(String stid) {
		this.stid = stid;
	}
	public String getTableId() {
		return tableid;
	}

	public void setTableId(String tid) {
		this.tableid = tid;
	}

	public String getStaRes() {
		return state;
	}

	public void setStaRes(String staRes) {
		this.state = staRes;
	}
//
//	public String getDrid() {
//		return drid;
//	}
//
//	public void setDrid(String drid) {
//		this.drid = drid;
//	}

	public String getRt() {
		return rt;
	}

	public void setRt(String rt) {
		this.rt = rt;
	}

//	public String getTnid() {
//		return tntid;
//	}
//
//	public void setTnid(String tnid) {
//		this.tntid = tnid;
//	}

	public String getrCode() {
		return rCode;
	}

	public void setrCode(String rCode) {
		this.rCode = rCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPeopleNum() {
		return peopleNum;
	}

	public void setPeopleNum(String num) {
		this.peopleNum = num;
	}

	public String getSprice() {
		return sPrice;
	}

	public void setSprice(String sPrice) {
		this.sPrice = sPrice;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
	public String getAId() {
		return aid;
	}

	public void setAId(String id) {
		this.aid = id;
	}

	public String getTableName() {
		return name;
	}

	public void setTableName(String name) {
		this.name = name;
	}
	public String getNote() {
		return note;
	}
	public String setNote(String note) {
		return this.note = note;
	}
//
//	public String getTotal() {
//		return total;
//	}
//
//	public void setTotal(String total) {
//		this.total = total;
//	}
}
