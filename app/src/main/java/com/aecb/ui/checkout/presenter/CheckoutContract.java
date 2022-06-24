package com.aecb.ui.checkout.presenter;

import androidx.lifecycle.LifecycleOwner;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.buycreditreport.BuyCreditReportResponse;
import com.aecb.data.api.models.cards.CreditCardData;
import com.aecb.data.api.models.edirham.CheckoutStatusRequest;
import com.aecb.data.api.models.edirham.PurchaseCheckoutRequest;
import com.aecb.data.api.models.emailrecipients.EmailRecipientsRequest;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.ui.purchasejourney.cardlist.presenter.CardListContract;
import com.aecb.ui.purchasejourney.commoncontract.CommonCardContract;

import java.util.List;

public interface CheckoutContract {
    interface View extends BaseView {
        LifecycleOwner getLifeCycleOwner();

        void setProductList(List<ProductsItem> productList, String currentLanguage);

        void setCardList(List<CreditCardData> getcreditCardDataList, DBUserTC dbUserTC);

        void displayPaymentSuccessDialog(BuyCreditReportResponse buyCreditReportResponse);

        void displayPaymentFailedDialog(BuyCreditReportResponse buyCreditReportResponse);

        void setIsAppFree(boolean isAppFree);

        void openEDirhamWebview(String checkoutId, String checkoutUrl);

        void showBlurView();

        void hideBlurView();

        void scoreNotAvailableError();
    }

    interface Presenter extends BasePresenter<View> {
        void getConfiguration();

        void getLastLoginUser();

        void getProducts();

        String getCurrentLanguage();

        void startPurchase(String cardNumber, String expireDate, String fullName, boolean
                isCardForSave, boolean isAcceptTermConditions, boolean isShowError,
                           boolean isDefault, ProductsItem productsItem, String paymentType);

        void callSendAdditionalEmailsApi(EmailRecipientsRequest recipientsRequest);

        void callBuyCreditReportForFree(ProductsItem finalProduct);

        String getInstallSource();

        String getInstalledCampaignId();

        boolean isNormalInstall();

        void callPurchaseCheckout(PurchaseCheckoutRequest purchaseCheckoutRequest);

        void getCheckOutStatus(CheckoutStatusRequest checkoutStatusRequest);

        void startEDirhamPurchase(ProductsItem finalProduct, String price);
    }
}