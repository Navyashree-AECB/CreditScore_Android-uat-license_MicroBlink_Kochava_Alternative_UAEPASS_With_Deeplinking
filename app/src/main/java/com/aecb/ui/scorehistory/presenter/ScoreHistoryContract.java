package com.aecb.ui.scorehistory.presenter;

import androidx.lifecycle.LifecycleOwner;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.data.api.models.purchasehistory.HistoryItem;

import java.util.HashMap;
import java.util.List;

public interface ScoreHistoryContract {
    interface View extends BaseView {

        LifecycleOwner getLifeCycleOwner();

        void setupScoreList(List<HistoryItem> filteredScoresOnly, List<ProductsItem> allProductList);

        void noScoreAvailable();
    }

    interface Presenter extends BasePresenter<View> {
        void getPurchaseHistory();

        String getCurrentLanguage();
    }
}
