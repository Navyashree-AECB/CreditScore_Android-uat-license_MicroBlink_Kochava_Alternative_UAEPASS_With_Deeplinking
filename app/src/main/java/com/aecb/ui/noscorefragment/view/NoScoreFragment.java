package com.aecb.ui.noscorefragment.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;

import com.aecb.R;
import com.aecb.base.mvp.BlurDialogBaseFragment;
import com.aecb.databinding.FragmentNoScoreBindingImpl;
import com.aecb.presentation.blurcustomview.BlurConfig;
import com.aecb.presentation.blurcustomview.SmartAsyncPolicyHolder;
import com.aecb.ui.dashboard.view.DashboardActivity;
import com.aecb.ui.noscorefragment.presenter.NoScoreContract;
import com.aecb.util.ScreenUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

import javax.inject.Inject;

import static com.aecb.AppConstants.ActivityIntentCode.FOR_NO_SCORE_CONTACT_US;
import static com.aecb.AppConstants.ActivityIntentCode.FOR_NO_SCORE_FAQ;

public class NoScoreFragment extends BlurDialogBaseFragment<NoScoreContract.View, NoScoreContract.Presenter>
        implements NoScoreContract.View, View.OnClickListener {

    @Inject
    NoScoreContract.Presenter mPresenter;
    FragmentNoScoreBindingImpl noScoreBinding;
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

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_no_score;
    }

    @Override
    public NoScoreContract.Presenter createPresenter() {
        return mPresenter;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), getLayoutRes(), null);
        noScoreBinding = DataBindingUtil.bind(contentView);
        clickListeners();
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)
                ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) contentView.getParent());
        ScreenUtils screenUtils = new ScreenUtils(getActivity());
        mBehavior.setPeekHeight(screenUtils.getHeight());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
        }
    }

    @NonNull
    protected BlurConfig blurConfig() {
        return new BlurConfig.Builder().overlayColor(R.color.blur_bg_color)
                .asyncPolicy(SmartAsyncPolicyHolder.INSTANCE.smartAsyncPolicy())
                .debug(true).build();
    }


    private void clickListeners() {
        noScoreBinding.btnFAQ.setOnClickListener(this);
        noScoreBinding.btnContactUs.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFAQ:
                dismiss();
                ((DashboardActivity) Objects.requireNonNull(getActivity())).openHelpSupport(FOR_NO_SCORE_FAQ);
                break;
            case R.id.btnContactUs:
                dismiss();
                ((DashboardActivity) Objects.requireNonNull(getActivity())).openHelpSupport(FOR_NO_SCORE_CONTACT_US);
                break;
        }
    }
}