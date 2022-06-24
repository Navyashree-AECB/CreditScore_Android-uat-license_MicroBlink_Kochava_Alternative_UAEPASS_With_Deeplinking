package com.aecb.ui.termsandcodition.view;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.aecb.R;
import com.aecb.base.mvp.BaseActivity;
import com.aecb.databinding.ActivityTermsAndConditionBinding;
import com.aecb.ui.termsandcodition.presenter.TermsAndConditionContract;

import javax.inject.Inject;

import static com.aecb.AppConstants.WEBVIEW_ENCODING;
import static com.aecb.AppConstants.WEBVIEW_MIME_TYPE;
import static com.aecb.util.Utilities.showSSLDialog;

public class TermsAndConditionActivity extends BaseActivity<TermsAndConditionContract.view,
        TermsAndConditionContract.Presenter> implements TermsAndConditionContract.view, View.OnClickListener {

    @Inject
    TermsAndConditionContract.Presenter mPresenter;
    ActivityTermsAndConditionBinding activityTermsAndConditionBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBinding();
    }

    private void enableBinding() {
        activityTermsAndConditionBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        activityTermsAndConditionBinding.backTermsAndCondition.setOnClickListener(this);
        mPresenter.getTermsConditionUrl();
    }

    @NonNull
    @Override
    public TermsAndConditionContract.Presenter createPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_terms_and_condition;
    }

    @Override
    public void initToolbar() {

    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    @Override
    public void loadTermsUrl(String title, String fieldContentEn) {
        activityTermsAndConditionBinding.webViewTermsAndCondition.setWebViewClient(new myWebClient());
        activityTermsAndConditionBinding.webViewTermsAndCondition.getSettings().setJavaScriptEnabled(true);
        activityTermsAndConditionBinding.webViewTermsAndCondition.getSettings().setBuiltInZoomControls(true);
        activityTermsAndConditionBinding.webViewTermsAndCondition.getSettings().setDisplayZoomControls(false);
        activityTermsAndConditionBinding.webViewTermsAndCondition.loadDataWithBaseURL(null, fieldContentEn, WEBVIEW_MIME_TYPE, WEBVIEW_ENCODING, null);
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            TermsAndConditionActivity.this.showLoading("");
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError er) {
            showSSLDialog(TermsAndConditionActivity.this, handler, er);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            TermsAndConditionActivity.this.hideLoading();
        }
    }

}