package com.aecb.ui.edirhamwebview.presenter;

import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.data.DataManager;

import javax.inject.Inject;

public class EDirhamWebviewPresenterImpl extends MvpBasePresenterImpl<EDirhamWebviewContract.View> implements
        EDirhamWebviewContract.Presenter {

    DataManager mDataManager;

    @Inject
    public EDirhamWebviewPresenterImpl(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    @Override
    public String getCurrentLanguage() {
        return dataManager.getCurrentLanguage();
    }
}
