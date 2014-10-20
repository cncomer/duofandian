package com.cncom.app.base.service;

import java.util.HashMap;
import java.util.Iterator;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;

import com.cncom.app.base.update.UpdateService;
import com.shwy.bestjoy.utils.DebugUtils;

public class TimeService extends Service{

	private static final String TAG = "TimeService";
	private Handler mWorkHandler;
	private Handler mHandler;
	private static TimeService mTimeService;
	
	private static HashMap<String, CountdownObject> mCountdownMaps = new HashMap<String, CountdownObject>();
	private CountdownThread mCountdownThread;
	@Override
	public void onCreate() {
		super.onCreate();
		DebugUtils.logD(TAG, "onCreate()");
		mTimeService = this;
		mHandler = new Handler();
		//start background thread.
		HandlerThread workThread = new HandlerThread("TimeServiceWorkThread", Process.THREAD_PRIORITY_BACKGROUND);
		workThread.start();
		mWorkHandler = new Handler(workThread.getLooper()) {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
			}
			
		};
		
		//start countdown thread
		mCountdownThread = new CountdownThread();
		mCountdownThread.start();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return Service.START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		DebugUtils.logD(TAG, "onDestroy()");
	}


	/**
	 * 
	 * @param countdownObject 倒数对象，如10，则从10开始倒数到1
	 * @return
	 */
	public void commit(CountdownObject countdownObject, boolean forceOverride) {
		DebugUtils.logD(TAG, "commit() key=" + countdownObject._key + ", countdown=" + countdownObject._countdown);
		synchronized(mCountdownMaps) {
			if (mCountdownMaps.containsKey(countdownObject._key)) {
				if (forceOverride) {
					mCountdownMaps.remove(countdownObject._key);
				} else {
					//更新回调对象
					countdownObject._countdown = mCountdownMaps.get(countdownObject._key)._countdown;
					mCountdownMaps.remove(countdownObject._key);
				}
			}
			mCountdownMaps.put(countdownObject._key, countdownObject);
			countdownObject._countdownCallback.start(countdownObject._countdown);
			mCountdownMaps.notifyAll();
		}
	}
	/**
	 * 如果确定倒数完成了，调用该方法移除倒数对象
	 * @param countdownObject
	 * @return
	 */
	public boolean remove(CountdownObject countdownObject) {
		synchronized(mCountdownMaps) {
			return mCountdownMaps.remove(countdownObject._key) != null;
		}
	}
	/**
	 * 检索Countdown对象
	 * @param key
	 * @return
	 */
	public CountdownObject getCountdownObject(String key) {
		synchronized(mCountdownMaps) {
			return mCountdownMaps.get(key);
		}
	}
	
	public static TimeService getService() {
		return mTimeService;
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	public static interface CountdownCallback {
		void countdown(int current);
		void start(int start);
	}
	
	public static class CountdownObject {
		private int _countdown = 0;
		private CountdownCallback _countdownCallback;
		private String _key;
		public CountdownObject(String key, int countdown, CountdownCallback callback) {
			_key = key;
			_countdown = countdown;
			_countdownCallback = callback;
		}
		
		protected boolean hasFinish() {
			return _countdown <= 1;
		}
		
		public void setCountdownCallback(CountdownCallback callback) {
			_countdownCallback = callback;
		}
		
		private void notifyCallback() {
			if (_countdownCallback != null) {
				_countdownCallback.countdown(_countdown);
			}
		}
		public synchronized int getCurrent() {
			return _countdown;
		}
		public synchronized void increase() {
			_countdown+=1;
			notifyCallback();
		}
		public synchronized void decrease() {
//			if (_countdown == 1) {
//				synchronized(mCountdownMaps) {
//					mCountdownMaps.remove(_key);
//					DebugUtils.logD(TAG, "CountdownObject key=" + _key + " has finished and removed.");
//				}
//				return;
//			}
			_countdown-=1;
			notifyCallback();
		}
	}
	
	private class CountdownThread extends Thread {

		private boolean _isCanceled = false;
		public void cancel(boolean cancel) {
			_isCanceled = cancel;
		}
		@Override
		public void run() {
			super.run();
			try {
				while(!_isCanceled) {
					synchronized(mCountdownMaps) {
						if (mCountdownMaps.size() == 0) {
							mCountdownMaps.wait();
						}
					}
					
					Thread.sleep(1000);
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							Iterator<CountdownObject> iterator = mCountdownMaps.values().iterator();
							while(iterator.hasNext()) {
								CountdownObject object = iterator.next();
								object.decrease();
								if (object.hasFinish()) {
									iterator.remove();
								}
							}
						}
					});
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void startService(Context context) {
		Intent intent = new Intent(context, TimeService.class);
		context.startService(intent);
	}
	
	
	public static class BootCompletedReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			startService(context);
			DebugUtils.logD("BootCompletedReceiver", "onReceive intent " + intent);
			String action = intent.getAction();
			if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
				UpdateService.startUpdateServiceOnBootCompleted(context);
			} else if ("android.intent.action.USER_PRESENT".equals(action)) {
				UpdateService.startUpdateServiceOnUserPresent(context);
			}
		}
		
	}

}
