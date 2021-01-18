package com.hug.mma.listener;

import android.os.Bundle;

import com.hug.mma.constants.AppConstants;
import com.hug.mma.db.room.entity.Device;
import com.hug.mma.preference.AppPrefConstants;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import static com.hug.mma.constants.IntentConstants.DEVICE_ID;
import static com.hug.mma.constants.IntentConstants.DEVICE_NAME;

public class DeviceDiffUtilCallback extends DiffUtil.Callback {
    ArrayList<Device> newList;
    ArrayList<Device> oldList;

    public DeviceDiffUtilCallback(ArrayList<Device> newList, ArrayList<Device> oldList) {
        this.newList = newList;
        this.oldList = oldList;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Device newModel = newList.get(newItemPosition);
        Device oldModel = oldList.get(oldItemPosition);

        Bundle diff = new Bundle();
        if (!newModel.getDevice().equals(oldModel.getDevice())
                || !newModel.getName().equals(oldModel.getName())) {
            diff.putSerializable(DEVICE_ID, newModel);
        }
        if (diff.size() == 0) {
            return null;
        }
        return diff;
    }

    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return newList.get(newItemPosition).getDevice().equals(oldList.get(oldItemPosition).getDevice());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        int result = newList.get(newItemPosition).compareTo(oldList.get(oldItemPosition));
        return result == 0;
    }
}
