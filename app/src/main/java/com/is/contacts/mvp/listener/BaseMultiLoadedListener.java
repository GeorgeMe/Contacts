package com.is.contacts.mvp.listener;

/**
 * Created by George on 2016/5/21.
 */

/**
 * @param <T>
 */
public interface BaseMultiLoadedListener<T> {

    /**
     * when data call back success
     *
     * @param event_tag
     * @param data
     */
    void onSuccess(int event_tag, T data);

    /**
     * when data call back error and when data call back occurred exception
     *
     * @param msg
     */
    void onFailure(String msg);

}