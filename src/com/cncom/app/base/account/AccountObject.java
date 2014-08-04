package com.cncom.app.base.account;

import java.util.Date;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.cncom.app.base.database.BjnoteContent;
import com.cncom.app.base.database.DBHelper;
import com.lnwoowken.lnwoowkenbook.MyApplication;
import com.lnwoowken.lnwoowkenbook.R;
import com.shwy.bestjoy.utils.DebugUtils;
import com.shwy.bestjoy.utils.InfoInterface;
/**
 * 账户对象，在程序启动时候会通过{@link MyAccountManager#setContext(Context context)}来获得当前默认账户。
 * 
 * 需要注意的是，在设计数据库的时候，有{@link DBHelper#ACCOUNT_HOME_COUNT}字段，该字段会随着新增或是删除一个HomeObject数据
 * 自动增加和减少，所以我们保存的时候不要设置他。成员mAccountHomes 以及 mBaoxiuCards默认都是空的，如果需要，需要额外调用方法来获得，
 * @author chenkai
 *
 */
public class AccountObject implements InfoInterface{
	private static final String TAG = "HaierAccount";
	
	private static final String[] PROJECTION = new String[]{
		DBHelper.ID,
		DBHelper.ACCOUNT_UID,
		DBHelper.ACCOUNT_NAME,
		DBHelper.ACCOUNT_TEL,
		DBHelper.ACCOUNT_PWD,
		DBHelper.ACCOUNT_EMAIL,
		DBHelper.ACCOUNT_USER,
		DBHelper.ACCOUNT_JIFEN,
		DBHelper.ACCOUNT_TOTAL,
		DBHelper.ACCOUNT_YUQITIME,
		DBHelper.ACCOUNT_PINJIA,
		DBHelper.ACCOUNT_LEVEL,
	};

	private static final String[] PROJECTION_UID = new String[]{
		DBHelper.ID,
		DBHelper.ACCOUNT_UID,
	};
	
	
	private static final int KEY_ID = 0;
	private static final int KEY_MD = 1;
	private static final int KEY_NAME = 2;
	private static final int KEY_TEL = 3;
	private static final int KEY_PWD = 4;
	private static final int KEY_EMAIL = 5;
	private static final int KEY_USER = 6;
	private static final int KEY_JIFEN = 7;
	private static final int KEY_TOTAL = 8;
	private static final int KEY_YUQITIME = 9;
	private static final int KEY_PINJIA = 10;
	private static final int KEY_LEVEL = 11;
	
	private static final String WHERE_DEFAULT = DBHelper.ACCOUNT_DEFAULT + "=1";
	private static final String WHERE_UID = DBHelper.ACCOUNT_UID + "=?";
	
	public long mAccountId = -1;
	public long mAccountUid = -1;
	public String mAccountName;
	public String mAccountTel;
	public String mAccountPwd;
	public String mAccountEmail;
	public String mAccountJifen;
	public String mAccountUser;
	public String mAccountTotal;
	public String mAccountYuqiTimes;
	public String mAccountPinjia;
	public String mAccountLevel;
	

	/**登陆或注册的时候会用到，表示当前的状态，statuscode:状态 1:成功   0：失败*/
	public int mStatusCode;
	/**登陆时候服务器返回的数据*/
	public String mStatusMessage;

	public boolean isLogined() {
		return mStatusCode != 0;
	}
	
	public AccountObject clone() {
		AccountObject newAccountObject = new AccountObject();
		newAccountObject.mAccountId = mAccountId;
		newAccountObject.mAccountUid = mAccountUid;
		newAccountObject.mAccountName = mAccountName;
		newAccountObject.mAccountTel = mAccountTel;
		newAccountObject.mAccountPwd = mAccountPwd;
		newAccountObject.mAccountEmail = mAccountEmail;
		newAccountObject.mAccountJifen = mAccountJifen;
		newAccountObject.mAccountUser = mAccountUser;
		newAccountObject.mAccountTotal = mAccountTotal;
		newAccountObject.mAccountYuqiTimes = mAccountYuqiTimes;
		newAccountObject.mAccountPinjia = mAccountPinjia;
		newAccountObject.mAccountLevel = mAccountLevel;
		return newAccountObject;
	}
	
	public static int deleteAccount(ContentResolver cr, long uid) {
		return cr.delete(BjnoteContent.Accounts.CONTENT_URI, WHERE_UID, new String[]{String.valueOf(uid)});
	}
	
	public static AccountObject getAccountFromDatabase(Context context) {
		return getAccountFromDatabase(context, -1);
	}
	
	public static AccountObject getAccountFromDatabase(Context context, long uid) {
		AccountObject account = null;
		Cursor c = null;
		if (uid == -1) {
			//默认账户
			c = context.getContentResolver().query(BjnoteContent.Accounts.CONTENT_URI, PROJECTION, WHERE_DEFAULT, null, null);
		} else {
			//根据指定的uid查询账户
			c = context.getContentResolver().query(BjnoteContent.Accounts.CONTENT_URI, PROJECTION, WHERE_UID, new String[]{String.valueOf(uid)}, null);
		}
		if (c != null) {
			if (c.moveToNext()) {
				account = new AccountObject();
				String idStr = c.getString(KEY_ID);
				if (TextUtils.isEmpty(idStr)) {
					DebugUtils.logD(TAG, "getHaierAccountFromDatabase accountId is " + idStr);
					return null;
				}
				account.mAccountId = Long.parseLong(idStr);
				if (account.mAccountId <= 0) {
					DebugUtils.logD(TAG, "getHaierAccountFromDatabase accountId is " + account.mAccountId);
					return null;
				}
				account.mAccountUid = c.getLong(KEY_MD);
				account.mAccountName = c.getString(KEY_NAME);
				account.mAccountTel = c.getString(KEY_TEL);
				account.mAccountPwd = c.getString(KEY_PWD);
				account.mAccountEmail = c.getString(KEY_EMAIL);
				account.mAccountJifen = c.getString(KEY_USER);
				account.mAccountUser = c.getString(KEY_JIFEN);
				account.mAccountTotal = c.getString(KEY_TOTAL);
				account.mAccountYuqiTimes = c.getString(KEY_YUQITIME);
				account.mAccountPinjia = c.getString(KEY_PINJIA);
				account.mAccountLevel = c.getString(KEY_LEVEL);
			}
		    c.close();
		}
		
		return account;
	}
	
	public boolean updateAccount(ContentResolver cr, ContentValues addtion) {
		int update = cr.update(BjnoteContent.Accounts.CONTENT_URI, addtion, WHERE_UID, new String[]{String.valueOf(mAccountUid)});
		DebugUtils.logD(TAG, "saveInDatebase update exsited uid#" + mAccountUid + "# " + (update > 0));
		return update > 0;
	}
	
	@Override
	public boolean saveInDatebase(ContentResolver cr, ContentValues addtion) {
		
		ContentValues values = new ContentValues();
		if (addtion != null) {
			values.putAll(addtion);
		}
		long id = isExsited(cr,mAccountUid);
		values.put(DBHelper.ACCOUNT_NAME, mAccountName);
		values.put(DBHelper.ACCOUNT_TEL, mAccountTel);
		values.put(DBHelper.ACCOUNT_PWD, mAccountPwd);
		values.put(DBHelper.ACCOUNT_EMAIL, mAccountEmail);
		values.put(DBHelper.ACCOUNT_USER, mAccountUser);
		values.put(DBHelper.ACCOUNT_JIFEN, mAccountJifen);
		values.put(DBHelper.ACCOUNT_TOTAL, mAccountTotal);
		values.put(DBHelper.ACCOUNT_YUQITIME, mAccountYuqiTimes);
		values.put(DBHelper.ACCOUNT_PINJIA, mAccountPinjia);
		values.put(DBHelper.ACCOUNT_LEVEL, mAccountLevel);
		values.put(DBHelper.DATE, new Date().getTime());
		if (id > 0) {
			int update = cr.update(BjnoteContent.Accounts.CONTENT_URI, values, WHERE_UID, new String[]{String.valueOf(mAccountUid)});
			if (update > 0) {
				DebugUtils.logD(TAG, "saveInDatebase update exsited uid#" + mAccountUid);
				mAccountId = id;
				//TODO
				return true;
			} else {
				DebugUtils.logD(TAG, "saveInDatebase failly update exsited uid " + mAccountUid);
			}
		} else {
			//如果没有本地没有账户，那么我们新增的时候增加ACCOUNT_MD字段,并设置为当前默认账户
			values.put(DBHelper.ACCOUNT_UID, mAccountUid);
			values.put(DBHelper.ACCOUNT_DEFAULT, 1);
			Uri uri = cr.insert(BjnoteContent.Accounts.CONTENT_URI, values);
			if (uri != null) {
				DebugUtils.logD(TAG, "saveInDatebase insert uid#" + mAccountUid);
				mAccountId = ContentUris.parseId(uri);
				return true;
			} else {
				DebugUtils.logD(TAG, "saveInDatebase failly insert uid#" + mAccountUid);
			}
		}
		return false;
	}
	
	private long isExsited(ContentResolver cr, long uid) {
		long id = -1;
		Cursor c = cr.query(BjnoteContent.Accounts.CONTENT_URI, PROJECTION_UID, WHERE_UID, new String[]{String.valueOf(uid)}, null);
		if (c != null) {
			if (c.moveToNext()) {
				id = c.getLong(KEY_ID);
			}
			c.close();
		}
		return id;
	}
	public static long DEMO_ACCOUNT_UID = 351356;
	public boolean isDemoAccountObject() {
		return mAccountUid == DEMO_ACCOUNT_UID;
	}
	
	public static ContentValues getDemoAccountObjectContentValues() {
		ContentValues values = new ContentValues();
		values.put(DBHelper.ACCOUNT_UID, DEMO_ACCOUNT_UID);
		values.put(DBHelper.ACCOUNT_NAME, MyApplication.getInstance().getResources().getString(R.string.demo_account));
		values.put(DBHelper.ACCOUNT_TEL, "11111111111");
		values.put(DBHelper.ACCOUNT_PWD, "123456");
		values.put(DBHelper.ACCOUNT_DEFAULT, 0);
		values.put(DBHelper.DATE, new Date().getTime());
		return values;
    }
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Account[uid=").append(mAccountUid)
		.append(", accountName=").append(mAccountName)
		.append(", accountTel=").append(mAccountTel)
		.append("]");
		return sb.toString();
	}
	
}
