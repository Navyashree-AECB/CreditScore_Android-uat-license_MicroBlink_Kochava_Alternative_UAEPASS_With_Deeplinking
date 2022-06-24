package com.aecb.ui.registerflow.otpverify.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.generateotp.GenerateOtpRequest;
import com.aecb.data.api.models.verifyotp.OtpVerifyRequest;

public interface OtpVerifyContract {

    interface View extends BaseView {

        void openNextScreen(String sessionToken, String userName);

        void setUpOTP(String otpToken, String refID, boolean canResend);

        void disableSubmitButton();

        void enableSubmitButton();
    }

    interface Presenter extends BasePresenter<View> {
        void verifyOtp(OtpVerifyRequest otpVerifyRequest, String userName);

        void generateOTP(GenerateOtpRequest generateOtpRequest);

        long getOtpTime();
    }
}