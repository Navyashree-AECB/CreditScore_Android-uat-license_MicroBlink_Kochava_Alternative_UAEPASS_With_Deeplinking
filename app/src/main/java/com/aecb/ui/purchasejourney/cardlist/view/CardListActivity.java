package com.aecb.ui.purchasejourney.cardlist.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aecb.App;
import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.base.mvp.BaseActivity;
import com.aecb.base.mvp.BlurDialogBaseFragment;
import com.aecb.data.api.models.buycreditreport.BuyCreditReportResponse;
import com.aecb.data.api.models.cards.CreditCardData;
import com.aecb.data.api.models.commonmessageresponse.MessageItem;
import com.aecb.data.api.models.commonmessageresponse.MessageResponse;
import com.aecb.data.api.models.emailrecipients.EmailRecipients;
import com.aecb.data.api.models.emailrecipients.EmailRecipientsRequest;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.data.api.models.payment_method.ActivateCardRequestBody;
import com.aecb.data.api.models.payment_method.AddCardResponse;
import com.aecb.data.api.models.payment_method.DeleteCardRequestBody;
import com.aecb.data.api.models.request.updateuserprofile.UpdateUserProfileRequest;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.databinding.ActivityCardListBinding;
import com.aecb.presentation.customviews.ToolbarView;
import com.aecb.ui.adapters.CardListAdapter;
import com.aecb.ui.adcbwebview.view.AdcbCardWebviewActivity;
import com.aecb.ui.addcvv.view.AddCvvFragment;
import com.aecb.ui.checkout.view.CheckoutActivity;
import com.aecb.ui.dashboard.view.DashboardActivity;
import com.aecb.ui.exit.view.ExitFragment;
import com.aecb.ui.purchasejourney.addcard.view.AddCardActivity;
import com.aecb.ui.purchasejourney.cardlist.presenter.CardListContract;
import com.aecb.ui.purchasejourney.cardupdatedeletefragment.view.PaymentUpdateDeleteFragment;
import com.aecb.util.ItemOffsetDecoration;
import com.aecb.util.Utilities;
import com.aecb.util.varibles.StringConstants;
import com.aecb.util.visibility.Visibility;
import com.aecb.util.visibility.VisibilityStates;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import timber.log.Timber;

import static com.aecb.AppConstants.ActivityIntentCode.ADCB_CARD_REQUEST;
import static com.aecb.AppConstants.ActivityIntentCode.AUTHORIZATION_FAILED;
import static com.aecb.AppConstants.ActivityIntentCode.FROM_TO_ADDCARD_SCREEN;
import static com.aecb.AppConstants.ActivityIntentCode.MAKE_DEFAULT_CARD;
import static com.aecb.AppConstants.ActivityIntentCode.MAKE_EDIRHAM_DEFAULT;
import static com.aecb.AppConstants.ActivityIntentCode.PAYMENT_METHOD_UPDATED;
import static com.aecb.AppConstants.ActivityIntentCode.UPDATE_DELETE_CARD;
import static com.aecb.AppConstants.ApiParameter.CARD_ACTIVATE;
import static com.aecb.AppConstants.AppLanguage.ISO_CODE_ARABIC;
import static com.aecb.AppConstants.IntentKey.DESC_CLICK;
import static com.aecb.AppConstants.IntentKey.EMAIL_RECIPIENTS;
import static com.aecb.AppConstants.IntentKey.FROM_ACTIVITY;
import static com.aecb.AppConstants.IntentKey.FROM_ADD_RECIPIENTS;
import static com.aecb.AppConstants.IntentKey.FROM_MENU_SCREEN;
import static com.aecb.AppConstants.IntentKey.IS_CARD_DEFAULT;
import static com.aecb.AppConstants.IntentKey.IS_EDIRHAM_DEFAULT;
import static com.aecb.AppConstants.IntentKey.PRODUCT_DETAILS;
import static com.aecb.AppConstants.IntentKey.SELECTED_NI_CARD;
import static com.aecb.AppConstants.IntentKey.SELECTED_PRODUCT;
import static com.aecb.AppConstants.IntentKey.SHOW_SCORE_LOADING;
import static com.aecb.AppConstants.PaymentMethods.E_DIRHAM;
import static com.aecb.AppConstants.PaymentMethods.PAYMENT_GW;
import static com.aecb.util.FirebaseLogging.addedCardFromCheckout;
import static com.aecb.util.FirebaseLogging.cancelFromPaymentScreen;
import static com.aecb.util.FirebaseLogging.selectedEDirhamFromCheckout;
import static com.aecb.util.ValidationUtil.isNullOrEmpty;
import static com.aecb.util.varibles.StringConstants.CONST_NO;
import static com.aecb.util.varibles.StringConstants.CONST_PYMT;
import static com.aecb.util.visibility.VisibilityStates.GONE;
import static com.aecb.util.visibility.VisibilityStates.VISIBLE;

public class CardListActivity extends BaseActivity<CardListContract.View, CardListContract.Presenter>
        implements CardListContract.View, View.OnClickListener {

    @Inject
    public CardListContract.Presenter mPresenter;
    public ArrayList<EmailRecipients> emailList = new ArrayList<>();
    PaymentUpdateDeleteFragment cardUpdateDeleteFragment;
    ProductsItem productsItem;
    Bundle bundle;
    String serverDate, expiryAsPerCard;
    boolean isDefault;
    BuyCreditReportResponse buyCreditReportResponse;
    Handler handler;
    String screenFrom;
    private BlurDialogBaseFragment deleteCardConfirmationDialog;
    private ActivityCardListBinding mCardListBinding;
    private CardListAdapter mAdapter;
    private CreditCardData selectedCard;
    private CreditCardData selectedCardForUpdate;
    private BlurDialogBaseFragment paymentStatusDialog;
    UpdateUserProfileRequest updateUserProfileRequest;
    private DBUserTC dbUserTC;
    boolean eDirhamSelected = false;
    private BlurDialogBaseFragment addCvvFragment;
    String transactionID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCardListBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        getBundleData();
        enableBinding();
        mPresenter.getLastLoginUser();
    }

    private void getBundleData() {
        bundle = getIntent().getExtras();
        if (bundle != null && Objects.equals(bundle.getString(FROM_ACTIVITY), FROM_ADD_RECIPIENTS)) {
            screenFrom = FROM_ADD_RECIPIENTS;
            productsItem = (ProductsItem) bundle.getSerializable(PRODUCT_DETAILS);
            emailList = (ArrayList<EmailRecipients>) bundle.getSerializable(EMAIL_RECIPIENTS);
            mCardListBinding.llPaymentMethods.setVisibility(View.GONE);
            mCardListBinding.ivThreeDotsEDirham.setVisibility(View.GONE);
        } else if (bundle != null && Objects.equals(bundle.getString(FROM_ACTIVITY), FROM_MENU_SCREEN)) {
            screenFrom = FROM_MENU_SCREEN;
            mCardListBinding.tvPaymentMethodTitle.setText(R.string.credit_or_debit_card);
            mCardListBinding.btnContinue.setText(R.string.update_payment_method);
            mCardListBinding.btnContinue.setVisibility(View.GONE);
            mCardListBinding.llPaymentMethods.setVisibility(View.GONE);
            mCardListBinding.btnAddCard.setVisibility(View.VISIBLE);
            mCardListBinding.ivThreeDotsEDirham.setVisibility(View.VISIBLE);
        }
    }

    private void enableBinding() {
        clickListeners();
        initRecyclerView();
    }

    private void clickListeners() {
        mCardListBinding.btnContinue.setOnClickListener(this);
        mCardListBinding.btnAddCard.setOnClickListener(this);
        mCardListBinding.ivThreeDotsEDirham.setOnClickListener(this);
        mCardListBinding.cvEDirham.setOnClickListener(this);
    }

    private void initRecyclerView() {
        mCardListBinding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mCardListBinding.recyclerView.setLayoutManager(linearLayoutManager);
        if (Objects.equals(bundle.getString(FROM_ACTIVITY), FROM_ADD_RECIPIENTS)) {
            mAdapter = new CardListAdapter(this, new ArrayList<>(), false);
        } else {
            mAdapter = new CardListAdapter(this, new ArrayList<>(), true);
        }
        mCardListBinding.recyclerView.addItemDecoration(new ItemOffsetDecoration(this, R.dimen._6sdp));
        mCardListBinding.recyclerView.setAdapter(mAdapter);
    }

    @NonNull
    @Override
    public CardListContract.Presenter createPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_card_list;
    }

    @Override
    public void initToolbar() {
        ToolbarView tb = findViewById(R.id.toolBar);
        tb.setCancelBtnListener(() -> {
            if (screenFrom.equals(FROM_ADD_RECIPIENTS)) {
                cancelFromPaymentScreen();
                Bundle bundle = new Bundle();
                bundle.putSerializable(SELECTED_PRODUCT, productsItem);
                bundle.putSerializable(AppConstants.IntentKey.EMAIL_RECIPIENTS, emailList);
                moveActivity(this, CheckoutActivity.class, true, true, bundle);
            } else {
                this.onBackPressed();
            }
        });
        tb.setBackBtnListener(this::onBackPressed);
        tb.setBtnVisibility(true, true);
        tb.setBackBtnListener(() -> finish());
        tb.setToolbarTitleVisibility(false);
    }

    public void setVisibility(VisibilityStates isEmptyView, VisibilityStates isRecyclerView) {
        Visibility.setVisibility(mCardListBinding.tvEmpty, isEmptyView);
        Visibility.setVisibility(mCardListBinding.recyclerView, isRecyclerView);
    }

    void setContinueButtonVisibility() {
        if (Objects.equals(bundle.getString(FROM_ACTIVITY), FROM_ADD_RECIPIENTS)) {
            if (mAdapter.getItemCount() > 0) {
                mCardListBinding.btnContinue.setEnabled(true);
                mCardListBinding.btnContinue.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_blue_button));
            } else {
                mCardListBinding.btnContinue.setEnabled(false);
                mCardListBinding.btnContinue.setBackground(ContextCompat.getDrawable(this, R.drawable.disable_button));
            }
        } else {
            mCardListBinding.btnContinue.setEnabled(false);
            mCardListBinding.btnContinue.setBackground(ContextCompat.getDrawable(this, R.drawable.disable_button));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddCard:
                openAddCard();
                addedCardFromCheckout();
                break;
            case R.id.btnContinue:
                if (Objects.equals(bundle.getString(FROM_ACTIVITY), FROM_ADD_RECIPIENTS)) {
                    showSelectedCardToCheckOut();
                } else {
                    //mPresenter.updatePaymentMethod(GOOGLE_PAY);
                }
                break;
            case R.id.ivThreeDotsEDirham:
                if (dbUserTC.getPreferredPaymentMethod().equalsIgnoreCase(PAYMENT_GW)) {
                    openMakeDefaultForEDirham();
                }
                break;
            case R.id.cvEDirham:
                mAdapter.borderForFirstTimeEDirham = true;
                mAdapter.borderForFirstTimeNI = false;
                mAdapter.notifyDataSetChanged();
                eDirhamSelected = true;
                mCardListBinding.cvEDirham.setBackground(ContextCompat.getDrawable(this, R.drawable.selected_card_backgraound));
                if (Objects.equals(bundle.getString(FROM_ACTIVITY), FROM_ADD_RECIPIENTS)) {
                    mCardListBinding.btnContinue.setEnabled(true);
                    mCardListBinding.btnContinue.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_blue_button));
                }
                break;
        }
    }

    public void disSelectEdirham() {
        eDirhamSelected = false;
        mCardListBinding.cvEDirham.setBackground(ContextCompat.getDrawable(this, R.drawable.selected_card_backgraound_white));
    }

    private void showSelectedCardToCheckOut() {
        if (eDirhamSelected) {
            selectedEDirhamFromCheckout();
        }
        if (selectedCard != null || eDirhamSelected) {
            if (!eDirhamSelected && selectedCard.getGatewayCode() == null) {
                Bundle bundle = new Bundle();
                bundle.putString(SELECTED_NI_CARD, new Gson().toJson(selectedCard));
                bundle.putString(FROM_ACTIVITY, screenFrom);
                addCvvFragment = AddCvvFragment.newInstance(bundle);
                addCvvFragment.show(getSupportFragmentManager(), addCvvFragment.getTag());
            } else {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(AppConstants.IntentKey.SELECTED_CARD, new Gson().toJson(selectedCard));
                resultIntent.putExtra(AppConstants.IntentKey.SELECTED_EDIRHAM, eDirhamSelected);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        } else {
            localValidationError(getString(R.string.error), getString(R.string.please_select_card));
        }
    }

    private void openAddCardFromMenu() {
        moveActivity(this, AddCardActivity.class, false, false, bundle, FROM_TO_ADDCARD_SCREEN);
    }

    private void openPaymentSummary() {
        if (selectedCard != null) {
            if (selectedCard.getPymtCardIsDefault() != null &&
                    selectedCard.getPymtCardIsDefault().equalsIgnoreCase(StringConstants.CONST_YES)) {
                isDefault = true;
            }
            mPresenter.addCard(selectedCard.getCardnumber(),
                    serverDate,
                    selectedCard.getCardholdername(),
                    true, true, true,
                    isDefault, productsItem, AppConstants.AddCardOperationType.PAYMENT_OLD_CARD, bundle.getString(FROM_ACTIVITY));
        } else {
            localValidationError(getString(R.string.error), getString(R.string.please_select_card));
        }
    }

    private void openAddCard() {
        Bundle b = new Bundle();
        b.putSerializable(PRODUCT_DETAILS, productsItem);
        b.putSerializable(AppConstants.IntentKey.EMAIL_RECIPIENTS, emailList);
        b.putString(FROM_ACTIVITY, screenFrom);
        moveActivity(this, AddCardActivity.class, false, false, b, FROM_TO_ADDCARD_SCREEN);
    }

    public void openCardUpdateDeleteView(CreditCardData item) {
        selectedCardForUpdate = item;
        Bundle bundle = new Bundle();
        if (item != null) {
            if (dbUserTC.getPreferredPaymentMethod().equalsIgnoreCase(E_DIRHAM)) {
                bundle.putString(IS_CARD_DEFAULT, CONST_NO);
            } else {
                bundle.putString(IS_CARD_DEFAULT, getIsDefault(item));
            }
            cardUpdateDeleteFragment = PaymentUpdateDeleteFragment.newInstance(bundle);
            cardUpdateDeleteFragment.show(getSupportFragmentManager(), cardUpdateDeleteFragment.getTag());
        }
    }

    public void openMakeDefaultForEDirham() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_EDIRHAM_DEFAULT, true);
        cardUpdateDeleteFragment = PaymentUpdateDeleteFragment.newInstance(bundle);
        cardUpdateDeleteFragment.show(getSupportFragmentManager(), cardUpdateDeleteFragment.getTag());
    }

    String getIsDefault(CreditCardData creditCardData) {
        if (creditCardData.getPymtCardIsDefault() != null && !creditCardData.getPymtCardIsDefault().isEmpty()) {
            return creditCardData.getPymtCardIsDefault();
        } else {
            return CONST_NO;
        }

    }

    public void setSelectedCard(CreditCardData item) {
        selectedCard = item;
        String date = selectedCard.getExpiryDate();

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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Timber.d("onActivityResult ::" + resultCode);
        if (requestCode == ADCB_CARD_REQUEST && resultCode == RESULT_OK) {
            showLoading(null);
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                ActivateCardRequestBody activateCardRequestBody = new ActivateCardRequestBody();
                activateCardRequestBody.setAmount("1019");
                activateCardRequestBody.setConsent(CONST_PYMT);
                activateCardRequestBody.setOperationType(CARD_ACTIVATE);
                activateCardRequestBody.setTransactionID(transactionID);
                presenter.activateCard(activateCardRequestBody, screenFrom,
                        CONST_PYMT);
            }, 5000);
        } else {
            mPresenter.getLastLoginUser();
        }

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

    @Override
    public void openDashboardScreen() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(SHOW_SCORE_LOADING, true);
        moveActivity(this, DashboardActivity.class, true, true, bundle);
    }

    private void paymentSuccessAction() {
        handler = new Handler();
        handler.postDelayed(() -> {
            openDashboardScreen();
        }, 3000);
    }

    void callSendEmailApi() {
        EmailRecipientsRequest recipientsRequest = new EmailRecipientsRequest();
        recipientsRequest.setApplicationId(buyCreditReportResponse.getBuyCreditReportData().getPortalUserId());
        recipientsRequest.setAdditionalReceivers(emailList);
        if (emailList != null && !emailList.isEmpty())
            mPresenter.callSendAdditionalEmailsApi(recipientsRequest);
    }

    public void showDeleteConfirmationMessage(int from, String title, String msg, String positiveText, String negativeText) {
        Bundle bundle = new Bundle();
        bundle.putInt(FROM_ACTIVITY,
                from);
        bundle.putString(AppConstants.IntentKey.TITLE_TEXT, title);
        bundle.putString(AppConstants.IntentKey.SUB_TITLE_TEXT, msg);
        bundle.putString(AppConstants.IntentKey.POSITIVE_TEXT, positiveText);
        if (negativeText != null)
            bundle.putString(AppConstants.IntentKey.NEGATIVE_TEXT, negativeText);
        deleteCardConfirmationDialog = ExitFragment.newInstance(bundle);
        deleteCardConfirmationDialog.show(getSupportFragmentManager(), deleteCardConfirmationDialog.getTag());
    }

    public void dialogBtnClick(boolean isStatus, int fromActivity) {
        if (deleteCardConfirmationDialog != null) {
            deleteCardConfirmationDialog.dismiss();
        }
        if (fromActivity == AppConstants.ActivityIntentCode.PAYMENT_STATUS_FAILED) {
            paymentFailedAction();
        } else if (fromActivity == AppConstants.ActivityIntentCode.PAYMENT_SUCCESSFULLY_SAVED) {
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
                openDashboardScreen();
            }
        }
        if (isStatus) {
            if (fromActivity == UPDATE_DELETE_CARD) {
                DeleteCardRequestBody deleteCardRequestBody = new DeleteCardRequestBody();
                deleteCardRequestBody.setCardNumber(selectedCardForUpdate.getCardnumber());
                deleteCardRequestBody.setExpiry(getExpiryDate(selectedCardForUpdate.getExpiryDate()));
                deleteCardRequestBody.setOperationType(AppConstants.ApiParameter.CARD_DELETE);
                deleteCardConfirmationDialog.dismiss();
                mPresenter.updateCard(deleteCardRequestBody);
            } else if (fromActivity == MAKE_DEFAULT_CARD) {
                if (selectedCardForUpdate.getGatewayCode() == null) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString(SELECTED_NI_CARD, new Gson().toJson(selectedCardForUpdate));
                    bundle1.putString(FROM_ACTIVITY, screenFrom);
                    addCvvFragment = AddCvvFragment.newInstance(bundle1);
                    addCvvFragment.show(getSupportFragmentManager(), addCvvFragment.getTag());
                } else {
                    DeleteCardRequestBody deleteCardRequestBody = new DeleteCardRequestBody();
                    deleteCardRequestBody.setCardNumber(selectedCardForUpdate.getCardnumber());
                    deleteCardRequestBody.setExpiry(getExpiryDate(selectedCardForUpdate.getExpiryDate()));
                    deleteCardRequestBody.setOperationType(AppConstants.ApiParameter.CARD_DEFAULT);
                    deleteCardConfirmationDialog.dismiss();
                    mPresenter.updateCard(deleteCardRequestBody);
                }
            } else if (fromActivity == PAYMENT_METHOD_UPDATED) {
                assert deleteCardConfirmationDialog != null;
                deleteCardConfirmationDialog.dismiss();
            } else if (fromActivity == MAKE_EDIRHAM_DEFAULT) {
                assert deleteCardConfirmationDialog != null;
                deleteCardConfirmationDialog.dismiss();
                updateUserProfileRequest = new UpdateUserProfileRequest();
                updateUserProfileRequest.setChannel(2);
                updateUserProfileRequest.setUpdateType(8);
                updateUserProfileRequest.setPreferredPaymentMethod(E_DIRHAM);
                mPresenter.updatePaymentMethod(updateUserProfileRequest, E_DIRHAM);
            }
        }
    }

    @Override
    public void updatePaymentMethodForNI() {
        updateUserProfileRequest = new UpdateUserProfileRequest();
        updateUserProfileRequest.setChannel(2);
        updateUserProfileRequest.setUpdateType(8);
        updateUserProfileRequest.setPreferredPaymentMethod(PAYMENT_GW);
        mPresenter.updatePaymentMethod(updateUserProfileRequest, PAYMENT_GW);
    }

    @Override
    public void updatedUserDB(DBUserTC dbUserTC) {
        this.dbUserTC = dbUserTC;
    }

    @Override
    public void setCreditDataIntoList(List<CreditCardData> list, DBUserTC dbUserTC) {
        this.dbUserTC = dbUserTC;
        if (dbUserTC.getPreferredPaymentMethod().equalsIgnoreCase(E_DIRHAM)) {
            mCardListBinding.tvDefaultEDirham.setVisibility(View.VISIBLE);
            eDirhamSelected = true;
            mCardListBinding.ivThreeDotsEDirham.setVisibility(View.GONE);
            mCardListBinding.cvEDirham.setBackground(ContextCompat.getDrawable(this, R.drawable.selected_card_backgraound));
        } else {
            mCardListBinding.tvDefaultEDirham.setVisibility(View.INVISIBLE);
            mCardListBinding.ivThreeDotsEDirham.setVisibility(View.VISIBLE);
            mCardListBinding.cvEDirham.setBackground(ContextCompat.getDrawable(this, R.drawable.selected_card_backgraound_white));
        }
        if (list.isEmpty()) {
            mCardListBinding.llListOfCard.setVisibility(View.GONE);
            mCardListBinding.tvTitleAddCard.setVisibility(View.GONE);
            setVisibility(GONE, GONE);
        } else {
            setVisibility(GONE, VISIBLE);
            mCardListBinding.tvTitleAddCard.setVisibility(View.VISIBLE);
            mCardListBinding.llListOfCard.setVisibility(View.VISIBLE);
        }
        mAdapter.setList(list, dbUserTC);
        setContinueButtonVisibility();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void paymentFailedAction() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(SELECTED_PRODUCT, productsItem);
        bundle.putSerializable(AppConstants.IntentKey.EMAIL_RECIPIENTS, emailList);
        moveActivity(this, CheckoutActivity.class, true, true, bundle);
    }

    @Override
    public void paymentMethodUpdateSuccessfully() {
        showDeleteConfirmationMessage(PAYMENT_METHOD_UPDATED, getString(R.string.title_payment_method), getString(R.string.update_payment_method_msg), getString(R.string.txt_ok), null);
    }

    public String getExpiryDate(String expiryDate) {
        String modifiedExpiryDate = null;
        if (!TextUtils.isEmpty(expiryDate) && expiryDate.length() == 6) {
            modifiedExpiryDate = expiryDate.substring(0, 1);
            modifiedExpiryDate = "0" + modifiedExpiryDate + "/" + expiryDate.substring(2, 6) + "";
        } else if (!TextUtils.isEmpty(expiryDate) && expiryDate.length() == 7) {
            modifiedExpiryDate = expiryDate.substring(0, 2);
            modifiedExpiryDate = modifiedExpiryDate + "/" + expiryDate.substring(3, 7) + "";
        }
        assert modifiedExpiryDate != null;
        if (!TextUtils.isEmpty(expiryDate) && modifiedExpiryDate.length() == 7) {
            String date1 = modifiedExpiryDate.substring(3, 7);
            modifiedExpiryDate = date1 + "-" + modifiedExpiryDate.substring(0, 2);
        }
        return modifiedExpiryDate;
    }

    @Override
    public void openVerifyPaymentScreen(AddCardResponse addCardResponse, String screenFrom,
                                        BuyCreditReportResponse buyCreditReportResponse) {
    }

    public void openAdcbWebview(Bundle bundle, String transactionID) {
        this.transactionID = transactionID;
        moveActivity(CardListActivity.this, AdcbCardWebviewActivity.class, false, false,
                bundle, ADCB_CARD_REQUEST);
    }

    @Override
    public void goToCheckoutScreen() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(AppConstants.IntentKey.SELECTED_CARD, new Gson().toJson(selectedCard));
        resultIntent.putExtra(AppConstants.IntentKey.SELECTED_EDIRHAM, eDirhamSelected);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void showAuthorizationFailureError(String message, String status, String useCase) {
        String jsonResponse = Utilities.loadJSONFromAsset();
        if (!isNullOrEmpty(status)) {
            MessageResponse messageResponse = new Gson().fromJson(jsonResponse, MessageResponse.class);
            MessageItem messageItem = getMessageItem(messageResponse.getMessage(), status, useCase);
            if (messageItem != null) {
                if (App.getAppComponent().getDataManager().getCurrentLanguage().equals(ISO_CODE_ARABIC)) {
                    showErrorDialogForAuthorization(messageItem.getMessageAr(), status);
                } else {
                    showErrorDialogForAuthorization(messageItem.getMessageEn(), status);
                }
            } else {
                showErrorDialogForAuthorization(message, status);
            }
        } else {
            showErrorDialogForAuthorization(message, status);
        }
    }

    public void showErrorDialogForAuthorization(String message, String status) {
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.IntentKey.FROM_ACTIVITY,
                AUTHORIZATION_FAILED);
        if (status.equalsIgnoreCase("E01-0006") || status.equalsIgnoreCase("202")) {
            bundle.putBoolean(DESC_CLICK, true);
        }
        bundle.putString(AppConstants.IntentKey.TITLE_TEXT, getString(R.string.error));
        bundle.putString(AppConstants.IntentKey.SUB_TITLE_TEXT, message);
        bundle.putString(AppConstants.IntentKey.POSITIVE_TEXT, getString(R.string.txt_ok));
        BlurDialogBaseFragment paymentStatusDialog = ExitFragment.newInstance(bundle);
        paymentStatusDialog.setCancelable(false);
        paymentStatusDialog.show(getSupportFragmentManager(), paymentStatusDialog.getTag());
    }

    public void refreshCardList() {
        mPresenter.getLastLoginUser();
    }
}