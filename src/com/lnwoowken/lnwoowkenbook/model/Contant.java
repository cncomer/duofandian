package com.lnwoowken.lnwoowkenbook.model;

import java.util.ArrayList;
import java.util.List;

/**
 * ��̬����
 * @author sean
 *
 */
public class Contant {
	public static String SDCARD;
	public static String WOOWKEN_DIR;
	public static int SCREEN_FLAG;
	public static float SCALE_WIDTH;
	public static int SCREEN_WIDTH_480 = 480;
	public static int MARGIN_LEFTRIGHT_480 = 34;
	//public static String SERVER_IP = "121.199.46.106";//--��������ַ
	//public static String SERVER_IP = "192.168.18.129";//--��������ַ
	public static String UPMPPAY = "Upmppay";
	public static String VERSION = "sversion";
	public static String VERSION_CODE = "1.3";
	public static String LISTBILL = "Rl2";
	public static String SERVER_IP = "manage.lnwoowken.com";//--��������ַ
	public static int SERVER_PORT =  80;//--�������˿�
	public static String JSON_STR;//--json�ַ���
	public static List<StoreInfo> SHOP_LIST = new ArrayList<StoreInfo>();//
	public static List<StoreInfo> SHOPID_LIST;//
	public static int ANIMITION_START_RESTUARANTLISTACITVITY = 1;
	public static int ANIMITION_START_REGISTACITVITY = 2;
	public static String NO_NET = "NONET";
	//public static List<StoreInfo> list_store;
	public static int SCREEN_WIDTH = 0;
	public static int SCREEN_HEIGHT = 0;
	public static int GALLERY_WIDTH = 0;
	public static int GALLERY_HEIGHT = 0;	
	public static int FLAG_GETALLSHOPINFO = 1;
	public static int FLAG_GETSHOPBYID = 2;
	public static int FLAG_REGIST = 3;
	public static int FLAG_GETSMS = 4;
	public static int FLAG_LOGIN = 5;
	public static boolean ISLOGIN = false;
	public static UserInfo USER = null;
	public static String DEFAULTSORT = "Ĭ������";
	public static String KOUWEI = "ȫ����ϵ";
	public static String ALL = "ȫ������";
	public static String AREA = "ȫ����Ȧ";
	public static List<ShopTree> SHOPTREE_LIST;
	
}
