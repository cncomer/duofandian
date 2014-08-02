package com.lnwoowken.lnwoowkenbook.adapter;

import com.lnwoowken.lnwoowkenbook.R;
import com.lnwoowken.lnwoowkenbook.model.Contant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MyGalleryAdapter extends BaseAdapter /*
												 * 改写BaseAdapter自定义一ImageAdapter
												 * class
												 */
{
	int mGalleryItemBackground;
	private Context mContext; /* ImageAdapter的建构子 */
	private int[] myImageIds = { R.drawable.pic_1, R.drawable.pic_2,
			R.drawable.pic_3, R.drawable.pic_4, R.drawable.pic_5, };

	public MyGalleryAdapter(Context c) {
		// mContext = c;
		// TypedArray a = obtainStyledAttributes(R.styleable.Gallery); /*
		// 使用在res/values/attrs.xml中的定义 的Gallery属性. */
		// mGalleryItemBackground =
		// a.getResourceId(R.styleable.Gallery_android_galleryItemBackground,
		// 0); ///*取得Gallery属性的Index
		// a.recycle();/* 让对象的styleable属性能够反复使用 */
		this.mContext = c;
	}

	public int getCount() /* 一定要重写的方法getCount,传回图片数目总数 */
	{
		// return myImageIds.length;
		return Integer.MAX_VALUE;
	}

	public Object getItem(int position) /* 一定要重写的方法getItem,传回position */
	{
		return position;
	}

	public long getItemId(int position) /* 一定要重写的方法getItemId,传回position */
	{
		return position;
	}
	
	/*
	 * 一定要重写的方法getView
	 * 
	 * 传回一View对象
	 */
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder groupHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.loyout_gallery, null);
			groupHolder = new ViewHolder();

			groupHolder.iv = (ImageView) convertView
					.findViewById(R.id.imageView1);
			LinearLayout.LayoutParams l3 = new LinearLayout.LayoutParams(
					Contant.GALLERY_WIDTH, Contant.GALLERY_HEIGHT);
			groupHolder.iv.setLayoutParams(l3);
			convertView.setTag(groupHolder);
		} else {
			groupHolder = (ViewHolder) convertView.getTag();
		}
		groupHolder.iv.setBackgroundResource(myImageIds[position
				% myImageIds.length]);

		return convertView;
		
	}

	static class ViewHolder {

		ImageView iv;
		
	}
}