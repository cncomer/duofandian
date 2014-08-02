package com.lnwoowken.lnwoowkenbook.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.lnwoowken.lnwoowkenbook.R;
import com.lnwoowken.lnwoowkenbook.model.StoreInfo;

public class ShopAdapter extends BaseAdapter {
	private Context context;
	private List<StoreInfo> list;
	private int selectedPosition = -1;
	//private String[][] arr;
//	private Typeface typeface12=null;//Î¢ÈíÑÅºÚ
//	private Typeface typeface3=null;//times
	public ShopAdapter(Context context,List<StoreInfo> list) {
		this.context = context;
		this.list = list;

	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		return list.size();
	}

	@Override
	public StoreInfo getItem(int arg0) {
		// TODO Auto-generated method stub
		StoreInfo item = null;

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
			convertView=LayoutInflater.from(context).inflate(R.layout.list_shop_classification_item,null);
			groupHolder=new ViewHolder();
			//groupHolder.row=(LinearLayout) convertView.findViewById(R.id.table_list_row);
			groupHolder.title=(TextView) convertView.findViewById(R.id.textView_shopName);
			//groupHolder.icon=(ImageView) convertView.findViewById(R.id.imageView_icon);
			//groupHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.tab_content);
			
			//groupHolder.img_distance=(ImageButton) convertView.findViewById(R.id.imageButton_distance);
			
			convertView.setTag(groupHolder);
		
		}else{
			groupHolder=(ViewHolder)convertView.getTag();
		}
		if (list.get(position)!=null) {
			groupHolder.title.setText(list.get(position).getName());
			//groupHolder.icon.setBackgroundResource(list.get(position).getImgId());
		}
		
		
		//convertView.setClickable(true);
		return convertView;
	}
	
	
	static class ViewHolder{
		TextView title;
	//	ImageView icon;
	//	LinearLayout linearLayout;
		//TextView environmentLevel;
		
		//ImageButton img_distance;
	}
}
