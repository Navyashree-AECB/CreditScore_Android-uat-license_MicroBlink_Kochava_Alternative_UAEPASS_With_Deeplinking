package com.aecb.ui.registerflow.securityquestionsroundtwo.presenter;

import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.DataManager;
import com.aecb.data.api.models.securityquestions.RoundSuccessData;
import com.aecb.data.api.models.securityquestions.SecurityQuestionsResponse;
import com.aecb.data.api.models.securityquestions.SecurityQuestionsSubmitRequest;
import com.google.gson.Gson;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.aecb.data.api.models.commonmessageresponse.UserCases.SECURITY_QUESTIONS_TWO;

public final class SecurityQuestionsRoundTwoPresenterImpl extends
        MvpBasePresenterImpl<SecurityQuestionsRoundTwoContract.View> implements
        SecurityQuestionsRoundTwoContract.Presenter {

    private DataManager mDataManager;

    @Inject
    public SecurityQuestionsRoundTwoPresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void submitSecurityQuestions(SecurityQuestionsSubmitRequest securityQuestionsSubmitRequest) {
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<SecurityQuestionsResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<SecurityQuestionsResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            view.hideLoading();
                            SecurityQuestionsResponse securityQuestionsResponse = (SecurityQuestionsResponse) response;
                            if (securityQuestionsResponse != null) {
                                switch (securityQuestionsResponse.getStatus()) {
                                    case "rr_01":
                                        String roundData = new Gson().toJson(securityQuestionsResponse.getSecurityQuestionsItems());
                                        RoundSuccessData roundSuccessData = new Gson().fromJson(roundData, RoundSuccessData.class);
                                        dataManager.setSessionToken(roundSuccessData.getParseUser().getSessionToken());
                                        view.openPasswordScreen(roundSuccessData);
                                        break;
                                    case "rr_05":
                                        view.openContactUsOrLoginDialog();
                                        break;
                                    default:
                                        view.showApiFailureError(securityQuestionsResponse.getMessage(),securityQuestionsResponse.getStatus(),SECURITY_QUESTIONS_TWO);
                                }
                            }
                        }
                    };

            MyAppDisposableObserver<SecurityQuestionsResponse> disposableObserver =
                    mDataManager.submitSecurityQuestions(securityQuestionsSubmitRequest)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(myAppDisposableObserver);
        });
    }
}