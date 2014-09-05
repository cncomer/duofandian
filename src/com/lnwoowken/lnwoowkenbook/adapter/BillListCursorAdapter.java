package com.lnwoowken.lnwoowkenbook.adapter;

import java.text.ParseException;
import java.util.Date;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lnwoowken.lnwoowkenbook.BillListManager;
import com.lnwoowken.lnwoowkenbook.R;
import com.lnwoowken.lnwoowkenbook.SurveyActivity;
import com.lnwoowken.lnwoowkenbook.UnPayInfoActivity;
import com.lnwoowken.lnwoowkenbook.model.BillObject;
import com.shwy.bestjoy.utils.DateUtils;

public class BillListCursorAdapter extends CursorAdapter {
	private Context mContext;
	private int mDataType = DATA_TYPE_ALL;
	public static final int DATA_TYPE_ALL = 0;
	public static final int DATA_TYPE_UNPAY = 1;
	
	private Drawable mUnvisitedTypeDrawable, mVisitedTypeDrawable;
	public BillListCursorAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		mContext = context;
		mUnvisitedTypeDrawable = context.getResources().getDrawable(R.drawable.btn_normal);
		mVisitedTypeDrawable = context.getResources().getDrawable(R.drawable.btn_visited);
	}
	
	public void changeCursor(Cursor c, int type) {
		mDataType = type;
		super.changeCursor(c);
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

	private void showDialog(String str, final String billNumber) {
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
				BillListManager.deleteBillByNumber(mContext.getContentResolver(), billNumber);
				alertDialog.dismiss();
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

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.bill_list_item, null);
		ViewHolder groupHolder = new ViewHolder();
		
		groupHolder.textView_billnumber=(TextView) view.findViewById(R.id.textView_billnumber);
		groupHolder.textView_state=(TextView) view.findViewById(R.id.textView_state);
		groupHolder.textView_createDate=(TextView) view.findViewById(R.id.textView_createDate);		
		groupHolder.textView_shopName=(TextView) view.findViewById(R.id.textView_shopName);		
		groupHolder.textView_time=(TextView) view.findViewById(R.id.textView_time);
		groupHolder.textView_tableName=(TextView) view.findViewById(R.id.textView_tableName);
		groupHolder.btn_survey=(ImageButton) view.findViewById(R.id.imageButton_survey);
		groupHolder.btn_delete = (ImageButton) view.findViewById(R.id.imageButton_delete);
		groupHolder.btn_tuiding = (Button) view.findViewById(R.id.button_tuiding);
		
		groupHolder.btn_confirm_pay = (Button) view.findViewById(R.id.button_confirm_pay);
		groupHolder.order_status_layout = view.findViewById(R.id.order_status_layout);
		
		groupHolder.billObject = BillListManager.getBillObject(cursor);
		view.setTag(groupHolder);
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		final ViewHolder groupHolder = (ViewHolder) view.getTag();
		
		groupHolder.textView_billnumber.setText(groupHolder.billObject.getBillNumber());
		groupHolder.textView_state.setText(getBillState(groupHolder.billObject.getState()));
		groupHolder.textView_shopName.setText(groupHolder.billObject.getShopName());
		groupHolder.textView_time.setText(groupHolder.billObject.getDate());
		groupHolder.textView_tableName.setText(groupHolder.billObject.getTableName() + " " + groupHolder.billObject.getTableType());
		groupHolder.textView_createDate.setText(groupHolder.billObject.getCreateTime());
		if(groupHolder.billObject.getState() != BillObject.STATE_SUCCESS) {
			groupHolder.btn_tuiding.setEnabled(false);
			groupHolder.btn_tuiding.setBackgroundColor(Color.GRAY);
		}
		groupHolder.btn_tuiding.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("bill_number", groupHolder.billObject.getBillNumber());
				Intent intent = new Intent();
				intent.setClass(mContext, UnPayInfoActivity.class);
				intent.putExtras(bundle);
				mContext.startActivity(intent);
			}
		});
		groupHolder.btn_survey.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext,SurveyActivity.class);
				intent.putExtra("rid", groupHolder.billObject.getId());
				mContext.startActivity(intent);
			}
		});
		groupHolder.btn_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDialog(mContext.getString(R.string.delete_tips), groupHolder.billObject.getBillNumber());
			}
		});
		
		
		if (mDataType == DATA_TYPE_ALL) {
			groupHolder.btn_survey.setImageDrawable(mUnvisitedTypeDrawable);
			groupHolder.btn_survey.setEnabled(true);
			groupHolder.btn_confirm_pay.setVisibility(View.GONE);
			groupHolder.order_status_layout.setVisibility(View.VISIBLE);
			groupHolder.btn_tuiding.setVisibility(View.VISIBLE);
		} else if (mDataType == DATA_TYPE_UNPAY) {
			groupHolder.btn_survey.setImageDrawable(mVisitedTypeDrawable);
			groupHolder.btn_survey.setEnabled(false);
			groupHolder.btn_confirm_pay.setVisibility(View.VISIBLE);
			groupHolder.order_status_layout.setVisibility(View.GONE);
			groupHolder.btn_tuiding.setVisibility(View.INVISIBLE);
			Date date;
			try {
				date = DateUtils.TOPIC_SUBJECT_DATE_TIME_FORMAT.parse(groupHolder.billObject.getCreateTime());
				long createTime = date.getTime();
				long currentTime = SystemClock.elapsedRealtime();
				groupHolder.btn_confirm_pay.setEnabled(Math.abs(currentTime - createTime) < OVER_TIME);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
	}
	private static final long OVER_TIME = 1000*60*10;//10分钟
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
		Button btn_confirm_pay;
		private View order_status_layout;
		private BillObject billObject;
	}

}
