package com.cncom.app.base.module;

import java.util.HashMap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.cncom.app.base.util.DebugUtils;
import com.lnwoowken.lnwoowkenbook.R;

public class ModuleSettings {

	private static final String TAG = "ModuleSettings";
	public static boolean MODULE_WEATHER_ENABLE = false;
	public static boolean MODULE_NOTE_ENABLE = false;
	public static boolean MODULE_PHOTO_ENABLE = false;
	public static boolean MODULE_CONTACT_MEMO_ENABLE = false;
	public static boolean MODULE_GPS_ENABLE = false;
	public static final int[] MODULE_IDS = new int[]{
		R.id.menu_duo_seat,
		R.id.menu_order_dishes,
		R.id.menu_my_order,
		R.id.menu_my_account,
		R.id.menu_pay_by_qr,
		R.id.menu_my_jifen,
		R.id.menu_settings,
		
	};
	public static final int MODULES_COUNT = MODULE_IDS.length;
	public static final int INDEX_MODULE_DUO_SEAT = 0;
	public static final int INDEX_MODULE_ORDER_DISHES = 1;
	public static final int INDEX_MODULE_MY_ORDER = 2;
	public static final int INDEX_MODULE_MY_ACCOUNT = 3;
	public static final int INDEX_MODULE_PAY_BY_QR = 4;
	public static final int INDEX_MODULE_MY_JIFEN = 5;
	public static final int INDEX_MODULE_SETTINGS = 6;
	
	public static final int[] MODULE_ICONS = new int[]{
		R.drawable.menu_duo_seat,
		R.drawable.menu_order_dishes,
		R.drawable.menu_my_order,
		R.drawable.menu_my_account,
		R.drawable.menu_pay_by_qr,
		R.drawable.menu_my_jifen,
		R.drawable.menu_settings,
	};
	
	public static final int[] MODULE_LABLES = new int[]{
		R.string.menu_duo_seat,
		R.string.menu_order_dishes,
		R.string.menu_my_order,
		R.string.menu_my_account,
		R.string.menu_pay_by_qr,
		R.string.menu_my_jifen,
		R.string.menu_settings,
		
	};
	
	private static HashMap<Integer, Module> mModuleMap = new HashMap<Integer, Module>(MODULES_COUNT);
	static {
		int count = MODULE_IDS.length;
		for(int index = 0 ; index < count; index++) {
			mModuleMap.put(MODULE_IDS[index], new Module(MODULE_LABLES[index], MODULE_ICONS[index], MODULE_IDS[index]));
		}
	}
	
	public static class Module {
		public static final int NoId = -1;
		public int mLabelResId = NoId;
		public int mIconResId = NoId;
		public int mId = NoId;

		public Module(int lableId, int iconId, int viewId) {
			mLabelResId = lableId;
			mIconResId = iconId;
			mId = viewId;
		}
		
	}
	
	public static ModuleSettings INSTANCE = new ModuleSettings();
	
	private Context mContext;
	
	
	private ModuleSettings() {}

	public static ModuleSettings getInstance() {
		return INSTANCE;
	}
	
	public void setContext(Context context) {
		mContext = context;
	}
	
	public void installModule(ViewGroup parentView, View.OnClickListener moduleClickListener) {
		
		DebugUtils.logD(TAG, "installModule [Duo Seat]");
		ModuleViewFactory.createSingleModuleViewWithIconAndLabel(parentView, 
				mModuleMap.get(MODULE_IDS[INDEX_MODULE_DUO_SEAT]))
				.setOnClickListener(moduleClickListener);
		
		DebugUtils.logD(TAG, "installModule [Order Dishes]");
		ModuleViewFactory.createSingleModuleViewWithIconAndLabel(parentView, 
				mModuleMap.get(MODULE_IDS[INDEX_MODULE_ORDER_DISHES]))
				.setOnClickListener(moduleClickListener);
		
		DebugUtils.logD(TAG, "installModule [My Order]");
		ModuleViewFactory.createSingleModuleViewWithIconAndLabel(parentView, 
				mModuleMap.get(MODULE_IDS[INDEX_MODULE_MY_ORDER]))
				.setOnClickListener(moduleClickListener);
		
		DebugUtils.logD(TAG, "installModule [My Acocunt]");
		ModuleViewFactory.createSingleModuleViewWithIconAndLabel(parentView, 
				mModuleMap.get(MODULE_IDS[INDEX_MODULE_MY_ACCOUNT]))
				.setOnClickListener(moduleClickListener);
		
		DebugUtils.logD(TAG, "installModule [Pay By QR]");
		ModuleViewFactory.createSingleModuleViewWithIconAndLabel(parentView, 
				mModuleMap.get(MODULE_IDS[INDEX_MODULE_PAY_BY_QR]))
				.setOnClickListener(moduleClickListener);
		
		DebugUtils.logD(TAG, "installModule [My JiFen]");
		ModuleViewFactory.createSingleModuleViewWithIconAndLabel(parentView, 
				mModuleMap.get(MODULE_IDS[INDEX_MODULE_MY_JIFEN]))
				.setOnClickListener(moduleClickListener);
		
		DebugUtils.logD(TAG, "installModule [Settings]");
		ModuleViewFactory.createSingleModuleViewWithIconAndLabel(parentView, 
				mModuleMap.get(MODULE_IDS[INDEX_MODULE_SETTINGS]))
				.setOnClickListener(moduleClickListener);
	}
	
	public void clearModule(ViewGroup parentView) {
		parentView.removeAllViews();
	}

}
