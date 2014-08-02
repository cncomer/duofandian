package com.lnwoowken.lnwoowkenbook.view;

import com.lnwoowken.lnwoowkenbook.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class TimeDialog extends Dialog {
	Context context;
    public TimeDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
    }
    public TimeDialog(Context context, int theme){
        super(context, theme);
        this.context = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dailog_timepicker);
    }
}
