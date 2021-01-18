package com.hug.mma.network;

import com.google.gson.JsonArray;
import com.hug.mma.db.room.entity.Device;
import com.hug.mma.viewmodel.request.SignInRequest;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Raviteja on 19-07-2017 for HugFit
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

public interface RestServices {

    @POST("/cattle/login")
    Call<Void> signIn(@Body SignInRequest signInRequest);

    @POST("/cattle/login")
    Call<Void> renew(@Body SignInRequest signInRequest);

    @GET("/cattle/device/user")
    Call<JsonArray> getDevices();

    @POST("/cattle/device/user")
    Call<Void> addDevices(@Body List<Device> devices);

    @GET("/cattle/device/summary/{device}")
    Call<JsonArray> getDeviceStats(@Path("device") String device, @Query("date") String date);
}