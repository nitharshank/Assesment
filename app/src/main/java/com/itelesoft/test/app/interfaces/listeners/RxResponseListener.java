package com.itelesoft.test.app.interfaces.listeners;

import retrofit2.Response;

public interface RxResponseListener<T> {

    void serviceResponse(Response<T> serviceResponseResponse);

    void serviceThrowable(Throwable throwable);

    void serviceComplete();
}