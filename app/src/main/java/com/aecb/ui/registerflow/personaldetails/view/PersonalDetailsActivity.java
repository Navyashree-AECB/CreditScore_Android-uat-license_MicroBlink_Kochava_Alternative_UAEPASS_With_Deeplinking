package com.aecb.ui.registerflow.personaldetails.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.aecb.App;
import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.base.mvp.BaseActivity;
import com.aecb.base.mvp.BlurDialogBaseFragment;
import com.aecb.data.api.models.nationalities.NationalitiesItem;
import com.aecb.data.api.models.registeruser.RegisterUserRequest;
import com.aecb.databinding.ActivityPersonalDetailsBinding;
import com.aecb.listeners.OnDateSetListener;
import com.aecb.microblink.result.extract.BaseResultExtractor;
import com.aecb.microblink.result.extract.RecognitionResultEntry;
import com.aecb.microblink.result.extract.ResultExtractorFactoryProvider;
import com.aecb.presentation.customviews.ToolbarView;
import com.aecb.presentation.dialogs.DatePickerFragment;
import com.aecb.ui.registerflow.contactdetails.view.ContactDetailsActivity;
import com.aecb.ui.registerflow.personaldetails.presenter.PersonalDetailsContract;
import com.aecb.ui.searchdialog.view.SearchFragment;
import com.aecb.ui.termsandconditions.view.TermsAndConditionsFragment;
import com.aecb.util.EmirateChangeListenerValidation;
import com.aecb.util.ValidationUtil;
import com.google.gson.Gson;
import com.microblink.entities.recognizers.Recognizer;
import com.microblink.entities.recognizers.RecognizerBundle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import timber.log.Timber;

import static com.aecb.AppConstants.Gender.FEMALE;
import static com.aecb.AppConstants.Gender.FEMALE_CODE;
import static com.aecb.AppConstants.Gender.MALE;
import static com.aecb.AppConstants.Gender.MALE_CODE;
import static com.aecb.AppConstants.IntentKey.REGISTER_REQUEST;
import static com.aecb.AppConstants.IntentKey.SCANNED;
import static com.aecb.AppConstants.MINIMUM_REQUIRED_AGE;
import static com.aecb.AppConstants.SIMPLEDATEFORMAT_FOR_DOB;
import static com.aecb.AppConstants.arabicToDecimal;
import static com.aecb.AppConstants.confirmEmail;
import static com.aecb.AppConstants.email;
import static com.aecb.AppConstants.mobileNumber;
import static com.aecb.util.FirebaseLogging.dobUpdate;
import static com.aecb.util.FirebaseLogging.emirateIdUpdate;
import static com.aecb.util.FirebaseLogging.fullNameUpdate;
import static com.aecb.util.FirebaseLogging.genderUpdate;
import static com.aecb.util.FirebaseLogging.nationalityUpdate;
import static com.aecb.util.varibles.StringConstants.DOB_PICKER;

public class PersonalDetailsActivity extends BaseActivity<PersonalDetailsContract.View,
        PersonalDetailsContract.Presenter> implements PersonalDetailsContract.View, View.OnClickListener,
        OnDateSetListener, CompoundButton.OnCheckedChangeListener, TextWatcher {

    @Inject
    public PersonalDetailsContract.Presenter mPresenter;
    protected RecognizerBundle mRecognizerBundle;
    ActivityPersonalDetailsBinding personalDetailsBinding;
    SearchFragment searchFragment;
    List<RecognitionResultEntry> resultEntries;
    private Calendar calendar = Calendar.getInstance();
    private String selectedGender;
    private String selectedNationality;
    private Boolean agreeTermsAndCondition = false;
    private boolean isDelete;
    private ArrayList<NationalitiesItem> allNationalityData = new ArrayList<>();
    private BlurDialogBaseFragment termsConditionFragment;
    private List<Recognizer> mRecognizersWithResult;
    private PersonalDetailsActivity mContext;
    private boolean isScanned = false;
    private String selectedYearByEmirates = "";
    private String scannedFullName = "";
    private String scannedEmirateID = "";
    private String scannedGender = "";
    private String scannedDOB = "";
    private String scannedNationality = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mobileNumber = "";
        email = "";
        confirmEmail = "";
        mContext = this;
        if (getIntent() != null) {
            if (getIntent().getBooleanExtra(SCANNED, false)) {
                mRecognizersWithResult = obtainRecognizersWithResult();
                isScanned = true;
            } else {
                isScanned = false;
            }
        }
        enableBinding();
        mPresenter.getAllNationalities();
    }

    @CallSuper
    @Override
    protected void onResume() {
        super.onResume();
        // clear saved state to be sure that data is cleared from cache and from file when
        // intent optimisation is used
        if (mRecognizerBundle != null) {
            mRecognizerBundle.clearSavedState();
        }
    }

    protected List<RecognitionResultEntry> createResultEntries() {
        // this must be called after the activity has been created
        List<RecognitionResultEntry> extractedData = new ArrayList<>();
        try {
            Recognizer<Recognizer.Result> recognizer = getRecognizerAtPosition(0);

            // Extract data from BaseRecognitionResult
            BaseResultExtractor resultExtractor = ResultExtractorFactoryProvider.get().createExtractor(recognizer);
            extractedData = resultExtractor.extractData(this, recognizer);

            if (extractedData.size() <= 0) {
                Toast.makeText(this, getString(R.string.result_list_is_empty), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Timber.e(e.getMessage());
        }


        return extractedData;
    }

    public Recognizer<Recognizer.Result> getRecognizerAtPosition(int resultPosition) {
        if (resultPosition > 0 || resultPosition <= mRecognizersWithResult.size()) {
           /* throw new IllegalStateException("Recognizer with non empty result on requested position"
                    + " does not exist. Possible cause is that recognizer bundle state has been lost"
                    + " in intent transactions.");*/
        }
        //noinspection unchecked
        return mRecognizersWithResult.get(resultPosition);
    }

    private List<Recognizer> obtainRecognizersWithResult() {
        try {
            List<Recognizer> recognizersWithResult = new ArrayList<>();
            mRecognizerBundle = new RecognizerBundle();
            mRecognizerBundle.loadFromIntent(getIntent());
            for (Recognizer<Recognizer.Result> r : mRecognizerBundle.getRecognizers()) {
                if (r.getResult().getResultState() != Recognizer.Result.State.Empty) {
                    recognizersWithResult.add(r);
                }
            }
            return recognizersWithResult;
        } catch (Exception e) {
            Timber.d("Exception : " + e.toString());
            return new ArrayList<>();
        }
    }

    @CallSuper
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mRecognizerBundle != null) {
            mRecognizerBundle.saveState();
        }
    }

    private void enableBinding() {
        personalDetailsBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        initSpinners();
        personalDetailsBinding.inputIdDateOfBirth.setOnClickListener(this);
        personalDetailsBinding.tvLabelNationality.setOnClickListener(this);
        personalDetailsBinding.tvNationality.setOnClickListener(this);
        personalDetailsBinding.tvLabelDOB.setOnClickListener(this);
        personalDetailsBinding.btnNext.setOnClickListener(this);
        personalDetailsBinding.edtName.setOnClickListener(this);
        personalDetailsBinding.spinnerGender.setOnClickListener(this);
        personalDetailsBinding.tvTermsAndCondition.setOnClickListener(this);
        personalDetailsBinding.cbAgreeTermsConditions.setOnCheckedChangeListener(this);
        personalDetailsBinding.edtName.addTextChangedListener(this);
        personalDetailsBinding.edtEmirateFirst.addTextChangedListener(this);
        personalDetailsBinding.edtEmirateSecond.addTextChangedListener(this);
        personalDetailsBinding.edtEmirateThird.addTextChangedListener(this);
        personalDetailsBinding.edtEmirateLast.addTextChangedListener(this);
        personalDetailsBinding.dummy.requestFocus();
        personalDetailsBinding.edtEmirateLast.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    View view = mContext.getCurrentFocus();
                    if (view == null) {
                        view = new View(mContext);
                    }
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    personalDetailsBinding.spinnerGender.expand();
                    personalDetailsBinding.dummy.requestFocus();
                }
                return false;
            }
        });
        personalDetailsBinding.edtEmirateNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!personalDetailsBinding.edtName.getText().toString().isEmpty() &&
                        !personalDetailsBinding.edtEmirateFirst.getText().toString().isEmpty() &&
                        !personalDetailsBinding.edtEmirateSecond.getText().toString().isEmpty() &&
                        !personalDetailsBinding.edtEmirateThird.getText().toString().isEmpty() &&
                        !personalDetailsBinding.edtEmirateLast.getText().toString().isEmpty() &&
                        selectedGender != null && !selectedGender.isEmpty() &&
                        personalDetailsBinding.tvNationality.getText() != null &&
                        !personalDetailsBinding.tvNationality.getText().toString().isEmpty() &&
                        !personalDetailsBinding.inputIdDateOfBirth.getText().toString().isEmpty() &&
                        personalDetailsBinding.cbAgreeTermsConditions.isChecked()) {
                    personalDetailsBinding.btnNext.setEnabled(true);
                    personalDetailsBinding.btnNext.setBackground(getResources().getDrawable(R.drawable.bg_blue_button));
                } else {
                    personalDetailsBinding.btnNext.setEnabled(false);
                    personalDetailsBinding.btnNext.setBackground(getResources().getDrawable(R.drawable.btn_disable_state));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.hashCode() == personalDetailsBinding.edtEmirateNo.getText().hashCode()) {
                    /* we using custom id format so user entering value then we appending dash*/
                    if (isDelete) {
                        isDelete = false;
                        return;
                    }
                    EmirateChangeListenerValidation.filterEID(s, personalDetailsBinding.edtEmirateNo, this);
                }
            }
        });
        editTextFocusListeners();
        focusChangeListener();
    }

    void initSpinners() {
        personalDetailsBinding.spinnerGender.setItems(getResources().getStringArray(R.array.genders));
        personalDetailsBinding.spinnerGender.setOnItemSelectedListener((view, position, id, item, parent) -> {
            if (position == 0) {
                selectedGender = MALE;
            } else if (position == 1) {
                selectedGender = FEMALE;
            }
            personalDetailsBinding.viewLineGender.setBackgroundResource(R.color.gray_light);
            personalDetailsBinding.tvGenderValidation.setVisibility(View.GONE);
            //openNationality();
            if (!personalDetailsBinding.edtName.getText().toString().isEmpty() &&
                    !personalDetailsBinding.edtEmirateFirst.getText().toString().isEmpty() &&
                    !personalDetailsBinding.edtEmirateSecond.getText().toString().isEmpty() &&
                    !personalDetailsBinding.edtEmirateThird.getText().toString().isEmpty() &&
                    !personalDetailsBinding.edtEmirateLast.getText().toString().isEmpty() &&
                    selectedGender != null && !selectedGender.isEmpty() &&
                    personalDetailsBinding.tvNationality.getText() != null &&
                    !personalDetailsBinding.tvNationality.getText().toString().isEmpty() &&
                    !personalDetailsBinding.inputIdDateOfBirth.getText().toString().isEmpty() &&
                    personalDetailsBinding.cbAgreeTermsConditions.isChecked()) {
                personalDetailsBinding.btnNext.setEnabled(true);
                personalDetailsBinding.btnNext.setBackground(getResources().getDrawable(R.drawable.bg_blue_button));
            } else {
                personalDetailsBinding.btnNext.setEnabled(false);
                personalDetailsBinding.btnNext.setBackground(getResources().getDrawable(R.drawable.btn_disable_state));
            }
        });
    }

    @NonNull
    @Override
    public PersonalDetailsContract.Presenter createPresenter() {
        return this.mPresenter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_personal_details;
    }

    @Override
    public void initToolbar() {
        ToolbarView tb = findViewById(R.id.toolBar);
        tb.setCancelBtnListener(this::onBackPressed);
        tb.setBtnVisibility(false, true);
        tb.setToolbarTitle(R.string.add_your_details);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inputIdDateOfBirth:
            case R.id.tvLabelDOB:
                if (personalDetailsBinding.inputIdDateOfBirth.getText().toString().isEmpty()) {
                    personalDetailsBinding.viewLineDOB.setBackgroundResource(R.color.underlineColor);
                }
                openDatePicker();
                break;

            case R.id.tvNationality:
            case R.id.tvLabelNationality:
                if (personalDetailsBinding.tvNationality.getText() == null ||
                        personalDetailsBinding.tvNationality.getText().toString().isEmpty()) {
                    personalDetailsBinding.viewLineNationality.setBackgroundResource(R.color.underlineColor);
                }
                openNationality();
                break;

            case R.id.btnNext:
                mPresenter.sendPersonalDetails(personalDetailsBinding.edtName.getText().toString(),
                        personalDetailsBinding.edtEmirateFirst.getText().toString() +
                                personalDetailsBinding.edtEmirateSecond.getText().toString() +
                                personalDetailsBinding.edtEmirateThird.getText().toString() +
                                personalDetailsBinding.edtEmirateLast.getText().toString(), selectedGender,
                        selectedNationality,
                        personalDetailsBinding.inputIdDateOfBirth.getText().toString(),
                        personalDetailsBinding.edtPassport.getText().toString(),
                        agreeTermsAndCondition, selectedYearByEmirates);
                registerUpdateScannedEvents();
                break;
            case R.id.tvTermsAndCondition:
                termsConditionFragment = TermsAndConditionsFragment.newInstance(null);
                termsConditionFragment.show(getSupportFragmentManager(), termsConditionFragment.getTag());
                break;
            case R.id.edtName:
                if (personalDetailsBinding.edtName.getText().toString().isEmpty()) {
                    personalDetailsBinding.viewLineFullName.setBackgroundResource(R.color.underlineColor);
                }
                break;
            case R.id.spinnerGender:
                if (selectedGender == null || selectedGender.isEmpty()) {
                    personalDetailsBinding.viewLineGender.setBackgroundResource(R.color.underlineColor);
                }
                break;
        }
    }

    private void registerUpdateScannedEvents() {
        if (!scannedFullName.equals(personalDetailsBinding.edtName.getText().toString())) {
            fullNameUpdate();
        }
        String fullEmirateId = personalDetailsBinding.edtEmirateFirst.getText().toString() +
                personalDetailsBinding.edtEmirateSecond.getText().toString() +
                personalDetailsBinding.edtEmirateThird.getText().toString() +
                personalDetailsBinding.edtEmirateLast.getText().toString();
        if (!scannedEmirateID.equals(fullEmirateId)) {
            emirateIdUpdate();
        }
        if (!scannedGender.equals(selectedGender)) {
            genderUpdate();
        }
        if (!scannedDOB.equals(personalDetailsBinding.inputIdDateOfBirth.getText())) {
            dobUpdate();
        }
        if (!scannedNationality.equals(selectedNationality)) {
            nationalityUpdate();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (view.getTag().toString().equals(DOB_PICKER)) {
            calendar.set(year, month, day);
            personalDetailsBinding.inputIdDateOfBirth.setText(SIMPLEDATEFORMAT_FOR_DOB.format(calendar.getTime()));
            personalDetailsBinding.edtPassport.requestFocus();
            personalDetailsBinding.viewLineDOB.setBackgroundResource(R.color.gray_light);
            personalDetailsBinding.tvDOBValidation.setVisibility(View.GONE);
            if (!personalDetailsBinding.edtName.getText().toString().isEmpty() &&
                    !personalDetailsBinding.edtEmirateFirst.getText().toString().isEmpty() &&
                    !personalDetailsBinding.edtEmirateSecond.getText().toString().isEmpty() &&
                    !personalDetailsBinding.edtEmirateThird.getText().toString().isEmpty() &&
                    !personalDetailsBinding.edtEmirateLast.getText().toString().isEmpty() &&
                    selectedGender != null && !selectedGender.isEmpty() &&
                    personalDetailsBinding.tvNationality.getText() != null &&
                    !personalDetailsBinding.tvNationality.getText().toString().isEmpty() &&
                    !personalDetailsBinding.inputIdDateOfBirth.getText().toString().isEmpty() &&
                    personalDetailsBinding.cbAgreeTermsConditions.isChecked()) {
                personalDetailsBinding.btnNext.setEnabled(true);
                personalDetailsBinding.btnNext.setBackground(getResources().getDrawable(R.drawable.bg_blue_button));
            } else {
                personalDetailsBinding.btnNext.setEnabled(false);
                personalDetailsBinding.btnNext.setBackground(getResources().getDrawable(R.drawable.btn_disable_state));
            }
        }
        if (App.appComponent.getDataManager().getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ENG)) {
            Locale localeStartDate = new Locale(AppConstants.AppLanguage.ISO_CODE_ENG);
            getResources().getConfiguration().setLocale(localeStartDate);
            Locale.setDefault(getResources().getConfiguration().locale);
        } else {
            Locale localeStartDate = new Locale(AppConstants.AppLanguage.ISO_CODE_ARABIC);
            getResources().getConfiguration().setLocale(localeStartDate);
            Locale.setDefault(getResources().getConfiguration().locale);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        agreeTermsAndCondition = isChecked;
        if (!personalDetailsBinding.edtName.getText().toString().isEmpty() &&
                !personalDetailsBinding.edtEmirateFirst.getText().toString().isEmpty() &&
                !personalDetailsBinding.edtEmirateSecond.getText().toString().isEmpty() &&
                !personalDetailsBinding.edtEmirateThird.getText().toString().isEmpty() &&
                !personalDetailsBinding.edtEmirateLast.getText().toString().isEmpty() &&
                selectedGender != null && !selectedGender.isEmpty() &&
                personalDetailsBinding.tvNationality.getText() != null &&
                !personalDetailsBinding.tvNationality.getText().toString().isEmpty() &&
                !personalDetailsBinding.inputIdDateOfBirth.getText().toString().isEmpty() &&
                personalDetailsBinding.cbAgreeTermsConditions.isChecked()) {
            personalDetailsBinding.btnNext.setEnabled(true);
            personalDetailsBinding.btnNext.setBackground(getResources().getDrawable(R.drawable.bg_blue_button));
        } else {
            personalDetailsBinding.btnNext.setEnabled(false);
            personalDetailsBinding.btnNext.setBackground(getResources().getDrawable(R.drawable.btn_disable_state));
        }
    }

    @Override
    public void showEmptyNameError() {
        localValidationError(getString(R.string.error), getString(R.string.enter_valid_name));
        personalDetailsBinding.viewLineFullName.setBackgroundResource(R.color.underlineColor);
        personalDetailsBinding.tvFullNameValidation.setVisibility(View.GONE);
        personalDetailsBinding.scrollView.post(() -> personalDetailsBinding.scrollView.scrollTo(0, personalDetailsBinding.edtName.getTop()));
        personalDetailsBinding.edtName.requestFocus();
    }

    @Override
    public void showEmptyEmirateIdError() {
        localValidationError(getString(R.string.error), getString(R.string.enter_valid_id_number));
        personalDetailsBinding.tvEmirateValidation.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyGenderError() {
        localValidationError(getString(R.string.error), getString(R.string.please_select_gender));
        personalDetailsBinding.viewLineGender.setBackgroundResource(R.color.underlineColor);
        personalDetailsBinding.tvGenderValidation.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyNationalityError() {
        localValidationError(getString(R.string.error), getString(R.string.please_select_nationality));
        personalDetailsBinding.viewLineNationality.setBackgroundResource(R.color.underlineColor);
        personalDetailsBinding.tvNationalityValidation.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyDateOfBirthError() {
        localValidationError(getString(R.string.error), getString(R.string.please_select_date_of_birth));
        personalDetailsBinding.viewLineDOB.setBackgroundResource(R.color.underlineColor);
        personalDetailsBinding.tvDOBValidation.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyPassportError() {
        localValidationError(getString(R.string.error), getString(R.string.please_select_passport_no));
    }

    @Override
    public void showAgreeTCError() {
        localValidationError(getString(R.string.error), getString(R.string.please_agree_with_tc));
    }

    @Override
    public void showInvalidDobError() {
        localValidationError(getString(R.string.error), getString(R.string.dob_invalid_error));
    }

    @Override
    public void showDOBisNot18Years() {
        localValidationError(getString(R.string.error), getString(R.string.age_is_not_18_yrs));
    }

    @Override
    public void showInvalidSecondEmirateIdError() {
        localValidationError(getString(R.string.error), getString(R.string.second_emirate_id_error));
        personalDetailsBinding.scrollView.post(() -> personalDetailsBinding.scrollView.scrollTo(0, personalDetailsBinding.edtEmirateSecond.getTop()));
        personalDetailsBinding.edtEmirateSecond.requestFocus();
    }

    @Override
    public void showInvalidNameError() {
        localValidationError(getString(R.string.error), getString(R.string.enter_valid_name));
        personalDetailsBinding.viewLineFullName.setBackgroundResource(R.color.underlineColor);
        personalDetailsBinding.tvFullNameValidation.setVisibility(View.GONE);
        personalDetailsBinding.scrollView.post(() -> personalDetailsBinding.scrollView.scrollTo(0, personalDetailsBinding.edtName.getTop()));
        personalDetailsBinding.edtName.requestFocus();
    }

    @Override
    public void showInvalidEmirateIdError() {
        localValidationError(getString(R.string.error), getString(R.string.enter_valid_id_number));
        personalDetailsBinding.scrollView.post(() -> personalDetailsBinding.scrollView.scrollTo(0, personalDetailsBinding.edtEmirateFirst.getTop()));
        if (personalDetailsBinding.edtEmirateFirst.getText().length() != 3) {
            personalDetailsBinding.edtEmirateFirst.requestFocus();
        } else if (personalDetailsBinding.edtEmirateSecond.getText().length() != 4) {
            personalDetailsBinding.edtEmirateSecond.requestFocus();
        } else if (personalDetailsBinding.edtEmirateThird.getText().length() != 7) {
            personalDetailsBinding.edtEmirateThird.requestFocus();
        } else if (personalDetailsBinding.edtEmirateLast.getText().length() != 1) {
            personalDetailsBinding.edtEmirateLast.requestFocus();
        }
    }

    @Override
    public void showInvalidPassportError() {
        localValidationError(getString(R.string.error), getString(R.string.please_select_passport_no));
    }


    @Override
    public void loadAllNationalities(List<NationalitiesItem> nationalities) {
        allNationalityData.addAll(nationalities);
        resultEntries = new ArrayList<>();
        if (isScanned) {
            resultEntries.addAll(createResultEntries());
            for (RecognitionResultEntry recognitionResultEntry : resultEntries) {
                try {
                    if (recognitionResultEntry.getKey().equals(getString(R.string.PPFullName))) {
                        personalDetailsBinding.edtName.setText(recognitionResultEntry.getValue());
                        scannedFullName = recognitionResultEntry.getValue();
                    }
                } catch (Exception e) {
                    Timber.d("Exception : " + e.toString());
                }
                if (recognitionResultEntry.getKey().equals(getString(R.string.PPSex))) {
                    if (recognitionResultEntry.getValue().equals(MALE_CODE)) {
                        personalDetailsBinding.spinnerGender.setSelectedIndex(0);
                        selectedGender = MALE;
                        scannedGender = MALE;
                    } else if (recognitionResultEntry.getValue().equals(FEMALE_CODE)) {
                        personalDetailsBinding.spinnerGender.setSelectedIndex(1);
                        selectedGender = FEMALE;
                        scannedGender = FEMALE;
                    }
                }
                if (recognitionResultEntry.getKey().equals(getString(R.string.PPPersonalNumber))) {
                    String fullEmirateId = recognitionResultEntry.getValue();
                    scannedEmirateID = recognitionResultEntry.getValue().replaceAll("-", "");
                    //personalDetailsBinding.edtEmirateFirst.setText(fullEmirateId.substring(0, 3));
                    personalDetailsBinding.edtEmirateSecond.setText(fullEmirateId.substring(4, 8));
                    selectedYearByEmirates = fullEmirateId.substring(4, 8);
                    personalDetailsBinding.edtEmirateThird.setText(fullEmirateId.substring(9, 16));
                    personalDetailsBinding.edtEmirateLast.setText(fullEmirateId.substring(fullEmirateId.length() - 1));
                    personalDetailsBinding.edtName.requestFocus();
                    personalDetailsBinding.edtName.setSelection(personalDetailsBinding.edtName.getText().length());
                    //personalDetailsBinding.edtEmirateNo.setText(recognitionResultEntry.getValue());
                }
                if (recognitionResultEntry.getKey().equals(getString(R.string.PPDateOfBirth))) {
                    personalDetailsBinding.inputIdDateOfBirth.setText(arabicToDecimal(recognitionResultEntry.getValue()));
                    scannedDOB = arabicToDecimal(recognitionResultEntry.getValue());
                }
                if (recognitionResultEntry.getKey().equals(getString(R.string.PPNationality))) {
                    for (NationalitiesItem nationalitiesItem : nationalities) {
                        if (recognitionResultEntry.getValue().equals(nationalitiesItem.getThreeDigitIso())) {
                            setSelectedNationality(nationalitiesItem);
                            scannedNationality = nationalitiesItem.getNationalityId();
                        }
                    }
                }

            }
        }

    }

    @Override
    public void showError(String message) {
        ValidationUtil.showToast(this, message);
    }

    @Override
    public void openContactDetailsActivity() {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setFullName(personalDetailsBinding.edtName.getText().toString());
        registerUserRequest.setEmiratesID(personalDetailsBinding.edtEmirateFirst.getText().toString() +
                personalDetailsBinding.edtEmirateSecond.getText().toString() +
                personalDetailsBinding.edtEmirateThird.getText().toString() +
                personalDetailsBinding.edtEmirateLast.getText().toString());
        registerUserRequest.setDob(personalDetailsBinding.inputIdDateOfBirth.getText().toString());
        if (selectedGender.equalsIgnoreCase(MALE)) {
            registerUserRequest.setGender("1");
        } else if (selectedGender.equalsIgnoreCase(FEMALE)) {
            registerUserRequest.setGender("2");
        } else {
            registerUserRequest.setGender("1");
        }
        registerUserRequest.setPassport(personalDetailsBinding.edtPassport.getText().toString());
        registerUserRequest.setNationality(selectedNationality);
        registerUserRequest.setPreferredlanguage(mPresenter.getCurrentAppLanguage());
        Intent intent = new Intent(this, ContactDetailsActivity.class);
        intent.putExtra(REGISTER_REQUEST, new Gson().toJson(registerUserRequest));
        startActivity(intent);
    }

    void editTextFocusListeners() {
        personalDetailsBinding.edtName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                personalDetailsBinding.txtInputLoutName.setHint(getString(R.string.hint_title_full_name));
            } else if (!personalDetailsBinding.edtName.getText().toString().isEmpty()) {
                personalDetailsBinding.txtInputLoutName.setHint(getString(R.string.hint_title_full_name));
            } else {
                personalDetailsBinding.txtInputLoutName.setHint(getString(R.string.hint_full_name_of_add_card));
            }
        });
        personalDetailsBinding.edtEmirateFirst.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && personalDetailsBinding.edtEmirateFirst.getText().toString().isEmpty()) {
                personalDetailsBinding.edtEmirateFirst.setBackgroundResource(R.drawable.ic_bg_emiates_id_red);
            }
        });
        personalDetailsBinding.edtEmirateSecond.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && personalDetailsBinding.edtEmirateSecond.getText().toString().isEmpty()) {
                personalDetailsBinding.edtEmirateSecond.setBackgroundResource(R.drawable.ic_bg_emiates_id_red);
            }
        });
        personalDetailsBinding.edtEmirateThird.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && personalDetailsBinding.edtEmirateThird.getText().toString().isEmpty()) {
                personalDetailsBinding.edtEmirateThird.setBackgroundResource(R.drawable.ic_bg_emiates_id_red);
            }
        });
        personalDetailsBinding.edtEmirateLast.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && personalDetailsBinding.edtEmirateLast.getText().toString().isEmpty()) {
                personalDetailsBinding.edtEmirateLast.setBackgroundResource(R.drawable.ic_bg_emiates_id_red);
            }
        });
        personalDetailsBinding.edtPassport.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                personalDetailsBinding.txtInputLoutPassport.setHint(getString(R.string.passport_number_optional_bracket));
            } else if (!personalDetailsBinding.edtPassport.getText().toString().isEmpty()) {
                personalDetailsBinding.txtInputLoutPassport.setHint(getString(R.string.passport_number_optional_bracket));
            } else {
                personalDetailsBinding.txtInputLoutPassport.setHint(getString(R.string.passport_number_optional_bracket));
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if (personalDetailsBinding.edtName.length() != 0) {
            fullNameValidation(personalDetailsBinding.edtName.getText().toString());
        } else {
            personalDetailsBinding.viewLineFullName.setBackgroundResource(R.color.underlineColor);
            personalDetailsBinding.tvFullNameValidation.setVisibility(View.GONE);
        }
        if (charSequence.hashCode() == personalDetailsBinding.edtEmirateFirst.getText().hashCode()) {
            if (personalDetailsBinding.edtEmirateFirst.length() != 3) {
                personalDetailsBinding.edtEmirateFirst.setBackgroundResource(R.drawable.ic_bg_emiates_id_red);
            } else {
                personalDetailsBinding.edtEmirateFirst.setBackgroundResource(R.drawable.ic_bg_emirate_id);
            }
            if (personalDetailsBinding.edtEmirateFirst.length() == 3) {
                personalDetailsBinding.edtEmirateSecond.requestFocus();
                personalDetailsBinding.edtEmirateSecond.setFocusable(true);
            }
        } else if (charSequence.hashCode() == personalDetailsBinding.edtEmirateSecond.getText().hashCode()) {
            if (personalDetailsBinding.edtEmirateSecond.length() != 4) {
                personalDetailsBinding.edtEmirateSecond.setBackgroundResource(R.drawable.ic_bg_emiates_id_red);
            } else {
                personalDetailsBinding.edtEmirateSecond.setBackgroundResource(R.drawable.ic_bg_emirate_id);
            }
            if (personalDetailsBinding.edtEmirateSecond.length() == 4) {
                personalDetailsBinding.edtEmirateThird.requestFocus();
                personalDetailsBinding.edtEmirateThird.setFocusable(true);
                selectedYearByEmirates = personalDetailsBinding.edtEmirateSecond.getText().toString();
            } else if (personalDetailsBinding.edtEmirateSecond.length() == 0) {
                personalDetailsBinding.edtEmirateFirst.requestFocus();
                personalDetailsBinding.edtEmirateFirst.setFocusable(true);
            }
        } else if (charSequence.hashCode() == personalDetailsBinding.edtEmirateThird.getText().hashCode()) {
            if (personalDetailsBinding.edtEmirateThird.length() != 7) {
                personalDetailsBinding.edtEmirateThird.setBackgroundResource(R.drawable.ic_bg_emiates_id_red);
            } else {
                personalDetailsBinding.edtEmirateThird.setBackgroundResource(R.drawable.ic_bg_emirate_id);
            }
            if (personalDetailsBinding.edtEmirateThird.length() == 7) {
                personalDetailsBinding.edtEmirateLast.requestFocus();
                personalDetailsBinding.edtEmirateLast.setFocusable(true);
            } else if (personalDetailsBinding.edtEmirateThird.length() == 0) {
                personalDetailsBinding.edtEmirateSecond.requestFocus();
                personalDetailsBinding.edtEmirateSecond.setFocusable(true);
            }
        } else if (charSequence.hashCode() == personalDetailsBinding.edtEmirateLast.getText().hashCode()) {
            if (personalDetailsBinding.edtEmirateLast.length() != 0) {
                personalDetailsBinding.edtEmirateLast.setBackgroundResource(R.drawable.ic_bg_emirate_id);
            } else {
                personalDetailsBinding.edtEmirateLast.setBackgroundResource(R.drawable.ic_bg_emiates_id_red);
            }
            if (personalDetailsBinding.edtEmirateLast.length() == 0) {
                personalDetailsBinding.edtEmirateThird.requestFocus();
                personalDetailsBinding.edtEmirateThird.setFocusable(true);
            }
        }

        if (!personalDetailsBinding.edtName.getText().toString().isEmpty() &&
                !personalDetailsBinding.edtEmirateFirst.getText().toString().isEmpty() &&
                !personalDetailsBinding.edtEmirateSecond.getText().toString().isEmpty() &&
                !personalDetailsBinding.edtEmirateThird.getText().toString().isEmpty() &&
                !personalDetailsBinding.edtEmirateLast.getText().toString().isEmpty() &&
                selectedGender != null && !selectedGender.isEmpty() &&
                personalDetailsBinding.tvNationality.getText() != null &&
                !personalDetailsBinding.tvNationality.getText().toString().isEmpty() &&
                !personalDetailsBinding.inputIdDateOfBirth.getText().toString().isEmpty() &&
                personalDetailsBinding.cbAgreeTermsConditions.isChecked()) {
            personalDetailsBinding.btnNext.setEnabled(true);
            personalDetailsBinding.btnNext.setBackground(getResources().getDrawable(R.drawable.bg_blue_button));
        } else {
            personalDetailsBinding.btnNext.setEnabled(false);
            personalDetailsBinding.btnNext.setBackground(getResources().getDrawable(R.drawable.btn_disable_state));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void setSelectedNationality(NationalitiesItem nationalitiesItem) {
        selectedNationality = nationalitiesItem.getNationalityId();
        personalDetailsBinding.tvNationality.setText(nationalitiesItem.getCountryName());
        /*if (!isScanned) {
            openDatePicker();
        }*/
        personalDetailsBinding.viewLineNationality.setBackgroundResource(R.color.gray_light);
        personalDetailsBinding.tvNationalityValidation.setVisibility(View.GONE);
        if (!personalDetailsBinding.edtName.getText().toString().isEmpty() &&
                !personalDetailsBinding.edtEmirateFirst.getText().toString().isEmpty() &&
                !personalDetailsBinding.edtEmirateSecond.getText().toString().isEmpty() &&
                !personalDetailsBinding.edtEmirateThird.getText().toString().isEmpty() &&
                !personalDetailsBinding.edtEmirateLast.getText().toString().isEmpty() &&
                selectedGender != null && !selectedGender.isEmpty() &&
                personalDetailsBinding.tvNationality.getText() != null &&
                !personalDetailsBinding.tvNationality.getText().toString().isEmpty() &&
                !personalDetailsBinding.inputIdDateOfBirth.getText().toString().isEmpty() &&
                personalDetailsBinding.cbAgreeTermsConditions.isChecked()) {
            personalDetailsBinding.btnNext.setEnabled(true);
            personalDetailsBinding.btnNext.setBackground(getResources().getDrawable(R.drawable.bg_blue_button));
        } else {
            personalDetailsBinding.btnNext.setEnabled(false);
            personalDetailsBinding.btnNext.setBackground(getResources().getDrawable(R.drawable.btn_disable_state));
        }
    }

    private void focusChangeListener() {
        personalDetailsBinding.edtEmirateNo.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                isDelete = true;
            }
            return false;
        });
    }

    public void checkTermsConditions(boolean b) {
        personalDetailsBinding.cbAgreeTermsConditions.setChecked(b);
    }

    public void openNationality() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstants.IntentKey.NATIONALITY_LIST, allNationalityData);
        searchFragment = SearchFragment.newInstance(bundle);
        searchFragment.show(getSupportFragmentManager(), searchFragment.getTag());
    }

    public void openDatePicker() {
        AppConstants.setLocalLanguageEng(this);
        DialogFragment birthDate;
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -MINIMUM_REQUIRED_AGE);
        birthDate = DatePickerFragment.newInstance(0, c.getTimeInMillis(), "", AppConstants.DateFormats.DD_MM_YY, DOB_PICKER, selectedYearByEmirates);
        birthDate.show(getSupportFragmentManager(), DOB_PICKER);
    }

    private void fullNameValidation(String fullName) {
        String[] splitedFullName = fullName.split("\\s+");
        String lastName = "";
        try {
            lastName = splitedFullName[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!(ValidationUtil.isNullOrEmpty(fullName))) {
            for (String s : splitedFullName) {
                if (s.length() <= 1 || s.length() > 120 || ValidationUtil.isNullOrEmpty(lastName)) {
                    personalDetailsBinding.viewLineFullName.setBackgroundResource(R.color.underlineColor);
                    personalDetailsBinding.tvFullNameValidation.setVisibility(View.GONE);
                    break;
                } else {
                    personalDetailsBinding.viewLineFullName.setBackgroundResource(R.color.gray_light);
                    personalDetailsBinding.tvFullNameValidation.setVisibility(View.GONE);
                }
            }
        }
    }
}