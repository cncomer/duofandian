﻿package com.lnwoowken.lnwoowkenbook.model;

public class Margins {
	private int top;
	private int bottom;
	private int left;
	private int right;
	public Margins(int left,int top,int right,int bottom){
		this.top = top;
		this.left = left;
		this.right = right;
		this.bottom = bottom;
	}
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public int getBottom() {
		return bottom;
	}
	public void setBottom(int bottom) {
		this.bottom = bottom;
	}
	public int getLeft() {
		return left;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public int getRight() {
		return right;
	}
	public void setRight(int right) {
		this.right = right;
	}

}
