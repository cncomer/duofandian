package com.cncom.app.base.util;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.contact.AddrBookUtils;
import com.cncom.app.base.database.BjnoteContent;
import com.cncom.app.base.service.EmptyService;
import com.cncom.app.base.service.PhotoManagerUtilsV2.TaskType;
import com.google.zxing.Result;
import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.VCardResultParser;
import com.lnwoowken.lnwoowkenbook.MyApplication;
import com.lnwoowken.lnwoowkenbook.R;
import com.shwy.bestjoy.utils.DebugUtils;
import com.shwy.bestjoy.utils.Intents;

public class VcfAsyncDownloadUtils {
	private static final String TAG = "VcfAsyncDownloadUtils";
	private static final VcfAsyncDownloadUtils INSTANCE = new VcfAsyncDownloadUtils();
	private static Context mContext;
	
	private VcfAsyncDownloadUtils(){};
	
	public static VcfAsyncDownloadUtils getInstance() {
		return INSTANCE;
	}
	
	public void setContext(Context context) {
		mContext = context;
	}
	
	/**
	 * 
	 * @param mm             Ҫ���ص�����
	 * @param notifyProgress �Ƿ�֪ͨ���ȣ�Ŀǰ��֧��
	 * @param handler
	 * @param taskType       ��������
	 * @param forceDownload  �Ƿ�ǿ������
	 * @param recordDownload �Ƿ��¼����
	 * @return
	 */
	public VcfAsyncDownloadTask executeTask(String mm, boolean notifyProgress, Handler handler, TaskType taskType, boolean forceDownload, boolean recordDownload) {
		VcfAsyncDownloadTask task = new VcfAsyncDownloadTask(mm, notifyProgress, handler, taskType, recordDownload);
		task.setForceDownload(forceDownload);
		task.execute();
		return task;
	}
	/**�첽������Ƭ��ݷ������÷�������ǿ�����غͱ������ؼ�¼��������Ԥ����Ƭʹ��*/
	public VcfAsyncDownloadTask executeTaskSimply(String mm, boolean notifyProgress, Handler handler, TaskType taskType) {
		VcfAsyncDownloadTask task = new VcfAsyncDownloadTask(mm, notifyProgress, handler, taskType, false);
		task.execute();
		return task;
	}
	
	public static void cancel(VcfAsyncDownloadTask task, boolean interrupted) {
		if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
			task.cancelTask(interrupted);
		}
	}
	
	public static abstract class VcfAsyncDownloadHandler extends Handler {
		
		/**��������ʼ��,�������û�и����������������ʾ������...*/
		public void onDownloadStart(){
			notifyMessage(R.string.msg_downloading_text);
		}
		
		public void downloadProgress(int currentSize, int totalSize){}
		/**
		 * ������س����ˣ�addressBookParsedResultΪnull, outMsgΪ������Ϣ��������಻���Ǹ÷�������ô����ʾ������Ϣ;
		 * @param addressBookParsedResult
		 * @param error ��addressBookParsedResultΪnullʱ�Ĵ�����Ϣ
		 */
		public void onDownloadFinished(AddressBookParsedResult addressBookParsedResult, String outMsg) {
			if (!TextUtils.isEmpty(outMsg)) {
				MyApplication.getInstance().showMessage(outMsg, Toast.LENGTH_SHORT);
			}
		}
		/**
		 * ������෵��true,���ʾ�������������ؽ����¼���������б��涯����Ĭ��Ҫ����
		 * @return false
		 */
		public boolean onDownloadFinishedInterrupted() {
			return false;
		}
		/**
		 * ������෵��true,��ʾ��������ת���ѱ������Ƭ����Ķ���,Ĭ�ϲ���ת
		 * @param contactUri
		 * @return
		 */
		public boolean onSaveFinished(Uri contactUri, String mm) {
			return true;
		}
		/***
		 * ������ϵ��
		 * @param addressBookParsedResult
		 * @return ��ϵ��uri
		 */
		public static Uri saveContactOrView(AddressBookParsedResult addressBookParsedResult) {
			if (DebugUtils.DEBUG_DOWNLAOD_THREAD) Log.v(TAG, "begin createContactEntry ");
			Uri contactUri = AddrBookUtils.getInstance().createContactEntry(addressBookParsedResult);
			if (DebugUtils.DEBUG_DOWNLAOD_THREAD) Log.v(TAG, "finish createContactEntry contactUri= " + contactUri);
			return contactUri;
		}
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what) {
			case VcfAsyncDownloadTask.EVENT_DOWNLOAD_FAILED:{
				onDownloadFinished(null, (String) msg.obj);
				Intent empterService = new Intent(MyApplication.getInstance(), EmptyService.class);
				MyApplication.getInstance().stopService(empterService);
				break;
			}
			case VcfAsyncDownloadTask.EVENT_DOWNLOAD_START:
				Intent empterService = new Intent(MyApplication.getInstance(), EmptyService.class);
				MyApplication.getInstance().startService(empterService);
				onDownloadStart();
				break;
			case VcfAsyncDownloadTask.EVENT_DOWNLOAD_SUCCESS:
			case VcfAsyncDownloadTask.EVENT_EXIST_VCF: {
				final String savedPath = (String) msg.obj;
				new Thread() {
					@Override
					public void run() {
						
						String result = VcfAsyncDownloadTask.readDownloadedFile(savedPath);
						if (result != null) {
							DebugUtils.logContactAsyncDownload(TAG, "begin VCardResultParser.parseResult()");
							Result rawResult = new Result(result);
							VCardResultParser parser = new VCardResultParser();
							final AddressBookParsedResult mAddressBookParsedResult = (AddressBookParsedResult) parser.parse(rawResult);
							if (mAddressBookParsedResult != null) {
								DebugUtils.logContactAsyncDownload(TAG, "get mAddressBookParsedResult != null");
								boolean interrupted = onDownloadFinishedInterrupted();
								VcfAsyncDownloadHandler.this.post(new Runnable(){

									@Override
									public void run() {
										onDownloadFinished(mAddressBookParsedResult, mContext.getString(R.string.msg_download_card_success));
									}
									
								});
								if (!interrupted) {
									DebugUtils.logContactAsyncDownload(TAG, "start a thread to saveContacts");
									internalSaveContactAfterDownload(mAddressBookParsedResult);
								}
								
							} else {
								//û�гɹ��Ľ�����AddressBookParsedResult����
								DebugUtils.logContactAsyncDownload(TAG, "VCardResultParser.parseResult() failed " + savedPath);
								VcfAsyncDownloadTask.deleteDownloadedFile(savedPath);
								VcfAsyncDownloadHandler.this.post(new Runnable(){

									@Override
									public void run() {
										onDownloadFinished(null, mContext.getString(R.string.msg_vcf_format_error));
									}
									
								});
							}
						} else {
							VcfAsyncDownloadTask.deleteDownloadedFile(savedPath);
							VcfAsyncDownloadHandler.this.post(new Runnable(){

								@Override
								public void run() {
									onDownloadFinished(null, mContext.getString(R.string.msg_vcf_read_error));
								}
								
							});
						}
						
						Intent empterService = new Intent(MyApplication.getInstance(), EmptyService.class);
						MyApplication.getInstance().stopService(empterService);
					}
					
				}.start();
						
			}
				break;
			case VcfAsyncDownloadTask.EVENT_SAVE_CONTACT_FINISH: {
				Bundle data = msg.getData();
				String mm = data.getString(Intents.EXTRA_BID);
				Uri contactUrl = data.getParcelable(Intents.EXTRA_URI);
				if (!onSaveFinished(contactUrl, mm)) {
					if (contactUrl != null) AddrBookUtils.getInstance().viewContact(contactUrl);
				}
			 }
				break;
			case VcfAsyncDownloadTask.EVENT_PROGRESS_START:
				downloadProgress(msg.arg1, msg.arg2);
				break;
			case VcfAsyncDownloadTask.EVENT_DOWNLOAD_CANCELED:
				onDownloadFinished(null, mContext.getString(R.string.msg_download_card_canceled));
				break;
			}
			
		}
		
		protected void notifyMessage(int resId) {
			MyApplication.getInstance().showMessage(resId, Toast.LENGTH_SHORT);
		}
		/**
		 * ���غ󱣴���ϵ����Ϣ
		 * @param mAddressBookParsedResult
		 */
		private void internalSaveContactAfterDownload(AddressBookParsedResult mAddressBookParsedResult) {
			Uri contactUrl = saveContactOrView(mAddressBookParsedResult);
			Message message = Message.obtain(VcfAsyncDownloadHandler.this);
			message.what = VcfAsyncDownloadTask.EVENT_SAVE_CONTACT_FINISH;
			Bundle data = new Bundle();
			data.putString(Intents.EXTRA_BID, mAddressBookParsedResult.getBid());
			data.putParcelable(Intents.EXTRA_URI, contactUrl);
			message.setData(data);
			sendMessage(message);
		}
	}
 }