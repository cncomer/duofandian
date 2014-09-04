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

public class PatternInfoUtils {
	private static final String TAG = "PatternInfoUtils";
	private static final String SELECTION_BY_SHOPID = DBHelper.SHOP_ID + "=?";

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
	
	public static List<ShopInfoObject> getShopInfo(JSONArray shops, ContentResolver cr) throws JSONException {
		List<ShopInfoObject> result = new ArrayList<ShopInfoObject>();
		if(shops == null) return result;
		//{"ShopName":"望湘园（测试1）","deskCount":"0","showid":"010005","HeadID":null,"HeadName":"望湘园","ShopID":"12"}
		//{"Address":"上海市金山区长宁路1018号龙之梦购物中心5楼蓝中庭","Serverprice":"","HeadID":1,"ShopID":11,"ShopName":"望湘园（测试）","CaiXi":"1","ShuYuZhuZhi":"1","GuDingPhone":null,"Contacts":"柳智","contacts_phone":"18621951099","ShopAddrID":null,"shop_brief":"望湘园换短发","qiang_wei_img":"","shop_img":"","ren_jun":"0","pingfen":null,"youhui":"0","tuangou":"0","diancan":"0","maidan":"0","showid":"010004","detail":null}
		for(int i = 0; i < shops.length(); i++) {
			ShopInfoObject shopInfoObject = getShopInfoObjectFromJsonObject(shops.getJSONObject(i));
			shopInfoObject.saveDatabase(cr, null);
			result.add(shopInfoObject);
		}
		return result;
	}
	
	public static List<ShopInfoObject> getShopInfoClean(JSONArray shops, ContentResolver cr) throws JSONException {
		deleteCachedData(cr);
		
		return getShopInfo(shops, cr);
	}
	
	public static List<ShopInfoObject> getShopInfoLocal(ContentResolver cr){
		List<ShopInfoObject> result = new ArrayList<ShopInfoObject>();
		Cursor c = cr.query(BjnoteContent.Shops.CONTENT_URI, ShopInfoObject.SHOP_PROJECTION, null, null, null);
		if (c != null) {
			while (c.moveToNext()) {
				ShopInfoObject shopInfoObject = getShopInfoObjectFromCursor(c);
				if (shopInfoObject != null && shopInfoObject.getShopID() != null) {
					result.add(shopInfoObject);
				}
				
			}
			c.close();
		}
		return result;
	}
	
	public static ShopInfoObject getShopInfoObjectFromCursor(Cursor c) {
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
		//shopInfoObject.setShopDetail(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_DETAIL)));
		shopInfoObject.setShopShen(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_SHEN)));
		shopInfoObject.setShopCity(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_CITY)));
		shopInfoObject.setShopQu(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_QU)));
		shopInfoObject.setShopDeskCount(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_DESK_COUNT)));
		
		shopInfoObject.mQiangweiTip = c.getString(c.getColumnIndex(ShopInfoObject.SHOP_QIANGWEI_TIP));
		shopInfoObject.mOrderConfirmTip = c.getString(c.getColumnIndex(ShopInfoObject.SHOP_ORDER_CONFIRM_TIP));
		shopInfoObject.mOrderPayTip = c.getString(c.getColumnIndex(ShopInfoObject.SHOP_ORDER_PAY_TIP));
		
		return shopInfoObject;
	}
	
	public static ShopInfoObject getShopInfoObjectFromJsonObject(JSONObject obj) throws JSONException {
		if (obj == null) {
			return null;
		}
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
	
	public static ShopInfoObject getShopInfoLocalById(ContentResolver cr, String shopId){
		ShopInfoObject shopInfoObject = new ShopInfoObject();
		Cursor c = cr.query(BjnoteContent.Shops.CONTENT_URI, ShopInfoObject.SHOP_PROJECTION, SELECTION_BY_SHOPID, new String[] {shopId}, null);
		if (c != null) {
			if (c.moveToNext()) {
				shopInfoObject = getShopInfoObjectFromCursor(c);
			}
			c.close();
		}
		return shopInfoObject;
	}
	
	public static List<TableInfoObject> getShopAvailableTableList(JSONArray shops) throws JSONException {
		List<TableInfoObject> result = new ArrayList<TableInfoObject>();
		if(shops == null) return result;
		//{"shiduan_id":"1","desk_type":"2人桌(1-2人)","desk_name":"A2","date":"2014/7/18 0:00:00","desk_status":"2","shiduan_time":"12:15","shiduan_name":"午市","DeskID":"1"}
		for(int i = 0; i < shops.length(); i++) {
			JSONObject obj = shops.getJSONObject(i);
			TableInfoObject shopAvailableTableObject = new TableInfoObject();
			shopAvailableTableObject.setShiduanId(obj.getString(TableInfoObject.SHIDUAN_ID));
			shopAvailableTableObject.setDeskType(obj.getString(TableInfoObject.DESK_TYPE));
			shopAvailableTableObject.setDeskName(obj.getString(TableInfoObject.DESK_NAME));
			shopAvailableTableObject.setDate(obj.getString(TableInfoObject.DATE));
			shopAvailableTableObject.setDeskStates(obj.getString(TableInfoObject.DATE_STATES));
			shopAvailableTableObject.setShiduanTime(obj.getString(TableInfoObject.SHIDUAN_TIME));
			shopAvailableTableObject.setShiduanName(obj.getString(TableInfoObject.SHIDUAN_TIME));
			shopAvailableTableObject.setDeskId(obj.getString(TableInfoObject.DESK_ID));
			shopAvailableTableObject.setDabiaoPrice(obj.getString(TableInfoObject.DESK_DABIAO_PRICE));
			shopAvailableTableObject.setServicePrice(obj.getString(TableInfoObject.DESK_SERVICE_PRICE));
			shopAvailableTableObject.setDingJinPrice(obj.getString(TableInfoObject.DESK_DINGJIN_PRICE));
			result.add(shopAvailableTableObject);
		}
		return result;
	}
	
//	public static TableInfoObject getTableInfoByName(ContentResolver cr, String tableName) {
//		Cursor c = cr.query(BjnoteContent.Bills.CONTENT_URI, TableInfoObject.TABLE_PROJECTION, TableInfoObject.TABLE_NAME_PROJECTION, new String[] {tableName}, null);
//		if (c != null) {
//			while (c.moveToNext()) {
//				return getTableObject(c);
//			}
//		}
//		
//		return new TableInfoObject();
//	}
//	
//	private static TableInfoObject getTableObject(Cursor c) {
//		TableInfoObject tableObj = new TableInfoObject();
//		tableObj.setShiduanId(c.getString(c.getColumnIndex(TableInfoObject.SHIDUAN_ID)));
//		tableObj.setDeskType(c.getString(c.getColumnIndex(TableInfoObject.DESK_TYPE)));
//		tableObj.setDeskName(c.getString(c.getColumnIndex(TableInfoObject.DESK_NAME)));
//		tableObj.setDate(c.getString(c.getColumnIndex(TableInfoObject.DATE)));
//		tableObj.setDeskStates(c.getString(c.getColumnIndex(TableInfoObject.DATE_STATES)));
//		tableObj.setShiduanTime(c.getString(c.getColumnIndex(TableInfoObject.SHIDUAN_TIME)));
//		tableObj.setShiduanName(c.getString(c.getColumnIndex(TableInfoObject.SHIDUAN_TIME)));
//		tableObj.setDeskId(c.getString(c.getColumnIndex(TableInfoObject.DESK_ID)));
//		tableObj.setDabiaoPrice(c.getString(c.getColumnIndex(TableInfoObject.DESK_DABIAO_PRICE)));
//		tableObj.setServicePrice(c.getString(c.getColumnIndex(TableInfoObject.DESK_SERVICE_PRICE)));
//		
//		return tableObj;
//	}

	public static List<String> getCaixiListLocal(ContentResolver cr) throws JSONException {
		List<String> result = new ArrayList<String>();
		Cursor c = cr.query(BjnoteContent.Caixi.CONTENT_URI, new String[] {DBHelper.CAIXI_NAME}, null, null, null);
		if (c != null) {
			while (c.moveToNext()) {
				result.add(c.getString(c.getColumnIndex(DBHelper.CAIXI_NAME)));
			}
			c.close();
		}
		return result;
	}
	
	public static List<String> getShangquanListLocal(ContentResolver cr) throws JSONException {
		List<String> result = new ArrayList<String>();
		Cursor c = cr.query(BjnoteContent.Shangquan.CONTENT_URI, new String[] {DBHelper.SHANGQUAN_NAME}, null, null, null);
		if (c != null) {
			while (c.moveToNext()) {
				result.add(c.getString(c.getColumnIndex(DBHelper.SHANGQUAN_NAME)));
			}
			c.close();
		}
		return result;
	}
	
	private static boolean saveCaixiDatabase(ContentResolver cr, String name) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.CAIXI_NAME, name);
		
		Uri uri = cr.insert(BjnoteContent.Caixi.CONTENT_URI, values);
		if (uri != null) {
			return true;
		}
		return false;
	}
	
	private static boolean saveShangquanDatabase(ContentResolver cr, String name) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.SHANGQUAN_NAME, name);
		
		Uri uri = cr.insert(BjnoteContent.Shangquan.CONTENT_URI, values);
		if (uri != null) {
			return true;
		}
		return false;
	}

	public static int deleteCachedData(ContentResolver cr) {
		return cr.delete(BjnoteContent.Shops.CONTENT_URI, null, null);
	}
	
	public static long isExsited(ContentResolver cr, String aid, String bid) {
		long id = -1;
		Cursor c = cr.query(BjnoteContent.Shops.CONTENT_URI, ShopInfoObject.SHOP_PROJECTION, null, null, null);
		if (c != null) {
			if (c.moveToNext()) {
				id = c.getLong(0);
			}
			c.close();
		}
		return id;
	}
	
	public static int getShopsDataCount(ContentResolver cr) {
		int length = 0;
		Cursor c = cr.query(BjnoteContent.Shops.CONTENT_URI, ShopInfoObject.SHOP_PROJECTION, null, null, null);
		if (c != null) {
			length = c.getCount();
			c.close();
		}
		return length;
	}
	
	public static int getCaixiDataCount(ContentResolver cr) {
		int length = 0;
		Cursor c = cr.query(BjnoteContent.Caixi.CONTENT_URI, new String[] {DBHelper.CAIXI_NAME}, null, null, null);
		if (c != null) {
			length = c.getCount();
			c.close();
		}
		return length;
	}
	
	public static int getShangquanDataCount(ContentResolver cr) {
		int length = 0;
		Cursor c = cr.query(BjnoteContent.Shangquan.CONTENT_URI, new String[] {DBHelper.SHANGQUAN_NAME}, null, null, null);
		if (c != null) {
			length = c.getCount();
			c.close();
		}
		return length;
	}
	
	public static ArrayList<String> getPinpaiList(JSONArray pinpais) throws JSONException {
		ArrayList<String> result = new ArrayList<String>();
		if(pinpais == null) return result;
		//{"shiduan_id":"1","desk_type":"2人桌(1-2人)","desk_name":"A2","date":"2014/7/18 0:00:00","desk_status":"2","shiduan_time":"12:15","shiduan_name":"午市","DeskID":"1"}
		for(int i = 0; i < pinpais.length(); i++) {
			JSONObject obj = pinpais.getJSONObject(i);
			result.add(obj.getString("HeadName").trim());
		}
		return result;
	}
	
	public static ArrayList<String> getXingzhengquList(JSONArray pinpais) throws JSONException {
		ArrayList<String> result = new ArrayList<String>();
		if(pinpais == null) return result;
		for(int i = 0; i < pinpais.length(); i++) {
			JSONObject obj = pinpais.getJSONObject(i);
			String str = obj.getString("City").trim();
			if(!result.contains(str)) result.add(str);
		}
		return result;
	}
	
	public static ArrayList<String> getCaixiList(JSONArray caixi) throws JSONException {
		ArrayList<String> result = new ArrayList<String>();
		if(caixi == null) return result;
		for(int i = 0; i < caixi.length(); i++) {
			String name = caixi.getJSONObject(i).getString(DBHelper.CAIXI_NAME).trim();
			result.add(name);
		}
		
		return result;
	}
	
	public static ArrayList<String> getShangquanList(JSONArray shangquan) throws JSONException {
		ArrayList<String> result = new ArrayList<String>();
		if(shangquan == null) return result;
		for(int i = 0; i < shangquan.length(); i++) {
			String name = shangquan.getString(i).trim();
			result.add(name);
		}
		
		return result;
	}
}
