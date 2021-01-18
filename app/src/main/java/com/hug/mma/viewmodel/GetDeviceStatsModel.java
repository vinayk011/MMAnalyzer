package com.hug.mma.viewmodel;

import android.content.Context;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.hug.mma.db.room.entity.DeviceStats;
import com.hug.mma.db.room.handlers.DataHandler;
import com.hug.mma.network.NetworkError;
import com.hug.mma.network.NetworkListener;
import com.hug.mma.network.RestCall;
import com.hug.mma.util.DateUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.hug.mma.constants.IntentConstants.DATE;
import static com.hug.mma.constants.IntentConstants.DEVICE_ID;

public class GetDeviceStatsModel extends BaseAndroidViewModel<Integer, JsonArray, Bundle, GetDeviceStatsModel> {

    public GetDeviceStatsModel(int errorCode) {
        super(true, errorCode);
    }

    @Override
    public GetDeviceStatsModel run(final Context context, Bundle param) {
        restCall = new RestCall<>(context, false);
        restCall.execute(restServices.getDeviceStats(param.getString(DEVICE_ID), param.getString(DATE)),3, new NetworkListener<JsonArray>() {
            @Override
            public void success(JsonArray jsonArray) {
                if (jsonArray != null) {
                    Type type = new TypeToken<List<DeviceStats>>() {
                    }.getType();
                    List<DeviceStats> deviceStats = new Gson().fromJson(jsonArray, type);
                    DataHandler.getInstance().addDeviceStatsToDb(deviceStats, param.getString(DATE), param.getString(DEVICE_ID) );
                    data.postValue(0);
                } else {
                    data.postValue(errorCode);
                }
            }

            @Override
            public void headers(Map<String, String> header) {

            }

            @Override
            public void fail(int code, List<NetworkError> networkErrors) {
                if (code == 404) {
                    data.postValue(0);
                } else {
                    data.postValue(errorCode);
                }
            }

            @Override
            public void failure() {
                data.postValue(errorCode);
            }
        });
        return this;
    }
}