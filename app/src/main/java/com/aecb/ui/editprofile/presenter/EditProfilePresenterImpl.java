package com.aecb.ui.editprofile.presenter;

import android.util.Patterns;

import com.aecb.base.db.DbDisposableObserver;
import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.DataManager;
import com.aecb.data.api.models.BaseResponse;
import com.aecb.data.api.models.commonmessageresponse.UserCases;
import com.aecb.data.api.models.request.updateuserprofile.UpdateUserProfileRequest;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.util.ValidationUtil;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.aecb.AppConstants.EditField.EMAIL_ID;
import static com.aecb.AppConstants.EditField.MOBILE_NO;
import static com.aecb.util.ValidationUtil.checkMinTextValidation;

public class EditProfilePresenterImpl extends MvpBasePresenterImpl<EditProfileContract.View>
        implements EditProfileContract.Presenter {

    private DataManager mDataManager;

    @Inject
    public EditProfilePresenterImpl(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void updateUserDetail(UpdateUserProfileRequest updateUserProfileRequest, DBUserTC dbUserTC,
                                 String editField) {
        if (validation(editField, updateUserProfileRequest)) {
            ifViewAttached(view -> {
                view.showLoading(null);
                MyAppDisposableObserver<BaseResponse> myAppDisposableObserver =
                        new MyAppDisposableObserver<BaseResponse>(view) {
                            @Override
                            protected void onSuccess(Object response) {
                                view.hideLoading();
                                BaseResponse baseResponse = (BaseResponse) response;
                                if (baseResponse.getStatus().equals("uu_03") ||
                                        baseResponse.getStatus().equals("uu_04") ||
                                        baseResponse.getStatus().equals("uu_05") ||
                                        baseResponse.getStatus().equals("uu_06") ||
                                        baseResponse.getStatus().equals("uu_07") ||
                                        baseResponse.getStatus().equals("uu_09")) {
                                    updateUserDetails(dbUserTC);
                                    view.openProfile(baseResponse.getMessage(),baseResponse.getStatus(), UserCases.EDT_PROFILE);
                                }
                            }
                        };

                MyAppDisposableObserver<BaseResponse> disposableObserver = mDataManager.updateUserProfile(updateUserProfileRequest)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(myAppDisposableObserver);
                compositeDisposable.add(disposableObserver);
            });
        }
    }

    private void updateUserDetails(DBUserTC dbUserTC) {
        ifViewAttached(view -> {
            DbDisposableObserver<Long> myAppDisposableObserver = new
                    DbDisposableObserver<Long>() {
                        @Override
                        protected void onSuccess(Object response) {
                        }
                    };
            DbDisposableObserver<Long> disposableObserver = dataManager.updateUserTC(dbUserTC)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    private boolean validation(String field, UpdateUserProfileRequest updateUserProfileRequest) {
        AtomicBoolean valid = new AtomicBoolean(true);
        ifViewAttached(view -> {
            if (field.equals(EMAIL_ID)) {
                if (ValidationUtil.isNullOrEmpty(updateUserProfileRequest.getEmail())) {
                    view.showEmptyEmailError();
                    valid.set(false);
                } else if (!Patterns.EMAIL_ADDRESS.matcher(updateUserProfileRequest.getEmail()).matches()) {
                    view.showInvalidEmailError();
                    valid.set(false);
                } else {
                    valid.set(true);
                }
            } else if (field.equals(MOBILE_NO)) {
                if (ValidationUtil.isNullOrEmpty(updateUserProfileRequest.getMobile())) {
                    view.showEmptyMobileError();
                    valid.set(false);
                } else if (!checkMinTextValidation(updateUserProfileRequest.getMobile(), 13)) {
                    view.showInvalidMobileError();
                    valid.set(false);
                } else {
                    valid.set(true);
                }
            } else {
                if (ValidationUtil.isNullOrEmpty(updateUserProfileRequest.getPassport())) {
                    view.showEmptyPassportNo();
                    valid.set(false);
                } else {
                    valid.set(true);
                }
            }
        });
        return valid.get();
    }
}