package com.aecb.ui.startsup.presenter;

import android.content.Context;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.login.UaePassRequest;
import com.aecb.data.api.models.request.updateuserprofile.UpdateUserProfileRequest;
import com.aecb.data.api.models.response.getuserdetail.GetUserDetailResponse;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.presentation.biometric.BiometricCallbackV28;
import com.aecb.ui.loginflow.uaepasspin.uaepass.UaePassUserModel;

public interface StartupContract {
    interface View extends BaseView {
        void setAppConfigurationResponse(int tcVersion, String drupalURL, String tcEN, String tcAR,
                                         String maintenanceStartDate, String maintenanceEndDate,
                                         String maintenanceDes, String maintenanceTitle, String minAndroidAppVersion);

        void setUser(DBUserTC dbUser);

        void openTCPage(String tcFor, String userName, int tcVersion, GetUserDetailResponse data,
                        DBUserTC dbUserTC);

        void openNextPage();

        void openAddDobPassportScreen();

        void setLastUser(DBUserTC dbUserTC, UaePassUserModel profileModel);
    }

    interface Presenter extends BasePresenter<View> {
        String getCurrentLanguage();

        void setCurrentLanguage(String currentLanguage);

        void updateUserDetail(UpdateUserProfileRequest updateUserProfileRequest, String tcFor, DBUserTC dbUserTC);

        void getAppConfigurations();

        void getLastLoginUser();

        void cancelBiometric(BiometricCallbackV28 biometricCallbackV28);

        void login(String email, String password);

        void setUpBiometric(Context context);

        void setInstalledCampaignId(String campId);

        void setInstallSource(String referral);

        void loginWithUAEPass(DBUserTC dbUserTC, String password, UaePassRequest uaePassRequest);

        void getLastUser(String userName, UaePassUserModel profileModel);
    }
}