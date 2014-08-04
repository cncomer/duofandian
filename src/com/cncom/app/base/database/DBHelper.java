package com.cncom.app.base.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shwy.bestjoy.utils.DebugUtils;

/**
 * @author Sean Owen
 * @author chenkai
 */
public final class DBHelper extends SQLiteOpenHelper {
private static final String TAG = "DBHelper";
  private static final int DB_VERSION = 1;
  private static final String DB_NAME = "cncom.db";
  public static final String ID = "_id";
  /**0为可见，1为删除，通常用来标记一条数据应该被删除，是不可见的，包含该字段的表查询需要增加deleted=0的条件*/
  public static final String FLAG_DELETED = "deleted";
  public static final String DATE = "date";
  //account table
  public static final String TABLE_NAME_ACCOUNTS = "accounts";
  /**用户唯一识别码*/
  public static final String ACCOUNT_UID = "uid";
  public static final String ACCOUNT_DEFAULT = "isDefault";
  public static final String ACCOUNT_TEL = "tel";
  public static final String ACCOUNT_NAME = "name";
  public static final String ACCOUNT_EMAIL = "email";
  public static final String ACCOUNT_PWD = "password";
  public static final String ACCOUNT_USER = "user";
  public static final String ACCOUNT_JIFEN = "jifen";
  public static final String ACCOUNT_TOTAL = "total";
  public static final String ACCOUNT_YUQITIME = "yuqitimes";
  public static final String ACCOUNT_PINJIA = "pinjia";
  public static final String ACCOUNT_LEVEL = "level";

  public static final String ACCOUNT_HAS_PHOTO = "hasPhoto";
  
  // Qrcode scan part begin
  public static final String TABLE_SCAN_NAME = "history";
  public static final String ID_COL = "id";
  public static final String TEXT_COL = "text";
  public static final String FORMAT_COL = "format";
  public static final String DISPLAY_COL = "display";
  public static final String TIMESTAMP_COL = "timestamp";
  public static final String DETAILS_COL = "details";
  // Qrcode scan part end
  
  
  public DBHelper(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
  }
  
  private SQLiteDatabase mWritableDatabase;
  private SQLiteDatabase mReadableDatabase;
  
  public synchronized SQLiteDatabase openWritableDatabase() {
	  if (mWritableDatabase == null) {
		  mWritableDatabase = getWritableDatabase();
	  }
	  return mWritableDatabase;
  }
  
  public synchronized SQLiteDatabase openReadableDatabase() {
	  if (mReadableDatabase == null) {
		  mReadableDatabase = getReadableDatabase();
	  }
	  return mReadableDatabase;
  }
  
  public synchronized void closeReadableDatabase() {
	  if (mReadableDatabase != null && mReadableDatabase.isOpen()) {
		  mReadableDatabase.close();
		  mReadableDatabase = null;
	  }
  }
  
  public synchronized void closeWritableDatabase() {
	  if (mWritableDatabase != null && mWritableDatabase.isOpen()) {
		  mWritableDatabase.close();
		  mWritableDatabase = null;
	  }
  }
  
  public synchronized void closeDatabase() {
	  closeReadableDatabase();
	  closeWritableDatabase();
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
      DebugUtils.logD(TAG, "onCreate");
   
       // Create Account table
  	   createAccountTable(sqLiteDatabase);
  	   // Create scan history
 		//createScanHistory(sqLiteDatabase);
  		
  }
  
  private void createAccountTable(SQLiteDatabase sqLiteDatabase) {
	  sqLiteDatabase.execSQL(
	            "CREATE TABLE " + TABLE_NAME_ACCOUNTS + " (" +
	            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
	            ACCOUNT_UID + " INTEGER NOT NULL DEFAULT 0, " +
	            ACCOUNT_TEL + " TEXT, " +
	            ACCOUNT_PWD + " TEXT, " +
	            ACCOUNT_DEFAULT + " INTEGER NOT NULL DEFAULT 1, " +
	            ACCOUNT_EMAIL + " TEXT, " +
	            ACCOUNT_USER + " TEXT, " +
	            ACCOUNT_NAME + " TEXT, " +
	            ACCOUNT_JIFEN + " TEXT, " +
	            ACCOUNT_TOTAL + " TEXT, " +
	            ACCOUNT_YUQITIME + " TEXT, " +
	            ACCOUNT_PINJIA + " TEXT, " +
	            ACCOUNT_LEVEL + " TEXT, " +
	            DATE + " TEXT" +
	            ");");
  }
  
  private void createScanHistory(SQLiteDatabase sqLiteDatabase) {
	  sqLiteDatabase.execSQL(
	            "CREATE TABLE " + TABLE_SCAN_NAME + " (" +
	            ID_COL + " INTEGER PRIMARY KEY, " +
	            TEXT_COL + " TEXT, " +
	            FORMAT_COL + " TEXT, " +
	            DISPLAY_COL + " TEXT, " +
	            TIMESTAMP_COL + " INTEGER, " +
	            DETAILS_COL + " TEXT);");
  }
  
  private void addTextColumn(SQLiteDatabase sqLiteDatabase, String table, String column) {
	    String alterForTitleSql = "ALTER TABLE " + table +" ADD " + column + " TEXT";
		sqLiteDatabase.execSQL(alterForTitleSql);
  }
  private void addIntColumn(SQLiteDatabase sqLiteDatabase, String table, String column, int defaultValue) {
	    String alterForTitleSql = "ALTER TABLE " + table +" ADD " + column + " INTEGER NOT NULL DEFAULT " + defaultValue;
		sqLiteDatabase.execSQL(alterForTitleSql);
}

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
	  DebugUtils.logD(TAG, "onUpgrade oldVersion " + oldVersion + " newVersion " + newVersion);
	 //XXX maybe do something.
  }
}
