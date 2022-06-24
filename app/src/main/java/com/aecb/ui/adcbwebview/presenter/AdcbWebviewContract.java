package com.aecb.ui.adcbwebview.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;

public interface AdcbWebviewContract {
    interface View extends BaseView {
    }

    interface Presenter extends BasePresenter<View> {
        String getCurrentLanguage();
    }
}
