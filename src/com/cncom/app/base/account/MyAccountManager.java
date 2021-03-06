﻿package com.cncom.app.base.account;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.lnwoowken.lnwoowkenbook.R;
import com.shwy.bestjoy.utils.DebugUtils;

public class MyAccountManager {
	private static final String TAG = "HaierAccountManager";
	private AccountObject mAccountObject;
	private Context mContext;
	SharedPreferences mSharedPreferences;
	private ContentResolver mContentResolver;
	private static MyAccountManager mInstance = new MyAccountManager();
	
	public static final int[] SYSTEM_AVATOR_ARRAY = new int[]{
		R.drawable.photo_defult,
		R.drawable.system_avator1,
		R.drawable.system_avator2,
		R.drawable.system_avator3,
		R.drawable.system_avator4,
		R.drawable.system_avator5,
		R.drawable.system_avator6,
	};
	private Drawable mManAvatorBg, mWomanAvatorBg, mDefaultAvatorBg;
	
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
		mDefaultAvatorBg = mContext.getResources().getDrawable(com.actionbarsherlock.R.drawable.abs__item_background_holo_light);
		mManAvatorBg = mContext.getResources().getDrawable(R.drawable.avator_man_bg);
		mWomanAvatorBg = mContext.getResources().getDrawable(R.drawable.avator_woman_bg);
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
	public long getCurrentAccountId() {
		return mAccountObject != null ? mAccountObject.mAccountUid : -1; 
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
    public int getAccountSystemAvatorResId() {
    	if (mAccountObject == null) {
    		return SYSTEM_AVATOR_ARRAY[0];
    	} else {
    		return SYSTEM_AVATOR_ARRAY[mAccountObject.getSystemAvatorIndex()];
    	}
    }
    
    public int getSystemAvatorBackgroudResId() {
    	if (mAccountObject != null) {
    		if (mAccountObject.getSystemAvatorIndex() == 0) {
    			return com.actionbarsherlock.R.drawable.abs__item_background_holo_light;
    		} else if (mAccountObject.getSystemAvatorIndex() % 2 == 0) {
    			//取余数，如果是奇数则是女性头像，否则是男性头像
    			return R.drawable.avator_man_bg;
    		} else {
    			return R.drawable.avator_woman_bg;
    		}
    	}
    	return com.actionbarsherlock.R.drawable.abs__item_background_holo_light;
    }
    /**
     * 由于我们定义的头像数组中包含了默认头像，索引为0，所以在实际使用中，如头像选取界面，Adapter的position+1才是对应的头像索引
     * @param position
     * @return
     */
    public int getSystemAvatorBackgroudResId(int position) {
    	if (mAccountObject != null) {
    		if (position % 2 == 0) {
    			//取余数，如果是奇数则是女性头像，否则是男性头像
    			return R.drawable.avator_man_bg_p;
    		} else {
    			return R.drawable.avator_woman_bg_p;
    		}
    	}
    	return com.actionbarsherlock.R.drawable.abs__item_background_holo_light;
    }
    /**
     * 返回系统头像的背景，单女双男，0为默认头像背景
     * @return
     */
    public Drawable getSystemAvatorBackgroudDrawable() {
    	if (mAccountObject != null) {
    		if (mAccountObject.getSystemAvatorIndex() == 0) {
    			return mDefaultAvatorBg;
    		} else if (mAccountObject.getSystemAvatorIndex() % 2 == 0) {
    			//取余数，如果是奇数则是女性头像，否则是男性头像
    			return mManAvatorBg;
    		} else {
    			return mWomanAvatorBg;
    		}
    	}
    	return mDefaultAvatorBg;
    }
    
    public boolean hasSystemAvator() {
    	return mAccountObject != null && mAccountObject.isSystemAvator();
    }
    
    public int getSystemAvatorIndex() {
    	if (mAccountObject != null && mAccountObject.isSystemAvator()) {
    		return mAccountObject.getSystemAvatorIndex();
    	}
    	return -1;
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
    
    private int[] mMemberLevelResIds = new int[]{
    		R.string.member_level_0, //普通会员
    		R.string.member_level_1, //金卡会员
    		R.string.member_level_2, //钻石会员
    };
    private static final int INDEX_MEMBER_LEVEL_0 = 0;
    private static final int INDEX_MEMBER_LEVEL_1 = 1;
    private static final int INDEX_MEMBER_LEVEL_2 = 2;
    /**
     * 会员等级只有:普通会员、金 卡会员、钻石会员。 
     * *后台数据库有严格的会员管 理标准:会员等级:按积分来 进行评定。
     * 10000 积分以下为 普通会员,
     * 10000-50000 积分为 金卡会员,
     * 50000 积分以上为钻石会员。由系统自动统计显示。
     * @return
     */
    public int getMemberLevelResId() {
    	int jifen = Integer.valueOf(mAccountObject.mAccountJifen);
    	if (jifen< 10000) {
    		return mMemberLevelResIds[INDEX_MEMBER_LEVEL_0];
    	} else if (jifen < 50000) {
    		return mMemberLevelResIds[INDEX_MEMBER_LEVEL_1];
    	} else {
    		return mMemberLevelResIds[INDEX_MEMBER_LEVEL_2];
    	}
    }
    
    public String getAccountName() {
    	if (!TextUtils.isEmpty(mAccountObject.mAccountName)) {
    		return mAccountObject.mAccountName;
    	}
    	return mContext.getString(R.string.title_member_name);
    }
    
}
