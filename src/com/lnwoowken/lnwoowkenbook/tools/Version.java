package com.lnwoowken.lnwoowkenbook.tools;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class Version {

	public static int getVersionCode(Context context) {
		int verCode = -1;

		try {

			verCode = context.getPackageManager().getPackageInfo(

			"com.lnwoowken.lnwoowkenbook", 0).versionCode;

		} catch (NameNotFoundException e) {

			Log.e("getVersionCode_NameNotFoundException", e.getMessage());

		}

		return verCode;

	}
	
	public static String getVersion(Context context){
		String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(
                    "com.lnwoowken.lnwoowkenbook", 0).versionName;
        } catch (NameNotFoundException e) {
            Log.e("getVersion_NameNotFoundException", e.getMessage());
        }
        return verName; 
	}
}
