package com.aecb.ui.maintanance.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;

public interface MaintenanceContract {
    interface View extends BaseView {
    }

    interface Presenter extends BasePresenter<View> {
    }
}