package com.aecb.data.db.repository.userInformation;

import java.util.List;

import io.reactivex.Observable;

public interface UserRepo {

    Observable<List<DbUserItem>> getAllUsers();

    Observable<Long> insertUser(DbUserItem dbUserItem);

    Observable<Long> updateUser(DbUserItem dbUserItem);

    Observable<DbUserItem> getUserDetailFromMobile(String mobile);

    Observable<Long> updateDeviceId(String deviceId, String mobile);

    Observable<Long> updatePassword(String password, String mobile);

    Observable<Long> updateEmail(String email, String mobile);

    Observable<Long> updateTouchId(int touchId, String mobile);

    Observable<Long> updateFaceId(int faceId, String mobile);

    Observable<Long> updateUserLastLogin(int lastLogin, String mobile);

    Observable<Long> updateMobileNumber(String mobile, String email);

    Observable<DbUserItem> getLastLoginUserDetail(int lastLogin);

    Observable<Long> updateUserAppLang(String mAppLang, String mobile);
}
