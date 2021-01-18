package com.hug.mma.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hug.mma.R;
import com.hug.mma.listener.ValueSelectedListener;
import com.hug.mma.model.LanguageData;

import java.util.List;

public class LanguageListAdapter extends BaseAdapter {
    private final List<LanguageData> displayList;
    private Context context;
    private ValueSelectedListener listener;

    public LanguageListAdapter(Context context, List<LanguageData> languages, ValueSelectedListener listener) {
        this.context = context;
        this.displayList = languages;
        this.listener = listener;
    }

    public int getCount() {
        return this.displayList.size();
    }

    public LanguageData getItem(int i) {
        return this.displayList.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.view_text_item_layout, null);
        }
        final LanguageData item = getItem(i);
        ((TextView) view.findViewById(R.id.country_row_item_full_name)).setText(item.getName() + " (" + item.getLocale() + ")");
        view.setOnClickListener(view1 -> listener.onValueSelected(item.getLocale()));
        return view;
    }
}