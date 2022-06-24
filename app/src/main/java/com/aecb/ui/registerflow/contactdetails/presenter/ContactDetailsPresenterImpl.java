package com.aecb.ui.registerflow.contactdetails.presenter;

import android.util.Patterns;

import com.aecb.BuildConfig;
import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.DataManager;
import com.aecb.data.api.models.commonmessageresponse.UserCases;
import com.aecb.data.api.models.registeruser.OtpGeneratedData;
import com.aecb.data.api.models.registeruser.RegisterUserRequest;
import com.aecb.data.api.models.registeruser.RegisterUserResponse;
import com.aecb.data.api.models.securityquestions.RoundQuestionsData;
import com.aecb.data.api.models.securityquestions.RoundSuccessData;
import com.aecb.util.ValidationUtil;
import com.google.gson.Gson;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ContactDetailsPresenterImpl extends MvpBasePresenterImpl<ContactDetailsContract.View>
        implements ContactDetailsContract.Presenter {

    private DataManager mDataManager;

    @Inject
    public ContactDetailsPresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void sendContactDetails(RegisterUserRequest registerUserRequest, String confirmEmail) {
        if (validation(registerUserRequest.getMobile(), registerUserRequest.getEmail(), confirmEmail)) {
            dataManager.removeSessionToken();
            registerUserRequest.setTcVersionNumber(dataManager.getTcVersionNumber());
            registerUserRequest.setPreferredlanguage(dataManager.getCurrentLanguage());
            if (BuildConfig.BUILD_TYPE.equals("dev") || BuildConfig.BUILD_TYPE.equals("uat")) {
                registerUserRequest.setReportType("7e22b2d1-7d13-e611-9407-00155d05660b");
            } else if (BuildConfig.BUILD_TYPE.equals("prod")) {
                registerUserRequest.setReportType("d7d6ac3b-5d69-e811-9449-00155db7a21e");
            } else {
                registerUserRequest.setReportType("7e22b2d1-7d13-e611-9407-00155d05660b");
            }
            registerUserRequest.setTcVersionNumber(dataManager.getTcVersionNumber());
            registerUserRequest.setChannel(3);
            registerUserRequest.setIsAuthenticated(false);
            registerUser(registerUserRequest);
        }
    }

    private boolean validation(String mobile, String email, String confirmEmail) {
        AtomicBoolean valid = new AtomicBoolean(true);
        ifViewAttached(view -> {
            if ((ValidationUtil.isNullOrEmpty(mobile))) {
                view.showEmptyMobileError();
                valid.set(false);
            } else if (mobile.length() < 13) {
                view.showInvalidMobileError();
                valid.set(false);
            } else if (ValidationUtil.isNullOrEmpty(email)) {
                view.showEmptyEmailError();
                valid.set(false);
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                view.showInvalidEmailError();
                valid.set(false);
            } else if (ValidationUtil.isNullOrEmpty(confirmEmail)) {
                view.showEmptyConfirmEmailError();
                valid.set(false);
            } else if (!Patterns.EMAIL_ADDRESS.matcher(confirmEmail).matches()) {
                view.showInvalidEmailError();
                valid.set(false);
            } else if (!email.equals(confirmEmail)) {
                view.showEmailNotMatchedError();
                valid.set(false);
            }
        });
        return valid.get();
    }

    private void registerUser(RegisterUserRequest registerUserRequest) {
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<RegisterUserResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<RegisterUserResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            view.hideLoading();
                            RegisterUserResponse registerUserResponse = (RegisterUserResponse) response;
                            if (registerUserResponse != null) {
                                switch (registerUserResponse.getStatus()) {
                                    case "rr_01":
                                        String roundData = new Gson().toJson(registerUserResponse.getRegisterUserItems());
                                        RoundSuccessData roundSuccessData = new Gson().fromJson(roundData, RoundSuccessData.class);
                                        view.openPasswordScreen(roundSuccessData);
                                        break;
                                    case "rr_02":
                                        String otpdata = new Gson().toJson(registerUserResponse.getRegisterUserItems());
                                        OtpGeneratedData otpGeneratedData = new Gson().fromJson(otpdata, OtpGeneratedData.class);
                                        view.openOtpDialog(otpGeneratedData, registerUserResponse.getRefId());
                                        break;
                                    case "rr_03":
                                        String questionsData = new Gson().toJson(registerUserResponse.getRegisterUserItems());
                                        RoundQuestionsData roundQuestionsData = new Gson().fromJson(questionsData, RoundQuestionsData.class);
                                        view.openQuestionsRoundOneScreen(roundQuestionsData);
                                        break;
                                    default:
                                        view.showApiFailureError(registerUserResponse.getMessage(), registerUserResponse.getStatus(), UserCases.REGISTRATION);
                                }
                            }
                        }
                    };

            MyAppDisposableObserver<RegisterUserResponse> disposableObserver =
                    mDataManager.registerNewUser(registerUserRequest)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }
}