package com.aecb.ui.introscreen.intromain.presenter;

import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.data.DataManager;

import javax.inject.Inject;

public class IntroMainPresenterImpl extends MvpBasePresenterImpl<IntroMainContract.View>
        implements IntroMainContract.Presenter {

    private DataManager mDataManager;

    @Inject
    IntroMainPresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void setIsFirstTime(boolean isFirstTimeUser) {
        mDataManager.isFirstTime(isFirstTimeUser);
    }
}
