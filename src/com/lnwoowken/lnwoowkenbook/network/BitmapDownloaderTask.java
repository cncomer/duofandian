package com.lnwoowken.lnwoowkenbook.network;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {  
    private String url;  
    private final WeakReference<ImageView> imageViewReference;  
  
    public BitmapDownloaderTask(ImageView imageView) {  
        imageViewReference = new WeakReference<ImageView>(imageView);  
    }  
  
    @Override  
    // Actual download method, run in the task thread  
    protected Bitmap doInBackground(String... params) {  
         // params comes from the execute() call: params[0] is the url.  
         return Client.downloadBitmap(params[0]);  
    }  
  
    @Override  
    // Once the image is downloaded, associates it to the imageView  
    protected void onPostExecute(Bitmap bitmap) {  
        if (isCancelled()) {  
            bitmap = null;  
        }  
  
        if (imageViewReference != null) {  
            ImageView imageView = imageViewReference.get();  
            BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);  
            // Change bitmap only if this process is still associated with it  
            if (this == bitmapDownloaderTask) {  
                imageView.setImageBitmap(bitmap);  
            }  
        }   
    } 
    
    @SuppressWarnings("unused")
	private static boolean cancelPotentialDownload(String url, ImageView imageView) {  
	    BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);  
	  
	    if (bitmapDownloaderTask != null) {  
	        String bitmapUrl = bitmapDownloaderTask.url;  
	        if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {  
	            bitmapDownloaderTask.cancel(true);  
	        } else {  
	            // The same URL is already being downloaded.  
	            return false;  
	        }  
	    }  
	    return true;  
	} 
    
    private static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {  
        if (imageView != null) {  
            Drawable drawable = imageView.getDrawable();  
            if (drawable instanceof DownloadedDrawable) {  
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;  
                return downloadedDrawable.getBitmapDownloaderTask();  
            }  
        }  
        return null;  
    }  
}  