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
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static com.aecb.BuildConfig.MAKE_PAYMENT_URL;

public interface MyAppService {

    @Headers("refId:true")
    @GET("configurations/getconfigurations1")
    Observable<ConfigurationResponse> getAppConfigurations();

    @Headers("refId:true")
    @GET("standard/getnationalities1")
    Observable<NationalityResponse> getAllNationalities();

    @GET("standard/terms1")
    Observable<List<TermsConditionData>> getTermsConditions();

    @POST("mobile/generateotp1")
    Observable<GenerateOtpResponse> generateOtp(@Body GenerateOtpRequest generateOtpRequest);

    @POST("mobile/verifyotp1")
    Observable<VerifyOtpResponse> verifyOtp(@Body OtpVerifyRequest otpVerifyRequest);

    @POST("12367076-f42e-4f18-a234-a3685bf1ee76/standard/updatebiometric")
    Observable<BaseResponse> updateBiometric(@Body UpdateBiometricRequest updateBiometricRequest);

    @POST("standard/submitsecurityquestions1")
    Observable<SecurityQuestionsResponse> submitSecurityQuestions(
            @Body SecurityQuestionsSubmitRequest securityQuestionsSubmitRequest);

    @POST("standard/registernewuser1")
    Observable<RegisterUserResponse> registerNewUser(@Body RegisterUserRequest registerUserRequest);

    @POST("save/deviceinfo1")
    Observable<BaseResponse> saveDeviceInfo(@Body SaveDeviceInfoRequest saveDeviceInfoRequest);

    @POST("standard/changepassword1")
    Observable<ChangePasswordResponse> changePassword(@Body ChangePasswordRequest changePasswordRequest);

    @Headers("refId:true")
    @POST("standard/login1")
    Observable<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("standard/logout1")
    Observable<BaseResponse> logout();

    @POST("standard/updateuserprofile1")
    Observable<BaseResponse> updateUserProfile(@Body UpdateUserProfileRequest updateUserProfileRequest);

    @GET("standard/getuserprofile1")
    Observable<GetProfileResponse> getUserDetail();

    @GET("configurations/getproducts1")
    Observable<GetProductResponse> getProducts();

    @POST(MAKE_PAYMENT_URL)
    Observable<AddCardResponse> addCardAndMakePayment(@Body String encryptedRequest);

    @POST("payments/makepayment6")
    Observable<MakePayment6Response> addADCBCardAndMakePayment(@Body String encryptedRequest);

    @GET("payments/getcards6")
    Observable<ResponseBody> getCreditCards();

    @POST("standard/registernewuser1")
    Observable<BuyCreditReportResponse> buyCreditReport(@Body RegisterUserRequest registerUserRequest);

    @GET("transactions/getpurchasehistory1")
    Observable<PurchaseHistoryResponse> getPurchaseHistory();

    @POST("transactions/buycreditreport1")
    Observable<BuyCreditReportResponse> buyCreditReport(@Body BuyCreditReportRequest registerUserRequest);

    @POST("standard/updatepaymentstatus4")
    Observable<UpdatePaymentStatusRequestResponse> updatePaymentStatus(@Body UpdatePaymentStatusRequestResponse item);

    @POST("transactions/sendaddtionalemails")
    Observable<BaseResponse> callSendAdditionalEmailsApi(@Body EmailRecipientsRequest recipientsRequest);

    @GET("transactions/getfile1?")
    Observable<GetFileResponse> getFile(@Query("applicationId") String reportId, @Query("channel") int i);

    @POST("standard/createcasereq1")
    Observable<BaseResponse> submitContactUs(@Body ContactUsSubmitRequest contactUsSubmitRequest);

    @POST("payments/managecards6")
    Observable<AddCardResponse> manageCard(@Body String encryptedRequest);

    @GET("configurations/getbankslist1")
    Observable<GetBankListReponse> getBankList();

    @Headers("refId:true")
    @POST("standard/createcasereq1")
    Observable<BaseResponse> submitContactUsRegister(@Body ContactUsSubmitRequest contactUsSubmitRequest);

    @POST("checkout/purchase")
    Observable<PurchaseHistoryResponse> callPurchaseCheckout(@Body PurchaseCheckoutRequest purchaseCheckoutRequest);
}