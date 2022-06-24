package com.aecb.data.db.repository.usertcversion;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "DBUserTC")
public final class DBUserTC {
    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "userName")
    private String userName;

    @ColumnInfo(name = "tcVersion")
    private int tcVersion;

    @ColumnInfo(name = "deviceId")
    private String deviceId;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "touchId")
    private boolean touchId = false;

    @ColumnInfo(name = "lastUser")
    private int lastUser;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "preferredLanguage")
    private String preferredLanguage;

    @ColumnInfo(name = "gender")
    private int gender;

    @ColumnInfo(name = "nationalityId")
    private String nationalityId;

    @ColumnInfo(name = "mobile")
    private String mobile;

    @ColumnInfo(name = "firstName")
    private String firstName;

    @ColumnInfo(name = "middleName")
    private String middleName;

    @ColumnInfo(name = "lastName")
    private String lastName;

    @ColumnInfo(name = "tcVersionNumber")
    private int tcVersionNumber;

    @ColumnInfo(name = "passport")
    private String passport;

    @ColumnInfo(name = "dob")
    private String dob;

    @ColumnInfo(name = "emiratesId")
    private String emiratesId;

    @ColumnInfo(name = "fullName")
    private String fullName;

    @ColumnInfo(name = "preferredPaymentMethod")
    private String preferredPaymentMethod;

    @ColumnInfo(name = "isUAEPassUser")
    private int isUAEPassUser = 0;

    public int getIsUAEPassUser() {
        return isUAEPassUser;
    }

    public void setIsUAEPassUser(int isUAEPassUser) {
        this.isUAEPassUser = isUAEPassUser;
    }

    public String getPreferredPaymentMethod() {
        return preferredPaymentMethod;
    }

    public void setPreferredPaymentMethod(String preferredPaymentMethod) {
        this.preferredPaymentMethod = preferredPaymentMethod;
    }

    public DBUserTC(String userName, int tcVersion, String deviceId, String password,
                    boolean touchId, int lastUser) {
        this.userName = userName;
        this.tcVersion = tcVersion;
        this.deviceId = deviceId;
        this.password = password;
        this.touchId = touchId;
        this.lastUser = lastUser;
        this.email = userName;
    }

    @Ignore
    public DBUserTC() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getNationalityId() {
        return nationalityId;
    }

    public void setNationalityId(String nationalityId) {
        this.nationalityId = nationalityId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getTcVersionNumber() {
        return tcVersionNumber;
    }

    public void setTcVersionNumber(int tcVersionNumber) {
        this.tcVersionNumber = tcVersionNumber;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmiratesId() {
        return emiratesId;
    }

    public void setEmiratesId(String emiratesId) {
        this.emiratesId = emiratesId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getTcVersion() {
        return tcVersion;
    }

    public void setTcVersion(int tcVersion) {
        this.tcVersion = tcVersion;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isTouchId() {
        return touchId;
    }

    public void setTouchId(boolean touchId) {
        this.touchId = touchId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLastUser() {
        return lastUser;
    }

    public void setLastUser(int lastUser) {
        this.lastUser = lastUser;
    }
}