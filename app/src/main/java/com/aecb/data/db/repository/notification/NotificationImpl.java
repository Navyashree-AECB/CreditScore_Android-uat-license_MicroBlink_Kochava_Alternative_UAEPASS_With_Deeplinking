package com.aecb.data.db.repository.notification;

import androidx.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class NotificationImpl implements NotificationRepo {
    private NotificationDao notificationDao;

    @Inject
    public NotificationImpl(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }

    @Override
    public Observable<Long> insertNotification(DbNotification dbNotification) {
        return Observable.fromCallable(() -> notificationDao.insertNotification(dbNotification));
    }

    @Override
    public LiveData<List<DbNotification>> getAllNotification() {
        return new DbNotificationsViewModel(notificationDao).allNotifications();
    }

    @Override
    public Completable deleteNotification(int id) {
        return Completable.fromAction(() -> notificationDao.deleteNotification(id, true));
    }

    @Override
    public Observable<Long> readNotification(int id) {
        return Observable.fromCallable(() -> notificationDao.readNotification(id, true));
    }

    @Override
    public Completable deleteAllNotification() {
        return Completable.fromAction(() -> notificationDao.deleteAllNotification(true));
    }
}