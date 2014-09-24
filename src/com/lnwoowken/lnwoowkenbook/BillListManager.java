package com.lnwoowken.lnwoowkenbook;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.database.Cursor;

import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.database.BjnoteContent;
import com.lnwoowken.lnwoowkenbook.model.BillObject;

public class BillListManager {
	public static List<BillObject> getBillListLocal(ContentResolver cr) {
		List<BillObject> billList = new ArrayList<BillObject>();

		Cursor c = cr.query(BjnoteContent.Bills.CONTENT_URI, BillObject.BILL_PROJECTION, null, null, BillObject.BILL_SORT);
		if (c != null) {
			while (c.moveToNext()) {
				billList.add(getBillObject(c));
			}
		}
		return billList;
	}
	
	public static List<BillObject> getBillList(ContentResolver cr, JSONArray mJsonArrayData) throws JSONException{
		List<BillObject> result = new ArrayList<BillObject>();
		if(mJsonArrayData == null) return result;
		deleteCachedData(cr);
		for(int i = 0; i < mJsonArrayData.length(); i++) {
			BillObject billObject = getBillFromJsonObject(mJsonArrayData.getJSONObject(i));
			result.add(billObject);
			
			saveBill(billObject, cr);
		}
		return result;
	}
	
	public static List<BillObject> getUnpayBillListLocal(ContentResolver cr) {
		List<BillObject> billList = new ArrayList<BillObject>();

		Cursor c = cr.query(BjnoteContent.Bills.CONTENT_URI, BillObject.BILL_PROJECTION, BillObject.BILL_UNPAY_SELECTION, new String[] {String.valueOf(BillObject.STATE_SUCCESS)}, BillObject.BILL_SORT);
		if (c != null) {
			while (c.moveToNext()) {
				billList.add(getBillObject(c));
			}
		}
		return billList;
	}
	
	public static Cursor getLocalUnpayBillCursor(ContentResolver cr) {
		return cr.query(BjnoteContent.Bills.CONTENT_URI, BillObject.BILL_PROJECTION, BillObject.BILL_UNPAY_SELECTION, new String[] {String.valueOf(BillObject.STATE_SUCCESS)}, BillObject.BILL_SORT);
	}
	
	public static Cursor getLocalAllBillCursor(ContentResolver cr) {
		return cr.query(BjnoteContent.Bills.CONTENT_URI, BillObject.BILL_PROJECTION, null, null, BillObject.BILL_SORT);
	}
	
	public static BillObject getBillObjectFromCursor(Cursor c) {
		return getBillObject(c);
	}
	
	public static BillObject getBillObjectByBillNumber(ContentResolver cr, String billNumber) {
		BillObject billObject = null;
		Cursor c = cr.query(BjnoteContent.Bills.CONTENT_URI, BillObject.BILL_PROJECTION, BillObject.BILL_NUMBER_SELECTION, new String[] {billNumber}, null);
		if (c != null) {
			if (c.moveToNext()) {
				billObject =  getBillObject(c);
			}
			c.close();
		}
		return billObject;
	}
	
	public static void updateBillStateByBillNumber(ContentResolver cr, String billNumber, int state) {
		BillObject billObj = getBillObjectByBillNumber(cr, billNumber);
		billObj.setState(state);
		
		saveBill(billObj, cr);
	}
	
	public static BillObject getBillObject(Cursor c) {
		BillObject billObj = new BillObject();
		billObj.setUid(c.getString(c.getColumnIndex(BillObject.BILL_UID)));
		billObj.setSid(c.getString(c.getColumnIndex(BillObject.BILL_SID)));
		billObj.setTid(c.getString(c.getColumnIndex(BillObject.BILL_TID)));
		billObj.setPeopleNum(c.getString(c.getColumnIndex(BillObject.BILL_PEOPLENUM)));
		billObj.setRcode(c.getString(c.getColumnIndex(BillObject.BILL_RCODE)));
		billObj.setMac(c.getString(c.getColumnIndex(BillObject.BILL_MAC)));
		billObj.setIp(c.getString(c.getColumnIndex(BillObject.BILL_IP)));
		billObj.setPhone(c.getString(c.getColumnIndex(BillObject.BILL_PHONE)));
		billObj.setVersion(c.getString(c.getColumnIndex(BillObject.BILL_VERSION)));
		billObj.setShopName(c.getString(c.getColumnIndex(BillObject.BILL_SHOPNAME)));
		billObj.setDate(c.getString(c.getColumnIndex(BillObject.BILL_DATE)));
		billObj.setTime(c.getString(c.getColumnIndex(BillObject.BILL_TIME)));
		billObj.setState(c.getInt(c.getColumnIndex(BillObject.BILL_STATE)));
		billObj.setTableName(c.getString(c.getColumnIndex(BillObject.BILL_TABLENAME)));
		billObj.setTableType(c.getString(c.getColumnIndex(BillObject.BILL_TABLESTYLE)));
		billObj.setCreateTime(c.getString(c.getColumnIndex(BillObject.BILL_CREATETIME)));
		billObj.setBillNumber(c.getString(c.getColumnIndex(BillObject.BILL_NUMBER)));
		billObj.setDabiaoPrice(c.getString(c.getColumnIndex(BillObject.BILL_DABIAO_PRICE)));
		billObj.setServicePrice(c.getString(c.getColumnIndex(BillObject.BILL_SERVICE_PRICE)));
		
		billObj.setVisited(c.getInt(c.getColumnIndex(BillObject.BILL_VISITED)));
		
		return billObj;
	}
	
	public static BillObject getBillFromJsonObject(JSONObject obj) throws JSONException {
		BillObject billObj = new BillObject();
		//billObj.setUid(obj.getString(BillObject.BILL_UID));
		//billObj.setSid(obj.getString(BillObject.BILL_SID));
		//billObj.setTid(obj.getString(BillObject.BILL_TID));
		//billObj.setPeopleNum(obj.getString(BillObject.BILL_PEOPLENUM));
		//billObj.setRcode(obj.getString(BillObject.BILL_RCODE));
		//billObj.setMac(obj.getString(BillObject.BILL_MAC));
		//billObj.setIp(obj.getString(BillObject.BILL_IP));
		//billObj.setPhone(obj.getString(BillObject.BILL_PHONE));
		//billObj.setVersion(obj.getString(BillObject.BILL_VERSION));
		billObj.setSid(obj.getString("ShopID"));
		billObj.setUid(MyAccountManager.getInstance().getCurrentAccountUid());
		
		billObj.setShopName(obj.getString(BillObject.BILL_SHOPNAME));
		billObj.setDate(obj.getString(BillObject.BILL_DATE));
		billObj.setTime(obj.getString(BillObject.BILL_TIME));
		billObj.setState(obj.getInt(BillObject.BILL_STATE));
		billObj.setTableName(obj.getString(BillObject.BILL_TABLENAME));
		billObj.setTableType(obj.getString(BillObject.BILL_TABLESTYLE));
		billObj.setCreateTime(obj.getString(BillObject.BILL_CREATETIME));
		billObj.setBillNumber(obj.getString(BillObject.BILL_NUMBER));
		//billObj.setDabiaoPrice(obj.getString(BillObject.BILL_DABIAO_PRICE));
		billObj.setServicePrice(obj.getString(BillObject.BILL_SERVICE_PRICE));
		billObj.setServicePrice(obj.getString(BillObject.BILL_DINGJIN_PRICE));
		
		billObj.setVisited(Integer.valueOf(obj.optString(BillObject.BILL_VISITED, "0")));
		
		return billObj;
	}
	
	public static void saveBill(BillObject billObject, ContentResolver cr) {
		if (isExsited(cr, billObject.getBillNumber()) > 0) {
			billObject.updateDatabase(cr);
		} else {
			billObject.saveDatabase(cr, null);
		}
	}

	public static boolean deleteBillByNumber(ContentResolver cr, String billNumber) {
		return cr.delete(BjnoteContent.Bills.CONTENT_URI, BillObject.BILL_SELECTION, new String[] {billNumber}) > 0;
	}

	public static int deleteCachedData(ContentResolver cr) {
		return cr.delete(BjnoteContent.Bills.CONTENT_URI, null, null);
	}

	public static long isExsited(ContentResolver cr, String bid) {
		long id = -1;
		Cursor c = cr.query(BjnoteContent.Bills.CONTENT_URI, BillObject.BILL_PROJECTION, BillObject.BILL_SELECTION, new String[] {bid}, null);
		if (c != null) {
			if (c.moveToNext()) {
				id = c.getLong(0);
			}
			c.close();
		}
		return id;
	}

	public static boolean isExsited(ContentResolver cr) {
		Cursor c = cr.query(BjnoteContent.Bills.CONTENT_URI, BillObject.BILL_PROJECTION, null, null, null);
		if (c != null) {
			if (c.moveToNext()) {
				return true;
			}
			c.close();
		}
		return false;
	}
	
	public static boolean isShowNew(){
		return MyApplication.getInstance().mPreferManager.getBoolean("bill_show_new", false);
	}
	
	public static boolean setShowNew(boolean show) {
		return MyApplication.getInstance().mPreferManager.edit().putBoolean("bill_show_new", show).commit();
	}
	
	public static boolean shouldRefreshBillFromServer() {
		return MyApplication.getInstance().mPreferManager.getBoolean("bill_refresh", false);
	}
	
	public static boolean setShouldRefreshBillFromServer(boolean refresh) {
		return MyApplication.getInstance().mPreferManager.edit().putBoolean("bill_refresh", refresh).commit();
	}
}
