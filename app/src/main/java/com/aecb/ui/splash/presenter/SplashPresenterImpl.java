package com.aecb.ui.splash.presenter;

import com.aecb.base.db.DbDisposableObserver;
import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.data.DataManager;
import com.aecb.data.db.repository.notification.DbNotification;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.google.firebase.iid.FirebaseInstanceId;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public final class SplashPresenterImpl extends MvpBasePresenterImpl<SplashContract.View>
        implements SplashContract.Presenter {

    private DataManager mDataManager;
    private DBUserTC lastUserDetail;

    @Inject
    public SplashPresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void saveFCMToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Timber.w("getInstanceId failed", task.getException());
                        return;
                    }
                    String token = task.getResult().getToken();
                    mDataManager.setFcmId(token);
                });
    }

    @Override
    public boolean isFirstTime() {
        return mDataManager.isFirstTime();
    }

    /**
     * get last login user detail from local database
     */
    @Override
    public void getLastLoginUser() {
        ifViewAttached(view -> {
            DbDisposableObserver<DBUserTC> myAppDisposableObserver = new
                    DbDisposableObserver<DBUserTC>() {
                        @Override
                        protected void onSuccess(Object response) {
                            lastUserDetail = (DBUserTC) response;
                            view.insertNotificationIntoDb();
                        }
                    };

            DbDisposableObserver<DBUserTC> disposableObserver = dataManager.getLastLoginUser()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    @Override
    public String getUserName() {
        return lastUserDetail.getUserName().toLowerCase();
    }

    @Override
    public void insertNotification(DbNotification notification) {
        DbDisposableObserver<Long> myAppDisposableObserver = new
                DbDisposableObserver<Long>() {
                    @Override
                    protected void onSuccess(Object response) {
                    }

                };
        DbDisposableObserver<Long> disposableObserver = dataManager.insertNotification(notification)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(myAppDisposableObserver);
        compositeDisposable.add(disposableObserver);
    }

}