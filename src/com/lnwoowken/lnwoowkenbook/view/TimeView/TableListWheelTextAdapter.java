package com.lnwoowken.lnwoowkenbook.view.TimeView;

import java.util.List;

import android.content.Context;

import com.lnwoowken.lnwoowkenbook.model.BookTime;
import com.lnwoowken.lnwoowkenbook.model.TableStyle;

public class TableListWheelTextAdapter extends AbstractWheelTextAdapter {
	// items
    private List<TableStyle> items;

    /**
     * Constructor
     * @param context the current context
     * @param time_list the items
     */
    public TableListWheelTextAdapter(Context context, List<TableStyle> time_list) {
        super(context);
        
        //setEmptyItemResource(TEXT_VIEW_ITEM_RESOURCE);
        this.items = time_list;
    }
    
    @Override
    public CharSequence getItemText(int index) {
        if (index >= 0 && index < items.size()) {
            String item = items.get(index).getStyleName();
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
