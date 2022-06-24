package com.aecb.services;

import com.aecb.App;
import com.aecb.base.db.DbDisposableObserver;
import com.aecb.data.db.repository.notification.DbNotification;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.util.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.schedulers.Schedulers;


public final class MyFirebaseMessagingService extends FirebaseMessagingService {

    DbNotification dbNotification;
    String email = "";
    String title = "";
    String body = "";
    Date currentDate = Calendar.getInstance().getTime();
    DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
    String formattedDate = df.format(currentDate);
    String formattedTime = dateFormat.format(currentDate);

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle();
            body = remoteMessage.getNotification().getBody();
        }
        getUserEmail();
        NotificationUtils.instance.postNotification(title, body);
    }

    private void getUserEmail() {
        DbDisposableObserver<DBUserTC> myAppDisposableObserver = new
                DbDisposableObserver<DBUserTC>() {
                    @Override
                    protected void onSuccess(Object response) {
                        DBUserTC dbUserItem = (DBUserTC) response;
                        email = dbUserItem.getUserName();
                        dbNotification = new DbNotification(title, body, formattedDate, formattedTime, email,
                                false, false);
                        DbDisposableObserver<Long> myAppDisposableObserver = new DbDisposableObserver<Long>() {
                            @Override
                            protected void onSuccess(Object response) {
                            }
                        };
                        App.getAppComponent().getDataManager().insertNotification(dbNotification)
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .subscribeWith(myAppDisposableObserver);
                    }
                };

        App.getAppComponent().getDataManager().getLastLoginUser()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(myAppDisposableObserver);
    }
}