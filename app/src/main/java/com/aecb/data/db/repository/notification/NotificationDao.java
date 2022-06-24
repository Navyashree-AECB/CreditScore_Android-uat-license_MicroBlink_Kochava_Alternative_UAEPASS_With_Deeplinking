package com.aecb.data.db.repository.notification;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertNotification(DbNotification dbNotification);

    @Query("SELECT * from notification WHERE isDeleted =:isDeleted")
    LiveData<List<DbNotification>> getAllNotification(boolean isDeleted);

    @Query("UPDATE notification SET isDeleted = :isDeleted where id =:id")
    void deleteNotification(int id, boolean isDeleted);

    @Query("UPDATE notification SET isRead = :isRead where id =:id")
    long readNotification(int id, boolean isRead);

    @Query("UPDATE notification SET isDeleted = :isDeleted")
    void deleteAllNotification(boolean isDeleted);
}