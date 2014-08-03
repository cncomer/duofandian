﻿package com.cncom.app.base.account;


import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.widget.TextView;

import com.lnwoowken.lnwoowkenbook.MyApplication;
import com.shwy.bestjoy.utils.DebugUtils;
import com.shwy.bestjoy.utils.InfoInterfaceImpl;
import com.shwy.bestjoy.utils.NetworkUtils;
/**
 * 这个类用来解析登陆返回的json数据，生成AccountObject账户对象。<br/>
 * 登陆测试数据如下：<br/>
 * http://115.29.231.29/Haier/login.ashx?cell=18621951097&pwd=wangkun
 * <br/>
 * <br/>
 * 返回数据如下<br/>
 * {"usersdata":{"StatusCode":"1","StatusMessage":"返回用户数据","Data":{"cell":"18621951097","pwd":"wangkun","yanma":null,"userName":"王坤","UID":1}},"address":[{"ShenFen":"江苏宝应宝应大道","City":"扬州","QuXian":"江苏宝应宝应大道","DetailAddr":"江苏宝应宝应大道","UID":1,"AID":1}]}
 * <br/>
 * 数据说明如下<br/>
 * 其中："usersdata" 里是上次的数据<br/>
 * cell:手机<br/>
 * pwd:密码<br/>
 * yama:验证码<br/>
 * username:名字<br/>
 * uid:唯一iD<br/>
 * <p/>
 * "address"：地址列表<br/>
 * ShenFen：省<br/>
 * City:市<br/>
 * QuXian:区县<br/>
 * DetailAddr：具体地址<br/>
 * UID:用户id <br/>
 * AID:地址id<br/>
 * 
 * @author chenkai
 *
 */
public class AccountParser extends InfoInterfaceImpl{
	private static final String TAG = "AccountParser";

	/**
	 * 解析JSON格式的Account数据，如果返回null，表示解析失败，如果返回accountObject.mStatusCode == 0，表示登录失败
	 * @param is
	 * @param goodsObject
	 * @return
	 * @throws JSONException 
	 */
	public static AccountObject parseJson(InputStream is, final TextView view) throws JSONException {
		 DebugUtils.logD(TAG, "Start parse");
		if (is == null ) {
			return null;
		}
		JSONObject jsonObject = new JSONObject(NetworkUtils.getContentFromInput(is));
		if (view != null) {
			MyApplication.getInstance().postAsync(new Runnable() {

				@Override
				public void run() {
//					view.setText(R.string.msg_login_download_accountinfo_wait);//提示正在下载什么样的数据
				}
			});
		}
		AccountObject accountObject = new AccountObject();
		//TODO 解析Account数据
		parseUserData(jsonObject, accountObject);
		return accountObject;
	}
	
	/***
	 * "usersdata": {"cell":"18621951097","pwd":"wangkun","yanma":null,"userName":"王坤","UID":1}
	 * @param jsonObject
	 * @param accountObject
	 * @throws JSONException
	 */
	public static void parseUserData(JSONObject jsonObject, AccountObject accountObject) throws JSONException {
//		//解析userdata
//		JSONObject userData = jsonObject.getJSONObject("usersdata");
//		
//		accountObject.mStatusCode = Integer.valueOf(userData.getString("StatusCode"));
//		accountObject.mStatusMessage = userData.getString("StatusMessage");
//		if (accountObject.mStatusCode == 0) {
//			//如果是失败的，我们提前返回，不用解析其他数据了
//			return;
//		}
//		
//		userData = userData.getJSONObject("Data");
//		accountObject.mAccountTel = userData.getString("cell");
//		accountObject.mAccountPwd = userData.getString("pwd");
//		accountObject.mAccountName = userData.getString("userName");
//		accountObject.mAccountUid = userData.getLong("UID");
	}
	
}
