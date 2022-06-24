package com.aecb.ui.reporthistory.presenter;

import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.data.DataManager;
import com.aecb.data.api.models.getproducts.GetProductResponse;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.data.api.models.purchasehistory.HistoryItem;
import com.aecb.data.api.models.purchasehistory.PurchaseHistoryResponse;
import com.aecb.util.SharedPreferenceStringLiveData;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

import static com.aecb.AppConstants.ProductIds.REPORT_ID;
import static com.aecb.AppConstants.ProductIds.SCORE_ID;
import static com.aecb.AppConstants.ProductIds.SCORE_WITH_REPORT_ID;
import static com.aecb.AppConstants.ProductIds.isDisplayFalseProduct;
import static com.aecb.data.preference.PrefHelperImpl.KEY_PRODUCTS_LIST;
import static com.aecb.data.preference.PrefHelperImpl.KEY_PURCHASE_HISTORY;

public class ReportHistoryPresenterImpl extends MvpBasePresenterImpl<ReportHistoryContract.View>
        implements ReportHistoryContract.Presenter {

    private DataManager mDataManager;
    private List<ProductsItem> allProductList = new ArrayList<>();

    @Inject
    public ReportHistoryPresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getProductHistory() {
        getProductList();
        ifViewAttached(view -> {
            SharedPreferenceStringLiveData sharedPreferenceStringLiveData = new SharedPreferenceStringLiveData(mDataManager.getPrefReference(), KEY_PURCHASE_HISTORY, "");
            sharedPreferenceStringLiveData.getStringLiveData(KEY_PURCHASE_HISTORY, "").observe(view.getLifeCycleOwner(), value -> {
                try {
                    Gson gson = new Gson();
                    PurchaseHistoryResponse purchaseHistoryResponse = gson.fromJson(value, PurchaseHistoryResponse.class);
                    Timber.d("purchaseHistory==" + purchaseHistoryResponse.toString());
                    List<HistoryItem> lst = purchaseHistoryResponse.getData().getHistory();
                    List<HistoryItem> filterList = new ArrayList<HistoryItem>();
                    filterList.clear();
                    for (int i = 0; i <= lst.size() - 1; i++) {
                        if (lst.get(i).isIsPdfAvailable() && lst.get(i).getReportTypeId().equals(REPORT_ID) ||
                                lst.get(i).getReportTypeId().equals(SCORE_ID) ||
                                lst.get(i).getReportTypeId().equals(SCORE_WITH_REPORT_ID)) {
                            filterList.add(lst.get(i));
                        }
                        int finalI = i;
                        for (ProductsItem productsItem : isDisplayFalseProduct) {
                            if (lst.get(finalI).getReportTypeId().equals(productsItem.getId())) {
                                filterList.add(lst.get(finalI));
                            }
                        }
                    }
                    view.setPurchaseList(filterList, allProductList, dataManager.getCurrentLanguage());
                } catch (Exception e) {
                    Timber.d("Exception : " + e.toString());
                }
            });
        });
    }

    public void getProductList() {
        ifViewAttached(view -> {
            SharedPreferenceStringLiveData sharedPreferenceStringLiveData = new SharedPreferenceStringLiveData(mDataManager.getPrefReference(), KEY_PRODUCTS_LIST, "");
            sharedPreferenceStringLiveData.getStringLiveData(KEY_PRODUCTS_LIST, "").observe(view.getLifeCycleOwner(), value -> {
                try {
                    GetProductResponse productResponse = new Gson().fromJson(value, GetProductResponse.class);
                    if (productResponse != null) {
                        allProductList.clear();
                        allProductList.addAll(productResponse.getData().getProducts());
                    }
                } catch (Exception e) {
                    Timber.d("Exception : " + e.toString());
                }

            });
        });
    }

    @Override
    public String getCurrentLanguage() {
        return dataManager.getCurrentLanguage();
    }

}