package com.aecb.data.api;

import com.aecb.data.api.models.BaseResponse;
import com.aecb.data.api.models.adcb.MakePayment6Response;
import com.aecb.data.api.models.banklist.GetBankListReponse;
import com.aecb.data.api.models.biometric.UpdateBiometricRequest;
import com.aecb.data.api.models.buycreditreport.BuyCreditReportRequest;
import com.aecb.data.api.models.buycreditreport.BuyCreditReportResponse;
import com.aecb.data.api.models.changepassword.ChangePasswordRequest;
import com.aecb.data.api.models.changepassword.ChangePasswordResponse;
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

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public interface ApiHelper {

    Observable<ConfigurationResponse> getAppConfigurations();

    Observable<NationalityResponse> getAllNationalities();

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

    Observable<BaseResponse> updateUserProfileRequest(UpdateUserProfileRequest updateUserProfileRequest);

    Observable<GetProfileResponse> getUserDetail();

    Observable<GetProductResponse> getProducts();

    Observable<AddCardResponse> addCardAndMakePayment(String encryptedRequest);

    Observable<ResponseBody> getCreditCards();

    Observable<BuyCreditReportResponse> buyCreditReport(BuyCreditReportRequest buyCreditReportRequest);

    Observable<UpdatePaymentStatusRequestResponse> updatePaymentStatus(UpdatePaymentStatusRequestResponse item);

    Observable<BaseResponse> callSendAdditionalEmailsApi(EmailRecipientsRequest recipientsRequest);

    Observable<PurchaseHistoryResponse> getPurchaseHistory();

    Observable<GetFileResponse> getFile(String reportId, int i);

    Observable<BuyCreditReportResponse> buyCreditReport(RegisterUserRequest registerUserRequest);

    Observable<BaseResponse> submitContactUs(ContactUsSubmitRequest contactUsSubmitRequest);

    Observable<GetBankListReponse> getBankList();

    Observable<AddCardResponse> manageCard(String encryptedRequest);

    Observable<BaseResponse> submitContactUsRegister(ContactUsSubmitRequest contactUsSubmitRequest);

    Observable<PurchaseHistoryResponse> callPurchaseCheckout(PurchaseCheckoutRequest purchaseCheckoutRequest);

    Observable<MakePayment6Response> addADCBCardAndMakePayment(String encryptedRequest);
}