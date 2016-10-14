package com.is.contacts.util;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.is.contacts.R;

/**
 * Created by Administrator on 2016/10/13 0013.
 */

public class ProgressDialog {
    public Dialog mDialog;
    public ProgressDialog(Context context,String message){
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.progress_view,null);
        TextView text = (TextView) view.findViewById(R.id.progress_message);
        text.setText(message);
        mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);
    }

    public void show() {
        mDialog.show();
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        mDialog.setCanceledOnTouchOutside(cancel);

    }

    public void dismiss() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
