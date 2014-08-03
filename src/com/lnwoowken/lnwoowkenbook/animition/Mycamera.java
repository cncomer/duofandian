package com.lnwoowken.lnwoowkenbook.animition;


import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

public class Mycamera extends Animation {
	int centerX, centerY;
	boolean isZoomin;
	Camera camera = new Camera();
	public Mycamera(boolean isZoomin) {
		this.isZoomin = isZoomin;
	}
	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		
		centerX = width / 2;
		centerY = height / 2;
		setDuration(400); 
		setFillAfter(true); 
		setInterpolator(new DecelerateInterpolator()); 
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		final Matrix matrix = t.getMatrix();
		int direction = -1;
		camera.save(); 
		if (!isZoomin) {
			interpolatedTime = 1 - interpolatedTime;
			direction = 1;
		}
		
		camera.translate(0, 0, 0); //x,y,z轴的偏移量
		camera.rotateY(360 * interpolatedTime * direction); //中心是绕Y轴旋转
		camera.getMatrix(matrix);//把我们的摄像头加在变换矩阵上
		matrix.preTranslate(-centerX, -centerY); 
		matrix.postTranslate(centerX, centerY);
		camera.restore(); 
	}

}
