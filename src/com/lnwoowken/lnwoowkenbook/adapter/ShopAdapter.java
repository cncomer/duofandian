package com.lnwoowken.lnwoowkenbook.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cncom.app.base.util.ShopInfoObject;
import com.lnwoowken.lnwoowkenbook.R;
import com.lnwoowken.lnwoowkenbook.model.StoreInfo;

public class ShopAdapter extends BaseAdapter {
	private Context context;
	private List<ShopInfoObject> mList;
	private int selectedPosition = -1;
	
	public ShopAdapter(Context context,List<ShopInfoObject> list) {
		this.context = context;
		this.mList = list;

	}
	
	public void updateShopList(List<ShopInfoObject> list) {
		mList = list;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		return mList.size();
	}

	@Override
	public ShopInfoObject getItem(int arg0) {
		ShopInfoObject item = null;
        if (null != mList)
        {
            item = mList.get(arg0);
        }
        return item;
		
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	public void setSelectedPosition(int position) {  
        selectedPosition = position;  
    } 
	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
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
		if (mList.get(position)!=null) {
			groupHolder.title.setText(mList.get(position).getShopName());
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
