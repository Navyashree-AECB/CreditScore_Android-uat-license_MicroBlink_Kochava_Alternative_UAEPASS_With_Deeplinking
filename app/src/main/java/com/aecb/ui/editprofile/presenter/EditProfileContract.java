package com.aecb.ui.editprofile.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.request.updateuserprofile.UpdateUserProfileRequest;
import com.aecb.data.db.repository.usertcversion.DBUserTC;

public interface EditProfileContract {
    interface View extends BaseView {
        void openProfile(String message,String status,String useCase);

        void showMessage(String message);

        void showEmptyEmailError();

        void showInvalidEmailError();

        void showEmptyMobileError();

        void showInvalidMobileError();

        void showEmptyPassportNo();

        void showInvalidPassportNo();
    }

    interface Presenter extends BasePresenter<EditProfileContract.View> {
        void updateUserDetail(UpdateUserProfileRequest updateUserProfileRequest, DBUserTC dbUserTC,
                              String editField);
    }
}