package com.aecb.ui.loginflow.login.presenter;

import android.content.Context;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.login.UaePassRequest;
import com.aecb.data.api.models.request.updateuserprofile.UpdateUserProfileRequest;
import com.aecb.data.api.models.response.getuserdetail.GetUserDetailResponse;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.presentation.biometric.BiometricCallbackV28;
import com.aecb.ui.loginflow.uaepasspin.uaepass.UaePassUserModel;

public interface LoginContract {
    interface View extends BaseView {
        void showEmptyEmailError();

        void showInvalidEmailError();

        void showPasswordLengthError();

        void showEmptyPasswordError();

        void showInvalidPassword();

        void openNextPage();

        void openTCPage(String tcFor, String userName, int tcVersion, GetUserDetailResponse data,
                        DBUserTC dbUserTC);

        void setUser(DBUserTC dbUser);

        void setLastUser(DBUserTC response, UaePassUserModel profileModel);
    }

    interface Presenter extends BasePresenter<View> {
        void login(String email, String password);

        void updateUserDetail(UpdateUserProfileRequest updateUserProfileRequest, String tcFor,
                              DBUserTC dbUserTC, GetUserDetailResponse getUserDetailResponse);

        void setUpBiometric(Context context);

        void cancelBiometric(BiometricCallbackV28 biometricCallbackV28);

        void getLastLoginUser();

        String getCurrentLanguage();

        void getLastUser(String email, UaePassUserModel userInfo);

        void loginWithUAEPass(DBUserTC dbUserTC, String password, UaePassRequest uaePassRequest);
    }
}