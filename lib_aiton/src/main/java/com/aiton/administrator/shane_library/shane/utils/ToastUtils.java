package com.aiton.administrator.shane_library.shane.utils;

import android.content.Context;
import android.widget.Toast;


public final class ToastUtils {

	private static Toast toast;
	public  static void showToast(Context context, String text) {
//		Toast toast = Toast.makeText(this, "toast", Toast.LENGTH_LONG);
//		if (toast != null){
//			toast.cancel();
//		}
//		toast = new Toast(context);
//		View view = LayoutInflater.from(context).inflate(R.layout.custom_toast, null);
//		TextView tvText = (TextView) view.findViewById(R.id.textView1);
//		tvText.setText(text);
//		// toast.setBackground();
//		toast.setView(view);
//		toast.setGravity(Gravity.TOP, 0, 60);
//		toast.setDuration(Toast.LENGTH_LONG);
		if (toast == null) {
			toast = Toast.makeText(context,
					text,
					Toast.LENGTH_SHORT);
		} else {
			toast.setText(text);
		}
		toast.show();
	}
	
	private ToastUtils(){}
}
