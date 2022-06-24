package com.aecb.ui.addrecipients.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.base.mvp.BaseActivity;
import com.aecb.data.api.models.cards.CreditCardData;
import com.aecb.data.api.models.emailrecipients.EmailRecipients;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.databinding.ActivityAddRecipientsBinding;
import com.aecb.listeners.DialogButtonClickListener;
import com.aecb.presentation.customviews.ToolbarView;
import com.aecb.ui.addrecipients.adapter.EmailRecipientsListAdapter;
import com.aecb.ui.addrecipients.presenter.AddRecipientsContract;
import com.aecb.util.ItemOffsetDecoration;
import com.aecb.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.aecb.AppConstants.IntentKey.EMAIL_RECIPIENTS;
import static com.aecb.AppConstants.IntentKey.PRODUCT_DETAILS;
import static com.aecb.util.FirebaseLogging.cancelFromRecipient;

public class AddRecipientsActivity extends BaseActivity<AddRecipientsContract.View,
        AddRecipientsContract.Presenter> implements AddRecipientsContract.View, View.OnClickListener,
        TextWatcher {

    @Inject
    public AddRecipientsContract.Presenter mPresenter;
    public ArrayList<EmailRecipients> emailList = new ArrayList<>();
    ActivityAddRecipientsBinding addRecipientsBinding;
    ProductsItem productsItem;
    List<CreditCardData> cardDataList = new ArrayList<>();
    private EmailRecipientsListAdapter emailRecipientsListAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBinding();
        getBundleData();
    }

    private void getBundleData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            productsItem = (ProductsItem) bundle.getSerializable(PRODUCT_DETAILS);
            emailList = (ArrayList<EmailRecipients>) bundle.getSerializable(EMAIL_RECIPIENTS);
            if (!emailList.isEmpty()) {
                displayAlreadyAddedEmail();
            }
        }
    }

    @NonNull
    @Override
    public AddRecipientsContract.Presenter createPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_add_recipients;
    }

    @Override
    public void initToolbar() {
        ToolbarView tb = findViewById(R.id.toolBar);
        tb.setCancelBtnListener(this::onBackPressed);
        tb.setBackBtnListener(this::onBackPressed);
        tb.setBtnVisibility(true, true);
        tb.setToolbarTitleVisibility(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cancelFromRecipient();
    }

    private void enableBinding() {
        addRecipientsBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        addRecipientsBinding.rvEmailRecipients.setHasFixedSize(true);
        addRecipientsBinding.btnAddEmail.setOnClickListener(this);
        addRecipientsBinding.btnContinue.setOnClickListener(this);
        addRecipientsBinding.btnContinue.setClickable(false);
        addRecipientsBinding.tvSkip.setOnClickListener(this);
        addRecipientsBinding.edtEmail.addTextChangedListener(this);
        addRecipientsBinding.edtName.addTextChangedListener(this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        addRecipientsBinding.rvEmailRecipients.setLayoutManager(linearLayoutManager);
        addRecipientsBinding.rvEmailRecipients.addItemDecoration(new ItemOffsetDecoration(this, R.dimen._6sdp));
        editTextFocusListeners();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddEmail:
                handleAddButtonEvents();
                break;
            case R.id.btnContinue:
                Intent resultIntent = new Intent();
                resultIntent.putExtra(AppConstants.IntentKey.EMAIL_RECIPIENTS, emailList);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                break;
        }
    }

    private void handleAddButtonEvents() {
        boolean found = false;
        if (ValidationUtil.isNullOrEmpty(addRecipientsBinding.edtEmail.getText().toString())) {
            localValidationError(getString(R.string.error), getString(R.string.please_enter_email_address));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(addRecipientsBinding.edtEmail.getText().toString()).matches()) {
            localValidationError(getString(R.string.error), getString(R.string.please_enter_valid_email));
        } else if (!emailList.isEmpty()) {
            for (EmailRecipients object : emailList) {
                if (object.getEmail().equals(addRecipientsBinding.edtEmail.getText().toString())) {
                    localValidationError(getString(R.string.error), getString(R.string.you_have_already_added_email));
                    found = true;
                    break;
                }
            }
            if (!found) {
                addEmail();
            }
        } else {
            addEmail();
        }
    }

    private void displayAlreadyAddedEmail() {
        emailRecipientsListAdapter = new EmailRecipientsListAdapter(this, emailList);
        addRecipientsBinding.rvEmailRecipients.setAdapter(emailRecipientsListAdapter);
        enableDisableContinueButton();
    }

    private void addEmail() {
        EmailRecipients emailRecipients = new EmailRecipients();
        emailRecipients.setEmail(addRecipientsBinding.edtEmail.getText().toString());
        emailRecipients.setFullName(addRecipientsBinding.edtName.getText().toString());
        emailList.add(emailRecipients);
        emailRecipientsListAdapter = new EmailRecipientsListAdapter(this, emailList);
        addRecipientsBinding.rvEmailRecipients.setAdapter(emailRecipientsListAdapter);
        addRecipientsBinding.edtEmail.setText(null);
        addRecipientsBinding.edtName.setText(null);
        enableDisableContinueButton();
        enableDisableEditText();
        addRecipientsBinding.edtName.requestFocus();
    }

    public void enableDisableEditText() {
        if (emailList.size() >= 5) {
            addRecipientsBinding.edtName.setEnabled(false);
            addRecipientsBinding.edtEmail.setEnabled(false);
        } else {
            addRecipientsBinding.edtName.setEnabled(true);
            addRecipientsBinding.edtEmail.setEnabled(true);
        }
    }

    public void enableDisableContinueButton() {
       /* if (emailList.isEmpty()) {
            addRecipientsBinding.btnContinue.setBackgroundResource(R.drawable.disable_button);
            addRecipientsBinding.btnContinue.setClickable(false);
        } else {*/
        addRecipientsBinding.btnContinue.setBackgroundResource(R.drawable.bg_blue_button);
        addRecipientsBinding.btnContinue.setClickable(true);
        //}
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateEmail(String email, String name, int position) {
        boolean found = false;
        for (EmailRecipients object : emailList) {
            if (object.getEmail().equals(addRecipientsBinding.edtEmail.getText().toString())) {
                localValidationError(getString(R.string.error), getString(R.string.you_have_already_added_email));
                found = true;
                break;
            }
        }
        if (!found) {
            EmailRecipients emailRecipients = new EmailRecipients();
            emailRecipients.setEmail(email);
            emailRecipients.setFullName(name);
            emailList.set(position, emailRecipients);
            emailRecipientsListAdapter = new EmailRecipientsListAdapter(this, emailList);
            addRecipientsBinding.rvEmailRecipients.setAdapter(emailRecipientsListAdapter);
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!addRecipientsBinding.edtName.getText().toString().isEmpty() &&
                !addRecipientsBinding.edtEmail.getText().toString().isEmpty()) {
            addRecipientsBinding.btnAddEmail.setBackgroundResource(R.drawable.bg_round_corner_blue);
            addRecipientsBinding.btnAddEmail.setClickable(true);
            addRecipientsBinding.btnAddEmail.setTextColor(getResources().getColor(R.color.border_color));
        } else {
            addRecipientsBinding.btnAddEmail.setBackgroundResource(R.drawable.blue_corner_disabled);
            addRecipientsBinding.btnAddEmail.setClickable(false);
            addRecipientsBinding.btnAddEmail.setTextColor(getResources().getColor(R.color.gray_light));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    void editTextFocusListeners() {
        addRecipientsBinding.edtName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                addRecipientsBinding.txtInputLoutName.setHint(getString(R.string.hint_title_full_name));
            } else if (!addRecipientsBinding.edtName.getText().toString().isEmpty()) {
                addRecipientsBinding.txtInputLoutName.setHint(getString(R.string.hint_title_full_name));
            } else {
                addRecipientsBinding.txtInputLoutName.setHint(getString(R.string.hint_full_name_of_add_card));
            }
        });
        addRecipientsBinding.edtEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                addRecipientsBinding.txtInputLoutEmail.setHint(getString(R.string.email_address));
            } else if (!addRecipientsBinding.edtEmail.getText().toString().isEmpty()) {
                addRecipientsBinding.txtInputLoutEmail.setHint(getString(R.string.email_address));
            } else {
                addRecipientsBinding.txtInputLoutEmail.setHint(getString(R.string.email_address));
            }
        });
    }

    @Override
    public void setCardList(List<CreditCardData> list) {
        cardDataList.clear();
        cardDataList.addAll(list);
    }

    public void showDeleteConfirmation(int position) {
        showErrorMsgFromApi(getString(R.string.aecb), getString(R.string.are_you_sure_delete),
                getString(R.string.yes), getString(R.string.no), new DialogButtonClickListener() {
                    @Override
                    public void onPositiveButtonClicked() {
                        emailRecipientsListAdapter.deleteItem(position);
                        enableDisableContinueButton();
                        enableDisableEditText();
                    }

                    @Override
                    public void onNegativeButtonClicked() {
                    }
                });
    }
}
