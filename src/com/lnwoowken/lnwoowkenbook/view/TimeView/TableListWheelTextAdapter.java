package com.lnwoowken.lnwoowkenbook.view.TimeView;

import android.content.Context;

public class TableListWheelTextAdapter extends AbstractWheelTextAdapter {
	// items
    public static final String[] DESK_TYPE = new String[] {
		"2人桌(1-2人)",
		"4人桌(3-4人)",
		"6人桌(5-6人)",
		"包房(8-10人)",
    };

    /**
     * Constructor
     * @param context the current context
     * @param time_list the items
     */
    public TableListWheelTextAdapter(Context context) {
        super(context);
        
        //setEmptyItemResource(TEXT_VIEW_ITEM_RESOURCE);
    }
    
    @Override
    public CharSequence getItemText(int index) {
        if (index >= 0 && index < DESK_TYPE.length) {
            String item = DESK_TYPE[index];
            if (item instanceof CharSequence) {
                return (CharSequence) item;
            }
            return item;
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return DESK_TYPE != null ? DESK_TYPE.length : 0;
    }
}
