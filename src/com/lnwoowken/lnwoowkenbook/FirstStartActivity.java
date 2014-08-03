package com.lnwoowken.lnwoowkenbook;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

public class FirstStartActivity extends Activity {
	
	private  SharedPreferences preferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_first);
		//initialize();
		if (checkFirst()) {
			
		}
		else {
			Intent intent = new Intent(FirstStartActivity.this, TestMain.class);
			startActivity(intent);
			FirstStartActivity.this.finish();
		}

	}
	
	private boolean checkFirst(){
		//读取SharedPreferences中需要的数据
        preferences = getSharedPreferences("count",MODE_WORLD_READABLE);
        int count = preferences.getInt("count", 0);
        boolean b = false;
        //判断程序与第几次运行，如果是第一次运行则跳转到引导页面
        if (count == 0) {
            b = true;
        }
        
        Editor editor = preferences.edit();
        //存入数据
        editor.putInt("count", ++count);
        //提交修改
        editor.commit();
        return b;
	}
}
