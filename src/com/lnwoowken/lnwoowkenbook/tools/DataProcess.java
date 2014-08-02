package com.lnwoowken.lnwoowkenbook.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import org.apache.commons.codec.binary.Base64;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


import android.util.Log;

public class DataProcess {

	public boolean saveObj(Context context,Object obj,String key) {  
	    SharedPreferences preferences = context.getSharedPreferences("base64",  
	            Activity.MODE_PRIVATE);  
	    // �����ֽ������  
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	    try {  
	        // �������������������װ�ֽ���  
	        ObjectOutputStream oos = new ObjectOutputStream(baos);  
	        // ������д���ֽ���  
	        oos.writeObject(obj);  
	        // ���ֽ��������base64���ַ���  
	        String oAuth_Base64 = new String(Base64.encodeBase64(baos  
	                .toByteArray()));  
	        Editor editor = preferences.edit();  
	        editor.putString(key, oAuth_Base64);  
	  
	        editor.commit();  
	        Log.i("ok", "�洢�ɹ�"); 
	        return true;
	    } catch (IOException e) {  
	        // TODO Auto-generated  
	    	Log.i("failed", "�洢ʧ��"); 
	    	return false;
	    }  
	     
	}  
	
	
	public Object getObj(Context context,String key) {  
		Object oAuth_1 = null;  
	    SharedPreferences preferences = context.getSharedPreferences("base64",  
	            Activity.MODE_PRIVATE);  
	    String productBase64 = preferences.getString(key, "");  
	              
	    //��ȡ�ֽ�  
	    byte[] base64 = Base64.decodeBase64(productBase64.getBytes());  
	      
	    //��װ���ֽ���  
	    ByteArrayInputStream bais = new ByteArrayInputStream(base64);  
	    try {  
	        //�ٴη�װ  
	        ObjectInputStream bis = new ObjectInputStream(bais);  
	        try {  
	            //��ȡ����  
	            oAuth_1 = (Object) bis.readObject();  
	        } catch (ClassNotFoundException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	    } catch (StreamCorruptedException e) {  
	        // TODO Auto-generated catch block  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        // TODO Auto-generated catch block  
	        e.printStackTrace();  
	    }  
	    return oAuth_1;  
	}  
}
