package com.aecb.ui.purchasejourney.addcard.presenter;

import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.base.db.DbDisposableObserver;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.api.models.BaseResponse;
import com.aecb.data.api.models.adcb.MakePayment6Response;
import com.aecb.data.api.models.buycreditreport.BuyCreditReportResponse;
import com.aecb.data.api.models.commonmessageresponse.UserCases;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.data.api.models.payment_method.ActivateCardRequestBody;
import com.aecb.data.api.models.payment_method.AddCardRequestBody;
import com.aecb.data.api.models.payment_method.AddCardResponse;
import com.aecb.data.api.models.request.updateuserprofile.UpdateUserProfileRequest;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.ui.purchasejourney.commoncontract.CommonCardPresenterImpl;
import com.aecb.util.ValidationUtil;
import com.aecb.util.varibles.StringConstants;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.aecb.AppConstants.IntentKey.FROM_ADD_RECIPIENTS;
import static com.aecb.AppConstants.IntentKey.FROM_MENU_SCREEN;
import static com.aecb.AppConstants.PaymentMethods.PAYMENT_GW;
import static com.aecb.data.api.models.commonmessageresponse.UserCases.ADD_CARD;
import static com.aecb.data.api.models.commonmessageresponse.UserCases.AUTHENTICATION_FAILED;
import static com.aecb.data.api.models.commonmessageresponse.UserCases.PAYMENT_DECLINED;
import static com.aecb.data.api.models.commonmessageresponse.UserCases.VERIFY_AMOUNT;
import static com.aecb.util.varibles.StringConstants.CONST_NO;
import static com.aecb.util.varibles.StringConstants.CONST_PYMT;
import static com.aecb.util.varibles.StringConstants.CONST_YES;
import static com.aecb.util.varibles.StringConstants.USER_SELECTED_CARD_ACTIVATED_SUCCESSFULLY_EN;

public class AddCardPresenterImpl extends CommonCardPresenterImpl<AddCardContract.View> implements
        AddCardContract.Presenter {

    UpdateUserProfileRequest updateUserProfileRequest;
    DBUserTC dbUserTC;
    String transactionID = "";

    @Inject
    public AddCardPresenterImpl() {
    }

    @Override
    public void addCard(String cardNumber, String expireDate, String cvv, String fullName,
                        boolean isCardForSave, boolean isAcceptTermConditions, boolean isShowError,
                        boolean isDefault, ProductsItem productsItem, String paymentType, String screenFrom) {
        if (checkValidations(cardNumber, expireDate, cvv, fullName, isCardForSave, isAcceptTermConditions, isShowError)) {
            if (screenFrom.equals(FROM_ADD_RECIPIENTS)) {
                addNewADCBCard(createBodyRequestForConfigureCard(fullName, expireDate, cardNumber, cvv, getResult(true),
                        getResultForDefault(true), paymentType, productsItem));
            } else if (screenFrom.equals(FROM_MENU_SCREEN)) {
                addNewADCBCard(createBodyRequestForConfigureCard(fullName, expireDate, cardNumber, cvv, getResult(true), getResultForDefault(isDefault), paymentType, productsItem));
            }
        }
    }

    @Override
    public String getCurrentLanguage() {
        return dataManager.getCurrentLanguage();
    }

    public void addNewCard(AddCardRequestBody addCardRequestBody) {
        String encryptedRequest = createEncryptedRequest(addCardRequestBody.toString());
        Timber.e("Plain Request == " + addCardRequestBody.toString());
        Timber.e("Encrypted Request == " + encryptedRequest);
        Timber.e("Decrypted Request ==" + createDecryptedRequest(encryptedRequest));
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<AddCardResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<AddCardResponse>(view, UserCases.ADD_CARD_FROM_HTTP) {
                        @Override
                        protected void onSuccess(Object response) {
                            view.hideLoading();
                            AddCardResponse addCardResponse = (AddCardResponse) response;
                            Timber.d(addCardResponse.toString());
                            if (addCardResponse.isSuccess()) {
                                view.goToVerifyAmountScreen(addCardResponse);
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

            MyAppDisposableObserver<AddCardResponse> disposableObserver =
                    dataManager.addCardAndMakePayment(encryptedRequest)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
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
                                            .getSimple().getHtmlBodyContent());
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
                                    view.showApiFailureError(addCardResponse.getMessage(),  "", "");
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

    private String getResultForDefault(boolean isDefault) {
        if (isDefault) {
            return CONST_PYMT;
        } else {
            return CONST_NO;
        }
    }

    public String getResult(boolean value) {
        if (value) {
            return CONST_YES;
        } else {
            return CONST_NO;
        }
    }

    @Override
    public void displayContinueButton(String cardNumber, String expireDate, String cvv, String fullName,
                                      boolean isCardForSave, boolean isAcceptTermConditions, boolean isShowError) {
        ifViewAttached(view -> {
            if (checkNullValueVerification(fullName, cardNumber, expireDate, cvv)) {
                view.showContinueButton(true);
            } else
                view.showContinueButton(false);
        });
    }

    private boolean checkNullValueVerification(String fullName, String cardNumber, String expireDate,
                                               String cvv) {
        if (ValidationUtil.isNullOrEmpty(cardNumber)) {
            return false;
        }
        if (ValidationUtil.isNullOrEmpty(fullName)) {
            return false;
        }
        if (ValidationUtil.isNullOrEmpty(expireDate)) {
            return false;
        }
        return !ValidationUtil.isNullOrEmpty(cvv);
    }

    @Override
    public boolean checkValidations(String cardNumber, String expireDate, String cvv, String fullName,
                                    boolean isCardForSave, boolean isAcceptTermConditions, boolean isShowError) {
        AtomicBoolean isValid = new AtomicBoolean(false);
        ifViewAttached(view -> {
            if (!validateCardNumber(cardNumber, isShowError)) {
                isValid.set(false);
            } else if (!validateName(fullName, isShowError)) {
                isValid.set(false);
            } else if (!validateExpiryDate(expireDate, isShowError)) {
                isValid.set(false);
            } else if (!validateCvv(cvv, isShowError)) {
                isValid.set(false);
            } else {
                isValid.set(true);
            }
        });
        return isValid.get();
    }

    /**
     * check card card expiry is valid or not
     *
     * @param expiryDate  card expiry date
     * @param isShowError
     * @return boolean (true - valid and false not valid)
     */
    private boolean validateExpiryDate(String expiryDate, boolean isShowError) {
        if (ValidationUtil.isNullOrEmpty(expiryDate)) {
            if (isShowError)
                ifViewAttached(view -> {
                    view.showExpiryDateError(view.getContext().getResources().getString(R.string.txt_msg_enter_expiry_date));
                });
            return false;
        } else if (!ValidationUtil.checkMinTextValidation(expiryDate, 5)) {
            if (isShowError)
                ifViewAttached(view -> {
                    view.showExpiryDateError(view.getContext().getResources().getString(R.string.txt_msg_expiry_date_min_length));
                });
            return false;
        }
        return true;
    }

    /**
     * check card card cvv is valid or not
     *
     * @param cvv         card cvv number
     * @param isShowError
     * @return boolean (true - valid and false not valid)
     */
    private boolean validateCvv(String cvv, boolean isShowError) {
        if (ValidationUtil.isNullOrEmpty(cvv)) {
            if (isShowError)
                ifViewAttached(view -> {
                    view.showCvvEmptyMessages(view.getContext().getResources().getString(R.string.txt_msg_enter_cvv));
                });
            return false;
        } else if (!ValidationUtil.checkMinTextValidation(cvv, 3)) {
            if (isShowError)
                ifViewAttached(view -> {
                    view.showCvvEmptyMessages(view.getContext().getResources().getString(R.string.txt_msg_cvv_min_length));
                });
            return false;
        } else {
            return true;
        }

    }

    /**
     * check card holder name is valid or not
     *
     * @param name        card holder name
     * @param isShowError
     * @return boolean (true - valid and false not valid)
     */
    private boolean validateName(String name, boolean isShowError) {
        if (ValidationUtil.isNullOrEmpty(name)) {
            if (isShowError)
                ifViewAttached(view -> {
                    view.showFullNameError(view.getContext().getResources().getString(R.string.please_enter_full_name));
                });
            return false;
        } else if (!ValidationUtil.isValidAlpha(name, AppConstants.ValidationRegex.ALPHA)) {
            if (isShowError)
                ifViewAttached(view -> {
                    view.showFullNameError(view.getContext().getResources().getString(R.string.name_should_be_alphabetic));
                });
            return false;
        }
        return true;
    }

    /**
     * check card number is valid or not
     *
     * @param cardNumber  card number
     * @param isShowError
     * @return boolean (true - valid and false not valid)
     */
    private boolean validateCardNumber(String cardNumber, boolean isShowError) {
        if (ValidationUtil.isNullOrEmpty(cardNumber)) {
            if (isShowError)
                ifViewAttached(view -> {
                    view.showCardNumberError(view.getContext().getResources().getString(R.string.please_enter_card_number));
                });
            return false;
        } else if (!ValidationUtil.checkMinTextValidation(cardNumber, 19)) {
            if (isShowError)
                ifViewAttached(view -> {
                    view.showCardNumberError(view.getContext().getResources().getString(R.string.txt_msg_valid_card_number));
                });
            return false;
        }
        return true;
    }

    @Override
    public void activateCard(ActivateCardRequestBody activateCardRequestBody, /*BaseResponse baseResponse,*/
                             BuyCreditReportResponse buyCreditReportResponse, String screenType, boolean isDefaultCard) {
        activateCardRequestBody.setTransactionID(transactionID);
        String encryptedRequest = createEncryptedRequest(activateCardRequestBody.toString());
        Timber.e("Plain Request == " + activateCardRequestBody.toString());
        Timber.e("Encrypted Request == " + encryptedRequest);
        ifViewAttached(view -> {
           // view.showLoading(null);
            MyAppDisposableObserver<AddCardResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<AddCardResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            AddCardResponse manageCardResponse = (AddCardResponse) response;
                            if (manageCardResponse != null) {
                                if (screenType.equals(FROM_ADD_RECIPIENTS)) {
                                    if (manageCardResponse.isSuccess()) {
                                        updateUserProfileRequest = new UpdateUserProfileRequest();
                                        updateUserProfileRequest.setChannel(2);
                                        updateUserProfileRequest.setUpdateType(8);
                                        updateUserProfileRequest.setPreferredPaymentMethod(PAYMENT_GW);
                                        updatePaymentMethod(updateUserProfileRequest, PAYMENT_GW, screenType, manageCardResponse.getMessage());
                                    } else {
                                        view.hideLoading();
                                        view.showApiFailureError(manageCardResponse.getMessage(), manageCardResponse.getStatus(), VERIFY_AMOUNT);
                                    }
                                } else {
                                    if (manageCardResponse.isSuccess()) {
                                        if (isDefaultCard) {
                                            updateUserProfileRequest = new UpdateUserProfileRequest();
                                            updateUserProfileRequest.setChannel(2);
                                            updateUserProfileRequest.setUpdateType(8);
                                            updateUserProfileRequest.setPreferredPaymentMethod(PAYMENT_GW);
                                            updatePaymentMethod(updateUserProfileRequest, PAYMENT_GW, screenType, manageCardResponse.getMessage());
                                        } else {
                                            view.hideLoading();
                                            if (manageCardResponse.getMessage().equalsIgnoreCase("user successfully authorized ")) {
                                                if (dataManager.getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ARABIC)) {
                                                    view.cardSavedSuccessfully(StringConstants.USER_SELECTED_CARD_ACTIVATED_SUCCESSFULLY_AR);
                                                } else {
                                                    view.cardSavedSuccessfully(USER_SELECTED_CARD_ACTIVATED_SUCCESSFULLY_EN);
                                                }
                                            } else {
                                                view.cardSavedSuccessfully(manageCardResponse.getMessage());
                                            }
                                        }
                                    } else {
                                        view.hideLoading();
                                        view.showApiFailureError(manageCardResponse.getMessage(), manageCardResponse.getStatus(), VERIFY_AMOUNT);
                                    }
                                }
                            }
                        }
                    };

            MyAppDisposableObserver<AddCardResponse> disposableObserver =
                    dataManager.manageCard(encryptedRequest)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    public void updatePaymentMethod(UpdateUserProfileRequest updateUserProfileRequest, String preferredPaymentMethod,
                                    String screenFrom, String message) {
        ifViewAttached(view -> {
            MyAppDisposableObserver<BaseResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<BaseResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            BaseResponse baseResponse = (BaseResponse) response;
                            view.hideLoading();
                            if (baseResponse.getStatus().equals("uu_03") ||
                                    baseResponse.getStatus().equals("uu_04") ||
                                    baseResponse.getStatus().equals("uu_05") ||
                                    baseResponse.getStatus().equals("uu_06") ||
                                    baseResponse.getStatus().equals("uu_07") ||
                                    baseResponse.getStatus().equals("uu_08") ||
                                    baseResponse.getStatus().equals("uu_09")) {
                                updateUserDetails(preferredPaymentMethod);
                                if (screenFrom.equals(FROM_ADD_RECIPIENTS)) {
                                    view.goToCheckoutScreen();
                                } else {
                                    if (message.equalsIgnoreCase("user successfully authorized ")) {
                                        if (dataManager.getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ARABIC)) {
                                            view.cardSavedSuccessfully(StringConstants.USER_SELECTED_CARD_ACTIVATED_SUCCESSFULLY_AR);
                                        } else {
                                            view.cardSavedSuccessfully(USER_SELECTED_CARD_ACTIVATED_SUCCESSFULLY_EN);
                                        }
                                    } else {
                                        view.cardSavedSuccessfully(message);

                                    }
                                }
                            } else {
                                view.hideLoading();
                            }
                        }
                    };

            MyAppDisposableObserver<BaseResponse> disposableObserver = dataManager.updateUserProfile(updateUserProfileRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    private void updateUserDetails(String preferredPaymentMethod) {
        ifViewAttached(view -> {
            DbDisposableObserver<Long> myAppDisposableObserver = new
                    DbDisposableObserver<Long>() {
                        @Override
                        protected void onSuccess(Object response) {
                            view.hideLoading();
                        }
                    };
            DbDisposableObserver<Long> disposableObserver = dataManager.updatePreferredPaymentMethod(dbUserTC.getUserName(), preferredPaymentMethod)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    @Override
    public void getLastLoginUser() {
        ifViewAttached(view -> {
            DbDisposableObserver<DBUserTC> myAppDisposableObserver = new
                    DbDisposableObserver<DBUserTC>() {
                        @Override
                        protected void onSuccess(Object response) {
                            dbUserTC = (DBUserTC) response;
                        }
                    };

            DbDisposableObserver<DBUserTC> disposableObserver = dataManager.getLastLoginUser()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }
}