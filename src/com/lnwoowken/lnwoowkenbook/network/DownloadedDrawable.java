package com.lnwoowken.lnwoowkenbook.network;

import java.lang.ref.WeakReference;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

public class DownloadedDrawable extends ColorDrawable {  
    private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;  
  
    public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {  
        super(Color.BLACK);  
        bitmapDownloaderTaskReference =  
            new WeakReference<BitmapDownloaderTask>(bitmapDownloaderTask);  
    }  
  
    public BitmapDownloaderTask getBitmapDownloaderTask() {  
        return bitmapDownloaderTaskReference.get();  
        
    }  
    
}  
