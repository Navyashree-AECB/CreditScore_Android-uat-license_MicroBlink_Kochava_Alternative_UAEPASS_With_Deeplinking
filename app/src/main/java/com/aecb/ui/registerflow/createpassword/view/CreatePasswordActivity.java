package com.aecb.ui.registerflow.createpassword.view;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.base.mvp.BaseActivity;
import com.aecb.base.mvp.BlurDialogBaseFragment;
import com.aecb.data.api.models.savedeviceinfo.SaveDeviceInfoRequest;
import com.aecb.databinding.ActivityCreatePasswordBinding;
import com.aecb.presentation.customviews.ToolbarView;
import com.aecb.ui.loginflow.login.view.LoginActivity;
import com.aecb.ui.loginflow.touchid.view.TouchIdFragment;
import com.aecb.ui.registerflow.createpassword.presenter.CreatePasswordContract;
import com.aecb.util.Utilities;
import com.facebook.ads.sdk.serverside.CustomData;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.kochava.base.Tracker;

import java.util.HashMap;

import javax.inject.Inject;

import static com.aecb.App.getFirebaseAnalytics;
import static com.aecb.AppConstants.ActivityIntentCode.FOR_CHANGE_PASSWORD;
import static com.aecb.AppConstants.IntentKey.PASSWORD;
import static com.aecb.AppConstants.generateRandomID;
import static com.aecb.util.ConversionApi.callConversionApi;
import static com.aecb.util.FirebaseLogging.cancelCreatePassword;
import static com.aecb.util.Utilities.isStringContainsSpecialCharacter;
import static com.aecb.util.Utilities.isTextContainLowerCase;
import static com.aecb.util.Utilities.isTextContainNumber;
import static com.aecb.util.Utilities.isTextContainUpperCase;

public class CreatePasswordActivity extends BaseActivity<CreatePasswordContract.View,
        CreatePasswordContract.Presenter> implements CreatePasswordContract.View, View.OnClickListener,
        TextWatcher {

    @Inject
    public CreatePasswordContract.Presenter mPresenter;
    ActivityCreatePasswordBinding createPasswordBinding;
    private BlurDialogBaseFragment touchIdFragment;
    private int openedFromScreen;
    private SaveDeviceInfoRequest saveDeviceInfoRequest;
    private String sessionToken = "";
    private String userName = "";
    private Handler handler = new Handler();
    AppEventsLogger logger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.getLastLoginUser();
        enableBinding();
        getBundleData();
        logger = AppEventsLogger.newLogger(this);
    }

    void getBundleData() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            openedFromScreen = bundle.getInt(AppConstants.IntentKey.FROM_ACTIVITY);
            if (openedFromScreen == FOR_CHANGE_PASSWORD) {
                createPasswordBinding.txtInputLoutCurrentPassword.setVisibility(View.VISIBLE);
                createPasswordBinding.txtInputLoutConfirmPassword.setHint(getString(R.string.re_enter_new_password));
                createPasswordBinding.toolBar.setToolbarTitle(getString(R.string.txt_change_password));
            } else {
                createPasswordBinding.btnConfirm.setText(R.string.txt_confirm_for_create_password);
                sessionToken = bundle.getString(AppConstants.IntentKey.SESSION_TOKEN);
                if (bundle.getString(AppConstants.IntentKey.USER_NAME) != null) {
                    userName = bundle.getString(AppConstants.IntentKey.USER_NAME);
                }
            }
        }
    }

    private void enableBinding() {
        createPasswordBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        createPasswordBinding.btnConfirm.setOnClickListener(this);
        createPasswordBinding.edtPassword.addTextChangedListener(this);
        createPasswordBinding.edtConfirmPassword.setLongClickable(false);
        createPasswordBinding.edtConfirmPassword.addTextChangedListener(this);
        editTextFocusListeners();
    }

    private void saveDeviceInfo(String password) {
        saveDeviceInfoRequest = new SaveDeviceInfoRequest();
        saveDeviceInfoRequest.setDeviceManufacturer(Utilities.getDeviceName());
        saveDeviceInfoRequest.setDeviceOsVersion(Utilities.getAndroidVersion());
        saveDeviceInfoRequest.setDeviceType(AppConstants.ApiParameter.DEVICE_TYPE);
        saveDeviceInfoRequest.setDeviceToken(mPresenter.getFcmId());
        saveDeviceInfoRequest.setDeviceTimeZone(Utilities.getDeviceTimeZone());
        saveDeviceInfoRequest.setDeviceId(generateRandomID());
        mPresenter.saveDeviceInfo(saveDeviceInfoRequest, password);
    }

    @NonNull
    @Override
    public CreatePasswordContract.Presenter createPresenter() {
        return this.mPresenter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_create_password;
    }

    @Override
    public void initToolbar() {
        ToolbarView tb = findViewById(R.id.toolBar);
        tb.setCancelBtnListener(this::onBackPressed);
        tb.setBtnVisibility(false, true);
        tb.setToolbarTitle(R.string.create_password);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnConfirm) {
            if (openedFromScreen == FOR_CHANGE_PASSWORD) {
                mPresenter.changePassword(createPasswordBinding.edtCurrentPassword.getText().toString(),
                        createPasswordBinding.edtPassword.getText().toString(),
                        createPasswordBinding.edtConfirmPassword.getText().toString(), sessionToken, userName);
            } else {
                mPresenter.sendPasswordDetails(createPasswordBinding.edtPassword.getText().toString(),
                        createPasswordBinding.edtConfirmPassword.getText().toString(), sessionToken, userName);
            }

        }
    }

    @Override
    public void showEmptyPasswordError() {
        localValidationError(getString(R.string.error), getString(R.string.please_enter_password));
    }

    @Override
    public void showInvalidPasswordError() {
        localValidationError(getString(R.string.error), getString(R.string.invalid_password_error_msgs));
    }

    @Override
    public void showEmptyConfirmPasswordError() {
        localValidationError(getString(R.string.error), getString(R.string.please_enter_confirm_password));
    }

    @Override
    public void showPasswordNotMatchedError() {
        if (openedFromScreen == FOR_CHANGE_PASSWORD) {
            localValidationError(getString(R.string.error), getString(R.string.password_does_not_match));
            cancelCreatePassword();
        } else {
            localValidationError(getString(R.string.error), getString(R.string.please_enter_correct_password));
        }
    }

    @Override
    public void showCurrentPasswordError() {
        localValidationError(getString(R.string.error), getString(R.string.please_enter_current_password));
    }

    @Override
    public void showInvalidCurrentPasswordError() {
        localValidationError(getString(R.string.error), getString(R.string.invalid_password_error_msgs));
    }

    @Override
    public void redirectToParticularScreen() {
        if (openedFromScreen == AppConstants.ActivityIntentCode.FOR_FORGOT_PASSWORD) {
            handler.postDelayed(() -> moveActivity(this, LoginActivity.class, true, true), 3000);
        } else if (openedFromScreen == FOR_CHANGE_PASSWORD) {
            moveActivity(this, LoginActivity.class, true, true);
        } else {
            saveDeviceInfo(createPasswordBinding.edtConfirmPassword.getText().toString());
            sendFbRegistrationEvent();
        }
    }

    private void sendFbRegistrationEvent() {
        if (mPresenter.getInstallSource().equalsIgnoreCase("") &&
                mPresenter.getInstalledCampaignId().equalsIgnoreCase("")) {
            logger.logEvent(AppConstants.Events.REGISTRATION);
            //logger.logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION);
            getFirebaseAnalytics().logEvent(AppConstants.Events.REGISTRATION, null);
            callConversionApi(new CustomData(), AppConstants.Events.REGISTRATION);
            Tracker.sendEvent(new Tracker.Event(AppConstants.Events.REGISTRATION));
        } else {
            Bundle params = new Bundle();
            params.putString("Source", mPresenter.getInstallSource());
            params.putString("campaignID", mPresenter.getInstalledCampaignId());
            logger.logEvent(AppConstants.Events.REGISTRATION, params);
            //logger.logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION);
            getFirebaseAnalytics().logEvent(AppConstants.Events.REGISTRATION, params);
            CustomData customData = new CustomData();
            HashMap<String, String> data = new HashMap<>();
            data.put("Source", mPresenter.getInstallSource());
            data.put("campaignID", mPresenter.getInstalledCampaignId());
            customData.setCustomProperties(data);
            callConversionApi(customData, AppConstants.Events.REGISTRATION);
            Tracker.sendEvent(new Tracker.Event(AppConstants.Events.REGISTRATION));
        }
    }

    @Override
    public void openTouchId(String password) {
        Bundle b = new Bundle();
        b.putString(PASSWORD, password);
        touchIdFragment = TouchIdFragment.newInstance(b);
        touchIdFragment.show(getSupportFragmentManager(), touchIdFragment.getTag());
    }

    void editTextFocusListeners() {
        createPasswordBinding.edtPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                createPasswordBinding.txtInputLoutPassword.setHint(getString(R.string.password));
            } else if (!createPasswordBinding.edtPassword.getText().toString().isEmpty()) {
                createPasswordBinding.txtInputLoutPassword.setHint(getString(R.string.password));
            } else {
                createPasswordBinding.txtInputLoutPassword.setHint(getString(R.string.password));
            }
        });
        createPasswordBinding.edtConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                createPasswordBinding.txtInputLoutConfirmPassword.setHint(getString(R.string.hint_confirm_password_for_create_password));
            } else if (!createPasswordBinding.edtConfirmPassword.getText().toString().isEmpty()) {
                createPasswordBinding.txtInputLoutConfirmPassword.setHint(getString(R.string.hint_confirm_password_for_create_password));
            } else {
                createPasswordBinding.txtInputLoutConfirmPassword.setHint(getString(R.string.hint_confirm_password_for_create_password));
            }
        });
        createPasswordBinding.edtCurrentPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                createPasswordBinding.txtInputLoutCurrentPassword.setHint(getString(R.string.current_password));
            } else if (!createPasswordBinding.edtCurrentPassword.getText().toString().isEmpty()) {
                createPasswordBinding.txtInputLoutCurrentPassword.setHint(getString(R.string.current_password));
            } else {
                createPasswordBinding.txtInputLoutCurrentPassword.setHint(getString(R.string.enter_current_password));
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!createPasswordBinding.edtPassword.getText().toString().isEmpty() &&
                !createPasswordBinding.edtConfirmPassword.getText().toString().isEmpty()) {
            if (openedFromScreen != 0 && openedFromScreen == FOR_CHANGE_PASSWORD) {
                if (!createPasswordBinding.edtCurrentPassword.getText().toString().isEmpty()) {
                    createPasswordBinding.btnConfirm.setEnabled(true);
                    createPasswordBinding.btnConfirm.setBackground(getResources().getDrawable(R.drawable.bg_blue_button));
                } else {
                    createPasswordBinding.btnConfirm.setEnabled(false);
                    createPasswordBinding.btnConfirm.setBackground(getResources().getDrawable(R.drawable.btn_disable_state));
                }
            } else {
                createPasswordBinding.btnConfirm.setEnabled(true);
                createPasswordBinding.btnConfirm.setBackground(getResources().getDrawable(R.drawable.bg_blue_button));
            }

        } else {
            createPasswordBinding.btnConfirm.setEnabled(false);
            createPasswordBinding.btnConfirm.setBackground(getResources().getDrawable(R.drawable.btn_disable_state));
        }
        passwordValidation();
    }


    private void passwordValidation() {
        if (createPasswordBinding.edtPassword.length() >= 8) {
            createPasswordBinding.tvPwdValidationOne.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password_green, 0, 0, 0);
            createPasswordBinding.tvPwdValidationOne.setTypeface(createPasswordBinding.tvPwdValidationTwo.getTypeface(),
                    Typeface.BOLD);
        } else {
            createPasswordBinding.tvPwdValidationOne.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password_grey, 0, 0, 0);
            createPasswordBinding.tvPwdValidationOne.setTypeface(null,
                    Typeface.NORMAL);
        }

        if (isTextContainUpperCase(createPasswordBinding.edtPassword.getText().toString())) {
            createPasswordBinding.tvPwdValidationTwo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password_green, 0, 0, 0);
            createPasswordBinding.tvPwdValidationTwo.setTypeface(createPasswordBinding.tvPwdValidationTwo.getTypeface(),
                    Typeface.BOLD);
        } else {
            createPasswordBinding.tvPwdValidationTwo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password_grey, 0, 0, 0);
            createPasswordBinding.tvPwdValidationTwo.setTypeface(null,
                    Typeface.NORMAL);
        }

        if (isTextContainLowerCase(createPasswordBinding.edtPassword.getText().toString())) {
            createPasswordBinding.tvPwdValidationThree.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password_green, 0, 0, 0);
            createPasswordBinding.tvPwdValidationThree.setTypeface(createPasswordBinding.tvPwdValidationTwo.getTypeface(),
                    Typeface.BOLD);
        } else {
            createPasswordBinding.tvPwdValidationThree.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password_grey, 0, 0, 0);
            createPasswordBinding.tvPwdValidationThree.setTypeface(null,
                    Typeface.NORMAL);
        }

        if (isStringContainsSpecialCharacter(createPasswordBinding.edtPassword.getText().toString())) {
            createPasswordBinding.tvPwdValidationFour.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password_green, 0, 0, 0);
            createPasswordBinding.tvPwdValidationFour.setTypeface(createPasswordBinding.tvPwdValidationTwo.getTypeface(),
                    Typeface.BOLD);
        } else {
            createPasswordBinding.tvPwdValidationFour.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password_grey, 0, 0, 0);
            createPasswordBinding.tvPwdValidationFour.setTypeface(null,
                    Typeface.NORMAL);
        }
        if (isTextContainNumber(createPasswordBinding.edtPassword.getText().toString())) {
            createPasswordBinding.tvPwdValidationFive.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password_green, 0, 0, 0);
            createPasswordBinding.tvPwdValidationFive.setTypeface(createPasswordBinding.tvPwdValidationFive.getTypeface(),
                    Typeface.BOLD);
        } else {
            createPasswordBinding.tvPwdValidationFive.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password_grey, 0, 0, 0);
            createPasswordBinding.tvPwdValidationFive.setTypeface(null,
                    Typeface.NORMAL);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}