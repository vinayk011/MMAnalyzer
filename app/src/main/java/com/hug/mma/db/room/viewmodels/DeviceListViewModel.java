package com.hug.mma.db.room.viewmodels;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;

import com.hug.mma.db.room.AppDatabase;
import com.hug.mma.db.room.entity.Device;
import com.hug.mma.db.room.entity.DevicesInfo;
import com.hug.mma.preference.AppPrefConstants;
import com.hug.mma.preference.AppPreference;
import com.hug.mma.util.Trace;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class DeviceListViewModel extends AndroidViewModel {
    private MutableLiveData<DevicesInfo> deviceInfo = new MutableLiveData<>();
    private AppDatabase appDatabase;
    private LiveData<DevicesInfo> deviceLiveData;

    public DeviceListViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getDatabase(application);
    }


    public LiveData<DevicesInfo> getDevices() {
        return deviceInfo;
    }

    public void run(LifecycleOwner lifecycleOwner) {
        clear(lifecycleOwner);
        if (AppPreference.getInstance().getString(AppPrefConstants.USER_PHONE) != null) {
            deviceLiveData = appDatabase.devicesDao().getDevicesByIDAsLive(AppPreference.getInstance().getString(AppPrefConstants.USER_PHONE));
            deviceLiveData.observe(lifecycleOwner, stepsObserver);
        }


       /* new AsyncTask<Void, Void, DevicesInfo>() {
            @Override
            protected void onPostExecute(DevicesInfo devicesInfo) {
                if (devicesInfo != null) {
                    Trace.i("Device List:" + devicesInfo.toString());
                    deviceInfo.setValue(devicesInfo);
                }
            }

            @Override
            protected DevicesInfo doInBackground(Void... voids) {
                if (AppPreference.getInstance().getString(AppPrefConstants.USER_PHONE) != null)
                    stepsLiveData = appDatabase.devicesDao().getDevicesByIDAsLive(AppPreference.getInstance().getString(AppPrefConstants.USER_PHONE));
                    stepsLiveData.observe(lifecycleOwner, stepsObserver);
                    return appDatabase.devicesDao().getDeviceByID(AppPreference.getInstance().getString(AppPrefConstants.USER_PHONE));
                else return null;
            }
        }.execute();
*/
// do async operation to fetch devices
        /*Handler myHandler = new Handler();
        myHandler.postDelayed(() -> {
            devices.postValue(appDatabase.devicesDao().getAll());
        }, 5000);*/
    }


    @Override
    protected void onCleared() {
        super.onCleared();
    }


    public void clear(LifecycleOwner lifecycleOwner) {
        if (deviceLiveData != null) {
            deviceLiveData.removeObservers(lifecycleOwner);
        }
    }

    private Observer<DevicesInfo> stepsObserver = info -> {
        if (info != null) {
            deviceInfo.setValue(info);
        }
    };

}
