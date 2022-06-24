package com.aecb.data.db.repository.usertcversion;

import javax.inject.Inject;

import io.reactivex.Observable;

public final class UserTCImpl implements UserTCRepo {
    private UserTCDao userTCDao;

    @Inject
    public UserTCImpl(UserTCDao userTCDao) {
        this.userTCDao = userTCDao;
    }

    @Override
    public Observable<DBUserTC> getUserWithTC(String userName) {
        return Observable.fromCallable(() -> userTCDao.getUserWithTC(userName));
    }

    @Override
    public Observable<Long> insertUser(DBUserTC dbUserTC) {
        return Observable.fromCallable(() -> userTCDao.insertUser(dbUserTC));
    }

    @Override
    public Observable<Long> updateUserTC(DBUserTC dbUserTC) {
        return Observable.fromCallable(() ->
                userTCDao.updateUserTC(dbUserTC.getUserName(), dbUserTC.getTcVersion(), dbUserTC.getMobile(), dbUserTC.getEmail(),
                        dbUserTC.getFullName(), dbUserTC.getDeviceId(), dbUserTC.getEmiratesId(),
                        dbUserTC.getPassport(), dbUserTC.getDob(), dbUserTC.getGender(), dbUserTC.getLastName(), dbUserTC.getNationalityId(), dbUserTC.getFirstName(), dbUserTC.getMiddleName(), dbUserTC.getPassword()));
    }

    @Override
    public Observable<Long> updateUserDeviceId(String userName, String deviceId) {
        return Observable.fromCallable(() -> userTCDao.updateUserDevice(userName, deviceId));
    }

    @Override
    public Observable<Long> updateTouchId(boolean touchID, String email) {
        return Observable.fromCallable(() -> userTCDao.updateTouchId(email, touchID));
    }

    @Override
    public Observable<DBUserTC> getLastUser() {
        return Observable.fromCallable(() -> userTCDao.getLastLoginUserDetail(1));
    }

    @Override
    public Observable<Integer> updateUser(DBUserTC dbUserTC) {
        return Observable.fromCallable(() -> userTCDao.updateUser(dbUserTC));
    }

    public UserTCDao getUserDao() {
        return userTCDao;
    }

    public void setUserDao(UserTCDao userDao) {
        this.userTCDao = userDao;
    }

    @Override
    public Observable<Long> updateLasUserLogin(String userName) {
        return Observable.fromCallable(() -> userTCDao.updateLastUser(userName));
    }

    @Override
    public Observable<Long> updateUserPassword(String userName, String password) {
        return Observable.fromCallable(() -> userTCDao.updateUserPassword(userName, password));
    }

    @Override
    public Observable<Long> updateLasUserLoginFalse(String userName) {
        return Observable.fromCallable(() -> userTCDao.updateLastUserFalse(userName));
    }

    @Override
    public Observable<Long> updatePreferredPaymentMethod(String userName, String preferredPaymentMethod) {
        return Observable.fromCallable(() -> userTCDao.updatePreferredPaymentMethod(userName, preferredPaymentMethod));
    }

    @Override
    public Observable<Long> updateLanguage(String userName, String language) {
        return Observable.fromCallable(() -> userTCDao.updateLanguage(userName, language));
    }

    @Override
    public Observable<Long> updateUserDOB(String userName, String dob) {
        return Observable.fromCallable(() -> userTCDao.updateUserDOB(userName, dob));
    }

    @Override
    public Observable<Long> updateUserPassport(String userName, String passport) {
        return Observable.fromCallable(() -> userTCDao.updateUserPassport(userName, passport));
    }

    @Override
    public Observable<Long> updateIsUAEPassUser(String userName, int isUAEPassUser) {
        return Observable.fromCallable(() -> userTCDao.updateIsUAEPassUser(userName, isUAEPassUser));
    }
}
