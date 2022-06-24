package com.aecb.ui.searchdialog.presenter;

import com.aecb.base.db.DbDisposableObserver;
import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.api.models.BaseResponse;
import com.aecb.data.api.models.commonmessageresponse.UserCases;
import com.aecb.data.api.models.request.updateuserprofile.UpdateUserProfileRequest;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.ui.searchdialog.view.SearchFragment;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchPresenterImpl extends MvpBasePresenterImpl<SearchContract.View>
        implements SearchContract.Presenter {

    @Inject
    public SearchPresenterImpl() {
    }

    @Override
    public String getAppLanguage() {
        return dataManager.getCurrentLanguage();
    }

    @Override
    public void updateUserDetail(UpdateUserProfileRequest updateUserProfileRequest, DBUserTC dbUserTC,
                                 SearchFragment mContext) {
        mContext.showLoading(null);
        MyAppDisposableObserver<BaseResponse> myAppDisposableObserver =
                new MyAppDisposableObserver<BaseResponse>(mContext) {
                    @Override
                    protected void onSuccess(Object response) {
                        mContext.hideLoading();
                        BaseResponse baseResponse = (BaseResponse) response;
                        if (baseResponse.getStatus().equals("uu_03") ||
                                baseResponse.getStatus().equals("uu_04") ||
                                baseResponse.getStatus().equals("uu_05") ||
                                baseResponse.getStatus().equals("uu_07")) {
                            updateUserDetails(dbUserTC, mContext, baseResponse.getMessage(),baseResponse.getStatus(), UserCases.EDT_PROFILE);
                        }
                    }
                };

        MyAppDisposableObserver<BaseResponse> disposableObserver = dataManager.updateUserProfile(updateUserProfileRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(myAppDisposableObserver);
        compositeDisposable.add(disposableObserver);

    }

    private void updateUserDetails(DBUserTC dbUserTC, SearchFragment mContext, String message,String status,String useCase) {
        DbDisposableObserver<Long> myAppDisposableObserver = new
                DbDisposableObserver<Long>() {
                    @Override
                    protected void onSuccess(Object response) {
                        mContext.openProfileDetail(dbUserTC.getNationalityId(), message,status,useCase);
                    }
                };
        DbDisposableObserver<Long> disposableObserver = dataManager.updateUserTC(dbUserTC)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(myAppDisposableObserver);
        compositeDisposable.add(disposableObserver);
    }
}