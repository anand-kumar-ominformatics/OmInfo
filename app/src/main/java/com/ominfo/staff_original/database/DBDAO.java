package com.ominfo.staff_original.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ominfo.staff_original.ui.dashboard.model.AttendanceDaysTable;
import com.ominfo.staff_original.ui.login.model.LoginResultTable;
import com.ominfo.staff_original.ui.login.model.LoginTable;


@Dao
public interface DBDAO {

    // For login Data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLoginData(LoginResultTable loginResultTable);

    @Query("SELECT * FROM table_login")
    LoginResultTable getLoginData();

  /*  @Query("SELECT * FROM table_login")
    LoginResultTable getAttendanceLoginData();*/

    @Query("DELETE FROM table_login")
    void deleteLogin();
    // For login Attendance test Data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAttendanceData(AttendanceDaysTable attendanceDaysTable);

    @Query("DELETE FROM test_attendance_data")
    void deleteAttendanceData();
}
