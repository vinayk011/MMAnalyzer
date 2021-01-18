package com.hug.mma.viewmodel;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.hug.mma.db.room.entity.Device;
import com.hug.mma.db.room.handlers.DataHandler;
import com.hug.mma.network.NetworkError;
import com.hug.mma.network.NetworkListener;
import com.hug.mma.network.RestCall;
import com.hug.mma.util.Trace;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetDeviceModel extends BaseAndroidViewModel<Integer, JsonArray, Void, GetDeviceModel> {

    public GetDeviceModel(int errorCode) {
        super(true, errorCode);
    }

    @Override
    public GetDeviceModel run(Context context, Void aVoid) {
        restCall = new RestCall<>(context, true);
        restCall.execute(restServices.getDevices(), 2, new NetworkListener<JsonArray>() {
            @Override
            public void success(JsonArray jsonArray) {
                if (jsonArray != null) {
                    Type type = new TypeToken<List<Device>>() {
                    }.getType();
                    try {

                        Trace.i("Devices from server: " +jsonArray.toString());
                        ArrayList<Device> devices = new Gson().fromJson(jsonArray, type);
                        DataHandler.getInstance().addDevicesToDb(devices);
                        data.postValue(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
