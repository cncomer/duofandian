package com.lnwoowken.lnwoowkenbook.adapter;


import java.util.List;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class MyViewPagerAdapter extends PagerAdapter {
	private List<ImageView> mListViews;
//	int     mGalleryItemBackground;  
//    private Context mContext;         /* ImageAdapter�Ľ����� */  
//    private int[] myImageIds = {R.drawable.photo1,   
//                                    R.drawable.photo2,   
//                                    R.drawable.photo3,   
//                                    };  
	public MyViewPagerAdapter(List<ImageView> mListViews) {
		//this.mListViews = mListViews;
		//this.mContext = c;
		this.mListViews = mListViews;// = new ArrayList<View>();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) 	{	
		container.removeView(mListViews.get(position));
		mListViews.get(position).setImageBitmap(null);
	}


	@Override
	public Object instantiateItem(ViewGroup container, int position) {			
		// container.addView(mListViews.get(position), 0);
//		 ImageView i = new ImageView(mContext);                  
//	     // i.setImageResource(myImageIds[position%myImageIds.length]);              /* �趨ͼƬ��imageView���� */  
//	      i.setScaleType(ImageView.ScaleType.FIT_XY);            /* �����趨ͼƬ�Ŀ�� */  
//	      i.setLayoutParams(new LinearLayout.LayoutParams(Contant.GALLERY_WIDTH, Contant.GALLERY_HEIGHT));  /* �����趨Layout�Ŀ�� */  
//	      i.setBackgroundResource(myImageIds[position%myImageIds.length]); 
	      container.addView(mListViews.get(position));/* �趨Gallery����ͼ */  
	    //  return i;
		 return mListViews.get(position);
	}

	@Override
	public int getCount() {			
		//return  mListViews.size();
		return Integer.MAX_VALUE;  
	}
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {			
		return arg0==arg1;
	}

}
