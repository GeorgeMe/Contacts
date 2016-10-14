package com.is.contacts.base;

import android.widget.Toast;

import com.is.contacts.mvp.listener.OnCommonPageSelectedListener;
import com.is.contacts.mvp.view.BaseView;
import com.is.ui.base.BaseLazyFragment;

/**
 *
 */
public abstract class BaseFragment extends BaseLazyFragment implements BaseView, OnCommonPageSelectedListener {

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
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }
}
