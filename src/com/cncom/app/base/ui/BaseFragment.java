package com.cncom.app.base.ui;

import java.io.File;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.actionbarsherlock.app.SherlockFragment;
import com.lnwoowken.lnwoowkenbook.MyApplication;
import com.lnwoowken.lnwoowkenbook.R;
import com.shwy.bestjoy.utils.ComConnectivityManager;
import com.shwy.bestjoy.utils.DebugUtils;
import com.shwy.bestjoy.utils.ImageHelper;
import com.shwy.bestjoy.utils.InfoInterface;
import com.shwy.bestjoy.utils.Intents;
import com.umeng.analytics.MobclickAgent;

public class BaseFragment extends SherlockFragment{
	private static final String TAG = "BaseFragment";
	/**请求扫描条码*/
	public static final int REQUEST_SCAN = 10000;
	
	private static final int CurrentPictureGalleryRequest = 101000;
	private static final int CurrentPictureCameraRequest = 101001;
	
	public static final int DIALOG_PICTURE_CHOOSE_CONFIRM = 101002;
	//add by chenkai, 20131208, for updating check
	/**SD不可�?*/
	protected static final int DIALOG_MEDIA_UNMOUNTED = 101003;
	
	public static final int DIALOG_DATA_NOT_CONNECTED = 101006;//数据连接不可�?
	public static final int DIALOG_MOBILE_TYPE_CONFIRM = 101007;//
	public static final int DIALOG_PROGRESS = 101008;
	private ProgressDialog mProgressDialog;
	
	private int mCurrentPictureRequest = -1;
	/**
	 * @param uri 选择的图库的图片的Uri
	 * @return
	 */
	protected void onPickFromGalleryFinish(Uri uri) {
	}
    protected void onPickFromCameraFinish() {
	}
    protected void onPickFromGalleryStart() {
	}
    protected void onPickFromCameraStart() {
	}
    protected void onMediaUnmountedConfirmClick() {
   	}
    protected void onDialgClick(int id, DialogInterface dialog, boolean ok, int witch) {
   	}
	/**
	 * pick avator from local gallery app.
	 * @return
	 */
    protected void pickFromGallery() {
    	if (!MyApplication.getInstance().hasExternalStorage()) {
			MyApplication.getInstance().showMessage(R.string.msg_no_sdcard);
			return;
		}
    	Intent intent = ImageHelper.createGalleryIntent();
    	startActivityForResult(intent, CurrentPictureGalleryRequest);
	}
	/**
	 * pick avator by camera
	 * @param savedFile
	 */
    protected void pickFromCamera(File savedFile) {
    	if (!MyApplication.getInstance().hasExternalStorage()) {
			MyApplication.getInstance().showMessage(R.string.msg_no_sdcard);
			return;
		}
		Intent intent = ImageHelper.createCaptureIntent(Uri.fromFile(savedFile));
		startActivityForResult(intent, CurrentPictureCameraRequest);
	}
    
    /**
	 * pick avator from local gallery app.
	 * @return
	 */
    protected void pickFromGallery(int questCode) {
    	if (!MyApplication.getInstance().hasExternalStorage()) {
			MyApplication.getInstance().showMessage(R.string.msg_no_sdcard);
			return;
		}
    	Intent intent = ImageHelper.createGalleryIntent();
    	startActivityForResult(intent, questCode);
	}
	/**
	 * pick avator by camera
	 * @param savedFile
	 */
    protected void pickFromCamera(File savedFile, int questCode) {
    	if (!MyApplication.getInstance().hasExternalStorage()) {
			MyApplication.getInstance().showMessage(R.string.msg_no_sdcard);
			return;
		}
		Intent intent = ImageHelper.createCaptureIntent(Uri.fromFile(savedFile));
		startActivityForResult(intent, questCode);
	}
    
    public int getCurrentPictureRequest() {
    	return mCurrentPictureRequest;
    }
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
   		super.onActivityResult(requestCode, resultCode, data);
   		if (resultCode == Activity.RESULT_OK) {
   			if (CurrentPictureGalleryRequest == requestCode) {
   				onPickFromGalleryFinish(data.getData());
   			} else if (CurrentPictureCameraRequest == requestCode) {
   				onPickFromCameraFinish();
   				
   			} else if (requestCode == REQUEST_SCAN) {
   			   //识别到了信息
			   setScanObjectAfterScan(getScanObjectAfterScan());
   			}
   		}
   	}
    /**
     * 请求条码扫描
     */
    public void startScan() {
		Intent scanIntent = new Intent(getActivity(), CaptureActivity.class);
		scanIntent.putExtra(Intents.EXTRA_SCAN_TASK, true);
		startActivityForResult(scanIntent, REQUEST_SCAN);
	}
    
    /**
	 * 当使用条码识别扫描返回了识别对象，会调用该方法，子类�?要条码识别功能的话，�?要覆盖该方法自行处理结果
	 * @param baoxiuCardObject
	 */
	public void setScanObjectAfterScan(InfoInterface barCodeObject) {
		
	}
	/**
	 * 子类将实现该方法返回条码识别后能够得到的对象，将在setScanObjectAfterScan()方法中使�?
	 * @return
	 */
	public InfoInterface getScanObjectAfterScan() {
		return null;
	}
       
   	public Dialog onCreateDialog(int id) {
   		if (getActivity() == null) {
   		 	DebugUtils.logE(TAG, "onCreateDialog() id = " + id + ", getActivity()=" + getActivity());
   			return null;
   		 }
   		switch(id) {
   		case DIALOG_PICTURE_CHOOSE_CONFIRM:
   			return new AlertDialog.Builder(getActivity())
   			.setItems(this.getResources().getStringArray(R.array.picture_op_items), new DialogInterface.OnClickListener() {
   				
   				@Override
   				public void onClick(DialogInterface dialog, int which) {
   					switch(which) {
   					case 0: //Gallery
   						mCurrentPictureRequest = CurrentPictureGalleryRequest;
   						onPickFromGalleryStart();
   						break;
   					case 1: //Camera
   						mCurrentPictureRequest = CurrentPictureCameraRequest;
   						onPickFromCameraStart();
   						break;
   					}
   					
   				}
   			})
   			.setNegativeButton(android.R.string.cancel, null)
   			.create();
   			
   		case DIALOG_MEDIA_UNMOUNTED:
   			return new AlertDialog.Builder(getActivity())
   			.setMessage(R.string.dialog_msg_media_unmounted)
   			.setCancelable(false)
   			.setPositiveButton(R.string.button_close, new DialogInterface.OnClickListener() {
   				
   				@Override
   				public void onClick(DialogInterface dialog, int which) {
   					onMediaUnmountedConfirmClick();
   					
   				}
   			})
   			.create();
   			 //add by chenkai, 20131201, add network check
   	      case DIALOG_DATA_NOT_CONNECTED:
   	    	  return ComConnectivityManager.getInstance().onCreateNoNetworkDialog(getActivity());
   	      case DIALOG_PROGRESS:
   	    	  mProgressDialog = new ProgressDialog(this.getActivity());
   	    	  mProgressDialog.setMessage(getString(R.string.msg_progressdialog_wait));
   	    	  mProgressDialog.setCancelable(false);
   	    	  return mProgressDialog;
   		}
   		return null;
   	}

    protected ProgressDialog getProgressDialog() {
 	   return mProgressDialog;
    }
    
    protected void dissmissDialog(int id) {
    	Dialog dialog = mDialogMap.get(id);
    	if (dialog != null && dialog.isShowing()) {
    		dialog.dismiss();
    	}
     }
    protected void dismissDialog(int id) {
    	Dialog dialog = mDialogMap.get(id);
    	if (dialog != null && dialog.isShowing()) {
    		dialog.dismiss();
    	}
     }
    
    protected void removeDialog(int id) {
    	Dialog dialog = mDialogMap.get(id);
    	if (dialog != null) {
    		dialog.dismiss();
    		dialog = null;
    		mDialogMap.remove(id);
    	}
     }
    
   	public void showDialog(int id) {
   		Dialog dialog = mDialogMap.get(id);
   		if (dialog == null) {
   			dialog = onCreateDialog(id);
   			mDialogMap.put(id, dialog);
   		}
   		if (dialog != null) {
   			dialog.show();
   		}
   	}
   	
   	private HashMap<Integer, Dialog> mDialogMap = new HashMap<Integer, Dialog>();
   	
  //add by chenkai, 20140726 增加youmeng统计页面 begin
  	@Override
  	public void onResume() {
  		super.onResume();
  		DebugUtils.logD(TAG, "onResume() this=" + this.getClass().getSimpleName());
  		MobclickAgent.onPageStart(getClass().getSimpleName()); //统计页面
  	}
  	
  	@Override
  	public void onPause() {
  		super.onPause();
  		DebugUtils.logD(TAG, "onPause() this=" + this.getClass().getSimpleName());
  		MobclickAgent.onPageEnd(getClass().getSimpleName());
  	}
   //add by chenkai, 20140726 增加youmeng统计页面 end
  	
  	@Override
	public void onDestroyView() {
		super.onDestroyView();
		DebugUtils.logD(TAG, "onDestroyView() this=" + this.getClass().getSimpleName());
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		DebugUtils.logD(TAG, "onDestroy() this=" + this.getClass().getSimpleName());
	}
}
