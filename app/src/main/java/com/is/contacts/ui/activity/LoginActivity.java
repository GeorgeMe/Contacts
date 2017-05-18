package com.is.contacts.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.is.contacts.R;
import com.is.contacts.base.BaseActivity;
import com.is.contacts.mvp.presenter.LoginPresenterImpl;
import com.is.contacts.mvp.view.LoginView;
import com.is.contacts.protocol.LoginResponse;
import com.is.ui.eventbus.EventCenter;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements LoginView {

    @Bind(R.id.userName)
    AutoCompleteTextView userName;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.user_login_form)
    LinearLayout user_login_form;
    @Bind(R.id.login_form)
    ScrollView loginForm;

    LoginPresenterImpl loginPresenter;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }

    @OnClick(R.id.btn_login)
    public void OnClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                if( getUserName() == null||getPassword()==null){
                    showToast("不能为空");
                }else{
                    login(getUserName(), getPassword());
                }
                break;
        }

    }

    @Override
    protected void initViewsAndEvents() {
        userName.clearFocus();
        password.clearFocus();
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return false;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }

    @Override
    public void login(String userName, String password) {
        loginPresenter = new LoginPresenterImpl(mContext, this);
        loginPresenter.getLogin(userName, password);
    }

    @Override
    public String getUserName() {
        return userName.getText().toString();
    }

    @Override
    public String getPassword() {
        return password.getText().toString();
    }

    @Override
    public void toMainActivity(LoginResponse loginResponse) {
        Bundle bundle = new Bundle();
        bundle.putInt("userId",loginResponse.getData().getId());
        readyGoThenKill(MainActivity.class,bundle);
    }
}

