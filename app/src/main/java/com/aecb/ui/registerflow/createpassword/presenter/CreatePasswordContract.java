package com.aecb.ui.registerflow.createpassword.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.savedeviceinfo.SaveDeviceInfoRequest;

public interface CreatePasswordContract {
    interface View extends BaseView {
        void showEmptyPasswordError();

        void showInvalidPasswordError();

        void showEmptyConfirmPasswordError();

        void showPasswordNotMatchedError();

        void redirectToParticularScreen();

        void openTouchId(String password);

        void showCurrentPasswordError();

        void showInvalidCurrentPasswordError();

    }

    interface Presenter extends BasePresenter<View> {
        void sendPasswordDetails(String password, String confirmPassword, String sessionToken,
                                 String userName);

        void saveDeviceInfo(SaveDeviceInfoRequest saveDeviceInfoRequest, String password);

        String getFcmId();

        void changePassword(String toString, String toString1, String toString2, String sessionToken, String userName);

        void getLastLoginUser();

        String getInstallSource();

        String getInstalledCampaignId();
    }
}