package com.aecb.ui.dashboard.presenter;

import com.aecb.base.db.DbDisposableObserver;
import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.DataManager;
import com.aecb.data.api.models.BaseResponse;
import com.aecb.data.api.models.configuration.ConfigurationResponse;
import com.aecb.data.api.models.getproducts.GetProductResponse;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.data.api.models.purchasehistory.PurchaseHistoryResponse;
import com.aecb.data.api.models.request.updateuserprofile.UpdateUserProfileRequest;
import com.aecb.data.db.repository.notification.DbNotification;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.aecb.AppConstants.ProductIds.D2C_PRODUCT_NUMBER;
import static com.aecb.AppConstants.ProductIds.REPORT_ID;
import static com.aecb.AppConstants.ProductIds.D2C_PRODUCT_ID;
import static com.aecb.AppConstants.ProductIds.REPORT_ID_PRODUCT_NUMBER;
import static com.aecb.AppConstants.ProductIds.REPORT_PRODUCT;
import static com.aecb.AppConstants.ProductIds.SCORE_ID;
import static com.aecb.AppConstants.ProductIds.SCORE_ID_PRODUCT_NUMBER;
import static com.aecb.AppConstants.ProductIds.SCORE_PRODUCT;
import static com.aecb.AppConstants.ProductIds.SCORE_WITH_REPORT_ID;
import static com.aecb.AppConstants.ProductIds.SCORE_WITH_REPORT_ID_PRODUCT_NUMBER;
import static com.aecb.AppConstants.ProductIds.SCORE_WITH_REPORT_PRODUCT;
import static com.aecb.AppConstants.ProductIds.isDisplayFalseProduct;
import static com.aecb.util.Utilities.isNullOrEmpty;

public class DashboardPresenterImpl extends MvpBasePresenterImpl<DashboardContract.View>
        implements DashboardContract.Presenter {

    private DataManager mDataManager;
    private String email = "";
    private ArrayList<ProductsItem> filteredProductList = new ArrayList<>();
    UpdateUserProfileRequest updateUserProfileRequest;

    @Inject
    public DashboardPresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getProducts(boolean showScoreLoadingScreen) {
        if (!showScoreLoadingScreen) getPurchaseHistory();
        ifViewAttached(view -> {
            if (!showScoreLoadingScreen) view.showLoading(null);
            MyAppDisposableObserver<GetProductResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<GetProductResponse>(view) {

                        @Override
                        protected void onSuccess(Object response) {
                            if (!showScoreLoadingScreen) view.hideLoading();
                            GetProductResponse getProductResponse = (GetProductResponse) response;
                            filteredProductList.clear();

                            for (ProductsItem productsItem : getProductResponse.getData().getProducts()) {
                                if (productsItem.isIsdisplay()) {
                                    filteredProductList.add(productsItem);
                                }
                            }
                            dataManager.setProductList(new Gson().toJson(getProductResponse));
                            getAppropriateProductId(getProductResponse.getData().getProducts());
                            view.setProductList(filteredProductList, dataManager.getCurrentLanguage());
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

    private void getAppropriateProductId(List<ProductsItem> productList) {
        isDisplayFalseProduct.clear();
        for (ProductsItem productsItem : productList) {
            if (productsItem.getProductNumber() != null) {
                if (productsItem.getProductNumber().equals(REPORT_ID_PRODUCT_NUMBER)) {
                    REPORT_ID = productsItem.getId();
                    REPORT_PRODUCT = productsItem;
                }
                if (productsItem.getProductNumber().equals(D2C_PRODUCT_NUMBER)) {
                    D2C_PRODUCT_ID = productsItem.getId();
                }
                if (productsItem.getProductNumber().equals(SCORE_ID_PRODUCT_NUMBER)) {
                    SCORE_ID = productsItem.getId();
                    SCORE_PRODUCT = productsItem;
                }
                if (productsItem.getProductNumber().equals(SCORE_WITH_REPORT_ID_PRODUCT_NUMBER)) {
                    SCORE_WITH_REPORT_ID = productsItem.getId();
                    SCORE_WITH_REPORT_PRODUCT = productsItem;
                }
                if (!productsItem.isIsdisplay()) {
                    isDisplayFalseProduct.add(productsItem);
                }
            }
        }
    }

    public void getPurchaseHistory() {
        ifViewAttached(view -> {
            MyAppDisposableObserver<PurchaseHistoryResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<PurchaseHistoryResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            PurchaseHistoryResponse getProductResponse = (PurchaseHistoryResponse) response;
                            dataManager.setPurchaseHistory(new Gson().toJson(getProductResponse));
                            view.refreshHomeFragment();
                            getAppConfigurations();

                        }
                    };

            MyAppDisposableObserver<PurchaseHistoryResponse> disposableObserver =
                    dataManager.getPurchaseHistory()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    public void getAppConfigurations() {
        ifViewAttached(view -> {
            MyAppDisposableObserver<ConfigurationResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<ConfigurationResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            ConfigurationResponse configurationsResponse = (ConfigurationResponse) response;
                            if (configurationsResponse.getData() != null && configurationsResponse.isSuccess()) {
                                dataManager.setConfigurationData(new Gson().toJson(configurationsResponse.getData()));

                            }
                        }
                    };

            MyAppDisposableObserver<ConfigurationResponse> disposableObserver = dataManager.getAppConfigurations()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    public void getNotificationData() {
        getLastUserDetail();
        try {
            ifViewAttached(view -> {
                mDataManager.getAllNotification().observe(view.getLifeCycleOwner(), list -> {
                    int count = 0;
                    List<DbNotification> lst = list;
                    for (int i = 0; i <= lst.size() - 1; i++) {
                        DbNotification item = lst.get(i);
                        if (!item.getNotificationDate().isEmpty() && !isNullOrEmpty(item.getEmail()) &&
                                item.getEmail().equals(email)) {
                            if (!lst.get(i).isRead()) {
                                count = count + 1;
                            }
                        }
                    }
                    view.showUnreadNotification(count);
                });
            });

        } catch (Exception e) {

        }
    }

    public void getLastUserDetail() {
        DbDisposableObserver<DBUserTC> myAppDisposableObserver = new
                DbDisposableObserver<DBUserTC>() {
                    @Override
                    protected void onSuccess(Object response) {
                        DBUserTC dbUserItem = (DBUserTC) response;
                        if (dbUserItem != null) {
                            email = dbUserItem.getEmail();
                        }
                        getNotificationData();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (throwable != null) {
                        }
                    }
                };

        DbDisposableObserver<DBUserTC> disposableObserver = mDataManager.getLastLoginUser()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(myAppDisposableObserver);
        compositeDisposable.add(disposableObserver);
    }

    @Override
    public void updateLanguageIfRequired() {
        DbDisposableObserver<DBUserTC> myAppDisposableObserver = new
                DbDisposableObserver<DBUserTC>() {
                    @Override
                    protected void onSuccess(Object response) {
                        DBUserTC dbUserItem = (DBUserTC) response;
                        if (dbUserItem != null) {
                            email = dbUserItem.getEmail();
                            if (!dbUserItem.getPreferredLanguage().equals(mDataManager.getCurrentLanguage())) {
                                updateUserProfileRequest = new UpdateUserProfileRequest();
                                updateUserProfileRequest.setUpdateType(3);
                                updateUserProfileRequest.setPreferredlanguage(mDataManager.getCurrentLanguage());
                                updateUserProfileRequest.setChannel(2);
                                updateUserLanguage(updateUserProfileRequest);
                            }
                        }
                    }
                };

        DbDisposableObserver<DBUserTC> disposableObserver = mDataManager.getLastLoginUser()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(myAppDisposableObserver);
        compositeDisposable.add(disposableObserver);
    }

    public void updateUserLanguage(UpdateUserProfileRequest updateUserProfileRequest) {
        ifViewAttached(view -> {
            MyAppDisposableObserver<BaseResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<BaseResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            BaseResponse baseResponse = (BaseResponse) response;
                            if (baseResponse != null) {
                                view.hideLoading();
                                updateLanguageInLocal(mDataManager.getCurrentLanguage(), email);
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

    private void updateLanguageInLocal(String language, String email) {
        DbDisposableObserver<Long> myAppDisposableObserver = new DbDisposableObserver<Long>() {
            @Override
            protected void onSuccess(Object response) {
            }
        };
        DbDisposableObserver<Long> disposableObserver =
                dataManager.updateLanguage(email, language)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(myAppDisposableObserver);
        compositeDisposable.add(disposableObserver);
    }

}