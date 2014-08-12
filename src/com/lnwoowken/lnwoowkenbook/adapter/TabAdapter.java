package com.lnwoowken.lnwoowkenbook.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lnwoowken.lnwoowkenbook.R;

/**
 * 选项卡
 * @author chs
 *
 */
public class TabAdapter extends BaseAdapter {
	private Context context;
	private int selectedPosition = -1;
	public static final String[] TABTEXT = new String[]{"品牌","商圈","菜系","行政区"};
	private ArrayList<String> mList;
	
	public TabAdapter(Context context) {
		this.context = context;
		mList = new ArrayList<String>();
		mList.clear();
		for(String str:TABTEXT) {
			mList.add(str);
		}
	}

	public void initTabList() {
		mList = new ArrayList<String>();
		mList.clear();
		for(String str:TABTEXT) {
			mList.add(str);
		}
		notifyDataSetChanged();
	}

	public void updateTabList(ArrayList<String> list) {
		mList = list;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public String getItem(int pos) {
        return mList.get(pos);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}
	public void setSelectedPosition(int position) {  
        selectedPosition = position;  
    } 

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder groupHolder=null;
		if(convertView==null){
			convertView = LayoutInflater.from(context).inflate(R.layout.tab_item, null);
			groupHolder=new ViewHolder();
			//groupHolder.row=(LinearLayout) convertView.findViewById(R.id.table_list_row);
			groupHolder.title=(TextView) convertView.findViewById(R.id.textView_title);
			groupHolder.icon=(ImageView) convertView.findViewById(R.id.imageView_icon);
			groupHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.tab_content);
			
			//groupHolder.img_distance=(ImageButton) convertView.findViewById(R.id.imageButton_distance);
			
			convertView.setTag(groupHolder);
		
		}else{
			groupHolder=(ViewHolder)convertView.getTag();
		}
		if (mList != null) {
			groupHolder.title.setText(mList.get(position));
			//groupHolder.icon.setBackgroundResource(list.get(position).getImgId());
		}
		if (selectedPosition == position) {  
			groupHolder.linearLayout.setSelected(true);  
			groupHolder.linearLayout.setPressed(true);  
			groupHolder.linearLayout.setBackgroundColor(Color.WHITE);  
        } else {  
        	groupHolder.linearLayout.setSelected(false);  
        	groupHolder.linearLayout.setPressed(false);  
        	groupHolder.linearLayout.setBackgroundColor(Color.TRANSPARENT);     

        }
		//convertView.setClickable(true);
		return convertView;
	}
	
	static class ViewHolder{
		TextView title;
		ImageView icon;
		LinearLayout linearLayout;
		//TextView environmentLevel;
		//ImageButton img_distance;
	}
}
