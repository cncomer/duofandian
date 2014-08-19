package com.lnwoowken.lnwoowkenbook;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.cncom.app.base.ui.BaseFragment;
import com.cncom.app.base.util.BitmapUtils;
import com.cncom.app.base.util.DebugUtils;
import com.lnwoowken.lnwoowkenbook.adapter.MyGalleryAdapter;
import com.lnwoowken.lnwoowkenbook.animition.MyAnimition;
import com.lnwoowken.lnwoowkenbook.animition.Mycamera;
import com.lnwoowken.lnwoowkenbook.model.Contant;

public class MainActivityContentFragment extends BaseFragment implements View.OnClickListener{
	private static final String TAG = "MainActivityContentFragment";
	private Handler mHandler;
	private MyGalleryAdapter mMyGalleryAdapter;
	private TextView mGalleryTitle;
	private LinearLayout mDotsLayout;
	private ViewPager mAdsViewPager;
	private boolean mAdsViewPagerIsScrolling = false;
	private Bitmap[] mAdsBitmaps;
	private ImageView[] mDotsViews = null;
	private ImageView[] mAdsPagerViews = null;
	private Drawable[] mDotDrawableArray;
	private int[] mAddsDrawableId = { R.drawable.pic_1, R.drawable.pic_2,
			R.drawable.pic_3, R.drawable.pic_4, R.drawable.pic_5, };
	private String[] mImageTitle = new String[] { "码头人家", "码头人家", "码头人家", "码头人家", "码头人家" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			mCurrentPagerIndex = savedInstanceState.getInt("adsIndex", 0);
			DebugUtils.logD(TAG, "onCreate savedInstanceState adsIndex=" + mCurrentPagerIndex);
		}
		mHandler = new Handler();
	}
	@Override
	public void onResume() {
		super.onResume();
		mHandler.postDelayed(mChangeAdsRunnable, DEFAULT_DELAY);
	}

	@Override
	public void onPause() {
		super.onPause();
		mHandler.removeCallbacks(mChangeAdsRunnable);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.right_panel, container, false);
		initViews(view);
		return view;
	}
	
	private void initViews(View view) {
		view.findViewById(R.id.imageButton_book).setOnClickListener(this);
		view.findViewById(R.id.imageButton_pick_food).setOnClickListener(this);
		view.findViewById(R.id.imageButton_vip).setOnClickListener(this);
		
		view.findViewById(R.id.imageView_bottom).setOnClickListener(this);
		
		mGalleryTitle = (TextView) view.findViewById(R.id.textView_imgtitle);
		mDotsLayout = (LinearLayout) view.findViewById(R.id.dots);
		mAdsViewPager = (ViewPager) view.findViewById(R.id.adsViewPager);
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
					mGalleryTitle.setText(mImageTitle[mCurrentPagerIndex]);
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				mAdsViewPagerIsScrolling  = state == 1;
				
			}
		});
		
		
	}
	
	private void initViewPagers(int count) {
		initAdsBitmap(count);
		mAdsPagerViews = new ImageView[count];
		LayoutInflater flater = getActivity().getLayoutInflater();
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
		mAdsBitmaps = BitmapUtils.getSuitedBitmaps(getActivity(), mAddsDrawableId, 800, 1024);
	}
	private void initDots(int count){
		LayoutInflater flater = getActivity().getLayoutInflater();
		if (mDotDrawableArray == null) {
			mDotDrawableArray = new Drawable[2];
			mDotDrawableArray[0] = this.getResources().getDrawable(R.drawable.ads_dot);
			mDotDrawableArray[1] = this.getResources().getDrawable(R.drawable.ads_dot_on);
		}
		mDotsViews = new ImageView[count];
		for (int j = 0; j < count; j++) {
			mDotsViews[j] = (ImageView) flater.inflate(R.layout.dot, mDotsLayout, false);
			if (mCurrentPagerIndex == j) {
				mDotsViews[j].setImageDrawable(mDotDrawableArray[1]);
			} else {
				mDotsViews[j].setImageDrawable(mDotDrawableArray[0]);
			}
			mDotsLayout.addView(mDotsViews[j]);
		}
	}
	
	private static long DEFAULT_DELAY = 5000;
	private int mCurrentPagerIndex = 0;
	private ChangeAdsRunnable mChangeAdsRunnable = new ChangeAdsRunnable();
	private class ChangeAdsRunnable implements Runnable {
		@Override
		public void run() {
			if (!mAdsViewPagerIsScrolling) {
				int pageCount = mAdsViewPager.getAdapter().getCount();
				int nextPage = mCurrentPagerIndex % pageCount + 1;
				if (nextPage >= pageCount) {
					nextPage = 0;
				}
				mAdsViewPager.setCurrentItem(nextPage);
			}
			mHandler.postDelayed(this, DEFAULT_DELAY);
		}
		
	}

	@Override
	public void onClick(View v) {
		int id  = v.getId();
		switch(id) {
		case R.id.imageButton_book:
		case R.id.imageButton_pick_food:
		case R.id.imageButton_vip:
			int flag = 0;
			if (id == R.id.imageButton_book) {
				flag = Contant.ANIMITION_START_RESTUARANTLISTACITVITY;
			} else if (id == R.id.imageButton_pick_food) {
				MyApplication.getInstance().showUnsupportMessage();
			} else if (id == R.id.imageButton_vip) {
				flag = Contant.ANIMITION_START_REGISTACITVITY;
			}
			
			Mycamera animition = new Mycamera(true);
			animition.setAnimationListener(new MyAnimition(flag, getActivity()));
			v.startAnimation(animition);
			break;
		}
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("adsIndex", mCurrentPagerIndex);
		DebugUtils.logD(TAG, "onSaveInstanceState save adsIndex=" + mCurrentPagerIndex);
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
	
	
}
