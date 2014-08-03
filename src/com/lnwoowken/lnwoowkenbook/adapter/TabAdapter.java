package com.lnwoowken.lnwoowkenbook.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lnwoowken.lnwoowkenbook.R;
import com.lnwoowken.lnwoowkenbook.model.ShopTree;
import com.lnwoowken.lnwoowkenbook.model.TabObj;

/**
 * 选项卡
 * @author sean
 *
 */
public class TabAdapter extends BaseAdapter {

	private Context context;
	private List<ShopTree> list;
	private int selectedPosition = -1;
	//private String[][] arr;
//	private Typeface typeface12=null;//微软雅黑
//	private Typeface typeface3=null;//times
	public TabAdapter(Context context,List<ShopTree> list) {
		this.context = context;
		this.list = list;

	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		return list.size();
	}

	@Override
	public ShopTree getItem(int arg0) {
		// TODO Auto-generated method stub
		ShopTree item = null;

        if (null != list)
        {
            item = list.get(arg0);
        }

        return item;
		
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	public void setSelectedPosition(int position) {  
        selectedPosition = position;  
    } 
	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder groupHolder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.tab_item,null);
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
		if (list.get(position)!=null) {
			groupHolder.title.setText(list.get(position).getName());
			//groupHolder.icon.setBackgroundResource(list.get(position).getImgId());
		}
		if (selectedPosition == position) {  
			Log.d("if---selectedPosition============="+selectedPosition, "position:"+position);
			groupHolder.linearLayout.setSelected(true);  
			groupHolder.linearLayout.setPressed(true);  
			groupHolder.linearLayout.setBackgroundColor(Color.WHITE);  
        } else {  
        	Log.d("else---selectedPosition============="+selectedPosition, "position:"+position+"");
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
