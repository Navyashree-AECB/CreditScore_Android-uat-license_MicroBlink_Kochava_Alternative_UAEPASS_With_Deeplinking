package com.aecb.ui.purchasejourney.commoncontract;

import com.aecb.base.db.DbDisposableObserver;
import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.api.models.BaseResponse;
import com.aecb.data.api.models.buycreditreport.BuyCreditReportRequest;
import com.aecb.data.api.models.buycreditreport.BuyCreditReportResponse;
import com.aecb.data.api.models.commonmessageresponse.UserCases;
import com.aecb.data.api.models.emailrecipients.EmailRecipientsRequest;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.data.api.models.payment_method.AddCardRequestBody;
import com.aecb.data.api.models.payment_method.AddCardResponse;
import com.aecb.data.api.models.payment_method.UpdatePaymentStatusRequestResponse;
import com.aecb.data.api.models.updatepaymentstatus.UpdatePaymentStatusItem;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.util.AESEncryption;
import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import timber.log.Timber;

import static com.aecb.AppConstants.IntentKey.FROM_ADD_RECIPIENTS;
import static com.aecb.AppConstants.generateRandomID;
import static com.aecb.util.Utilities.fullString;
import static com.aecb.util.Utilities.getCurrentDateInApiFormat;
import static com.aecb.util.Utilities.getStringFromErrorBody;

public class CommonCardPresenterImpl<V extends CommonCardContract.View> extends MvpBasePresenterImpl<V>
        implements CommonCardContract.Presenter<V> {

    public DBUserTC dbUser;

    public void callBuyCreditReport(ProductsItem productsItem, AddCardRequestBody bodyRequestForConfigNewCard,
                                    String screenFrom) {
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<BuyCreditReportResponse> observer = new MyAppDisposableObserver<BuyCreditReportResponse>(view) {
                @Override
                protected void onSuccess(Object response) {
                    BuyCreditReportResponse buyCreditReportResponse = ((BuyCreditReportResponse) response);
                    if (buyCreditReportResponse.isSuccess()) {
                        bodyRequestForConfigNewCard.setTransactionID(buyCreditReportResponse.getBuyCreditReportData().getPortalUserId());
                        addNewCard(bodyRequestForConfigNewCard, buyCreditReportResponse, screenFrom);
                    } else {
                        view.hideLoading();
                        view.showApiFailureError(buyCreditReportResponse.getMessage(), "", "");
                    }
                }
            };
            MyAppDisposableObserver<BuyCreditReportResponse> disposableObserver = dataManager.buyCreditReport(buyCreditReportRequest(productsItem.getId()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(observer);
            compositeDisposable.add(disposableObserver);

        });

    }

    BuyCreditReportRequest buyCreditReportRequest(String productId) {
        BuyCreditReportRequest buyCreditReportRequest = new BuyCreditReportRequest();
        buyCreditReportRequest.setMobile(dbUser.getMobile());
        buyCreditReportRequest.setFullName(dbUser.getFullName());
        buyCreditReportRequest.setEmiratesID(dbUser.getEmiratesId());
        buyCreditReportRequest.setDob(dbUser.getDob());
        if (dbUser.getGender() == 0) {
            buyCreditReportRequest.setGender("Male");
        } else {
            buyCreditReportRequest.setGender("Female");
        }
        buyCreditReportRequest.setEmail(dbUser.getEmail());
        buyCreditReportRequest.setPreferredlanguage(dataManager.getCurrentLanguage());
        buyCreditReportRequest.setReportType(productId);
        buyCreditReportRequest.setTcVersionNumber(dataManager.getTcVersionNumber());
        buyCreditReportRequest.setPassport(dbUser.getPassport());
        buyCreditReportRequest.setChannel(3);
        buyCreditReportRequest.setIsAuthenticated(true);
        buyCreditReportRequest.setNationality(dbUser.getNationalityId());
        return buyCreditReportRequest;
    }

    public void addNewCard(AddCardRequestBody addCardRequestBody,
                           BuyCreditReportResponse buyCreditReportResponse, String screenFrom) {
        String encryptedRequest = createEncryptedRequest(addCardRequestBody.toString());
        Timber.e("Plain Request == " + addCardRequestBody.toString());
        Timber.e("Encrypted Request == " + encryptedRequest);
        Timber.e("Decrypted Request ==" + createDecryptedRequest(encryptedRequest));
        ifViewAttached(view -> {
            MyAppDisposableObserver<AddCardResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<AddCardResponse>(view, UserCases.ADD_CARD_FROM_HTTP) {
                        @Override
                        protected void onSuccess(Object response) {
                            AddCardResponse addCardResponse = (AddCardResponse) response;
                            Timber.d(addCardResponse.toString());
                            if (screenFrom.equals(FROM_ADD_RECIPIENTS)) {
                                if (addCardResponse.isSuccess()) {
                                    view.openVerifyPaymentScreen(addCardResponse, screenFrom, buyCreditReportResponse);
                                } else {
                                    callUpdatePaymentStatus(buyCreditReportResponse, addCardResponse);
                                }
                            } else {
                                callUpdatePaymentStatus(buyCreditReportResponse, addCardResponse);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            if (throwable instanceof HttpException) {
                                AddCardResponse addCardResponse = null;
                                ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
                                String errorString = "";
                                try {
                                    errorString = getStringFromErrorBody(responseBody.bytes()).toString();
                                    addCardResponse = (new Gson()).fromJson(errorString, AddCardResponse.class);
                                } catch (Exception e) {
                                    Timber.e(e.getMessage());
                                }
                                callUpdatePaymentStatus(buyCreditReportResponse, addCardResponse);
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

    public AddCardRequestBody createBodyRequestForConfigureCard(String fullName, String expireDate,
                                                                String cardNumber, String cvv,
                                                                String isConsent, String isPymt,
                                                                String paymentType, ProductsItem productsItem) {

        double priceWithVAT;
        int finalPrice;
        if (productsItem != null) {
            finalPrice = 1;
        } else {
            finalPrice = 1;
        }


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

    public void callUpdatePaymentStatus(BuyCreditReportResponse buyCreditReportResponse,
                                        AddCardResponse addCardResponse) {

        UpdatePaymentStatusRequestResponse request = new UpdatePaymentStatusRequestResponse();
        UpdatePaymentStatusItem item = new UpdatePaymentStatusItem();
        item.setTransactionID(buyCreditReportResponse.getBuyCreditReportData().getPortalUserId());
        if (addCardResponse != null && addCardResponse.getCardResponseItems() != null) {
            item.setPaymentSource(addCardResponse.getCardResponseItems().getPaymentSource());
            if (addCardResponse.getCardResponseItems().getPaymentdate() != null) {
                item.setPaymentdate(addCardResponse.getCardResponseItems().getPaymentdate());
            } else {
                item.setPaymentdate(getCurrentDateInApiFormat());
            }
            if (addCardResponse.getCardResponseItems().getResultCode() != null) {
                item.setResultCode(addCardResponse.getCardResponseItems().getResultCode());
            } else {
                item.setResultCode("00");
            }
            if (addCardResponse.getCardResponseItems().getPaymentRef() != null) {
                item.setPaymentRef(addCardResponse.getCardResponseItems().getPaymentRef());
            } else {
                item.setPaymentRef("");
            }
            if (addCardResponse.getCardResponseItems().getResultMessage() != null) {
                item.setResultMessage(addCardResponse.getCardResponseItems().getResultMessage());
            } else {
                item.setResultMessage(addCardResponse.getMessage());
            }
        } else {
            item.setPaymentdate(getCurrentDateInApiFormat());
            item.setResultCode("00");
            item.setPaymentSource("");
            item.setPaymentRef("");
            item.setResultMessage(addCardResponse.getMessage());
        }

        request.setStatus(addCardResponse.getStatus());
        request.setMessage(addCardResponse.getMessage());
        if (addCardResponse.getRefId() != null) {
            request.setRefid(addCardResponse.getRefId());
        } else {
            request.setRefid("");
        }
        request.setSuccess(addCardResponse.isSuccess());
        request.setData(item);

        ifViewAttached(view -> {
            MyAppDisposableObserver<UpdatePaymentStatusRequestResponse> observer = new MyAppDisposableObserver<UpdatePaymentStatusRequestResponse>(view) {
                @Override
                protected void onSuccess(Object response) {
                    view.hideLoading();
                    UpdatePaymentStatusRequestResponse requestResponse = (UpdatePaymentStatusRequestResponse) response;
                    if (requestResponse.isSuccess()) {
                        if (addCardResponse.isSuccess()) {
                            view.displayPaymentSuccessDialog(buyCreditReportResponse);
                        } else {
                            view.displayPaymentFailedDialog(buyCreditReportResponse);
                        }
                    } else {
                        view.displayPaymentFailedDialog(buyCreditReportResponse);
                    }
                }
            };
            MyAppDisposableObserver disposableObserver =
                    dataManager.updatePaymentStatus(request)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(observer);
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
                            dbUser = (DBUserTC) response;
                        }
                    };

            DbDisposableObserver<DBUserTC> disposableObserver = dataManager.getLastLoginUser()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    @Override
    public void callSendAdditionalEmailsApi(EmailRecipientsRequest recipientsRequest) {
        ifViewAttached(view -> {
            MyAppDisposableObserver<BaseResponse> observer = new MyAppDisposableObserver<BaseResponse>(view) {
                @Override
                protected void onSuccess(Object response) {
                }

                @Override
                public void onError(Throwable throwable) {
//                    super.onError(throwable);
//                    view.openDashboardScreen();
                }
            };
            MyAppDisposableObserver disposableObserver =
                    dataManager.callSendAdditionalEmailsApi(recipientsRequest)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(observer);
            compositeDisposable.add(disposableObserver);
        });
    }
}