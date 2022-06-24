package com.aecb.ui.notification.presenter;

import com.aecb.base.db.DbDisposableObserver;
import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.data.DataManager;
import com.aecb.data.api.models.notification.NotificationData;
import com.aecb.data.db.repository.notification.DbNotification;
import com.aecb.data.db.repository.usertcversion.DBUserTC;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.aecb.AppConstants.DateFormats.DATE_MONTH_YEAR_FORMAT;
import static com.aecb.util.Utilities.isNullOrEmpty;

public class NotificationPresenterImpl extends MvpBasePresenterImpl<NotificationContract.View>
        implements NotificationContract.Presenter {

    private DataManager mDataManager;
    private String email;

    @Inject
    public NotificationPresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getNotificationData() {
        getLastUserDetail();
        try {
            ifViewAttached(view -> {
                mDataManager.getAllNotification().observe(view.getLifeCycleOwner(), list -> {
                    List<NotificationData> rvNotificationDataList = new ArrayList<>();
                    int count = 0;
                    List<DbNotification> lst = list;
                    for (int i = 0; i <= lst.size() - 1; i++) {
                        DbNotification item = lst.get(i);
                        if (!item.getNotificationDate().isEmpty() && !isNullOrEmpty(item.getEmail()) &&
                                item.getEmail().equals(email)) {
                            NotificationData nData = new NotificationData(item.getId(), item.getNotificationTitle(), item.getNotificationBody(),
                                    item.getNotificationDate() + " " + item.getNotificationTime(),
                                    item.isRead());
                            rvNotificationDataList.add(nData);
                            if (!lst.get(i).isRead()) {
                                count = count + 1;
                            }
                        }
                    }
                    view.loadNotification(rvNotificationDataList);
                });
            });

        } catch (Exception e) {

        }
    }

    @Override
    public void deleteNotification(int id) {
        ifViewAttached(view -> {
            CompletableObserver observer = new CompletableObserver() {

                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onComplete() {
                }

                @Override
                public void onError(Throwable throwable) {
                }
            };

            mDataManager.deleteNotification(id)
                    .subscribeOn(Schedulers.io())
                    .subscribe(observer);
        });
    }

    @Override
    public void deleteAllNotification() {
        ifViewAttached(view -> {
            CompletableObserver observer = new CompletableObserver() {

                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onComplete() {
                }

                @Override
                public void onError(Throwable throwable) {
                }
            };

            mDataManager.deleteAllNotification()
                    .subscribeOn(Schedulers.io())
                    .subscribe(observer);
        });
    }

    @Override
    public void readNotification(int id) {
        ifViewAttached(view -> {
            DbDisposableObserver<Long> myAppDisposableObserver = new
                    DbDisposableObserver<Long>() {
                        @Override
                        protected void onSuccess(Object response) {
                        }
                    };
            DbDisposableObserver<Long> disposableObserver = mDataManager.readNotification(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    @Override
    public void getLastUserDetail() {
        DbDisposableObserver<DBUserTC> myAppDisposableObserver = new
                DbDisposableObserver<DBUserTC>() {
                    @Override
                    protected void onSuccess(Object response) {
                        DBUserTC dbUserItem = (DBUserTC) response;
                        if (dbUserItem != null) {
                            email = dbUserItem.getEmail();
                        }
                        getNotificationData();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (throwable != null) {
                        }
                    }
                };

        DbDisposableObserver<DBUserTC> disposableObserver = mDataManager.getLastLoginUser()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(myAppDisposableObserver);
        compositeDisposable.add(disposableObserver);
    }

    public long daysBetween(String d1, String d2) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_MONTH_YEAR_FORMAT, Locale.ENGLISH);
        Date date1 = null, date2 = null;
        try {
            date1 = sdf.parse(d1);
            date2 = sdf.parse(d2);
        } catch (Exception e) {
            Timber.d("Exception : " + e.toString());
        }
        return (date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000);
    }

}