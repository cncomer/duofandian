package com.lnwoowken.lnwoowkenbook.network;

import android.widget.ImageView;

public class ImageDownLoader {  
	  
    public void download(String url, ImageView imageView) {  
            BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);  
            task.execute(url);  
        }  
    }  
  
    /* class BitmapDownloaderTask, see below */  

