package com.aecb.data.api.models.notification;

import com.aecb.data.api.models.BaseResponse;

import java.io.Serializable;

public class NotificationData extends BaseResponse implements Serializable {

    int id;
    String notificationTitle;
    String notificationBody;
    String date;
    boolean isRead;

    public NotificationData(int id, String notificationTitle, String notificationBody,
                            String date, boolean isRead) {
        this.id = id;
        this.notificationTitle = notificationTitle;
        this.notificationBody = notificationBody;
        this.date = date;
        this.isRead = isRead;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationBody() {
        return notificationBody;
    }

    public void setNotificationBody(String notificationBody) {
        this.notificationBody = notificationBody;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

}