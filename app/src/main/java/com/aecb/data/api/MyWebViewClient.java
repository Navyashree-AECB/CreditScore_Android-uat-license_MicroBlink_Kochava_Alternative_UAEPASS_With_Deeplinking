package com.aecb.data.api;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aecb.base.mvp.WebViewInterface;

import timber.log.Timber;

import static com.aecb.util.Utilities.makeCall;
import static com.aecb.util.Utilities.sendEmail;

public class MyWebViewClient extends WebViewClient {

    private WebViewInterface webViewInterface;
    private Context context;

    public MyWebViewClient(WebViewInterface webViewInterface, Context context) {
        this.webViewInterface = webViewInterface;
        this.context = context;
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Timber.e("shouldOverrideUrlLoading url: " + url);
        if (url != null) {
            handleUrl(view, url);
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Timber.e("shouldOverrideUrlLoading N url: " + (request != null ? request.getUrl() : null));
        this.handleUrl(view, String.valueOf(request != null ? request.getUrl() : null));
        return true;
    }

    private void handleUrl(WebView view, String url) {
        String address;
        if (url.startsWith("mailto:")) {
            address = url.substring(url.lastIndexOf("mailto:") + 1);
            Timber.e("add: " + address);
            sendEmail(context, address, "", "");
        } else if (url.startsWith("tel:")) {
            String number = url.substring(url.lastIndexOf("tel:") + 1);
            Timber.e("num: " + number);
            makeCall(context, number);
        } else {
            view.loadUrl(url);
        }

    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Timber.e("onPageStarted " + url);
        webViewInterface.showProgressBar();
        super.onPageStarted(view, url, favicon);
    }

    public void onPageFinished(WebView view, String url) {
        Timber.e("onPageFinished url: " + url);
        this.hideProgressBar();
        super.onPageFinished(view, url);
    }

    @TargetApi(Build.VERSION_CODES.N)
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        hideProgressBar();
        super.onReceivedError(view, request, error);
    }

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        hideProgressBar();
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    private void hideProgressBar() {
        this.webViewInterface.hideProgressbar();
    }

    public final WebViewInterface getWebViewInterface() {
        return this.webViewInterface;
    }

    public final void setWebViewInterface(WebViewInterface webViewInterface) {
        this.webViewInterface = webViewInterface;
    }

    public final Context getContext() {
        return this.context;
    }

    public final void setContext(Context context) {
        this.context = context;
    }
}