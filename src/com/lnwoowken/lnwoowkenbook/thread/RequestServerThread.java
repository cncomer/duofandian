/**
 * 请求服务器的线程
 * 
 */
package com.lnwoowken.lnwoowkenbook.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.lnwoowken.lnwoowkenbook.adapter.AllStoreListAdapter;
import com.lnwoowken.lnwoowkenbook.lnwoowkeninterface.RequestServerInterface;
import com.lnwoowken.lnwoowkenbook.model.Contant;
import com.lnwoowken.lnwoowkenbook.network.Client;

public class RequestServerThread extends Thread implements
		RequestServerInterface {

	private Context context;
	private String url = "";
	private Handler handler = null;
	private String result = "";
	private int flag = 0;
	private AllStoreListAdapter adapter;
	private Handler listDataChanged_handler;
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public RequestServerThread(String url, Handler handler, Context context,
			int flag) {
		this.flag = flag;
		this.url = url;
		this.handler = handler;
		this.context = context;
	}
	
	public RequestServerThread(String url, Handler handler, Context context,
			int flag,AllStoreListAdapter adapter,Handler listDataChanged_handler) {
		this.flag = flag;
		this.url = url;
		this.handler = handler;
		this.context = context;
		this.adapter = adapter;
		this.listDataChanged_handler = listDataChanged_handler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		doExecuteByFlag();

	}

	/**
	 * 获取所有餐厅信息列表
	 * 
	 * @author sean
	 */
	@Override
	public void getAllShopInfo() {
		// TODO Auto-generated method stub
		this.result = Client.executeHttpGetAndCheckNet(this.url, this.context);
		Message msg = new Message();
		if (this.result.equals(Contant.NO_NET)) { 
			msg.arg1 = 1;
		}
		this.handler.sendMessage(msg);
	}


	/**
	 * 注册用户
	 * 
	 * @author sean
	 */
	@Override
	public void registUserInfo() {
		// TODO Auto-generated method stub
		this.result = Client.executeHttpGetAndCheckNet(this.url, context);
		Log.d("regist==============", result);
		if (this.result.equals(Contant.NO_NET)) {
			Message msg = new Message();
			msg.arg1 = 1;
			this.handler.sendMessage(msg);
		} else if (this.result != null && !this.result.equals(Contant.NO_NET)) {
			Message msg = new Message();
			this.handler.sendMessage(msg);
		}
	}

	/**
	 * 获取验证短信
	 * 
	 * @author sean
	 */
	@Override
	public void getSMS() {
		// TODO Auto-generated method stub
		this.result = Client.executeHttpGetAndCheckNet(this.url, context);
		Log.d("getSMS=====================", this.result);
		
//		if (this.result.equals(Contant.NO_NET)) {
//			Message msg = new Message();
//			msg.arg1 = 1;
//			this.handler.sendMessage(msg);
		if (this.result != null) {
			Message msg = new Message();
			this.handler.sendMessage(msg);
		}
	}

	/**
	 * 用户登录
	 * 
	 * @author sean
	 */
	@Override
	public String login() {
		String temp = Client.executeHttpGetAndCheckNet(this.url, context);
		if(TextUtils.isEmpty(temp)) return null;
		this.result = temp;//Client.decodeBase64(temp);
		Message msg = new Message();
		this.handler.sendMessage(msg);
		return this.result;
	}

	/**
	 * 通过标识调用相应的方法
	 * 
	 * @author sean
	 */
	@Override
	public void doExecuteByFlag() {
		switch (this.flag) {
		case Contant.FLAG_GETALLSHOPINFO:
			getAllShopInfo();
			break;
		case Contant.FLAG_GETSHOPBYID:
			getShopInfoById();
			break;
		case Contant.FLAG_REGIST:
			registUserInfo();
			break;
		case Contant.FLAG_GETSMS:
			getSMS();
			break;
		case Contant.FLAG_LOGIN:
			login();
			break;
		case 7:
			getVersion();
			break;
		default:
			break;
		}
	}


	private String getVersion(){ 
		String resultJson = null; 
//		String opJson = "{\"sid\":\""
//				+ Contant.SHOP_LIST.get(i).getId() + "\",\"rt\":\""
//				+ time + "\"}";
//		opJson = Client.encodeBase64(opJson);
//		String str = Tools.getRequestStr(Contant.SERVER_IP,
//				Contant.SERVER_PORT + "", "sys?id=", "st5",
//				"&op=" + opJson);
//		String str = "http://" + Contant.SERVER_IP + ":"+ Contant.SERVER_PORT + "/javadill/sys?id={\"id\":\"sversion\",\"vd\":\"0\",\"vc\":\"0\"}";
		
		String url = "http://" + Contant.SERVER_IP + ":"+ Contant.SERVER_PORT + "/Mobile/GetVersion.ashx";
		
//		String strId = "{\"id\":\""+Contant.VERSION+"\",\"vd\":\"0\",\"vc\":\"0\"}";
//		strId = Client.encodeBase64(strId);
//		List <NameValuePair> params=new ArrayList<NameValuePair>();
//	    params.add(new BasicNameValuePair("id",strId));
//	    Log.d(url, strId);
		try {
			
			resultJson = Client.executeHttpGetAndCheckNet(url, context);
//			resultJson = Client.doPost(params, url);
//			resultJson = Client.decodeBase64(resultJson);
			this.result = resultJson;
			Message msg = new Message();
			this.handler.sendMessage(msg);
			Log.d("version=============", resultJson);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//resultJson = Client.executeHttpGetAndCheckNet(str, this.context);
		
		return resultJson;
//		resultJson = Client.decodeBase64(resultJson);

//		if (resultJson != null) {
//
//			Log.d("tableNumber=============", resultJson);
//			// JsonParser.parseShopInfoJson(resultJson,Contant.SHOP_LIST.get(i));
//
//		}
	}

	@Override
	public void getShopInfoById() {
		
	}
	
	

	
}
