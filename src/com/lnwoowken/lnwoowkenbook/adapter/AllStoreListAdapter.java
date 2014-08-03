package com.lnwoowken.lnwoowkenbook.adapter;

import java.util.List;

import com.lnwoowken.lnwoowkenbook.R;
import com.lnwoowken.lnwoowkenbook.model.StoreInfo;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 餐厅信息listview的适配器
 * @author sean
 *
 */
public class AllStoreListAdapter extends BaseAdapter {

	private Context context;
	
	private List<StoreInfo> store;
	
//	private Typeface typeface12=null;//微软雅黑
//	private Typeface typeface3=null;//times
	public AllStoreListAdapter(Context context,List<StoreInfo> store) {
		this.context = context;
		
		//this.subclassname=subclassname;
		this.store = store;// = InitProduct();
		//this.child=InitChild();
//		this.typeface12=Typeface.createFromAsset(context.getAssets(),"font/msyh.ttf");
//		this.typeface3=Typeface.createFromAsset(context.getAssets(),"font/TIMES.TTF");
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		return this.store.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder groupHolder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.listview_shop_item,null);
			groupHolder=new ViewHolder();
			
			groupHolder.storeName=(TextView) convertView.findViewById(R.id.textView_storename);
			groupHolder.tv_1=(TextView) convertView.findViewById(R.id.textView_1);
			groupHolder.tv_3=(TextView) convertView.findViewById(R.id.textView_3);
			groupHolder.try_other=(TextView) convertView.findViewById(R.id.textView_tryother);		
			groupHolder.price=(TextView) convertView.findViewById(R.id.textView_price);	
			groupHolder.distance=(TextView) convertView.findViewById(R.id.textView_distance);
			groupHolder.location=(TextView) convertView.findViewById(R.id.textView_position);
			groupHolder.tablenumber=(TextView) convertView.findViewById(R.id.textView_tablenumber);
			groupHolder.icon = (ImageView) convertView.findViewById(R.id.imageView_hui);
			
			convertView.setTag(groupHolder);
		}else{
			groupHolder=(ViewHolder)convertView.getTag();
		} 
		String address = store.get(position).getAddress();
		if (address.equals(null)||address.equals("")) {
			groupHolder.location.setText(R.string.no_data);
		}
		else {
			groupHolder.location.setText(store.get(position).getAddress());
		}
		
		groupHolder.storeName.setText(store.get(position).getName());
		
		String average = store.get(position).getAveragePrice();
		//Log.d("--------------", store.get(position).getAveragePrice());
		if (average.equals(null)||average.equals("")) {
			groupHolder.price.setText(context.getString(R.string.listview_item_price)+"暂无数据");
			
		}
		else {
			groupHolder.price.setText(context.getString(R.string.listview_item_price)+store.get(position).getAveragePrice());
		}
		groupHolder.distance.setText("距离:"+context.getString(R.string.no_data));
		int num = store.get(position).getTableNum();
		if (num==0) {
			groupHolder.tv_1.setVisibility(View.GONE);
			groupHolder.tv_3.setVisibility(View.GONE);
			groupHolder.try_other.setVisibility(View.VISIBLE);
			groupHolder.tablenumber.setText("今日夺完啦");
			groupHolder.tablenumber.setTextColor(R.color.textColor_gray);
		}
		else {
			groupHolder.tv_1.setVisibility(View.VISIBLE);
			groupHolder.tv_3.setVisibility(View.VISIBLE);
			groupHolder.tablenumber.setText(num+"");
			groupHolder.tablenumber.setTextColor(Color.RED);
			groupHolder.try_other.setVisibility(View.GONE);
		}
		String iconStr = store.get(position).getIcon();
		if (iconStr!=null&&!iconStr.equals("")) {
			setImageIcon(iconStr,groupHolder.icon );
		}
		

		return convertView;
	}
	
	
	static class ViewHolder{
		TextView storeName;
		TextView price;
		TextView distance;
		TextView location;
		TextView tablenumber;
		TextView tv_1;
		TextView tv_3;
		TextView try_other;
		ImageView icon;

	}
	
	private void setImageIcon(String str,View v){
		if (str.equals("0")) {
			v.setBackgroundResource(R.drawable.icon_hui);
		}
		else if (str.equals("1")) {
			v.setBackgroundResource(R.drawable.icon_tui);
		}
		else if (str.equals("2")) {
			v.setBackgroundResource(R.drawable.icon_tuan);
		}
		else if (str.equals("3")) {
			v.setBackgroundResource(R.drawable.icon_chao);
		}
		else if (str.equals("4")) {
			v.setBackgroundResource(R.drawable.icon_mai);
		}
		else if (str.equals("5")) {
			v.setBackgroundResource(R.drawable.icon_dian);
		}
	}

}
