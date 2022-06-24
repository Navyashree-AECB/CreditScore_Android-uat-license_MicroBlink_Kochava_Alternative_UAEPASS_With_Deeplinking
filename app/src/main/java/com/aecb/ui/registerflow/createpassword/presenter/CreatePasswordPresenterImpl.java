package com.aecb.ui.registerflow.createpassword.presenter;

import com.aecb.base.db.DbDisposableObserver;
import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.DataManager;
import com.aecb.data.api.models.BaseResponse;
import com.aecb.data.api.models.changepassword.ChangePasswordRequest;
import com.aecb.data.api.models.changepassword.ChangePasswordResponse;
import com.aecb.data.api.models.savedeviceinfo.SaveDeviceInfoRequest;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.util.AESEncryption;
import com.aecb.util.ValidationUtil;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.aecb.AppConstants.OTPFor.FOR_CHANGE_PASSWORD;
import static com.aecb.AppConstants.OTPFor.OTP_FOR_FORGOT_PASSWORD;
import static com.aecb.AppConstants.OTPFor.OTP_FOR_REGISTRATION;
import static com.aecb.AppConstants.ValidationRegex.PASSWORD;
import static com.aecb.data.api.models.commonmessageresponse.UserCases.CHANGE_PASSWORD_SCREEN;
import static com.aecb.util.Utilities.fullString;

public class CreatePasswordPresenterImpl extends MvpBasePresenterImpl<CreatePasswordContract.View>
        implements CreatePasswordContract.Presenter {

    ChangePasswordRequest changePasswordRequest;
    private DataManager mDataManager;
    private DBUserTC dbUser;
    private String password = "";

    @Inject
    public CreatePasswordPresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void sendPasswordDetails(String password, String confirmPassword, String sessionToken, String userName) {
        if (validation(password, confirmPassword)) {
            changePasswordRequest = new ChangePasswordRequest();
            this.password = password;
            changePasswordRequest.setNewPassword(createEncryptedRequest(password));
            if (userName != null && !userName.isEmpty()) {
                changePasswordRequest.setSessionToken(sessionToken);
                changePasswordRequest.setUserName(createEncryptedRequest(userName));
                changePasswordRequest.setType(OTP_FOR_FORGOT_PASSWORD);
            } else {
                changePasswordRequest.setType(OTP_FOR_REGISTRATION);
            }
            changePassword(changePasswordRequest, userName);
        }
    }

    @Override
    public void changePassword(String currentPassword, String confirmPassword, String newPassword, String sessionToken, String userName) {
        if (validation(currentPassword, newPassword, confirmPassword)) {
            changePasswordRequest = new ChangePasswordRequest();
            password = confirmPassword;
            changePasswordRequest.setOldPassword(createEncryptedRequest(currentPassword));
            changePasswordRequest.setNewPassword(createEncryptedRequest(confirmPassword));
            changePasswordRequest.setType(FOR_CHANGE_PASSWORD);
            changePassword(changePasswordRequest, userName);
        }
    }

    private boolean validation(String password, String confirmPassword) {
        AtomicBoolean valid = new AtomicBoolean(true);
        ifViewAttached(view -> {
            if ((ValidationUtil.isNullOrEmpty(password))) {
                view.showEmptyPasswordError();
                valid.set(false);
            } else if (!(Pattern.matches(PASSWORD, password))) {
                view.showInvalidPasswordError();
                valid.set(false);
            } else if (ValidationUtil.isNullOrEmpty(confirmPassword)) {
                view.showEmptyConfirmPasswordError();
                valid.set(false);
            } else if (!(Pattern.matches(PASSWORD, confirmPassword))) {
                view.showInvalidPasswordError();
                valid.set(false);
            } else if (!password.equals(confirmPassword)) {
                view.showPasswordNotMatchedError();
                valid.set(false);
            }
        });
        return valid.get();
    }

    private boolean validation(String currentPassword, String password, String confirmPassword) {
        AtomicBoolean valid = new AtomicBoolean(true);
        ifViewAttached(view -> {
            if ((ValidationUtil.isNullOrEmpty(currentPassword))) {
                view.showCurrentPasswordError();
                valid.set(false);
            } else if (!(Pattern.matches(PASSWORD, currentPassword))) {
                view.showInvalidCurrentPasswordError();
                valid.set(false);
            } else if ((ValidationUtil.isNullOrEmpty(password))) {
                view.showEmptyPasswordError();
                valid.set(false);
            } else if (!(Pattern.matches(PASSWORD, password))) {
                view.showInvalidPasswordError();
                valid.set(false);
            } else if (ValidationUtil.isNullOrEmpty(confirmPassword)) {
                view.showEmptyConfirmPasswordError();
                valid.set(false);
            } else if (!(Pattern.matches(PASSWORD, confirmPassword))) {
                view.showInvalidPasswordError();
                valid.set(false);
            } else if (!password.equals(confirmPassword)) {
                view.showPasswordNotMatchedError();
                valid.set(false);
            }
        });
        return valid.get();
    }

    @Override
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

    @Override
    public String getInstallSource() {
        return dataManager.getInstallSource();
    }

    @Override
    public String getInstalledCampaignId() {
        return dataManager.getInstalledCampaignId();
    }

    @Override
    public void saveDeviceInfo(SaveDeviceInfoRequest saveDeviceInfoRequest, String password) {
        ifViewAttached(view -> {
            view.showLoading("");
            MyAppDisposableObserver<BaseResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<BaseResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            view.hideLoading();
                            BaseResponse saveDeviceResponse = (BaseResponse) response;
                            if (saveDeviceResponse != null) {
                                mDataManager.setPreviousDeviceID(saveDeviceInfoRequest.getDeviceId());
                                view.openTouchId(password);
                            }
                        }
                    };

            MyAppDisposableObserver<BaseResponse> disposableObserver =
                    mDataManager.saveDeviceInfo(saveDeviceInfoRequest)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    @Override
    public String getFcmId() {
        return dataManager.getFcmId();
    }

    public void changePassword(ChangePasswordRequest changePasswordRequest, String userName) {
        ifViewAttached(view -> {
            view.showLoading("");
            MyAppDisposableObserver<ChangePasswordResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<ChangePasswordResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            view.hideLoading();
                            ChangePasswordResponse changePasswordResponse = (ChangePasswordResponse) response;
                            if (changePasswordResponse.isSuccess()) {
                                try {
                                    if (changePasswordResponse != null) {
                                        dataManager.setSessionToken(changePasswordResponse.getParseUser().getSessionToken());
                                    }
                                } catch (Exception e) {
                                    Timber.d("Exception : " + e.toString());
                                }
                                if (changePasswordRequest.getType().equals(OTP_FOR_FORGOT_PASSWORD)) {
                                    if (dbUser != null) {
                                        if (dbUser.getEmail() != null && dbUser.getEmail().equalsIgnoreCase(userName)) {
                                            updateTouchIdInLocal(false, dbUser.getEmail().toLowerCase());
                                        }
                                    }
                                    view.showApiSuccessMessage(changePasswordResponse.getMessage(), changePasswordResponse.getStatus(), CHANGE_PASSWORD_SCREEN);
                                }
                                view.redirectToParticularScreen();
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

    private void updateTouchIdInLocal(boolean touchEnable, String email) {
        DbDisposableObserver<Long> myAppDisposableObserver = new DbDisposableObserver<Long>() {
            @Override
            protected void onSuccess(Object response) {
            }
        };
        DbDisposableObserver<Long> disposableObserver =
                dataManager.updateTouchId(touchEnable, email)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(myAppDisposableObserver);
        compositeDisposable.add(disposableObserver);
    }
}