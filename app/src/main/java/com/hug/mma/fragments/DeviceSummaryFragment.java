package com.hug.mma.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.hug.mma.R;
import com.hug.mma.adapters.SpinnerAdapter;
import com.hug.mma.constants.EnumConstants;
import com.hug.mma.databinding.FragmentDeviceSummaryBinding;
import com.hug.mma.db.room.AppDatabase;
import com.hug.mma.db.room.entity.Device;
import com.hug.mma.listener.BatchesCallback;
import com.hug.mma.model.SummaryScope;
import com.hug.mma.preference.AppPrefConstants;
import com.hug.mma.preference.AppPreference;
import com.hug.mma.summary.BaseSummary;
import com.hug.mma.summary.DeviceStatsSummary;
import com.hug.mma.util.DateUtil;
import com.hug.mma.util.StringUtil;
import com.hug.mma.util.Trace;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

public class DeviceSummaryFragment extends BaseFragment<FragmentDeviceSummaryBinding> implements AdapterView.OnItemSelectedListener {

    private SummaryScope summaryScope = new SummaryScope();
    private BaseSummary baseSummary;
    private long c = 0, p = 0, n = 0;
    String deviceId;
    private ArrayList<Device> devicesList = new ArrayList<>();
    private SpinnerAdapter spinnerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = (FragmentDeviceSummaryBinding) DataBindingUtil.inflate(inflater, R.layout.fragment_device_summary, container, false);
        observeClick(binding.getRoot());
        setHasOptionsMenu(true);
        binding.setColor(ContextCompat.getColor(context, R.color.white));
        readArgs();
        setUpChart();
        setUpSpinner();
        init();
        return binding.getRoot();
    }

    private void readArgs() {
        if (getArguments() != null) {
            deviceId = DeviceSummaryFragmentArgs.fromBundle(getArguments()).getDeviceId();
            if (!StringUtil.isEmpty(DeviceSummaryFragmentArgs.fromBundle(getArguments()).getSummaryType())) {
                if (getString(R.string.device_stats).equals(DeviceSummaryFragmentArgs.fromBundle(getArguments()).getSummaryType())) {
                    baseSummary = new DeviceStatsSummary(context, getLifecycle(), new BatchesCallback() {
                        @Override
                        public void onBatchesReceived(boolean hasBatches) {
                            binding.setHasBatches(hasBatches);
                        }
                    });
                    //binding.toolbarTitle.setText(baseSummary.getTitle());
                    binding.setColor(baseSummary.getColor());
                    return;
                }
            }
        }

        toastView(getString(R.string.something_went_wrong_error));
    }


    private void setUpChart() {
        if (baseSummary != null) {
            baseSummary.setRecyclerView(binding.summaryView, binding.tvTime);
            baseSummary.setCardsRecyclerView(binding.cardsView);
            baseSummary.setChart(binding.graphView);
            baseSummary.setBottomText(getString(R.string.batches));
        }
    }

    private void init() {
        binding.setIsGraphView(AppPreference.getInstance().getBoolean(AppPrefConstants.PREF_SUMMARY_GRAPH));
        binding.setSummaryScope(summaryScope.getCurrent());
        binding.previous.setOnClickListener(v -> previous());
        binding.next.setOnClickListener(v -> next());
        binding.setSummaryDate(DateUtil.today());
        //update();
        buttonsUpdate(false, true);
    }

    private void setUpSpinner() {
        spinnerAdapter = new SpinnerAdapter(context, devicesList);
        binding.spinner.setAdapter(spinnerAdapter);
        updateDevices();
    }

    private void updateDevices() {
        if (AppPreference.getInstance().getString(AppPrefConstants.USER_PHONE) != null)
            AppDatabase.getDatabase(context).devicesDao().getDevicesByIDAsLive(AppPreference.getInstance()
                    .getString(AppPrefConstants.USER_PHONE)).observe(this, devicesInfo -> {
                this.devicesList = (ArrayList<Device>) devicesInfo.getDevices();
                if (devicesList != null) {
                    this.spinnerAdapter.setDevices(devicesList);
                    for (int i = 0; i < devicesList.size(); i++) {
                        if (deviceId.equals(devicesList.get(i).getDevice())) {
                            binding.spinner.setSelection(i);
                            break;
                        }
                    }
                    binding.spinner.setOnItemSelectedListener(this);
                }
            });
    }

    private void buttonsUpdate(boolean next, boolean previous) {
        binding.previous.setEnabled(previous);
        binding.previous.setAlpha(previous ? 1.0f : 0.5f);
        binding.next.setEnabled(next);
        binding.next.setAlpha(next ? 1.0f : 0.5f);
        if (!next && !previous) {
            binding.previous.setVisibility(View.GONE);
            binding.next.setVisibility(View.GONE);
        } else {
            binding.previous.setVisibility(View.VISIBLE);
            binding.next.setVisibility(View.VISIBLE);
        }
    }


    private void update() {
        baseSummary.update(binding.getSummaryScope(), deviceId, binding.getSummaryDate());
    }

    private void next() {
        navigate(1);
    }

    private void previous() {
        navigate(-1);
    }

    private void navigate(int value) {
        if (binding == null) {
            return;
        }
        Calendar calendar = DateUtil.calToday();
        if (binding.getSummaryDate() != null) {
            calendar.setTime(binding.getSummaryDate());
        }
        switch (summaryScope.getCurrent()) {
            case DAY:
                calendar.add(Calendar.DAY_OF_YEAR, value);
                baseSummary.setBottomText(getString(R.string.batches));
                break;
            /*case WEEK:
                calendar.add(Calendar.DAY_OF_YEAR, value * 7);
                baseSummary.setBottomText(getString(R.string.day_of_week));
                break;
            case MONTH:
                calendar.add(Calendar.MONTH, value);
                baseSummary.setBottomText(getString(R.string.day_of_month));
                break;*/
        }
        if (DateUtil.isAfterDay(calendar, Calendar.getInstance())) {
            calendar = DateUtil.calToday();
        }
        binding.setSummaryDate(calendar.getTime());
        process(calendar, summaryScope.getCurrent());
        baseSummary.update(binding.getSummaryScope(), deviceId, binding.getSummaryDate());
    }

    private void process(Calendar calendar, EnumConstants.Scope scope) {
        if (scope == EnumConstants.Scope.DAY) {
            buttonsUpdate(!DateUtil.isToday(calendar), true);
        }/* else if (scope == EnumConstants.Scope.WEEK) {
            buttonsUpdate(!DateUtil.isInCurrentWeek(calendar), true);
        } else if (scope == EnumConstants.Scope.MONTH) {
            buttonsUpdate(!DateUtil.isCurrentMonth(calendar), true);
        }*/
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        deviceId = devicesList.get(position).getDevice();
        init();
        update();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_device_details, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //Todo handle menu items visibility

        menu.findItem(R.id.graph_view).setVisible(!AppPreference.getInstance().getBoolean(AppPrefConstants.PREF_SUMMARY_GRAPH));
        menu.findItem(R.id.card_view).setVisible(AppPreference.getInstance().getBoolean(AppPrefConstants.PREF_SUMMARY_GRAPH));
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.graph_view == item.getItemId()) {
            updateViews(true);
            return true;
        } else if (R.id.card_view == item.getItemId()) {
            updateViews(false);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void updateViews(boolean graphView) {
        AppPreference.getInstance().putBoolean(AppPrefConstants.PREF_SUMMARY_GRAPH, graphView);
        binding.setIsGraphView(graphView);
        baseSummary.updateViews();
        getActivity().invalidateOptionsMenu();

    }


    public void destroy() {
        Trace.i("destroyed");
    }
}
