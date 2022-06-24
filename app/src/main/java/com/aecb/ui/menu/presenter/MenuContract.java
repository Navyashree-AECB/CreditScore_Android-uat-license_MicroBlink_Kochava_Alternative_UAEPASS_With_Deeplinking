package com.aecb.ui.menu.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.request.updateuserprofile.UpdateUserProfileRequest;
import com.aecb.data.db.repository.usertcversion.DBUserTC;

public interface MenuContract {
    interface View extends BaseView {
        void showError(String message);

        void updateUserAdvProfileResponse();

        void updateBiometricResponse(boolean touchEnable);

        void setLastUserDetails(DBUserTC dbUserTC);

        void openLoginActivity();

        void setCheckTouchId();
    }

    interface Presenter extends BasePresenter<View> {
        String getAppLanguage();

        void saveUserAdvProfile(UpdateUserProfileRequest updateUserProfileRequest);

        boolean getTouchLoggedIn();

        void setTouchLoggedIn(boolean isEnabledTouchID);

        void getUserDetail();

        void callLoginApi(boolean touchEnable, String email, String password);

        void logout();

        void updateTouchIdInLocal(boolean touchEnable, String email);
    }
}
