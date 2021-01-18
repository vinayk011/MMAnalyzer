package com.hug.mma.db.room.dao;

import com.hug.mma.db.room.entity.DeviceStats;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DeviceStatsDao {
    @Query("SELECT * FROM DeviceStats WHERE device = :deviceId")
    List<DeviceStats> getAll(String deviceId);

    @Query("SELECT * FROM DeviceStats WHERE device = :deviceId")
    LiveData<List<DeviceStats>> getAllAsLive(String deviceId);

    @Query("SELECT * FROM DeviceStats WHERE recorded = :date AND device =:deviceId")
    List<DeviceStats> getBatchesForDateAndDevice(Date date, String deviceId);

    @Query("SELECT * FROM DeviceStats WHERE recorded = :date AND device =:deviceId")
    LiveData<List<DeviceStats>> getBatchesForDateAndDeviceAsLive(Date date, String deviceId);

    @Query("SELECT * FROM DeviceStats WHERE batch = :batchId")
    DeviceStats getBatchForDevice(String batchId);

    @Query("SELECT * FROM DeviceStats WHERE batch = :batchId AND device =:deviceId AND recorded= :date")
    DeviceStats getBatchForDeviceAndDate(String batchId, String deviceId, Date date);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(DeviceStats... deviceStats);

    @Update
    void update(DeviceStats deviceStats);

    @Delete
    void delete(DeviceStats deviceStats);

    @Query("DELETE FROM DeviceStats")
    public void emptyDeviceStats();


}
