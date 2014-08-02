/**
 * 请求服务器的线程
 * 
 */
package com.lnwoowken.lnwoowkenbook.thread;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lnwoowken.lnwoowkenbook.adapter.AllStoreListAdapter;
import com.lnwoowken.lnwoowkenbook.lnwoowkeninterface.RequestServerInterface;
import com.lnwoowken.lnwoowkenbook.model.Contant;
import com.lnwoowken.lnwoowkenbook.model.StoreInfo;
import com.lnwoowken.lnwoowkenbook.network.Client;
import com.lnwoowken.lnwoowkenbook.network.JsonParser;
import com.lnwoowken.lnwoowkenbook.tools.Tools;

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
	 * 通过id获取指定餐厅信息
	 * 
	 * @author sean
	 */
	@Override
	public void getShopInfoById() {
		// TODO Auto-generated method stub
		if (Contant.SHOP_LIST != null) {
			String time = Tools.getCurrentTime();
			
			for (int i = 0; i < Contant.SHOPID_LIST.size(); i++) {
				String resultJson;
				String opJson = Contant.SHOPID_LIST.get(i).getId()+"";
				StoreInfo tempShop = new StoreInfo();
				tempShop.setId(Contant.SHOPID_LIST.get(i).getId());
				//opJson = Client.encodeBase64(opJson);
				// String url =
				// "http://"+Contant.SERVER_IP+":"+Contant.SERVER_PORT+"/javadill/shop?id=s7&op="+opJson;
				// Log.d("url______________", url);
				String str ="http://"+Contant.SERVER_IP+":"+Contant.SERVER_PORT+"/Mobile/GetShopDetailByID.ashx?shopid="+opJson; 
//						
//						Tools.getRequestStr(Contant.SERVER_IP,
//						Contant.SERVER_PORT + "", "shop?id=", "s7", "&op="
//								+ opJson);

				
				
				resultJson = Client
						.executeHttpGetAndCheckNet(str, this.context);
				if (resultJson==null) {
					Message msg = new Message();
					msg.arg1 = 1;
					this.handler.sendMessage(msg);
					return;
				}
				if (resultJson.equals(Contant.NO_NET)) {
					Message msg = new Message();
					msg.arg1 = 1;
					this.handler.sendMessage(msg);
					return;
				} else {
					//resultJson = Client.decodeBase64(resultJson);

					if (resultJson != null) {

						JsonParser.parseShopInfoJson(resultJson,
								tempShop);

					}
					
					/**
					 * 获取今日还剩多少桌
					 */
					/*Contant.SHOP_LIST.add(tempShop);
					String resultJson_tableNum;
					String opJson_tableNum = "{\"sid\":\""
							+ Contant.SHOP_LIST.get(i).getId() + "\",\"rt\":\""
							+ time + "\"}";
					opJson_tableNum = Client.encodeBase64(opJson_tableNum);
					String str_tableNum = Tools.getRequestStr(Contant.SERVER_IP,
							Contant.SERVER_PORT + "", "shopTable?id=", "st8",
							"&op=" + opJson_tableNum);

					resultJson_tableNum = Client
							.executeHttpGetAndCheckNet(str_tableNum, this.context);

					resultJson_tableNum = Client.decodeBase64(resultJson_tableNum);

					if (resultJson_tableNum != null) {
						JsonParser.parseTableNumberOfShop(resultJson_tableNum,
								Contant.SHOP_LIST.get(i));
						Log.d("tableNumber=============", resultJson_tableNum);
						// JsonParser.parseShopInfoJson(resultJson,Contant.SHOP_LIST.get(i));

					}*/
					
					
					Message msg1 = new Message();
					this.listDataChanged_handler.sendMessage(msg1);
					try {
						this.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//adapter.notifyDataSetChanged();
					
				}
				
				


			}
			Message msg = new Message();
			this.handler.sendMessage(msg);
		}
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
		// TODO Auto-generated method stub
		String temp = Client.executeHttpGetAndCheckNet(this.url, context);
		this.result = Client.decodeBase64(temp);
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
		// TODO Auto-generated method stub
		switch (this.flag) {
		case 1:
			getAllShopInfo();
			break;
		case 2:
			getShopInfoById();
			break;
		case 3:
			registUserInfo();
			break;
		case 4:
			getSMS();
			break;

		case 5:
			login();
			break;
		case 6:
			getTables();
			break;
		case 7:
			getVersion();
			break;
		default:
			break;
		}
	}

	private void getTables() {
		if (Contant.SHOP_LIST != null) {
			String time = Tools.getCurrentTime();
			for (int i = 0; i < Contant.SHOP_LIST.size(); i++) {
				String resultJson;
				String opJson = "{\"sid\":\""
						+ Contant.SHOP_LIST.get(i).getId() + "\",\"rt\":\""
						+ time + "\"}";
				opJson = Client.encodeBase64(opJson);
				String str = Tools.getRequestStr(Contant.SERVER_IP,
						Contant.SERVER_PORT + "", "shopTable?id=", "st5",
						"&op=" + opJson);
				resultJson = Client.executeHttpGetAndCheckNet(str, this.context);
				resultJson = Client.decodeBase64(resultJson);

				if (resultJson != null) {

					Log.d("tableNumber=============", resultJson);
					// JsonParser.parseShopInfoJson(resultJson,Contant.SHOP_LIST.get(i));

				}

			}
			Message msg = new Message();
			this.handler.sendMessage(msg);
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
	
	

	
}
