package com.aecb.ui.changepassword.presenter;

import com.aecb.base.db.DbDisposableObserver;
import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.DataManager;
import com.aecb.data.api.models.BaseResponse;
import com.aecb.data.api.models.changepassword.ChangePasswordRequest;
import com.aecb.data.api.models.changepassword.ChangePasswordResponse;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.util.AESEncryption;
import com.aecb.util.ValidationUtil;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.aecb.AppConstants.OTPFor.FOR_CHANGE_PASSWORD;
import static com.aecb.AppConstants.ValidationRegex.PASSWORD;
import static com.aecb.data.api.models.commonmessageresponse.UserCases.CHANGE_PASSWORD_SCREEN;
import static com.aecb.util.Utilities.fullString;

public class ChangePasswordPresenterImpl extends MvpBasePresenterImpl<ChangePasswordContract.View>
        implements ChangePasswordContract.Presenter {

    ChangePasswordRequest changePasswordRequest;
    DBUserTC dbUser;
    private DataManager mDataManager;
    private String password = "";

    @Inject
    public ChangePasswordPresenterImpl(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    @Override
    public void changePassword(String currentPassword, String confirmPassword, String newPassword) {
        getLastLoginUser();
        if (validation(currentPassword, newPassword, confirmPassword)) {
            changePasswordRequest = new ChangePasswordRequest();
            changePasswordRequest.setOldPassword(createEncryptedRequest(currentPassword));
            changePasswordRequest.setNewPassword(createEncryptedRequest(confirmPassword));
            changePasswordRequest.setType(FOR_CHANGE_PASSWORD);
            changePassword(changePasswordRequest);
            password = newPassword;
        }
    }

    private boolean validation(String currentPassword, String password, String confirmPassword) {
        AtomicBoolean valid = new AtomicBoolean(true);
        ifViewAttached(view -> {
            if ((ValidationUtil.isNullOrEmpty(currentPassword))) {
                view.showCurrentPasswordError();
                valid.set(false);
            } else if ((ValidationUtil.isNullOrEmpty(password))) {
                view.showEmptyPasswordError();
                valid.set(false);
            } else if (!(Pattern.matches(PASSWORD, password))) {
                if (password.length() < 8 || password.length() > 20) {
                    view.showPasswordSizeError();
                } else {
                    view.showInvalidPasswordError();
                }
                valid.set(false);
            } else if (ValidationUtil.isNullOrEmpty(confirmPassword)) {
                view.showEmptyConfirmPasswordError();
                valid.set(false);
            } else if (!(Pattern.matches(PASSWORD, confirmPassword))) {
                if (confirmPassword.length() < 8 || confirmPassword.length() > 20) {
                    view.showPasswordSizeError();
                } else {
                    view.showInvalidPasswordError();
                }
                valid.set(false);
            } else if (!password.equals(confirmPassword)) {
                view.showPasswordNotMatchedError();
                valid.set(false);
            }
        });
        return valid.get();
    }

    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        ifViewAttached(view -> {
            view.showLoading("");
            MyAppDisposableObserver<ChangePasswordResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<ChangePasswordResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            view.hideLoading();
                            ChangePasswordResponse changePasswordResponse = (ChangePasswordResponse) response;
                            BaseResponse baseResponse = (BaseResponse) response;
                            if (changePasswordResponse.isSuccess()) {
                                if (password != null && !password.isEmpty()) {
                                    if (dbUser != null) {
                                        updateUserPassword(dbUser.getUserName(), password);
                                    }
                                }
                                view.openLogin(baseResponse.getMessage(), baseResponse.getStatus(), CHANGE_PASSWORD_SCREEN);
                            } else {
                                view.showApiFailureError(changePasswordResponse.getMessage(), changePasswordResponse.getStatus(), CHANGE_PASSWORD_SCREEN);
                            }
                        }

                    };

            MyAppDisposableObserver<ChangePasswordResponse> disposableObserver =
                    mDataManager.changePassword(changePasswordRequest)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    public String createEncryptedRequest(String requestBody) {
        AESEncryption.setKeyOld(fullString());
        AESEncryption.encrypt(requestBody);
        return AESEncryption.getEncryptedString()
                .replaceAll("\\s", "")
                .replaceAll("\\n", "")
                .replaceAll("\\r", "");
    }

    public void getLastLoginUser() {
        ifViewAttached(view -> {
            DbDisposableObserver<DBUserTC> myAppDisposableObserver = new
                    DbDisposableObserver<DBUserTC>() {
                        @Override
                        protected void onSuccess(Object response) {
                            dbUser = (DBUserTC) response;
                        }
                    };

            DbDisposableObserver<DBUserTC> disposableObserver = dataManager.getLastLoginUser()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    private void updateUserPassword(String email, String password) {
        DbDisposableObserver<Long> myAppDisposableObserver = new DbDisposableObserver<Long>() {
            @Override
            protected void onSuccess(Object response) {
            }
        };
        DbDisposableObserver<Long> disposableObserver =
                dataManager.updateUserPassword(email, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(myAppDisposableObserver);
        compositeDisposable.add(disposableObserver);
    }

}