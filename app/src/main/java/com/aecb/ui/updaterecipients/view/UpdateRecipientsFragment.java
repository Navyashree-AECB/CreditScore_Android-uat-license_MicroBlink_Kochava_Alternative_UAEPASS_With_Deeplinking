package com.aecb.ui.updaterecipients.view;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;

import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.base.mvp.BlurDialogBaseFragment;
import com.aecb.data.api.models.emailrecipients.EmailRecipients;
import com.aecb.databinding.FragmentUpdateRecipientsBinding;
import com.aecb.presentation.blurcustomview.BlurConfig;
import com.aecb.presentation.blurcustomview.SmartAsyncPolicyHolder;
import com.aecb.ui.addrecipients.view.AddRecipientsActivity;
import com.aecb.ui.updaterecipients.presenter.UpdateRecipientsContract;
import com.aecb.util.ScreenUtils;
import com.aecb.util.ValidationUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.aecb.AppConstants.IntentKey.EMAIL_RECIPIENTS;

public class UpdateRecipientsFragment extends BlurDialogBaseFragment<UpdateRecipientsContract.View,
        UpdateRecipientsContract.Presenter> implements UpdateRecipientsContract.View, View.OnClickListener,
        TextWatcher {

    @Inject
    public UpdateRecipientsContract.Presenter mPresenter;
    public ArrayList<EmailRecipients> emailList = new ArrayList<>();
    FragmentUpdateRecipientsBinding updateRecipientsBinding;
    String email, fullName;
    int position;
    private boolean editName = false;
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

    public static UpdateRecipientsFragment newInstance(Bundle bundle) {
        UpdateRecipientsFragment fragment = new UpdateRecipientsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_update_recipients;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                boolean isExist = false;
                for (int i = 0; i <= emailList.size() - 1; i++) {
                    if (emailList.get(i).getEmail().equals(updateRecipientsBinding.edtEmail.getText().toString())) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    if (getActivity() instanceof AddRecipientsActivity) {
                        if (ValidationUtil.isNullOrEmpty(updateRecipientsBinding.edtEmail.getText().toString())) {
                            localValidationError(getString(R.string.error), getString(R.string.please_enter_email_address));
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(updateRecipientsBinding.edtEmail.getText().toString()).matches()) {
                            localValidationError(getString(R.string.error), getString(R.string.please_enter_valid_email));
                        } else if (ValidationUtil.isNullOrEmpty(updateRecipientsBinding.edtName.getText().toString())) {
                            localValidationError(getString(R.string.error), getString(R.string.please_enter_name));
                        } else {
                            ((AddRecipientsActivity) getActivity()).updateEmail(updateRecipientsBinding.edtEmail.getText().toString(),
                                    updateRecipientsBinding.edtName.getText().toString(),
                                    position);
                            dismiss();
                        }
                    }
                } else {
                    if (editName) {
                        if (ValidationUtil.isNullOrEmpty(updateRecipientsBinding.edtEmail.getText().toString())) {
                            localValidationError(getString(R.string.error), getString(R.string.please_enter_email_address));
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(updateRecipientsBinding.edtEmail.getText().toString()).matches()) {
                            localValidationError(getString(R.string.error), getString(R.string.please_enter_valid_email));
                        } else if (ValidationUtil.isNullOrEmpty(updateRecipientsBinding.edtName.getText().toString())) {
                            localValidationError(getString(R.string.error), getString(R.string.please_enter_name));
                        } else {
                            ((AddRecipientsActivity) getActivity()).updateEmail(updateRecipientsBinding.edtEmail.getText().toString(),
                                    updateRecipientsBinding.edtName.getText().toString(),
                                    position);
                            dismiss();
                        }
                    } else {
                        localValidationError(getString(R.string.error), getString(R.string.you_have_already_added_email));
                        updateRecipientsBinding.edtEmail.requestFocus();
                    }
                }
                break;
            case R.id.tvCancel:
                dismiss();
                break;
        }
    }

    @Override
    public UpdateRecipientsContract.Presenter createPresenter() {
        return mPresenter;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            email = bundle.getString(AppConstants.IntentKey.EMAIL);
            position = bundle.getInt(AppConstants.IntentKey.POSITION);
            fullName = bundle.getString(AppConstants.IntentKey.FULL_NAME);
            emailList = (ArrayList<EmailRecipients>) getArguments().getSerializable(EMAIL_RECIPIENTS);
        }
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), getLayoutRes(), null);
        updateRecipientsBinding = DataBindingUtil.bind(contentView);
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
        updateRecipientsBinding.btnSave.setOnClickListener(this);
        updateRecipientsBinding.tvCancel.setOnClickListener(this);
        updateRecipientsBinding.edtEmail.addTextChangedListener(this);
        updateRecipientsBinding.edtName.addTextChangedListener(this);
        updateRecipientsBinding.edtEmail.setText(email);
        updateRecipientsBinding.edtName.setText(fullName);
        editTextFocusListeners();
    }

    @NonNull
    protected BlurConfig blurConfig() {
        return new BlurConfig.Builder().overlayColor(R.color.blur_bg_color)
                .asyncPolicy(SmartAsyncPolicyHolder.INSTANCE.smartAsyncPolicy())
                .debug(true).build();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!updateRecipientsBinding.edtName.getText().toString().isEmpty() &&
                !updateRecipientsBinding.edtEmail.getText().toString().isEmpty()) {
            if (updateRecipientsBinding.edtEmail.getText().toString().equals(email) && !
                    updateRecipientsBinding.edtName.getText().toString().equals(fullName)) {
                editName = true;
            }
            updateRecipientsBinding.btnSave.setBackgroundResource(R.drawable.bg_round_corner_blue);
            updateRecipientsBinding.btnSave.setClickable(true);
            updateRecipientsBinding.btnSave.setTextColor(getResources().getColor(R.color.border_color));
        } else {
            updateRecipientsBinding.btnSave.setBackgroundResource(R.drawable.blue_corner_disabled);
            updateRecipientsBinding.btnSave.setClickable(false);
            updateRecipientsBinding.btnSave.setTextColor(getResources().getColor(R.color.gray_light));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void editTextFocusListeners() {
        updateRecipientsBinding.edtName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                updateRecipientsBinding.txtInputLoutName.setHint(getString(R.string.hint_title_full_name));
            } else if (!updateRecipientsBinding.edtName.getText().toString().isEmpty()) {
                updateRecipientsBinding.txtInputLoutName.setHint(getString(R.string.hint_title_full_name));
            } else {
                updateRecipientsBinding.txtInputLoutName.setHint(getString(R.string.hint_full_name_of_add_card));
            }
        });
        updateRecipientsBinding.edtEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                updateRecipientsBinding.txtInputLoutEmail.setHint(getString(R.string.email_address));
            } else if (!updateRecipientsBinding.edtEmail.getText().toString().isEmpty()) {
                updateRecipientsBinding.txtInputLoutEmail.setHint(getString(R.string.email_address));
            } else {
                updateRecipientsBinding.txtInputLoutEmail.setHint(getString(R.string.email_address));
            }
        });
    }
}