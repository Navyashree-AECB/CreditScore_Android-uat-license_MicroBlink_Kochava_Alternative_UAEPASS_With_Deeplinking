package com.aecb.ui.registerflow.securityquestionsroundone.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.base.mvp.BaseActivity;
import com.aecb.data.api.models.registeruser.RegisterUserRequest;
import com.aecb.data.api.models.securityquestions.AnswersItem;
import com.aecb.data.api.models.securityquestions.QuestionsItem;
import com.aecb.data.api.models.securityquestions.RoundQuestionsData;
import com.aecb.data.api.models.securityquestions.RoundSuccessData;
import com.aecb.data.api.models.securityquestions.SecurityQuestionsSubmitRequest;
import com.aecb.databinding.ActivitySecurityQuestionsRoundOneBinding;
import com.aecb.listeners.DialogButtonClickListener;
import com.aecb.presentation.commontitleview.CustomInputText;
import com.aecb.presentation.customviews.ToolbarView;
import com.aecb.ui.contactusregistration.view.ContactUsRegistrationFragment;
import com.aecb.ui.registerflow.createpassword.view.CreatePasswordActivity;
import com.aecb.ui.registerflow.securityquestionsroundone.adapter.SecurityQuestionOneAdapter;
import com.aecb.ui.registerflow.securityquestionsroundone.presenter.SecurityQuestionsRoundOneContract;
import com.aecb.ui.registerflow.securityquestionsroundtwo.view.SecurityQuestionsRoundTwoActivity;
import com.aecb.ui.startsup.view.StartupActivity;
import com.aecb.util.ItemOffsetDecoration;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

import static com.aecb.AppConstants.IntentKey.MOBILE;
import static com.aecb.AppConstants.IntentKey.REGISTER_REQUEST;
import static com.aecb.AppConstants.IntentKey.ROUND_QUESTIONS_FROM_REGISTER;
import static com.aecb.AppConstants.IntentKey.SECURITY_QUESTION;
import static com.aecb.AppConstants.QuestionTypes.A_MINUS;
import static com.aecb.AppConstants.QuestionTypes.A_ONLY;
import static com.aecb.AppConstants.QuestionTypes.A_PLUS;
import static com.aecb.AppConstants.QuestionTypes.B_ONLY;
import static com.aecb.util.FirebaseLogging.cancelSecurityRoundOne;

public class SecurityQuestionsRoundOneActivity extends BaseActivity<SecurityQuestionsRoundOneContract.View,
        SecurityQuestionsRoundOneContract.Presenter> implements SecurityQuestionsRoundOneContract.View,
        View.OnClickListener {

    @Inject
    public SecurityQuestionsRoundOneContract.Presenter mPresenter;
    ActivitySecurityQuestionsRoundOneBinding securityQuestionsRoundOneBinding;
    SecurityQuestionOneAdapter securityQuestionAdapter;
    List<QuestionsItem> questionsRoundOneList;
    int currentPosition = 0;
    RegisterUserRequest registerUserRequest;
    private LinearLayoutManager layoutManager;
    private RoundQuestionsData roundQuestionsData;
    private SecurityQuestionsSubmitRequest securityQuestionsSubmitRequest;
    private List<AnswersItem> answersList = new ArrayList<>();
    private List<QuestionsItem> typeAList = new ArrayList<>();
    private List<QuestionsItem> typeBList = new ArrayList<>();
    private String mobileNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromScreens();
        enableBinding();
        initRecyclerView();
    }


    void getDataFromScreens() {
        questionsRoundOneList = new ArrayList<>();
        securityQuestionsSubmitRequest = new SecurityQuestionsSubmitRequest();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            mobileNum = getIntent().getStringExtra(MOBILE);
            registerUserRequest = new Gson().fromJson(getIntent().getStringExtra(REGISTER_REQUEST), RegisterUserRequest.class);
            roundQuestionsData = new Gson().fromJson(bundle.getString(ROUND_QUESTIONS_FROM_REGISTER), RoundQuestionsData.class);
            Timber.e(roundQuestionsData.toString());
            for (QuestionsItem questionsItem : roundQuestionsData.getQuestions()) {
                if (questionsItem.getQuestionType().getFormattedValues().getDotQuestiontype().equals(A_ONLY)) {
                    typeAList.add(questionsItem);
                }
                if (questionsItem.getQuestionType().getFormattedValues().getDotQuestiontype().equals(B_ONLY)) {
                    typeBList.add(questionsItem);
                }
            }
            for (QuestionsItem questionsItemA : typeAList) {
                if (!questionsRoundOneList.contains(questionsItemA)) {
                    questionsRoundOneList.add(questionsItemA);
                }
                for (QuestionsItem questionsItemB : typeBList) {
                    if (questionsItemA.getId().equals(questionsItemB.getParentQuestionId())) {
                        if (!questionsRoundOneList.contains(questionsItemB)) {
                            questionsRoundOneList.add(questionsItemB);
                        }
                    }
                }
            }
            Timber.d(questionsRoundOneList.toString());
            securityQuestionsSubmitRequest.setApplicationId(roundQuestionsData.getApplicationId());
            securityQuestionsSubmitRequest.setIndividualId(roundQuestionsData.getIndividualId());
            securityQuestionsSubmitRequest.setPortalUserId(roundQuestionsData.getPortalUserId());
            securityQuestionsSubmitRequest.setQuestionRound(1);
        }
    }

    @SuppressLint("SetTextI18n")
    private void enableBinding() {
        securityQuestionsRoundOneBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        securityQuestionsRoundOneBinding.btnContinue.setOnClickListener(this);
        securityQuestionsRoundOneBinding.tvContactUs.setOnClickListener(this);
        securityQuestionsRoundOneBinding.tvquestionTitle.setText(getString(R.string.question));
        securityQuestionsRoundOneBinding.seekBar.setMax(questionsRoundOneList.size());
        securityQuestionsRoundOneBinding.seekBar.setProgress(currentPosition + 1);
    }

    void initRecyclerView() {
        securityQuestionsRoundOneBinding.rvQuestions.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        securityQuestionsRoundOneBinding.rvQuestions.setLayoutManager(layoutManager);
        securityQuestionAdapter = new SecurityQuestionOneAdapter(this, questionsRoundOneList);
        securityQuestionsRoundOneBinding.rvQuestions.addItemDecoration(new ItemOffsetDecoration(this, R.dimen._6sdp));
        securityQuestionsRoundOneBinding.rvQuestions.setAdapter(securityQuestionAdapter);
    }

    @NonNull
    @Override
    public SecurityQuestionsRoundOneContract.Presenter createPresenter() {
        return this.mPresenter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_security_questions_round_one;
    }

    @Override
    public void initToolbar() {
        ToolbarView tb = findViewById(R.id.toolBar);
        tb.setCancelBtnListener(this::onBackPressed);
        tb.setBtnVisibility(false, true);
        tb.setToolbarTitle(R.string.security_questions);
    }

    @Override
    public void onBackPressed() {
        cancelSecurityRoundOne();
        moveActivity(this, StartupActivity.class, true, true);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnContinue) {
            proceedToNextQuestion();
        } else if (v.getId() == R.id.tvContactUs) {
            Bundle bundle = new Bundle();
            bundle.putString(REGISTER_REQUEST, new Gson().toJson(registerUserRequest));
            bundle.putString(MOBILE, mobileNum);
            ContactUsRegistrationFragment contactUsRegistrationFragment =
                    ContactUsRegistrationFragment.newInstance(bundle);
            contactUsRegistrationFragment.show(getSupportFragmentManager(), contactUsRegistrationFragment.getTag());
        }
    }

    @SuppressLint("SetTextI18n")
    void proceedToNextQuestion() {
        try {
            SecurityQuestionOneAdapter.ItemViewHolder itemViewHolderForNow =
                    (SecurityQuestionOneAdapter.ItemViewHolder) securityQuestionsRoundOneBinding.rvQuestions
                            .findViewHolderForAdapterPosition(currentPosition);
            CustomInputText customInputTextNow = itemViewHolderForNow.itemView.findViewById(R.id.edtAnswer);
            RadioGroup radioGroup = itemViewHolderForNow.itemView.findViewById(R.id.rgQuestions);
            if (questionsRoundOneList.get(currentPosition).getQuestionType().
                    getFormattedValues().getDotQuestiontype().equals(A_PLUS) ||
                    questionsRoundOneList.get(currentPosition).getQuestionType()
                            .getFormattedValues().getDotQuestiontype().equals(A_MINUS) ||
                    questionsRoundOneList.get(currentPosition).getQuestionType()
                            .getFormattedValues().getDotQuestiontype().equals(A_ONLY)) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.rbYes) {
                    AnswersItem answersItem = new AnswersItem();
                    answersItem.setDotAnswer("1");
                    answersItem.setDotAuthenticationquestionid(questionsRoundOneList.get(currentPosition).getId());
                    answersList.add(answersItem);
                    radioGroup.clearCheck();
                    if (currentPosition < (questionsRoundOneList.size() - 1)) {
                        currentPosition = currentPosition + 1;
                        securityQuestionsRoundOneBinding.rvQuestions.post(() ->
                                securityQuestionsRoundOneBinding.rvQuestions.scrollToPosition(currentPosition));
                        securityQuestionsRoundOneBinding.seekBar.setProgress(currentPosition + 1);
                        securityQuestionsRoundOneBinding.tvquestionTitle.setText(getString(R.string.question));
                    } else {
                        securityQuestionsSubmitRequest.setAnswers(answersList);
                        mPresenter.submitSecurityQuestions(securityQuestionsSubmitRequest);
                    }
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbNo) {
                    AnswersItem answersItem = new AnswersItem();
                    answersItem.setDotAnswer("0");
                    answersItem.setDotAuthenticationquestionid(questionsRoundOneList.get(currentPosition).getId());
                    AnswersItem answersItemForChild = new AnswersItem();
                    answersItemForChild.setDotAnswer("0");
                    answersItemForChild.setDotAuthenticationquestionid(questionsRoundOneList.get(currentPosition + 1).getId());
                    answersList.add(answersItem);
                    answersList.add(answersItemForChild);
                    radioGroup.clearCheck();
                    if (currentPosition + 1 == (questionsRoundOneList.size() - 1)) {
                        securityQuestionsSubmitRequest.setAnswers(answersList);
                        mPresenter.submitSecurityQuestions(securityQuestionsSubmitRequest);
                    } else if (currentPosition < (questionsRoundOneList.size() - 1)) {
                        currentPosition = currentPosition + 2;
                        securityQuestionsRoundOneBinding.rvQuestions.post(() ->
                                securityQuestionsRoundOneBinding.rvQuestions.scrollToPosition(currentPosition));
                        securityQuestionsRoundOneBinding.seekBar.setProgress(currentPosition + 1);
                        securityQuestionsRoundOneBinding.tvquestionTitle.setText(getString(R.string.question));
                    } else {
                        securityQuestionsSubmitRequest.setAnswers(answersList);
                        mPresenter.submitSecurityQuestions(securityQuestionsSubmitRequest);
                    }
                } else {
                    localValidationError(getString(R.string.error), getString(R.string.please_enter_answer));
                }
            } else if (!customInputTextNow.getText().equals("")) {
                if (Integer.parseInt(customInputTextNow.getText()) > 100000000) {
                    localValidationError(getString(R.string.error), getString(R.string.amount_is_not_valid));
                } else {
                    AnswersItem answersItem = new AnswersItem();
                    answersItem.setDotAnswer(customInputTextNow.getText());
                    answersItem.setDotAuthenticationquestionid(questionsRoundOneList.get(currentPosition).getId());
                    answersList.add(answersItem);
                    customInputTextNow.setText(null);
                    radioGroup.clearCheck();
                    if (currentPosition < (questionsRoundOneList.size() - 1)) {
                        currentPosition = currentPosition + 1;
                        securityQuestionsRoundOneBinding.rvQuestions.post(() ->
                                securityQuestionsRoundOneBinding.rvQuestions.scrollToPosition(currentPosition));
                        securityQuestionsRoundOneBinding.seekBar.setProgress(currentPosition + 1);
                        securityQuestionsRoundOneBinding.tvquestionTitle.setText(getString(R.string.question));
                    } else {
                        securityQuestionsSubmitRequest.setAnswers(answersList);
                        mPresenter.submitSecurityQuestions(securityQuestionsSubmitRequest);
                    }
                }
            } else {
                localValidationError(getString(R.string.error), getString(R.string.please_enter_answer));
            }
        } catch (Exception e) {
            Timber.d("Exception : " + e.toString());
        }
    }

    @Override
    public void openPasswordScreen(RoundSuccessData roundSuccessData) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.IntentKey.SESSION_TOKEN, roundSuccessData.getParseUser().getSessionToken());
        bundle.putString(AppConstants.IntentKey.FROM_ACTIVITY, SECURITY_QUESTION);
        moveActivity(this, CreatePasswordActivity.class, false, false, bundle);
    }

    public void showRoundTwoPop(RoundQuestionsData roundQuestionsData) {
        showErrorMsgFromApi(getString(R.string.error), getString(R.string.proceed_to_round_two_desc),
                getString(R.string.ok), null, new DialogButtonClickListener() {
                    @Override
                    public void onPositiveButtonClicked() {
                        Bundle bundle = new Bundle();
                        bundle.putString(MOBILE, mobileNum);
                        bundle.putString(ROUND_QUESTIONS_FROM_REGISTER, new Gson().toJson(roundQuestionsData));
                        moveActivity(SecurityQuestionsRoundOneActivity.this, SecurityQuestionsRoundTwoActivity.class, false, false, bundle);
                    }

                    @Override
                    public void onNegativeButtonClicked() {
                    }
                });
    }

    @Override
    public void openQuestionsRoundTwoScreen(RoundQuestionsData roundQuestionsData) {
        showRoundTwoPop(roundQuestionsData);
    }

    @Override
    public void openContactUsOrLoginDialog() {
        showErrorMsgFromApi(getString(R.string.error), getString(R.string.user_not_authenticated_desc),
                getString(R.string.txt_proceed), getString(R.string.contact_us), new DialogButtonClickListener() {
                    @Override
                    public void onPositiveButtonClicked() {
                        cancelSecurityRoundOne();
                        moveActivity(SecurityQuestionsRoundOneActivity.this, StartupActivity.class, true, true);
                    }

                    @Override
                    public void onNegativeButtonClicked() {
                        Bundle bundle = new Bundle();
                        bundle.putString(REGISTER_REQUEST, new Gson().toJson(registerUserRequest));
                        bundle.putString(MOBILE, mobileNum);
                        ContactUsRegistrationFragment contactUsRegistrationFragment = ContactUsRegistrationFragment.newInstance(bundle);
                        contactUsRegistrationFragment.show(getSupportFragmentManager(), contactUsRegistrationFragment.getTag());
                    }
                });
    }

    /*public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("test.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            Timber.d("Error : " + ex.toString());
            return null;
        }
        return json;
    }*/
}