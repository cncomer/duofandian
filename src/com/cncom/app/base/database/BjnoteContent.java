package com.cncom.app.base.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.lnwoowken.lnwoowkenbook.MyApplication;

public class BjnoteContent {

	public static final String AUTHORITY = MyApplication.PKG_NAME + ".provider.BjnoteProvider";
    // The notifier authority is used to send notifications regarding changes to messages (insert,
    // delete, or update) and is intended as an optimization for use by clients of message list
    // cursors (initially, the email AppWidget).
    public static final String NOTIFIER_AUTHORITY =  MyApplication.PKG_NAME + ".notify.BjnoteProvider";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    
    public static final String DEVICE_AUTHORITY = MyApplication.PKG_NAME + ".provider.DeviceProvider";
    public static final String DEVICE_NOTIFIER_AUTHORITY = MyApplication.PKG_NAME + ".notify.DeviceProvider";
    public static final Uri DEVICE_CONTENT_URI = Uri.parse("content://" + DEVICE_AUTHORITY);
    
    // All classes share this
    public static final String RECORD_ID = "_id";

    public static final String[] COUNT_COLUMNS = new String[]{"count(*)"};

    /**
     * This projection can be used with any of the EmailContent classes, when all you need
     * is a list of id's.  Use ID_PROJECTION_COLUMN to access the row data.
     */
    public static final String[] ID_PROJECTION = new String[] {
        RECORD_ID
    };
    public static final int ID_PROJECTION_COLUMN = 0;

    public static final String ID_SELECTION = RECORD_ID + " =?";
    
    
    public static class Accounts extends BjnoteContent{
    	public static final Uri CONTENT_URI = Uri.withAppendedPath(BjnoteContent.CONTENT_URI, "accounts");
    }
    public static class Shops extends BjnoteContent{
    	public static final Uri CONTENT_URI = Uri.withAppendedPath(BjnoteContent.CONTENT_URI, "shops");
    }
    public static class Caixi extends BjnoteContent{
    	public static final Uri CONTENT_URI = Uri.withAppendedPath(BjnoteContent.CONTENT_URI, "caixi");
    }
    public static class Shangquan extends BjnoteContent{
    	public static final Uri CONTENT_URI = Uri.withAppendedPath(BjnoteContent.CONTENT_URI, "shangquan");
    }
    public static class Bills extends BjnoteContent{
    	public static final Uri CONTENT_URI = Uri.withAppendedPath(BjnoteContent.CONTENT_URI, "bills");
    	/**账户管理里的订单查询*/
    	public static final Uri ACCOUNT_CONTENT_URI = Uri.withAppendedPath(BjnoteContent.CONTENT_URI, "account_bills");
    	
    }
    public static class ScanHistory extends BjnoteContent{
    	public static final Uri CONTENT_URI = Uri.withAppendedPath(BjnoteContent.CONTENT_URI, "scan_history");
    }
    
    /**调用该类的CONTENT_URI来关闭设备数据库*/
    public static class CloseDeviceDatabase extends BjnoteContent{
    	private static final Uri CONTENT_URI = Uri.withAppendedPath(BjnoteContent.DEVICE_CONTENT_URI, "closedevice");
    	/**调用该方法来关闭设备数据库*/
    	public static void closeDeviceDatabase(ContentResolver cr) {
    		cr.query(CONTENT_URI, null, null, null, null);
    	}
    }
    
    public static class YMESSAGE extends BjnoteContent{
    	public static final Uri CONTENT_URI = Uri.withAppendedPath(BjnoteContent.CONTENT_URI, "ymessage");
    	
    	public static String[] PROJECTION = new String[]{
    		DBHelper.ID,
    		DBHelper.YOUMENG_MESSAGE_ID,
    		DBHelper.YOUMENG_TITLE,
    		DBHelper.YOUMENG_TEXT,
    		DBHelper.YOUMENG_MESSAGE_ACTIVITY,
    		DBHelper.YOUMENG_MESSAGE_URL,
    		DBHelper.YOUMENG_MESSAGE_CUSTOM,
    		DBHelper.YOUMENG_MESSAGE_RAW, 
    		DBHelper.DATE,
    	};
    	
    	public static final int INDEX_ID = 0;
    	public static final int INDEX_MESSAGE_ID = 1;
    	public static final int INDEX_TITLE = 2;
    	public static final int INDEX_TEXT = 3;
    	public static final int INDEX_MESSAGE_ACTIVITY = 4;
    	public static final int INDEX_MESSAGE_URL = 5;
    	public static final int INDEX_MESSAGE_CUSTOM = 6;
    	public static final int INDEX_MESSAGE_RAW = 7;
    	public static final int INDEX_DATE = 8;
    	
    	public static final String WHERE_YMESSAGE_ID = DBHelper.YOUMENG_MESSAGE_ID + "=?";
    }
    
    public static long existed(ContentResolver cr, Uri uri, String where, String[] selectionArgs) {
    	long id = -1;
		Cursor c = cr.query(uri, ID_PROJECTION, where, selectionArgs, null);
		if (c != null) {
			if (c.moveToNext()) {
				id = c.getLong(0);
			}
			c.close();
		}
		return id;
	}
	
	public static int update(ContentResolver cr, Uri uri, ContentValues values, String where, String[] selectionArgs) {
		return cr.update(uri, values, where, selectionArgs);
	}
	
	public static Uri insert(ContentResolver cr, Uri uri, ContentValues values) {
		return cr.insert(uri, values);
	}
	
	public static int delete(ContentResolver cr, Uri uri,  String where, String[] selectionArgs) {
		return cr.delete(uri, where, selectionArgs);
	}
}
