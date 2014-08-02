package com.lnwoowken.lnwoowkenbook.model;

import java.util.List;

public class StoreInfo {
	private int tableNum;//--��ǰʱ�����ʣ�������
	private int id;//--��ƷID
	private String name;//--����
	private String address;//--��
	private String phoneNumber;//--��ϵ�绰
	private String priceLevel;//--�۸�����
	private String environmentLevel;//--��������
	private String flavorLevel;//--ζ������
	private String info;//--���
	private String averagePrice;//--�˾�����
	private String imagePath;//--ͼƬ·��
	private String tableImagePath;//--����ͼƬ·��
	private List<BookTime> timeList;
	private String servicePrice;//--�����
	private String icon;
	
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getServicePrice() {
		return servicePrice;
	}

	public void setServicePrice(String servicePrice) {
		this.servicePrice = servicePrice;
	}

	public int getTableNum() {
		return tableNum;
	}

	public void setTableNum(int tableNum) {
		this.tableNum = tableNum;
	}
	
	public List<BookTime> getTimeList() {
		return timeList;
	}

	public void setTimeList(List<BookTime> timeList) {
		this.timeList = timeList;
	}

	public String getTableImagePath() {
		return tableImagePath;
	}

	public void setTableImagePath(String tableImagePath) {
		this.tableImagePath = tableImagePath;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPriceLevel() {
		return priceLevel;
	}

	public void setPriceLevel(String priceLevel) {
		this.priceLevel = priceLevel;
	}

	public String getEnvironmentLevel() {
		return environmentLevel;
	}

	public void setEnvironmentLevel(String environmentLevel) {
		this.environmentLevel = environmentLevel;
	}

	public String getFlavorLevel() {
		return flavorLevel;
	}

	public void setFlavorLevel(String flavorLevel) {
		this.flavorLevel = flavorLevel;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getAveragePrice() {
		return averagePrice;
	}

	public void setAveragePrice(String averagePrice) {
		this.averagePrice = averagePrice;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
