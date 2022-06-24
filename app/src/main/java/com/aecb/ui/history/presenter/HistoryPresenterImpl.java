package com.aecb.ui.history.presenter;

import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.data.DataManager;

import javax.inject.Inject;

public class HistoryPresenterImpl extends MvpBasePresenterImpl<HistoryContract.View>
        implements HistoryContract.Presenter {

    private DataManager mDataManager;

    @Inject
    public HistoryPresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }
}
