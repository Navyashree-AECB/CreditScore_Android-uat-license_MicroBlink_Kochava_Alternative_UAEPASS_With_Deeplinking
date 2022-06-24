package com.aecb.ui.purchaseoptions.presenter;

import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.data.api.models.getproducts.GetProductResponse;
import com.google.gson.Gson;

import javax.inject.Inject;

public class PurchaseOptionsPresenterImpl extends MvpBasePresenterImpl<PurchaseOptionsContract.View>
        implements PurchaseOptionsContract.Presenter {

    @Inject
    public PurchaseOptionsPresenterImpl() {

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

    @Override
    public String getAppLanguage() {
        return dataManager.getCurrentLanguage();
    }
}