package com.lnwoowken.lnwoowkenbook.view;

import com.lnwoowken.lnwoowkenbook.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class CalendarDialog extends Dialog {

    Context context;
    public CalendarDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
    }
    public CalendarDialog(Context context, int theme){
        super(context, theme);
        this.context = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_calendar_layout);
    }

}
