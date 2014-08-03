package com.cncom.app.base.account;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.cncom.app.base.database.BjnoteContent;
import com.cncom.app.base.database.HaierDBHelper;
import com.shwy.bestjoy.utils.DebugUtils;

public class MyAccountManager {
	private static final String TAG = "HaierAccountManager";
	private AccountObject mHaierAccount;
	private Context mContext;
	SharedPreferences mSharedPreferences;
	private ContentResolver mContentResolver;
	private static MyAccountManager mInstance = new MyAccountManager();
	
	private MyAccountManager() {}
	
	public static MyAccountManager getInstance() {
		return mInstance;
	}
	
	public void setContext(Context context) {
		mContext = context; 
		if (mContext == null) {
			throw new RuntimeException("MyAccountManager.setContext(null), you must apply a Context object.");
		}
		mContentResolver = mContext.getContentResolver();
		mHaierAccount = null;
		mSharedPreferences = mContext.getSharedPreferences(TAG, Context.MODE_PRIVATE);
		initAccountObject();
	}
	
	public void initAccountObject() {
		//如果没有默认账户，我们使用Demo账户
		if (mHaierAccount == null) {
			mHaierAccount = AccountObject.getHaierAccountFromDatabase(mContext);
		}
		if (mHaierAccount == null) {
			mHaierAccount = AccountObject.getHaierAccountFromDatabase(mContext, AccountObject.DEMO_ACCOUNT_UID);
		}
	}
	
	
	public void deleteDefaultAccount() {
		if (mHaierAccount != null) {
			DebugUtils.logD(TAG, "start deleteDefaultAccount() for uid " + mHaierAccount.mAccountUid);
			int deleted = AccountObject.deleteAccount(mContext.getContentResolver(), mHaierAccount.mAccountUid);
			DebugUtils.logD(TAG, "deleted " + deleted + " Account =" + mHaierAccount.toString());
			mHaierAccount = null;
			DebugUtils.logD(TAG, "end deleteDefaultAccount()");
		} else {
			DebugUtils.logD(TAG, "deleteDefaultAccount() nothing to do");
		}
	}
	/**
	 * 鍒犻櫎鎸囧畾uid镄勮处鎴?
	 * @param uid
	 */
	public static void deleteAccountForUid(ContentResolver cr, long uid) {
		DebugUtils.logD(TAG, "start deleteAccountForUid() for uid " + uid);
		int deleted = AccountObject.deleteAccount(cr, uid);
		DebugUtils.logD(TAG, "end deleteAccountForUid()");
	}
	
	public AccountObject getAccountObject() {
		return mHaierAccount;
	}
	
	public String getDefaultPhoneNumber() {
		return mHaierAccount != null ? mHaierAccount.mAccountTel : null;
	}
	
	public String getCurrentAccountMd() {
		return getCurrentAccountUid();
	}
	
	public String getCurrentAccountUid() {
		return mHaierAccount != null ? String.valueOf(mHaierAccount.mAccountUid) : null; 
	}
	/**是否已经登录*/
	public boolean hasLoginned() {
		return mHaierAccount != null && mHaierAccount.mAccountId > 0;
	}
	
	/**
	 * 得到最近登录使用的用户名
	 * @return
	 */
	public String getLastUsrTel() {
		return mSharedPreferences.getString("lastUserTel", "");
	}
	
    public void saveLastUsrTel(String userName) {
    	mSharedPreferences.edit().putString("lastUserTel", (userName == null ? "" : userName)).commit();
	}
    
    public boolean saveAccountObject(ContentResolver cr, AccountObject accountObject) {
    	
    	if (mHaierAccount != accountObject) {
    		boolean success = accountObject.saveInDatebase(cr, null);
    		if (success) {
    			updateAccount();
    			return true;
    		}
    	}
    	return false;
    	
    }
    /**
     * 重新更新账户对象
     */
    public void updateAccount() {
    	mHaierAccount = null;
    	initAccountObject();
    }
    
    /**
     * 初始化演示账户
     * @return
     */
    public static AccountObject initDemoAccountObject(Context context) {
    	return AccountObject.getHaierAccountFromDatabase(context, AccountObject.DEMO_ACCOUNT_UID);
    }
    
}
