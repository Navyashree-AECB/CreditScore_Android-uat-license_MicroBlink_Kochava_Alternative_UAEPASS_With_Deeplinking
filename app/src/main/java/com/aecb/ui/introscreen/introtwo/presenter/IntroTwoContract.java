package com.aecb.ui.introscreen.introtwo.presenter;


import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;

public interface IntroTwoContract {
    interface View extends BaseView {
    }

    interface Presenter extends BasePresenter<View> {
    }
}