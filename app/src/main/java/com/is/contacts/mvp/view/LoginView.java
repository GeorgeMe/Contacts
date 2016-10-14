package com.is.contacts.mvp.view;

import com.is.contacts.protocol.LoginResponse;

/**
 * Created by Administrator on 2016/10/14 0014.
 */

public interface LoginView extends BaseView {
    void login(LoginResponse loginResponse);
}
