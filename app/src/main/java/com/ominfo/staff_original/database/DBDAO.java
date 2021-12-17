package com.ominfo.staff_original.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ominfo.staff_original.ui.login.model.LoginResultTable;


@Dao
public interface DBDAO {

    // For login Data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLoginData(LoginResultTable loginResultTable);

    @Query("SELECT * FROM table_login")
    LoginResultTable getLoginData();

    @Query("DELETE FROM table_login")
    void deleteLogin();

}
