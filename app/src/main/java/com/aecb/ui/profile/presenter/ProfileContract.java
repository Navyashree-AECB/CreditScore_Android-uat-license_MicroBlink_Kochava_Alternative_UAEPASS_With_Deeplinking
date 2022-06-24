package com.aecb.ui.profile.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.nationalities.NationalitiesItem;
import com.aecb.data.db.repository.usertcversion.DBUserTC;

import java.util.List;

public interface ProfileContract {
    interface View extends BaseView {

        void loadAllNationalities(List<NationalitiesItem> nationalities);

        void showError(String message);

        void setUser(DBUserTC dbUserItem);
    }

    interface Presenter extends BasePresenter<View> {
        void getAllNationalities();

        void getLastUserDetail();
    }

}