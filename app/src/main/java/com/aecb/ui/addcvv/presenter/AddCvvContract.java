package com.aecb.ui.addcvv.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;

public interface AddCvvContract {
    interface View extends BaseView {

        void openADCBWebview(String htmlBodyContent, String transactionID);
    }

    interface Presenter extends BasePresenter<View> {
        void addCard(String cardNumber, String expireDate, String cvv, String fullName,
                     String paymentType, String screenFrom, String isDefault);
    }
}
