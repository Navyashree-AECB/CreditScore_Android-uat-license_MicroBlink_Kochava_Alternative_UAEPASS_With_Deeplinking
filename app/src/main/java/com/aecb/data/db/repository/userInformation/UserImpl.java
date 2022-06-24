package com.aecb.data.db.repository.userInformation;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public final class UserImpl implements UserRepo {

    private UserDao userDao;

    @Inject
    public UserImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Observable<List<DbUserItem>> getAllUsers() {
        return Observable.fromCallable(() -> userDao.getAllUsers());
    }

    @Override
    public Observable<Long> insertUser(DbUserItem dbUserItem) {
        return Observable.fromCallable(() -> userDao.insertUser(dbUserItem));
    }

    @Override
    public Observable<Long> updateUser(DbUserItem dbUserItem) {
        return Observable.fromCallable(() -> null);
    }

    @Override
    public Observable<DbUserItem> getUserDetailFromMobile(String mobile) {
        return Observable.fromCallable(() -> userDao.getUserDetailFromMobile(mobile));
    }

    @Override
    public Observable<Long> updateDeviceId(String deviceId, String mobile) {
        return Observable.fromCallable(() -> userDao.updateDeviceId(deviceId, mobile));
    }

    @Override
    public Observable<Long> updatePassword(String password, String mobile) {
        return Observable.fromCallable(() -> userDao.updatePassword(password, mobile));
    }

    @Override
    public Observable<Long> updateEmail(String email, String mobile) {
        return Observable.fromCallable(() -> userDao.updateEmail(email, mobile));
    }

    @Override
    public Observable<Long> updateTouchId(int touchId, String mobile) {
        return Observable.fromCallable(() -> userDao.updateTouchId(touchId, mobile));
    }

    @Override
    public Observable<Long> updateFaceId(int faceId, String mobile) {
        return Observable.fromCallable(() -> null);
    }

    @Override
    public Observable<Long> updateUserLastLogin(int lastLogin, String mobile) {
        return Observable.fromCallable(() -> userDao.updateLastLogin(lastLogin, mobile));
    }

    @Override
    public Observable<Long> updateMobileNumber(String mobile, String email) {
        return Observable.fromCallable(() -> userDao.updateMobileNumber(mobile, email));
    }

    @Override
    public Observable<DbUserItem> getLastLoginUserDetail(int lastLogin) {
        return Observable.fromCallable(() -> userDao.getLastLoginUserDetail(lastLogin));
    }

    @Override
    public Observable<Long> updateUserAppLang(String mAppLang, String mobile) {
        return Observable.fromCallable(() -> userDao.updateAppLang(mAppLang, mobile));
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}