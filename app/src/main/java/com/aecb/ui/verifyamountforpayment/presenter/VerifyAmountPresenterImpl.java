package com.aecb.ui.verifyamountforpayment.presenter;

import com.aecb.AppConstants;
import com.aecb.base.db.DbDisposableObserver;
import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.DataManager;
import com.aecb.data.api.models.BaseResponse;
import com.aecb.data.api.models.adcb.MakePayment6Response;
import com.aecb.data.api.models.buycreditreport.BuyCreditReportResponse;
import com.aecb.data.api.models.emailrecipients.EmailRecipientsRequest;
import com.aecb.data.api.models.payment_method.ActivateCardRequestBody;
import com.aecb.data.api.models.payment_method.AddCardResponse;
import com.aecb.data.api.models.payment_method.UpdatePaymentStatusRequestResponse;
import com.aecb.data.api.models.request.updateuserprofile.UpdateUserProfileRequest;
import com.aecb.data.api.models.updatepaymentstatus.UpdatePaymentStatusItem;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.util.AESEncryption;
import com.aecb.util.varibles.StringConstants;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.aecb.AppConstants.IntentKey.FROM_ADD_RECIPIENTS;
import static com.aecb.AppConstants.PaymentMethods.PAYMENT_GW;
import static com.aecb.data.api.models.commonmessageresponse.UserCases.VERIFY_AMOUNT;
import static com.aecb.util.Utilities.fullString;
import static com.aecb.util.Utilities.getCurrentDateInApiFormat;
import static com.aecb.util.varibles.StringConstants.USER_SELECTED_CARD_ACTIVATED_SUCCESSFULLY_EN;

public class VerifyAmountPresenterImpl extends MvpBasePresenterImpl<VerifyAmountContract.View>
        implements VerifyAmountContract.Presenter {

    private DataManager mDataManager;
    DBUserTC dbUserTC;
    UpdateUserProfileRequest updateUserProfileRequest;

    @Inject
    public VerifyAmountPresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void activateCard(ActivateCardRequestBody activateCardRequestBody, BaseResponse baseResponse,
                             BuyCreditReportResponse buyCreditReportResponse, String screenFrom, boolean isDefaultCard) {
        String encryptedRequest = createEncryptedRequest(activateCardRequestBody.toString());
        Timber.e("Plain Request == " + activateCardRequestBody.toString());
        Timber.e("Encrypted Request == " + encryptedRequest);
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<MakePayment6Response> myAppDisposableObserver =
                    new MyAppDisposableObserver<MakePayment6Response>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            MakePayment6Response manageCardResponse = (MakePayment6Response) response;
                            if (manageCardResponse != null) {
                                if (screenFrom.equals(FROM_ADD_RECIPIENTS)) {
                                    if (manageCardResponse.isSuccess()) {
                                        updateUserProfileRequest = new UpdateUserProfileRequest();
                                        updateUserProfileRequest.setChannel(2);
                                        updateUserProfileRequest.setUpdateType(8);
                                        updateUserProfileRequest.setPreferredPaymentMethod(PAYMENT_GW);
                                        updatePaymentMethod(updateUserProfileRequest, PAYMENT_GW, screenFrom, manageCardResponse.getMessage());
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
                                            updatePaymentMethod(updateUserProfileRequest, PAYMENT_GW, screenFrom, manageCardResponse.getMessage());
                                        } else {
                                            view.hideLoading();
                                            if (manageCardResponse.getMessage().equalsIgnoreCase("User Selected Card Activated Successfully")) {
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

            MyAppDisposableObserver<MakePayment6Response> disposableObserver =
                    dataManager.manageCard(encryptedRequest)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
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

    private String createEncryptedRequest(String requestBody) {
        AESEncryption.setKeyNew(fullString());
        AESEncryption.encrypt(requestBody);
        return AESEncryption.getEncryptedString()
                .replaceAll("\\s", "")
                .replaceAll("\\n", "")
                .replaceAll("\\r", "");
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
                                    if (message.equalsIgnoreCase("User Selected Card Activated Successfully")) {
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