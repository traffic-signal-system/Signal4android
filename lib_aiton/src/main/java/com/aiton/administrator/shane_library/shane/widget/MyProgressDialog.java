package com.aiton.administrator.shane_library.shane.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aiton.administrator.shane_library.R;

/**
 * Created by Administrator on 2016/8/11.
 */
public class MyProgressDialog extends ProgressDialog {
    private Context context;
    private String msg;
    public MyProgressDialog(Context context, String msg) {
        super(context, R.style.dialog);
        this.context = context;
        this.msg = msg;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        init();
    }
    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_progress_dialog, null);
        setContentView(view);

//        ImageView imageView_rotate = (ImageView) view.findViewById(R.id.imageView_rotate);
        TextView textView_msg = (TextView) view.findViewById(R.id.textView_msg);
//        Animation animation = AnimationUtils.loadAnimation(context, R.anim.scan_anim);
//        LinearInterpolator lin = new LinearInterpolator();
//        animation.setInterpolator(lin);
//        if (animation != null) {
//            imageView_rotate.startAnimation(animation);
//        }
        textView_msg.setText(msg);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }
}
