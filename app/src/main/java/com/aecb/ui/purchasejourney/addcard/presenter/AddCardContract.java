package com.aecb.ui.purchasejourney.addcard.presenter;

import android.content.Context;

import com.aecb.data.api.models.BaseResponse;
import com.aecb.data.api.models.buycreditreport.BuyCreditReportResponse;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.data.api.models.payment_method.ActivateCardRequestBody;
import com.aecb.data.api.models.payment_method.AddCardResponse;
import com.aecb.ui.purchasejourney.commoncontract.CommonCardContract;

public interface AddCardContract {
    interface View extends CommonCardContract.View {

        Context getContext();

        void showCardNumberError(String string);

        void showFullNameError(String string);

        void showExpiryDateError(String string);

        void showCvvEmptyMessages(String string);

        void showContinueButton(boolean b);

        void showTermsConditionError();

        void goToVerifyAmountScreen(AddCardResponse updatePaymentResponse);

        void openADCBWebview(String htmlBodyContent);

        void cardSavedSuccessfully(String message);

        void goToCheckoutScreen();
    }

    interface Presenter extends CommonCardContract.Presenter<View> {

        boolean checkValidations(String cardNumber, String expireDate, String cvv, String fullName,
                                 boolean isCardForSave, boolean isAcceptTermConditions, boolean isShowError);

        void displayContinueButton(String cardNumber, String expireDate, String cvv, String fullName,
                                   boolean isCardForSave, boolean isAcceptTermConditions, boolean isShowError);

        void addCard(String cardNumber, String expireDate, String cvv, String fullName, boolean isCardForSave,
                     boolean isAcceptTermConditions, boolean isShowError, boolean isDefault,
                     ProductsItem productsItem, String paymentType, String screenFrom);

        String getCurrentLanguage();

        void activateCard(ActivateCardRequestBody activateCardRequestBody, /*BaseResponse baseResponse,*/
                          BuyCreditReportResponse buyCreditReportResponse, String screenType, boolean isDefaultCard);

        void getLastLoginUser();

    }

}