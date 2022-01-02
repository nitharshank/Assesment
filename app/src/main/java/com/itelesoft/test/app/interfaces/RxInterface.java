package com.itelesoft.test.app.interfaces;

import com.google.gson.JsonElement;
import com.itelesoft.test.app.config.AppConst;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface RxInterface {

    // Fetch News Feed
    @GET(AppConst.PREFIX + "v2/everything?")
    Observable<Response<JsonElement>> fetchNewsFeed(@Header("Content-Type") String contentType, @QueryMap Map<String, String> params);

}
