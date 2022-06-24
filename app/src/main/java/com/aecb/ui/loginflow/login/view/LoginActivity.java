package com.aecb.ui.loginflow.login.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.text.Editable;
import android.text.TextWatcher;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.aecb.databinding.ActivityLoginBinding;
import com.aecb.listeners.OnDateSetListener;
import com.aecb.presentation.biometric.BiometricCallbackV28;
import com.aecb.presentation.biometric.BiometricUtils;
import com.aecb.ui.addpassportdob.view.AddDobPassportFragment;
import com.aecb.ui.dashboard.view.DashboardActivity;
import com.aecb.ui.loginflow.forgotpassword.view.ForgotPasswordActivity;
import com.aecb.ui.loginflow.login.presenter.LoginContract;
import com.aecb.ui.loginflow.touchid.view.TouchIdFragment;
import com.aecb.ui.loginflow.uaepasspin.uaepass.UaePassAuthorizationModel;
import com.aecb.ui.loginflow.uaepasspin.uaepass.UaePassCallback;
import com.aecb.ui.loginflow.uaepasspin.uaepass.UaePassUserModel;
import com.aecb.ui.loginflow.uaepasspin.uaepass.UaePassUtil;
import com.aecb.ui.loginflow.uaepasspin.view.UAEPassPinActivity;
import com.aecb.ui.startsup.view.StartupActivity;
import com.aecb.ui.termsandconditions.view.TermsAndConditionsFragment;
import com.aecb.util.Utilities;
import com.aecb.util.ValidationUtil;
import com.google.gson.Gson;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import timber.log.Timber;

import static com.aecb.AppConstants.ActivityIntentCode.UAE_PASS_REQUEST_CODE;
import static com.aecb.AppConstants.IntentKey.AUTHENTICATION_CODE;
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
public class LoginActivity extends BaseActivity<LoginContract.View, LoginContract.Presenter>
        implements LoginContract.View, View.OnClickListener, TextWatcher, UaePassCallback, OnDateSetListener {

    @Inject
    public LoginContract.Presenter mPresenter;
    private BiometricCallbackV28 biometricCallbackV28;
    private ActivityLoginBinding loginBinding;
    private TermsAndConditionsFragment termsConditionFragment;
    private BlurDialogBaseFragment touchIdFragment;
    private DBUserTC dbUser;
    UaePassRequest uaePassRequest;
    private BlurDialogBaseFragment addDobPassportFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBinding();
        mPresenter.getLastLoginUser();
        if (BuildConfig.DEBUG) {
            loginBinding.edtEmail.setText("dinawaleed1133@icloud.com");
            loginBinding.edtPassword.setText("AecbAdmin@2022");

        }
    }

    private void enableBinding() {
        loginBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        loginBinding.tvForgotPassword.setOnClickListener(this);
        loginBinding.btnSubmit.setOnClickListener(this);
        loginBinding.ivTouchId.setOnClickListener(this);
        loginBinding.btnLoginWithUAEPass.setOnClickListener(this);
        uaePassRequest = new UaePassRequest();
        if (mPresenter.getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ENG)) {
            //loginBinding.ivLoginUAEEN.setVisibility(View.VISIBLE);
           // loginBinding.ivLoginUAEAR.setVisibility(View.GONE);
        } else {
           // loginBinding.ivLoginUAEEN.setVisibility(View.VISIBLE);
            //loginBinding.ivLoginUAEAR.setVisibility(View.GONE);
        }
        editTextFocusListeners();
    }

    void editTextFocusListeners() {
        loginBinding.edtEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                loginBinding.txtInputLoutEmail.setHint(getString(R.string.email));
            } else if (!loginBinding.edtEmail.getText().toString().isEmpty()) {
                loginBinding.txtInputLoutEmail.setHint(getString(R.string.email));
            } else {
                loginBinding.txtInputLoutEmail.setHint(getString(R.string.email_address));
            }
        });
        loginBinding.edtPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                loginBinding.txtInputLoutPassword.setHint(getString(R.string.password));
            } else if (!loginBinding.edtPassword.getText().toString().isEmpty()) {
                loginBinding.txtInputLoutPassword.setHint(getString(R.string.password));
            } else {
                loginBinding.txtInputLoutPassword.setHint(getString(R.string.password));
            }
        });
        loginBinding.edtEmail.addTextChangedListener(this);
        loginBinding.edtPassword.addTextChangedListener(this);
    }

    @NonNull
    @Override
    public LoginContract.Presenter createPresenter() {
        return this.mPresenter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    public void initToolbar() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void biometricSetUp() {
        mPresenter.setUpBiometric(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                mPresenter.login(loginBinding.edtEmail.getText().toString(),
                        loginBinding.edtPassword.getText().toString());
                break;
            case R.id.tvForgotPassword:
                moveActivity(this, ForgotPasswordActivity.class, false, false);
                break;
            case R.id.ivTouchId:
                mPresenter.getLastLoginUser();
                break;
            case R.id.btnLoginWithUAEPass:
                Intent intent = new Intent(LoginActivity.this, UAEPassPinActivity.class);
                startActivityForResult(intent, UAE_PASS_REQUEST_CODE);
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.P)
    public void displayBiometricPrompt() {
        LoginActivityPermissionsDispatcher.askPermissionForBiometricWithPermissionCheck(this);
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

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void cancelBiometricClick() {
        if (touchIdFragment != null) {
            touchIdFragment.dismiss();
        }
        mPresenter.cancelBiometric(biometricCallbackV28);
    }

    @Override
    public void showEmptyEmailError() {
        localValidationError(getString(R.string.error), getString(R.string.empty_email_error));
        loginBinding.edtEmail.requestFocus();
    }

    @Override
    public void showInvalidEmailError() {
        localValidationError(getString(R.string.error), getString(R.string.please_ensure_the_login_details_are_entered_correctly));
        loginBinding.edtEmail.requestFocus();
    }

    @Override
    public void showEmptyPasswordError() {
        localValidationError(getString(R.string.error), getString(R.string.empty_password_error));
        loginBinding.edtPassword.requestFocus();
    }

    @Override
    public void showPasswordLengthError() {
        localValidationError(getString(R.string.error), getString(R.string.please_ensure_the_login_details_are_entered_correctly));
        loginBinding.edtPassword.requestFocus();
    }

    @Override
    public void showInvalidPassword() {
        localValidationError(getString(R.string.error), getString(R.string.invalid_password_error_msgs));
        loginBinding.edtPassword.requestFocus();
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!loginBinding.edtEmail.getText().toString().isEmpty() && !loginBinding.edtPassword.getText().toString().isEmpty()) {
            loginBinding.btnSubmit.setEnabled(true);
            loginBinding.btnSubmit.setBackground(getResources().getDrawable(R.drawable.bg_blue_button));
        } else {
            loginBinding.btnSubmit.setEnabled(false);
            loginBinding.btnSubmit.setBackground(getResources().getDrawable(R.drawable.btn_disable_state));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void setUser(DBUserTC dbUser) {
        this.dbUser = dbUser;
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
        } else {
            ValidationUtil.showLongToast(this, getString(R.string.last_user_not_enabled_biometric));
        }

    }

    @Override
    public void onBackPressed() {
        moveActivity(LoginActivity.this, StartupActivity.class, true, true);
    }

    private void callAccessTokenUaePass(String code) {
        showLoading(null);
        UaePassUtil.callUAEPassAccessToken(this, code, LoginActivity.this);
    }

    private void callUaePassProfile(String accessToken) {
        UaePassUtil.callUserProfile(this, accessToken, LoginActivity.this);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UAE_PASS_REQUEST_CODE && resultCode == RESULT_OK) {
            String code = data.getStringExtra(AUTHENTICATION_CODE);
            callAccessTokenUaePass(code);
        }
    }
}