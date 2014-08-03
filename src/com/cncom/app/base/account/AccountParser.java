package com.cncom.app.base.account;


import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.widget.TextView;

import com.lnwoowken.lnwoowkenbook.MyApplication;
import com.shwy.bestjoy.utils.DebugUtils;
import com.shwy.bestjoy.utils.InfoInterfaceImpl;
import com.shwy.bestjoy.utils.NetworkUtils;
/**
 * ���������������½���ص�json���ݣ�����AccountObject�˻�����<br/>
 * ��½�����������£�<br/>
 * http://115.29.231.29/Haier/login.ashx?cell=18621951097&pwd=wangkun
 * <br/>
 * <br/>
 * ������������<br/>
 * {"usersdata":{"StatusCode":"1","StatusMessage":"�����û�����","Data":{"cell":"18621951097","pwd":"wangkun","yanma":null,"userName":"����","UID":1}},"address":[{"ShenFen":"���ձ�Ӧ��Ӧ���","City":"����","QuXian":"���ձ�Ӧ��Ӧ���","DetailAddr":"���ձ�Ӧ��Ӧ���","UID":1,"AID":1}]}
 * <br/>
 * ����˵������<br/>
 * ���У�"usersdata" �����ϴε�����<br/>
 * cell:�ֻ�<br/>
 * pwd:����<br/>
 * yama:��֤��<br/>
 * username:����<br/>
 * uid:ΨһiD<br/>
 * <p/>
 * "address"����ַ�б�<br/>
 * ShenFen��ʡ<br/>
 * City:��<br/>
 * QuXian:����<br/>
 * DetailAddr�������ַ<br/>
 * UID:�û�id <br/>
 * AID:��ַid<br/>
 * 
 * @author chenkai
 *
 */
public class AccountParser extends InfoInterfaceImpl{
	private static final String TAG = "AccountParser";

	/**
	 * ����JSON��ʽ��Account���ݣ��������null����ʾ����ʧ�ܣ��������accountObject.mStatusCode == 0����ʾ��¼ʧ��
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
//					view.setText(R.string.msg_login_download_accountinfo_wait);//��ʾ��������ʲô��������
				}
			});
		}
		AccountObject accountObject = new AccountObject();
		//TODO ����Account����
		parseUserData(jsonObject, accountObject);
		return accountObject;
	}
	
	/***
	 * "usersdata": {"cell":"18621951097","pwd":"wangkun","yanma":null,"userName":"����","UID":1}
	 * @param jsonObject
	 * @param accountObject
	 * @throws JSONException
	 */
	public static void parseUserData(JSONObject jsonObject, AccountObject accountObject) throws JSONException {
//		//����userdata
//		JSONObject userData = jsonObject.getJSONObject("usersdata");
//		
//		accountObject.mStatusCode = Integer.valueOf(userData.getString("StatusCode"));
//		accountObject.mStatusMessage = userData.getString("StatusMessage");
//		if (accountObject.mStatusCode == 0) {
//			//�����ʧ�ܵģ�������ǰ���أ����ý�������������
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
