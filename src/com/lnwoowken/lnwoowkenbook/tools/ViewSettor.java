package com.lnwoowken.lnwoowkenbook.tools;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.lnwoowken.lnwoowkenbook.lnwoowkeninterface.ViewInterface;
import com.lnwoowken.lnwoowkenbook.model.Contant;
import com.lnwoowken.lnwoowkenbook.model.Location;
import com.lnwoowken.lnwoowkenbook.model.Margins;
import com.lnwoowken.lnwoowkenbook.model.Paddings;
import com.lnwoowken.lnwoowkenbook.model.Screen;
import com.lnwoowken.lnwoowkenbook.model.Size;

public class ViewSettor implements ViewInterface {
	public static void setLayoutParams(){
		
	}
	
	public static void setViewInRelativeLayout(Paddings padding,Size size,Margins margin,Location location,View view){
		RelativeLayout.LayoutParams l1 = new RelativeLayout.LayoutParams(
				size.getWidth(), size.getHeight());
		l1.setMargins(margin.getLeft(), margin.getTop(), margin.getRight(), margin.getBottom());
		if (location.getBelow()!=0) {
			l1.addRule(RelativeLayout.BELOW,location.getBelow() );
		}
		if (location.getBelow()!=0) {
			l1.addRule(RelativeLayout.ABOVE,location.getAbove() );
		}
		if (location.getBelow()!=0) {
			l1.addRule(RelativeLayout.LEFT_OF,location.getToTheLeftOf() );
		}
		if (location.getBelow()!=0) {
			l1.addRule(RelativeLayout.RIGHT_OF,location.getToTheRightOf() );
		}
		
		
		view.setLayoutParams(l1);
		view.setPadding(padding.getLeft(), padding.getTop(), padding.getRight(), padding.getBottom());
	}
	
	public static void setViewInLinearLayout(Paddings padding,Size size,Margins margin,View view){
		LinearLayout.LayoutParams l1 = new LinearLayout.LayoutParams(
				size.getWidth(), size.getHeight());
		l1.setMargins(margin.getLeft(), margin.getTop(), margin.getRight(), margin.getBottom());
		if (size.getWeight()!=0) {
			l1.weight = size.getWeight();
		}
		view.setLayoutParams(l1);
		view.setPadding(padding.getLeft(), padding.getTop(), padding.getRight(), padding.getBottom());
		
	}
	
	
	
	
	public static void setBottom(View view,Paddings padding,Size size){
		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(size.getWidth(), size.getHeight());  
	    lp2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM); 
	    
	    view.setLayoutParams(lp2);
	    view.setPadding(padding.getLeft(), padding.getTop(), padding.getRight(), padding.getBottom());
		
	}
//	public static void setTop(View view,Paddings padding,Size size){
//		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(size.getWidth(), size.getHeight());  
//	    lp2.addRule(RelativeLayout.ALIGN_PARENT_TOP); 
//	    
//	    view.setLayoutParams(lp2);
//	    view.setPadding(padding.getLeft(), padding.getTop(), padding.getRight(), padding.getBottom());
//		
//	}
	
	@SuppressWarnings("deprecation")
	public static Screen getScreen(Activity activity){
		Screen screen = new Screen();
		int screenWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
		int screenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
		screen.setHeight(screenHeight);
		screen.setWidth(screenWidth);
		return screen;
		
	}

	@Override
	public void setTop(View view, Paddings padding, Size size) {
		// TODO Auto-generated method stub
		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(size.getWidth(), size.getHeight());  
	    lp2.addRule(RelativeLayout.ALIGN_PARENT_TOP); 
	    
	    view.setLayoutParams(lp2);
	    view.setPadding(padding.getLeft(), padding.getTop(), padding.getRight(), padding.getBottom());
	}

}
