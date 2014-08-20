package com.lnwoowken.lnwoowkenbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.ui.BaseActionbarActivity;
import com.cncom.app.base.util.DebugUtils;
import com.cncom.app.base.util.PatternInfoUtils;
import com.cncom.app.base.util.TableInfoObject;
import com.lnwoowken.lnwoowkenbook.ServiceObject.ServiceResultObject;
import com.lnwoowken.lnwoowkenbook.data.PayInfoData;
import com.lnwoowken.lnwoowkenbook.model.PayInfo;
import com.lnwoowken.lnwoowkenbook.model.StoreInfo;
import com.lnwoowken.lnwoowkenbook.model.TableInfo;
import com.lnwoowken.lnwoowkenbook.model.TableStyle;
import com.lnwoowken.lnwoowkenbook.tools.Tools;
import com.lnwoowken.lnwoowkenbook.view.CalendarDialog;
import com.lnwoowken.lnwoowkenbook.view.CalendarView;
import com.lnwoowken.lnwoowkenbook.view.DeskListDialog;
import com.lnwoowken.lnwoowkenbook.view.ProgressDialog;
import com.lnwoowken.lnwoowkenbook.view.TimeDialog;
import com.lnwoowken.lnwoowkenbook.view.TimeView.ArrayListWheelAdapter;
import com.lnwoowken.lnwoowkenbook.view.TimeView.TableListWheelTextAdapter;
import com.lnwoowken.lnwoowkenbook.view.TimeView.WheelView;
import com.shwy.bestjoy.utils.AsyncTaskUtils;
import com.shwy.bestjoy.utils.DateUtils;
import com.shwy.bestjoy.utils.NetworkUtils;
import com.umpay.creditcard.android.UmpayActivity;

public class BookTableActivity extends BaseActionbarActivity implements OnClickListener, OnTouchListener {
	private static final String TAG = "BookTableActivity";
	private Button btn_chooseFood;
	// private RequestTableStyleThread tableStyleThread;
	private EditText edite_content;
	private TextView textView;
	private RelativeLayout tableRelativeLayout;
	private String se;
	private String tableStyleId;
	public static final int RESULT_CODE_NOFOUND = 200;
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private DisplayMetrics dm;
	private Bitmap bitmap;
	/** 最小缩放比例 */
	float minScaleR = 1.0f;
	/** 最大缩放比例 */
	static final float MAX_SCALE = 10f;
	/** 初始状态 */
	static final int NONE = 0;
	/** 拖动 */
	static final int DRAG = 1;
	/** 缩放 */
	static final int ZOOM = 2;
	/** 当前模式 */
	int mode = NONE;
	private PointF prev = new PointF();
	private PointF mid = new PointF();
	float dist = 1f;
	private TextView title_date;
	private ImageButton btn_left;
	private ImageButton btn_Right;
	Dialog dialog_calendar;
	CalendarView calendar;
	private Context context = BookTableActivity.this;
	private RelativeLayout layout_shoptable;
	private LinearLayout choose_time;
	private LinearLayout choose_seat;
	private final int requestCode = 888;
	private TableInfo tableInfo;
	private ImageButton btn_selectSeat;
	private Button btn_back;
	public static int hour;
	public static int minute;
	private ImageButton btn_select_time;

	private TextView textView_bookTime;
	private TextView textView_selectTime;
	private TextView textView_selectTable;
	private Button btn_commintButton;
	private boolean isTimeChosen = false;
	private Button btn_more;
	private ImageView tableImage;
	private boolean isTableChosen = false;
	private int tableId = -1;
	private Intent intent;
	private String mShopId;
	private String mDeskType;
	private String mShiduanName;
	private Display display;
	private Dialog dialog;
	private List<TableInfoObject> mShopAvailableTableList;
	private DeskListAdapter mDeskListAdapter;
	private String mSelectedDeskID;
	private String mDabiaoPrice;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		display = getWindowManager().getDefaultDisplay();
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.table_back);
		setContentView(R.layout.activity_booktable);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		intent = BookTableActivity.this.getIntent();
		mShopId = intent.getExtras().getString("shop_id");
		initialize();

	}

	/**
	 * 初始化一些控件
	 */
	@SuppressWarnings("deprecation")
	private void initialize() {

		textView = (TextView) findViewById(R.id.textView_attention);

		edite_content = (EditText) findViewById(R.id.editText_content);
		edite_content.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					if (edite_content.getText().toString().contains("备注")) {
						edite_content.setText("");
					}
				} else {

				}
			}
		});
		btn_chooseFood = (Button) findViewById(R.id.imageButton_pickfood);
		btn_chooseFood.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(context, "敬请期待", Toast.LENGTH_SHORT).show();
			}
		});

		textView_bookTime = (TextView) findViewById(R.id.textView_book_time);
		textView_selectTime = (TextView) findViewById(R.id.textView_selected_time);
		btn_select_time = (ImageButton) findViewById(R.id.button_select_time);
		btn_select_time.setOnClickListener(BookTableActivity.this);
		btn_selectSeat = (ImageButton) findViewById(R.id.button_select_table);
		btn_selectSeat.setOnClickListener(BookTableActivity.this);
		btn_commintButton = (Button) findViewById(R.id.button_commit);
		btn_commintButton.setOnClickListener(BookTableActivity.this);
		textView_selectTable = (TextView) findViewById(R.id.textView_selected_table);
		choose_time = (LinearLayout) findViewById(R.id.layout_choose_time);
		choose_seat = (LinearLayout) findViewById(R.id.layout_choose_seat);
		choose_time.setOnClickListener(BookTableActivity.this);
		choose_seat.setOnClickListener(BookTableActivity.this);
		layout_shoptable = (RelativeLayout) findViewById(R.id.layout_shoptable);
//		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
//		int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
//
//
		tableImage = (ImageView) findViewById(R.id.imageView_table_info);
//		int margin_int = com.lnwoowken.lnwoowkenbook.tools.Tools.dip2px(
//				context, 20);
//		RelativeLayout.LayoutParams l2 = new RelativeLayout.LayoutParams(
//				screenWidth, screenWidth * 235 / 480);
//
//		l2.setMargins(0, 0, 0, 0);
//
//		RelativeLayout.LayoutParams l1 = new RelativeLayout.LayoutParams(
//				screenWidth, screenWidth * 235 / 480);
//
//		l1.setMargins(0, Tools.dip2px(context, 50), 0, 0);
		tableRelativeLayout = (RelativeLayout) findViewById(R.id.layout_shoptable);
//
//		tableRelativeLayout.setLayoutParams(l1);
//		tableImage.setLayoutParams(l2);
		tableImage.setImageBitmap(bitmap);// 填充控件
		tableImage.setOnTouchListener(this);// 设置触屏监听
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);// 获取分辨率
		// minZoom();
		center();
		tableImage.setImageMatrix(matrix);
	}

	public TableInfo getTableInfoById() {
		return null;
	}


	/**
	 * 在这里接收并处理支付结果
	 * 
	 * @param requestCode
	 *            支付请求码
	 * @param resultCode
	 *            SDK固定返回88888
	 * @param data
	 *            支付结果和结果描述信息
	 * @author niexuyang 2012-8-20
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (this.requestCode == requestCode && resultCode == 88888) {
			Toast.makeText(
					this,
					resultCode + " umpResultMessage:"
							+ data.getStringExtra("umpResultMessage")
							+ "\n umpResultCode:"
							+ data.getStringExtra("umpResultCode")
							+ "\n orderId:" + data.getStringExtra("orderId"),
					Toast.LENGTH_LONG).show();
			super.onActivityResult(requestCode, resultCode, data);
		} else {
			Toast.makeText(this, "支付失败", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 调用SDK进行支付
	 * 
	 * @param tradNo
	 *            下单获得的交易号
	 * @param payType
	 *            当前需要的支付类型
	 * @author niexuyang 2012-8-28
	 */
	private void startSdkToPay(String tradNo, int payType) {
		// 跳转到SDK页面
		// 将输入的参数传入Activity
		Intent intent = new Intent();
		intent.putExtra("tradeNo", tradNo);
		intent.putExtra("payType", payType);
		intent.setClass(BookTableActivity.this, UmpayActivity.class);
		startActivityForResult(intent, requestCode);
	}

	@Override
	public void onClick(View v) {
		if (v.equals(btn_select_time) || v.equals(choose_time)) {
			dialog_calendar = new CalendarDialog(BookTableActivity.this,
					R.style.MyDialog);

			dialog_calendar.show();
			calendar = (CalendarView) dialog_calendar
					.findViewById(R.id.calendar);
			Button btn = (Button) dialog_calendar
					.findViewById(R.id.button_next);
			title_date = (TextView) dialog_calendar
					.findViewById(R.id.textView_title_date);
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					dialog_calendar.dismiss();
					//Toast.makeText(BookTableActivity.this, DateUtils.TOPIC_DATE_TIME_FORMAT.format(calendar.getSelectedStartDate()), Toast.LENGTH_SHORT).show();
					//queryAvilableTableSynckTAsk(DateUtils.TOPIC_DATE_TIME_FORMAT.format(calendar.getSelectedStartDate()));

					showTimeDialog();
				}
			});
			// 获取日历控件对象
			// calendar = (CalendarView)findViewById(R.id.calendar);
			// 获取日历中年月 ya[0]为年，ya[1]为月（格式大家可以自行在日历控件中改）
			String[] ya = calendar.getYearAndmonth().split("-");
			title_date.setText(ya[0] + "年" + ya[1]);

			btn_left = (ImageButton) dialog_calendar
					.findViewById(R.id.calendarLeft);
			btn_Right = (ImageButton) dialog_calendar
					.findViewById(R.id.calendarRight);
			btn_left.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 点击上一月 同样返回年月
					String leftYearAndmonth = calendar.clickLeftMonth();
					String[] lya = leftYearAndmonth.split("-");
					title_date.setText(lya[0] + "年" + lya[1]);
				}
			});
			btn_Right.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 点击下一月
					String rightYearAndmonth = calendar.clickRightMonth();
					String[] rya = rightYearAndmonth.split("-");
					title_date.setText(rya[0] + "年" + rya[1]);
				}
			});
		} else if (v.equals(btn_selectSeat) || v.equals(choose_seat)) {
			if (isTimeChosen) {
				queryAvilableTableSynckTAsk();
			} else {
				Toast.makeText(BookTableActivity.this, "请先选择时间",
						Toast.LENGTH_LONG).show();
			}
		} else if (v.equals(btn_commintButton)) {
			if (isTimeChosen) {
				if (MyAccountManager.getInstance().hasLoginned()) {
					StoreInfo shop;
					boolean b = false;
					Calendar c = Calendar.getInstance();
					c.setTime(calendar.getSelectedStartDate());
					if (!TextUtils.isEmpty(mSelectedDeskID)) {
						TableInfoObject shopAvailableTableObj = null;
						for(TableInfoObject obj : mShopAvailableTableList) {
							if(mSelectedDeskID.equals(obj.getDeskId())) {
								shopAvailableTableObj = obj;
							}
						}
						mDabiaoPrice = shopAvailableTableObj.getDabiaoPrice();
						PayInfo pay = new PayInfo();
						pay.setShopId(mShopId);
						pay.setTime(c.get(Calendar.MONTH) + 1 + this.getResources().getString(R.string.month) + c.get(Calendar.DAY_OF_MONTH) + this.getResources().getString(R.string.day) + " " + DateUtils.getInstance().getWeekDay(c) + " " + shopAvailableTableObj.getmShiduanTime());
						pay.setTableName(shopAvailableTableObj.getDeskName() + "  " + mDeskType.substring(0, mDeskType.indexOf("(")));
						pay.setTableId(shopAvailableTableObj.getDeskId());
						pay.setTablePrice(mDabiaoPrice);
						pay.setUid(MyAccountManager.getInstance().getCurrentAccountUid());
						//pay.setRprice(tempStyle.getPrice());
						pay.setSprice(shopAvailableTableObj.getServicePrice());
						//pay.setDtimeid(tableInfo.getAId());
						//pay.setSttid(tableStyleId + "");
						//pay.setSecid(se);
						pay.setContent(edite_content.getText().toString());
						Intent intent = new Intent();
						intent.setClass(context, CommitActivity.class);
						Bundle bundle = new Bundle();
						bundle.putParcelable("PayInfo", new PayInfoData(pay));
						intent.putExtras(bundle);

						startActivity(intent);
						// BookTableActivity.this.finish();
					} else {
						Toast.makeText(context, "请选择您要预定的桌子", Toast.LENGTH_SHORT).show();
					}
				} else {
					showLoginDialog();
				}
			} else {
				Toast.makeText(context, "请选择您要预定的时间", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private QueryAvilableTableSynckTAsk mQueryAvilableTableSynckTAsk;
	private void queryAvilableTableSynckTAsk(String... param) {
		showProgressDialog();
		AsyncTaskUtils.cancelTask(mQueryAvilableTableSynckTAsk);
		mQueryAvilableTableSynckTAsk = new QueryAvilableTableSynckTAsk();
		mQueryAvilableTableSynckTAsk.execute(param);
	}

	private class QueryAvilableTableSynckTAsk extends AsyncTask<String, Void, ServiceResultObject> {
		@Override
		protected ServiceResultObject doInBackground(String... params) {
			//更新保修卡信息
			ServiceResultObject serviceResultObject = new ServiceResultObject();
			InputStream is = null;
			try {
				JSONObject queryJsonObject = new JSONObject();
				queryJsonObject.put("shopid", mShopId);//mShopId
				queryJsonObject.put("date",  DateUtils.TOPIC_DATE_TIME_FORMAT.format(calendar.getSelectedStartDate()));
				queryJsonObject.put("desktype", mDeskType);//2人桌(1-2人) 4人桌(4-6人) 6人桌(8-10人) 包房
				queryJsonObject.put("shiduan_name", mShiduanName);

				is = NetworkUtils.openContectionLocked(ServiceObject.getAllAvailableTableUrl("para", queryJsonObject.toString()), null);
				serviceResultObject = ServiceResultObject.parseAvailableTables(NetworkUtils.getContentFromInput(is));
				mShopAvailableTableList = PatternInfoUtils.getShopAvailableTableList(serviceResultObject.mShops);
				DebugUtils.logD(TAG, "mShopAvailableTableList = " + mShopAvailableTableList);
				DebugUtils.logD(TAG, "StatusCode = " + serviceResultObject.mStatusCode);
				DebugUtils.logD(TAG, "StatusMessage = " + serviceResultObject.mStatusMessage);
			} catch (JSONException e) {
				DebugUtils.logD(TAG, "JSONException = " + e);
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
			} catch (IOException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
			}finally {
				NetworkUtils.closeInputStream(is);
			}
			return serviceResultObject;
		}

		@Override
		protected void onPostExecute(ServiceResultObject result) {
			super.onPostExecute(result);
			if(result.mShops == null || result.mShops.length() == 0) {
				MyApplication.getInstance().showMessage(R.string.shop_info_query_fail);
			} else {
				showDeskList();
			}
			dismissProgressDialog();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			dismissProgressDialog();
		}
	}

	private void dismissProgressDialog(){
		if(dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
	}
	
	private void showProgressDialog(){
		if(dialog != null && dialog.isShowing()) return; 
		dialog = new ProgressDialog(BookTableActivity.this, R.style.ProgressDialog);
		dialog.show();
		LinearLayout progress = (LinearLayout) dialog.findViewById(R.id.imageView_progress);
	
		progress.setBackgroundResource(R.anim.animition_progress); 
		final AnimationDrawable draw = (AnimationDrawable)progress.getBackground(); 
        draw.start();
        dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface arg0) {
				draw.stop();
			}
		});
//		progress.setIndeterminate(true);
		//setProgressBarIndeterminateVisibility(true); 
		dialog.setCanceledOnTouchOutside(false);
		// TableListDialog dialog = new
		// TableListDialog(BookTableActivity.this);
		// dialog.show();
	}
	private TableStyle findTableStyleById(int id, List<TableStyle> list) {
		List<TableStyle> styleList = list;
		TableStyle tempStyle = null;
		if (styleList != null) {
			for (int i = 0; i < styleList.size(); i++) {
				if (styleList.get(i).getId() == id) {
					tempStyle = styleList.get(i);
				}
			}
		}
		return tempStyle;
	}
	
	private void showDeskList() {

		final Dialog dialog = new DeskListDialog(BookTableActivity.this, R.style.MyDialog);
		dialog.show();
		ListView deskList = (ListView) dialog.findViewById(R.id.desklist);
		mDeskListAdapter = new DeskListAdapter(BookTableActivity.this);
		deskList.setAdapter(mDeskListAdapter);
		Button btnBack = (Button) dialog.findViewById(R.id.button_back);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		Button btnOk = (Button) dialog.findViewById(R.id.button_ok);
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mSelectedDeskID != null) {
					dialog.dismiss();
					isTableChosen = true;
				} else {
					MyApplication.getInstance().showMessage(R.string.select_desk_tips);
				}
			}
		});
	}
	public class DeskListAdapter extends BaseAdapter{
		private Context _context;
		private int month, day;
		private Calendar c = Calendar.getInstance();
		private DeskListAdapter (Context context) {
			_context = context;
			c.setTime(calendar.getSelectedStartDate());
			month = c.get(Calendar.MONTH) + 1;
			day =  c.get(Calendar.DAY_OF_MONTH);
		}
		@Override
		public int getCount() {
			return mShopAvailableTableList != null ? mShopAvailableTableList.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(_context).inflate(R.layout.listview_book_desk_item, parent, false);
				holder = new ViewHolder();
				holder._name = (TextView) convertView.findViewById(R.id.textView_deskname);
				holder._result = (TextView) convertView.findViewById(R.id.textView_result);
				holder._qiang = (Button) convertView.findViewById(R.id.button_qiang);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final Button btn = holder._qiang;
			holder._name.setText(mShopAvailableTableList.get(position).getDeskName());
			holder._result.setText(_context.getResources().getString(R.string.book_time) + month + _context.getResources().getString(R.string.month) + day + _context.getResources().getString(R.string.day) + DateUtils.getInstance().getWeekDay(c) + mShopAvailableTableList.get(position).getmShiduanTime());
			if(mShopAvailableTableList.get(position).getDeskId().equals(mSelectedDeskID)) {
				holder._qiang.setBackgroundResource(R.drawable.ump_forward_btn_forcus);
			} else {
				holder._qiang.setBackgroundResource(R.drawable.button_commit);
			}
			holder._qiang.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					mDeskListAdapter.notifyDataSetChanged();
					mSelectedDeskID = mShopAvailableTableList.get(position).getDeskId();
				}
			});
			return convertView;
		}

		private class ViewHolder {
			private TextView _name, _result;
			private Button _qiang;
			private boolean isChecked;
		}
	}

	private void showTimeDialog() {
		final Dialog dialog = new TimeDialog(BookTableActivity.this, R.style.MyDialog);
		dialog.show();
		
		final WheelView minute = (WheelView) dialog.findViewById(R.id.minute);
		ArrayListWheelAdapter arrayListWheelAdapter = new ArrayListWheelAdapter(BookTableActivity.this);
		arrayListWheelAdapter.setTextSize(26);
		minute.setViewAdapter(arrayListWheelAdapter);
		minute.setCyclic(true);
		minute.setCurrentItem(1);
		minute.setVisibleItems(2);
		Button btnBack = (Button) dialog.findViewById(R.id.button_back);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				BookTableActivity.this.onClick(btn_select_time);
			}
		});
		Button btnOk = (Button) dialog.findViewById(R.id.button_ok);
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mShiduanName = ArrayListWheelAdapter.SHIDUAN[minute.getCurrentItem()];
				dialog.dismiss();

				final Dialog tableStyle = new TimeDialog(BookTableActivity.this, R.style.MyDialog);

				tableStyle.show();
				
				final WheelView style = (WheelView) tableStyle.findViewById(R.id.minute);
				TableListWheelTextAdapter tableListWheelTextAdapter = new TableListWheelTextAdapter(BookTableActivity.this);
				tableListWheelTextAdapter.setTextSize(26);
				style.setViewAdapter(tableListWheelTextAdapter);
				style.setCyclic(true);
				style.setCurrentItem(1);
				style.setVisibleItems(3);
				TextView title = (TextView) tableStyle.findViewById(R.id.textView_title);
				title.setText("请选择桌型");
				Button btnBack = (Button) tableStyle.findViewById(R.id.button_back);
				btnBack.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						tableStyle.dismiss();
						BookTableActivity.this.showTimeDialog();
					}
				});
				Button btnOk = (Button) tableStyle.findViewById(R.id.button_ok);
				btnOk.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						tableStyle.dismiss();
						mDeskType = TableListWheelTextAdapter.DESK_TYPE[style.getCurrentItem()];
						StringBuilder str = new StringBuilder("你选择的日期是：" + DateUtils.TOPIC_DATE_TIME_FORMAT.format(calendar.getSelectedStartDate()) + "\n桌型：" + mDeskType + "\n时段：" + mShiduanName  + "\n达标金额：");
						if(!TextUtils.isEmpty(mDabiaoPrice)) str.append(mDabiaoPrice + "元"); else str.append("无"); 
						showDialog(str.toString());
					}
				});
			}
		});
	}

	private void showDialog(String str) {
		final Dialog alertDialog = new Dialog(BookTableActivity.this, R.style.MyDialog);
		alertDialog.setTitle("提示信息");
		alertDialog.setContentView(R.layout.dialog_layout);
		TextView title = (TextView) alertDialog.findViewById(R.id.title);
		TextView context = (TextView) alertDialog.findViewById(R.id.message);
		Button buttonOk = (Button) alertDialog.findViewById(R.id.button_ok);
		Button buttonCancel = (Button) alertDialog.findViewById(R.id.button_back);
		title.setText("提示信息");
		context.setText(str);
		alertDialog.setCanceledOnTouchOutside(false);
		buttonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isTimeChosen = true;
				isTableChosen = false;
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

	private void showLoginDialog() {
		final Dialog alertDialog = new Dialog(BookTableActivity.this, R.style.MyDialog);
		alertDialog.setTitle("提示");
		alertDialog.setContentView(R.layout.dialog_layout);
		TextView title = (TextView) alertDialog.findViewById(R.id.title);
		TextView context = (TextView) alertDialog.findViewById(R.id.message);
		Button buttonOk = (Button) alertDialog.findViewById(R.id.button_ok);
		Button buttonCancel = (Button) alertDialog.findViewById(R.id.button_back);
		title.setText("提示");
		context.setText("您还没有登录,请先登录");
		alertDialog.setCanceledOnTouchOutside(false);
		buttonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BookTableActivity.this, LoginActivity.class);
				startActivity(intent);
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

	public void SureOnClick(View v) {

	}

	/**
	 * 触屏监听
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		// 主点按下
		case MotionEvent.ACTION_DOWN:
			savedMatrix.set(matrix);
			prev.set(event.getX(), event.getY());
			mode = DRAG;
			break;
		// 副点按下
		case MotionEvent.ACTION_POINTER_DOWN:
			dist = spacing(event);
			// 如果连续两点距离大于10，则判定为多点模式
			if (spacing(event) > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			// savedMatrix.set(matrix);
			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - prev.x, event.getY()
						- prev.y);
			} else if (mode == ZOOM) {
				float newDist = spacing(event);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float tScale = newDist / dist;
					matrix.postScale(tScale, tScale, mid.x, mid.y);
				}
			}
			break;
		}
		tableImage.setImageMatrix(matrix);
		CheckView();
		return true;
	}

	/**
	 * 限制最大最小缩放比例，自动居中
	 */
	private void CheckView() {
		float p[] = new float[9];
		matrix.getValues(p);
		if (mode == ZOOM) {
			if (p[0] < minScaleR) {
				// Log.d("", "当前缩放级别:"+p[0]+",最小缩放级别:"+minScaleR);
				matrix.setScale(minScaleR, minScaleR);
			}
			if (p[0] > MAX_SCALE) {
				// Log.d("", "当前缩放级别:"+p[0]+",最大缩放级别:"+MAX_SCALE);
				matrix.set(savedMatrix);
			}
		}
		center();
	}

	/**
	 * 最小缩放比例，最大为100%
	 */
	private void minZoom() {
		minScaleR = Math.min(
				(float) dm.widthPixels / (float) bitmap.getWidth(),
				(float) dm.heightPixels / (float) bitmap.getHeight());
		if (minScaleR < 1.0) {
			matrix.postScale(minScaleR, minScaleR);
		}
	}

	private void center() {
		center(true, true);
	}

	/**
	 * 横向、纵向居中
	 */
	protected void center(boolean horizontal, boolean vertical) {

		Matrix m = new Matrix();
		m.set(matrix);
		RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
		m.mapRect(rect);

		float height = rect.height();
		float width = rect.width();

		float deltaX = 0, deltaY = 0;

		if (vertical) {
			// 图片小于屏幕大小，则居中显示。大于屏幕，上方留空则往上移，下方留空则往下移
			// int screenHeight = dm.heightPixels;
			int screenHeight = layout_shoptable.getHeight();
			if (height < screenHeight) {
				deltaY = (screenHeight - height) / 2 - rect.top;
			} else if (rect.top > 0) {
				deltaY = -rect.top;
			} else if (rect.bottom < screenHeight) {
				deltaY = tableImage.getHeight() - rect.bottom;
			}

		}

		if (horizontal) {
			// int screenWidth = dm.widthPixels;
			int screenWidth = layout_shoptable.getWidth();
			if (width < screenWidth) {
				deltaX = (screenWidth - width) / 2 - rect.left;
			} else if (rect.left > 0) {
				deltaX = -rect.left;
			} else if (rect.right < screenWidth) {
				deltaX = screenWidth - rect.right;
			}
		}
		matrix.postTranslate(deltaX, deltaY);
	}

	/**
	 * 两点的距离
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	/**
	 * 两点的中点
	 */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	public static class ImageViewOnClickListener implements
			View.OnClickListener {
		private Context context;
		private String img_path;

		public ImageViewOnClickListener(Context context, String img_path) {
			this.img_path = img_path;
			this.context = context;
		}

		@Override
		public void onClick(View v) {
			if (img_path != null) {
				Intent intent = new Intent(context, BookTableActivity.class);
				intent.putExtra("PhotoPath", img_path);
				context.startActivity(intent);
			}

		}
	}

	@Override
	protected boolean checkIntent(Intent intent) {
		return true;
	}
}
