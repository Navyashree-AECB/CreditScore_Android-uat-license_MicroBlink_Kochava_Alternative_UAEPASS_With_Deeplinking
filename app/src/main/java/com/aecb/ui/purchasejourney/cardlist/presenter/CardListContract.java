package com.aecb.ui.purchasejourney.cardlist.presenter;

import com.aecb.data.api.models.cards.CreditCardData;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.data.api.models.payment_method.ActivateCardRequestBody;
import com.aecb.data.api.models.payment_method.DeleteCardRequestBody;
import com.aecb.data.api.models.request.updateuserprofile.UpdateUserProfileRequest;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.ui.purchasejourney.commoncontract.CommonCardContract;

import java.util.List;

public interface CardListContract {
    interface View extends CommonCardContract.View {
        void setCreditDataIntoList(List<CreditCardData> list, DBUserTC dbUserTC);

        void showError(String message);

        void paymentMethodUpdateSuccessfully();

        void updatePaymentMethodForNI();

        void updatedUserDB(DBUserTC dbUserTC);

        void goToCheckoutScreen();

        void showAuthorizationFailureError(String message, String status, String useCase);
    }

    interface Presenter extends CommonCardContract.Presenter<View> {
        void getCardList();

        void getLastLoginUser();

        void updateCard(DeleteCardRequestBody deleteCardRequestBody);

        void updatePaymentMethod(UpdateUserProfileRequest updateUserProfileRequest, String preferredPaymentMethod);

        void addCard(String cardNumber, String expireDate, String fullName, boolean
                isCardForSave, boolean isAcceptTermConditions, boolean isShowError,
                     boolean isDefault, ProductsItem productsItem, String paymentType, String screenFrom);

        void activateCard(ActivateCardRequestBody activateCardRequestBody, String screenFrom, String constPymt);
    }
}