package com.aecb.ui.touchdialog.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;

import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.base.mvp.BlurDialogBaseFragment;
import com.aecb.databinding.FragmentTouchBinding;
import com.aecb.presentation.blurcustomview.BlurConfig;
import com.aecb.presentation.blurcustomview.SmartAsyncPolicyHolder;
import com.aecb.ui.menu.view.MenuActivity;
import com.aecb.ui.touchdialog.presenter.TouchContract;
import com.aecb.util.ScreenUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

import javax.inject.Inject;

public class TouchFragment extends BlurDialogBaseFragment<TouchContract.View,
        TouchContract.Presenter> implements TouchContract.View, View.OnClickListener {

    @Inject
    public TouchContract.Presenter mPresenter;

    private int fromActivity;
    private String btnText;
    private String negativeText;
    private String subTile;
    private FragmentTouchBinding touchBinding;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new
            BottomSheetBehavior.BottomSheetCallback() {
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


    /**
     * instantiate bottom_line new fragment
     *
     * @return fragment
     */
    public static TouchFragment newInstance(Bundle bundle) {
        TouchFragment fragment = new TouchFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * get resource file of this fragment
     */
    public int getLayoutRes() {
        return R.layout.fragment_touch;
    }

    @Override
    public TouchContract.Presenter createPresenter() {
        return mPresenter;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        getBundles();
    }

    private void getBundles() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            fromActivity = bundle.getInt(AppConstants.IntentKey.FROM_ACTIVITY);
            if (bundle.containsKey(AppConstants.IntentKey.TITLE_TEXT)) {
            }
            if (bundle.containsKey(AppConstants.IntentKey.POSITIVE_TEXT)) {
                btnText = bundle.getString(AppConstants.IntentKey.POSITIVE_TEXT, "");
            }
            if (bundle.containsKey(AppConstants.IntentKey.NEGATIVE_TEXT)) {
                negativeText = bundle.getString(AppConstants.IntentKey.NEGATIVE_TEXT, "");
            }
            if (bundle.containsKey(AppConstants.IntentKey.SUB_TITLE_TEXT)) {
                subTile = bundle.getString(AppConstants.IntentKey.SUB_TITLE_TEXT, "");
            }
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), getLayoutRes(), null);
        touchBinding = DataBindingUtil.bind(contentView);
        assert touchBinding != null;
        touchBinding.tvDesc.setText(subTile);
        touchBinding.btnOk.setText(btnText);
        if (negativeText != null) {
            touchBinding.btnNo.setVisibility(View.VISIBLE);
            touchBinding.btnNo.setText(negativeText);
        }
        setScreenDirection(dialog);
        clickListeners();
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) ((View)
                contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) contentView.getParent());
        ScreenUtils screenUtils = new ScreenUtils(getActivity());
        mBehavior.setPeekHeight(screenUtils.getHeight());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        editFocusListeners();
    }

    private void editFocusListeners() {
        touchBinding.edtPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                touchBinding.txtInputLoutPassword.setHint(getString(R.string.password));
            } else if (!touchBinding.edtPassword.getText().toString().isEmpty()) {
                touchBinding.txtInputLoutPassword.setHint(getString(R.string.password));
            } else {
                touchBinding.txtInputLoutPassword.setHint(getString(R.string.enter_new_password));
            }
        });
    }

    /**
     * set on view click listener
     */
    private void clickListeners() {
        touchBinding.btnOk.setOnClickListener(this);
        touchBinding.btnNo.setOnClickListener(this);
    }

    /**
     * this method use for set dialog layout direction(LTR and RTL)
     *
     * @param dialog
     */
    public void setScreenDirection(Dialog dialog) {

    }

    /**
     * set bottomsheet background blur
     *
     * @return BlurConfig
     */
    @NonNull
    protected BlurConfig blurConfig() {
        return new BlurConfig.Builder().overlayColor(R.color.blur_bg_color)
                .asyncPolicy(SmartAsyncPolicyHolder.INSTANCE.smartAsyncPolicy())
                .debug(true).build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                if (getContext() != null && getContext() instanceof MenuActivity) {
                    mPresenter.checkPasswordValidation(touchBinding.edtPassword.getText().toString());
                }
                break;
            case R.id.btnNo:
                if (getContext() != null && getContext() instanceof MenuActivity) {
                    ((MenuActivity) getContext()).updateTouchEnable(false, fromActivity, touchBinding.edtPassword.getText().toString());
                }
                break;
        }

    }

    @Override
    public void callLoginApi(String password) {
        ((MenuActivity) Objects.requireNonNull(getContext())).updateTouchEnable(true, fromActivity, password);
    }

    @Override
    public void showEmptyPasswordError() {
        localValidationError(getString(R.string.error), getString(R.string.empty_password_error));
        touchBinding.edtPassword.requestFocus();
    }

    @Override
    public void showPasswordLengthError() {
        localValidationError(getString(R.string.error), getString(R.string.please_enter_a_valid_password));
        touchBinding.edtPassword.requestFocus();
    }

    @Override
    public void showInvalidPassword() {
        localValidationError(getString(R.string.error), getString(R.string.invalid_password_error_msgs));
        touchBinding.edtPassword.requestFocus();
    }
}