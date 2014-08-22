package com.cncom.app.base.module;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cncom.app.base.account.MyAccountManager;
import com.cncom.app.base.module.ModuleSettings.Module;
import com.cncom.app.base.module.ModuleViewFactory.ModuleView;
import com.lnwoowken.lnwoowkenbook.BillListManager;
import com.lnwoowken.lnwoowkenbook.MyApplication;
import com.lnwoowken.lnwoowkenbook.R;
/**
 * 单模块类型, 其视图样式为
 * <pre>
 * ################################
 * ######## ICON   label ##########
 * ################################
 * </pre>
 * @author chenkai
 */
public class SingleModuleViewWithIconAndLabel extends ModuleView{
	private View mModule1View;
	protected Module mModule1Info;
	protected View.OnClickListener mOnClickListener;
	
	public SingleModuleViewWithIconAndLabel(Module module1Info, View.OnClickListener onClickListener) {
		mModule1Info = module1Info;
		mOnClickListener = onClickListener;
	}
	
	public SingleModuleViewWithIconAndLabel(Module module1Info) {
		mModule1Info = module1Info;
	}

	
	@Override
	public void initView(View loduleLayout) {
		mModule1View = loduleLayout;
		mModule1View.setId(mModule1Info.mId);
		
		ImageView mModule1IconView = (ImageView) loduleLayout.findViewById(R.id.icon);
		
		mModule1IconView.setImageResource(mModule1Info.mIconResId);
		TextView mModule1TextView = (TextView) loduleLayout.findViewById(R.id.title);
//		mModule1TextView.getPaint().setFakeBoldText(true);
		mModule1TextView.setText(mModule1Info.mLabelResId);
		
		if(mModule1Info.mIconResId == R.drawable.menu_my_order) {
			if(MyAccountManager.getInstance().hasLoginned() && BillListManager.isExsited(MyApplication.getInstance().getContentResolver())) {
				ImageView dotIconView = (ImageView) loduleLayout.findViewById(R.id.imageView_myBill_tips);
				
				dotIconView.setImageResource(R.drawable.dot_tips);
				dotIconView.setVisibility(View.VISIBLE);
			}
		}
		if (mOnClickListener != null) {
			mModule1View.setOnClickListener(mOnClickListener);
		}
	}
	
	@Override
	protected void setOnClickListener(View.OnClickListener onClickListener) {
		super.setOnClickListener(onClickListener);
		mModule1View.setOnClickListener(onClickListener);
	}

	@Override
	public int getLayoutResId() {
		return R.layout.module_icon_and_title_layout;
	}

}
