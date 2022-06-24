package com.aecb.data.db.repository.usertcversion;

import io.reactivex.Observable;

public interface UserTCRepo {

    Observable<DBUserTC> getUserWithTC(String userName);

    Observable<Long> insertUser(DBUserTC dbUserItem);

    Observable<Long> updateUserTC(DBUserTC dbUserTC);

    Observable<Long> updateUserDeviceId(String userName, String deviceId);

    Observable<Long> updateTouchId(boolean touchID, String email);

    Observable<DBUserTC> getLastUser();

    Observable<Integer> updateUser(DBUserTC dbUserTC);

    Observable<Long> updateLasUserLogin(String userName);

    Observable<Long> updateLasUserLoginFalse(String userName);

    Observable<Long> updateUserPassword(String userName, String password);

    Observable<Long> updatePreferredPaymentMethod(String userName, String preferredPaymentMethod);

    Observable<Long> updateLanguage(String userName, String language);

    Observable<Long> updateUserDOB(String userName, String dob);

    Observable<Long> updateUserPassport(String userName, String passport);

    Observable<Long> updateIsUAEPassUser(String userName, int isUAEPassUser);
}