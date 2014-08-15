package com.cncom.app.base.module;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncom.app.base.module.ModuleSettings.Module;

public class ModuleViewFactory {

	/**
	 * 创建单模块类型，并将单模块视图填充到parentView中
	 * @param parentView 要填充双模块的父视图
	 * @param module1Info 模块1的模块信息
	 * @return
	 */
	public static ModuleView createSingleModuleView(ViewGroup parentView, Module module1Info) {
		return createSingleModuleViewWithIconAndLabel(parentView, module1Info);
	}
	
	
	/**
	 * 创建单模块类型，并将单模块视图填充到parentView中
	 * @param parentView 要填充双模块的父视图
	 * @param module1Info 模块1的模块信息
	 * @return
	 */
	public static ModuleView createSingleModuleViewWithIconAndLabel(ViewGroup parentView, Module module1Info) {
		SingleModuleViewWithIconAndLabel moduleView = new SingleModuleViewWithIconAndLabel(module1Info);
		moduleView.createView(parentView, true);
		return moduleView;
	}
	
	/**
	 * 单、双类型模块的父类，当新增加一个类型模块时，需要继承该类，并由{@link #ModuleViewFactory}创建。
	 * @author chenkai
	 *
	 */
	public static abstract class ModuleView {
		protected ViewGroup mParent;
		protected View.OnClickListener mOnClickListener;
		protected boolean isCallSuperCreateView;
		
		protected View createView(ViewGroup parentView, boolean attachToRoot) {
			mParent = parentView;
			isCallSuperCreateView = true;
			
			return createView(mParent, LayoutInflater.from(parentView.getContext()), attachToRoot);
		}
		protected View createView(ViewGroup parentView, LayoutInflater inflater, boolean attachToRoot) {
			View view =  inflater.inflate(getLayoutResId(), parentView, false);
			initView(view);
			parentView.addView(view);
			return view;
		}
		
		public abstract int getLayoutResId();
		
		public abstract void initView(View loduleLayout);
		
		protected View getParent() {
			return mParent;
		}
		protected void setOnClickListener(View.OnClickListener onClickListener) {
			mOnClickListener = onClickListener;
		}
		
	}
}
