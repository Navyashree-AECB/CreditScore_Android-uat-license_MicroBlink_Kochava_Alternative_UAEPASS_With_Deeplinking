package com.aecb.ui.reporthistory.presenter;

import androidx.lifecycle.LifecycleOwner;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.data.api.models.purchasehistory.HistoryItem;

import java.util.List;

public interface ReportHistoryContract {
    interface View extends BaseView {
        LifecycleOwner getLifeCycleOwner();

        void setPurchaseList(List<HistoryItem> history, List<ProductsItem> productsItemList, String currentLanguage);
    }

    interface Presenter extends BasePresenter<View> {
        void getProductHistory();

        String getCurrentLanguage();
    }
}