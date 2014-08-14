package com.lnwoowken.lnwoowkenbook;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cncom.app.base.util.BitmapUtils;

public class JumpActivity extends Activity implements OnClickListener {
	private int mCurrentPagerIndex = 0;
	private LinearLayout mDotsLayout;
	private ViewPager mAdsViewPager;
	private Bitmap[] mAdsBitmaps;
	private ImageView[] mDotsViews = null;
	private ImageView[] mAdsPagerViews = null;
	private Drawable[] mDotDrawableArray;
	private int[] mAddsDrawableId = { R.drawable.start_1, R.drawable.start_2, };
	private Button mButtonGo;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jump);
		mHandler = new Handler();
		if (checkFirst()) {
			mDotsLayout = (LinearLayout) findViewById(R.id.dots);
			mAdsViewPager = (ViewPager) findViewById(R.id.adsViewPager);
			mButtonGo = (Button) findViewById(R.id.button_go);
			mButtonGo.setOnClickListener(this);
			initViewPagers(mAddsDrawableId.length);
			initDots(mAddsDrawableId.length);
			mAdsViewPager.setAdapter(new AdsViewPagerAdapter());
			
			mAdsViewPager.setOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					if (mCurrentPagerIndex != position) {
						mDotsViews[mCurrentPagerIndex].setImageDrawable(mDotDrawableArray[0]);
						mDotsViews[position].setImageDrawable(mDotDrawableArray[1]);
						mCurrentPagerIndex = position;
						
						if (mCurrentPagerIndex == mAddsDrawableId.length -1) {
//							mButtonGo.setVisibility(View.VISIBLE);
							mHandler.postDelayed(new Runnable() {

								@Override
								public void run() {
									goWelcomeActivity();
								}
								
							}, 1000);
						} else {
//							mButtonGo.setVisibility(View.GONE);
						}
					}
				}
				
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					
				}
				
				@Override
				public void onPageScrollStateChanged(int state) {
					
				}
			});
		} else {
			goWelcomeActivity();
		}
	}

	
	private void goWelcomeActivity() {
		Intent intent = new Intent(JumpActivity.this, WelcomeActivity.class);
		startActivity(intent);
		JumpActivity.this.finish();
	}
	private void initViewPagers(int count) {
		initAdsBitmap(count);
		mAdsPagerViews = new ImageView[count];
		LayoutInflater flater = getLayoutInflater();
		for (int j = 0; j < count; j++) {
			mAdsPagerViews[j] = (ImageView) flater.inflate(R.layout.ads, null, false);
			mAdsPagerViews[j].setImageBitmap(mAdsBitmaps[j]);
		}
	}
	private void initAdsBitmap(int count) {
		if (mAdsBitmaps == null) {
			mAdsBitmaps = new Bitmap[count];
		} else {
			for(Bitmap bitmap:mAdsBitmaps) {
				bitmap.recycle();
			}
		}
		mAdsBitmaps = BitmapUtils.getSuitedBitmaps(this, mAddsDrawableId, 800, 1024);
	}
	private void initDots(int count){
		LayoutInflater flater = getLayoutInflater();
		if (mDotDrawableArray == null) {
			mDotDrawableArray = new Drawable[2];
			mDotDrawableArray[0] = this.getResources().getDrawable(R.drawable.ads_dot);
			mDotDrawableArray[1] = this.getResources().getDrawable(R.drawable.ads_dot_on);
		}
		mDotsViews = new ImageView[count];
		for (int j = 0; j < count; j++) {
			mDotsViews[j] = (ImageView) flater.inflate(R.layout.activity_jump_dot, mDotsLayout, false);
			if (mCurrentPagerIndex == j) {
				mDotsViews[j].setImageDrawable(mDotDrawableArray[1]);
			} else {
				mDotsViews[j].setImageDrawable(mDotDrawableArray[0]);
			}
			mDotsLayout.addView(mDotsViews[j]);
		}
	}
	
	class AdsViewPagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return mAdsPagerViews.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		private View getView(ViewGroup container, int position) {
			container.addView(mAdsPagerViews[position]);
			return mAdsPagerViews[position];
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			return getView(container, position);
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mAdsPagerViews[position]);
		}
		
	}

	private boolean checkFirst(){
        boolean first = MyApplication.getInstance().mPreferManager.getBoolean("firstStartApp", true);
        if (first) {
        	MyApplication.getInstance().mPreferManager.edit().putBoolean("firstStartApp", false).commit();
        }
        return first;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.button_go:
			goWelcomeActivity();
			break;
		}
		
	}

}
