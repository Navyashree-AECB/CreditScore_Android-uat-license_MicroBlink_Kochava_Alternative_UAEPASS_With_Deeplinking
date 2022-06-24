package com.aecb.ui.home.presenter;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.data.api.models.purchasehistory.HistoryItem;

import java.util.List;

public interface HomeContract {
    interface View extends BaseView {
        void setProductList(List<ProductsItem> productList, String currentLanguage);

        Context getActivityContext();

        LifecycleOwner getLifeCycleOwner();

        void setupScoreProgress(HistoryItem historyItem);

        void displayNoCreditScoreAndReportView();

        void displayNoReportWithCreditScoreView(List<ProductsItem> productList);

        void displayNoScoreWithReportView(HistoryItem historyItem, List<ProductsItem> productList);

        void displayScoreWithReportView(HistoryItem historyItem);

        void displayReportDetails(HistoryItem historyItem);

        void loadAllProductList(List<ProductsItem> allProductList);

        void displayDashboardDetails();
    }

    interface Presenter extends BasePresenter<View> {
        void getProductList(boolean callPurchaseApi);

        String getAppLanguage();
    }
}