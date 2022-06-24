package com.aecb.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.aecb.AppConstants;
import com.aecb.data.db.repository.commonresponse.DbResponse;
import com.aecb.data.db.repository.commonresponse.ResponseDao;
import com.aecb.data.db.repository.notification.DbNotification;
import com.aecb.data.db.repository.notification.NotificationDao;
import com.aecb.data.db.repository.userInformation.DbUserItem;
import com.aecb.data.db.repository.userInformation.UserDao;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.data.db.repository.usertcversion.UserTCDao;
import com.aecb.typeconverter.ConfigurationDataTypeConverter;

@Database(entities = {DbUserItem.class, DBUserTC.class, DbNotification.class, DbResponse.class},
        version = AppConstants.DB_VERSION)
@TypeConverters(ConfigurationDataTypeConverter.class)

public abstract class MyAppDb extends RoomDatabase {
    public abstract UserDao userDao();

    public abstract UserTCDao userTCDao();

    public abstract ResponseDao responseDao();

    public abstract NotificationDao notificationDao();
}