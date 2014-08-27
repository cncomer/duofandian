package com.cncom.app.base.database;


import java.io.FileNotFoundException;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;

import com.lnwoowken.lnwoowkenbook.BillListManager;
import com.shwy.bestjoy.utils.DebugUtils;

public class BjnoteProvider extends ContentProvider{
	private static final String TAG = "BjnoteProvider";
	private SQLiteDatabase mContactDatabase;
	private String[] mTables = new String[]{
			DBHelper.TABLE_NAME_ACCOUNTS,
			DBHelper.TABLE_NAME_SHOPS,
			//DBHelper.TABLE_SCAN_NAME,
			DBHelper.TABLE_NAME_CAIXI,
			DBHelper.TABLE_NAME_SHANGQUAN,
			DBHelper.TABLE_NAME_BILL,
//			ContactsDBHelper.TABLE_NAME_MYLIFE_CONSUME,
	};
	private static final int BASE = 8;
	
	private static final int ACCOUNT = 0x0000;
	private static final int ACCOUNT_ID = 0x0001;
	
	private static final int SHOP = 0x0100;
	private static final int SHOP_ID = 0x0101;
	
	private static final int CAIXI = 0x0200;
	private static final int CAIXI_ID = 0x0201;
	
	private static final int SHANGQUAN = 0x0300;
	private static final int SHANGQUAN_ID = 0x0301;
	
	private static final int BILL = 0x0400;
	private static final int BILL_ID = 0x0401;
	
	private static final int SCAN_HISTORY = 0x0500;
	private static final int SCAN_HISTORY_ID = 0x0501;
	
	
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	 static {
	        // URI matching table
	        UriMatcher matcher = sURIMatcher;
	        matcher.addURI(BjnoteContent.AUTHORITY, "accounts", ACCOUNT);
	        matcher.addURI(BjnoteContent.AUTHORITY, "accounts/#", ACCOUNT_ID);

	        matcher.addURI(BjnoteContent.AUTHORITY, "shops", SHOP);
	        matcher.addURI(BjnoteContent.AUTHORITY, "shops/#", SHOP_ID);

	        matcher.addURI(BjnoteContent.AUTHORITY, "caixi", CAIXI);
	        matcher.addURI(BjnoteContent.AUTHORITY, "caixi/#", CAIXI_ID);

	        matcher.addURI(BjnoteContent.AUTHORITY, "shangquan", SHANGQUAN);
	        matcher.addURI(BjnoteContent.AUTHORITY, "shangquan/#", SHANGQUAN_ID);

	        matcher.addURI(BjnoteContent.AUTHORITY, "bills", BILL);
	        matcher.addURI(BjnoteContent.AUTHORITY, "bills/#", BILL_ID);
	        
	        matcher.addURI(BjnoteContent.AUTHORITY, "scan_history", SCAN_HISTORY);
	        matcher.addURI(BjnoteContent.AUTHORITY, "scan_history/#", SCAN_HISTORY_ID);
	        
	        //TODO 增加
	 }
	
	synchronized SQLiteDatabase getDatabase(Context context) {
        // Always return the cached database, if we've got one
        if (mContactDatabase != null) {
            return mContactDatabase;
        }


        DBHelper helper = new DBHelper(context);
        mContactDatabase = helper.getWritableDatabase();
        mContactDatabase.setLockingEnabled(true);
        return mContactDatabase;
	}
	
	@Override
	public boolean onCreate() {
		return false;
	}
	
	/**
     * Wrap the UriMatcher call so we can throw a runtime exception if an unknown Uri is passed in
     * @param uri the Uri to match
     * @return the match value
     */
    private static int findMatch(Uri uri, String methodName) {
        int match = sURIMatcher.match(uri);
        if (match < 0) {
            throw new IllegalArgumentException("Unknown uri: " + uri);
        } 
        DebugUtils.logD(TAG, methodName + ": uri=" + uri + ", match is " + match);
        return match;
    }
    
    private void notifyChange(int match) {
    	Context context = getContext();
    	Uri notify = BjnoteContent.CONTENT_URI;
    	switch(match) {
    	case ACCOUNT:
    	case ACCOUNT_ID:
    		notify = BjnoteContent.Accounts.CONTENT_URI;
    		break;
    	case SHOP:
    	case SHOP_ID:
    		notify = BjnoteContent.Shops.CONTENT_URI;
    		break;
    	case CAIXI:
    	case CAIXI_ID:
    		notify = BjnoteContent.Caixi.CONTENT_URI;
    		break;
    	case SHANGQUAN:
    	case SHANGQUAN_ID:
    		notify = BjnoteContent.Shangquan.CONTENT_URI;
    		break;
    	case BILL:
    	case BILL_ID:
    		notify = BjnoteContent.Bills.CONTENT_URI;
    		break;
		case SCAN_HISTORY:
		case SCAN_HISTORY_ID:
			notify = BjnoteContent.ScanHistory.CONTENT_URI;
			break;
    	}
    	ContentResolver resolver = context.getContentResolver();
        resolver.notifyChange(notify, null);
    }

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int match = findMatch(uri, "delete");
        Context context = getContext();

        // See the comment at delete(), above
        SQLiteDatabase db = getDatabase(context);
        String table = mTables[match>>BASE];
        DebugUtils.logProvider(TAG, "delete data from table " + table);
        int count = 0;
        switch(match) {
	        case ACCOUNT:
	    	case ACCOUNT_ID:
	    	case SHOP:
	    	case SHOP_ID:
	    	case CAIXI:
	    	case CAIXI_ID:
	    	case SHANGQUAN:
	    	case SHANGQUAN_ID:
	    	case BILL:
	    	case BILL_ID:
			case SCAN_HISTORY:
			case SCAN_HISTORY_ID:
        	count = db.delete(table, buildSelection(match, uri, selection), selectionArgs);
        }
        if (count >0) {
//        	switch(match) {
//    		 case BILL:
//    		 case BILL_ID:
//    			BillListManager.setShowNew(true);
//    			 break;
//    		 }
        	notifyChange(match);
        }
		return count;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		 int match = findMatch(uri, "insert");
         Context context = getContext();

         // See the comment at delete(), above
         SQLiteDatabase db = getDatabase(context);
         String table = mTables[match>>BASE];
         DebugUtils.logProvider(TAG, "insert values into table " + table);
//         switch(match) {
//	         case MY_CARD:
//	         case MY_CARD_ID:
//	         case RECEIVED_CONTACT:
//	         case RECEIVED_CONTACT_ID:
//	         case EXCHANGE_TOPIC:
//	     	 case EXCHANGE_TOPIC_ID:
//	     	 case EXCHANGE_TOPIC_LIST:
//	     	 case EXCHANGE_TOPIC_LIST_ID:
//	     	 case CIRCLE_TOPIC:
//			 case CIRCLE_TOPIC_ID:
//			 case CIRCLE_TOPIC_LIST:
//			 case CIRCLE_TOPIC_LIST_ID:
//			 case CIRCLE_MEMBER_DETAIL:
//			 case CIRCLE_MEMBER_DETAIL_ID:
//			 case ACCOUNT:
//			 case ACCOUNT_ID:
//			 case FEEDBACK:
//			 case FEEDBACK_ID:
//			 case QUANPHOTO:
//			 case QUANPHOTO_ID:
//			 case ZHT:
//			 case ZHT_ID:
//	     		break;
//         }
         //Insert 操作不允许设置_id字段，如果有的话，我们需要移�?
         if (values.containsKey(DBHelper.ID)) {
      		values.remove(DBHelper.ID);
      	 }
     	 long id = db.insert(table, null, values);
     	 if (id > 0) {
     		 switch(match) {
     		 case BILL:
     		 case BILL_ID:
     			BillListManager.setShowNew(true);
     			 break;
     		 }
     		notifyChange(match);
   		    return ContentUris.withAppendedId(uri, id);
     	 }
		 return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		 int match = findMatch(uri, "query");
         Context context = getContext();
         // See the comment at delete(), above
         SQLiteDatabase db = getDatabase(context);
         int i =match>>BASE;
         String table = mTables[i];
         DebugUtils.logProvider(TAG, "query table " + table);
         Cursor result = null;
         switch(match) {
			case ACCOUNT:
			case ACCOUNT_ID:
	    	case SHOP:
	    	case SHOP_ID:
	    	case CAIXI:
	    	case CAIXI_ID:
	    	case SHANGQUAN:
	    	case SHANGQUAN_ID:
	    	case BILL:
	    	case BILL_ID:
			case SCAN_HISTORY:
			case SCAN_HISTORY_ID:
        	     result = db.query(table, projection, selection, selectionArgs, null, null, sortOrder);
         }
         if (result != null) result.setNotificationUri(getContext().getContentResolver(), uri);
		return result;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int match = findMatch(uri, "update");
        Context context = getContext();
        
        SQLiteDatabase db = getDatabase(context);
        String table = mTables[match>>BASE];
        DebugUtils.logProvider(TAG, "update data for table " + table);
        int count = 0;
        switch(match) {
	        case ACCOUNT:
	    	case ACCOUNT_ID:
	    	case SHOP:
	    	case SHOP_ID:
	    	case CAIXI:
	    	case CAIXI_ID:
	    	case SHANGQUAN:
	    	case SHANGQUAN_ID:
	    	case BILL:
	    	case BILL_ID:
			case SCAN_HISTORY:
			case SCAN_HISTORY_ID:
        	    count = db.update(table, values, buildSelection(match, uri, selection), selectionArgs);
        }
        if (count >0) {
        	switch(match) {
    		 case BILL:
    		 case BILL_ID:
    			BillListManager.setShowNew(true);
    			 break;
    		 }
        	notifyChange(match);
        }
		return count;
	}
	
	private String buildSelection(int match, Uri uri, String selection) {
		long id = -1;
		switch(match) {
	    	case ACCOUNT_ID:
	    	case SHOP_ID:
	    	case CAIXI_ID:
	    	case SHANGQUAN_ID:
	    	case BILL_ID:
			case SCAN_HISTORY_ID:
			try {
				id = ContentUris.parseId(uri);
			} catch(java.lang.NumberFormatException e) {
				e.printStackTrace();
			}
			break;
		}
		
		if (id == -1) {
			return selection;
		}
		DebugUtils.logProvider(TAG, "find id from Uri#" + id);
		StringBuilder sb = new StringBuilder();
		sb.append(DBHelper.ID);
		sb.append("=").append(id);
		if (!TextUtils.isEmpty(selection)) {
			sb.append(" and ");
			sb.append(selection);
		}
		DebugUtils.logProvider(TAG, "rebuild selection#" + sb.toString());
		return sb.toString();
	}


	@Override
	public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
		int match = findMatch(uri, "openFile");
//		switch(match) {
//			case BILL_AVATOR:
//	        	File file = MyApplication.getInstance().getProductFaPiaoFile(BaoxiuCardObject.objectUseForbill.getFapiaoPhotoId());
//	        	if (BaoxiuCardObject.objectUseForbill.mBillTempFile != null && BaoxiuCardObject.objectUseForbill.mBillTempFile.exists()) {
//			    	return ParcelFileDescriptor.open(BaoxiuCardObject.objectUseForbill.mBillTempFile, ParcelFileDescriptor.MODE_READ_WRITE);
//			    } else if (file.exists()) {
//			        return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_WRITE);
//			    } else {
//			    	DebugUtils.logE(TAG, "openFile can't find file " + file.getAbsolutePath());
//			    }
//				break;
//		}
		throw new FileNotFoundException("no Image found for uri " + uri);
	}
	
}
