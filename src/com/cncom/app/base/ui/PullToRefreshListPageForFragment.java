package com.cncom.app.base.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lnwoowken.lnwoowkenbook.MyApplication;
import com.lnwoowken.lnwoowkenbook.R;
import com.lnwoowken.lnwoowkenbook.ServiceObject;
import com.shwy.bestjoy.utils.AdapterWrapper;
import com.shwy.bestjoy.utils.AsyncTaskUtils;
import com.shwy.bestjoy.utils.DebugUtils;
import com.shwy.bestjoy.utils.InfoInterface;
import com.shwy.bestjoy.utils.NetworkUtils;
import com.shwy.bestjoy.utils.PageInfo;
import com.shwy.bestjoy.utils.Query;

public abstract class PullToRefreshListPageForFragment extends BaseFragment implements AdapterView.OnItemClickListener{

	private static final String TAG ="PullToRefreshListPageActivity";
	protected ListView mListView;
	protected TextView mEmptyView;
	private Query mQuery;
	protected Context mGlobalContext;
	private AdapterWrapper<? extends BaseAdapter> mAdapterWrapper;
	private ContentResolver mContentResolver;
	
	private PullToRefreshListView mPullRefreshListView;
	/**第一次刷新*/
	private boolean mIsFirstRefresh= false;
	private boolean mDestroyed = false;
	private View mLoadMoreFootView;
	private long mLastRefreshTime = -1, mLastClickTitleTime = -1;
	/**如果导航回该界面，从上次刷新以来已经10分钟了，那么自动开始刷新*/
	private static final int MAX_REFRESH_TIME = 1000 * 60 * 10;
	
	private ProgressBar mFooterViewProgressBar;
	private TextView mFooterViewStatusText;
	private boolean mIsUpdate = false;
	
	private boolean isNeedRequestAgain = true;
	/**如果当前在列表底部了*/
	private boolean mIsAtListBottom = false;
	private WakeLock mWakeLock;
	
	//子类必须实现的方法
	/**提供一个CursorAdapter类的包装对象*/
	protected abstract AdapterWrapper<? extends BaseAdapter> getAdapterWrapper();
	/**检查intent是否包含必须数据，如果没有将finish自身*/
//	protected abstract boolean checkIntent(Intent intent);
	/**返回本地的Cursor*/
	protected abstract Cursor loadLocal(ContentResolver contentResolver);
	protected abstract int savedIntoDatabase(ContentResolver contentResolver, List<? extends InfoInterface> infoObjects);
	protected abstract List<? extends InfoInterface> getServiceInfoList(InputStream is, PageInfo pageInfo);
	protected abstract Query getQuery();
	protected abstract void onRefreshStart();
	protected abstract void onRefreshEnd();
	protected void onRefreshPostEnd() {}
	protected void onLoadLocalEnd() {}
	protected abstract int getContentLayout();
	protected ListView getListView() {
		return mListView;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGlobalContext = MyApplication.getInstance();
		mContentResolver = mGlobalContext.getContentResolver();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mQuery = getQuery();
		if (mQuery == null) {
			DebugUtils.logD(TAG, "getQuery() return null");
		}
		View view = inflater.inflate(getContentLayout(), container, false);
		mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		
//		mEmptyView = (TextView) view.findViewById(android.R.id.empty);
		
		
		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(mGlobalContext, System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				//重设为0，这样我们可以从头开始更新数据
				if (mQuery == null) {
					mQuery = getQuery();
					if (mQuery.mPageInfo == null) {
						mQuery.mPageInfo = new PageInfo();
					}
				}
				mQuery.mPageInfo.reset();
				isNeedRequestAgain = true;
				int count = mAdapterWrapper.getCount();
				mQuery.mPageInfo.computePageSize(count);
				// Do work to refresh the list here.
				loadServerDataAsync();
			}
		});
		
		mPullRefreshListView.setScrollingWhileRefreshingEnabled(true);
		
		mListView = mPullRefreshListView.getRefreshableView();
		mListView.setOnItemClickListener(this);
		addFooterView();
		updateFooterView(false, null);
		mAdapterWrapper = getAdapterWrapper();
		mListView.setAdapter(mAdapterWrapper.getAdapter());
		removeFooterView();
		
		mIsFirstRefresh = true;
		
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && mIsAtListBottom && !mIsUpdate) {
					DebugUtils.logExchangeBC(TAG, "we go to load more.");
					if (isNeedRequestAgain) {
						updateFooterView(true, null);
						new QueryServiceTask().execute();
					} else {
						DebugUtils.logExchangeBC(TAG, "isNeedRequestAgain is false, we not need to load more");
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (totalItemCount > 0) {
					if (firstVisibleItem == 0 && firstVisibleItem + visibleItemCount == totalItemCount) {
						mIsAtListBottom = true;
					} else if (firstVisibleItem > 0 && firstVisibleItem + visibleItemCount < totalItemCount) {
						mIsAtListBottom = false;
					} else {
						mIsAtListBottom = true;
					}
					
				} else {
					mIsAtListBottom = false;
				}
				
			}
			
		});
		
		return view;
	}
	@Override
	public void onResume() {
		super.onResume();
		if (isNeedLoadLocalOnResume()) {
			loadLocalDataAsync();
		}
		if (isNeedForceRefreshOnResume()) {
			//手动刷新一次
			forceRefresh();
		}
	}
	/**
	 * 当Activity onResume时候是否要做一次强制刷新，默认实现是 如果导航回该界面，从上次刷新以来已经10分钟了，那么自动开始刷新
	 * @return
	 */
	protected boolean isNeedForceRefreshOnResume() {
		long resumTime = System.currentTimeMillis();
		return resumTime - mLastRefreshTime > MAX_REFRESH_TIME;
	}
	
	protected boolean isNeedLoadLocalOnResume() {
		return true;
	}
	
	public void forceRefresh() {
		if (mQuery == null) {
			mQuery = getQuery();
			if (mQuery.mPageInfo == null) {
				mQuery.mPageInfo = new PageInfo();
			}
		}
		//手动刷新一次
		mPullRefreshListView.getLoadingLayoutProxy().setRefreshingLabel(getString(R.string.pull_to_refresh_refreshing_label));
		//手动刷新一次
		mPullRefreshListView.setRefreshing();
		mQuery.mPageInfo.reset();
		int count = mAdapterWrapper.getCount();
		mQuery.mPageInfo.computePageSize(count);
		 //Do work to refresh the list here.
		isNeedRequestAgain = true;
		loadServerDataAsync();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onStop() {
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mDestroyed = true;
		AsyncTaskUtils.cancelTask(mQueryServiceTask);
		AsyncTaskUtils.cancelTask(mLoadLocalTask);
		if (mAdapterWrapper != null) mAdapterWrapper.releaseAdapter();
	}
	
	private void addFooterView() {
		if (mLoadMoreFootView != null) return; 
		if (mLoadMoreFootView == null) {
			mLoadMoreFootView = LayoutInflater.from(mGlobalContext).inflate(R.layout.load_more_footer, mListView, false);
			mFooterViewProgressBar = (ProgressBar) mLoadMoreFootView.findViewById(R.id.load_more_progressBar);
			mFooterViewStatusText = (TextView) mLoadMoreFootView.findViewById(R.id.load_more_text);
		}
		mListView.addFooterView(mLoadMoreFootView, true, false);
	}
	
	private void removeFooterView() {
		if (mLoadMoreFootView != null) {
			mListView.removeFooterView(mLoadMoreFootView);
			mLoadMoreFootView = null;
		}
	}
	
	private void updateFooterView(boolean loading, String status) {
		if (mLoadMoreFootView == null) {
			addFooterView();
		}
		if (loading) {
			mFooterViewProgressBar.setVisibility(View.VISIBLE);
		} else {
			mFooterViewProgressBar.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(status)) mFooterViewStatusText.setText(status);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View itemView, int position, long id) {
		//子类实现具体的动作
	}
	
	private LoadLocalTask mLoadLocalTask;
	private void loadLocalDataAsync() {
		AsyncTaskUtils.cancelTask(mLoadLocalTask);
		mLoadLocalTask = new LoadLocalTask();
		mLoadLocalTask.execute();
	}
	private class LoadLocalTask extends AsyncTask<Void, Void, Cursor> {

		@Override
		protected Cursor doInBackground(Void... params) {
			DebugUtils.logD(TAG, "LoadLocalTask load local data....");
			return loadLocal(mContentResolver);
		}

		@Override
		protected void onPostExecute(Cursor result) {
			super.onPostExecute(result);
			mAdapterWrapper.changeCursor(result);
			int requestCount = 0;
			if (result != null) {
				requestCount = result.getCount();
			}
			onLoadLocalEnd();
			DebugUtils.logD(TAG, "LoadLocalTask load local data finish....localCount is " + requestCount);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
		
	}
	
	private QueryServiceTask mQueryServiceTask;
	private void loadServerDataAsync() {
		AsyncTaskUtils.cancelTask(mQueryServiceTask);
		mQueryServiceTask = new QueryServiceTask();
		mQueryServiceTask.execute();
	}

	/**更新或是新增的总数 >0表示有更新数据，需要刷新，=-1网络问题， =-2 已是最新数据 =0 没有更多数据*/
	private class QueryServiceTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... arg0) {
			mIsUpdate = true;
			int insertOrUpdateCount = 0;
			try {
				if (mQuery.mPageInfo.mPageIndex == PageInfo.DEFAULT_PAGEINDEX) {
					//开始刷新
					onRefreshStart();
					if (mIsFirstRefresh) {
						mIsFirstRefresh = false;
						final Cursor cursor = loadLocal(mContentResolver);
						if (cursor != null) {
							mQuery.mPageInfo.computePageSize(cursor.getCount());
							cursor.close();
						}
					}
				}
//				if (mIsFirstRefresh) {
//					mIsFirstRefresh = false;
//					DebugUtils.logD(TAG, "first load local data....");
//					final Cursor cursor = loadLocal(mContentResolver);
//					if (cursor != null && cursor.getCount() != 0) {
//						int requestCount = cursor.getCount();
//						MyApplication.getInstance().postAsync(new Runnable() {
//
//							@Override
//							public void run() {
//								mAdapterWrapper.changeCursor(cursor);
//							}
//							
//						});
//						
//						DebugUtils.logD(TAG, "load local data finish....localCount is " + requestCount);
//						mPageInfo.computePageSize(requestCount);
//					}
//				}
				
//				while (isNeedRequestAgain) {
					DebugUtils.logD(TAG, "openConnection....");
					DebugUtils.logD(TAG, "start pageIndex " + mQuery.mPageInfo.mPageIndex + " pageSize = " + mQuery.mPageInfo.mPageSize);
					InputStream is = NetworkUtils.openContectionLocked(ServiceObject.buildPageQuery(mQuery.qServiceUrl, mQuery.mPageInfo.mPageIndex, mQuery.mPageInfo.mPageSize), MyApplication.getInstance().getSecurityKeyValuesObject());
					if (is == null) {
						DebugUtils.logD(TAG, "finish task due to openContectionLocked return null InputStream");
						isNeedRequestAgain = false;
						return 0;
					}
					DebugUtils.logD(TAG, "begin parseList....");
					List<? extends InfoInterface> serviceInfoList = getServiceInfoList(is, mQuery.mPageInfo);
					int newCount = serviceInfoList.size();
					DebugUtils.logD(TAG, "find new date #count = " + newCount + " totalSize = " + mQuery.mPageInfo.mTotalCount);
					if (mQuery.mPageInfo.mPageIndex == PageInfo.DEFAULT_PAGEINDEX) {
						onRefreshEnd();
					}
					if (newCount == 0) {
						DebugUtils.logD(TAG, "no more date");
						isNeedRequestAgain = false;
						return 0;
					}
					if (mQuery.mPageInfo.mTotalCount <= mQuery.mPageInfo.mPageIndex * mQuery.mPageInfo.mPageSize) {
						DebugUtils.logD(TAG, "returned data count is less than that we requested, so not need to pull data again");
						isNeedRequestAgain = false;
					}
					
					DebugUtils.logD(TAG, "begin insert or update local database");
					insertOrUpdateCount = savedIntoDatabase(mContentResolver, serviceInfoList);
					//若果不需要再请求数据了，我们返回更新的结果
//					if (!isNeedRequestAgain || insertOrUpdateCount >= PER_PAGE_SIZE / 2) {
//						return insertOrUpdateCount;
//					}
//					final Cursor cursor = loadLocal(mContentResolver);
//					if (cursor != null && cursor.getCount() != 0) {
//						MyApplication.getInstance().postAsync(new Runnable() {
//
//							@Override
//							public void run() {
//								mAdapterWrapper.changeCursor(cursor);
//							}
//							
//						});
//					}
//					mCurrentPageIndex++;
					return insertOrUpdateCount;
//				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				isNeedRequestAgain=false;
				return -1;
			} catch (IOException e) {
				e.printStackTrace();
				isNeedRequestAgain=false;
				return -1;
			}
		}


		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (mDestroyed) return;
			if (result == -1){
				MyApplication.getInstance().showMessage(R.string.msg_network_error_for_receive);
			} 
//			else if (result == -2) {
//				MyApplication.getInstance().showMessage(R.string.msg_nonew_for_receive);
//			} else if (result == 0) {
//				MyApplication.getInstance().showMessage(R.string.msg_nomore_for_receive);
//			}
			if (!isNeedRequestAgain) {
				removeFooterView();
				
			}
			mLastRefreshTime = System.currentTimeMillis();
			// Call onRefreshComplete when the list has been refreshed.
		    mPullRefreshListView.onRefreshComplete();
		    mIsUpdate = false;
		    onRefreshPostEnd();
		    loadLocalDataAsync();
		    if (isNeedRequestAgain) {
		    	mQuery.mPageInfo.mPageIndex+=1;
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
		
	}
	
}
