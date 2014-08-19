package com.lnwoowken.lnwoowkenbook.view;



import com.lnwoowken.lnwoowkenbook.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;


public class TableListDialog extends Dialog{

	public TableListDialog(Context context) {
		super(context);
	}
	
	public TableListDialog(Context context,int id) {
		super(context,id);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_table_list);
		
	}

}
