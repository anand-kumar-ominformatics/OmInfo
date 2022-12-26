package com.ominfo.staff_original.database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ominfo.staff_original.ui.login.model.CompanyIdData;

import java.lang.reflect.Type;

public class CompanyIdConverter {
    @TypeConverter
    public static CompanyIdData fromString(String value) {
        Type listType = new TypeToken<CompanyIdData>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(CompanyIdData list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
