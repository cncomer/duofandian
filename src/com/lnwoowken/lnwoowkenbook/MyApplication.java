package com.lnwoowken.lnwoowkenbook;

import java.io.File;

import org.json.JSONObject;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.service.PhotoManagerUtilsV2;
import com.cncom.app.base.util.BeepAndVibrate;
import com.cncom.app.base.util.BitmapUtils;
import com.shwy.bestjoy.utils.ComConnectivityManager;
import com.shwy.bestjoy.utils.DateUtils;
import com.shwy.bestjoy.utils.DebugUtils;
import com.shwy.bestjoy.utils.DeviceStorageUtils;
import com.shwy.bestjoy.utils.DevicesUtils;
import com.shwy.bestjoy.utils.SecurityUtils.SecurityKeyValuesObject;
import com.umeng.analytics.MobclickAgent;

public class MyApplication extends Application{
	
	private static final String TAG ="BJfileApp";
	/**���ڲ�ͬ�ı��޿�������ֻҪȷ���ñ���Ϊ��ȷ��Ӧ�ð�������*/
	public static final String PKG_NAME = "com.bestjoy.app.bjwarrantycard";
	private Handler mHandler;
	private static MyApplication mInstance;
	public SharedPreferences mPreferManager;
	
	/**�ɹ�ɾ���ַ���*/
	public static final String mDeleteOk="ok";
	
	private InputMethodManager mImMgr;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate()");
		MobclickAgent.setDebugMode(true);
		mHandler = new Handler();
		mInstance = this;
		DevicesUtils.getInstance().setContext(this);
		DeviceStorageUtils.getInstance().setContext(this);
		
		DateUtils.getInstance().setContext(this);
		//add by chenkai, 20131201, �������
		ComConnectivityManager.getInstance().setContext(this);
		BeepAndVibrate.getInstance().setContext(this);
		
		BitmapUtils.getInstance().setContext(this);
		
		MyAccountManager.getInstance().setContext(this);
		
		mPreferManager = PreferenceManager.getDefaultSharedPreferences(this);
		
		mImMgr = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		
		PhotoManagerUtilsV2.getInstance().setContext(this);
		//������Ļ����
		DisplayMetrics display = this.getResources().getDisplayMetrics();
		Log.d(TAG, display.toString());
		Log.d(TAG, getDeviceInfo(this));
		
	}
	
	public synchronized static MyApplication getInstance() {
		return mInstance;
	}
	
	public File getCachedContactFile(String name) {
		return new File(getFilesDir(), name+ ".vcf");
	}
	
	/**�õ��˻���Ƭ��ͷ��ͼƬ�ļ�*/
	public File getAccountCardAvatorFile(String name) {
		return new File(getAccountDir(MyAccountManager.getInstance().getCurrentAccountMd()), name+ ".p");
	}
	public File getAccountCardAvatorFile(String accountUid, String name) {
		return new File(getAccountDir(accountUid), name+ ".p");
	}
	/**���ػ���Ŀ¼caches/�������ʱͷ���ļ�*/
	public File getCachedPreviewAvatorFile(String name) {
		return new File(getCacheDir(), name+ ".p");
	}
	/**���ػ���Ŀ¼caches/�������ʱvcf�ļ�*/
	public File getCachedPreviewContactFile(String name) {
		return new File(getCacheDir(), name+ ".vcf");
	}
	public File getAccountCardFile(String accountUid, String cardMm) {
		if (TextUtils.isEmpty(accountUid) || TextUtils.isEmpty(cardMm)) {
			DebugUtils.logE(TAG, "getAccountCardFile return null due to accountMd=" + accountUid + " cardMm" + cardMm);
			return null;
		}
		return new File(getAccountDir(accountUid), cardMm+ ".vcf");
	}
	
	public File getAppFilesDir(String dirName) {
		File root = new File(getFilesDir(), dirName);
		if (!root.exists()) {
			root.mkdirs();
		}
		return root;
	}
	
	public File getAccountsRoot() {
		File accountsRoot = getAppFilesDir("accounts");
		
		if (!accountsRoot.exists()) {
			accountsRoot.mkdirs();
		}
		return accountsRoot;
	}
	
	public File getAccountDir(String accountMd) {
		File accountRoot = new File(getAppFilesDir("accounts"), accountMd);
		
		if (!accountRoot.exists()) {
			accountRoot.mkdirs();
		}
		return accountRoot;
	}
	
	public File getProductDir() {
		File productRoot = new File(getAppFilesDir("accounts"), "product");
		if (!productRoot.exists()) {
			productRoot.mkdirs();
		}
		return productRoot;
	}
	public File getProductSubDir(String dirName) {
		File productRoot = new File(getProductDir(), dirName);
		if (!productRoot.exists()) {
			productRoot.mkdirs();
		}
		return productRoot;
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		ComConnectivityManager.getInstance().endConnectivityMonitor();
	}
	
	
	public boolean hasExternalStorage() {
	    	return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	public void showMessageAsync(final int resId) {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(mInstance, resId, Toast.LENGTH_LONG).show();
			}
		});
	}
	
	public void showMessageAsync(final String msg) {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(mInstance, msg, Toast.LENGTH_LONG).show();
			}
		});
	}
	
	public void showMessageAsync(final int resId, final int length) {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(mInstance, resId, resId).show();
			}
		});
	}
	
	public void showMessageAsync(final String msg, final int length) {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(mInstance, msg, length).show();
			}
		});
	}
	
	public void showShortMessageAsync(final int msgId, final int toastId) {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(mInstance, msgId, toastId).show();
			}
		});
	}
	
	public void showMessage(int resId) {
		Toast.makeText(mInstance, resId, Toast.LENGTH_LONG).show();
	}
	
	public void showMessage(String msg) {
		Toast.makeText(mInstance, msg, Toast.LENGTH_LONG).show();
	}
	public void showMessage(String msg, int length) {
		Toast.makeText(mInstance, msg, length).show();
	}
	
	public void showMessage(int resId, int length) {
		Toast.makeText(mInstance, resId, length).show();
	}
	
	public void showShortMessage(int resId) {
		showMessage(resId, Toast.LENGTH_SHORT);
	}
	
	public void postAsync(Runnable runnable){
		mHandler.post(runnable);
	}
	public void postDelay(Runnable runnable, long delayMillis){
		mHandler.postDelayed(runnable, delayMillis);
	}
	
	
	public void showUnsupportMessage() {
    	showMessage(R.string.msg_unsupport_operation);
    }
	/**��ʾ��Ҫ�ȵ�¼��ʾ��Ϣ*/
	public void showNeedLoginMessage() {
    	showMessage(R.string.msg_need_login_operation);
    }
	
	//add by chenkai, 20131123, security support begin
    private SecurityKeyValuesObject mSecurityKeyValuesObject;
    public SecurityKeyValuesObject getSecurityKeyValuesObject() {
    	if (mSecurityKeyValuesObject == null) {
    		//Here, we need to notice.
    		new Exception("warnning getSecurityKeyValuesObject() return null").printStackTrace();
    	}
    	return mSecurityKeyValuesObject;
    }
    public void setSecurityKeyValuesObject(SecurityKeyValuesObject securityKeyValuesObject) {
    	mSecurityKeyValuesObject = securityKeyValuesObject;
    }
    
  //add by chenkai, 20131208, updating check begin
    public File buildLocalDownloadAppFile(int downloadedVersionCode) {
    	StringBuilder sb = new StringBuilder("Warranty_");
    	sb.append(String.valueOf(downloadedVersionCode))
    	.append(".apk");
    	return new File(getExternalStorageRoot(".download"), sb.toString());
    }
    
    /**
     * ����SD����Ӧ�ø�Ŀ¼��typeΪ��Ŀ¼���֣� ��download��.download
     * @param type
     * @return
     */
    public File getExternalStorageRoot(String type) {
    	if (!hasExternalStorage()) {
    		return null;
    	}
    	File root = new File(Environment.getExternalStorageDirectory(), getPackageName());
    	if (!root.exists()) {
    		root.mkdirs();
    	}
    	root =  new File(root, type);
    	if (!root.exists()) {
    		root.mkdir();
    	}
    	return root;
    }
    //add by chenkai, 20131208, updating check end
    
    /***
     * ��ʾͨ�����������Ӵ���
     * @return
     */
    public String getGernalNetworkError() {
    	return this.getString(R.string.msg_gernal_network_error);
    }
    
  //add by chenkai, for Usage, 2013-06-05 begin
    /**return mnt/sdcard/xxx/accountmdĿ¼*/
    public File getExternalStorageAccountRoot(String accountMd) {
    	if (!hasExternalStorage()) {
    		return null;
    	}
    	File root =  new File(getExternalStorageRoot("account"), accountMd);
    	if (!root.exists()) {
    		root.mkdir();
    	}
    	return root;
    }
    /**�õ�SD���˺Ŷ�Ӧ�����Ŀ¼*/
    public File getExternalStorageModuleRootForAccount(String accountMd, String moduleName) {
    	if (!hasExternalStorage()) {
    		return null;
    	}
    	File root = new File(getExternalStorageAccountRoot(accountMd), moduleName);
    	if (!root.exists()) {
    		root.mkdirs();
    	}
    	return root;
    }
    /**���ز�Ʒʹ��˵����*/
    public File getProductUsagePdf(String ky) {
    	String accountUid = String.valueOf(MyAccountManager.getInstance().getAccountObject().mAccountUid);
		File goodsUsagePdfFile =  new File(getExternalStorageModuleRootForAccount(accountUid, "product") , ky + ".pdf");
		return goodsUsagePdfFile;
	}
    /**��ʾû��SD������*/
    public void showNoSDCardMountedMessage() {
    	showMessage(R.string.msg_sd_unavailable);
    }
    //add by chenkai, for Usage, 2013-06-05 end
    
    public void hideInputMethod(IBinder token) {
    	if (mImMgr != null) {
    		mImMgr.hideSoftInputFromWindow(token, 0);
    	}
    }
    
    /**
     * ���ػ����Ʒ���ͺ��ļ������������SD�������ļ���������ô洢��xxx/account/xxxx/xinghaoĿ¼�£��������ֻ��ڲ��洢��xxx/files/
     * @param pingpaiCode
     * @return
     */
    public File getCachedXinghaoFile(String pingpaiCode) {
    	File xinghaoFile =  null;
    	if (hasExternalStorage()) {
    		xinghaoFile =  new File(getCachedXinghaoExternalRoot(), pingpaiCode + ".json");
    	} else {
    		xinghaoFile =  new File(getCachedXinghaoInternalRoot() , pingpaiCode + ".json");;
    	}
		return xinghaoFile;
    }
    
    /**
     * �õ�sdcard�ϵ��ͺ�Ŀ¼/mnt/sdcard/xxxx/xinghao
     * @return
     */
    public File getCachedXinghaoExternalRoot() {
    	return getExternalStorageRoot("xinghao");
    }
    /**
     * �õ�sdcard�ϵ��ͺ�Ŀ¼/xxx/files/xinghao
     * @return
     */
    public File getCachedXinghaoInternalRoot() {
    	return getAppFilesDir("xinghao");
    }
    
    public File getAppFiles(String fileName) {
    	File root = getFilesDir();
    	if (!root.exists()) {
    		root.mkdirs();
    	}
		return new File(root, fileName);
	}
    
    public static String getDeviceInfo(Context context) {
	    try{
	        JSONObject json = new JSONObject();
	        TelephonyManager tm = (android.telephony.TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	  
	        String device_id = tm.getDeviceId();
	      
	        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	          
	        String mac = wifi.getConnectionInfo().getMacAddress();
	        json.put("mac", mac);
	      
	       if(TextUtils.isEmpty(device_id) ){
	            device_id = mac;
	       }
	      
	      if( TextUtils.isEmpty(device_id) ){
	           device_id = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
	      }
	      
	      json.put("device_id", device_id);
	      return json.toString();
	    }catch(Exception e){
	      e.printStackTrace();
	    }
	    return null;
	}
}
