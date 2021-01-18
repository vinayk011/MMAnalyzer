package com.hug.mma.util;

import com.hug.mma.db.room.entity.DeviceStats;

import java.util.Comparator;

public class TimeComparator implements Comparator<DeviceStats> {

    @Override
    public int compare(DeviceStats stats1, DeviceStats stats2) {
        return (stats1.getStartTime() < stats2.getStartTime()) ? -1 : (stats1.getStartTime() > stats2.getStartTime()) ? 1 : 0;
    }
}
