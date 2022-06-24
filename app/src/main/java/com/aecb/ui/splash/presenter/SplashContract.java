package com.aecb.ui.splash.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.db.repository.notification.DbNotification;

public interface SplashContract {

    interface View extends BaseView {
        void setLocale(String locale);

        void insertNotificationIntoDb();
    }

    interface Presenter extends BasePresenter<View> {
        void saveFCMToken();

        boolean isFirstTime();

        void getLastLoginUser();

        String getUserName();

        void insertNotification(DbNotification dbNotification);
    }
}