package com.aecb.ui.helpandsupport.presenter;

import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.data.DataManager;

import javax.inject.Inject;

public class HelpSupportPresenterImpl extends MvpBasePresenterImpl<HelpSupportContract.View>
        implements HelpSupportContract.Presenter {

    private DataManager mDataManager;

    @Inject
    public HelpSupportPresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }
}
