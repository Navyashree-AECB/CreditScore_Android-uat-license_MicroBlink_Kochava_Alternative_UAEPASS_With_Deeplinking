package com.aecb.ui.home.presenter;

import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.DataManager;
import com.aecb.data.api.models.getproducts.GetProductResponse;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.data.api.models.purchasehistory.HistoryItem;
import com.aecb.data.api.models.purchasehistory.PurchaseHistoryResponse;
import com.aecb.ui.dashboard.view.DashboardActivity;
import com.aecb.ui.home.view.HomeFragment;
import com.aecb.util.SharedPreferenceStringLiveData;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.aecb.AppConstants.ProductIds.D2C_PRODUCT_ID;
import static com.aecb.AppConstants.ProductIds.REPORT_ID;
import static com.aecb.AppConstants.ProductIds.SCORE_ID;
import static com.aecb.AppConstants.ProductIds.SCORE_WITH_REPORT_ID;
import static com.aecb.AppConstants.ProductIds.isDisplayFalseProduct;
import static com.aecb.data.preference.PrefHelperImpl.KEY_PRODUCTS_LIST;
import static com.aecb.data.preference.PrefHelperImpl.KEY_PURCHASE_HISTORY;

public class HomePresenterImpl extends MvpBasePresenterImpl<HomeContract.View>
        implements HomeContract.Presenter {

    private DataManager mDataManager;
    private List<HistoryItem> filteredScoresOnly = new ArrayList<>();
    private List<HistoryItem> filteredReportsOnly = new ArrayList<>();
    private List<ProductsItem> allProductList = new ArrayList<>();
    private List<HistoryItem> allPurchaseHistory = new ArrayList<>();

    @Inject
    public HomePresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getProductList(boolean callPurchaseApi) {
        //getPurchaseHistory();
        ifViewAttached(view -> {
            SharedPreferenceStringLiveData sharedPreferenceStringLiveData = new SharedPreferenceStringLiveData(mDataManager.getPrefReference(), KEY_PRODUCTS_LIST, "");
            sharedPreferenceStringLiveData.getStringLiveData(KEY_PRODUCTS_LIST, "").observe(view.getLifeCycleOwner(), value -> {
                try {
                    GetProductResponse productResponse = new Gson().fromJson(value, GetProductResponse.class);
                    if (productResponse != null) {
                        allProductList.clear();
                        allProductList.addAll(productResponse.getData().getProducts());
                        if (callPurchaseApi) {
                            getPurchaseHistoryFromApi();
                        } else {
                            getPurchaseHistory();
                        }
                        view.loadAllProductList(allProductList);
                    }
                } catch (Exception e) {
                    Timber.d("Exception : " + e.toString());
                }

            });
        });
    }

    public void getPurchaseHistoryFromApi() {
        ifViewAttached(view -> {
            MyAppDisposableObserver<PurchaseHistoryResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<PurchaseHistoryResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            PurchaseHistoryResponse purchaseHistoryResponse = (PurchaseHistoryResponse) response;
                            dataManager.setPurchaseHistory(new Gson().toJson(purchaseHistoryResponse));
                            List<HistoryItem> purchaseHistory = purchaseHistoryResponse.getData().getHistory();
                            allPurchaseHistory.clear();
                            allPurchaseHistory.addAll(purchaseHistory);
                            displayAppropriateDashboard(purchaseHistory);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
                            getPurchaseHistory();
                            view.displayDashboardDetails();
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

    private void getPurchaseHistory() {
        ifViewAttached(view -> {
            SharedPreferenceStringLiveData sharedPreferenceStringLiveData = new SharedPreferenceStringLiveData(mDataManager.getPrefReference(), KEY_PURCHASE_HISTORY, "");
            sharedPreferenceStringLiveData.getStringLiveData(KEY_PURCHASE_HISTORY, "").observe(view.getLifeCycleOwner(), value -> {
                String data;
                try {
                    Gson gson = new Gson();
                   /* try {
                        data = loadJSONFromAsset();
                    } catch (Exception e) {*/
                        data = value;
                        //Timber.d("Exception : " + e.toString());
                    //}

                    PurchaseHistoryResponse purchaseHistoryResponse = gson.fromJson(data, PurchaseHistoryResponse.class);
                    Timber.d("purchaseHistory==" + purchaseHistoryResponse.toString());
                    if (purchaseHistoryResponse != null) {
                        List<HistoryItem> purchaseHistory = purchaseHistoryResponse.getData().getHistory();
                        allPurchaseHistory.clear();
                        allPurchaseHistory.addAll(purchaseHistory);
                        displayAppropriateDashboard(purchaseHistory);
                    }
                } catch (Exception e) {
                    Timber.d("Exception : " + e.toString());
                }
            });
        });
    }

    @Override
    public String getAppLanguage() {
        return dataManager.getCurrentLanguage();
    }

    private void displayAppropriateDashboard(List<HistoryItem> purchaseHistory) {
        ifViewAttached(view -> {
            if (purchaseHistory.isEmpty()) {
                view.displayNoCreditScoreAndReportView();
                view.setProductList(allProductList, dataManager.getCurrentLanguage());
                view.displayDashboardDetails();
            } else {
                checkIfMultipleScoreOrReportsAreThere(purchaseHistory);
                view.displayDashboardDetails();
            }
        });
    }

    private void checkIfMultipleScoreOrReportsAreThere(List<HistoryItem> purchaseHistory) {
        filteredReportsOnly.clear();
        filteredScoresOnly.clear();
        for (HistoryItem historyItem : purchaseHistory) {
            if (historyItem.getReportTypeId().equals(SCORE_ID) || historyItem.getReportTypeId().equals(SCORE_WITH_REPORT_ID)) {
                filteredScoresOnly.add(historyItem);
            }
            if (historyItem.getReportTypeId().equals(REPORT_ID) || historyItem.getReportTypeId().equals(SCORE_WITH_REPORT_ID)) {
                filteredReportsOnly.add(historyItem);
            }
            for (ProductsItem productsItem : isDisplayFalseProduct) {
                if (historyItem.getReportTypeId().equals(D2C_PRODUCT_ID)) {
                    filteredReportsOnly.add(historyItem);
                    filteredScoresOnly.add(historyItem);
                }
            }
        }
        Collections.sort(filteredReportsOnly, (m1, m2) -> m1.getCreatedon().compareTo(m2.getCreatedon()));
        Collections.reverse(filteredReportsOnly);
        Collections.sort(filteredScoresOnly, (m1, m2) -> m1.getCreatedon().compareTo(m2.getCreatedon()));
        Collections.reverse(filteredScoresOnly);
        Collections.sort(allPurchaseHistory, (m1, m2) -> m1.getCreatedon().compareTo(m2.getCreatedon()));
        Collections.reverse(allPurchaseHistory);
        ifViewAttached(view -> {
            if (!filteredScoresOnly.isEmpty() && filteredReportsOnly.isEmpty()) {
                view.displayNoReportWithCreditScoreView(allProductList);
                view.setupScoreProgress(filteredScoresOnly.get(0));
                view.displayReportDetails(allPurchaseHistory.get(0));
            } else if (filteredScoresOnly.isEmpty() && !filteredReportsOnly.isEmpty()) {
                view.displayNoScoreWithReportView(filteredReportsOnly.get(0), allProductList);
            } else if (!filteredScoresOnly.isEmpty() && !filteredReportsOnly.isEmpty()) {
                view.setupScoreProgress(filteredScoresOnly.get(0));
                view.displayScoreWithReportView(filteredScoresOnly.get(0));
                view.displayReportDetails(allPurchaseHistory.get(0));
            } else if (filteredScoresOnly.isEmpty() && filteredReportsOnly.isEmpty()) {
                view.displayNoCreditScoreAndReportView();
            }
        });
    }

    public String loadJSONFromAsset() {
        String json = null;
        InputStream is = null;
        try {
            if (DashboardActivity.scenario == 1) {
                is = HomeFragment.context.getAssets().open("NoScore_NoReport.json");
            } else if (DashboardActivity.scenario == 2) {
                is = HomeFragment.context.getAssets().open("SingleScore_NoReport.json");
            } else if (DashboardActivity.scenario == 3) {
                is = HomeFragment.context.getAssets().open("MultipleScore_NoReport.json");
            } else if (DashboardActivity.scenario == 4) {
                is = HomeFragment.context.getAssets().open("NoScore_SingleReport.json");
            } else if (DashboardActivity.scenario == 5) {
                is = HomeFragment.context.getAssets().open("NoScore_MultipleReport.json");
            } else if (DashboardActivity.scenario == 6) {
                is = HomeFragment.context.getAssets().open("MultipleScore_MultipleReport.json");
            } else if (DashboardActivity.scenario == 7) {
                is = HomeFragment.context.getAssets().open("MultipleScore_MultipleReport_No_Last_year.json");
            }
            assert is != null;
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            Timber.d("Exception : " + ex.toString());
            return null;
        }
        return json;
    }

}