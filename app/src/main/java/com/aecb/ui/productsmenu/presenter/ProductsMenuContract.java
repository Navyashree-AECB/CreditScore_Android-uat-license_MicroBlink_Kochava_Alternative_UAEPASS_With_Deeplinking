package com.aecb.ui.productsmenu.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.getproducts.ProductsItem;

import java.util.List;

public interface ProductsMenuContract {
    interface View extends BaseView {
        void setProductList(List<ProductsItem> products, String currentLanguage);
    }

    interface Presenter extends BasePresenter<View> {
        void getProductList();
    }
}