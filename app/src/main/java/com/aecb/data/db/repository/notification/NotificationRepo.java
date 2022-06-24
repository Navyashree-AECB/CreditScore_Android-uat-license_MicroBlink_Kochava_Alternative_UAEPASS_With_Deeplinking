package com.aecb.data.db.repository.notification;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface NotificationRepo {
    Observable<Long> insertNotification(DbNotification dbNotification);

    LiveData<List<DbNotification>> getAllNotification();

    Completable deleteAllNotification();

    Completable deleteNotification(int id);

    Observable<Long> readNotification(int id);
}