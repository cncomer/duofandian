package com.lnwoowken.lnwoowkenbook;

import java.lang.reflect.Field;

import java.util.Date;
import java.util.List;
import com.lnwoowken.lnwoowkenbook.adapter.TableListAdapter;
import com.lnwoowken.lnwoowkenbook.data.PayInfoData;

import com.lnwoowken.lnwoowkenbook.model.BookTime;
import com.lnwoowken.lnwoowkenbook.model.Contant;
import com.lnwoowken.lnwoowkenbook.model.PayInfo;
import com.lnwoowken.lnwoowkenbook.model.StoreInfo;
import com.lnwoowken.lnwoowkenbook.model.TableInfo;
import com.lnwoowken.lnwoowkenbook.model.TableStyle;
import com.lnwoowken.lnwoowkenbook.tools.Tools;
import com.lnwoowken.lnwoowkenbook.network.Client;
import com.lnwoowken.lnwoowkenbook.network.JsonParser;

import com.lnwoowken.lnwoowkenbook.view.CalendarDialog;
import com.lnwoowken.lnwoowkenbook.view.CalendarView;
import com.lnwoowken.lnwoowkenbook.view.TimeDialog;

import com.lnwoowken.lnwoowkenbook.view.MyDialog;
import com.lnwoowken.lnwoowkenbook.view.TimeView.ArrayListWheelAdapter;
import com.lnwoowken.lnwoowkenbook.view.TimeView.ArrayWheelAdapter;
import com.lnwoowken.lnwoowkenbook.view.TimeView.NumericWheelAdapter;
import com.lnwoowken.lnwoowkenbook.view.TimeView.TableListWheelTextAdapter;
import com.lnwoowken.lnwoowkenbook.view.TimeView.WheelView;

import com.umpay.creditcard.android.UmpayActivity;
import com.umpay.creditcard.android.e;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;

import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

@SuppressLint({ "HandlerLeak", "FloatMath" })
@SuppressWarnings("unused")
public class BookTableActivity extends Activity implements OnClickListener,
		OnTouchListener {
	private boolean isAccept = false;
	private Button btn_chooseFood;
	// private RequestTableStyleThread tableStyleThread;
	private EditText edite_content;
	private TextView textView;
	private String minPrice;
	private RelativeLayout tableRelativeLayout;
	private String se;
	private String tableStyleId;
	private List<TableStyle> list_tableStyle;
	private static final String TAG = "PhotoViewer";
	public static final int RESULT_CODE_NOFOUND = 200;
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private DisplayMetrics dm;
	private Bitmap bitmap;
	/** ��С���ű��� */
	float minScaleR = 1.0f;
	/** ������ű��� */
	static final float MAX_SCALE = 10f;
	/** ��ʼ״̬ */
	static final int NONE = 0;
	/** �϶� */
	static final int DRAG = 1;
	/** ���� */
	static final int ZOOM = 2;
	/** ��ǰģʽ */
	int mode = NONE;
	private PointF prev = new PointF();
	private PointF mid = new PointF();
	float dist = 1f;

	private RequestTimeThread timeThread;
	private PopupWindow popupWindow;
	private Button btn_home;
	private TextView title_date;
	private ImageButton btn_left;
	private ImageButton btn_Right;
	private int mHour;
	private int mMinute;
	Dialog dialog_calendar;
	private String selectDate;
	// private String selectTime;
	// private ImageButton btn_bottom_home;
	// private ImageButton btn_bottom_eat;
	// private ImageButton btn_bottom_my;
	CalendarView calendar;
	private Context context = BookTableActivity.this;
	private RelativeLayout layout_shoptable;
	private LinearLayout choose_time;
	private LinearLayout choose_seat;
	private final int requestCode = 888;
	// private int tablePosition = -1;
	private List<TableInfo> tableList;
	private TableInfo tableInfo;
	// private RequestTableInfoThread tableThread;
	private String time1 = "";
	private ImageButton btn_selectSeat;
	private Button btn_back;
	public static int hour;
	public static int minute;
	private boolean readTimeOver = false;
	private ImageButton btn_select_time;

	private TextView textView_bookTime;
	private TextView textView_selectTime;
	private TextView textView_selectTable;
	private List<BookTime> time_list;
	private Button btn_commintButton;
	// private DateWidget dialog;
	// private Dialog calendarDialog;
	private boolean isTimeChosen = false;
	private String tableName;
	// private String tablePrice;
	private Button btn_more;
	private ImageView tableImage;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.arg1 == 1) {
				myThread.start();
			}

		}

	};

	private Handler initial_view_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (time_list != null) {

				String str = "��Ԥ����ʱ��:\n\t";
				for (int i = 0; i < time_list.size(); i++) {
					str += time_list.get(i).getRsTime().replace("/", "-") + "~"
							+ time_list.get(i).getRdTime().replace("/", "-")
							+ "\n\t";
				}
				Tools.findShopById(shopId).setTimeList(time_list);
				Log.d("time_________", str);
				readTimeOver = true;
				Message msg1 = new Message();
				handler_showTimeDialog.sendMessage(msg1);
			}

		}
	};

	private Handler setTable_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			RequestTableInfoThread tableThread = new RequestTableInfoThread();
			tableThread.setSe(se);
			tableThread.setTableStyleId(tableStyleId);
			tableThread.start();
		}
	};

	private Handler handler_showTimeDialog = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			showTimeDialog();
		}
	};

	/**
	 * �ӷ�������ȡ�����̵Ŀ�Ԥ��ʱ��
	 * 
	 * @author sean
	 * 
	 */
	public class RequestTableStyleThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			// String op = "{\"id\":\"" + shopId + "\"}";
			// op = Client.encodeBase64(op);
			// String str = Tools.getRequestStr(Contant.SERVER_IP,
			// Contant.SERVER_PORT + "", "shop?id=", "s6", "&op=" + op);
			// String result = Client.executeHttpGetAndCheckNet(str,
			// BookTableActivity.this);
			// if (result.equals(Contant.NO_NET)) {
			// Message msg = new Message();
			// msg.arg1 = 1;
			// initial_view_handler.sendMessage(msg);
			// } else {
			//
			// result = Client.decodeBase64(result);
			// if (result != null && !result.equals("")) {
			// time_list = JsonParser.parseTimeInfo(result);
			// if (time_list != null) {
			// Message msg = new Message();
			// initial_view_handler.sendMessage(msg);
			// }
			// }
			// }

			String op = "{\"Sid\":\"" + shopId + "\""
					+ ",\"vd\":\"0\",\"vc\":\"0\"" + "}";
			op = Client.encodeBase64(op);
			String str = Tools.getRequestStr(Contant.SERVER_IP,
					Contant.SERVER_PORT + "", "shop?id=", "s9", "&op=" + op);
			String result = Client.executeHttpGetAndCheckNet(str,
					BookTableActivity.this);
			result = Client.decodeBase64(result);
			Log.d("s9============", "result:" + result);
			if (result.equals(Contant.NO_NET)) {
				Message msg = new Message();
				msg.arg1 = 1;
				initial_view_handler.sendMessage(msg);
			} else {

				// result = Client.decodeBase64(result);
				if (result != null && !result.equals("")) {
					list_tableStyle = JsonParser.parseTableStylefo(result);

					if (list_tableStyle != null) {

					}
				}
			}
			readTimeOver = true;

			// Log.d("timeinfo==============url", str);
			// Log.d("timeinfo==============result", result);

		}
	}

	/**
	 * �ӷ�������ȡ��ѡʱ��ĸ����̵����п�Ԥ�����ӵ���Ϣ
	 * 
	 * @author sean
	 * 
	 */
	public class RequestTimeThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();

			String[] dateArr = calendar.getYearAndmonth().split("-");
			String date;
			if (selectDate==null||selectDate.equals("")) {
				selectDate = Tools.dateToString(calendar.getToday(), "MEDIUM");
				date = Tools.dateToString(calendar.getToday(), "MEDIUM");
				if (date.contains("��")) {
					date = date.replace("��", "-");
					
				}
				if (date.contains("��")) {
					date = date.replace("��", "-");
					
				}
				if (date.contains("��")) {
					date = date.replace("��", "");
					
				}
				Log.d("selected Date----today:====================", date);
			}
			else {
				
				date = selectDate;
				if (date.contains("��")) {
					date = date.replace("��", "-");
					
				}
				if (date.contains("��")) {
					date = date.replace("��", "-");
					
				}
				if (date.contains("��")) {
					date = date.replace("��", "");
					
				}
			}
			Log.d("selected Date:====================", date);
			String jsonStr = "{\"Sid\":\"" + shopId + "\",\"rt\":\"" + date
					+ "\"}";
			jsonStr = Client.encodeBase64(jsonStr);
			String str = Tools.getRequestStr(Contant.SERVER_IP,
					Contant.SERVER_PORT + "", "shopTable?id=", "st7", "&op="
							+ jsonStr);
			String result = Client.executeHttpGetAndCheckNet(str,
					BookTableActivity.this);
			result = Client.decodeBase64(result);

			Log.d("st7===============", result);
			if (result != null) {
				if (JsonParser.checkError(result)) {
					Log.d("st7 error===============", result);
					Message msg = new Message();
					msg.arg1 = 1;
					toastHandler.sendMessage(msg);
				} else {
					// result = Client.decodeBase64(result);
					if (result != null && !result.equals("")) {
						time_list = JsonParser.parseTimeInfo(result);
						if (time_list != null) {
							Message msg = new Message();
							initial_view_handler.sendMessage(msg);
						}
					}
				}

			}

			
		}
	}

	/**
	 * �ӷ�������ȡ��ѡʱ��ĸ����̵����п�Ԥ�����ӵ���Ϣ
	 * 
	 * @author sean
	 * 
	 */
	public class RequestTableInfoThread extends Thread {
		private String se;
		private String tableStyle;

		public String getTableStyleId() {
			return tableStyle;
		}

		public void setTableStyleId(String tableStyleId) {
			this.tableStyle = tableStyleId;
		}

		public String getSe() {
			return se;
		}

		public void setSe(String se) {
			this.se = se;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();

			// String jsonStr = "{\"id\":\"" + tableStyle +
			// "\",\"se\":\""+se+"\",\"dt\":\"" + selectDate
			if (selectDate.contains("��")) {
				selectDate = selectDate.replace("��", "-");
			}
			if (selectDate.contains("��")) {
				selectDate = selectDate.replace("��", "-");
			}
			if (selectDate.contains("��")) {
				selectDate = selectDate.replace("��", "");
			}
			
			//selectDate = selectDate.replace("��", "-");
			String jsonStr = "{\"id\":\"" + tableStyle + "\",\"se\":\"" + se
					+ "\",\"dt\":\"" + selectDate + "\"}";// "{\"Sid\":\""+shopId+"\",\"Tid\":\"1\",\"RSTime\":\""+time+"\"}";
			jsonStr = Client.encodeBase64(jsonStr);
			String str = Tools.getRequestStr(Contant.SERVER_IP,
					Contant.SERVER_PORT + "", "shop?id=", "s10", "&op="
							+ jsonStr); 
			String result = Client.executeHttpGetAndCheckNet(str,
					BookTableActivity.this);
			result = Client.decodeBase64(result);

			Log.d("s10===============", result);
			if (result != null) {
				if (JsonParser.checkError(result)) {

					
					Message msg = new Message();
					msg.arg1 = 1;
					toastHandler.sendMessage(msg); 
				} else {
					tableList = JsonParser.parseTableInfoJson(result);
					Log.d("RequestTableInfoThread=========", tableList.size()+"");
					if (tableList.size() > 0) {
						Message msg = new Message();
						showTableListDialog.sendMessage(msg);
					} else {

						Message msg = new Message();
						msg.arg1 = 2;
						toastHandler.sendMessage(msg);
					}
				}

			}
			//

			Log.d("shopId===============" + shopId, "time===========" + time1);
			// String jsonStr = "{\"sid\":\"" + shopId + "\",\"rt\":\"" + time1
			// + "\"}";//
			// "{\"Sid\":\""+shopId+"\",\"Tid\":\"1\",\"RSTime\":\""+time+"\"}";
			// jsonStr = Client.encodeBase64(jsonStr);
			// String str = Tools.getRequestStr(Contant.SERVER_IP,
			// Contant.SERVER_PORT + "", "shopTable?id=", "st6", "&op="
			// + jsonStr);
			// String result = Client.executeHttpGetAndCheckNet(str,
			// BookTableActivity.this);
			// result = Client.decodeBase64(result);
			//
			// Log.d("st6===============", result);
			// if (result != null) {
			// if (JsonParser.checkError(result)) {
			//
			// Message msg = new Message();
			// msg.arg1 = 1;
			// toastHandler.sendMessage(msg);
			// } else {
			// // tableList = JsonParser.parseTableInfoJson(result);
			// // if (tableList.size() > 0) {
			// // Message msg = new Message();
			// // showTableListDialog.sendMessage(msg);
			// // } else {
			// //
			// // Message msg = new Message();
			// // msg.arg1 = 2;
			// // toastHandler.sendMessage(msg);
			// // }
			// }
			//
			// }
			// //

		}
	}

	private Handler toastHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.arg1 == 1) {
				Toast.makeText(context,
						"���ʷ����������" + context.getString(R.string.no_data),
						Toast.LENGTH_SHORT).show();
			}
			if (msg.arg1 == 2) {
				Toast.makeText(BookTableActivity.this,
						"�Բ���,����ѡ���ʱ�䵱ǰû���κ����ӿ���Ԥ��", Toast.LENGTH_LONG).show();
			}

		}

	};

	public class RequestPayInfoThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			// Log.d("___________====", "I'm in");
			// Log.d("___________====", url);
			String content = edite_content.getText().toString();
			if (content==null||content.equals("")) {
				content = "";
			}
			String jsonStr = "{\"uid\":\"1\",\"sid\":\"" + shopId
					+ "\",\"tid\":\"" + tableId
					+ "\",\"price\":\"0.01\",\"strdr\":\"" + time1 + "\",\"content\":\""+content+"\"}";
			// + time1 + "\"}";//
			// "{\"Sid\":\""+shopId+"\",\"Tid\":\"1\",\"RSTime\":\""+time+"\"}";
			jsonStr = Client.encodeBase64(jsonStr);
			// String url = "http://" + Contant.SERVER_IP + ":"
			// + Contant.SERVER_PORT + "/javadill//Reserve?id=Rl3&op="
			// + jsonStr;

			String str = Tools.getRequestStr(Contant.SERVER_IP,
					Contant.SERVER_PORT + "", "Reserve?id=", "Rl3", "&op="
							+ jsonStr);
			// Log.d("url___________====", url);
			String result = Client.executeHttpGetAndCheckNet(str,
					BookTableActivity.this);

			// Log.d("url___________====", result);
			if (result != null) {
				// Toast.makeText(BookTableActivity.this,
				// result,Toast.LENGTH_LONG).show();
			}

			result = Client.decodeBase64(result);

			if (result != null) {
				Toast.makeText(BookTableActivity.this, result,
						Toast.LENGTH_LONG).show();
				Log.d("pay", str);
				Log.d("pay", result);
				List<PayInfo> payList = JsonParser.parsePayInfoJson(result);
				PayInfo pay = payList.get(0);
				startSdkToPay(pay.gettNumber(), 9);

			}

		}
	}

	private Handler showTableListDialog = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			final Dialog dialog = new MyDialog(BookTableActivity.this,
					R.style.MyDialog);

			dialog.show();
			ListView list = (ListView) dialog
					.findViewById(R.id.listView_table_list);
			list.setAdapter(new TableListAdapter(BookTableActivity.this,
					tableList));
			list.setDivider(null);
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					// Toast.makeText(BookTableActivity.this,
					// ""+arg2,Toast.LENGTH_LONG).show();
					tableInfo = null;
					tableInfo = tableList.get(arg2);
					if (tableInfo != null) {
						textView_selectTable.setText("��ѡ���������:"
								+ tableInfo.getAname());
					}

					dialog.dismiss();
				}
			});
			Button btn = (Button) dialog.findViewById(R.id.button_table_commit);
			btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					dialog.dismiss();
				}
			});

			// TableListDialog dialog = new
			// TableListDialog(BookTableActivity.this);
			// dialog.show();
		}

	};

	private Handler payHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			RequestPayInfoThread payThread = new RequestPayInfoThread();
			payThread.run();

		}

	};

	private boolean isTableChosen = false;
	private int tableId = -1;
	private RequestTableStyleThread myThread;
	private Intent intent;
	private int shopId;
	private Display display;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		display = getWindowManager().getDefaultDisplay();
		bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.table_back);
		setContentView(R.layout.activity_booktable);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		intent = BookTableActivity.this.getIntent();
		shopId = intent.getExtras().getInt("shopId");
		initialize();
		Log.d("===============shopid", shopId + "");
		myThread = new RequestTableStyleThread();
		// tableThread = new RequestTableInfoThread();
		Message msg = new Message();
		msg.arg1 = 1;
		handler.sendMessage(msg);
		textView = (TextView) findViewById(R.id.textView_attention);
		
		
		edite_content = (EditText) findViewById(R.id.editText_content);
		edite_content.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					if (edite_content.getText().toString().contains("��ע")) {
						edite_content.setText("");
					}
				}else {
					
				}
			}
		});
		
		btn_chooseFood = (Button) findViewById(R.id.imageButton_pickfood);
		btn_chooseFood.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "�����ڴ�", Toast.LENGTH_SHORT).show();
			}
		});
		// tableStyleThread = new RequestTableStyleThread();
		// Message msg2 = new Message();
		//
		// msg2.arg1 = 2;
		// handler.sendMessage(msg2);

	}

	/**
	 * ��ʼ��һЩ�ؼ�
	 */
	@SuppressWarnings("deprecation")
	private void initialize() {

		btn_back = (Button) findViewById(R.id.Button_back);
		btn_back.setOnClickListener(BookTableActivity.this);
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
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
		
		
	//	int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		//	int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
			
		
		
		
		// int margin_int = Tools.dip2px(context, 20);
		// LinearLayout.LayoutParams l2 = new LinearLayout.LayoutParams(
		// screenWidth, (int) (screenWidth / 2.1518987 + 0.5));
		// // l2.setMargins(margin_int, 0,margin_int, 0);
		// layout_shoptable.setLayoutParams(l2);
		btn_home = (Button) findViewById(R.id.button_home);
		btn_home.setOnClickListener(BookTableActivity.this);
		btn_more = (Button) findViewById(R.id.button_more);
		btn_more.setOnClickListener(BookTableActivity.this);

		tableImage = (ImageView) findViewById(R.id.imageView_table_info);
		int margin_int = com.lnwoowken.lnwoowkenbook.tools.Tools.dip2px(
				context, 20);
		RelativeLayout.LayoutParams l2 = new RelativeLayout.LayoutParams(
				screenWidth,
				screenWidth*235/480);
		
		l2.setMargins(0, 0, 0, 0);
		
		RelativeLayout.LayoutParams l1 = new RelativeLayout.LayoutParams(
				screenWidth,
				screenWidth*235/480);
		
		l1.setMargins(0, Tools.dip2px(context, 50), 0, 0);
		tableRelativeLayout = (RelativeLayout) findViewById(R.id.layout_shoptable);
		
		tableRelativeLayout.setLayoutParams(l1);
		tableImage.setLayoutParams(l2);
		tableImage.setImageBitmap(bitmap);// ���ؼ�
		tableImage.setOnTouchListener(this);// ���ô�������
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);// ��ȡ�ֱ���
		// minZoom();
		center();
		tableImage.setImageMatrix(matrix);

	}

	public TableInfo getTableInfoById() {
		return null;
	}

	// /**
	// * ���ôӷ������õ��ĸ����̵Ŀ�Ԥ��ʱ�䵽TEXTVIEW
	// */
	// private Handler setTimeHandler = new Handler() {
	//
	// @Override
	// public void handleMessage(Message msg) {
	// // TODO Auto-generated method stub
	// super.handleMessage(msg);
	//
	//
	// if (dialog_calendar != null) {
	// time1 = getTimeString(calendar);
	// // String timeStr = time1.replace("%20", " ");
	// textView_selectTime.setText("��ѡ���ʱ����:" + time1);
	// // boolean b = Tools.checkTime(find);
	// // Log.d("checktime____________", b+"");
	// Log.d("ѡ���ʱ��", time1);
	// isTimeChosen = true;
	// isTableChosen = false;
	//
	// }
	// }
	//
	// };

	private String getTimeString(CalendarView dialog) {

		String sHour = "00";
		String sMinute = "00";
		sHour = addZero(sHour, mHour);
		sMinute = addZero(sMinute, mMinute);
		return selectDate + " " + sHour + ":" + sMinute + ":" + "00";

	}

	private String addZero(String str, int res) {
		String temp = str;
		if (res >= 0 && res < 10) {
			temp = "0" + res;
		} else {
			temp = "" + res;
		}

		return temp;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		//
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			BookTableActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * ��������ղ�����֧�����
	 * 
	 * @param requestCode
	 *            ֧��������
	 * @param resultCode
	 *            SDK�̶�����88888
	 * @param data
	 *            ֧������ͽ��������Ϣ
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
			Toast.makeText(this, "֧��ʧ��", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * ����SDK����֧��
	 * 
	 * @param tradNo
	 *            �µ���õĽ��׺�
	 * @param payType
	 *            ��ǰ��Ҫ��֧������
	 * @author niexuyang 2012-8-28
	 */
	private void startSdkToPay(String tradNo, int payType) {
		// ��ת��SDKҳ��
		// ������Ĳ�������Activity
		Intent intent = new Intent();
		intent.putExtra("tradeNo", tradNo);
		intent.putExtra("payType", payType);
		intent.setClass(BookTableActivity.this, UmpayActivity.class);
		startActivityForResult(intent, requestCode);
	}

	/** * �����ֻ��ķֱ��ʴ�px(����) �ĵ�λ ת��Ϊdp */
	public static int pxToDip(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	class calendarItemClickListener implements CalendarView.OnItemClickListener {

		@Override
		public void OnItemClick(Date date) {
			// TODO Auto-generated method stub

			selectDate = Tools.dateToString(date, "MEDIUM");
			Toast.makeText(getApplicationContext(), selectDate + "",
					Toast.LENGTH_SHORT).show();
		}
	}

	private Handler timehandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			timeThread.start();
		}

	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(btn_back)) {
			BookTableActivity.this.finish();
		} else if (v.equals(btn_select_time) || v.equals(choose_time)) {
			if (readTimeOver) {

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
						// TODO Auto-generated method stub

						dialog_calendar.dismiss();
						timeThread = new RequestTimeThread();
						Message msg = new Message();
						timehandler.sendMessage(msg);

						// showTimeDialog();
					}
				});
				// ��ȡ�����ؼ�����
				// calendar = (CalendarView)findViewById(R.id.calendar);
				// ��ȡ���������� ya[0]Ϊ�꣬ya[1]Ϊ�£���ʽ��ҿ��������������ؼ��иģ�
				String[] ya = calendar.getYearAndmonth().split("-");
				title_date.setText(ya[0] + "��" + ya[1]);

				// ���ÿؼ����������Լ����������ÿһ�죨���Ҳ�����ڿؼ��������趨��
				calendar.setOnItemClickListener(new calendarItemClickListener());

				btn_left = (ImageButton) dialog_calendar
						.findViewById(R.id.calendarLeft);
				btn_Right = (ImageButton) dialog_calendar
						.findViewById(R.id.calendarRight);
				btn_left.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// �����һ�� ͬ����������
						String leftYearAndmonth = calendar.clickLeftMonth();
						String[] lya = leftYearAndmonth.split("-");
						title_date.setText(lya[0] + "��" + lya[1]);
					}
				});
				btn_Right.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// �����һ��
						String rightYearAndmonth = calendar.clickRightMonth();
						String[] rya = rightYearAndmonth.split("-");
						title_date.setText(rya[0] + "��" + rya[1]);
					}
				});

			}
		} else if (v.equals(btn_selectSeat) || v.equals(choose_seat)) {
			if (isTimeChosen) {
				Message msg1 = new Message();
				setTable_handler.sendMessage(msg1);
			} else {
				Toast.makeText(BookTableActivity.this, "����ѡ��ʱ��",
						Toast.LENGTH_LONG).show();
			}
		} else if (v.equals(btn_more)) {
			if (popupWindow == null || !popupWindow.isShowing()) {
				View view = LayoutInflater.from(context).inflate(
						R.layout.popmenu, null);
				popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				popupWindow.showAsDropDown(v, 10, 10);
				// ʹ��ۼ�
				// popupWindow.setFocusable(true);
				// ����������������ʧ
				// popupWindow.setOutsideTouchable(true);
				// ˢ��״̬������ˢ�·�����Ч��
				popupWindow.update();
			} else {
				popupWindow.dismiss();
				popupWindow = null;
			}

		} else if (v.equals(btn_home)) {
			Intent intent = new Intent(BookTableActivity.this, TestMain.class);
			startActivity(intent);
			BookTableActivity.this.finish();
		} else if (v.equals(btn_commintButton)) {

			if (isAccept) {
				if (Contant.ISLOGIN && Contant.USER != null) {
					StoreInfo shop;

					shop = Tools.findShopById(shopId);
					boolean b = false;

					if (tableStyleId!=null&&!tableStyleId.equals("")) {
						int id = Integer.parseInt(tableStyleId);
						TableStyle tempStyle = findTableStyleById(id, list_tableStyle);
						if (tableInfo != null&&tempStyle!=null) {
							PayInfo pay = new PayInfo();
							pay.setShopId(shopId);
							pay.setTime(tableInfo.getRt());
							Log.d("tableInfo.getRt()________________", tableInfo.getRt());
							pay.setTableName(tableInfo.getAname());
							pay.setTableId(tableInfo.getStid());
							pay.setTablePrice(tempStyle.getPrice());
							pay.setUid(Contant.USER.getId()+"");
							pay.setRprice(tempStyle.getPrice());
							pay.setSprice(Float.parseFloat(Tools.findShopById(shopId).getServicePrice()));
							pay.setDtimeid(tableInfo.getAId());
							pay.setSttid(tableStyleId+"");
							pay.setSecid(se);
							pay.setContent(edite_content.getText().toString());
							Intent intent = new Intent();
							intent.setClass(context, CommitActivity.class);
							Bundle bundle = new Bundle();  
							bundle.putString("MyString", "test bundle");  
							bundle.putParcelable("PayInfo", new PayInfoData(pay));
							
							intent.putExtras(bundle);

							startActivity(intent);
							//BookTableActivity.this.finish();
						} else {
							Toast.makeText(context, "��ѡ������", Toast.LENGTH_SHORT)
									.show();
						}
					} else {
						Toast.makeText(context, "��ѡ���ʱ�䲻�ڸõ�Ŀ�Ԥ��ʱ�䷶Χ��",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					showDialog();
				}
			}
			else {
				Toast.makeText(context, "����û�н���Э��", Toast.LENGTH_SHORT).show();
			}
			
			

		}

		else {

		}

	}
	
	private TableStyle findTableStyleById(int id,List<TableStyle> list){
		List<TableStyle> styleList = list;
		TableStyle tempStyle = null;
		if (styleList!=null) {
			for (int i = 0; i < styleList.size(); i++) {
				if (styleList.get(i).getId()==id) {
					tempStyle = styleList.get(i);
				}
			}
		}
		return tempStyle;
	}

	

	private void showTimeDialog() {

		final Dialog dialog = new TimeDialog(BookTableActivity.this,
				R.style.MyDialog);

		dialog.show();
		// // LayoutInflater inflater = getLayoutInflater();
		// //
		// // View layout = inflater.inflate(R.layout.dailog_timepicker,
		//
		// // (ViewGroup) findViewById(R.id.dialog_layout_timer));
		// final WheelView hour = (WheelView)dialog. findViewById(R.id.hour);
		// final String minutes[] = new String[] {"15", "30", "45","00"};
		//
		// // wv.setViewAdapter(new ArrayWheelAdapter<String>(MainActivity.this,
		// countries));
		// // wv.setCurrentItem(7);
		//
		//
		//
		// hour.setViewAdapter(new NumericWheelAdapter(BookTableActivity.this,
		// 0, 23));
		// hour.setCyclic(true);
		// hour.setCurrentItem(20);
		// hour.setVisibleItems(3);
		final WheelView minute = (WheelView) dialog.findViewById(R.id.minute);
		// minute.setViewAdapter(new
		// ArrayWheelAdapter<String>(BookTableActivity.this, minutes));
		minute.setViewAdapter(new ArrayListWheelAdapter(BookTableActivity.this,
				time_list));
		minute.setCyclic(true);
		minute.setCurrentItem(1);
		minute.setVisibleItems(3);
		Button btnOk = (Button) dialog.findViewById(R.id.button_ok);
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// mHour = hour.getCurrentItem();
				// mMinute = Integer.parseInt(
				// minutes[minute.getCurrentItem()]);
				// Message msg = new Message();
				// setTimeHandler.sendMessage(msg);

				se = time_list.get(minute.getCurrentItem()).getId() + "";
				final String timeName = time_list.get(minute.getCurrentItem())
						.getTimeName();
				dialog.dismiss();
				
				final Dialog tableStyle = new TimeDialog(
						BookTableActivity.this, R.style.MyDialog);

				tableStyle.show();

				final WheelView style = (WheelView) tableStyle
						.findViewById(R.id.minute);
				// minute.setViewAdapter(new
				// ArrayWheelAdapter<String>(BookTableActivity.this, minutes));
				style.setViewAdapter(new TableListWheelTextAdapter(
						BookTableActivity.this, list_tableStyle));
				style.setCyclic(true);
				style.setCurrentItem(1);
				style.setVisibleItems(3);
				TextView title = (TextView) tableStyle
						.findViewById(R.id.textView_title);
				title.setText("��ѡ������");
				Button btnOk = (Button) tableStyle.findViewById(R.id.button_ok);
				btnOk.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						if (list_tableStyle.size()>0) {
							tableStyleId = list_tableStyle.get(
									style.getCurrentItem()).getId()
									+ "";
							minPrice = list_tableStyle.get(
									style.getCurrentItem()).getCount();
							tableStyle.dismiss();
							String str = "";
							String money = list_tableStyle.get(style.getCurrentItem()).getCount();
//							for (int i = 0; i < list_tableStyle.size(); i++) {
//								str += list_tableStyle.get(i).getStyleName()+"������"+list_tableStyle.get(i).getCount()+"Ԫ,";
//							}
							str +="����Ԥ����"
								+list_tableStyle.get(style.getCurrentItem()).getStyleName()+"������"
								+money+"Ԫ.��������������"
								+money+"Ԫ,����֧���Ķ�����1-3����������ȫ���.\n����������δ����"
								+money+"Ԫ,����֧���Ķ��𽫲��践��.\nС��ʾ:Ϊȷ���������ѽ����ʵ��,�����ڲ�������ʱ,��֪��������Ա���Ƕ᷹���Ա.\n���Ƿ����?";						
//							str +=list_tableStyle.get(style.getCurrentItem()).getStyleName()+"������"+list_tableStyle.get(style.getCurrentItem()).getCount()
//									+ "�����������Ŷ�����Ȩ.\n���Ƿ����?";
							showCheckDialog(str);
							//textView.setText(str);
							textView_selectTime.setText("��ѡ���ʱ����:" + selectDate
									+ " " + timeName);
							isTimeChosen = true;
							isTableChosen = false;
						}
						else {
							Toast.makeText(context, "�õ������κ����Ϳɹ�ѡ��", Toast.LENGTH_SHORT).show();
							tableStyle.dismiss();
						}
						
					}
				});
			}
		});

	}
	
	
	private void showCheckDialog(String str) {
		Dialog alertDialog = new AlertDialog.Builder(this)
				.setTitle("��ʾ")
				.setMessage(str)
				.
				// setIcon(R.drawable.welcome_logo).
				setPositiveButton("����", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						isAccept = true;
						// TODO Auto-generated method stub
//						Intent intent = new Intent(context, LoginActivity.class);
//						startActivity(intent);
//
//						//BookTableActivity.this.finish();
					}
				})
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						isAccept = false;
					}
				}).

				create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
	}
	

	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	//
	// // TODO Auto-generated method stub
	//
	// if (popupWindow != null && popupWindow.isShowing()) {
	//
	// popupWindow.dismiss();
	//
	// popupWindow = null;
	//
	// }
	//
	// return super.onTouchEvent(event);
	//
	// }

	private void showDialog() {
		Dialog alertDialog = new AlertDialog.Builder(this)
				.setTitle("��ʾ")
				.setMessage("����û�е�¼,���ȵ�¼")
				.
				// setIcon(R.drawable.welcome_logo).
				setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(context, LoginActivity.class);
						startActivity(intent);

						//BookTableActivity.this.finish();
					}
				})
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				}).

				create();
		alertDialog.show();
	}

	public void SureOnClick(View v) {

	}

	/**
	 * ��������
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		// ���㰴��
		case MotionEvent.ACTION_DOWN:
			savedMatrix.set(matrix);
			prev.set(event.getX(), event.getY());
			mode = DRAG;
			break;
		// ���㰴��
		case MotionEvent.ACTION_POINTER_DOWN:
			dist = spacing(event);
			// �����������������10�����ж�Ϊ���ģʽ
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
	 * ���������С���ű������Զ�����
	 */
	private void CheckView() {
		float p[] = new float[9];
		matrix.getValues(p);
		if (mode == ZOOM) {
			if (p[0] < minScaleR) {
				// Log.d("", "��ǰ���ż���:"+p[0]+",��С���ż���:"+minScaleR);
				matrix.setScale(minScaleR, minScaleR);
			}
			if (p[0] > MAX_SCALE) {
				// Log.d("", "��ǰ���ż���:"+p[0]+",������ż���:"+MAX_SCALE);
				matrix.set(savedMatrix);
			}
		}
		center();
	}

	/**
	 * ��С���ű��������Ϊ100%
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
	 * �����������
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
			// ͼƬС����Ļ��С���������ʾ��������Ļ���Ϸ������������ƣ��·�������������
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
	 * ����ľ���
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	/**
	 * ������е�
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
}
