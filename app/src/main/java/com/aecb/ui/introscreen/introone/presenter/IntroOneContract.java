package com.aecb.ui.introscreen.introone.presenter;


import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;

public interface IntroOneContract {
    interface View extends BaseView {
    }

    interface Presenter extends BasePresenter<View> {
    }
}