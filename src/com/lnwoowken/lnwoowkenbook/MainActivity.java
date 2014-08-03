package com.lnwoowken.lnwoowkenbook;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends Activity implements OnClickListener,
OnPageChangeListener {
	private  SharedPreferences preferences;
	// ҳ������
	private ViewPager viewPager;

	private ViewPagerAdapter viewPagerAdapter;
	// ҳ���б�
	private List<View> views;

	private ImageView[] dots;

	private int currentIndex;
	
	private int lastX = 0;

	private static final int[] pics = {R.drawable.start_1,	R.drawable.start_2};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (checkFirst()) {
			viewPager = (ViewPager) findViewById(R.id.viewpager);
			views = new ArrayList<View>();
			
	/*		// ��ʼ������ͼƬ�б�
			LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.FILL_PARENT);

			for (int i = 0; i < pics.length; i++) {
				ImageView iv = new ImageView(this);
				iv.setLayoutParams(mParams);
				iv.setImageResource(pics[i]);
				views.add(iv);
			}*/
			
			// ��ʼ������ͼƬ�б�
			LayoutInflater inflater = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view1 = inflater.inflate(R.layout.viewpager1, null);
			View view2 = inflater.inflate(R.layout.viewpager2, null);
			views.add(view1);
			views.add(view2);
			
			// ��ʼ��Adapter
			viewPagerAdapter = new ViewPagerAdapter(views);
			//viewPager.setOnTouchListener(this);
			viewPager.setAdapter(viewPagerAdapter);
			// ����һ������������ViewPager�е�ҳ��ı�ʱ����
			viewPager.setOnPageChangeListener(this);

			initBottomDots();
		}
		else {
			Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
			startActivity(intent);
			MainActivity.this.finish();
		}
		
		
		

	}

	private void initBottomDots() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.dot);
		dots = new ImageView[pics.length];
		for (int i = 0; i < pics.length; i++) {
			dots[i] = (ImageView) ll.getChildAt(i);
			dots[i].setEnabled(true);
			dots[i].setOnClickListener(this);
			dots[i].setTag(i);
		}
		currentIndex = 0;
		dots[currentIndex].setEnabled(false);
	}

	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		setCurView(position);
		setCurDot(position);
	}



//	/**
//	 * ����������
//	 */
//	public void GoToMainActivity() {
//		Intent i = new Intent(MainActivity.this, FristActivity.class);
//		startActivity(i);
//		this.finish();
//	}
	
	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@Override
	public void onPageScrolled(int index, float arg1, int dis) {
	}

	@Override
	public void onPageSelected(int index) {
		setCurDot(index);
	}

	private void setCurDot(int positon) {
		if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
			return;
		}
		dots[positon].setEnabled(false);
		dots[currentIndex].setEnabled(true);
		currentIndex = positon;
		final Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
		new Thread(){
			public void run() {
				try {
					
					Thread.sleep(1000);
					startActivity(intent);
					finish();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
			
		}.start();
		

//		GoToMainActivity();
	}

	private void setCurView(int position) {
		if (position < 0 || position > pics.length) {
			return;
		}
		viewPager.setCurrentItem(position);
	}

//	@Override
//	public boolean onTouch(View v, MotionEvent event) {
//		switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			lastX = (int)event.getX();
//			break;
//		case MotionEvent.ACTION_MOVE:
//			if((lastX - event.getX()) >100 && (currentIndex == views.size() -1)){
//				GoToMainActivity();
//				this.finish();
//			}
//			break;
//		default:
//			break;
//		}
//		return false;
//	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		finish();
	}


	private boolean checkFirst(){
		//��ȡSharedPreferences����Ҫ������
        preferences = getSharedPreferences("count",MODE_WORLD_READABLE);
        int count = preferences.getInt("count", 0);
        boolean b = false;
        //�жϳ�����ڼ������У�����ǵ�һ����������ת������ҳ��
        if (count == 0) {
            b = true;
        }
        
        Editor editor = preferences.edit();
        //��������
        editor.putInt("count", ++count);
        //�ύ�޸�
        editor.commit();
        return b;
	}

}
