package com.aecb.ui.contactusregistration.view;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;

import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.base.mvp.BlurDialogBaseFragment;
import com.aecb.data.api.models.configuration.CaseTypesItem;
import com.aecb.data.api.models.contactus.ContactUsSubmitRequest;
import com.aecb.data.api.models.registeruser.RegisterUserRequest;
import com.aecb.databinding.FragmentContactUsRegistrationBinding;
import com.aecb.presentation.blurcustomview.BlurConfig;
import com.aecb.presentation.blurcustomview.SmartAsyncPolicyHolder;
import com.aecb.ui.contactusregistration.presenter.ContactUsRegistrationContract;
import com.aecb.ui.loginflow.login.view.LoginActivity;
import com.aecb.ui.registerflow.securityquestionsroundone.view.SecurityQuestionsRoundOneActivity;
import com.aecb.ui.registerflow.securityquestionsroundtwo.view.SecurityQuestionsRoundTwoActivity;
import com.aecb.util.ScreenUtils;
import com.aecb.util.Utilities;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.aecb.AppConstants.AppLanguage.ISO_CODE_ENG;
import static com.aecb.AppConstants.IntentKey.MOBILE;
import static com.aecb.AppConstants.IntentKey.REGISTER_REQUEST;
import static com.aecb.AppConstants.MOBILE_NUMBER_PREFIX;
import static com.aecb.util.FirebaseLogging.contactUsGuest;

public class ContactUsRegistrationFragment extends BlurDialogBaseFragment<ContactUsRegistrationContract.View,
        ContactUsRegistrationContract.Presenter> implements ContactUsRegistrationContract.View,
        TextWatcher, View.OnClickListener {

    @Inject
    public ContactUsRegistrationContract.Presenter mPresenter;
    FragmentContactUsRegistrationBinding contactUsRegistrationBinding;
    RegisterUserRequest registerUserRequest;
    private ArrayList<CaseTypesItem> allReasons = new ArrayList<>();
    private ArrayList<String> reasonNames = new ArrayList<>();
    private String selectedReason = "";
    private String mobileNum = "";
    private ContactUsSubmitRequest contactUsSubmitRequest;
    private Handler handler = new Handler();
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

    public static ContactUsRegistrationFragment newInstance(Bundle bundle) {
        ContactUsRegistrationFragment fragment = new ContactUsRegistrationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.getReasons();
    }

    @Override
    public void setReasons(List<CaseTypesItem> caseTypes, String currentLanguage) {
        allReasons.clear();
        allReasons.addAll(caseTypes);
        reasonNames.add(getString(R.string.select_reason));
        for (CaseTypesItem caseTypesItem : allReasons) {
            if (currentLanguage.equals(ISO_CODE_ENG)) {
                reasonNames.add(caseTypesItem.getLabelEnglish());
            } else {
                reasonNames.add(caseTypesItem.getLabelArabic());
            }
        }
        contactUsRegistrationBinding.spinnerReason.setItems(reasonNames);
        contactUsRegistrationBinding.spinnerReason.setOnItemSelectedListener((view, position, id, item, parent) -> {
            if (position != 0) {
                selectedReason = (String) item;
                contactUsSubmitRequest.setReason(allReasons.get(position).getCode());
            } else {
                contactUsSubmitRequest.setReason("");
            }
        });
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        Bundle bundle = this.getArguments();
        contactUsSubmitRequest = new ContactUsSubmitRequest();
        if (bundle != null) {
            registerUserRequest = new Gson().fromJson(bundle.getString(REGISTER_REQUEST), RegisterUserRequest.class);
            mobileNum = bundle.getString(MOBILE);
        }
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), getLayoutRes(), null);
        contactUsRegistrationBinding = DataBindingUtil.bind(contentView);
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)
                ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) contentView.getParent());
        ScreenUtils screenUtils = new ScreenUtils(getActivity());
        View bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
        bottomSheet.getLayoutParams().height = screenUtils.getHeight() - 150;
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        editTextFocusListeners();
        contactUsGuest();
        if (registerUserRequest != null) {
            String mobileNumber = Utilities.after(mobileNum, "5");
            contactUsRegistrationBinding.edtMobile.setText(AppConstants.MOBILE_NUMBER_PREFIX + mobileNumber);
            contactUsRegistrationBinding.edtEmail.setText(registerUserRequest.getEmail());
            contactUsRegistrationBinding.edtName.setText(registerUserRequest.getFullName());
        }
    }

    @NonNull
    protected BlurConfig blurConfig() {
        return new BlurConfig.Builder().overlayColor(R.color.blur_bg_color)
                .asyncPolicy(SmartAsyncPolicyHolder.INSTANCE.smartAsyncPolicy())
                .debug(true).build();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_contact_us_registration;
    }

    @Override
    public ContactUsRegistrationContract.Presenter createPresenter() {
        return mPresenter;
    }

    private void editTextFocusListeners() {
        contactUsRegistrationBinding.edtSubjectName.addTextChangedListener(this);
        contactUsRegistrationBinding.edtName.addTextChangedListener(this);
        contactUsRegistrationBinding.edtMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith(MOBILE_NUMBER_PREFIX)) {
                    contactUsRegistrationBinding.edtMobile.setText(MOBILE_NUMBER_PREFIX);
                    Selection.setSelection(contactUsRegistrationBinding.edtMobile.getText(), contactUsRegistrationBinding.edtMobile.getText().length());
                }
            }
        });
        contactUsRegistrationBinding.edtEmail.addTextChangedListener(this);
        contactUsRegistrationBinding.edtMessage.addTextChangedListener(this);
        contactUsRegistrationBinding.btnSubmit.setOnClickListener(this);
        contactUsRegistrationBinding.llCall.setOnClickListener(this);
        contactUsRegistrationBinding.edtSubjectName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                contactUsRegistrationBinding.txtInputLoutSubjectName.setHint(getString(R.string.lbl_subject));
            } else if (!contactUsRegistrationBinding.edtSubjectName.getText().toString().isEmpty()) {
                contactUsRegistrationBinding.txtInputLoutSubjectName.setHint(getString(R.string.lbl_subject));
            } else {
                contactUsRegistrationBinding.txtInputLoutSubjectName.setHint(getString(R.string.enter_subject));
            }
        });
        contactUsRegistrationBinding.edtName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                contactUsRegistrationBinding.txtInputLoutName.setHint(getString(R.string.hint_title_full_name));
            } else if (!contactUsRegistrationBinding.edtName.getText().toString().isEmpty()) {
                contactUsRegistrationBinding.txtInputLoutName.setHint(getString(R.string.hint_title_full_name));
            } else {
                contactUsRegistrationBinding.txtInputLoutName.setHint(getString(R.string.hint_full_name));
            }
        });
        contactUsRegistrationBinding.edtMobile.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                contactUsRegistrationBinding.txtInputLoutMobile.setHint(getString(R.string.txt_mobile_number));
            } else if (!contactUsRegistrationBinding.edtMobile.getText().toString().isEmpty()) {
                contactUsRegistrationBinding.txtInputLoutMobile.setHint(getString(R.string.txt_mobile_number));
            } else {
                contactUsRegistrationBinding.txtInputLoutMobile.setHint(getString(R.string.enter_mobile));
            }
        });
        contactUsRegistrationBinding.edtEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                contactUsRegistrationBinding.txtInputLoutEmail.setHint(getString(R.string.email_address));
            } else if (!contactUsRegistrationBinding.edtEmail.getText().toString().isEmpty()) {
                contactUsRegistrationBinding.txtInputLoutEmail.setHint(getString(R.string.email_address));
            } else {
                contactUsRegistrationBinding.txtInputLoutEmail.setHint(getString(R.string.lbl_enter_email_address));
            }
        });
        contactUsRegistrationBinding.edtMessage.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                contactUsRegistrationBinding.txtInputLoutMessage.setHint(getString(R.string.your_message));
            } else if (!contactUsRegistrationBinding.edtMessage.getText().toString().isEmpty()) {
                contactUsRegistrationBinding.txtInputLoutMessage.setHint(getString(R.string.your_message));
            } else {
                contactUsRegistrationBinding.txtInputLoutMessage.setHint(getString(R.string.enter_your_message));
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!contactUsRegistrationBinding.edtSubjectName.getText().toString().isEmpty() &&
                !contactUsRegistrationBinding.edtEmail.getText().toString().isEmpty() &&
                !contactUsRegistrationBinding.edtName.getText().toString().isEmpty() &&
                !contactUsRegistrationBinding.edtMobile.getText().toString().isEmpty() &&
                !contactUsRegistrationBinding.edtMessage.getText().toString().isEmpty() &&
                !selectedReason.isEmpty()) {
            contactUsRegistrationBinding.btnSubmit.setEnabled(true);
            contactUsRegistrationBinding.btnSubmit.setBackground(getResources().getDrawable(R.drawable.bg_blue_button));
        } else {
            contactUsRegistrationBinding.btnSubmit.setEnabled(false);
            contactUsRegistrationBinding.btnSubmit.setBackground(getResources().getDrawable(R.drawable.btn_disable_state));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSubmit) {
            contactUsSubmitRequest.setChannel(3);
            contactUsSubmitRequest.setSubject(contactUsRegistrationBinding.edtSubjectName.getText().toString());
            contactUsSubmitRequest.setDescription(contactUsRegistrationBinding.edtMessage.getText().toString());
            contactUsSubmitRequest.setEmail(contactUsRegistrationBinding.edtEmail.getText().toString());
            contactUsSubmitRequest.setMobile(contactUsRegistrationBinding.edtMobile.getText().toString().replaceAll(" ", ""));
            contactUsSubmitRequest.setFullName(contactUsRegistrationBinding.edtName.getText().toString());
            contactUsSubmitRequest.setType("contactus");
            mPresenter.submitContactUsQuery(contactUsSubmitRequest);
        } else if (v.getId() == R.id.llCall) {
            Utilities.makeCall(getActivity(), "800287328");
        }
    }

    @Override
    public void showInvalidMobileError() {
        showMessage(getString(R.string.enter_valid_phone));
    }

    @Override
    public void showInvalidEmailError() {
        showMessage(getString(R.string.please_enter_valid_email));
    }

    @Override
    public void showEmptySubjectError() {
        showMessage(getString(R.string.txt_msg_select_query_subject));
    }

    @Override
    public void showQuerySubjectValidError() {
        showMessage(getString(R.string.txt_msg_validate_alphanumeric));
    }

    @Override
    public void showInvalidNameError() {
        showMessage(getString(R.string.enter_valid_name));
    }

    @Override
    public void showQueryTypeError() {
        showMessage(getString(R.string.txt_msg_select_reason));
    }

    @Override
    public void showQueryMessagesValidError() {
        showMessage(getString(R.string.txt_msg_validate_alphanumeric));
    }

    @Override
    public void showQueryMessagesError() {
        showMessage(getString(R.string.txt_msg_select_query_message));
    }

    @Override
    public void showMessage(String message) {
        localValidationError(getString(R.string.error), message);
    }

    @Override
    public void resetData() {
        contactUsRegistrationBinding.edtSubjectName.setText(null);
        contactUsRegistrationBinding.edtName.setText(null);
        contactUsRegistrationBinding.edtEmail.setText(null);
        contactUsRegistrationBinding.edtMessage.setText(null);
        contactUsRegistrationBinding.edtMobile.setText(null);
        contactUsRegistrationBinding.spinnerReason.setItems(new ArrayList<>());
        selectedReason = "";
        contactUsSubmitRequest.setReason("");
        contactUsRegistrationBinding.spinnerReason.setItems(reasonNames);
        handler.postDelayed(this::dismiss, 3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (getContext() != null && getContext() instanceof SecurityQuestionsRoundOneActivity) {
            ((SecurityQuestionsRoundOneActivity) getContext()).moveActivity(getActivity(), LoginActivity.class,
                    true, true);
        }
        if (getContext() != null && getContext() instanceof SecurityQuestionsRoundTwoActivity) {
            ((SecurityQuestionsRoundTwoActivity) getContext()).moveActivity(getActivity(), LoginActivity.class,
                    true, true);
        }
    }
}