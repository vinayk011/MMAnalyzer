package com.hug.mma.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hug.mma.R;
import com.hug.mma.binding.ViewHolderBinding;
import com.hug.mma.databinding.ListItemDeviceBinding;
import com.hug.mma.db.room.entity.Device;
import com.hug.mma.listener.DeviceDiffUtilCallback;
import com.hug.mma.listener.ListItemListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;

import static com.hug.mma.constants.IntentConstants.DEVICE_ID;

public class DevicesAdapter extends RecyclerView.Adapter {

    private ArrayList<Device> devices = new ArrayList<>();
    private Context mContext;
    private ListItemListener listItemListener;

    public DevicesAdapter(ArrayList<Device> devices, Context mContext, ListItemListener listItemListener) {
        setDevices(devices, false);
        this.mContext = mContext;
        this.listItemListener = listItemListener;
    }

    public void setDevices(ArrayList<Device> newDevices, boolean callBackRequired) {
        if (callBackRequired) {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DeviceDiffUtilCallback(newDevices, devices));

            diffResult.dispatchUpdatesTo(new ListUpdateCallback() {
                @Override
                public void onInserted(int position, int count) {
                    Log.i("onInserted", "position" + position + "--" + count);
                    notifyItemInserted(position);
                    listItemListener.onChanged(true);
                }

                @Override
                public void onRemoved(int position, int count) {
                    Log.i("onRemoved", "position");
                    notifyItemRemoved(position);
                    listItemListener.onChanged(true);
                }

                @Override
                public void onMoved(int fromPosition, int toPosition) {
                    Log.i("onMoved", "position");
                }

                @Override
                public void onChanged(int position, int count, Object payload) {
                    Log.i("onChanged", "position");
                    notifyItemChanged(position, payload);
                    listItemListener.onChanged(true);
                }
            });
            devices.clear();
            this.devices.addAll(newDevices);
        } else {
            devices.clear();
            this.devices.addAll(newDevices);
            notifyDataSetChanged();
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.list_item_device, parent, false);
        return new ViewHolderBinding(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Device device = devices.get(holder.getAdapterPosition());
        final ListItemDeviceBinding itemDeviceBinding = (ListItemDeviceBinding) ((ViewHolderBinding) holder).binding;
        itemDeviceBinding.layoutHandler.setOnClickListener(v -> listItemListener.onItemClick(position));
        itemDeviceBinding.setDeviceInfo(device);
        itemDeviceBinding.executePendingBindings();

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            Bundle o = (Bundle) payloads.get(0);
            for (String key : o.keySet()) {
                if (key.equals(DEVICE_ID)) {
                    final ListItemDeviceBinding itemDeviceBinding = (ListItemDeviceBinding) ((ViewHolderBinding) holder).binding;
                    itemDeviceBinding.layoutHandler.setOnClickListener(v -> listItemListener.onItemClick(position));
                    itemDeviceBinding.setDeviceInfo((Device) o.getSerializable(DEVICE_ID));
                    itemDeviceBinding.executePendingBindings();
                    //holder.mPrice.setTextColor(Color.GREEN);
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public void removeItem(int position) {
        devices.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Device deletedModel, int deletedPosition) {
        devices.add(deletedPosition, deletedModel);
        notifyItemInserted(deletedPosition);
    }
}
