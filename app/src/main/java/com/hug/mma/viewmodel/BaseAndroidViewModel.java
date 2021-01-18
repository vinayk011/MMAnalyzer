package com.hug.mma.viewmodel;


import android.content.Context;

import com.hug.mma.network.RestCall;
import com.hug.mma.network.RestServices;
import com.hug.mma.network.RetrofitService;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * Created by Raviteja on 02-08-2017 for HugFit.
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

abstract class BaseAndroidViewModel<LiveData, Response, Request, Base> implements LifecycleObserver {
    RestCall<Response> restCall;
    RestServices restServices;
    int errorCode;

    final MutableLiveData<LiveData> data = new MutableLiveData<>();

    abstract Base run(Context context, Request request);

    public BaseAndroidViewModel(boolean session, Lifecycle lifecycle) {
        restServices = RetrofitService.getRestService(session).create(RestServices.class);
        lifecycle.addObserver(this);
    }

    BaseAndroidViewModel(boolean session, int errorCode) {
        this.errorCode = errorCode;
        restServices = RetrofitService.getRestService(session).create(RestServices.class);
    }

    BaseAndroidViewModel(boolean session, String accept) {
        restServices = RetrofitService.getRestService(session, accept).create(RestServices.class);
    }

    BaseAndroidViewModel(boolean session) {
        restServices = RetrofitService.getRestService(session).create(RestServices.class);
    }

    public MutableLiveData<LiveData> getData() {
        return data;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        if (restCall != null) {
            restCall.cancel();
        }
    }

    void reset() {
        data.setValue(null);
    }
}
