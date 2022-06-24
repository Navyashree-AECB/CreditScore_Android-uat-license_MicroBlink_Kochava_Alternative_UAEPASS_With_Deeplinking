package com.aecb.ui.aboutus.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.aecb.R;
import com.aecb.base.mvp.BaseActivity;
import com.aecb.databinding.ActivityAboutUsBinding;
import com.aecb.ui.aboutus.presenter.AboutusContract;

import javax.inject.Inject;

import timber.log.Timber;

import static com.aecb.AppConstants.WEBVIEW_PASSWORD;
import static com.aecb.AppConstants.WEBVIEW_USERNAME;
import static com.aecb.util.Utilities.showSSLDialog;

public class AboutUsActivity extends BaseActivity<AboutusContract.View, AboutusContract.Presenter>
        implements AboutusContract.View, View.OnClickListener {

    @Inject
    AboutusContract.Presenter mPresenter;
    ActivityAboutUsBinding aboutUsBinding;
    Context mContext;
    boolean isPrivacy = false;
    boolean showPrivacyPolicyButton = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        enableBinding();
    }

    private void enableBinding() {
        aboutUsBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        mPresenter.getUrl();
        aboutUsBinding.backAboutUs.setOnClickListener(this);
        aboutUsBinding.privacyPolicyAboutusBtn.setOnClickListener(this);
    }

    @NonNull
    @Override
    public AboutusContract.Presenter createPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initToolbar() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_about_us:
                onBackPressed();
                break;
            case R.id.privacy_policy_aboutus_btn:
                loadPrivacy();
                break;
        }
    }

    private void loadPrivacy() {
        this.showLoading("");
        isPrivacy = true;
        showPrivacyPolicyButton = false;
        aboutUsBinding.webViewPrivacyPolicy.setWebViewClient(new MyClient());
        aboutUsBinding.webViewPrivacyPolicy.getSettings().setJavaScriptEnabled(true);
        aboutUsBinding.webViewPrivacyPolicy.getSettings().setBuiltInZoomControls(true);
        aboutUsBinding.webViewPrivacyPolicy.getSettings().setDisplayZoomControls(false);
        aboutUsBinding.webViewPrivacyPolicy.loadUrl(mPresenter.getLanguage());
        aboutUsBinding.privacyPolicyAboutusBtn.setVisibility(View.GONE);
        aboutUsBinding.webViewAboutUs.setVisibility(View.GONE);
        aboutUsBinding.webViewPrivacyPolicy.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void loadWebView(String loadWebViewUrl) {
        this.showLoading("");
        aboutUsBinding.webViewAboutUs.setWebViewClient(new MyClient() {
            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                handler.proceed(WEBVIEW_USERNAME, WEBVIEW_PASSWORD);
            }
        });
        aboutUsBinding.webViewAboutUs.getSettings().setJavaScriptEnabled(true);
        aboutUsBinding.webViewAboutUs.getSettings().setBuiltInZoomControls(true);
        aboutUsBinding.webViewAboutUs.getSettings().setDisplayZoomControls(false);
        aboutUsBinding.webViewAboutUs.loadUrl(loadWebViewUrl);
    }

    @Override
    public void onBackPressed() {
        if (aboutUsBinding.webViewPrivacyPolicy.getVisibility() == View.VISIBLE) {
            if (aboutUsBinding.webViewPrivacyPolicy.canGoBack()) {
                aboutUsBinding.webViewPrivacyPolicy.goBack();
            } else {
                aboutUsBinding.webViewAboutUs.setVisibility(View.VISIBLE);
                aboutUsBinding.webViewPrivacyPolicy.setVisibility(View.GONE);
                aboutUsBinding.privacyPolicyAboutusBtn.setVisibility(View.VISIBLE);
            }
        } else {
            finish();
        }
    }

    private class MyClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String cleanUrl = url;
            if (url.contains("?")) {
                cleanUrl = url.substring(0, url.indexOf("?"));
            }

            if (cleanUrl.endsWith("pdf")) {
                try {
                    Uri uriUrl = Uri.parse(cleanUrl);
                    Intent intentUrl = new Intent(Intent.ACTION_VIEW, uriUrl);
                    startActivity(intentUrl);
                    return true;
                } catch (Exception e) {
                    Timber.e(e.getMessage());
                }
            } else {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            AboutUsActivity.this.hideLoading();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError er) {
            showSSLDialog(mContext, handler, er);
        }
    }

}