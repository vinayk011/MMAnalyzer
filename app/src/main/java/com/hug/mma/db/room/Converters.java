package com.hug.mma.db.room;

import android.text.TextUtils;


import com.google.gson.reflect.TypeToken;
import com.hug.mma.db.room.entity.Device;
import com.hug.mma.db.room.entity.DeviceStats;
import com.hug.mma.db.room.entity.Ec;
import com.hug.mma.db.room.entity.Ph;
import com.hug.mma.network.GsonUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import androidx.room.TypeConverter;

/**
 * Created by Raviteja on 26-07-2017 for HugFit.
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date value) {
        if (value == null) {
            return null;
        } else {
            return value.getTime();
        }
    }

    @TypeConverter
    public static List<Device> stringToDeviceList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        return GsonUtil.getGson().fromJson(data, new TypeToken<List<Device>>() {
        }.getType());
    }

    @TypeConverter
    public static String DeviceListToString(List<Device> data) {
        return GsonUtil.getGson().toJson(data);
    }

    @TypeConverter
    public static List<DeviceStats> stringToDeviceStatsList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        return GsonUtil.getGson().fromJson(data, new TypeToken<List<DeviceStats>>() {
        }.getType());
    }

    @TypeConverter
    public static String DeviceStatsListToString(List<DeviceStats> data) {
        return GsonUtil.getGson().toJson(data);
    }

    @TypeConverter
    public static Ph stringToPh(String data) {
        if (data == null) {
            return new Ph();
        }
        return GsonUtil.getGson().fromJson(data, new TypeToken<Ph>() {
        }.getType());

    }

    @TypeConverter
    public static String phToString(Ph ph) {
        return GsonUtil.getGson().toJson(ph);
    }

    @TypeConverter
    public static Ec stringToEc(String data) {
        if (data == null) {
            return new Ec();
        }
        return GsonUtil.getGson().fromJson(data, new TypeToken<Ec>() {
        }.getType());

    }

    @TypeConverter
    public static String ecToString(Ec ec) {
        return GsonUtil.getGson().toJson(ec);
    }


}
