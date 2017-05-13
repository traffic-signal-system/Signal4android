package com.aiton.administrator.shane_library.shane.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.aiton.administrator.shane_library.R;

public class AdDialog extends Dialog {

    private Context context;
    private ClickListenerInterface clickListenerInterface;
    private int imgID;

    public interface ClickListenerInterface {

        void doWhat();
        void cancle();

    }

    public AdDialog(Context context, int imgID) {
        super(context, R.style.dialog);
        this.context = context;
        this.imgID = imgID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ad_dialog, null);
        setContentView(view);
        ImageView imageView_ad = (ImageView) view.findViewById(R.id.imageView_ad);
        imageView_ad.setImageResource(imgID);
        imageView_ad.setOnClickListener(new clickListener());
        view.findViewById(R.id.imageView_cancle).setOnClickListener(new clickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); //获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); //宽度设置为屏幕的0.8
        lp.height = (int) (d.heightPixels * 0.8);//高度设置为屏幕的0.8
        dialogWindow.setAttributes(lp);
    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int id = v.getId();
            if (id == R.id.imageView_ad) {
                clickListenerInterface.doWhat();
            }else if (id== R.id.imageView_cancle){
                clickListenerInterface.cancle();
            }
        }
    }
}