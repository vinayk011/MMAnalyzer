package com.hug.mma.viewmodel;

import android.content.Context;

import com.hug.mma.db.room.entity.Device;
import com.hug.mma.db.room.handlers.DataHandler;
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

public class AddDeviceModel extends BaseAndroidViewModel<Integer, Void, List<Device>, AddDeviceModel> {

    public AddDeviceModel(int errorCode) {
        super(true, errorCode);
    }

    @Override
    public AddDeviceModel run(final Context context, final List<Device> deviceList) {
        restCall = new RestCall<>(context, true);
        restCall.execute(restServices.addDevices(deviceList), 2,
                new NetworkListener<Void>() {
                    @Override
                    public void success(Void aVoid) {
                        Trace.i("");
                        DataHandler.getInstance().addDevicesToDb(deviceList);
                        data.postValue(0);
                    }

                    @Override
                    public void headers(Map<String, String> header) {

                    }

                    @Override
                    public void fail(int code, List<NetworkError> networkErrors) {
                        new NetworkErrorDialog(context, networkErrors, () -> data.postValue(errorCode)).show();
                    }

                    @Override
                    public void failure() {
                        data.postValue(errorCode);
                    }
                });
        return this;
    }
}