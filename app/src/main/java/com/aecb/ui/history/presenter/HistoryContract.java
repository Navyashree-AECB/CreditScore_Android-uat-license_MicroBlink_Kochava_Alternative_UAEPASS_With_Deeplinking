package com.aecb.ui.history.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;

public interface HistoryContract {
    interface View extends BaseView {
    }

    interface Presenter extends BasePresenter<View> {
    }
}