package com.aecb.ui.productsmenu.presenter;

import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.data.api.models.getproducts.GetProductResponse;
import com.google.gson.Gson;

import javax.inject.Inject;

public class ProductsMenuPresenterImpl extends MvpBasePresenterImpl<ProductsMenuContract.View>
        implements ProductsMenuContract.Presenter {

    @Inject
    public ProductsMenuPresenterImpl() {
    }


    @Override
    public void getProductList() {
        ifViewAttached(view -> {
            GetProductResponse productResponse = new Gson().fromJson(dataManager.getProductList(), GetProductResponse.class);
            if (productResponse != null) {
                view.setProductList(productResponse.getData().getProducts(), dataManager.getCurrentLanguage());
            }
        });
    }
}