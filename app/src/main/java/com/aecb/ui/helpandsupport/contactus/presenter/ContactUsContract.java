package com.aecb.ui.helpandsupport.contactus.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.configuration.CaseTypesItem;
import com.aecb.data.api.models.contactus.ContactUsSubmitRequest;

import java.util.List;

public interface ContactUsContract {
    interface View extends BaseView {
        void setReasons(List<CaseTypesItem> caseTypes, String currentLanguage);

        void resetData();

        void showMessage(String message);

        void showQueryTypeError();

        void showQuerySubjectError();

        void showQuerySubjectValidError();

        void showQueryMessagesError();

        void showQueryMessagesValidError();
    }

    interface Presenter extends BasePresenter<View> {
        void getReasons();

        void submitContactUsQuery(ContactUsSubmitRequest querySubmitRequest);
    }
}