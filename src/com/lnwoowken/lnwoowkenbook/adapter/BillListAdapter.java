package com.lnwoowken.lnwoowkenbook.adapter;

import java.util.HashMap;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lnwoowken.lnwoowkenbook.BillListManager;
import com.lnwoowken.lnwoowkenbook.R;
import com.lnwoowken.lnwoowkenbook.SurveyActivity;
import com.lnwoowken.lnwoowkenbook.UnPayInfoActivity;
import com.lnwoowken.lnwoowkenbook.model.BillObject;

public class BillListAdapter extends BaseAdapter {
	private Context mContext;
	private List<BillObject> mBillList;
	HashMap<Integer, View> lmap = new HashMap<Integer, View>();
	public BillListAdapter(Context context,List<BillObject> bill) {
		this.mContext = context;
		this.mBillList = bill;
	}
	
	public void updateList(List<BillObject> billList) {
		this.mBillList = billList;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return this.mBillList.size();
	}

	@Override
	public Object getItem(int pos) {
		return pos;
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder groupHolder=null;
		View view;
		if (lmap.get(position) == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.bill_list_item, null);
			groupHolder=new ViewHolder();
			
			groupHolder.textView_billnumber=(TextView) view.findViewById(R.id.textView_billnumber);
			groupHolder.textView_state=(TextView) view.findViewById(R.id.textView_state);
			groupHolder.textView_createDate=(TextView) view.findViewById(R.id.textView_createDate);		
			groupHolder.textView_shopName=(TextView) view.findViewById(R.id.textView_shopName);		
			groupHolder.textView_time=(TextView) view.findViewById(R.id.textView_time);
			groupHolder.textView_tableName=(TextView) view.findViewById(R.id.textView_tableName);
			groupHolder.btn_survey=(ImageButton) view.findViewById(R.id.imageButton_survey);
			groupHolder.btn_delete = (ImageButton) view.findViewById(R.id.imageButton_delete);
			groupHolder.btn_tuiding = (Button) view.findViewById(R.id.button_tuiding);

			view.setTag(groupHolder);
		} else {
			view = lmap.get(position);
			groupHolder=(ViewHolder)view.getTag();
		}
		if (mBillList.get(position)!=null) {
			final BillObject b = mBillList.get(position);
			groupHolder.textView_billnumber.setText(b.getBillNumber());
			groupHolder.textView_state.setText(getBillState(b.getState()));
			groupHolder.textView_shopName.setText(b.getShopName());
			groupHolder.textView_time.setText(b.getDate());
			groupHolder.textView_tableName.setText(b.getTableName());
			groupHolder.textView_createDate.setText(b.getCreateTime());
			if(b.getState() != BillObject.STATE_SUCCESS) {
				groupHolder.btn_tuiding.setEnabled(false);
				groupHolder.btn_tuiding.setBackgroundColor(Color.GRAY);
			}
			groupHolder.btn_tuiding.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Bundle bundle = new Bundle();
					bundle.putString("bill_number", b.getBillNumber());
					Intent intent = new Intent();
					intent.setClass(mContext, UnPayInfoActivity.class);
					intent.putExtras(bundle);
					mContext.startActivity(intent);
				}
			});
		} else {
			String str = mContext.getResources().getString(R.string.no_data);
			groupHolder.textView_billnumber.setText(str);
			groupHolder.textView_state.setText(str);
			groupHolder.textView_createDate.setText(str);
			groupHolder.textView_shopName.setText(str);
			groupHolder.textView_time.setText(str);
			groupHolder.textView_tableName.setText(str);
		}
		groupHolder.btn_survey.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext,SurveyActivity.class);
				intent.putExtra("rid", mBillList.get(position).getId());
				mContext.startActivity(intent);
			}
		});
		groupHolder.btn_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDialog(mContext.getString(R.string.delete_tips), position);
			}
		});
		return view;
	}
	
	private String getBillState(int state) {
		int res = R.string.bill_unpay;
		switch (state) {
		case BillObject.STATE_IDLE:
			res = R.string.bill_unpay;
			break;
		case BillObject.STATE_UNPAY:
			res = R.string.bill_unpay;
			break;
		case BillObject.STATE_SUCCESS:
			res = R.string.bill_pay_sucess;
			break;
		case BillObject.STATE_TUIDING_SUCCESS:
			res = R.string.bill_tuiding_sucess;
			break;
		}
		return mContext.getString(res);
	}

	static class ViewHolder{
		TextView textView_billnumber;
		TextView textView_shopName;
		TextView textView_time;
		TextView textView_tableName;
		TextView textView_state;
		TextView textView_createDate;
		ImageButton btn_survey;
		ImageButton btn_delete;
		Button btn_tuiding;
	}
	

	private void showDialog(String str, final int position) {
		final Dialog alertDialog = new Dialog(mContext, R.style.MyDialog);
		alertDialog.setTitle(R.string.dialog_title);
		alertDialog.setContentView(R.layout.dialog_layout);
		TextView title = (TextView) alertDialog.findViewById(R.id.title);
		TextView context = (TextView) alertDialog.findViewById(R.id.message);
		Button buttonOk = (Button) alertDialog.findViewById(R.id.button_ok);
		Button buttonCancel = (Button) alertDialog.findViewById(R.id.button_back);
		title.setText(R.string.dialog_title);
		context.setText(str);
		alertDialog.setCanceledOnTouchOutside(false);
		buttonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BillListManager.deleteBillByNumber(mContext.getContentResolver(), mBillList.get(position).getBillNumber());
				alertDialog.dismiss();
				mBillList.remove(mBillList.get(position));
				notifyDataSetChanged();
			}
		});
		buttonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}

}
