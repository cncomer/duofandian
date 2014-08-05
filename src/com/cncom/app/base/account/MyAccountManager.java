package com.cncom.app.base.account;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.cncom.app.base.database.BjnoteContent;
import com.cncom.app.base.database.DBHelper;
import com.shwy.bestjoy.utils.DebugUtils;

public class MyAccountManager {
	private static final String TAG = "HaierAccountManager";
	private AccountObject mAccountObject;
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
		mAccountObject = null;
		mSharedPreferences = mContext.getSharedPreferences(TAG, Context.MODE_PRIVATE);
		initAccountObject();
	}
	
	public void initAccountObject() {
		//如果没有默认账户，我们使用Demo账户
		if (mAccountObject == null) {
			mAccountObject = AccountObject.getAccountFromDatabase(mContext);
		}
		if (mAccountObject == null) {
			mAccountObject = AccountObject.getAccountFromDatabase(mContext, AccountObject.DEMO_ACCOUNT_UID);
		}
	}
	
	
	public void deleteDefaultAccount() {
		if (mAccountObject != null) {
			DebugUtils.logD(TAG, "start deleteDefaultAccount() for uid " + mAccountObject.mAccountUid);
			int deleted = AccountObject.deleteAccount(mContext.getContentResolver(), mAccountObject.mAccountUid);
			DebugUtils.logD(TAG, "deleted " + deleted + " Account =" + mAccountObject.toString());
			mAccountObject = null;
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
		return mAccountObject;
	}
	
	public String getDefaultPhoneNumber() {
		return mAccountObject != null ? mAccountObject.mAccountTel : null;
	}
	
	public String getCurrentAccountMd() {
		return getCurrentAccountUid();
	}
	
	public String getCurrentAccountUid() {
		return mAccountObject != null ? String.valueOf(mAccountObject.mAccountUid) : null; 
	}
	public String getCurrentAccountName() {
		return mAccountObject != null ? String.valueOf(mAccountObject.mAccountName) : null; 
	}
	/**是否已经登录*/
	public boolean hasLoginned() {
		return mAccountObject != null && mAccountObject.mAccountId > 0;
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
    	
    	if (mAccountObject != accountObject) {
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
    	mAccountObject = null;
    	initAccountObject();
    }
    
    /**
     * 初始化演示账户
     * @return
     */
    public static AccountObject initDemoAccountObject(Context context) {
    	return AccountObject.getAccountFromDatabase(context, AccountObject.DEMO_ACCOUNT_UID);
    }
    
}
