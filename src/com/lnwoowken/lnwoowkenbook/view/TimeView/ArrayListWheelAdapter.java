package com.lnwoowken.lnwoowkenbook.view.TimeView;

import java.util.List;

import com.lnwoowken.lnwoowkenbook.model.BookTime;

import android.content.Context;

public class ArrayListWheelAdapter extends AbstractWheelTextAdapter {
    
    // items
    private List<BookTime> items;

    /**
     * Constructor
     * @param context the current context
     * @param time_list the items
     */
    public ArrayListWheelAdapter(Context context, List<BookTime> time_list) {
        super(context);
        
        //setEmptyItemResource(TEXT_VIEW_ITEM_RESOURCE);
        this.items = time_list;
    }
    
    @Override
    public CharSequence getItemText(int index) {
        if (index >= 0 && index < items.size()) {
            String item = items.get(index).getTimeName();
            if (item instanceof CharSequence) {
                return (CharSequence) item;
            }
            return item;
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return items.size();
    }
}
