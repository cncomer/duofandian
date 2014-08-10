package com.lnwoowken.lnwoowkenbook.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.lnwoowken.lnwoowkenbook.R;

public class TimeDialog extends Dialog {
	Context context;
    public TimeDialog(Context context) {
        super(context);
        this.context = context;
    }
    public TimeDialog(Context context, int theme){
        super(context, theme);
        this.context = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dailog_timepicker);
    }
}
