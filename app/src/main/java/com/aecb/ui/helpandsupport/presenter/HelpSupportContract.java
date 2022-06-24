package com.aecb.ui.helpandsupport.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;

public interface HelpSupportContract {
    interface View extends BaseView {
    }

    interface Presenter extends BasePresenter<View> {
    }
}