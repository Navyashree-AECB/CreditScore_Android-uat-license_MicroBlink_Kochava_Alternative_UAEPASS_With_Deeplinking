package com.aecb.ui.creditreport.creditreport.presenter;

import com.aecb.AppConstants;
import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.data.DataManager;
import com.aecb.data.api.models.getproducts.GetProductResponse;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.util.SharedPreferenceStringLiveData;
import com.google.gson.Gson;

import javax.inject.Inject;

import timber.log.Timber;

import static com.aecb.data.preference.PrefHelperImpl.KEY_PRODUCTS_LIST;

public class CreditReportPresenterImpl extends MvpBasePresenterImpl<CreditReportContract.View>
        implements CreditReportContract.Presenter {

    private DataManager mDataManager;

    @Inject
    public CreditReportPresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getTitleFromProduct(String productID) {
        ifViewAttached(view -> {
            SharedPreferenceStringLiveData sharedPreferenceStringLiveData = new SharedPreferenceStringLiveData(mDataManager.getPrefReference(), KEY_PRODUCTS_LIST, "");
            sharedPreferenceStringLiveData.getStringLiveData(KEY_PRODUCTS_LIST, "").observe(view.getLifeCycleOwner(), value -> {
                try {
                    GetProductResponse productResponse = new Gson().fromJson(value, GetProductResponse.class);
                    if (productResponse != null) {
                        for (ProductsItem productsItem : productResponse.getData().getProducts()) {
                            if (productsItem.getId().equals(productID))
                                if (dataManager.getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ARABIC)) {
                                    view.setTitle(productsItem.getTitleAr());
                                } else {
                                    view.setTitle(productsItem.getTitleEn());
                                }
                        }
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