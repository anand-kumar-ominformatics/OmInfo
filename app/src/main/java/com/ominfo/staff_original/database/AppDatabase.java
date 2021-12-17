package com.ominfo.staff_original.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ominfo.staff_original.ui.login.model.LoginResultTable;

@Database(entities = {LoginResultTable.class}, version = 2, exportSchema = false)
@TypeConverters({JsonTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract DBDAO getDbDAO();


}
