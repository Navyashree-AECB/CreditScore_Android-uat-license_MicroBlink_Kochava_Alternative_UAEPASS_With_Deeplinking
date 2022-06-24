package com.aecb.ui.registerflow.contactdetails.view;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.base.mvp.BaseActivity;
import com.aecb.data.api.models.registeruser.OtpGeneratedData;
import com.aecb.data.api.models.registeruser.RegisterUserRequest;
import com.aecb.data.api.models.securityquestions.RoundQuestionsData;
import com.aecb.data.api.models.securityquestions.RoundSuccessData;
import com.aecb.databinding.ActivityContactDetailsBinding;
import com.aecb.presentation.customviews.ToolbarView;
import com.aecb.ui.registerflow.contactdetails.presenter.ContactDetailsContract;
import com.aecb.ui.registerflow.createpassword.view.CreatePasswordActivity;
import com.aecb.ui.registerflow.otpverify.view.OtpVerifyFragment;
import com.aecb.ui.registerflow.securityquestionsroundone.view.SecurityQuestionsRoundOneActivity;
import com.aecb.util.FirebaseLogging;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import javax.inject.Inject;

import timber.log.Timber;

import static com.aecb.AppConstants.IntentKey.MOBILE;
import static com.aecb.AppConstants.IntentKey.REGISTER_REQUEST;
import static com.aecb.AppConstants.MOBILE_NUMBER_MAX_LENGTH;
import static com.aecb.AppConstants.MOBILE_NUMBER_PREFIX;
import static com.aecb.AppConstants.confirmEmail;
import static com.aecb.AppConstants.email;
import static com.aecb.AppConstants.mobileNumber;
import static com.aecb.util.FirebaseLogging.withScanRegistration;
import static com.aecb.util.FirebaseLogging.withoutScanRegistration;

public class ContactDetailsActivity extends BaseActivity<ContactDetailsContract.View,
        ContactDetailsContract.Presenter> implements ContactDetailsContract.View, View.OnClickListener,
        TextWatcher {

    @Inject
    public ContactDetailsContract.Presenter mPresenter;
    ActivityContactDetailsBinding contactDetailsBinding;
    OtpVerifyFragment otpVerifyFragment;
    RegisterUserRequest registerUserRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBinding();
        getDataFromPersonalScreen();
        registerBroadcastReceiver();
    }

    void getDataFromPersonalScreen() {
        registerUserRequest = new Gson().fromJson(getIntent().getStringExtra(REGISTER_REQUEST), RegisterUserRequest.class);
        Timber.e(registerUserRequest.toString());
    }

    private void enableBinding() {
        contactDetailsBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        contactDetailsBinding.btnSubmit.setOnClickListener(this);
        contactDetailsBinding.edtMobile.setOnClickListener(this);
        contactDetailsBinding.edtEmail.setOnClickListener(this);
        contactDetailsBinding.edtConfirmEmail.setOnClickListener(this);
        contactDetailsBinding.edtConfirmEmail.setLongClickable(false);
        contactDetailsBinding.edtMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (contactDetailsBinding.edtMobile.length() != 0 &&
                        contactDetailsBinding.edtMobile.getText().toString().length() == 14) {
                    contactDetailsBinding.viewLineMobile.setBackgroundResource(R.color.gray_light);
                } else {
                    contactDetailsBinding.viewLineMobile.setBackgroundResource(R.color.underlineColor);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == MOBILE_NUMBER_MAX_LENGTH) {
                    contactDetailsBinding.edtEmail.requestFocus();
                }
                if (!s.toString().startsWith(MOBILE_NUMBER_PREFIX)) {
                    contactDetailsBinding.edtMobile.setText(MOBILE_NUMBER_PREFIX);
                    Selection.setSelection(contactDetailsBinding.edtMobile.getText(), contactDetailsBinding.edtMobile.getText().length());
                }
            }
        });
        contactDetailsBinding.edtEmail.addTextChangedListener(this);
        contactDetailsBinding.edtConfirmEmail.addTextChangedListener(this);
        editTextFocusListeners();
        setUpCountyCode();
        if (mobileNumber != null && !mobileNumber.isEmpty()) {
            contactDetailsBinding.edtMobile.setText(mobileNumber);
        }
        if (email != null && !email.isEmpty()) {
            contactDetailsBinding.edtEmail.setText(email);
        }
        if (confirmEmail != null && !confirmEmail.isEmpty()) {
            contactDetailsBinding.edtConfirmEmail.setText(confirmEmail);
        }
        contactDetailsBinding.viewLineMobile.setBackgroundResource(R.color.gray_light);
        contactDetailsBinding.viewLineEmail.setBackgroundResource(R.color.gray_light);
        contactDetailsBinding.viewLineConfirmEmail.setBackgroundResource(R.color.gray_light);
    }

    @NonNull
    @Override
    public ContactDetailsContract.Presenter createPresenter() {
        return this.mPresenter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_contact_details;
    }

    @Override
    public void initToolbar() {
        ToolbarView tb = findViewById(R.id.toolBar);
        tb.setCancelBtnListener(this::onBackPressed);
        tb.setBtnVisibility(false, true);
        tb.setToolbarTitle(R.string.contact_details);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                if (FirebaseLogging.isScanned) {
                    withScanRegistration();
                } else {
                    withoutScanRegistration();
                }
                registerUserRequest.setMobile(contactDetailsBinding.edtMobile.getText().toString().replaceAll(" ", ""));
                registerUserRequest.setEmail(contactDetailsBinding.edtEmail.getText().toString());
                mPresenter.sendContactDetails(registerUserRequest,
                        contactDetailsBinding.edtConfirmEmail.getText().toString());
                mobileNumber = "";
                email = "";
                confirmEmail = "";
                break;
            case R.id.edtMobile:
                if (contactDetailsBinding.edtMobile.getText().toString().isEmpty()) {
                    contactDetailsBinding.viewLineMobile.setBackgroundResource(R.color.underlineColor);
                }
                break;
            case R.id.edtEmail:
                if (contactDetailsBinding.edtEmail.getText().toString().isEmpty()) {
                    contactDetailsBinding.viewLineEmail.setBackgroundResource(R.color.underlineColor);
                }
                break;
            case R.id.edtConfirmEmail:
                if (contactDetailsBinding.edtConfirmEmail.getText().toString().isEmpty()) {
                    contactDetailsBinding.viewLineConfirmEmail.setBackgroundResource(R.color.underlineColor);
                }
                break;
        }

    }

    @Override
    public void showEmptyMobileError() {
        localValidationError(getString(R.string.error), getString(R.string.please_enter_mobile));
        contactDetailsBinding.viewLineMobile.setBackgroundResource(R.color.underlineColor);
        contactDetailsBinding.edtMobile.requestFocus();
    }

    @Override
    public void showEmptyEmailError() {
        localValidationError(getString(R.string.error), getString(R.string.please_enter_email_address));
        contactDetailsBinding.viewLineEmail.setBackgroundResource(R.color.underlineColor);
        contactDetailsBinding.edtEmail.requestFocus();
    }

    @Override
    public void showInvalidEmailError() {
        localValidationError(getString(R.string.error), getString(R.string.please_enter_valid_email));
        contactDetailsBinding.viewLineEmail.setBackgroundResource(R.color.underlineColor);
        contactDetailsBinding.edtEmail.requestFocus();
    }

    @Override
    public void showEmptyConfirmEmailError() {
        localValidationError(getString(R.string.error), getString(R.string.please_enter_confirm_email));
        contactDetailsBinding.viewLineConfirmEmail.setBackgroundResource(R.color.underlineColor);
        contactDetailsBinding.edtConfirmEmail.requestFocus();
    }

    @Override
    public void showEmailNotMatchedError() {
        localValidationError(getString(R.string.error), getString(R.string.email_does_not_match));
        contactDetailsBinding.viewLineConfirmEmail.setBackgroundResource(R.color.underlineColor);
        contactDetailsBinding.edtConfirmEmail.requestFocus();
    }

    @Override
    public void showInvalidMobileError() {
        localValidationError(getString(R.string.error), getString(R.string.enter_valid_phone));
        contactDetailsBinding.viewLineMobile.setBackgroundResource(R.color.underlineColor);
        contactDetailsBinding.edtMobile.requestFocus();
    }

    @Override
    public void openPasswordScreen(RoundSuccessData roundSuccessData) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.IntentKey.SESSION_TOKEN, roundSuccessData.getParseUser().getSessionToken());
        moveActivity(this, CreatePasswordActivity.class, false, false, bundle);
    }

    @Override
    public void openQuestionsRoundOneScreen(RoundQuestionsData roundQuestionsData) {
        Bundle bundle = new Bundle();
        bundle.putString(REGISTER_REQUEST, new Gson().toJson(registerUserRequest));
        bundle.putString(MOBILE, contactDetailsBinding.edtMobile.getText().toString());
        bundle.putString(AppConstants.IntentKey.ROUND_QUESTIONS_FROM_REGISTER, new Gson().toJson(roundQuestionsData));
        moveActivity(this, SecurityQuestionsRoundOneActivity.class, false, false, bundle);
    }

    @Override
    public void openOtpDialog(OtpGeneratedData otpGeneratedData, String referenceID) {
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.IntentKey.FROM_ACTIVITY, AppConstants.ActivityIntentCode.FOR_CONTACT_DETAILS);
        bundle.putString(AppConstants.IntentKey.OTP_GENERATED_FROM_REGISTER, new Gson().toJson(otpGeneratedData));
        bundle.putString(AppConstants.IntentKey.MOBILE, registerUserRequest.getMobile());
        bundle.putString(AppConstants.IntentKey.EMAIL, registerUserRequest.getEmail());
        bundle.putString(AppConstants.IntentKey.REFERENCE_ID, referenceID);
        otpVerifyFragment = OtpVerifyFragment.newInstance(bundle);
        otpVerifyFragment.show(getSupportFragmentManager(), otpVerifyFragment.getTag());
    }

    private void setUpCountyCode() {
        contactDetailsBinding.edtMobile.addTextChangedListener(this);
        contactDetailsBinding.edtMobile.setText(MOBILE_NUMBER_PREFIX);
        Selection.setSelection(contactDetailsBinding.edtMobile.getText(), contactDetailsBinding.edtMobile.getText().length());
    }

    void editTextFocusListeners() {
        contactDetailsBinding.edtMobile.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (contactDetailsBinding.edtMobile.getText().toString().length() < 13) {
                    contactDetailsBinding.viewLineMobile.setBackgroundResource(R.color.underlineColor);
                }
                contactDetailsBinding.txtInputLoutMobile.setHint(getString(R.string.txt_mobile_number));
            } else if (!contactDetailsBinding.edtMobile.getText().toString().isEmpty()) {
                contactDetailsBinding.txtInputLoutMobile.setHint(getString(R.string.txt_mobile_number));
            } else {
                contactDetailsBinding.txtInputLoutMobile.setHint(getString(R.string.txt_mobile_number));
            }
        });
        contactDetailsBinding.edtEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (contactDetailsBinding.edtEmail.getText().toString().isEmpty()) {
                    contactDetailsBinding.viewLineEmail.setBackgroundResource(R.color.underlineColor);
                }
                contactDetailsBinding.txtInputLoutEmail.setHint(getString(R.string.email_address));
            } else if (!contactDetailsBinding.edtEmail.getText().toString().isEmpty()) {
                contactDetailsBinding.txtInputLoutEmail.setHint(getString(R.string.email_address));
            } else {
                contactDetailsBinding.txtInputLoutEmail.setHint(getString(R.string.email_address));
            }
        });
        contactDetailsBinding.edtConfirmEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (contactDetailsBinding.edtConfirmEmail.getText().toString().isEmpty()) {
                    contactDetailsBinding.viewLineConfirmEmail.setBackgroundResource(R.color.underlineColor);
                }
                contactDetailsBinding.txtInputLoutConfirmEmail.setHint(getString(R.string.confirm_email));
            } else if (!contactDetailsBinding.edtConfirmEmail.getText().toString().isEmpty()) {
                contactDetailsBinding.txtInputLoutConfirmEmail.setHint(getString(R.string.confirm_email));
            } else {
                contactDetailsBinding.txtInputLoutConfirmEmail.setHint(getString(R.string.confirm_email));
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.hashCode() == contactDetailsBinding.edtEmail.getText().hashCode()) {
            if (contactDetailsBinding.edtEmail.length() != 0 &&
                    Patterns.EMAIL_ADDRESS.matcher(contactDetailsBinding.edtEmail.getText().toString()).matches()) {
                contactDetailsBinding.viewLineEmail.setBackgroundResource(R.color.gray_light);
            } else {
                contactDetailsBinding.viewLineEmail.setBackgroundResource(R.color.underlineColor);
            }
        }

        if (s.hashCode() == contactDetailsBinding.edtConfirmEmail.getText().hashCode()) {
            if (contactDetailsBinding.edtConfirmEmail.length() != 0 &&
                    Patterns.EMAIL_ADDRESS.matcher(contactDetailsBinding.edtConfirmEmail.getText().toString()).matches()) {
                contactDetailsBinding.viewLineConfirmEmail.setBackgroundResource(R.color.gray_light);
            } else {
                contactDetailsBinding.viewLineConfirmEmail.setBackgroundResource(R.color.underlineColor);
            }
        }

        if (!contactDetailsBinding.edtMobile.getText().toString().isEmpty() &&
                !contactDetailsBinding.edtEmail.getText().toString().isEmpty() &&
                !contactDetailsBinding.edtConfirmEmail.getText().toString().isEmpty() &&
                contactDetailsBinding.edtMobile.getText().toString().length() == 14) {
            contactDetailsBinding.btnSubmit.setEnabled(true);
            contactDetailsBinding.btnSubmit.setBackground(getResources().getDrawable(R.drawable.bg_blue_button));
        } else {
            contactDetailsBinding.btnSubmit.setEnabled(false);
            contactDetailsBinding.btnSubmit.setBackground(getResources().getDrawable(R.drawable.btn_disable_state));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mobileNumber = contactDetailsBinding.edtMobile.getText().toString();
        email = contactDetailsBinding.edtEmail.getText().toString();
        confirmEmail = contactDetailsBinding.edtConfirmEmail.getText().toString();
    }

    private void startSmsUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        client.startSmsUserConsent(null).addOnSuccessListener(aVoid -> {
        }).addOnFailureListener(e -> {
        });
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
//        unregisterReceiver(smsVerificationReceiver);
    }

    //Navya crashlytics
    @Override
    protected void onDestroy() {
        super.onDestroy();
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