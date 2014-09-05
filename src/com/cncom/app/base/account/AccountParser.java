package com.cncom.app.base.account;


import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.shwy.bestjoy.utils.InfoInterfaceImpl;
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

	/***
	 * {"StatusCode":"1",
	 * "StatusMessage":"成功返回"
	 * "Data":{"user_cell":"18611986102","user_email":"250763254@qq.com","user_account":"","user_jifen":"0","username_name":"roy","user_total":"0","user_yuqitimes":0,"user_pinjia":"","user_level":"0","ID":"14","pwd":null}}
	 */
	public static void parseAccountData(JSONObject jsonObject, AccountObject accountObject) throws JSONException {
		accountObject.mAccountTel = jsonObject.getString("user_cell");
		accountObject.mAccountEmail = jsonObject.getString("user_email");
		accountObject.mAccountUser = jsonObject.getString("user_account");
		accountObject.mAccountJifen = jsonObject.getString("user_jifen");
		accountObject.mAccountName = jsonObject.getString("username_name");
		accountObject.mAccountTotal = jsonObject.getString("user_total");
		accountObject.mAccountYuqiTimes = jsonObject.getString("user_yuqitimes");
		accountObject.mAccountPinjia = jsonObject.getString("user_pinjia");
		accountObject.mAccountLevel = jsonObject.getString("user_level");
		accountObject.mAccountUid = jsonObject.getLong("ID");
		accountObject.mAccountAvator = jsonObject.optString("img", AccountObject.DEFAULT_AVATOR_INDEX);
		if (TextUtils.isEmpty(accountObject.mAccountAvator)) {
			accountObject.mAccountAvator = AccountObject.DEFAULT_AVATOR_INDEX;
		}
	}
	
	public static AccountObject parseAccountData(JSONObject jsonObject) throws JSONException {
		AccountObject accountObject = new AccountObject();
		parseAccountData(jsonObject, accountObject);
		return accountObject;
	}
	
}
