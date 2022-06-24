package com.aecb.ui.contactusregistration.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.configuration.CaseTypesItem;
import com.aecb.data.api.models.contactus.ContactUsSubmitRequest;

import java.util.List;

public interface ContactUsRegistrationContract {
    interface View extends BaseView {
        void setReasons(List<CaseTypesItem> caseTypes, String currentLanguage);

        void showInvalidEmailError();

        void showInvalidMobileError();

        void showInvalidNameError();

        void showQueryMessagesError();

        void showQueryMessagesValidError();

        void showQueryTypeError();

        void showMessage(String message);

        void resetData();

        void showEmptySubjectError();

        void showQuerySubjectValidError();
    }

    interface Presenter extends BasePresenter<View> {
        void getReasons();

        void submitContactUsQuery(ContactUsSubmitRequest contactUsSubmitRequest);
    }
}