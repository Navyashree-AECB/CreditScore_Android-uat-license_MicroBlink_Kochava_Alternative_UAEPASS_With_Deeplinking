package com.aecb.ui.helpandsupport.datacorrection.view;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.aecb.R;
import com.aecb.base.mvp.BaseFragment;
import com.aecb.data.api.models.banklist.BankListItem;
import com.aecb.data.api.models.contactus.ContactUsSubmitRequest;
import com.aecb.databinding.FragmentDataCorrectionBinding;
import com.aecb.ui.helpandsupport.datacorrection.presenter.DataCorrectionContract;
import com.aecb.util.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class DataCorrectionFragment extends BaseFragment<DataCorrectionContract.View, DataCorrectionContract.Presenter>
        implements DataCorrectionContract.View, View.OnClickListener, TextWatcher {

    @Inject
    DataCorrectionContract.Presenter mPresenter;
    private FragmentDataCorrectionBinding dataCorrectionBinding;
    private ArrayList<BankListItem> allBanks = new ArrayList<>();
    private ArrayList<String> bankNames = new ArrayList<>();
    private String selectedBank = "";
    private String selectedBankEmail = "";
    private ContactUsSubmitRequest contactUsSubmitRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        dataCorrectionBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false);
        View view = dataCorrectionBinding.getRoot();
        enableBinding();
        editTextFocusListner();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                mPresenter.loadBankList();
            }
        });
    }

    private void editTextFocusListner() {
        dataCorrectionBinding.edtDataCorrectionSubjectName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                dataCorrectionBinding.txtInputLoutDataCorrectionSubjectName.setHint(getString(R.string.lbl_subject));
            } else if (!Objects.requireNonNull(dataCorrectionBinding.edtDataCorrectionSubjectName.getText()).toString().isEmpty()) {
                dataCorrectionBinding.txtInputLoutDataCorrectionSubjectName.setHint(getString(R.string.lbl_subject));
            } else {
                dataCorrectionBinding.txtInputLoutDataCorrectionSubjectName.setHint(getString(R.string.lbl_subject));
            }
        });
        dataCorrectionBinding.edtDataCorrectionMsg.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                dataCorrectionBinding.txtInputLoutDataCorrectionMsg.setHint(getString(R.string.your_message));
            } else if (!Objects.requireNonNull(dataCorrectionBinding.edtDataCorrectionMsg.getText()).toString().isEmpty()) {
                dataCorrectionBinding.txtInputLoutDataCorrectionMsg.setHint(getString(R.string.your_message));
            } else {
                dataCorrectionBinding.txtInputLoutDataCorrectionMsg.setHint(getString(R.string.your_message));
            }
        });
    }

    private void enableBinding() {
        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        dataCorrectionBinding.edtDataCorrectionSubjectName.addTextChangedListener(this);
        dataCorrectionBinding.edtDataCorrectionMsg.addTextChangedListener(this);
        dataCorrectionBinding.spinnerDataProvider.addTextChangedListener(this);
        dataCorrectionBinding.btnSubmitDataCorrection.setOnClickListener(this);
        contactUsSubmitRequest = new ContactUsSubmitRequest();
        dataCorrectionBinding.edtDataCorrectionSubjectName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                View view = getActivity().getCurrentFocus();
                if (view == null) {
                    view = new View(getActivity());
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                dataCorrectionBinding.spinnerDataProvider.expand();
            }
            return false;
        });
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_data_correction;
    }

    @Override
    public DataCorrectionContract.Presenter createPresenter() {
        return mPresenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmitDataCorrection:
                contactUsSubmitRequest.setDescription(dataCorrectionBinding.edtDataCorrectionMsg.getText().toString());
                contactUsSubmitRequest.setSubject(dataCorrectionBinding.edtDataCorrectionSubjectName.getText().toString());
                contactUsSubmitRequest.setType("contactus");
                mPresenter.submitContactUsQuery(contactUsSubmitRequest);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!Objects.requireNonNull(dataCorrectionBinding.edtDataCorrectionSubjectName.getText()).toString().isEmpty() &&
                !Objects.requireNonNull(dataCorrectionBinding.edtDataCorrectionMsg.getText()).toString().isEmpty() &&
                !dataCorrectionBinding.spinnerDataProvider.getText().toString().isEmpty()) {
            dataCorrectionBinding.btnSubmitDataCorrection.setEnabled(true);
            dataCorrectionBinding.btnSubmitDataCorrection.setBackground(getResources().getDrawable(R.drawable.bg_blue_button));
        } else {
            dataCorrectionBinding.btnSubmitDataCorrection.setEnabled(false);
            dataCorrectionBinding.btnSubmitDataCorrection.setBackground(getResources().getDrawable(R.drawable.btn_disable_state));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void setBankList(List<BankListItem> bankList, String currentLanguage) {
        allBanks.clear();
        allBanks.addAll(bankList);
        bankNames.add(getString(R.string.select_data_provider));
        for (BankListItem bankListItem : allBanks) {
            bankNames.add(bankListItem.getBankName());
        }
        dataCorrectionBinding.spinnerDataProvider.setItems(bankNames);
        dataCorrectionBinding.spinnerDataProvider.setOnItemSelectedListener((view, position, id, item, parent) -> {
            dataCorrectionBinding.edtDataCorrectionMsg.requestFocus();
            if (position != 0) {
                selectedBank = (String) item;
                contactUsSubmitRequest.setBankCode(allBanks.get(position - 1).getBankCode());
                selectedBankEmail = allBanks.get(position - 1).getBankEmail();
            } else {
                contactUsSubmitRequest.setBankCode("");
            }
        });
    }

    @Override
    public void showSelectBankError() {
        showMessage(getString(R.string.please_select_data_provider));
    }

    @Override
    public void showQuerySubjectError() {
        showMessage(getString(R.string.txt_msg_select_query_subject));
    }

    @Override
    public void showQuerySubjectValidError() {
        showMessage(getString(R.string.txt_msg_validate_alphanumeric));
    }

    @Override
    public void showQueryMessagesError() {
        showMessage(getString(R.string.txt_msg_select_query_message));
    }

    @Override
    public void showQueryMessagesValidError() {
        showMessage(getString(R.string.txt_msg_validate_alphanumeric));
    }

    @Override
    public void showMessage(String message) {
        localValidationError(getString(R.string.error), message);
    }

    @Override
    public void resetData() {
        dataCorrectionBinding.edtDataCorrectionSubjectName.setText(null);
        dataCorrectionBinding.edtDataCorrectionMsg.setText(null);
        dataCorrectionBinding.spinnerDataProvider.setItems(new ArrayList<>());
        selectedBank = "";
        contactUsSubmitRequest.setBankCode("");
        dataCorrectionBinding.spinnerDataProvider.setItems(bankNames);
    }

    @Override
    public void openEmail() {
        Utilities.sendEmail(getActivity(), selectedBankEmail, dataCorrectionBinding.edtDataCorrectionSubjectName.getText().toString(),
                dataCorrectionBinding.edtDataCorrectionMsg.getText().toString());
    }
}