package com.aecb.ui.loginflow.uaepasspin.presenter;

import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.data.DataManager;

import javax.inject.Inject;

public final class UAEPassPinPresenterImpl extends MvpBasePresenterImpl<UAEPassPinContract.View>
        implements UAEPassPinContract.Presenter {

    private DataManager mDataManager;

    @Inject
    public UAEPassPinPresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public String getCurrentLanguage() {
        return mDataManager.getCurrentLanguage();
    }
}