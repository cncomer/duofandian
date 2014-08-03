package com.lnwoowken.lnwoowkenbook.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

public class MyCount extends CountDownTimer{  
	  TextView tv;
    public MyCount(long millisInFuture, long countDownInterval,View v) {  
        super(millisInFuture, countDownInterval);  
        tv = (TextView) v;
    }  
    @Override  
    public void onTick(long millisUntilFinished) {  
        Date date = new Date(millisUntilFinished);  
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");  
        String str = sdf.format(date);  
        System.out.println(str);  
        tv.setText(str);  
    }  
    @Override  
    public void onFinish() {  
        tv.setText("finish");  
    }  
}
