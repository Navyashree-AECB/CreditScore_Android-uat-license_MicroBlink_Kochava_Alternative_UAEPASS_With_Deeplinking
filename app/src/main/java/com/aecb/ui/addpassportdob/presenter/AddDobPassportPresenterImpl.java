package com.aecb.ui.addpassportdob.presenter;

import com.aecb.AppConstants;
import com.aecb.base.db.DbDisposableObserver;
import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.DataManager;
import com.aecb.data.api.models.BaseResponse;
import com.aecb.data.api.models.login.LoginRequest;
import com.aecb.data.api.models.login.UaePassRequest;
import com.aecb.data.api.models.request.updateuserprofile.UpdateUserProfileRequest;
import com.aecb.data.api.models.response.getuserdetail.GetProfileResponse;
import com.aecb.data.api.models.response.getuserdetail.GetUserDetailResponse;
import com.aecb.data.api.models.response.login.LoginResponse;
import com.aecb.data.api.models.savedeviceinfo.SaveDeviceInfoRequest;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.util.Utilities;
import com.tumblr.remember.Remember;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.aecb.AppConstants.IntentKey.UPDATE_TC_VERSION;
import static com.aecb.AppConstants.PaymentMethods.PAYMENT_GW;
import static com.aecb.AppConstants.generateRandomID;
import static com.aecb.data.api.models.commonmessageresponse.UserCases.LOGIN_CASE;
import static com.aecb.services.MyFirebaseInstanceIDService.KEY_FCM_ID;
import static com.google.firebase.analytics.FirebaseAnalytics.Event.LOGIN;

public class AddDobPassportPresenterImpl extends MvpBasePresenterImpl<AddDobPassportContract.View>
        implements AddDobPassportContract.Presenter {

    private DataManager mDataManager;
    private int tcVersion;
    private DBUserTC userWithTC, dbUser;
    private UpdateUserProfileRequest updateUserProfileRequest = new UpdateUserProfileRequest();

    @Inject
    public AddDobPassportPresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void loginWithUAEPass(UaePassRequest uaePassRequest) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUaePassData(uaePassRequest);
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<LoginResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<LoginResponse>(view, LOGIN) {
                        @Override
                        protected void onSuccess(Object response) {
                            LoginResponse loginResponse = (LoginResponse) response;
                            tcVersion = mDataManager.getTcVersionNumber();
                            if (loginResponse.getData() != null) {
                                if (loginResponse.getData().getSessionToken() != null && !loginResponse.getData().getSessionToken().isEmpty()) {
                                    getUserWithTC(uaePassRequest.getEmail());
                                    mDataManager.setSessionToken(loginResponse.getData().getSessionToken());
                                    getUserDetail(uaePassRequest.getEmail(), "", uaePassRequest);
                                } else if (loginResponse.getData().getParseUser() != null) {
                                    if (loginResponse.getData().getParseUser().getSessionToken() != null &&
                                            !loginResponse.getData().getParseUser().getSessionToken().isEmpty()) {
                                        getUserWithTC(uaePassRequest.getEmail());
                                        mDataManager.setSessionToken(loginResponse.getData().getParseUser().getSessionToken());
                                        getUserDetail(uaePassRequest.getEmail(), "", uaePassRequest);
                                    }
                                } else {
                                    view.hideLoading();
                                }
                            } else {
                                view.hideLoading();
                                view.showApiFailureError(loginResponse.getMessage(), loginResponse.getStatus(), LOGIN_CASE);
                            }
                        }
                    };

            MyAppDisposableObserver<LoginResponse> disposableObserver = mDataManager.login(loginRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    private void getUserDetail(String userName, String password, UaePassRequest uaePassRequest) {
        ifViewAttached(view -> {
            MyAppDisposableObserver<GetProfileResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<GetProfileResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            GetProfileResponse getUserDetailResponse = (GetProfileResponse) response;
                            GetUserDetailResponse data = getUserDetailResponse.getData();
                            if (data != null) {
                                if (!data.getPreferredLanguage().equals(dataManager.getCurrentLanguage())) {
                                    if (dataManager.getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ENG)) {
                                        updateUserProfileRequest.setUpdateType(3);
                                        updateUserProfileRequest.setPreferredlanguage(AppConstants.AppLanguage.ISO_CODE_ENG);
                                    } else {
                                        updateUserProfileRequest.setPreferredlanguage(AppConstants.AppLanguage.ISO_CODE_ARABIC);
                                    }
                                    updateUserProfileRequest.setChannel(2);
                                    updateLanguage(updateUserProfileRequest);
                                    data.setPreferredLanguage(dataManager.getCurrentLanguage());
                                }
                                String userEmail = data.getEmail();
                                data.setEmail(userEmail.toLowerCase());
                                data.setPassword(password);
                                String s = String.valueOf(data.getTcVersionNumber());
                                String result = s.substring(0, s.indexOf("."));
                                int userAcceptedTC = Integer.parseInt(result);
                                if (userWithTC != null) {
                                    // updateUserPassword(userName, password);
                                    if (userWithTC.getEmail().equalsIgnoreCase(uaePassRequest.getEmail())) {
                                        updateUserDOB(userWithTC.getEmail(), uaePassRequest.getDob(),
                                                uaePassRequest.getPassportNumber(), 1);
                                    } else if (userAcceptedTC != tcVersion) {
                                        view.openTCPage(UPDATE_TC_VERSION, userName, tcVersion, data, userWithTC);
                                    } else if (data.getDeviceId() != null && !data.getDeviceId().equals(userWithTC.getDeviceId())) {
                                        mDataManager.setPreviousDeviceID(generateRandomID());
                                        userWithTC.setDeviceId(mDataManager.getPreviousDeviceID());
                                        saveDeviceInfo(mDataManager.getPreviousDeviceID(), userWithTC, data);
                                    } else {
                                        updateLastLoginUser(userWithTC.getUserName(), data, userWithTC);
                                        String preferredPaymentMethod = PAYMENT_GW;
                                        if (data.getPreferredPaymentMethod() != null) {
                                            preferredPaymentMethod = data.getPreferredPaymentMethod();
                                        }
                                        updatePreferredPaymentMethod(userWithTC, preferredPaymentMethod);
                                        view.openNextPage();
                                    }
                                } else if (userAcceptedTC != tcVersion) {
                                    DBUserTC dbUserTC = createDBUser(userName, data, false, uaePassRequest);
                                    view.openTCPage(UPDATE_TC_VERSION, userName, tcVersion, data, dbUserTC);
                                } else {
                                    mDataManager.setPreviousDeviceID(generateRandomID());
                                    DBUserTC dbUserTC = createDBUser(userName, data, false, uaePassRequest);
                                    saveDeviceInfo(mDataManager.getPreviousDeviceID(), dbUserTC, data);
                                }
                            } else {
                                view.showApiFailureError(getUserDetailResponse.getMessage(), getUserDetailResponse.getStatus(), LOGIN_CASE);
                            }
                        }
                    };

            MyAppDisposableObserver<GetProfileResponse> disposableObserver = mDataManager.getUserDetail()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    private void updateLanguage(UpdateUserProfileRequest updateUserProfileRequest) {
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<BaseResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<BaseResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            BaseResponse baseResponse = (BaseResponse) response;
                            if (baseResponse != null) {
                                view.hideLoading();
                                if (!baseResponse.isSuccess()) {
                                    view.showApiFailureError(baseResponse.getMessage(), baseResponse.getStatus(), "");
                                }
                            }
                        }
                    };

            MyAppDisposableObserver<BaseResponse> disposableObserver = dataManager.updateUserProfile(updateUserProfileRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    private void getUserWithTC(String userName) {
        ifViewAttached(view -> {
            DbDisposableObserver<DBUserTC> myAppDisposableObserver = new
                    DbDisposableObserver<DBUserTC>() {
                        @Override
                        protected void onSuccess(Object response) {
                            try {
                                userWithTC = (DBUserTC) response;
                            } catch (Exception e) {
                                Timber.e(e.getMessage());
                            }
                        }
                    };
            DbDisposableObserver<DBUserTC> disposableObserver = dataManager.getUserWithTC(userName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    private void saveDeviceInfo(String deviceID, DBUserTC dbUserTC, GetUserDetailResponse data) {
        SaveDeviceInfoRequest saveDeviceInfoRequest = new SaveDeviceInfoRequest();
        saveDeviceInfoRequest.setDeviceManufacturer(Utilities.getDeviceName());
        saveDeviceInfoRequest.setDeviceOsVersion(Utilities.getAndroidVersion());
        saveDeviceInfoRequest.setDeviceType(AppConstants.ApiParameter.DEVICE_TYPE);
        saveDeviceInfoRequest.setDeviceToken(Remember.getString(KEY_FCM_ID, ""));
        saveDeviceInfoRequest.setDeviceTimeZone(Utilities.getDeviceTimeZone());
        saveDeviceInfoRequest.setDeviceId(deviceID);
        ifViewAttached(view -> {
            MyAppDisposableObserver<BaseResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<BaseResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            BaseResponse baseResponse = (BaseResponse) response;
                            if (baseResponse.isSuccess()) {
                                insertUserDetails(dbUserTC, data);
                            }
                        }
                    };

            MyAppDisposableObserver<BaseResponse> disposableObserver = mDataManager.saveDeviceInfo(saveDeviceInfoRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    private void insertUserDetails(DBUserTC dbUserTC, GetUserDetailResponse data) {
        ifViewAttached(view -> {
            DbDisposableObserver<Long> myAppDisposableObserver = new
                    DbDisposableObserver<Long>() {
                        @Override
                        protected void onSuccess(Object response) {
                            updateLastLoginUser(dbUserTC.getUserName(), data, dbUserTC);

                        }
                    };
            DbDisposableObserver<Long> disposableObserver = dataManager.insertUserWithTC(dbUserTC)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    private void updateLastLoginUser(String userName, GetUserDetailResponse data, DBUserTC dbUserTC) {
        ifViewAttached(view -> {
            DbDisposableObserver<Long> myAppDisposableObserver = new
                    DbDisposableObserver<Long>() {
                        @Override
                        protected void onSuccess(Object response) {
                            String s = String.valueOf(data.getTcVersionNumber());
                            String result = s.substring(0, s.indexOf("."));
                            int i = Integer.parseInt(result);
                            if (i == tcVersion || userWithTC == null) {
                                view.openNextPage();
                            } else {
                                view.hideLoading();
                                view.openTCPage(UPDATE_TC_VERSION, userName, tcVersion, data, dbUserTC);
                            }
                            updateLastLoginFalse(userName);
                        }
                    };
            DbDisposableObserver<Long> disposableObserver = dataManager.updateLasUserLogin(userName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    private void updateLastLoginFalse(String userName) {
        ifViewAttached(view -> {
            DbDisposableObserver<Long> myAppDisposableObserver = new
                    DbDisposableObserver<Long>() {
                        @Override
                        protected void onSuccess(Object response) {

                        }
                    };
            DbDisposableObserver<Long> disposableObserver = dataManager.updateLasUserLoginFalse(userName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    private DBUserTC createDBUser(String userName, GetUserDetailResponse data, boolean isTouchID, UaePassRequest uaePassRequest) {
        DBUserTC dbUserTC = new DBUserTC();
        dbUserTC.setUserName(userName);
        dbUserTC.setTcVersion(tcVersion);
        dbUserTC.setDeviceId(mDataManager.getPreviousDeviceID());
        dbUserTC.setEmail(data.getEmail());
        dbUserTC.setMobile(data.getMobile());
        dbUserTC.setEmiratesId(data.getEmiratesId());
        dbUserTC.setLastUser(1);
        dbUserTC.setTouchId(isTouchID);
        if (data.getDob() == null) {
            dbUserTC.setDob(uaePassRequest.getDob());
        } else {
            dbUserTC.setDob(data.getDob());
        }
        dbUserTC.setNationalityId(data.getNationalityId());
        dbUserTC.setGender(data.getGender());
        if (data.getPassport() == null) {
            dbUserTC.setPassport(uaePassRequest.getPassportNumber());
        } else {
            dbUserTC.setPassport(data.getPassport());
        }
        dbUserTC.setPreferredLanguage(data.getPreferredLanguage());
        dbUserTC.setFullName(data.getName());
        dbUserTC.setFirstName(data.getFirstname());
        dbUserTC.setIsUAEPassUser(1);
        dbUserTC.setMiddleName(data.getMiddlename());
        dbUserTC.setPassword(data.getPassword());
        if (data.getPreferredPaymentMethod() != null) {
            dbUserTC.setPreferredPaymentMethod(data.getPreferredPaymentMethod());
        } else {
            dbUserTC.setPreferredPaymentMethod(PAYMENT_GW);
        }

        return dbUserTC;
    }

    private void updatePreferredPaymentMethod(DBUserTC dbUserTC, String preferredPaymentMethod) {
        ifViewAttached(view -> {
            DbDisposableObserver<Long> myAppDisposableObserver = new
                    DbDisposableObserver<Long>() {
                        @Override
                        protected void onSuccess(Object response) {
                            Timber.e(response.toString());
                        }
                    };
            DbDisposableObserver<Long> disposableObserver = dataManager.updatePreferredPaymentMethod(dbUserTC.getUserName(), preferredPaymentMethod)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    private void updateUserDOB(String email, String dob, String passport, int isUAEPassUser) {
        ifViewAttached(view -> {
            DbDisposableObserver<Long> myAppDisposableObserver = new
                    DbDisposableObserver<Long>() {
                        @Override
                        protected void onSuccess(Object response) {
                            updateUserPassport(email, passport, isUAEPassUser);
                        }
                    };
            DbDisposableObserver<Long> disposableObserver = dataManager.updateUserDOB(email, dob)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }


    private void updateUserPassport(String email, String passport, int isUAEPassUser) {
        ifViewAttached(view -> {
            DbDisposableObserver<Long> myAppDisposableObserver = new
                    DbDisposableObserver<Long>() {
                        @Override
                        protected void onSuccess(Object response) {
                            Timber.e(response.toString());
                            updateIsUAEPassUser(email, isUAEPassUser);
                        }
                    };
            DbDisposableObserver<Long> disposableObserver = dataManager.updateUserPassport(email, passport)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    private void updateIsUAEPassUser(String email, int isUAEPassUser) {
        ifViewAttached(view -> {
            DbDisposableObserver<Long> myAppDisposableObserver = new
                    DbDisposableObserver<Long>() {
                        @Override
                        protected void onSuccess(Object response) {
                            updateLastLogin(email);
                        }
                    };
            DbDisposableObserver<Long> disposableObserver = dataManager.updateIsUAEPassUser(email, isUAEPassUser)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    @Override
    public void updateUserDetail(UpdateUserProfileRequest updateUserProfileRequest, String tcfor, DBUserTC dbUserTC, GetUserDetailResponse data) {
        ifViewAttached(view -> {
            MyAppDisposableObserver<BaseResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<BaseResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            BaseResponse baseResponse = (BaseResponse) response;
                            if (baseResponse.getStatus().equals("uu_02")) {
                                if (userWithTC != null) {
                                    if (!userWithTC.getDeviceId().equals(dbUserTC.getDeviceId())) {
                                        mDataManager.setPreviousDeviceID(generateRandomID());
                                        dbUserTC.setDeviceId(mDataManager.getPreviousDeviceID());
                                        saveDeviceInfo(mDataManager.getPreviousDeviceID(), dbUserTC, data);
                                    } else {
                                        updateUserDetails(dbUserTC);
                                    }
                                } else {
                                    mDataManager.setPreviousDeviceID(generateRandomID());
                                    dbUserTC.setDeviceId(mDataManager.getPreviousDeviceID());
                                    saveDeviceInfo(mDataManager.getPreviousDeviceID(), dbUserTC, data);
                                }

                            }
                        }
                    };

            MyAppDisposableObserver<BaseResponse> disposableObserver = mDataManager.updateUserProfile(updateUserProfileRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    private void updateUserDetails(DBUserTC dbUserTC) {
        ifViewAttached(view -> {
            DbDisposableObserver<Long> myAppDisposableObserver = new
                    DbDisposableObserver<Long>() {
                        @Override
                        protected void onSuccess(Object response) {
                            Timber.e(dbUserTC.toString());
                            view.hideLoading();
                            updateLastLogin(dbUserTC.getUserName());
                        }
                    };
            DbDisposableObserver<Long> disposableObserver = dataManager.updateUserTC(dbUserTC)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    private void updateLastLogin(String userName) {
        ifViewAttached(view -> {
            DbDisposableObserver<Long> myAppDisposableObserver = new
                    DbDisposableObserver<Long>() {
                        @Override
                        protected void onSuccess(Object response) {
                            updateLastLoginFalse(userName);
                            updateTouchIdInLocal(false, userName);
                            view.openNextPage();
                        }
                    };
            DbDisposableObserver<Long> disposableObserver = dataManager.updateLasUserLogin(userName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    public void updateTouchIdInLocal(boolean touchEnable, String email) {
        DbDisposableObserver<Long> myAppDisposableObserver = new DbDisposableObserver<Long>() {
            @Override
            protected void onSuccess(Object response) {
                ifViewAttached(view -> {

                });
            }
        };
        DbDisposableObserver<Long> disposableObserver =
                dataManager.updateTouchId(touchEnable, email)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(myAppDisposableObserver);
        compositeDisposable.add(disposableObserver);
    }


}