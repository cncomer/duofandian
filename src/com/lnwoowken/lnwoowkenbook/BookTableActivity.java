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
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.service.PhotoManagerUtilsV2;
import com.cncom.app.base.ui.BaseActionbarActivity;
import com.cncom.app.base.util.DebugUtils;
import com.cncom.app.base.util.PatternInfoUtils;
import com.cncom.app.base.util.ShopInfoObject;
import com.cncom.app.base.util.TableInfoObject;
import com.lnwoowken.lnwoowkenbook.ServiceObject.ServiceResultObject;
import com.lnwoowken.lnwoowkenbook.model.StoreInfo;
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
import com.shwy.bestjoy.utils.Intents;
import com.shwy.bestjoy.utils.NetworkUtils;
import com.shwy.bestjoy.utils.NotifyRegistrant;

public class BookTableActivity extends BaseActionbarActivity implements OnClickListener, OnTouchListener {
	private static final String TAG = "BookTableActivity";
	// private RequestTableStyleThread tableStyleThread;
	private EditText mNoteInput;
	public static final int RESULT_CODE_NOFOUND = 200;
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private DisplayMetrics dm;
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
	private RelativeLayout mShopImgLayout;
	private final int requestCode = 888;

	private TextView mSelectTableView;
	private ImageView tableImage;
	private boolean isTableChosen = false;
	private String mDeskType;
	private String mShiduanName;
	private Dialog dialog;
	private List<TableInfoObject> mShopAvailableTableList;
	private DeskListAdapter mDeskListAdapter;
	private String mSelectedDeskID;
	private int mSelectedDeskPosition = -1;
	
	private ShopInfoObject mShopInfoObject;
	private Handler mHandler;
	private Bitmap mShopImageBitmap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFinishing()) {
			return;
		}
		PhotoManagerUtilsV2.getInstance().requestToken(TAG);
		setContentView(R.layout.activity_booktable);
		
		initialize();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		NotifyRegistrant.getInstance().unRegister(mHandler);
		PhotoManagerUtilsV2.getInstance().releaseToken(TAG);
	}

	/**
	 * 初始化一些控件
	 */
	private void initialize() {

		((TextView) findViewById(R.id.textView_attention)).setText(mShopInfoObject.mQiangweiTip);
		mNoteInput = (EditText) findViewById(R.id.editText_content);
		findViewById(R.id.imageButton_pickfood).setOnClickListener(this);//提前点菜，暂不支持

		findViewById(R.id.layout_choose_seat).setOnClickListener(this); //选择座位
		
		findViewById(R.id.button_commit).setOnClickListener(this);//提交订单
		mSelectTableView = (TextView) findViewById(R.id.textView_selected_table);
		
		mShopImgLayout = (RelativeLayout) findViewById(R.id.layout_shoptable);
		tableImage = (ImageView) findViewById(R.id.imageView_table_info);
		
		
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);// 获取分辨率
		// minZoom();
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch(msg.what) {
				case NotifyRegistrant.EVENT_NOTIFY_MESSAGE_RECEIVED:
					
					Bundle bundle = (Bundle) msg.obj;
	            	boolean status = bundle.getBoolean(PhotoManagerUtilsV2.EXTRA_DOWNLOAD_STATUS);
	            	String photoid = bundle.getString(Intents.EXTRA_PHOTOID);
					
					if (photoid.equals(mShopInfoObject.getShopPhotoId("02"))) {
						tableImage.setOnTouchListener(BookTableActivity.this);// 设置触屏监听
						mShopImageBitmap = ((BitmapDrawable) tableImage.getDrawable()).getBitmap();
						center();
						tableImage.setImageMatrix(matrix);
					}
					break;
				}
				
			}
		};
		NotifyRegistrant.getInstance().register(mHandler);
		PhotoManagerUtilsV2.getInstance().loadPhotoAsync(TAG, tableImage, mShopInfoObject.getShopPhotoId("02"), null, PhotoManagerUtilsV2.TaskType.SHOP_IMAGE);
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
			MyApplication.getInstance().showMessage(resultCode + " umpResultMessage:"
					+ data.getStringExtra("umpResultMessage")
					+ "\n umpResultCode:"
					+ data.getStringExtra("umpResultCode")
					+ "\n orderId:" + data.getStringExtra("orderId"));
			super.onActivityResult(requestCode, resultCode, data);
		} else {
			MyApplication.getInstance().showMessage(R.string.msg_pay_failed);
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.imageButton_pickfood: //提前点菜
			MyApplication.getInstance().showUnsupportMessage();
			break;
		case R.id.layout_choose_seat: //选择桌位
			dialog_calendar = new CalendarDialog(BookTableActivity.this, R.style.MyDialog);
			dialog_calendar.show();
			calendar = (CalendarView) dialog_calendar.findViewById(R.id.calendar);
			Button btn = (Button) dialog_calendar.findViewById(R.id.button_next);
			title_date = (TextView) dialog_calendar.findViewById(R.id.textView_title_date);
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					dialog_calendar.dismiss();

					showTimeDialog();
				}
			});
			// 获取日历控件对象
			// calendar = (CalendarView)findViewById(R.id.calendar);
			// 获取日历中年月 ya[0]为年，ya[1]为月（格式大家可以自行在日历控件中改）
			String[] ya = calendar.getYearAndmonth().split("-");
			title_date.setText(ya[0] + "年" + ya[1]);

			btn_left = (ImageButton) dialog_calendar.findViewById(R.id.calendarLeft);
			btn_Right = (ImageButton) dialog_calendar.findViewById(R.id.calendarRight);
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
			break;
		case R.id.button_commit: //提交订单
			if (mSelectedDeskPosition != -1) {
				if (MyAccountManager.getInstance().hasLoginned()) {
					TableInfoObject shopAvailableTableObj = mShopAvailableTableList.get(mSelectedDeskPosition);
					Calendar c = Calendar.getInstance();
					c.setTime(calendar.getSelectedStartDate());
//					shopAvailableTableObj.setTime(c.get(Calendar.MONTH) + 1 + this.getResources().getString(R.string.month) + c.get(Calendar.DAY_OF_MONTH) + this.getResources().getString(R.string.day) + " " + DateUtils.getInstance().getWeekDay(c) + " " + shopAvailableTableObj.getmShiduanTime());
					shopAvailableTableObj.setUid(MyAccountManager.getInstance().getCurrentAccountUid());
					shopAvailableTableObj.setNote(mNoteInput.getText().toString());
					shopAvailableTableObj.setShopId(mShopInfoObject.getShopID());
					Intent intent = new Intent();
					intent.setClass(context, CommitActivity.class);
					Bundle bundle = new Bundle();
					bundle.putParcelable("shopobject", shopAvailableTableObj);
					intent.putExtras(bundle);

					startActivity(intent);
				} else {
					showLoginDialog();
				}
			} else {
				MyApplication.getInstance().showMessage(R.string.selected_table_info);
			}
			break;
			default:
				super.onClick(v);
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
				queryJsonObject.put("shopid", mShopInfoObject.getShopID());
				queryJsonObject.put("date",  DateUtils.TOPIC_DATE_TIME_FORMAT.format(calendar.getSelectedStartDate()));
				queryJsonObject.put("desktype", mDeskType);//2人桌(1-2人) 4人桌(4-6人) 6人桌(8-10人) 包房
				queryJsonObject.put("shiduan_name", mShiduanName);

				is = NetworkUtils.openContectionLocked(ServiceObject.getAllAvailableTableUrl("para", queryJsonObject.toString()), null);
				serviceResultObject = ServiceResultObject.parseJsonArray(NetworkUtils.getContentFromInput(is));
				mShopAvailableTableList = PatternInfoUtils.getShopAvailableTableList(serviceResultObject.mJsonArrayData);
				DebugUtils.logD(TAG, "mShopAvailableTableList = " + mShopAvailableTableList);
				DebugUtils.logD(TAG, "StatusCode = " + serviceResultObject.mStatusCode);
				DebugUtils.logD(TAG, "StatusMessage = " + serviceResultObject.mStatusMessage);
			} catch (JSONException e) {
				DebugUtils.logD(TAG, "JSONException = " + e);
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
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
			dismissProgressDialog();
			//现在服务器如果没有查询到该时段的桌子，任然是返回的成功，所以这里额外的做了一下判断
			if (result.isOpSuccessfully()) {
				if (mShopAvailableTableList.size() > 0) {
					showDeskList();
				} else {
					MyApplication.getInstance().showMessage(R.string.shop_info_query_fail);
				}
			} else {
				MyApplication.getInstance().showMessage(result.mStatusMessage);
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			dismissProgressDialog();
		}
	}

	private void dismissProgressDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
	}

	private void showProgressDialog() {
		if (dialog != null && dialog.isShowing())
			return;
		dialog = new ProgressDialog(BookTableActivity.this, R.style.ProgressDialog);
		dialog.show();
		LinearLayout progress = (LinearLayout) dialog.findViewById(R.id.imageView_progress);

		progress.setBackgroundResource(R.anim.animition_progress);
		final AnimationDrawable draw = (AnimationDrawable) progress.getBackground();
		draw.start();
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface arg0) {
				draw.stop();
			}
		});
		dialog.setCanceledOnTouchOutside(false);
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
				if(mSelectedDeskPosition != -1) {
					dialog.dismiss();
					setTableStatus();
					showDialog();
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
					mSelectedDeskPosition = position;
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
		mSelectedDeskPosition = -1;
		final Dialog dialog = new TimeDialog(BookTableActivity.this, R.style.MyDialog);
		dialog.show();
		
		final WheelView minute = (WheelView) dialog.findViewById(R.id.minute);
		ArrayListWheelAdapter arrayListWheelAdapter = new ArrayListWheelAdapter(BookTableActivity.this);
		arrayListWheelAdapter.setTextSize(26);
		minute.setViewAdapter(arrayListWheelAdapter);
		minute.setCyclic(true);
		minute.setCurrentItem(1);
		minute.setVisibleItems(3);
		Button btnBack = (Button) dialog.findViewById(R.id.button_back);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				BookTableActivity.this.onClick(findViewById(R.id.layout_choose_seat));
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
						isTableChosen = false;
						mSelectedDeskID = null;
						queryAvilableTableSynckTAsk();
					}
				});
			}
		});
	}

	/***
	 * 设置选中的座位信息，如您已选择 9月4号 晚市 2人桌 H21
	 */
	private void setTableStatus() {
		if (mSelectedDeskPosition != -1) {
			TableInfoObject shopAvailableTableObj = mShopAvailableTableList.get(mSelectedDeskPosition);
			Calendar c = Calendar.getInstance();
			c.setTime(calendar.getSelectedStartDate());
			String deskType = shopAvailableTableObj.getDeskType();
			int igonre = deskType.indexOf("(");
			if (igonre > 0) {
				deskType = deskType.substring(0, igonre);
			}
			mSelectTableView.setText(getString(R.string.table_selected_status_format, TableInfoObject.BILL_ORDER_DATE_FORMAT.format(shopAvailableTableObj.getOrderDate()) + " " + shopAvailableTableObj.getShiduanName() + " " + deskType + " " + shopAvailableTableObj.getDeskName()));
		}
		
		
	}
	private void showDialog() {
		StringBuilder str = new StringBuilder("你选择的日期是：" + DateUtils.TOPIC_DATE_TIME_FORMAT.format(calendar.getSelectedStartDate()) + "\n桌型：" + mDeskType + "\n时段：" + mShiduanName  + "\n达标金额：");
		if(mShopAvailableTableList.size() > 0 && mShopAvailableTableList.get(mSelectedDeskPosition).getDabiaoPrice() != null) str.append(mShopAvailableTableList.get(mSelectedDeskPosition).getDabiaoPrice() + "元"); else str.append("无"); 
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

	private void center() {
		center(true, true);
	}

	/**
	 * 横向、纵向居中
	 */
	protected void center(boolean horizontal, boolean vertical) {

		if (mShopImageBitmap == null) {
			return;
		}
		Matrix m = new Matrix();
		m.set(matrix);
		RectF rect = new RectF(0, 0, mShopImageBitmap.getWidth(), mShopImageBitmap.getHeight());
		m.mapRect(rect);

		float height = rect.height();
		float width = rect.width();

		float deltaX = 0, deltaY = 0;

		if (vertical) {
			// 图片小于屏幕大小，则居中显示。大于屏幕，上方留空则往上移，下方留空则往下移
			// int screenHeight = dm.heightPixels;
			int screenHeight = mShopImgLayout.getHeight();
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
			int screenWidth = mShopImgLayout.getWidth();
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

	@Override
	protected boolean checkIntent(Intent intent) {
		String shopId = intent.getExtras().getString("shop_id");
		mShopInfoObject = ShopInfoObject.getShopInfoObjectByShopId(getContentResolver(), shopId);
		if (mShopInfoObject == null || mShopInfoObject.getShopID() == null) {
			DebugUtils.logD(TAG, "checkIntent failed mShopInfoObject=" + mShopInfoObject);
			return false;
		}
		return true;
	}
}
