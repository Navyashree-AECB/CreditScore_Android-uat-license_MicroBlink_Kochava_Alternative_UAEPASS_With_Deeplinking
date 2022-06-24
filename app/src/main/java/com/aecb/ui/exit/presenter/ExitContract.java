package com.aecb.ui.exit.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;

public interface ExitContract {

    interface View extends BaseView {
    }

    interface Presenter extends BasePresenter<View> {
    }
}