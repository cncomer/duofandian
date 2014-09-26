package com.cncom.app.base.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.cncom.app.base.database.BjnoteContent;
import com.cncom.app.base.database.DBHelper;
import com.shwy.bestjoy.utils.DebugUtils;
import com.shwy.bestjoy.utils.InfoInterface;
/**
 * 
 * {"Address":"上海市黄浦区中山南路505弄2号楼6层",
 * "Serverprice":"0",
 * "HeadID":28,
 * "ShopID":48,
 * "ShopName":"码头人家（Test)",
 * "CaiXi":"3",
 * "ShuYuZhuZhi":"1",
 * "GuDingPhone":null,
 * "Contacts":"司双前",
 * "contacts_phone":"400-8809488",
 * "Contacts_Telephone":"",
 * "ShopAddrID":null,
 * "shop_brief":"耳边传来金嗓子周璇的靡靡之音，眼前的则是旧时上海的石库门里弄，一进门就可以看见一辆黄包车和一张八仙桌",
 * "qiang_wei_img":"","shop_img":"","ren_jun":"90","pingfen":null,"youhui":"","tuangou":"","diancan":"","maidan":"",
 * "showid":"280002","Shen":"上海","City":"宝山区","Qu":"大华地区","desk_count":23,
 * "tip":{"TipOne":"会员专座免除排队等候时间，抢到即可前往就餐。","TipTwo":"请会员在预订的时间准时到达，夺饭点为您保留座位10分钟。","TipThree":"抢位成功后，会员提前2小时退订或拨打电话XXX-XX-XXX退订，则您的服务费预定金直接退入您夺饭点账户，期待您下次消费。"}}
 * @author chenkai
 *
 */
public class ShopInfoObject implements InfoInterface{
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
	
	public String mQiangweiTip;
	public String mOrderConfirmTip;
	public String mOrderPayTip;
	
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
		
		DBHelper.SHOP_QIANGWEI_TIP,
		DBHelper.SHOP_ORDER_CONFIRM_TIP,
		DBHelper.SHOP_ORDER_PAY_TIP,
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
	  
	  public static final String SHOP_QIANGWEI_TIP = "TipOne";
	  public static final String SHOP_ORDER_CONFIRM_TIP = "TipTwo";
	  public static final String SHOP_ORDER_PAY_TIP = "TipThree";
	  
	  
	  public static String SHOP_ID_SELECTION = DBHelper.SHOP_ID + "=?";
	
	 public String getDetailAddress() {
		 StringBuilder sb = new StringBuilder();
		 sb.append(getShopShen()).append(getShopCity()).append(getShopQu()).append(getShopAddress());
		 return sb.toString();
	 }
	 
	 @Override
	 public String toString() {
		 StringBuilder sb = new StringBuilder();
		 sb.append(TAG).append("[shopID=").append(shopID).append(", shopName=").append(shopName).append("]");
		 return sb.toString();
	 }

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
	
	public static List<ShopInfoObject> getShopInfo(JSONArray shops) throws JSONException {
		List<ShopInfoObject> result = new ArrayList<ShopInfoObject>();
		if(shops == null) return result;
		//{"ShopName":"望湘园（测试1）","deskCount":"0","showid":"010005","HeadID":null,"HeadName":"望湘园","ShopID":"12"}
		//{"Address":"上海市金山区长宁路1018号龙之梦购物中心5楼蓝中庭","Serverprice":"","HeadID":1,"ShopID":11,"ShopName":"望湘园（测试）","CaiXi":"1","ShuYuZhuZhi":"1","GuDingPhone":null,"Contacts":"柳智","contacts_phone":"18621951099","ShopAddrID":null,"shop_brief":"望湘园换短发","qiang_wei_img":"","shop_img":"","ren_jun":"0","pingfen":null,"youhui":"0","tuangou":"0","diancan":"0","maidan":"0","showid":"010004","detail":null}
		for(int i = 0; i < shops.length(); i++) {
			ShopInfoObject shopInfoObject = getShopInfoObjectFromJsonObject(shops.getJSONObject(i));
			result.add(shopInfoObject);
		}
		return result;
	}
	
	public static ShopInfoObject getShopInfoObjectFromJsonObject(JSONObject obj) throws JSONException {
		ShopInfoObject shopInfoObject = new ShopInfoObject();
		shopInfoObject.setShopAddress(obj.getString(ShopInfoObject.SHOP_ADDREES));
		shopInfoObject.setShopServerprice(obj.getString(ShopInfoObject.SHOP_SERVERPRICE));
		shopInfoObject.setShopHeadId(obj.getString(ShopInfoObject.SHOP_HEAD_ID));
		shopInfoObject.setShopID(obj.getString(ShopInfoObject.SHOP_ID));
		shopInfoObject.setShopName(obj.getString(ShopInfoObject.SHOP_NAME));
		shopInfoObject.setShopCaiXi(obj.getString(ShopInfoObject.SHOP_CAIXI));
		shopInfoObject.setShopShuYuZhuZhi(obj.getString(ShopInfoObject.SHOP_SHUYUZHUZHI));
		shopInfoObject.setShopGuDingPhone(obj.getString(ShopInfoObject.SHOP_GUDINGPHONE));
		shopInfoObject.setShopContacts(obj.getString(ShopInfoObject.SHOP_CONTACTS));
		shopInfoObject.setShopContactsPhone(obj.getString(ShopInfoObject.SHOP_CONTACTS_PHONE));
		shopInfoObject.setShopAddrID(obj.getString(ShopInfoObject.SHOP_ADDR_ID));
		shopInfoObject.setShopBrief(obj.getString(ShopInfoObject.SHOP_BRIEF));
		shopInfoObject.setShopQiangWeiImg(obj.getString(ShopInfoObject.SHOP_QIANG_WEI_IMG));
		shopInfoObject.setShopImg(obj.getString(ShopInfoObject.SHOP_IMG));
		shopInfoObject.setShopRenJun(obj.getString(ShopInfoObject.SHOP_RENJUN));
		shopInfoObject.setShopPingFen(obj.getString(ShopInfoObject.SHOP_PINGFEN));
		shopInfoObject.setShopYouHui(obj.getString(ShopInfoObject.SHOP_YOUHUI));
		shopInfoObject.setShopTuanGou(obj.getString(ShopInfoObject.SHOP_TUANGOU));
		shopInfoObject.setShopDianCan(obj.getString(ShopInfoObject.SHOP_DIANCAN));
		shopInfoObject.setShopMaiDian(obj.getString(ShopInfoObject.SHOP_MAIDIAN));
		shopInfoObject.setShopShowId(obj.getString(ShopInfoObject.SHOP_SHOW_ID));
		//shopInfoObject.setShopDetail(obj.getString(ShopInfoObject.SHOP_DETAIL));
		shopInfoObject.setShopShen(obj.getString(ShopInfoObject.SHOP_SHEN));
		shopInfoObject.setShopCity(obj.getString(ShopInfoObject.SHOP_CITY));
		shopInfoObject.setShopQu(obj.getString(ShopInfoObject.SHOP_QU));
		shopInfoObject.setShopDeskCount(obj.getString(ShopInfoObject.SHOP_DESK_COUNT));
		
		JSONObject tipJSONObject = obj.optJSONObject("tip");
		if (tipJSONObject != null) {
			DebugUtils.logD(TAG, "getShopInfoObjectFromJsonObject find tipJSONObject=" + tipJSONObject.toString());
			shopInfoObject.mQiangweiTip = tipJSONObject.optString(ShopInfoObject.SHOP_QIANGWEI_TIP, "");
			shopInfoObject.mOrderConfirmTip = tipJSONObject.optString(ShopInfoObject.SHOP_ORDER_CONFIRM_TIP, "");
			shopInfoObject.mOrderPayTip = tipJSONObject.optString(ShopInfoObject.SHOP_ORDER_PAY_TIP, "");
		}
		return shopInfoObject;
	}
	
	public static ShopInfoObject getShopInfoObjectByShopId(ContentResolver cr, String shopId) {
		ShopInfoObject shopInfoObject = null;
		Cursor c = cr.query(BjnoteContent.Shops.CONTENT_URI, SHOP_PROJECTION, SHOP_ID_SELECTION, new String[]{shopId}, null);
		if (c != null) {
			if (c.moveToNext()) {
				shopInfoObject = getShopInfoObject(c);
				c.close();
			}
		}
		
		return shopInfoObject;

	}
	
	public static ShopInfoObject getShopInfoObject(Cursor c) {
		ShopInfoObject shopInfoObject = new ShopInfoObject();
		
		shopInfoObject.setShopAddress(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_ADDREES)));
		shopInfoObject.setShopServerprice(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_SERVERPRICE)));
		shopInfoObject.setShopHeadId(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_HEAD_ID)));
		shopInfoObject.setShopID(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_ID)));
		shopInfoObject.setShopName(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_NAME)));
		shopInfoObject.setShopCaiXi(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_CAIXI)));
		shopInfoObject.setShopShuYuZhuZhi(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_SHUYUZHUZHI)));
		shopInfoObject.setShopGuDingPhone(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_GUDINGPHONE)));
		shopInfoObject.setShopContacts(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_CONTACTS)));
		shopInfoObject.setShopContactsPhone(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_CONTACTS_PHONE)));
		shopInfoObject.setShopAddrID(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_ADDR_ID)));
		shopInfoObject.setShopBrief(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_BRIEF)));
		shopInfoObject.setShopQiangWeiImg(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_QIANG_WEI_IMG)));
		shopInfoObject.setShopImg(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_IMG)));
		shopInfoObject.setShopRenJun(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_RENJUN)));
		shopInfoObject.setShopPingFen(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_PINGFEN)));
		shopInfoObject.setShopYouHui(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_YOUHUI)));
		shopInfoObject.setShopTuanGou(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_TUANGOU)));
		shopInfoObject.setShopDianCan(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_DIANCAN)));
		shopInfoObject.setShopMaiDian(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_MAIDIAN)));
		shopInfoObject.setShopShowId(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_SHOW_ID)));
		//shopInfoObject.setShopDetail(obj.getString(ShopInfoObject.SHOP_DETAIL));
		shopInfoObject.setShopShen(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_SHEN)));
		shopInfoObject.setShopCity(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_CITY)));
		shopInfoObject.setShopQu(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_QU)));
		shopInfoObject.setShopDeskCount(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_DESK_COUNT)));
		
		shopInfoObject.mQiangweiTip = c.getString(c.getColumnIndex(ShopInfoObject.SHOP_QIANGWEI_TIP));
		shopInfoObject.mOrderConfirmTip = c.getString(c.getColumnIndex(ShopInfoObject.SHOP_ORDER_CONFIRM_TIP));
		shopInfoObject.mOrderPayTip = c.getString(c.getColumnIndex(ShopInfoObject.SHOP_ORDER_PAY_TIP));
		return shopInfoObject;
	}
	
	@Override
	public boolean saveInDatebase(ContentResolver cr, ContentValues addtion) {
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
		
		values.put(DBHelper.SHOP_QIANGWEI_TIP, mQiangweiTip);
		values.put(DBHelper.SHOP_ORDER_CONFIRM_TIP, mOrderConfirmTip);
		values.put(DBHelper.SHOP_ORDER_PAY_TIP, mOrderPayTip);
		
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
