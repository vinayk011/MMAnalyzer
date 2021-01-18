package com.hug.mma.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.hug.mma.R;
import com.hug.mma.binding.ViewHolderBinding;
import com.hug.mma.databinding.ViewSummaryGridBinding;
import com.hug.mma.model.Summary;

import java.util.ArrayList;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Raviteja on 22-08-2017 for HugFit.
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

public class SummaryAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<Summary> arrayList;

    public SummaryAdapter(Context context, ArrayList<Summary> arrayList) {
        mContext = context;
        this.arrayList = arrayList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.view_summary_grid, parent, false);
        return new ViewHolderBinding(viewDataBinding);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Summary summary = arrayList.get(holder.getAdapterPosition());
        final ViewHolderBinding viewHolderMain = (ViewHolderBinding) holder;
        final ViewSummaryGridBinding viewSummaryGridBinding = (ViewSummaryGridBinding) viewHolderMain.binding;
        viewSummaryGridBinding.setSummary(summary);
        viewSummaryGridBinding.executePendingBindings();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}