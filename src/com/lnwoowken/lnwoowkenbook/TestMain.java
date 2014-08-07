package com.lnwoowken.lnwoowkenbook;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;





import com.cncom.app.base.account.MyAccountManager;
import com.lnwoowken.lnwoowkenbook.adapter.ImageAdapter;
import com.lnwoowken.lnwoowkenbook.adapter.MyGalleryAdapter;
import com.lnwoowken.lnwoowkenbook.animition.MyAnimition;
import com.lnwoowken.lnwoowkenbook.animition.Mycamera;
import com.lnwoowken.lnwoowkenbook.model.Contant;
import com.lnwoowken.lnwoowkenbook.tools.Tools;
import com.lnwoowken.lnwoowkenbook.network.Client;
import com.lnwoowken.lnwoowkenbook.network.JsonParser;
import com.lnwoowken.lnwoowkenbook.network.UpdateManager;
import com.lnwoowken.lnwoowkenbook.thread.RequestServerThread;
import com.lnwoowken.lnwoowkenbook.view.SlidingLayout;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;




import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
@SuppressWarnings("deprecation")

/**
 * 主界面
 * @author sean
 *
 */
public class TestMain extends Activity implements OnClickListener {
	private ScrollView scroll;
	private RelativeLayout content;
	private LinearLayout mainLayout;
	private LinearLayout item_camera;
	 /** 
     * 侧滑布局对象，用于通过手指滑动将左侧的菜单布局进行显示或隐藏。 
     */  
    private SlidingLayout slidingLayout;  
	private int screenWidth;// = getWindowManager().getDefaultDisplay().getWidth();
	private int screenHeight;// = getWindowManager().getDefaultDisplay().getHeight();
	private RequestServerThread versionThread;
	private boolean isalive = true;
	private MyGalleryAdapter mGallery;
	private int index;
	private PopupWindow popupWindow;
	private ImageButton btn_more;
	private Gallery gallery;
	// private ViewPager viewPager;
	private ImageView imageViewBottom;
	private RelativeLayout relativeLayout;
	private ImageButton btn_vip;
	private ImageButton btn_pick_food;
	// private RequestServerThread mThread;
	private Context context = TestMain.this;
	private ImageButton btn_book;
	private RequestServerThread myThread;
	private Button btn_login;
	private UpdateManager mUpdateManager;
	private TextView textGallery;
	private String[] strArr = new String[] { "page1", "page2", "page3", "page4", "page5" };

	// 屏幕1080
	@SuppressWarnings("unused")
	private int[] picture = { R.drawable.pic_1, R.drawable.pic_2,
			R.drawable.pic_3, R.drawable.pic_4, R.drawable.pic_5 };
	
	@SuppressWarnings("unused")
	private Gallery pictureGallery = null;
	@SuppressWarnings("unused")
	private ImageAdapter adapter;

	/**
	 * 启动线程
	 */
	private Handler checkVersionHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (versionThread != null) {
				versionThread.start();
			}
		}

	};

	/**
	 * 
	 */
	private Handler versionResultHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			//
			if (versionThread != null) {
				String version = versionThread.getResult();
				version = JsonParser.parseVersionCodeJson(version);

				if (version != null && !version.equals("")) {
					String[] versionArr = version.split("\\.");
					Log.d("versionResultHandler===========length",
							versionArr.length + "");
					if (versionArr.length > 0) {
						String[] currentVersion = Contant.VERSION_CODE
								.split("\\.");
						Log.d(versionArr[1], currentVersion[1]);
						if (versionArr[0].equals(currentVersion[0])) {
							if (versionArr[1].equals(currentVersion[1])) {

							} else {
								mUpdateManager = new UpdateManager(
										TestMain.this);
								mUpdateManager.checkUpdateInfo(1);
							}
						} else {
							showUpdateDialog();
						}
					} else {
						Toast.makeText(context,
								"版本解析错误，您当前版本可能存在问题，请重新下载并安装客户端",
								Toast.LENGTH_LONG).show();
					}

				}
			}

		}

	};

	/**
	 * 判断登录状态,更改界面上文字
	 */
	private BroadcastReceiver bcr = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			if (btn_login != null) {
				if (MyAccountManager.getInstance().hasLoginned()) {
					btn_login.setText(TextUtils.isEmpty(MyAccountManager.getInstance().getCurrentAccountName()) ? MyAccountManager.getInstance().getDefaultPhoneNumber() : MyAccountManager.getInstance().getCurrentAccountName());
				} else {
					btn_login.setText(context.getResources().getString(R.string.login));
				}
			}
		}
	};

	/**
	 * 启动线程
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			myThread.start();
		}

	};

	/**
	 * 解析JSON
	 */
	private Handler parseJson_handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			String result = myThread.getResult();
			if (result.equals(Contant.NO_NET)) {
				Toast.makeText(context, R.string.no_net, Toast.LENGTH_SHORT)
						.show();
			} else {
				//result = Client.decodeBase64(result);
				Log.d("TestMain=============parseJson_handler", result);

				if (JsonParser.checkError(result)) {
					Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
				} else {
					if (result != null && !result.equals("")) {
						Contant.SHOPID_LIST = JsonParser.parseShopIdJson(result);
					}
				}

			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 获取屏幕大小
//		Display mDisplay = getWindowManager().getDefaultDisplay();
//		int width = mDisplay.getWidth();
//		int height = mDisplay.getHeight();
//		if (width == 480 && height == 800) {
//			setContentView(R.layout.activity_test);
//		} else if (width == 1080 && height == 1920) {
//			Log.d("setContentView====================", 1080+"");
//			setContentView(R.layout.activity_test2);
//		}
//		else {
//			setContentView(R.layout.activity_test);
//		}
		
		
		setContentView(R.layout.activity_test);
		slidingLayout = (SlidingLayout) findViewById(R.id.slidingLayout);
		content = (RelativeLayout) findViewById(R.id.content);
		scroll = (ScrollView) findViewById(R.id.scrollView_content);
		item_camera = (LinearLayout) findViewById(R.id.item_camera);
		item_camera.setOnClickListener(TestMain.this);
		mainLayout = (LinearLayout) findViewById(R.id.main_layout);
		initialize();
		slidingLayout.setScrollEvent(scroll);
		//Activity a = TestMain.this;
		//UPPayAssistEx.startPay(a, null, null, tn, "01");
		//UPPayAssistEx.startPayByJAR(a, PayActivity.class, null, null, "201407260202300050142", "01");

	}

	private int getScreenFlag(int width) {
		int flag = 0;
		if (width == 480) {
			flag = 1;
		} else if (width == 1080) {
			flag = 2;
		}
		return flag;
	}
	
	private void judgeScreen(){
		
		Log.d("screenWidth======================", screenWidth + "");
		Log.d("screenHeight======================", screenHeight + "");
		Contant.SCREEN_FLAG = getScreenFlag(screenWidth);
		
		
		Log.d("Contant.GALLERY_WIDTH==========", Contant.GALLERY_WIDTH + "");
		Contant.SCREEN_WIDTH = screenWidth;
		Contant.SCREEN_HEIGHT = screenHeight;
		Contant.SCALE_WIDTH = 1;// Contant.SCREEN_WIDTH/Contant.SCREEN_WIDTH_480;
		Contant.GALLERY_WIDTH = (int) (screenWidth - Contant.MARGIN_LEFTRIGHT_480
				* Contant.SCALE_WIDTH * 2); // (screenWidth -
											// Tools.dip2px(context, 40));
		Log.d("SCREEN_WIDTH", Contant.SCREEN_WIDTH + "");
		Log.d("SCREEN_HEIGHT", Contant.SCREEN_HEIGHT + "");
		Contant.GALLERY_HEIGHT = Contant.GALLERY_WIDTH * 380 / 412;
		int margin_int = (int) (Contant.SCALE_WIDTH * Contant.MARGIN_LEFTRIGHT_480);// Tools.dip2px(context,
																					// 20);
		LinearLayout.LayoutParams l1 = new LinearLayout.LayoutParams(
				Contant.GALLERY_WIDTH, Contant.GALLERY_HEIGHT);
		l1.setMargins(margin_int, 0, margin_int, 0);

		// RelativeLayout.LayoutParams l2 = new RelativeLayout.LayoutParams(
		// (screenWidth - Tools.dip2px(context, 40)),
		// (int) ((screenWidth - Tools.dip2px(context, 40)) / 3 + 0.5));

		RelativeLayout.LayoutParams l2 = new RelativeLayout.LayoutParams(
				Contant.GALLERY_WIDTH, Contant.GALLERY_WIDTH * 126 / 412);
		RelativeLayout.LayoutParams l3 = new RelativeLayout.LayoutParams(
				Contant.GALLERY_WIDTH, Contant.GALLERY_HEIGHT);
		imageViewBottom.setLayoutParams(l2);
		relativeLayout.setLayoutParams(l1);
		
		gallery.setLayoutParams(l3);
		mGallery = new MyGalleryAdapter(context);
		gallery.setAdapter(mGallery);
		setSquareLayoutParam();
	}

	@SuppressLint("SimpleDateFormat")
	private void initialize() {
		Contant.SDCARD = Tools.getApplicationRootPath(this);
		Contant.WOOWKEN_DIR = Contant.SDCARD+"/"+"woowken";
		File f = new File(Contant.WOOWKEN_DIR);
		if (!f.exists()) {
			f.mkdir();
		}
		textGallery = (TextView) findViewById(R.id.textView_gallery);
		btn_more = (ImageButton) findViewById(R.id.imageButton_more);
		btn_more.setOnClickListener(TestMain.this);
		relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout_pictures);
		imageViewBottom = (ImageView) findViewById(R.id.imageView_bottom);
		btn_book = (ImageButton) findViewById(R.id.imageButton_book);
		btn_book.setOnClickListener(TestMain.this);
		btn_pick_food = (ImageButton) findViewById(R.id.imageButton_pick_food);
		btn_pick_food.setOnClickListener(TestMain.this);

		// float scale = context.getResources().getDisplayMetrics().density;
		screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		screenHeight = getWindowManager().getDefaultDisplay().getHeight();
		gallery = (Gallery) findViewById(R.id.gallery);

		btn_vip = (ImageButton) findViewById(R.id.imageButton_vip);
		int flag = Contant.FLAG_GETALLSHOPINFO;
		String str = "http://"+Contant.SERVER_IP+":"+Contant.SERVER_PORT+"/Mobile/GetShopID.ashx";
//				Tools.getRequestStr(Contant.SERVER_IP, Contant.SERVER_PORT
//				+ "", "shop?id=", "s8", "");

		myThread = new RequestServerThread(str, parseJson_handler,
				TestMain.this, flag);
		Message msg = new Message();
		handler.sendMessage(msg);
		btn_vip.setOnClickListener(TestMain.this);
		btn_login = (Button) findViewById(R.id.button_login);
		btn_login.setOnClickListener(TestMain.this);

		judgeScreen();

		if (MyAccountManager.getInstance().hasLoginned()) {
			btn_login.setText(MyAccountManager.getInstance().getCurrentAccountName());
		} else {
			btn_login.setText(context.getResources().getString(R.string.login));
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (isalive) {
					index = index % 5; // 图片区间[0,count_drawable)
					// Log.i(TAG, "cur_index"+ cur_index +" count_drawble --"+
					// count_drawble);
					// msg.arg1 = cur_index
					Message msg = new Message();
					msg.arg1 = index;
					mhandler.sendMessage(msg);
					// 更新时间间隔为 2s
					try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					index++; // 放置在Thread.sleep(2000)
								// ；防止mhandler处理消息的同步性，导致cur_index>=count_drawble
				}
			}
		}).start();

		versionThread = new RequestServerThread("", versionResultHandler,
				TestMain.this, 7);
		Message msg1 = new Message();
		checkVersionHandler.sendMessage(msg1);
	}

	private Handler mhandler = new Handler() {
		public void handleMessage(Message msg) {

			gallery.setSelection(msg.arg1);
			textGallery.setText(strArr[msg.arg1]);
			// UI Thread直接更改图片 ，不利用Gallery.OnItemSelectedListener监听更改

		}
	};

	private void registLoginReciever() {
		IntentFilter filter = new IntentFilter();
		filter.addAction("login");
		this.registerReceiver(bcr, filter);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// 注册广播接收器（动态注册）
		registLoginReciever();
	}

	@Override
	protected void onStop() {
		super.onStop();
		// 取消注册广播接收器

	}

	@Override
	public void onClick(View v) {
		int flag = 0;
		if (v.equals(btn_vip)) {
			Mycamera animition = new Mycamera(true);
			animition.setAnimationListener(new MyAnimition(
					Contant.ANIMITION_START_REGISTACITVITY, context));
			v.startAnimation(animition);
			if (popupWindow != null) {
				popupWindow.dismiss();
				popupWindow = null;
			}

		} else if (v.equals(btn_login)) {
			if (MyAccountManager.getInstance().hasLoginned()) {
				showExitLoginDialog();
			} else {
				Intent intent = new Intent(context, LoginActivity.class);
				startActivity(intent);
			}
			// popupWindow.dismiss();
			// popupWindow = null;
		} else if (v.equals(btn_book)) {
			flag = Contant.ANIMITION_START_RESTUARANTLISTACITVITY;
			Mycamera animition = new Mycamera(true);
			animition.setAnimationListener(new MyAnimition(flag, context));
			v.startAnimation(animition);
			// popupWindow.dismiss();
			// popupWindow = null;
		} else if (v.equals(btn_pick_food)) {
			Mycamera animition = new Mycamera(true);
			animition.setAnimationListener(new MyAnimition(flag, context));
			v.startAnimation(animition);
			// popupWindow.dismiss();
			// popupWindow = null;
			Toast.makeText(context, "该功能暂不开放", Toast.LENGTH_SHORT).show();
		} else if (v.equals(btn_more)) {
			if (popupWindow == null || !popupWindow.isShowing()) {
				View view = LayoutInflater.from(context).inflate(
						R.layout.popmenu, null);
				RelativeLayout myBill = (RelativeLayout) view
						.findViewById(R.id.mybill);
				RelativeLayout exitLogin = (RelativeLayout) view
						.findViewById(R.id.exit_login);
				TextView tv = (TextView) view.findViewById(R.id.textView_exit_login);
				if(MyAccountManager.getInstance().hasLoginned()) {
					tv.setText(context.getString(R.string.exit_login));
				} else {
					tv.setText(context.getString(R.string.login));
				}
				exitLogin.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (MyAccountManager.getInstance().hasLoginned()) {
							showExitLoginDialog();
						} else {
							Intent intent = new Intent(context,
									LoginActivity.class);
							startActivity(intent);

						}
						popupWindow.dismiss();
						popupWindow = null;
					}
				});
				myBill.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Log.d("popwindow=============", "in");
						Intent intent = new Intent(context,
								BillListActivity.class);
						startActivity(intent);
						popupWindow.dismiss();
						popupWindow = null;
					}
				});
				popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				popupWindow.showAsDropDown(v, 10, 10);
				// 使其聚集
				// popupWindow.setFocusable(true);
				// 设置允许在外点击消失
				// popupWindow.setOutsideTouchable(true);
				// 刷新状态（必须刷新否则无效）
				popupWindow.update();
			} else {
				popupWindow.dismiss();
				popupWindow = null;
			}

		}
		else if (v.equals(item_camera)) {
			Intent it = new Intent("android.media.action.IMAGE_CAPTURE"); 
			it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File("/sdcard/dfd"))); 
			startActivityForResult(it, Activity.DEFAULT_KEYS_DIALER); 
		}
		
		else {
			flag = 0;
			Mycamera animition = new Mycamera(true);
			animition.setAnimationListener(new MyAnimition(flag, context));
			v.startAnimation(animition);

		}

	}

	// private void check(){
	// if (isalive) {
	//
	// }
	// }

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// TODO Auto-generated method stub
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (bcr != null) {

			unregisterReceiver(bcr);
			isalive = false;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 是否触发按键为back键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 弹出退出确认框
			Dialog alertDialog = new AlertDialog.Builder(TestMain.this)
					.setTitle(
							"您确定要退出"
									+ context.getResources().getText(
											R.string.app_name) + "吗")
					.setPositiveButton("是",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									TestMain.this.finish();
									System.exit(0);

								}
							})

					// 返回该程序的Activity
					.setNegativeButton("否",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							}).create();
			alertDialog.show();

			return true;
		} else// 如果不是back键正常响应
		{
			return super.onKeyDown(keyCode, event);
		}

	}

	private void showExitLoginDialog() {
		Dialog alertDialog = new AlertDialog.Builder(this)
				.setTitle("提示")
				.setMessage("您已经登录,是否要退出重新登录?")
				.
				// setIcon(R.drawable.welcome_logo).
				setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						MyAccountManager.getInstance().deleteDefaultAccount();
						Intent in = new Intent();
						in.setAction("login");
						sendBroadcast(in);
						Toast.makeText(context, "成功退出登录", Toast.LENGTH_SHORT)
								.show();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).

				create();
		alertDialog.show();
	}

	private void showUpdateDialog() {
		Dialog alertDialog = new AlertDialog.Builder(this).setTitle("提示")
				.setMessage("您当前版本过低,需要重新下载客户端")
				.
				// setIcon(R.drawable.welcome_logo).
				setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mUpdateManager = new UpdateManager(TestMain.this);
						mUpdateManager.checkUpdateInfo(0);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						System.exit(0);
					}
				}).

				create();
		alertDialog.show();
	}

	private void setSquareLayoutParam() {
		int width = (Contant.SCREEN_WIDTH - 34 * 2 - 18 * 2) / 3;// (Contant.SCREEN_WIDTH-((Tools.dip2px(context,
																	// 34))*2+(Tools.dip2px(context,
																	// 18))*2))/3;
		int height = width;
		int marginTop = 20;// Tools.dip2px(context, 20);
		// int marginBottom = Tools.sp2px(context, 20);
		int marginLeft = 20;// Tools.dip2px(context, 18);

		LinearLayout.LayoutParams l1 = new LinearLayout.LayoutParams(width,
				height);
		l1.setMargins(0, marginTop, 0, 0);
		LinearLayout.LayoutParams l2 = new LinearLayout.LayoutParams(width,
				height);
		l2.setMargins(marginLeft, marginTop, 0, 0);
		btn_book.setLayoutParams(l1);
		btn_pick_food.setLayoutParams(l2);
		btn_vip.setLayoutParams(l2);

	}
}
