package com.hug.mma.activity;

import android.os.Bundle;
import android.os.Handler;

import com.hug.mma.M2AApplication;
import com.hug.mma.R;
import com.hug.mma.databinding.ActivityDataSyncBinding;
import com.hug.mma.preference.AppPrefConstants;
import com.hug.mma.preference.AppPreference;
import com.hug.mma.util.AppUtil;
import com.hug.mma.viewmodel.GetDeviceModel;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.databinding.DataBindingUtil;

public class SyncActivity extends BaseActivity<ActivityDataSyncBinding> {

    private final static long TIMEOUT = 5000;
    private long start = 0;
    private long failed = 0;
    private static final int maxRetries = 3;
    private int currentRetry = 0;

    private final static AtomicInteger pool = new AtomicInteger(0);

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_data_sync);
        start = System.currentTimeMillis();
        startServerSync();
    }

    private int get2Pow(int i) {
        return (int) Math.pow(2, i);
    }

    
    private void startServerSync() {
        showLoading(getString(R.string.syncing_data_from_server));
        // this value will be set based on how many calls we do in the data sync activity
        pool.set(1);
        getDevices(get2Pow(1));
    }

    private void getDevices(int pow) {
        GetDeviceModel deviceModel = new GetDeviceModel(pow);
        deviceModel.run(context, null).getData().observe(this, integer -> {
            if (integer != null && integer > 0) {
                failed += integer;
            }
            done();
        });
    }

    private void done() {
        pool.decrementAndGet();
        if (pool.get() == 0) {
            if (failed > 0) {
                currentRetry++;
                if (currentRetry < maxRetries) {
                    processFailedCalls();
                } else {
                    dismissLoading();
                    M2AApplication.getInstance().snackBarView(this, getString(R.string.data_sync_error)).show();
                    AppPreference.getInstance().putBoolean(AppPrefConstants.SIGN_IN, false);
                    AppUtil.dashboard(context);
                }
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoading();
                        AppPreference.getInstance().putBoolean(AppPrefConstants.SYNC_FROM_SERVER, true);
                        AppUtil.dashboard(context);
                    }
                }, Math.max(TIMEOUT - (System.currentTimeMillis() - start), 0));
            }
        }
    }

    private void processFailedCalls() {
        List<Integer> failedList = AppUtil.getPowers(failed);
        pool.set(failedList.size());
        failed = 0;
        if (failedList.contains(get2Pow(1))) {
            getDevices(get2Pow(1));
        }
    }
}
