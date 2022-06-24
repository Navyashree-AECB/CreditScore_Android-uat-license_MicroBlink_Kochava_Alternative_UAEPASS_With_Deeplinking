package com.aecb.ui.loginflow.touchid.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;

public interface TouchIdContract {
    interface View extends BaseView {
        void openDashboard(boolean enable);
    }

    interface Presenter extends BasePresenter<View> {
        void setTouchLoggedIn(boolean enable, String password);
    }
}