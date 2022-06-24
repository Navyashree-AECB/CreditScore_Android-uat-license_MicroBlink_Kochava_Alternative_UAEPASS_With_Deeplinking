package com.aecb.ui.menu.presenter;

import com.aecb.base.db.DbDisposableObserver;
import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.DataManager;
import com.aecb.data.api.models.BaseResponse;
import com.aecb.data.api.models.commonmessageresponse.UserCases;
import com.aecb.data.api.models.configuration.ConfigurationData;
import com.aecb.data.api.models.login.LoginRequest;
import com.aecb.data.api.models.request.updateuserprofile.UpdateUserProfileRequest;
import com.aecb.data.api.models.response.login.LoginResponse;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.util.AESEncryption;
import com.google.gson.Gson;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.aecb.data.api.models.commonmessageresponse.UserCases.EDT_PROFILE;
import static com.aecb.util.Utilities.fullString;

public class MenuPresenterImpl extends MvpBasePresenterImpl<MenuContract.View>
        implements MenuContract.Presenter {

    DataManager mDataManager;

    @Inject
    public MenuPresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public String getAppLanguage() {
        return dataManager.getCurrentLanguage();
    }

    @Override
    public boolean getTouchLoggedIn() {
        return dataManager.getTouchLoggedIn();
    }

    @Override
    public void setTouchLoggedIn(boolean touchId) {
        dataManager.setTouchLoggedIn(touchId);
    }

    @Override
    public void saveUserAdvProfile(UpdateUserProfileRequest updateUserProfileRequest) {
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<BaseResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<BaseResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            BaseResponse baseResponse = (BaseResponse) response;
                            if (baseResponse != null) {
                                view.hideLoading();
                                if (baseResponse.isSuccess()) {
                                    dataManager.setCurrentLanguage(updateUserProfileRequest.getPreferredlanguage());
                                    view.updateUserAdvProfileResponse();
                                    view.showApiSuccessMessage(baseResponse.getMessage(), baseResponse.getStatus(), EDT_PROFILE);
                                } else {
                                    view.showApiFailureError(baseResponse.getMessage(), baseResponse.getStatus(), "");
                                }
                            }
                        }
                    };

            MyAppDisposableObserver<BaseResponse> disposableObserver = dataManager.updateUserProfile(updateUserProfileRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    @Override
    public void getUserDetail() {
        ifViewAttached(view -> {
            DbDisposableObserver<DBUserTC> myAppDisposableObserver = new
                    DbDisposableObserver<DBUserTC>() {
                        @Override
                        protected void onSuccess(Object response) {
                            DBUserTC dbUserTC = (DBUserTC) response;
                            view.setLastUserDetails(dbUserTC);
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
    public void callLoginApi(boolean touchEnable, String email, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(createEncryptedRequest(email));
        loginRequest.setPassword(createEncryptedRequest(password));
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<LoginResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<LoginResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            view.hideLoading();
                            LoginResponse loginResponse = (LoginResponse) response;
                            if (loginResponse.getData() != null) {
                                if (loginResponse.getData().getSessionToken() != null && !loginResponse.getData().getSessionToken().isEmpty()) {
                                    mDataManager.setSessionToken(loginResponse.getData().getSessionToken());
                                    updateTouchIdInLocal(touchEnable, email);
                                }
                            } else {
                                view.setCheckTouchId();
                                view.hideLoading();
                                view.showApiFailureError(loginResponse.getMessage(), loginResponse.getStatus(), UserCases.USING_INCORRECT_PASSWORD);
                            }
                        }
                    };

            MyAppDisposableObserver<LoginResponse> disposableObserver = mDataManager.login(loginRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    @Override
    public void updateTouchIdInLocal(boolean touchEnable, String email) {
        DbDisposableObserver<Long> myAppDisposableObserver = new DbDisposableObserver<Long>() {
            @Override
            protected void onSuccess(Object response) {
                //setTouchLoggedIn(touchEnable);
                ifViewAttached(view -> {
                    view.updateBiometricResponse(touchEnable);
                    getUserDetail();
                });
            }
        };
        DbDisposableObserver<Long> disposableObserver =
                dataManager.updateTouchId(touchEnable, email)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(myAppDisposableObserver);
        compositeDisposable.add(disposableObserver);
    }

    public String createEncryptedRequest(String requestBody) {
        AESEncryption.setKeyOld(fullString());
        AESEncryption.encrypt(requestBody);
        return AESEncryption.getEncryptedString()
                .replaceAll("\\s", "")
                .replaceAll("\\n", "")
                .replaceAll("\\r", "");
    }

    @Override
    public void logout() {
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<BaseResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<BaseResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            String fcmId = dataManager.getFcmId();
                            long otpTime = dataManager.getOtpTime();
                            int tcVersion = dataManager.getTcVersionNumber();
                            String appLanguage = dataManager.getCurrentLanguage();
                            boolean isAppFirstTime = dataManager.isFirstTime();
                            ConfigurationData configurationResponse = dataManager.getConfigurationData();
                            mDataManager.clearAll();
                            mDataManager.setFcmId(fcmId);
                            dataManager.setOtpTime(otpTime);
                            dataManager.setTcVersionNumber(tcVersion);
                            dataManager.setCurrentLanguage(appLanguage);
                            dataManager.setConfigurationData(new Gson().toJson(configurationResponse));
                            dataManager.isFirstTime(isAppFirstTime);
                            view.hideLoading();
                            view.openLoginActivity();
                        }
                    };
            MyAppDisposableObserver<BaseResponse> disposableObserver =
                    mDataManager.logout()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });

    }

}