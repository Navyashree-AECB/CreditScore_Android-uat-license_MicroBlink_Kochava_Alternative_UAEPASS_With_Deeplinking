package com.aecb.data.db.repository.notification;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

public class DbNotificationsViewModel extends ViewModel {
    private NotificationDao notificationDao;

    @Inject
    public DbNotificationsViewModel(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }

    public LiveData<List<DbNotification>> allNotifications() {
        return notificationDao.getAllNotification(false);
    }
}