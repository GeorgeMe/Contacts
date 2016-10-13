package com.is.contacts.presenter;

import com.is.contacts.Contacts;

import java.util.List;

/**
 * Created by Administrator on 2016/10/13 0013.
 */

public interface ContactsPresenter {
    void loadDataSuccess(List<Contacts> contacts);

    void loadDataFailure();
}
