package com.aecb.ui.creditreport.creditreport.presenter;

import androidx.lifecycle.LifecycleOwner;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;

public interface CreditReportContract {
    interface View extends BaseView {
        LifecycleOwner getLifeCycleOwner();
        void setTitle(String title);
    }

    interface Presenter extends BasePresenter<View> {
        void getTitleFromProduct(String string);

        String getCurrentLanguage();
    }
}