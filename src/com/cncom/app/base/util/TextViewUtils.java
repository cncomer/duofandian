package com.cncom.app.base.util;

import android.text.TextPaint;
import android.widget.TextView;

public class TextViewUtils {

	/**
	 * 设置粗体
	 * @param v
	 * @param text
	 */
	public static void setBoldText(TextView v, String text) {
		 TextPaint tp = v.getPaint();
         tp.setFakeBoldText(true);
         v.setText(text);
	}
	
	/**
	 * 设置粗体
	 * @param v
	 * @param text
	 */
	public static void setBoldText(TextView v) {
		 TextPaint tp = v.getPaint();
         tp.setFakeBoldText(true);
	}
}
