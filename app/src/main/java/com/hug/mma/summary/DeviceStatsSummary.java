package com.hug.mma.summary;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hug.mma.R;
import com.hug.mma.adapters.BatchListAdapter;
import com.hug.mma.adapters.BindingAdapters;
import com.hug.mma.adapters.SummaryAdapter;
import com.hug.mma.constants.EnumConstants;
import com.hug.mma.db.room.AppDatabase;
import com.hug.mma.db.room.entity.DeviceStats;
import com.hug.mma.listener.BatchesCallback;
import com.hug.mma.listener.ValueSelectedListener;
import com.hug.mma.model.Summary;
import com.hug.mma.preference.AppPrefConstants;
import com.hug.mma.preference.AppPreference;
import com.hug.mma.util.DateUtil;
import com.hug.mma.util.TimeComparator;
import com.hug.mma.util.Trace;
import com.hug.mma.viewmodel.GetDeviceStatsModel;
import com.hug.mma.widget.DeviceStatsChart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.recyclerview.widget.RecyclerView.VERTICAL;
import static com.hug.mma.constants.IntentConstants.DATE;
import static com.hug.mma.constants.IntentConstants.DEVICE_ID;

public class DeviceStatsSummary implements BaseSummary<DeviceStats>, LifecycleOwner {
    private Lifecycle lifecycle;
    private Context context;
    private AppDatabase appDatabase;
    private SummaryAdapter summaryAdapter;
    private BatchListAdapter batchListAdapter;
    private DeviceStatsChart deviceStatsChart;
    private List<DeviceStats> deviceStatsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView textView;
    private RecyclerView cardsRecyclerView;
    private int currentIndex = 0;
    private ArrayList<Summary> summaryArrayList = new ArrayList<>();
    private BatchesCallback callback;

    public DeviceStatsSummary(Context context, Lifecycle lifecycle, BatchesCallback callback) {
        this.context = context;
        this.appDatabase = AppDatabase.getDatabase(context);
        this.deviceStatsChart = new DeviceStatsChart(context, selectedListener);
        this.lifecycle = lifecycle;
        this.callback = callback;
        defaultSummary();
    }

    @Override
    public void defaultSummary() {
        summaryArrayList.add(new Summary(context.getString(R.string.type), context.getString(R.string.space), getColor()));
        summaryArrayList.add(new Summary(context.getString(R.string.total_samples), context.getString(R.string.space), getColor()));
        summaryArrayList.add(new Summary(context.getString(R.string.min_ec), context.getString(R.string.space), getColor()));
        summaryArrayList.add(new Summary(context.getString(R.string.max_ec), context.getString(R.string.space), getColor()));
        summaryArrayList.add(new Summary(context.getString(R.string.min_diff_ec), context.getString(R.string.space), getColor()));
        summaryArrayList.add(new Summary(context.getString(R.string.max_diff_ec), context.getString(R.string.space), getColor()));
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public int getTitleLogo() {
        return 0;
    }

    @Override
    public int getColor() {
        return ContextCompat.getColor(context, R.color.colorLogoYellow);
    }

    @Override
    public void callServer(String device, Date date) {
        GetDeviceStatsModel deviceStatsModel = new GetDeviceStatsModel(1);
        Bundle bundle = new Bundle();
        bundle.putString(DEVICE_ID, device);
        bundle.putString(DATE, DateUtil.convertDateFormat(date));
        deviceStatsModel.run(context, bundle).getData().observeForever(new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer != null && integer == code) {
                    Trace.i("Failed");
                }
            }
        });
    }

    @Override
    public LiveData<List<DeviceStats>> callDB(EnumConstants.Scope scope, String device, Date date) {
        return appDatabase.deviceStatsDao().getBatchesForDateAndDeviceAsLive(date, device);
    }

    @Override
    public void setChart(FrameLayout frameLayout) {
        if (frameLayout != null) {
            frameLayout.removeAllViews();
            frameLayout.addView(deviceStatsChart);
            deviceStatsChart.setLineChartProperties();
            deviceStatsChart.setVerticalLegend(context.getString(R.string.qty_of_milk));
        }
    }

    @Override
    public void setRecyclerView(RecyclerView recyclerView, TextView textView) {
        this.recyclerView = recyclerView;
        this.textView = textView;
        summaryAdapter = new SummaryAdapter(context, summaryArrayList);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new GridLayoutManager(context, 2, VERTICAL, false));
        this.recyclerView.setAdapter(summaryAdapter);
    }

    @Override
    public void setCardsRecyclerView(RecyclerView recyclerView) {
        this.cardsRecyclerView = recyclerView;
        batchListAdapter = new BatchListAdapter(context, deviceStatsList);
        this.cardsRecyclerView.setHasFixedSize(true);
        this.cardsRecyclerView.setLayoutManager(new LinearLayoutManager(context, VERTICAL, false));
        this.cardsRecyclerView.setAdapter(batchListAdapter);
    }

    @Override
    public void setBottomText(String text) {
        if (deviceStatsChart != null) {
            deviceStatsChart.setHorizontalLegend(text);
        }
    }

    @Override
    public Lifecycle getLifecycle() {
        return lifecycle;
    }

    @Override
    public void updateSummary(List<DeviceStats> list, EnumConstants.Scope scope, Date date) {
        //Todo update list tiles

    }

    @Override
    public void updateSummary() {
        try {
            if (deviceStatsList != null & !deviceStatsList.isEmpty()) {
                if (currentIndex >= 0 & currentIndex < deviceStatsList.size()) {
                    for (Summary summary : summaryArrayList) {
                        if (context.getString(R.string.type).equals(summary.getTitle())) {
                            summary.setData(deviceStatsList.get(currentIndex).getType());
                        } else if (context.getString(R.string.total_samples).equals(summary.getTitle())) {
                            summary.setData(deviceStatsList.get(currentIndex).getTotalSamples());
                        } else if (context.getString(R.string.min_ec).equals(summary.getTitle())) {
                            summary.setData(deviceStatsList.get(currentIndex).getEc().getMinEc());
                        } else if (context.getString(R.string.max_ec).equals(summary.getTitle())) {
                            summary.setData(deviceStatsList.get(currentIndex).getEc().getMaxEc() > 0 ? deviceStatsList.get(currentIndex).getEc().getMaxEc() : 0);
                        } else if (context.getString(R.string.min_diff_ec).equals(summary.getTitle())) {
                            summary.setData(deviceStatsList.get(currentIndex).getEc().getMinDiff() > 0 ? deviceStatsList.get(currentIndex).getEc().getMinDiff() : 0);
                        } else if (context.getString(R.string.max_diff_ec).equals(summary.getTitle())) {
                            summary.setData(deviceStatsList.get(currentIndex).getEc().getMaxDiff() > 0 ? deviceStatsList.get(currentIndex).getEc().getMaxDiff() : 0);
                        }
                    }
                    BindingAdapters.setTime(textView, deviceStatsList.get(currentIndex).getStartTime(), deviceStatsList.get(currentIndex).getEndTime());
                }
            } else {
                for (Summary summary : summaryArrayList) {
                    summary.setData(context.getString(R.string.space));
                }
                textView.setText(" ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        summaryAdapter.notifyDataSetChanged();
    }

    @Override
    public void update(EnumConstants.Scope scope, String device, Date date) {
        if (deviceStatsChart != null) {
            deviceStatsChart.changeScope(scope, date);
            currentIndex = 0;
            callServer(device, date);
            callDB(scope, device, date).observe(this, deviceStats -> {
                if (deviceStatsChart.isValid(scope, date)) {
                    if (deviceStats != null) {
                        deviceStatsList = sortByQuality(processDeviceStatsForSeq(deviceStats));
                        callback.onBatchesReceived(!deviceStats.isEmpty());
                    }
                    updateViews();
                }
            });
        }
    }

    private List<DeviceStats> processDeviceStatsForSeq(List<DeviceStats> deviceStats) {
        Collections.sort(deviceStats, new TimeComparator());
        int i = 1;
        for (DeviceStats stats : deviceStats) {
            stats.setSeq(i);
            i++;
        }
        Collections.reverse(deviceStats);
        return deviceStats;
    }

    private List<DeviceStats> sortByQuality(List<DeviceStats> deviceStats) {

        if (deviceStats != null && !deviceStats.isEmpty()) {
            List<DeviceStats> devicesM = new ArrayList<>();
            List<DeviceStats> devicesG = new ArrayList<>();
            List<DeviceStats> devicesB = new ArrayList<>();
            for (DeviceStats stats : deviceStats) {
                if (stats.getQuality().equals("MASTITIS")) {
                    devicesM.add(stats);
                } else if (stats.getQuality().equals("BAD")) {
                    devicesB.add(stats);
                } else {
                    devicesG.add(stats);
                }
            }

            deviceStats.clear();
            deviceStats.addAll(devicesM);
            deviceStats.addAll(devicesB);
            deviceStats.addAll(devicesG);
        }
        return deviceStats;
    }

    @Override
    public void updateViews() {
        if (AppPreference.getInstance().getBoolean(AppPrefConstants.PREF_SUMMARY_GRAPH)) {
            deviceStatsChart.insertData(deviceStatsList);
            updateSummary();
        } else {
            batchListAdapter.setBatchList(deviceStatsList);
        }
    }

    private ValueSelectedListener selectedListener = new ValueSelectedListener() {
        @Override
        public void onValueSelected(int index) {
            currentIndex = index;
            updateSummary();
        }

        @Override
        public void onValueSelected(String value) {

        }
    };


}
