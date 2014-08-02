package com.lnwoowken.lnwoowkenbook.adapter;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

public class TestTimer extends TimePickerDialog {

	public TestTimer(Context context, int theme, OnTimeSetListener callBack,
			int hourOfDay, int minute, boolean is24HourView) {
		super(context, theme, callBack, hourOfDay, minute, is24HourView);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		super.onTimeChanged(view, hourOfDay, minute);
	}

}
