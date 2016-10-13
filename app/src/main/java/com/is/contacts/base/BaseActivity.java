package com.is.contacts.base;

import android.support.v7.app.AppCompatActivity;

import com.is.common.StringUtils;
import com.is.contacts.ProgressDialog;
import com.is.contacts.R;

/**
 * Created by Administrator on 2016/10/13 0013.
 */

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;

    public void showLoading(String msg) {
        if (progressDialog == null) {
            if (StringUtils.StringIsEmpty(msg)) {
                progressDialog = new ProgressDialog(this, getString(R.string.loading));
                progressDialog.show();
            } else {
                progressDialog = new ProgressDialog(this, msg);
                progressDialog.show();
            }
        } else {
            progressDialog.show();
        }
    }

    public void hideLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}
