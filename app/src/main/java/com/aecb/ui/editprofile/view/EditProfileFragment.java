package com.aecb.ui.editprofile.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;

import com.aecb.R;
import com.aecb.base.mvp.BlurDialogBaseFragment;
import com.aecb.data.api.models.request.updateuserprofile.UpdateUserProfileRequest;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.databinding.FragmentEditProfileBinding;
import com.aecb.presentation.blurcustomview.BlurConfig;
import com.aecb.presentation.blurcustomview.SmartAsyncPolicyHolder;
import com.aecb.ui.editprofile.presenter.EditProfileContract;
import com.aecb.ui.profile.view.ProfileActivity;
import com.aecb.util.ScreenUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import javax.inject.Inject;

import static com.aecb.AppConstants.EditField.EMAIL_ID;
import static com.aecb.AppConstants.EditField.MOBILE_NO;
import static com.aecb.AppConstants.IntentKey.EDIT_FIELD_DATA;
import static com.aecb.AppConstants.IntentKey.EDIT_FIELD_NAME;
import static com.aecb.AppConstants.IntentKey.USER_DATA;
import static com.aecb.AppConstants.MOBILE_NUMBER_PREFIX;
import static com.aecb.util.FirebaseLogging.editedPassportCancelled;
import static com.aecb.util.FirebaseLogging.editedPassportSaved;

public class EditProfileFragment extends BlurDialogBaseFragment<EditProfileContract.View,
        EditProfileContract.Presenter> implements EditProfileContract.View, View.OnClickListener {

    @Inject
    public EditProfileContract.Presenter mPresenter;
    UpdateUserProfileRequest updateUserProfileRequest;
    FragmentEditProfileBinding fragmentEditProfileBinding;
    Bundle bundle;
    private DBUserTC dbUserTC;
    private String editField;
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

    public static EditProfileFragment newInstance(Bundle bundle) {
        EditProfileFragment fragment = new EditProfileFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_edit_profile;
    }

    @Override
    public EditProfileContract.Presenter createPresenter() {
        return mPresenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        getBundle();
        updateUserProfileRequest = new UpdateUserProfileRequest();
    }

    private void getBundle() {
        bundle = this.getArguments();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (bundle != null) {
            dbUserTC = new Gson().fromJson(bundle.getString(USER_DATA), DBUserTC.class);
            if (bundle.getString(EDIT_FIELD_NAME).equals(EMAIL_ID)) {
                editField = bundle.getString(EDIT_FIELD_NAME);
                fragmentEditProfileBinding.llEmail.setVisibility(View.VISIBLE);
                fragmentEditProfileBinding.edtEmail.setText(bundle.getString(EDIT_FIELD_DATA));
                fragmentEditProfileBinding.edtEmail.setSelection(fragmentEditProfileBinding.edtEmail.getText().length());
            } else if (bundle.getString(EDIT_FIELD_NAME).equals(MOBILE_NO)) {
                editField = bundle.getString(EDIT_FIELD_NAME);
                fragmentEditProfileBinding.llMobile.setVisibility(View.VISIBLE);
                fragmentEditProfileBinding.edtMobile.setText(bundle.getString(EDIT_FIELD_DATA));
                fragmentEditProfileBinding.edtMobile.setSelection(fragmentEditProfileBinding.edtMobile.getText().length());
                fragmentEditProfileBinding.edtMobile.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!s.toString().startsWith(MOBILE_NUMBER_PREFIX)) {
                            fragmentEditProfileBinding.edtMobile.setText(MOBILE_NUMBER_PREFIX);
                            Selection.setSelection(fragmentEditProfileBinding.edtMobile.getText(), fragmentEditProfileBinding.edtMobile.getText().length());
                        }
                    }
                });

            } else {
                editField = bundle.getString(EDIT_FIELD_NAME);
                fragmentEditProfileBinding.llPassport.setVisibility(View.VISIBLE);
                fragmentEditProfileBinding.edtPassport.setText(bundle.getString(EDIT_FIELD_DATA));
                fragmentEditProfileBinding.edtPassport.setSelection(fragmentEditProfileBinding.edtPassport.getText().length());
            }
        }
    }

    private void enableBinding() {
        fragmentEditProfileBinding.btnSave.setOnClickListener(this);
        fragmentEditProfileBinding.tvCancel.setOnClickListener(this);
        fragmentEditProfileBinding.edtPassport.requestFocus();
        fragmentEditProfileBinding.edtMobile.requestFocus();
        fragmentEditProfileBinding.edtEmail.requestFocus();
        editTextFocusListeners();
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
        fragmentEditProfileBinding = DataBindingUtil.bind(contentView);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                updateUserProfileRequest.setChannel(2);
                if (bundle.getString(EDIT_FIELD_NAME).equals(EMAIL_ID)) {
                    updateUserProfileRequest.setUpdateType(2);
                    dbUserTC.setEmail(fragmentEditProfileBinding.edtEmail.getText().toString());
                    updateUserProfileRequest.setEmail(fragmentEditProfileBinding.edtEmail.getText().toString());
                } else if (bundle.getString(EDIT_FIELD_NAME).equals(MOBILE_NO)) {
                    updateUserProfileRequest.setUpdateType(4);
                    dbUserTC.setMobile(fragmentEditProfileBinding.edtMobile.getText().toString());
                    updateUserProfileRequest.setMobile(fragmentEditProfileBinding.edtMobile.getText().toString().replaceAll(" ", ""));
                } else {
                    editedPassportSaved();
                    updateUserProfileRequest.setUpdateType(5);
                    dbUserTC.setPassport(fragmentEditProfileBinding.edtPassport.getText().toString());
                    updateUserProfileRequest.setPassport(fragmentEditProfileBinding.edtPassport.getText().toString());
                }
                mPresenter.updateUserDetail(updateUserProfileRequest, dbUserTC, editField);
                break;
            case R.id.tvCancel:
                if (fragmentEditProfileBinding.edtPassport.getVisibility() == View.VISIBLE) {
                    editedPassportCancelled();
                }
                if (getActivity() instanceof ProfileActivity) {
                    ((ProfileActivity) getContext()).dismissDialog();
                }
                break;
        }
    }

    @Override
    public void openProfile(String message, String status, String useCase) {
        if (getActivity() instanceof ProfileActivity) {
            ((ProfileActivity) getContext()).dismissDialog();
            ((ProfileActivity) getContext()).showProfileUpdatedMessage(message, status, useCase);
        }
    }

    @Override
    public void showMessage(String message) {
        localValidationError(getString(R.string.error), message);
    }

    @Override
    public void showEmptyEmailError() {
        showMessage(getString(R.string.empty_email_error));
        fragmentEditProfileBinding.edtEmail.requestFocus();
    }

    @Override
    public void showInvalidEmailError() {
        showMessage(getString(R.string.enter_valid_email));
        fragmentEditProfileBinding.edtEmail.requestFocus();
    }

    @Override
    public void showEmptyMobileError() {
        showMessage(getString(R.string.enter_mobile));
        fragmentEditProfileBinding.edtMobile.requestFocus();
    }

    @Override
    public void showInvalidMobileError() {
        showMessage(getString(R.string.enter_valid_mobile));
        fragmentEditProfileBinding.edtMobile.requestFocus();
    }

    @Override
    public void showEmptyPassportNo() {
        showMessage(getString(R.string.hint_passport_no));
        fragmentEditProfileBinding.edtPassport.requestFocus();
    }

    @Override
    public void showInvalidPassportNo() {
        showMessage(getString(R.string.enter_valid_passport));
        fragmentEditProfileBinding.edtPassport.requestFocus();
    }

    private void editTextFocusListeners() {
        fragmentEditProfileBinding.edtMobile.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                fragmentEditProfileBinding.txtInputLoutMobile.setHint(getString(R.string.txt_mobile_number));
            } else if (!fragmentEditProfileBinding.edtMobile.getText().toString().isEmpty()) {
                fragmentEditProfileBinding.txtInputLoutMobile.setHint(getString(R.string.txt_mobile_number));
            } else {
                fragmentEditProfileBinding.txtInputLoutMobile.setHint(getString(R.string.txt_mobile_number));
            }
        });
        fragmentEditProfileBinding.edtEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                fragmentEditProfileBinding.txtInputLoutEmail.setHint(getString(R.string.email_address));
            } else if (!fragmentEditProfileBinding.edtEmail.getText().toString().isEmpty()) {
                fragmentEditProfileBinding.txtInputLoutEmail.setHint(getString(R.string.email_address));
            } else {
                fragmentEditProfileBinding.txtInputLoutEmail.setHint(getString(R.string.email_address));
            }
        });
        fragmentEditProfileBinding.edtPassport.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                fragmentEditProfileBinding.txtInputLoutPassport.setHint(getString(R.string.passport_no));
            } else if (!fragmentEditProfileBinding.edtPassport.getText().toString().isEmpty()) {
                fragmentEditProfileBinding.txtInputLoutPassport.setHint(getString(R.string.passport_no));
            } else {
                fragmentEditProfileBinding.txtInputLoutPassport.setHint(getString(R.string.passport_number_hint));
            }
        });
    }

}