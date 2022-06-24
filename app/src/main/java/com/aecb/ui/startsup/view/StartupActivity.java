
package com.aecb.ui.startsup.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.biometrics.BiometricPrompt;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
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
import com.aecb.databinding.ActivityStartupBinding;
import com.aecb.listeners.DialogButtonClickListener;
import com.aecb.listeners.OnDateSetListener;
import com.aecb.presentation.biometric.BiometricCallbackV28;
import com.aecb.presentation.biometric.BiometricUtils;
import com.aecb.ui.addpassportdob.view.AddDobPassportFragment;
import com.aecb.ui.dashboard.view.DashboardActivity;
import com.aecb.ui.devicerooted.view.DeviceRootedActivity;
import com.aecb.ui.loginflow.login.view.LoginActivity;
import com.aecb.ui.loginflow.touchid.view.TouchIdFragment;
import com.aecb.ui.loginflow.uaepasspin.uaepass.UaePassAuthorizationModel;
import com.aecb.ui.loginflow.uaepasspin.uaepass.UaePassCallback;
import com.aecb.ui.loginflow.uaepasspin.uaepass.UaePassUserModel;
import com.aecb.ui.loginflow.uaepasspin.uaepass.UaePassUtil;
import com.aecb.ui.maintanance.view.MaintenanceActivity;
import com.aecb.ui.scanemirate.view.ScanEmirateIdActivity;
import com.aecb.ui.startsup.presenter.StartupContract;
import com.aecb.ui.termsandconditions.view.TermsAndConditionsFragment;
import com.aecb.util.LocaleHelper;
import com.aecb.util.Utilities;
import com.aecb.util.ValidationUtil;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.gson.Gson;
import com.kochava.base.Tracker;
import com.microblink.util.RecognizerCompatibility;
import com.microblink.util.RecognizerCompatibilityStatus;
import com.scottyab.rootbeer.RootBeer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import timber.log.Timber;

import static com.aecb.AppConstants.DateFormats.STANDARD_DATE_FORMAT;
import static com.aecb.AppConstants.IntentKey.BUNDLE_DB_USER;
import static com.aecb.AppConstants.IntentKey.BUNDLE_USER_DETAILS;
import static com.aecb.AppConstants.IntentKey.LAST_LOGIN_USER;
import static com.aecb.AppConstants.IntentKey.OPEN_TC_FOR;
import static com.aecb.AppConstants.IntentKey.TC_FOR_DOB;
import static com.aecb.AppConstants.IntentKey.TC_VERSION;
import static com.aecb.AppConstants.IntentKey.UAE_PASS_REQUEST;
import static com.aecb.AppConstants.IntentKey.USER_NAME;
import static com.aecb.ui.loginflow.uaepasspin.uaepass.UaePassUtil.REDIRECT_URL;
import static com.aecb.ui.loginflow.uaepasspin.uaepass.UaePassUtil.UAEPASS_AUTH_CALLBACK;
import static com.aecb.ui.loginflow.uaepasspin.uaepass.UaePassUtil.UAEPASS_PROFILE_CALLBACK;
import static com.aecb.util.varibles.StringConstants.DOB_PICKER;

@RuntimePermissions
public class StartupActivity extends BaseActivity<StartupContract.View, StartupContract.Presenter>
        implements StartupContract.View, View.OnClickListener, OnDateSetListener, UaePassCallback {

    public static Context context;
    @Inject
    public StartupContract.Presenter mPresenter;
    boolean doubleBackToExitPressedOnce = false;
    String minAndroidAppVersion;
    int latestAndroidVersion = 0, currentVersion = 0;
    private ActivityStartupBinding startupBinding;
    private String drupalURL, tcEN, tcAR;
    private int tcVersion;
    private DBUserTC dbUser;
    private BlurDialogBaseFragment touchIdFragment;
    private BiometricCallbackV28 biometricCallbackV28;
    private TermsAndConditionsFragment termsConditionFragment;
    UaePassRequest uaePassRequest;
    private BlurDialogBaseFragment addDobPassportFragment;
    public String lang = "en";
    private String state = "";
    private boolean dialogCancelled = false;

    private String mSuccessURLUAEPass = "";
    private String mFailureURLUAEPass = "";
    private WebView webView;

    private androidx.appcompat.app.AlertDialog webViewAlertDialog;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        enableBinding();
        if (isNetworkConnected()) {
            RootBeer rootBeer = new RootBeer(this);
            if (rootBeer.isRootedWithoutBusyBoxCheck()) {
                moveActivity(this, DeviceRootedActivity.class, true, true);
            } else {
                mPresenter.getAppConfigurations();
            }
        } else {
            showInternetNotAvailableDialog();
        }
        checkIfBlinkIdNotSupported();
        receiveDynamicLinkData();
    }


    public void receiveDynamicLinkData() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    // Get deep link from result (may be null if no link is found)
                    Uri deepLink = null;
                    String referral;
                    String campId;
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.getLink();
                        if (deepLink != null) {
                            referral = deepLink.getQueryParameter("referral");
                            campId = deepLink.getQueryParameter("campId");
                            Timber.d("Referral =" + referral);
                            Timber.d("CampId =" + campId);
                            mPresenter.setInstalledCampaignId(campId);
                            mPresenter.setInstallSource(referral);
                        }
                    }
                })
                .addOnFailureListener(this, e -> Timber.w("getDynamicLink:onFailure", e));
    }

    private void checkIfBlinkIdNotSupported() {
        RecognizerCompatibilityStatus supportStatus = RecognizerCompatibility.getRecognizerCompatibilityStatus(this);
        if (supportStatus != RecognizerCompatibilityStatus.RECOGNIZER_SUPPORTED) {
            Toast.makeText(this, getString(R.string.recognition_not_supported_reason) + supportStatus.name(), Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void enableBinding() {
        context = this;
        startupBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        startupBinding.btnLogin.setOnClickListener(this);
        startupBinding.btnLoginWithUAEPass.setOnClickListener(this);
        startupBinding.ivTouchId.setOnClickListener(this);
        startupBinding.tvRegister.setOnClickListener(this);
        startupBinding.swChangeLanguage.setOnClickListener(this);
        startupBinding.btnLoginWithUAEPass.setOnClickListener(this);
        uaePassRequest = new UaePassRequest();
        setDefaultLanguage();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setDefaultLanguage() {
        if (mPresenter.getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ENG)) {
            startupBinding.tvEnglish.setVisibility(View.VISIBLE);
            startupBinding.tvArabic.setVisibility(View.GONE);
            startupBinding.ivLoginUAEEN.setVisibility(View.VISIBLE);
            startupBinding.ivLoginUAEAR.setVisibility(View.GONE);
            mPresenter.setCurrentLanguage(AppConstants.AppLanguage.ISO_CODE_ENG);
        } else {
            startupBinding.tvEnglish.setVisibility(View.GONE);
            startupBinding.tvArabic.setVisibility(View.VISIBLE);
            startupBinding.ivLoginUAEEN.setVisibility(View.VISIBLE);
            startupBinding.ivLoginUAEAR.setVisibility(View.GONE);
            mPresenter.setCurrentLanguage(AppConstants.AppLanguage.ISO_CODE_ARABIC);
        }
    }

    @NonNull
    @Override
    public StartupContract.Presenter createPresenter() {
        return this.mPresenter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_startup;
    }

    @Override
    public void initToolbar() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                moveActivity(this, LoginActivity.class, false, false);
                break;
            case R.id.btnLoginWithUAEPass:
               /* startupBinding.btnLoginWithUAEPass.setEnabled(false);
                startupBinding.btnLoginWithUAEPass.setClickable(false);*/
                state = "";
                state ="ShNP22hyl1jUU2RGjTRkpg=="; /*UaePassUtil.generateRandomString(24)*/;

                String url;
                if (UaePassUtil.appInstalledOrNot(this, UaePassUtil.UAE_PASS_PACKAGE_ID)) {
                    url = UaePassUtil.UAEPASS_AUTHENTICATION_URL + "?redirect_uri=" + REDIRECT_URL + "&client_id=" + UaePassUtil.UAE_PASS_CLIENT_ID + "&state=" + state + "&response_type=" + UaePassUtil.RESPONSE_TYPE + "&scope=" + UaePassUtil.SCOPE + "&acr_values=" + UaePassUtil.ACR_VALUES_MOBILE + "&ui_locales=" + lang;
                    Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
                } else {
                    url = UaePassUtil.UAEPASS_AUTHENTICATION_URL + "?redirect_uri=" + REDIRECT_URL + "&client_id=" + UaePassUtil.UAE_PASS_CLIENT_ID + "&state=" + state + "&response_type=" + UaePassUtil.RESPONSE_TYPE + "&scope=" + UaePassUtil.SCOPE + "&acr_values=" + UaePassUtil.ACR_VALUES_WEB + "&ui_locales=" + lang;
                    Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
                }
                login(url);
                break;
            case R.id.tvRegister:
                moveActivity(this, ScanEmirateIdActivity.class, false, false);
                break;
            case R.id.ivTouchId:
                if (dbUser != null) {
                    if (dbUser.isTouchId()) {
                        if (isNetworkConnected()) {
                            if (BiometricUtils.isBiometricPromptEnabled()) {
                                displayBiometricPrompt();
                            } else {
                                Bundle b = new Bundle();
                                b.putString(LAST_LOGIN_USER, new Gson().toJson(dbUser));
                                touchIdFragment = TouchIdFragment.newInstance(b);
                                touchIdFragment.show(getSupportFragmentManager(), touchIdFragment.getTag());
                            }
                        } else {
                            showInternetNotAvailableDialog();
                        }
                    }
                }
                break;
            case R.id.swChangeLanguage:
                if (mPresenter.getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ENG)) {
                    changeLanguage(true);
                } else {
                    changeLanguage(false);
                }

                break;
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void login(String url) {
        CookieManager.getInstance().removeAllCookie();

        webViewAlertDialog = new androidx.appcompat.app.AlertDialog.Builder(this).create();
        webViewAlertDialog.setTitle(null);

        RelativeLayout dialogView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.uaepass_login, null);
        webViewAlertDialog.setView(dialogView);
        webViewAlertDialog.setCancelable(true);
        webView = dialogView.findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(false);
        webView.clearCache(true);
        webView.clearHistory();
        webViewAlertDialog.setOnKeyListener((arg0, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialogCancelled = true;
                webViewAlertDialog.dismiss();
            }
            return true;
        });
        webViewAlertDialog.setOnDismissListener(dialog -> {
            webView.clearCache(true);
            webView.clearHistory();
            webView.loadUrl("about:blank");
            webView.onPause();
            webView.removeAllViews();
            webView.destroyDrawingCache();
            webView.pauseTimers();
            webView.destroy();
            webView = null;
            if (dialogCancelled) {
                dialogCancelled = false;
                localValidationError(getString(R.string.error), getString(R.string.user_cancelled_authentication));
            }
        });
        if (Build.VERSION.SDK_INT >= 26) {
            settings.setSafeBrowsingEnabled(false);
        }

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.contains("uaepass://digitalid")) {

                    if (!UaePassUtil.IS_PRODUCTION) {
                        url = url.replace("uaepass://", "uaepassqa://");
                    }

                    mSuccessURLUAEPass = UaePassUtil.getQueryParameterValue(url, "successurl");
                    mFailureURLUAEPass = UaePassUtil.getQueryParameterValue(url, "failureurl");

                    // success url
                    if (url.contains("successurl")) {
                        url = UaePassUtil.replaceUriParameter(Uri.parse(url), "successurl", REDIRECT_URL).toString();
                    }

                    // failure url
                    if (url.contains("failureurl")) {
                        url = UaePassUtil.replaceUriParameter(Uri.parse(url), "failureurl", REDIRECT_URL).toString();
                    }

                    if (url.contains("browserpackage")) {

                        url = UaePassUtil.replaceUriParameter(Uri.parse(url), "browserpackage", BuildConfig.APPLICATION_ID).toString();

                        url = url + "&closeondone=false";
                    }

                    Intent launchIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                    //launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    PackageManager packageManager = getPackageManager();
                    if (launchIntent.resolveActivity(packageManager) != null) {
                        startActivity(launchIntent);
                    } else {
                        UaePassUtil.openUAEPassAppInPlayStore(StartupActivity.this);
                        webViewAlertDialog.dismiss();
                    }

                    return true;
                } else {
                    String redirectUrl = REDIRECT_URL;
                    if (url.startsWith(redirectUrl)) {
                        String code = UaePassUtil.getQueryParameterValue(url, "code");
                        String state_ = UaePassUtil.getQueryParameterValue(url, "state");
                        String error = UaePassUtil.getQueryParameterValue(url, "error");

                        if (error != null) {
                            webViewAlertDialog.dismiss();
                            if (error.contains("access_denied")) {
                                localValidationError(getString(R.string.error), getString(R.string.user_cancelled_authentication));
                                hideLoading();
                            } else {
                                localValidationError(getString(R.string.error), getString(R.string.user_cancelled_authentication));
                                hideLoading();
                            }
                            return false;
                        }

                        if (!state.equals(state_)) {
                            code = null;
                        }
                        if (code != null) {
                            callAccessTokenUaePass(code);
                        }
                        webViewAlertDialog.dismiss();
                        return true;
                    } else {
                        return false;
                    }
                }
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);


            }
        });
        EditText edit = (EditText) dialogView.findViewById(R.id.editText);
        edit.setFocusable(true);
        edit.requestFocus();
        webView.loadUrl(url);


        webViewAlertDialog.show();
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
        uaePassRequest.setFullnameEN(profileModel.getFullnameEN().replaceAll(",", ""));
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
    public void setLastUser(DBUserTC dbUserTC, UaePassUserModel profileModel) {
       /* startupBinding.btnLoginWithUAEPass.setEnabled(true);
        startupBinding.btnLoginWithUAEPass.setClickable(true);*/
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getDataString().equals(REDIRECT_URL)) {
            webView.loadUrl(mSuccessURLUAEPass);
        }
    }

    private void callAccessTokenUaePass(String code) {
        showLoading(null);
        UaePassUtil.callUAEPassAccessToken(this, code, StartupActivity.this);
    }

    private void callUaePassProfile(String accessToken) {
        UaePassUtil.callUserProfile(this, accessToken, StartupActivity.this);
    }

    public void changeLanguage(boolean chkChangeLang) {
        if (chkChangeLang) {
            LocaleHelper.setLocale(getApplicationContext(), AppConstants.AppLanguage.ISO_CODE_ARABIC);
        } else {
            LocaleHelper.setLocale(getApplicationContext(), AppConstants.AppLanguage.ISO_CODE_ENG);
        }
        finish();
        startActivity(getIntent());
    }

    @Override
    public void setAppConfigurationResponse(int tcVersion, String drupalURL, String tcEN,
                                            String tcAR, String maintenanceStartDate, String maintenanceEndDate,
                                            String maintenanceDes, String maintenanceTitle, String minAndroidAppVersion) {
        this.tcVersion = tcVersion;
        this.drupalURL = drupalURL;
        this.tcEN = tcEN;
        this.tcAR = tcAR;
        this.minAndroidAppVersion = minAndroidAppVersion;
        Date mtStartDate = Utilities.stringToDateDubai(maintenanceStartDate, STANDARD_DATE_FORMAT);
        Date mtEndDate = Utilities.stringToDateDubai(maintenanceEndDate, STANDARD_DATE_FORMAT);
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat(STANDARD_DATE_FORMAT);
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        String strTodayDate = dateFormatGmt.format(new Date());
        Date todayDate = Utilities.stringToDate(strTodayDate, STANDARD_DATE_FORMAT);
        if (todayDate.after(mtStartDate)
                && todayDate.before(mtEndDate)) {
            Bundle bundle = new Bundle();
            bundle.putString(AppConstants.AppUnderMaintenance.DES, maintenanceDes);
            bundle.putString(AppConstants.AppUnderMaintenance.Title, maintenanceTitle);
            moveActivity(StartupActivity.this, MaintenanceActivity.class, true, true, bundle);
        }
        showAppUpdateDialog();
        mPresenter.getLastLoginUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (minAndroidAppVersion != null) {
            showAppUpdateDialog();
        }
    }

    private void showAppUpdateDialog() {
        try {
            String latestVersionName = minAndroidAppVersion.replaceAll("\\.", "");
            latestAndroidVersion = Integer.parseInt(latestVersionName);
            //String currentVersionName = BuildConfig.VERSION_NAME.replaceAll("\\.", "");
            currentVersion = BuildConfig.VERSION_CODE;
            if (currentVersion < latestAndroidVersion) {
                showErrorMsgFromApi(getString(R.string.warning_msg), getString(R.string.update_required_desc),
                        getString(R.string.update_now), null, new DialogButtonClickListener() {
                            @Override
                            public void onPositiveButtonClicked() {
                                Tracker.sendEvent(new Tracker.Event(AppConstants.Events.APP_UPDATE));
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                                }
                            }

                            @Override
                            public void onNegativeButtonClicked() {
                            }
                        });
            }
        } catch (Exception e) {
            Timber.e(e.getMessage());
        }
    }

    @Override
    public void setUser(DBUserTC dbUser) {
        this.dbUser = dbUser;
        if (dbUser != null) {
            if (dbUser.isTouchId()) {
                if (isNetworkConnected()) {
                    if (BiometricUtils.isBiometricPromptEnabled()) {
                        if (currentVersion > latestAndroidVersion) {
                            displayBiometricPrompt();
                        }
                    } else {
                        if (currentVersion > latestAndroidVersion) {
                            Bundle b = new Bundle();
                            b.putString(LAST_LOGIN_USER, new Gson().toJson(dbUser));
                            touchIdFragment = TouchIdFragment.newInstance(b);
                            touchIdFragment.show(getSupportFragmentManager(), touchIdFragment.getTag());
                        }
                    }
                } else {
                    showInternetNotAvailableDialog();
                }
            }
        } else {
            ValidationUtil.showLongToast(this, getString(R.string.last_user_not_enabled_biometric));
        }

    }

    @TargetApi(Build.VERSION_CODES.P)
    public void displayBiometricPrompt() {
        StartupActivityPermissionsDispatcher.askPermissionForBiometricWithPermissionCheck(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @NeedsPermission({Manifest.permission.USE_BIOMETRIC})
    public void askPermissionForBiometric() {
        if (dbUser != null && dbUser.isTouchId()) {
            CancellationSignal mCancellationSignal = new CancellationSignal();
            biometricCallbackV28 = new BiometricCallbackV28(this, mCancellationSignal);
            new BiometricPrompt.Builder(this)
                    .setTitle(this.getString(R.string.txt_login_with_touch_id))
                    .setSubtitle("")
                    .setDescription(this.getString(R.string.txt_desc_touchlogin))
                    .setNegativeButton(this.getString(R.string.txt_cancel), this.getMainExecutor(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (biometricCallbackV28 != null && biometricCallbackV28.getCancellationSignal() != null) {
                                if (!biometricCallbackV28.getCancellationSignal().isCanceled()) {
                                    biometricCallbackV28.getCancellationSignal().cancel();
                                }
                            }
                        }
                    })
                    .build()
                    .authenticate(mCancellationSignal, this.getMainExecutor(), biometricCallbackV28);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @OnPermissionDenied({Manifest.permission.USE_BIOMETRIC})
    public void showPermissionDeniedForBiometric() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.permission_biometric_denied)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                }).show();


    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @OnNeverAskAgain({Manifest.permission.USE_BIOMETRIC})
    public void showNeverAskForStorage() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.permission_biometric_denial)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                }).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @OnShowRationale({Manifest.permission.USE_BIOMETRIC})
    public void showRationaleForStorage(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.permission_biometric_rationale)
                .setPositiveButton(R.string.allow, (dialog, which) -> request.proceed())
                .setNegativeButton(R.string.deny, (dialog, which) -> request.cancel()).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        askPermissionForBiometric();
    }

    public void biometricAuthenticationSucceeded() {
        if (dbUser != null) {
            mPresenter.login(dbUser.getEmail(), dbUser.getPassword());
        } else {
            ValidationUtil.showLongToast(this, getString(R.string.no_user_available_in_local));
        }
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
    public void openNextPage() {
        if (termsConditionFragment != null) {
            termsConditionFragment.dismiss();
        }
        moveActivity(this, DashboardActivity.class, true, true);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void cancelBiometricClick() {
        if (touchIdFragment != null) {
            touchIdFragment.dismiss();
        }
        mPresenter.cancelBiometric(biometricCallbackV28);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void biometricSetUp() {
        mPresenter.setUpBiometric(this);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.back_press_warning), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    @Override
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
}