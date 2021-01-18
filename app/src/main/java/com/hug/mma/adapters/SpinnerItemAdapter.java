package com.hug.mma.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hug.mma.R;
import com.hug.mma.db.room.entity.Device;

import java.util.ArrayList;
import java.util.List;

public class SpinnerItemAdapter extends BaseAdapter {
    Context context;
    List<String> items;
    LayoutInflater inflter;

    public SpinnerItemAdapter(Context applicationContext, List<String> itemList) {
        this.context = applicationContext;
        this.items = itemList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_custom_layout, null);
        TextView name = (TextView) view.findViewById(R.id.tv_name);
        name.setText(items.get(i));
        return view;
    }

    public void setDevices(ArrayList<String> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}
