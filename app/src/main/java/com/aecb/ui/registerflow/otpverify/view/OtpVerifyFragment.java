package com.aecb.ui.registerflow.otpverify.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;

import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.base.mvp.BaseActivity;
import com.aecb.base.mvp.BlurDialogBaseFragment;
import com.aecb.data.api.models.generateotp.GenerateOtpRequest;
import com.aecb.data.api.models.registeruser.OtpGeneratedData;
import com.aecb.data.api.models.verifyotp.OtpVerifyRequest;
import com.aecb.databinding.FragmentOtpVerifyBinding;
import com.aecb.presentation.blurcustomview.BlurConfig;
import com.aecb.presentation.blurcustomview.SmartAsyncPolicyHolder;
import com.aecb.ui.registerflow.createpassword.view.CreatePasswordActivity;
import com.aecb.ui.registerflow.otpverify.presenter.OtpVerifyContract;
import com.aecb.util.ScreenUtils;
import com.aecb.util.Utilities;
import com.aecb.util.ValidationUtil;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import java.util.UUID;

import javax.inject.Inject;

import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static com.aecb.AppConstants.ActivityIntentCode.FOR_CONTACT_DETAILS;
import static com.aecb.AppConstants.ActivityIntentCode.FOR_FORGOT_PASSWORD;
import static com.aecb.AppConstants.ApiParameter.FORGOT_PASSWORD;
import static com.aecb.AppConstants.COUNTDOWN_INTERVAL_TIME;
import static com.aecb.AppConstants.DEFAULT_TIMER;
import static com.aecb.AppConstants.IntentKey.CAN_RESEND;
import static com.aecb.AppConstants.IntentKey.OTP_GENERATED_FROM_FORGOT_PASSWORD;
import static com.aecb.AppConstants.IntentKey.OTP_GENERATED_FROM_REGISTER;
import static com.aecb.AppConstants.IntentKey.OTP_TOKEN;
import static com.aecb.AppConstants.IntentKey.REF_ID;
import static com.aecb.AppConstants.IntentKey.REGISTRATION;
import static com.aecb.AppConstants.IntentKey.USER_NAME;
import static com.aecb.AppConstants.TWO_DIGIT;
import static com.aecb.AppConstants.arabicToDecimal;
import static com.aecb.util.varibles.StringConstants.SPACE;

public class OtpVerifyFragment extends BlurDialogBaseFragment<OtpVerifyContract.View,
        OtpVerifyContract.Presenter> implements OtpVerifyContract.View, TextWatcher, View.OnClickListener {

    @Inject
    public OtpVerifyContract.Presenter mPresenter;
    private FragmentOtpVerifyBinding mOtpVerifyBinding;
    private Context mContext;
    private int openedFromScreen;
    private OtpVerifyRequest otpVerifyRequest;
    private GenerateOtpRequest generateOtpRequest;
    private CountDownTimer countDownTimer;
    private OtpGeneratedData otpGeneratedData;
    private String mobile, email, referenceId;
    private String otpToken, refId, userName;
    private boolean canResend = false;

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback =
            new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss();
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            };

    public static OtpVerifyFragment newInstance(Bundle args) {
        OtpVerifyFragment fragment = new OtpVerifyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public OtpVerifyContract.Presenter createPresenter() {
        return this.mPresenter;
    }

    public int getLayoutRes() {
        return R.layout.fragment_otp_verify;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        if (getActivity() != null) mContext = getActivity();
        otpVerifyRequest = new OtpVerifyRequest();
        generateOtpRequest = new GenerateOtpRequest();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            otpGeneratedData = new Gson().fromJson(bundle.getString(OTP_GENERATED_FROM_REGISTER), OtpGeneratedData.class);
            openedFromScreen = bundle.getInt(AppConstants.IntentKey.FROM_ACTIVITY);
            mobile = bundle.getString(AppConstants.IntentKey.MOBILE);
            email = bundle.getString(AppConstants.IntentKey.EMAIL);
            referenceId = bundle.getString(AppConstants.IntentKey.REFERENCE_ID);
            if (openedFromScreen == FOR_FORGOT_PASSWORD) {
                otpGeneratedData = new Gson().fromJson(bundle.getString(OTP_GENERATED_FROM_FORGOT_PASSWORD), OtpGeneratedData.class);
                otpToken = bundle.getString(OTP_TOKEN);
                refId = bundle.getString(REF_ID);
                canResend = bundle.getBoolean(CAN_RESEND);
                userName = bundle.getString(USER_NAME);
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (openedFromScreen == FOR_FORGOT_PASSWORD) {
            setUpOTP(otpToken, refId, canResend);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mContext = null;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(mContext, getLayoutRes(), null);
        mOtpVerifyBinding = DataBindingUtil.bind(contentView);
        setViewListener();
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)
                ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) contentView.getParent());
        ScreenUtils screenUtils = new ScreenUtils(mContext);
        mBehavior.setPeekHeight(screenUtils.getHeight());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        setTimer();
    }

    private void setViewListener() {
        mOtpVerifyBinding.edtOtpOne.addTextChangedListener(this);
        mOtpVerifyBinding.edtOtpTwo.addTextChangedListener(this);
        mOtpVerifyBinding.edtOtpThree.addTextChangedListener(this);
        mOtpVerifyBinding.edtOtpFour.addTextChangedListener(this);
        mOtpVerifyBinding.edtOtpFive.addTextChangedListener(this);
        mOtpVerifyBinding.edtOtpSix.addTextChangedListener(this);
        mOtpVerifyBinding.btnSubmit.setOnClickListener(this);
        mOtpVerifyBinding.tvResendOtp.setOnClickListener(this);
    }

    @NonNull
    protected BlurConfig blurConfig() {
        return new BlurConfig.Builder()
                .overlayColor(R.color.blur_bg_color)
                .asyncPolicy(SmartAsyncPolicyHolder.INSTANCE.smartAsyncPolicy())
                .debug(true)
                .build();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        mOtpVerifyBinding.txtError.setVisibility(View.GONE);
        if (charSequence.hashCode() == mOtpVerifyBinding.edtOtpOne.getText().hashCode()) {
            if (count == 1) {
                mOtpVerifyBinding.edtOtpTwo.requestFocus();
                mOtpVerifyBinding.edtOtpTwo.setFocusable(true);
            }
        } else if (charSequence.hashCode() == mOtpVerifyBinding.edtOtpTwo.getText().hashCode()) {
            if (count == 1) {
                mOtpVerifyBinding.edtOtpThree.requestFocus();
                mOtpVerifyBinding.edtOtpThree.setFocusable(true);
            } else if (count == 0) {
                mOtpVerifyBinding.edtOtpOne.requestFocus();
                mOtpVerifyBinding.edtOtpOne.setFocusable(true);
            }
        } else if (charSequence.hashCode() == mOtpVerifyBinding.edtOtpThree.getText().hashCode()) {
            if (count == 1) {
                mOtpVerifyBinding.edtOtpFour.requestFocus();
                mOtpVerifyBinding.edtOtpFour.setFocusable(true);
            } else if (count == 0) {
                mOtpVerifyBinding.edtOtpTwo.requestFocus();
                mOtpVerifyBinding.edtOtpTwo.setFocusable(true);
            }
        } else if (charSequence.hashCode() == mOtpVerifyBinding.edtOtpFour.getText().hashCode()) {
            if (count == 1) {
                mOtpVerifyBinding.edtOtpFive.requestFocus();
                mOtpVerifyBinding.edtOtpFive.setFocusable(true);
            } else if (count == 0) {
                mOtpVerifyBinding.edtOtpThree.requestFocus();
                mOtpVerifyBinding.edtOtpThree.setFocusable(true);
            }
        } else if (charSequence.hashCode() == mOtpVerifyBinding.edtOtpFive.getText().hashCode()) {
            if (count == 1) {
                mOtpVerifyBinding.edtOtpSix.requestFocus();
                mOtpVerifyBinding.edtOtpSix.setFocusable(true);
            } else if (count == 0) {
                mOtpVerifyBinding.edtOtpFour.requestFocus();
                mOtpVerifyBinding.edtOtpFour.setFocusable(true);
            }
        } else if (charSequence.hashCode() == mOtpVerifyBinding.edtOtpSix.getText().hashCode()) {
            if (count == 1) {
                mOtpVerifyBinding.edtOtpSix.requestFocus();
                mOtpVerifyBinding.edtOtpSix.setFocusable(true);
            } else if (count == 0) {
                mOtpVerifyBinding.edtOtpFive.requestFocus();
                mOtpVerifyBinding.edtOtpFive.setFocusable(true);
            }
        }
        if (validate(mOtpVerifyBinding.edtOtpOne.getText().toString(),
                mOtpVerifyBinding.edtOtpTwo.getText().toString(),
                mOtpVerifyBinding.edtOtpThree.getText().toString(),
                mOtpVerifyBinding.edtOtpFour.getText().toString(),
                mOtpVerifyBinding.edtOtpFive.getText().toString(),
                mOtpVerifyBinding.edtOtpSix.getText().toString()
        )) {
            ValidationUtil.setErrorMessage(mOtpVerifyBinding.txtError, "", false);
            mOtpVerifyBinding.btnSubmit.setBackgroundResource(R.drawable.bg_blue_button);
            otpVerifyRequest.setOtpToken(otpGeneratedData.getOtpToken());
            if (openedFromScreen == FOR_CONTACT_DETAILS) {
                otpVerifyRequest.setOtpType(REGISTRATION);
            } else {
                otpVerifyRequest.setOtpType(FORGOT_PASSWORD);
            }
            mPresenter.verifyOtp(otpVerifyRequest, userName);
        } else {
            mOtpVerifyBinding.txtError.setVisibility(View.GONE);
            mOtpVerifyBinding.btnSubmit.setBackgroundResource(R.drawable.disable_button);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                if (validate(mOtpVerifyBinding.edtOtpOne.getText().toString(),
                        mOtpVerifyBinding.edtOtpTwo.getText().toString(),
                        mOtpVerifyBinding.edtOtpThree.getText().toString(),
                        mOtpVerifyBinding.edtOtpFour.getText().toString(),
                        mOtpVerifyBinding.edtOtpFive.getText().toString(),
                        mOtpVerifyBinding.edtOtpSix.getText().toString())) {
                    otpVerifyRequest.setOtpToken(otpGeneratedData.getOtpToken());
                    if (openedFromScreen == FOR_CONTACT_DETAILS) {
                        otpVerifyRequest.setOtpType(REGISTRATION);
                    } else {
                        otpVerifyRequest.setOtpType(FORGOT_PASSWORD);
                    }
                    mPresenter.verifyOtp(otpVerifyRequest, userName);
                } else {
                    mOtpVerifyBinding.txtError.setVisibility(View.GONE);
                }
                break;
            case R.id.tvResendOtp:
                if (openedFromScreen == FOR_CONTACT_DETAILS) {
                    generateOtpRequest.setMobile(mobile);
                    generateOtpRequest.setReferenceId(referenceId);
                    generateOtpRequest.setRequestOtpFor(REGISTRATION);
                    generateOtpRequest.setUsername(email);
                    generateOtpRequest.setOtpToken(otpGeneratedData.getOtpToken());
                    mPresenter.generateOTP(generateOtpRequest);
                } else {
                    generateOtpRequest.setReferenceId(UUID.randomUUID().toString());
                    generateOtpRequest.setRequestOtpFor(FORGOT_PASSWORD);
                    generateOtpRequest.setUsername(userName);
                    mPresenter.generateOTP(generateOtpRequest);
                }
                break;
        }
    }

    private void clearOtpFields() {
        mOtpVerifyBinding.edtOtpOne.setText(null);
        mOtpVerifyBinding.edtOtpTwo.setText(null);
        mOtpVerifyBinding.edtOtpThree.setText(null);
        mOtpVerifyBinding.edtOtpFour.setText(null);
        mOtpVerifyBinding.edtOtpFive.setText(null);
        mOtpVerifyBinding.edtOtpSix.setText(null);
        mOtpVerifyBinding.edtOtpOne.clearFocus();
        mOtpVerifyBinding.edtOtpTwo.clearFocus();
        mOtpVerifyBinding.edtOtpThree.clearFocus();
        mOtpVerifyBinding.edtOtpFour.clearFocus();
        mOtpVerifyBinding.edtOtpFive.clearFocus();
        mOtpVerifyBinding.edtOtpSix.clearFocus();
        mOtpVerifyBinding.txtError.setVisibility(View.GONE);
    }

    private boolean validate(String otpTxtOne, String otpTxtTwo, String otpTxtThree, String otpTxtFour,
                             String otpTxtFive, String otpTxtSix) {
        boolean valid = true;
        if (ValidationUtil.isNullOrEmpty(otpTxtOne) || ValidationUtil.isNullOrEmpty(otpTxtTwo)
                || ValidationUtil.isNullOrEmpty(otpTxtThree) || ValidationUtil.isNullOrEmpty(otpTxtFour)
                || ValidationUtil.isNullOrEmpty(otpTxtFive) || ValidationUtil.isNullOrEmpty(otpTxtSix)) {
            valid = false;
        }
        otpVerifyRequest.setOtpValue(otpTxtOne + otpTxtTwo + otpTxtThree + otpTxtFour + otpTxtFive +
                otpTxtSix);
        return valid;
    }

    @Override
    public void openNextScreen(String sessionToken, String userName) {
        if (openedFromScreen == FOR_CONTACT_DETAILS && mContext instanceof BaseActivity) {
            dismiss();
            ((BaseActivity) mContext).moveActivity(mContext, CreatePasswordActivity.class, false,
                    false);
        } else if (openedFromScreen == FOR_FORGOT_PASSWORD && mContext instanceof BaseActivity) {
            dismiss();
            Bundle bundle = new Bundle();
            bundle.putInt(AppConstants.IntentKey.FROM_ACTIVITY, FOR_FORGOT_PASSWORD);
            bundle.putString(AppConstants.IntentKey.SESSION_TOKEN, sessionToken);
            bundle.putString(USER_NAME, userName);
            ((BaseActivity) mContext).moveActivity(mContext, CreatePasswordActivity.class, false,
                    false, bundle);
        }
    }

    @Override
    public void setUpOTP(String otpToken, String refID, boolean canResend) {
        otpGeneratedData.setOtpToken(otpToken);
        clearOtpFields();
        setTimer();
        if (canResend) {
            mOtpVerifyBinding.tvResendOtp.setClickable(true);
        } else {
            mOtpVerifyBinding.tvResendOtp.setClickable(false);
        }
    }

    @Override
    public void disableSubmitButton() {
        mOtpVerifyBinding.btnSubmit.setClickable(false);
        mOtpVerifyBinding.btnSubmit.setBackgroundResource(R.drawable.disable_button);
        mOtpVerifyBinding.edtOtpOne.setEnabled(false);
        mOtpVerifyBinding.edtOtpTwo.setEnabled(false);
        mOtpVerifyBinding.edtOtpThree.setEnabled(false);
        mOtpVerifyBinding.edtOtpFour.setEnabled(false);
        mOtpVerifyBinding.edtOtpFive.setEnabled(false);
        mOtpVerifyBinding.edtOtpSix.setEnabled(false);
    }

    @Override
    public void enableSubmitButton() {
        mOtpVerifyBinding.edtOtpOne.setEnabled(true);
        mOtpVerifyBinding.edtOtpTwo.setEnabled(true);
        mOtpVerifyBinding.edtOtpThree.setEnabled(true);
        mOtpVerifyBinding.edtOtpFour.setEnabled(true);
        mOtpVerifyBinding.edtOtpFive.setEnabled(true);
        mOtpVerifyBinding.edtOtpSix.setEnabled(true);
    }

    @SuppressLint("SetTextI18n")
    private void setTimer() {
        countDownTimer = new CountDownTimer(mPresenter.getOtpTime(), COUNTDOWN_INTERVAL_TIME) {
            public void onTick(long millisUntilFinished) {
                String timmerText = String.format(TWO_DIGIT, millisUntilFinished / COUNTDOWN_INTERVAL_TIME);
                if (getContext() != null) {
                    mOtpVerifyBinding.tvTimeRemaining.setText(getString(R.string.time_remaining) + SPACE + arabicToDecimal(timmerText) +
                            getString(R.string.short_form_seconds));
                    mOtpVerifyBinding.tvResendOtp.setTextColor(getResources().getColor(R.color.gray_light));
                    mOtpVerifyBinding.tvResendOtp.setClickable(false);
                }
            }

            public void onFinish() {
                if (getContext() != null) {
                    mOtpVerifyBinding.tvTimeRemaining.setText(getString(R.string.time_remaining) + SPACE + DEFAULT_TIMER +
                            getString(R.string.short_form_seconds));
                    mOtpVerifyBinding.tvResendOtp.setTextColor(getResources().getColor(R.color.border_color));
                    mOtpVerifyBinding.tvResendOtp.setClickable(true);
                    try {
                        countDownTimer.cancel();
                        countDownTimer = null;
                    } catch (Exception e) {
                        Timber.d("Exception : " + e.toString());
                    }

                }
            }
        }.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (countDownTimer != null) {
            mOtpVerifyBinding.tvTimeRemaining.setText(getString(R.string.time_remaining) + SPACE + DEFAULT_TIMER +
                    getString(R.string.short_form_seconds));
            mOtpVerifyBinding.tvResendOtp.setTextColor(getResources().getColor(R.color.border_color));
            mOtpVerifyBinding.tvResendOtp.setClickable(true);
            countDownTimer.onFinish();
        }
    }

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            //That gives all message to us.
            // We need to get the code from inside with regex
            String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);

        }
    }*/

    public void setOTPFromReceiver(String message) {
        String otp = Utilities.getOtpFromMessage(message);
        if (otp != null && otp.length() == 6) {
            mOtpVerifyBinding.edtOtpOne.setText(Character.toString(otp.charAt(0)));
            mOtpVerifyBinding.edtOtpTwo.setText(Character.toString(otp.charAt(1)));
            mOtpVerifyBinding.edtOtpThree.setText(Character.toString(otp.charAt(2)));
            mOtpVerifyBinding.edtOtpFour.setText(Character.toString(otp.charAt(3)));
            mOtpVerifyBinding.edtOtpFive.setText(Character.toString(otp.charAt(4)));
            mOtpVerifyBinding.edtOtpSix.setText(Character.toString(otp.charAt(5)));
        }
    }
}