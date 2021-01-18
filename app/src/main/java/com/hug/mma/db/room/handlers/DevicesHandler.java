package com.hug.mma.db.room.handlers;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.hug.mma.M2AApplication;
import com.hug.mma.db.room.AppDatabase;
import com.hug.mma.db.room.entity.Device;
import com.hug.mma.db.room.entity.DeviceStats;
import com.hug.mma.db.room.entity.DevicesInfo;
import com.hug.mma.preference.AppPrefConstants;
import com.hug.mma.preference.AppPreference;

import java.util.List;

public class DevicesHandler {
    @SuppressLint("StaticFieldLeak")
    void processDevicesFromServer(List<Device> deviceList) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                AppDatabase database = AppDatabase.getDatabase(M2AApplication.getInstance());
                String userId = AppPreference.getInstance().getString(AppPrefConstants.USER_PHONE);
                if (userId != null && deviceList != null) {
                    DevicesInfo devicefromDb = database.devicesDao().getDeviceByID(userId);
                    if (devicefromDb == null) {
                        devicefromDb = new DevicesInfo();
                        devicefromDb.setUserId(userId);
                        devicefromDb.setDevices(deviceList);
                        database.devicesDao().insertAll(devicefromDb);
                    } else {
                        devicefromDb.setDevices(deviceList);
                        database.devicesDao().update(devicefromDb);
                    }
                }
                return null;
            }
        }.execute();

    }

    private Device findDifferencesInDevice(Device device, Device devicefromDb) {
        if (devicefromDb.getName() != device.getName()) {
            devicefromDb.setName(device.getName());
            devicefromDb.setChanged(true);
        }
        return devicefromDb;
    }
}
