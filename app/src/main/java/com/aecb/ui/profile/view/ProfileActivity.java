package com.aecb.ui.profile.view;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.base.mvp.BaseActivity;
import com.aecb.base.mvp.BlurDialogBaseFragment;
import com.aecb.data.api.models.nationalities.NationalitiesItem;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.databinding.ActivityProfileBinding;
import com.aecb.ui.editprofile.view.EditProfileFragment;
import com.aecb.ui.menu.view.MenuActivity;
import com.aecb.ui.profile.presenter.ProfileContract;
import com.aecb.ui.searchdialog.view.SearchFragment;
import com.aecb.util.EmirateChangeListenerValidation;
import com.aecb.util.Utilities;
import com.aecb.util.ValidationUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

import static com.aecb.AppConstants.IntentKey.EDIT_FIELD_DATA;
import static com.aecb.AppConstants.IntentKey.EDIT_FIELD_NAME;
import static com.aecb.AppConstants.IntentKey.EDIT_NATIONALITY;
import static com.aecb.AppConstants.IntentKey.USER_DATA;
import static com.aecb.util.FirebaseLogging.editedEmailFromSetting;
import static com.aecb.util.FirebaseLogging.editedMobileFromSetting;
import static com.aecb.util.FirebaseLogging.editedNationalityFromSetting;
import static com.aecb.util.FirebaseLogging.editedPassportFromSetting;

public class ProfileActivity extends BaseActivity<ProfileContract.View, ProfileContract.Presenter>
        implements ProfileContract.View, View.OnClickListener, TextWatcher {

    @Inject
    public ProfileContract.Presenter mPresenter;
    ActivityProfileBinding activityProfileBinding;
    SearchFragment searchFragment;
    private BlurDialogBaseFragment editProfileFragment;
    private String selectedGender;
    private DBUserTC dbUserTC;
    private NationalitiesItem selectedNationality;
    private ArrayList<NationalitiesItem> allNationalityData = new ArrayList<>();
    private long lastClickTime = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBinding();
        mPresenter.getAllNationalities();
    }

    private void enableBinding() {
        activityProfileBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        activityProfileBinding.edtProfileName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        activityProfileBinding.btnBack.setOnClickListener(this);
        activityProfileBinding.tvEditPassportNo.setOnClickListener(this);
        activityProfileBinding.tvEditNationality.setOnClickListener(this);
        activityProfileBinding.tvEditMobileNo.setOnClickListener(this);
        activityProfileBinding.tvEditEmail.setOnClickListener(this);
        activityProfileBinding.tvProfileNationality.setOnClickListener(this);
        activityProfileBinding.edtProfileEmirateNo.addTextChangedListener(this);
        initSpinners();
        editTextFocusListener();
    }

    private void editTextFocusListener() {
        activityProfileBinding.edtProfileName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                activityProfileBinding.txtInputLoutProfileName.setHint(getString(R.string.hint_title_full_name));
            } else if (!activityProfileBinding.edtProfileName.getText().toString().isEmpty()) {
                activityProfileBinding.txtInputLoutProfileName.setHint(getString(R.string.hint_title_full_name));
            } else {
                activityProfileBinding.txtInputLoutProfileName.setHint(getString(R.string.enter_full_name));
            }
        });

        activityProfileBinding.edtProfileEmirateNo.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                activityProfileBinding.txtInputLoutProfileEmirateNo.setHint(getString(R.string.enter_emirates_id_number_profile));
            } else if (!activityProfileBinding.edtProfileEmirateNo.getText().toString().isEmpty()) {
                activityProfileBinding.txtInputLoutProfileEmirateNo.setHint(getString(R.string.enter_emirates_id_number_profile));
            } else {
                activityProfileBinding.txtInputLoutProfileEmirateNo.setHint(getString(R.string.hint_emirate));
            }
        });

        activityProfileBinding.edtProfileMobile.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                activityProfileBinding.txtInputLoutProfileMobile.setHint(getString(R.string.txt_mobile_number));
            } else if (!activityProfileBinding.edtProfileMobile.getText().toString().isEmpty()) {
                activityProfileBinding.txtInputLoutProfileMobile.setHint(getString(R.string.txt_mobile_number));
            } else {
                activityProfileBinding.txtInputLoutProfileMobile.setHint(getString(R.string.txt_mobile_number));
            }
        });

        activityProfileBinding.edtProfileEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                activityProfileBinding.txtInputLoutProfileEmail.setHint(getString(R.string.email));
            } else if (!activityProfileBinding.edtProfileEmail.getText().toString().isEmpty()) {
                activityProfileBinding.txtInputLoutProfileEmail.setHint(getString(R.string.email));
            } else {
                activityProfileBinding.txtInputLoutProfileEmail.setHint(getString(R.string.email_address));
            }
        });
    }

    @SuppressLint("NewApi")
    public void loadAllNationalities(List<NationalitiesItem> nationalities) {
        allNationalityData.addAll(nationalities);
    }

    @Override
    public void showError(String message) {
        ValidationUtil.showToast(this, message);
    }

    @Override
    public void setUser(DBUserTC dbUserItem) {
        dbUserTC = dbUserItem;
        setUserData();
    }

    void initSpinners() {
        activityProfileBinding.spinnerProfileGender.setEnabled(false);
        activityProfileBinding.spinnerProfileGender.setItems(getResources().getStringArray(R.array.genders_profile));
        activityProfileBinding.spinnerProfileGender.setOnItemSelectedListener((view, position, id, item, parent) -> {
            selectedGender = (String) item;
        });
    }

    void setUserData() {
        runOnUiThread(() -> {
            activityProfileBinding.edtProfileName.setText(dbUserTC.getFullName());
            activityProfileBinding.edtProfileEmirateNo.setText(dbUserTC.getEmiratesId());
            String mobileNumber = Utilities.after(dbUserTC.getMobile(), "5");
            Timber.d("MobileNumber" + mobileNumber);
            activityProfileBinding.edtProfileMobile.setText(AppConstants.MOBILE_NUMBER_PREFIX + mobileNumber);
            activityProfileBinding.edtProfileEmail.setText(dbUserTC.getEmail());
            activityProfileBinding.inputIdDateOfBirth.setText(dbUserTC.getDob());
            activityProfileBinding.edtProfilePassport.setText(dbUserTC.getPassport());
            if (dbUserTC.getGender() == 1) {
                activityProfileBinding.spinnerProfileGender.setSelectedIndex(1);
            } else if (dbUserTC.getGender() == 2) {
                activityProfileBinding.spinnerProfileGender.setSelectedIndex(2);
            } else {
                activityProfileBinding.spinnerProfileGender.setSelectedIndex(0);
            }
            setSelectedNationality(dbUserTC.getNationalityId());
        });
    }

    public void showProfileUpdatedMessage(String message, String status, String useCase) {
        final Handler handler = new Handler();
        handler.postDelayed(() -> showApiSuccessMessage(message, status, useCase), 500);
    }

    public void setSelectedNationality(String id) {
        for (int i = 0; i <= allNationalityData.size() - 1; i++) {
            if (id.equals(allNationalityData.get(i).getNationalityId())) {
                selectedNationality = allNationalityData.get(i);
                break;
            }
        }
        activityProfileBinding.tvProfileNationality.setText(selectedNationality.getCountryName());
    }

    @Override
    public ProfileContract.Presenter createPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_profile;
    }

    @Override
    public void initToolbar() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                moveActivity(this, MenuActivity.class, true, false);
                break;
            case R.id.tvEditEmail:
                if (SystemClock.elapsedRealtime() - lastClickTime < 2000) return;
                lastClickTime = SystemClock.elapsedRealtime();
                editedEmailFromSetting();
                openEditDialog(AppConstants.EditField.EMAIL_ID,
                        activityProfileBinding.edtProfileEmail.getText().toString());
                break;
            case R.id.tvEditMobileNo:
                if (SystemClock.elapsedRealtime() - lastClickTime < 2000) return;
                lastClickTime = SystemClock.elapsedRealtime();
                editedMobileFromSetting();
                openEditDialog(AppConstants.EditField.MOBILE_NO,
                        activityProfileBinding.edtProfileMobile.getText().toString());
                break;
            case R.id.tvEditNationality:
                if (SystemClock.elapsedRealtime() - lastClickTime < 2000) return;
                lastClickTime = SystemClock.elapsedRealtime();
                editedNationalityFromSetting();
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstants.IntentKey.NATIONALITY_LIST, allNationalityData);
                bundle.putString(EDIT_NATIONALITY, new Gson().toJson(dbUserTC));
                searchFragment = SearchFragment.newInstance(bundle);
                searchFragment.show(getSupportFragmentManager(), searchFragment.getTag());
                break;
            case R.id.tvEditPassportNo:
                if (SystemClock.elapsedRealtime() - lastClickTime < 2000) return;
                lastClickTime = SystemClock.elapsedRealtime();
                editedPassportFromSetting();
                openEditDialog(AppConstants.EditField.PASSPORT_NO,
                        activityProfileBinding.edtProfilePassport.getText().toString());
                break;

        }
    }

    private void openEditDialog(String editFieldName, String data) {
        FragmentManager manager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString(EDIT_FIELD_NAME, editFieldName);
        bundle.putString(EDIT_FIELD_DATA, data);
        bundle.putString(USER_DATA, new Gson().toJson(dbUserTC));
        editProfileFragment = EditProfileFragment.newInstance(bundle);
        editProfileFragment.show(manager, editProfileFragment.getTag());
    }

    public void dismissDialog() {
        if (editProfileFragment != null) {
            editProfileFragment.dismiss();
        }
        mPresenter.getLastUserDetail();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.hashCode() == activityProfileBinding.edtProfileEmirateNo.getText().hashCode()) {
            /* we using custom id format so user entering value then we appending dash*/
            EmirateChangeListenerValidation.filterEID(s, activityProfileBinding.edtProfileEmirateNo, this);
        }
    }
}