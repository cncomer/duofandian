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


public class TabContentAdapter extends BaseAdapter {
	
	private Context context;
	private List<String> list;

	public TabContentAdapter(Context context,List<String> list) {
		this.context = context;
		this.list = list;

	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		return list.size();
	}

	@Override
	public String getItem(int arg0) {
		// TODO Auto-generated method stub
		String item = null;

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

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder groupHolder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.tab_content_item,null);
			groupHolder=new ViewHolder();
			groupHolder.content=(TextView) convertView.findViewById(R.id.textView_content);
			
	
			convertView.setTag(groupHolder);
		}else{
			groupHolder=(ViewHolder)convertView.getTag();
		}
		if (list.get(position)!=null) {
			groupHolder.content.setText(list.get(position));
			
		}
		
		//convertView.setClickable(true);
		return convertView;
	}
	
	
	static class ViewHolder{
		TextView content;
		
		//TextView environmentLevel;
		
		//ImageButton img_distance;
	}
}
