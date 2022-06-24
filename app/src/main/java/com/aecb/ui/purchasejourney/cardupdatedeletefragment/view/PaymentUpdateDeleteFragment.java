package com.aecb.ui.purchasejourney.cardupdatedeletefragment.view;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.aecb.R;
import com.aecb.base.mvp.BlurDialogBaseFragment;
import com.aecb.databinding.FragmentCardUpdateDeleteBinding;
import com.aecb.presentation.blurcustomview.BlurConfig;
import com.aecb.presentation.blurcustomview.SmartAsyncPolicyHolder;
import com.aecb.ui.purchasejourney.cardlist.view.CardListActivity;
import com.aecb.ui.purchasejourney.cardupdatedeletefragment.presenter.PaymentUpdateDeleteContract;
import com.aecb.util.ScreenUtils;
import com.aecb.util.varibles.StringConstants;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import javax.inject.Inject;

import static com.aecb.AppConstants.ActivityIntentCode.MAKE_DEFAULT_CARD;
import static com.aecb.AppConstants.ActivityIntentCode.MAKE_EDIRHAM_DEFAULT;
import static com.aecb.AppConstants.ActivityIntentCode.UPDATE_DELETE_CARD;
import static com.aecb.AppConstants.IntentKey.IS_CARD_DEFAULT;
import static com.aecb.AppConstants.IntentKey.IS_EDIRHAM_DEFAULT;
import static com.aecb.util.varibles.StringConstants.CONST_YES;

public class PaymentUpdateDeleteFragment extends BlurDialogBaseFragment<PaymentUpdateDeleteContract.View,
        PaymentUpdateDeleteContract.Presenter> implements PaymentUpdateDeleteContract.View, View.OnClickListener {

    @Inject
    public PaymentUpdateDeleteContract.Presenter mPresenter;
    boolean isEDirhamDefault = false;

    FragmentCardUpdateDeleteBinding cardUpdateDeleteBinding;
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

    public static PaymentUpdateDeleteFragment newInstance(Bundle bundle) {
        PaymentUpdateDeleteFragment fragment = new PaymentUpdateDeleteFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_card_update_delete;
    }

    @Override
    public PaymentUpdateDeleteContract.Presenter createPresenter() {
        return mPresenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.SheetDialog);

    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), getLayoutRes(), null);
        cardUpdateDeleteBinding = DataBindingUtil.bind(contentView);
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onClickListeners();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if (bundle.getString(IS_CARD_DEFAULT, StringConstants.CONST_NO).equalsIgnoreCase(CONST_YES)) {
                cardUpdateDeleteBinding.btnMakeDefault.setVisibility(View.GONE);
                cardUpdateDeleteBinding.btnDelete.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.plain_ic_platter));
            }
            if (bundle.containsKey(IS_EDIRHAM_DEFAULT)) {
                isEDirhamDefault = bundle.getBoolean(IS_EDIRHAM_DEFAULT);
                if (bundle.getBoolean(IS_EDIRHAM_DEFAULT)) {
                    cardUpdateDeleteBinding.btnMakeDefault.setVisibility(View.VISIBLE);
                    cardUpdateDeleteBinding.btnDelete.setVisibility(View.GONE);
                }
            }
        }
    }

    private void onClickListeners() {
        cardUpdateDeleteBinding.btnCancel.setOnClickListener(this);
        cardUpdateDeleteBinding.btnDelete.setOnClickListener(this);
        cardUpdateDeleteBinding.btnMakeDefault.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                dismiss();
                break;
            case R.id.btnMakeDefault:
                dismiss();
                if (isEDirhamDefault) {
                    ((CardListActivity) getActivity()).showDeleteConfirmationMessage(MAKE_EDIRHAM_DEFAULT,
                            getString(R.string.txt_default), getString(R.string.default_card_msg),
                            getString(R.string.txt_yes), getString(R.string.txt_no));
                } else {
                    ((CardListActivity) getActivity()).showDeleteConfirmationMessage(MAKE_DEFAULT_CARD,
                            getString(R.string.txt_default), getString(R.string.default_card_msg),
                            getString(R.string.txt_yes), getString(R.string.txt_no));
                }
                break;
            case R.id.btnDelete:
                dismiss();
                ((CardListActivity) getActivity()).showDeleteConfirmationMessage(UPDATE_DELETE_CARD,
                        getString(R.string.txt_delete),
                        getString(R.string.delete_card_msg), getString(R.string.txt_yes), getString(R.string.txt_no));
                break;
        }
    }

    @NonNull
    protected BlurConfig blurConfig() {
        return new BlurConfig.Builder().overlayColor(R.color.blur_bg_color)
                .asyncPolicy(SmartAsyncPolicyHolder.INSTANCE.smartAsyncPolicy())
                .debug(true).build();
    }

}