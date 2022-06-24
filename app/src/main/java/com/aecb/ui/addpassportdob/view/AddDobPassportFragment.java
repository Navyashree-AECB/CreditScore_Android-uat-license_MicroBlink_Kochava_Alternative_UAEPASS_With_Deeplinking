package com.aecb.ui.addpassportdob.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.base.mvp.BlurDialogBaseFragment;
import com.aecb.data.api.models.login.UaePassRequest;
import com.aecb.data.api.models.request.updateuserprofile.UpdateUserProfileRequest;
import com.aecb.data.api.models.response.getuserdetail.GetUserDetailResponse;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.databinding.FragmentAddDobPassportBinding;
import com.aecb.presentation.blurcustomview.BlurConfig;
import com.aecb.presentation.blurcustomview.SmartAsyncPolicyHolder;
import com.aecb.presentation.dialogs.DatePickerFragment;
import com.aecb.ui.addpassportdob.presenter.AddDobPassportContract;
import com.aecb.ui.loginflow.login.view.LoginActivity;
import com.aecb.ui.scanemirate.view.ScanEmirateIdActivity;
import com.aecb.ui.startsup.view.StartupActivity;
import com.aecb.ui.termsandconditions.view.TermsAndConditionsFragment;
import com.aecb.util.ScreenUtils;
import com.aecb.util.ValidationUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import java.util.Calendar;

import javax.inject.Inject;

import static com.aecb.AppConstants.IntentKey.BUNDLE_DB_USER;
import static com.aecb.AppConstants.IntentKey.BUNDLE_USER_DETAILS;
import static com.aecb.AppConstants.IntentKey.OPEN_TC_FOR;
import static com.aecb.AppConstants.IntentKey.TC_FOR_DOB;
import static com.aecb.AppConstants.IntentKey.TC_VERSION;
import static com.aecb.AppConstants.IntentKey.UAE_PASS_REQUEST;
import static com.aecb.AppConstants.IntentKey.USER_NAME;
import static com.aecb.AppConstants.MINIMUM_REQUIRED_AGE;
import static com.aecb.AppConstants.SIMPLEDATEFORMAT_FOR_DOB;
import static com.aecb.util.varibles.StringConstants.DOB_PICKER;

public class AddDobPassportFragment extends BlurDialogBaseFragment<AddDobPassportContract.View,
        AddDobPassportContract.Presenter> implements AddDobPassportContract.View, View.OnClickListener {

    @Inject
    public AddDobPassportContract.Presenter mPresenter;
    FragmentAddDobPassportBinding addDobPassportBinding;
    private Calendar calendar = Calendar.getInstance();
    UaePassRequest uaePassRequest;
    Bundle bundle;
    private TermsAndConditionsFragment termsConditionFragment;
    private DBUserTC dbUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        getBundleData();
    }

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

    public static AddDobPassportFragment newInstance(Bundle bundle) {
        AddDobPassportFragment fragment = new AddDobPassportFragment();
        fragment.setArguments(bundle);
        return fragment;
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
        addDobPassportBinding = DataBindingUtil.bind(contentView);
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
        editTextFocusListeners();
    }

    private void getBundleData() {
        bundle = this.getArguments();
        if (bundle != null && bundle.containsKey(UAE_PASS_REQUEST)) {
            uaePassRequest = (UaePassRequest) bundle.getSerializable(UAE_PASS_REQUEST);
        }
    }

    private void enableBinding() {
        //addDobPassportBinding.inputIdDateOfBirth.setOnClickListener(this);
        addDobPassportBinding.btnSubmit.setOnClickListener(this);
        addDobPassportBinding.tvCancel.setOnClickListener(this);

        EditText edtDOB = addDobPassportBinding.inputIdDateOfBirth.editText;

        edtDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
    }


    @NonNull
    @Override
    public AddDobPassportContract.Presenter createPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_add_dob_passport;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inputIdDateOfBirth:
                //openDatePicker();
                break;
            case R.id.btnSubmit:
                if (ValidationUtil.isNullOrEmpty(addDobPassportBinding.inputIdDateOfBirth.editText.getText().toString())) {
                    showEmptyDateOfBirthError();
                } else {
                    uaePassRequest.setDob(addDobPassportBinding.inputIdDateOfBirth.editText.getText().toString());
                    uaePassRequest.setPassportNumber(addDobPassportBinding.edtPassport.getText().toString());
                    mPresenter.loginWithUAEPass(uaePassRequest);
                }
                break;
            case R.id.tvCancel:
                dismiss();
                break;
        }
    }

    public void showEmptyDateOfBirthError() {
        localValidationError(getString(R.string.error), getString(R.string.please_select_date_of_birth));
        addDobPassportBinding.viewLineDOB.setBackgroundResource(R.color.underlineColor);
        addDobPassportBinding.tvDOBValidation.setVisibility(View.GONE);
    }


    void editTextFocusListeners() {
        addDobPassportBinding.edtPassport.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                addDobPassportBinding.txtInputLoutPassport.setHint(getString(R.string.passport_number_optional_bracket));
            } else if (!addDobPassportBinding.edtPassport.getText().toString().isEmpty()) {
                addDobPassportBinding.txtInputLoutPassport.setHint(getString(R.string.passport_number_optional_bracket));
            } else {
                addDobPassportBinding.txtInputLoutPassport.setHint(getString(R.string.passport_number_optional_bracket));
            }
        });
    }

    public void openDatePicker() {
        AppConstants.setLocalLanguageEng(getActivity());
        DialogFragment birthDate;
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -MINIMUM_REQUIRED_AGE);
        birthDate = DatePickerFragment.newInstance(0, c.getTimeInMillis(), "", AppConstants.DateFormats.DD_MM_YY, DOB_PICKER, "");
        birthDate.show(getActivity().getSupportFragmentManager(), DOB_PICKER);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (view.getTag().toString().equals(DOB_PICKER)) {
            calendar.set(year, month, day);
            addDobPassportBinding.inputIdDateOfBirth.setText(SIMPLEDATEFORMAT_FOR_DOB.format(calendar.getTime()));
            addDobPassportBinding.edtPassport.requestFocus();
            addDobPassportBinding.viewLineDOB.setBackgroundResource(R.color.gray_light);
            addDobPassportBinding.tvDOBValidation.setVisibility(View.GONE);
            if (!addDobPassportBinding.inputIdDateOfBirth.getText().isEmpty()) {
                addDobPassportBinding.btnSubmit.setEnabled(true);
                addDobPassportBinding.btnSubmit.setBackground(getResources().getDrawable(R.drawable.bg_blue_button));
            } else {
                addDobPassportBinding.btnSubmit.setEnabled(false);
                addDobPassportBinding.btnSubmit.setBackground(getResources().getDrawable(R.drawable.btn_disable_state));
            }
        }
    }

    @Override
    public void openTCPage(String tcFor, String userName, int tcVersion, GetUserDetailResponse data, DBUserTC dbUserTC) {
        hideLoading();
        Bundle b = new Bundle();
        b.putString(OPEN_TC_FOR, tcFor);
        b.putString(USER_NAME, userName);
        b.putInt(TC_VERSION, tcVersion);
        b.putString(BUNDLE_DB_USER, new Gson().toJson(dbUserTC));
        b.putBoolean(TC_FOR_DOB, true);
        if (data != null)
            b.putString(BUNDLE_USER_DETAILS, new Gson().toJson(data));
        termsConditionFragment = TermsAndConditionsFragment.newInstance(b);
        termsConditionFragment.show(getActivity().getSupportFragmentManager(), termsConditionFragment.getTag());
    }

    @Override
    public void openNextPage() {
        if (termsConditionFragment != null) {
            termsConditionFragment.dismiss();
        }
        if (getContext() instanceof LoginActivity) {
            ((LoginActivity) getContext()).openNextPage();
        }
        if (getContext() instanceof ScanEmirateIdActivity) {
            ((ScanEmirateIdActivity) getContext()).openNextPage();
        }
    }

    public void updateUserDetailForUaePass(UpdateUserProfileRequest updateUserProfileRequest, String tcFor,
                                           DBUserTC dbUserTC, GetUserDetailResponse getUserDetailResponse) {
        mPresenter.updateUserDetail(updateUserProfileRequest, tcFor, dbUserTC, getUserDetailResponse);

    }
}