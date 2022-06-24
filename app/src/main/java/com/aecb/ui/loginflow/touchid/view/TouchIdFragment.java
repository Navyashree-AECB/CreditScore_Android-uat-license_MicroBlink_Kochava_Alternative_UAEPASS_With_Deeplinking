package com.aecb.ui.loginflow.touchid.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;

import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.base.mvp.BlurDialogBaseFragment;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.databinding.FragmentTouchIdBinding;
import com.aecb.presentation.biometric.BiometricUtils;
import com.aecb.presentation.blurcustomview.BlurConfig;
import com.aecb.presentation.blurcustomview.SmartAsyncPolicyHolder;
import com.aecb.ui.dashboard.view.DashboardActivity;
import com.aecb.ui.loginflow.login.view.LoginActivity;
import com.aecb.ui.loginflow.touchid.presenter.TouchIdContract;
import com.aecb.ui.registerflow.createpassword.view.CreatePasswordActivity;
import com.aecb.ui.startsup.view.StartupActivity;
import com.aecb.util.ScreenUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import javax.inject.Inject;

import static com.aecb.AppConstants.IntentKey.LAST_LOGIN_USER;

public class TouchIdFragment extends BlurDialogBaseFragment<TouchIdContract.View,
        TouchIdContract.Presenter> implements TouchIdContract.View, View.OnClickListener {

    @Inject
    public TouchIdContract.Presenter mPresenter;
    private FragmentTouchIdBinding touchIdBinding;
    private DBUserTC dbUser;
    private String password = "";
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

    public static TouchIdFragment newInstance(Bundle bundle) {
        TouchIdFragment fragment = new TouchIdFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_touch_id;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnContinue:
                if (biometricSetUp()) {
                    mPresenter.setTouchLoggedIn(true, password);
                } else {
                    Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                    startActivity(intent);
                }
                break;
            case R.id.tvCancel:
                if (getContext() != null && getContext() instanceof LoginActivity) {
                    dismiss();
                    //mPresenter.setTouchLoggedIn(false, password);
                } else if (getContext() != null && getContext() instanceof CreatePasswordActivity) {
                    dismiss();
                    //mPresenter.setTouchLoggedIn(false, password);
                } else if (getContext() != null && getContext() instanceof StartupActivity) {
                    dismiss();
                    //mPresenter.setTouchLoggedIn(false, password);
                }
                break;
            case R.id.tvSkip:
                if (getContext() != null && getContext() instanceof CreatePasswordActivity) {
                    mPresenter.setTouchLoggedIn(false, password);
                }
                break;

        }
    }

    public boolean biometricSetUp() {
        boolean check = false;
        if (BiometricUtils.isSdkVersionSupported()) {
            if (BiometricUtils.isPermissionGranted(getActivity())) {
                if (BiometricUtils.isHardwareSupported(getActivity())) {
                    if (BiometricUtils.isFingerprintAvailable(getActivity())) {
                        check = true;
                    }
                }
            }
        }
        return check;
    }

    @Override
    public TouchIdContract.Presenter createPresenter() {
        return this.mPresenter;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if (bundle.getString(AppConstants.IntentKey.PASSWORD) != null) {
                password = bundle.getString(AppConstants.IntentKey.PASSWORD);
            } else if (bundle.getString(LAST_LOGIN_USER) != null) {
                dbUser = new Gson().fromJson(bundle.getString(LAST_LOGIN_USER), DBUserTC.class);
            }
        }
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), getLayoutRes(), null);
        touchIdBinding = DataBindingUtil.bind(contentView);
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)
                ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) contentView.getParent());
        ScreenUtils screenUtils = new ScreenUtils(getActivity());
        mBehavior.setPeekHeight(screenUtils.getHeight());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        touchIdBinding.btnContinue.setOnClickListener(this);
        touchIdBinding.tvCancel.setOnClickListener(this);
        touchIdBinding.tvSkip.setOnClickListener(this);
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        initialUISetup();
    }

    private void initialUISetup() {
        if (getContext() != null && getContext() instanceof StartupActivity || getContext() != null && getContext() instanceof LoginActivity) {
            touchIdBinding.txtTitle.setText(getString(R.string.enable_touch_id));
            touchIdBinding.btnContinue.setVisibility(View.GONE);
            touchIdBinding.tvCancel.setVisibility(View.VISIBLE);
            touchIdBinding.tvSkip.setVisibility(View.GONE);
            if (getContext() instanceof CreatePasswordActivity){
                touchIdBinding.btnContinue.setText(R.string.txt_setup_fingerprint);
                touchIdBinding.txtTitle.setText(R.string.create_touch_id);
            }

            if (BiometricUtils.isBiometricPromptEnabled()) {
                if (getContext() instanceof StartupActivity) {
                    ((StartupActivity) getContext()).displayBiometricPrompt();
                } else if (getContext() instanceof LoginActivity) {
                    ((LoginActivity) getContext()).displayBiometricPrompt();
                }
            } else {
                if (getContext() instanceof StartupActivity) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ((StartupActivity) getContext()).biometricSetUp();
                    }
                } else if (getContext() instanceof LoginActivity) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ((LoginActivity) getContext()).biometricSetUp();
                    }
                }
            }
        } else if (getContext() != null && getContext() instanceof CreatePasswordActivity) {
            touchIdBinding.txtTitle.setText(getString(R.string.create_touch_id));
        }
    }

    @NonNull
    protected BlurConfig blurConfig() {
        return new BlurConfig.Builder().overlayColor(R.color.blur_bg_color)
                .asyncPolicy(SmartAsyncPolicyHolder.INSTANCE.smartAsyncPolicy())
                .debug(true).build();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getContext() != null && getContext() instanceof LoginActivity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ((LoginActivity) getContext()).cancelBiometricClick();
            }
        } else if (getContext() != null && getContext() instanceof StartupActivity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ((StartupActivity) getContext()).cancelBiometricClick();
            }
        }
    }

    @Override
    public void openDashboard(boolean enable) {
        if (getContext() != null && getContext() instanceof CreatePasswordActivity) {
            ((CreatePasswordActivity) getContext()).moveActivity(getActivity(), DashboardActivity.class, true, true);
        }
    }
}