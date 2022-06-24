package com.aecb.ui.registerflow.personaldetails.presenter;

import com.aecb.base.mvp.BasePresenter;
import com.aecb.base.mvp.BaseView;
import com.aecb.data.api.models.nationalities.NationalitiesItem;

import java.util.List;

public interface PersonalDetailsContract {

    interface View extends BaseView {
        void showEmptyNameError();

        void showEmptyEmirateIdError();

        void showEmptyGenderError();

        void showEmptyNationalityError();

        void showEmptyDateOfBirthError();

        void showEmptyPassportError();

        void openContactDetailsActivity();

        void showAgreeTCError();

        void showInvalidNameError();

        void showInvalidEmirateIdError();

        void showInvalidPassportError();

        void loadAllNationalities(List<NationalitiesItem> nationalities);

        void showError(String message);

        void showInvalidDobError();

        void showDOBisNot18Years();

        void showInvalidSecondEmirateIdError();
    }

    interface Presenter extends BasePresenter<View> {
        void getAllNationalities();

        void sendPersonalDetails(String fullName, String emirateId, String gender, String nationality,
                                 String dateOfBirth, String passportNo, Boolean agreeTermsAndCondition,
                                 String selectedYearByEmirates);

        String getCurrentAppLanguage();
    }
}