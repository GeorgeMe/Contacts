package com.is.contacts.mvp.presenter;

import android.content.Context;

import com.is.contacts.mvp.interactor.LoginInteractorImpl;
import com.is.contacts.mvp.listener.BaseSingleLoadedListener;
import com.is.contacts.mvp.view.LoginView;
import com.is.contacts.protocol.LoginResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/10/14 0014.
 */

public class LoginPresenterImpl implements BaseSingleLoadedListener<LoginResponse> {
    Context mContext = null;
    LoginInteractorImpl loginInteractor;
    LoginView loginView;

    public LoginPresenterImpl(Context mContext, LoginView loginView) {
        this.mContext = mContext;
        this.loginView = loginView;
        this.loginInteractor = new LoginInteractorImpl(mContext, this);
    }

    public void getLogin(String userNanme, String password) {
        loginView.showLoading("登录中...");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", userNanme);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        loginInteractor.getCommonSingleData(jsonObject);
    }

    @Override
    public void onSuccess(LoginResponse data) {
        loginView.login(data);
        loginView.hideLoading();
    }

    @Override
    public void onFailure(String msg) {
        loginView.showToast(msg);
        loginView.hideLoading();
    }
}
