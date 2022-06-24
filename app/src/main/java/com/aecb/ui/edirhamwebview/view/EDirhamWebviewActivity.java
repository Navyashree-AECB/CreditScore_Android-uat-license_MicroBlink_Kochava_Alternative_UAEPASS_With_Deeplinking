package com.aecb.ui.edirhamwebview.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.aecb.BuildConfig;
import com.aecb.R;
import com.aecb.base.mvp.BaseActivity;
import com.aecb.databinding.ActivityEDirhamWebviewBinding;
import com.aecb.ui.edirhamwebview.presenter.EDirhamWebviewContract;

import javax.inject.Inject;

import static com.aecb.AppConstants.IntentKey.CHECKOUT_ID;
import static com.aecb.AppConstants.IntentKey.CHECKOUT_URL;
import static com.aecb.util.Utilities.showSSLDialog;

public class EDirhamWebviewActivity extends BaseActivity<EDirhamWebviewContract.View, EDirhamWebviewContract.Presenter>
        implements EDirhamWebviewContract.View {

    @Inject
    EDirhamWebviewContract.Presenter mPresenter;
    ActivityEDirhamWebviewBinding eDirhamWebviewBinding;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        enableBinding();
    }

    private void enableBinding() {
        eDirhamWebviewBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String checkOutUrl = bundle.getString(CHECKOUT_URL);
            String checkoutId = bundle.getString(CHECKOUT_ID);
            getUrlAndLoadUrl(checkOutUrl, checkoutId);
        }
    }

    private void getUrlAndLoadUrl(String url, String checkoutId) {
        eDirhamWebviewBinding.webViewEDirham.getSettings().setJavaScriptEnabled(true);
        eDirhamWebviewBinding.webViewEDirham.getSettings().setBuiltInZoomControls(true);
        eDirhamWebviewBinding.webViewEDirham.getSettings().setDisplayZoomControls(false);
        eDirhamWebviewBinding.webViewEDirham.setWebViewClient(new WvClient());
        eDirhamWebviewBinding.webViewEDirham.loadUrl(url + "?lang=en" + "&checkoutId=" + checkoutId);
    }

    @NonNull
    @Override
    public EDirhamWebviewContract.Presenter createPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_e_dirham_webview;
    }

    @Override
    public void initToolbar() {
    }

    private class WvClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError er) {
            showSSLDialog(mContext, handler, er);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            eDirhamWebviewBinding.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            eDirhamWebviewBinding.progressBar.setVisibility(View.GONE);
            if (BuildConfig.BUILD_TYPE.equalsIgnoreCase("prod")) {
                if (url.contains("https://online.aecb.gov.ae:8000/payment/mobile-edirham")) {
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            } else {
                if (url.contains("https://onlineuat.aecb.gov.ae/payment/mobile-edirham")) {
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (eDirhamWebviewBinding.webViewEDirham.canGoBack()) {
            eDirhamWebviewBinding.webViewEDirham.goBack();
        } else {
            finish();
        }

    }
}
