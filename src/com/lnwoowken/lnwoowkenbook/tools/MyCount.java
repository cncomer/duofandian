package com.lnwoowken.lnwoowkenbook.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lnwoowken.lnwoowkenbook.R;

public class MyCount extends CountDownTimer {  
	private Button bt;
	private TextView tv;

	public MyCount(long millisInFuture, long countDownInterval, View v) {
		super(millisInFuture, countDownInterval);
		tv = (TextView) v.findViewById(R.id.textView_count);
		bt = (Button) v.findViewById(R.id.button_commit);
	}

	@Override
	public void onTick(long millisUntilFinished) {
		Date date = new Date(millisUntilFinished);
		SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
		String str = sdf.format(date);
		str = str.substring(str.indexOf(":") + 1);
		System.out.println(str);
		tv.setText("倒计时" + str + "秒");
	}

	@Override
	public void onFinish() {
		tv.setText("结束");
		bt.setEnabled(false);
		bt.setBackgroundColor(Color.GRAY);
	}
}
