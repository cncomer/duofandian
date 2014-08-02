package com.lnwoowken.lnwoowkenbook.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.lnwoowken.lnwoowkenbook.R;
import com.lnwoowken.lnwoowkenbook.model.TableInfo;

public class TableListAdapter extends BaseAdapter {
private Context context;
	
	private List<TableInfo> tableList;
	
//	private Typeface typeface12=null;//Œ¢»Ì—≈∫⁄
//	private Typeface typeface3=null;//times
	public TableListAdapter(Context context,List<TableInfo> tableList) {
		this.context = context;
		
		//this.subclassname=subclassname;
		this.tableList = tableList;// = InitProduct();
		//this.child=InitChild();
//		this.typeface12=Typeface.createFromAsset(context.getAssets(),"font/msyh.ttf");
//		this.typeface3=Typeface.createFromAsset(context.getAssets(),"font/TIMES.TTF");
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		return this.tableList.size();
	}

	@Override
	public TableInfo getItem(int arg0) {
		// TODO Auto-generated method stub
		TableInfo item = null;

        if (null != tableList)
        {
            item = tableList.get(arg0);
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
			convertView=LayoutInflater.from(context).inflate(R.layout.table_list_item,null);
			groupHolder=new ViewHolder();
			groupHolder.row=(LinearLayout) convertView.findViewById(R.id.table_list_row);
			groupHolder.tableName=(TextView) convertView.findViewById(R.id.textView_table_name);
			groupHolder.total=(TextView) convertView.findViewById(R.id.textView_table_total);
			groupHolder.time=(TextView) convertView.findViewById(R.id.textView_time);
			
			//groupHolder.img_distance=(ImageButton) convertView.findViewById(R.id.imageButton_distance);
			
			convertView.setTag(groupHolder);
		}else{
			groupHolder=(ViewHolder)convertView.getTag();
		}
		int total_num = -1;
		TableInfo table = getItem(position);
		if (table!=null) {
//			String str_total =  table.getTotal();
			String table_state = "";
			table_state = table.getStaRes();
			if (table_state.equals("")||table_state == null) {
				Log.d("table_state================", "");
				groupHolder.total.setBackgroundResource(R.drawable.blue_gray);
				groupHolder.total.setText("«¿Œª");
				groupHolder.time.setTextColor(R.color.main_color);
			}
			else {
				int state = Integer.parseInt(table.getStaRes());
				switch (state) {
				case 7:
				case 3:
				case 4:
					groupHolder.total.setBackgroundResource(R.drawable.blue_gray);
					groupHolder.total.setText("«¿Œª");
					groupHolder.time.setTextColor(R.color.main_color);
					break;

				default:
					groupHolder.total.setBackgroundResource(R.drawable.gray);
					groupHolder.total.setText("∂·ÕÍ¿≤");
					groupHolder.time.setTextColor(R.color.gray);
					break;
				}
			}
//			if (str_total!=null&&!str_total.equals("")) {
//				try {
//					total_num = Integer.parseInt(str_total);
//				} catch (Exception e) {
//					// TODO: handle exception
//					Log.d("catch",  e.toString());
//				}
//			}
//			
//			if (total_num<=1&&total_num>=0) {
//				groupHolder.row.setVisibility(View.VISIBLE);
//				if (total_num==0) {
//				//	Log.d("free______________", "");
//					groupHolder.row.setBackgroundResource(R.drawable.boder_bottom);
//					table_state = "ø’œ–";
//					
//				}
//				if (total_num == 1) {
//				//	Log.d("unfree______________", "");
//					groupHolder.row.setBackgroundColor(R.color.textColor_gray);
//					table_state = "∑±√¶";
//				}
//				
//				
//			}
//			else {
//				groupHolder.row.setVisibility(View.GONE);
//			}
			groupHolder.tableName.setText(tableList.get(position).getAname());
			groupHolder.time.setText(tableList.get(position).getRt());
			//groupHolder.total.setText(table_state);
		}
		
		//convertView.setClickable(true);
		return convertView;
	}
	
	
	static class ViewHolder{
		TextView tableName;
		TextView total;
		TextView time;
		LinearLayout row;
		//TextView environmentLevel;
		
		//ImageButton img_distance;
	}
}
