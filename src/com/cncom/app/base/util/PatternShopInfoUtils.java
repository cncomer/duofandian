package com.cncom.app.base.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.database.Cursor;

import com.cncom.app.base.database.BjnoteContent;
import com.cncom.app.base.database.DBHelper;

public class PatternShopInfoUtils {
	private static final String SELECTION_BY_SHOPID = DBHelper.SHOP_ID + "=?";
	
	public static List<ShopInfoObject> getShopInfo(JSONArray shops, ContentResolver cr) throws JSONException {
		List<ShopInfoObject> result = new ArrayList<ShopInfoObject>();
		if(shops == null) return result;
		//{"ShopName":"望湘园（测试1）","deskCount":"0","showid":"010005","HeadID":null,"HeadName":"望湘园","ShopID":"12"}
		//{"Address":"上海市金山区长宁路1018号龙之梦购物中心5楼蓝中庭","Serverprice":"","HeadID":1,"ShopID":11,"ShopName":"望湘园（测试）","CaiXi":"1","ShuYuZhuZhi":"1","GuDingPhone":null,"Contacts":"柳智","contacts_phone":"18621951099","ShopAddrID":null,"shop_brief":"望湘园换短发","qiang_wei_img":"","shop_img":"","ren_jun":"0","pingfen":null,"youhui":"0","tuangou":"0","diancan":"0","maidan":"0","showid":"010004","detail":null}
		for(int i = 0; i < shops.length(); i++) {
			ShopInfoObject shopInfoObject = new ShopInfoObject();
			JSONObject obj = shops.getJSONObject(i);
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
			shopInfoObject.setShopDetail(obj.getString(ShopInfoObject.SHOP_DETAIL));
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
	
	public static ShopInfoObject getShopInfoLocalById(ContentResolver cr, String shopId){
		ShopInfoObject shopInfoObject = new ShopInfoObject();
		Cursor c = cr.query(BjnoteContent.Shops.CONTENT_URI, ShopInfoObject.SHOP_PROJECTION, SELECTION_BY_SHOPID, new String[] {shopId}, null);
		if (c != null) {
			if (c.moveToNext()) {
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
			}
			c.close();
		}
		return shopInfoObject;
	}
	
	public static List<TableInfoObject> getShopAvailableTableList(JSONArray shops) throws JSONException  {
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
			
			result.add(shopAvailableTableObject);
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
