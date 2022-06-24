package com.aecb.ui.purchaseoptions.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.getproducts.ProductsItem;

import java.util.List;

public interface PurchaseOptionsContract {
    interface View extends BaseView {
        void setProductList(List<ProductsItem> products, String currentLanguage);
    }

    interface Presenter extends BasePresenter<View> {
        void getProductList();

        String getAppLanguage();
    }
}