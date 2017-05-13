package com.aiton.administrator.shane_library.shane.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ListView4ScrollView extends ListView {
	public ListView4ScrollView(Context context) {
		super(context);
	}

	public ListView4ScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListView4ScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	/**
	 * 重写该方法，达到使ListView适应ScrollView的效果
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	
	
	
	
	
	
	
	
	
	
	
}
