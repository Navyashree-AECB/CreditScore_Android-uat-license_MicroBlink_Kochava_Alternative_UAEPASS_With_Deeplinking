package com.aecb.data.db.repository.commonresponse;

import javax.inject.Inject;

import io.reactivex.Observable;

public class ResponseRepoImpl implements ResponseRepo {

    private ResponseDao responseDao;

    @Inject
    public ResponseRepoImpl(ResponseDao responseDao) {
        this.responseDao = responseDao;
    }

    public ResponseDao getResponseDao() {
        return responseDao;
    }

    public void seResponseDao(ResponseDao responseDao) {
        this.responseDao = responseDao;
    }

    @Override
    public Observable<Long> updateResponseIntoDb(DbResponse dbResponse) {
        return Observable.fromCallable(() -> responseDao.updateResponseIntoDb(dbResponse));
    }

    @Override
    public Observable<DbResponse> getResponseFromTable(int id) {
        return Observable.fromCallable(() ->
                responseDao.getResponseFromTable(id)
        );
    }
}