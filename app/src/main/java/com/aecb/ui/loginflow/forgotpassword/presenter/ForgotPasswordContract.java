package com.aecb.ui.loginflow.forgotpassword.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.registeruser.OtpGeneratedData;

public interface ForgotPasswordContract {
    interface View extends BaseView {

        void showEmptyEmailError();

        void showInvalidEmailError();

        void showMessage(String message);

        void setUpOTP(String otpToken, String refID, boolean canResend, OtpGeneratedData generateOtpResponse);
    }

    interface Presenter extends BasePresenter<View> {
        void sendOtp(String email);
    }
}