package com.ominfo.staff_original.database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ominfo.staff_original.ui.dashboard.model.DayData;

import java.lang.reflect.Type;

public class DaysTypeConverter {
    @TypeConverter
    public static DayData fromString(String value) {
        Type listType = new TypeToken<DayData>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(DayData list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
