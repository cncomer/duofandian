package com.lnwoowken.lnwoowkenbook.view.VercialTabHost;

import java.util.ArrayList;
import java.util.List;

import com.lnwoowken.lnwoowkenbook.R;


import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.View.OnKeyListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
//import android.widget.TabHost;
//import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

/*import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;*/
//import android.widget.TabHost.TabSpec;


public class MyTabHostLayout extends FrameLayout implements ViewTreeObserver.OnTouchModeChangeListener{
	
    private LinearLayout mTabWidget;
    private FrameLayout mTabContent;
    private List<TabSpec> mTabSpecs = new ArrayList<TabSpec>(2);
    
    protected int mCurrentTab = -1;
    private View mCurrentView = null;
    
    protected LocalActivityManager mLocalActivityManager = null;
    private OnTabChangeListener mOnTabChangeListener;
    private OnKeyListener mTabKeyListener;

	public MyTabHostLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

    public MyTabHostLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //initTabHost();
    }
	
	public void setCurrentTab(int index){
        if (index < 0 || index >= mTabSpecs.size()) {
            return;
        }

        if (index == mCurrentTab) {
            return;
        }

        // notify old tab content
        if (mCurrentTab != -1) {
        	mTabWidget.getChildAt(mCurrentTab).setSelected(false);
            mTabSpecs.get(mCurrentTab).mContentStrategy.tabClosed();
        }

        
        final TabSpec spec = mTabSpecs.get(index);

        // Call the tab widget's focusCurrentTab(), instead of just
        // selecting the tab.
        //mTabWidget.focusCurrentTab(mCurrentTab);
        final int oldTab = mCurrentTab;

        // set the tab
        //setCurrentTab(index);

        
        //mSelectedTab = index;
        mTabWidget.getChildAt(index).setSelected(true);
        //mStripMoved = true;

        // change the focus if applicable.
        if (oldTab != index) {
        	mTabWidget.getChildAt(index).requestFocus();
        }

        mCurrentTab = index;
        // tab content
        mCurrentView = spec.mContentStrategy.getContentView();

        if (mCurrentView.getParent() == null) {
            mTabContent
                    .addView(
                            mCurrentView,
                            new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT));
        }

        if (!mTabWidget.hasFocus()) {
            // if the tab widget didn't take focus (likely because we're in touch mode)
            // give the current tab content view a shot
            mCurrentView.requestFocus();
        }

        //mTabContent.requestFocus(View.FOCUS_FORWARD);
        invokeOnTabChangeListener();
	}

	private void invokeOnTabChangeListener() {
		// TODO Auto-generated method stub
		//mOnTabChangeListener.onTabChanged(mCurrentTab);
        if (mOnTabChangeListener != null) {
            mOnTabChangeListener.onTabChanged(getCurrentTabTag());
        }
	}



	private String getCurrentTabTag() {
		// TODO Auto-generated method stub
        if (mCurrentTab >= 0 && mCurrentTab < mTabSpecs.size()) {
            return mTabSpecs.get(mCurrentTab).getTag();
        }
        return null;
	}



	public void setOnTabChangedListener(OnTabChangeListener l) {
		// TODO Auto-generated method stub
		mOnTabChangeListener = l;
	}
	
	public void setup() {
		// TODO Auto-generated method stub
		mTabWidget = (LinearLayout)findViewById(R.id.tabs);
        if (mTabWidget == null) {
            throw new RuntimeException(
                    "Your MyTabHostLayout must have a TabWidget that id :R.id.tabs");
        }
        
        mTabKeyListener = new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                    case KeyEvent.KEYCODE_DPAD_UP:
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                    case KeyEvent.KEYCODE_ENTER:
                        return false;

                }
                mTabContent.requestFocus(View.FOCUS_FORWARD);
                return mTabContent.dispatchKeyEvent(event);
            }

        };
        
        mTabContent = (FrameLayout) findViewById(R.id.tabcontent);
        if (mTabContent == null) {
            throw new RuntimeException(
                    "Your MyTabHostLayout must have a FrameLayout that id:R.id.tabcontent");
        }
	}

	public void setup(LocalActivityManager activityGroup) {
		// TODO Auto-generated method stub
        setup();
        mLocalActivityManager = activityGroup;
	}

	public LinearLayout getTabWidget() {
		// TODO Auto-generated method stub
		return mTabWidget;
	}

	public TabSpec newTabSpec(String string) {
		// TODO Auto-generated method stub
		return new TabSpec(string);
	}


    
	private View.OnClickListener onTabSpecClickListener = new View.OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.i("onTabSpecClickListener", ""+v.getClass());
			
			setCurrentTab(getTabIdByView(v));
		}
	};
	
	private int getTabIdByView(View v) {
		// TODO Auto-generated method stub
		for (int i = 0;i <mTabWidget.getChildCount();i++){
			if (v == mTabWidget.getChildAt(i)){
				Log.i("change tag", "click tag id:"+i);
				return i;
			}
		}
		return 0;
	}
		
	public void addTab(TabSpec tabSpec) {
		// TODO Auto-generated method stub
        if (tabSpec.mIndicatorStrategy == null) {
            throw new IllegalArgumentException("you must specify a way to create the tab indicator.");
        }

        if (tabSpec.mContentStrategy == null) {
            throw new IllegalArgumentException("you must specify a way to create the tab content");
        }
        View tabIndicator = tabSpec.mIndicatorStrategy.createIndicatorView();
        tabIndicator.setOnKeyListener(mTabKeyListener);
        tabIndicator.setOnClickListener(onTabSpecClickListener);

        // If this is a custom view, then do not draw the bottom strips for
        // the tab indicators.
        if (tabSpec.mIndicatorStrategy instanceof ViewIndicatorStrategy) {
            //mTabWidget.setStripEnabled(false);
            //invalidate();
        }
        mTabWidget.addView(tabIndicator);
        mTabSpecs.add(tabSpec);

        if (mCurrentTab == -1) {
            setCurrentTab(0);
        }
	}

	public int getCurrentTab() {
		// TODO Auto-generated method stub
		return mCurrentTab;
	}
	

    
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        final ViewTreeObserver treeObserver = getViewTreeObserver();
        if (treeObserver != null) {
            treeObserver.addOnTouchModeChangeListener(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        final ViewTreeObserver treeObserver = getViewTreeObserver();
        if (treeObserver != null) {
            treeObserver.removeOnTouchModeChangeListener(this);
        }
    }
    
    public void onTouchModeChanged(boolean isInTouchMode) {
        if (!isInTouchMode) {
            // leaving touch mode.. if nothing has focus, let's give it to
            // the indicator of the current tab
            if (mCurrentView != null && (!mCurrentView.hasFocus() || mCurrentView.isFocused())) {
                mTabWidget.getChildAt(mCurrentTab).requestFocus();
            }
        }
    }
    
    
    /**
     * A tab has a tab indicator, content, and a tag that is used to keep
     * track of it.  This builder helps choose among these options.
     *
     * For the tab indicator, your choices are:
     * 1) set a label
     * 2) set a label and an icon
     *
     * For the tab content, your choices are:
     * 1) the id of a {@link View}
     * 2) a {@link TabContentFactory} that creates the {@link View} content.
     * 3) an {@link Intent} that launches an {@link android.app.Activity}.
     */
    public class TabSpec {

        private String mTag;

        private IndicatorStrategy mIndicatorStrategy;
        private ContentStrategy mContentStrategy;

        private TabSpec(String tag) {
            mTag = tag;
        }

/*        *//**
         * Specify a label as the tab indicator.
         *//*
        public TabSpec setIndicator(CharSequence label) {
            mIndicatorStrategy = new LabelIndicatorStrategy(label);
            return this;
        }*/

        /**
         * Specify a label and icon as the tab indicator.
         */
        public TabSpec setIndicator(CharSequence label, Drawable icon) {
            mIndicatorStrategy = new LabelAndIconIndicatorStrategy(label, icon);
            return this;
        }

/*        *//**
         * Specify a view as the tab indicator.
         *//*
        public TabSpec setIndicator(View view) {
            mIndicatorStrategy = new ViewIndicatorStrategy(view);
            return this;
        }*/

/*        *//**
         * Specify the id of the view that should be used as the content
         * of the tab.
         *//*
        public TabSpec setContent(int viewId) {
            mContentStrategy = new ViewIdContentStrategy(viewId);
            return this;
        }

        *//**
         * Specify a {@link android.widget.TabHost.TabContentFactory} to use to
         * create the content of the tab.
         *//*
        public TabSpec setContent(TabContentFactory contentFactory) {
            mContentStrategy = new FactoryContentStrategy(mTag, contentFactory);
            return this;
        }*/

        /**
         * Specify an intent to use to launch an activity as the tab content.
         */
        public TabSpec setContent(Intent intent) {
            mContentStrategy = new IntentContentStrategy(mTag, intent);
            return this;
        }


        public String getTag() {
            return mTag;
        }
    }
    

    /**
     * Specifies what you do to create a tab indicator.
     */
    private static interface IndicatorStrategy {

        /**
         * Return the view for the indicator.
         */
        View createIndicatorView();
    }

    /**
     * Specifies what you do to manage the tab content.
     */
    private static interface ContentStrategy {

        /**
         * Return the content view.  The view should may be cached locally.
         */
        View getContentView();

        /**
         * Perhaps do something when the tab associated with this content has
         * been closed (i.e make it invisible, or remove it).
         */
        void tabClosed();
    }

/*    *//**
     * How to create a tab indicator that just has a label.
     *//*
    private class LabelIndicatorStrategy implements IndicatorStrategy {

        private final CharSequence mLabel;

        private LabelIndicatorStrategy(CharSequence label) {
            mLabel = label;
        }

        public View createIndicatorView() {
            final Context context = getContext();
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View tabIndicator = inflater.inflate(R.layout.tab_indicator,
                    mTabWidget, // tab widget is the parent
                    false); // no inflate params

            final TextView tv = (TextView) tabIndicator.findViewById(R.id.title);
            tv.setText(mLabel);

            if (context.getApplicationInfo().targetSdkVersion <= Build.VERSION_CODES.DONUT) {
                // Donut apps get old color scheme
                tabIndicator.setBackgroundResource(R.drawable.tab_indicator_v4);
                tv.setTextColor(context.getResources().getColorStateList(R.color.tab_indicator_text_v4));
            }
            
            return tabIndicator;
        }
    }*/

    /**
     * How we create a tab indicator that has a label and an icon
     */
    private class LabelAndIconIndicatorStrategy implements IndicatorStrategy {

        private final CharSequence mLabel;
        private final Drawable mIcon;

        private LabelAndIconIndicatorStrategy(CharSequence label, Drawable icon) {
            mLabel = label;
            mIcon = icon;
        }

        public View createIndicatorView() {
            final Context context = getContext();
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View tabIndicator = inflater.inflate(R.layout.tab_indicator,
                    mTabWidget, // tab widget is the parent
                    false); // no inflate params

            final TextView tv = (TextView) tabIndicator.findViewById(R.id.tabtitle);
            tv.setText(mLabel);

            /**2014.05.04
            final ImageView iconView = (ImageView) tabIndicator.findViewById(R.id.tabicon);
            iconView.setImageDrawable(mIcon);
            
            *
            */

/*            if (context.getApplicationInfo().targetSdkVersion <= Build.VERSION_CODES.DONUT) {
                // Donut apps get old color scheme
                tabIndicator.setBackgroundResource(R.drawable.tab_indicator_v4);
                tv.setTextColor(context.getResources().getColorStateList(R.color.tab_indicator_text_v4));
            }*/
            
            return tabIndicator;
        }
    }

    /**
     * How to create a tab indicator by specifying a view.
     */
    private class ViewIndicatorStrategy implements IndicatorStrategy {

        private final View mView;

        private ViewIndicatorStrategy(View view) {
            mView = view;
        }

        public View createIndicatorView() {
            return mView;
        }
    }

    /**
     * How to create the tab content via a view id.
     */
    private class ViewIdContentStrategy implements ContentStrategy {

        private final View mView;

        private ViewIdContentStrategy(int viewId) {
            mView = mTabContent.findViewById(viewId);
            if (mView != null) {
                mView.setVisibility(View.GONE);
            } else {
                throw new RuntimeException("Could not create tab content because " +
                        "could not find view with id " + viewId);
            }
        }

        public View getContentView() {
            mView.setVisibility(View.VISIBLE);
            return mView;
        }

        public void tabClosed() {
            mView.setVisibility(View.GONE);
        }
    }

    /**
     * How tab content is managed using {@link TabContentFactory}.
     */
    private class FactoryContentStrategy implements ContentStrategy {
        private View mTabContent;
        private final CharSequence mTag;
        private TabContentFactory mFactory;

        public FactoryContentStrategy(CharSequence tag, TabContentFactory factory) {
            mTag = tag;
            mFactory = factory;
        }

        public View getContentView() {
            if (mTabContent == null) {
                mTabContent = mFactory.createTabContent(mTag.toString());
            }
            mTabContent.setVisibility(View.VISIBLE);
            return mTabContent;
        }

        public void tabClosed() {
            mTabContent.setVisibility(View.GONE);
        }
    }

    /**
     * How tab content is managed via an {@link Intent}: the content view is the
     * decorview of the launched activity.
     */
    private class IntentContentStrategy implements ContentStrategy {

        private final String mTag;
        private final Intent mIntent;

        private View mLaunchedView;

        private IntentContentStrategy(String tag, Intent intent) {
            mTag = tag;
            mIntent = intent;
        }

        public View getContentView() {
            if (mLocalActivityManager == null) {
                throw new IllegalStateException("Did you forget to call 'public void setup(LocalActivityManager activityGroup)'?");
            }
            final Window w = mLocalActivityManager.startActivity(
                    mTag, mIntent);
            final View wd = w != null ? w.getDecorView() : null;
            if (mLaunchedView != wd && mLaunchedView != null) {
                if (mLaunchedView.getParent() != null) {
                    mTabContent.removeView(mLaunchedView);
                }
            }
            mLaunchedView = wd;

            // XXX Set FOCUS_AFTER_DESCENDANTS on embedded activities for now so they can get
            // focus if none of their children have it. They need focus to be able to
            // display menu items.
            //
            // Replace this with something better when Bug 628886 is fixed...
            //
            if (mLaunchedView != null) {
                mLaunchedView.setVisibility(View.VISIBLE);
                mLaunchedView.setFocusableInTouchMode(true);
                ((ViewGroup) mLaunchedView).setDescendantFocusability(
                        FOCUS_AFTER_DESCENDANTS);
            }
            return mLaunchedView;
        }

        public void tabClosed() {
            if (mLaunchedView != null) {
                mLaunchedView.setVisibility(View.GONE);
            }
        }
    }
    
    /**
     * Makes the content of a tab when it is selected. Use this if your tab
     * content needs to be created on demand, i.e. you are not showing an
     * existing view or starting an activity.
     */
    public interface TabContentFactory {
        /**
         * Callback to make the tab contents
         *
         * @param tag
         *            Which tab was selected.
         * @return The view to display the contents of the selected tab.
         */
        View createTabContent(String tag);
    }
    
    /**
     * Interface definition for a callback to be invoked when tab changed
     */
    public interface OnTabChangeListener {
        void onTabChanged(String tabId);
    }

}
