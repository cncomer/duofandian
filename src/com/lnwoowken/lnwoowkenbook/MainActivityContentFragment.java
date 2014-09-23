package com.lnwoowken.lnwoowkenbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import com.cncom.app.base.service.PhotoManagerUtilsV2;
import com.cncom.app.base.service.PhotoManagerUtilsV2.TaskType;
import com.cncom.app.base.ui.BaseFragment;
import com.cncom.app.base.util.DebugUtils;
import com.cncom.app.base.util.InstallFileUtils;
import com.lnwoowken.lnwoowkenbook.ServiceObject.ServiceResultObject;
import com.lnwoowken.lnwoowkenbook.animition.MyAnimition;
import com.lnwoowken.lnwoowkenbook.animition.Mycamera;
import com.lnwoowken.lnwoowkenbook.model.Contant;
import com.shwy.bestjoy.utils.AsyncTaskUtils;
import com.shwy.bestjoy.utils.NetworkUtils;

public class MainActivityContentFragment extends BaseFragment implements View.OnClickListener{
	private static final String TAG = "MainActivityContentFragment";
	private Handler mHandler;
	private TextView mGalleryTitle;
	private LinearLayout mDotsLayout;
	private ViewPager mAdsViewPager;
	private boolean mAdsViewPagerIsScrolling = false;
	private ImageView[] mDotsViews = null;
	private ImageView[] mAdsPagerViews = null;
	private Drawable[] mDotDrawableArray;
	private String[] mImageTitle = new String[] { "望湘园", "侬好蛙", "望湘园", "星怡会", "码头人家" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			mCurrentPagerIndex = savedInstanceState.getInt("adsIndex", 0);
			DebugUtils.logD(TAG, "onCreate savedInstanceState adsIndex=" + mCurrentPagerIndex);
		}
		PhotoManagerUtilsV2.getInstance().requestToken(TAG);
		mHandler = new Handler();
	}
	@Override
	public void onResume() {
		super.onResume();
		mHandler.removeCallbacks(mChangeAdsRunnable);
		if (mAdsViewPager.getAdapter() != null && mAdsViewPager.getAdapter().getCount() > 0) {
			mHandler.postDelayed(mChangeAdsRunnable, DEFAULT_DELAY);
		}
		
	}

	@Override
	public void onPause() {
		super.onPause();
		mHandler.removeCallbacks(mChangeAdsRunnable);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		AsyncTaskUtils.cancelTask(mLoadAdsTask);
	    AsyncTaskUtils.cancelTask(mLoadLocalAdsTask);
	    PhotoManagerUtilsV2.getInstance().releaseToken(TAG);
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
		
//		mAdsFrameLayout = (FrameLayout) view.findViewById(R.id.relativeLayout_pictures);
		
		mGalleryTitle = (TextView) view.findViewById(R.id.textView_imgtitle);
		mGalleryTitle.setText("");//设置为第一幅的标题
		mDotsLayout = (LinearLayout) view.findViewById(R.id.dots);
		mAdsViewPager = (ViewPager) view.findViewById(R.id.adsViewPager);
		mAdsViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if (mCurrentPagerIndex != position) {
					mDotsViews[mCurrentPagerIndex].setImageDrawable(mDotDrawableArray[0]);
					mDotsViews[position].setImageDrawable(mDotDrawableArray[1]);
					mCurrentPagerIndex = position;
					AdsViewPagerAdapter adapter = (AdsViewPagerAdapter) mAdsViewPager.getAdapter();
					mGalleryTitle.setText(adapter.getItem(position)._name);
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
		
		loadLocalAdsAync();
		loadAdsAsync();
		
	}
	
	private void initDots(int count){
		mDotsLayout.removeAllViews();
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
	
	private void initViewPagers(int count) {
		mAdsPagerViews = new ImageView[count];
		LayoutInflater flater = getActivity().getLayoutInflater();
		for (int j = 0; j < count; j++) {
			mAdsPagerViews[j] = (ImageView) flater.inflate(R.layout.ads, null, false);
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

		List<AdsObject> mAdsList;
		private AdsViewPagerAdapter(List<AdsObject> data) {
			mAdsList = data;
		}
		@Override
		public int getCount() {
			return mAdsList.size();
		}

		public AdsObject getItem(int position) {
			return mAdsList.get(position);
		}
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		private View getView(ViewGroup container, int position) {
			container.addView(mAdsPagerViews[position]);
			AdsObject adsObject = getItem(position);
			PhotoManagerUtilsV2.getInstance().loadPhotoAsync(TAG, mAdsPagerViews[position], adsObject._path, null, TaskType.INDEX_IMAGE);
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
	private static final String ADS_FILE_NAME = "ads.xml";
	private List<AdsObject> mAdsObjectList = new ArrayList<AdsObject>();
	private class AdsObject {
		private String _path, _name;
		private int _index;
	}
	private LoadAdsTask mLoadAdsTask;
    private void loadAdsAsync() {
    	AsyncTaskUtils.cancelTask(mLoadAdsTask);
    	mLoadAdsTask = new LoadAdsTask();
    	mLoadAdsTask.execute();
    }
    
    private LoadLocalAdsTask mLoadLocalAdsTask;
    private void loadLocalAdsAync() {
    	AsyncTaskUtils.cancelTask(mLoadLocalAdsTask);
    	mLoadLocalAdsTask = new LoadLocalAdsTask();
    	mLoadLocalAdsTask.execute();
    }
    
    /**
     * {"StatusCode":"1","Data":[{"ShopID":null,"ImgAddr":"../comphoto/2014-09-23/90d3c9b8b285da5010ebfc87d9dbc079.jpg","imgindex":"2"},
     * @author chenkai
     *
     */
    private class LoadAdsTask extends AsyncTask<Void, Void, ServiceResultObject> {

		@Override
		protected ServiceResultObject doInBackground(Void... params) {
			InputStream is = null;
			ServiceResultObject serviceResultObject = new ServiceResultObject();
			try {
				is = NetworkUtils.openContectionLocked(ServiceObject.getIndexPageAdsUrl(), MyApplication.getInstance().getSecurityKeyValuesObject());
				if (is != null) {
					//将数据缓存成文件
					boolean saved = InstallFileUtils.saveFile(is, MyApplication.getInstance().getAppFiles(ADS_FILE_NAME));
					if (saved) {
						String content = NetworkUtils.getContentFromInput(new FileInputStream(MyApplication.getInstance().getAppFiles(ADS_FILE_NAME)));
						serviceResultObject = ServiceResultObject.parseJsonArray(content);
						
						if (serviceResultObject.isOpSuccessfully()) {
							mAdsObjectList.clear();
							
							if (serviceResultObject.mJsonArrayData != null) {
								int len = serviceResultObject.mJsonArrayData.length();
								JSONObject jsonObject = null;
								AdsObject adsObjet = null;
								for(int index = 0; index < len; index++) {
									jsonObject = serviceResultObject.mJsonArrayData.getJSONObject(index);
									adsObjet = new AdsObject();
									adsObjet._path = jsonObject.optString("ImgAddr", "").replace("../", "");
									adsObjet._name = mImageTitle[index];
									adsObjet._index = Integer.valueOf(jsonObject.getString("imgindex"))-1;
									mAdsObjectList.add(adsObjet);
								}
							}
							
						}
					}
				}
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
			} catch (IOException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
			} catch (JSONException e) {
				e.printStackTrace();
				serviceResultObject.mStatusMessage = e.getMessage();
			}
			return serviceResultObject;
		}

		@Override
		protected void onPostExecute(ServiceResultObject result) {
			super.onPostExecute(result);
			if (result.isOpSuccessfully()) {
				initDots(mAdsObjectList.size());
				initViewPagers(mAdsObjectList.size());
				mAdsViewPager.setAdapter(new AdsViewPagerAdapter(mAdsObjectList));
				mCurrentPagerIndex = 0;
				mHandler.removeCallbacks(mChangeAdsRunnable);
				if (mAdsViewPager.getAdapter().getCount() > 0) {
					mGalleryTitle.setText(mAdsObjectList.get(mCurrentPagerIndex)._name);//设置为第一幅的标题
					mHandler.postDelayed(mChangeAdsRunnable, DEFAULT_DELAY);
				}
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
    }
		
		
		private class LoadLocalAdsTask extends AsyncTask<Void, Void, ServiceResultObject> {

			@Override
			protected ServiceResultObject doInBackground(Void... params) {
				ServiceResultObject serviceResultObject = new ServiceResultObject();
				try {
					//读取缓存数据文件
					File adsFile = MyApplication.getInstance().getAppFiles(ADS_FILE_NAME);
					if (adsFile.exists()) {
						String content = NetworkUtils.getContentFromInput(new FileInputStream(adsFile));
						serviceResultObject = ServiceResultObject.parseJsonArray(content);
						
						if (serviceResultObject.isOpSuccessfully()) {
							mAdsObjectList.clear();
							
							if (serviceResultObject.mJsonArrayData != null) {
								int len = serviceResultObject.mJsonArrayData.length();
								JSONObject jsonObject = null;
								AdsObject adsObjet = null;
								for(int index = 0; index < len; index++) {
									jsonObject = serviceResultObject.mJsonArrayData.getJSONObject(index);
									adsObjet = new AdsObject();
									adsObjet._path = jsonObject.optString("ImgAddr", "").replace("../", "");
									adsObjet._name = mImageTitle[index];
									mAdsObjectList.add(adsObjet);
								}
							}
							
					    }
					}
					 
				} catch (IOException e) {
					e.printStackTrace();
					serviceResultObject.mStatusMessage = e.getMessage();
				} catch (JSONException e) {
					e.printStackTrace();
					serviceResultObject.mStatusMessage = e.getMessage();
				}
				return serviceResultObject;
			}

			@Override
			protected void onPostExecute(ServiceResultObject result) {
				super.onPostExecute(result);
				if (result.isOpSuccessfully()) {
					initDots(mAdsObjectList.size());
					initViewPagers(mAdsObjectList.size());
					mAdsViewPager.setAdapter(new AdsViewPagerAdapter(mAdsObjectList));
					mCurrentPagerIndex = 0;
					mHandler.removeCallbacks(mChangeAdsRunnable);
					if (mAdsViewPager.getAdapter().getCount() > 0) {
						mHandler.postDelayed(mChangeAdsRunnable, DEFAULT_DELAY);
						mGalleryTitle.setText(mAdsObjectList.get(mCurrentPagerIndex)._name);//设置为第一幅的标题
					}
				}
			}

			@Override
			protected void onCancelled() {
				super.onCancelled();
			}
		}
    	
	
	
}
