package com.is.contacts.base;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.is.common.StringUtils;
import com.is.contacts.R;
import com.is.contacts.mvp.view.BaseView;
import com.is.ui.base.BaseAppCompatActivity;
import com.is.ui.netstatus.NetUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/13 0013.
 */

public abstract class BaseActivity extends BaseAppCompatActivity implements BaseView {
    protected Toolbar mToolbar;
    private ProgressDialog progressDialog;
    private Map<Integer, Runnable> allowablePermissionRunnables = new HashMap<>();
    private Map<Integer, Runnable> disallowablePermissionRunnables = new HashMap<>();

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mToolbar = ButterKnife.findById(this, R.id.common_toolbar);
        if (null != mToolbar) {

            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {
        if (type.name().equals("WIFI")) {

        } else if (type.name().equals("CMNET")) {
        } else if (type.name().equals("CMWAP")) {
        } else if (type.name().equals("NONE")) {

        }
        showToast("正在连接" + type.name() + "网络");
    }

    @Override
    protected void onNetworkDisConnected() {
        showToast("网络连接断开");
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void showException(String msg) {

    }

    @Override
    public void showLoading(String msg) {
        if (progressDialog == null) {
            if (StringUtils.StringIsEmpty(msg)) {
                progressDialog = new ProgressDialog(mContext);
                progressDialog.setTitle(msg);
                progressDialog.show();
            } else {
                progressDialog = new ProgressDialog(mContext);
                progressDialog.setTitle(msg);
                progressDialog.show();
            }
        } else {
            progressDialog.setTitle(msg);
            progressDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 动态请求权限
     *
     * @param id 请求授权的id 唯一标识即可
     * @param permission 请求的权限
     * @param allowableRunnable 同意授权
     * @param disallowableRunnable 禁止授权
     */
    protected void requestPermission(int id, String permission, Runnable allowableRunnable, Runnable disallowableRunnable) {
        if (allowableRunnable == null) {
            throw new IllegalArgumentException("allowableRunnable == null");
        }

        allowablePermissionRunnables.put(id, allowableRunnable);
        if (disallowableRunnable != null) {
            disallowablePermissionRunnables.put(id, disallowableRunnable);
        }

        //版本判断
        if (Build.VERSION.SDK_INT >= 23) {
            //减少是否拥有权限
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                //弹出对话框接收权限
                ActivityCompat.requestPermissions(BaseActivity.this, new String[]{permission}, id);
                return;
            } else {
                allowableRunnable.run();
            }
        } else {
            allowableRunnable.run();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Runnable allowRun = allowablePermissionRunnables.get(requestCode);
            allowRun.run();
        } else {
            Runnable disallowRun = disallowablePermissionRunnables.get(requestCode);
            disallowRun.run();
        }
    }
}
