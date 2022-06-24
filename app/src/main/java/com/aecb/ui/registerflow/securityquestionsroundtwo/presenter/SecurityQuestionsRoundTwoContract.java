package com.aecb.ui.registerflow.securityquestionsroundtwo.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.securityquestions.RoundSuccessData;
import com.aecb.data.api.models.securityquestions.SecurityQuestionsSubmitRequest;

public interface SecurityQuestionsRoundTwoContract {
    interface View extends BaseView {
        void openPasswordScreen(RoundSuccessData roundSuccessData);

        void openContactUsOrLoginDialog();
    }

    interface Presenter extends BasePresenter<View> {
        void submitSecurityQuestions(SecurityQuestionsSubmitRequest securityQuestionsSubmitRequest);
    }
}