package com.aecb.ui.checkout.presenter;

import com.aecb.App;
import com.aecb.AppConstants;
import com.aecb.BuildConfig;
import com.aecb.base.db.DbDisposableObserver;
import com.aecb.base.mvp.BaseView;
import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.DataManager;
import com.aecb.data.api.ApiClient;
import com.aecb.data.api.ApiService;
import com.aecb.data.api.models.BaseResponse;
import com.aecb.data.api.models.adcb.MakePayment6Response;
import com.aecb.data.api.models.buycreditreport.BuyCreditReportRequest;
import com.aecb.data.api.models.buycreditreport.BuyCreditReportResponse;
import com.aecb.data.api.models.cards.CreditCardList;
import com.aecb.data.api.models.commonmessageresponse.UserCases;
import com.aecb.data.api.models.configuration.ConfigurationData;
import com.aecb.data.api.models.edirham.CheckoutStatusRequest;
import com.aecb.data.api.models.edirham.CheckoutStatusResponse;
import com.aecb.data.api.models.edirham.PurchaseCheckoutRequest;
import com.aecb.data.api.models.edirham.PurchaseCheckoutResponse;
import com.aecb.data.api.models.edirham.PurchaseDetails;
import com.aecb.data.api.models.edirham.ServiceItem;
import com.aecb.data.api.models.emailrecipients.EmailRecipientsRequest;
import com.aecb.data.api.models.getproducts.GetProductResponse;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.data.api.models.payment_method.AddCardRequestBody;
import com.aecb.data.api.models.payment_method.UpdatePaymentStatusRequestResponse;
import com.aecb.data.api.models.response.getuserdetail.GetProfileResponse;
import com.aecb.data.api.models.response.getuserdetail.GetUserDetailResponse;
import com.aecb.data.api.models.updatepaymentstatus.UpdatePaymentStatusItem;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.util.AESEncryption;
import com.aecb.util.SharedPreferenceStringLiveData;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;
import timber.log.Timber;

import static com.aecb.AppConstants.PaymentMethods.EDIRHAMS;
import static com.aecb.AppConstants.PaymentMethods.PAYMENT_GW;
import static com.aecb.data.preference.PrefHelperImpl.KEY_CONFIGURATION_DATA;
import static com.aecb.util.Utilities.fullString;
import static com.aecb.util.Utilities.getCurrentDateInApiFormat;
import static com.aecb.util.Utilities.getStringFromErrorBody;
import static com.aecb.util.varibles.StringConstants.CONST_NO;
import static com.aecb.util.varibles.StringConstants.CONST_PYMT;
import static com.aecb.util.varibles.StringConstants.CONST_YES;

public class CheckoutPresenterImpl extends MvpBasePresenterImpl<CheckoutContract.View>
        implements CheckoutContract.Presenter {

    private DataManager mDataManager;
    public DBUserTC dbUser;
    public String upgradeProductNumber = " ";
    ProductsItem selectedProduct;
    ApiService apiService;
    BuyCreditReportResponse buyCreditReportResponseForEDirham;

    @Inject
    public CheckoutPresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
        apiService = ApiClient.getClient(App.getAppComponent().appContext())
                .create(ApiService.class);
    }

    @Override
    public void getProducts() {
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<GetProductResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<GetProductResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            GetProductResponse getProductResponse = (GetProductResponse) response;
                            dataManager.setProductList(new Gson().toJson(getProductResponse));
                            view.setProductList(getProductResponse.getData().getProducts(), dataManager.getCurrentLanguage());
                            getCardList();
                        }
                    };

            MyAppDisposableObserver<GetProductResponse> disposableObserver =
                    dataManager.getProducts()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    @Override
    public String getCurrentLanguage() {
        return dataManager.getCurrentLanguage();
    }

    private void getCardList() {
        ifViewAttached(view -> {
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
                        view.hideLoading();
                        view.setCardList(list.getcreditCardDataList(), dbUser);
                    } catch (Exception e) {
                        Timber.d("Exception : " + e.toString());
                        view.setCardList(new ArrayList<>(), dbUser);
                    }

                }

                @Override
                public void onError(Throwable throwable) {
                    view.hideLoading();
                    view.setCardList(new ArrayList<>(), dbUser);
                }
            };
            MyAppDisposableObserver<Response<ResponseBody>> disposableObserver = dataManager.getCreditCards()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(observer);
            compositeDisposable.add(disposableObserver);
        });
    }

    @Override
    public void startPurchase(String cardNumber, String expireDate, String fullName, boolean isCardForSave, boolean isAcceptTermConditions,
                              boolean isShowError, boolean isDefault, ProductsItem productsItem, String paymentType) {
        if (dbUser != null) {
            getUserDetail(dbUser.getEmail(), dbUser.getPassword(), productsItem, cardNumber, expireDate, fullName,
                    isCardForSave, isDefault, paymentType);
        } else {
            callBuyCreditReport(productsItem, createBodyRequestForCard(fullName, expireDate, cardNumber, null,
                    getResult(isCardForSave), getResultForDefault(isDefault), paymentType, productsItem));
        }
    }

    public void callBuyCreditReport(ProductsItem productsItem, AddCardRequestBody bodyRequestForCard) {
        ifViewAttached(view -> {
            view.showBlurView();
            MyAppDisposableObserver<BuyCreditReportResponse> observer = new MyAppDisposableObserver<BuyCreditReportResponse>(view) {
                @Override
                protected void onSuccess(Object response) {
                    BuyCreditReportResponse buyCreditReportResponse = ((BuyCreditReportResponse) response);
                    if (!buyCreditReportResponse.getBuyCreditReportData().isNoHit() &&
                            !buyCreditReportResponse.getBuyCreditReportData().isScoreError()) {
                        if (buyCreditReportResponse.isSuccess()) {
                            bodyRequestForCard.setTransactionID(buyCreditReportResponse.getBuyCreditReportData().getPortalUserId());
                            addNewCard(bodyRequestForCard, buyCreditReportResponse);
                        } else {
                            view.hideLoading();
                            view.hideBlurView();
                            view.showApiFailureError(buyCreditReportResponse.getMessage(), "", "");
                        }
                    } else if(!buyCreditReportResponse.getBuyCreditReportData().isNoHit()  && buyCreditReportResponse.getBuyCreditReportData().isScoreError()) {
                        upgradeProductNumber = productsItem.getProductNumber();
                        if (upgradeProductNumber.equalsIgnoreCase(AppConstants.ProductIds.SCORE_WITH_REPORT_ID_PRODUCT_NUMBER))  {
                            bodyRequestForCard.setTransactionID(buyCreditReportResponse.getBuyCreditReportData().getPortalUserId());
                            addNewCard(bodyRequestForCard, buyCreditReportResponse);
                        } else {
                            view.hideLoading();
                            view.hideBlurView();
                            view.scoreNotAvailableError();
                        }
                    }
                    else {
                        view.hideLoading();
                        view.hideBlurView();
                        view.scoreNotAvailableError();
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    super.onError(throwable);
                    view.hideLoading();
                    view.hideBlurView();
                }
            };
            MyAppDisposableObserver<BuyCreditReportResponse> disposableObserver = dataManager.buyCreditReport(
                    buyCreditReportRequest(productsItem.getId(), false))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(observer);
            compositeDisposable.add(disposableObserver);

        });

    }

    private BuyCreditReportRequest buyCreditReportRequest(String productId, boolean isEDirham) {
        BuyCreditReportRequest buyCreditReportRequest = new BuyCreditReportRequest();
        if (dbUser != null) {
            if (dbUser.getMobile() != null)
                buyCreditReportRequest.setMobile(dbUser.getMobile());
            else buyCreditReportRequest.setMobile("");
            if (dbUser.getFullName() != null)
                buyCreditReportRequest.setFullName(dbUser.getFullName());
            else buyCreditReportRequest.setFullName("");
            if (dbUser.getEmiratesId() != null)
                buyCreditReportRequest.setEmiratesID(dbUser.getEmiratesId());
            else buyCreditReportRequest.setEmiratesID("");
            if (dbUser.getDob() != null) buyCreditReportRequest.setDob(dbUser.getDob());
            else buyCreditReportRequest.setDob("");
            if (dbUser.getGender() == 1) buyCreditReportRequest.setGender("1");
            else if (dbUser.getGender() == 2) buyCreditReportRequest.setGender("2");
            else buyCreditReportRequest.setGender("NULL");
            if (dbUser.getEmail() != null) buyCreditReportRequest.setEmail(dbUser.getEmail());
            else buyCreditReportRequest.setEmail("");
            if (dbUser.getPassport() != null)
                buyCreditReportRequest.setPassport(dbUser.getPassport());
            else buyCreditReportRequest.setPassport("");
            if (dbUser.getNationalityId() != null)
                buyCreditReportRequest.setNationality(dbUser.getNationalityId());
            else buyCreditReportRequest.setNationality("");
            if (isEDirham) buyCreditReportRequest.setPaymentSource(EDIRHAMS);
            else buyCreditReportRequest.setPaymentSource(PAYMENT_GW);
        } else {
            ifViewAttached(BaseView::sessionTokenExpired);
        }
        buyCreditReportRequest.setPreferredlanguage(dataManager.getCurrentLanguage());
        buyCreditReportRequest.setReportType(productId);
        buyCreditReportRequest.setTcVersionNumber(dataManager.getTcVersionNumber());
        buyCreditReportRequest.setChannel(3);
        buyCreditReportRequest.setIsAuthenticated(true);
        return buyCreditReportRequest;
    }

    public AddCardRequestBody createBodyRequestForCard(String fullName, String expireDate,
                                                       String cardNumber, String cvv,
                                                       String isConsent, String isPymt,
                                                       String paymentType, ProductsItem productsItem) {

        double priceWithVAT;
        int finalPrice;
        if (productsItem != null) {
            priceWithVAT = ((productsItem.getPrice() * productsItem.getVat()) / 100);
            finalPrice = (int) ((productsItem.getPrice() + priceWithVAT) * 100);
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
        addCardRequestBody.setTransactionID("");
        addCardRequestBody.setPymtCardIsDefault(isPymt);
        return addCardRequestBody;
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

    private void getUserDetail(String userName, String password,
                               ProductsItem productsItem,
                               String cardNumber, String expireDate, String fullName, boolean isCardForSave,
                               boolean isDefault, String paymentType) {
        ifViewAttached(view -> {
            view.showBlurView();
            MyAppDisposableObserver<GetProfileResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<GetProfileResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            GetProfileResponse getUserDetailResponse = (GetProfileResponse) response;
                            GetUserDetailResponse data = getUserDetailResponse.getData();
                            if (data != null) {
                                DBUserTC dbUserTC = createDBUser(userName, data, dbUser.isTouchId(), password);
                                insertUserDetails(dbUserTC, userName, password, productsItem, cardNumber, expireDate, fullName,
                                        isCardForSave, isDefault, paymentType);
                            } else {
                                callBuyCreditReport(productsItem, createBodyRequestForCard(fullName, expireDate, cardNumber, null,
                                        getResult(isCardForSave), getResultForDefault(isDefault), paymentType, productsItem));
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            callBuyCreditReport(productsItem, createBodyRequestForCard(fullName, expireDate, cardNumber, null,
                                    getResult(isCardForSave), getResultForDefault(isDefault), paymentType, productsItem));
                        }
                    };

            MyAppDisposableObserver<GetProfileResponse> disposableObserver = mDataManager.getUserDetail()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    private DBUserTC createDBUser(String userName, GetUserDetailResponse data,
                                  boolean isTouchID,
                                  String password) {
        DBUserTC dbUserTC = new DBUserTC();
        dbUserTC.setUserName(userName);
        dbUserTC.setTcVersion(Math.round(data.getTcVersionNumber()));
        dbUserTC.setDeviceId(data.getDeviceId());
        dbUserTC.setEmail(data.getEmail());
        dbUserTC.setMobile(data.getMobile());
        dbUserTC.setEmiratesId(data.getEmiratesId());
        dbUserTC.setLastUser(1);
        dbUserTC.setTouchId(isTouchID);
        dbUserTC.setDob(data.getDob());
        dbUserTC.setNationalityId(data.getNationalityId());
        dbUserTC.setGender(data.getGender());
        dbUserTC.setPassport(data.getPassport());
        dbUserTC.setPreferredLanguage(data.getPreferredLanguage());
        dbUserTC.setFullName(data.getName());
        dbUserTC.setFirstName(data.getFirstname());
        dbUserTC.setMiddleName(data.getMiddlename());
        dbUserTC.setIsUAEPassUser(dbUser.getIsUAEPassUser());
        dbUserTC.setPassword(password);
        if (data.getPreferredPaymentMethod() != null) {
            dbUserTC.setPreferredPaymentMethod(data.getPreferredPaymentMethod());
        } else {
            dbUserTC.setPreferredPaymentMethod(PAYMENT_GW);
        }
        return dbUserTC;
    }

    private void insertUserDetails(DBUserTC dbUserTC, String userName, String password,
                                   ProductsItem productsItem,
                                   String cardNumber, String expireDate, String fullName, boolean isCardForSave,
                                   boolean isDefault, String paymentType) {
        ifViewAttached(view -> {
            DbDisposableObserver<Long> myAppDisposableObserver = new
                    DbDisposableObserver<Long>() {
                        @Override
                        protected void onSuccess(Object response) {
                            callBuyCreditReport(productsItem, createBodyRequestForCard(fullName, expireDate, cardNumber, null,
                                    getResult(isCardForSave), getResultForDefault(isDefault), paymentType, productsItem));
                        }
                    };
            DbDisposableObserver<Long> disposableObserver = dataManager.insertUserWithTC(dbUserTC)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    public void addNewCard(AddCardRequestBody addCardRequestBody,
                           BuyCreditReportResponse buyCreditReportResponse) {
        String encryptedRequest = createEncryptedRequest(addCardRequestBody.toString());
        Timber.e("Plain Request == " + addCardRequestBody.toString());
        Timber.e("Encrypted Request == " + encryptedRequest);
        Timber.e("Decrypted Request ==" + createDecryptedRequest(encryptedRequest));
        ifViewAttached(view -> {
            MyAppDisposableObserver<MakePayment6Response> myAppDisposableObserver =
                    new MyAppDisposableObserver<MakePayment6Response>(view, UserCases.ADD_CARD_FROM_HTTP) {
                        @Override
                        protected void onSuccess(Object response) {
                            MakePayment6Response addCardResponse = (MakePayment6Response) response;
                            Timber.d(addCardResponse.toString());
                            callUpdatePaymentStatus(buyCreditReportResponse, addCardResponse);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            view.hideLoading();
                            view.hideBlurView();
                            try {
                                if (throwable instanceof HttpException) {
                                    MakePayment6Response addCardResponse = null;
                                    ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
                                    String errorString = "";
                                    try {
                                        errorString = getStringFromErrorBody(responseBody.bytes()).toString();
                                        addCardResponse = (new Gson()).fromJson(errorString, MakePayment6Response.class);
                                    } catch (Exception e) {
                                        Timber.e(e.getMessage());
                                    }
                                    callUpdatePaymentStatus(buyCreditReportResponse, addCardResponse);
                                }
                            } catch (Exception e) {
                                Timber.e(e.getMessage());
                                view.displayPaymentFailedDialog(buyCreditReportResponse);
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

    public void callUpdatePaymentStatus(BuyCreditReportResponse buyCreditReportResponse,
                                        MakePayment6Response addCardResponse) {

        UpdatePaymentStatusRequestResponse request = new UpdatePaymentStatusRequestResponse();
        UpdatePaymentStatusItem item = new UpdatePaymentStatusItem();
        item.setTransactionID(buyCreditReportResponse.getBuyCreditReportData().getPortalUserId());
        if (addCardResponse.getData() != null) {
            item.setPaymentSource(addCardResponse.getData().getPaymentSource());
            if (addCardResponse.getData().getPaymentdate() != null) {
                item.setPaymentdate(addCardResponse.getData().getPaymentdate());
            } else {
                item.setPaymentdate(getCurrentDateInApiFormat());
            }
            if (addCardResponse.getData().getResultCode() != null) {
                item.setResultCode(addCardResponse.getData().getResultCode());
            } else {
                item.setResultCode("00");
            }
            if (addCardResponse.getData().getPaymentRef() != null) {
                item.setPaymentRef(addCardResponse.getData().getPaymentRef());
            } else {
                item.setPaymentRef("");
            }
            if (addCardResponse.getData().getResultMessage() != null) {
                item.setResultMessage(addCardResponse.getData().getResultMessage());
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
                    view.hideBlurView();
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

                @Override
                public void onError(Throwable throwable) {
                    super.onError(throwable);
                    view.hideLoading();
                    view.hideBlurView();
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
    public void callSendAdditionalEmailsApi(EmailRecipientsRequest recipientsRequest) {
        ifViewAttached(view -> {
            MyAppDisposableObserver<BaseResponse> observer = new MyAppDisposableObserver<BaseResponse>(view) {
                @Override
                protected void onSuccess(Object response) {
                }

                @Override
                public void onError(Throwable throwable) {
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

    public void getConfiguration() {
        // Navya Crashlytics
        try {
            ifViewAttached(view -> {
                SharedPreferenceStringLiveData sharedPreferenceStringLiveData = new SharedPreferenceStringLiveData(mDataManager.getPrefReference(), KEY_CONFIGURATION_DATA, "");
                sharedPreferenceStringLiveData.getStringLiveData(KEY_CONFIGURATION_DATA, "").observe(view.getLifeCycleOwner(), value -> {
                    Gson gson = new Gson();
                    ConfigurationData configurationData = gson.fromJson(value, ConfigurationData.class);
                    Timber.d("purchaseHistory==" + configurationData.toString());
                    ifViewAttached(view1 -> {
                        view.setIsAppFree(configurationData.isIsAppFree());
                    });
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void callBuyCreditReportForFree(ProductsItem finalProduct) {
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<BuyCreditReportResponse> observer = new MyAppDisposableObserver<BuyCreditReportResponse>(view) {
                @Override
                protected void onSuccess(Object response) {
                    BuyCreditReportResponse buyCreditReportResponse = ((BuyCreditReportResponse) response);
                    if (!buyCreditReportResponse.getBuyCreditReportData().isNoHit() &&
                            !buyCreditReportResponse.getBuyCreditReportData().isScoreError()) {
                        if (buyCreditReportResponse.isSuccess()) {
                            updatePaymentStatusForFree(buyCreditReportResponse);
                        } else {
                            view.hideLoading();
                            view.hideBlurView();
                            view.showApiFailureError(buyCreditReportResponse.getMessage(), "", "");
                        }
                    } else {
                        view.hideLoading();
                        view.hideBlurView();
                        view.scoreNotAvailableError();
                    }
                }
            };
            MyAppDisposableObserver<BuyCreditReportResponse> disposableObserver = dataManager.buyCreditReport(
                    buyCreditReportRequest(finalProduct.getId(), false))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(observer);
            compositeDisposable.add(disposableObserver);

        });
    }

    @Override
    public String getInstallSource() {
        return dataManager.getInstallSource();
    }

    @Override
    public String getInstalledCampaignId() {
        return dataManager.getInstalledCampaignId();
    }

    @Override
    public boolean isNormalInstall() {
        return dataManager.isNormalInstall();
    }

    private void updatePaymentStatusForFree(BuyCreditReportResponse buyCreditReportResponse) {
        UpdatePaymentStatusRequestResponse request = new UpdatePaymentStatusRequestResponse();
        UpdatePaymentStatusItem item = new UpdatePaymentStatusItem();
        item.setTransactionID(buyCreditReportResponse.getBuyCreditReportData().getPortalUserId());
        item.setPaymentdate(getCurrentDateInApiFormat());
        item.setResultCode("00");
        item.setPaymentSource("");
        item.setPaymentRef("");
        item.setResultMessage(buyCreditReportResponse.getMessage());

        request.setStatus(buyCreditReportResponse.getStatus());
        request.setMessage(buyCreditReportResponse.getMessage());
        if (buyCreditReportResponse.getRefId() != null) {
            request.setRefid(buyCreditReportResponse.getRefId());
        } else {
            request.setRefid("");
        }
        request.setSuccess(buyCreditReportResponse.isSuccess());
        request.setData(item);

        ifViewAttached(view -> {
            MyAppDisposableObserver<UpdatePaymentStatusRequestResponse> observer = new MyAppDisposableObserver<UpdatePaymentStatusRequestResponse>(view) {
                @Override
                protected void onSuccess(Object response) {
                    view.hideLoading();
                    UpdatePaymentStatusRequestResponse requestResponse = (UpdatePaymentStatusRequestResponse) response;
                    if (requestResponse.isSuccess()) {
                        if (buyCreditReportResponse.isSuccess()) {
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
    public void callPurchaseCheckout(PurchaseCheckoutRequest purchaseCheckoutRequest) {
        ifViewAttached(view -> {
            MyAppDisposableObserver<PurchaseCheckoutResponse> observer = new MyAppDisposableObserver<PurchaseCheckoutResponse>(view) {
                @Override
                protected void onSuccess(Object response) {
                    view.hideLoading();
                    PurchaseCheckoutResponse purchaseCheckoutResponse = ((PurchaseCheckoutResponse) response);
                    Timber.e(purchaseCheckoutResponse.getCheckoutId());
                    Timber.e(purchaseCheckoutResponse.getCheckoutUrl());
                    view.openEDirhamWebview(purchaseCheckoutResponse.getCheckoutId(),
                            purchaseCheckoutResponse.getCheckoutUrl());
                }
            };
            MyAppDisposableObserver<PurchaseCheckoutResponse> disposableObserver = apiService.callPurchaseCheckout(purchaseCheckoutRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(observer);
            compositeDisposable.add(disposableObserver);
        });
    }

    @Override
    public void getCheckOutStatus(CheckoutStatusRequest checkoutStatusRequest) {
        checkoutStatusRequest.setOrderNumber(buyCreditReportResponseForEDirham.getBuyCreditReportData().getPortalUserId()
                .replaceAll("-", ""));
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<CheckoutStatusResponse> observer = new MyAppDisposableObserver<CheckoutStatusResponse>(view) {
                @Override
                protected void onSuccess(Object response) {
                    view.hideLoading();
                    CheckoutStatusResponse checkoutStatusResponse = ((CheckoutStatusResponse) response);
                    Timber.e(checkoutStatusResponse.toString());
                    updatePaymentStatusForEDirham(checkoutStatusResponse);
                }
            };
            MyAppDisposableObserver<CheckoutStatusResponse> disposableObserver = apiService.callCheckoutStatus(checkoutStatusRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(observer);
            compositeDisposable.add(disposableObserver);
        });
    }

    @Override
    public void startEDirhamPurchase(ProductsItem finalProduct, String price) {
        callBuyCreditReportForEDirham(finalProduct, price);
    }

    public void callBuyCreditReportForEDirham(ProductsItem finalProduct, String price) {
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<BuyCreditReportResponse> observer = new MyAppDisposableObserver<BuyCreditReportResponse>(view) {
                @Override
                protected void onSuccess(Object response) {
                    BuyCreditReportResponse buyCreditReportResponse = ((BuyCreditReportResponse) response);
                    if (!buyCreditReportResponse.getBuyCreditReportData().isNoHit() &&
                            !buyCreditReportResponse.getBuyCreditReportData().isScoreError()) {
                        if (buyCreditReportResponse.isSuccess()) {
                            buyCreditReportResponseForEDirham = buyCreditReportResponse;
                            callPurchaseCheckout(buyCreditReportResponse.getBuyCreditReportData().getPortalUserId(),
                                    price, finalProduct.geteDhiramServiceCode());
                        } else {
                            view.hideLoading();
                            view.showApiFailureError(buyCreditReportResponse.getMessage(), "", "");
                        }
                    } else {
                        view.hideLoading();
                        view.hideBlurView();
                        view.scoreNotAvailableError();
                    }
                }
            };
            MyAppDisposableObserver<BuyCreditReportResponse> disposableObserver = dataManager.buyCreditReport(
                    buyCreditReportRequest(finalProduct.getId(), true))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(observer);
            compositeDisposable.add(disposableObserver);
        });
    }

    private void callPurchaseCheckout(String transactionID, String price, String serviceCode) {
        PurchaseCheckoutRequest purchaseCheckoutRequest = new PurchaseCheckoutRequest();
        purchaseCheckoutRequest.setUserName("AECB");
        if (BuildConfig.BUILD_TYPE.equalsIgnoreCase("prod")) {
            purchaseCheckoutRequest.setPassword("YT78nm5op3sn9ws1mL0ceg0vs2grh7");
            purchaseCheckoutRequest.setMerchantSiteUrl("https://online.aecb.gov.ae:8000/payment/mobile-edirham");
        } else {
            purchaseCheckoutRequest.setPassword("05052020");
            purchaseCheckoutRequest.setMerchantSiteUrl("https://onlineuat.aecb.gov.ae/payment/mobile-edirham");
        }
        purchaseCheckoutRequest.setOrderNumber(transactionID.replaceAll("-", ""));
        purchaseCheckoutRequest.setChannel("ECOMMERCE");
        purchaseCheckoutRequest.setLanguage("EN");
        purchaseCheckoutRequest.setGovernmentServices(true);
        purchaseCheckoutRequest.setAddTransactionFeesOnTop(true);
        purchaseCheckoutRequest.setSessionTimeoutSecs("5000");
        List<String> paymentMethodList = new ArrayList<>();
        paymentMethodList.add("EDIRHAM_CARD");
        paymentMethodList.add("NON_EDIRHAM_CARD");
        paymentMethodList.add("EDIRHAM_INSTANT");
        purchaseCheckoutRequest.setPaymentMethodList(paymentMethodList);
        PurchaseDetails purchaseDetails = new PurchaseDetails();
        List<ServiceItem> serviceItemList = new ArrayList<>();
        ServiceItem serviceItem = new ServiceItem();
        serviceItem.setNumberOfUnits(1);
        serviceItem.setServiceCode(serviceCode);
        serviceItem.setTransactionAmount(Double.parseDouble(price));
        serviceItem.setQuantity(1);
        serviceItemList.add(serviceItem);
        purchaseDetails.setService(serviceItemList);
        purchaseCheckoutRequest.setPurchaseDetails(purchaseDetails);
        callPurchaseCheckout(purchaseCheckoutRequest);
    }

    private void updatePaymentStatusForEDirham(CheckoutStatusResponse checkoutStatusResponse) {
        UpdatePaymentStatusRequestResponse request = new UpdatePaymentStatusRequestResponse();
        UpdatePaymentStatusItem item = new UpdatePaymentStatusItem();
        item.setTransactionID(buyCreditReportResponseForEDirham.getBuyCreditReportData().getPortalUserId());
        item.setPaymentdate(getCurrentDateInApiFormat());
        item.setResultCode(String.valueOf(checkoutStatusResponse.getOrderStatus()));
        item.setPaymentSource(EDIRHAMS);
        if (checkoutStatusResponse.getUrn() != null) {
            item.setPaymentRef(checkoutStatusResponse.getUrn());
        } else {
            item.setPaymentRef("");
        }
        item.setResultMessage(checkoutStatusResponse.getErrorMessage());
        item.setPaymentCheckoutId(checkoutStatusResponse.getCheckoutId());
        request.setStatus(buyCreditReportResponseForEDirham.getStatus());
        request.setMessage(checkoutStatusResponse.getErrorMessage());
        if (buyCreditReportResponseForEDirham.getRefId() != null) {
            request.setRefid(buyCreditReportResponseForEDirham.getRefId());
        } else {
            request.setRefid("");
        }
        boolean success = false;
        if (checkoutStatusResponse.getOrderStatus() == 2) {
            success = true;
        }
        request.setSuccess(success);
        request.setData(item);

        boolean finalSuccess = success;
        ifViewAttached(view -> {
            MyAppDisposableObserver<UpdatePaymentStatusRequestResponse> observer = new MyAppDisposableObserver<UpdatePaymentStatusRequestResponse>(view) {
                @Override
                protected void onSuccess(Object response) {
                    view.hideLoading();
                    UpdatePaymentStatusRequestResponse requestResponse = (UpdatePaymentStatusRequestResponse) response;
                    if (requestResponse.isSuccess()) {
                        if (finalSuccess) {
                            view.displayPaymentSuccessDialog(buyCreditReportResponseForEDirham);
                        } else {
                            view.displayPaymentFailedDialog(buyCreditReportResponseForEDirham);
                        }
                    } else {
                        view.displayPaymentFailedDialog(buyCreditReportResponseForEDirham);
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

}