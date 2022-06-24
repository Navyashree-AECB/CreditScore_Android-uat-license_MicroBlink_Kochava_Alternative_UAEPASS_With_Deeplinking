package com.aecb.ui.registerflow.contactdetails.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.registeruser.OtpGeneratedData;
import com.aecb.data.api.models.registeruser.RegisterUserRequest;
import com.aecb.data.api.models.securityquestions.RoundQuestionsData;
import com.aecb.data.api.models.securityquestions.RoundSuccessData;

public interface ContactDetailsContract {
    interface View extends BaseView {
        void showEmptyMobileError();

        void showEmptyEmailError();

        void showInvalidEmailError();

        void showEmptyConfirmEmailError();

        void showEmailNotMatchedError();

        void openOtpDialog(OtpGeneratedData otpGeneratedData, String referenceID);

        void showInvalidMobileError();

        void openPasswordScreen(RoundSuccessData roundSuccessData);

        void openQuestionsRoundOneScreen(RoundQuestionsData roundQuestionsData);
    }

    interface Presenter extends BasePresenter<View> {
        void sendContactDetails(RegisterUserRequest registerUserRequest, String confirmEmail);
    }
}