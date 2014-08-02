package com.lnwoowken.lnwoowkenbook.model;

public class BookTime {

	
	private int id;//--时段ID
	private int sid;//--商铺ID
	private String period;//--过期时间
	private String timeName;//--时段名
	private String rstart;//--时段有效开始时间
	private String rend;//--时段有效结束时间
	private String rsTime;//--起始时间
	private String rdTime;//--结束时间
	private String price;//--价格
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getTimeName() {
		return timeName;
	}
	public void setTimeName(String timeName) {
		this.timeName = timeName;
	}
	public String getRstart() {
		return rstart;
	}
	public void setRstart(String rstart) {
		this.rstart = rstart;
	}
	public String getRend() {
		return rend;
	}
	public void setRend(String rend) {
		this.rend = rend;
	}
	public String getRsTime() {
		return rsTime;
	}
	public void setRsTime(String rsTime) {
		this.rsTime = rsTime;
	}
	public String getRdTime() {
		return rdTime;
	}
	public void setRdTime(String rdTime) {
		this.rdTime = rdTime;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
	
	
}
