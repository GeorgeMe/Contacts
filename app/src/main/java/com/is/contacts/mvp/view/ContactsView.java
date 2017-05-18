package com.is.contacts.mvp.view;

import com.is.contacts.protocol.ContactsResponse;

/**
 * Created by MVPHelper on 2016/10/13
 */

public interface ContactsView extends BaseView {
    void showData(ContactsResponse contacts);
    void showNo();
    void exit();
}