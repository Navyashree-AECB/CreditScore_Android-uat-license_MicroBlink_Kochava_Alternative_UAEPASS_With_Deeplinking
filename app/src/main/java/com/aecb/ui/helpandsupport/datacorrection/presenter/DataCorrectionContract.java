package com.aecb.ui.helpandsupport.datacorrection.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.banklist.BankListItem;
import com.aecb.data.api.models.contactus.ContactUsSubmitRequest;

import java.util.List;

public interface DataCorrectionContract {
    interface View extends BaseView {
        void setBankList(List<BankListItem> bankList, String currentLanguage);

        void showSelectBankError();

        void showQuerySubjectError();

        void showQuerySubjectValidError();

        void showQueryMessagesError();

        void showQueryMessagesValidError();

        void showMessage(String message);

        void resetData();

        void openEmail();
    }

    interface Presenter extends BasePresenter<View> {
        void loadBankList();

        void submitContactUsQuery(ContactUsSubmitRequest contactUsSubmitRequest);
    }
}