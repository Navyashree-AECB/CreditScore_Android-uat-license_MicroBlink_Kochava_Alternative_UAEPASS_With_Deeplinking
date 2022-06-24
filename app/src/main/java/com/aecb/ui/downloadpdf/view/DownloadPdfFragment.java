package com.aecb.ui.downloadpdf.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.aecb.App;
import com.aecb.R;
import com.aecb.base.mvp.BaseFragment;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.data.api.models.purchasehistory.HistoryItem;
import com.aecb.databinding.FragmentDownloadPdfBinding;
import com.aecb.ui.dashboard.view.DashboardActivity;
import com.aecb.ui.downloadpdf.presenter.DownloadPdfContract;
import com.aecb.ui.home.view.HomeFragment;
import com.aecb.ui.pdf.view.PdfViewActivity;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import timber.log.Timber;

import static com.aecb.AppConstants.AppLanguage.ISO_CODE_ARABIC;
import static com.aecb.AppConstants.IntentKey.HISTORY_ITEM;
import static com.aecb.AppConstants.IntentKey.REPORT_DETAILS;
import static com.aecb.AppConstants.IntentKey.REPORT_NAME;
import static com.aecb.AppConstants.ProductIds.REPORT_ID;
import static com.aecb.AppConstants.ProductIds.REPORT_PRODUCT;
import static com.aecb.AppConstants.ProductIds.SCORE_ID;
import static com.aecb.AppConstants.ProductIds.SCORE_PRODUCT;
import static com.aecb.AppConstants.ProductIds.SCORE_WITH_REPORT_ID;
import static com.aecb.AppConstants.ProductIds.SCORE_WITH_REPORT_PRODUCT;
import static com.aecb.AppConstants.ProductIds.isDisplayFalseProduct;
import static com.aecb.util.Utilities.convertServerDateToDisplayFormat2;

public class DownloadPdfFragment extends BaseFragment<DownloadPdfContract.View,
        DownloadPdfContract.Presenter> implements DownloadPdfContract.View, View.OnClickListener {

    @Inject
    public DownloadPdfContract.Presenter mPresenter;
    FragmentDownloadPdfBinding downloadPdfBinding;
    HistoryItem historyItem;

    public static DownloadPdfFragment newInstance(Bundle bundle) {
        DownloadPdfFragment fragment = new DownloadPdfFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        downloadPdfBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false);
        View view = downloadPdfBinding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBundle();
    }

    private void getBundle() {
        if (App.getAppComponent().getDataManager().getCurrentLanguage().equals(ISO_CODE_ARABIC)) {
            downloadPdfBinding.tvResponseId.setGravity(View.FOCUS_RIGHT);
        }
        Bundle bundle = this.getArguments();
        if (bundle != null)
            historyItem = (HistoryItem) bundle.getSerializable(HISTORY_ITEM);
        clickListeners();
        assert historyItem != null;
        try {
            downloadPdfBinding.tvDate.setText(convertServerDateToDisplayFormat2(historyItem.getCreatedon()));
            downloadPdfBinding.tvResponseId.setText(historyItem.getReportNumber());
        } catch (Exception e) {
            Timber.d("Exception : " + e.toString());
        }
    }

    private void clickListeners() {
        downloadPdfBinding.btnDownload.setOnClickListener(this);
        downloadPdfBinding.ivAecbLogo.setOnClickListener(this);
        downloadPdfBinding.ivCancel.setOnClickListener(this);
    }

    @NonNull
    @Override
    public DownloadPdfContract.Presenter createPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_download_pdf;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDownload:
                openPDFActivity();
                break;
            case R.id.ivAecbLogo:
                ((DashboardActivity) getActivity()).loadScreens(new HomeFragment(), 0);
                break;
            case R.id.ivCancel:
                Objects.requireNonNull(getActivity()).onBackPressed();
                break;
        }
    }

    private void openPDFActivity() {
        AtomicReference<String> title = new AtomicReference<>("");
        Intent intent = new Intent(getContext(), PdfViewActivity.class);
        intent.putExtra(REPORT_DETAILS, historyItem.getReportId());
        if (mPresenter.getCurrentLanguage().equalsIgnoreCase(ISO_CODE_ARABIC)){
            if (historyItem.getReportTypeId().equals(SCORE_WITH_REPORT_ID)) {
                title.set(SCORE_WITH_REPORT_PRODUCT.getTitleAr());
            } else if (historyItem.getReportTypeId().equals(REPORT_ID)) {
                title.set(REPORT_PRODUCT.getTitleAr());
            } else if (historyItem.getReportTypeId().equals(SCORE_ID)) {
                title.set(SCORE_PRODUCT.getTitleAr());
            }
            for (ProductsItem productsItem : isDisplayFalseProduct) {
                if (historyItem.getReportTypeId().equals(productsItem.getId())) {
                    title.set(productsItem.getTitleAr());
                }
            }
        }else {
            if (historyItem.getReportTypeId().equals(SCORE_WITH_REPORT_ID)) {
                title.set(SCORE_WITH_REPORT_PRODUCT.getTitleEn());
            } else if (historyItem.getReportTypeId().equals(REPORT_ID)) {
                title.set(REPORT_PRODUCT.getTitleEn());
            } else if (historyItem.getReportTypeId().equals(SCORE_ID)) {
                title.set(SCORE_PRODUCT.getTitleEn());
            }
            for (ProductsItem productsItem : isDisplayFalseProduct) {
                if (historyItem.getReportTypeId().equals(productsItem.getId())) {
                    title.set(productsItem.getTitleEn());
                }
            }
        }
        intent.putExtra(REPORT_NAME, title.get());
        startActivity(intent);
    }
}