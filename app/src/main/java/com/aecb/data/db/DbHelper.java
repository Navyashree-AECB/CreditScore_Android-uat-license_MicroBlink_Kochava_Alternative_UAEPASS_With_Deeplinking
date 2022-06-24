package com.aecb.data.db;

import androidx.lifecycle.LiveData;

import com.aecb.data.db.repository.commonresponse.DbResponse;
import com.aecb.data.db.repository.notification.DbNotification;
import com.aecb.data.db.repository.userInformation.DbUserItem;
import com.aecb.data.db.repository.usertcversion.DBUserTC;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface DbHelper {

    Observable<Long> insertUser(DbUserItem dbUserItem);

    Observable<DbUserItem> getUserDetailFromMobile(String mobile);

    Observable<Long> updateDeviceId(String deviceId, String mobile);

    Observable<Long> updatePassword(String password, String mobile);

    Observable<Long> updateEmail(String email, String mobile);

    Observable<Long> updateUser(DbUserItem dbUserItem);

    Observable<List<DbUserItem>> getAllUsers();

    Observable<Long> insertUserWithTC(DBUserTC dbUserTC);

    Observable<DBUserTC> getUserWithTC(String userName);

    Observable<Long> updateUserTC(DBUserTC dbUserTC);

    Observable<Long> updateUserDeviceId(String userName, String deviceId);

    Observable<Long> updateUserPassword(String userName, String password);

    Observable<Long> updateTouchId(boolean touchEnable, String email);

    Observable<Integer> updateLastLoginUser(DBUserTC dbUserTC);

    Observable<DBUserTC> getLastLoginUser();

    Observable<Long> updateLasUserLogin(String userName);

    Observable<Long> updateLasUserLoginFalse(String userName);

    Observable<Long> insertNotification(DbNotification dbNotification);

    LiveData<List<DbNotification>> getAllNotification();

    Completable deleteNotification(int id);

    Observable<Long> readNotification(int id);

    Completable deleteAllNotification();

    Observable<Long> updateResponseIntoDb(DbResponse dbResponse);

    Observable<Long> updatePreferredPaymentMethod(String userName, String preferredPaymentMethod);

    Observable<Long> updateLanguage(String userName, String language);

    Observable<Long> updateUserDOB(String userName, String dob);

    Observable<Long> updateUserPassport(String userName, String passport);

    Observable<Long> updateIsUAEPassUser(String userName, int isUAEPassUser);
}
