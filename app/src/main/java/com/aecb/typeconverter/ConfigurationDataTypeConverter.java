package com.aecb.typeconverter;

import androidx.room.TypeConverter;

import com.aecb.data.api.models.settings.ConfigurationData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ConfigurationDataTypeConverter {
    @TypeConverter
    public static ConfigurationData storedStringToMyObjects(String data) {
        Gson gson = new Gson();
        if (data == null) return new ConfigurationData();
        Type listType = new TypeToken<ConfigurationData>() {
        }.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String myObjectsToStoredString(ConfigurationData myObjects) {
        Gson gson = new Gson();
        return gson.toJson(myObjects);
    }
}