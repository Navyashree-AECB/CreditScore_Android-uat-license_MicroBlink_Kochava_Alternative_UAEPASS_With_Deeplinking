package com.aecb.ui.changepassword.view;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.aecb.R;
import com.aecb.base.mvp.BaseActivity;
import com.aecb.databinding.ActivityChangePasswordBinding;
import com.aecb.ui.changepassword.presenter.ChangePasswordContract;
import com.aecb.ui.loginflow.login.view.LoginActivity;

import javax.inject.Inject;

import static com.aecb.util.FirebaseLogging.cancelChangePassword;
import static com.aecb.util.Utilities.isStringContainsSpecialCharacter;
import static com.aecb.util.Utilities.isTextContainLowerCase;
import static com.aecb.util.Utilities.isTextContainNumber;
import static com.aecb.util.Utilities.isTextContainUpperCase;

public class ChangePasswordActivity extends BaseActivity<ChangePasswordContract.View,
        ChangePasswordContract.Presenter> implements ChangePasswordContract.View, View.OnClickListener,
        TextWatcher {

    @Inject
    ChangePasswordContract.Presenter mPresenter;
    ActivityChangePasswordBinding changePasswordBinding;
    private Handler handler = new Handler();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_change_password:
                cancelChangePassword();
                finish();
                break;
            case R.id.btnChangePassword:
                mPresenter.changePassword(changePasswordBinding.edtCurrentPassword.getText().toString(),
                        changePasswordBinding.edtPassword.getText().toString(),
                        changePasswordBinding.edtConfirmPassword.getText().toString());
                break;
            case R.id.tvCancelChangePass:
                finish();
                break;
        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_change_password;
    }

    @Override
    public void initToolbar() {

    }

    @NonNull
    @Override
    public ChangePasswordContract.Presenter createPresenter() {
        return mPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBinding();
    }

    public void showSnackbar(String message) {
        localValidationError(getString(R.string.error), message);
    }

    private void enableBinding() {
        changePasswordBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        changePasswordBinding.btnChangePassword.setOnClickListener(this);
        changePasswordBinding.edtCurrentPassword.setOnClickListener(this);
        changePasswordBinding.backChangePassword.setOnClickListener(this);
        changePasswordBinding.tvCancelChangePass.setOnClickListener(this);
        changePasswordBinding.edtPassword.addTextChangedListener(this);
        changePasswordBinding.edtConfirmPassword.setLongClickable(false);
        changePasswordBinding.edtConfirmPassword.addTextChangedListener(this);
        editTextFocusListeners();
    }

    void editTextFocusListeners() {
        changePasswordBinding.edtPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                changePasswordBinding.txtInputLoutPassword.setHint(getString(R.string.hint_new_password));
            } else if (!changePasswordBinding.edtPassword.getText().toString().isEmpty()) {
                changePasswordBinding.txtInputLoutPassword.setHint(getString(R.string.hint_new_password));
            } else {
                changePasswordBinding.txtInputLoutPassword.setHint(getString(R.string.enter_password));
            }
        });
        changePasswordBinding.edtConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                changePasswordBinding.txtInputLoutConfirmPassword.setHint(getString(R.string.hint_confirm_password));
            } else if (!changePasswordBinding.edtConfirmPassword.getText().toString().isEmpty()) {
                changePasswordBinding.txtInputLoutConfirmPassword.setHint(getString(R.string.hint_confirm_password));
            } else {
                changePasswordBinding.txtInputLoutConfirmPassword.setHint(getString(R.string.re_enter_new_password));
            }
        });
        changePasswordBinding.edtCurrentPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                changePasswordBinding.txtInputLoutCurrentPassword.setHint(getString(R.string.current_password));
            } else if (!changePasswordBinding.edtCurrentPassword.getText().toString().isEmpty()) {
                changePasswordBinding.txtInputLoutCurrentPassword.setHint(getString(R.string.current_password));
            } else {
                changePasswordBinding.txtInputLoutCurrentPassword.setHint(getString(R.string.enter_current_password));
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!changePasswordBinding.edtCurrentPassword.getText().toString().isEmpty() &&
                !changePasswordBinding.edtPassword.getText().toString().isEmpty() &&
                !changePasswordBinding.edtConfirmPassword.getText().toString().isEmpty()) {
            changePasswordBinding.btnChangePassword.setEnabled(true);
            changePasswordBinding.btnChangePassword.setBackground(getResources().getDrawable(R.drawable.bg_blue_button));
        } else {
            changePasswordBinding.btnChangePassword.setEnabled(false);
            changePasswordBinding.btnChangePassword.setBackground(getResources().getDrawable(R.drawable.btn_disable_state));
        }
        passwordValidation();
    }

    private void passwordValidation() {
        if (changePasswordBinding.edtPassword.length() >= 8) {
            changePasswordBinding.tvPwdValidationOne.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password_green, 0, 0, 0);
            changePasswordBinding.tvPwdValidationOne.setTypeface(changePasswordBinding.tvPwdValidationTwo.getTypeface(),
                    Typeface.BOLD);
        } else {
            changePasswordBinding.tvPwdValidationOne.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password_grey, 0, 0, 0);
            changePasswordBinding.tvPwdValidationOne.setTypeface(null,
                    Typeface.NORMAL);
        }

        if (isTextContainUpperCase(changePasswordBinding.edtPassword.getText().toString())) {
            changePasswordBinding.tvPwdValidationTwo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password_green, 0, 0, 0);
            changePasswordBinding.tvPwdValidationTwo.setTypeface(changePasswordBinding.tvPwdValidationTwo.getTypeface(),
                    Typeface.BOLD);
        } else {
            changePasswordBinding.tvPwdValidationTwo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password_grey, 0, 0, 0);
            changePasswordBinding.tvPwdValidationTwo.setTypeface(null,
                    Typeface.NORMAL);
        }

        if (isTextContainLowerCase(changePasswordBinding.edtPassword.getText().toString())) {
            changePasswordBinding.tvPwdValidationThree.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password_green, 0, 0, 0);
            changePasswordBinding.tvPwdValidationThree.setTypeface(changePasswordBinding.tvPwdValidationTwo.getTypeface(),
                    Typeface.BOLD);
        } else {
            changePasswordBinding.tvPwdValidationThree.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password_grey, 0, 0, 0);
            changePasswordBinding.tvPwdValidationThree.setTypeface(null,
                    Typeface.NORMAL);
        }

        if (isStringContainsSpecialCharacter(changePasswordBinding.edtPassword.getText().toString())) {
            changePasswordBinding.tvPwdValidationFour.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password_green, 0, 0, 0);
            changePasswordBinding.tvPwdValidationFour.setTypeface(changePasswordBinding.tvPwdValidationTwo.getTypeface(),
                    Typeface.BOLD);
        } else {
            changePasswordBinding.tvPwdValidationFour.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password_grey, 0, 0, 0);
            changePasswordBinding.tvPwdValidationFour.setTypeface(null,
                    Typeface.NORMAL);
        }

        if (isTextContainNumber(changePasswordBinding.edtPassword.getText().toString())) {
            changePasswordBinding.tvPwdValidationFive.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password_green, 0, 0, 0);
            changePasswordBinding.tvPwdValidationFive.setTypeface(changePasswordBinding.tvPwdValidationFive.getTypeface(),
                    Typeface.BOLD);
        } else {
            changePasswordBinding.tvPwdValidationFive.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password_grey, 0, 0, 0);
            changePasswordBinding.tvPwdValidationFive.setTypeface(null,
                    Typeface.NORMAL);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void showErrorMSg(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showEmptyPasswordError() {
        showSnackbar(getString(R.string.please_enter_password));
    }

    @Override
    public void showInvalidPasswordError() {
        showSnackbar(getString(R.string.invalid_password_error_msgs));
    }

    @Override
    public void showEmptyConfirmPasswordError() {
        showSnackbar(getString(R.string.please_enter_confirm_password));
    }

    @Override
    public void showPasswordNotMatchedError() {
        showSnackbar(getString(R.string.password_does_not_match));
    }

    @Override
    public void showCurrentPasswordError() {
        showSnackbar(getString(R.string.please_enter_current_password));
    }

    @Override
    public void showInvalidCurrentPasswordError() {
        showSnackbar(getString(R.string.invalid_password_error_msgs));
    }

    @Override
    public void showPasswordSizeError() {
        showSnackbar(getString(R.string.password_length_should_be_between_8_to_20_char));
    }

    @Override
    public void openLogin(String message, String status, String useCase) {
        showApiSuccessMessage(message, status, useCase);
        handler.postDelayed(() -> moveActivity(this, LoginActivity.class, true, true), 3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

}