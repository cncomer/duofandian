package com.lnwoowken.lnwoowkenbook.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lnwoowken.lnwoowkenbook.BillListActivity;
import com.lnwoowken.lnwoowkenbook.R;
import com.lnwoowken.lnwoowkenbook.SurveyActivity;
import com.lnwoowken.lnwoowkenbook.model.Bill;

public class BillListAdapter  extends BaseAdapter {

	private Context context;
	private List<Bill> bill;
	HashMap<Integer, View> lmap = new HashMap<Integer, View>();
	public BillListAdapter(Context context,List<Bill> bill) {
		this.context = context;
		this.bill = bill;// = InitProduct();

	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		return this.bill.size();
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

	@Override
	public View getView(final int position, View convertView1, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder groupHolder=null;
		View view;
		if(lmap.get(position) == null){
			view = LayoutInflater.from(context).inflate(
					R.layout.survey_list_item, null);
			view=LayoutInflater.from(context).inflate(R.layout.bill_list_item,null);
			groupHolder=new ViewHolder();
			
			groupHolder.textView_billnumber=(TextView) view.findViewById(R.id.textView_billnumber);
			groupHolder.textView_tableStyle=(TextView) view.findViewById(R.id.textView_tableStyle);
			groupHolder.textView_state=(TextView) view.findViewById(R.id.textView_state);
			groupHolder.textView_createDate=(TextView) view.findViewById(R.id.textView_createDate);		
			groupHolder.textView_shopName=(TextView) view.findViewById(R.id.textView_shopName);		
			groupHolder.textView_date=(TextView) view.findViewById(R.id.textView_date);
			groupHolder.textView_time=(TextView) view.findViewById(R.id.textView_time);
			groupHolder.textView_tableName=(TextView) view.findViewById(R.id.textView_tableName);
			groupHolder.btn_survey=(ImageButton) view.findViewById(R.id.imageButton_survey);

			
			view.setTag(groupHolder);
		}else{
			view = lmap.get(position);
			groupHolder=(ViewHolder)view.getTag();
		} 
		if (bill.get(position)!=null) {
			Bill b = bill.get(position);
			groupHolder.textView_billnumber.setText(b.getBillNumber());
			groupHolder.textView_tableStyle.setText(b.getTableStyle());
			groupHolder.textView_state.setText(b.getState()+"");
			groupHolder.textView_createDate.setText(b.getCreateTime());
			groupHolder.textView_shopName.setText(b.getShopName());
			groupHolder.textView_date.setText(b.getDate());
			groupHolder.textView_time.setText(b.getTimeId()+"");
			groupHolder.textView_tableName.setText(b.getTableName());
		}
		else {
			String str = context.getResources().getString(R.string.no_data);
			groupHolder.textView_billnumber.setText(str);
			groupHolder.textView_tableStyle.setText(str);
			groupHolder.textView_state.setText(str);
			groupHolder.textView_createDate.setText(str);
			groupHolder.textView_shopName.setText(str);
			groupHolder.textView_date.setText(str);
			groupHolder.textView_time.setText(str);
			groupHolder.textView_tableName.setText(str);
		}
		groupHolder.btn_survey.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,SurveyActivity.class);
				intent.putExtra("rid", bill.get(position).getId());
				context.startActivity(intent);
			}
		});
		
		
		//groupHolder.name.setText(productList.get(position));
		//convertView.setClickable(true);
		return view;
	}
	
	
	static class ViewHolder{
		
		TextView textView_billnumber;
		TextView textView_shopName;
		TextView textView_date;
		TextView textView_time;
		TextView textView_tableName;
		TextView textView_tableStyle;
		TextView textView_state;
		TextView textView_createDate;
		ImageButton btn_survey;

	}

}
