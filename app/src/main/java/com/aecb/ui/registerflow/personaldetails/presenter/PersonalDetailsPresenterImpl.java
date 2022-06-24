package com.aecb.ui.registerflow.personaldetails.presenter;

import android.text.TextUtils;

import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.DataManager;
import com.aecb.data.api.models.nationalities.NationalityResponse;
import com.aecb.util.Utilities;
import com.aecb.util.ValidationUtil;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.aecb.AppConstants.DateFormats.DD_MM_YY_DOB;
import static com.aecb.AppConstants.MINIMUM_REQUIRED_AGE;

public class PersonalDetailsPresenterImpl extends MvpBasePresenterImpl<PersonalDetailsContract.View>
        implements PersonalDetailsContract.Presenter {

    private DataManager mDataManager;

    @Inject
    public PersonalDetailsPresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getAllNationalities() {
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<NationalityResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<NationalityResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            view.hideLoading();
                            NationalityResponse nationalityResponse = (NationalityResponse) response;
                            if (nationalityResponse.isSuccess() && nationalityResponse.getData() != null) {
                                view.loadAllNationalities(nationalityResponse.getData().getNationalities());
                            } else {
                                view.showApiFailureError(nationalityResponse.getMessage(), nationalityResponse.getStatus(), "");
                            }
                        }
                    };

            MyAppDisposableObserver<NationalityResponse> disposableObserver = mDataManager.getAllNationalities()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    @Override
    public void sendPersonalDetails(String fullName, String emirateId, String gender, String nationality,
                                    String dateOfBirth, String passportNo, Boolean agreeTermsAndCondition, String selectedYearByEmirates) {
        if (validation(fullName, emirateId, gender, nationality, dateOfBirth, passportNo, agreeTermsAndCondition,
                selectedYearByEmirates)) {
            ifViewAttached(PersonalDetailsContract.View::openContactDetailsActivity);
        }
    }

    @Override
    public String getCurrentAppLanguage() {
        return dataManager.getCurrentLanguage();
    }

    private boolean validation(String fullName, String emirateId, String gender, String nationality,
                               String dateOfBirth, String passportNo, Boolean agreeTermsAndCondition,
                               String selectedYearByEmirates) {
        AtomicBoolean valid = new AtomicBoolean(true);
        DateTimeFormatter formatter = DateTimeFormat.forPattern(DD_MM_YY_DOB);
        DateTime dateOfBirthJoda = formatter.parseDateTime(dateOfBirth);
        int selectedYearOfEmirateId = 2020;
        try {
            selectedYearOfEmirateId = Integer.parseInt(selectedYearByEmirates);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int finalSelectedYearOfEmirateId = selectedYearOfEmirateId;
        ifViewAttached(view -> {
            String[] splitedFullName = fullName.split("\\s+");
            String lastName = "";
            try {
                lastName = splitedFullName[1];
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ((ValidationUtil.isNullOrEmpty(fullName))) {
                view.showEmptyNameError();
                valid.set(false);
            } else if (lastName.length() < 2 || splitedFullName[0].length() < 2) {
                for (String s : splitedFullName) {
                    if (s.length() <= 1 || s.length() > 120 || ValidationUtil.isNullOrEmpty(lastName)) {
                        view.showInvalidNameError();
                        valid.set(false);
                        break;
                    }
                }
            } else if (ValidationUtil.isNullOrEmpty(emirateId)) {
                view.showEmptyEmirateIdError();
                valid.set(false);
            } else if (!TextUtils.isDigitsOnly(emirateId)) {
                view.showInvalidEmirateIdError();
                valid.set(false);
            } else if (emirateId.length() < 15) {
                view.showInvalidEmirateIdError();
                valid.set(false);
            } else if (finalSelectedYearOfEmirateId < 1900 || finalSelectedYearOfEmirateId > 2100) {
                view.showInvalidSecondEmirateIdError();
                valid.set(false);
            } else if (ValidationUtil.isNullOrEmpty(gender)) {
                view.showEmptyGenderError();
                valid.set(false);
            } else if (ValidationUtil.isNullOrEmpty(nationality)) {
                view.showEmptyNationalityError();
                valid.set(false);
            } else if (ValidationUtil.isNullOrEmpty(dateOfBirth)) {
                view.showEmptyDateOfBirthError();
                valid.set(false);
            } else if (!Utilities.isUser18Older(dateOfBirthJoda, MINIMUM_REQUIRED_AGE)) {
                view.showDOBisNot18Years();
                valid.set(false);
           /* } else if (!selectedYearByEmirates.equalsIgnoreCase(dateOfBirth.substring(dateOfBirth.length() - 4))) {
                view.showInvalidDobError();
                valid.set(false);*/
            } else if (!agreeTermsAndCondition) {
                view.showAgreeTCError();
                valid.set(false);
            }
        });
        return valid.get();
    }
}