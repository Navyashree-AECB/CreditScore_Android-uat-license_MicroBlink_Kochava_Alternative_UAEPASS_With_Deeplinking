package com.aecb.ui.loginflow.uaepasspin.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;

public interface UAEPassPinContract {
    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter<View> {
        String getCurrentLanguage();
    }
}