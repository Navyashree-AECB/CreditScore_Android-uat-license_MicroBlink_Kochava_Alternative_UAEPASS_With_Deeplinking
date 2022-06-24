package com.aecb.ui.termsandconditions.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;

public interface TermsConditionContract {
    interface View extends BaseView {
        void loadTermsUrl(String title, String fieldContentEn);
    }

    interface Presenter extends BasePresenter<View> {
        void getTermsConditionUrl();

        int getTCVersion();
    }
}