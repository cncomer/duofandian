package com.lnwoowken.lnwoowkenbook.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.lnwoowken.lnwoowkenbook.R;

public class UserDialog extends Dialog {
    Context context;
    public UserDialog(Context context) {
        super(context);
        this.context = context;
    }
    public UserDialog(Context context, int theme){
        super(context, theme);
        this.context = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.user_dialog);
    }
}
