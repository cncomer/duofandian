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
		//��ȡSharedPreferences����Ҫ������
        preferences = getSharedPreferences("count",MODE_WORLD_READABLE);
        int count = preferences.getInt("count", 0);
        boolean b = false;
        //�жϳ�����ڼ������У�����ǵ�һ����������ת������ҳ��
        if (count == 0) {
            b = true;
        }
        
        Editor editor = preferences.edit();
        //��������
        editor.putInt("count", ++count);
        //�ύ�޸�
        editor.commit();
        return b;
	}
}
