package com.aecb.data.db.repository.commonresponse;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

@Dao
public abstract class ResponseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract Long insertIgnore(DbResponse entity);

    @Transaction
    Long updateResponseIntoDb(DbResponse dbResponse) {
        if (insertIgnore(dbResponse) != -1L) {
            return insertIgnore(dbResponse);
        } else {
            update(dbResponse);
            return 1L;
        }
    }

    @Update
    abstract void update(DbResponse dbResponse);

    @Query("SELECT * FROM Responses WHERE responseId = :id")
    abstract DbResponse getResponseFromTable(int id);
}