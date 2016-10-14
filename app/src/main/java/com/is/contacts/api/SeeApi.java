package com.is.contacts.api;

import com.is.contacts.protocol.ContactsResponse;
import com.is.contacts.protocol.LoginResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/10/14 0014.
 */

public interface SeeApi {
    @GET("contacts")
    Call<ContactsResponse> getContactsList();

    @POST("login")
    Call<LoginResponse> login(@Query("userName") String userName, @Query("password") String password);
}
