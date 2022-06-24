package com.aecb.ui.checkout.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;

import com.aecb.AppConstants;
import com.aecb.BuildConfig;
import com.aecb.R;
import com.aecb.base.mvp.BaseActivity;
import com.aecb.base.mvp.BlurDialogBaseFragment;
import com.aecb.data.api.models.buycreditreport.BuyCreditReportResponse;
import com.aecb.data.api.models.cards.CreditCardData;
import com.aecb.data.api.models.edirham.CheckoutStatusRequest;
import com.aecb.data.api.models.emailrecipients.EmailRecipients;
import com.aecb.data.api.models.emailrecipients.EmailRecipientsRequest;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.databinding.ActivityCheckoutBinding;
import com.aecb.listeners.ToolbarListener;
import com.aecb.presentation.customviews.ToolbarView;
import com.aecb.ui.addrecipients.view.AddRecipientsActivity;
import com.aecb.ui.checkout.presenter.CheckoutContract;
import com.aecb.ui.creditreport.creditreport.view.CreditReportFragment;
import com.aecb.ui.dashboard.view.DashboardActivity;
import com.aecb.ui.edirhamwebview.view.EDirhamWebviewActivity;
import com.aecb.ui.exit.view.ExitFragment;
import com.aecb.ui.purchasejourney.cardlist.view.CardListActivity;
import com.aecb.util.varibles.StringConstants;
import com.facebook.ads.sdk.serverside.CustomData;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;
import com.kochava.base.Tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

import static com.aecb.App.getFirebaseAnalytics;
import static com.aecb.AppConstants.ActivityIntentCode.ADD_RECIPIENTS_CODE;
import static com.aecb.AppConstants.ActivityIntentCode.CHANGE_CARD_CODE;
import static com.aecb.AppConstants.ActivityIntentCode.EDIRHAM_REQUEST;
import static com.aecb.AppConstants.CARD_PREFIX;
import static com.aecb.AppConstants.COLON;
import static com.aecb.AppConstants.COMMA;
import static com.aecb.AppConstants.CardTypes.CIRRUS;
import static com.aecb.AppConstants.CardTypes.MASTER_CARD;
import static com.aecb.AppConstants.CardTypes.VISA;
import static com.aecb.AppConstants.IntentKey.API_PRODUCTS;
import static com.aecb.AppConstants.IntentKey.CALL_API_TYPE;
import static com.aecb.AppConstants.IntentKey.EMAIL_RECIPIENTS;
import static com.aecb.AppConstants.IntentKey.FROM_ACTIVITY;
import static com.aecb.AppConstants.IntentKey.FROM_ADD_RECIPIENTS;
import static com.aecb.AppConstants.IntentKey.PRODUCT_DETAILS;
import static com.aecb.AppConstants.IntentKey.PRODUCT_ID;
import static com.aecb.AppConstants.IntentKey.SELECTED_CARD;
import static com.aecb.AppConstants.IntentKey.SELECTED_EDIRHAM;
import static com.aecb.AppConstants.IntentKey.SELECTED_PRODUCT;
import static com.aecb.AppConstants.IntentKey.SHOW_SCORE_LOADING;
import static com.aecb.AppConstants.PERCENTAGE;
import static com.aecb.AppConstants.PaymentMethods.E_DIRHAM;
import static com.aecb.AppConstants.YES;
import static com.aecb.AppConstants.arabicToDecimal;
import static com.aecb.AppConstants.doubleDecimalValue;
import static com.aecb.util.ConversionApi.callConversionApi;
import static com.aecb.util.FirebaseLogging.addedPaymentDetails;
import static com.aecb.util.FirebaseLogging.addedRecipients;
import static com.aecb.util.FirebaseLogging.addedRemovedProducts;
import static com.aecb.util.FirebaseLogging.cancelFromCheckout;
import static com.aecb.util.varibles.StringConstants.SPACE;

public class CheckoutActivity extends BaseActivity<CheckoutContract.View, CheckoutContract.Presenter>
        implements CheckoutContract.View, View.OnClickListener {

    @Inject
    public CheckoutContract.Presenter mPresenter;
    public ArrayList<EmailRecipients> emailList;
    ActivityCheckoutBinding checkoutBinding;
    List<ProductsItem> productsList = new ArrayList<>();
    ProductsItem selectedProduct;
    ProductsItem upgradableProductWith, upgradableProductTo;
    ProductsItem finalProduct;
    private CreditCardData selectedCard;
    private BlurDialogBaseFragment creditReportFragment;
    String serverDate, expiryAsPerCard;
    boolean isDefault;
    private BlurDialogBaseFragment paymentStatusDialog;
    BuyCreditReportResponse buyCreditReportResponse;
    Handler handler;
    boolean isDefaultAvailable = false;
    boolean isAppFree;
    AppEventsLogger logger;
    String checkoutId = "";
    DBUserTC dbUserTC;
    boolean isEDirhamSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBinding();
        getBundleData();
        mPresenter.getLastLoginUser();
        mPresenter.getConfiguration();
        logger = AppEventsLogger.newLogger(this);
    }

    void getBundleData() {
        emailList = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        selectedProduct = (ProductsItem) bundle.getSerializable(SELECTED_PRODUCT);
        if (bundle.getSerializable(EMAIL_RECIPIENTS) != null) {
            ArrayList allEmailList = (ArrayList<EmailRecipients>) bundle.getSerializable(EMAIL_RECIPIENTS);
            emailList.clear();
            emailList.addAll(allEmailList);
            handleEmailListItems();
        }
    }

    @NonNull
    @Override
    public CheckoutContract.Presenter createPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_checkout;
    }

    @Override
    public void initToolbar() {
        ToolbarView tb = findViewById(R.id.toolBar);
        tb.setCancelBtnListener(this::onBackPressed);
        tb.setBtnVisibility(true, true);
        tb.setBackBtnListener(new ToolbarListener() {
            @Override
            public void backInvoke() {
                onBackPressed();
            }
        });
        tb.setToolbarTitleVisibility(false);
    }

    @Override
    public void onBackPressed() {
        cancelFromCheckout();
        moveActivity(CheckoutActivity.this, DashboardActivity.class, true, true);
    }

    private void enableBinding() {
        checkoutBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        checkoutBinding.tvViewAll.setOnClickListener(this);
        checkoutBinding.btnAddProduct.setOnClickListener(this);
        checkoutBinding.tvRemove.setOnClickListener(this);
        checkoutBinding.btnContinue.setOnClickListener(this);
        checkoutBinding.ivCreditScoreOnlyInfo.setOnClickListener(this);
        checkoutBinding.btnAddRecipient.setOnClickListener(this);
        checkoutBinding.tvChangeCard.setOnClickListener(this);
        mPresenter.getProducts();
    }

    @SuppressLint("SetTextI18n")
    void displayInitialData() {
        getUpgradableWithToProducts();
        finalProduct = selectedProduct;
        String selectedProductTitle = "";
        String selectedProductDesc = "";
        if (mPresenter.getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ARABIC)) {
            selectedProductTitle = selectedProduct.getTitleAr();
            selectedProductDesc = selectedProduct.getShortDescAr();
        } else {
            selectedProductTitle = selectedProduct.getTitleEn();
            selectedProductDesc = selectedProduct.getShortdescEn();
        }
        checkoutBinding.tvProductTitle.setText(selectedProductTitle);
        checkoutBinding.tvProductDesc.setText(selectedProductDesc);
        double priceWithVAT = (selectedProduct.getPrice() * selectedProduct.getVat()) / 100;
        checkoutBinding.tvProductPrice.setText(arabicToDecimal(doubleDecimalValue(2, (selectedProduct.getPrice() + priceWithVAT))));
        checkoutBinding.tvSummaryTitle.setText(selectedProductTitle);
        checkoutBinding.tvSummaryPrice.setText(getString(R.string.aed) + SPACE + arabicToDecimal(doubleDecimalValue(2, selectedProduct.getPrice())));
        checkoutBinding.tvSummaryVatTitle.setText(getString(R.string.txt_vat) + SPACE + arabicToDecimal(doubleDecimalValue(2, selectedProduct.getVat())) + SPACE + PERCENTAGE);
        double priceVAT = (selectedProduct.getPrice() * selectedProduct.getVat()) / 100;
        checkoutBinding.tvSummaryVatPrice.setText(getString(R.string.aed) + SPACE + arabicToDecimal(doubleDecimalValue(2, priceVAT)));
        checkoutBinding.tvSummaryTotal.setText(getString(R.string.aed) + SPACE + arabicToDecimal(doubleDecimalValue(2, (selectedProduct.getPrice() + priceWithVAT))));
        if (selectedProduct.getUpgradeableTo().equals("")) {
            checkoutBinding.llAddProduct.setVisibility(View.GONE);
        } else {
                checkoutBinding.llAddProduct.setVisibility(View.VISIBLE);
                double upgradableWithPriceWithVAT = (upgradableProductWith.getPrice() * upgradableProductWith.getVat()) / 100;
                double upgradableToPriceWithVAT = (upgradableProductTo.getPrice() * upgradableProductTo.getVat()) / 100;

                if (mPresenter.getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ARABIC)) {
                    checkoutBinding.tvOtherProductPrice.setText(getString(R.string.add) + SPACE + upgradableProductTo.getTitleAr()
                            + SPACE + getString(R.string.txt_for) + SPACE + getString(R.string.aed) + SPACE +
                            (arabicToDecimal(doubleDecimalValue(2, (upgradableProductTo.getPrice() + upgradableToPriceWithVAT) - (selectedProduct.getPrice() + priceWithVAT)))
                                    + SPACE + getString(R.string.only)));
                } else {
                    checkoutBinding.tvOtherProductPrice.setText(getString(R.string.add) + SPACE + upgradableProductTo.getTitleEn()
                            + SPACE + getString(R.string.txt_for) + SPACE + getString(R.string.aed) + SPACE +
                            (arabicToDecimal(doubleDecimalValue(2, (upgradableProductTo.getPrice() + upgradableToPriceWithVAT) - (selectedProduct.getPrice() + priceWithVAT)))
                                    + SPACE + getString(R.string.only)));
                }
                checkoutBinding.tvProductRegularPrice.setText(getString(R.string.regular_price) + SPACE + getString(R.string.aed) + SPACE +
                        arabicToDecimal(doubleDecimalValue(2, ((upgradableProductWith.getPrice() + upgradableWithPriceWithVAT) +
                                (selectedProduct.getPrice() + priceWithVAT)))));
        }
    }


    void getUpgradableWithToProducts() {

        for (ProductsItem productsItem : productsList) {
            if (selectedProduct.getUpgradeableTo().equals(productsItem.getProductNumber())) {
                upgradableProductTo = productsItem;
            }

            if (selectedProduct.getUpgradeWith().equals(productsItem.getProductNumber())) {
                upgradableProductWith = productsItem;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddProduct:
                addedRemovedProducts();
                addUpgradableProduct();
                break;
            case R.id.tvRemove:
                addedRemovedProducts();
                removeUpgradableProduct();
                break;
            case R.id.tvViewAll:
                if (finalProduct != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PRODUCT_DETAILS, finalProduct);
                    bundle.putSerializable(AppConstants.IntentKey.EMAIL_RECIPIENTS, emailList);
                    moveActivity(this, AddRecipientsActivity.class, false, false, bundle,
                            ADD_RECIPIENTS_CODE);
                }
                break;
            case R.id.ivCreditScoreOnlyInfo:
                openCreditScore();
                break;
            case R.id.btnAddRecipient:
                addedRecipients();
                Intent intent = new Intent(CheckoutActivity.this, AddRecipientsActivity.class);
                startActivityForResult(intent, ADD_RECIPIENTS_CODE);
                break;
            case R.id.tvChangeCard:
                if (finalProduct != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PRODUCT_DETAILS, finalProduct);
                    bundle.putSerializable(AppConstants.IntentKey.EMAIL_RECIPIENTS, emailList);
                    bundle.putString(FROM_ACTIVITY, FROM_ADD_RECIPIENTS);
                    moveActivity(this, CardListActivity.class, false, false, bundle,
                            CHANGE_CARD_CODE);
                }
                break;
            case R.id.btnContinue:
                if (isAppFree) {
                    mPresenter.callBuyCreditReportForFree(finalProduct);
                } else if (isEDirhamSelected) {
                    double upgradableToPriceWithVAT = (finalProduct.getPrice() * finalProduct.getVat()) / 100;
                    String price = arabicToDecimal(doubleDecimalValue(2, (finalProduct.getPrice() + upgradableToPriceWithVAT)));
                    mPresenter.startEDirhamPurchase(finalProduct, price);
                } else {
                    if (selectedCard != null) {
                        if (selectedCard.getPymtCardIsDefault() != null &&
                                selectedCard.getPymtCardIsDefault().equalsIgnoreCase(StringConstants.CONST_YES)) {
                            isDefault = true;
                        }
                        mPresenter.startPurchase(selectedCard.getCardnumber().replaceAll("\\*", "x"),
                                serverDate,
                                selectedCard.getCardholdername(),
                                true, true, true,
                                isDefault, finalProduct, AppConstants.AddCardOperationType.PAYMENT_OLD_CARD);
                    } else {
                        addedPaymentDetails();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(PRODUCT_DETAILS, finalProduct);
                        bundle.putSerializable(AppConstants.IntentKey.EMAIL_RECIPIENTS, emailList);
                        bundle.putString(FROM_ACTIVITY, FROM_ADD_RECIPIENTS);
                        moveActivity(this, CardListActivity.class, false, false, bundle,
                                CHANGE_CARD_CODE);
                    }
                }
                break;
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

    private void openCreditScore() {
        if (finalProduct != null) {
            Bundle bundle = new Bundle();
            bundle.putString(CALL_API_TYPE, API_PRODUCTS);
            bundle.putString(PRODUCT_ID, finalProduct.getId());
            creditReportFragment = CreditReportFragment.newInstance(bundle);
            creditReportFragment.show(getSupportFragmentManager(), creditReportFragment.getTag());
        }
    }

    @SuppressLint("SetTextI18n")
    private void addUpgradableProduct() {
        try {
            finalProduct = upgradableProductTo;
            String selectedProductTitle = "";
            String selectedProductShortDesc = "";
            if (mPresenter.getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ARABIC)) {
                selectedProductTitle = upgradableProductTo.getTitleAr();
                selectedProductShortDesc = upgradableProductTo.getShortDescAr();
            } else {
                selectedProductTitle = upgradableProductTo.getTitleEn();
                selectedProductShortDesc = upgradableProductTo.getShortdescEn();
            }
            checkoutBinding.llAddProduct.setVisibility(View.GONE);
            checkoutBinding.llProductAdded.setVisibility(View.VISIBLE);
            checkoutBinding.tvProductTitle.setText(selectedProductTitle);
            checkoutBinding.tvProductDesc.setText(selectedProductShortDesc);
            double upgradableToPriceWithVAT = (upgradableProductTo.getPrice() * upgradableProductTo.getVat()) / 100;
            double selectedProductPriceWithVAT = (selectedProduct.getPrice() * selectedProduct.getVat()) / 100;
            checkoutBinding.tvProductPrice.setText(arabicToDecimal(doubleDecimalValue(2, (upgradableProductTo.getPrice() + upgradableToPriceWithVAT))));
            double priceOfVATUpgradableWith = (upgradableProductWith.getPrice() * upgradableProductWith.getVat()) / 100;
            if (mPresenter.getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ARABIC)) {
                checkoutBinding.tvProductNameWithPrice.setText(upgradableProductTo.getTitleAr() + SPACE +
                        COLON + SPACE + getString(R.string.aed) + SPACE + arabicToDecimal(doubleDecimalValue(2, (upgradableProductTo.getPrice() + upgradableToPriceWithVAT) - (selectedProduct.getPrice() + selectedProductPriceWithVAT))));
            } else {
                checkoutBinding.tvProductNameWithPrice.setText(upgradableProductTo.getTitleEn() + SPACE +
                        COLON + SPACE + getString(R.string.aed) + SPACE + arabicToDecimal(doubleDecimalValue(2, (upgradableProductTo.getPrice() + upgradableToPriceWithVAT) - (selectedProduct.getPrice() + selectedProductPriceWithVAT))));
            }
            checkoutBinding.tvSummaryTitle.setText(selectedProductTitle);
            checkoutBinding.tvSummaryPrice.setText(getString(R.string.aed) + SPACE + arabicToDecimal(doubleDecimalValue(2, upgradableProductTo.getPrice())));
            checkoutBinding.tvSummaryVatTitle.setText(getString(R.string.txt_vat) + SPACE + arabicToDecimal(doubleDecimalValue(2, upgradableProductTo.getVat())) + SPACE + PERCENTAGE);
            double priceVATUpgradableTo = (upgradableProductTo.getPrice() * upgradableProductTo.getVat()) / 100;
            checkoutBinding.tvSummaryVatPrice.setText(getString(R.string.aed) + SPACE + arabicToDecimal(doubleDecimalValue(2, priceVATUpgradableTo)));
            checkoutBinding.tvSummaryTotal.setText(getString(R.string.aed) + SPACE + arabicToDecimal(doubleDecimalValue(2, (upgradableProductTo.getPrice() + upgradableToPriceWithVAT))));
        } catch (Exception e) {
            Timber.e(e.getMessage());
        }

    }

    @SuppressLint("SetTextI18n")
    private void removeUpgradableProduct() {
        String selectedProductTitle = "";
        String selectedProductDesc = "";
        if (mPresenter.getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ARABIC)) {
            selectedProductTitle = selectedProduct.getTitleAr();
            selectedProductDesc = selectedProduct.getShortDescAr();
        } else {
            selectedProductTitle = selectedProduct.getTitleEn();
            selectedProductDesc = selectedProduct.getShortdescEn();
        }
        finalProduct = selectedProduct;
        checkoutBinding.llAddProduct.setVisibility(View.VISIBLE);
        checkoutBinding.llProductAdded.setVisibility(View.GONE);
        checkoutBinding.tvProductTitle.setText(selectedProductTitle);
        checkoutBinding.tvProductDesc.setText(selectedProductDesc);
        double priceWithVAT = (selectedProduct.getPrice() * selectedProduct.getVat()) / 100;
        checkoutBinding.tvProductPrice.setText(arabicToDecimal(doubleDecimalValue(2, (selectedProduct.getPrice() + priceWithVAT))));
        checkoutBinding.tvSummaryTitle.setText(selectedProductTitle);
        checkoutBinding.tvSummaryPrice.setText(getString(R.string.aed) + SPACE + arabicToDecimal(doubleDecimalValue(2, selectedProduct.getPrice())));
        checkoutBinding.tvSummaryVatTitle.setText(getString(R.string.txt_vat) + SPACE + arabicToDecimal(doubleDecimalValue(2, selectedProduct.getVat())) + SPACE + PERCENTAGE);
        double priceVAT = (selectedProduct.getPrice() * selectedProduct.getVat()) / 100;
        checkoutBinding.tvSummaryVatPrice.setText(getString(R.string.aed) + SPACE + arabicToDecimal(doubleDecimalValue(2, priceVAT)));
        checkoutBinding.tvSummaryTotal.setText(getString(R.string.aed) + SPACE + arabicToDecimal(doubleDecimalValue(2, (selectedProduct.getPrice() + priceWithVAT))));
    }

    @Override
    public void setProductList(List<ProductsItem> productList, String currentLanguage) {
        this.productsList.addAll(productList);
        displayInitialData();
    }


    @Override
    public void setCardList(List<CreditCardData> cardList, DBUserTC dbUserTC) {
        //navya crashlytics
        try {
            this.dbUserTC = dbUserTC;
            if (isAppFree) {
                checkoutBinding.llPaymentMethod.setVisibility(View.GONE);
                checkoutBinding.btnContinue.setText(getString(R.string.purchase_now));
            } else if (dbUserTC.getPreferredPaymentMethod().equalsIgnoreCase(E_DIRHAM)) {
                isEDirhamSelected = true;
                checkoutBinding.llPaymentMethod.setVisibility(View.VISIBLE);
                checkoutBinding.cvEDirham.setVisibility(View.VISIBLE);
                checkoutBinding.cvCardList.setVisibility(View.GONE);
                checkoutBinding.btnContinue.setText(getString(R.string.purchase_now));
                checkoutBinding.tvDefaultEDirham.setVisibility(View.VISIBLE);
                checkoutBinding.cvEDirham.setBackground(ContextCompat.getDrawable(this, R.drawable.selected_card_backgraound));
            } else {
                if (!cardList.isEmpty()) {
                    for (CreditCardData cardData : cardList) {
                        if (cardData.getPymtCardIsDefault().equalsIgnoreCase(YES)) {
                            isDefaultAvailable = true;
                            break;
                        }
                    }
                    if (isDefaultAvailable) {
                        checkoutBinding.llPaymentMethod.setVisibility(View.VISIBLE);
                        checkoutBinding.btnContinue.setText(getString(R.string.purchase_now));
                    } else {
                        checkoutBinding.llPaymentMethod.setVisibility(View.GONE);
                        checkoutBinding.btnContinue.setText(getString(R.string.add_payment_details));
                    }
                    for (CreditCardData creditCardData : cardList) {
                        if (creditCardData.getPymtCardIsDefault().equalsIgnoreCase(YES)) {
                            selectedCard = creditCardData;
                            setSelectedCard(creditCardData);
                            displayCardLogo(creditCardData.getCardType());
                            checkoutBinding.tvCardType.setText(creditCardData.getCardType());
                            checkoutBinding.tvCardType.setVisibility(View.GONE);
                            checkoutBinding.tvCardIsDefault.setVisibility(View.VISIBLE);
                            String last4NumberOfCard = creditCardData.getCardnumber().substring(creditCardData.getCardnumber().length() - 4);
                            checkoutBinding.tvCardNumber.setText(CARD_PREFIX + last4NumberOfCard);
                            checkoutBinding.cvCardList.setBackground(ContextCompat.getDrawable(this, R.drawable.selected_card_backgraound));
                        }
                    }
                } else {
                    checkoutBinding.llPaymentMethod.setVisibility(View.GONE);
                    checkoutBinding.btnContinue.setText(getString(R.string.add_payment_details));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayCardLogo(String cardType) {
        switch (cardType) {
            case MASTER_CARD:
                checkoutBinding.ivCardLogo.setImageResource(R.drawable.ic_master_card);
                break;
            case VISA:
                checkoutBinding.ivCardLogo.setImageResource(R.drawable.ic_visa);
                break;
            case CIRRUS:
                checkoutBinding.ivCardLogo.setImageResource(R.drawable.ic_cirrus);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_RECIPIENTS_CODE && resultCode == RESULT_OK) {
            ArrayList allEmailList = (ArrayList<EmailRecipients>) data.getSerializableExtra(EMAIL_RECIPIENTS);
            emailList.clear();
            emailList.addAll(allEmailList);
            handleEmailListItems();

        } else if (requestCode == CHANGE_CARD_CODE && resultCode == RESULT_OK) {
            isEDirhamSelected = data.getBooleanExtra(SELECTED_EDIRHAM, false);
            if (data.getBooleanExtra(SELECTED_EDIRHAM, false)) {
                checkoutBinding.llPaymentMethod.setVisibility(View.VISIBLE);
                checkoutBinding.cvEDirham.setVisibility(View.VISIBLE);
                checkoutBinding.cvCardList.setVisibility(View.GONE);
                checkoutBinding.cvEDirham.setBackground(ContextCompat.getDrawable(this, R.drawable.selected_card_backgraound));
                checkoutBinding.btnContinue.setText(getString(R.string.purchase_now));
            } else {
                checkoutBinding.cvEDirham.setVisibility(View.GONE);
                checkoutBinding.cvCardList.setVisibility(View.VISIBLE);
                checkoutBinding.llPaymentMethod.setVisibility(View.VISIBLE);
                checkoutBinding.btnContinue.setText(getString(R.string.purchase_now));
                selectedCard = new Gson().fromJson(data.getStringExtra(SELECTED_CARD), CreditCardData.class);
                setSelectedCard(selectedCard);
                displayCardLogo(selectedCard.getCardType());
                checkoutBinding.tvCardType.setText(selectedCard.getCardType());
                checkoutBinding.tvCardType.setVisibility(View.GONE);
                String last4NumberOfCard = selectedCard.getCardnumber().substring(selectedCard.getCardnumber().length() - 4);
                checkoutBinding.tvCardNumber.setText(CARD_PREFIX + last4NumberOfCard);
                if (selectedCard.getPymtCardIsDefault().equalsIgnoreCase(YES))
                    checkoutBinding.tvCardIsDefault.setVisibility(View.VISIBLE);
                else checkoutBinding.tvCardIsDefault.setVisibility(View.INVISIBLE);
                checkoutBinding.cvCardList.setBackground(ContextCompat.getDrawable(this, R.drawable.selected_card_backgraound));
            }
        } else if (requestCode == EDIRHAM_REQUEST && (resultCode == RESULT_OK || resultCode == RESULT_CANCELED)) {
            CheckoutStatusRequest checkoutStatusRequest = new CheckoutStatusRequest();
            checkoutStatusRequest.setCheckoutId(checkoutId);
            if (BuildConfig.BUILD_TYPE.equalsIgnoreCase("prod")) {
                checkoutStatusRequest.setPassword("YT78nm5op3sn9ws1mL0ceg0vs2grh7");
            } else {
                checkoutStatusRequest.setPassword("05052020");
            }
            checkoutStatusRequest.setUserName("AECB");
            mPresenter.getCheckOutStatus(checkoutStatusRequest);
        }
    }


    private void handleEmailListItems() {
        if (emailList.isEmpty()) {
            checkoutBinding.llAddRecipients.setVisibility(View.VISIBLE);
            checkoutBinding.llListRecipients.setVisibility(View.GONE);
        } else {
            checkoutBinding.llAddRecipients.setVisibility(View.GONE);
            checkoutBinding.llListRecipients.setVisibility(View.VISIBLE);
            displayEmailRecipientsName();
        }
    }

    @SuppressLint("SetTextI18n")
    private void displayEmailRecipientsName() {
        int size = emailList.size();
        if (size == 1) {
            checkoutBinding.tvRecipientsNames.setText(getString(R.string.your_report_will_be_sent_to) + SPACE +
                    emailList.get(0).getFullName());
        } else if (size == 2) {
            checkoutBinding.tvRecipientsNames.setText(getString(R.string.your_report_will_be_sent_to) + SPACE +
                    emailList.get(0).getFullName() + COMMA + SPACE + emailList.get(1).getFullName());
        } else if (size > 2) {
            checkoutBinding.tvRecipientsNames.setText(getString(R.string.your_report_will_be_sent_to) + SPACE +
                    emailList.get(0).getFullName() + COMMA + SPACE + emailList.get(1).getFullName() + SPACE +
                    getString(R.string.shared_with_others, size - 2));
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
        paymentSuccessAction();
    }

    void callSendEmailApi() {
        EmailRecipientsRequest recipientsRequest = new EmailRecipientsRequest();
        recipientsRequest.setApplicationId(buyCreditReportResponse.getBuyCreditReportData().getPortalUserId());
        recipientsRequest.setAdditionalReceivers(emailList);
        if (emailList != null && !emailList.isEmpty())
            mPresenter.callSendAdditionalEmailsApi(recipientsRequest);
    }

    private void paymentSuccessAction() {
        sendFbMakePaymentEvent();
        sendProductEvent();
        handler = new Handler();
        handler.postDelayed(() -> {
            openDashboardScreen();
        }, 3000);
    }

    private void sendFbMakePaymentEvent() {
        String paymentAmount = "";
        try {
            double priceWithVAT = (finalProduct.getPrice() * finalProduct.getVat()) / 100;
            paymentAmount = arabicToDecimal(doubleDecimalValue(2, (finalProduct.getPrice() + priceWithVAT)));
        } catch (Exception e) {
            Timber.e(e.getMessage());
        }
        if (mPresenter.getInstallSource().equalsIgnoreCase("") &&
                mPresenter.getInstalledCampaignId().equalsIgnoreCase("")) {
            Bundle paramsSpecificAndroid = new Bundle();
            paramsSpecificAndroid.putString("PaymentAndroid", paymentAmount);
            logger.logEvent(AppConstants.Events.MAKE_PAYMENT_ANDROID, paramsSpecificAndroid);
            //logger.logEvent(AppEventsConstants.EVENT_NAME_PURCHASED, paramsSpecificAndroid);
            getFirebaseAnalytics().logEvent(AppConstants.Events.MAKE_PAYMENT_ANDROID, paramsSpecificAndroid);
            CustomData customData = new CustomData();
            HashMap<String, String> data = new HashMap<>();
            data.put("PaymentAndroid", paymentAmount);
            customData.setCustomProperties(data);
            callConversionApi(customData, AppConstants.Events.MAKE_PAYMENT_ANDROID);
            Tracker.sendEvent(new Tracker.Event(AppConstants.Events.MAKE_PAYMENT_ANDROID));

            Bundle params = new Bundle();
            params.putString("Payment", paymentAmount);
            logger.logEvent(AppConstants.Events.MAKE_PAYMENT, params);
            //logger.logEvent(AppEventsConstants.EVENT_NAME_PURCHASED, params);
            getFirebaseAnalytics().logEvent(AppConstants.Events.MAKE_PAYMENT, params);
            CustomData customDataNew = new CustomData();
            HashMap<String, String> dataNew = new HashMap<>();
            data.put("Payment", paymentAmount);
            customData.setCustomProperties(data);
            callConversionApi(customDataNew, AppConstants.Events.MAKE_PAYMENT);
            Tracker.sendEvent(new Tracker.Event(AppConstants.Events.MAKE_PAYMENT));
        } else {
            Bundle params = new Bundle();
            params.putString("Source", mPresenter.getInstallSource());
            params.putString("campaignID", mPresenter.getInstalledCampaignId());
            params.putString("Payment", paymentAmount);
            logger.logEvent(AppConstants.Events.MAKE_PAYMENT, params);
            //logger.logEvent(AppEventsConstants.EVENT_NAME_PURCHASED, params);
            getFirebaseAnalytics().logEvent(AppConstants.Events.MAKE_PAYMENT, params);
            CustomData customData = new CustomData();
            HashMap<String, String> data = new HashMap<>();
            data.put("Source", mPresenter.getInstallSource());
            data.put("campaignID", mPresenter.getInstalledCampaignId());
            data.put("Payment", paymentAmount);
            customData.setCustomProperties(data);
            callConversionApi(customData, AppConstants.Events.MAKE_PAYMENT);
            Tracker.sendEvent(new Tracker.Event(AppConstants.Events.MAKE_PAYMENT));
        }
    }

    public void sendProductEvent() {
        String selectedProductTitle = finalProduct.getTitleEn();
        try {
            selectedProductTitle = finalProduct.getTitleEn().replaceAll("\\s", "_");
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.logEvent(selectedProductTitle, null);
        getFirebaseAnalytics().logEvent(selectedProductTitle, null);
        Tracker.sendEvent(new Tracker.Event(selectedProductTitle));
    }

    public void openDashboardScreen() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(SHOW_SCORE_LOADING, true);
        moveActivity(this, DashboardActivity.class, true, true, bundle);
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
        if (fromActivity == AppConstants.ActivityIntentCode.PAYMENT_SUCCESSFULLY_SAVED) {
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
                openDashboardScreen();
            }
        }
    }

    @Override
    public LifecycleOwner getLifeCycleOwner() {
        return this;
    }

    @Override
    public void setIsAppFree(boolean isAppFree) {
        this.isAppFree = isAppFree;
    }

    @Override
    public void openEDirhamWebview(String checkoutId, String checkoutUrl) {
        if (checkoutId != null && checkoutUrl != null) {
            this.checkoutId = checkoutId;
            Bundle bundle = new Bundle();
            bundle.putString(AppConstants.IntentKey.CHECKOUT_URL, checkoutUrl);
            bundle.putString(AppConstants.IntentKey.CHECKOUT_ID, checkoutId);
            moveActivity(CheckoutActivity.this, EDirhamWebviewActivity.class, false, false, bundle, EDIRHAM_REQUEST);
        } else {
            showApiFailureError(getString(R.string.serverError), "", "");
        }
    }

    @Override
    public void showBlurView() {
        checkoutBinding.blurView.setVisibility(View.VISIBLE);
        checkoutBinding.rlLoadingView.setVisibility(View.VISIBLE);
        enableDisableView(checkoutBinding.parentView, false);
    }

    @Override
    public void hideBlurView() {
        checkoutBinding.blurView.setVisibility(View.GONE);
        checkoutBinding.rlLoadingView.setVisibility(View.GONE);
        enableDisableView(checkoutBinding.parentView, true);
    }

    public static void enableDisableView(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int idx = 0; idx < group.getChildCount(); idx++) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }
    }

    @Override
    public void scoreNotAvailableError() {
        localValidationError(getString(R.string.error),getString(R.string.score_not_available));
    }
}