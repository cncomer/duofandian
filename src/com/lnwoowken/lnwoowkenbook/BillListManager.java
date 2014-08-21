package com.lnwoowken.lnwoowkenbook;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.database.Cursor;

import com.cncom.app.base.database.BjnoteContent;
import com.lnwoowken.lnwoowkenbook.model.BillObject;

public class BillListManager {
	public static List<BillObject> getBillListLocal(ContentResolver cr) {
		List<BillObject> billList = new ArrayList<BillObject>();

		Cursor c = cr.query(BjnoteContent.Bills.CONTENT_URI, BillObject.BILL_PROJECTION, null, null, null);
		if (c != null) {
			while (c.moveToNext()) {
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
				billObj.setTableStyle(c.getString(c.getColumnIndex(BillObject.BILL_TABLESTYLE)));
				billObj.setCreateTime(c.getString(c.getColumnIndex(BillObject.BILL_CREATETIME)));
				billObj.setBillNumber(c.getString(c.getColumnIndex(BillObject.BILL_NUMBER)));
				
				billList.add(billObj);
			}
		}
		return billList;
	}
	
	public static void saveBill(BillObject billObject, ContentResolver cr) {
		if (isExsited(cr, billObject.getBillNumber()) > 0) {
			billObject.updateDatabase(cr);
		} else {
			billObject.saveDatabase(cr, null);
		}
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
}