package com.lnwoowken.lnwoowkenbook.view;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;


import com.lnwoowken.lnwoowkenbook.BookTableActivity;
import com.lnwoowken.lnwoowkenbook.R;
import com.lnwoowken.lnwoowkenbook.view.TimeView.ArrayWheelAdapter;
import com.lnwoowken.lnwoowkenbook.view.TimeView.NumericWheelAdapter;
import com.lnwoowken.lnwoowkenbook.view.TimeView.WheelView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TimePicker;

/**
 * Android日历控件示例
 * 
 * @Description:Android日历控件示例
 * 
 * @FileName: DateWidget.java
 * 
 * @Package com.decarta.calendar
 * 
 * @Author Hanyonglu
 * 
 * @Date 2012-3-26 上午11:46:14
 * 
 * @Version V1.0
 */
@SuppressWarnings("unused")
public class DateWidget extends Dialog {
	Context context;
	Handler handler;
	Display display;
	float widthNum = 1;
	float heightNum = 1;

	public DateWidget(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public DateWidget(Context context, int theme, Handler handler,
			Display display) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.handler = handler;
		this.display = display;
		iDayCellSize = pxToDip(context, iDayCellSize);
		iDayHeaderHeight = pxToDip(context, iDayHeaderHeight);
		iTotalWidth = pxToDip(context, iTotalWidth);
	}

	private ArrayList<DateWidgetDayCell> days = new ArrayList<DateWidgetDayCell>();
	private Calendar calStartDate = Calendar.getInstance();
	private Calendar calToday = Calendar.getInstance();
	private Calendar calCalendar = Calendar.getInstance();
	private Calendar calSelected = Calendar.getInstance();

	LinearLayout layContent = null;
	Button btnToday = null;

	private int iFirstDayOfWeek = Calendar.SUNDAY;
	private int iMonthViewCurrentMonth = 0;
	private int iMonthViewCurrentYear = 0;
	public static final int SELECT_DATE_REQUEST = 111;
	private int iDayCellSize = 34;
	private int iDayHeaderHeight = 24;
	private int iTotalWidth = (iDayCellSize * 7);
	private TextView tv, monthTextView, yearTextView;
	private int mYear;

	private int mMonth;
	private int mDay;
	
	private int type;
	private Button btn_nextStep;
	private int mHour; // 存放时间的小时
	private int mMinute; // 存放时间的分钟
	private boolean isSelected = false;

	public int getmHour() {
		return mHour;
	}

	public void setmHour(int mHour) {
		this.mHour = mHour;
	}

	public int getmMinute() {
		return mMinute;
	}

	public void setmMinute(int mMinute) {
		this.mMinute = mMinute;
	}

	public int getmYear() {
		return mYear;
	}

	public void setmYear(int mYear) {
		this.mYear = mYear;
	}

	public int getmMonth() {
		return mMonth;
	}

	public void setmMonth(int mMonth) {
		this.mMonth = mMonth;
	}

	public int getmDay() {
		return mDay;
	}

	public void setmDay(int mDay) {
		this.mDay = mDay;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Bundle bundle = this.getIntent().getExtras();
		// type = bundle.getInt("type");

		requestWindowFeature(Window.FEATURE_NO_TITLE); // 声明使用自定义标�?
		iFirstDayOfWeek = Calendar.SUNDAY;
		mYear = calSelected.get(Calendar.YEAR);
		mMonth = calSelected.get(Calendar.MONTH);
		mDay = calSelected.get(Calendar.DAY_OF_MONTH);
		setContentView(generateContentView());
		getWindow().setFeatureInt(Window.FEATURE_NO_TITLE, R.layout.title);// 自定义布�?���?
		calStartDate = getCalendarStartDate();
		DateWidgetDayCell daySelected = updateCalendar();
		updateControlsState();
		if (daySelected != null)
			daySelected.requestFocus();
	}

	@SuppressWarnings("deprecation")
	private LinearLayout createLayout(int iOrientation) {
		LinearLayout lay = new LinearLayout(this.context);
		lay.setLayoutParams(new LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		lay.setOrientation(iOrientation);
		return lay;
	}

	private Button createButton(String sText, int iWidth, int iHeight) {
		Button btn = new Button(this.context);
		btn.setText(sText);
		btn.setLayoutParams(new LayoutParams(iWidth, iHeight));
		return btn;
	}

	private void generateTopButtons(LinearLayout layTopControls) {

		final int iSmallButtonWidth = pxToDip(context, 40);
		btnToday = createButton("", iTotalWidth - iSmallButtonWidth * 4,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		// LinearLayout.LayoutParams lp = new
		// LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
		// LinearLayout.LayoutParams.WRAP_CONTENT);
		// lp.setMargins(15, 0, 15, 0);

		monthTextView = new TextView(this.context);
		monthTextView.setPadding(0, 0, 0, 0);
		monthTextView.setText(mYear + "");
		monthTextView.setWidth(pxToDip(context, 55));
		// monthTextView.setLayoutParams(lp);
		yearTextView = new TextView(this.context);
		yearTextView.setPadding(pxToDip(context, 20), 0, 0, 0);
		yearTextView.setText(format(mMonth + 1));
		yearTextView.setWidth(pxToDip(context, 55));

		Button btnPrevMonth = new Button(this.context);
		btnPrevMonth.setLayoutParams(new LayoutParams(iSmallButtonWidth,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		btnPrevMonth.setBackgroundResource(R.drawable.prev_month);

		Button btnPrevYear = new Button(this.context);
		btnPrevYear.setLayoutParams(new LayoutParams(iSmallButtonWidth,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		btnPrevYear.setBackgroundResource(R.drawable.prev_year);
		Button btnNextMonth = new Button(this.context);
		btnNextMonth.setLayoutParams(new LayoutParams(iSmallButtonWidth,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		btnNextMonth.setBackgroundResource(R.drawable.btn_right);
		Button btnNextYear = new Button(this.context);
		btnNextYear.setLayoutParams(new LayoutParams(iSmallButtonWidth,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		btnNextYear.setBackgroundResource(R.drawable.next_year);
		
		
		
		// set events
		btnPrevMonth.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				setPrevMonthViewItem();
			}
		});

		btnNextMonth.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				setNextMonthViewItem();
			}
		});

		btnPrevYear.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				setPrevYearViewItem();
			}
		});

		btnNextYear.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				setNextYearViewItem();
			}
		});

		layTopControls.setGravity(Gravity.CENTER_HORIZONTAL);
		layTopControls.addView(btnPrevYear);
		layTopControls.addView(btnPrevMonth);
		layTopControls.addView(monthTextView);
		layTopControls.addView(yearTextView);
		layTopControls.addView(btnNextMonth);
		layTopControls.addView(btnNextYear);
	}

	private View generateContentView() {
		LinearLayout layMain = createLayout(LinearLayout.VERTICAL);
		layMain.setPadding(0, 0, 0, 0);
		LinearLayout layTopControls = createLayout(LinearLayout.HORIZONTAL);

		layContent = createLayout(LinearLayout.VERTICAL);
		layContent.setPadding(pxToDip(context, 20), 0, pxToDip(context, 20), 0);
		generateTopButtons(layTopControls);
		generateCalendar(layContent);
		layMain.addView(layTopControls);
		layMain.addView(layContent);

		tv = new TextView(this.context);
		tv.setPadding(pxToDip(context, 20), 0, pxToDip(context, 20), 0);
		layMain.addView(tv);
		btn_nextStep = new Button(this.context);
		btn_nextStep.setText("下一步");
		btn_nextStep.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isSelected) {
//					DateWidget.this.dismiss();
//
//					LayoutInflater inflater = getLayoutInflater();
//
//					View layout = inflater.inflate(R.layout.dailog_timepicker,
//
//					(ViewGroup) findViewById(R.id.dialog_layout_timer));
////					final TimePicker picker = (TimePicker) layout
////							.findViewById(R.id.timePicker1);
////					initialTimerPicker(picker);
////					picker.setCurrentMinute(0);
////					picker.setCurrentHour(18);
////					picker.setIs24HourView(true);
//					
//					
//					WheelView hour = (WheelView)layout. findViewById(R.id.hour);
//					String minutes[] = new String[] {"15", "30", "45","00"};  
//					 
////					wv.setViewAdapter(new ArrayWheelAdapter<String>(MainActivity.this, countries));           
////			      wv.setCurrentItem(7);  
//					
//					
//					
//					hour.setViewAdapter(new NumericWheelAdapter(context, 0, 23));
//					hour.setCyclic(true);
//					hour.setCurrentItem(20);
//					hour.setVisibleItems(3); 
//					WheelView minute = (WheelView) layout.findViewById(R.id.minute);
//					minute.setViewAdapter(new ArrayWheelAdapter<String>(context, minutes));  
//					minute.setCyclic(true);
//					minute.setCurrentItem(1);
//					minute.setVisibleItems(3); 
//					final Dialog dialog = new AlertDialog.Builder(context)
//							.setTitle("请选择时间").setView(layout).setPositiveButton("确定",
//									new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(
//										DialogInterface dialog,
//										int which) {
//									// TODO Auto-generated method stub
//								
//								}
//							})
//					.setNegativeButton("取消",
//							new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(
//										DialogInterface dialog,
//										int which) {
//									// TODO Auto-generated method stub
//
//								}
//							}).create();
//					dialog.show();

					// TimePickerDialog t = new TimePickerDialog(context,
					// callBack, hourOfDay, minute, is24HourView)
					// TimePickerDialog t = new TimePickerDialog(
					// DateWidget.this.context, otsl, mHour, mMinute, true);
					// TimePicker picker = new TimePicker(context);
					// Log.d("hour:"+hourOfDay, "minute:"+minute);
					// t.onTimeChanged(picker, hourOfDay, minute);
					// t.show();
				}

			}
		});
		layMain.addView(btn_nextStep);
		return layMain;
	}

	// OnTimeChangedListener timeChange = new OnTimeChangedListener() {
	//
	// @Override
	// public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
	// // TODO Auto-generated method stub
	//
	// Log.d("hour:"+hourOfDay, "minute:"+minute);
	// }
	// };

	// OnTimeSetListener otsl = new OnTimeSetListener() {
	//
	// @Override
	// public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	// // TODO Auto-generated method stub
	// // 设置小时、分钟，并把时间显示在文本框上
	// mHour = hourOfDay;
	// mMinute = minute;
	// Log.d("onTimeSet", mHour + ":" + mMinute);
	// Message msg = new Message();
	// DateWidget.this.handler.sendMessage(msg);
	// // mTextView.setText("当前时间：" + mHour +":"+mMinute);
	// }
	// };

	private View generateCalendarRow() {
		LinearLayout layRow = createLayout(LinearLayout.HORIZONTAL);
		for (int iDay = 0; iDay < 7; iDay++) {
			DateWidgetDayCell dayCell = new DateWidgetDayCell(this.context,
					iDayCellSize, iDayCellSize);
			dayCell.setItemClick(mOnDayCellClick);
			days.add(dayCell);
			layRow.addView(dayCell);
		}
		return layRow;
	}

	private View generateCalendarHeader() {
		LinearLayout layRow = createLayout(LinearLayout.HORIZONTAL);
		for (int iDay = 0; iDay < 7; iDay++) {
			DateWidgetDayHeader day = new DateWidgetDayHeader(this.context,
					iDayCellSize, iDayHeaderHeight);
			final int iWeekDay = DayStyle.getWeekDay(iDay, iFirstDayOfWeek);
			day.setData(iWeekDay);
			layRow.addView(day);
		}
		return layRow;
	}

	private void generateCalendar(LinearLayout layContent) {
		layContent.addView(generateCalendarHeader());
		days.clear();
		for (int iRow = 0; iRow < 6; iRow++) {
			layContent.addView(generateCalendarRow());
		}
	}

	private Calendar getCalendarStartDate() {
		calToday.setTimeInMillis(System.currentTimeMillis());
		calToday.setFirstDayOfWeek(iFirstDayOfWeek);

		if (calSelected.getTimeInMillis() == 0) {
			calStartDate.setTimeInMillis(System.currentTimeMillis());
			calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
		} else {
			calStartDate.setTimeInMillis(calSelected.getTimeInMillis());
			calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
		}
		updateStartDateForMonth();

		return calStartDate;
	}

	private DateWidgetDayCell updateCalendar() {
		DateWidgetDayCell daySelected = null;
		boolean bSelected = false;
		final boolean bIsSelection = (calSelected.getTimeInMillis() != 0);
		final int iSelectedYear = calSelected.get(Calendar.YEAR);
		final int iSelectedMonth = calSelected.get(Calendar.MONTH);
		final int iSelectedDay = calSelected.get(Calendar.DAY_OF_MONTH);
		calCalendar.setTimeInMillis(calStartDate.getTimeInMillis());
		for (int i = 0; i < days.size(); i++) {
			final int iYear = calCalendar.get(Calendar.YEAR);
			final int iMonth = calCalendar.get(Calendar.MONTH);
			final int iDay = calCalendar.get(Calendar.DAY_OF_MONTH);
			final int iDayOfWeek = calCalendar.get(Calendar.DAY_OF_WEEK);
			DateWidgetDayCell dayCell = days.get(i);
			// check today
			boolean bToday = false;
			if (calToday.get(Calendar.YEAR) == iYear)
				if (calToday.get(Calendar.MONTH) == iMonth)
					if (calToday.get(Calendar.DAY_OF_MONTH) == iDay)
						bToday = true;
			// check holiday
			boolean bHoliday = false;
			if ((iDayOfWeek == Calendar.SATURDAY)
					|| (iDayOfWeek == Calendar.SUNDAY))
				bHoliday = true;
			if ((iMonth == Calendar.JANUARY) && (iDay == 1))
				bHoliday = true;

			dayCell.setData(iYear, iMonth, iDay, bToday, bHoliday,
					iMonthViewCurrentMonth, iDayOfWeek);
			bSelected = false;
			if (bIsSelection)
				if ((iSelectedDay == iDay) && (iSelectedMonth == iMonth)
						&& (iSelectedYear == iYear)) {
					bSelected = true;
				}
			dayCell.setSelected(bSelected);
			if (bSelected)
				daySelected = dayCell;
			calCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		layContent.invalidate();
		return daySelected;
	}

	private void updateStartDateForMonth() {
		iMonthViewCurrentMonth = calStartDate.get(Calendar.MONTH);
		iMonthViewCurrentYear = calStartDate.get(Calendar.YEAR);
		calStartDate.set(Calendar.DAY_OF_MONTH, 1);
		UpdateCurrentMonthDisplay();
		// update days for week
		int iDay = 0;
		int iStartDay = iFirstDayOfWeek;
		if (iStartDay == Calendar.MONDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
			if (iDay < 0)
				iDay = 6;
		}
		if (iStartDay == Calendar.SUNDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
			if (iDay < 0)
				iDay = 6;
		}
		calStartDate.add(Calendar.DAY_OF_WEEK, -iDay);
	}

	private void UpdateCurrentMonthDisplay() {
		String s = calCalendar.get(Calendar.YEAR) + "/"
				+ (calCalendar.get(Calendar.MONTH) + 1);// dateMonth.format(calCalendar.getTime());
		btnToday.setText(s);
		mYear = calCalendar.get(Calendar.YEAR);
	}

	private void setPrevMonthViewItem() {
		iMonthViewCurrentMonth--;
		if (iMonthViewCurrentMonth == -1) {
			iMonthViewCurrentMonth = 11;
			iMonthViewCurrentYear--;
		}
		calStartDate.set(Calendar.DAY_OF_MONTH, 1);
		calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
		calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);
		updateDate();
		updateCenterTextView(iMonthViewCurrentMonth, iMonthViewCurrentYear);
	}

	private void setNextMonthViewItem() {
		iMonthViewCurrentMonth++;
		if (iMonthViewCurrentMonth == 12) {
			iMonthViewCurrentMonth = 0;
			iMonthViewCurrentYear++;
		}
		calStartDate.set(Calendar.DAY_OF_MONTH, 1);
		calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
		calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);
		updateDate();
		updateCenterTextView(iMonthViewCurrentMonth, iMonthViewCurrentYear);
	}

	private void setPrevYearViewItem() {
		iMonthViewCurrentYear--;
		calStartDate.set(Calendar.DAY_OF_MONTH, 1);
		calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
		calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);
		updateDate();
		updateCenterTextView(iMonthViewCurrentMonth, iMonthViewCurrentYear);
	}

	private void setNextYearViewItem() {
		iMonthViewCurrentYear++;
		calStartDate.set(Calendar.DAY_OF_MONTH, 1);
		calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
		calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);
		updateDate();
		updateCenterTextView(iMonthViewCurrentMonth, iMonthViewCurrentYear);
	}

	private DateWidgetDayCell.OnItemClick mOnDayCellClick = new DateWidgetDayCell.OnItemClick() {
		public void OnClick(DateWidgetDayCell item) {
			calSelected.setTimeInMillis(item.getDate().getTimeInMillis());
			item.setSelected(true);
			updateCalendar();
			updateControlsState();
			isSelected = true;
			// Intent i = new Intent(DateWidget.this,Main.class);
			// startActivity(i);
		}
	};

	private void updateCenterTextView(int iMonthViewCurrentMonth,
			int iMonthViewCurrentYear) {
		monthTextView.setText(iMonthViewCurrentYear + "");
		yearTextView.setText(format(iMonthViewCurrentMonth + 1) + "");
	}

	private void updateDate() {
		updateStartDateForMonth();
		updateCalendar();
	}

	private void updateControlsState() {
		mYear = calSelected.get(Calendar.YEAR);
		mMonth = calSelected.get(Calendar.MONTH);
		mDay = calSelected.get(Calendar.DAY_OF_MONTH);
		tv.setText("您选择的日期是："
				+ new StringBuilder().append(mYear).append("/")
						.append(format(mMonth + 1)).append("/")
						.append(format(mDay)));
		tv.setHorizontallyScrolling(true);
		// SharedPreferences mPerferences =
		// PreferenceManager.getDefaultSharedPreferences(this);
		// SharedPreferences.Editor mEditor = mPerferences.edit();
		// if (type == 0)
		// mEditor.putString("check_in", format(mMonth +
		// 1)+"-"+format(mDay)+"-"+mYear);
		// else if (type == 1)
		// mEditor.putString("check_out", format(mMonth +
		// 1)+"-"+format(mDay)+"-"+mYear);
		// mEditor.commit();
	}

	private String format(int x) {
		String s = "" + x;
		if (s.length() == 1)
			s = "0" + s;
		return s;
	}

	@SuppressWarnings("deprecation")
	private void initialWidthAndHeight() {
		if (this.display != null) {
			int width = display.getWidth();
			int height = display.getHeight();
			Log.d("===========width", width + "");
			Log.d("===========height", height + "");
			this.widthNum = width / 480;
			this.heightNum = height / 800;
		}

	}

	/** * 根据手机的分辨率从px(像素) 的单位 转成为dp */
	public static int pxToDip(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	private void initialTimerPicker(TimePicker picker) {

		picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				try {
					Field mMinutePicker = view.getClass().getDeclaredField(
							"mMinutePicker");

					mMinutePicker.setAccessible(true);
					Object value = mMinutePicker.get(view);
					// Log.i(TAG,value.getClass()+"");
					// 反射NumberPicker里面的mCurrent此属性值是显示给用户看的
					Field mCurrent = value.getClass().getDeclaredField(
							"mCurrent");
					mCurrent.setAccessible(true);
					// 反射TimePicker类里面的mCurrentMinute属性，此属性是保存用户设置的时间
					Field mCurrentMinute = view.getClass().getDeclaredField(
							"mCurrentMinute");
					mCurrentMinute.setAccessible(true);

					if (minute >= 45 && minute <= 59) {
						mCurrent.set(value, 45);
						mCurrentMinute.set(view, 45);
					}

					else if (minute >= 30 && minute < 45) {
						mCurrent.set(value, 30);
						mCurrentMinute.set(view, 30);
					}

					else if (minute >= 15 && minute < 30) {
						mCurrent.set(value, 15);
						mCurrentMinute.set(view, 15);
					} else if (minute >= 0 && minute < 15) {
						mCurrent.set(value, 0);
						mCurrentMinute.set(view, 0);
					}

					//
					Log.d("timepicker___________", minute + "");
					// 判断用户是否点击了减按钮
					if (minute == 59) {

						mCurrent.set(value, 45);
						mCurrentMinute.set(view, 45);
					} else if (minute == 1) {
						mCurrent.set(value, 15);
						mCurrentMinute.set(view, 15);
					} else if (minute == 46) {
						mCurrent.set(value, 0);
						mCurrentMinute.set(view, 0);
					} else if (minute == 44) {
						mCurrent.set(value, 30);
						mCurrentMinute.set(view, 30);
					} else if (minute == 31) {
						mCurrent.set(value, 45);
						mCurrentMinute.set(view, 45);
					} else if (minute == 29) {
						mCurrent.set(value, 15);
						mCurrentMinute.set(view, 15);
					} else if (minute == 16) {// 30---->29
						mCurrent.set(value, 30);
						mCurrentMinute.set(view, 30);
					} else if (minute == 14) {// 30---->29
						mCurrent.set(value, 0);
						mCurrentMinute.set(view, 0);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
