package com.lnwoowken.lnwoowkenbook.view.TimeView;

import android.content.Context;

public class ArrayListWheelAdapter extends AbstractWheelTextAdapter {
    
    // items
    public static final String[] SHIDUAN = new String[]{
    	"午市",
    	"晚市"
    };

    /**
     * Constructor
     * @param context the current context
     * @param time_list the items
     */
    public ArrayListWheelAdapter(Context context) {
        super(context);
        
        //setEmptyItemResource(TEXT_VIEW_ITEM_RESOURCE);
    }
    
    @Override
    public CharSequence getItemText(int index) {
        if (index >= 0 && index < SHIDUAN.length) {
            String item = SHIDUAN[index];
            if (item instanceof CharSequence) {
                return (CharSequence) item;
            }
            return item;
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return SHIDUAN != null ? SHIDUAN.length : 0;
    }
}
