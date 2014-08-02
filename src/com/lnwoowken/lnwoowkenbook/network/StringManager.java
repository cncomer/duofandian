package com.lnwoowken.lnwoowkenbook.network;

public class StringManager {

	public static boolean isJson(String res){
		return true;
	}
	
	public static String parseToNormalJson(String res){
		String temp = "";
		if (res.startsWith("\"")&&res.endsWith("\"")) {
			temp = temp.substring(1, temp.length()-1);
			temp = temp.replace("\\\"", "\"");
			return temp;
		}
		else {
			return temp;
		}
	}
	
}
