package com.hug.mma.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hug.mma.R;
import com.hug.mma.binding.ViewHolderBinding;
import com.hug.mma.databinding.ListItemSummaryBinding;
import com.hug.mma.db.room.entity.DeviceStats;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class BatchListAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<DeviceStats> batchList;

    public BatchListAdapter(Context context, List<DeviceStats> arrayList) {
        mContext = context;
        this.batchList = arrayList;
    }

    public void setBatchList(List<DeviceStats> batchList) {
        this.batchList = batchList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.list_item_summary, parent, false);
        return new ViewHolderBinding(viewDataBinding);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final DeviceStats stats = batchList.get(holder.getAdapterPosition());
        final ViewHolderBinding viewHolderMain = (ViewHolderBinding) holder;
        final ListItemSummaryBinding itemSummaryBinding = (ListItemSummaryBinding) viewHolderMain.binding;
        itemSummaryBinding.setBatchStats(stats);
        itemSummaryBinding.executePendingBindings();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return batchList.size();
    }
}