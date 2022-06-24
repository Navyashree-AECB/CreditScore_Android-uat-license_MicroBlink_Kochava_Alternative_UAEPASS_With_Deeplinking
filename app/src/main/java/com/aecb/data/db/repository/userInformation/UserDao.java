package com.aecb.data.db.repository.userInformation;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertUser(DbUserItem dbUserItem);

    @Query("SELECT * from user where mobile_number = :mobile LIMIT 1")
    DbUserItem getUserDetailFromMobile(String mobile);

    /**
     * Updating only device Id
     */
    @Query("UPDATE user SET device_id = :deviceId where mobile_number =:mobile")
    long updateDeviceId(String deviceId, String mobile);


    /**
     * Updating only password
     */
    @Query("UPDATE user SET password = :password where mobile_number =:mobile")
    long updatePassword(String password, String mobile);


    /**
     * Updating only Email
     */
    @Query("UPDATE user SET email_id = :email where mobile_number =:mobile")
    long updateEmail(String email, String mobile);


    /**
     * Updating only touchId
     */
    @Query("UPDATE user SET touch_id = :touchId where mobile_number =:mobile")
    long updateTouchId(int touchId, String mobile);


    /**
     * Updating only lastLogin
     */
    @Query("UPDATE user SET last_login = :lastLogin where mobile_number =:mobile")
    long updateLastLogin(int lastLogin, String mobile);

    /**
     * Updating only MobileNumber
     */
    @Query("UPDATE user SET mobile_number = :mobileNumber where email_id =:email")
    long updateMobileNumber(String mobileNumber, String email);

    @Query("SELECT * from user")
    List<DbUserItem> getAllUsers();

    @Query("SELECT * from user where last_login = :lastLogin LIMIT 1")
    DbUserItem getLastLoginUserDetail(int lastLogin);

    /**
     * Updating only User App Lang
     */
    @Query("UPDATE user SET app_language = :mAppLang where mobile_number =:mobile")
    long updateAppLang(String mAppLang, String mobile);

}