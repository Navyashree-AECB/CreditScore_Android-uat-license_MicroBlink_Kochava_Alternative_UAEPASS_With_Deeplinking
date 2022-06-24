package com.aecb.ui.aboutus.presenter;

import com.aecb.AppConstants;
import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.data.DataManager;
import com.aecb.data.api.models.configuration.ConfigurationData;

import javax.inject.Inject;

public class AboutusImpl extends MvpBasePresenterImpl<AboutusContract.View> implements
        AboutusContract.Presenter {

    DataManager mDataManager;

    @Inject
    public AboutusImpl(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    @Override
    public void getUrl() {
        ifViewAttached(view1 -> {
            ifViewAttached(view -> {
                ConfigurationData configurationData = dataManager.getConfigurationData();
                String url = configurationData.getDrupalBaseUrl();
                String ar = configurationData.getPages().getAboutusAr();
                String en = configurationData.getPages().getAboutusEn();
                if (mDataManager.getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ENG)) {
                    url = url + en;
                } else {
                    url = url + ar;
                }
                view.loadWebView(url);
            });
        });
    }

    @Override
    public String getLanguage() {
        String asset;
        if (mDataManager.getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ENG)) {
            asset = "file:///android_asset/privacy_policy.html";
        } else {
            asset = "file:///android_asset/privacy_policy_ar.html";
        }
        return asset;
    }

}