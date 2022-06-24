package com.aecb.ui.introscreen.intromain.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;

public interface IntroMainContract {

    interface View extends BaseView {
    }

    interface Presenter extends BasePresenter<View> {
        void setIsFirstTime(boolean isFirstTimeUser);
    }
}
