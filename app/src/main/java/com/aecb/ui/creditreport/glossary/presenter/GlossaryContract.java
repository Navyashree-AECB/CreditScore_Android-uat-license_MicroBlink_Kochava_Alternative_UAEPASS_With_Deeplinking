package com.aecb.ui.creditreport.glossary.presenter;

import androidx.lifecycle.LifecycleOwner;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.purchasehistory.HistoryItem;

import java.util.List;

public interface GlossaryContract {
    interface View extends BaseView {
        LifecycleOwner getLifeCycleOwner();

        void setProductList(List<HistoryItem> history, String currentLanguage);

        void loadWebView(String loadWebViewUrl);
    }

    interface Presenter extends BasePresenter<View> {
        void getProduct(String productID);

        void getConfiguration();
    }
}