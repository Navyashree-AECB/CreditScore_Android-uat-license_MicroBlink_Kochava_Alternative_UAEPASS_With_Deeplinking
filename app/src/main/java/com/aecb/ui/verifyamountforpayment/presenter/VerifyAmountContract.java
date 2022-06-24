package com.aecb.ui.verifyamountforpayment.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.BaseResponse;
import com.aecb.data.api.models.buycreditreport.BuyCreditReportResponse;
import com.aecb.data.api.models.emailrecipients.EmailRecipientsRequest;
import com.aecb.data.api.models.payment_method.ActivateCardRequestBody;
import com.aecb.data.api.models.payment_method.AddCardResponse;

public interface VerifyAmountContract {
    interface View extends BaseView {
        void cardSavedSuccessfully(String message);

        void displayPaymentSuccessDialog(BuyCreditReportResponse buyCreditReportResponse);

        void displayPaymentFailedDialog(BuyCreditReportResponse buyCreditReportResponse);

        void openDashboardScreen();

        void goToCheckoutScreen();
    }

    interface Presenter extends BasePresenter<View> {
        void activateCard(ActivateCardRequestBody activateCardRequestBody, BaseResponse baseResponse,
                          BuyCreditReportResponse buyCreditReportResponse, String screenFrom, boolean isDefaultCard);

        void callSendAdditionalEmailsApi(EmailRecipientsRequest recipientsRequest);

        void getLastLoginUser();
    }
}