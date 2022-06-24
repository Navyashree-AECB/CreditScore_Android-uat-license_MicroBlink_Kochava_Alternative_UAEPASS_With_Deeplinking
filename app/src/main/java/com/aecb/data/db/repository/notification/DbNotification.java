package com.aecb.data.db.repository.notification;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notification")
public final class DbNotification {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "notificationTitle")
    private String notificationTitle;
    @ColumnInfo(name = "notificationBody")
    private String notificationBody;
    @ColumnInfo(name = "notificationDate")
    private String notificationDate;
    @ColumnInfo(name = "notificationTime")
    private String notificationTime;
    @ColumnInfo(name = "email")
    private String email;
    @ColumnInfo(name = "isRead")
    private boolean isRead = false;
    @ColumnInfo(name = "isDeleted")
    private boolean isDeleted = false;

    public DbNotification(String notificationTitle, String notificationBody, String notificationDate,
                          String notificationTime, String email, boolean isRead, boolean isDeleted) {
        this.notificationTitle = notificationTitle;
        this.notificationBody = notificationBody;
        this.notificationDate = notificationDate;
        this.notificationTime = notificationTime;
        this.email = email;
        this.isRead = isRead;
        this.isDeleted = isDeleted;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }

    public String getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(String notificationTime) {
        this.notificationTime = notificationTime;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}