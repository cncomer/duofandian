package com.cncom.app.base.service;

import java.util.Collection;
import java.util.HashMap;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;

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
	/**
	 * 
	 * @param key 需要倒数的对象描述
	 * @param countdown 倒数值，如10，则从10开始倒数到1
	 * @param forceOverride
	 * @return
	 */
	public boolean commit(CountdownObject countdownObject, boolean forceOverride) {
		DebugUtils.logD(TAG, "commit() key=" + countdownObject._key + ", countdown=" + countdownObject._countdown + ", forceOverride=" + forceOverride);
		synchronized(mCountdownMaps) {
			if (mCountdownMaps.containsKey(countdownObject._key)) {
				if (!forceOverride) {
					DebugUtils.logD(TAG, "ignore commit, CountdownObject has exsited for key=" + countdownObject._key);
					return false;
				} else {
					//更新回调对象
					countdownObject._countdown = mCountdownMaps.get(countdownObject._key)._countdown;
					mCountdownMaps.remove(countdownObject._key);
				}
			}
			mCountdownMaps.put(countdownObject._key, countdownObject);
			mCountdownMaps.notifyAll();
		}
		return true;
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
		
		private void notifyCallback() {
			if (_countdownCallback != null) {
				_countdownCallback.countdown(_countdown);
			}
		}
		
		public synchronized void increase() {
			_countdown+=1;
			notifyCallback();
		}
		public synchronized void decrease() {
			if (_countdown == 1) {
				mCountdownMaps.remove(_key);
			}
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
					final Collection<CountdownObject> list = mCountdownMaps.values();
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							for(CountdownObject object : list) {
								object.decrease();
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
		}
		
	}

}
