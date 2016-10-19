package com.is.contacts.mvp.interactor;

import android.content.Context;
import android.util.Log;

import com.is.contacts.base.Retrofit2See;
import com.is.contacts.mvp.listener.BaseSingleLoadedListener;
import com.is.contacts.mvp.listener.CommonSingleInteractor;
import com.is.contacts.protocol.LoginResponse;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/10/14 0014.
 */

public class LoginInteractorImpl extends Retrofit2See implements CommonSingleInteractor {
    BaseSingleLoadedListener loadedListener;

    public LoginInteractorImpl(Context context, BaseSingleLoadedListener loadedListener) {
        super(context);
        this.loadedListener = loadedListener;
    }

    @Override
    public void getCommonSingleData(JSONObject json) {
        Call<LoginResponse> call = seeApi.login(json.optString("userName"), json.optString("password"));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.i("single", response.code() + "");
                Log.i("single", response.code() + response.body().getData());
                if (response.body() != null) {
                    loadedListener.onSuccess(response.body());
                } else {
                    loadedListener.onFailure("出错啦");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loadedListener.onFailure("出错啦");
            }
        });
    }
}
