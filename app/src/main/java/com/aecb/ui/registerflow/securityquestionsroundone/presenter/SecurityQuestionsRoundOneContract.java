package com.aecb.ui.registerflow.securityquestionsroundone.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.securityquestions.RoundQuestionsData;
import com.aecb.data.api.models.securityquestions.RoundSuccessData;
import com.aecb.data.api.models.securityquestions.SecurityQuestionsSubmitRequest;

public interface SecurityQuestionsRoundOneContract {
    interface View extends BaseView {

        void openPasswordScreen(RoundSuccessData roundSuccessData);

        void openQuestionsRoundTwoScreen(RoundQuestionsData roundQuestionsData);

        void openContactUsOrLoginDialog();
    }

    interface Presenter extends BasePresenter<View> {
        void submitSecurityQuestions(SecurityQuestionsSubmitRequest securityQuestionsSubmitRequest);
    }
}