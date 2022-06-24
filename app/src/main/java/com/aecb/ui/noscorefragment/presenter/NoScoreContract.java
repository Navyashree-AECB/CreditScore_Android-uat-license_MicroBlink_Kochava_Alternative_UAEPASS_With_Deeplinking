package com.aecb.ui.noscorefragment.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;

public interface NoScoreContract {
    interface View extends BaseView {
    }

    interface Presenter extends BasePresenter<View> {
    }
}