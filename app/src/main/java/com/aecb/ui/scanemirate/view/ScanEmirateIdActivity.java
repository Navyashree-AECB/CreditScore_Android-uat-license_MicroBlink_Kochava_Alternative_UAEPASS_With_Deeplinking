package com.aecb.ui.scanemirate.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.aecb.AppConstants;
import com.aecb.BuildConfig;
import com.aecb.R;
import com.aecb.base.mvp.BaseActivity;
import com.aecb.base.mvp.BlurDialogBaseFragment;
import com.aecb.data.api.models.login.UaePassRequest;
import com.aecb.data.api.models.request.updateuserprofile.UpdateUserProfileRequest;
import com.aecb.data.api.models.response.getuserdetail.GetUserDetailResponse;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.databinding.ActivityScanEmirateIdBinding;
import com.aecb.listeners.OnDateSetListener;
import com.aecb.microblink.result.extract.BaseResultExtractorFactory;
import com.aecb.microblink.result.extract.ResultExtractorFactoryProvider;
import com.aecb.microblink.result.extract.blinkid.BlinkIdResultExtractorFactory;
import com.aecb.microblink.util.ImageSettings;
import com.aecb.presentation.customviews.ToolbarView;
import com.aecb.ui.addpassportdob.view.AddDobPassportFragment;
import com.aecb.ui.dashboard.view.DashboardActivity;
import com.aecb.ui.loginflow.uaepasspin.uaepass.UaePassAuthorizationModel;
import com.aecb.ui.loginflow.uaepasspin.uaepass.UaePassCallback;
import com.aecb.ui.loginflow.uaepasspin.uaepass.UaePassUserModel;
import com.aecb.ui.loginflow.uaepasspin.uaepass.UaePassUtil;
import com.aecb.ui.loginflow.uaepasspin.view.UAEPassPinActivity;
import com.aecb.ui.registerflow.personaldetails.view.PersonalDetailsActivity;
import com.aecb.ui.scanemirate.CustomVerificationFlowActivity;
import com.aecb.ui.scanemirate.presenter.ScanEmirateIdContract;
import com.aecb.ui.termsandconditions.view.TermsAndConditionsFragment;
import com.aecb.util.FirebaseLogging;
import com.aecb.util.Utilities;
import com.aecb.util.ValidationUtil;
import com.google.gson.Gson;
import com.microblink.MicroblinkSDK;
import com.microblink.entities.recognizers.RecognizerBundle;
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer;
import com.microblink.hardware.camera.CameraType;
import com.microblink.intent.IntentDataTransferMode;
import com.microblink.util.RecognizerCompatibility;
import com.microblink.util.RecognizerCompatibilityStatus;

import javax.inject.Inject;

import timber.log.Timber;

import static com.aecb.AppConstants.ActivityIntentCode.UAE_PASS_REQUEST_CODE;
import static com.aecb.AppConstants.IntentKey.AUTHENTICATION_CODE;
import static com.aecb.AppConstants.IntentKey.BUNDLE_DB_USER;
import static com.aecb.AppConstants.IntentKey.BUNDLE_USER_DETAILS;
import static com.aecb.AppConstants.IntentKey.FROM_ACTIVITY;
import static com.aecb.AppConstants.IntentKey.FROM_ADD_RECIPIENTS;
import static com.aecb.AppConstants.IntentKey.OPEN_TC_FOR;
import static com.aecb.AppConstants.IntentKey.SCANNED;
import static com.aecb.AppConstants.IntentKey.TC_FOR_DOB;
import static com.aecb.AppConstants.IntentKey.TC_VERSION;
import static com.aecb.AppConstants.IntentKey.UAE_PASS_REQUEST;
import static com.aecb.AppConstants.IntentKey.USER_NAME;
import static com.aecb.ui.loginflow.uaepasspin.uaepass.UaePassUtil.REDIRECT_URL;
import static com.aecb.ui.loginflow.uaepasspin.uaepass.UaePassUtil.UAEPASS_AUTH_CALLBACK;
import static com.aecb.ui.loginflow.uaepasspin.uaepass.UaePassUtil.UAEPASS_PROFILE_CALLBACK;
import static com.aecb.util.varibles.StringConstants.DOB_PICKER;

public class ScanEmirateIdActivity extends BaseActivity<ScanEmirateIdContract.view,
        ScanEmirateIdContract.Presenter> implements ScanEmirateIdContract.view, View.OnClickListener,
        UaePassCallback, OnDateSetListener {

    public static final int MY_BLINKID_REQUEST_CODE = 0x101;
    private static boolean isRecognitionSupported = true;


    @Inject
    public ScanEmirateIdContract.Presenter mPresenter;
    ActivityScanEmirateIdBinding scanEmirateIdBinding;
    private RecognizerBundle mRecognizerBundle;
    UaePassRequest uaePassRequest;
    private BlurDialogBaseFragment addDobPassportFragment;
    private TermsAndConditionsFragment termsConditionFragment;

    public static boolean isRecognitionSupported() {
        return isRecognitionSupported;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBinding();
    }

    private void enableBinding() {
       /* if (!BuildConfig.BUILD_TYPE.equalsIgnoreCase("dev")){
            try {
                initMicroBlink();
            }catch(Exception e){
                e.printStackTrace();

            }
        }*/
        scanEmirateIdBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        scanEmirateIdBinding.btnContinue.setOnClickListener(this);
        scanEmirateIdBinding.tvSkip.setOnClickListener(this);
        scanEmirateIdBinding.btnLoginWithUAEPass.setOnClickListener(this);
        uaePassRequest = new UaePassRequest();
        if (mPresenter.getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ENG)) {
            scanEmirateIdBinding.ivLoginUAEEN.setVisibility(View.VISIBLE);
            scanEmirateIdBinding.ivLoginUAEAR.setVisibility(View.GONE);
        } else {
            scanEmirateIdBinding.ivLoginUAEEN.setVisibility(View.VISIBLE);
            scanEmirateIdBinding.ivLoginUAEAR.setVisibility(View.GONE);
        }

    }

    @NonNull
    @Override
    public ScanEmirateIdContract.Presenter createPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_scan_emirate_id;
    }

    @Override
    public void initToolbar() {
        ToolbarView tb = findViewById(R.id.toolBar);
        tb.setCancelBtnListener(this::onBackPressed);
        tb.setBtnVisibility(false, true);
        tb.setToolbarTitle(R.string.scan_your_emirate_id);
    }

    protected String getLicenceFilePath() {
        String license = "";
        MicroblinkSDK.setShowTrialLicenseWarning(false);
        switch (BuildConfig.BUILD_TYPE) {
            case AppConstants.ApkBuildType.UAT:
                license = "MB_com.aecb.uat_BlinkID-Android-2023-04-21.key";
                break;
            case AppConstants.ApkBuildType.DEV: {
                license = "MB_com.aecb.dev_BlinkID_Android_2020-12-26.mblic";
                break;
            }
            case AppConstants.ApkBuildType.PROD:
                license = "MB_com.aecb.app_BlinkID_Android_2021-04-09.mblic";
            default:
        }
        return license;
    }

    @SuppressLint("TimberArgCount")
    private void initMicroBlink() {
        RecognizerCompatibilityStatus supportStatus = RecognizerCompatibility.getRecognizerCompatibilityStatus(this);
        if (supportStatus == RecognizerCompatibilityStatus.RECOGNIZER_SUPPORTED) {
            Timber.i("Recognition is supported!");
        } else if (supportStatus == RecognizerCompatibilityStatus.NO_CAMERA) {
            Toast.makeText(this, getString(R.string.recognition_not_supported), Toast.LENGTH_SHORT).show();
            Timber.w("Recognition is supported only in DirectAPI mode!");
        } else {
            isRecognitionSupported = false;
            Toast.makeText(this, getString(R.string.recognition_not_supported_reason) + supportStatus.name(), Toast.LENGTH_LONG).show();
            Timber.e("Recognition is not supported! Reason: {}", supportStatus.name());
        }

        if (isRecognitionSupported) {
            MicroblinkSDK.setLicenseFile(getLicenceFilePath(), this);
            MicroblinkSDK.setIntentDataTransferMode(IntentDataTransferMode.PERSISTED_OPTIMISED);
        }

        ResultExtractorFactoryProvider.set(createResultExtractorFactory());
    }

    protected BaseResultExtractorFactory createResultExtractorFactory() {
        return new BlinkIdResultExtractorFactory();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnContinue:
                if (!BuildConfig.BUILD_TYPE.equalsIgnoreCase("dev")){
                    try {
                        initMicroBlink();
                    }catch(Exception e){
                        showErrorDialog("Technical error... Please try again","");
                        e.printStackTrace();
                        return;
                    }
                }
                FirebaseLogging.isScanned = true;
                Intent intent = new Intent(this, CustomVerificationFlowActivity.class);
                mRecognizerBundle = new RecognizerBundle(ImageSettings.enableAllImages(new BlinkIdCombinedRecognizer()));
                mRecognizerBundle.saveToIntent(intent);
                intent.putExtra(FROM_ACTIVITY, FROM_ADD_RECIPIENTS);
                intent.putExtra(CustomVerificationFlowActivity.EXTRAS_BEEP_RESOURCE, R.raw.beep);
                intent.putExtra(CustomVerificationFlowActivity.EXTRAS_COMBINED_CAMERA_TYPE, (Parcelable) CameraType.CAMERA_BACKFACE);
                startActivityForResult(intent, MY_BLINKID_REQUEST_CODE);
                break;
            case R.id.tvSkip:
                FirebaseLogging.isScanned = false;
                Intent i = new Intent(this, PersonalDetailsActivity.class);
                i.putExtra(SCANNED, false);
                startActivity(i);
                break;
            case R.id.btnLoginWithUAEPass:
                Intent uaePassintent = new Intent(ScanEmirateIdActivity.this, UAEPassPinActivity.class);
                startActivityForResult(uaePassintent, UAE_PASS_REQUEST_CODE);
                break;
        }
    }

    private void callAccessTokenUaePass(String code) {
        showLoading(null);
        UaePassUtil.callUAEPassAccessToken(this, code, ScanEmirateIdActivity.this);
    }

    private void callUaePassProfile(String accessToken) {
        UaePassUtil.callUserProfile(this, accessToken, ScanEmirateIdActivity.this);
    }

    @Override
    public void uaePassResponse(int type, String response) {
        if (type == UAEPASS_AUTH_CALLBACK) {
            UaePassAuthorizationModel response1 = new Gson().fromJson(response, UaePassAuthorizationModel.class);
            if (response1 != null && response1.getAccess_token() != null) {
                callUaePassProfile(response1.getAccess_token());
            } else {
                hideLoading();
                uaePassError(UAEPASS_AUTH_CALLBACK);
            }
        }

        if (type == UAEPASS_PROFILE_CALLBACK) {
            UaePassUserModel userInfo = new Gson().fromJson(response, UaePassUserModel.class);
            Log.e("user info", response);
            Timber.e("Profile Model" + userInfo.toString());

            if (userInfo != null) {
                if (userInfo.getUserType().equalsIgnoreCase("SOP1")) {
                    hideLoading();
                    displaySOP1UserMessage();
                } else {
                    mPresenter.getLastUser(userInfo.getEmail(), userInfo);
                }
            } else {
                hideLoading();
                uaePassError(UAEPASS_PROFILE_CALLBACK);
            }
        }
    }

    @Override
    public void uaePassError(int type) {
        hideLoading();
        localValidationError(getString(R.string.error), getString(R.string.user_cancelled_authentication));
    }

    private void displaySOP1UserMessage() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_sop1_user_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_main_bg);
        dialog.setCancelable(false);
        TextView tvOK = dialog.findViewById(R.id.tvOk);
        TextView tvDescOne = dialog.findViewById(R.id.tvDescOne);
        TextView tvDescTwo = dialog.findViewById(R.id.tvDescTwo);
        tvOK.setOnClickListener(v -> {
            dialog.dismiss();
        });
        tvDescOne.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://selfcare.uaepass.ae/locations"));
            startActivity(browserIntent);
        });
        tvDescTwo.setOnClickListener(v -> {
            Utilities.makeCall(this, "800287328");
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void setLastUser(DBUserTC dbUserTC, UaePassUserModel profileModel) {
        if (profileModel.getDob() == null || profileModel.getPassportNumber() == null) {
            if (dbUserTC != null) {
                if (!ValidationUtil.isNullOrEmpty(dbUserTC.getDob())
                        && dbUserTC.getIsUAEPassUser() == 1) {
                    mPresenter.loginWithUAEPass(dbUserTC, "", getUaePassRequest(profileModel));
                } else {
                    getUaePassRequest(profileModel);
                    openAddDobPassportScreen();
                }
            } else {
                getUaePassRequest(profileModel);
                openAddDobPassportScreen();
            }
        } else {
            mPresenter.loginWithUAEPass(dbUserTC, "", getUaePassRequest(profileModel));
        }
    }

    public void openAddDobPassportScreen() {
        hideLoading();
        Bundle bundle = new Bundle();
        bundle.putSerializable(UAE_PASS_REQUEST, uaePassRequest);
        addDobPassportFragment = AddDobPassportFragment.newInstance(bundle);
        addDobPassportFragment.show(getSupportFragmentManager(), addDobPassportFragment.getTag());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (view.getTag().toString().equals(DOB_PICKER)) {
            if (addDobPassportFragment != null && addDobPassportFragment instanceof AddDobPassportFragment)
                ((AddDobPassportFragment) addDobPassportFragment).onDateSet(view, year, month, day);
        }
    }

    public void updateUserDetailForUaePass(UpdateUserProfileRequest updateUserProfileRequest, String tcFor,
                                           DBUserTC dbUserTC, GetUserDetailResponse getUserDetailResponse) {
        if (addDobPassportFragment != null) {
            ((AddDobPassportFragment) addDobPassportFragment).updateUserDetailForUaePass(updateUserProfileRequest, tcFor, dbUserTC, getUserDetailResponse);
        }
    }

    private UaePassRequest getUaePassRequest(UaePassUserModel profileModel) {
        uaePassRequest.setHomeAddressAreaCode(profileModel.getHomeAddressAreaCode());
        uaePassRequest.setSub(profileModel.getSub());
        uaePassRequest.setHomeAddressPOBox(profileModel.getHomeAddressPOBox());
        uaePassRequest.setCardHolderSignatureImage(profileModel.getPhoto());
        uaePassRequest.setPassportNumber(profileModel.getPassportNumber());
        uaePassRequest.setHomeAddressTypeCode(profileModel.getHomeAddressTypeCode());
        uaePassRequest.setHomeAddressAreaDescriptionAR(profileModel.getHomeAddressAreaDescriptionAR());
        uaePassRequest.setHomeAddressAreaDescriptionEN(profileModel.getHomeAddressAreaDescriptionEN());
        uaePassRequest.setUserType(profileModel.getUserType());
        uaePassRequest.setFullnameAR(profileModel.getFullnameAR());
        uaePassRequest.setFullnameEN(!ValidationUtil.isNullOrEmpty(profileModel.getFullnameEN()) ? profileModel.getFullnameEN().replaceAll(",", "") : profileModel.getFullnameEN());
        uaePassRequest.setHomeAddressAreaCode(profileModel.getHomeAddressAreaCode());
        uaePassRequest.setDomain(profileModel.getDomain());
        uaePassRequest.setGender(profileModel.getGender());
        uaePassRequest.setHomeAddressEmirateDescriptionAR(profileModel.getHomeAddressEmirateDescriptionAR());
        uaePassRequest.setLastnameEN(profileModel.getLastnameEN());
        uaePassRequest.setNationalityAR(profileModel.getNationalityAR());
        uaePassRequest.setFirstnameEN(profileModel.getFirstnameEN());
        uaePassRequest.setPhoto(profileModel.getPhoto());
        uaePassRequest.setHomeAddressEmirateDescriptionEN(profileModel.getHomeAddressEmirateDescriptionEN());
        uaePassRequest.setIdn(profileModel.getIdn());
        uaePassRequest.setHomeAddressCityCode(profileModel.getHomeAddressCityCode());
        uaePassRequest.setHomeAddressCityDescriptionEN(profileModel.getHomeAddressCityDescriptionEN());
        uaePassRequest.setEmail(profileModel.getEmail());
        uaePassRequest.setHomeAddressMobilePhoneNumber(profileModel.getHomeAddressMobilePhoneNumber());
        uaePassRequest.setDob(profileModel.getDob());
        uaePassRequest.setNationalityEN(profileModel.getNationalityEN());
        uaePassRequest.setFirstnameAR(profileModel.getFirstnameAR());
        uaePassRequest.setHomeAddressCityDescriptionAR(profileModel.getHomeAddressCityDescriptionAR());
        uaePassRequest.setLastnameAR(profileModel.getLastnameAR());
        uaePassRequest.setAcr(profileModel.getAcr());
        uaePassRequest.setMobile(profileModel.getMobile());
        uaePassRequest.setAmr(profileModel.getAmr());
        uaePassRequest.setUuid(profileModel.getUuid());
        uaePassRequest.setIdType(profileModel.getIdType());
        uaePassRequest.setSpuuid(profileModel.getSpuuid());
        uaePassRequest.setTitleEN(profileModel.getTitleEN());
        uaePassRequest.setTitleAR(profileModel.getTitleAR());
        return uaePassRequest;
    }

    @Override
    public void openNextPage() {
        if (termsConditionFragment != null) {
            termsConditionFragment.dismiss();
        }
        moveActivity(this, DashboardActivity.class, true, true);
    }

    @Override
    public void openTCPage(String tcFor, String userName, int tcVersion, GetUserDetailResponse data,
                           DBUserTC dbUserTC) {
        Bundle b = new Bundle();
        b.putString(OPEN_TC_FOR, tcFor);
        b.putString(USER_NAME, userName);
        b.putInt(TC_VERSION, tcVersion);
        b.putString(BUNDLE_DB_USER, new Gson().toJson(dbUserTC));
        b.putBoolean(TC_FOR_DOB, false);
        if (data != null)
            b.putString(BUNDLE_USER_DETAILS, new Gson().toJson(data));
        termsConditionFragment = TermsAndConditionsFragment.newInstance(b);
        termsConditionFragment.show(getSupportFragmentManager(), termsConditionFragment.getTag());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UAE_PASS_REQUEST_CODE && resultCode == RESULT_OK) {
            String code = data.getStringExtra(AUTHENTICATION_CODE);
            callAccessTokenUaePass(code);
        }
    }


}
