package com.is.contacts.mvp.interactor;


import android.content.Context;
import android.util.Log;

import com.is.contacts.base.Retrofit2See;
import com.is.contacts.mvp.listener.BaseSingleLoadedListener;
import com.is.contacts.mvp.listener.CommonListInteractor;
import com.is.contacts.protocol.ContactsResponse;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MVPHelper on 2016/10/13
 */

public class ContactsInteractorImpl extends Retrofit2See implements CommonListInteractor {
    BaseSingleLoadedListener<ContactsResponse> loadedListener;

    public ContactsInteractorImpl(Context context, BaseSingleLoadedListener<ContactsResponse> loadedListener) {
        super(context);
        this.loadedListener = loadedListener;
    }

    @Override
    public void getCommonListData(JSONObject json) {
        Call<ContactsResponse> call = seeApi.getContactsList(json.toString());

        call.enqueue(new Callback<ContactsResponse>() {
            @Override
            public void onResponse(Call<ContactsResponse> call, Response<ContactsResponse> response) {

                if (response.body() != null) {
                    loadedListener.onSuccess(response.body());
                } else
                    loadedListener.onFailure("Disconnext");
            }

            @Override
            public void onFailure(Call<ContactsResponse> call, Throwable t) {

                loadedListener.onFailure("Disconnext");
            }
        });
    }
}