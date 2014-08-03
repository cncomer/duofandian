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

public class OtherShopAdapter extends BaseAdapter {

	private Context context;
	
	private List<StoreInfo> store;
	
//	private Typeface typeface12=null;//微软雅黑
//	private Typeface typeface3=null;//times
	public OtherShopAdapter(Context context,List<StoreInfo> store) {
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
			convertView=LayoutInflater.from(context).inflate(R.layout.othershop_item,null);
			groupHolder=new ViewHolder();
			
			groupHolder.storeName=(TextView) convertView.findViewById(R.id.textView_shopName);
			
			
			convertView.setTag(groupHolder);
		}else{
			groupHolder=(ViewHolder)convertView.getTag();
		} 
		
		
		groupHolder.storeName.setText(store.get(position).getName());
		
		

		return convertView;
	}
	
	
	static class ViewHolder{
		TextView storeName;
		

	}
	
	

}
