package com.is.contacts.base;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import com.is.common.TLog;
import com.is.common.XmlDB;
import com.is.contacts.SeeConstant;
import com.is.contacts.api.SeeApi;
import okhttp3.*;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2016/10/14 0014.
 */

public class Retrofit2See {
    protected String baseUrl = "http://www.1drivers.net/";
    //protected String baseUrl = "http://192.168.0.116:8080/driver-admin/";
    protected Retrofit retrofit;
    protected SeeApi seeApi;
    private static Context mContext = null;

    public Retrofit2See(Context context) {
        mContext = context;
        if (retrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            /**
             * 设置Cookie
             */
            //builder.addInterceptor(addCookieInterceptor());
            /**
             * 设置头
             */
            builder.addInterceptor(addHeaderInterceptor());
            /**
             * 设置公共参数
             */
            builder.addInterceptor(addQueryParameterInterceptor());

            /**
             * 设置缓存
             */
            File cacheFile = new File(mContext.getExternalCacheDir(), "RetrofitCache");
            Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
            builder.cache(cache).addInterceptor(addCacheInterceptor());

            /**
             * 设置超时
             */
            /*builder.connectTimeout(5, TimeUnit.SECONDS);
            builder.readTimeout(20, TimeUnit.SECONDS);
            builder.writeTimeout(20, TimeUnit.SECONDS);*/
            /**
             * 错误重连
             */
            builder.retryOnConnectionFailure(true);
            /**
             * okhttp3
             */
            OkHttpClient client = builder.build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        seeApi = retrofit.create(SeeApi.class);

    }

    /**
     * 设置Cookie
     * 导致请求执行了两次
     * 2017-5-18发现
     */
    private static Interceptor addCookieInterceptor() {
        Interceptor cookieInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                if (!TextUtils.isEmpty(XmlDB.getInstance(mContext).getKeyString(SeeConstant.SEE_COOKIE, "")) && !original.url().toString().contains("loginUsernameEmail")) {
                    Request request = original.newBuilder()
                            .addHeader("Cookie", "u=" + XmlDB.getInstance(mContext).getKeyString(SeeConstant.SEE_COOKIE, "") + ";") // 不能转UTF-8
                            .build();
                    TLog.e("Cookie", "okhttplog: set header cookie:" + XmlDB.getInstance(mContext).getKeyString(SeeConstant.SEE_COOKIE, ""));
                    TLog.e("Cookie", "okhttplog: set header cookie:" + URLEncoder.encode(XmlDB.getInstance(mContext).getKeyString(SeeConstant.SEE_COOKIE, "")));
                    return chain.proceed(request);
                } else {
                    for (String header : chain.proceed(original).headers("Set-Cookie")) {
                        if (header.startsWith("u=")) {
                            String cookie = header.split(";")[0].substring(2);
                            TLog.e("Cookie", "okhttplog: add cookie:" + cookie);
                            if (!TextUtils.isEmpty(cookie)) {

                                SeeConstant.Cookie = cookie;
                                XmlDB.getInstance(mContext).saveKey(SeeConstant.SEE_COOKIE, cookie);
                            }
                        }
                    }
                }
                return chain.proceed(original);
            }
        };
        return cookieInterceptor;
    }

    /**
     * 设置头
     */
    private static Interceptor addHeaderInterceptor() {
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder()
                        // Provide your custom header here
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .method(originalRequest.method(), originalRequest.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        return headerInterceptor;
    }

    /**
     * 设置公共参数
     */
    private static Interceptor addQueryParameterInterceptor() {
        Interceptor addQueryParameterInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request request;
                HttpUrl modifiedUrl = originalRequest.url().newBuilder()
                        // Provide your custom parameter here
                        .addQueryParameter("platform", "android")
                        .addQueryParameter("version", "1.0.0")
                        .build();
                request = originalRequest.newBuilder().url(modifiedUrl).build();
                return chain.proceed(request);
            }
        };
        return addQueryParameterInterceptor;
    }

    /**
     * 设置缓存
     */
    private static Interceptor addCacheInterceptor() {
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!isNetworkAvailable(mContext)) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if (isNetworkAvailable(mContext)) {
                    int maxAge = 0;
                    // 有网络时 设置缓存超时时间0个小时
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Retrofit")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                            .build();
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("nyn")
                            .build();
                }
                return response;
            }
        };
        return cacheInterceptor;
    }


    /**
     * 判断网络
     */
    public static boolean isNetworkAvailable(Context ct) {
        Context context = ct.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
