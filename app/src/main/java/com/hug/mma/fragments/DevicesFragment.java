package com.hug.mma.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.hug.mma.R;
import com.hug.mma.activity.HomeActivity;
import com.hug.mma.adapters.DevicesAdapter;
import com.hug.mma.databinding.FragmentDevicesBinding;
import com.hug.mma.db.room.entity.Device;
import com.hug.mma.db.room.viewmodels.DeviceListViewModel;
import com.hug.mma.listener.ListItemListener;
import com.hug.mma.util.Trace;
import com.hug.mma.viewmodel.GetDeviceModel;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;


public class DevicesFragment extends BaseFragment<FragmentDevicesBinding> {

    private DeviceListViewModel devicesViewModel;
    private DevicesAdapter devicesAdapter;
    private ArrayList<Device> devicesList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = (FragmentDevicesBinding) DataBindingUtil.inflate(inflater, R.layout.fragment_devices, container, false);
        observeClick(binding.getRoot());
        init();
        listenData();
        //setHasOptionsMenu(true);
        return binding.getRoot();
    }

    private void listenData() {
        devicesViewModel = ViewModelProviders.of(this).get(DeviceListViewModel.class);
        devicesViewModel.getDevices().observe(this, devicesInfo -> {
            //Todo
            devicesList = (ArrayList<Device>) devicesInfo.getDevices();
            binding.setHasDevices(devicesList != null && !devicesList.isEmpty());
            Collections.sort(devicesList, (d1, d2) -> d1.getName().compareTo(d2.getName()));
            if (devicesAdapter != null) devicesAdapter.setDevices(devicesList, false);
        });
    }

    private void init() {
        setRecyclerView();
    }

    private void setRecyclerView() {
        try {
            devicesAdapter = new DevicesAdapter(devicesList, context, new ListItemListener() {
                @Override
                public void onItemClick(int position) {
                    NavDirections directions = DevicesFragmentDirections.actionDevicesFragmentToDeviceSummaryFragment(devicesList.get(position).getDevice(), getString(R.string.device_stats));
                    Navigation.findNavController(getActivity(), R.id.home_nav_fragment).navigate(directions);
                }

                @Override
                public void onChanged(boolean isChanged) {

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(devicesAdapter);
    }


    public void resume() {
        attachObservers();
        updateDevicesFromServer();
    }

    private void attachObservers() {
        if (devicesViewModel != null) {
            devicesViewModel.run(this);
        }
    }

    private void updateDevicesFromServer() {
        GetDeviceModel deviceModel = new GetDeviceModel(1);
        deviceModel.run(context, null).getData().observe(this, integer -> {
            listenData();
            if (integer != null && integer == 0) {
                Trace.i(getString(R.string.device_update_success));
            } else {
                Trace.i(getString(R.string.device_update_failed));
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_device_list, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.device_management == item.getItemId()) {
            Trace.i("Device management screen");
            navigateDeviceManagement();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    private void navigateDeviceManagement() {
        ((HomeActivity) context).getNavController().navigate(R.id.device_managementFragment);
    }


}
