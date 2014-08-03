package com.cncom.app.base.database;

import android.content.ContentResolver;
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
    public static class ScanHistory extends BjnoteContent{
    	public static final Uri CONTENT_URI = Uri.withAppendedPath(BjnoteContent.CONTENT_URI, "scan_history");
    }
    
    /**���ø����CONTENT_URI���ر��豸���ݿ�*/
    public static class CloseDeviceDatabase extends BjnoteContent{
    	private static final Uri CONTENT_URI = Uri.withAppendedPath(BjnoteContent.DEVICE_CONTENT_URI, "closedevice");
    	/**���ø÷������ر��豸���ݿ�*/
    	public static void closeDeviceDatabase(ContentResolver cr) {
    		cr.query(CONTENT_URI, null, null, null, null);
    	}
    }
}
