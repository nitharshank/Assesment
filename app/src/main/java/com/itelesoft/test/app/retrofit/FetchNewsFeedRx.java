package com.itelesoft.test.app.retrofit;

import android.content.Context;

import androidx.collection.ArrayMap;

import com.google.gson.JsonElement;
import com.itelesoft.test.app.BuildConfig;
import com.itelesoft.test.app.config.ApiConst;
import com.itelesoft.test.app.config.AppConst;
import com.itelesoft.test.app.interfaces.RxInterface;
import com.itelesoft.test.app.interfaces.listeners.RxResponseListener;
import com.itelesoft.test.app.utils.AppUtil;
import com.itelesoft.test.app.utils.WebUtil;

import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class FetchNewsFeedRx {

    // constants
    private static final String TAG = FetchNewsFeedRx.class.getSimpleName();

    //response listener
    private RxResponseListener mRxResponseListenerForRoles;

    private Context mContext;
    private String mQueryText;
    private String mPageCount;

    public FetchNewsFeedRx(Context context, String queryText, String pageCount, RxResponseListener rxResponseListenerForRoles) {
        mContext = context;
        this.mQueryText = queryText;
        this.mPageCount = pageCount;
        this.mRxResponseListenerForRoles = rxResponseListenerForRoles;
    }

    public void callServiceRXWay() {
        RxInterface rXService = null;

        Map<String, String> queryMap = new ArrayMap<>(6);
        if (BuildConfig.DEBUG) {
            rXService = WebUtil.getRetrofitInstanceForUserRoles(AppConst.SANDBOX_BASE_URL_FOR_ROLES).create(RxInterface.class);
            queryMap.put(ApiConst.ATTR_TAG_API_KEY, AppConst.SANDBOX_API_KEY);
        } else {
            rXService = WebUtil.getRetrofitInstanceForUserRoles(AppConst.PRODUCTION_BASE_URL_FOR_ROLES).create(RxInterface.class);
            queryMap.put(ApiConst.ATTR_TAG_API_KEY, AppConst.PRODUCTION_API_KEY);
        }


        queryMap.put(ApiConst.ATTR_TAG_QUERY_TEXT_KEY, mQueryText);
        queryMap.put(ApiConst.ATTR_TAG_QUERY_FROM_KEY, AppUtil.getToday());
        queryMap.put(ApiConst.ATTR_TAG_QUERY_SORT_BY_KEY, AppConst.SORT_BY_VALUE);
        queryMap.put(ApiConst.ATTR_TAG_PAGE_SIZE_KEY, String.valueOf(AppConst.PAGE_SIZE_VALUE));
        queryMap.put(ApiConst.ATTR_TAG_PAGE_KEY, mPageCount);

        Observable<Response<JsonElement>> observable = rXService.fetchNewsFeed(AppConst.CONTENT_TYPE, queryMap);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<Response<JsonElement>>() {
                               @Override
                               public void call(Response<JsonElement> serviceResponseResponse) {
                                   mRxResponseListenerForRoles.serviceResponse(serviceResponseResponse);
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                mRxResponseListenerForRoles.serviceThrowable(throwable);
                            }
                        },
                        new Action0() {
                            @Override
                            public void call() {
                                mRxResponseListenerForRoles.serviceComplete();
                            }
                        });

    }
}
