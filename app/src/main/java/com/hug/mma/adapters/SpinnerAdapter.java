package com.hug.mma.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hug.mma.R;
import com.hug.mma.databinding.ListItemDeviceBinding;
import com.hug.mma.db.room.entity.Device;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

public class SpinnerAdapter extends BaseAdapter {
    Context context;
    List<Device> devices;
    LayoutInflater inflter;

    public SpinnerAdapter(Context applicationContext, List<Device> deviceList) {
        this.context = applicationContext;
        this.devices = deviceList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int i) {
        return devices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_custom_layout, null);
        TextView name = (TextView) view.findViewById(R.id.tv_name);
        name.setText(devices.get(i).getName());
        return view;
    }

    public void setDevices(ArrayList<Device> devicesList) {
        this.devices = devicesList;
        notifyDataSetChanged();
    }
}
