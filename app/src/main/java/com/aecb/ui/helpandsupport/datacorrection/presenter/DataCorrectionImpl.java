package com.aecb.ui.helpandsupport.datacorrection.presenter;

import com.aecb.AppConstants;
import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.DataManager;
import com.aecb.data.api.models.banklist.GetBankListReponse;
import com.aecb.data.api.models.contactus.ContactUsSubmitRequest;
import com.aecb.util.ValidationUtil;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.aecb.AppConstants.AppLanguage.ISO_CODE_ENG;

public class DataCorrectionImpl extends MvpBasePresenterImpl<DataCorrectionContract.View> implements
        DataCorrectionContract.Presenter {
    DataManager mDataManager;

    @Inject
    public DataCorrectionImpl(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    @Override
    public void loadBankList() {
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<GetBankListReponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<GetBankListReponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            view.hideLoading();
                            GetBankListReponse getBankListReponse = (GetBankListReponse) response;
                            view.setBankList(getBankListReponse.getData().getBankList(), dataManager.getCurrentLanguage());
                        }
                    };

            MyAppDisposableObserver<GetBankListReponse> disposableObserver =
                    dataManager.getBankList()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    @Override
    public void submitContactUsQuery(ContactUsSubmitRequest contactUsSubmitRequest) {
        if (validate(contactUsSubmitRequest)) {
            //Todo confirm that this code will be used later or not

            /* view.showLoading(null);
                MyAppDisposableObserver<BaseResponse> myAppDisposableObserver = new
                        MyAppDisposableObserver<BaseResponse>(view) {
                            @Override
                            protected void onSuccess(Object response) {
                                view.hideLoading();
                                BaseResponse contactUsQueryResponse = (BaseResponse) response;
                                if (contactUsQueryResponse != null) {
                                    if (contactUsQueryResponse.isSuccess()) {
                                        view.showApiSuccessMessage(contactUsQueryResponse.getMessage());
                                        view.resetData();
                                    } else {
                                        view.showApiFailureError(contactUsQueryResponse.getMessage());
                                    }
                                }
                            }
                        };

                MyAppDisposableObserver<BaseResponse> disposableObserver =
                        dataManager.submitContactUs(contactUsSubmitRequest)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(myAppDisposableObserver);
                compositeDisposable.add(disposableObserver);*/
            ifViewAttached(DataCorrectionContract.View::openEmail);
        }
    }

    private boolean validate(ContactUsSubmitRequest submitRequest) {
        boolean valid = true;
        if (ValidationUtil.isNullOrEmpty(submitRequest.getBankCode())) {
            ifViewAttached(DataCorrectionContract.View::showSelectBankError);
            return false;
        }
        if (!validateSubject(submitRequest.getSubject())) {
            return false;
        }
        if (!validateMessage(submitRequest.getDescription())) {
            return false;
        }
        return valid;
    }

    private boolean validateSubject(String subject) {
        if (ValidationUtil.isNullOrEmpty(subject)) {
            ifViewAttached(DataCorrectionContract.View::showQuerySubjectError);
            return false;
        /*} else if (!ValidationUtil.isValidAlpha(subject, AppConstants.ValidationRegex.ALPHANUMERIC)) {
            ifViewAttached(DataCorrectionContract.View::showQuerySubjectValidError);
            return false;*/
        }
        return true;
    }

    private boolean validateMessage(String message) {
        if (ValidationUtil.isNullOrEmpty(message)) {
            ifViewAttached(DataCorrectionContract.View::showQueryMessagesError);
            return false;
       /* } else if (!ValidationUtil.isValidAlpha(message, AppConstants.ValidationRegex.ALPHANUMERIC)) {
            ifViewAttached(DataCorrectionContract.View::showQueryMessagesValidError);
            return false;*/
        }
        return true;
    }
}