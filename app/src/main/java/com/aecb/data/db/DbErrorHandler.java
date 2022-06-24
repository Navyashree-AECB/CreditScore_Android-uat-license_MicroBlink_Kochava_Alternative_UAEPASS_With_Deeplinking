package com.aecb.data.db;

import android.database.sqlite.SQLiteConstraintException;

import timber.log.Timber;

public class DbErrorHandler {

    public static long handleDbError(Throwable error) {
        if (error instanceof SQLiteConstraintException) {
            Timber.e("handle constraint exception");
            return -1L;
        } else {
            Timber.e("error wasn't be handled");
            Timber.e(error);
            return -2L;
        }
    }
}
