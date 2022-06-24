package com.aecb.ui.contactusregistration.presenter;

import android.util.Patterns;

import com.aecb.AppConstants;
import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.DataManager;
import com.aecb.data.api.models.BaseResponse;
import com.aecb.data.api.models.configuration.CaseTypesItem;
import com.aecb.data.api.models.configuration.ConfigurationData;
import com.aecb.data.api.models.contactus.ContactUsSubmitRequest;
import com.aecb.util.ValidationUtil;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.aecb.AppConstants.AppLanguage.ISO_CODE_ENG;
import static com.aecb.AppConstants.ValidationRegex.NAME;

public class ContactUsRegistrationPresenterImpl extends MvpBasePresenterImpl<ContactUsRegistrationContract.View>
        implements ContactUsRegistrationContract.Presenter {

    DataManager mDataManger;

    @Inject
    public ContactUsRegistrationPresenterImpl(DataManager dataManager) {
        this.mDataManger = dataManager;
    }

    @Override
    public void getReasons() {
        ifViewAttached(view -> {
            ConfigurationData configurationData = dataManager.getConfigurationData();
            List<CaseTypesItem> caseTypes = configurationData.getCaseTypes();
            view.setReasons(caseTypes, dataManager.getCurrentLanguage());
        });
    }

    @Override
    public void submitContactUsQuery(ContactUsSubmitRequest contactUsSubmitRequest) {
        if (validate(contactUsSubmitRequest)) {
            ifViewAttached(view -> {
                view.showLoading(null);
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
                                        view.showApiFailureError(contactUsQueryResponse.getMessage(), contactUsQueryResponse.getStatus(), "");
                                    }
                                }
                            }
                        };

                MyAppDisposableObserver<BaseResponse> disposableObserver =
                        dataManager.submitContactUsRegister(contactUsSubmitRequest)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(myAppDisposableObserver);
                compositeDisposable.add(disposableObserver);
            });
        }
    }

    private boolean validate(ContactUsSubmitRequest submitRequest) {
        boolean valid = true;
        if (ValidationUtil.isNullOrEmpty(submitRequest.getSubject())) {
            ifViewAttached((ContactUsRegistrationContract.View::showEmptySubjectError));
            return false;
      /*  } else if (!ValidationUtil.isValidAlpha(submitRequest.getSubject(), AppConstants.ValidationRegex.ALPHANUMERIC)) {
            ifViewAttached(ContactUsRegistrationContract.View::showQuerySubjectValidError);
            return false;*/
        } else if (ValidationUtil.isNullOrEmpty(submitRequest.getReason())) {
            ifViewAttached(ContactUsRegistrationContract.View::showQueryTypeError);
            return false;
        } else if (dataManager.getCurrentLanguage().equalsIgnoreCase(ISO_CODE_ENG)) {
            if (!submitRequest.getFullName().matches(NAME)) {
                ifViewAttached(ContactUsRegistrationContract.View::showInvalidNameError);
                return false;
            }
        } else if (!validateMessage(submitRequest.getDescription())) {
            return false;
        } else if (submitRequest.getMobile().length() < 13) {
            ifViewAttached(ContactUsRegistrationContract.View::showInvalidMobileError);
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(submitRequest.getEmail()).matches()) {
            ifViewAttached(ContactUsRegistrationContract.View::showInvalidEmailError);
            return false;
        }
        return valid;
    }

    private boolean validateMessage(String message) {
        if (ValidationUtil.isNullOrEmpty(message)) {
            ifViewAttached(ContactUsRegistrationContract.View::showQueryMessagesError);
            return false;
       /* } else if (!ValidationUtil.isValidAlpha(message, AppConstants.ValidationRegex.ALPHANUMERIC)) {
            ifViewAttached(ContactUsRegistrationContract.View::showQueryMessagesValidError);
            return false;*/
        }
        return true;
    }
}