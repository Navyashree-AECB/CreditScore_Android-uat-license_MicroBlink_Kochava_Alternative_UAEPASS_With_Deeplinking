package com.aecb.data.db;

import androidx.lifecycle.LiveData;

import com.aecb.data.db.repository.commonresponse.DbResponse;
import com.aecb.data.db.repository.commonresponse.ResponseRepo;
import com.aecb.data.db.repository.notification.DbNotification;
import com.aecb.data.db.repository.notification.NotificationRepo;
import com.aecb.data.db.repository.userInformation.DbUserItem;
import com.aecb.data.db.repository.userInformation.UserRepo;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.data.db.repository.usertcversion.UserTCRepo;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

public final class DbHelperImpl implements DbHelper {
    private UserRepo userRepo;
    private UserTCRepo userTCRepo;
    private NotificationRepo notificationRepo;
    private ResponseRepo responseRepo;

    @Inject
    public DbHelperImpl(UserRepo userRepo, UserTCRepo userTCRepo, ResponseRepo responseRepo,
                        NotificationRepo notificationRepo) {
        this.userRepo = userRepo;
        this.userTCRepo = userTCRepo;
        this.responseRepo = responseRepo;
        this.notificationRepo = notificationRepo;
    }

    @Override
    public Observable<Long> insertUser(DbUserItem dbUserItem) {
        return this.userRepo.insertUser(dbUserItem);
    }

    @Override
    public Observable<DbUserItem> getUserDetailFromMobile(String mobile) {
        return this.userRepo.getUserDetailFromMobile(mobile);
    }

    @Override
    public Observable<Long> updateDeviceId(String deviceId, String mobile) {
        return this.userRepo.updateDeviceId(deviceId, mobile);
    }

    @Override
    public Observable<Long> updatePassword(String password, String mobile) {
        return this.userRepo.updatePassword(password, mobile);
    }

    @Override
    public Observable<Long> updateEmail(String email, String mobile) {
        return this.userRepo.updateEmail(email, mobile);
    }

    @Override
    public Observable<Long> updateUser(DbUserItem dbUserItem) {
        return this.userRepo.updateUser(dbUserItem);
    }

    @Override
    public Observable<List<DbUserItem>> getAllUsers() {
        return this.userRepo.getAllUsers();
    }

    @Override
    public Observable<Long> insertUserWithTC(DBUserTC dbUserTC) {
        return this.userTCRepo.insertUser(dbUserTC);
    }

    @Override
    public Observable<DBUserTC> getUserWithTC(String userName) {
        return this.userTCRepo.getUserWithTC(userName);
    }

    @Override
    public Observable<Long> updateUserTC(DBUserTC dbUserTC) {
        return this.userTCRepo.updateUserTC(dbUserTC);
    }

    @Override
    public Observable<Long> updateUserDeviceId(String userName, String deviceId) {
        return this.userTCRepo.updateUserDeviceId(userName, deviceId);
    }

    @Override
    public Observable<Long> updateUserPassword(String userName, String password) {
        return this.userTCRepo.updateUserPassword(userName, password);
    }

    @Override
    public Observable<Long> updateTouchId(boolean touchID, String email) {
        return this.userTCRepo.updateTouchId(touchID, email);
    }

    @Override
    public Observable<Integer> updateLastLoginUser(DBUserTC dbUserTC) {
        return this.userTCRepo.updateUser(dbUserTC);
    }

    @Override
    public Observable<DBUserTC> getLastLoginUser() {
        return this.userTCRepo.getLastUser();
    }

    @Override
    public Observable<Long> updateLasUserLogin(String userName) {
        return userTCRepo.updateLasUserLogin(userName);
    }

    @Override
    public Observable<Long> updateLasUserLoginFalse(String userName) {
        return userTCRepo.updateLasUserLoginFalse(userName);
    }

    @Override
    public Observable<Long> insertNotification(DbNotification dbNotification) {
        return this.notificationRepo.insertNotification(dbNotification);
    }

    @Override
    public LiveData<List<DbNotification>> getAllNotification() {
        return this.notificationRepo.getAllNotification();
    }

    @Override
    public Completable deleteNotification(int id) {
        return this.notificationRepo.deleteNotification(id);
    }

    @Override
    public Observable<Long> readNotification(int id) {
        return this.notificationRepo.readNotification(id);
    }

    @Override
    public Completable deleteAllNotification() {
        return this.notificationRepo.deleteAllNotification();
    }

    @Override
    public Observable<Long> updateResponseIntoDb(DbResponse dbResponse) {
        return responseRepo.updateResponseIntoDb(dbResponse);
    }

    @Override
    public Observable<Long> updatePreferredPaymentMethod(String userName, String preferredPaymentMethod) {
        return userTCRepo.updatePreferredPaymentMethod(userName, preferredPaymentMethod);
    }

    @Override
    public Observable<Long> updateLanguage(String userName, String language) {
        return userTCRepo.updateLanguage(userName, language);
    }

    @Override
    public Observable<Long> updateUserDOB(String userName, String dob) {
        return userTCRepo.updateUserDOB(userName, dob);
    }

    @Override
    public Observable<Long> updateUserPassport(String userName, String passport) {
        return userTCRepo.updateUserPassport(userName, passport);
    }

    @Override
    public Observable<Long> updateIsUAEPassUser(String userName, int isUAEPassUser) {
        return userTCRepo.updateIsUAEPassUser(userName, isUAEPassUser);
    }
}