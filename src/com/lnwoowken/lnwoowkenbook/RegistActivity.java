
/**
 * 注册的ACTIVITY
 */
package com.lnwoowken.lnwoowkenbook;



import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.lnwoowken.lnwoowkenbook.model.Contant;
import com.lnwoowken.lnwoowkenbook.tools.Tools;
import com.lnwoowken.lnwoowkenbook.network.Client;
import com.lnwoowken.lnwoowkenbook.thread.RequestServerThread;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

@SuppressLint("HandlerLeak")
public class RegistActivity extends Activity implements OnClickListener {
	private PopupWindow popupWindow;
	private long num;// = Tools.getRandomNum();
	private long pwd;// = Tools.getRandomNum();
	private Button btn_regist;
	private Button btn_back;
	private Button btn_home;//--返回主界面
	private Context context = RegistActivity.this;
	private RequestServerThread mThread;
	private RequestServerThread threadSMS;
	private Button btn_getSMS;
	
	private boolean isRecieved = false;
	private EditText editText_checkSMS;
	private EditText name;
//	private EditText phone;
//	private EditText email;
//	private EditText niname;
	private EditText password;
	private EditText editText_pwd_confirm;
	private Button btn_more;
	
	private Handler getSMShandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (threadSMS!=null) {
				Log.d("sms==============", "threadStart");
				threadSMS.start();
			}
		}
		
	};
	
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (mThread!=null) {
				Log.d("regist==============", "threadStart");
				mThread.start();
			}
		}
		
	};
	
	private Handler resultHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (mThread.getResult().equals(Contant.NO_NET)||msg.arg1==1) {
				Toast.makeText(context,  R.string.no_net, Toast.LENGTH_SHORT).show();
			}
			else {
				String result = Client.decodeBase64(mThread.getResult());
				if (result.contains("id")) {
					Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show();
					showDialog();
				}
				else {
					Toast.makeText(context, "注册失败,原因:"+result, Toast.LENGTH_SHORT).show();
					
				}
			}
			
			
			
		}
		
	};
	
	
	private void initialize(){
		btn_more = (Button) findViewById(R.id.button_more);
		btn_more.setOnClickListener(RegistActivity.this);
		btn_home = (Button) findViewById(R.id.button_home);
		btn_home.setOnClickListener(RegistActivity.this);
		name = (EditText) findViewById(R.id.editText_name);
		password = (EditText) findViewById(R.id.editText_pwd);
		editText_pwd_confirm = (EditText) findViewById(R.id.editText_pwd_confirm);
		editText_checkSMS = (EditText) findViewById(R.id.editText_checkSMS);
		name.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					if (name.getText().toString().contains("用户名")) {
						name.setText("");
					}
				}else {
					
				}
			}
		});
		editText_pwd_confirm.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					if (editText_pwd_confirm.getText().toString().contains("密码")) {
						editText_pwd_confirm.setText("");
						editText_pwd_confirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					}
				}else {
					
				}
			}
		});
		password.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					if (password.getText().toString().contains("密码")) {
						password.setText("");
						password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					}
				}else {
					
				}
			}
		});
	}
	
	//@SuppressWarnings("unused")
	private Handler smsResultHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			String result = threadSMS.getResult();
			Log.d("sms==============", result);
			checkRecieved(result);
		}
		
	};
	
	
	private boolean checkRecieved(String result){
//		if (result.contains("\"")) {
//			result.replace("\"", "");
//		}
		Log.d("checkRecieved==============", result);
		if (result.contains("0")) {
			isRecieved = true;
			if (name.getText().toString()!=null&&!name.getText().toString().equals("")) {
				Toast.makeText(context, "短信已经发送号码为"+name.getText().toString()+"的手机", Toast.LENGTH_SHORT).show();
			}
			
		}
		return isRecieved;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitvity_regist);
		btn_getSMS = (Button) findViewById(R.id.button_getSMS);
		btn_getSMS.setOnClickListener(RegistActivity.this);
		btn_back = (Button) findViewById(R.id.button_back);
		btn_back.setOnClickListener(RegistActivity.this);
		btn_regist = (Button) findViewById(R.id.button_regist);
		btn_regist.setOnClickListener(RegistActivity.this);
		initialize();
		
	}

	private String getOpJson(){
		
		String start = "{";
		String dot = ",";
		String user = "\"User\":\""+name.getText().toString()+"\"";
		String pwd = "\"Pwd\":\""+password.getText().toString()+"\"";
		String phno = "\"Phno\":\""+name.getText().toString()+"\"";
		String nameStr = "\"Name\":\""+""+"\"";
		String emailStr = "\"Email\":\""+""+"\"";
		String end = "}";
		String jsonStr = start+user+dot+pwd+dot+phno+dot+nameStr+dot+emailStr+end;
		return jsonStr;
	}
	
	private boolean checkConfirm(){
		boolean b = false;
		if (editText_pwd_confirm.getText().toString().equals(password.getText().toString())) {
			b = true;
		}
		else {
			//Toast.makeText(context, "密码输入不一致", Toast.LENGTH_SHORT).show();
			b = false;
		}
		return b;
	}
	
	private boolean checkSMSpwd(){
		boolean b = false;
		String str = editText_checkSMS.getText().toString();
		if (isRecieved) {
			if (str.equals(pwd+"")) {
				b = true;
			}
			else {
				b = false;
			}
		}
		else {
			b = false;
			Toast.makeText(context, "请确认收到短信后再提交注册信息", Toast.LENGTH_SHORT).show();
		}
		return b;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(btn_regist)) {
			Log.d("regist==============", "onclick");			
			if (checkConfirm()) {
				if (checkSMSpwd()) {
					//String tempStr = "http://"+Contant.SERVER_IP+":"+Contant.SERVER_PORT+"/javadill/user?id=l10&op=";
					String jsonStr = getOpJson();
					jsonStr = Client.encodeBase64(jsonStr);
					String str = Tools.getRequestStr(Contant.SERVER_IP, Contant.SERVER_PORT
							+ "", "user?id=", "l10", "&op="+jsonStr);		
				//	String url = tempStr+jsonStr;		
					int flag = Contant.FLAG_REGIST;
					mThread = new RequestServerThread(str, resultHandler, context, flag);			
					Message msg = new Message();
					handler.sendMessage(msg);
				}
				else {
					Toast.makeText(context, "验证码不正确", Toast.LENGTH_SHORT).show();
				}
				
				
				
			}
			else {
				Toast.makeText(context, "密码输入不一致", Toast.LENGTH_SHORT).show();
			}		
		}
		else if (v.equals(btn_getSMS)) {
			String phone = name.getText().toString();
			if (phone!=null&&!phone.equals("")) {
				getSMS(phone);
				Message msg = new Message();
				getSMShandler.sendMessage(msg);
			}
			else {
				Toast.makeText(context, "用户名不能为空", Toast.LENGTH_SHORT).show();
			}
			
		}
		else if (v.equals(btn_back)) {
			RegistActivity.this.finish();
		}
		else if (v.equals(btn_more)) {
			if (popupWindow == null || !popupWindow.isShowing()) {
				View view = LayoutInflater.from(context).inflate(
						R.layout.popmenu, null);
				RelativeLayout myBill = (RelativeLayout) view.findViewById(R.id.mybill);
				RelativeLayout exitLogin = (RelativeLayout) view.findViewById(R.id.exit_login);
				exitLogin.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (Contant.ISLOGIN) {
							showExitLoginDialog();
						} else {
							Intent intent = new Intent(context, LoginActivity.class);
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
						Intent intent = new Intent(context, BillListActivity.class);
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
			}
		}
		else if (v.equals(btn_home)) {
			Intent intent = new Intent(RegistActivity.this, TestMain.class);
			startActivity(intent);
			RegistActivity.this.finish();
		}
	}
	
	
	
	private void getSMS(String phone){
		num = Tools.getRandomNum();
		pwd = Tools.getRandomNum();
		String strCN = "编号为"+num+"的注册验证码为"+pwd+"感谢您使用夺饭点";
		try {
			strCN = URLEncoder.encode(strCN,"utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String idValue = "{\"id\":\"SendSMS\",\"vd\":\"0\",\"vc\":\"0\"}";
		idValue = Client.encodeBase64(idValue);
		String opStr = "{\"strmobileno\":\""+phone+"\",\"strmsg\":\""+strCN+"\"}";
		//opStr = Client.encodeBase64(opStr);
		String smsStr = "http://"+Contant.SERVER_IP+":"+Contant.SERVER_PORT+"/javadill/sms?id="+idValue+"&op="+opStr;
		threadSMS = new RequestServerThread(smsStr, smsResultHandler, context, Contant.FLAG_GETSMS);
	}
	
	private void showDialog() {
		Dialog alertDialog = new AlertDialog.Builder(this)
				.setTitle("提示")
				.setMessage("是否现在登录?")
				.
				// setIcon(R.drawable.welcome_logo).
				setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(context, LoginActivity.class);
						startActivity(intent);
						RegistActivity.this.finish();
						//BookTableActivity.this.finish();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						RegistActivity.this.finish();
					}
				}).

				create();
		alertDialog.show();
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
						// TODO Auto-generated method stub
						Contant.ISLOGIN = false;
						Contant.USER = null;
						Intent intent1 = new Intent();
						intent1.setAction("login");
						sendBroadcast(intent1);
						Toast.makeText(context, "成功退出登录", Toast.LENGTH_SHORT)
								.show();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				}).

				create();
		alertDialog.show();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// TODO Auto-generated method stub

		if (popupWindow != null && popupWindow.isShowing()) {

			popupWindow.dismiss();

			popupWindow = null;

		}

		return super.onTouchEvent(event);

	}

}
