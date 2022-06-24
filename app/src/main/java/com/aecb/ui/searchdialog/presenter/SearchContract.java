package com.aecb.ui.searchdialog.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.request.updateuserprofile.UpdateUserProfileRequest;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.ui.searchdialog.view.SearchFragment;

public interface SearchContract {
    interface View extends BaseView {

        void showMessage(String message);

        void openProfileDetail(String id, String message,String status,String useCase);

    }

    interface Presenter extends BasePresenter<View> {

        String getAppLanguage();

        void updateUserDetail(UpdateUserProfileRequest updateUserProfileRequest, DBUserTC dbUserTC,
                              SearchFragment context);
    }
}