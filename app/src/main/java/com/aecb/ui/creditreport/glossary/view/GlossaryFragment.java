package com.aecb.ui.creditreport.glossary.view;


import android.annotation.SuppressLint;
import android.content.Context;
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
import com.aecb.databinding.FragmentGlossaryBinding;
import com.aecb.ui.creditreport.glossary.presenter.GlossaryContract;

import java.util.List;

import javax.inject.Inject;

import static com.aecb.AppConstants.IntentKey.API_CONFIGURATION;
import static com.aecb.AppConstants.IntentKey.API_PRODUCTS;
import static com.aecb.AppConstants.IntentKey.CALL_API_TYPE;
import static com.aecb.AppConstants.IntentKey.PRODUCT_ID;
import static com.aecb.AppConstants.WEBVIEW_PASSWORD;
import static com.aecb.AppConstants.WEBVIEW_USERNAME;
import static com.aecb.util.Utilities.showSSLDialog;

public class GlossaryFragment extends BaseFragment<GlossaryContract.View,
        GlossaryContract.Presenter> implements GlossaryContract.View {

    @Inject
    public GlossaryContract.Presenter mPresenter;
    private FragmentGlossaryBinding fragmentFaqBinding;
    private Context mContext;

    public static GlossaryFragment newInstance(Bundle bundle) {
        GlossaryFragment fragment = new GlossaryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_glossary;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        mContext = this.getActivity();
        if (bundle != null) {
            if (bundle.getString(CALL_API_TYPE).equals(API_CONFIGURATION)) {
                mPresenter.getConfiguration();
            } else if (bundle.getString(CALL_API_TYPE).equals(API_PRODUCTS)) {
                String productID = bundle.getString(PRODUCT_ID);
                mPresenter.getProduct(productID);
            }
        }
    }

    @Override
    public GlossaryContract.Presenter createPresenter() {
        return mPresenter;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentFaqBinding = DataBindingUtil.inflate(
                inflater, getLayoutRes(), container, false);
        View view = fragmentFaqBinding.getRoot();
        return view;
    }

    @Override
    public LifecycleOwner getLifeCycleOwner() {
        return this;
    }

    @Override
    public void setProductList(List<HistoryItem> history, String currentLanguage) {
    }

    @Override
    public void loadWebView(String loadWebViewUrl) {
        fragmentFaqBinding.webView.setWebViewClient(new myWebClient() {
            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                handler.proceed(WEBVIEW_USERNAME, WEBVIEW_PASSWORD);

            }
        });
        fragmentFaqBinding.webView.getSettings().setJavaScriptEnabled(true);
        fragmentFaqBinding.webView.getSettings().setBuiltInZoomControls(true);
        fragmentFaqBinding.webView.getSettings().setDisplayZoomControls(false);
        fragmentFaqBinding.webView.loadUrl(loadWebViewUrl);


    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError er) {
            showSSLDialog(mContext, handler, er);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

}