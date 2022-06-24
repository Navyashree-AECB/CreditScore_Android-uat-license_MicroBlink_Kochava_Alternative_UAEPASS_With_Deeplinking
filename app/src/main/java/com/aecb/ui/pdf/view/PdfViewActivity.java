package com.aecb.ui.pdf.view;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBindingUtil;

import com.aecb.R;
import com.aecb.base.mvp.BaseActivity;
import com.aecb.databinding.ActivityPdfViewBinding;
import com.aecb.presentation.customviews.ToolbarView;
import com.aecb.ui.pdf.presenter.PdfContract;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;

import javax.inject.Inject;

import static com.aecb.AppConstants.IntentKey.REPORT_DETAILS;
import static com.aecb.AppConstants.IntentKey.REPORT_NAME;


public class PdfViewActivity extends BaseActivity<PdfContract.View, PdfContract.Presenter> implements
        PdfContract.View {

    @Inject
    public PdfContract.Presenter mPresenter;
    ActivityPdfViewBinding activityPdfViewBinding;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    String title = "";
    byte[] theByteArray;
    boolean isVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBinding();
        getBundleValue();
    }

    void displayDialog(String errorMsg) {
        // create an alert builder
        builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.password));
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_pdf_password, null);
        builder.setView(customLayout);
        AppCompatEditText edtPassword = customLayout.findViewById(R.id.edtPassword);
        TextView btnOK = customLayout.findViewById(R.id.btnOK);
        TextView btnCancel = customLayout.findViewById(R.id.btnCancel);
        if (errorMsg != null && !errorMsg.isEmpty()) {
            localValidationError(getString(R.string.error), errorMsg);
        }

        builder.setCancelable(false);
        btnOK.setOnClickListener(v -> {
            if (edtPassword.getText().toString().length() == 12)
                showPDf(edtPassword.getText().toString());
            else {
                localValidationError(getString(R.string.error), getString(R.string.incorrect_password));
            }
        });
//        btnCancel.setOnClickListener(v -> onBackPressed());
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onBackPressed();
            }
        });
        // create and show the alert dialog
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private void showPDf(String password) {
        if (theByteArray != null)
            activityPdfViewBinding.pdfView.fromBytes(theByteArray).password(password).onError(new OnErrorListener() {
                @Override
                public void onError(Throwable t) {
                    dialog.dismiss();
                    displayDialog(getString(R.string.incorrect_password));
                }
            }).onLoad(new OnLoadCompleteListener() {
                @Override
                public void loadComplete(int nbPages) {
                    dialog.dismiss();
                }
            }).load();
    }

    @NonNull
    @Override
    public PdfContract.Presenter createPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_pdf_view;
    }

    @Override
    public void initToolbar() {
        ToolbarView tb = findViewById(R.id.toolBar);
        tb.setCancelBtnListener(this::onBackPressed);
        tb.setBtnVisibility(true, true);
        tb.setToolbarTitle(title);
        tb.setBackBtnListener(this::onBackPressed);
    }

    private void enableBinding() {
        activityPdfViewBinding = DataBindingUtil.setContentView(this, getLayoutRes());
    }

    @Override
    public void displayPDF(byte[] theByteArray) {
        this.theByteArray = theByteArray;
        displayDialog("");
    }

    public void getBundleValue() {
        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            String reportID = intent.getString(REPORT_DETAILS);
            title = intent.getString(REPORT_NAME);
            initToolbar();
            mPresenter.getFile(reportID);
        }
    }

}