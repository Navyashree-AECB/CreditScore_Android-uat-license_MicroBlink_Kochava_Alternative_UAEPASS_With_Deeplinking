package com.aecb.ui.devicerooted.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;

public interface DeviceRootedContract {
    interface View extends BaseView {
    }

    interface Presenter extends BasePresenter<View> {
    }
}