package com.aecb.ui.dashboard.presenter;

import androidx.lifecycle.LifecycleOwner;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.getproducts.ProductsItem;

import java.util.List;

public interface DashboardContract {
    interface View extends BaseView {
        void setProductList(List<ProductsItem> products, String currentLanguage);

        LifecycleOwner getLifeCycleOwner();

        void refreshHomeFragment();

        void showUnreadNotification(int count);
    }

    interface Presenter extends BasePresenter<View> {
        void getProducts(boolean showScoreLoadingScreen);

        void updateLanguageIfRequired();
    }
}