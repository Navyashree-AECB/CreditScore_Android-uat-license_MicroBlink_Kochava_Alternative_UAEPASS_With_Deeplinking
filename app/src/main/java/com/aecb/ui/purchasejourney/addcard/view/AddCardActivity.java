package com.aecb.ui.purchasejourney.addcard.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.base.mvp.BaseActivity;
import com.aecb.base.mvp.BlurDialogBaseFragment;
import com.aecb.data.api.models.buycreditreport.BuyCreditReportResponse;
import com.aecb.data.api.models.cards.CreditCardData;
import com.aecb.data.api.models.emailrecipients.EmailRecipients;
import com.aecb.data.api.models.emailrecipients.EmailRecipientsRequest;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.data.api.models.payment_method.ActivateCardRequestBody;
import com.aecb.data.api.models.payment_method.AddCardResponse;
import com.aecb.databinding.ActivityAddCardBinding;
import com.aecb.presentation.customviews.ToolbarView;
import com.aecb.presentation.monthpicker.MonthPicker;
import com.aecb.ui.adcbwebview.view.AdcbCardWebviewActivity;
import com.aecb.ui.checkout.view.CheckoutActivity;
import com.aecb.ui.dashboard.view.DashboardActivity;
import com.aecb.ui.exit.view.ExitFragment;
import com.aecb.ui.purchasejourney.addcard.presenter.AddCardContract;
import com.aecb.ui.termsandconditions.view.TermsAndConditionsFragment;
import com.aecb.ui.verifyamountforpayment.view.VerifyAmountForPaymentActivity;
import com.aecb.util.varibles.StringConstants;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import timber.log.Timber;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;
import static com.aecb.AppConstants.ActivityIntentCode.ADCB_CARD_REQUEST;
import static com.aecb.AppConstants.ActivityIntentCode.FROM_TO_ADDCARD_SCREEN;
import static com.aecb.AppConstants.ActivityIntentCode.MY_SCAN_REQUEST_CODE;
import static com.aecb.AppConstants.ActivityIntentCode.SAVE_CARD_SUCCESSFULLY;
import static com.aecb.AppConstants.AddCardOperationType.PAYMENT_CONFIG_NEW_CARD;
import static com.aecb.AppConstants.ApiParameter.CARD_ACTIVATE;
import static com.aecb.AppConstants.DIVISION;
import static com.aecb.AppConstants.IntentKey.ADD_CARD_RESPONSE;
import static com.aecb.AppConstants.IntentKey.BUY_CREDIT_RESPONSE;
import static com.aecb.AppConstants.IntentKey.EMAIL_RECIPIENTS;
import static com.aecb.AppConstants.IntentKey.FROM_ACTIVITY;
import static com.aecb.AppConstants.IntentKey.FROM_ADD_RECIPIENTS;
import static com.aecb.AppConstants.IntentKey.FROM_MENU_SCREEN;
import static com.aecb.AppConstants.IntentKey.IS_DEFAULT_SELECTED;
import static com.aecb.AppConstants.IntentKey.PRODUCT_DETAILS;
import static com.aecb.AppConstants.IntentKey.SCREEN_FROM;
import static com.aecb.AppConstants.IntentKey.SELECTED_PRODUCT;
import static com.aecb.AppConstants.MINUS;
import static com.aecb.AppConstants.ZERO;
import static com.aecb.util.FirebaseLogging.cancelFromAddCard;
import static com.aecb.util.FirebaseLogging.cardNotScanned;
import static com.aecb.util.FirebaseLogging.cardScanned;
import static com.aecb.util.FirebaseLogging.updatedCardNumber;
import static com.aecb.util.GsonKeys.SELECTED_CARD;
import static com.aecb.util.Utilities.isNullOrEmpty;
import static com.aecb.util.varibles.StringConstants.CONST_PYMT;

public class AddCardActivity extends BaseActivity<AddCardContract.View, AddCardContract.Presenter>
        implements AddCardContract.View, TextWatcher, View.OnClickListener {

    @Inject
    public AddCardContract.Presenter mPresenter;
    public ArrayList<EmailRecipients> emailList = new ArrayList<>();
    String serverDate, expiryAsPerCard;
    boolean lock;
    ProductsItem productsItem;
    Handler handler;
    Bundle bundle;
    BuyCreditReportResponse buyCreditReportResponse;
    private ActivityAddCardBinding mAddCardBinding;
    private MonthPicker monthPicker;
    private BlurDialogBaseFragment termsConditionFragment;
    private BlurDialogBaseFragment paymentStatusDialog;
    private CreditCardData selectedCreditCard;
    private String screenType;
    boolean isCardScanClicked = false;
    String scannedCardNumber = "";
    String scannedCardName = "";
    String scannedCardExpiry = "";


    public static String insertPeriodically(String text, String insert, int period) {
        StringBuilder builder = new StringBuilder(text.length() + insert.length() *
                (text.length() / period) + 1);
        int index = 0;
        String prefix = "";
        while (index < text.length()) {
            builder.append(prefix);
            prefix = insert;
            builder.append(text.substring(index,
                    Math.min(index + period, text.length())));
            index += period;
        }
        return builder.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("LocalLanguage==" + Locale.getDefault().getLanguage());
        enableBinding();
    }

    private void initMonthPicker() {
        Locale locale = new Locale(AppConstants.AppLanguage.ISO_CODE_ENG);
        monthPicker = new MonthPicker(this)
                .setLocale(locale)
                .setPositiveButton((month, startDate, endDate, year, monthLabel) -> {
                    String strMonth = "" + month;
                    if (strMonth.length() == 1) {
                        strMonth = ZERO + strMonth;
                    }
                    String strYear = "" + year;
                    serverDate = strYear + MINUS + strMonth;
                    String e = strYear + MINUS + strMonth;
                    if (!TextUtils.isEmpty(e) && e.length() == 7) {
                        expiryAsPerCard = e.substring(5, 7) + DIVISION;
                        expiryAsPerCard = expiryAsPerCard + e.substring(2, 4);
                    }
                    mAddCardBinding.edtExpireDate.setText(expiryAsPerCard);
                    mAddCardBinding.txtInputLoutExpiryDate.setHint(getString(R.string.hint_expiry_date));
                    mAddCardBinding.edtCVV.requestFocus();

                })
                .setNegativeButton(dialog -> dialog.dismiss());
    }

    private void focusChangeListener() {
        mAddCardBinding.edtCardNumber.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mAddCardBinding.txtInputLoutCardNumber.setHint(getString(R.string.hint_card_number));
            } else if (!mAddCardBinding.edtCardNumber.getText().toString().isEmpty()) {
                mAddCardBinding.txtInputLoutCardNumber.setHint(getString(R.string.hint_card_number));
            } else {
                mAddCardBinding.txtInputLoutCardNumber.setHint(getString(R.string.enter_card_number));
            }
        });
        mAddCardBinding.edtFullName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    View view = getCurrentFocus();
                    if (view == null) {
                        view = new View(getApplicationContext());
                    }
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    showMonthPicker();
                }
                return false;
            }
        });
        mAddCardBinding.edtExpireDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mAddCardBinding.txtInputLoutExpiryDate.setHint(getString(R.string.txt_yyyy_mm));
            } else if (!mAddCardBinding.edtExpireDate.getText().toString().isEmpty()) {
                mAddCardBinding.txtInputLoutExpiryDate.setHint(getString(R.string.txt_yyyy_mm));
            } else {
                mAddCardBinding.txtInputLoutExpiryDate.setHint(getString(R.string.hint_expiry_date));
            }
        });

        mAddCardBinding.edtCVV.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mAddCardBinding.txtInputLoutCVV.setHint(getString(R.string.hint_cvv));
            } else if (!mAddCardBinding.edtCVV.getText().toString().isEmpty()) {
                mAddCardBinding.txtInputLoutCVV.setHint(getString(R.string.hint_cvv));
            } else {
                mAddCardBinding.txtInputLoutCVV.setHint(getString(R.string.enter_cvv));
            }
        });
        mAddCardBinding.edtFullName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mAddCardBinding.txtInputLoutFullName.setHint(getString(R.string.hint_title_full_name));
            } else if (!mAddCardBinding.edtFullName.getText().toString().isEmpty()) {
                mAddCardBinding.txtInputLoutFullName.setHint(getString(R.string.hint_title_full_name));
            } else {
                mAddCardBinding.txtInputLoutFullName.setHint(getString(R.string.enter_full_name));
            }
        });
        mAddCardBinding.edtCardNumber.addTextChangedListener(this);
        mAddCardBinding.edtExpireDate.addTextChangedListener(this);
        mAddCardBinding.edtCVV.addTextChangedListener(this);
        mAddCardBinding.edtFullName.addTextChangedListener(this);
        mAddCardBinding.edtCardNumber.setLongClickable(false);
    }

    private void textChangeListeners() {
        mPresenter.displayContinueButton(mAddCardBinding.edtCardNumber.getText().toString(),
                mAddCardBinding.edtExpireDate.getText().toString(),
                mAddCardBinding.edtCVV.getText().toString(), mAddCardBinding.edtFullName.getText().toString(),
                mAddCardBinding.radioSaveCard.isChecked(), mAddCardBinding.checkboxTC.isChecked(), false);
    }

    private void enableBinding() {
        mAddCardBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        mPresenter.getLastLoginUser();
        getBundleData();
        clickListeners();
        focusChangeListener();
        initMonthPicker();
        radioButtonCheckListener();
    }

    private void radioButtonCheckListener() {
        mAddCardBinding.radioSaveCard.setOnClickListener(v -> {
            if (!mAddCardBinding.radioSaveCard.isSelected()) {
                mAddCardBinding.radioSaveCard.setChecked(true);
                mAddCardBinding.radioSaveCard.setSelected(true);
                mAddCardBinding.llTermsConditions.setVisibility(View.GONE);
            } else {
                mAddCardBinding.radioGroupSaveCard.clearCheck();
                mAddCardBinding.radioSaveCard.setChecked(false);
                mAddCardBinding.radioSaveCard.setSelected(false);
                mAddCardBinding.llTermsConditions.setVisibility(View.GONE);
            }
        });
    }

    private void clickListeners() {
        mAddCardBinding.edtExpireDate.setOnClickListener(this);
        mAddCardBinding.ivScan.setOnClickListener(this);
        mAddCardBinding.btnContinue.setOnClickListener(this);
        mAddCardBinding.tvTermsAndCondition.setOnClickListener(this);
        mAddCardBinding.rbMakeDefault.setOnClickListener(v -> {
            if (!mAddCardBinding.rbMakeDefault.isSelected()) {
                mAddCardBinding.rbMakeDefault.setChecked(true);
                mAddCardBinding.rbMakeDefault.setSelected(true);
            } else {
                mAddCardBinding.rbMakeDefault.setChecked(false);
                mAddCardBinding.rbMakeDefault.setSelected(false);
            }
        });
    }

    @NonNull
    @Override
    public AddCardContract.Presenter createPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_add_card;
    }

    @Override
    public void initToolbar() {
        ToolbarView tb = findViewById(R.id.toolBar);
        tb.setCancelBtnListener(() -> {
            if (screenType.equals(FROM_ADD_RECIPIENTS)) {
                cancelFromAddCard();
                paymentFailedAction();
            } else {
                finish();
            }
        });
        tb.setBackBtnListener(this::onBackPressed);
        tb.setBtnVisibility(true, true);
        tb.setToolbarTitleVisibility(false);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cancelFromAddCard();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.hashCode() == mAddCardBinding.edtCardNumber.getText().hashCode()) {
            lock = before != 0;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        textChangeListeners();
        if (s.length() == 19) {
            mAddCardBinding.edtFullName.requestFocus();
        }
        if (s.hashCode() == mAddCardBinding.edtCardNumber.getText().hashCode()) {
            String source = s.toString();
            int length = source.length();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(source);
            if (length > 0 && length % 5 == 0) {
                if (lock)
                    stringBuilder.deleteCharAt(length - 1);
                else
                    stringBuilder.insert(length - 1, " ");
                mAddCardBinding.edtCardNumber.setText(stringBuilder);
                mAddCardBinding.edtCardNumber.setSelection(mAddCardBinding.edtCardNumber.getText().length());
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnContinue:
                callAddCard();
                if (!isCardScanClicked) {
                    cardNotScanned();
                }
                if (!scannedCardNumber.equals(mAddCardBinding.edtCardNumber.getText().toString())) {
                    updatedCardNumber();
                }
                break;
            case R.id.ivScan:
                isCardScanClicked = true;
                onScanCard();
                break;
            case R.id.edtExpireDate:
                showMonthPicker();
                break;
            case R.id.tvTermsAndCondition:
                termsConditionFragment = TermsAndConditionsFragment.newInstance(null);
                termsConditionFragment.show(getSupportFragmentManager(), termsConditionFragment.getTag());
                break;
        }
    }

    private void showMonthPicker() {
        Locale localeStartDate = new Locale(AppConstants.AppLanguage.ISO_CODE_ENG);
        getResources().getConfiguration().setLocale(localeStartDate);
        Locale.setDefault(getResources().getConfiguration().locale);
        monthPicker.show();
    }

    private void callAddCard() {
        mPresenter.addCard(mAddCardBinding.edtCardNumber.getText().toString(), serverDate,
                mAddCardBinding.edtCVV.getText().toString(), mAddCardBinding.edtFullName.getText().toString(),
                mAddCardBinding.radioSaveCard.isChecked(), mAddCardBinding.checkboxTC.isChecked(),
                true, mAddCardBinding.rbMakeDefault.isChecked(), productsItem, PAYMENT_CONFIG_NEW_CARD,
                bundle.getString(FROM_ACTIVITY));
    }

    private void getBundleData() {
        bundle = getIntent().getExtras();
        assert bundle != null;
        screenType = bundle.getString(FROM_ACTIVITY);
        if (bundle != null && Objects.equals(bundle.getString(FROM_ACTIVITY), FROM_ADD_RECIPIENTS)) {
            productsItem = (ProductsItem) bundle.getSerializable(PRODUCT_DETAILS);
            emailList = (ArrayList<EmailRecipients>) bundle.getSerializable(EMAIL_RECIPIENTS);
            mAddCardBinding.radioGroupSaveCard.setVisibility(View.GONE);
            if (bundle.containsKey(SELECTED_CARD)) {
                selectedCreditCard = new Gson().fromJson(bundle.getString(SELECTED_CARD), CreditCardData.class);
                if (selectedCreditCard != null) {
                    setCardDetailsAndDisableView(selectedCreditCard);
                }
            }
            mAddCardBinding.rbMakeDefault.setVisibility(View.GONE);
        } else if (bundle != null && Objects.equals(bundle.getString(FROM_ACTIVITY), FROM_MENU_SCREEN)) {
            mAddCardBinding.radioGroupSaveCard.setVisibility(View.GONE);
            mAddCardBinding.llTermsConditions.setVisibility(View.GONE);
            mAddCardBinding.btnContinue.setText(R.string.txt_continue);
        }
    }

    private void setCardDetailsAndDisableView(CreditCardData selectedCreditCard) {
        mAddCardBinding.edtFullName.setText(selectedCreditCard.getCardholdername());
        mAddCardBinding.edtExpireDate.setText(selectedCreditCard.getExpiryDate());
        mAddCardBinding.edtCardNumber.setEnabled(false);
        mAddCardBinding.edtFullName.setEnabled(false);
        mAddCardBinding.radioSaveCard.setEnabled(false);
        mAddCardBinding.edtExpireDate.setEnabled(false);
        mAddCardBinding.checkboxTC.setEnabled(false);
        String date = selectedCreditCard.getExpiryDate();

        if (!TextUtils.isEmpty(date) && date.length() == 6) {
            expiryAsPerCard = date.substring(0, 1);
            expiryAsPerCard = "0" + expiryAsPerCard + "/" + date.substring(2, 6) + "";
        } else if (!TextUtils.isEmpty(date) && date.length() == 7) {
            expiryAsPerCard = date.substring(0, 2);
            expiryAsPerCard = expiryAsPerCard + "/" + date.substring(3, 7) + "";
        }
        if (!TextUtils.isEmpty(date) && expiryAsPerCard.length() == 7) {
            String date1 = expiryAsPerCard.substring(3, 7);
            serverDate = date1 + "-" + expiryAsPerCard.substring(0, 2);
        }

        String cardNumber = insertPeriodically(selectedCreditCard.getCardnumber(), " ", 4);

        if (!isNullOrEmpty(expiryAsPerCard)) {
            mAddCardBinding.edtExpireDate.setText(expiryAsPerCard);
        }

        mAddCardBinding.edtCardNumber.setText(cardNumber);
        mAddCardBinding.checkboxTC.setChecked(true);
        mAddCardBinding.radioSaveCard.setChecked(true);
        if (selectedCreditCard.getPymtCardIsDefault() != null && selectedCreditCard.getPymtCardIsDefault().equalsIgnoreCase(StringConstants.CONST_YES)) {
            mAddCardBinding.rbMakeDefault.setChecked(true);
        }
        mAddCardBinding.tvTextTitle.setText(getString(R.string.txt_title_card_details));
    }

    private void onScanCard() {
        Intent intent = new Intent(this, CardIOActivity.class);
        intent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, false); // default: false
        intent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        intent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false);
        intent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false);
        intent.putExtra(CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION, true);
        intent.putExtra(CardIOActivity.EXTRA_RETURN_CARD_IMAGE, true);// default: false
        startActivityForResult(intent, MY_SCAN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_SCAN_REQUEST_CODE) {
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                Bitmap card = CardIOActivity.getCapturedCardImage(data);
                setCardDetail(scanResult, card);
                cardScanned();
            }
        } else if (requestCode == FROM_TO_ADDCARD_SCREEN && resultCode == SAVE_CARD_SUCCESSFULLY) {
            finish();
        } else if (requestCode == ADCB_CARD_REQUEST && resultCode == RESULT_OK) {
            showLoading(null);
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                ActivateCardRequestBody activateCardRequestBody = new ActivateCardRequestBody();
                activateCardRequestBody.setAmount("1019");
                activateCardRequestBody.setConsent(CONST_PYMT);
                activateCardRequestBody.setOperationType(CARD_ACTIVATE);
                presenter.activateCard(activateCardRequestBody, buyCreditReportResponse, screenType,
                        mAddCardBinding.rbMakeDefault.isChecked());
            }, 5000);

            /*if (data != null) {
                if (!ValidationUtil.isNullOrEmpty(data.getStringExtra(ADCB_WEBVIEW_HTML_CONTENT))) {
                    BaseResponse baseResponse = new Gson().fromJson(data.getStringExtra(ADCB_WEBVIEW_HTML_CONTENT), BaseResponse.class);
                    if (baseResponse.isSuccess() && baseResponse.getStatus().equals("200")) {
                        ActivateCardRequestBody activateCardRequestBody = new ActivateCardRequestBody();
                        activateCardRequestBody.setAmount("1019");
                        activateCardRequestBody.setConsent(CONST_PYMT);
                        activateCardRequestBody.setOperationType(CARD_ACTIVATE);
                        activateCardRequestBody.setTransactionID(baseResponse.getTransactionID());
                        presenter.activateCard(activateCardRequestBody, baseResponse, buyCreditReportResponse, screenType,
                                mAddCardBinding.rbMakeDefault.isChecked());
                    } else {
                        showApiFailureError(baseResponse.getMessage(), baseResponse.getStatus(), "");
                    }
                }
            }*/
        }
    }

    private void setCardDetail(CreditCard scanResult, Bitmap card) {
        String scannedCardNumber = "";
        try {
            scannedCardNumber = scanResult.getFormattedCardNumber().replaceAll(" ", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (scannedCardNumber.matches("[0-9]+")) {
            mAddCardBinding.edtCardNumber.setText(scanResult.getFormattedCardNumber());
        } else {
            mAddCardBinding.edtCardNumber.setText("");
        }
        if (scanResult.isExpiryValid()) {
            mAddCardBinding.edtExpireDate.setText(scanResult.expiryYear + MINUS + scanResult.expiryMonth);
        }
        if (scanResult.cvv != null) {
            mAddCardBinding.edtCVV.setText(scanResult.cvv.length());
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showCardNumberError(String string) {
        showSnackBar(string);
    }

    @Override
    public void showFullNameError(String string) {
    }

    @Override
    public void showExpiryDateError(String string) {
    }

    @Override
    public void showCvvEmptyMessages(String string) {
        showSnackBar(string);
    }

    private void showSnackBar(String string) {
        localValidationError(getString(R.string.error), string);
    }

    @Override
    public void showContinueButton(boolean b) {
        if (b) {
            mAddCardBinding.btnContinue.setEnabled(true);
            mAddCardBinding.btnContinue.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_blue_button));
        } else {
            mAddCardBinding.btnContinue.setEnabled(false);
            mAddCardBinding.btnContinue.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btn_disable_state));
        }
    }

    @Override
    public void showTermsConditionError() {
        showSnackBar(getString(R.string.please_select_terms_conditions));
    }

    public void selectTermsConditions(boolean value) {
        mAddCardBinding.checkboxTC.setChecked(value);
    }

    @Override
    public void displayPaymentSuccessDialog(BuyCreditReportResponse buyCreditReportResponse) {
        this.buyCreditReportResponse = buyCreditReportResponse;
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.IntentKey.FROM_ACTIVITY,
                AppConstants.ActivityIntentCode.PAYMENT_SUCCESSFULLY_SAVED);
        bundle.putString(AppConstants.IntentKey.TITLE_TEXT, getString(R.string.payment_successfully));
        bundle.putString(AppConstants.IntentKey.SUB_TITLE_TEXT, getString(R.string.your_payment_has_been_successfully_processed));
        bundle.putString(AppConstants.IntentKey.POSITIVE_TEXT, getString(R.string.txt_ok));
        paymentStatusDialog = ExitFragment.newInstance(bundle);
        paymentStatusDialog.setCancelable(false);
        paymentStatusDialog.show(getSupportFragmentManager(), paymentStatusDialog.getTag());
        callSendEmailApi();
        if (Objects.equals(this.bundle.getString(FROM_ACTIVITY), FROM_ADD_RECIPIENTS))
            paymentSuccessAction();
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
        Intent intent = new Intent(this, CheckoutActivity.class);
        intent.setFlags(FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(SELECTED_PRODUCT, productsItem);
        startActivity(intent);
    }

    private void paymentSuccessAction() {
        handler = new Handler();
        handler.postDelayed(() -> {
            openDashboardScreen();
            if (paymentStatusDialog != null)
                paymentStatusDialog.dismiss();
        }, 3000);
    }

    void callSendEmailApi() {
        EmailRecipientsRequest recipientsRequest = new EmailRecipientsRequest();
        recipientsRequest.setApplicationId(buyCreditReportResponse.getBuyCreditReportData().getPortalUserId());
        recipientsRequest.setAdditionalReceivers(emailList);
        if (emailList != null && !emailList.isEmpty())
            mPresenter.callSendAdditionalEmailsApi(recipientsRequest);
    }

    @Override
    public void openDashboardScreen() {
        moveActivity(this, DashboardActivity.class, true, true);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        mAddCardBinding.mainLayout.setVisibility(View.VISIBLE);
        //mAddCardBinding.progressCircular.setVisibility(View.GONE);
    }

    @Override
    public void goToVerifyAmountScreen(AddCardResponse addCardResponse) {
        hideLoading();
        if (addCardResponse != null) {
            Bundle bundle = new Bundle();
            bundle.putString(ADD_CARD_RESPONSE, new Gson().toJson(addCardResponse));
            bundle.putString(SCREEN_FROM, screenType);
            bundle.putSerializable(PRODUCT_DETAILS, productsItem);
            bundle.putBoolean(IS_DEFAULT_SELECTED, mAddCardBinding.rbMakeDefault.isChecked());
            bundle.putSerializable(AppConstants.IntentKey.EMAIL_RECIPIENTS, (Serializable) emailList);
            moveActivity(AddCardActivity.this, VerifyAmountForPaymentActivity.class, false, false,
                    bundle, FROM_TO_ADDCARD_SCREEN);
        }
    }

    @Override
    public void openVerifyPaymentScreen(AddCardResponse addCardResponse, String screenFrom,
                                        BuyCreditReportResponse buyCreditReportResponse) {
        hideLoading();
        Bundle bundle = new Bundle();
        bundle.putString(SCREEN_FROM, screenFrom);
        bundle.putString(BUY_CREDIT_RESPONSE, new Gson().toJson(buyCreditReportResponse));
        bundle.putString(ADD_CARD_RESPONSE, new Gson().toJson(addCardResponse));
        bundle.putSerializable(PRODUCT_DETAILS, productsItem);
        bundle.putSerializable(AppConstants.IntentKey.EMAIL_RECIPIENTS, (Serializable) emailList);
        moveActivity(AddCardActivity.this, VerifyAmountForPaymentActivity.class, false, false,
                bundle);
    }

    @Override
    public void openADCBWebview(String htmlBodyContent) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.IntentKey.ADCB_WEBVIEW_HTML, htmlBodyContent);
        moveActivity(AddCardActivity.this, AdcbCardWebviewActivity.class, false, false,
                bundle, ADCB_CARD_REQUEST);
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
    public void goToCheckoutScreen() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(SELECTED_PRODUCT, productsItem);
        bundle.putSerializable(AppConstants.IntentKey.EMAIL_RECIPIENTS, emailList);
        moveActivity(this, CheckoutActivity.class, true, true, bundle);
    }
}