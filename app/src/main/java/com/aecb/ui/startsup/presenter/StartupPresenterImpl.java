package com.aecb.ui.startsup.presenter;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;

import androidx.annotation.RequiresApi;

import com.aecb.AppConstants;
import com.aecb.base.db.DbDisposableObserver;
import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.base.network.MyAppDisposableObserver;
import com.aecb.data.DataManager;
import com.aecb.data.api.models.BaseResponse;
import com.aecb.data.api.models.configuration.ApplicationCodesItem;
import com.aecb.data.api.models.configuration.ConfigurationResponse;
import com.aecb.data.api.models.login.LoginRequest;
import com.aecb.data.api.models.login.UaePassRequest;
import com.aecb.data.api.models.request.updateuserprofile.UpdateUserProfileRequest;
import com.aecb.data.api.models.response.getuserdetail.GetProfileResponse;
import com.aecb.data.api.models.response.getuserdetail.GetUserDetailResponse;
import com.aecb.data.api.models.response.login.LoginResponse;
import com.aecb.data.api.models.savedeviceinfo.SaveDeviceInfoRequest;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.presentation.biometric.BiometricCallbackV23;
import com.aecb.presentation.biometric.BiometricCallbackV28;
import com.aecb.presentation.biometric.BiometricUtils;
import com.aecb.ui.loginflow.uaepasspin.uaepass.UaePassUserModel;
import com.aecb.util.AESEncryption;
import com.aecb.util.Utilities;
import com.google.gson.Gson;
import com.tumblr.remember.Remember;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static android.content.Context.FINGERPRINT_SERVICE;
import static com.aecb.AppConstants.AppUnderMaintenance.MAINTENANCE_DESCRIPTION_MOBILE;
import static com.aecb.AppConstants.AppUnderMaintenance.MAINTENANCE_TITLE_MOBILE;
import static com.aecb.AppConstants.IntentKey.UPDATE_TC_VERSION;
import static com.aecb.AppConstants.PaymentMethods.PAYMENT_GW;
import static com.aecb.AppConstants.generateRandomID;
import static com.aecb.data.api.models.commonmessageresponse.UserCases.LOGIN_CASE;
import static com.aecb.services.MyFirebaseInstanceIDService.KEY_FCM_ID;
import static com.aecb.util.Utilities.fullString;
import static com.google.firebase.analytics.FirebaseAnalytics.Event.LOGIN;

public final class StartupPresenterImpl extends MvpBasePresenterImpl<StartupContract.View>
        implements StartupContract.Presenter {

    private static final String KEY_NAME = UUID.randomUUID().toString();
    private DataManager mDataManager;
    private BiometricCallbackV23 helper;
    private DBUserTC userWithTC, dbUser;
    private KeyStore keyStore;
    private Cipher cipher;
    private int tcVersion;

    @Inject
    public StartupPresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public String getCurrentLanguage() {
        return mDataManager.getCurrentLanguage();
    }

    @Override
    public void setCurrentLanguage(String currentLanguage) {
        mDataManager.setCurrentLanguage(currentLanguage);
    }

    @Override
    public void getAppConfigurations() {
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<ConfigurationResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<ConfigurationResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            view.hideLoading();
                            ConfigurationResponse configurationsResponse = (ConfigurationResponse) response;
                            if (configurationsResponse.getData() != null && configurationsResponse.isSuccess()) {
                                Long otpTime = 0L;
                                if (configurationsResponse.getData().getOtpAliveTime() != null) {
                                    otpTime = configurationsResponse.getData().getOtpAliveTime();
                                }
                                String drupalURL = configurationsResponse.getData().getDrupalBaseUrl();
                                String tcEN = configurationsResponse.getData().getPages().getTcAecbEn();
                                String tcAR = configurationsResponse.getData().getPages().getTcAecbAr();

                                String maintenanceStartDate = "";
                                String maintenanceEndDate = "";
                                String minAndroidAppVersion = "1";
                                if (configurationsResponse.getData().getMaintenanceStartDate() != null) {
                                    maintenanceStartDate = configurationsResponse.getData().getMaintenanceStartDate().getIso();
                                }
                                if (configurationsResponse.getData().getMaintenanceEndDate() != null) {
                                    maintenanceEndDate = configurationsResponse.getData().getMaintenanceEndDate().getIso();
                                }
                                if (configurationsResponse.getData().getMinAndroidAppVersion() != null) {
                                    minAndroidAppVersion = configurationsResponse.getData().getMinAndroidAppVersion();
                                }
                                String maintenanceDes = "";
                                String maintenanceTitle = "";
                                List<ApplicationCodesItem> list = configurationsResponse.getData().getApplicationCodes();
                                for (ApplicationCodesItem item : list) {
                                    if (item.getCode().equals(MAINTENANCE_DESCRIPTION_MOBILE)) {
                                        if (mDataManager.getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ARABIC))
                                            maintenanceDes = item.getMessageAr();
                                        else
                                            maintenanceDes = item.getMessageEn();
                                    }
                                    if (item.getCode().equals(MAINTENANCE_TITLE_MOBILE)) {
                                        if (mDataManager.getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ARABIC))
                                            maintenanceTitle = item.getMessageAr();
                                        else
                                            maintenanceTitle = item.getMessageEn();
                                    }
                                }
                                dataManager.setOtpTime(otpTime);
                                dataManager.setTcVersionNumber(configurationsResponse.getData().getTermsAndConditions()
                                        .getVersionNumber());
                                dataManager.setConfigurationData(new Gson().toJson(configurationsResponse.getData()));
                                view.setAppConfigurationResponse(configurationsResponse.getData().getTermsAndConditions()
                                                .getVersionNumber(), drupalURL, tcEN, tcAR, maintenanceStartDate,
                                        maintenanceEndDate, maintenanceDes, maintenanceTitle, minAndroidAppVersion);
                            } else {
                                view.showApiFailureError(configurationsResponse.getMessage(), configurationsResponse.getStatus(), "");
                            }
                        }
                    };

            MyAppDisposableObserver<ConfigurationResponse> disposableObserver = dataManager.getAppConfigurations()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    @Override
    public void getLastLoginUser() {
        ifViewAttached(view -> {
            DbDisposableObserver<DBUserTC> myAppDisposableObserver = new
                    DbDisposableObserver<DBUserTC>() {
                        @Override
                        protected void onSuccess(Object response) {
                            dbUser = (DBUserTC) response;
                            if (dbUser != null) {
                                view.setUser(dbUser);
                            }
                        }
                    };

            DbDisposableObserver<DBUserTC> disposableObserver = dataManager.getLastLoginUser()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    @Override
    public void loginWithUAEPass(DBUserTC dbUserTC, String password, UaePassRequest uaePassRequest) {
        LoginRequest loginRequest = new LoginRequest();
        uaePassRequest.setDob(dbUserTC.getDob());
        uaePassRequest.setPassportNumber(dbUserTC.getPassport());
        loginRequest.setUaePassData(uaePassRequest);
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<LoginResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<LoginResponse>(view, LOGIN) {
                        @Override
                        protected void onSuccess(Object response) {
                            LoginResponse loginResponse = (LoginResponse) response;
                            if (loginResponse.isSuccess()) {
                                tcVersion = mDataManager.getTcVersionNumber();
                                mDataManager.setSessionToken(loginResponse.getData().getSessionToken());
                                getUserDetail(dbUserTC.getEmail(), password);
                            } else {
                                view.hideLoading();
                                view.showApiFailureError(loginResponse.getMessage(), loginResponse.getStatus(), "");
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
                            view.hideLoading();
                        }
                    };

            MyAppDisposableObserver<LoginResponse> disposableObserver = mDataManager.login(loginRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void cancelBiometric(BiometricCallbackV28 biometricCallbackV28) {
        if (helper != null && helper.getCancellationSignal() != null) {
            helper.cancellationSignal("finish activity");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (biometricCallbackV28 != null && biometricCallbackV28.getCancellationSignal() != null) {
                biometricCallbackV28.cancellationSignal("finish activity");
            }
        }
    }

    @Override
    public void login(String email, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(createEncryptedRequest(email));
        loginRequest.setPassword(createEncryptedRequest(password));
        if (email != null && password != null) {
            ifViewAttached(view -> {
                view.showLoading(null);
                MyAppDisposableObserver<LoginResponse> myAppDisposableObserver =
                        new MyAppDisposableObserver<LoginResponse>(view) {
                            @Override
                            protected void onSuccess(Object response) {
                                view.hideLoading();
                                LoginResponse loginResponse = (LoginResponse) response;
                                if (loginResponse.getData() != null) {
                                    if (loginResponse.getData().getSessionToken() != null && !loginResponse.getData().getSessionToken().isEmpty()) {
                                        tcVersion = mDataManager.getTcVersionNumber();
                                        mDataManager.setSessionToken(loginResponse.getData().getSessionToken());
                                        getUserDetail(email, password);
                                    }
                                } else {
                                    view.showApiFailureError(loginResponse.getMessage(), loginResponse.getStatus(), "");
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
    }

    public String createEncryptedRequest(String requestBody) {
        AESEncryption.setKeyOld(fullString());
        AESEncryption.encrypt(requestBody);
        return AESEncryption.getEncryptedString()
                .replaceAll("\\s", "")
                .replaceAll("\\n", "")
                .replaceAll("\\r", "");
    }

    private void getUserDetail(String userName, String password) {
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
                                String userEmail = data.getEmail();
                                data.setEmail(userEmail.toLowerCase());
                                data.setPassword(password);
                                String s = String.valueOf(data.getTcVersionNumber());
                                String result = s.substring(0, s.indexOf("."));
                                int userAcceptedTC = Integer.parseInt(result);
                                if (dbUser != null) {
                                    if (userAcceptedTC != tcVersion) {
                                        view.openTCPage(UPDATE_TC_VERSION, userName, tcVersion, data, dbUser);
                                    } else if (data.getDeviceId() != null && !data.getDeviceId().equals(dbUser.getDeviceId())) {
                                        updateLastLoginUser(userName);
                                        mDataManager.setPreviousDeviceID(generateRandomID());
                                        dbUser.setDeviceId(mDataManager.getPreviousDeviceID());
                                        saveDeviceInfo(mDataManager.getPreviousDeviceID(), dbUser, data);
                                    } else {
                                        updateLastLoginUser(userName);
                                        String preferredPaymentMethod = PAYMENT_GW;
                                        if (data.getPreferredPaymentMethod() != null) {
                                            preferredPaymentMethod = data.getPreferredPaymentMethod();
                                        }
                                        updatePreferredPaymentMethod(dbUser, preferredPaymentMethod);
                                        view.openNextPage();
                                    }
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

    private void updateLastLoginUser(String userName) {
        ifViewAttached(view -> {
            DbDisposableObserver<Long> myAppDisposableObserver = new
                    DbDisposableObserver<Long>() {
                        @Override
                        protected void onSuccess(Object response) {
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

    @Override
    public void updateUserDetail(UpdateUserProfileRequest updateUserProfileRequest, String tcfor, DBUserTC dbUserTC) {
        ifViewAttached(view -> {
            view.showLoading(null);
            MyAppDisposableObserver<BaseResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<BaseResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            view.hideLoading();
                            BaseResponse baseResponse = (BaseResponse) response;
                            if (baseResponse.getStatus().equals("uu_02")) {
                                Timber.e("updateUserDetails :" + "uu_02");
                                if (tcfor != null && tcfor.equals(UPDATE_TC_VERSION)) {
                                    updateUserDetails(dbUserTC);
                                    Timber.e("updateUserDetails :" + "updateUserDetails");
                                } else {
                                    view.openNextPage();
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
                            view.openNextPage();
                            Timber.e("updateUserDetails :" + "openNextPage");

                        }
                    };
            DbDisposableObserver<Long> disposableObserver = dataManager.updateUserTC(dbUserTC)
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
            view.showLoading(null);
            MyAppDisposableObserver<BaseResponse> myAppDisposableObserver =
                    new MyAppDisposableObserver<BaseResponse>(view) {
                        @Override
                        protected void onSuccess(Object response) {
                            view.hideLoading();
                            BaseResponse baseResponse = (BaseResponse) response;
                            if (baseResponse.isSuccess()) {
                                if (data.getPreferredPaymentMethod() != null) {
                                    dbUserTC.setPreferredPaymentMethod(data.getPreferredPaymentMethod());
                                } else {
                                    dbUserTC.setPreferredPaymentMethod(PAYMENT_GW);
                                }
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
                            view.openNextPage();
                        }
                    };
            DbDisposableObserver<Long> disposableObserver = dataManager.insertUserWithTC(dbUserTC)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void setUpBiometric(Context context) {
        if (BiometricUtils.isSdkVersionSupported()) {
            if (BiometricUtils.isPermissionGranted(context)) {
                if (BiometricUtils.isHardwareSupported(context)) {
                    if (BiometricUtils.isFingerprintAvailable(context)) {
                        if (BiometricUtils.isKeyguardSecure(context)) {
                            generateKey();
                            if (cipherInit()) {
                                FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(FINGERPRINT_SERVICE);
                                FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                                helper = new BiometricCallbackV23(context);
                                helper.startAuth(fingerprintManager, cryptoObject);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void setInstalledCampaignId(String campId) {
        dataManager.setInstalledCampaignId(campId);
    }

    @Override
    public void setInstallSource(String referral) {
        dataManager.setInstallSource(referral);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            Timber.e(e.getMessage());
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            Timber.e(e.getMessage());
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void generateKey() {
        try {
            if (keyStore == null) {
                keyStore = KeyStore.getInstance("AndroidKeyStore");
            }
        } catch (Exception e) {
            Timber.d("Exception : " + e.toString());
        }

        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | CertificateException | IOException e) {
            Timber.e(e.getMessage());
        }
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

    @Override
    public void getLastUser(String userName, UaePassUserModel profileModel) {
        ifViewAttached(view -> {
            DbDisposableObserver<DBUserTC> myAppDisposableObserver = new
                    DbDisposableObserver<DBUserTC>() {
                        @Override
                        protected void onSuccess(Object response) {
                            try {
                                view.setLastUser((DBUserTC) response, profileModel);
                            } catch (Exception e) {
                                Timber.e(e.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
                            view.setLastUser(null, profileModel);
                        }
                    };
            DbDisposableObserver<DBUserTC> disposableObserver = dataManager.getUserWithTC(userName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(myAppDisposableObserver);
            compositeDisposable.add(disposableObserver);
        });
    }
}