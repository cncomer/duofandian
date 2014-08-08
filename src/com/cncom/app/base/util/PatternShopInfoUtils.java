package com.cncom.app.base.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.database.Cursor;

import com.cncom.app.base.database.BjnoteContent;

public class PatternShopInfoUtils {

	public static List<ShopInfoObject> getShopInfo(JSONArray shops, ContentResolver cr) throws JSONException {
		List<ShopInfoObject> result = new ArrayList<ShopInfoObject>();
		if(shops == null) return result;
		//{"ShopName":"望湘园（测试1）","deskCount":"0","showid":"010005","HeadID":null,"HeadName":"望湘园","ShopID":"12"}
		for(int i = 0; i < shops.length(); i++) {
			ShopInfoObject shopInfoObject = new ShopInfoObject();
			JSONObject obj = shops.getJSONObject(i);
//			shopInfoObject.setShopAddress(obj.getString(ShopInfoObject.SHOP_ADDREES));
//			shopInfoObject.setShopServerprice(obj.getString(ShopInfoObject.SHOP_SERVERPRICE));
			shopInfoObject.setShopHeadId(obj.getString(ShopInfoObject.SHOP_HEAD_ID));
			shopInfoObject.setShopID(obj.getString(ShopInfoObject.SHOP_ID));
			shopInfoObject.setShopName(obj.getString(ShopInfoObject.SHOP_NAME));
//			shopInfoObject.setShopCaiXi(obj.getString(ShopInfoObject.SHOP_CAIXI));
//			shopInfoObject.setShopShuYuZhuZhi(obj.getString(ShopInfoObject.SHOP_SHUYUZHUZHI));
//			shopInfoObject.setShopGuDingPhone(obj.getString(ShopInfoObject.SHOP_GUDINGPHONE));
//			shopInfoObject.setShopContacts(obj.getString(ShopInfoObject.SHOP_CONTACTS));
//			shopInfoObject.setShopContactsPhone(obj.getString(ShopInfoObject.SHOP_CONTACTS_PHONE));
//			shopInfoObject.setShopAddrID(obj.getString(ShopInfoObject.SHOP_ADDR_ID));
//			shopInfoObject.setShopBrief(obj.getString(ShopInfoObject.SHOP_BRIEF));
//			shopInfoObject.setShopQiangWeiImg(obj.getString(ShopInfoObject.SHOP_QIANG_WEI_IMG));
//			shopInfoObject.setShopImg(obj.getString(ShopInfoObject.SHOP_IMG));
//			shopInfoObject.setShopRenJun(obj.getString(ShopInfoObject.SHOP_RENJUN));
//			shopInfoObject.setShopPingFen(obj.getString(ShopInfoObject.SHOP_PINGFEN));
//			shopInfoObject.setShopYouHui(obj.getString(ShopInfoObject.SHOP_YOUHUI));
//			shopInfoObject.setShopTuanGou(obj.getString(ShopInfoObject.SHOP_TUANGOU));
//			shopInfoObject.setShopDianCan(obj.getString(ShopInfoObject.SHOP_DIANCAN));
//			shopInfoObject.setShopMaiDian(obj.getString(ShopInfoObject.SHOP_MAIDIAN));
//			shopInfoObject.setShopShowId(obj.getString(ShopInfoObject.SHOP_SHOW_ID));
//			shopInfoObject.setShopDetail(obj.getString(ShopInfoObject.SHOP_DETAIL));
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
				shopInfoObject.setShopDetail(c.getString(c.getColumnIndex(ShopInfoObject.SHOP_DETAIL)));
				
				result.add(shopInfoObject);
			}
			c.close();
		}
		return result;
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
	
	public static int getDataCount(ContentResolver cr) {
		int length = 0;
		Cursor c = cr.query(BjnoteContent.Shops.CONTENT_URI, ShopInfoObject.SHOP_PROJECTION, null, null, null);
		if (c != null) {
			length = c.getCount();
			c.close();
		}
		return length;
	}
}
