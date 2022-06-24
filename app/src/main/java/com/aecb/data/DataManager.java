package com.aecb.data;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

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
import com.aecb.data.db.repository.commonresponse.DbResponse;
import com.aecb.data.db.repository.notification.DbNotification;
import com.aecb.data.db.repository.userInformation.DbUserItem;
import com.aecb.data.db.repository.usertcversion.DBUserTC;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.ResponseBody;

public interface DataManager {

    Observable<Long> insertUser(DbUserItem dbUserItem);

    Observable<DbUserItem> getUserDetailFromMobile(String mobileNumber);

    Observable<Long> updateDeviceId(String deviceId, String mobile);

    Observable<Long> updateUser(DbUserItem dbUserItem);

    Observable<Long> updatePassword(String password, String mobile);

    Observable<Long> updateEmail(String email, String mobile);

    Observable<List<DbUserItem>> getAllUsers();

    void clearAll();

    boolean isFirstTime();

    void isFirstTime(boolean isFirstTime);

    String getCurrentLanguage();

    void setCurrentLanguage(String currentLanguage);

    String getSessionToken();

    void setSessionToken(String sessionToken);

    Observable<ConfigurationResponse> getAppConfigurations();

    Long getOtpTime();

    void setOtpTime(Long otpTime);

    int getTcVersionNumber();

    void setTcVersionNumber(int tcVersionNumber);

    Observable<NationalityResponse> getAllNationalities();

    String getFcmId();

    void setFcmId(String fcmId);

    String getPreviousDeviceID();

    void setPreviousDeviceID(String deviceID);

    ConfigurationData getConfigurationData();

    void setConfigurationData(String configurationData);

    Observable<GenerateOtpResponse> generateOtp(GenerateOtpRequest generateOtpRequest);

    Observable<VerifyOtpResponse> verifyOtp(OtpVerifyRequest otpVerifyRequest);

    Observable<BaseResponse> updateBiometric(UpdateBiometricRequest updateBiometricRequest);

    Observable<SecurityQuestionsResponse> submitSecurityQuestions(SecurityQuestionsSubmitRequest securityQuestionsSubmitRequest);

    Observable<RegisterUserResponse> registerNewUser(RegisterUserRequest registerUserRequest);

    Observable<BaseResponse> saveDeviceInfo(SaveDeviceInfoRequest saveDeviceInfoRequest);

    Observable<ChangePasswordResponse> changePassword(ChangePasswordRequest changePasswordRequest);

    Observable<List<TermsConditionData>> getTermsConditions();

    Observable<LoginResponse> login(LoginRequest loginRequest);

    Observable<BaseResponse> logout();

    void setUserSession(String userSession);

    Observable<Long> insertUserWithTC(DBUserTC dbUserTC);

    Observable<DBUserTC> getUserWithTC(String userName);

    Observable<Long> updateUserTC(DBUserTC dbUserTC);

    Observable<Long> updateUserDeviceId(String userName, String deviceId);

    Observable<Long> updateUserPassword(String userName, String password);

    Observable<BaseResponse> updateUserProfile(UpdateUserProfileRequest updateUserProfileRequest);

    Observable<GetProfileResponse> getUserDetail();

    boolean getTouchLoggedIn();

    void setTouchLoggedIn(boolean touchLoggedIn);

    Observable<Long> updateTouchId(boolean touchId, String mobile);

    Observable<Integer> updateLastLoginUser(DBUserTC dbUserTC);

    Observable<DBUserTC> getLastLoginUser();

    Observable<GetProductResponse> getProducts();

    String getProductList();

    void setProductList(String productList);

    SharedPreferences getPrefReference();

    Observable<AddCardResponse> addCardAndMakePayment(String encryptedRequest);

    Observable<ResponseBody> getCreditCards();

    Observable<BuyCreditReportResponse> buyCreditReport(BuyCreditReportRequest buyCreditReportRequest);

    Observable<PurchaseHistoryResponse> getPurchaseHistory();

    void setPurchaseHistory(String purchaseHistory);

    Observable<UpdatePaymentStatusRequestResponse> updatePaymentStatus(UpdatePaymentStatusRequestResponse item);

    Observable<BaseResponse> callSendAdditionalEmailsApi(EmailRecipientsRequest recipientsRequest);

    Observable<GetFileResponse> getFile(String reportId, int i);

    Observable<Long> updateLasUserLogin(String userName);

    Observable<Long> updateLasUserLoginFalse(String userName);

    void removeSessionToken();

    Observable<Long> insertNotification(DbNotification dbNotification);

    LiveData<List<DbNotification>> getAllNotification();

    Completable deleteNotification(int id);

    Observable<Long> readNotification(int id);

    Completable deleteAllNotification();

    Observable<BaseResponse> submitContactUs(ContactUsSubmitRequest contactUsSubmitRequest);

    Observable<GetBankListReponse> getBankList();

    Observable<AddCardResponse> manageCard(String encryptedRequest);

    Observable<Long> updateResponseIntoDb(DbResponse dbResponse);

    Observable<BaseResponse> submitContactUsRegister(ContactUsSubmitRequest contactUsSubmitRequest);

    String getInstallSource();

    void setInstallSource(String installSource);

    String getInstalledCampaignId();

    void setInstalledCampaignId(String installedCampaignId);

    boolean isNormalInstall();

    void setNormalInstall(boolean normalInstall);

    Observable<PurchaseHistoryResponse> callPurchaseCheckout(PurchaseCheckoutRequest purchaseCheckoutRequest);

    Observable<Long> updatePreferredPaymentMethod(String userName, String preferredPaymentMethod);

    Observable<MakePayment6Response> addADCBCardAndMakePayment(String encryptedRequest);

    Observable<Long> updateLanguage(String userName, String language);

    Observable<Long> updateUserDOB(String userName, String dob);

    Observable<Long> updateUserPassport(String userName, String passport);

    Observable<Long> updateIsUAEPassUser(String userName, int isUAEPassUser);
}