package com.cncom.app.base.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import com.cncom.app.base.database.BjnoteContent;
import com.cncom.app.base.database.DBHelper;
import com.lnwoowken.lnwoowkenbook.ServiceObject;
import com.shwy.bestjoy.utils.DebugUtils;
import com.shwy.bestjoy.utils.UrlEncodeStringBuilder;

public class ShopInfoObject {
	private static final String TAG = "ShopInfoObject";
	private String shopAddress;
	private String shopServerprice;
	private String shopHeadId;
	private String shopID;
	private String shopName;
	private String shopCaiXi;
	private String shopShuYuZhuZhi;
	private String shopGuDingPhone;
	private String shopContacts;
	private String shopContactsPhone;
	private String shopAddrID;
	private String shopBrief;
	private String shopQiangWeiImg;
	private String shopImg;
	private String shopRenJun;
	private String shopPingFen;
	private String shopYouHui;
	private String shopTuanGou;
	private String shopDianCan;
	private String shopMaiDian;
	private String shopShowId;
	private String shopDetail;
	private String shopShen;
	private String shopCity;
	private String shopQu;
	private String shopDeskCount;
	
	private String maintenancePointUrl;
	public static final String[] SHOP_PROJECTION = new String[]{
		DBHelper.ID,
		DBHelper.SHOP_ADDREES,
		DBHelper.SHOP_SERVERPRICE,
		DBHelper.SHOP_HEAD_ID,
		DBHelper.SHOP_ID,
		DBHelper.SHOP_NAME,
		DBHelper.SHOP_CAIXI,
		DBHelper.SHOP_SHUYUZHUZHI,
		DBHelper.SHOP_GUDINGPHONE,
		DBHelper.SHOP_CONTACTS,
		DBHelper.SHOP_CONTACTS_PHONE,
		DBHelper.SHOP_ADDR_ID,
		DBHelper.SHOP_BRIEF,
		DBHelper.SHOP_QIANG_WEI_IMG,
		DBHelper.SHOP_IMG,
		DBHelper.SHOP_RENJUN,
		DBHelper.SHOP_PINGFEN,
		DBHelper.SHOP_YOUHUI,
		DBHelper.SHOP_TUANGOU,
		DBHelper.SHOP_DIANCAN,
		DBHelper.SHOP_MAIDIAN,
		DBHelper.SHOP_SHOW_ID,
		DBHelper.SHOP_DETAIL,
		DBHelper.SHOP_SHEN,
		DBHelper.SHOP_CITY,
		DBHelper.SHOP_QU,
		DBHelper.SHOP_DESK_COUNT,
	};

	/*public static final String MAINTENCE_PROJECTION_AID_SELECTION = DBHelper.MAINTENCE_POINT_AID + "=?";
	public static final String MAINTENCE_PROJECTION_BID_SELECTION = DBHelper.MAINTENCE_POINT_BID + "=?";
	public static final String MAINTENCE_PROJECTION_AID_BID_SELECTION = MAINTENCE_PROJECTION_AID_SELECTION + " and " + MAINTENCE_PROJECTION_BID_SELECTION;
	*/
	  public static final String SHOP_ADDREES = "Address";
	  public static final String SHOP_SERVERPRICE = "Serverprice";
	  public static final String SHOP_HEAD_ID = "HeadID";
	  public static final String SHOP_ID = "ShopID";
	  public static final String SHOP_NAME = "ShopName";
	  public static final String SHOP_CAIXI = "CaiXi";
	  public static final String SHOP_SHUYUZHUZHI = "ShuYuZhuZhi";
	  public static final String SHOP_GUDINGPHONE = "GuDingPhone";
	  public static final String SHOP_CONTACTS = "Contacts";
	  public static final String SHOP_CONTACTS_PHONE = "contacts_phone";
	  public static final String SHOP_ADDR_ID = "ShopAddrID";
	  public static final String SHOP_BRIEF = "shop_brief";
	  public static final String SHOP_QIANG_WEI_IMG = "qiang_wei_img";
	  public static final String SHOP_IMG = "shop_img";
	  public static final String SHOP_RENJUN = "ren_jun";
	  public static final String SHOP_PINGFEN = "pingfen";
	  public static final String SHOP_YOUHUI = "youhui";
	  public static final String SHOP_TUANGOU = "tuangou";
	  public static final String SHOP_DIANCAN = "diancan";
	  public static final String SHOP_MAIDIAN = "maidan";
	  public static final String SHOP_SHOW_ID = "showid";
	  public static final String SHOP_DETAIL = "detail";
	  public static final String SHOP_SHEN = "Shen";
	  public static final String SHOP_CITY = "City";
	  public static final String SHOP_QU = "Qu";
	  public static final String SHOP_DESK_COUNT = "desk_count";
	

	public String getShopAddress() {
		return shopAddress;
	}
	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}

	public String getShopServerprice() {
		return shopServerprice;
	}
	public void setShopServerprice(String shopServerprice) {
		this.shopServerprice = shopServerprice;
	}

	public String getShopHeadId() {
		return shopHeadId;
	}
	public void setShopHeadId(String shopHeadId) {
		this.shopHeadId = shopHeadId;
	}

	public String getShopID() {
		return shopID;
	}
	public void setShopID(String shopID) {
		this.shopID = shopID;
	}
	
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	
	public String getShopCaiXi() {
		return shopCaiXi;
	}
	public void setShopCaiXi(String shopCaiXi) {
		this.shopCaiXi = shopCaiXi;
	}
	
	public String getShopShuYuZhuZhi() {
		return shopShuYuZhuZhi;
	}
	public void setShopShuYuZhuZhi(String shopShuYuZhuZhi) {
		this.shopShuYuZhuZhi = shopShuYuZhuZhi;
	}
	
	public String getShopGuDingPhone() {
		return shopGuDingPhone;
	}
	public void setShopGuDingPhone(String shopGuDingPhone) {
		this.shopGuDingPhone = shopGuDingPhone;
	}
	
	public String getShopContacts() {
		return shopContacts;
	}
	public void setShopContacts(String shopContacts) {
		this.shopContacts = shopContacts;
	}
	
	public String getShopContactsPhone() {
		return shopContactsPhone;
	}
	public void setShopContactsPhone(String shopContactsPhone) {
		this.shopContactsPhone = shopContactsPhone;
	}
	
	public String getShopAddrID() {
		return shopAddrID;
	}
	public void setShopAddrID(String shopAddrID) {
		this.shopAddrID = shopAddrID;
	}
	
	public String getShopBrief() {
		return shopBrief;
	}
	public void setShopBrief(String shopBrief) {
		this.shopBrief = shopBrief;
	}
	
	public String getShopQiangWeiImg() {
		return shopQiangWeiImg;
	}
	public void setShopQiangWeiImg(String shopQiangWeiImg) {
		this.shopQiangWeiImg = shopQiangWeiImg;
	}
	
	public String getShopImg() {
		return shopImg;
	}
	public void setShopImg(String shopImg) {
		this.shopImg = shopImg;
	}
	
	public String getShopRenJun() {
		return shopRenJun;
	}
	public void setShopRenJun(String shopRenJun) {
		this.shopRenJun = shopRenJun;
	}
	
	public String getShopPingFen() {
		return shopPingFen;
	}
	public void setShopPingFen(String shopPingFen) {
		this.shopPingFen = shopPingFen;
	}
	
	public String getShopYouHui() {
		return shopYouHui;
	}
	public void setShopYouHui(String shopYouHui) {
		this.shopYouHui = shopYouHui;
	}
	
	public String getShopTuanGou() {
		return shopTuanGou;
	}
	public void setShopTuanGou(String shopTuanGou) {
		this.shopTuanGou = shopTuanGou;
	}
	
	public String getShopDianCan() {
		return shopDianCan;
	}
	public void setShopDianCan(String shopDianCan) {
		this.shopDianCan = shopDianCan;
	}
	
	public String getShopMaiDian() {
		return shopMaiDian;
	}
	public void setShopMaiDian(String shopMaiDian) {
		this.shopMaiDian = shopMaiDian;
	}
	
	public String getShopShowId() {
		return shopShowId;
	}
	public void setShopShowId(String shopShowId) {
		this.shopShowId = shopShowId;
	}
	
	public String getShopDetail() {
		return shopDetail;
	}
	public void setShopDetail(String shopDetail) {
		this.shopDetail = shopDetail;
	}
	
	public String getShopShen() {
		return shopShen;
	}
	public void setShopShen(String shopShen) {
		this.shopShen = shopShen;
	}
	
	public String getShopCity() {
		return shopCity;
	}
	public void setShopCity(String shopCity) {
		this.shopCity = shopCity;
	}
	
	public String getShopQu() {
		return shopQu;
	}
	public void setShopQu(String shopQu) {
		this.shopQu = shopQu;
	}
	
	public String getShopDeskCount() {
		return shopDeskCount;
	}
	public void setShopDeskCount(String shopDeskCount) {
		this.shopDeskCount = shopDeskCount;
	}
	/**返回形如showId_01的形式，主要用来加载商铺图片的*/
	public String getShopPhotoId(String type) {
		StringBuilder sb = new StringBuilder();
		sb.append(shopShowId).append("_").append(type);
		return sb.toString();
	}
	
	public boolean saveDatabase(ContentResolver cr, ContentValues addtion) {
		ContentValues values = new ContentValues();
		if (addtion != null) {
			values.putAll(addtion);
		}
		values.put(DBHelper.SHOP_ADDREES, shopAddress);
		values.put(DBHelper.SHOP_SERVERPRICE, shopServerprice);
		values.put(DBHelper.SHOP_HEAD_ID, shopHeadId);
		values.put(DBHelper.SHOP_ID, shopID);
		values.put(DBHelper.SHOP_NAME, shopName);
		values.put(DBHelper.SHOP_CAIXI, shopCaiXi);
		values.put(DBHelper.SHOP_SHUYUZHUZHI, shopShuYuZhuZhi);
		values.put(DBHelper.SHOP_GUDINGPHONE, shopGuDingPhone);
		values.put(DBHelper.SHOP_CONTACTS, shopContacts);
		values.put(DBHelper.SHOP_CONTACTS_PHONE, shopContactsPhone);
		values.put(DBHelper.SHOP_ADDR_ID, shopAddrID);
		values.put(DBHelper.SHOP_BRIEF, shopBrief);
		values.put(DBHelper.SHOP_QIANG_WEI_IMG, shopQiangWeiImg);
		values.put(DBHelper.SHOP_IMG, shopImg);
		values.put(DBHelper.SHOP_RENJUN, shopRenJun);
		values.put(DBHelper.SHOP_PINGFEN, shopPingFen);
		values.put(DBHelper.SHOP_YOUHUI, shopYouHui);
		values.put(DBHelper.SHOP_TUANGOU, shopTuanGou);
		values.put(DBHelper.SHOP_DIANCAN, shopDianCan);
		values.put(DBHelper.SHOP_MAIDIAN, shopMaiDian);
		values.put(DBHelper.SHOP_SHOW_ID, shopShowId);
		values.put(DBHelper.SHOP_DETAIL, shopDetail);
		values.put(DBHelper.SHOP_SHEN, shopShen);
		values.put(DBHelper.SHOP_CITY, shopCity);
		values.put(DBHelper.SHOP_QU, shopQu);
		values.put(DBHelper.SHOP_DESK_COUNT, shopDeskCount);
		
		Uri uri = cr.insert(BjnoteContent.Shops.CONTENT_URI, values);
		if (uri != null) {
			DebugUtils.logD(TAG, "saveInDatebase insert");
			return true;
		} else {
			DebugUtils.logD(TAG, "saveInDatebase failly insert");
		}
		return false;
	}
}
