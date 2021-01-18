package com.hug.mma.viewmodel;

import android.content.Context;
import com.hug.mma.network.NetworkError;
import com.hug.mma.network.NetworkErrorDialog;
import com.hug.mma.network.NetworkErrorListener;
import com.hug.mma.network.NetworkListener;
import com.hug.mma.network.RestCall;
import com.hug.mma.preference.AppPrefConstants;
import com.hug.mma.preference.AppPreference;
import com.hug.mma.util.Trace;
import com.hug.mma.viewmodel.request.SignInRequest;

import java.util.List;
import java.util.Map;

/**
 * Created by Raviteja on 02-08-2017 for HugFit.
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

public class SignInModel extends BaseAndroidViewModel<Boolean, Void, SignInRequest, SignInModel> {
    private boolean background;

    public SignInModel(boolean background) {
        super(false);
        this.background = background;
    }

    @Override
    public SignInModel run(final Context context, final SignInRequest signInRequest) {
        restCall = new RestCall<>(context, background);
        restCall.execute(restServices.signIn(signInRequest), true,
                new NetworkListener<Void>() {
                    @Override
                    public void success(Void aVoid) {
                        Trace.i("");
                        AppPreference.getInstance().putBoolean(AppPrefConstants.SIGN_IN, true);
                        AppPreference.getInstance().putString(AppPrefConstants.USER_PHONE, signInRequest.getMobile());
                        AppPreference.getInstance().putString(AppPrefConstants.USER_PASS, signInRequest.getPassword());
                        data.postValue(true);
                    }

                    @Override
                    public void headers(Map<String, String> header) {

                    }

                    @Override
                    public void fail(int code, List<NetworkError> networkErrors) {
                        if (!background) {
                            new NetworkErrorDialog(context, networkErrors, new NetworkErrorListener() {
                                @Override
                                public void ok() {
                                    data.postValue(false);
                                }
                            }).show();
                        } else {
                            data.postValue(false);
                        }
                    }

                    @Override
                    public void failure() {
                        data.postValue(false);
                    }
                });
        return this;
    }
}