package com.aecb.ui.creditreport.faq.presenter;

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

public class FAQPresenterImpl extends MvpBasePresenterImpl<FAQContract.View>
        implements FAQContract.Presenter {

    private DataManager mDataManager;
    private String loadWebViewUrl;

    @Inject
    public FAQPresenterImpl(DataManager mDataManager) {
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
                                    loadWebViewUrl = productsItem.getDrupalBaseUrl() + productsItem.getProductInformationFaqsAr();
                                    view.loadWebView(loadWebViewUrl);
                                } else {
                                    loadWebViewUrl = productsItem.getDrupalBaseUrl() + productsItem.getProductInformationFaqsEn();
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
    public void getConfiguration(boolean creditReport) {
        ifViewAttached(view -> {
            SharedPreferenceStringLiveData sharedPreferenceStringLiveData = new SharedPreferenceStringLiveData(mDataManager.getPrefReference(), KEY_CONFIGURATION_DATA, "");
            sharedPreferenceStringLiveData.getStringLiveData(KEY_CONFIGURATION_DATA, "").observe(view.getLifeCycleOwner(), value -> {
                try {
                    Gson gson = new Gson();
                    ConfigurationData configurationData = gson.fromJson(value, ConfigurationData.class);
                    Timber.d("purchaseHistory==" + configurationData.toString());

                    if (configurationData != null && creditReport) {
                        if (dataManager.getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ARABIC)) {
                            loadWebViewUrl = configurationData.getDrupalBaseUrl() + configurationData.getPages().getInforFaqsAr();
                        } else {
                            loadWebViewUrl = configurationData.getDrupalBaseUrl() + configurationData.getPages().getInforFaqsEn();
                            Timber.d("urlllll", "" + loadWebViewUrl);
                        }
                        view.loadWebView(loadWebViewUrl);
                    } else {
                        if (dataManager.getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ARABIC)) {
                            loadWebViewUrl = configurationData.getDrupalBaseUrl() + configurationData.getPages().getFaqAr();
                        } else {
                            loadWebViewUrl = configurationData.getDrupalBaseUrl() + configurationData.getPages().getFaq();
                            Timber.d("urlllll", "" + loadWebViewUrl);
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