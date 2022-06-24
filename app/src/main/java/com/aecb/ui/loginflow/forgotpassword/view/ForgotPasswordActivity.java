package com.aecb.ui.loginflow.forgotpassword.view;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.base.mvp.BaseActivity;
import com.aecb.data.api.models.registeruser.OtpGeneratedData;
import com.aecb.databinding.ActivityForgotPasswordBinding;
import com.aecb.presentation.customviews.ToolbarView;
import com.aecb.ui.loginflow.forgotpassword.presenter.ForgotPasswordContract;
import com.aecb.ui.registerflow.otpverify.view.OtpVerifyFragment;
import com.aecb.util.ValidationUtil;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import javax.inject.Inject;

import static com.aecb.AppConstants.IntentKey.CAN_RESEND;
import static com.aecb.AppConstants.IntentKey.OTP_TOKEN;
import static com.aecb.AppConstants.IntentKey.REF_ID;
import static com.aecb.AppConstants.IntentKey.USER_NAME;
import static com.aecb.util.FirebaseLogging.cancelFromForgotPassword;

public class ForgotPasswordActivity extends BaseActivity<ForgotPasswordContract.View,
        ForgotPasswordContract.Presenter> implements ForgotPasswordContract.View, View.OnClickListener,
        TextWatcher {

    @Inject
    public ForgotPasswordContract.Presenter mPresenter;
    ActivityForgotPasswordBinding forgotPasswordBinding;
    OtpVerifyFragment otpVerifyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBinding();
        registerBroadcastReceiver();
    }

    private void enableBinding() {
        forgotPasswordBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        forgotPasswordBinding.btnSendOtp.setOnClickListener(this);
        forgotPasswordBinding.tvCancel.setOnClickListener(this);
        forgotPasswordBinding.edtEmail.addTextChangedListener(this);
        editTextFocusListeners();
    }

    void editTextFocusListeners() {
        forgotPasswordBinding.edtEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                forgotPasswordBinding.txtInputLoutEmail.setHint(getString(R.string.email));
            } else if (!forgotPasswordBinding.edtEmail.getText().toString().isEmpty()) {
                forgotPasswordBinding.txtInputLoutEmail.setHint(getString(R.string.email));
            } else {
                forgotPasswordBinding.txtInputLoutEmail.setHint(getString(R.string.email_address));
            }
        });
    }

    @NonNull
    @Override
    public ForgotPasswordContract.Presenter createPresenter() {
        return this.mPresenter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_forgot_password;
    }

    @Override
    public void initToolbar() {
        ToolbarView tb = findViewById(R.id.toolBar);
        tb.setCancelBtnListener(this::onBackPressed);
        tb.setBtnVisibility(false, true);
        tb.setToolbarTitle(R.string.forgot_password);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cancelFromForgotPassword();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSendOtp:
                mPresenter.sendOtp(forgotPasswordBinding.edtEmail.getText().toString().toLowerCase());
                break;
            case R.id.tvCancel:
                onBackPressed();
                break;
        }
    }

    @Override
    public void showEmptyEmailError() {
        localValidationError(getString(R.string.error), getString(R.string.please_enter_email_address));
    }

    @Override
    public void showInvalidEmailError() {
        localValidationError(getString(R.string.error), getString(R.string.please_ensure_the_login_details_are_entered_correctly));
    }

    @Override
    public void showMessage(String message) {
        ValidationUtil.showLongToast(this, message);
    }

    @Override
    public void setUpOTP(String otpToken, String refID, boolean canResend, OtpGeneratedData generatedData) {
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.IntentKey.FROM_ACTIVITY, AppConstants.ActivityIntentCode.FOR_FORGOT_PASSWORD);
        bundle.putString(AppConstants.IntentKey.OTP_GENERATED_FROM_FORGOT_PASSWORD, new Gson().toJson(generatedData));
        bundle.putString(OTP_TOKEN, otpToken);
        bundle.putString(REF_ID, refID);
        bundle.putBoolean(CAN_RESEND, canResend);
        bundle.putString(USER_NAME, forgotPasswordBinding.edtEmail.getText().toString());
        otpVerifyFragment = OtpVerifyFragment.newInstance(bundle);
        otpVerifyFragment.show(getSupportFragmentManager(), otpVerifyFragment.getTag());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!forgotPasswordBinding.edtEmail.getText().toString().isEmpty()) {
            forgotPasswordBinding.btnSendOtp.setEnabled(true);
            forgotPasswordBinding.btnSendOtp.setBackground(getResources().getDrawable(R.drawable.bg_blue_button));
        } else {
            forgotPasswordBinding.btnSendOtp.setEnabled(false);
            forgotPasswordBinding.btnSendOtp.setBackground(getResources().getDrawable(R.drawable.btn_disable_state));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsVerificationReceiver, intentFilter, SmsRetriever.SEND_PERMISSION, null);

        Task<Void> task = SmsRetriever.getClient(this).startSmsUserConsent(null);
        task.addOnSuccessListener(aVoid -> {
        });
        task.addOnFailureListener(e -> {
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(smsVerificationReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static final int SMS_CONSENT_REQUEST = 2;  // Set to an unused request code
    private final BroadcastReceiver smsVerificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                Status smsRetrieverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

                switch (smsRetrieverStatus.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                        try {
                            startActivityForResult(consentIntent, SMS_CONSENT_REQUEST);
                        } catch (ActivityNotFoundException e) {
                        }
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        break;
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (otpVerifyFragment != null) {
            if (requestCode == SMS_CONSENT_REQUEST) {
                if (resultCode == RESULT_OK) {
                    String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                    otpVerifyFragment.setOTPFromReceiver(message);
                }
            }
        }
    }
}