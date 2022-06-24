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

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public final class ApiHelperImpl implements ApiHelper {
    private MyAppService myAppService;

    @Inject
    public ApiHelperImpl(MyAppService myAppService) {
        this.myAppService = myAppService;
    }

    @Override
    public Observable<ConfigurationResponse> getAppConfigurations() {
        return myAppService.getAppConfigurations();
    }

    @Override
    public Observable<NationalityResponse> getAllNationalities() {
        return myAppService.getAllNationalities();
    }

    @Override
    public Observable<GenerateOtpResponse> generateOtp(GenerateOtpRequest generateOtpRequest) {
        return myAppService.generateOtp(generateOtpRequest);
    }

    @Override
    public Observable<VerifyOtpResponse> verifyOtp(OtpVerifyRequest otpVerifyRequest) {
        return myAppService.verifyOtp(otpVerifyRequest);
    }

    @Override
    public Observable<BaseResponse> updateBiometric(UpdateBiometricRequest updateBiometricRequest) {
        return myAppService.updateBiometric(updateBiometricRequest);
    }

    @Override
    public Observable<SecurityQuestionsResponse> submitSecurityQuestions(
            SecurityQuestionsSubmitRequest securityQuestionsSubmitRequest) {
        return myAppService.submitSecurityQuestions(securityQuestionsSubmitRequest);
    }

    @Override
    public Observable<RegisterUserResponse> registerNewUser(RegisterUserRequest registerUserRequest) {
        return myAppService.registerNewUser(registerUserRequest);
    }

    @Override
    public Observable<BaseResponse> saveDeviceInfo(SaveDeviceInfoRequest saveDeviceInfoRequest) {
        return myAppService.saveDeviceInfo(saveDeviceInfoRequest);
    }

    @Override
    public Observable<ChangePasswordResponse> changePassword(ChangePasswordRequest changePasswordRequest) {
        return myAppService.changePassword(changePasswordRequest);
    }

    @Override
    public Observable<List<TermsConditionData>> getTermsConditions() {
        return myAppService.getTermsConditions();
    }

    @Override
    public Observable<LoginResponse> login(LoginRequest loginRequest) {
        return myAppService.login(loginRequest);
    }

    @Override
    public Observable<BaseResponse> logout() {
        return myAppService.logout();
    }

    @Override
    public Observable<BaseResponse> updateUserProfileRequest(UpdateUserProfileRequest updateUserProfileRequest) {
        return myAppService.updateUserProfile(updateUserProfileRequest);
    }

    @Override
    public Observable<GetProfileResponse> getUserDetail() {
        return myAppService.getUserDetail();
    }

    @Override
    public Observable<GetProductResponse> getProducts() {
        return myAppService.getProducts();
    }

    @Override
    public Observable<AddCardResponse> addCardAndMakePayment(String encryptedRequest) {
        return myAppService.addCardAndMakePayment(encryptedRequest);
    }

    @Override
    public Observable<ResponseBody> getCreditCards() {
        return myAppService.getCreditCards();
    }

    @Override
    public Observable<BuyCreditReportResponse> buyCreditReport(RegisterUserRequest registerUserRequest) {
        return myAppService.buyCreditReport(registerUserRequest);
    }

    @Override
    public Observable<BaseResponse> submitContactUs(ContactUsSubmitRequest contactUsSubmitRequest) {
        return myAppService.submitContactUs(contactUsSubmitRequest);
    }

    @Override
    public Observable<GetBankListReponse> getBankList() {
        return myAppService.getBankList();
    }

    @Override
    public Observable<PurchaseHistoryResponse> getPurchaseHistory() {
        return myAppService.getPurchaseHistory();
    }

    @Override
    public Observable<BuyCreditReportResponse> buyCreditReport(BuyCreditReportRequest buyCreditReportRequest) {
        return myAppService.buyCreditReport(buyCreditReportRequest);
    }

    @Override
    public Observable<UpdatePaymentStatusRequestResponse> updatePaymentStatus(UpdatePaymentStatusRequestResponse item) {
        return myAppService.updatePaymentStatus(item);
    }

    @Override
    public Observable<BaseResponse> callSendAdditionalEmailsApi(EmailRecipientsRequest recipientsRequest) {
        return myAppService.callSendAdditionalEmailsApi(recipientsRequest);
    }

    @Override
    public Observable<GetFileResponse> getFile(String reportId, int i) {
        return myAppService.getFile(reportId, i);
    }

    @Override
    public Observable<AddCardResponse> manageCard(String encryptedRequest) {
        return myAppService.manageCard(encryptedRequest);
    }

    @Override
    public Observable<BaseResponse> submitContactUsRegister(ContactUsSubmitRequest contactUsSubmitRequest) {
        return myAppService.submitContactUsRegister(contactUsSubmitRequest);
    }

    @Override
    public Observable<PurchaseHistoryResponse> callPurchaseCheckout(PurchaseCheckoutRequest purchaseCheckoutRequest) {
        return myAppService.callPurchaseCheckout(purchaseCheckoutRequest);
    }

    @Override
    public Observable<MakePayment6Response> addADCBCardAndMakePayment(String encryptedRequest) {
        return myAppService.addADCBCardAndMakePayment(encryptedRequest);
    }
}