package com.itelesoft.test.app.utils;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.itelesoft.test.app.config.AppConst;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebUtil {

    // retrofit instances
    private static Retrofit mRetrofitInstance = null;

    /**
     * Create and return Retrofit instance.
     *
     * @return mRetrofitInstance object
     */
    public static Retrofit getRetrofitInstanceForUserRoles(String baseUrl) {

        if (mRetrofitInstance == null) {
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addNetworkInterceptor((Interceptor) new StethoInterceptor())
                    .readTimeout(AppConst.DATA_TIMEOUT, TimeUnit.SECONDS) // socket timeout
                    .connectTimeout(AppConst.CONNECTION_TIMEOUT, TimeUnit.SECONDS) // connect timeout
                    .build();

            mRetrofitInstance = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return mRetrofitInstance;
    }

}
