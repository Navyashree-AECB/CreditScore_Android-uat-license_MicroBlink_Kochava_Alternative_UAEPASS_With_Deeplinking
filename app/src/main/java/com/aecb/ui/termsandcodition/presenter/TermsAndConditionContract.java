package com.aecb.ui.termsandcodition.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;

public interface TermsAndConditionContract {
    interface view extends BaseView {
        void loadTermsUrl(String title, String fieldContentEn);
    }

    interface Presenter extends BasePresenter<view> {
        void getTermsConditionUrl();
    }
}