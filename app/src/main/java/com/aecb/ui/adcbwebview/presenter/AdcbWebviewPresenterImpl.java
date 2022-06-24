package com.aecb.ui.adcbwebview.presenter;

import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.data.DataManager;

import javax.inject.Inject;

public class AdcbWebviewPresenterImpl extends MvpBasePresenterImpl<AdcbWebviewContract.View> implements
        AdcbWebviewContract.Presenter {

    DataManager mDataManager;

    @Inject
    public AdcbWebviewPresenterImpl(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    @Override
    public String getCurrentLanguage() {
        return dataManager.getCurrentLanguage();
    }
}
