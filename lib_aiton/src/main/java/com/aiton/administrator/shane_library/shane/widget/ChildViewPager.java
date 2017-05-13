package com.aiton.administrator.shane_library.shane.widget;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ChildViewPager extends ViewPager {
	/** 触摸时按下的点 **/
	PointF downP = new PointF();
	/** 触摸时当前的点 **/
	PointF curP = new PointF();
	private OnSingleTouchListener listener;

	public ChildViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ChildViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// 当拦截触摸事件到达此位置的时候，返回true，
		// 说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 每次进行onTouch事件都记录当前的按下的坐标
		curP.x = event.getX();
		curP.y = event.getY();

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			downP.x = event.getX();
			downP.y = event.getY();
			// 通知父容器当前触摸事件是由当前控件处理
			getParent().requestDisallowInterceptTouchEvent(true);
		}

		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			// 通知父容器当前触摸事件是由当前控件处理
			getParent().requestDisallowInterceptTouchEvent(true);
		}

		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (downP.x == curP.x && downP.y == curP.y) {
				// 说明只是单击了一下
				int position = getCurrentItem();
				onSingleTouch(position);
			}
		}
		return super.onTouchEvent(event);
	}
	
	private void onSingleTouch(int position) {
		if(listener != null)
		{
			listener.onSingleTouch(position);
		}
	}

	public interface OnSingleTouchListener
	{
		void onSingleTouch (int position);
	}
	
	public void setOnSingleTouchListener(OnSingleTouchListener listener)
	{
		this.listener = listener;
		
	}
}
