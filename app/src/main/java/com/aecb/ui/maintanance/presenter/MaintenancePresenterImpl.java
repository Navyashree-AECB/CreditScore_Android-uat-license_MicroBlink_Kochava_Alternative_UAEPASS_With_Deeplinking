package com.aecb.ui.maintanance.presenter;

import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.data.DataManager;

import javax.inject.Inject;

public class MaintenancePresenterImpl extends MvpBasePresenterImpl<MaintenanceContract.View>
        implements MaintenanceContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public MaintenancePresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

}