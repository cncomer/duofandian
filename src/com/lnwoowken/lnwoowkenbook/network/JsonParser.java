package com.lnwoowken.lnwoowkenbook.network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.annotation.SuppressLint;
import android.util.Log;

import com.lnwoowken.lnwoowkenbook.model.BillObject;
import com.lnwoowken.lnwoowkenbook.model.BookTime;
import com.lnwoowken.lnwoowkenbook.model.PayInfo;
import com.lnwoowken.lnwoowkenbook.model.ShopTree;
import com.lnwoowken.lnwoowkenbook.model.StoreInfo;
import com.lnwoowken.lnwoowkenbook.model.SurveyAnswer;
import com.lnwoowken.lnwoowkenbook.model.SurveyQuestion;
import com.lnwoowken.lnwoowkenbook.model.TableInfo;
import com.lnwoowken.lnwoowkenbook.model.TableStyle;
import com.lnwoowken.lnwoowkenbook.model.UserInfo;

public class JsonParser {
	/**
	 * 解析服务器发来的问卷调查的答案json数据
	 * 
	 * @param jsonData
	 *            json格式的字符串
	 */
	@SuppressLint("NewApi")
	public static List<SurveyAnswer> parseSurveyAnswerJson(String jsonData) {
		List<SurveyAnswer> tempList = new ArrayList<SurveyAnswer>();
		//if (checkError(jsonData)) {

	//	}
	//	else {
			if (jsonData.startsWith("\"") && jsonData.endsWith("\"")) {
				jsonData = jsonData.substring(1, jsonData.length() - 1);
				jsonData = jsonData.replace("\\\"", "\"");
			}
			if (jsonData.startsWith("[") && jsonData.endsWith("]")) {

				JSONArray arr;
				try {
					arr = new JSONArray(jsonData);
					for (int i = 0; i < arr.length(); i++) {
						JSONObject temp = (JSONObject) arr.get(i);
						int id = Integer.parseInt(temp.getString("Id"));
						String text = temp.getString("Text");
						int sid = Integer.parseInt(temp.getString("Qid"));
						// String name = temp.getString("version");
						SurveyAnswer answer = new SurveyAnswer(id, text, sid);
						
						tempList.add(answer);

						// Log.d("___________", tempList.get(i).getId()+"");

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {

				
			}
		//}

		return tempList;
	}
	
	/**
	 * 解析服务器发来的版本号
	 * 
	 * @param jsonData
	 *            json格式的字符串
	 */
	@SuppressLint("NewApi")
	public static String parseVersionCodeJson(String jsonData) {
		//UserInfo user = new UserInfo();
		String version = null;
		Log.d("shoplist___________", jsonData);
		if (jsonData.startsWith("\"") && jsonData.endsWith("\"")) {
			jsonData = jsonData.substring(1, jsonData.length() - 1);
			jsonData = jsonData.replace("\\\"", "\"");
		}
		if (jsonData.startsWith("[") && jsonData.endsWith("]")) {
			
			JSONArray arr;
			try {
				if (jsonData.contains("{")) {
					arr = new JSONArray(jsonData);
					for (int i = 0; i < arr.length(); i++) {
						JSONObject temp = (JSONObject) arr.get(i);
						// String id = temp.getString("Id");
						version =temp.getString("settingvalue");
					}
				}
				else {
					jsonData = jsonData.replace("[", "");
					jsonData = jsonData.replace("]", "");
					String[] arrStr = jsonData.split(":");
					if (arrStr.length>0) {
						version = arrStr[1].replace("\"", "");
						Log.d("parseVersionCodeJson=========", version);
					}
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}

		} else {

			try {
				//String payId;
				JSONObject temp = new JSONObject(jsonData);
				version =temp.getString("version");
				// tempList.add(shop);
				// Log.d("___________", tempList.get(0).getId()+"");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return version;
	}
	
	
	/**
	 * 解析服务器发来的支付号
	 * 
	 * @param jsonData
	 *            json格式的字符串
	 */
	@SuppressLint("NewApi")
	public static String parseTradeNumberJson(String jsonData) {
		//UserInfo user = new UserInfo();
		String tn = null;
		Log.d("shoplist___________", jsonData);
		if (jsonData.startsWith("\"") && jsonData.endsWith("\"")) {
			jsonData = jsonData.substring(1, jsonData.length() - 1);
			jsonData = jsonData.replace("\\\"", "\"");
		}
		if (jsonData.startsWith("[") && jsonData.endsWith("]")) {
			
			JSONArray arr;
			try {
				arr = new JSONArray(jsonData);
				
				for (int i = 0; i < arr.length(); i++) {
					JSONObject temp = (JSONObject) arr.get(i);
					// String id = temp.getString("Id");
					tn =temp.getString("tn");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {

			try {
				//String payId;
				JSONObject temp = new JSONObject(jsonData);
				tn =temp.getString("tn");
				// tempList.add(shop);
				// Log.d("___________", tempList.get(0).getId()+"");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tn;
	}
	
	
	/**
	 * 解析服务器发来的支付号
	 * 
	 * @param jsonData
	 *            json格式的字符串
	 */
	@SuppressLint("NewApi")
	public static String parseTreeShopJson(String jsonData) {
		//UserInfo user = new UserInfo();
		String tid = null;
		Log.d("shoplist___________", jsonData);
		if (jsonData.startsWith("\"") && jsonData.endsWith("\"")) {
			jsonData = jsonData.substring(1, jsonData.length() - 1);
			jsonData = jsonData.replace("\\\"", "\"");
		}
		if (jsonData.startsWith("[") && jsonData.endsWith("]")) {
			
			JSONArray arr;
			try {
				arr = new JSONArray(jsonData);
				
				
				for (int i = 0; i < arr.length(); i++) {
					JSONObject temp = (JSONObject) arr.get(i);
					// String id = temp.getString("Id");
					tid =temp.getString("Tid");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {

//			try {
//				//String payId;
//				JSONObject temp = new JSONObject(jsonData);
//				tn =temp.getString("tn");
//				// tempList.add(shop);
//				// Log.d("___________", tempList.get(0).getId()+"");
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		return tid;
	}
	
	
	/**
	 * 解析服务器发来的支付信息
	 * 
	 * @param jsonData
	 *            json格式的字符串
	 */
	@SuppressLint("NewApi")
	public static String parsePayNumberJson(String jsonData) {
		//UserInfo user = new UserInfo();
		String payId = null;
		Log.d("shoplist___________", jsonData);
		if (jsonData.startsWith("\"") && jsonData.endsWith("\"")) {
			jsonData = jsonData.substring(1, jsonData.length() - 1);
			jsonData = jsonData.replace("\\\"", "\"");
		}
		if (jsonData.startsWith("[") && jsonData.endsWith("]")) {
			
			JSONArray arr;
			try {
				arr = new JSONArray(jsonData);
				
				for (int i = 0; i < arr.length(); i++) {
					JSONObject temp = (JSONObject) arr.get(i);
					// String id = temp.getString("Id");
					payId =temp.getString("PayId");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {

			try {
				//String payId;
				JSONObject temp = new JSONObject(jsonData);
				payId =temp.getString("Id");
				// tempList.add(shop);
				// Log.d("___________", tempList.get(0).getId()+"");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return payId;
	}
	
	
	/**
	 * 解析服务器发来的用户信息的json数据
	 * 
	 * @param jsonData
	 *            json格式的字符串
	 */
	@SuppressLint("NewApi")
	public static UserInfo parseUserInfoJson(String jsonData) {
		UserInfo user = new UserInfo();
		Log.d("shoplist___________", jsonData);
		if (jsonData.startsWith("\"") && jsonData.endsWith("\"")) {
			jsonData = jsonData.substring(1, jsonData.length() - 1);
			jsonData = jsonData.replace("\\\"", "\"");
		}
		if (jsonData.startsWith("[") && jsonData.endsWith("]")) {

			JSONArray arr;
			try {
				arr = new JSONArray(jsonData);
				
				
				for (int i = 0; i < arr.length(); i++) {
					JSONObject temp = (JSONObject) arr.get(i);
					// String id = temp.getString("Id");
					user.setId(Integer.parseInt(temp.getString("Id")));
					user.setSid(temp.getString("Sid"));
					user.setUserName(temp.getString("User"));
					user.setPwd(temp.getString("Pwd"));
					user.setName(temp.getString("Name"));
					user.setPhoneNum(temp.getString("PhNo"));
					user.seteMail(temp.getString("E-mail"));
					user.setLogin(temp.getString("Login"));
					user.setCreateDate(temp.getString("Create"));
					user.setState(temp.getString("State"));
					user.setRights(temp.getString("Rights"));
					user.setVersion(temp.getString("Version"));
					user.setCu(temp.getString("Cu"));
					user.setErrorconn(temp.getString("errorconn"));
					user.setErrorlogin(temp.getString("errorlogin"));
					user.setLastlogin(temp.getString("lastlogin"));
					user.setVcode(temp.getString("vcode"));
					
					// tempList.add(shop);

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {

			try {
				JSONObject temp = new JSONObject(jsonData);
				user.setId(Integer.parseInt(temp.getString("Id")));
				user.setSid(temp.getString("Sid"));
				user.setUserName(temp.getString("User"));
				user.setPwd(temp.getString("Pwd"));
				user.setName(temp.getString("Name"));
				user.setPhoneNum(temp.getString("PhNo"));
				user.seteMail(temp.getString("E-mail"));
				user.setLogin(temp.getString("Login"));
				user.setCreateDate(temp.getString("Create"));
				user.setState(temp.getString("State"));
				user.setRights(temp.getString("Rights"));
				user.setVersion(temp.getString("Version"));
				user.setCu(temp.getString("Cu"));
				user.setErrorconn(temp.getString("errorconn"));
				user.setErrorlogin(temp.getString("errorlogin"));
				user.setLastlogin(temp.getString("lastlogin"));
				user.setVcode(temp.getString("vcode"));
				// tempList.add(shop);
				// Log.d("___________", tempList.get(0).getId()+"");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return user;
	}
	
	
	/**
	 * 解析服务器发来的分类列表的json数据
	 * 
	 * @param jsonData
	 *            json格式的字符串
	 */
	@SuppressLint("NewApi")
	public static List<ShopTree> parseShopTreeJson(String jsonData) {
		List<ShopTree> tempList = new ArrayList<ShopTree>();
		//if (checkError(jsonData)) {

	//	}
	//	else {
			if (jsonData.startsWith("\"") && jsonData.endsWith("\"")) {
				jsonData = jsonData.substring(1, jsonData.length() - 1);
				jsonData = jsonData.replace("\\\"", "\"");
			}
			if (jsonData.startsWith("[") && jsonData.endsWith("]")) {

				JSONArray arr;
				try {
					arr = new JSONArray(jsonData);
					for (int i = 0; i < arr.length(); i++) {
						JSONObject temp = (JSONObject) arr.get(i);
						String id = temp.getString("ID");
						String name = temp.getString("Name").replace("\n","");
						// String name = temp.getString("version");
						ShopTree shop = new ShopTree();
						shop.setId(Integer.parseInt(id));
						shop.setName(name);
						tempList.add(shop);

						// Log.d("___________", tempList.get(i).getId()+"");

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {

				try {
					JSONObject temp = new JSONObject(jsonData);
					String id = temp.getString("ID");
					String name = temp.getString("Name");
					// String name = temp.getString("version");
					ShopTree shop = new ShopTree();
					shop.setId(Integer.parseInt(id));
					shop.setName(name);
					tempList.add(shop);
					// Log.d("___________", tempList.get(0).getId()+"");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		//}

		return tempList;
	}
	

	/**
	 * 解析服务器发来的所有商铺ID的json数据
	 * 
	 * @param jsonData
	 *            json格式的字符串
	 */
	@SuppressLint("NewApi")
	public static List<StoreInfo> parseShopIdJson(String jsonData) {
		List<StoreInfo> tempList = new ArrayList<StoreInfo>();
		//if (checkError(jsonData)) {

	//	}
	//	else {
			if (jsonData.startsWith("\"") && jsonData.endsWith("\"")) {
				jsonData = jsonData.substring(1, jsonData.length() - 1);
				jsonData = jsonData.replace("\\\"", "\"");
			}
			if (jsonData.startsWith("[") && jsonData.endsWith("]")) {

				JSONArray arr;
				try {
					arr = new JSONArray(jsonData);
					for (int i = 0; i < arr.length(); i++) {
						JSONObject temp = (JSONObject) arr.get(i);
						String id = temp.getString("ID");
						// String name = temp.getString("version");
						StoreInfo shop = new StoreInfo();
						shop.setId(Integer.parseInt(id));
						tempList.add(shop);

						// Log.d("___________", tempList.get(i).getId()+"");

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {

				try {
					JSONObject temp = new JSONObject(jsonData);
					StoreInfo shop = new StoreInfo();
					shop.setId(Integer.parseInt(temp.getString("ID")));
					tempList.add(shop);
					// Log.d("___________", tempList.get(0).getId()+"");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		//}

		return tempList;
	}
	
	
	public static List<TableStyle> parseTableStylefo(String jsonData) {
		List<TableStyle> list = new ArrayList<TableStyle>();
		if (jsonData.startsWith("\"") && jsonData.endsWith("\"")) {
			jsonData = jsonData.substring(1, jsonData.length() - 1);
			jsonData = jsonData.replace("\\\"", "\"");
		}
		if (jsonData.startsWith("[") && jsonData.endsWith("]")) {

			JSONArray arr;
			try {
				arr = new JSONArray(jsonData);
				for (int i = 0; i < arr.length(); i++) {
					JSONObject temp = (JSONObject) arr.get(i);
					TableStyle tempStyle = new TableStyle();
					tempStyle.setPrice(Float.parseFloat( temp.getString("Price")));
					tempStyle.setStyleName(temp.getString("TypeName"));
					tempStyle.setId(Integer.parseInt(temp.getString("Id")));
					tempStyle.setSid(Integer.parseInt(temp.getString("Sid")));
					tempStyle.setMembership(Integer.parseInt(temp.getString("Membership")));
					tempStyle.setSmallNum(temp.getString("Smallnum"));
					String tempCount = temp.getString("Count");
					if (tempCount==null) {
						tempCount = "0";
					}
					tempStyle.setCount(tempCount);
					list.add(tempStyle);
					// String id = temp.getString("Id");
					// tempList.add(shop);
					Log.d("parseTableStylefo___________", tempStyle.getStyleName());
//					Log.d("parseTimeInfo___________", tempTime.getRsTime());
//					Log.d("parseTimeInfo___________", tempTime.getPrice());
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {

		}
		return list;
	}
	
	/**
	 * 解析服务器发来的订单列表的json数据
	 * 
	 * @param jsonData
	 *            json格式的字符串
	 */
	@SuppressLint("NewApi")
	public static List<BillObject> parseBillListJson(String jsonData) {
		List<BillObject> tempList = new ArrayList<BillObject>();
			if (jsonData.startsWith("\"") && jsonData.endsWith("\"")) {
				jsonData = jsonData.substring(1, jsonData.length() - 1);
				jsonData = jsonData.replace("\\\"", "\"");
			}
			if (jsonData.startsWith("[") && jsonData.endsWith("]")) {

				JSONArray arr;
				try {
					arr = new JSONArray(jsonData);
					for (int i = 0; i < arr.length(); i++) {
						JSONObject temp = (JSONObject) arr.get(i);
						String id = temp.getString("Id");
						String uid = temp.getString("UId");
						String state = temp.getString("state");
						String sid = temp.getString("SId");
						String tid = temp.getString("TId");
						String peopleNum = temp.getString("PeopleNum");
						String rCode = temp.getString("RCode");
						String createT = temp.getString("CreateT");
						String mac = temp.getString("Mac");
						String ip = temp.getString("Ip");
						String phone = temp.getString("phone");
						String version = temp.getString("version");
						String shopname = temp.getString("shopname");
						String tablename = temp.getString("tablename");
						BillObject bill = new BillObject();
						bill.setId(id);
						bill.setIp(ip);
						bill.setMac(mac);
						bill.setPeopleNum(peopleNum);
						bill.setPhone(phone);
						bill.setRcode(rCode);
						bill.setSid(sid);
						bill.setTid(tid);
						bill.setUid(uid);
						bill.setVersion(version);
						bill.setShopName(shopname);
						bill.setState(Integer.parseInt(state));
						bill.setCreateTime(createT);
						bill.setState(Integer.parseInt(state));
						bill.setTableName(tablename);
						tempList.add(bill);
						// Log.d("___________", tempList.get(i).getId()+"");

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {

				try {
					JSONObject temp = new JSONObject(jsonData);
					String id = temp.getString("Id");
					String uid = temp.getString("UId");
					String state = temp.getString("state");
					String sid = temp.getString("SId");
					String tid = temp.getString("TId");
					String peopleNum = temp.getString("PeopleNum");
					String rCode = temp.getString("RCode");
					String createT = temp.getString("CreateT");
					String mac = temp.getString("Mac");
					String ip = temp.getString("Ip");
					String phone = temp.getString("phone");
					String version = temp.getString("version");
					String shopname = temp.getString("shopname");
					String tablename = temp.getString("tablename");
					BillObject bill = new BillObject();
					bill.setId(id);
					bill.setIp(ip);
					bill.setMac(mac);
					bill.setPeopleNum(peopleNum);
					bill.setPhone(phone);
					bill.setRcode(rCode);
					bill.setSid(sid);
					bill.setTid(tid);
					bill.setUid(uid);
					bill.setVersion(version);
					bill.setShopName(shopname);
					bill.setState(Integer.parseInt(state));
					bill.setCreateTime(createT);
					bill.setState(Integer.parseInt(state));
					bill.setTableName(tablename);
					tempList.add(bill);
				// Log.d("___________", tempList.get(0).getId()+"");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		//}

		return tempList;
	}
	
	
	public static List<BookTime> parseTimeInfo(String jsonData) {
		List<BookTime> list = new ArrayList<BookTime>();
		if (jsonData.startsWith("\"") && jsonData.endsWith("\"")) {
			jsonData = jsonData.substring(1, jsonData.length() - 1);
			jsonData = jsonData.replace("\\\"", "\"");
		}
		if (jsonData.startsWith("[") && jsonData.endsWith("]")) {

			JSONArray arr;
			try {
				arr = new JSONArray(jsonData);
				for (int i = 0; i < arr.length(); i++) {
					JSONObject temp = (JSONObject) arr.get(i);
					BookTime tempTime = new BookTime();
					//Log.d("parseTimeInfo==============", temp.getString("RSTime"));
					tempTime.setRsTime(getTimeFromDate(temp.getString("RSTime")));
					tempTime.setRdTime(getTimeFromDate(temp.getString("RDTime")));
					tempTime.setPrice(temp.getString("price"));
					tempTime.setPeriod(getTimeFromDate(temp.getString("period")));
					tempTime.setRstart(getTimeFromDate(temp.getString("Rstart")));
					tempTime.setRend(getTimeFromDate(temp.getString("Rend")));
					tempTime.setTimeName(temp.getString("TimeName"));
					tempTime.setId(Integer.parseInt(temp.getString("Id")));
					tempTime.setSid(Integer.parseInt(temp.getString("SId")));
					list.add(tempTime);
					// String id = temp.getString("Id");
					// tempList.add(shop);
					Log.d("parseTimeInfo___________", tempTime.getRdTime());
					Log.d("parseTimeInfo___________", tempTime.getRsTime());
					Log.d("parseTimeInfo___________", tempTime.getPrice());
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else {

			try {
				JSONObject temp = new JSONObject(jsonData);

				BookTime tempTime = new BookTime();
				tempTime.setRsTime(temp.getString("RSTime"));
				tempTime.setRdTime(temp.getString("RDTime"));
				tempTime.setPrice(temp.getString("price"));
				list.add(tempTime);
				// String id = temp.getString("Id");
				// tempList.add(shop);
				Log.d("___________", tempTime.getRdTime());
				Log.d("___________", tempTime.getRsTime());
				Log.d("___________", tempTime.getPrice());
				// tempList.add(shop);
				// Log.d("___________", tempList.get(0).getId()+"");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public static void parseTableNumberOfShop(String jsonData, StoreInfo tempShop){
	//	Log.d("shoplist___________", jsonData);
		if (jsonData.startsWith("\"") && jsonData.endsWith("\"")) {
			jsonData = jsonData.substring(1, jsonData.length() - 1);
			jsonData = jsonData.replace("\\\"", "\"");
		}
		if (jsonData.startsWith("[") && jsonData.endsWith("]")) {

			JSONArray arr;
			try {
				arr = new JSONArray(jsonData);
				for (int i = 0; i < arr.length(); i++) {
					JSONObject temp = (JSONObject) arr.get(i);
					// String id = temp.getString("Id");
					tempShop.setTableNum(Integer.parseInt(temp.getString("Rtotal")));

					// tempList.add(shop);

					

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {

//			try {
////				JSONObject temp = new JSONObject(jsonData);
////				tempShop.setId(Integer.parseInt(temp.getString("Id")));
////				tempShop.setName(temp.getString("Name"));
////				tempShop.setInfo(temp.getString("ShopIntr"));
////				tempShop.setTableImagePath(temp.getString("PicMain"));
////				tempShop.setPriceLevel(temp.getString("priceLevel"));
////				tempShop.setEnvironmentLevel(temp.getString("environmentLevel"));
////				tempShop.setFlavorLevel(temp.getString("flavorLevel"));
////				tempShop.setAddress(temp.getString("address"));
////				tempShop.setPhoneNumber(temp.getString("phoneNumber"));
////				tempShop.setAveragePrice(temp.getString("averagePrice"));
//				// tempList.add(shop);
//				// Log.d("___________", tempList.get(0).getId()+"");
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}

	public static StoreInfo parseShopInfoJson(String jsonData, StoreInfo tempShop) {
		// StoreInfo tempShop = null;
		Log.d("shoplist___________", jsonData);
		if (jsonData.startsWith("\"") && jsonData.endsWith("\"")) {
			jsonData = jsonData.substring(1, jsonData.length() - 1);
			jsonData = jsonData.replace("\\\"", "\"");
		}
		if (jsonData.startsWith("[") && jsonData.endsWith("]")) {

			JSONArray arr;
			try {
				arr = new JSONArray(jsonData);
				for (int i = 0; i < arr.length(); i++) {
					JSONObject temp = (JSONObject) arr.get(i);
					// String id = temp.getString("Id");
					tempShop.setId(Integer.parseInt(temp.getString("ShopID")));
					String name = temp.getString("ShopName ");
					if (name.contains("\n")) {
						tempShop.setName(name.replace("\n",""));
					}
					else {
						tempShop.setName(name);
					}
					
					tempShop.setInfo(temp.getString("shop_brief"));
					tempShop.setTableImagePath(temp.getString("qiang_wei_img"));
					tempShop.setPriceLevel(temp.getString("priceLevel"));
//					tempShop.setEnvironmentLevel(temp
//							.getString("environmentLevel"));
//					tempShop.setFlavorLevel(temp.getString("flavorLevel"));
					//String address = temp.getString("address");
					//if (address.contains("\n")) {
						//address = address.replace("\n", "");
					//}
					//tempShop.setAddress(address);
					//tempShop.setPhoneNumber(temp.getString("contacts_phone"));
					//tempShop.setAveragePrice(temp.getString("averagePrice"));
					//tempShop.setServicePrice(temp.getString("Serverprice"));
			//		tempShop.setIcon(temp.getString("icon"));
					//tempShop.setImagePath(temp.getString("shop_img"));
					
					//String s = temp.getString("Create");

					// tempList.add(shop);

					//Log.d("___________", s);

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else {

			try {
				JSONObject temp = new JSONObject(jsonData);
				tempShop.setId(Integer.parseInt(temp.getString("ShopID")));
				String name = temp.getString("ShopName");
				if (name.contains("\n")) {
					tempShop.setName(name.replace("\n",""));
				}
				else {
					tempShop.setName(name);
				}
				
				tempShop.setInfo(temp.getString("shop_brief"));
				//tempShop.setTableImagePath(temp.getString("qiang_wei_img"));
				//tempShop.setPriceLevel(temp.getString("priceLevel"));
//				tempShop.setEnvironmentLevel(temp
//						.getString("environmentLevel"));
//				tempShop.setFlavorLevel(temp.getString("flavorLevel"));
				/*String address = temp.getString("address");
				if (address.contains("\n")) {
					address = address.replace("\n", "");
				}
				tempShop.setAddress(address);
				tempShop.setPhoneNumber(temp.getString("contacts_phone"));
				tempShop.setAveragePrice(temp.getString("averagePrice"));
				tempShop.setServicePrice(temp.getString("Serverprice"));
		//		tempShop.setIcon(temp.getString("icon"));
				tempShop.setImagePath(temp.getString("shop_img"));*/
				// tempList.add(shop);
				// Log.d("___________", tempList.get(0).getId()+"");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		 return tempShop;
	}

	public static List<TableInfo> parseTableInfoJson(String jsonData) {
		// StoreInfo tempShop = null;
		List<TableInfo> tempList;
		tempList = new ArrayList<TableInfo>();
		if (jsonData.startsWith("\"") && jsonData.endsWith("\"")) {
			jsonData = jsonData.substring(1, jsonData.length() - 1);
			jsonData = jsonData.replace("\\\"", "\"");
		}
		if (jsonData.startsWith("[") && jsonData.endsWith("]")) {
			Log.d("table___________________________", jsonData);

			JSONArray arr;
			try {
				arr = new JSONArray(jsonData);
				for (int i = 0; i < arr.length(); i++) {
					JSONObject temp = (JSONObject) arr.get(i);
					// String id = temp.getString("Id");
					TableInfo tempTable = new TableInfo();
					tempTable.setAId(temp.getString("aid"));
					tempTable.setTableName(temp.getString("aname"));
					tempTable.setStaRes(temp.getString("staRes"));
					tempTable.setStid(temp.getString("stid"));
					tempTable.setTableStyle(temp.getString("dtypeid"));
				//	tempTable.setPrice(temp.getString("price"));
					tempTable.setRt(temp.getString("rt"));
					//tempTable.setTnid(temp.getString("tntid"));
					tempTable.setrCode(temp.getString("RCode"));
					tempTable.setPhone(temp.getString("Phone"));
					tempTable.setPeopleNum(temp.getString("PeopleNum"));
					tempTable.setTableId(temp.getString("TId"));
					// tempShop.setTableImagePath(temp.getString("PicMain"));
					// tempShop.setPriceLevel(temp.getString("priceLevel"));
					// tempShop.setEnvironmentLevel(temp.getString("environmentLevel"));
					// tempShop.setFlavorLevel(temp.getString("flavorLevel"));
					// tempShop.setAddress(temp.getString("address"));
					// tempShop.setPhoneNumber(temp.getString("phoneNumber"));
					// tempShop.setAveragePrice(temp.getString("averagePrice"));
					// String s = temp.getString("Create");
//					int total = -1;
//					if (temp.getString("total") != null
//							&& !temp.getString("total").equals("")) {
//						total = Integer.parseInt(temp.getString("total"));
//					}
//					if (total >= 0 && total <= 1) {
//						tempList.add(tempTable);
//					}
					tempList.add(tempTable);
					// Log.d("___________", s);

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else {

			try {
				JSONObject temp = new JSONObject(jsonData);
				TableInfo tempTable = new TableInfo();
				tempTable.setAId(temp.getString("aid"));
				tempTable.setTableName(temp.getString("aname"));
				tempTable.setStaRes(temp.getString("staRes"));
				tempTable.setStid(temp.getString("stid"));
				tempTable.setTableStyle(temp.getString("dtypeid"));
			//	tempTable.setPrice(temp.getString("price"));
				tempTable.setRt(temp.getString("rt"));
			//	tempTable.setTnid(temp.getString("tntid"));
				tempTable.setrCode(temp.getString("RCode"));
				tempTable.setPhone(temp.getString("Phone"));
				tempTable.setPeopleNum(temp.getString("PeopleNum"));
				tempTable.setTableId(temp.getString("TId"));
				tempList.add(tempTable);
				// Log.d("___________", tempList.get(0).getId()+"");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return tempList;
	}

	public static List<PayInfo> parsePayInfoJson(String jsonData) {
		List<PayInfo> tempList;
		tempList = new ArrayList<PayInfo>();
		if (jsonData.startsWith("\"") && jsonData.endsWith("\"")) {
			jsonData = jsonData.substring(1, jsonData.length() - 1);
			jsonData = jsonData.replace("\\\"", "\"");
		}
		if (jsonData.startsWith("[") && jsonData.endsWith("]")) {

			JSONArray arr;
			try {
				arr = new JSONArray(jsonData);
				for (int i = 0; i < arr.length(); i++) {
					JSONObject temp = (JSONObject) arr.get(i);
					// String id = temp.getString("Id");
					PayInfo pay = new PayInfo();
					pay.setId(temp.getString("id"));
					pay.setOrder(temp.getString("order"));
					pay.settNumber(temp.getString("tno"));
					// tempShop.setTableImagePath(temp.getString("PicMain"));
					// tempShop.setPriceLevel(temp.getString("priceLevel"));
					// tempShop.setEnvironmentLevel(temp.getString("environmentLevel"));
					// tempShop.setFlavorLevel(temp.getString("flavorLevel"));
					// tempShop.setAddress(temp.getString("address"));
					// tempShop.setPhoneNumber(temp.getString("phoneNumber"));
					// tempShop.setAveragePrice(temp.getString("averagePrice"));
					// String s = temp.getString("Create");

					tempList.add(pay);
					// Log.d("___________", s);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else {

			try {
				JSONObject temp = new JSONObject(jsonData);
				PayInfo tempTable = new PayInfo();
				tempTable.setId(temp.getString("id"));
				tempTable.setOrder(temp.getString("order"));
				tempTable.settNumber(temp.getString("tno"));
				tempList.add(tempTable);
				// Log.d("___________", tempList.get(0).getId()+"");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return tempList;
	}
	
	/**
	 * ERROR
	 */
	public static boolean checkError(String res) {
		boolean isError = false;
		String temp = res;
		temp = temp.toLowerCase();
		if (res.contains("\"error\"")) {
			isError = true;
		}
		return isError;
	}
	
	private static String getTimeFromDate(String res){
		String temp = "";
		if (res!=null&&!res.equals("")) {
			temp = res.split(" ")[1];
		}
		
		return temp;
	}
}
