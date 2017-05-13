package com.aiton.administrator.shane_library.shane.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class LetterSideBar extends View {
	private static final int LETTER_LENGTH = 26;
	private Paint paint;
	private boolean isPressed;
	private int index = -1;
	private OnLetterChangedListener listener;

	public interface OnLetterChangedListener {
		void onLetterChanged (String letter);
		void onTouchAction (int action);
	}
	public void setOnLetterChangedListener(OnLetterChangedListener l) {
		this.listener = l;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (isPressed) {
			canvas.drawColor(Color.parseColor("#80000000"));
		}
		int height = getHeight();// 获得当前控件的高度
		int width = getWidth();
		int textHeight = height / LETTER_LENGTH;
		char ch = 'A';
		for (int i = 0; i < LETTER_LENGTH; i++) {
			if (index == i) {
				paint.setColor(Color.RED);
			} else {
				paint.setColor(Color.BLACK);
			}
			// 当前选中的字母颜色不同
			// 计算x轴，使字母居中显示
			String letter = "" + (char) (ch + i);
			float letterWidth = paint.measureText(letter);
			int x = (int) (width / 2 - letterWidth / 2);
			canvas.drawText(letter, x, textHeight * (i + 1), paint);
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float y = event.getY();// 获得按下的Y轴坐标
		index = (int) (y * LETTER_LENGTH / getHeight());
		String currLetter = "" + (char) ('A' + index);
		int action = event.getAction();
		if (listener != null){
			listener.onTouchAction(action);
		}
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			// Log.e("onTouchEvent", "按下:" + (char) ('A' + index));
			// isPressed = true;
			// break;
		case MotionEvent.ACTION_MOVE:
			if (listener != null){
				listener.onLetterChanged(currLetter);
			}
			
			Log.e("onTouchEvent", "移动:" + currLetter);
			isPressed = true;
			break;
		case MotionEvent.ACTION_UP:
			Log.e("onTouchEvent", "抬起");
			isPressed = false;
			index = -1;
			break;

		default:
			break;
		}
		invalidate();
		return true;
	}

	public LetterSideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setTextSize(26);
		paint.setColor(Color.BLACK);
	}

}
