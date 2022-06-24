package com.aecb.ui.helpandsupport.contactus.presenter;

import com.aecb.AppConstants;
import com.aecb.base.db.DbDisposableObserver;
import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.DataManager;
import com.aecb.data.api.models.BaseResponse;
import com.aecb.data.api.models.commonmessageresponse.UserCases;
import com.aecb.data.api.models.configuration.CaseTypesItem;
import com.aecb.data.api.models.configuration.ConfigurationData;
import com.aecb.data.api.models.contactus.ContactUsSubmitRequest;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.util.ValidationUtil;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.aecb.AppConstants.AppLanguage.ISO_CODE_ENG;

public class ContactUsFragmentImpl extends MvpBasePresenterImpl<ContactUsContract.View>
        implements ContactUsContract.Presenter {

    DataManager mDataManger;

    @Inject
    public ContactUsFragmentImpl(DataManager dataManager) {
        this.mDataManger = dataManager;
    }

    @Override
    public void getReasons() {
        getLastLoginUser();
        ifViewAttached(view -> {
            ConfigurationData configurationData = dataManager.getConfigurationData();
            List<CaseTypesItem> caseTypes = configurationData.getCaseTypes();
            view.setReasons(caseTypes, dataManager.getCurrentLanguage());
        });
    }

    public void getLastLoginUser() {
        ifViewAttached(view -> {
            DbDisposableObserver<DBUserTC> myAppDisposableObserver = new
                    DbDisposableObserver<DBUserTC>() {
                        @Override
                        protected void onSuccess(Object response) {
                            DBUserTC dbUser = (DBUserTC) response;
                            if (dbUser != null) {
                                //view.setUser(dbUser);
                            }
                        }
                    };

            DbDisposableObserver<DBUserTC> disposableObserver = dataManager.getLastLoginUser()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    @Override
    public void submitContactUsQuery(ContactUsSubmitRequest querySubmitRequest) {
        if (validate(querySubmitRequest)) {
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
                                        view.showApiSuccessMessage(contactUsQueryResponse.getMessage(), contactUsQueryResponse.getStatus(), UserCases.CONTACT_US);
                                        view.resetData();
                                    } else {
                                        view.showApiFailureError(contactUsQueryResponse.getMessage(), contactUsQueryResponse.getStatus(), UserCases.CONTACT_US);
                                    }
                                }
                            }
                        };

                MyAppDisposableObserver<BaseResponse> disposableObserver =
                        dataManager.submitContactUs(querySubmitRequest)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(myAppDisposableObserver);
                compositeDisposable.add(disposableObserver);
            });
        }
    }

    private boolean validate(ContactUsSubmitRequest submitRequest) {
        boolean valid = true;
        if (ValidationUtil.isNullOrEmpty(submitRequest.getReason())) {
            ifViewAttached(ContactUsContract.View::showQueryTypeError);
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
            ifViewAttached(ContactUsContract.View::showQuerySubjectError);
            return false;
       /* } else if (!ValidationUtil.isValidAlpha(subject, AppConstants.ValidationRegex.ALPHANUMERIC)) {
            ifViewAttached(ContactUsContract.View::showQuerySubjectValidError);
            return false;*/
        }
        return true;
    }

    private boolean validateMessage(String message) {
        if (ValidationUtil.isNullOrEmpty(message)) {
            ifViewAttached(ContactUsContract.View::showQueryMessagesError);
            return false;
        }
        return true;
    }
}