package com.itelesoft.test.app.interfaces.listeners;

import com.itelesoft.test.app.dtos.ServiceResponse;

import retrofit2.Response;

public interface RxResponseListener<T> {

    void serviceResponse(Response<T> serviceResponseResponse);

    void serviceThrowable(Throwable throwable);

    void serviceComplete();
}