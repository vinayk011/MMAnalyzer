package com.hug.mma.db.room.dao;

import com.hug.mma.db.room.entity.Device;
import com.hug.mma.db.room.entity.DevicesInfo;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DevicesDao {

    @Query("SELECT * FROM DevicesInfo")
    List<DevicesInfo> getAll();

    @Query("SELECT * FROM DevicesInfo WHERE userId =:userId ")
    DevicesInfo getDeviceByID(String userId);

    @Query("SELECT * FROM DevicesInfo WHERE userId =:userId ")
    LiveData<DevicesInfo> getDevicesByIDAsLive(String userId);

    @Query("SELECT * FROM DevicesInfo")
    LiveData<List<DevicesInfo>> getAllAsLive();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(DevicesInfo... devicesInfos);

    @Update
    void update(DevicesInfo devicesInfo);

    @Query("DELETE FROM DevicesInfo")
    public void emptyDevices();
}
