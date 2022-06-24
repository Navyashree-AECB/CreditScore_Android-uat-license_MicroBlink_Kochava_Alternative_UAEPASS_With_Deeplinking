package com.aecb.data.db.repository.commonresponse;

import io.reactivex.Observable;

public interface ResponseRepo {
    Observable<Long> updateResponseIntoDb(DbResponse dbResponse);

    Observable<DbResponse> getResponseFromTable(int id);
}