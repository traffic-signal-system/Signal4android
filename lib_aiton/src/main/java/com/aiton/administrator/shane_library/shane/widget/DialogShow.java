package com.aiton.administrator.shane_library.shane.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.aiton.administrator.shane_library.R;


/**
 * Created by zjb on 2016/3/3.
 */
public class DialogShow {

    public static boolean isShow = true;

    //一个按钮的dialog提示
    public static void setDialog(Context context, String messageTxt, String iSeeTxt) {
        if (isShow) {
            final AlertDialog dialog;
            LayoutInflater localinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View commit_dialog = localinflater.inflate(R.layout.commit_dialog, null);
            TextView message = (TextView) commit_dialog.findViewById(R.id.message);
            Button ISee = (Button) commit_dialog.findViewById(R.id.ISee);
            message.setText(messageTxt);
            ISee.setText(iSeeTxt);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            dialog = builder.setView(commit_dialog)
                    .create();
            dialog.setCancelable(false);
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog.show();
            isShow = false;
            commit_dialog.findViewById(R.id.ISee).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    isShow = true;
                }
            });
        }
    }
}
