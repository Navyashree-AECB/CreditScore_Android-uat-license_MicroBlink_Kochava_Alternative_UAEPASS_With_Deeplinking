package com.aecb.ui.creditreport.faq.view;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;

import com.aecb.R;
import com.aecb.base.mvp.BaseFragment;
import com.aecb.data.api.models.purchasehistory.HistoryItem;
import com.aecb.databinding.FragmentFaqBinding;
import com.aecb.ui.creditreport.faq.presenter.FAQContract;

import java.util.List;

import javax.inject.Inject;

import static com.aecb.AppConstants.IntentKey.API_CONFIGURATION;
import static com.aecb.AppConstants.IntentKey.API_PRODUCTS;
import static com.aecb.AppConstants.IntentKey.CALL_API_TYPE;
import static com.aecb.AppConstants.IntentKey.CREDIT_REPORT;
import static com.aecb.AppConstants.IntentKey.PRODUCT_ID;
import static com.aecb.AppConstants.WEBVIEW_PASSWORD;
import static com.aecb.AppConstants.WEBVIEW_USERNAME;
import static com.aecb.util.Utilities.showSSLDialog;

public class FAQFragment extends BaseFragment<FAQContract.View,
        FAQContract.Presenter> implements FAQContract.View {

    @Inject
    public FAQContract.Presenter mPresenter;
    boolean creditReport;
    boolean isProduct = false;
    private FragmentFaqBinding fragmentFaqBinding;

    public static FAQFragment newInstance(Bundle bundle) {
        FAQFragment fragment = new FAQFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_faq;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            isProduct = true;
            if (bundle.getString(CALL_API_TYPE).equals(API_CONFIGURATION)) {
                creditReport = bundle.getBoolean(CREDIT_REPORT);
                mPresenter.getConfiguration(creditReport);
            } else if (bundle.getString(CALL_API_TYPE).equals(API_PRODUCTS)) {
                String productID = bundle.getString(PRODUCT_ID);
                mPresenter.getProduct(productID);
            }
        } else {
            creditReport = false;
            mPresenter.getConfiguration(creditReport);
        }
    }

    @Override
    public FAQContract.Presenter createPresenter() {
        return mPresenter;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentFaqBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false);
        return fragmentFaqBinding.getRoot();
    }

    @Override
    public LifecycleOwner getLifeCycleOwner() {
        return this;
    }

    @Override
    public void setProductList(List<HistoryItem> history, String currentLanguage) {
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void loadWebView(String loadWebViewUrl) {
        if (isProduct) {
            fragmentFaqBinding.webView.setVisibility(View.GONE);
            fragmentFaqBinding.tvUnderMaintanance.setVisibility(View.VISIBLE);
        } else {
            FAQFragment mContext = new FAQFragment();
            fragmentFaqBinding.webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            fragmentFaqBinding.webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError er) {
                    showSSLDialog(mContext.getActivity(), handler, er);
                }

                @Override
                public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                    handler.proceed(WEBVIEW_USERNAME, WEBVIEW_PASSWORD);
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    mContext.showLoading("");
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    mContext.hideLoading();
                }

            });
            fragmentFaqBinding.webView.getSettings().setJavaScriptEnabled(true);
            fragmentFaqBinding.webView.getSettings().setBuiltInZoomControls(true);
            fragmentFaqBinding.webView.getSettings().setDisplayZoomControls(false);
            fragmentFaqBinding.webView.setVerticalScrollBarEnabled(true);
            fragmentFaqBinding.webView.getSettings().setUseWideViewPort(true);
            fragmentFaqBinding.webView.getSettings().setLoadWithOverviewMode(true);
            fragmentFaqBinding.webView.loadUrl(loadWebViewUrl);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentFaqBinding.webView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        fragmentFaqBinding.webView.onPause();
    }

    public boolean canGoBack() {
        return fragmentFaqBinding.webView.canGoBack();
    }
}