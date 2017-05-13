package com.aiton.administrator.shane_library.shane.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 解决ScrollView嵌套GridView时，GridView只显示一行的问题
 * @author Administrator
 *
 */
public class GridView4ScrollView extends GridView {
	public GridView4ScrollView(Context context) {
		super(context);
	}

	public GridView4ScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GridView4ScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	/**
	 * ��д�÷������ﵽʹListView��ӦScrollView��Ч��
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(
			Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	
	
	
	
	
	
	
	
	
	
	
}
