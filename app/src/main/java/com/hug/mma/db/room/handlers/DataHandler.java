package com.hug.mma.db.room.handlers;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.hug.mma.M2AApplication;
import com.hug.mma.db.room.AppDatabase;
import com.hug.mma.db.room.entity.Device;
import com.hug.mma.db.room.entity.DeviceStats;
import com.hug.mma.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.hug.mma.constants.IntentConstants.DEVICE_ID;

public class DataHandler {
    private static DataHandler dataHandler;
    private final int DEVICES = 101;
    private final int CLEAR = 105;
    private final int DEVICE_STATS = 102;
    private final String DATE = "date";
    private ReceiverThread receiverThread;

    private class ReceiverThread extends Thread {
        private Handler mHandler;

        Handler getmHandler() {
            return mHandler;
        }

        @Override
        public void run() {
            mHandler = new Handler(Looper.myLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    if (msg != null) {
                        if (DEVICES == msg.what) {
                            List<Device> deviceList = (List<Device>) msg.obj;
                            new DevicesHandler().processDevicesFromServer(deviceList);
                        } else if (DEVICE_STATS == msg.what) {
                            List<DeviceStats> deviceStats = (List<DeviceStats>) msg.obj;
                            Bundle bundle = msg.getData();
                            new DeviceStatsHandler().processDeviceStatsFromServer(deviceStats, DateUtil.convertDateFormat(bundle.getString(DATE)), bundle.getString(DEVICE_ID));
                        }
                    }
                }
            };
        }
    }

    private void clearDataFromDB() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                AppDatabase appDB = AppDatabase.getDatabase(M2AApplication.getInstance());
                appDB.devicesDao().emptyDevices();
                return null;
            }
        }.execute();
    }

    private DataHandler() {
        receiverThread = new ReceiverThread();
        receiverThread.run();
    }

    public static void init() {
        if (dataHandler == null) {
            dataHandler = new DataHandler();
        }
    }

    public static DataHandler getInstance() {
        if (dataHandler == null) {
            throw new NullPointerException("Initialize before use.");
        }
        return dataHandler;
    }

    public void clearData() {
        Message message = new Message();
        message.what = CLEAR;
        receiverThread.getmHandler().sendMessage(message);
    }

    public void addDevicesToDb(List<Device> deviceList) {
        Message message = new Message();
        message.what = DEVICES;
        message.obj = deviceList;
        receiverThread.getmHandler().sendMessage(message);
    }

    public void addDeviceStatsToDb(List<DeviceStats> deviceList, String date, String deviceID) {
        Message message = new Message();
        message.what = DEVICE_STATS;
        message.obj = deviceList;
        Bundle bundle = new Bundle();
        bundle.putString(DATE, date);
        bundle.putString(DEVICE_ID, deviceID);
        message.setData(bundle);
        receiverThread.getmHandler().sendMessage(message);
    }
}
