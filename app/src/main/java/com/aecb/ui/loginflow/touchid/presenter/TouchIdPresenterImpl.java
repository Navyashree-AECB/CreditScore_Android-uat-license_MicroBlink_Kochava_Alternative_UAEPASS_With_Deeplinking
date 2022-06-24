package com.aecb.ui.loginflow.touchid.presenter;

import com.aecb.base.db.DbDisposableObserver;
import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.api.models.response.getuserdetail.GetProfileResponse;
import com.aecb.data.api.models.response.getuserdetail.GetUserDetailResponse;
import com.aecb.data.db.repository.usertcversion.DBUserTC;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.aecb.AppConstants.PaymentMethods.PAYMENT_GW;

public class TouchIdPresenterImpl extends MvpBasePresenterImpl<TouchIdContract.View>
        implements TouchIdContract.Presenter {

    @Inject
    public TouchIdPresenterImpl() {

    }

    @Override
    public void setTouchLoggedIn(boolean enable, String password) {
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<GetProfileResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<GetProfileResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            view.hideLoading();
                            GetProfileResponse getUserDetailResponse = (GetProfileResponse) response;
                            GetUserDetailResponse data = getUserDetailResponse.getData();
                            if (data != null) {
                                DBUserTC dbUserTC = new DBUserTC();
                                if (!password.isEmpty()) {
                                    dbUserTC.setPassword(password);
                                }
                                String s = String.valueOf(data.getTcVersionNumber());
                                String result = s.substring(0, s.indexOf("."));
                                int i = Integer.parseInt(result);
                                dbUserTC.setUserName(data.getEmail().toLowerCase());
                                dbUserTC.setTcVersion(i);
                                dbUserTC.setDeviceId(data.getDeviceId());
                                dbUserTC.setTouchId(enable);
                                dbUserTC.setLastUser(1);
                                dbUserTC.setDob(data.getDob());
                                dbUserTC.setPreferredLanguage(data.getPreferredLanguage());
                                dbUserTC.setMobile(data.getMobile());
                                dbUserTC.setEmiratesId(data.getEmiratesId());
                                dbUserTC.setNationalityId(data.getNationalityId());
                                dbUserTC.setGender(data.getGender());
                                dbUserTC.setPassport(data.getPassport());
                                dbUserTC.setFullName(data.getName());
                                dbUserTC.setFirstName(data.getFirstname());
                                dbUserTC.setMiddleName(data.getMiddlename());
                                dbUserTC.setEmail(data.getEmail().toLowerCase());
                                if (data.getPreferredPaymentMethod() != null) {
                                    dbUserTC.setPreferredPaymentMethod(data.getPreferredLanguage());
                                } else {
                                    dbUserTC.setPreferredPaymentMethod(PAYMENT_GW);
                                }
                                updateLastLoginFalse(dbUserTC.getUserName());
                                insertUserDetails(dbUserTC);
                            }
                        }
                    };
            MyAppDisposableObserver<GetProfileResponse> disposableObserver = dataManager.getUserDetail()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
        dataManager.setTouchLoggedIn(enable);
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

    private void insertUserDetails(DBUserTC dbUserTC) {
        ifViewAttached(view -> {
            DbDisposableObserver<Long> myAppDisposableObserver = new
                    DbDisposableObserver<Long>() {
                        @Override
                        protected void onSuccess(Object response) {
                            view.openDashboard(dbUserTC.isTouchId());
                        }
                    };
            DbDisposableObserver<Long> disposableObserver = dataManager.insertUserWithTC(dbUserTC)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }
}