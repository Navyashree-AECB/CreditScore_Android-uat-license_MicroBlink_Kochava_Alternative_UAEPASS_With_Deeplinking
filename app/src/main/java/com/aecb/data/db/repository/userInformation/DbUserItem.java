package com.aecb.data.db.repository.userInformation;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public final class DbUserItem {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "mobile_number")
    private String mobile;

    @ColumnInfo(name = "email_id")
    private String email;

    @ColumnInfo(name = "device_id")
    private String deviceId;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "touch_id")
    private int touchId;

    @ColumnInfo(name = "app_language")
    private String appLanguage;

    @ColumnInfo(name = "tc_version")
    private float tcVersionNumber;

    @ColumnInfo(name = "last_login")
    private int lastLogin;

    public DbUserItem(String mobile, String email, String deviceId, String password, int touchId, String appLanguage, float tcVersionNumber, int lastLogin) {
        this.mobile = mobile;
        this.email = email;
        this.deviceId = deviceId;
        this.password = password;
        this.touchId = touchId;
        this.appLanguage = appLanguage;
        this.tcVersionNumber = tcVersionNumber;
        this.lastLogin = lastLogin;
    }

    @Ignore
    public DbUserItem() {
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppLanguage() {
        return appLanguage;
    }

    public void setAppLanguage(String appLanguage) {
        this.appLanguage = appLanguage;
    }

    public float getTcVersionNumber() {
        return tcVersionNumber;
    }

    public void setTcVersionNumber(float tcVersionNumber) {
        this.tcVersionNumber = tcVersionNumber;
    }

    public int getTouchId() {
        return touchId;
    }

    public void setTouchId(int touchId) {
        this.touchId = touchId;
    }

    public int getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(int lastLogin) {
        this.lastLogin = lastLogin;
    }
}