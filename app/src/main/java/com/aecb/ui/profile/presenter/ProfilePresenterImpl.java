package com.aecb.ui.profile.presenter;

import com.aecb.base.db.DbDisposableObserver;
import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.DataManager;
import com.aecb.data.api.models.nationalities.NationalityResponse;
import com.aecb.data.db.repository.usertcversion.DBUserTC;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProfilePresenterImpl extends MvpBasePresenterImpl<ProfileContract.View>
        implements ProfileContract.Presenter {

    DataManager mDataManager;

    @Inject
    ProfilePresenterImpl(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    @Override
    public void getAllNationalities() {
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<NationalityResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<NationalityResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            view.hideLoading();
                            NationalityResponse nationalityResponse = (NationalityResponse) response;
                            if (nationalityResponse.isSuccess() && nationalityResponse.getData() != null) {
                                view.loadAllNationalities(nationalityResponse.getData().getNationalities());
                                getLastUserDetail();
                            } else {
                                view.showApiFailureError(nationalityResponse.getMessage(),nationalityResponse.getStatus(),"");
                            }
                        }
                    };

            MyAppDisposableObserver<NationalityResponse> disposableObserver = dataManager.getAllNationalities()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
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
                            ifViewAttached(view -> {
                                view.setUser(dbUserItem);
                            });
                        }
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
}