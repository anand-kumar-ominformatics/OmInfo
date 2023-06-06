package com.ominfo.staff_original.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ominfo.staff_original.ui.dashboard.model.AttendanceDaysTable;
import com.ominfo.staff_original.ui.dashboard.model.GetUserRightsResult;
import com.ominfo.staff_original.ui.login.model.LoginResultTable;
import com.ominfo.staff_original.ui.login.model.LoginTable;
import com.ominfo.staff_original.ui.upload_pod.model.UploadPodRequest;

import java.util.List;


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


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UploadPodRequest products);



    @Query("DELETE from pds_details WHERE id= :id ")
    void deletePdsById(Integer id);


    @Query("SELECT * from pds_details WHERE gc_id= :GCID ")
    List<UploadPodRequest> getItemByGcId(String GCID);

    @Query("SELECT * FROM pds_details")
    List<UploadPodRequest> getPdsList();

    default void insertOrUpdate(UploadPodRequest item) {
        List<UploadPodRequest> itemsFromDB = getItemByGcId(item.getGcId());
        if (itemsFromDB.isEmpty())
        {
            insert(item);
        }
    }




    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GetUserRightsResult getUserRightsResult);

    @Query("DELETE from user_rights WHERE App_Button_Name= :id ")
    void deleteUserRightByButtonName(String id);

    @Query("SELECT * from user_rights WHERE id= :buttonId ")
    List<GetUserRightsResult> getItemByButtonId(String buttonId);

    @Query("SELECT * FROM user_rights")
    List<GetUserRightsResult> getUserRightsList();

    default void insertOrUpdate(GetUserRightsResult item) {
        List<GetUserRightsResult> itemsFromDB = getItemByButtonId(item.getAppButtonID());
        if (itemsFromDB.isEmpty())
        {
            insert(item);
        }
    }




}
