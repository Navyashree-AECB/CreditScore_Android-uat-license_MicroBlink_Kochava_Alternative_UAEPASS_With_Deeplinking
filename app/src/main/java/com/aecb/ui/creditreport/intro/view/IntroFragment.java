package com.aecb.ui.creditreport.intro.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.data.api.models.purchasehistory.HistoryItem;
import com.aecb.databinding.FragmentIntroBinding;
import com.aecb.ui.checkout.view.CheckoutActivity;
import com.aecb.ui.creditreport.creditreport.view.CreditReportFragment;
import com.aecb.ui.creditreport.intro.presenter.IntroContract;

import java.util.List;

import javax.inject.Inject;

import static com.aecb.AppConstants.IntentKey.API_CONFIGURATION;
import static com.aecb.AppConstants.IntentKey.API_PRODUCTS;
import static com.aecb.AppConstants.IntentKey.CALL_API_TYPE;
import static com.aecb.AppConstants.IntentKey.PRODUCT_ID;
import static com.aecb.AppConstants.IntentKey.SELECTED_PRODUCT;
import static com.aecb.AppConstants.WEBVIEW_PASSWORD;
import static com.aecb.AppConstants.WEBVIEW_USERNAME;
import static com.aecb.AppConstants.arabicToDecimal;
import static com.aecb.AppConstants.doubleDecimalValue;
import static com.aecb.util.Utilities.showSSLDialog;
import static com.aecb.util.varibles.StringConstants.SPACE;

public class IntroFragment extends BaseFragment<IntroContract.View, IntroContract.Presenter> implements
        IntroContract.View, View.OnClickListener {

    @Inject
    public IntroContract.Presenter mPresenter;
    ProductsItem selectedProduct;
    private FragmentIntroBinding fragmentIntroBinding;
    private Context mContext;

    public static IntroFragment newInstance(Bundle bundle) {
        IntroFragment fragment = new IntroFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_intro;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        mContext = this.getActivity();
        if (bundle != null) {
            if (bundle.getSerializable(SELECTED_PRODUCT) != null) {
                selectedProduct = (ProductsItem) bundle.getSerializable(SELECTED_PRODUCT);
                double priceWithVAT = (selectedProduct.getPrice() * selectedProduct.getVat()) / 100;
                fragmentIntroBinding.btnBuyProduct.setVisibility(View.GONE);
                fragmentIntroBinding.btnBuyProduct.setText(getString(R.string.get) + SPACE + selectedProduct.getTitleEn() + SPACE +
                        getString(R.string.txt_aed) + SPACE +
                        arabicToDecimal(doubleDecimalValue(2, (selectedProduct.getPrice() + priceWithVAT))));
            } else {
                fragmentIntroBinding.btnBuyProduct.setVisibility(View.GONE);
            }
            if (bundle.getString(CALL_API_TYPE).equals(API_CONFIGURATION)) {
                mPresenter.getConfiguration();
            } else if (bundle.getString(CALL_API_TYPE).equals(API_PRODUCTS)) {
                String productID = bundle.getString(PRODUCT_ID);
                mPresenter.getProduct(productID);
            }
        }
    }

    @Override
    public IntroContract.Presenter createPresenter() {
        return mPresenter;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentIntroBinding = DataBindingUtil.inflate(
                inflater, getLayoutRes(), container, false);
        View view = fragmentIntroBinding.getRoot();
        fragmentIntroBinding.btnBuyProduct.setOnClickListener(this);
        return view;
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
        fragmentIntroBinding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                handler.proceed(WEBVIEW_USERNAME, WEBVIEW_PASSWORD);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError er) {
                showSSLDialog(mContext, handler, er);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                fragmentIntroBinding.progressBar.setVisibility(View.VISIBLE);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                fragmentIntroBinding.progressBar.setVisibility(View.GONE);
            }
        });
        fragmentIntroBinding.webView.getSettings().setJavaScriptEnabled(true);
        fragmentIntroBinding.webView.getSettings().setBuiltInZoomControls(true);
        fragmentIntroBinding.webView.getSettings().setDisplayZoomControls(false);
        fragmentIntroBinding.webView.loadUrl(loadWebViewUrl);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnBuyProduct) {
            Intent intent = new Intent(getActivity(), CheckoutActivity.class);
            intent.putExtra(SELECTED_PRODUCT, selectedProduct);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
            ((CreditReportFragment) getParentFragment()).dismissDialog();
        }
    }

}