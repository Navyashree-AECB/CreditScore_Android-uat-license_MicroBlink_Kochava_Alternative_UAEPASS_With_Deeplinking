package com.aecb.ui.noscorefragment.presenter;

import com.aecb.data.DataManager;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

public class NoScoreImpl extends MvpBasePresenter<NoScoreContract.View> implements NoScoreContract.Presenter {

    DataManager mDataManager;

    @Inject
    public NoScoreImpl(DataManager dataManager) {
        this.mDataManager = dataManager;
    }
}
