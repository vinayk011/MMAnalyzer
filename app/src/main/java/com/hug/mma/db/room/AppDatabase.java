package com.hug.mma.db.room;


import android.content.Context;

import com.hug.mma.db.room.dao.DeviceStatsDao;
import com.hug.mma.db.room.dao.DevicesDao;
import com.hug.mma.db.room.entity.DeviceStats;
import com.hug.mma.db.room.entity.DevicesInfo;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {DevicesInfo.class, DeviceStats.class}, version = 2)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public static AppDatabase getDatabase(Context context) {
        try {
            if (instance == null) {
                instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "hug_mma")
                        .addMigrations(MIGRATION_1_2)
                        .build();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return instance;
    }

    public abstract DevicesDao devicesDao();

    public abstract DeviceStatsDao deviceStatsDao();

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE DeviceStats ADD COLUMN startTime REAL");
            database.execSQL("ALTER TABLE DeviceStats ADD COLUMN endTime REAL");
        }
    };
}
