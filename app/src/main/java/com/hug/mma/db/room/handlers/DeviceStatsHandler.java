package com.hug.mma.db.room.handlers;

import android.os.AsyncTask;

import com.hug.mma.M2AApplication;
import com.hug.mma.db.room.AppDatabase;
import com.hug.mma.db.room.entity.DeviceStats;

import java.util.Date;
import java.util.List;

public class DeviceStatsHandler {

    void processDeviceStatsFromServer(List<DeviceStats> deviceStatsList, Date date, String deviceId) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                AppDatabase database = AppDatabase.getDatabase(M2AApplication.getInstance());
                for (DeviceStats deviceStats : deviceStatsList) {
                    if (deviceStats != null) {
                        DeviceStats statsFromDb = database.deviceStatsDao().getBatchForDeviceAndDate(deviceStats.getBatch(), deviceId, date);
                        if (statsFromDb == null) {
                            statsFromDb = deviceStats;
                            statsFromDb.setRecorded(date);
                            statsFromDb.setDevice(deviceId);
                            database.deviceStatsDao().insertAll(statsFromDb);
                        } else {
                            statsFromDb = deviceStats;
                            statsFromDb.setRecorded(date);
                            statsFromDb.setDevice(deviceId);
                            database.deviceStatsDao().update(statsFromDb);
                        }
                    }
                }
                return null;
            }
        }.execute();
    }

    private DeviceStats findDifferenceInBatchStats(DeviceStats statsFromDb, DeviceStats deviceStats) {
      /*  if (!statsFromDb.getName().equals(deviceStats.getName())) {
            statsFromDb.setName(deviceStats.getName());
            statsFromDb.setChanged(true);
        }
        if (!statsFromDb.getType().equals(deviceStats.getType())) {
            statsFromDb.setType(deviceStats.getType());
            statsFromDb.setChanged(true);
        }*/
        return statsFromDb;
    }
}
