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
	
	public TableListAdapter(Context context,List<TableInfo> tableList) {
		this.context = context;
		this.tableList = tableList;
	}
	
	@Override
	public int getCount() {
		return this.tableList.size();
	}

	@Override
	public TableInfo getItem(int arg0) {
		TableInfo item = null;
		if (null != tableList) {
			item = tableList.get(arg0);
		}

        return item;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder groupHolder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.table_list_item,null);
			groupHolder=new ViewHolder();
			groupHolder.row=(LinearLayout) convertView.findViewById(R.id.table_list_row);
			groupHolder.tableName=(TextView) convertView.findViewById(R.id.textView_table_name);
			groupHolder.total=(TextView) convertView.findViewById(R.id.textView_table_total);
			groupHolder.time=(TextView) convertView.findViewById(R.id.textView_time);
			convertView.setTag(groupHolder);
		}else{
			groupHolder=(ViewHolder)convertView.getTag();
		}
		TableInfo table = getItem(position);
		if (table!=null) {
			String table_state = "";
			table_state = table.getStaRes();
			if (table_state.equals("")||table_state == null) {
				Log.d("table_state================", "");
				groupHolder.total.setBackgroundResource(R.drawable.blue_gray);
				groupHolder.total.setText("抢位");
				groupHolder.time.setTextColor(R.color.main_color);
			}
			else {
				int state = Integer.parseInt(table.getStaRes());
				switch (state) {
				case 7:
				case 3:
				case 4:
					groupHolder.total.setBackgroundResource(R.drawable.blue_gray);
					groupHolder.total.setText("抢位");
					groupHolder.time.setTextColor(R.color.main_color);
					break;

				default:
					groupHolder.total.setBackgroundResource(R.drawable.gray);
					groupHolder.total.setText("夺完啦");
					groupHolder.time.setTextColor(R.color.gray);
					break;
				}
			}
			groupHolder.tableName.setText(tableList.get(position).getTableName());
			groupHolder.time.setText(tableList.get(position).getRt());
		}
		return convertView;
	}
	
	
	static class ViewHolder{
		TextView tableName;
		TextView total;
		TextView time;
		LinearLayout row;
	}
}
