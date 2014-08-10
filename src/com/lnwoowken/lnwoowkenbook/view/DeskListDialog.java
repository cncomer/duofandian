package com.lnwoowken.lnwoowkenbook.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.lnwoowken.lnwoowkenbook.R;

public class DeskListDialog extends Dialog {
	Context context;
    public DeskListDialog(Context context) {
        super(context);
        this.context = context;
    }
    public DeskListDialog(Context context, int theme){
        super(context, theme);
        this.context = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dailog_desklist);
    }
}
