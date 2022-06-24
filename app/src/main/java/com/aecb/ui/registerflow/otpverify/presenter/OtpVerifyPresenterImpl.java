package com.aecb.ui.registerflow.otpverify.presenter;

import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.DataManager;
import com.aecb.data.api.models.BaseResponse;
import com.aecb.data.api.models.generateotp.GenerateOtpData;
import com.aecb.data.api.models.generateotp.GenerateOtpRequest;
import com.aecb.data.api.models.generateotp.GenerateOtpResponse;
import com.aecb.data.api.models.registeruser.OtpGeneratedData;
import com.aecb.data.api.models.verifyotp.OtpVerifyRequest;
import com.aecb.data.api.models.verifyotp.VerifyOtpData;
import com.aecb.data.api.models.verifyotp.VerifyOtpResponse;
import com.aecb.data.api.models.verifyotp.verifyotpforregister.VerifyOTPResponseForRegister;
import com.google.gson.Gson;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.aecb.AppConstants.IntentKey.REGISTRATION;
import static com.aecb.data.api.models.commonmessageresponse.UserCases.OTP_GENERATE;
import static com.aecb.data.api.models.commonmessageresponse.UserCases.USER_EXCEED_THE_TIME_LIMIT;
import static com.aecb.data.api.models.commonmessageresponse.UserCases.VERIFY_OTP;
import static com.aecb.util.FirebaseLogging.cancelFromOTPScreen;

public final class OtpVerifyPresenterImpl extends MvpBasePresenterImpl<OtpVerifyContract.View>
        implements OtpVerifyContract.Presenter {

    private DataManager mDataManager;

    @Inject
    OtpVerifyPresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void verifyOtp(OtpVerifyRequest otpVerifyRequest, String userName) {
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<VerifyOtpResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<VerifyOtpResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            view.hideLoading();
                            VerifyOtpResponse otpVerifyResponse = (VerifyOtpResponse) response;
                            BaseResponse baseResponse = (BaseResponse) response;
                            String otpdata = new Gson().toJson(otpVerifyResponse.getData());
                            if (otpVerifyResponse.getStatus().equals("E02-0005") || otpVerifyResponse.getMessage().contains("OTP verify limit reached.")) {
                                view.disableSubmitButton();
                            }
                            if (otpVerifyRequest.getOtpType().equals(REGISTRATION)) {
                                VerifyOTPResponseForRegister otpGeneratedData = new Gson().fromJson(otpdata, VerifyOTPResponseForRegister.class);
                                GenerateOtpData generateOtpData = new Gson().fromJson(otpdata, GenerateOtpData.class);
                                if (otpVerifyResponse.isSuccess()) {
                                    if (otpGeneratedData.getParseUser().getSessionToken() != null &&
                                            !otpGeneratedData.getParseUser().getSessionToken().isEmpty()) {
                                        mDataManager.setSessionToken(otpGeneratedData.getParseUser().getSessionToken());
                                        view.openNextScreen(otpGeneratedData.getParseUser().getSessionToken(), userName);
                                    }
                                } else {
                                    cancelFromOTPScreen();
                                    if (generateOtpData != null) {
                                        if (generateOtpData.getRemainingAttempts() == 2) {
                                            view.showApiFailureError(baseResponse.getMessage(), otpVerifyResponse.getStatus(), VERIFY_OTP);
                                        } else if (generateOtpData.getRemainingAttempts() == 1) {
                                            view.showApiFailureError(baseResponse.getMessage(), "201", VERIFY_OTP);
                                        } else if (generateOtpData.getRemainingAttempts() == 0) {
                                            view.showApiFailureError(baseResponse.getMessage(), "202", VERIFY_OTP);
                                        }
                                    } else {
                                        view.showApiFailureError(baseResponse.getMessage(), "203", USER_EXCEED_THE_TIME_LIMIT);
                                    }
                                }
                            } else {
                                VerifyOtpData verifyOtpData = new Gson().fromJson(otpdata, VerifyOtpData.class);
                                GenerateOtpData generateOtpData = new Gson().fromJson(otpdata, GenerateOtpData.class);
                                if (otpVerifyResponse.isSuccess()) {
                                    view.openNextScreen(verifyOtpData.getSessionToken(), userName);
                                } else {
                                    if (generateOtpData != null) {
                                        if (generateOtpData.getRemainingAttempts() == 2) {
                                            view.showApiFailureError(baseResponse.getMessage(), otpVerifyResponse.getStatus(), VERIFY_OTP);
                                        } else if (generateOtpData.getRemainingAttempts() == 1) {
                                            view.showApiFailureError(baseResponse.getMessage(), "201", VERIFY_OTP);
                                        } else if (generateOtpData.getRemainingAttempts() == 0) {
                                            view.showApiFailureError(baseResponse.getMessage(), "202", VERIFY_OTP);
                                        }
                                    } else {
                                        view.showApiFailureError(baseResponse.getMessage(), "203", USER_EXCEED_THE_TIME_LIMIT);
                                    }
                                }
                            }
                        }
                    };

            MyAppDisposableObserver<VerifyOtpResponse> disposableObserver =
                    mDataManager.verifyOtp(otpVerifyRequest)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    @Override
    public void generateOTP(GenerateOtpRequest generateOtpRequest) {
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<GenerateOtpResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<GenerateOtpResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            view.hideLoading();
                            GenerateOtpResponse generateOtpResponse = (GenerateOtpResponse) response;
                            if (generateOtpResponse != null && generateOtpResponse.getData() != null) {
                                OtpGeneratedData data = generateOtpResponse.getData();
                                String otpToken = data.getOtpToken();
                                String refID = generateOtpResponse.getRefId();
                                boolean canResend = data.isCanResend();
                                view.setUpOTP(otpToken, refID, canResend);
                                view.enableSubmitButton();
                                if (generateOtpResponse.getMessage().equalsIgnoreCase("OTP generated successfully")) {
                                    view.showApiSuccessMessage(generateOtpResponse.getMessage(), generateOtpResponse.getStatus(), OTP_GENERATE);
                                } else {
                                    view.showApiSuccessMessage(generateOtpResponse.getMessage());
                                }

                            } else {
                                BaseResponse baseResponse = (BaseResponse) response;
                                view.showApiFailureError(baseResponse.getMessage(), generateOtpResponse.getStatus(), "");
                            }
                        }
                    };

            MyAppDisposableObserver<GenerateOtpResponse> disposableObserver =
                    mDataManager.generateOtp(generateOtpRequest)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    @Override
    public long getOtpTime() {
        return mDataManager.getOtpTime();
    }
}