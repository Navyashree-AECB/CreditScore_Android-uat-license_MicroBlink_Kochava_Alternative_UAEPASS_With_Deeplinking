package com.aecb.ui.addcvv.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;

import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.base.mvp.BlurDialogBaseFragment;
import com.aecb.data.api.models.cards.CreditCardData;
import com.aecb.databinding.FragmentAddCvvBinding;
import com.aecb.presentation.blurcustomview.BlurConfig;
import com.aecb.presentation.blurcustomview.SmartAsyncPolicyHolder;
import com.aecb.ui.addcvv.presenter.AddCvvContract;
import com.aecb.ui.purchasejourney.cardlist.view.CardListActivity;
import com.aecb.util.ScreenUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import javax.inject.Inject;

import static com.aecb.AppConstants.AddCardOperationType.PAYMENT_CONFIG_OLD_CARD_PURCHASE;
import static com.aecb.AppConstants.AddCardOperationType.PAYMENT_CONFIG_OLD_CARD_SETTINGS;
import static com.aecb.AppConstants.IntentKey.FROM_ACTIVITY;
import static com.aecb.AppConstants.IntentKey.FROM_ADD_RECIPIENTS;
import static com.aecb.AppConstants.IntentKey.SELECTED_NI_CARD;
import static com.aecb.util.varibles.StringConstants.CONST_NO;
import static com.aecb.util.varibles.StringConstants.CONST_PYMT;


public class AddCvvFragment extends BlurDialogBaseFragment<AddCvvContract.View,
        AddCvvContract.Presenter> implements AddCvvContract.View, View.OnClickListener, TextWatcher {

    @Inject
    public AddCvvContract.Presenter mPresenter;
    FragmentAddCvvBinding addCvvBinding;
    Bundle bundle;
    String screenFrom;
    private CreditCardData selectedCard;
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

    public static AddCvvFragment newInstance(Bundle bundle) {
        AddCvvFragment fragment = new AddCvvFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        getBundle();
    }

    private void getBundle() {
        bundle = this.getArguments();
        if (bundle != null) {
            screenFrom = bundle.getString(FROM_ACTIVITY);
            selectedCard = new Gson().fromJson(bundle.getString(SELECTED_NI_CARD), CreditCardData.class);
        }
    }

    @NonNull
    protected BlurConfig blurConfig() {
        return new BlurConfig.Builder().overlayColor(R.color.blur_bg_color)
                .asyncPolicy(SmartAsyncPolicyHolder.INSTANCE.smartAsyncPolicy())
                .debug(true).build();
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), getLayoutRes(), null);
        addCvvBinding = DataBindingUtil.bind(contentView);
        dialog.setContentView(contentView);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(contentView.getWindowToken(), 0);
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
        enableBinding();
    }

    private void enableBinding() {
        addCvvBinding.btnProceed.setOnClickListener(this);
        addCvvBinding.edtCVV.addTextChangedListener(this);
    }


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_add_cvv;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnProceed) {
            if (addCvvBinding.edtCVV.length() < 3) {
                localValidationError(getString(R.string.error), getString(R.string.txt_msg_cvv_min_length));
            } else {
                callAddCardApi(selectedCard);
            }
        }
    }

    private void callAddCardApi(CreditCardData selectedCard) {
        String cardNumber = selectedCard.getCardnumber().replaceAll("x", "*");
        String expiryMonth = selectedCard.getExpiryDate().substring(0, 2);
        String expiryYear = selectedCard.getExpiryDate().substring(selectedCard.getExpiryDate().length() - 4);
        String expiryDate = expiryYear + "-" + expiryMonth;
        String paymentType = PAYMENT_CONFIG_OLD_CARD_SETTINGS;
        String isDefault = CONST_PYMT;
        if (screenFrom.equals(FROM_ADD_RECIPIENTS)) {
            paymentType = PAYMENT_CONFIG_OLD_CARD_PURCHASE;
            isDefault = CONST_NO;
        }
        mPresenter.addCard(cardNumber, expiryDate, addCvvBinding.edtCVV.getText().toString(),
                selectedCard.getCardholdername(), paymentType, screenFrom, isDefault);
    }

    @Override
    public AddCvvContract.Presenter createPresenter() {
        return mPresenter;
    }

    @Override
    public void openADCBWebview(String htmlBodyContent, String transactionID) {
        dismiss();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.IntentKey.ADCB_WEBVIEW_HTML, htmlBodyContent);
        ((CardListActivity) getContext()).openAdcbWebview(bundle, transactionID);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!addCvvBinding.edtCVV.getText().toString().isEmpty()) {
            addCvvBinding.btnProceed.setEnabled(true);
            addCvvBinding.btnProceed.setBackground(getResources().getDrawable(R.drawable.bg_blue_button));
        } else {
            addCvvBinding.btnProceed.setEnabled(false);
            addCvvBinding.btnProceed.setBackground(getResources().getDrawable(R.drawable.btn_disable_state));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}