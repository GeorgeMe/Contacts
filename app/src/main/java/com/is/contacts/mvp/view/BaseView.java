package com.is.contacts.mvp.view;

/**
 * Created by Administrator on 2016/10/14 0014.
 */

public interface BaseView {
    /**
     * show loading message
     *
     * @param msg
     */
    void showLoading(String msg);

    /**
     * hide loading
     */
    void hideLoading();

    /**
     * show error message
     */
    public void showError(String msg);

    /**
     * show exception message
     */
    void showException(String msg);

    /**
     * show toast message
     */
    void showToast(String msg);
}
