package com.aecb.ui.scanemirate.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.login.UaePassRequest;
import com.aecb.data.api.models.request.updateuserprofile.UpdateUserProfileRequest;
import com.aecb.data.api.models.response.getuserdetail.GetUserDetailResponse;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.ui.loginflow.uaepasspin.uaepass.UaePassUserModel;

public interface ScanEmirateIdContract {
    interface view extends BaseView {

        void setLastUser(DBUserTC response, UaePassUserModel profileModel);

        void openNextPage();

        void openTCPage(String tcFor, String userName, int tcVersion, GetUserDetailResponse data,
                        DBUserTC dbUserTC);
    }

    interface Presenter extends BasePresenter<view> {
        String getCurrentLanguage();

        void getLastUser(String userName, UaePassUserModel profileModel);

        void loginWithUAEPass(DBUserTC dbUserTC, String password, UaePassRequest uaePassRequest);

        void updateUserDetail(UpdateUserProfileRequest updateUserProfileRequest, String tcFor,
                              DBUserTC dbUserTC, GetUserDetailResponse getUserDetailResponse);
    }
}