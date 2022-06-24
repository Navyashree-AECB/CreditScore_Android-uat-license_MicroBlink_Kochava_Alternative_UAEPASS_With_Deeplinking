package com.aecb.ui.aboutus.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;

public interface AboutusContract {
    interface View extends BaseView {
        void loadWebView(String loadWebViewUrl);
    }

    interface Presenter extends BasePresenter<View> {
        void getUrl();

        String getLanguage();

    }
}