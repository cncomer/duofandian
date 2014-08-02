package com.lnwoowken.lnwoowkenbook.tools;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;

import com.lnwoowken.lnwoowkenbook.model.Contant;
import com.lnwoowken.lnwoowkenbook.model.StoreInfo;
import com.lnwoowken.lnwoowkenbook.model.UserInfo;
import com.lnwoowken.lnwoowkenbook.network.Client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
@SuppressLint("SimpleDateFormat")
@SuppressWarnings("unused")
public class Tools {
	
	
	/** 
     * ��pxֵת��Ϊspֵ����֤���ִ�С���� 
     *  
     * @param pxValue 
     * @param fontScale 
     *            ��DisplayMetrics��������scaledDensity�� 
     * @return 
     */  
    public static int px2sp(Context context, float pxValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (pxValue / fontScale + 0.5f);  
    }  
  
    /** 
     * ��spֵת��Ϊpxֵ����֤���ִ�С���� 
     *  
     * @param spValue 
     * @param fontScale 
     *            ��DisplayMetrics��������scaledDensity�� 
     * @return 
     */  
    public static int sp2px(Context context, float spValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (spValue * fontScale + 0.5f);  
    } 

	public static long getRandomNum(){
		int max=99999;
        int min=10000;
        Random random = new Random();

        long result = random.nextInt(max)%(max-min+1) + min;
        return result;
	}
	
	
	 /** 
     * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * �����ֻ��ķֱ��ʴ� px(����) �ĵ�λ ת��Ϊ dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
    
    /**
     * ��ȡid������JSON�ַ���
     */
    public static String getParamValueStr(String paramValue){
    	String op = "{\"id\":\""+paramValue+"\",\"vd\":\"0\",\"vc\":\"0\"}";
    	return op;
    }
    
    /**
     * ��ȡ�����������URL�ַ���
     */
    public static String getRequestStr(String serverIp,String serverPort,String param,String paramValue,String operation){
    	String serverAddress = "http://" + serverIp + ":"+ serverPort + "/javadill/";
    	String params = param;
		String opid = Tools.getParamValueStr(paramValue);
		opid = Client.encodeBase64(opid);
		String operations = operation;
		operations = operation;
		String str = serverAddress+params+opid+operations;
    	return str;
    }
    
    
	public static boolean checkTime(String time,String start,String end){
    	Date now = new Date();
    	  Calendar cal1 = Calendar.getInstance();
    	  Calendar cal2 = Calendar.getInstance();
    	  Calendar cal3 = Calendar.getInstance();
    	  SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	  String time1 = start;
    	  String time2 = end;
    	  
    	  try {
    		cal1.setTime(sdf.parse(time));
			cal2.setTime(sdf.parse(time1));
			cal3.setTime(sdf.parse(time2));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	  
    	  if(cal2.before(cal1)&& cal3.after(cal1)){
    	    //��ʱ����ڡ�
    		  return true;
    	}
    	  return false;
    }
    
    public static Date stringToDate1(String time){
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//Сд��mm��ʾ���Ƿ���  
    	
    	java.util.Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return date;
    }
    
    public static String dateToString(Date date, String type) {  
        String str = null;  
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
        if (type.equals("SHORT")) {  
            // 07-1-18  
            format = DateFormat.getDateInstance(DateFormat.SHORT);  
            str = format.format(date);  
        } else if (type.equals("MEDIUM")) {  
            // 2007-1-18  
            format = DateFormat.getDateInstance(DateFormat.MEDIUM);  
            str = format.format(date);  
        } else if (type.equals("FULL")) {  
            // 2007��1��18�� ������  
            format = DateFormat.getDateInstance(DateFormat.FULL);  
            str = format.format(date);  
        }  
        return str;  
    }  
    
    public static StoreInfo findShopById(int id) {
		for (int i = 0; i < Contant.SHOP_LIST.size(); i++) {
			if (Contant.SHOP_LIST.get(i).getId() == id) {
				return Contant.SHOP_LIST.get(i);
			}
		}
		return null;
	}
    
    public static String getCurrentTime(){
    	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");       
		String date = sDateFormat.format(new java.util.Date());
		return date;
    }
    
    public static boolean checkSMS(String res,String pwd){
    	boolean b = false;
    	if (res.equals(pwd)) {
			b = true;
		}
    	else {
			b = false;
		}
    	return b;
    }
    
    
	public void saveOAuth(UserInfo user,Context context) {
		@SuppressWarnings("static-access")
		SharedPreferences preferences =context.getSharedPreferences("base64",
				context.MODE_PRIVATE);
		// �����ֽ������
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			// �������������������װ�ֽ���
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			// ������д���ֽ���
			oos.writeObject(user);
			// ���ֽ��������base64���ַ���
			String oAuth_Base64 = new String(Base64.encodeBase64(baos
					.toByteArray()));
			Editor editor = preferences.edit();
			editor.putString("oAuth_1", oAuth_Base64);

			editor.commit();
		} catch (IOException e) {
			// TODO Auto-generated
		}
		Log.i("ok", "�洢�ɹ�");
	}
	
	
	public UserInfo readOAuth(Context context) {
		UserInfo user = null;
		@SuppressWarnings("static-access")
		SharedPreferences preferences = context.getSharedPreferences("base64",
				context.MODE_PRIVATE);
		String productBase64 = preferences.getString("oAuth_1", "");
				
		//��ȡ�ֽ�
		byte[] base64 = Base64.decodeBase64(productBase64.getBytes());
		
		//��װ���ֽ���
		ByteArrayInputStream bais = new ByteArrayInputStream(base64);
		try {
			//�ٴη�װ
			ObjectInputStream bis = new ObjectInputStream(bais);
			try {
				//��ȡ����
				user = (UserInfo) bis.readObject();
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
		return user;
	}
	
	
	/**
     * Save Bitmap to a file.����ͼƬ��SD����
     * 
     * @param bitmap
     * @param file
     * @return error message if the saving is failed. null if the saving is
     *         successful.
     * @throws IOException
     */
    public static void saveBitmapToFile(Bitmap bitmap, String _file)
            throws IOException {
        BufferedOutputStream os = null;
        try {
            File file = new File(_file);
            // String _filePath_file.replace(File.separatorChar +
            // file.getName(), "");
            int end = _file.lastIndexOf(File.separator);
            String _filePath = _file.substring(0, end);
            File filePath = new File(_filePath);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            file.createNewFile();
            os = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    Log.e("error========", e.getMessage(), e);
                }
            }
        }
    }
    
    
    public static String getSDPath(){ 
        File sdDir = null; 
        boolean sdCardExist = Environment.getExternalStorageState()   
                            .equals(Environment.MEDIA_MOUNTED);   //�ж�sd���Ƿ���� 
        if   (sdCardExist)   
        {                               
          sdDir = Environment.getExternalStorageDirectory();//��ȡ��Ŀ¼ 
       }
        
        return sdDir.toString(); 
        
 }
}
