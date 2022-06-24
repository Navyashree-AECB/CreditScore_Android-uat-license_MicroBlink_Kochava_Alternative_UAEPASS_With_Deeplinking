package com.aecb.data.db.repository.usertcversion;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserTCDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertUser(DBUserTC dbUserItem);

    @Query("SELECT * from DBUserTC where userName =:userName")
    DBUserTC getUserWithTC(String userName);

    /**
     * Updating only terms & condition version
     */
//    @Query("UPDATE DBUserTC SET tcVersion = :tcVersion, email =:email,mobile =:mobile, lastUser =:lastUser where userName =:userName")
//    long updateUserTC(String userName, float tcVersion, String email, String mobile, int lastUser);

    /**
     * Updating only deviceId
     */
    @Query("UPDATE DBUserTC SET deviceId = :deviceId where userName =:userName")
    long updateUserDevice(String userName, String deviceId);

    @Query("UPDATE DBUserTC SET touchId = :touchId where email =:userName")
    long updateTouchId(String userName, boolean touchId);

    @Query("SELECT * from DBUserTC where lastUser = :lastLogin LIMIT 1")
    DBUserTC getLastLoginUserDetail(int lastLogin);

    @Update
    int updateUser(DBUserTC dbUserTC);

    @Query("UPDATE DBUserTC SET lastUser = 1, tcVersion = :tcVersion, email =:email,mobile =:mobile, fullName =:fullName, deviceId =:deviceId,emiratesId =:emiratesId, passport =:passport, dob=:dob,gender =:gender, lastName =:lastName,firstName =:firstName,middleName =:middleName,nationalityId =:nationalityId,password =:password where userName =:userName")
    long updateUserTC(String userName, int tcVersion, String mobile, String email, String fullName, String deviceId, String emiratesId, String passport, String dob, int gender, String lastName, String nationalityId, String firstName, String middleName, String password);

    @Query("UPDATE DBUserTC SET lastUser = 0 Where userName != :userName ")
    long updateLastUserFalse(String userName);

    @Query("UPDATE DBUserTC SET lastUser = 1 Where userName == :userName ")
    long updateLastUser(String userName);

    @Query("UPDATE DBUserTC SET password = :password where userName =:userName")
    long updateUserPassword(String userName, String password);

    @Query("UPDATE DBUserTC SET preferredPaymentMethod = :preferredPaymentMethod where email =:userName")
    long updatePreferredPaymentMethod(String userName, String preferredPaymentMethod);

    @Query("UPDATE DBUserTC SET preferredLanguage = :language where email =:userName")
    long updateLanguage(String userName, String language);

    @Query("UPDATE DBUserTC SET dob = :dob where userName =:userName")
    long updateUserDOB(String userName, String dob);

    @Query("UPDATE DBUserTC SET passport = :passport where email =:userName")
    long updateUserPassport(String userName, String passport);

    @Query("UPDATE DBUserTC SET isUAEPassUser = :isUAEPassUser where email =:userName")
    long updateIsUAEPassUser(String userName, int isUAEPassUser);

}