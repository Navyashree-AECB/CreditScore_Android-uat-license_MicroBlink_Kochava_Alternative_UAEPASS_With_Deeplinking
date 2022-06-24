package com.aecb.ui.verifyamountforpayment.view;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.base.mvp.BaseActivity;
import com.aecb.base.mvp.BlurDialogBaseFragment;
import com.aecb.data.api.models.BaseResponse;
import com.aecb.data.api.models.buycreditreport.BuyCreditReportResponse;
import com.aecb.data.api.models.emailrecipients.EmailRecipients;
import com.aecb.data.api.models.emailrecipients.EmailRecipientsRequest;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.data.api.models.payment_method.ActivateCardRequestBody;
import com.aecb.databinding.ActivityVerifyAmountForPaymentBinding;
import com.aecb.listeners.ToolbarListener;
import com.aecb.presentation.customviews.ToolbarView;
import com.aecb.ui.checkout.view.CheckoutActivity;
import com.aecb.ui.dashboard.view.DashboardActivity;
import com.aecb.ui.exit.view.ExitFragment;
import com.aecb.ui.verifyamountforpayment.presenter.VerifyAmountContract;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import timber.log.Timber;

import static com.aecb.AppConstants.ActivityIntentCode.SAVE_CARD_SUCCESSFULLY;
import static com.aecb.AppConstants.ApiParameter.CARD_ACTIVATE;
import static com.aecb.AppConstants.IntentKey.ADD_CARD_RESPONSE;
import static com.aecb.AppConstants.IntentKey.EMAIL_RECIPIENTS;
import static com.aecb.AppConstants.IntentKey.FROM_ADD_RECIPIENTS;
import static com.aecb.AppConstants.IntentKey.IS_DEFAULT_SELECTED;
import static com.aecb.AppConstants.IntentKey.PRODUCT_DETAILS;
import static com.aecb.AppConstants.IntentKey.SCREEN_FROM;
import static com.aecb.AppConstants.IntentKey.SELECTED_PRODUCT;
import static com.aecb.AppConstants.IntentKey.SHOW_SCORE_LOADING;
import static com.aecb.util.FirebaseLogging.cancelFromCardVerificationScreen;
import static com.aecb.util.varibles.StringConstants.CONST_PYMT;

public class VerifyAmountForPaymentActivity extends BaseActivity<VerifyAmountContract.View,
        VerifyAmountContract.Presenter> implements VerifyAmountContract.View, View.OnClickListener,
        TextWatcher {

    @Inject
    public VerifyAmountContract.Presenter mPresenter;
    public ArrayList<EmailRecipients> emailList = new ArrayList<>();
    ActivityVerifyAmountForPaymentBinding verifyAmountForPaymentBinding;
    BaseResponse baseResponse;
    BuyCreditReportResponse buyCreditReportResponse;
    String screenFrom;
    Handler handler;
    ProductsItem productsItem;
    private BlurDialogBaseFragment paymentStatusDialog;
    boolean isDefaultCard = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBinding();
        Timber.d("LocalLanguage==" + Locale.getDefault().getLanguage());
        mPresenter.getLastLoginUser();
    }

    private void enableBinding() {
        getBundleData();
        verifyAmountForPaymentBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        verifyAmountForPaymentBinding.btnContinue.setOnClickListener(this);
        verifyAmountForPaymentBinding.edtAmount.addTextChangedListener(this);
        verifyAmountForPaymentBinding.edtAmount.setKeyListener(DigitsKeyListener.getInstance(true, true));
    }

    private void getBundleData() {
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        if (bundle != null) {
            baseResponse = new Gson().fromJson(bundle.getString(ADD_CARD_RESPONSE), BaseResponse.class);
            //buyCreditReportResponse = new Gson().fromJson(bundle.getString(BUY_CREDIT_RESPONSE), BuyCreditReportResponse.class);
            screenFrom = bundle.getString(SCREEN_FROM);
            emailList = (ArrayList<EmailRecipients>) bundle.getSerializable(EMAIL_RECIPIENTS);
            productsItem = (ProductsItem) bundle.getSerializable(PRODUCT_DETAILS);
            isDefaultCard = bundle.getBoolean(IS_DEFAULT_SELECTED);
        }
    }

    @NonNull
    @Override
    public VerifyAmountContract.Presenter createPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_verify_amount_for_payment;
    }

    @Override
    public void initToolbar() {
        ToolbarView tb = findViewById(R.id.toolBar);
        tb.setCancelBtnListener(this::onBackPressed);
        tb.setBackBtnListener(this::onBackPressed);
        tb.setBtnVisibility(true, false);
        tb.setBackBtnListener(new ToolbarListener() {
            @Override
            public void backInvoke() {
                finish();
                cancelFromCardVerificationScreen();
            }
        });
        tb.setToolbarTitleVisibility(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnContinue:
                float enteredAmount = Float.parseFloat(verifyAmountForPaymentBinding.edtAmount.getText().toString());
                if (enteredAmount >= 1.0 && enteredAmount <= 5.0) {
                    ActivateCardRequestBody activateCardRequestBody = new ActivateCardRequestBody();
                    //float amount = Float.parseFloat(verifyAmountForPaymentBinding.edtAmount.getText().toString()) * 100;
                   // activateCardRequestBody.setAmount((int) amount);
                    activateCardRequestBody.setConsent(CONST_PYMT);
                    activateCardRequestBody.setOperationType(CARD_ACTIVATE);
                    activateCardRequestBody.setTransactionID(baseResponse.getTransactionID());
                    presenter.activateCard(activateCardRequestBody, baseResponse, buyCreditReportResponse, screenFrom,isDefaultCard);
                } else {
                    localValidationError(getString(R.string.error), getString(R.string.please_enter_valid_payement_amount));
                }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!verifyAmountForPaymentBinding.edtAmount.getText().toString().isEmpty()) {
            verifyAmountForPaymentBinding.btnContinue.setEnabled(true);
            verifyAmountForPaymentBinding.btnContinue.setBackground(getResources().getDrawable(R.drawable.bg_blue_button));
        } else {
            verifyAmountForPaymentBinding.btnContinue.setEnabled(false);
            verifyAmountForPaymentBinding.btnContinue.setBackground(getResources().getDrawable(R.drawable.btn_disable_state));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void cardSavedSuccessfully(String message) {
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.IntentKey.FROM_ACTIVITY,
                SAVE_CARD_SUCCESSFULLY);
        bundle.putString(AppConstants.IntentKey.TITLE_TEXT, getString(R.string.saved_card));
        bundle.putString(AppConstants.IntentKey.SUB_TITLE_TEXT, message);
        bundle.putString(AppConstants.IntentKey.POSITIVE_TEXT, getString(R.string.txt_ok));
        paymentStatusDialog = ExitFragment.newInstance(bundle);
        paymentStatusDialog.setCancelable(false);
        paymentStatusDialog.show(getSupportFragmentManager(), paymentStatusDialog.getTag());
    }

    @Override
    public void displayPaymentSuccessDialog(BuyCreditReportResponse buyCreditReportResponse) {
        this.buyCreditReportResponse = buyCreditReportResponse;
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.IntentKey.FROM_ACTIVITY,
                AppConstants.ActivityIntentCode.PAYMENT_SUCCESSFULLY_SAVED);
        bundle.putString(AppConstants.IntentKey.TITLE_TEXT, getString(R.string.payment_successfully));
        bundle.putString(AppConstants.IntentKey.SUB_TITLE_TEXT, getString(R.string.your_payment_has_been_successfully_processed));
        bundle.putString(AppConstants.IntentKey.POSITIVE_TEXT, getString(R.string.ok));
        paymentStatusDialog = ExitFragment.newInstance(bundle);
        paymentStatusDialog.setCancelable(false);
        paymentStatusDialog.show(getSupportFragmentManager(), paymentStatusDialog.getTag());
        callSendEmailApi();
        if (Objects.equals(screenFrom, FROM_ADD_RECIPIENTS))
            paymentSuccessAction();
    }

    private void paymentSuccessAction() {
        handler = new Handler();
        handler.postDelayed(() -> {
            openDashboardScreen();
        }, 3000);
    }

    public void openDashboardScreen() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(SHOW_SCORE_LOADING, true);
        moveActivity(this, DashboardActivity.class, true, true, bundle);
    }

    @Override
    public void goToCheckoutScreen() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(SELECTED_PRODUCT, productsItem);
        bundle.putSerializable(AppConstants.IntentKey.EMAIL_RECIPIENTS, emailList);
        moveActivity(this, CheckoutActivity.class, true, true, bundle);
    }

    @Override
    public void displayPaymentFailedDialog(BuyCreditReportResponse buyCreditReportResponse) {
        this.buyCreditReportResponse = buyCreditReportResponse;
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.IntentKey.FROM_ACTIVITY,
                AppConstants.ActivityIntentCode.PAYMENT_STATUS_FAILED);
        bundle.putString(AppConstants.IntentKey.TITLE_TEXT, getString(R.string.title_payment_failed));
        bundle.putString(AppConstants.IntentKey.SUB_TITLE_TEXT, getString(R.string.payment_failed_description_msg));
        bundle.putString(AppConstants.IntentKey.POSITIVE_TEXT, getString(R.string.txt_ok));
        paymentStatusDialog = ExitFragment.newInstance(bundle);
        paymentStatusDialog.setCancelable(false);
        paymentStatusDialog.show(getSupportFragmentManager(), paymentStatusDialog.getTag());
    }

    void callSendEmailApi() {
        EmailRecipientsRequest recipientsRequest = new EmailRecipientsRequest();
        recipientsRequest.setApplicationId(buyCreditReportResponse.getBuyCreditReportData().getPortalUserId());
        recipientsRequest.setAdditionalReceivers(emailList);
        if (emailList != null && !emailList.isEmpty())
            mPresenter.callSendAdditionalEmailsApi(recipientsRequest);
    }

    public void dialogBtnClick(int fromActivity) {
        if (paymentStatusDialog != null) {
            paymentStatusDialog.dismiss();
        }
        if (fromActivity == AppConstants.ActivityIntentCode.PAYMENT_STATUS_FAILED) {
            paymentFailedAction();
        } else if (fromActivity == AppConstants.ActivityIntentCode.PAYMENT_SUCCESSFULLY_SAVED) {
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
                openDashboardScreen();
            }
        } else if (fromActivity == SAVE_CARD_SUCCESSFULLY) {
            setResult(SAVE_CARD_SUCCESSFULLY);
            finish();
        }
    }

    private void paymentFailedAction() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(SELECTED_PRODUCT, productsItem);
        bundle.putSerializable(AppConstants.IntentKey.EMAIL_RECIPIENTS, emailList);
        moveActivity(this, CheckoutActivity.class, true, true, bundle);
    }
}