package com.aecb.ui.addcvv.presenter;

import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.DataManager;
import com.aecb.data.api.models.adcb.MakePayment6Response;
import com.aecb.data.api.models.commonmessageresponse.UserCases;
import com.aecb.data.api.models.payment_method.AddCardRequestBody;
import com.aecb.util.AESEncryption;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.aecb.AppConstants.generateRandomID;
import static com.aecb.data.api.models.commonmessageresponse.UserCases.ADD_CARD;
import static com.aecb.data.api.models.commonmessageresponse.UserCases.AUTHENTICATION_FAILED;
import static com.aecb.data.api.models.commonmessageresponse.UserCases.PAYMENT_DECLINED;
import static com.aecb.util.Utilities.fullString;
import static com.aecb.util.varibles.StringConstants.CONST_YES;

public class AddCvvPresenterImpl extends MvpBasePresenterImpl<AddCvvContract.View>
        implements AddCvvContract.Presenter {

    private DataManager mDataManager;
    String transactionID = "";

    @Inject
    public AddCvvPresenterImpl(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void addCard(String cardNumber, String expireDate, String cvv, String fullName,
                        String paymentType, String screenFrom, String isDefault) {
        addNewADCBCard(createBodyRequestForConfigureCard(fullName, expireDate, cardNumber, cvv, CONST_YES,
                isDefault, paymentType));
    }


    public AddCardRequestBody createBodyRequestForConfigureCard(String fullName, String expireDate,
                                                                String cardNumber, String cvv,
                                                                String isConsent, String isPymt,
                                                                String paymentType) {
        int finalPrice = 1;
        AddCardRequestBody addCardRequestBody = new AddCardRequestBody();
        addCardRequestBody.setOperationType(paymentType);
        addCardRequestBody.setCardholderName(fullName);
        addCardRequestBody.setAmount(String.valueOf(finalPrice));
        addCardRequestBody.setExpiry(expireDate);
        addCardRequestBody.setCardNumber(cardNumber.replaceAll(" ", ""));
        addCardRequestBody.setCvv(cvv);
        addCardRequestBody.setConsent(isConsent);
        addCardRequestBody.setTransactionID(generateRandomID());
        addCardRequestBody.setPymtCardIsDefault(isPymt);
        return addCardRequestBody;
    }

    public void addNewADCBCard(AddCardRequestBody addCardRequestBody) {
        String encryptedRequest = createEncryptedRequest(addCardRequestBody.toString());
        Timber.e("Plain Request == " + addCardRequestBody.toString());
        Timber.e("Encrypted Request == " + encryptedRequest);
        Timber.e("Decrypted Request ==" + createDecryptedRequest(encryptedRequest));
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<MakePayment6Response> myAppDisposableObserver =
                    new MyAppDisposableObserver<MakePayment6Response>(view, UserCases.ADD_CARD_FROM_HTTP) {
                        @Override
                        protected void onSuccess(Object response) {
                            view.hideLoading();
                            MakePayment6Response addCardResponse = (MakePayment6Response) response;
                            if (addCardResponse.isSuccess()) {
                                transactionID = addCardResponse.getTransactionID();
                                if (addCardResponse.getData() != null) {
                                    Timber.d(addCardResponse.toString());
                                    view.openADCBWebview(addCardResponse.getData().getJsonMember3DSecure().getAuthenticationRedirect()
                                            .getSimple().getHtmlBodyContent(), transactionID);
                                } else {
                                    view.showApiFailureError(addCardResponse.getMessage(), addCardResponse.getStatus(), "");
                                }
                            } else {
                                if (addCardResponse.getMessage().equalsIgnoreCase("Card Details Already Configured For The User")) {
                                    view.showApiFailureError(addCardResponse.getMessage(), addCardResponse.getStatus(), ADD_CARD);
                                } else if (addCardResponse.getMessage().equalsIgnoreCase("Authentication Failed")) {
                                    view.showApiFailureError(addCardResponse.getMessage(), addCardResponse.getStatus(), AUTHENTICATION_FAILED);
                                } else if (addCardResponse.getMessage().equalsIgnoreCase("Payment declined")) {
                                    view.showApiFailureError(addCardResponse.getMessage(), addCardResponse.getStatus(), PAYMENT_DECLINED);
                                } else {
                                    view.showApiFailureError(addCardResponse.getMessage(), "", "");
                                }
                            }
                        }
                    };

            MyAppDisposableObserver<MakePayment6Response> disposableObserver =
                    dataManager.addADCBCardAndMakePayment(encryptedRequest)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    public String createEncryptedRequest(String requestBody) {
        AESEncryption.setKeyNew(fullString());
        AESEncryption.encrypt(requestBody);
        return AESEncryption.getEncryptedString()
                .replaceAll("\\s", "")
                .replaceAll("\\n", "")
                .replaceAll("\\r", "");
    }

    public String createDecryptedRequest(String requestBody) {
        AESEncryption.setKeyNew(fullString());
        AESEncryption.decrypt(requestBody);
        return AESEncryption.getDecryptedString();
    }

}
