package com.ominfo.staff_original.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ominfo.staff_original.ui.dashboard.model.AttendanceDaysTable;
import com.ominfo.staff_original.ui.login.model.LoginResultTable;
import com.ominfo.staff_original.ui.login.model.LoginTable;

@Database(entities = {LoginResultTable.class, AttendanceDaysTable.class}, version = 2, exportSchema = false)
@TypeConverters({JsonTypeConverter.class,CompanyIdConverter.class,DaysTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract DBDAO getDbDAO();


}
