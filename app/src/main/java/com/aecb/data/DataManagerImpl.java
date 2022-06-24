package com.aecb.data;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

import com.aecb.data.api.ApiHelper;
import com.aecb.data.api.models.BaseResponse;
import com.aecb.data.api.models.adcb.MakePayment6Response;
import com.aecb.data.api.models.banklist.GetBankListReponse;
import com.aecb.data.api.models.biometric.UpdateBiometricRequest;
import com.aecb.data.api.models.buycreditreport.BuyCreditReportRequest;
import com.aecb.data.api.models.buycreditreport.BuyCreditReportResponse;
import com.aecb.data.api.models.changepassword.ChangePasswordRequest;
import com.aecb.data.api.models.changepassword.ChangePasswordResponse;
import com.aecb.data.api.models.configuration.ConfigurationData;
import com.aecb.data.api.models.configuration.ConfigurationResponse;
import com.aecb.data.api.models.contactus.ContactUsSubmitRequest;
import com.aecb.data.api.models.edirham.PurchaseCheckoutRequest;
import com.aecb.data.api.models.emailrecipients.EmailRecipientsRequest;
import com.aecb.data.api.models.generateotp.GenerateOtpRequest;
import com.aecb.data.api.models.generateotp.GenerateOtpResponse;
import com.aecb.data.api.models.getfile.GetFileResponse;
import com.aecb.data.api.models.getproducts.GetProductResponse;
import com.aecb.data.api.models.login.LoginRequest;
import com.aecb.data.api.models.nationalities.NationalityResponse;
import com.aecb.data.api.models.payment_method.AddCardResponse;
import com.aecb.data.api.models.payment_method.UpdatePaymentStatusRequestResponse;
import com.aecb.data.api.models.purchasehistory.PurchaseHistoryResponse;
import com.aecb.data.api.models.registeruser.RegisterUserRequest;
import com.aecb.data.api.models.registeruser.RegisterUserResponse;
import com.aecb.data.api.models.request.updateuserprofile.UpdateUserProfileRequest;
import com.aecb.data.api.models.response.getuserdetail.GetProfileResponse;
import com.aecb.data.api.models.response.login.LoginResponse;
import com.aecb.data.api.models.savedeviceinfo.SaveDeviceInfoRequest;
import com.aecb.data.api.models.securityquestions.SecurityQuestionsResponse;
import com.aecb.data.api.models.securityquestions.SecurityQuestionsSubmitRequest;
import com.aecb.data.api.models.termsconditions.TermsConditionData;
import com.aecb.data.api.models.verifyotp.OtpVerifyRequest;
import com.aecb.data.api.models.verifyotp.VerifyOtpResponse;
import com.aecb.data.db.DbHelper;
import com.aecb.data.db.repository.commonresponse.DbResponse;
import com.aecb.data.db.repository.notification.DbNotification;
import com.aecb.data.db.repository.userInformation.DbUserItem;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.data.preference.PrefHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.ResponseBody;

public final class DataManagerImpl implements DataManager {

    private PrefHelper mPreferenceHelper;
    private ApiHelper mApiHelper;
    private DbHelper mDBHelper;

    @Inject
    public DataManagerImpl(PrefHelper mPreferenceHelper, ApiHelper mApiHelper, DbHelper mDBHelper) {
        this.mPreferenceHelper = mPreferenceHelper;
        this.mApiHelper = mApiHelper;
        this.mDBHelper = mDBHelper;
    }

    public boolean isUserLoggedIn() {
        return this.mPreferenceHelper.isLoggedIn();
    }

    @Override
    public Observable<Long> insertUser(DbUserItem dbUserItem) {
        return mDBHelper.insertUser(dbUserItem);
    }

    @Override
    public Observable<DbUserItem> getUserDetailFromMobile(String mobile) {
        return mDBHelper.getUserDetailFromMobile(mobile);
    }

    @Override
    public Observable<Long> updateDeviceId(String deviceId, String mobile) {
        return mDBHelper.updateDeviceId(deviceId, mobile);
    }

    @Override
    public Observable<Long> updateUser(DbUserItem dbUserItem) {
        return mDBHelper.updateUser(dbUserItem);
    }

    @Override
    public Observable<Long> updatePassword(String password, String mobile) {
        return mDBHelper.updatePassword(password, mobile);
    }

    @Override
    public Observable<Long> updateEmail(String email, String mobile) {
        return mDBHelper.updateEmail(email, mobile);
    }

    @Override
    public Observable<List<DbUserItem>> getAllUsers() {
        return mDBHelper.getAllUsers();
    }

    @Override
    public void clearAll() {
        mPreferenceHelper.clearAll();
    }

    @Override
    public boolean isFirstTime() {
        return this.mPreferenceHelper.isFirstTime();
    }

    @Override
    public void isFirstTime(boolean isFirstTime) {
        this.mPreferenceHelper.isFirstTime(isFirstTime);
    }

    @Override
    public String getCurrentLanguage() {
        return mPreferenceHelper.getCurrentLanguage();
    }

    @Override
    public void setCurrentLanguage(String currentLanguage) {
        mPreferenceHelper.setCurrentLanguage(currentLanguage);
    }

    @Override
    public String getSessionToken() {
        return mPreferenceHelper.getSessionToken();
    }

    @Override
    public void setSessionToken(String sessionToken) {
        mPreferenceHelper.setSessionToken(sessionToken);
    }

    @Override
    public Observable<ConfigurationResponse> getAppConfigurations() {
        return mApiHelper.getAppConfigurations();
    }

    @Override
    public Long getOtpTime() {
        return mPreferenceHelper.getOtpTime();
    }

    @Override
    public void setOtpTime(Long otpTime) {
        mPreferenceHelper.setOtpTime(otpTime);
    }

    @Override
    public int getTcVersionNumber() {
        return mPreferenceHelper.getTcVersionNumber();
    }

    @Override
    public void setTcVersionNumber(int tcVersionNumber) {
        mPreferenceHelper.setTcVersionNumber(tcVersionNumber);
    }

    @Override
    public Observable<NationalityResponse> getAllNationalities() {
        return mApiHelper.getAllNationalities();
    }

    @Override
    public String getFcmId() {
        return mPreferenceHelper.getFcmId();
    }

    @Override
    public void setFcmId(String fcmId) {
        mPreferenceHelper.setFcmId(fcmId);
    }

    @Override
    public String getPreviousDeviceID() {
        return mPreferenceHelper.getPreviousRandomDeviceID();
    }

    @Override
    public void setPreviousDeviceID(String deviceID) {
        mPreferenceHelper.setPreviousRandomDeviceID(deviceID);
    }

    @Override
    public ConfigurationData getConfigurationData() {
        return mPreferenceHelper.getConfigurationData();
    }

    @Override
    public void setConfigurationData(String configurationData) {
        mPreferenceHelper.setConfigurationData(configurationData);
    }

    @Override
    public Observable<GenerateOtpResponse> generateOtp(GenerateOtpRequest generateOtpRequest) {
        return mApiHelper.generateOtp(generateOtpRequest);
    }

    @Override
    public Observable<VerifyOtpResponse> verifyOtp(OtpVerifyRequest otpVerifyRequest) {
        return mApiHelper.verifyOtp(otpVerifyRequest);
    }

    @Override
    public Observable<BaseResponse> updateBiometric(UpdateBiometricRequest updateBiometricRequest) {
        return mApiHelper.updateBiometric(updateBiometricRequest);
    }

    @Override
    public Observable<SecurityQuestionsResponse> submitSecurityQuestions(
            SecurityQuestionsSubmitRequest securityQuestionsSubmitRequest) {
        return mApiHelper.submitSecurityQuestions(securityQuestionsSubmitRequest);
    }

    @Override
    public Observable<RegisterUserResponse> registerNewUser(RegisterUserRequest registerUserRequest) {
        return mApiHelper.registerNewUser(registerUserRequest);
    }

    @Override
    public Observable<BaseResponse> saveDeviceInfo(SaveDeviceInfoRequest saveDeviceInfoRequest) {
        return mApiHelper.saveDeviceInfo(saveDeviceInfoRequest);
    }

    @Override
    public Observable<ChangePasswordResponse> changePassword(ChangePasswordRequest changePasswordRequest) {
        return mApiHelper.changePassword(changePasswordRequest);
    }

    @Override
    public Observable<List<TermsConditionData>> getTermsConditions() {
        return mApiHelper.getTermsConditions();
    }

    @Override
    public boolean getTouchLoggedIn() {
        return mPreferenceHelper.getTouchLoggedIn();
    }

    public void setTouchLoggedIn(boolean touchLoggedIn) {
        this.mPreferenceHelper.setTouchLoggedIn(touchLoggedIn);
    }

    @Override
    public Observable<LoginResponse> login(LoginRequest loginRequest) {
        return mApiHelper.login(loginRequest);
    }

    @Override
    public Observable<BaseResponse> logout() {
        return mApiHelper.logout();
    }


    @Override
    public void setUserSession(String userSession) {
        this.mPreferenceHelper.setUserSession(userSession);
    }

    @Override
    public Observable<Long> insertUserWithTC(DBUserTC dbUserTC) {
        return mDBHelper.insertUserWithTC(dbUserTC);
    }

    @Override
    public Observable<DBUserTC> getUserWithTC(String userName) {
        return mDBHelper.getUserWithTC(userName);
    }

    @Override
    public Observable<Long> updateUserTC(DBUserTC dbUserTC) {
        return mDBHelper.updateUserTC(dbUserTC);
    }

    @Override
    public Observable<Long> updateUserDeviceId(String userName, String deviceId) {
        return mDBHelper.updateUserDeviceId(userName, deviceId);
    }

    @Override
    public Observable<Long> updateUserPassword(String userName, String password) {
        return mDBHelper.updateUserPassword(userName, password);
    }

    @Override
    public Observable<BaseResponse> updateUserProfile(UpdateUserProfileRequest updateUserProfileRequest) {
        return mApiHelper.updateUserProfileRequest(updateUserProfileRequest);
    }

    @Override
    public Observable<GetProfileResponse> getUserDetail() {
        return mApiHelper.getUserDetail();
    }

    @Override
    public Observable<Long> updateTouchId(boolean touchId, String email) {
        return mDBHelper.updateTouchId(touchId, email);
    }

    @Override
    public Observable<Integer> updateLastLoginUser(DBUserTC dbUserTC) {
        return mDBHelper.updateLastLoginUser(dbUserTC);
    }

    @Override
    public Observable<DBUserTC> getLastLoginUser() {
        return mDBHelper.getLastLoginUser();
    }

    @Override
    public Observable<GetProductResponse> getProducts() {
        return mApiHelper.getProducts();
    }

    @Override
    public String getProductList() {
        return mPreferenceHelper.getProductList();
    }

    @Override
    public void setProductList(String productList) {
        mPreferenceHelper.setProductList(productList);
    }

    @Override
    public SharedPreferences getPrefReference() {
        return mPreferenceHelper.prefReference();
    }

    @Override
    public Observable<AddCardResponse> addCardAndMakePayment(String encryptedRequest) {
        return mApiHelper.addCardAndMakePayment(encryptedRequest);
    }

    @Override
    public Observable<ResponseBody> getCreditCards() {
        return mApiHelper.getCreditCards();
    }

    @Override
    public Observable<PurchaseHistoryResponse> getPurchaseHistory() {
        return mApiHelper.getPurchaseHistory();
    }

    @Override
    public void setPurchaseHistory(String purchaseHistory) {
        mPreferenceHelper.setPurchaseHistory(purchaseHistory);
    }

    @Override
    public Observable<BuyCreditReportResponse> buyCreditReport(BuyCreditReportRequest buyCreditReportRequest) {
        return mApiHelper.buyCreditReport(buyCreditReportRequest);
    }

    @Override
    public Observable<UpdatePaymentStatusRequestResponse> updatePaymentStatus(UpdatePaymentStatusRequestResponse item) {
        return mApiHelper.updatePaymentStatus(item);
    }

    @Override
    public Observable<BaseResponse> callSendAdditionalEmailsApi(EmailRecipientsRequest recipientsRequest) {
        return mApiHelper.callSendAdditionalEmailsApi(recipientsRequest);
    }

    @Override
    public Observable<GetFileResponse> getFile(String reportId, int i) {
        return mApiHelper.getFile(reportId, i);
    }

    @Override
    public Observable<Long> updateLasUserLogin(String userName) {
        return mDBHelper.updateLasUserLogin(userName);
    }

    @Override
    public Observable<Long> updateLasUserLoginFalse(String userName) {
        return mDBHelper.updateLasUserLoginFalse(userName);
    }

    @Override
    public void removeSessionToken() {
        mPreferenceHelper.removeSessionToken();
    }

    @Override
    public Observable<Long> insertNotification(DbNotification dbNotification) {
        return mDBHelper.insertNotification(dbNotification);
    }

    @Override
    public LiveData<List<DbNotification>> getAllNotification() {
        return mDBHelper.getAllNotification();
    }

    @Override
    public Completable deleteNotification(int id) {
        return mDBHelper.deleteNotification(id);
    }

    @Override
    public Observable<Long> readNotification(int id) {
        return mDBHelper.readNotification(id);
    }

    @Override
    public Completable deleteAllNotification() {
        return mDBHelper.deleteAllNotification();
    }

    @Override
    public Observable<BaseResponse> submitContactUs(ContactUsSubmitRequest contactUsSubmitRequest) {
        return mApiHelper.submitContactUs(contactUsSubmitRequest);
    }

    @Override
    public Observable<GetBankListReponse> getBankList() {
        return mApiHelper.getBankList();
    }

    @Override
    public Observable<AddCardResponse> manageCard(String encryptedRequest) {
        return mApiHelper.manageCard(encryptedRequest);
    }

    @Override
    public Observable<Long> updateResponseIntoDb(DbResponse dbResponse) {
        return mDBHelper.updateResponseIntoDb(dbResponse);
    }

    @Override
    public Observable<BaseResponse> submitContactUsRegister(ContactUsSubmitRequest contactUsSubmitRequest) {
        return mApiHelper.submitContactUsRegister(contactUsSubmitRequest);
    }

    @Override
    public String getInstallSource() {
        return mPreferenceHelper.getInstallSource();
    }

    @Override
    public void setInstallSource(String installSource) {
        mPreferenceHelper.setInstallSource(installSource);
    }

    @Override
    public String getInstalledCampaignId() {
        return mPreferenceHelper.getInstalledCampaignId();
    }

    @Override
    public void setInstalledCampaignId(String installedCampaignId) {
        mPreferenceHelper.setInstalledCampaignId(installedCampaignId);
    }

    @Override
    public boolean isNormalInstall() {
        return mPreferenceHelper.isNormalInstall();
    }

    @Override
    public void setNormalInstall(boolean normalInstall) {
        mPreferenceHelper.setNormalInstall(normalInstall);
    }

    @Override
    public Observable<PurchaseHistoryResponse> callPurchaseCheckout(PurchaseCheckoutRequest purchaseCheckoutRequest) {
        return mApiHelper.callPurchaseCheckout(purchaseCheckoutRequest);
    }

    @Override
    public Observable<Long> updatePreferredPaymentMethod(String userName, String preferredPaymentMethod) {
        return mDBHelper.updatePreferredPaymentMethod(userName, preferredPaymentMethod);
    }

    @Override
    public Observable<MakePayment6Response> addADCBCardAndMakePayment(String encryptedRequest) {
        return mApiHelper.addADCBCardAndMakePayment(encryptedRequest);
    }

    @Override
    public Observable<Long> updateLanguage(String userName, String language) {
        return mDBHelper.updateLanguage(userName, language);
    }

    @Override
    public Observable<Long> updateUserDOB(String userName, String dob) {
        return mDBHelper.updateUserDOB(userName, dob);
    }

    @Override
    public Observable<Long> updateUserPassport(String userName, String passport) {
        return mDBHelper.updateUserPassport(userName, passport);
    }

    @Override
    public Observable<Long> updateIsUAEPassUser(String userName, int isUAEPassUser) {
        return mDBHelper.updateIsUAEPassUser(userName, isUAEPassUser);
    }
}