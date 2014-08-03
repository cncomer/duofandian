

package com.lnwoowken.lnwoowkenbook.model;
/**
 * 桌子信息
 * @author sean
 *
 */
public class TableInfo {

	private String aid;//--时段id
	private String aname;//--桌名
	//private String tntid;//--桌型id
	private String rt;//--时点
	private String dtypeid;//--桌型id
	private String stid;
	//private String total;//--当前桌子状态
	//private String price;//--价格
	private String staRes;//--状态
	//private String drid;//--时段id	
	private String rCode;//--订单号
	private String phone;//--联系方式
	private String peopleNum;//--人数
	private String tid;//--桌子id
	
	public String getDtypeid() {
		return dtypeid;
	}

	public void setDtypeid(String dtypeid) {
		this.dtypeid = dtypeid;
	}

	public String getStid() {
		return stid;
	}

	public void setStid(String stid) {
		this.stid = stid;
	}
	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getStaRes() {
		return staRes;
	}

	public void setStaRes(String staRes) {
		this.staRes = staRes;
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


	
	
//	public String getPrice() {
//		return price;
//	}
//
//	public void setPrice(String price) {
//		this.price = price;
//	}

	
	public String getAId() {
		return aid;
	}

	public void setAId(String id) {
		this.aid = id;
	}

	public String getAname() {
		return aname;
	}

	public void setAname(String name) {
		this.aname = name;
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
