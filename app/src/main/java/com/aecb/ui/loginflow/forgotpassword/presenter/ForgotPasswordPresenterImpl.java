package com.aecb.ui.loginflow.forgotpassword.presenter;

import android.util.Patterns;

import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.DataManager;
import com.aecb.data.api.models.BaseResponse;
import com.aecb.data.api.models.commonmessageresponse.UserCases;
import com.aecb.data.api.models.generateotp.GenerateOtpRequest;
import com.aecb.data.api.models.generateotp.GenerateOtpResponse;
import com.aecb.data.api.models.registeruser.OtpGeneratedData;
import com.aecb.util.ValidationUtil;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.aecb.AppConstants.ApiParameter.FORGOT_PASSWORD;

public class ForgotPasswordPresenterImpl extends MvpBasePresenterImpl<ForgotPasswordContract.View>
        implements ForgotPasswordContract.Presenter {

    private DataManager mDataManager;

    @Inject
    public ForgotPasswordPresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void sendOtp(String email) {
        if (validation(email)) {
            GenerateOtpRequest generateOtpRequest = new GenerateOtpRequest();
            generateOtpRequest.setReferenceId(UUID.randomUUID().toString());
            generateOtpRequest.setRequestOtpFor(FORGOT_PASSWORD);
            generateOtpRequest.setUsername(email);
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
                                    view.setUpOTP(otpToken, refID, canResend, data);
                                } else {
                                    BaseResponse baseResponse = (BaseResponse) response;
                                    view.showApiFailureError(baseResponse.getMessage(),generateOtpResponse.getStatus(), UserCases.FORGET_PASSWORD_SCREEN);
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
    }

    private boolean validation(String email) {
        AtomicBoolean valid = new AtomicBoolean(true);
        ifViewAttached(view -> {
            if (ValidationUtil.isNullOrEmpty(email)) {
                view.showEmptyEmailError();
                valid.set(false);
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                view.showInvalidEmailError();
                valid.set(false);
            }
        });
        return valid.get();
    }
}