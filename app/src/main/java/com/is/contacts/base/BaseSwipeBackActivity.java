package com.is.contacts.base;

import android.support.v7.widget.Toolbar;
import com.is.contacts.R;
import com.is.contacts.mvp.view.BaseView;
import com.is.ui.base.BaseSwipeBackCompatActivity;
import com.is.ui.netstatus.NetUtils;

import butterknife.ButterKnife;

/**
 *
 */
public abstract class BaseSwipeBackActivity extends BaseSwipeBackCompatActivity implements BaseView {

    protected Toolbar mToolbar;

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
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showToast(String msg) {

    }
}
