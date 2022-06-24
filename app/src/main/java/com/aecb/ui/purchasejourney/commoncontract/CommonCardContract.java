package com.aecb.ui.purchasejourney.commoncontract;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.buycreditreport.BuyCreditReportResponse;
import com.aecb.data.api.models.emailrecipients.EmailRecipientsRequest;
import com.aecb.data.api.models.payment_method.AddCardResponse;

public interface CommonCardContract {
    interface View extends BaseView {
        void displayPaymentSuccessDialog(BuyCreditReportResponse buyCreditReportResponse);

        void displayPaymentFailedDialog(BuyCreditReportResponse buyCreditReportResponse);

        void openDashboardScreen();

        void openVerifyPaymentScreen(AddCardResponse addCardResponse, String screenFrom,
                                     BuyCreditReportResponse buyCreditReportResponse);
    }

    interface Presenter<V extends View> extends BasePresenter<V> {
        void getLastLoginUser();

        void callSendAdditionalEmailsApi(EmailRecipientsRequest recipientsRequest);
    }
}
