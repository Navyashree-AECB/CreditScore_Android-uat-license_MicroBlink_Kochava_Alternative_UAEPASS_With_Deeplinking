package com.aecb.ui.creditreport.intro.presenter;

import com.aecb.AppConstants;
import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.data.DataManager;
import com.aecb.data.api.models.configuration.ConfigurationData;
import com.aecb.data.api.models.getproducts.GetProductResponse;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.util.SharedPreferenceStringLiveData;
import com.google.gson.Gson;

import javax.inject.Inject;

import timber.log.Timber;

import static com.aecb.data.preference.PrefHelperImpl.KEY_CONFIGURATION_DATA;
import static com.aecb.data.preference.PrefHelperImpl.KEY_PRODUCTS_LIST;

public class IntroPresenterImpl extends MvpBasePresenterImpl<IntroContract.View>
        implements IntroContract.Presenter {

    private DataManager mDataManager;
    private String loadWebViewUrl;

    @Inject
    public IntroPresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getProduct(String productID) {
        ifViewAttached(view -> {
            SharedPreferenceStringLiveData sharedPreferenceStringLiveData = new SharedPreferenceStringLiveData(mDataManager.getPrefReference(), KEY_PRODUCTS_LIST, "");
            sharedPreferenceStringLiveData.getStringLiveData(KEY_PRODUCTS_LIST, "").observe(view.getLifeCycleOwner(), value -> {
                try {
                    GetProductResponse productResponse = new Gson().fromJson(value, GetProductResponse.class);
                    if (productResponse != null) {
                        for (ProductsItem productsItem : productResponse.getData().getProducts()) {
                            if (productsItem.getId().equals(productID))
                                if (dataManager.getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ARABIC)) {
                                    if (productsItem.getProductInformationIntroAr() != null){
                                        loadWebViewUrl = productsItem.getDrupalBaseUrl() + productsItem.getProductInformationIntroAr();
                                    }else{
                                        loadWebViewUrl = productsItem.getDrupalBaseUrl();
                                    }
                                    view.loadWebView(loadWebViewUrl);
                                } else {
                                    if (productsItem.getProductInformationIntroEn() != null){
                                        loadWebViewUrl = productsItem.getDrupalBaseUrl() + productsItem.getProductInformationIntroEn();
                                    }else{
                                        loadWebViewUrl = productsItem.getDrupalBaseUrl();
                                    }
                                    view.loadWebView(loadWebViewUrl);
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
    public void getConfiguration() {
        ifViewAttached(view -> {
            SharedPreferenceStringLiveData sharedPreferenceStringLiveData = new SharedPreferenceStringLiveData(mDataManager.getPrefReference(), KEY_CONFIGURATION_DATA, "");
            sharedPreferenceStringLiveData.getStringLiveData(KEY_CONFIGURATION_DATA, "").observe(view.getLifeCycleOwner(), value -> {
                try {
                    Gson gson = new Gson();
                    ConfigurationData configurationData = gson.fromJson(value, ConfigurationData.class);
                    Timber.d("purchaseHistory==" + configurationData.toString());

                    if (configurationData != null) {
                        if (dataManager.getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ARABIC)) {
                            loadWebViewUrl = configurationData.getDrupalBaseUrl() + configurationData.getPages().getIntroAecbAr();
                        } else {
                            loadWebViewUrl = configurationData.getDrupalBaseUrl() + configurationData.getPages().getIntroAecbEn();
                        }
                        view.loadWebView(loadWebViewUrl);
                    }
                } catch (Exception e) {
                    Timber.d("Exception : " + e.toString());
                }

            });


        });
    }
}