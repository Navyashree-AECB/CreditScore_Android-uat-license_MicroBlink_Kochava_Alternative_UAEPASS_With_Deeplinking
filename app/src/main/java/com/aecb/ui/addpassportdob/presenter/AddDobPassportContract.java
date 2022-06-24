package com.aecb.ui.addpassportdob.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.login.UaePassRequest;
import com.aecb.data.api.models.request.updateuserprofile.UpdateUserProfileRequest;
import com.aecb.data.api.models.response.getuserdetail.GetUserDetailResponse;
import com.aecb.data.db.repository.usertcversion.DBUserTC;

public interface AddDobPassportContract {
    interface View extends BaseView {
        void openTCPage(String updateTcVersion, String userName, int tcVersion, GetUserDetailResponse data, DBUserTC userWithTC);

        void openNextPage();
    }

    interface Presenter extends BasePresenter<View> {
        void loginWithUAEPass(UaePassRequest uaePassRequest);

        void updateUserDetail(UpdateUserProfileRequest updateUserProfileRequest, String tcFor,
                              DBUserTC dbUserTC, GetUserDetailResponse getUserDetailResponse);
    }
}