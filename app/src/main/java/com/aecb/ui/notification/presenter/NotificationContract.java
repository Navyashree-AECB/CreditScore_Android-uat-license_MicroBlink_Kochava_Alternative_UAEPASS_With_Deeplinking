package com.aecb.ui.notification.presenter;

import androidx.lifecycle.LifecycleOwner;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.notification.NotificationData;

import java.util.List;

public interface NotificationContract {
    interface View extends BaseView {
        void loadNotification(List<NotificationData> notificationList);

        LifecycleOwner getLifeCycleOwner();
    }

    interface Presenter extends BasePresenter<View> {
        void getNotificationData();

        void deleteAllNotification();

        void deleteNotification(int notificationID);

        void readNotification(int notificationID);

        void getLastUserDetail();
    }
}
