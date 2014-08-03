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
import com.cncom.app.base.database.HaierDBHelper;
import com.lnwoowken.lnwoowkenbook.MyApplication;
import com.lnwoowken.lnwoowkenbook.R;
import com.shwy.bestjoy.utils.DebugUtils;
import com.shwy.bestjoy.utils.InfoInterface;
/**
 * �˻������ڳ�������ʱ���ͨ��{@link MyAccountManager#setContext(Context context)}����õ�ǰĬ���˻���
 * 
 * ��Ҫע����ǣ���������ݿ��ʱ����{@link HaierDBHelper#ACCOUNT_HOME_COUNT}�ֶΣ����ֶλ�������������ɾ��һ��HomeObject����
 * �Զ����Ӻͼ��٣��������Ǳ����ʱ��Ҫ����������ԱmAccountHomes �Լ� mBaoxiuCardsĬ�϶��ǿյģ������Ҫ����Ҫ������÷�������ã�
 * @author chenkai
 *
 */
public class AccountObject implements InfoInterface{
	private static final String TAG = "HaierAccount";
	
	private static final String[] PROJECTION = new String[]{
		HaierDBHelper.ID,
		HaierDBHelper.ACCOUNT_UID,
		HaierDBHelper.ACCOUNT_NAME,
		HaierDBHelper.ACCOUNT_TEL,
		HaierDBHelper.ACCOUNT_PWD,
	};
	
	private static final String[] PROJECTION_UID = new String[]{
		HaierDBHelper.ID,
		HaierDBHelper.ACCOUNT_UID,
	};
	
	
	private static final int KEY_ID = 0;
	private static final int KEY_MD = 1;
	private static final int KEY_NAME = 2;
	private static final int KEY_TEL = 3;
	private static final int KEY_PWD = 4;
	
	private static final String WHERE_DEFAULT = HaierDBHelper.ACCOUNT_DEFAULT + "=1";
	private static final String WHERE_UID = HaierDBHelper.ACCOUNT_UID + "=?";
	
	public long mAccountId = -1;
	public long mAccountUid = -1;
	public String mAccountName;
	public String mAccountTel;
	public String mAccountPwd;
	
	
	
	public AccountObject clone() {
		AccountObject newAccountObject = new AccountObject();
		newAccountObject.mAccountId = mAccountId;
		newAccountObject.mAccountUid = mAccountUid;
		newAccountObject.mAccountName = mAccountName;
		newAccountObject.mAccountTel = mAccountTel;
		newAccountObject.mAccountPwd = mAccountPwd;
		return newAccountObject;
	}
	
	public static int deleteAccount(ContentResolver cr, long uid) {
		return cr.delete(BjnoteContent.Accounts.CONTENT_URI, WHERE_UID, new String[]{String.valueOf(uid)});
	}
	
	public static AccountObject getHaierAccountFromDatabase(Context context) {
		return getHaierAccountFromDatabase(context, -1);
	}
	
	public static AccountObject getHaierAccountFromDatabase(Context context, long uid) {
		AccountObject haierAccount = null;
		Cursor c = null;
		if (uid == -1) {
			//Ĭ���˻�
			c = context.getContentResolver().query(BjnoteContent.Accounts.CONTENT_URI, PROJECTION, WHERE_DEFAULT, null, null);
		} else {
			//����ָ����uid��ѯ�˻�
			c = context.getContentResolver().query(BjnoteContent.Accounts.CONTENT_URI, PROJECTION, WHERE_UID, new String[]{String.valueOf(uid)}, null);
		}
		if (c != null) {
			if (c.moveToNext()) {
				haierAccount = new AccountObject();
				String idStr = c.getString(KEY_ID);
				if (TextUtils.isEmpty(idStr)) {
					DebugUtils.logD(TAG, "getHaierAccountFromDatabase accountId is " + idStr);
					return null;
				}
				haierAccount.mAccountId = Long.parseLong(idStr);
				if (haierAccount.mAccountId <= 0) {
					DebugUtils.logD(TAG, "getHaierAccountFromDatabase accountId is " + haierAccount.mAccountId);
					return null;
				}
				haierAccount.mAccountUid = c.getLong(KEY_MD);
				haierAccount.mAccountName = c.getString(KEY_NAME);
				haierAccount.mAccountTel = c.getString(KEY_TEL);
				haierAccount.mAccountPwd = c.getString(KEY_PWD);
			}
		    c.close();
		}
		
		return haierAccount;
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
		values.put(HaierDBHelper.ACCOUNT_NAME, mAccountName);
		values.put(HaierDBHelper.ACCOUNT_TEL, mAccountTel);
		values.put(HaierDBHelper.ACCOUNT_PWD, mAccountPwd);
		//����������HOME���ϴ����˴�������һ��������ɾ�ᴥ������Account��ACCOUNT_HOME_COUNT�ֶΣ����ԣ�����Ͳ��ø��¸��ֶ���
//		values.put(HaierDBHelper.ACCOUNT_HOME_COUNT, mAccountHomes.size());
		values.put(HaierDBHelper.DATE, new Date().getTime());
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
			//���û�б���û���˻�����ô����������ʱ������ACCOUNT_MD�ֶ�,������Ϊ��ǰĬ���˻�
			values.put(HaierDBHelper.ACCOUNT_UID, mAccountUid);
			values.put(HaierDBHelper.ACCOUNT_DEFAULT, 1);
			Uri uri = cr.insert(BjnoteContent.Accounts.CONTENT_URI, values);
			if (uri != null) {
				DebugUtils.logD(TAG, "saveInDatebase insert uid#" + mAccountUid);
				mAccountId = ContentUris.parseId(uri);
				//TODO
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
		values.put(HaierDBHelper.ACCOUNT_UID, DEMO_ACCOUNT_UID);
		values.put(HaierDBHelper.ACCOUNT_NAME, MyApplication.getInstance().getResources().getString(R.string.demo_account));
		values.put(HaierDBHelper.ACCOUNT_TEL, "11111111111");
		values.put(HaierDBHelper.ACCOUNT_PWD, "123456");
		values.put(HaierDBHelper.ACCOUNT_DEFAULT, 0);
		values.put(HaierDBHelper.DATE, new Date().getTime());
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
