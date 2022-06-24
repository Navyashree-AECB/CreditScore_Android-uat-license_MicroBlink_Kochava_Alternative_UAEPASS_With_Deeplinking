package com.aecb.ui.termsandconditions.presenter;

import com.aecb.AppConstants;
import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.api.models.termsconditions.TermsConditionData;
import com.aecb.data.api.models.termsconditions.TermsConditionResponse;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TermsConditionPresenterImpl extends MvpBasePresenterImpl<TermsConditionContract.View>
        implements TermsConditionContract.Presenter {

    @Inject
    public TermsConditionPresenterImpl() {

    }

    @Override
    public void getTermsConditionUrl() {
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<List<TermsConditionResponse>> myAppDisposableObserver =
                    new MyAppDisposableObserver<List<TermsConditionResponse>>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            view.hideLoading();
                            List<TermsConditionData> termsConditionData = (List<TermsConditionData>) response;
                            if (termsConditionData != null) {
                                // if (termsConditionData.isSuccess()) {
                                if (dataManager.getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ENG)) {
                                    view.loadTermsUrl(termsConditionData.get(0).getTitle(),
                                            termsConditionData.get(0).getFieldContentEn());
                                } else {
                                    view.loadTermsUrl(termsConditionData.get(0).getTitle(),
                                            termsConditionData.get(0).getFieldContentAr());
                                }
                              /*  } else {
                                    view.showErrorMsgFromApi("", otpVerifyResponse.getMessage(), OK, null);
                                }*/
                            }
                        }
                    };

            MyAppDisposableObserver<List<TermsConditionResponse>> disposableObserver =
                    dataManager.getTermsConditions()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    @Override
    public int getTCVersion() {
        return dataManager.getTcVersionNumber();
    }
}