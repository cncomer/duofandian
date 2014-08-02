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
												 * ��дBaseAdapter�Զ���һImageAdapter
												 * class
												 */
{
	int mGalleryItemBackground;
	private Context mContext; /* ImageAdapter�Ľ����� */
	private int[] myImageIds = { R.drawable.pic_1, R.drawable.pic_2,
			R.drawable.pic_3, R.drawable.pic_4, R.drawable.pic_5, };

	public MyGalleryAdapter(Context c) {
		// mContext = c;
		// TypedArray a = obtainStyledAttributes(R.styleable.Gallery); /*
		// ʹ����res/values/attrs.xml�еĶ��� ��Gallery����. */
		// mGalleryItemBackground =
		// a.getResourceId(R.styleable.Gallery_android_galleryItemBackground,
		// 0); ///*ȡ��Gallery���Ե�Index
		// a.recycle();/* �ö����styleable�����ܹ�����ʹ�� */
		this.mContext = c;
	}

	public int getCount() /* һ��Ҫ��д�ķ���getCount,����ͼƬ��Ŀ���� */
	{
		// return myImageIds.length;
		return Integer.MAX_VALUE;
	}

	public Object getItem(int position) /* һ��Ҫ��д�ķ���getItem,����position */
	{
		return position;
	}

	public long getItemId(int position) /* һ��Ҫ��д�ķ���getItemId,����position */
	{
		return position;
	}
	
	/*
	 * һ��Ҫ��д�ķ���getView
	 * 
	 * ����һView����
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