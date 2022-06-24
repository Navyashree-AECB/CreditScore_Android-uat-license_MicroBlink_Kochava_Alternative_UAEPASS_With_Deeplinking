package com.aecb.ui.exit.view;

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
import com.aecb.databinding.FragmentExitBinding;
import com.aecb.presentation.blurcustomview.BlurConfig;
import com.aecb.presentation.blurcustomview.SmartAsyncPolicyHolder;
import com.aecb.ui.checkout.view.CheckoutActivity;
import com.aecb.ui.exit.presenter.ExitContract;
import com.aecb.ui.menu.view.MenuActivity;
import com.aecb.ui.purchasejourney.addcard.view.AddCardActivity;
import com.aecb.ui.purchasejourney.cardlist.view.CardListActivity;
import com.aecb.ui.verifyamountforpayment.view.VerifyAmountForPaymentActivity;
import com.aecb.util.ScreenUtils;
import com.aecb.util.Utilities;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import javax.inject.Inject;

import static com.aecb.AppConstants.ActivityIntentCode.API_FAILURE_SUCCESS;
import static com.aecb.AppConstants.ActivityIntentCode.AUTHORIZATION_FAILED;
import static com.aecb.AppConstants.ActivityIntentCode.FOR_LOGOUT;

public class ExitFragment extends BlurDialogBaseFragment<ExitContract.View,
        ExitContract.Presenter> implements ExitContract.View, View.OnClickListener {

    @Inject
    public ExitContract.Presenter mPresenter;

    private int fromActivity;
    private String btnText, negativeText, title, subTile;
    private boolean allowDescClick = false;
    private FragmentExitBinding mExitBinding;
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
    public static ExitFragment newInstance(Bundle bundle) {
        ExitFragment fragment = new ExitFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * get resource file of this fragment
     */
    public int getLayoutRes() {
        return R.layout.fragment_exit;
    }

    @Override
    public ExitContract.Presenter createPresenter() {
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
                title = bundle.getString(AppConstants.IntentKey.TITLE_TEXT, "");
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
            if (bundle.containsKey(AppConstants.IntentKey.DESC_CLICK)) {
                allowDescClick = bundle.getBoolean(AppConstants.IntentKey.DESC_CLICK, false);
            }
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), getLayoutRes(), null);
        mExitBinding = DataBindingUtil.bind(contentView);
        assert mExitBinding != null;
        mExitBinding.tvTitle.setText(title);
        mExitBinding.tvDesc.setText(subTile);
        mExitBinding.btnOk.setText(btnText);
        if (negativeText != null) {
            mExitBinding.btnNo.setVisibility(View.VISIBLE);
            mExitBinding.btnNo.setText(negativeText);
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
    }

    /**
     * set on view click listener
     */
    private void clickListeners() {
        mExitBinding.btnOk.setOnClickListener(this);
        mExitBinding.btnNo.setOnClickListener(this);
        mExitBinding.tvDesc.setOnClickListener(this);
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
                if (fromActivity == API_FAILURE_SUCCESS) {
                    dismiss();
                } else if (fromActivity == AUTHORIZATION_FAILED) {
                    dismiss();
                    ((CardListActivity) getContext()).refreshCardList();
                } else {
                    if (getContext() != null && getContext() instanceof AddCardActivity) {
                        ((AddCardActivity) getContext()).dialogBtnClick(fromActivity);
                    }
                    if (getContext() != null && getContext() instanceof CardListActivity) {
                        ((CardListActivity) getContext()).dialogBtnClick(true, fromActivity);
                    }
                    if (getContext() != null && getContext() instanceof CheckoutActivity) {
                        ((CheckoutActivity) getContext()).dialogBtnClick(fromActivity);
                    }
                    if (getContext() != null && getContext() instanceof VerifyAmountForPaymentActivity) {
                        ((VerifyAmountForPaymentActivity) getContext()).dialogBtnClick(fromActivity);
                    }
                    if (getContext() != null && getContext() instanceof MenuActivity) {
                        if (fromActivity == FOR_LOGOUT) {
                            ((MenuActivity) getContext()).mPresenter.logout();
                        } else {
                            ((MenuActivity) getContext()).changeLanguage(true);
                        }

                    }
                }
                break;
            case R.id.btnNo:
                if (getContext() != null && getContext() instanceof CardListActivity) {
                    ((CardListActivity) getContext()).dialogBtnClick(false, fromActivity);
                }
                if (getContext() != null && getContext() instanceof MenuActivity) {
                    if (fromActivity == FOR_LOGOUT) {
                        ((MenuActivity) getContext()).dialogDismiss();
                    } else {
                        ((MenuActivity) getContext()).changeLanguage(false);
                    }
                }
                break;
            case R.id.tvDesc:
                if (allowDescClick) {
                    Utilities.makeCall(getActivity(), "800287328");
                }
                break;
        }

    }

}