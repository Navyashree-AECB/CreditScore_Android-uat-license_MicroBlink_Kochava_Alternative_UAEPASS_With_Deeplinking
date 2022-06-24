package com.aecb.ui.purchasejourney.cardlist.presenter;

import com.aecb.AppConstants;
import com.aecb.base.db.DbDisposableObserver;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.api.models.BaseResponse;
import com.aecb.data.api.models.cards.CreditCardData;
import com.aecb.data.api.models.cards.CreditCardList;
import com.aecb.data.api.models.commonmessageresponse.UserCases;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.data.api.models.payment_method.ActivateCardRequestBody;
import com.aecb.data.api.models.payment_method.AddCardResponse;
import com.aecb.data.api.models.payment_method.DeleteCardRequestBody;
import com.aecb.data.api.models.request.updateuserprofile.UpdateUserProfileRequest;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.ui.purchasejourney.commoncontract.CommonCardPresenterImpl;
import com.aecb.util.AESEncryption;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;
import timber.log.Timber;

import static com.aecb.AppConstants.ApiParameter.CARD_DELETE;
import static com.aecb.AppConstants.IntentKey.FROM_ADD_RECIPIENTS;
import static com.aecb.AppConstants.PaymentMethods.PAYMENT_GW;
import static com.aecb.data.api.models.commonmessageresponse.UserCases.VERIFY_AMOUNT;
import static com.aecb.util.Utilities.fullString;
import static com.aecb.util.Utilities.getStringFromErrorBody;
import static com.aecb.util.varibles.StringConstants.CONST_NO;
import static com.aecb.util.varibles.StringConstants.CONST_PYMT;
import static com.aecb.util.varibles.StringConstants.CONST_YES;

public class CardListPresenterImpl extends CommonCardPresenterImpl<CardListContract.View> implements
        CardListContract.Presenter {

    List<CreditCardData> allCardList = new ArrayList<>();
    DBUserTC dbUserTC;
    UpdateUserProfileRequest updateUserProfileRequest;

    @Inject
    public CardListPresenterImpl() {
    }

    @Override
    public void getCardList() {
        ifViewAttached(view -> {
            getCreditCards();
        });
    }

    public void getCreditCards() {
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<Response<ResponseBody>> observer = new MyAppDisposableObserver<Response<ResponseBody>>(view) {

                @Override
                protected void onSuccess(Object response) {
                    ResponseBody responseBody = (ResponseBody) response;
                    try {
                        String encryptedBody = getStringFromErrorBody(responseBody.bytes()).toString();
                        AESEncryption.setKeyNew(fullString());
                        AESEncryption.decrypt(encryptedBody);

                        String decryptedBody = AESEncryption.getDecryptedString();
                        Gson gson = new Gson();
                        CreditCardList list = gson.fromJson(decryptedBody, CreditCardList.class);
                        allCardList.clear();
                        allCardList.addAll(list.getcreditCardDataList());
                        view.hideLoading();
                        AtomicInteger defaultCardPosition = new AtomicInteger(0);
                        for (CreditCardData creditCardData : allCardList) {
                            if (creditCardData.getPymtCardIsDefault().equalsIgnoreCase(AppConstants.YES)) {
                                defaultCardPosition.set(allCardList.indexOf(creditCardData));
                            }
                        }
                        if (allCardList.size() > 1) {
                            try {
                                Collections.swap(allCardList, 0, defaultCardPosition.get());
                            } catch (Exception e) {
                                Timber.e(e.getMessage());
                            }
                        }
                        view.setCreditDataIntoList(allCardList, dbUserTC);
                    } catch (Exception e) {
                        view.hideLoading();
                        Timber.d("Exception : " + e.toString());
                    }

                }
            };
            MyAppDisposableObserver<Response<ResponseBody>> disposableObserver = dataManager.getCreditCards()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(observer);
            compositeDisposable.add(disposableObserver);
        });
    }

    public void updateCard(DeleteCardRequestBody deleteCardRequestBody) {
        String encryptedRequest = createEncryptedRequest(deleteCardRequestBody.toString());
        Timber.e("Plain Request == " + deleteCardRequestBody.toString());
        Timber.e("Encrypted Request == " + encryptedRequest);
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<BaseResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<BaseResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            view.hideLoading();
                            BaseResponse baseResponse = (BaseResponse) response;
                            if (baseResponse != null) {
                                if (baseResponse.isSuccess()) {
                                    if (deleteCardRequestBody.getOperationType().equals(CARD_DELETE)) {
                                        view.showApiSuccessMessage(baseResponse.getMessage(), baseResponse.getStatus(), UserCases.DELETE_CARD);
                                        getCreditCards();
                                    } else {
                                        view.updatePaymentMethodForNI();
                                    }
                                } else
                                    view.showApiFailureError(baseResponse.getMessage(), baseResponse.getStatus(), UserCases.DELETE_CARD);
                            }
                        }
                    };

            MyAppDisposableObserver<BaseResponse> disposableObserver =
                    dataManager.manageCard(encryptedRequest)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    @Override
    public void updatePaymentMethod(UpdateUserProfileRequest updateUserProfileRequest, String preferredPaymentMethod) {
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<BaseResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<BaseResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            BaseResponse baseResponse = (BaseResponse) response;
                            if (baseResponse.getStatus().equals("uu_03") ||
                                    baseResponse.getStatus().equals("uu_04") ||
                                    baseResponse.getStatus().equals("uu_05") ||
                                    baseResponse.getStatus().equals("uu_06") ||
                                    baseResponse.getStatus().equals("uu_07") ||
                                    baseResponse.getStatus().equals("uu_08") ||
                                    baseResponse.getStatus().equals("uu_09")) {
                                updateUserDetails(preferredPaymentMethod);
                                if (!baseResponse.getMessage().equalsIgnoreCase("uu_08")) {
                                    view.hideLoading();
                                    getCreditCards();
                                }
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
                            getLastLoginUser();
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
    public void addCard(String cardNumber, String expireDate, String fullName, boolean isCardForSave,
                        boolean isAcceptTermConditions, boolean isShowError, boolean isDefault,
                        ProductsItem productsItem, String paymentType, String screenFrom) {
        callBuyCreditReport(productsItem, createBodyRequestForConfigureCard(fullName, expireDate, cardNumber, null,
                getResult(isCardForSave), getResultForDefault(isDefault), paymentType, productsItem), "");
    }

    public String getResult(boolean value) {
        if (value) {
            return CONST_YES;
        } else {
            return CONST_NO;
        }
    }

    private String getResultForDefault(boolean isDefault) {
        if (isDefault) {
            return CONST_PYMT;
        } else {
            return CONST_NO;
        }
    }

    @Override
    public void getLastLoginUser() {
        ifViewAttached(view -> {
            DbDisposableObserver<DBUserTC> myAppDisposableObserver = new
                    DbDisposableObserver<DBUserTC>() {
                        @Override
                        protected void onSuccess(Object response) {
                            dbUserTC = (DBUserTC) response;
                            view.updatedUserDB(dbUserTC);
                            getCardList();
                        }
                    };

            DbDisposableObserver<DBUserTC> disposableObserver = dataManager.getLastLoginUser()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    @Override
    public void activateCard(ActivateCardRequestBody activateCardRequestBody, String screenFrom, String constPymt) {
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
                                if (screenFrom.equals(FROM_ADD_RECIPIENTS)) {
                                    if (manageCardResponse.isSuccess()) {
                                        view.hideLoading();
                                        view.goToCheckoutScreen();
                                    } else {
                                        view.hideLoading();
                                        view.showAuthorizationFailureError(manageCardResponse.getMessage(), manageCardResponse.getStatus(), VERIFY_AMOUNT);
                                    }
                                } else {
                                    if (manageCardResponse.isSuccess()) {
                                        view.hideLoading();
                                        updateUserProfileRequest = new UpdateUserProfileRequest();
                                        updateUserProfileRequest.setChannel(2);
                                        updateUserProfileRequest.setUpdateType(8);
                                        updateUserProfileRequest.setPreferredPaymentMethod(PAYMENT_GW);
                                        updatePaymentMethod(updateUserProfileRequest, PAYMENT_GW);
                                    } else {
                                        view.hideLoading();
                                        view.showAuthorizationFailureError(manageCardResponse.getMessage(), manageCardResponse.getStatus(), VERIFY_AMOUNT);
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
}