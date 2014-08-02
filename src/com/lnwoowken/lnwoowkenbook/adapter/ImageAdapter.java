package com.lnwoowken.lnwoowkenbook.adapter;

import com.lnwoowken.lnwoowkenbook.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter{

	private int[] picture = { R.drawable.pic_1, R.drawable.pic_2, 
            R.drawable.pic_3, R.drawable.pic_4, R.drawable.pic_5 }; 
	private Context context;
	
	
	

	public ImageAdapter(int[] picture, Context context) {
		super();
		this.picture = picture;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView imageView=new ImageView(context);
		imageView.setImageResource(picture[position % picture.length]);
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		imageView.setLayoutParams(new Gallery.LayoutParams(Gallery.LayoutParams.FILL_PARENT,
				Gallery.LayoutParams.FILL_PARENT));
		return imageView;
	}
	


}
